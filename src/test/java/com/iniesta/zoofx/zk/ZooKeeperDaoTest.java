package com.iniesta.zoofx.zk;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.iniesta.zoofx.model.ZNodeFX;
import com.iniesta.zoofx.model.ZNodeFXContent;

@Ignore
public class ZooKeeperDaoTest {

	private ZookeeperDao zkDao;

	@BeforeClass
	public static void startUp(){
//		final ZooKeeperServerMain zksm = new ZooKeeperServerMain();
//		final ServerConfig config = new ServerConfig();
//		new Thread(){
//			public void run(){
//				
//				zksm.runFromConfig(config);
//			}
//		}.start();
		
	}
	
	@Before
	public void before(){
		zkDao = new ZookeeperDao();
	}
	
	@Test
	public void testRegularSave() throws KeeperException, InterruptedException, IOException{
		ZooKeeper zk = ZookeeperConnection.getInstance().connect("localhost:2181", 1000);
		ZNodeFX parent = new ZNodeFX();
		parent.setName("/");
		ZNodeFX test = new ZNodeFX();
		test.setName("/test");
		zkDao.removeZNode(zk, test);
		test = zkDao.createEmptyZnode(zk , parent , "test");
		ZNodeFXContent saved = zkDao.saveZNode(zk, test, "content...");
		test.setStat(saved.getStat());
		zkDao.saveZNode(zk, test, "content...2");
		zkDao.removeZNode(zk, test);
	}
}
