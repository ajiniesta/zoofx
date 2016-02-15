package com.iniesta.zoofx;

import java.util.List;

import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import com.iniesta.zoofx.model.ZNodeFX;
import com.iniesta.zoofx.services.FXWatcher;

public class ZKTests {
	public static void main(String[] args) throws Exception {
		ZooKeeper zk = new ZooKeeper("localhost:2181", 1000, new FXWatcher("1"));
		ZNodeFX parent = getAllZNodes(zk, "/");
		System.out.println(parent);
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
					node.addChildren(getAllZNodes(zk, add(path, kid)));
				}
			}
		}
		return node;
	}

	private static String add(String path, String kid) {
		String interfix = "";
		if(!path.endsWith("/")){
			interfix = "/";
		}
		return path + interfix + kid;
	}
}
