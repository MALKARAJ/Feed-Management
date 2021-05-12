package com.feed;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.google.appengine.api.datastore.EntityNotFoundException;

public interface UserDao {
	 
		public boolean userAuthenticator(User u);
		public JSONObject addUser(User user);
}
