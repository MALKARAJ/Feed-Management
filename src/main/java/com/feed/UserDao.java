package com.feed;

import org.json.JSONObject;
import com.google.appengine.api.datastore.EntityNotFoundException;

public interface UserDao {
	 
		public boolean userAuthenticator(User u);
		public JSONObject addUser(User user);
		public JSONObject getUser(String userId) throws EntityNotFoundException;
		public Boolean udpateImage(String email,String userId) throws EntityNotFoundException;
		public Boolean deleteUser(String userId);
		public boolean isValidNewUser(String email);
}
