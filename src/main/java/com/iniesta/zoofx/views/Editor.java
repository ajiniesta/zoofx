package com.iniesta.zoofx.views;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.zookeeper.ZooKeeper;

import com.iniesta.zoofx.model.StatItem;
import com.iniesta.zoofx.model.ZNodeFX;
import com.iniesta.zoofx.services.ZNodeLoad;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
    private TableView<?> tableEditor;

    @FXML
    private TableColumn<?, ?> valCol;

    @FXML
    private TableColumn<?, ?> keyCol;

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
    
	private ZNodeFX znode;
    
	private ZooKeeper zk;
	
    public Editor(ZooKeeper zk, ZNodeFX value) {
		this.zk = zk;
		this.znode = value;
	}

	@FXML
    void onSaveAction(ActionEvent event) {

    }

    @FXML
    void onAddKey(ActionEvent event) {

    }

    @FXML
    void onRemoveKey(ActionEvent event) {

    }

    @FXML
    void initialize() throws Exception {
    	statKeyColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItem()));
    	statValueColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue()));
    	addKeyButton.disableProperty().bind(tableTab.selectedProperty().not());
    	removeSelKeyButton.disableProperty().bind(tableTab.selectedProperty().not());
    	final ZNodeLoad loader = new ZNodeLoad(zk, znode);
    	loader.stateProperty().addListener((ChangeListener<State>) (observable, oldValue, newValue) -> {
			if(newValue == State.SUCCEEDED){
				editor.setText(loader.getValue().getContent());
				statTable.setItems(FXCollections.observableArrayList(StatItem.extractStatItem(loader.getValue().getStat())));
			}
		});
    	loader.start();
    }

}
