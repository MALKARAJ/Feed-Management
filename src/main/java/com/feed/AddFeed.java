package com.feed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
//import java.util.Date;
//import java.util.UUID;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet("/feed")
public class AddFeed extends HttpServlet {


	private static final long serialVersionUID = 1L;

@Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException 
	{
		response.setContentType("application/json");
		PrintWriter out=response.getWriter();
		FeedDao feed=new FeedOperations();
		try {
			List<String> result=feed.getNewsFeeds();
			response.setStatus(200);
			out.println(result);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			response.sendError(500);
		} catch (IOException e) {
			response.sendError(500);
			e.printStackTrace();
		}
	

	}
@Override
public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException 
	{
		response.setContentType("application/json");
		Feed f=new Feed();
		FeedDao feed=new FeedOperations();
	    StringBuffer jb = new StringBuffer();
	    String line = null;
	    BufferedReader reader = request.getReader();
	    while ((line = reader.readLine()) != null)
	        jb.append(line);
	    String str=jb.toString();
	    ObjectMapper mapper = new ObjectMapper();
	    JsonNode json = mapper.readTree(str);
	    try {
			f.setFeed_content(json.get("content").asText());
			f.setFeed_id(json.get("feedId").asText());
			f.setCategory(json.get("category").asText());
			f.setDate(json.get("date").asText());
			feed.addFeed(f);
			response.setStatus(200);
		} catch (Exception e) {
			response.sendError(500);
			e.printStackTrace();
		}
	}
}