package com.iniesta.zoofx.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.iniesta.zoofx.model.ZookeeperCluster;

import javafx.application.Platform;
import javafx.util.Callback;

public class ZFXConf {
	
	public final static String CONNECTION = "connection";
	
	public final static String TIMEOUT = "timeout";

	private final static String LAST = "connections.lasts";
	
	private final static int MAX_LASTS = 4;
	
	private static ZFXConf instance;

	private List<Callback<List<String>, Void>> listeners;
	
	private ZFXConf(){
		getConf();
		listeners = new ArrayList<>();
	}
	
	public static synchronized ZFXConf getInstance(){
		if(instance==null){
			instance = new ZFXConf();
		}
		return instance;
	}
	
	public List<String> getLastConnections(){
		Properties conf = getConf();
		String lasts = conf.getProperty(LAST);
		return extractConnections(lasts);
	}

	public void addSuccessfulConnection(ZookeeperCluster zk){
		Properties conf = getConf();
		String lasts = conf.getProperty(LAST);
		String newLasts = addSuccess(lasts, zk.getConnString());
		conf.setProperty(LAST, newLasts);
		savePropsIntoConfFile(getConfFile(), conf, "Updating last successful");
		notifyToListeners();
	}
	
	protected String addSuccess(String lasts, String lastConn) {
		List<String> current = extractConnections(lasts);
		if(current.size()<MAX_LASTS){
			if(!current.contains(lastConn)){
				current.add(lastConn);				
			}			
		}else{
			if(current.contains(lastConn)){
				current.remove(lastConn);				
			}else{
				current.remove(0);
			}
			current.add(lastConn);
		}
		return flatConns(current);
	}

	private String flatConns(List<String> conns){
		String flat = "";
		for (int i= 0; i < conns.size(); i++) {
			String v = conns.get(i);
			flat += v + ((i<conns.size()-1)?"|":"");
		}
		return flat;
	}
	
	protected List<String> extractConnections(String lasts) {
		List<String> conns = new ArrayList<>();
		if(lasts!=null){
			String connections[] = lasts.split("\\|");
			for (String c : connections) {
				if(c!=null && !c.isEmpty()){
					conns.add(c);
				}
			}
		}
		return conns;
	}

	private Properties getConf() {
		String confFile = getConfFile();
		Properties conf = new Properties();
		try {			
			conf.load(new FileInputStream(new File(confFile)));
		} catch (IOException e) {
			savePropsIntoConfFile(confFile, conf, "Initial generated file");
		}
		return conf;
	}

	private void savePropsIntoConfFile(String confFile, Properties conf, String comment) {
		try {
			conf.store(new FileOutputStream(new File(confFile)), comment);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private String getConfFile() {
		String userHome = System.getProperty("user.home");
		String confHome = userHome + System.getProperty("file.separator") + ".zoofx";
		File confDir = new File(confHome);
		if(!confDir.exists()){
			confDir.mkdirs();
		}
		String confFile = confHome + System.getProperty("file.separator") + "zoofx.properties";
		return confFile;
	}

	public void addListener(Callback<List<String>, Void> callback) {
		listeners.add(callback);
	}
	
	private void notifyToListeners(){
		if(Platform.isFxApplicationThread()){
			innerNotifyToListeners();
		}else{
			Platform.runLater(() -> innerNotifyToListeners());
		}
	}

	private void innerNotifyToListeners() {
		for (Callback<List<String>, Void> callback : listeners) {
			callback.call(getLastConnections());
		}
	}
	
	private Properties getBuildProperties(){
		Properties props = new Properties();
		try {
			props.load(getClass().getClassLoader().getResourceAsStream("build.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return props;
	}
	
	public String getVersion(){
		return getBuildProperties().getProperty("version","");
	}
	
	public String getBuildDate(){
		return getBuildProperties().getProperty("build.date","");
	}
}
