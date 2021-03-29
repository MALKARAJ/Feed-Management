package com.feed;



import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class CommentOperations implements CommentDao{
	
	   DatastoreService ds= DatastoreServiceFactory.getDatastoreService();
	   

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
