package com.iniesta.zoofx.services;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

public class FXWatcher implements Watcher {

	private String discriminator;

	public FXWatcher(String discriminator) {
		this.discriminator = discriminator;
	}

	@Override
	public void process(WatchedEvent event) {
		System.out.println("Event("+discriminator+"): " + event);
		
	}

}
