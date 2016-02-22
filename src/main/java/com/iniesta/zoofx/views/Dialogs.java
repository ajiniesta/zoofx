package com.iniesta.zoofx.views;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

import com.iniesta.zoofx.conf.ZFXConf;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

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
		ZFXConf conf = ZFXConf.getInstance();
		String version = conf.getVersion();
		String buildTime = conf.getBuildDate();
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



	public static void showException(Throwable ex) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("An exception occured");
		alert.setHeaderText("");
		alert.setContentText(ex.getLocalizedMessage());

		// Create expandable Exception.
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		String exceptionText = sw.toString();

		Label label = new Label("The exception stacktrace was:");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		// Set expandable Exception into the dialog pane.
		alert.getDialogPane().setExpandableContent(expContent);

		alert.showAndWait();		
	}
}
