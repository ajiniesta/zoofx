package com.iniesta.zoofx.services;

import org.apache.zookeeper.ZooKeeper;

import com.iniesta.zoofx.model.ZNodeFX;
import com.iniesta.zoofx.model.ZNodeFXContent;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ZNodeLoad extends Service<ZNodeFXContent> {

	private ZooKeeper zk;
	private ZNodeFX znode;

	public ZNodeLoad(ZooKeeper zk, ZNodeFX znode){
		this.zk = zk;
		this.znode = znode;
	}
	
	private final class TaskExtension extends Task<ZNodeFXContent> {
		@Override
		protected ZNodeFXContent call() throws Exception {
			byte[] data = zk.getData(znode.getName(), false, znode.getStat());
			return new ZNodeFXContent(data, znode.getStat());
		}
	}

	@Override
	protected Task<ZNodeFXContent> createTask() {
		return new TaskExtension();
	}

}
