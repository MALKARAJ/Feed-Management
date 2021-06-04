package com.feed;

import java.io.BufferedReader;
import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

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
		    String line = null;
		    BufferedReader reader = request.getReader();
		    while ((line = reader.readLine()) != null)
		        jb.append(line);
		    String str=jb.toString();

	        JSONObject json=new JSONObject(str);
            System.out.println(json);
            Queue queue = QueueFactory.getQueue("delete");
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
