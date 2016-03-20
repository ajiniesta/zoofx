package com.iniesta.zoofx.views;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.zookeeper.ZooKeeper;

import com.iniesta.zoofx.model.Entry;
import com.iniesta.zoofx.model.StatItem;
import com.iniesta.zoofx.model.ZNodeFX;
import com.iniesta.zoofx.model.ZNodeFXContent;
import com.iniesta.zoofx.services.ZNodeLoad;
import com.iniesta.zoofx.services.ZNodeSaver;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;

public class Editor {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private VBox parent;

	@FXML
	private TextArea editor;

	@FXML
	private Tab textTab;

	@FXML
	private Tab tableTab;

	@FXML
	private TableView<Entry> tableEditor;

	@FXML
	private TableColumn<Entry, String> valCol;

	@FXML
	private TableColumn<Entry, String> keyCol;

	@FXML
	private TextField znodeName;

	@FXML
	private Button addKeyButton;

	@FXML
	private Button removeSelKeyButton;

	@FXML
	private TableView<StatItem> statTable;

	@FXML
	private TableColumn<StatItem, String> statKeyColumn;

	@FXML
	private TableColumn<StatItem, String> statValueColumn;

	@FXML
	private TextField filterField;

	private ZNodeFX znode;

	private ZooKeeper zk;

	private ServiceWorker serviceWorker;

	private String editorText;

	public enum Operation {
		DELETE, UPDATE_VALUE, UPDATE_KEY, ADD
	}

	public Editor(ZooKeeper zk, ZNodeFX value, ServiceWorker serviceWorker) {
		this.zk = zk;
		this.znode = value;
		this.serviceWorker = serviceWorker;
	}

	@FXML
	void onSaveAction(ActionEvent event) {
		final ZNodeSaver saver = new ZNodeSaver(zk, znode, editor.getText());
		serviceWorker.bind(saver);
		saver.stateProperty().addListener((ChangeListener<State>) (observable, oldValue, newValue) -> {
			if (newValue == State.SUCCEEDED) {
				setContent(saver);
			}
		});
		saver.start();
	}

	@FXML
	void onAddKey(ActionEvent event) {
		Entry newEntry = new Entry("edit me", "edit me");
		tableEditor.itemsProperty().unbind();
		tableEditor.getItems().add(newEntry);
		String newContent = updateTextArea(newEntry);
		setEditorText(newContent);
	}

	private void setEditorText(String newContent) {
		editor.textProperty().unbind();
		editor.setText(newContent);
		editorText = newContent;
	}

	@FXML
	void onRemoveKey(ActionEvent event) {
		Entry selectedItem = tableEditor.getSelectionModel().getSelectedItem();
		int selectedIndex = tableEditor.getSelectionModel().getSelectedIndex();
		tableEditor.getItems().remove(selectedItem);
		String newContent = updateTextArea(selectedItem, selectedIndex, Operation.DELETE);
		setEditorText(newContent);
	}

	@FXML
	void onTabChanged(Event event) {
		setTableContent(editor.getText());
	}

	@FXML
	void onFilterTyped(Event event) {

	}

	@FXML
	void initialize() throws Exception {
		statKeyColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItem()));
		statValueColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue()));
		keyCol.setCellValueFactory(new PropertyValueFactory<Entry, String>("key"));
		keyCol.setCellFactory(TextFieldTableCell.forTableColumn());
		keyCol.setOnEditCommit(new EventHandler<CellEditEvent<Entry, String>>() {
			@Override
			public void handle(CellEditEvent<Entry, String> event) {
				event.getTableView().getItems().get(event.getTablePosition().getRow()).setKey(event.getNewValue());
				String newContent = updateTextArea(
						event.getTableView().getItems().get(event.getTablePosition().getRow()), event.getTablePosition().getRow(), Operation.UPDATE_KEY);
				setEditorText(newContent);
			}
		});
		valCol.setCellValueFactory(new PropertyValueFactory<Entry, String>("value"));
		valCol.setCellFactory(TextFieldTableCell.forTableColumn());
		valCol.setOnEditCommit(new EventHandler<CellEditEvent<Entry, String>>() {
			@Override
			public void handle(CellEditEvent<Entry, String> event) {
				event.getTableView().getItems().get(event.getTablePosition().getRow()).setValue(event.getNewValue());
				String newContent = updateTextArea(
						event.getTableView().getItems().get(event.getTablePosition().getRow()),
						event.getTablePosition().getRow(), Operation.UPDATE_VALUE);
				setEditorText(newContent);
			}
		});
		addKeyButton.disableProperty().bind(tableTab.selectedProperty().not());
		removeSelKeyButton.disableProperty().bind(tableTab.selectedProperty().not());
		final ZNodeLoad loader = new ZNodeLoad(zk, znode);
		serviceWorker.bind(loader);
		loader.stateProperty().addListener((ChangeListener<State>) (observable, oldValue, newValue) -> {
			if (newValue == State.SUCCEEDED) {
				setContent(loader);
			}
		});
		loader.start();
	}

	protected String updateTextArea(Entry entry){
		return updateTextArea(entry, 0, Operation.ADD);
	}
			
	protected String updateTextArea(Entry entry, int index, Operation op) {
		String newContent = "";
		String text = editor.getText();
		String[] lines = text.split("\n");
		if (op == Operation.ADD) {
			for (String line : lines) {
				newContent += line + "\n";
			}
			newContent += entry.getKey() + "=" + entry.getValue() + "\n";
		} else {
			for (int i=0; i<lines.length; i++) {
				String line = lines[i];
				String[] fields = line.split("=");
				if (fields.length > 1) {
					if (i==index) {
						if (op == Operation.UPDATE_VALUE) {
							newContent += fields[0] + "=" + entry.getValue() + "\n";
						} else if (op == Operation.UPDATE_KEY) {
							newContent += entry.getKey() + "=" + fields[1] + "\n";
						}
					} else {
						newContent += line + "\n";
					}
				}
			}
		}
		return newContent;
	}

	private void setContent(final Service<? extends ZNodeFXContent> service) {
		znode.setStat(service.getValue().getStat());
		setEditorText(service.getValue().getContent());
		setTableContent(service.getValue().getContent());
		statTable.setItems(FXCollections.observableArrayList(StatItem.extractStatItem(service.getValue().getStat())));
	}

	private void setTableContent(String content) {
		String[] lines = content.split("\n");
		tableEditor.getItems().clear();
		for (String line : lines) {
			String[] fields = line.split("=");
			if (fields.length > 1) {
				Entry entry = new Entry(fields[0].trim(), fields[1].trim());
				tableEditor.getItems().add(entry);
			}
		}
	}

}
