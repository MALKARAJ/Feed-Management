package com.feed;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

@WebServlet("/logout")
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public Logout() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out=response.getWriter();

		try {
			HttpSession session=request.getSession(false);
			session.invalidate();
			JSONObject obj=new JSONObject();
			response.setStatus(200);
			obj.put("success", true);
			obj.put("code", 200);
			obj.put("message", "Logout successful");
			out.println(obj);
		} catch (JSONException e) {
			JSONObject obj=new JSONObject();
			response.setStatus(500);
			obj.put("success", false);
			obj.put("code", 500);
			obj.put("message", "Logout Unsuccessful due to JSON exception");
			out.println(obj);
			e.printStackTrace();
		}
		catch(Exception e)
		{
			JSONObject obj=new JSONObject();
			response.setStatus(500);
			obj.put("success", false);
			obj.put("code", 500);
			obj.put("message", "Logout Unsuccessful");
			out.println(obj);
		}
	}

}
