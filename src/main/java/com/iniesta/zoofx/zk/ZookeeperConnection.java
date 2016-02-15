package com.iniesta.zoofx.zk;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;

public class ZookeeperConnection {

	private Map<String, ZooKeeper> connections = new HashMap<>();

	private static ZookeeperConnection instance;
	
	private ZookeeperConnection(){
		
	}
	
	public synchronized static ZookeeperConnection getInstance(){
		if(instance == null){
			instance = new ZookeeperConnection();
		}
		return instance;
	}
	
	public ZooKeeper createZooKeeper(String host, int timeout) throws IOException, InterruptedException {
		final CountDownLatch connectedSignal = new CountDownLatch(1);
		ZooKeeper zoo = new ZooKeeper(host, timeout, new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				if (event.getState() == KeeperState.SyncConnected) {
					connectedSignal.countDown();
				}
			}
		});
		connectedSignal.await();
		return zoo;
	}
	
	public ZooKeeper connect(String host, int timeout) throws IOException, InterruptedException{
		ZooKeeper zoo = null;
		if((zoo = connections.get(host))==null){
			zoo = createZooKeeper(host, timeout);
		}
		return zoo;
	}
	
	public void close(String host) throws InterruptedException{
		ZooKeeper zoo = null;
		if((zoo = connections.get(host))!=null){
			close(zoo);
		}
	}

	private void close(ZooKeeper zoo) throws InterruptedException {
		connections.remove(zoo);
		if(zoo!=null){
			zoo.close();	
		}
	}
}
