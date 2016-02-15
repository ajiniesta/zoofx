package com.iniesta.zoofx.model;

import java.io.UnsupportedEncodingException;

import org.apache.zookeeper.data.Stat;

public class ZNodeFXContent {

	private static final String UTF_8 = "UTF-8";
	private String content;
	private Stat stat;

	public ZNodeFXContent() {
		this((String)null, null);
	}
	
	public ZNodeFXContent(byte[] content, Stat stat) throws UnsupportedEncodingException {
		this(new String(content, UTF_8),stat);
	}
	
	public ZNodeFXContent(String content, Stat stat) {
		super();
		this.content = content;
		this.stat = stat;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public void setContent(byte[] content) throws UnsupportedEncodingException {
		this.content = new String(content, UTF_8);
	}
	public Stat getStat() {
		return stat;
	}
	public void setStat(Stat stat) {
		this.stat = stat;
	}
	@Override
	public String toString() {
		return "ZNodeFXContent [content=" + content + ", stat=" + stat + "]";
	}
	
	
}
