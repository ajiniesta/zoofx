package com.iniesta.zoofx;

import org.apache.zookeeper.ZooKeeper;

import com.iniesta.zoofx.model.ZookeeperCluster;
import com.iniesta.zoofx.views.Viewer;
import com.iniesta.zoofx.zk.ZookeeperConnection;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Unit test for simple App.
 */
public class ViewerLauncher extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		ZooKeeper zk = ZookeeperConnection.getInstance().connect("localhost:2181", 5000);
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/Viewer.fxml"));
		loader.setController(new Viewer(new ZookeeperCluster("localhost:2181", zk), null));
		Parent parent = loader.load();
		Scene scene = new Scene(parent);
		stage.setScene(scene);
		stage.show();
	}
}
