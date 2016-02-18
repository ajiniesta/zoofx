package com.iniesta.zoofx.views;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;

public class Dialogs {
	public static boolean confirmation(String title, String header, String content) {
		Alert confirmation = new Alert(AlertType.CONFIRMATION);
		confirmation.setTitle(title);
		confirmation.setHeaderText(header);
		confirmation.setContentText(content);
		Optional<ButtonType> result = confirmation.showAndWait();
		return result.get() == ButtonType.OK;		
	}
	
	

	public static void about(){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("About ZooFx");
		String version = "0.1.0";
		String buildTime = "-";
		alert.setHeaderText(String.format("Version: %s - Build time: %s", version, buildTime));
		alert.setContentText("Viewer and Editor of Zookeeper data");
		alert.showAndWait();
	}
	
	public static String getConnection(){
		return askAQuestion("Connect to ZooKeeper server", "Enter a connection string in format: host:port", "Please enter your connection:");
	}

	public static String askAQuestion(String title, String header, String content) {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle(title);
		dialog.setHeaderText(header);
		dialog.setContentText(content);
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
		    return result.get();
		} else {
			return null;
		}
	}
	
	public static void showError(String header, String content){
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
}
