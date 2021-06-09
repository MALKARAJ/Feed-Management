package com.task;



import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;


@WebServlet("/worker")
public class Worker extends HttpServlet {
	private static final long serialVersionUID = 1L;
   	private static final Logger log = Logger.getLogger(Worker.class.getName());	

    public Worker() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("inside worker class");
		DatastoreService ds= DatastoreServiceFactory.getDatastoreService(); 	    
    try
    {
        String feeds = request.getParameter("key");
		JSONObject obj=new JSONObject(feeds);
        JSONArray arr = obj.getJSONArray("Feeds");
        List<Key> list=new ArrayList<Key>();
        List<Key> list1=new ArrayList<Key>();
        for (int i = 0; i < arr.length(); i++) {
            String feed_id = arr.getJSONObject(i).getString("feedId");
            JSONArray comment=arr.getJSONObject(i).getJSONArray("comments");
           // System.out.println(comment);
            Key k=KeyFactory.createKey("Feed",feed_id);
            for(int j=0;j<comment.length();j++)
            {
	            String comment_id=comment.getJSONObject(j).getString("commentId");
	            Key key=new KeyFactory.Builder("Feed",feed_id)
				        .addChild("Comment", comment_id)
				        .getKey();
	            list1.add(key);            	
            }

            list.add(k);
            
        }
        ds.delete(list);
        ds.delete(list1);

        log.info("Deletion is successfull");
     	response.setStatus(HttpServletResponse.SC_OK);

    }
    catch(Exception e)
    {
        log.info("message :"+e);
     	response.setStatus(500);

    }
	}

}
