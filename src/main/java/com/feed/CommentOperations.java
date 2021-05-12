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
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.repackaged.org.joda.time.DateTime;

public class CommentOperations implements CommentDao{
	
	   DatastoreService ds= DatastoreServiceFactory.getDatastoreService();
	   
		public JSONObject getSingleComment(Comment c) throws JsonProcessingException, IOException, ParseException, EntityNotFoundException {

			   Key k=new KeyFactory.Builder("Feed", c.getFeed_id())
				        .addChild("Comment", c.getComment_id())
				        .getKey();
			   
			   Entity comment=ds.get(k);
			   JSONObject obj= new JSONObject();

			   if(comment.getProperty("delete").toString().equals("false")) 
			   {
				   obj.put("feedId", c.getFeed_id());
				   obj.put("comment", comment.getProperty("comment").toString());
				   obj.put("commentId", comment.getProperty("comment_id").toString());
				   obj.put("userId", comment.getProperty("userId"));
				   long d=Long.parseLong(comment.getProperty("date").toString());
				   Date date=new Date(d);
				   obj.put("date", date);
				   obj.put("likes", Integer.parseInt(comment.getProperty("like").toString()));
			   }
			   return obj;

		}
		
	   public JSONObject getComments(Comment c) throws JsonProcessingException, IOException, EntityNotFoundException, ParseException{
		   List<JSONObject> comments=new ArrayList<JSONObject>();
		   Key k=KeyFactory.createKey("Feed",c.getFeed_id());
		   Entity e=ds.get(k);
		   JSONObject obj= new JSONObject();
		   if(e.getProperty("delete").toString().equals("false")) 
		   {
			   obj.put("feedId", c.getFeed_id());
			   obj.put("content", e.getProperty("feed_content").toString());
			   obj.put("category", e.getProperty("category").toString());
			   long d=Long.parseLong(e.getProperty("date").toString());
			   obj.put("userId", e.getProperty("userId"));

			   Date date=new Date(d);
			   obj.put("date", date);
			   obj.put("likes", Integer.parseInt(e.getProperty("like").toString()));
			   Filter delete = new FilterPredicate("delete", FilterOperator.EQUAL,false);

			   Query q=new Query("Comment").setAncestor(e.getKey()).addSort("Updation_date", SortDirection.DESCENDING).setFilter(delete);
			   for (Entity entity : ds.prepare(q).asIterable()) {	
					   JSONObject obj1= new JSONObject();
					   obj1.put("feedId", c.getFeed_id());
					   obj1.put("comment", entity.getProperty("comment").toString());
					   obj1.put("commentId", entity.getProperty("comment_id").toString());
					   obj1.put("userId", entity.getProperty("userId"));

					   long d1=Long.parseLong(entity.getProperty("date").toString());
					   Date date1=new Date(d1);
					   obj1.put("date", date1);
					   obj1.put("likes", Integer.parseInt(entity.getProperty("like").toString()));
					   comments.add(obj1);
				   }	  
			   obj.put("comments", comments);
			   JSONObject objResult= new JSONObject();
			   objResult.put("feed", obj);
			   return objResult;
		   }

		   return obj;
	   }
	   
	   public String addComment(Comment c) throws EntityNotFoundException
	   {
		   
		   Key k=KeyFactory.createKey("Feed",c.getFeed_id());
		   Entity e=ds.get(k);
		   if(e.getProperty("delete").toString().equals("false")) {
			   Entity comment=new Entity("Comment",c.getComment_id(),e.getKey());
			   comment.setProperty("feed_id",c.getFeed_id());
			   comment.setProperty("comment_id",c.getComment_id());
			   comment.setProperty("comment",c.getComment());
			   comment.setProperty("userId", c.getUser_id());
			   Date date=c.getDate();
			   DateTime d=new DateTime(date);
			   comment.setProperty("date",d.getMillis());
			   comment.setProperty("Updation_date",d.getMillis());		  
			   comment.setProperty("like", 0);
			   comment.setProperty("delete", false);
			   ds.put(comment);
			   int like=(int) comment.getProperty("like");
			   return comment.getProperty("feed_id").toString();
		   }
		   return "";
	   }
	   
	   public void updateComment(Comment c) throws EntityNotFoundException {
		   Key k=new KeyFactory.Builder("Feed", c.getFeed_id())
				        .addChild("Comment", c.getComment_id())
				        .getKey();		   
		   Entity comment=ds.get(k);
		   if(comment.getProperty("delete").toString().equals("false")) {
	
			   comment.setProperty("feed_id",c.getFeed_id());
			   comment.setProperty("comment_id",c.getComment_id());
			   comment.setProperty("userId", c.getUser_id());

			   comment.setProperty("comment",c.getComment());

			   if(!c.isLike())
			   {
				   Date date=c.getUpdateDate();
				   DateTime d=new DateTime(date);
				   comment.setProperty("Updation_date", d.getMillis());
			   }
			   if(c.isLike())
			   {
				   comment.setProperty("like", c.getLikes()+1);
				   c.setLikes(Integer.parseInt(comment.getProperty("like").toString()));
	
			   }
			   ds.put(comment);
		   }
	   }
	   
	   public void setLikePojo(Comment c) throws EntityNotFoundException
	   {
		   Key k=new KeyFactory.Builder("Feed", c.getFeed_id())
			        .addChild("Comment", c.getComment_id())
			        .getKey();	
		   Entity comment=ds.get(k);	   
		   int like=Integer.parseInt(comment.getProperty("like").toString());
		   c.setLikes(like);
	   }
	      
	   public void deleteComment(Comment c) throws EntityNotFoundException {
		   Key k=new KeyFactory.Builder("Feed", c.getFeed_id())
			        .addChild("Comment", c.getComment_id())
			        .getKey();	
		   Entity comment=ds.get(k);
		   comment.setProperty("delete", true);
		   ds.put(comment);
	   }
}
