package com.feed;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        DateTime now = new DateTime();
        Date millis=new Date(now.getMillis());
	    f.setDate(millis);
	    assertEquals("Content",f.getFeed_content());
	    assertEquals("feed123123",f.getFeed_id());
	    assertEquals("movie",f.getCategory());
	    assertEquals(millis,f.getDate());
	}
	
	@Test
	public void testAddFeed() throws EntityNotFoundException {
		DatastoreService ds=DatastoreServiceFactory.getDatastoreService();
		FeedDao feed=new FeedOperations();
		Feed f=new Feed();
	    f.setFeed_content("Content");
	    f.setFeed_id("feed123123");
	    f.setCategory("movie");
        DateTime now = new DateTime();
        Date millis=new Date(now.getMillis());
	    f.setDate(millis);
	    String e=feed.addFeed(f);
	    Key k=KeyFactory.createKey("Feed",e);
	    Entity entity=ds.get(k);
	    assertEquals("Content",entity.getProperty("feed_content"));	    
	    assertEquals("feed123123",entity.getProperty("feed_id"));
	    assertEquals("movie",entity.getProperty("category"));
	    assertEquals(millis,entity.getProperty("date"));	
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
        DateTime now = new DateTime();
        Date millis=new Date(now.getMillis());
	    f.setDate(millis);
	    String e=feed.addFeed(f);
	    Key k=KeyFactory.createKey("Feed",e);
	    Entity entity=ds.get(k);
	    assertEquals("Updated Content",entity.getProperty("feed_content"));	    
	    assertEquals("feed123123",entity.getProperty("feed_id"));
	    assertEquals("movie",entity.getProperty("category"));
	    assertEquals(millis,entity.getProperty("date"));
	    
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
	
	@Test
	public void testFeedValidator() throws JsonProcessingException, IOException
	{	Validator v=new Validator();
		Feed f=new Feed();
		String j="{\"content\":\"Content\",\"category\":\"movie\"}";
	    ObjectMapper mapper = new ObjectMapper();
	    JsonNode json = mapper.readTree(j);
	    if(v.isValidFeed(json,f)) {
		    f.setFeed_content("Content");
		    f.setFeed_id("feed123123");
		    f.setCategory("movie");
	    }
	    assertEquals("Content",f.getFeed_content());
	}
	
	@Test
	public void testFeedUpdateValidator() throws JsonProcessingException, IOException, ParseException, EntityNotFoundException
	{	Validator v=new Validator();
		Feed f=new Feed();
		String j="{\"content\":\"Content\",\"category\":\"movie\"}";
	    ObjectMapper mapper = new ObjectMapper();
	    JsonNode json = mapper.readTree(j);
	    if(v.isValidFeedUpdate(json,f)) {
	    	
		    f.setFeed_content("Content");
		    f.setFeed_id("feed123123");
		    f.setCategory("movie");
	    }
	    assertEquals("Content",f.getFeed_content());
	}
}
