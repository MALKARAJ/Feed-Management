package com.feed;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.cache.CacheException;

import org.json.JSONArray;
import org.json.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.QueryResultList;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.repackaged.org.joda.time.DateTime;
import com.google.appengine.api.datastore.Query.FilterPredicate;
public class FeedOperations implements FeedDao{
	   private static final Logger log = Logger.getLogger(FeedOperations.class.getName());	

	   DatastoreService ds= DatastoreServiceFactory.getDatastoreService(); 
	   
	   public JSONObject getSingleFeed(Feed f) throws JsonProcessingException, IOException, ParseException, EntityNotFoundException {
		   
		   
		   log.info("Collecting a single feed");
			Key k=KeyFactory.createKey("Feed", f.getFeed_id());
			   Entity feed=ds.get(k);
			   JSONObject obj= new JSONObject();  
			if(feed.getProperty("deleted").toString().equals("false")) 
			   {  
				   obj.put("feed_id", f.getFeed_id());
				   obj.put("userId",feed.getProperty("userId").toString());
				   obj.put("feed_content", feed.getProperty("feed_content").toString());
				   obj.put("category", feed.getProperty("category").toString());
				   long d=Long.parseLong(feed.getProperty("date").toString());
	 			   Date date=new Date(d);
				   obj.put("date", date);
				   obj.put("likes", Integer.parseInt(feed.getProperty("like").toString()));
				   
				   Query qq=new Query("Comment").setAncestor(feed.getKey()).addSort("Updation_date", SortDirection.DESCENDING);
				   List<JSONObject> comments=new ArrayList<JSONObject>();
				   for (Entity e : ds.prepare(qq).asIterable()) {
					   if (e!=null && e.getProperty("deleted").toString().equals("false")) {
						JSONObject commentobj = new JSONObject();
						commentobj.put("feed_id", e.getProperty("feed_id").toString());
						commentobj.put("comment", new StringBuilder(e.getProperty("comment").toString()));
						commentobj.put("comment_id", e.getProperty("comment_id").toString());
						commentobj.put("userId", e.getProperty("userId").toString());
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

	   public JSONObject getNewsFeeds(String startCursor) throws JsonProcessingException, IOException, ParseException, CacheException{

		   			log.info("Collecting all feeds");
				 	int PAGE_SIZE=30;
				 	FetchOptions fetchOptions = FetchOptions.Builder.withLimit(PAGE_SIZE);

				   // String startCursor = req.getParameter("cursor");
				    if (startCursor != null) {
				      fetchOptions.startCursor(Cursor.fromWebSafeString(startCursor));
				    }

				   List<JSONObject> feeds=new ArrayList<JSONObject>();
				   Filter delete = new FilterPredicate("deleted", FilterOperator.EQUAL,false);
				   Query q=new Query("Feed").addSort("Updation_date", SortDirection.DESCENDING).setFilter(delete);
				   QueryResultList<Entity> results=ds.prepare(q).asQueryResultList(fetchOptions);
				   for (Entity entity : results) {	
						   JSONObject obj= new JSONObject();
						   obj.put("feed_id", entity.getProperty("feed_id").toString());
						   obj.put("feed_content", entity.getProperty("feed_content").toString());
						   obj.put("category", entity.getProperty("category").toString());
						   obj.put("userId", entity.getProperty("userId").toString());
						   long d=Long.parseLong(entity.getProperty("date").toString());
						   Date date=new Date(d);
						   obj.put("date", date);
						   obj.put("likes", Integer.parseInt(entity.getProperty("like").toString()));
						   Query qq=new Query("Comment").setAncestor(entity.getKey()).addSort("Updation_date", SortDirection.DESCENDING).setFilter(delete);
						   List<JSONObject> comments=new ArrayList<JSONObject>();
						   for (Entity e : ds.prepare(qq).asIterable()) {
							   if (e!=null ) {
								JSONObject commentobj = new JSONObject();
								commentobj.put("feed_id", e.getProperty("feed_id").toString());
								commentobj.put("comment", e.getProperty("comment").toString());
								commentobj.put("comment_id", e.getProperty("comment_id").toString());
								commentobj.put("userId", e.getProperty("userId").toString());
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
			       	String cursorString = results.getCursor().toWebSafeString();
					JSONObject objResult = new JSONObject();
					objResult.put("success", true);
					objResult.put("cursor", cursorString);
					objResult.put("code", "200");
					objResult.put("feeds", feeds);
					return objResult;			    	
			    }

	   public List<JSONObject> getDeletedFeeds(Feed f) throws JsonProcessingException, IOException, ParseException
	   {
		   
		   
		   log.info("collecting deleted feeds");
		   List<JSONObject> feeds=new ArrayList<JSONObject>();
		   Filter cat = new FilterPredicate("userId", FilterOperator.EQUAL,f.getUserId());
		   Filter del = new FilterPredicate("deleted", FilterOperator.EQUAL,true);
		   CompositeFilter catdel =
				    CompositeFilterOperator.and(cat, del);		   
		   Query q=new Query("Feed").addSort("Updation_date", SortDirection.DESCENDING).setFilter(catdel);
		   for (Entity entity : ds.prepare(q).asIterable()) 
		   {	
				   JSONObject obj= new JSONObject();
				   obj.put("feed_id", entity.getProperty("feed_id").toString());
				   obj.put("feed_content", entity.getProperty("feed_content").toString());
				   obj.put("category", entity.getProperty("category").toString());
				   long d=Long.parseLong(entity.getProperty("date").toString());
				   obj.put("userId", entity.getProperty("userId").toString());
				   obj.put("userId", entity.getProperty("userId").toString());

				   Date date=new Date(d);
				   obj.put("date", date);
				   obj.put("likes", Integer.parseInt(entity.getProperty("like").toString()));
				   Query qq=new Query("Comment").setAncestor(entity.getKey()).addSort("Updation_date", SortDirection.DESCENDING).setFilter(del);
				   List<JSONObject> comments=new ArrayList<JSONObject>();
				   for (Entity e : ds.prepare(qq).asIterable()) {
					   if (e!=null) 
					   {
						JSONObject commentobj = new JSONObject();
						commentobj.put("feed_id", e.getProperty("feed_id").toString());
						commentobj.put("comment", e.getProperty("comment").toString());
						commentobj.put("comment_id", e.getProperty("comment_id").toString());
						long d1 = Long.parseLong(e.getProperty("date").toString());
						commentobj.put("userId", e.getProperty("userId").toString());
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

	   public JSONObject getCategoryFeeds(String category, String startCursor) throws JsonProcessingException, IOException, ParseException, CacheException{
		
		   log.info("collecting feeds of category "+category);
		   MemcacheService cache = MemcacheServiceFactory.getMemcacheService();
		 	int PAGE_SIZE=30;
		 	FetchOptions fetchOptions = FetchOptions.Builder.withLimit(PAGE_SIZE);

		 	System.out.println("c"+startCursor);
		 	System.out.println(cache.get(category));
		   if(cache.get(category)!=null && startCursor.isEmpty())
		   {
			   System.out.println("In memcache");
			   String cat=cache.get(category).toString();
			   String curs=cache.get("cursor").toString();
			   JSONArray cacheData=new JSONArray(cat);
			   JSONObject obj=new JSONObject();
			   obj.put("success", true);
			   obj.put("code", "200");
			   obj.put("cursor",curs);
			   obj.put("feeds", cacheData);
			   return obj;			
		   }
		   else 
		   {
			    if (startCursor != null) {
				      fetchOptions.startCursor(Cursor.fromWebSafeString(startCursor));
				    }

			   System.out.println("From the DB");
			   List<JSONObject> feeds=new ArrayList<JSONObject>();
			   System.out.println(category);
			   Filter cat = new FilterPredicate("category", FilterOperator.EQUAL,category);
			   Filter del = new FilterPredicate("deleted", FilterOperator.EQUAL,false);
			   CompositeFilter catdel =
					    CompositeFilterOperator.and(cat, del);
			   Query q=new Query("Feed").addSort("Updation_date", SortDirection.DESCENDING).setFilter(catdel);
			   QueryResultList<Entity> results=ds.prepare(q).asQueryResultList(fetchOptions);
			   for (Entity entity :results) {	
					   JSONObject obj= new JSONObject();
					   obj.put("feed_id", entity.getProperty("feed_id").toString());
					   obj.put("feed_content", entity.getProperty("feed_content").toString());
					   obj.put("category", entity.getProperty("category").toString());
					   obj.put("userId", entity.getProperty("userId").toString());
					   long d=Long.parseLong(entity.getProperty("date").toString());
					   Date date=new Date(d);
					   obj.put("date", date);
					   obj.put("likes", Integer.parseInt(entity.getProperty("like").toString()));
					   Query qq=new Query("Comment").setAncestor(entity.getKey()).addSort("Updation_date", SortDirection.DESCENDING).setFilter(del);
					   List<JSONObject> comments=new ArrayList<JSONObject>();
					   for (Entity e : ds.prepare(qq).asIterable()) {
						   if (e!=null && e.getProperty("deleted").toString().equals("false")) 
						   {
							JSONObject commentobj = new JSONObject();
							commentobj.put("feed_id", e.getProperty("feed_id").toString());
							commentobj.put("comment", e.getProperty("comment").toString());
							commentobj.put("comment_id", e.getProperty("comment_id").toString());
							commentobj.put("userId", entity.getProperty("userId").toString());

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
			   
			   JSONObject objResult = new JSONObject();
			   JSONArray cacheData=new JSONArray(feeds);
			   String cursor_string=results.getCursor().toWebSafeString();

			   objResult.put("feeds", feeds);
			   if(cache.contains(category))
			   {
				   String cachedData=cache.get(category).toString();
				   JSONArray arr= new JSONArray(cachedData);
				   List<Object> list = arr.toList();
				   list.addAll(cacheData.toList());
				   cache.put(category,new JSONArray(list).toString());
				   cache.put("cursor", cursor_string);
			   }
			   else
			   {
				   cache.put(category,feeds.toString());
				   cache.put("cursor", cursor_string);
			   }

			   objResult.put("success", true);
			   objResult.put("cursor", cursor_string);
			   objResult.put("code", "200");
			   return objResult;			   
			}
	   }
	                                                                              
	   public String addFeed(Feed f) 
	   {                                 
		   log.info("adding a feed");
		   Entity feed=new Entity("Feed",f.getFeed_id());                       
		   feed.setProperty("feed_id",f.getFeed_id());                          
		   feed.setProperty("feed_content",f.getFeed_content());                 
		   feed.setProperty("category", f.getCategory());            
		   Date date=f.getDate();
		   DateTime d=new DateTime(date);            
		   feed.setProperty("date",d.getMillis());            
		   feed.setProperty("Updation_date",d.getMillis());           
		   feed.setProperty("like", 0);                     
		   feed.setProperty("userId", f.getUserId());
		   feed.setProperty("deleted", false);                       
		   ds.put(feed);
		   MemcacheService cache = MemcacheServiceFactory.getMemcacheService();
		   System.out.println(f.getUserId());                            
		   JSONObject newFeed=new JSONObject();                       
		   newFeed.put("feed_id", f.getFeed_id());                          
		   newFeed.put("userId", f.getUserId());
		   newFeed.put("feed_content", f.getFeed_content());
		   newFeed.put("category", f.getCategory());
		   newFeed.put("date", f.getDate());
		   newFeed.put("likes", 0);
		   newFeed.put("comments",new JSONArray());
		   if(cache.contains(f.getCategory()))
		   {
			   String cat=cache.get(f.getCategory()).toString();
			   System.out.println(cat);
			   JSONArray feeds=new JSONArray(cat);
			   feeds.put(newFeed);
			   System.out.println(feeds.get(feeds.length()-1));
			   //cache.delete(f.getCategory());
               cache.put(f.getCategory(), feeds.toString());	
               		   
		   }
		   else
		   {
			   JSONArray feeds=new JSONArray();
			   feeds.put(newFeed.toString());
			   cache.put(f.getCategory(), feeds.toString());			   

		   }
		   return feed.getProperty("feed_id").toString();
	   }
	   
	   public void updateFeed(Feed f) throws EntityNotFoundException {
		   log.info("updating the feed with id "+f.getFeed_id());
		   MemcacheService cache = MemcacheServiceFactory.getMemcacheService();
		   cache.clearAll();
		   Key k=KeyFactory.createKey("Feed", f.getFeed_id());
		   Entity feed=ds.get(k);
		   Date d1=new Date(Long.parseLong(feed.getProperty("date").toString()));
		   
		   f.setDate(d1);
		   feed.setProperty("feed_id", f.getFeed_id());
		   feed.setProperty("feed_content", f.getFeed_content());
		   feed.setProperty("category", f.getCategory());
		   feed.setProperty("userId",f.getUserId());
		   
		   if(!f.isLike())
		   {
			   Date date=f.getUpdateDate();
			   DateTime d=new DateTime(date);
			   feed.setProperty("Updation_date", d.getMillis());
		   }		   
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
		   log.info("deleting the feed "+f.getFeed_id());
		   Key k=KeyFactory.createKey("Feed",f.getFeed_id());
		   MemcacheService cache = MemcacheServiceFactory.getMemcacheService("cache");
		   Entity e= ds.get(k);
		   cache.delete(e.getProperty("category").toString());
		   Query q = new Query("Comment").setAncestor(e.getKey());
		   for (Entity entity : ds.prepare(q).asIterable()) {	
			   	entity.setProperty("deleted", true);
			   	ds.put(entity);
		   }	
		   e.setProperty("deleted", true); 
		   
		   ds.put(e);
	   }
}
