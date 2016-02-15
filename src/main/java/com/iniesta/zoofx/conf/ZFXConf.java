package com.iniesta.zoofx.conf;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ZFXConf {
	
	public final static String CONNECTION = "connection";
	
	public final static String TIMEOUT = "timeout";
	
	public List<String> getLastConnections(){
		String userHome = System.getProperty("user.home");
		String confHome = userHome + System.getProperty("file.separator") + ".zoofx";
		File confDir = new File(confHome);
		if(!confDir.exists()){
			confDir.mkdirs();
		}
		String confFile = confHome + System.getProperty("file.separator") + "zoofx.latest";
		return new ArrayList<>();
	}
	
}
