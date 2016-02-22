package com.iniesta.zoofx.zk;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import com.iniesta.zoofx.model.ZNodeFX;
import com.iniesta.zoofx.model.ZNodeFXContent;

public class ZookeeperDao {

	public ZNodeFX createEmptyZnode(ZooKeeper zk, ZNodeFX parent, String znodeName) throws KeeperException, InterruptedException{
		ZNodeFX newone = new ZNodeFX();
		String path = ZNodeFX.addPath(parent.getName(), znodeName);
		zk.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		newone.setName(path);
		return newone;
	}
	
	public ZNodeFXContent saveZNode(ZooKeeper zk, ZNodeFX znode, String content) throws KeeperException, InterruptedException{
		Stat oldStat = znode.getStat();
		int version = (oldStat!=null)?(oldStat.getVersion()):0;
		Stat stat = zk.setData(znode.getName(), content.getBytes(), version);
		return new ZNodeFXContent(content, stat);
	}
	
	public Boolean removeZNode(ZooKeeper zk, ZNodeFX znode) throws InterruptedException, KeeperException{
		Boolean ok = false;
		zk.delete(znode.getName(), -1);
		ok = true;
		return ok;
	}
}
