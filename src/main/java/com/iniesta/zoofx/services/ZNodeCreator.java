package com.iniesta.zoofx.services;

import org.apache.zookeeper.ZooKeeper;

import com.iniesta.zoofx.model.ZNodeFX;
import com.iniesta.zoofx.zk.ZookeeperDao;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ZNodeCreator extends Service<ZNodeFX> {

	private ZooKeeper zk;
	private ZNodeFX parent;
	private String znodeName;

	public ZNodeCreator(ZooKeeper zk, ZNodeFX parent, String znodeName) {
		this.zk = zk;
		this.parent = parent;
		this.znodeName = znodeName;
	}

	private final class TaskExtension extends Task<ZNodeFX> {
		@Override
		protected ZNodeFX call() throws Exception {
			ZookeeperDao zkDao = new ZookeeperDao();
			return zkDao.createEmptyZnode(zk, parent, znodeName);
		}
	}

	@Override
	protected Task<ZNodeFX> createTask() {
		return new TaskExtension();
	}

}
