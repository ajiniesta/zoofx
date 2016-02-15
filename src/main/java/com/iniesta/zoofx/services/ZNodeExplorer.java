package com.iniesta.zoofx.services;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import com.iniesta.zoofx.conf.ZFXConf;
import com.iniesta.zoofx.model.ZNodeFX;
import com.iniesta.zoofx.zk.ZookeeperConnection;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.TreeItem;

public class ZNodeExplorer extends Service<TreeItem<ZNodeFX>> {

	private ZooKeeper zk;

	public ZNodeExplorer(ZooKeeper zk) throws IOException, InterruptedException {
		this.zk = zk;
		 
	}

	@Override
	protected Task<TreeItem<ZNodeFX>> createTask() {
		return new TaskExtension();
	}

	private final class TaskExtension extends Task<TreeItem<ZNodeFX>> {
		@Override
		protected TreeItem<ZNodeFX> call() throws Exception {
			
			ZNodeFX parentZNode = getAllZNodes(zk, "/");
			TreeItem<ZNodeFX> parent = convertToTreeItem(parentZNode);

			return parent;
		}
	}
	
	private static ZNodeFX getAllZNodes(ZooKeeper zk, String path) throws Exception {
		ZNodeFX node = new ZNodeFX();
		Stat stat = zk.exists(path, true);
		if (stat != null) {
			node.setName(path);
			node.setStat(stat);
			List<String> children = zk.getChildren(path, new FXWatcher("t1"));
			if (children != null && !children.isEmpty()) {
				for (String kid : children) {
					node.addChildren(getAllZNodes(zk, ZNodeFX.addPath(path, kid)));
				}
			}
		}
		return node;
	}

	public TreeItem<ZNodeFX> convertToTreeItem(ZNodeFX znode) {
		TreeItem<ZNodeFX> node = new TreeItem<ZNodeFX>(znode);
		if(znode.getChildren()!=null){
			for (ZNodeFX kid : znode.getChildren()) {
				node.getChildren().add(convertToTreeItem(kid));
			}
		}
		node.getValue().setChildren(null);
		return node;
	}

}
