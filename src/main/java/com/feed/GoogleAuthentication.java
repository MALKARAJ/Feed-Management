package com.feed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Date;
import java.util.logging.Logger;

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
import com.google.appengine.repackaged.com.google.api.client.http.javanet.NetHttpTransport;
import com.google.appengine.repackaged.com.google.api.client.json.jackson.JacksonFactory;
import com.google.appengine.repackaged.org.joda.time.DateTime;




@WebServlet("/google")
public class GoogleAuthentication extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger log = Logger.getLogger(FeedOperations.class.getName());	

    public GoogleAuthentication() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        StringBuffer jb = new StringBuffer();
	    PrintWriter out=response.getWriter();
	    String line = null;
	    BufferedReader reader = request.getReader();
	    while ((line = reader.readLine()) != null)
	        jb.append(line);
	    String str=jb.toString();
        JSONObject json=new JSONObject(str);

        
        
        
        
        HttpTransport transport = new NetHttpTransport();
        JacksonFactory jacksonFactory = new JacksonFactory();
        String idTokenString=json.getString("idtoken");
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jacksonFactory)
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
                  
			  	  if(obj!=null) {
			  		   log.info("Signed up succesfully");
			  		  	session.setAttribute("userId", userId);
						response.setStatus(200);
                        response.sendRedirect("/");
				  }
			  	  else
			  	  {
  	                    JSONObject obj2=u.getUserByEmail(email);
  	                    if(obj2!=null)
  	                    {
  	                    	log.info("Log in succesfull");
  				  		  	session.setAttribute("userId", obj2.get("userId").toString());
  							response.setStatus(200);
                            response.sendRedirect("/");
                    	
  	                    }
  	                    else
  	                    {
  	                    	log.severe("Google log in failure");
  	                    }


						
			  	  }

		
			} 
			else 
			{
			  
			  log.severe("Google log in failure due to invalid token");

			  JSONObject obj1=new JSONObject();
			  System.out.println("Invalid ID token.");
			  response.setStatus(400);
			  obj1.put("success", false);
			  obj1.put("code",400);
			  obj1.put("error","Invalid ID token");
   			  out.println(obj1);

				
			}
		
		} catch (GeneralSecurityException | IOException e) {

			  log.severe("Google log in failure due to security exception");

			
			  JSONObject obj1=new JSONObject();
			  System.out.println("Invalid ID token.");
			  response.setStatus(400);
			  obj1.put("success", false);
			  obj1.put("code",400);
			  obj1.put("error","Security Issues");
 			  out.println(obj1);			
 			  e.printStackTrace();
        }
            catch (Exception e) {
             
  			  log.severe("Google log in failure due to server issues");

			
			  JSONObject obj1=new JSONObject();
			  response.setStatus(500);
			  obj1.put("success", false);
			  obj1.put("code",500);
			  obj1.put("error","Exception");
 			  out.println(obj1);			
 			  e.printStackTrace();
		}
    }


}
