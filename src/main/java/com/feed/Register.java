package com.feed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
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

import com.google.appengine.repackaged.org.joda.time.DateTime;

import at.favre.lib.crypto.bcrypt.BCrypt;


@WebServlet("/register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public Register() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session=request.getSession(false);  

		if(session.getAttribute("email")==null) {
			request.getRequestDispatcher("/jsp/register.jsp").forward(request,response);
		}
		else
		{
			request.getRequestDispatcher("/jsp/feed.jsp");
		}
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
        UserDao userOp=new UserOperations();
        response.setContentType("application/json");
        User user=new User();
        try {
	    	UUID id=UUID.randomUUID();
	    	String email=json.get("email").toString();
			String pass=Encryption.encrypt(json.get("password").toString(),"passwordencryptor");
	        DateTime now = new DateTime();
	        Date date=new Date(now.getMillis());
			user.setEmail(email);

			user.setPassword(pass);
			user.setUserId(id.toString());
			user.setDate(date);
			JSONObject obj=userOp.addUser(user);
			JSONObject obj1=new JSONObject();

			if(obj!=null) {
				response.setStatus(200);
				obj1.put("success", true);
				obj1.put("code",200);
				obj1.put("detail",obj);
			}
			else
			{	
					obj1.put("message", "User already present");
					response.setStatus(400);
					obj1.put("success", false);
					obj1.put("code",400);
			}
			out.println(obj1);
		} catch (JSONException q) {
            JSONObject obj=new JSONObject();
            response.setStatus(400);
            obj.put("success", false);
            obj.put("code", 400);
            obj.put("message", "Invalid user");
            out.println(obj);

			q.printStackTrace();
		}
              
	}

}
