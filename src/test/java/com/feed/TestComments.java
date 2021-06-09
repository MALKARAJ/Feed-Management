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

public class TestComments {
	  private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

	  @Before
	  public void setUp() {
	    helper.setUp();
	  }

	  @After
	  public void tearDown() {
	    helper.tearDown();
	  }
	public void createComment()
	{
		DatastoreService ds= DatastoreServiceFactory.getDatastoreService();
		Entity e= new Entity("Comment","comment123123");
		ds.put(e);
	}
	@Test
	public void testCommentPojo()
	{
		Comment c=new Comment();
	    c.setComment("Content");
	    c.setFeed_id("feed123123");
	    c.setComment_id("comment123");
        DateTime now = new DateTime();
        Date millis=new Date(now.getMillis());
	    c.setDate(millis);
	    c.setLikes(0);
	    assertEquals("Content",c.getComment());
	    assertEquals("feed123123",c.getFeed_id());
	    assertEquals("comment123",c.getComment_id());
	    assertEquals(millis,c.getDate());
	}
	@Test
	public void testAddComment() throws EntityNotFoundException, CacheException {
		TestFeed f=new TestFeed();
		f.testAddFeed();
		createComment();
		CommentDao comment=new CommentOperations();
		Comment c=new Comment();
	    c.setComment("Comment");
	    c.setFeed_id("feed123123");
	    c.setUser_id("user123");
	    c.setComment_id("comment123123");
        DateTime now = new DateTime();
        Date millis=new Date(now.getMillis());
	    c.setDate(millis);
	    String e=comment.addComment(c);
	    assertEquals(c.getFeed_id(),e);
	}
	@Test
	public void testUpdateComment() throws EntityNotFoundException, CacheException {
		createComment();
		DatastoreService ds=DatastoreServiceFactory.getDatastoreService();
		testAddComment();
		CommentDao comment=new CommentOperations();
		Comment c=new Comment();
	    c.setComment("Updated comment");
	    c.setFeed_id("feed123123");
	    c.setUser_id("user123");

	    c.setComment_id("comment123123");
        DateTime now = new DateTime();
        Date millis=new Date(now.getMillis());
	    c.setDate(millis);
	    comment.updateComment(c);
		Key k=new KeyFactory.Builder("Feed", c.getFeed_id())
			        .addChild("Comment", c.getComment_id())
			        .getKey();
	    Entity entity=ds.get(k);
	    assertEquals("Updated comment",entity.getProperty("comment"));
	}
	@Test
	public void testCommentLike() throws EntityNotFoundException, CacheException
	{		
		DatastoreService ds=DatastoreServiceFactory.getDatastoreService();
		createComment();
		testAddComment();
		CommentDao comment=new CommentOperations();
		Comment c=new Comment();
	    c.setFeed_id("feed123123");
	    c.setUser_id("user123");

	    c.setComment_id("comment123123");
	    c.setComment("Updated comment");
	    c.setFeed_id("feed123123");
	    c.setLike(true);
	    c.setComment_id("comment123123");
        DateTime now = new DateTime();
        Date millis=new Date(now.getMillis());
	    c.setDate(millis);
	    comment.updateComment(c);
	    comment.updateComment(c);
	    Key k=new KeyFactory.Builder("Feed", c.getFeed_id())
		        .addChild("Comment", c.getComment_id())
		        .getKey();
	    Entity entity=ds.get(k);	 
	    assertEquals(2,Integer.parseInt(entity.getProperty("like").toString()));
	}
	
	
	@Test
	public void testCommentValidator() throws JsonProcessingException, IOException
	{	Validator v=new Validator();
		Comment c=new Comment();
		JSONObject obj = new JSONObject();
		obj.put("comment", "Content");
		obj.put("feedId", "feed123123");
		obj.put("userId","user123");
	    assertTrue(v.isValidComment(obj, c));
	}
	
	@Test
	public void testCommentUpdateValidator() throws JsonProcessingException, IOException, ParseException, EntityNotFoundException, InterruptedException, CacheException
	{		
		testAddComment();
		Validator v=new Validator();
		Comment c=new Comment();
		JSONObject obj = new JSONObject();
		obj.put("comment", "Content");
		obj.put("feedId", "feed123123");
		obj.put("commentId", "comment123123");
		obj.put("userId","user123");
		obj.put("like", "false");
	    assertTrue(v.isValidCommentUpdate(obj, c));
	}
	
	/*
	  @Test public void testUpdateTime() throws InterruptedException,
	  EntityNotFoundException, ParseException, CacheException
	  { 
		  testAddComment();
		  Validator v=new Validator(); 
		  Comment c=new Comment();
		  Date d=new Date();
		  c.setDate(d);
		  JSONObject obj = new JSONObject();
		  obj.put("comment", "Content"); 
		  obj.put("feedId", "feed123123");
		  obj.put("commentId", "comment123123"); 
		  obj.put("userId","user123");
		  obj.put("like", "false");
		  assertFalse(v.isValidCommentUpdate(obj,c)); 
	 }
	  */
		@Test
		public void testDelete() throws EntityNotFoundException, CacheException
		{
			DatastoreService ds=DatastoreServiceFactory.getDatastoreService();
			TestComments tc=new TestComments();
			tc.testAddComment();
			CommentDao comment=new CommentOperations();
			Comment c=new Comment();
			c.setFeed_id("feed123123");
			c.setComment_id("comment123123");
			c.setUser_id("user123");
			comment.deleteComment(c);

		    Key k1=new KeyFactory.Builder("Feed", "feed123123")
			        .addChild("Comment", "comment123123")
			        .getKey();
		    Entity c1=ds.get(k1);
			assertEquals("true",c1.getProperty("deleted").toString());
			
		}
	 
}
