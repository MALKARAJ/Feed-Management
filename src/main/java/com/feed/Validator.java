package com.feed;

import java.text.ParseException;
import org.json.JSONObject;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.repackaged.org.joda.time.DateTime;

public class Validator {

	public boolean isValidFeed(JSONObject obj,Feed f)
	{
        if(!(obj.length()==3 && obj.has("userId") && obj.has("content") && obj.has("category")))
        {
        	f.setError("Insuffiencient data");
        	return false;
        }
		if(obj.get("userId").toString().replaceAll("\\s", "").equals("") ||obj.get("content").toString().replaceAll("\\s", "").equals("") || obj.get("category").toString().replaceAll("\\s", "").equals(""))
		{
			f.setError("Either content or category has a null value");
			return false;
		}
		return true;
		
	}
	public boolean isValidFeedUpdate(JSONObject json,Feed f) throws ParseException, EntityNotFoundException
	{
        
        if(!(json.length()==5 && json.has("userId")  &&json.has("feedId") && json.has("content") && json.has("category") && json.has("like")))
        {
			f.setError("Insufficient data");

        	return false;
        }

		if(json.get("userId").toString().replaceAll("\\s", "").equals("")||json.get("content").toString().replaceAll("\\s", "").equals("") || json.get("category").toString().replaceAll("\\s", "").equals("") || json.get("like").toString().replaceAll("\\s", "").equals("") ||json.get("feedId").toString().replaceAll("\\s", "").equals(""))
		{
			f.setError("Null values present in the request");

			return false;
		}
		if(json.get("like").toString().equals("false")){
			
			DatastoreService ds=DatastoreServiceFactory.getDatastoreService();
			Key k=KeyFactory.createKey("Feed", json.get("feedId").toString());
			Entity feed=ds.get(k);
			long feedDate=Long.parseLong(feed.getProperty("date").toString());

	        DateTime now = new DateTime();
	        
	        if(now.getMillis()-feedDate>60000)
	        {
				f.setError("Time limit for updation exceeded");

	        	return false;
	        }
		}
		

		return true;

		
	}
	public boolean isValidComment(JSONObject json,Comment c)
	{
        if(!(json.length()==3 && json.has("userId")  && json.has("comment") && json.has("feedId")))
        {
        	c.setError("Invalid request");
        	return false;
        }
		if(json.get("userId").toString().replaceAll("\\s", "").equals("") || json.get("comment").toString().replaceAll("\\s", "").equals("") ||json.get("feedId").toString().replaceAll("\\s", "").equals(""))
		{
        	c.setError("Invalid request either comment or feedId is empty");

			return false;
		}
				
		return true;
		
	}
	public boolean isValidCommentUpdate(JSONObject json,Comment c) throws EntityNotFoundException, ParseException
	{

        
        if(!(json.length()==5 && json.has("userId")   && json.has("feedId") && json.has("comment") && json.has("commentId") && json.has("like")))
        {
        	c.setError("Invalid request");

        	return false;
        }
        

		if(json.get("userId").toString().replaceAll("\\s", "").equals("") || json.get("comment").toString().replaceAll("\\s", "").equals("") || json.get("commentId").toString().replaceAll("\\s", "").equals("") ||json.get("feedId").toString().replaceAll("\\s", "").equals("")|| json.get("like").toString().replaceAll("\\s", "").equals(""))
		{
        	c.setError("Null values present in the request");

			return false;
		}
		if(json.get("like").toString().equals("false"))
		{

			DatastoreService ds=DatastoreServiceFactory.getDatastoreService();
			Key k=new KeyFactory.Builder("Feed", json.get("feedId").toString())
				        .addChild("Comment", json.get("commentId").toString())
				        .getKey();
			Entity comment=ds.get(k);
			long feedDate=Long.parseLong(comment.getProperty("date").toString());
	
	        DateTime now = new DateTime();
	        if(now.getMillis()-feedDate>60000)
	        {
				c.setError("Time limit for updation exceeded");
	
	        	return false;
	        }
	        
		}
		return true;
	
	}
}
