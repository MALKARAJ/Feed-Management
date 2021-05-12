package com.feed;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;


@WebServlet("/worker")
public class Worker extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public Worker() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DatastoreService ds= DatastoreServiceFactory.getDatastoreService(); 	    
		String feeds = request.getParameter("key");
		feeds = feeds.replaceAll("[\\[\\](){}]","");
		String[] feed=feeds.split(", ");
		for(int i=0;i<feed.length;i++)
		{
			Key k=KeyFactory.createKey("Feed", feed[i]);
		    ds.delete(k);
		}
		response.setStatus(200);

	}

}
