package com.feed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.api.datastore.EntityNotFoundException;

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
		String feedId = pathParts[1];
		c.setFeed_id(feedId);
		System.out.println(feedId);
		List<String> comments;
		try {
			comments = comment.getComments(c);
			response.setStatus(200);
			out.println(comments);
		} catch (IOException | EntityNotFoundException e) {
			response.setStatus(500);
			e.printStackTrace();
		}

	}
	
@Override
public void doPost(HttpServletRequest request, HttpServletResponse response) 
    throws IOException 
	{
		response.setContentType("application/json");
		Comment c=new Comment();
		PrintWriter out=response.getWriter();
		CommentDao comment=new CommentOperations();
	    StringBuffer jb = new StringBuffer();
	    String line = null;
	    BufferedReader reader = request.getReader();
	    while ((line = reader.readLine()) != null)
	        jb.append(line);
	    String str=jb.toString();
	    ObjectMapper mapper = new ObjectMapper();
	    JsonNode json = mapper.readTree(str);
    	UUID commentId=UUID.randomUUID();
    	

	    try {
	    	Date date=new Date();
	    	Long millis = date.getTime();

			c.setComment(json.get("comment").asText());
			c.setFeed_id(json.get("feedId").asText());
			c.setComment_id(commentId.toString());
			c.setDate(millis);
			comment.addComment(c);
			response.setStatus(200);
		    String result="{\"commentId\":\""+c.getComment_id()+"\",\"Operation\" :\"Successful\"}";
		    out.println(result);
		    
		} catch (Exception e) {
			response.sendError(500);
			e.printStackTrace();
		}
	}


@Override
protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	response.setContentType("application/json");
	Comment c =new Comment();
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
	    ObjectMapper mapper = new ObjectMapper();
	    JsonNode json = mapper.readTree(str);
	    
    	Date date=new Date();
    	Long millis = date.getTime();
    	
	    c.setComment(json.get("comment").asText());
	    c.setFeed_id(json.get("feedId").asText());
	    c.setComment_id(json.get("commentId").asText());
	    c.setDate(millis);
	   
	    comment.updateComment(c);
	    String result="{\"commentId\":\""+commentId+"\",\"Update\" :\"Successful\"}";
		response.setStatus(200);
		out.println(result);

	} 
    catch (EntityNotFoundException e) {
    	response.sendError(500);
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
	    String result="{\"commentId\":\""+commentId+"\",\"Delete\" :\"Successful\"}";
	    out.println(result);

	} 
	catch (EntityNotFoundException e) {
		response.setStatus(500);
		e.printStackTrace();
	}
}


}