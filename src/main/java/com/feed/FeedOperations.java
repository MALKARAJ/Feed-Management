package com.feed;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
//import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;

public class FeedOperations implements FeedDao{
	
	   DatastoreService ds= DatastoreServiceFactory.getDatastoreService();
	   
	   
	   public List<String> getNewsFeeds() throws JsonProcessingException, IOException{
		   Feed f=new Feed();
		   List<String> feeds=new ArrayList<String>();
		   Query q=new Query("Feed");
		   for (Entity entity : ds.prepare(q).asIterable()) {	
			   	f.setFeed_id(entity.getProperty("feed_id").toString());
			   	f.setCategory(entity.getProperty("category").toString());
			   	f.setDate(entity.getProperty("date").toString());
			   	f.setFeed_content(entity.getProperty("feed_content").toString());
			   	ObjectMapper obj=new ObjectMapper();
	            String jsonStr = obj.writeValueAsString(f);
			   	feeds.add(jsonStr);
			}	        
		   return feeds;
	   }
	   
	   public String addFeed(Feed f)
	   {
		   Entity feed=new Entity("Feed",f.getFeed_id());
		   feed.setProperty("feed_id",f.getFeed_id());
		   feed.setProperty("feed_content",f.getFeed_content());
		   feed.setProperty("category", f.getCategory());
		   feed.setProperty("date", f.getDate());
		   feed.setProperty("like", 0);
		   ds.put(feed);
		   return feed.getProperty("feed_id").toString();
	   }
	   
	   public void updateFeed(Feed f) throws EntityNotFoundException {
		   Key k=KeyFactory.createKey("Feed", f.getFeed_id());
		   Entity feed=ds.get(k);
		   feed.setProperty("feed_id", f.getFeed_id());
		   feed.setProperty("feed_content", f.getFeed_content());
		   feed.setProperty("category", f.getCategory());
		   feed.setProperty("date", f.getDate());
		   ds.put(feed);
	   }
	   
	   public void setLike(Feed f) throws EntityNotFoundException
	   {
		   Key k=KeyFactory.createKey("Feed", f.getFeed_id());
		   Entity feed=ds.get(k);
		   int like=Integer.parseInt(feed.getProperty("like").toString())+1;
		   feed.setProperty("like", like);
		   ds.put(feed);
	   }
	   
	   public int getLike(Feed f) throws EntityNotFoundException
	   {
		   Key k=KeyFactory.createKey("Feed", f.getFeed_id());
		   Entity feed=ds.get(k);
		   return Integer.parseInt(feed.getProperty("like").toString());
		   
	   }
	   
	   public void deleteFeed(Feed f) throws EntityNotFoundException {
		   Key k=KeyFactory.createKey("Feed",f.getFeed_id());
		   Entity e= ds.get(k);
		   Query q = new Query("comments").setAncestor(e.getKey());
		   for (Entity entity : ds.prepare(q).asIterable()) {	
			   ds.delete(entity.getKey());
			}	
		   ds.delete(e.getKey());   
	   }
}
