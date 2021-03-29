package com.feed;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.EntityNotFoundException;

@WebServlet("/feed/comments/likes/*")
public class CommentLikes extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public CommentLikes() {
        super();
    }

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Comment c=new Comment();
		response.setContentType("application/json");
		CommentDao comment=new CommentOperations();
		PrintWriter out=response.getWriter();
		String pathInfo = request.getPathInfo(); 
		String[] pathParts = pathInfo.split("/");
		String feedId = pathParts[1];
		String commentId=pathParts[2];
		//System.out.println(feedId);
		//System.out.println(commentId);
		try 
    	{    
			c.setFeed_id(feedId);
    		c.setComment_id(commentId);
		    comment.setLike(c);
		    int l=comment.getLike(c);
		    String like="{\"commentId\":\""+commentId+"\",\"likes\" :\""+ l+"\"}";
			response.setStatus(200);
		    out.println(like);
		} 
	    catch (EntityNotFoundException e) {
			response.sendError(500);
			e.printStackTrace();
		}	
	    }

}
