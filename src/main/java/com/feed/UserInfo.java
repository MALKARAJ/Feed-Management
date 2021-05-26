package com.feed;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONObject;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.EntityNotFoundException;

/**
 * Servlet implementation class GetUserInfo
 */



@MultipartConfig( fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
maxFileSize = 1024 * 1024 * 10,      // 10 MB
maxRequestSize = 1024 * 1024 * 100 )
@WebServlet("/user")
public class UserInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
   public UserInfo() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out=response.getWriter();
		HttpSession session=request.getSession(false);
        UserDao userOp=new UserOperations();
        response.setContentType("application/JSON");
		if(session!=null)
		{
			String userId=(String) session.getAttribute("userId");
			try {
				JSONObject obj=userOp.getUser(userId);
				JSONObject obj1=new JSONObject();
				response.setStatus(200);
				obj1.put("code",200);
				obj1.put("success",true);
				obj1.put("user", obj);
				out.println(obj1);

				
			} catch (EntityNotFoundException e) {
				JSONObject obj1=new JSONObject();
				response.setStatus(400);
				obj1.put("code",400);
				obj1.put("success",false);
				obj1.put("error", "No user found");
				out.println(obj1);
			}
			
		}
		else
		{
			JSONObject obj1=new JSONObject();
			response.setStatus(500);
			obj1.put("code",500);
			obj1.put("success",false);
			obj1.put("error", "Please login to continue");
			out.println(obj1);		}
	}


}
