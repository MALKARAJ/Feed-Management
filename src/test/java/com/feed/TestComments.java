package com.feed;

import static org.junit.Assert.*;

import java.util.Date;

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
	public void testFeedPojo()
	{
		Comment c=new Comment();
	    c.setComment("Content");
	    c.setFeed_id("feed123123");
	    c.setComment_id("comment123");
    	Date date=new Date();
    	Long millis = date.getTime();
	    c.setDate(millis);
	    assertEquals("Content",c.getComment());
	}
	@Test
	public void testAddComment() throws EntityNotFoundException {
		createComment();
		CommentDao comment=new CommentOperations();
		Comment c=new Comment();
	    c.setComment("Comment");
	    c.setFeed_id("feed123123");
	    c.setComment_id("comment123");
    	Date date=new Date();
    	Long millis = date.getTime();
	    c.setDate(millis);
	    String e=comment.addComment(c);
	    assertEquals(c.getFeed_id(),e);
	}
	@Test
	public void testUpdateComment() throws EntityNotFoundException {
		createComment();
		DatastoreService ds=DatastoreServiceFactory.getDatastoreService();
		testAddComment();
		CommentDao comment=new CommentOperations();
		Comment c=new Comment();
	    c.setComment("Updated comment");
	    c.setFeed_id("feed123123");
	    c.setComment_id("comment123");
    	Date date=new Date();
    	Long millis = date.getTime();
	    c.setDate(millis);
	    comment.addComment(c);
		Key k=new KeyFactory.Builder("Feed", c.getFeed_id())
			        .addChild("Comment", c.getComment_id())
			        .getKey();
	    Entity entity=ds.get(k);
	    assertEquals("Updated comment",entity.getProperty("comment"));
	}
	@Test
	public void testFeedLike() throws EntityNotFoundException
	{
		createComment();
		testAddComment();
		CommentDao comment=new CommentOperations();
		Comment c=new Comment();
	    c.setFeed_id("feed123123");
	    c.setComment_id("comment123");
	    comment.setLike(c);
	    comment.setLike(c);
	    comment.setLike(c);
	    int likes=comment.getLike(c);
	    assertEquals(3,likes);
	}
}
