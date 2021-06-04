package com.feed;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import javax.cache.CacheException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.core.JsonProcessingException;
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
		HttpSession session=request.getSession(false);

		try {
			
				if(pathInfo!=null)
				{
					String[] pathParts = pathInfo.split("/");
					FeedDao feed=new FeedOperations();
					Feed f=new Feed();
					
						List<String> path=new ArrayList<String>();
						path=Arrays.asList(pathParts);
						
						if(path.contains("trash"))
						{
							f.setUserId((String)session.getAttribute("userId"));
							List<JSONObject> result;
							result = feed.getDeletedFeeds(f);
							if (result.size()>0) 
							{
								JSONObject obj = new JSONObject();
								response.setStatus(200);
								obj.put("success", true);
								obj.put("code", "200");
								obj.put("feeds", result);
								out.println(obj);
							}
							else
							{
								JSONObject obj=new JSONObject();
								JSONObject obj1=new JSONObject();
								response.setStatus(400);
								obj.put("success", false);
								obj1.put("code","400");
								obj1.put("detail", "No feeds present at the moment");
								obj.put("error", obj1);
								out.println(obj);
							}
						}
						else if(path.contains("category"))
						{
							//List<JSONObject> result;
							//result = feed.getCategoryFeeds(pathParts[2]);
							String cursor=request.getParameter("cursor");

							JSONObject result = feed.getCategoryFeeds(pathParts[2],cursor);
							if (result.get("feeds")!=null) 
							{
								//JSONObject obj = new JSONObject();
								response.setStatus(200);
								//obj.put("success", true);
								//obj.put("code", "200");
								//obj.put("feeds", result);
								out.println(result);
							}
							else
							{
								JSONObject obj=new JSONObject();
								JSONObject obj1=new JSONObject();
								response.setStatus(400);
								obj.put("success", false);
								obj1.put("code","400");
								obj1.put("detail", "No feeds present at the moment");
								obj.put("error", obj1);
								out.println(obj);
							}
						}
						else
						{				
							f.setFeed_id(pathParts[1]);
							JSONObject obj=new JSONObject();
							JSONObject result=feed.getSingleFeed(f);
							if(result.length()>0) 
	     					{
								response.setStatus(200);
								obj.put("success", true);
								obj.put("code","200");
								obj.put("feed", result);
								out.println(obj);
							}
							else
							{
								response.setStatus(400);
								obj.put("success", false);
								result.put("code","400");
								result.put("detail", "Feed not found or deleted");
								obj.put("error", result);
								out.println(obj);
							}
					    }

			
				} 
				
				else
				{
					FeedDao feed=new FeedOperations();
		
					JSONObject result;
					String cursor=request.getParameter("cursor");
					result = feed.getNewsFeeds(cursor);
					if (result.get("feeds")!=null) 
						{
						//	JSONObject obj = new JSONObject();
							response.setStatus(200);
							//response.addHeader("Cache-Control", "private,max-age=15552000,must-revalidate");
						//	obj.put("success", true);
						//	obj.put("code", "200");
						//	obj.put("feeds", result);
							out.println(result);
						}
						else
						{
							JSONObject obj=new JSONObject();
							JSONObject obj1=new JSONObject();
							response.setStatus(400);
							obj.put("success", false);
							obj1.put("code","400");
							obj1.put("detail", "No feeds present at the moment");
							obj.put("error", obj1);
							out.println(obj);
						}
					
				}
		}
		 catch (JsonProcessingException e) {
			e.printStackTrace();
			response.setStatus(500);
	    	JSONObject obj=new JSONObject();
	    	JSONObject obj1=new JSONObject();
			obj1.put("code", "500");
			obj.put("success", false);
			obj1.put("details", "Server error");
			obj.put("error", obj1);
			out.println(obj);
		} catch (IOException e) {
			response.setStatus(500);
	    	JSONObject obj=new JSONObject();
	    	JSONObject obj1=new JSONObject();
			obj1.put("code", "500");
			obj.put("success", false);
			obj1.put("details", "Server error");
			obj.put("error", obj1);
			out.println(obj);
			e.printStackTrace();
		} catch (ParseException e) {
			response.setStatus(500);
	    	JSONObject obj=new JSONObject();
	    	JSONObject obj1=new JSONObject();
			obj1.put("code", "500");
			obj.put("success", false);
			obj1.put("details", "Server error");
			obj.put("error", obj1);
			out.println(obj);
			e.printStackTrace();
			
		} catch (EntityNotFoundException e) {
			response.setStatus(500);
	    	JSONObject obj=new JSONObject();
	    	JSONObject obj1=new JSONObject();
			obj1.put("code", "500");
			obj.put("success", false);
			obj1.put("details", "Server error");
			obj.put("error", obj1);
			out.println(obj);
			e.printStackTrace();
		} catch (CacheException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

        JSONObject json=new JSONObject(str);
	    DateTime now = new DateTime();
        Date millis=new Date(now.getMillis());
	    if(validator.isValidFeed(json,f)) {
		    try 
		    {
		    	
		    	UUID id=UUID.randomUUID();
		    	HttpSession session=request.getSession(false);	    	
				f.setFeed_content(json.get("content").toString());
				f.setFeed_id(id.toString());
				f.setCategory(json.get("category").toString());
				f.setDate(millis);
				f.setLikes(0);
				f.setUserId((String)session.getAttribute("userId"));
				feed.addFeed(f);
				response.setStatus(200);
			    JSONObject rjson=new JSONObject(f);
				response.setStatus(200);
				JSONObject obj=new JSONObject();
				obj.put("code", "200");
				obj.put("success", true);
				rjson.remove("like");
				obj.put("data", rjson);
				out.println(obj);
			} 
		    catch (Exception e) 
		    {
				response.setStatus(500);
		    	JSONObject obj=new JSONObject();
		    	JSONObject obj1=new JSONObject();
				obj1.put("code", "500");
				obj.put("success", false);
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
			obj.put("success", false);
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
	HttpSession session=request.getSession(false);	    	

	PrintWriter out=response.getWriter();

	try 
    {    
	    StringBuffer jb = new StringBuffer();
	    String line = null;
	    BufferedReader reader = request.getReader();
	    while ((line = reader.readLine()) != null)
	        jb.append(line);
	    String str=jb.toString();

        JSONObject json=new JSONObject(str);


        DateTime now = new DateTime();
        Date millis=new Date(now.getMillis());
    	if(validator.isValidFeedUpdate(json,f)) 
    	{
		        if(json.has("like") && json.get("like").equals("false")) 
			    {
		
		
					    f.setFeed_content(json.get("content").toString());
					    f.setFeed_id(json.get("feedId").toString());
					    f.setCategory(json.get("category").toString());
					    f.setUpdateDate(millis);
					    f.setUserId((String)session.getAttribute("userId"));
			    		f.setLike(false);
					    feed.updateFeed(f);
					    JSONObject rjson=new JSONObject(f);
					    System.out.println(rjson);
						response.setStatus(200);
						JSONObject obj=new JSONObject();
						obj.put("code", "200");
						obj.put("success", true);
						rjson.remove("like");
						obj.put("feed", rjson);
						out.println(obj);
		
			    }
			    else if (json.has("like") && json.get("like").equals("true"))
			    {
		    		f.setLike(true);
				    f.setFeed_content(json.get("content").toString());
				    f.setFeed_id(json.get("feedId").toString());
				    f.setCategory(json.get("category").toString());
				    f.setUserId(json.get("userId").toString());

				    feed.setLikePojo(f);
				    feed.updateFeed(f);
				    
				    
				    JSONObject rjson=new JSONObject(f);
					response.setStatus(200);
					JSONObject obj=new JSONObject();
					obj.put("code", "200");
					obj.put("success", true);
					rjson.remove("like");
					obj.put("feed", rjson);
					out.println(obj);
			    }
			    else
			    {
			    	response.setStatus(400);
			    	JSONObject obj=new JSONObject();
			    	JSONObject obj1=new JSONObject();
					obj1.put("code", "400");
					obj.put("success", false);
					obj1.put("details", "Invalid request");
					obj.put("error", obj1);
					out.println(obj);
			    }
    	}
    	else
    	{
	    	response.setStatus(400);
	    	JSONObject obj=new JSONObject();
	    	JSONObject obj1=new JSONObject();
			obj1.put("code", "400");
			obj.put("success", false);
			obj1.put("details", f.getError());
			obj.put("error", obj1);
			out.println(obj);
    	}

	} 
    catch (EntityNotFoundException e) {
    	response.setStatus(500);
    	JSONObject obj=new JSONObject();
    	JSONObject obj1=new JSONObject();
		obj1.put("code", "500");
		obj.put("success", false);
		obj1.put("details", "Enitity not found");
		obj.put("error", obj1);
		out.println(obj);		
		e.printStackTrace();
	} catch (ParseException e) {
		response.setStatus(500);
    	JSONObject obj=new JSONObject();
    	JSONObject obj1=new JSONObject();
		obj1.put("code", "500");
		obj.put("success", false);
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
    	JSONObject deleted=new JSONObject();
    	deleted=feed.getSingleFeed(f);
    	if(deleted.length()!=0) {
			feed.deleteFeed(f);
			response.setStatus(200);
	    	JSONObject obj=new JSONObject();
			obj.put("code", "200");
			obj.put("success", true);
			obj.put("feed", deleted);
			out.println(obj);	    
    	}
    	else
    	{
        	JSONObject obj=new JSONObject();
        	JSONObject obj1=new JSONObject();
    		obj1.put("code", "400");
    		obj.put("success", false);
    		obj1.put("details", "Feed not found or already deleted");
    		obj.put("error", obj1);
    	}

	} 
	catch (EntityNotFoundException e) {
		response.setStatus(500);
    	JSONObject obj=new JSONObject();
    	JSONObject obj1=new JSONObject();
		obj1.put("code", "500");
		obj.put("success", true);
		obj1.put("details", "Enitity not found");
		obj.put("error", obj1);
		e.printStackTrace();
	} catch (ParseException e) {
		response.setStatus(500);
    	JSONObject obj=new JSONObject();
    	JSONObject obj1=new JSONObject();
		obj1.put("code", "500");
		obj.put("success", false);
		obj1.put("details", "Server error");
		obj.put("error", obj1);
		e.printStackTrace();
	} catch (CacheException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}