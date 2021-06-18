package com.feed;

import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

public class SyncApp {
	
	 URLFetchService fetcher = URLFetchServiceFactory.getURLFetchService();
	 final String sentKey="9ac80b0c-d004-11eb-b8bc-0242ac130003";
	 final String recieveKey="0115daa6-d00a-11eb-b8bc-0242ac130003";
}
