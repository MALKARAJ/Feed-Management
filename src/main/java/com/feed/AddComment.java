package com.feed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet("/feed/comments")
public class AddComment extends HttpServlet {


	private static final long serialVersionUID = 1L;


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
	    try {
			c.setComment(json.get("comment").asText());
			c.setFeed_id(json.get("feedId").asText());
			c.setComment_id(json.get("commentId").asText());
			c.setDate(json.get("date").asText());
			comment.addComment(c);
			response.setStatus(200);
		    String result="{\"commentId\":\""+json.get("comment").asText()+"\",\"Operation\" :\"Successful\"}";
		    out.println(result);
		    
		} catch (Exception e) {
			response.sendError(500);
			e.printStackTrace();
		}
	}
}