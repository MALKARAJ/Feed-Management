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

@WebServlet("feed/comments/*")
public class UpdateComment extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public UpdateComment() {
        super();
    }

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		Comment c =new Comment();
		CommentDao comment=new CommentOperations();
		PrintWriter out=response.getWriter();
		String pathInfo = request.getPathInfo(); 
		String[] pathParts = pathInfo.split("/");
		String commentId = pathParts[1];
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
		    
		    c.setComment(json.get("comment").asText());
		    c.setFeed_id(json.get("feedId").asText());
		    c.setComment_id(json.get("commentId").asText());
		    c.setDate(json.get("date").asText());
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
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		String pathInfo = request.getPathInfo(); 
		String[] pathParts = pathInfo.split("/");
		String commentId = pathParts[1];
		PrintWriter out=response.getWriter();
		Comment c=new Comment();
		CommentDao comment=new CommentOperations();
		c.setComment_id(commentId);
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
