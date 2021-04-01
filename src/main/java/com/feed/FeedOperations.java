package com.feed;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//import java.util.UUID;

import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
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
	   
		public JSONObject getSingleFeed(Feed f) throws JsonProcessingException, IOException, ParseException, EntityNotFoundException {
			   Key k=KeyFactory.createKey("Feed", f.getFeed_id());
			   Entity feed=ds.get(k);
			   JSONObject obj= new JSONObject();
			   obj.put("feedId", f.getFeed_id());
			   obj.put("content", feed.getProperty("feed_content").toString());
			   obj.put("category", feed.getProperty("category").toString());
			   obj.put("date", feed.getProperty("date"));
			   obj.put("likes", Integer.parseInt(feed.getProperty("like").toString()));
			   return obj;
		}
	   
	   public List<JSONObject> getNewsFeeds() throws JsonProcessingException, IOException, ParseException{
		   Feed f=new Feed();
		   List<JSONObject> feeds=new ArrayList<JSONObject>();
		   Query q=new Query("Feed");
		   for (Entity entity : ds.prepare(q).asIterable()) {	
				/*
				 * f.setFeed_id(entity.getProperty("feed_id").toString());
				 * f.setCategory(entity.getProperty("category").toString()); String
				 * datetime=entity.getProperty("date").toString(); System.out.println(datetime);
				 * DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy"); Date
				 * date = (Date)formatter.parse(datetime); f.setDate(date);
				 * f.setFeed_content(entity.getProperty("feed_content").toString());
				 * f.setLikes(Integer.parseInt(entity.getProperty("like").toString()));
				 * ObjectMapper obj=new ObjectMapper(); 
				 * String jsonStr = obj.writeValueAsString(f);
				 */
				   JSONObject obj= new JSONObject();
				   obj.put("feedId", entity.getProperty("feed_id").toString());
				   obj.put("content", entity.getProperty("feed_content").toString());
				   obj.put("category", entity.getProperty("category").toString());
				   obj.put("date", entity.getProperty("date").toString());
				   obj.put("likes", Integer.parseInt(entity.getProperty("like").toString()));
			   	   feeds.add(obj);
			}	        
		   return feeds;
	   }
	   
	   public String addFeed(Feed f)
	   {
		   Entity feed=new Entity("Feed",f.getFeed_id());
		   feed.setProperty("feed_id",f.getFeed_id());
		   feed.setProperty("feed_content",f.getFeed_content());
		   feed.setProperty("category", f.getCategory());
		   feed.setProperty("date",f.getDate());
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
		   int like=Integer.parseInt(feed.getProperty("like").toString());
		   return like;
		   
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
