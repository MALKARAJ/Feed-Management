package com.feed;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
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
			e.setProperty("userId", user.getUserId().toString());
			e.setProperty("date", new DateTime(user.getDate()).getMillis());
			JSONObject obj=new JSONObject();
			//JSONObject obj1=new JSONObject();
			obj.put("email", user.getEmail());
			if(isValidNewUser(user.getEmail()))
				{
					ds.put(e);
					return obj;
				}
			return null;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	public boolean isValidNewUser(String email) {
		

		DatastoreService ds= DatastoreServiceFactory.getDatastoreService(); 
		Query q=new Query("User");
 	    for (Entity entity : ds.prepare(q).asIterable()) 
 	    {	
 	    	if(entity.getProperty("email").toString().equals(email))
 	    	{
 	    			return false;
 	    		
 	    	}
 	    }
		return true;
	}
	
	
	
	public boolean isValid(String email, String password) {
		
		DatastoreService ds= DatastoreServiceFactory.getDatastoreService(); 
		Query q=new Query("User");
 	    for (Entity entity : ds.prepare(q).asIterable()) 
 	    {	
 	    	if(entity.getProperty("email").toString().equals(email))
 	    	{
 	    		if(Encryption.decrypt(entity.getProperty("password").toString(),"passwordencryptor").equals(Encryption.decrypt(password,"passwordencryptor"))) {
 	    			return true;
 	    		}
 	    	}
 	    }
		return false;
	}
	
	 
}
