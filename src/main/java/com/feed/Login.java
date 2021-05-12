package com.feed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;


@WebServlet("/login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public Login() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session2=request.getSession(false);
        if(session2==null ||session2.getAttribute("email")==null)
        {
            request.getRequestDispatcher("/jsp/login.jsp").forward(request,response);

        }
        else
        {
        	response.sendRedirect("/");
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
        User u=new User();
        UserDao user=new UserOperations();
        response.setContentType("application/json");
        u.setEmail(json.get("email").toString());
        u.setPassword(json.get("password").toString());
        if(user.userAuthenticator(u))
        {
            HttpSession session=request.getSession(true);            
            session.setAttribute("userId", u.getUserId());
            JSONObject obj=new JSONObject();
            JSONObject obj1=new JSONObject();
            response.setStatus(200);
            //response.addHeader("Cache-Control", "private,max-age=15552000,must-revalidate");
            obj1.put("email", json.get("email").toString());
            obj.put("success", true);
            obj.put("code", 200);
            obj.put("message", "Login successfull");
            obj.put("user",obj1);
            out.println(obj);
         
        }
        else
        {
            JSONObject obj=new JSONObject();
            response.setStatus(400);
            obj.put("success", false);
            obj.put("code", 400);
            obj.put("message", u.getError());
            out.println(obj);

        }
	}

}
