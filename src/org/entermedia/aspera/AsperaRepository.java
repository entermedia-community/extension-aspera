package org.entermedia.aspera;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openedit.repository.BaseRepository;
import org.openedit.repository.ContentItem;
import org.openedit.repository.Repository;
import org.openedit.repository.RepositoryException;

import com.asperasoft.cmdclient.CmdClient;
import com.asperasoft.cmdclient.CmdReplyFile;
import com.asperasoft.cmdclient.CmdReplyFile.File;
import com.asperasoft.cmdclient.CmdReplyInfo;
import com.asperasoft.cmdclient.CmdReplySize;
import com.openedit.OpenEditException;
import com.openedit.users.User;
import com.openedit.users.UserManager;
import com.openedit.util.Exec;
import com.openedit.util.ExecResult;
import com.openedit.util.PathUtilities;

public class AsperaRepository extends BaseRepository
{
	private static final Log log = LogFactory.getLog(AsperaRepository.class);
	protected Exec fieldExec;
	public Exec getExec()
	{
		return fieldExec;
	}

	public void setExec(Exec inExec)
	{
		fieldExec = inExec;
	}

	protected String fieldServer;
	protected int fieldPort = 22;
	
	public int getPort()
	{
		return fieldPort;
	}

	public String getServer()
	{
		return fieldServer;
	}

	public void setServer(String inServer)
	{
		fieldServer = inServer;
	}

	protected String fieldUserName;
	public String getUserName()
	{
		return fieldUserName;
	}

	public void setUserName(String inUserName)
	{
		fieldUserName = inUserName;
	}

	protected UserManager fieldUserManager;
	
	public UserManager getUserManager()
	{
		return fieldUserManager;
	}

	public void setUserManager(UserManager inUserManager)
	{
		fieldUserManager = inUserManager;
	}

	public CmdClient getCmdClient()
	{
		if (fieldCmdClient == null)
		{
			CmdClient client =  new CmdClient();
			client.setConnectionTimeout(10000);
			client.setCommandTimeout(12000);
            try
            {
            	User user = getUserForUserName();
            	String pw = getDecryptedPassword(user);
            	client.connect(getServer(), getUserName(), pw, getPort());
	             
	            if (client.isConnected()) {
	                //System.out.println("Connected...");
	                CmdReplyInfo info = client.execInfo();
	                info.printReply();
	            }
	            fieldCmdClient = client;
            }
            catch( Exception ex)
            {
            	log.error("failed to connect to aspera with: " + getServer() + ", " + getUserName() + ", " + getPort());
            	throw new OpenEditException(ex);
            }
		}

		return fieldCmdClient;
	}

	protected User getUserForUserName() {
		return getUserManager().getUser(getUserName());
	}

	protected String getDecryptedPassword(User user) {
		return getUserManager().decryptPassword(user);
	}

	public void setCmdClient(CmdClient inCmdClient)
	{
		fieldCmdClient = inCmdClient;
	}

	protected CmdClient fieldCmdClient;
	
	
	
	@Override
	public ContentItem get(String inPath) throws RepositoryException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentItem getStub(String inPath) throws RepositoryException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean doesExist(String inPath) throws RepositoryException
	{
		try
		{
			CmdReplySize reply = getCmdClient().execDu(getExternalPath() + inPath);
			//reply.
			return true;
		}
		catch (Exception e)
		{
			//throws exception of the file isn't found
			return false;
		}
	}

	public void put(ContentItem inContent) throws RepositoryException
	{
		String destination = getExternalPath() + inContent.getPath();
		//call mkdir folder may not exist
		if( doesExist(destination))
		{
			log.info("File already exists"); //TODO: Check file size
			return;
		}
		log.error("AsperaRepository.put: " + destination);
		System.out.println("AsperaRepository.put: " + destination);
		String dir = PathUtilities.extractDirectoryPath(destination);
		try
		{
			log.info("getCmdClient().execMkdir("+dir+");"); //TODO: Check file size
			getCmdClient().execMkdir(dir);
		}
		catch (Exception e)
		{
			if( e instanceof OpenEditException)
			{
				throw (OpenEditException)e;
			}
			throw new OpenEditException(e);
		}
		
		List<String> command = new ArrayList<String>();
		command.add(inContent.getAbsolutePath());
		command.add(destination);
		command.add(getUserName());
		User user = getUserForUserName();
        String pw = getDecryptedPassword(user);
		command.add(pw);
		command.add(getServer());
		ExecResult result = getExec().runExec("asperaupload", command, true);
		if( !result.isRunOk())
		{
			throw new OpenEditException("Could not run aspera upload " + result.getStandardError());
		}
	}

	@Override
	public void copy(ContentItem inSource, ContentItem inDestination) throws RepositoryException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void move(ContentItem inSource, ContentItem inDestination) throws RepositoryException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void move(ContentItem inSource, Repository inSourceRepository, ContentItem inDestination) throws RepositoryException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(ContentItem inPath) throws RepositoryException
	{
		try
		{
			getCmdClient().execRm(getExternalPath() + inPath.getPath());
		}
		catch (Exception e)
		{
			throw new RepositoryException(e);
		}
	}

	@Override
	public List getVersions(String inPath) throws RepositoryException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentItem getLastVersion(String inPath) throws RepositoryException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getChildrenNames(String inParent) throws RepositoryException
	{
		try
		{
			CmdReplyFile info = getCmdClient().execLs(inParent);
			File[] files = info.getFileList();
			List names = new ArrayList(files.length);
			for (int i = 0; i < files.length; i++)
			{
				names.add(files[i].getName());
			}
			return names;
		}
		catch ( Exception ex)
		{
			throw new RepositoryException(ex);
		}
	}

	@Override
	public void deleteOldVersions(String inPath) throws RepositoryException
	{
		// TODO Auto-generated method stub

	}

}
