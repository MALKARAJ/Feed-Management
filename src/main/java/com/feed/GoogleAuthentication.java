package com.feed;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Date;

import javax.mail.Transport;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import com.google.appengine.repackaged.com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.appengine.repackaged.com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.appengine.repackaged.com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.appengine.repackaged.com.google.api.client.http.HttpTransport;
import com.google.appengine.repackaged.com.google.api.client.json.JsonFactory;
import com.google.appengine.repackaged.org.joda.time.DateTime;




@WebServlet("/google/signin")
public class GoogleAuthentication extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public GoogleAuthentication() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpTransport transport=null;
		JsonFactory jsonFactory=null;
		PrintWriter out=response.getWriter();
		String idTokenString=request.getParameter("idtoken");
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
		    .setAudience(Collections.singletonList("330149014446-918mp1mtuakch6p0vokknncpj276tm1m.apps.googleusercontent.com"))
		    .build();

		GoogleIdToken idToken;
		HttpSession session=request.getSession(true);
		try {
				
			idToken = verifier.verify(idTokenString);
	
			if (idToken != null) {
				
				  Payload payload = idToken.getPayload();
		
				  String userId = payload.getSubject();
				  System.out.println("User ID: " + userId);
		
				  String email = payload.getEmail();
				  String pictureUrl = (String) payload.get("picture");
				  UserDao u=new UserOperations();
				  User user=new User();
				  user.setEmail(email);
				  user.setUserId(userId);
			      DateTime now = new DateTime();
			      Date date=new Date(now.getMillis());
				  user.setDate(date);
				  user.setImage(pictureUrl);
				  user.setActive(true);
				  JSONObject obj=u.addUser(user);
				  session.setAttribute("userId", userId);
				  JSONObject obj1=new JSONObject();
			  	  if(obj!=null) {
						response.setStatus(200);
						obj1.put("success", true);
						obj1.put("code",200);
						obj1.put("detail",obj);
						out.println(obj1);
				  }
			  	  else
			  	  {
						response.setStatus(200);
						obj1.put("success", true);
						obj1.put("code",200);
						obj1.put("message","existing user");
						out.println(obj1);

						
			  	  }

		
			} 
			else 
			{
			  JSONObject obj1=new JSONObject();
			  System.out.println("Invalid ID token.");
			  response.setStatus(400);
			  obj1.put("success", false);
			  obj1.put("code",400);
			  obj1.put("error","Invalid ID token");
   			  out.println(obj1);

				
			}
		
		} catch (GeneralSecurityException | IOException e) {
			
			  JSONObject obj1=new JSONObject();
			  System.out.println("Invalid ID token.");
			  response.setStatus(400);
			  obj1.put("success", false);
			  obj1.put("code",400);
			  obj1.put("error","Security Issues");
 			  out.println(obj1);			
 			  e.printStackTrace();
		}
	}

}
