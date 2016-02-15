package com.iniesta.zoofx.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.data.Stat;

public class ZNodeFX {

	private String name;
	private Stat stat;
	private List<ZNodeFX> children;
	
	public ZNodeFX(){
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameInTree() {
		String p = name;
		if(name!=null && !name.endsWith("/")){
			String[] split = name.split("/");
			p = split[split.length-1];
		}
		return p;
	}

	
	public Stat getStat() {
		return stat;
	}

	public void setStat(Stat stat) {
		this.stat = stat;
	}

	public List<ZNodeFX> getChildren() {
		return children;
	}

	public void setChildren(List<ZNodeFX> children) {
		this.children = children;
	}
	
	public void addChildren(ZNodeFX kid){
		if(children==null){
			children = new ArrayList<>();
		}
		children.add(kid);
	}

	@Override
	public String toString() {
		return "ZNodeFX [name=" + name + ", stat=" + stat + ", children=" + children + "]";
	}

	public static String addPath(String path, String kid) {
		String interfix = "";
		if(!path.endsWith("/")){
			interfix = "/";
		}
		return path + interfix + kid;
	}

	public boolean isRoot() {
		return "/".equals(name);
	}
}
