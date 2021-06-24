package com.feed;

import java.io.IOException;
import java.util.logging.Logger;

import org.json.JSONObject;

import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

public class SyncApp {
	
     URLFetchService fetcher = URLFetchServiceFactory.getURLFetchService(); 
     
	 final String sentKey="9ac80b0c-d004-11eb-b8bc-0242ac130003";
	 final String recieveKey="0115daa6-d00a-11eb-b8bc-0242ac130003";
	 
	 private static final Logger log = Logger.getLogger("logger");	

	 public JSONObject sentRequest(HTTPRequest req) throws IOException
	 {

		JSONObject obj1=new JSONObject();
		int code = 500;
		int retryLimit=3;
		int i=0;
		while(i<retryLimit)
		{
			log.info("registration attempt no : "+ i);
			HTTPResponse res = fetcher.fetch(req);
            code= res.getResponseCode();
            log.info("code :"+code);
			if(code>=200 && code<300)
			{
				
				obj1.put("success", true);
				obj1.put("code",code);
				break;
			}
			else
			{
				i++;
			}
		}
		if(i>2)
		{
			obj1.put("message", "retry limit exeeded");
			obj1.put("success", false);
			obj1.put("code",code);
		}
		return obj1;
	 }
}
