package com.feed;

import static org.junit.Assert.*;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.cache.CacheException;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.repackaged.org.joda.time.DateTime;
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
	    f.setUserId("user123");
        DateTime now = new DateTime();
        Date millis=new Date(now.getMillis());
	    f.setDate(millis);
	    assertEquals("Content",f.getFeed_content());
	    assertEquals("feed123123",f.getFeed_id());
	    assertEquals("movie",f.getCategory());
	    assertEquals(millis,f.getDate());
	}
	
	@Test
	public void testAddFeed() throws EntityNotFoundException, CacheException {
		DatastoreService ds=DatastoreServiceFactory.getDatastoreService();
		FeedDao feed=new FeedOperations();
		Feed f=new Feed();
	    f.setFeed_content("Content");
	    f.setUserId("user123");
	    f.setFeed_id("feed123123");
	    f.setCategory("movie");
        DateTime now = new DateTime();
        Date millis=new Date(now.getMillis());
	    f.setDate(millis);
	    String e=feed.addFeed(f);
	    Key k=KeyFactory.createKey("Feed",e);
	    Entity entity=ds.get(k);
	    assertEquals("Content",entity.getProperty("feed_content"));	    
	    assertEquals("movie",entity.getProperty("category"));
	    }
	
	@Test
	public void testUpdateFeed() throws EntityNotFoundException, CacheException {
		DatastoreService ds=DatastoreServiceFactory.getDatastoreService();
		testAddFeed();
		FeedDao feed=new FeedOperations();
		Feed f=new Feed();
	    f.setFeed_content("Updated Content");
	    f.setFeed_id("feed123123");
	    f.setCategory("movie");
	    f.setUserId("user123");

	    f.setLike(false);
        DateTime now = new DateTime();
        Date millis=new Date(now.getMillis());
	    f.setDate(millis);
	    String e=feed.addFeed(f);
	    Key k=KeyFactory.createKey("Feed",e);
	    Entity entity=ds.get(k);
	    assertEquals("Updated Content",entity.getProperty("feed_content"));	    
	    assertEquals("movie",entity.getProperty("category"));
	    
	}
	@Test
	public void testFeedLike() throws EntityNotFoundException, CacheException
	{
		DatastoreService ds=DatastoreServiceFactory.getDatastoreService();
		testAddFeed();
		FeedDao feed=new FeedOperations();
		Feed f=new Feed();
		
	    f.setFeed_content("Updated Content");
	    f.setFeed_id("feed123123");
	    f.setUserId("user123");

	    f.setCategory("movie");
	    f.setLike(true);
        DateTime now = new DateTime();
        Date millis=new Date(now.getMillis());
	    f.setDate(millis);
	   
	    feed.updateFeed(f);
	    feed.updateFeed(f);
	    feed.updateFeed(f);
	    
	    Key k=KeyFactory.createKey("Feed",f.getFeed_id());
	    Entity entity=ds.get(k);
	    int likes=Integer.parseInt(entity.getProperty("like").toString());
	    assertEquals(3,likes);
	}
	
	@Test
	public void testFeedValidator() throws JsonProcessingException, IOException
	{	
        Validator v=new Validator();
		Feed f=new Feed();
		JSONObject obj = new JSONObject();
		obj.put("content", "Content");
		obj.put("category", "movie");
		obj.put("userId", "user123");
	    assertTrue(v.isValidFeed(obj, f));
	}
	
	@Test
	public void testFeedUpdateValidator() throws JsonProcessingException, IOException, ParseException, EntityNotFoundException, InterruptedException, CacheException
	{	
		testAddFeed();
		Validator v=new Validator();
		Feed f=new Feed();
		JSONObject obj = new JSONObject();
		obj.put("content", "Content");
		obj.put("userId", "user123");
		obj.put("feedId", "feed123123");
		obj.put("category", "movie");
		obj.put("like", "false");
	    assertTrue(v.isValidFeedUpdate(obj, f));
	}
    
    /*
	@Test
	public void testUpdateTime() throws InterruptedException, EntityNotFoundException, ParseException, CacheException
	{
		testAddFeed();
		Validator v=new Validator();
		Feed f=new Feed();
		Date d=new Date();
		f.setDate(d);
		JSONObject obj = new JSONObject();
		obj.put("content", "Content");
		obj.put("feedId", "feed123123");
		obj.put("userId", "user123");
		obj.put("category", "movie");
		obj.put("like", "false");
		assertFalse(v.isValidFeedUpdate(obj,f));
	}
	*/
	@Test
	public void testDelete() throws EntityNotFoundException, CacheException
	{
		DatastoreService ds=DatastoreServiceFactory.getDatastoreService();
		TestComments tc=new TestComments();
		tc.testAddComment();
		FeedDao feed=new FeedOperations();
		Feed f=new Feed();
		f.setFeed_id("feed123123");
		f.setUserId("user123");
		feed.deleteFeed(f);
	    Key k=KeyFactory.createKey("Feed",f.getFeed_id());
	    Entity entity=ds.get(k);
	    Key k1=new KeyFactory.Builder("Feed", "feed123123")
		        .addChild("Comment", "comment123123")
		        .getKey();
	    Entity comment=ds.get(k1);
		assertEquals("true",entity.getProperty("deleted").toString());
		assertEquals("true",comment.getProperty("deleted").toString());
		
	}
}
