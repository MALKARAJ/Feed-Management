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
import com.google.appengine.repackaged.org.joda.time.DateTime;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class TestUser {
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
	public void testRegister() throws EntityNotFoundException {
		User user=new User();
		UserDao u=new UserOperations();
		DatastoreService ds=DatastoreServiceFactory.getDatastoreService();
		
	    user.setUserId("user123");
	    user.setEmail("george@123");
	    user.setPassword("user123123");
	    
        DateTime now = new DateTime();
        Date millis=new Date(now.getMillis());
        user.setDate(millis);
        u.addUser(user);
	    Key k=KeyFactory.createKey("User",user.getUserId());
	    Entity entity=ds.get(k);
	    assertEquals("george@123",entity.getProperty("email").toString());
	}
	
	@Test
	public void testLogin() throws EntityNotFoundException
	{
		UserDao u=new UserOperations();
		testRegister();
		boolean b=u.isValid("george@123","user123123");
		assertTrue(b);
	}

}
