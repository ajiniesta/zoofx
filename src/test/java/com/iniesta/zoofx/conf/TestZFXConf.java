package com.iniesta.zoofx.conf;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TestZFXConf {

	private ZFXConf zconf;

	@Before
	public void before(){
		zconf = ZFXConf.getInstance();
	}
	
	@Test
	public void testConnections() throws Exception{
		List<String> connections = zconf.extractConnections("localhost:2181");
		assertNotNull(connections);
		assertEquals(1, connections.size());
		assertEquals("localhost:2181", connections.get(0));
	}

	@Test
	public void testConnectionsSeveral() throws Exception{
		List<String> connections = zconf.extractConnections("localhost:2181|srv1:2181");
		assertNotNull(connections);
		assertEquals(2, connections.size());
		assertEquals("localhost:2181", connections.get(0));
		assertEquals("srv1:2181", connections.get(1));
	}
	
	@Test
	public void testConnectionsSeveralSeveral() throws Exception{
		List<String> connections = zconf.extractConnections("localhost:2181|srv1:2181,srv2:2181");
		assertNotNull(connections);
		assertEquals(2, connections.size());
		assertEquals("localhost:2181", connections.get(0));
		assertEquals("srv1:2181,srv2:2181", connections.get(1));
	}
	
	@Test
	public void testConnectionsZero() throws Exception{
		List<String> connections = zconf.extractConnections("");
		assertNotNull(connections);
		assertEquals(0, connections.size());
	}
	
	@Test
	public void testConnectionsNull() throws Exception{
		List<String> connections = zconf.extractConnections(null);
		assertNotNull(connections);
		assertEquals(0, connections.size());
	}
	
	@Test
	public void testAdd1() throws Exception{
		String lasts = "";
		String lastConn = "srv1:2181";
		String actual = zconf.addSuccess(lasts, lastConn);
		assertEquals(lastConn, actual);
	}
	@Test
	public void testAdd2() throws Exception{
		String lasts = null;
		String lastConn = "srv1:2181";
		String actual = zconf.addSuccess(lasts, lastConn);
		assertEquals(lastConn, actual);
	}
	@Test
	public void testAdd3() throws Exception{
		String lasts = "srv1:2181";
		String lastConn = "srv1:2181";
		String actual = zconf.addSuccess(lasts, lastConn);
		assertEquals(lastConn, actual);
	}
	@Test
	public void testAdd5() throws Exception{
		String lasts = "srv1:2181|srv2:2181|srv3:2181";
		String lastConn = "srv4:2181";
		String actual = zconf.addSuccess(lasts, lastConn);
		assertEquals("srv1:2181|srv2:2181|srv3:2181|srv4:2181", actual);
	}

	@Test
	public void testAdd6() throws Exception{
		String lasts = "srv1:2181|srv2:2181|srv3:2181|srv4:2181";
		String lastConn = "srv4:2181";
		String actual = zconf.addSuccess(lasts, lastConn);
		assertEquals("srv1:2181|srv2:2181|srv3:2181|srv4:2181", actual);
	}
	
	@Test
	public void testAdd7() throws Exception{
		String lasts = "srv1:2181|srv2:2181|srv3:2181|srv4:2181";
		String lastConn = "srv5:2181";
		String actual = zconf.addSuccess(lasts, lastConn);
		assertEquals("srv2:2181|srv3:2181|srv4:2181|srv5:2181", actual);
	}

}
