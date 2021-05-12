package com.feed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Date;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;
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
		HttpSession session = request.getSession(false);
		if(pathParts.length>2)
		{
			String feedId = pathParts[1];
			String commentId = pathParts[2];
			c.setFeed_id(feedId);
			c.setComment_id(commentId);
			c.setUser_id((String)session.getAttribute("userId"));
			JSONObject comments;
			try {
				comments = comment.getSingleComment(c);
				if (comments.length()>=1) 
				{
					JSONObject j=new JSONObject();
					response.setStatus(200);
					j.put("code", "200");
					j.put("success", true);
					j.put("comment", comments);
					out.println(j);
				}
				else
				{
					response.setStatus(400);
					comments.put("code", "400");
					JSONObject j=new JSONObject();
					j.put("success", false);
					j.put("details","The comment is deleted");
					comments.put("error",j);
					out.println(comments);
				}
			} 
			catch (IOException | EntityNotFoundException e) {
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
				e.printStackTrace();
			}

		}
		else
		{
			String feedId = pathParts[1];
			c.setFeed_id(feedId);
			c.setUser_id((String)session.getAttribute("userId"));

			JSONObject comments;
			try {
				comments = comment.getComments(c);
				if (comments.length()!=0) 
				{
					response.setStatus(200);
					comments.put("code", "200");
					comments.put("success", true);
					out.println(comments);
				}
				else
				{
					response.setStatus(400);
					comments.put("code", "400");
					JSONObject j=new JSONObject();
					j.put("success", false);
					j.put("details","The feed is not found or deleted");
					comments.put("error",j);
					out.println(comments);
				}
			} catch (IOException | EntityNotFoundException e) {
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
		HttpSession session=request.getSession(false);
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
				c.setUser_id((String)session.getAttribute("userId"));
				c.setLikes(0);
				String a=comment.addComment(c);
				if(a.equals("")) {
			    	response.setStatus(400);
			    	JSONObject obj=new JSONObject();
			    	JSONObject obj1=new JSONObject();
					obj1.put("code", "400");
					obj.put("success", false);
					obj1.put("details", "Feed not found or deleted ");
					obj.put("error", obj1);
					out.println(obj);
				}
				else {
			    JSONObject rjson=new JSONObject(c);
			    rjson.remove("like");
				response.setStatus(200);
				JSONObject obj=new JSONObject();
				obj.put("code", "200");
				obj.put("success", true);
				obj.put("comment", rjson);
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
				obj1.put("details", c.getError());
				obj.put("error", obj1);
				out.println(obj);
	    	}
		    
		} catch (Exception e) {
			response.setStatus(500);
	    	JSONObject obj=new JSONObject();
	    	JSONObject obj1=new JSONObject();
			obj1.put("code", "500");
			obj.put("success", false);
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

	try 
    {    
	    StringBuffer jb = new StringBuffer();
	    String line = null;
	    BufferedReader reader = request.getReader();
	    while ((line = reader.readLine()) != null)
	        jb.append(line);
	    String str=jb.toString();
		JSONObject json = new JSONObject(str);
		HttpSession session=request.getSession(false);
    	if(validator.isValidCommentUpdate(json,c))
    	{
			    if(json.get("like").toString().equals("false")) {
			    	
				        DateTime now = new DateTime();
				        Date millis=new Date(now.getMillis());
				        c.setLike(false);
						c.setComment(json.get("comment").toString());
						c.setFeed_id(json.get("feedId").toString());
						c.setUser_id((String)session.getAttribute("userId"));
						c.setComment_id(json.get("commentId").toString());
						c.setDate(millis);
						comment.updateComment(c);
						JSONObject rjson=new JSONObject(c);
						response.setStatus(200);
						JSONObject obj=new JSONObject();
						obj.put("code", "200");
						obj.put("success", true);
						rjson.remove("like");
						obj.put("comment", rjson);
						
						out.println(obj);
		
			    }
			    
			    else if(json.get("like").toString().equals("true"))
			    {
			    		c.setLike(true);
				        DateTime now = new DateTime();
				        Date millis=new Date(now.getMillis());		
						c.setComment(json.get("comment").toString());
						c.setUser_id(json.get("userId").toString());
						c.setFeed_id(json.get("feedId").toString());
						c.setComment_id(json.get("commentId").toString());
						c.setDate(millis);
						comment.setLikePojo(c);
						comment.updateComment(c);
					    JSONObject obj1=new JSONObject(c);
					    obj1.remove("like");
					    JSONObject obj=new JSONObject();
						obj.put("code", "200");
						obj.put("success", true);
						obj.put("comment", obj1);
						response.setStatus(200);
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
			obj1.put("details", c.getError());
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
		obj.put("success", false);
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
		JSONObject deleted=new JSONObject();
		deleted=comment.getSingleComment(c);
		if(deleted.length()>0)
		{
			comment.deleteComment(c);
			response.setStatus(200);
		    JSONObject obj=new JSONObject();
			obj.put("code", "200");
			obj.put("success", true);
			obj.put("comment", deleted);
			response.setStatus(200);
		    out.println(obj);	   
		}
		else
		{
			response.setStatus(500);
	    	JSONObject obj=new JSONObject();
	    	JSONObject obj1=new JSONObject();
			obj1.put("code", "500");
			obj.put("success", false);
			obj1.put("details", "Comment not found or already deleted");
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
    	JSONObject obj=new JSONObject();
    	JSONObject obj1=new JSONObject();
		obj1.put("code", "500");
		obj.put("success", false);
		obj1.put("details", "Server Error");
		obj.put("error", obj1);
		out.println(obj);
		e.printStackTrace();
	}
}


}