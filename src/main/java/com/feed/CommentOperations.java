package com.feed;



import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

public class CommentOperations implements CommentDao{
	
	   DatastoreService ds= DatastoreServiceFactory.getDatastoreService();
	   
		public JSONObject getSingleComment(Comment c) throws JsonProcessingException, IOException, ParseException, EntityNotFoundException {

			   Key k=new KeyFactory.Builder("Feed", c.getFeed_id())
				        .addChild("Comment", c.getComment_id())
				        .getKey();
			   
			   Entity comment=ds.get(k);
			   JSONObject obj= new JSONObject();
			   obj.put("feedId", c.getFeed_id());
			   obj.put("comment", comment.getProperty("comment").toString());
			   obj.put("commentId", comment.getProperty("comment_id").toString());
			   obj.put("date", comment.getProperty("date"));
			   obj.put("likes", Integer.parseInt(comment.getProperty("like").toString()));
			   return obj;
		}
		
	   public JSONObject getComments(Comment c) throws JsonProcessingException, IOException, EntityNotFoundException, ParseException{
		   List<JSONObject> comments=new ArrayList<JSONObject>();
		   Key k=KeyFactory.createKey("Feed",c.getFeed_id());
		   Entity e=ds.get(k);
		   
		   JSONObject obj= new JSONObject();
		   obj.put("feedId", c.getFeed_id());
		   obj.put("content", e.getProperty("feed_content").toString());
		   obj.put("category", e.getProperty("category").toString());
		   obj.put("date", e.getProperty("date"));
		   obj.put("likes", Integer.parseInt(e.getProperty("like").toString()));
		   
		   Query q=new Query("Comment").setAncestor(e.getKey());
		   for (Entity entity : ds.prepare(q).asIterable()) {	
				/*
				 * c.setFeed_id(entity.getProperty("feed_id").toString());
				 * c.setComment_id(entity.getProperty("comment_id").toString()); String
				 * datetime=entity.getProperty("date").toString(); System.out.println(datetime);
				 * DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy"); Date
				 * date = (Date)formatter.parse(datetime); c.setDate(date);
				 * c.setComment(entity.getProperty("comment").toString());
				 * c.setLikes(Integer.parseInt(entity.getProperty("like").toString()));
				 * ObjectMapper obj=new ObjectMapper(); String jsonStr =
				 * obj.writeValueAsString(c); comments.add(jsonStr);
				 */
			   JSONObject obj1= new JSONObject();
			   obj1.put("feedId", c.getFeed_id());
			   obj1.put("comment", entity.getProperty("comment").toString());
			   obj1.put("commentId", entity.getProperty("comment_id").toString());
			   obj1.put("date", entity.getProperty("date"));
			   obj1.put("likes", Integer.parseInt(entity.getProperty("like").toString()));
			   comments.add(obj1);
			}	  
		   JSONObject objResult= new JSONObject();
		   objResult.put("feed", obj);
		   objResult.put("Comments", comments);
		   return objResult;
	   }
	   
	   public String addComment(Comment c) throws EntityNotFoundException
	   {
		   Key k=KeyFactory.createKey("Feed",c.getFeed_id());
		   Entity e=ds.get(k);
		   Entity comment=new Entity("Comment",c.getComment_id(),e.getKey());
		   comment.setProperty("feed_id",c.getFeed_id());
		   comment.setProperty("comment_id",c.getComment_id());
		   comment.setProperty("comment",c.getComment());
		   comment.setProperty("date", c.getDate());
		   comment.setProperty("like", 0);
		   ds.put(comment);
		   int like=(int) comment.getProperty("like");
		   System.out.println(like);
		   return comment.getProperty("feed_id").toString();
	   }
	   
	   public void updateComment(Comment c) throws EntityNotFoundException {
		   Key k=new KeyFactory.Builder("Feed", c.getFeed_id())
				        .addChild("Comment", c.getComment_id())
				        .getKey();		   
		   Entity comment=ds.get(k);

		   comment.setProperty("feed_id",c.getFeed_id());
		   comment.setProperty("comment_id",c.getComment_id());
		   comment.setProperty("comment",c.getComment());
		   comment.setProperty("date", c.getDate());
		   ds.put(comment);
	   }
	   
	   public void setLike(Comment c) throws EntityNotFoundException
	   {
		   Key k=new KeyFactory.Builder("Feed", c.getFeed_id())
			        .addChild("Comment", c.getComment_id())
			        .getKey();	
		   Entity comment=ds.get(k);	   
		   int like=Integer.parseInt(comment.getProperty("like").toString())+1;
		   comment.setProperty("like", like);
		   ds.put(comment);
	   }
	   
	   public int getLike(Comment c) throws EntityNotFoundException
	   {

		   Key k=new KeyFactory.Builder("Feed", c.getFeed_id())
			        .addChild("Comment", c.getComment_id())
			        .getKey();	
		   Entity comment=ds.get(k);		   
		   return Integer.parseInt(comment.getProperty("like").toString());
		   
	   }	   
	   public void deleteComment(Comment c) throws EntityNotFoundException {
		   Key k=new KeyFactory.Builder("Feed", c.getFeed_id())
			        .addChild("Comment", c.getComment_id())
			        .getKey();	
		   Entity comment=ds.get(k);
		   ds.delete(comment.getKey());   
	   }


}
