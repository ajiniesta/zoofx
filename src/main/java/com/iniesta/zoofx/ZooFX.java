package com.iniesta.zoofx;

import com.iniesta.zoofx.conf.ZFXConf;
import com.iniesta.zoofx.views.ZooFx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ZooFX extends Application {
	
	public static void main(String[] args) {
		ZFXConf.getInstance();
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/Zoofx.fxml"));
		loader.setController(new ZooFx());
		Parent parent = loader.load();
		Scene scene = new Scene(parent);
		stage.setScene(scene);
		stage.setWidth(800);
		stage.setHeight(600);
		stage.setTitle("ZooFx");
		stage.show();
	}
}
