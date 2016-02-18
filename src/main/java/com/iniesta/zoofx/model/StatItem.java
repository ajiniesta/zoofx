package com.iniesta.zoofx.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.zookeeper.data.Stat;

public class StatItem {

	private String item;
	private String value;

	public StatItem(String item, String value) {
		super();
		this.item = item;
		this.value = value;
	}

	public StatItem() {
		super();
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "StatItem [item=" + item + ", value=" + value + "]";
	}

	public static List<StatItem> extractStatItem(Stat stat) {
		List<StatItem> items = new ArrayList<>();
		if(stat!=null){
			items.add(new StatItem("cZxid",String.valueOf(stat.getCzxid())));
			items.add(new StatItem("ctime",new Date(stat.getCtime()).toString()));
			items.add(new StatItem("mZxid",String.valueOf(stat.getMzxid())));
			items.add(new StatItem("mtime",new Date(stat.getMtime()).toString()));
			items.add(new StatItem("pZxid",String.valueOf(stat.getPzxid())));
			items.add(new StatItem("cversion",String.valueOf(stat.getCversion())));
			items.add(new StatItem("dataversion	",String.valueOf(stat.getVersion())));
			items.add(new StatItem("aclversion",String.valueOf(stat.getAversion())));
			items.add(new StatItem("ephemeralOwner",String.valueOf(stat.getEphemeralOwner())));
			items.add(new StatItem("datalength",String.valueOf(stat.getDataLength())));
			items.add(new StatItem("numChildren",String.valueOf(stat.getNumChildren())));
		}
		return items;
	}

}
