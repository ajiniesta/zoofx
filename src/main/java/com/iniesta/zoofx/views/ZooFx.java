package com.iniesta.zoofx.views;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.iniesta.zoofx.conf.ZFXConf;
import com.iniesta.zoofx.model.ZookeeperCluster;
import com.iniesta.zoofx.services.ZKConnector;
import com.iniesta.zoofx.zk.ZookeeperConnection;

import javafx.beans.value.ChangeListener;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.util.Callback;

public class ZooFx {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TabPane tabs;

	@FXML
	private ProgressBar progress;

	@FXML
	private Menu recentMenu;

	@FXML
	void onConnect(ActionEvent event) {
		final String conn = Dialogs.getConnection();
		connectToZK(conn);
	}

	private void connectToZK(final String conn) {
		final ZKConnector connector = new ZKConnector(conn);
		progress.progressProperty().bind(connector.progressProperty());
		progress.visibleProperty().bind(connector.runningProperty());
		connector.stateProperty().addListener((ChangeListener<State>) (observable, oldValue, newValue) -> {
			switch (newValue) {
			case FAILED:
				Dialogs.showError("Wrong ZooKeeper connection", conn);
				break;
			case SUCCEEDED:
				createTab(connector.getValue());
				ZFXConf.getInstance().addSuccessfulConnection(connector.getValue());
				break;
			default:
				break;
			}
		});
		connector.start();
	}

	private void createTab(final ZookeeperCluster zkc) {
		Tab tab = new Tab(zkc.getConnString());
		tabs.getTabs().add(tab);
		tabs.getSelectionModel().select(tab);
		tab.setOnCloseRequest(event -> {
			try {
				ZookeeperConnection.getInstance().close(zkc.getConnString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/Viewer.fxml"));
		try {
			loader.setController(new Viewer(zkc, new ServiceWorker(progress)));
			Parent parent = loader.load();
			tab.setContent(parent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void onAbout(ActionEvent event) {
		Dialogs.about();
	}
	
    @FXML
    void onExit(ActionEvent event) {
    	System.exit(0);
    }

	@FXML
	void initialize() {
		ZFXConf.getInstance().addListener(new Callback<List<String>, Void>() {
			@Override
			public Void call(List<String> param) {
				fillMenus(param);
				return null;
			}
		});
		fillMenus(ZFXConf.getInstance().getLastConnections());
	}

	protected void fillMenus(List<String> param) {
		for (String conn : param) {
			recentMenu.getItems().add(createRecentMenuItem(conn));
		}
	}

	private MenuItem createRecentMenuItem(String conn) {
		MenuItem menuItem = new MenuItem(conn);
		menuItem.setOnAction(event -> {
			connectToZK(conn);
		});
		return menuItem;
	}
}
