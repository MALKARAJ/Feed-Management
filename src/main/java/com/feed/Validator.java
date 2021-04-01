package com.feed;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
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
        if(!(obj.length()==2 && obj.has("content") && obj.has("category")))
        {
        	f.setError("Insuffiencient data");
        	return false;
        }
		if(obj.get("content").toString()=="" || obj.get("category").toString()=="")
		{
			f.setError("Either content or category has a null value");
			return false;
		}
		return true;
		
	}
	public boolean isValidFeedUpdate(JSONObject json,Feed f) throws ParseException, EntityNotFoundException
	{
        
        if(!(json.length()==4 && json.has("feedId") && json.has("content") && json.has("category") && json.has("like")))
        {
			f.setError("Insufficient data");

        	return false;
        }

		if(json.get("content").toString()=="" || json.get("category").toString()=="" || json.get("like").toString()=="" ||json.get("feedId").toString()=="")
		{
			f.setError("Null values present in the request");

			return false;
		}
		
		DatastoreService ds=DatastoreServiceFactory.getDatastoreService();
		Key k=KeyFactory.createKey("Feed", json.get("feedId").toString());
		Entity feed=ds.get(k);
		String feedDate=feed.getProperty("date").toString();
	   	DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
		Date dateFeed = (Date)formatter.parse(feedDate);
        DateTime now = new DateTime();
        Date millis=new Date(now.getMillis());
        
        if(millis.getTime()-dateFeed.getTime()>15000)
        {
			f.setError("Time limit for updation exceeded");

        	return false;
        }
		return true;

		
	}
	public boolean isValidComment(JSONObject json,Comment c)
	{
        if(!(json.length()==2 && json.has("comment") && json.has("feedId")))
        {
        	c.setError("Invalid request");
        	return false;
        }
		if(json.get("comment").toString()=="" ||json.get("feedId").toString()=="")
		{
        	c.setError("Invalid request either comment or feedId is empty");

			return false;
		}
				
		return true;
		
	}
	public boolean isValidCommentUpdate(JSONObject json,Comment c) throws EntityNotFoundException, ParseException
	{

        
        if(!(json.length()==4 && json.has("feedId") && json.has("comment") && json.has("commentId") && json.has("like")))
        {
        	c.setError("Invalid request");

        	return false;
        }
        

		if(json.get("comment").toString()=="" || json.get("commentId").toString()=="" ||json.get("feedId").toString()==""|| json.get("like").toString()=="")
		{
        	c.setError("Null values present in the request");

			return false;
		}
			
		DatastoreService ds=DatastoreServiceFactory.getDatastoreService();
		Key k=new KeyFactory.Builder("Feed", json.get("feedId").toString())
			        .addChild("Comment", json.get("commentId").toString())
			        .getKey();
		Entity comment=ds.get(k);
		String commentDate=comment.getProperty("date").toString();
	   	DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
		Date dateComment = (Date)formatter.parse(commentDate);
        DateTime now = new DateTime();
        Date millis=new Date(now.getMillis());
        
        if(millis.getTime()-dateComment.getTime()>15000)
        {
			c.setError("Time limit for updation exceeded");

        	return false;
        }
		return true;
	}
}
