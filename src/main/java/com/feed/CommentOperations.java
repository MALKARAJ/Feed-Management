package com.feed;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	   
	   public List<String> getComments(Comment c) throws JsonProcessingException, IOException, EntityNotFoundException{
		   List<String> comments=new ArrayList<String>();
		   Key k=KeyFactory.createKey("Feed",c.getFeed_id());
		   Entity e=ds.get(k);
		   Query q=new Query("Comment").setAncestor(e.getKey());
		   for (Entity entity : ds.prepare(q).asIterable()) {	
			   	c.setFeed_id(entity.getProperty("feed_id").toString());
			   	c.setComment_id(entity.getProperty("comment_id").toString());
			   	c.setDate((Long)entity.getProperty("date"));
			   	c.setComment(entity.getProperty("comment").toString());
			   	c.setLikes(entity.getProperty("like").toString());
			   	ObjectMapper obj=new ObjectMapper();
	            String jsonStr = obj.writeValueAsString(c);
			   	comments.add(jsonStr);
			}	        
		   return comments;
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
