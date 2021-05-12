package com.feed;

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
		    FetchOptions fetchOptions = FetchOptions.Builder.withLimit(100);
		    Queue queue = QueueFactory.getDefaultQueue();
			DatastoreService ds= DatastoreServiceFactory.getDatastoreService(); 
			HttpSession session=request.getSession(false);
			Filter cat = new FilterPredicate("userId", FilterOperator.EQUAL,session.getAttribute("userId"));
			Filter del = new FilterPredicate("delete", FilterOperator.EQUAL,false);
			CompositeFilter catdel =   CompositeFilterOperator.and(cat, del);
			Query q=new Query("Feed").setFilter(catdel);		
			q.addProjection(new PropertyProjection("feed_id",String.class));
		    String currentCursorString = "";
		    List<String> list=new ArrayList<String>();
		    JSONObject obj=new JSONObject();
		    QueryResultList<Entity> results = ds.prepare(q).asQueryResultList(fetchOptions);
		    String nextCursorString = results.getCursor().toWebSafeString();	

		    while(results.size()!=0)
		    {
		    	for(Entity e:results)
		    	{
		    		list.add(e.getProperty("feed_id").toString());
		    	}
		    	//obj.put("feedId", list);
		    	queue.add(TaskOptions.Builder.withUrl("/worker").param("key", list.toString()));
		    	//obj.clear();
		    	list.clear();
		        if (nextCursorString != null) {
		            fetchOptions.startCursor(Cursor.fromWebSafeString(nextCursorString));
		          }
		    	results = ds.prepare(q).asQueryResultList(fetchOptions);
		    	currentCursorString=nextCursorString;
			    nextCursorString = results.getCursor().toWebSafeString();	
		    }
			response.sendRedirect("/");

			/*PrintWriter out=response.getWriter();	
			JSONObject obj1=new JSONObject();
			obj1.put("success",true);
			obj1.put("code", 200);
			obj1.put("message", "deletion added to queue");
			out.println(obj1);*/
		} 
		catch (JSONException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
	    }

}
