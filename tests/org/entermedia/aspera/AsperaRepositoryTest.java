package org.entermedia.aspera;

import java.util.List;

import org.openedit.entermedia.BaseEnterMediaTest;
import org.openedit.repository.ContentItem;

import com.openedit.page.Page;

public class AsperaRepositoryTest extends BaseEnterMediaTest
{

	public void testGetChildrenNames() throws Exception
	{
		AsperaManager manager = (AsperaManager)getFixture().getModuleManager().getBean("asperaManager");
		AsperaRepository repo  = manager.loadRepository("testcatalog", "aspera.visualdatainc.com");
		//Page item = getPage("/somefile.jpg");
		//repo.put(item.getContentItem());
		List names  = repo.getChildrenNames("/");
		assertTrue(names.size() > 0);
	}
	
	public void testUpload() throws Exception
	{
		AsperaManager manager = (AsperaManager)getFixture().getModuleManager().getBean("asperaManager");
		AsperaRepository repo  = manager.loadRepository("testcatalog", "aspera.visualdatainc.com");
		Page item = getPage("/somefile.jpg");
		repo.put(item.getContentItem());
	}
	
	public void testDoesExist() throws Exception
	{
		AsperaManager manager = (AsperaManager)getFixture().getModuleManager().getBean("asperaManager");
		AsperaRepository repo  = manager.loadRepository("testcatalog", "aspera.visualdatainc.com");
		assertTrue(repo.doesExist("/somefile.jpg"));
		assertFalse(repo.doesExist("/somefile.xxx"));
	}
	
	public void testRemove() throws Exception
	{
		AsperaManager manager = (AsperaManager)getFixture().getModuleManager().getBean("asperaManager");
		AsperaRepository repo  = manager.loadRepository("testcatalog", "aspera.visualdatainc.com");
		Page page = getPage("/somefile.jpg");
		ContentItem item = page.getContentItem();
		repo.remove(item);
		assertFalse(repo.doesExist(item.getPath()));
	}
}
