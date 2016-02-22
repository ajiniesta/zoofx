package com.iniesta.zoofx.services;

import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import com.iniesta.zoofx.model.ZNodeFX;
import com.iniesta.zoofx.model.ZNodeFXContent;

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
			Stat oldStat = znode.getStat();
			int version = (oldStat!=null)?(oldStat.getVersion()+1):0;
			Stat stat = zk.setData(znode.getName(), content.getBytes(), version);
			return new ZNodeFXContent(content, stat);
		}
	}

	@Override
	protected Task<ZNodeFXContent> createTask() {
		return new TaskExtension();
	}

}
