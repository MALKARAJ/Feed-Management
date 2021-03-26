package com.feed;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class TestFeed {
	  private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

	  @Before
	  public void setUp() {
	    helper.setUp();
	  }

	  @After
	  public void tearDown() {
	    helper.tearDown();
	  }
	  
	@Test
	public void testFeedPojo()
	{
		Feed f=new Feed();
	    f.setFeed_content("Content");
	    f.setFeed_id("feed123123");
	    f.setCategory("movie");
	    f.setDate("25/03/2021 18:30");
	    assertEquals("Content",f.getFeed_content());
	}
	@Test
	public void testAddFeed() {
		FeedDao feed=new FeedOperations();
		Feed f=new Feed();
	    f.setFeed_content("Content");
	    f.setFeed_id("feed123123");
	    f.setCategory("movie");
	    f.setDate("25/03/2021 18:30");
	    String e=feed.addFeed(f);
	    assertEquals(f.getFeed_id(),e);
	}
	@Test
	public void testUpdateFeed() throws EntityNotFoundException {
		DatastoreService ds=DatastoreServiceFactory.getDatastoreService();
		testAddFeed();
		FeedDao feed=new FeedOperations();
		Feed f=new Feed();
	    f.setFeed_content("Updated Content");
	    f.setFeed_id("feed123123");
	    f.setCategory("movie");
	    f.setDate("25/03/2021 18:30");
	    String e=feed.addFeed(f);
	    Key k=KeyFactory.createKey("Feed",e);
	    Entity entity=ds.get(k);
	    assertEquals("Updated Content",entity.getProperty("feed_content"));
	}
	@Test
	public void testFeedLike() throws EntityNotFoundException
	{
		testAddFeed();
		FeedDao feed=new FeedOperations();
		Feed f=new Feed();
	    f.setFeed_id("feed123123");
	    feed.setLike(f);
	    feed.setLike(f);
	    feed.setLike(f);
	    int likes=feed.getLike(f);
	    assertEquals(3,likes);
	}
}
