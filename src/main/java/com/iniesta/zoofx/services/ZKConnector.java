package com.iniesta.zoofx.services;

import org.apache.zookeeper.ZooKeeper;

import com.iniesta.zoofx.model.ZookeeperCluster;
import com.iniesta.zoofx.zk.ZookeeperConnection;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ZKConnector extends Service<ZookeeperCluster> {

	private String hostPort;

	public ZKConnector(String hostPort) {
		this.hostPort = hostPort;
	}

	private final class TaskExtension extends Task<ZookeeperCluster> {
		@Override
		protected ZookeeperCluster call() throws Exception {
			ZooKeeper zk = ZookeeperConnection.getInstance().connect(hostPort, 5000);
			return new ZookeeperCluster(hostPort, zk);
		}
	}

	@Override
	protected Task<ZookeeperCluster> createTask() {
		return new TaskExtension();
	}

}
