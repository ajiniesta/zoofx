package com.iniesta.zoofx.services;

import org.apache.zookeeper.ZooKeeper;

import com.iniesta.zoofx.model.ZNodeFX;
import com.iniesta.zoofx.model.ZNodeFXContent;
import com.iniesta.zoofx.zk.ZookeeperDao;

import javafx.concurrent.Task;

public class ZNodeSaver extends ThrowableService<ZNodeFXContent> {

	private ZooKeeper zk;
	private ZNodeFX znode;
	public String content;

	public ZNodeSaver(ZooKeeper zk, ZNodeFX znode, String content) {
		this.zk = zk;
		this.znode = znode;
		this.content = content;
	}

	private final class TaskExtension extends Task<ZNodeFXContent> {
		@Override
		protected ZNodeFXContent call() throws Exception {
			ZookeeperDao zkDao = new ZookeeperDao();
			return zkDao.saveZNode(zk, znode, content);
		}
	}

	@Override
	protected Task<ZNodeFXContent> createTask() {
		return new TaskExtension();
	}

}
