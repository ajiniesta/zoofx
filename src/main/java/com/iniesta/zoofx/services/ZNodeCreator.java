package com.iniesta.zoofx.services;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import com.iniesta.zoofx.model.ZNodeFX;

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
			ZNodeFX newone = new ZNodeFX();
			String path = ZNodeFX.addPath(parent.getName(), znodeName);
			try{
				zk.create(path , "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				newone.setName(znodeName);
			}catch(Exception e){
				newone = null;
				setException(e);
			}
			return newone;
		}
	}

	

	@Override
	protected Task<ZNodeFX> createTask() {
		return new TaskExtension();
	}

}
