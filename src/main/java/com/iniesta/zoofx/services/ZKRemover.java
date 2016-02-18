package com.iniesta.zoofx.services;

import org.apache.zookeeper.ZooKeeper;

import com.iniesta.zoofx.model.ZNodeFX;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ZKRemover extends Service<Boolean> {

	private ZNodeFX znode;
	private ZooKeeper zk;

	public ZKRemover(ZooKeeper zk, ZNodeFX znode) {
		this.zk = zk;
		this.znode = znode;
	}

	private final class TaskExtension extends Task<Boolean> {
		@Override
		protected Boolean call() throws Exception {
			Boolean ok = false;
			try {
				zk.delete(znode.getName(), -1);
				ok = true;
			} catch (Exception e) {
				ok = false;
				setException(e);
			}
			return ok;
		}
	}

	@Override
	protected Task<Boolean> createTask() {
		return new TaskExtension();
	}

}
