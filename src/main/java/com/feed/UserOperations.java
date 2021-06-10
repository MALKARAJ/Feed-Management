package com.feed;


import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;

import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;

import com.google.appengine.repackaged.org.joda.time.DateTime;
import com.google.appengine.api.datastore.Query.FilterPredicate;
public class UserOperations implements UserDao{


	
	
	public JSONObject addUser(User user)
	{
		
		DatastoreService ds= DatastoreServiceFactory.getDatastoreService();
		try {
			System.out.println(user.getUserId());
			Entity e= new Entity("User",user.getUserId());
			e.setProperty("email", user.getEmail());
			e.setProperty("password", user.getPassword());
			e.setProperty("image", user.getImage());
			e.setProperty("active", user.getActive());
			e.setProperty("date", new DateTime(user.getDate()).getMillis());
			JSONObject obj=new JSONObject();
			//JSONObject obj1=new JSONObject();
			obj.put("email", user.getEmail());
			if(isValidNewUser(user.getEmail()))
				{
					ds.put(e);
					return obj;
				}
			else
			{
				e=null;
				obj=null;
			}
			return null;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	public boolean isValidNewUser(String email) {
		

		DatastoreService ds= DatastoreServiceFactory.getDatastoreService(); 
		Filter mailConstraint = new FilterPredicate("email", FilterOperator.EQUAL,email);

		Query q=new Query("User").setFilter(mailConstraint);
 	    Entity entity=ds.prepare(q).asSingleEntity();

		if(entity==null)
		{
			return true;
		}
 	  
		return false;
	}
	
	
	
	public boolean userAuthenticator(User u) {
		DatastoreService ds= DatastoreServiceFactory.getDatastoreService(); 
		Filter mailConstraint = new FilterPredicate("email", FilterOperator.EQUAL,u.getEmail());
		Query q=new Query("User").setFilter(mailConstraint);
 	    Entity entity=ds.prepare(q).asSingleEntity();

 	   if(entity==null)
		{
	 		u.setError("Email does not exist");
			return false;

		}
 	   if((Boolean)entity.getProperty("active"))
 	   {
	 	   if (BCrypt.checkpw(u.getPassword(), entity.getProperty("password").toString()))
	 		{
	 			u.setUserId(entity.getKey().getName());
	 			return true;
	 		}
	 		else
	 		{
	 			u.setError("Password doesn't match the email");
	 			return false;
	 		}
 	   }
		u.setError("Account is not active");
		return false;
	}
	@Override
	public JSONObject getUser(String userId) throws EntityNotFoundException {
		DatastoreService ds= DatastoreServiceFactory.getDatastoreService(); 

		Key k=KeyFactory.createKey("User", userId);
		Entity entity=ds.get(k);
		if(entity!=null) {
			
			JSONObject obj= new JSONObject();
	  	    obj.put("email",entity.getProperty("email").toString());
			obj.put("image", entity.getProperty("image").toString());
			obj.put("active",(boolean) entity.getProperty("active"));
			obj.put("userId", entity.getKey().getName());
			long d=Long.parseLong(entity.getProperty("date").toString());
			Date date=new Date(d);
			obj.put("date", date);
			return obj;
			
		}
		else
		{
			return null;
		}

	}
	@Override
	public Boolean udpateImage(String userId,String name) throws EntityNotFoundException {
		
		DatastoreService ds= DatastoreServiceFactory.getDatastoreService(); 
		System.out.println(userId);
		Key k=KeyFactory.createKey("User", userId);
		Entity entity=ds.get(k);
		if(entity!=null) {
			entity.setProperty("image",name);
			ds.put(entity);
			return true;
		}
		else
		{
			return false;
		}	
	}
	
	
	public Boolean deleteUser(String userId)
	{
		DatastoreService ds= DatastoreServiceFactory.getDatastoreService(); 
  	    try {
			Key k=KeyFactory.createKey("User", userId);
			ds.delete(k);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	@Override
	public JSONObject getUserByEmail(String email) throws EntityNotFoundException {

		DatastoreService ds= DatastoreServiceFactory.getDatastoreService(); 
		Filter mailConstraint = new FilterPredicate("email", FilterOperator.EQUAL,email);
		Query q=new Query("User").setFilter(mailConstraint);
 	    Entity entity=ds.prepare(q).asSingleEntity();
		
		
		if(entity!=null) {
			
			JSONObject obj= new JSONObject();
	  	    obj.put("email",entity.getProperty("email").toString());
			obj.put("image", entity.getProperty("image").toString());
			obj.put("active",(boolean) entity.getProperty("active"));
			obj.put("userId", entity.getKey().getName());
			long d=Long.parseLong(entity.getProperty("date").toString());
			Date date=new Date(d);
			obj.put("date", date);
			return obj;
			
		}
		else
		{
			return null;
		}
	}
	
}
