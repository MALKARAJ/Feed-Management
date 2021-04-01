package com.feed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.repackaged.org.joda.time.DateTime;

@WebServlet("/feed/comments/*")
public class CommentServlet extends HttpServlet {


	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		Comment c =new Comment();
		CommentDao comment=new CommentOperations();
		PrintWriter out=response.getWriter();
		String pathInfo = request.getPathInfo(); 
		String[] pathParts = pathInfo.split("/");
		System.out.println(pathParts.length);
		for(String i:pathParts)
		{
			System.out.println(i);
		}
		if(pathParts.length>2)
		{
			String feedId = pathParts[1];
			String commentId = pathParts[2];
			c.setFeed_id(feedId);
			c.setComment_id(commentId);
			JSONObject comments;
			try {
				comments = comment.getSingleComment(c);
				JSONObject obj=new JSONObject();
				response.setStatus(200);
				obj.put("status", "success");
				obj.put("code","200");
				obj.put("comment", comments);
				out.println(obj);
			} catch (IOException | EntityNotFoundException e) {
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
				e.printStackTrace();
			}

		}
		else
		{
			String feedId = pathParts[1];
			c.setFeed_id(feedId);
			JSONObject comments;
			try {
				comments = comment.getComments(c);
				response.setStatus(200);
				comments.put("status", "success");
				comments.put("code","200");
				out.println(comments);
			} catch (IOException | EntityNotFoundException e) {
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

	}
	
@Override
public void doPost(HttpServletRequest request, HttpServletResponse response) 
    throws IOException 
	{
		response.setContentType("application/json");
		Comment c=new Comment();
		Validator validator=new Validator();
		PrintWriter out=response.getWriter();
		CommentDao comment=new CommentOperations();
	    StringBuffer jb = new StringBuffer();
	    String line = null;
	    BufferedReader reader = request.getReader();
	    while ((line = reader.readLine()) != null)
	        jb.append(line);
	    String str=jb.toString();
		JSONObject json = new JSONObject(str);

    	UUID commentId=UUID.randomUUID();
    	

	    try {
	        DateTime now = new DateTime();
	        Date millis=new Date(now.getMillis());
	    	if(validator.isValidComment(json,c)) 
	    	{
				c.setComment(json.get("comment").toString());
				c.setFeed_id(json.get("feedId").toString());
				c.setComment_id(commentId.toString());
				c.setDate(millis);
				c.setLikes(0);
				comment.addComment(c);
			    JSONObject rjson=new JSONObject(c);
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
				obj1.put("details", c.getError());
				obj.put("error", obj1);
				out.println(obj);
	    	}
		    
		} catch (Exception e) {
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


@Override
protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	response.setContentType("application/json");
	Comment c =new Comment();
	Validator validator=new Validator();
	CommentDao comment=new CommentOperations();
	PrintWriter out=response.getWriter();
	String pathInfo = request.getPathInfo(); 
	String[] pathParts = pathInfo.split("/");
	String feedId = pathParts[1];
	String commentId=pathParts[2];
	try 
    {    
	    StringBuffer jb = new StringBuffer();
	    String line = null;
	    BufferedReader reader = request.getReader();
	    while ((line = reader.readLine()) != null)
	        jb.append(line);
	    String str=jb.toString();
		JSONObject json = new JSONObject(str);

	    if(json.get("like").equals("false")) {
	    	
		        DateTime now = new DateTime();
		        Date millis=new Date(now.getMillis());
		    	if(validator.isValidCommentUpdate(json,c))
		    	{
				    c.setComment(json.get("comment").toString());
				    c.setFeed_id(json.get("feedId").toString());
				    c.setComment_id(json.get("commentId").toString());
				    c.setDate(millis);
				   
				    comment.updateComment(c);
				    JSONObject rjson=new JSONObject(c);
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
					obj1.put("details", c.getError());
					obj.put("error", obj1);
					out.println(obj);
		    	}
	    }
	    
	    else if(json.get("like").equals("true"))
	    {
				c.setFeed_id(feedId);
	    		c.setComment_id(commentId);
			    comment.setLike(c);
			    int l=comment.getLike(c);
			    JSONObject obj=new JSONObject();
				obj.put("code", "200");
				obj.put("status", "success");
				obj.put("commentId", commentId);
				obj.put("likes", l);
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
    catch (EntityNotFoundException | JSONException | ParseException e) 
	{
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

@Override
protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	response.setContentType("application/json");
	String pathInfo = request.getPathInfo(); 
	String[] pathParts = pathInfo.split("/");
	String feedId = pathParts[1];
	String commentId=pathParts[2];
	PrintWriter out=response.getWriter();
	Comment c=new Comment();
	CommentDao comment=new CommentOperations();
	c.setComment_id(commentId);
	c.setFeed_id(feedId);
	try {
		comment.deleteComment(c);
		response.setStatus(200);
	    JSONObject obj=new JSONObject();
		obj.put("code", "200");
		obj.put("status", "success");
		obj.put("commentId", commentId);
		response.setStatus(200);
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
		out.println(obj);
		e.printStackTrace();
	}
}


}