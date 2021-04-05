package com.feed;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.repackaged.org.joda.time.DateTime;
import com.google.appengine.api.datastore.Query.FilterPredicate;
public class FeedOperations implements FeedDao{
	
	   DatastoreService ds= DatastoreServiceFactory.getDatastoreService(); 
	   
		public JSONObject getSingleFeed(Feed f) throws JsonProcessingException, IOException, ParseException, EntityNotFoundException {
			Key k=KeyFactory.createKey("Feed", f.getFeed_id());
			   Entity feed=ds.get(k);
			   JSONObject obj= new JSONObject();  
			if(feed.getProperty("delete").toString().equals("false")) 
			   {  
				   obj.put("feedId", f.getFeed_id());
				   obj.put("content", feed.getProperty("feed_content").toString());
				   obj.put("category", feed.getProperty("category").toString());
				   long d=Long.parseLong(feed.getProperty("date").toString());
	 			   Date date=new Date(d);
				   obj.put("date", date);
				   obj.put("likes", Integer.parseInt(feed.getProperty("like").toString()));
				   
				   Query qq=new Query("Comment").setAncestor(feed.getKey()).addSort("Updation_date", SortDirection.DESCENDING);
				   List<JSONObject> comments=new ArrayList<JSONObject>();
				   for (Entity e : ds.prepare(qq).asIterable()) {
					   if (e!=null && e.getProperty("delete").toString().equals("false")) {
						JSONObject commentobj = new JSONObject();
						commentobj.put("feedId", e.getProperty("feed_id").toString());
						commentobj.put("comment", e.getProperty("comment").toString());
						commentobj.put("commentId", e.getProperty("comment_id").toString());
						long d1 = Long.parseLong(e.getProperty("date").toString());
						Date date1 = new Date(d1);
						commentobj.put("date", date1);
						commentobj.put("likes", Integer.parseInt(e.getProperty("like").toString()));
						comments.add(commentobj);
					}
				   }			   
				   obj.put("comments",comments);

			   }
			   return obj;
		}
	   
	   public List<JSONObject> getNewsFeeds() throws JsonProcessingException, IOException, ParseException{
		   List<JSONObject> feeds=new ArrayList<JSONObject>();
		   Filter delete = new FilterPredicate("delete", FilterOperator.EQUAL,false);
		   Query q=new Query("Feed").addSort("Updation_date", SortDirection.DESCENDING).setFilter(delete);
		   for (Entity entity : ds.prepare(q).asIterable()) {	
				   JSONObject obj= new JSONObject();
				   obj.put("feedId", entity.getProperty("feed_id").toString());
				   obj.put("content", entity.getProperty("feed_content").toString());
				   obj.put("category", entity.getProperty("category").toString());
				   long d=Long.parseLong(entity.getProperty("date").toString());
				   Date date=new Date(d);
				   obj.put("date", date);
				   obj.put("likes", Integer.parseInt(entity.getProperty("like").toString()));
				   Query qq=new Query("Comment").setAncestor(entity.getKey()).addSort("Updation_date", SortDirection.DESCENDING);
				   List<JSONObject> comments=new ArrayList<JSONObject>();
				   for (Entity e : ds.prepare(qq).asIterable()) {
					   if (e!=null && e.getProperty("delete").toString().equals("false")) {
						JSONObject commentobj = new JSONObject();
						commentobj.put("feedId", e.getProperty("feed_id").toString());
						commentobj.put("comment", e.getProperty("comment").toString());
						commentobj.put("commentId", e.getProperty("comment_id").toString());
						long d1 = Long.parseLong(e.getProperty("date").toString());
						Date date1 = new Date(d1);
						commentobj.put("date", date1);
						commentobj.put("likes", Integer.parseInt(e.getProperty("like").toString()));
						comments.add(commentobj);
					}
				   }
				   obj.put("comments", comments);
			   	   feeds.add(obj);
			}	        
		   return feeds;
	   }
	   
	   
	   public List<JSONObject> getDeletedFeeds() throws JsonProcessingException, IOException, ParseException{
		   List<JSONObject> feeds=new ArrayList<JSONObject>();
		   Filter delete = new FilterPredicate("delete", FilterOperator.EQUAL,true);
		   Query q=new Query("Feed").addSort("Updation_date", SortDirection.DESCENDING).setFilter(delete);
		   for (Entity entity : ds.prepare(q).asIterable()) {	
				   JSONObject obj= new JSONObject();
				   obj.put("feedId", entity.getProperty("feed_id").toString());
				   obj.put("content", entity.getProperty("feed_content").toString());
				   obj.put("category", entity.getProperty("category").toString());
				   long d=Long.parseLong(entity.getProperty("date").toString());
				   Date date=new Date(d);
				   obj.put("date", date);
				   obj.put("likes", Integer.parseInt(entity.getProperty("like").toString()));
				   Query qq=new Query("Comment").setAncestor(entity.getKey()).addSort("Updation_date", SortDirection.DESCENDING).setFilter(delete);
				   List<JSONObject> comments=new ArrayList<JSONObject>();
				   for (Entity e : ds.prepare(qq).asIterable()) {
					   if (e!=null) {
						JSONObject commentobj = new JSONObject();
						commentobj.put("feedId", e.getProperty("feed_id").toString());
						commentobj.put("comment", e.getProperty("comment").toString());
						commentobj.put("commentId", e.getProperty("comment_id").toString());
						long d1 = Long.parseLong(e.getProperty("date").toString());
						Date date1 = new Date(d1);
						commentobj.put("date", date1);
						commentobj.put("likes", Integer.parseInt(e.getProperty("like").toString()));
						comments.add(commentobj);
					}
				   }
				   obj.put("comments", comments);
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
		   Date date=f.getDate();
		   DateTime d=new DateTime(date);
		   feed.setProperty("date",d.getMillis());
		   feed.setProperty("Updation_date",d.getMillis());
		   feed.setProperty("like", 0);
		   feed.setProperty("delete", false);
		   ds.put(feed);
		   return feed.getProperty("feed_id").toString();
	   }
	   
	   public void updateFeed(Feed f) throws EntityNotFoundException {
		   Key k=KeyFactory.createKey("Feed", f.getFeed_id());
		   Entity feed=ds.get(k);
		   feed.setProperty("feed_id", f.getFeed_id());
		   feed.setProperty("feed_content", f.getFeed_content());
		   feed.setProperty("category", f.getCategory());
		   feed.setProperty("Updation_date", f.getUpdateDate());
		   if(f.isLike())
		   {
			   feed.setProperty("like", f.getLikes()+1);
			   f.setLikes(Integer.parseInt(feed.getProperty("like").toString()));

		   }
		   ds.put(feed);
	   }
	   
	   public void setLikePojo(Feed f) throws EntityNotFoundException{
		   Key k=KeyFactory.createKey("Feed",f.getFeed_id());
		   Entity en=ds.get(k);
		   f.setLikes(Integer.parseInt(en.getProperty("like").toString()));
	   }

	   public void deleteFeed(Feed f) throws EntityNotFoundException 
	   {
		   Key k=KeyFactory.createKey("Feed",f.getFeed_id());
		   Entity e= ds.get(k);
		   Query q = new Query("Comment").setAncestor(e.getKey());
		   for (Entity entity : ds.prepare(q).asIterable()) {	
			   	entity.setProperty("delete", true);
			   	ds.put(entity);
		   }	
		   e.setProperty("delete", true); 
		   
		   ds.put(e);
	   }
}
