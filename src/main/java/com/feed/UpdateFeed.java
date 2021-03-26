package com.feed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.api.datastore.EntityNotFoundException;

@WebServlet("feed/*")
public class UpdateFeed extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public UpdateFeed() {
        super();
    }

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Feed f=new Feed();
		FeedDao feed=new FeedOperations();
		PrintWriter out=response.getWriter();
		String pathInfo = request.getPathInfo(); 
		String[] pathParts = pathInfo.split("/");
		String feedId = pathParts[1];
		String req =pathParts[2];
		
	    if(req.equals("feed-task")) {
		    try 
		    {    
			    StringBuffer jb = new StringBuffer();
			    String line = null;
			    BufferedReader reader = request.getReader();
			    while ((line = reader.readLine()) != null)
			        jb.append(line);
			    String str=jb.toString();
			    ObjectMapper mapper = new ObjectMapper();
			    JsonNode json = mapper.readTree(str);
			    
			    f.setFeed_content(json.get("content").asText());
			    f.setFeed_id(json.get("feedId").asText());
			    f.setCategory(json.get("category").asText());
			    f.setDate(json.get("date").asText());
			    
			    feed.updateFeed(f);
			} 
		    catch (EntityNotFoundException e) {
				e.printStackTrace();
			}
	    }
	    else if(req.equals("feed-likes"))
	    {		   
	    	try 
	    	{    
	    		f.setFeed_id(feedId);
			    feed.setLike(f);
			    int likes=feed.getLike(f);
			    out.println(likes);
			} 
		    catch (EntityNotFoundException e) {
				e.printStackTrace();
			}
		    	
	    }
	}

}
