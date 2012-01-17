package org.entermedia.aspera;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.asperasoft.cmdclient.CmdClient;
import com.openedit.users.User;

public class AsperaRepositoryTest {
	
	@Test
	public void testGetExtractDirectoryPath() throws Exception{
		AsperaRepository repo = new AsperaRepository();
		String path = null;
//		path = repo.getExtractDirectoryPath(null);
//		assertNotNull("NULL path", path);
	}

	@Test
	public void testGetCmdClient() {
		AsperaRepository repo = new AsperaRepository(){

			

			@Override
			public String getUserName() {
				return "aspera_vdms";
			}

			@Override
			protected User getUserForUserName() {
				return null;
			}

			@Override
			protected String getDecryptedPassword(User user) {
				return "transfer123";
			}

			@Override
			public int getPort() {
				return 22;
			}

			@Override
			public String getServer() {
				return "aspera.visualdatainc.com";
			}	
			
			
			
		};
		CmdClient client = null;
		client = repo.getCmdClient();
		assertNotNull("NULL Client", client);
		assertTrue("NOT CONNECTED!", client.isConnected());
		client.disconnect();
		assertFalse("CONNECTED!", client.isConnected());
		
	}

}
