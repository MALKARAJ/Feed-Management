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

@WebServlet("/feed/*")
public class UpdateFeed extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public UpdateFeed() {
        super();
    }

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		Feed f=new Feed();
		FeedDao feed=new FeedOperations();
		PrintWriter out=response.getWriter();
		String pathInfo = request.getPathInfo(); 
		String[] pathParts = pathInfo.split("/");
		String feedId = pathParts[1];
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
			response.setStatus(200);
		    String result="{\"feedId\":\""+feedId+"\",\"Update\" :\"Successful\"}";
			out.println(result);

		} 
	    catch (EntityNotFoundException e) {
	    	response.sendError(500);
			e.printStackTrace();
		}
    }
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		String pathInfo = request.getPathInfo(); 
		String[] pathParts = pathInfo.split("/");
		String feedId = pathParts[1];
		PrintWriter out=response.getWriter();
		Feed f=new Feed();
		FeedDao feed=new FeedOperations();
		f.setFeed_id(feedId);
		try {
			feed.deleteFeed(f);
			response.setStatus(200);
		    String result="{\"feedId\":\""+feedId+"\",\"Delete\" :\"Successful\"}";
		    out.println(result);

		} 
		catch (EntityNotFoundException e) {
			response.setStatus(500);
			e.printStackTrace();
		}
	}


}
