package com.feed;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter("/SessionController")
public class SessionFilter implements Filter {
    static Logger logger = Logger.getLogger("logger");

    public SessionFilter() {
    }

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        
        logger.info("In session control filter");

        if (session == null || session.getAttribute("userId")==null) {   
        	logger.severe("User not logged in");	
        	req.getRequestDispatcher("/login").forward(request, response);
        } 
        else {
        	logger.info("User logged in and redirected successfully");	
            chain.doFilter(request, response);
        }
	}


	public void init(FilterConfig fConfig) throws ServletException {
	}

}
