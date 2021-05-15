package com.feed;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public Worker() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DatastoreService ds= DatastoreServiceFactory.getDatastoreService(); 	    
		String feeds = request.getParameter("key");
		JSONObject obj=new JSONObject(feeds);
        JSONArray arr = obj.getJSONArray("Feeds");
        List<Key> list=new ArrayList<Key>();
        for (int i = 0; i < arr.length(); i++) {
            String feed_id = arr.getJSONObject(i).getString("feedId");
            JSONArray comment=arr.getJSONObject(i).getJSONArray("comments");
            Key k=KeyFactory.createKey("Feed",feed_id);
            for(int j=0;j<comment.length();j++)
            {
	            String comment_id=comment.getJSONObject(i).getString("commentId");
	            Key key=new KeyFactory.Builder("Feed",feed_id)
				        .addChild("Comment", comment_id)
				        .getKey();
	            list.add(key);            	
            }

            list.add(k);
            
        }
        ds.delete(list);
		/*for(int i=0;i<.size();i++)
		{
			Key k=KeyFactory.createKey("Feed",obj.getJSONArray("Feeds").get(i).);
		    ds.delete(k);
		}*/
		
		response.setStatus(200);
	}

}
