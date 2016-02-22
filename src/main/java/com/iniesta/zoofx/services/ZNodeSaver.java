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
//			try {
				Stat stat = zk.setData(znode.getName(), content.getBytes(), znode.getStat().getVersion());
				return new ZNodeFXContent(content, stat);
//			} catch (Exception e) {
//				setException(e);
//				return null;
//			}
		}
	}

	@Override
	protected Task<ZNodeFXContent> createTask() {
		return new TaskExtension();
	}

}
