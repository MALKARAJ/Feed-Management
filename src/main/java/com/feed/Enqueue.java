package com.feed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PropertyProjection;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.QueryResultList;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

@WebServlet(    name = "TaskEnque",
				description = "taskqueue: Enqueue a job with a key",
				urlPatterns = "/enqueue")
public class Enqueue extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public Enqueue() {
        super();
    }


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    
		try {
		    StringBuffer jb = new StringBuffer();
		    PrintWriter out=response.getWriter();
		    String line = null;
		    BufferedReader reader = request.getReader();
		    while ((line = reader.readLine()) != null)
		        jb.append(line);
		    String str=jb.toString();

	        JSONObject json=new JSONObject(str);
	        System.out.println(json);
		    Queue queue = QueueFactory.getDefaultQueue();
		    queue.add(TaskOptions.Builder.withUrl("/worker").param("key", json.toString()));


		} 
		catch (JSONException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
	    }

}
