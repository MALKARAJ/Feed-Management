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

import org.mindrot.jbcrypt.BCrypt;


@WebFilter("/RegisterFilter")
public class RegisterFilter implements Filter {
    static Logger logger = Logger.getLogger("logger");

    public RegisterFilter() {
    }


	public void destroy() {
	}


	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        if(req.getMethod().equalsIgnoreCase("POST"))
        {
	
	        SyncApp sync= new SyncApp();
	        String Origin=req.getHeader("Origin");
	      
	        if(Origin.equals("http://localhost:8080") || Origin.equals("https://georgefulltraining12.uc.r.appspot.com"))
	        {
	            logger.info("register API request from same-origin");
	
	    		chain.doFilter(request, response);
	
	        }
	        
	        else 
	        {
	        	
	        	String token=req.getHeader("Authorization");
	        	if(BCrypt.checkpw(sync.recieveKey, token))
	        	{
	                logger.info("Authorization succesfull");
	        		chain.doFilter(request, response);
	        	}
	        	else
	        	{
	                logger.severe("register API request from unknown device");
	
	        		res.sendError(HttpServletResponse.SC_FORBIDDEN);
	        	}
	        	
	        }

        }
        else
        {
        	chain.doFilter(request, response);

        }


	}


	public void init(FilterConfig fConfig) throws ServletException {
	}

}
