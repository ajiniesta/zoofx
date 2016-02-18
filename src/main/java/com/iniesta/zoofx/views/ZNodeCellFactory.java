package com.iniesta.zoofx.views;

import org.apache.zookeeper.ZooKeeper;

import com.iniesta.zoofx.model.ZNodeFX;
import com.iniesta.zoofx.services.ZKRemover;

import javafx.beans.value.ChangeListener;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;

public class ZNodeCellFactory extends TreeCell<ZNodeFX> {

	private TextField textField;
	private ContextMenu allMenu = new ContextMenu();
	// private ContextMenu rootMenu = new ContextMenu();

	public ZNodeCellFactory(final ZooKeeper zk, final TabPane znodes) {
		MenuItem createMenuItem = new MenuItem("Create ZNode");
		createMenuItem.setOnAction(event -> {
			ZNodeFX znode = new ZNodeFX();
			znode.setName("NewZNode");
			TreeItem<ZNodeFX> item = new TreeItem<>(znode);
			getTreeItem().getChildren().add(item);			
		});
				
		MenuItem removeMenuItem = new MenuItem("Remove ZNode");
		removeMenuItem.setOnAction(event -> {
			boolean ok = Dialogs.confirmation("Remove ZNode", "You are going to remove a ZNode", "Are you sure you want to continue?");
			if(ok){
				removeItem(zk, getTreeView(), getTreeItem());
			}
		});
		
		MenuItem getMenuItem = new MenuItem("Get ZNode");
		getMenuItem.setOnAction(event -> {
			String name = getTreeItem().getValue().getName();
			Tab tab = new Tab(name);
			znodes.getTabs().add(tab);
			znodes.getSelectionModel().select(tab);
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/Editor.fxml"));
			try {
				loader.setController(new Editor(zk, getTreeItem().getValue()));
				Parent parent = loader.load();
				tab.setContent(parent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		allMenu.getItems().addAll(getMenuItem, removeMenuItem);
		// rootMenu.getItems().addAll(createMenuItem, getMenuItem);

	}

	private void removeItem(ZooKeeper zk, TreeView<ZNodeFX> treeView, TreeItem<ZNodeFX> item) {
		TreeItem<ZNodeFX> parent = item.getParent();
		if(parent!=null){
			final ZKRemover remover = new ZKRemover(zk, item.getValue());
			remover.stateProperty().addListener((ChangeListener<State>) (observable, oldValue, newValue) -> {
				switch (newValue) {
				case SUCCEEDED:
					if(remover.getValue()){
						parent.getChildren().remove(item);
					}
					break;
				case FAILED:
					Dialogs.showError("Error removing item " + item.getValue().getName(), "It is forbidden to remove the root item");
				default:
					break;
				}
			});
			remover.start();
		}else{
			Dialogs.showError("Error removing item " + item.getValue().getName(), "It is forbidden to remove the root item");
		}
	}

	@Override
    public void startEdit() {
        super.startEdit();

        if (textField == null) {
            createTextField();
        }
        setText(null);
        setGraphic(textField);
        textField.selectAll();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getItem().getName());
        setGraphic(getTreeItem().getGraphic());
    }

	
	@Override
	public void updateItem(ZNodeFX item, boolean empty) {
		super.updateItem(item, empty);
		setGraphic(null);

		if (!empty) {
			setText(item.getNameInTree());
			// if(item.isRoot()){
			// setContextMenu(rootMenu);
			// }else{
			setContextMenu(allMenu);
			// }
		} else {
			setText(null);
			setContextMenu(null);
		}
	}

    private void createTextField() {
        textField = new TextField(getString());
        textField.setOnKeyReleased(keyEvent -> {
		    if (keyEvent.getCode() == KeyCode.ENTER) {
		        commitEdit(createZNode(textField.getText()));
		    } else if (keyEvent.getCode() == KeyCode.ESCAPE) {
		        cancelEdit();
		    }
		});  
    }

    private ZNodeFX createZNode(String text) {
    	ZNodeFX znode = new ZNodeFX();
    	znode.setName(text);
		return znode;
	}
    
    private String getString() {
        return getItem() == null ? "" : getItem().toString();
    }
}
