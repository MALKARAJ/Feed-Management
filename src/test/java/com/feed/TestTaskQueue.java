package com.feed;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.dev.LocalTaskQueue;
import com.google.appengine.api.taskqueue.dev.QueueStateInfo;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalTaskQueueTestConfig;

public class TestTaskQueue {

	  private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig(),new LocalTaskQueueTestConfig());
	  @Before
	  public void setUp() {
	    helper.setUp();
	  }

	  @After
	  public void tearDown() {
	   helper.tearDown();
	  }
	  
	  @Test(expected = IllegalArgumentException.class)
	   public void testEmptyUrlNotAllowed() {
	       QueueFactory.getDefaultQueue().add(TaskOptions.Builder.withUrl(""));
	  }

	   
	  @Test
	  public void TestDelete()
	  {
		  QueueFactory.getDefaultQueue().add(TaskOptions.Builder.withTaskName("delete").url("/enqueue").param("feedId", "feed123"));
		  LocalTaskQueue ltq = LocalTaskQueueTestConfig.getLocalTaskQueue();
		  QueueStateInfo qsi = ltq.getQueueStateInfo().get(QueueFactory.getDefaultQueue().getQueueName());
		  assertEquals(1, qsi.getTaskInfo().size());
		  assertEquals("delete", qsi.getTaskInfo().get(0).getTaskName());
		  assertEquals("/enqueue", qsi.getTaskInfo().get(0).getUrl());

	  }
}
