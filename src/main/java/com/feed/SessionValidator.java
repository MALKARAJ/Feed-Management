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

@WebFilter("/SessionValidator")
public class SessionValidator implements Filter {

    static Logger logger = Logger.getLogger("logger");

    public SessionValidator() {
    }

	public void destroy() {
	}
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        if(session!=null && session.getAttribute("userId")!=null)
        {
        	logger.info("Session exist and preparing to process the request");
    		chain.doFilter(request, response);
        }
        else
        {
        	logger.info("Invalid session");

        	res.sendError(401);
        }
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}

}
