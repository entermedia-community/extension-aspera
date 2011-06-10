package org.entermedia.aspera;

import java.util.HashMap;
import java.util.Map;

import org.openedit.Data;
import org.openedit.data.SearcherManager;

import com.openedit.ModuleManager;

public class AsperaManager
{
	protected Map fieldRepositories;
	protected SearcherManager fieldSearcherManager;
	protected ModuleManager fieldModuleManager;
	
	public ModuleManager getModuleManager()
	{
		return fieldModuleManager;
	}


	public void setModuleManager(ModuleManager inModuleManager)
	{
		fieldModuleManager = inModuleManager;
	}


	public Map getRepositories()
	{
		if (fieldRepositories == null)
		{
			fieldRepositories = new HashMap();
		}
		return fieldRepositories;
	}


	public void setRepositories(Map inRepositories)
	{
		fieldRepositories = inRepositories;
	}


	public SearcherManager getSearcherManager()
	{
		return fieldSearcherManager;
	}


	public void setSearcherManager(SearcherManager inSearcherManager)
	{
		fieldSearcherManager = inSearcherManager;
	}


	public AsperaRepository loadRepository(String inCatalogId, Data inDestination)
	{
		AsperaRepository repo = (AsperaRepository)getRepositories().get(inDestination.getId());
		if( repo == null)
		{
			repo = (AsperaRepository)getModuleManager().getBean("asperaRepository");
			String server = inDestination.get("server");
			repo.setServer(server);
			repo.setUserName(inDestination.get("username"));
			repo.setExternalPath(inDestination.get("url"));
			getRepositories().put(inDestination.getId(), repo);
		}
		return repo;
	}
}
