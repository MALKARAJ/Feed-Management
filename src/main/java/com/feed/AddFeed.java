package com.feed;

import java.io.BufferedReader;
import java.io.IOException;
//import java.util.Date;
//import java.util.UUID;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet(
    name = "newsfeed",
    urlPatterns = {"/feed"}
)
public class AddFeed extends HttpServlet {


	private static final long serialVersionUID = 1L;

@Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException {

    response.setContentType("text/plain");
    response.setCharacterEncoding("UTF-8");


  }
@Override
public void doPost(HttpServletRequest request, HttpServletResponse response) 
    throws IOException {
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
    f.setFeed_content(json.get("content").asText());
    f.setFeed_id(json.get("feedId").asText());
    f.setCategory(json.get("category").asText());
    f.setDate(json.get("date").asText());
    feed.addFeed(f);
}
}