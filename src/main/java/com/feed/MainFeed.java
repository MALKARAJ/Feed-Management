package com.feed;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class MainFeed
 */
@WebServlet("/")
public class MainFeed extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public MainFeed() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session2=request.getSession(false);
        if(session2!=null && session2.getAttribute("userId")!=null)
        {
          request.getRequestDispatcher("jsp/feed.jsp").forward(request, response);

        }
        else
        {
            // response.sendError(400, "not logged in ");
        	response.sendRedirect("/login");
        }
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}