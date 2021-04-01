package com.feed;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.repackaged.org.joda.time.DateTime;

@WebServlet("/feed/*")
public class FeedServlet extends HttpServlet {


	private static final long serialVersionUID = 1L;

@Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException 
	{
		response.setContentType("application/json");
		PrintWriter out=response.getWriter();
		String pathInfo = request.getPathInfo(); 
		System.out.println(pathInfo);
		
		if(pathInfo!=null)
		{
			String[] pathParts = pathInfo.split("/");
			FeedDao feed=new FeedOperations();
			Feed f=new Feed();
			f.setFeed_id(pathParts[1]);
			try {

				JSONObject obj=new JSONObject();
				JSONObject result=feed.getSingleFeed(f);
				response.setStatus(200);
				obj.put("status", "success");
				obj.put("code","200");
				obj.put("feed", result);
				out.println(obj);
			} 
			
			catch (JsonProcessingException e) {
				response.setStatus(500);
		    	JSONObject obj=new JSONObject();
		    	JSONObject obj1=new JSONObject();
				obj1.put("code", "500");
				obj.put("status", "failed");
				obj1.put("details", "Server error");
				obj.put("error", obj1);
				e.printStackTrace();
			} 
			catch (IOException e) {
				response.setStatus(500);
		    	JSONObject obj=new JSONObject();
		    	JSONObject obj1=new JSONObject();
				obj1.put("code", "500");
				obj.put("status", "failed");
				obj1.put("details", "Server error");
				obj.put("error", obj1);				
				e.printStackTrace();
			}
			catch (ParseException e) {
				response.setStatus(500);
		    	JSONObject obj=new JSONObject();
		    	JSONObject obj1=new JSONObject();
				obj1.put("code", "500");
				obj.put("status", "failed");
				obj1.put("details", "Server error");
				obj.put("error", obj1);
				e.printStackTrace();
			} 
			catch (EntityNotFoundException e) {
				response.setStatus(500);
		    	JSONObject obj=new JSONObject();
		    	JSONObject obj1=new JSONObject();
				obj1.put("code", "500");
				obj.put("status", "failed");
				obj1.put("details", "Enitity not found");
				obj.put("error", obj1);
				out.println(obj);
				e.printStackTrace();
			}
		}
		else
		{
			FeedDao feed=new FeedOperations();
			try {
				JSONObject obj=new JSONObject();
				List<JSONObject> result=feed.getNewsFeeds();
				response.setStatus(200);
				obj.put("status", "success");
				obj.put("code","200");
				obj.put("feeds", result);

				out.println(obj);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				response.setStatus(500);
		    	JSONObject obj=new JSONObject();
		    	JSONObject obj1=new JSONObject();
				obj1.put("code", "500");
				obj.put("status", "failed");
				obj1.put("details", "Server error");
				obj.put("error", obj1);
				out.println(obj);
			} catch (IOException e) {
				response.setStatus(500);
		    	JSONObject obj=new JSONObject();
		    	JSONObject obj1=new JSONObject();
				obj1.put("code", "500");
				obj.put("status", "failed");
				obj1.put("details", "Server error");
				obj.put("error", obj1);
				out.println(obj);
				e.printStackTrace();
			} catch (ParseException e) {
				response.setStatus(500);
		    	JSONObject obj=new JSONObject();
		    	JSONObject obj1=new JSONObject();
				obj1.put("code", "500");
				obj.put("status", "failed");
				obj1.put("details", "Server error");
				obj.put("error", obj1);
				out.println(obj);
				e.printStackTrace();
			}
		}

	

	}
@Override
public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException 
	{
		response.setContentType("application/json");
		Feed f=new Feed();
		Validator validator=new Validator();
		FeedDao feed=new FeedOperations();
	    StringBuffer jb = new StringBuffer();
	    PrintWriter out=response.getWriter();
	    String line = null;
	    BufferedReader reader = request.getReader();
	    while ((line = reader.readLine()) != null)
	        jb.append(line);
	    String str=jb.toString();
	    //ObjectMapper mapper = new ObjectMapper();
	    //JsonNode json = mapper.readTree(str);
        JSONObject json=new JSONObject(str);
	    DateTime now = new DateTime();
        Date millis=new Date(now.getMillis());
	    if(validator.isValidFeed(json,f)) {
		    try 
		    {
		    	
		    	UUID id=UUID.randomUUID();

				f.setFeed_content(json.get("content").toString());
				f.setFeed_id(id.toString());
				f.setCategory(json.get("category").toString());
				f.setDate(millis);
				f.setLikes(0);
				feed.addFeed(f);
				response.setStatus(200);
			    JSONObject rjson=new JSONObject(f);
				response.setStatus(200);
				JSONObject obj=new JSONObject();
				obj.put("code", "200");
				obj.put("status", "success");
				obj.put("data", rjson);
				out.println(obj);
			} 
		    catch (Exception e) 
		    {
				response.setStatus(500);
		    	JSONObject obj=new JSONObject();
		    	JSONObject obj1=new JSONObject();
				obj1.put("code", "500");
				obj.put("status", "failed");
				obj1.put("details", "Server error");
				obj.put("error", obj1);
				out.println(obj);
				e.printStackTrace();
			}
	    }
	    else
	    {
	    	response.setStatus(400);
	    	JSONObject obj=new JSONObject();
	    	JSONObject obj1=new JSONObject();

			obj1.put("code", "400");
			obj.put("status", "failed");
			obj1.put("details", f.getError());
			obj.put("error", obj1);
			out.println(obj);
	    }
	}
@Override
protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	response.setContentType("application/json");
	Feed f=new Feed();
	FeedDao feed=new FeedOperations();
	Validator validator=new Validator();
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
	    //ObjectMapper mapper = new ObjectMapper();
	    //JsonNode json = mapper.readTree(str);
        JSONObject json=new JSONObject(str);


        DateTime now = new DateTime();
        Date millis=new Date(now.getMillis());

        if(json.has("like") && json.get("like").equals("false")) 
	    {

	    	if(validator.isValidFeedUpdate(json,f)) 
	    	{
			    f.setFeed_content(json.get("content").toString());
			    f.setFeed_id(json.get("feedId").toString());
			    f.setCategory(json.get("category").toString());
			    f.setDate(millis);
			    feed.updateFeed(f);
			    JSONObject rjson=new JSONObject(f);
				response.setStatus(200);
				JSONObject obj=new JSONObject();
				obj.put("code", "200");
				obj.put("status", "success");
				obj.put("data", rjson);
				out.println(obj);
	    	}
	    	else
	    	{
		    	response.setStatus(400);
		    	JSONObject obj=new JSONObject();
		    	JSONObject obj1=new JSONObject();
				obj1.put("code", "400");
				obj.put("status", "failed");
				obj1.put("details", f.getError());
				obj.put("error", obj1);
				out.println(obj);
	    	}
	    }
	    else if (json.has("like") && json.get("like").equals("true"))
	    {
    		f.setFeed_id(feedId);
		    feed.setLike(f);
		    int l=feed.getLike(f);
		    JSONObject obj1=new JSONObject();

		    JSONObject obj=new JSONObject();
			obj.put("code", "200");
			obj.put("status", "success");
			obj1.put("feedId", feedId);
			obj1.put("likes", l);
			obj.put("data", obj1);
			response.setStatus(200);
		    out.println(obj);
	    }
	    else
	    {
	    	response.setStatus(400);
	    	JSONObject obj=new JSONObject();
	    	JSONObject obj1=new JSONObject();
			obj1.put("code", "400");
			obj.put("status", "failed");
			obj1.put("details", "Invalid request");
			obj.put("error", obj1);
			out.println(obj);
	    }

	} 
    catch (EntityNotFoundException e) {
    	response.setStatus(500);
    	JSONObject obj=new JSONObject();
    	JSONObject obj1=new JSONObject();
		obj1.put("code", "500");
		obj.put("status", "failed");
		obj1.put("details", "Enitity not found");
		obj.put("error", obj1);
		out.println(obj);		
		e.printStackTrace();
	} catch (ParseException e) {
		response.setStatus(500);
    	JSONObject obj=new JSONObject();
    	JSONObject obj1=new JSONObject();
		obj1.put("code", "500");
		obj.put("status", "failed");
		obj1.put("details", "Server error");
		obj.put("error", obj1);
		out.println(obj);
		e.printStackTrace();
	} 
}
@Override
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
    	JSONObject obj=new JSONObject();
		obj.put("code", "200");
		obj.put("status", "success");
		obj.put("feedId", feedId);
		out.println(obj);	    

	} 
	catch (EntityNotFoundException e) {
		response.setStatus(500);
    	JSONObject obj=new JSONObject();
    	JSONObject obj1=new JSONObject();
		obj1.put("code", "500");
		obj.put("status", "failed");
		obj1.put("details", "Enitity not found");
		obj.put("error", obj1);
		e.printStackTrace();
	}
}
}