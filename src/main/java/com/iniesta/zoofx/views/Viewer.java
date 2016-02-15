package com.iniesta.zoofx.views;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.iniesta.zoofx.model.ZNodeFX;
import com.iniesta.zoofx.model.ZookeeperCluster;
import com.iniesta.zoofx.services.ZNodeExplorer;

import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeView;

public class Viewer {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TreeView<ZNodeFX> treeNodes;

    @FXML
    private TabPane znodes;

	private ZookeeperCluster zkc;
	
	public Viewer(ZookeeperCluster zkc){
		this.zkc = zkc;
	}

    @FXML
    void initialize() throws IOException, InterruptedException {
    	treeNodes.setCellFactory(param -> new ZNodeCellFactory(zkc.getZk(), znodes));    	
		final ZNodeExplorer explorer = new ZNodeExplorer(zkc.getZk());
		treeNodes.rootProperty().bind(explorer.valueProperty());
		explorer.start();
		
    }
    
}