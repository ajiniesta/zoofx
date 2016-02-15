package com.iniesta.zoofx.model;

import org.apache.zookeeper.ZooKeeper;

public class ZookeeperCluster {

	private ZooKeeper zk;
	private String connString;
	
	public ZookeeperCluster(){
		this(null, null);
	}
	
	public ZookeeperCluster(String connString, ZooKeeper zk){
		this.connString = connString;
		this.zk = zk;
	}

	public ZooKeeper getZk() {
		return zk;
	}

	public void setZk(ZooKeeper zk) {
		this.zk = zk;
	}

	public String getConnString() {
		return connString;
	}

	public void setConnString(String connString) {
		this.connString = connString;
	}

	@Override
	public String toString() {
		return "ZookeeperCluster [zk=" + zk + ", connString=" + connString + "]";
	}
	
	
}
