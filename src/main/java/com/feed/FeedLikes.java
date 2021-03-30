package com.feed;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.EntityNotFoundException;

@WebServlet("/feed/likes/*")
public class FeedLikes extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public FeedLikes() {
        super();
    }

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		Feed f=new Feed();
		response.setContentType("application/json");
		FeedDao feed=new FeedOperations();
		PrintWriter out=response.getWriter();
		String pathInfo = request.getPathInfo(); 
		String[] pathParts = pathInfo.split("/");
		String feedId = pathParts[1];
		System.out.println(feedId);
		try 
    	{    
    		f.setFeed_id(feedId);
		    feed.setLike(f);
		    int l=feed.getLike(f);
		    String like="{\"feedId\":\""+feedId+"\",\"likes\" :\""+ l+"\"}";
			response.setStatus(200);
		    out.println(like);
		} 
	    catch (EntityNotFoundException e) 
		{
			response.sendError(500);
			e.printStackTrace();
		}	
	}

}
