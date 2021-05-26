package com.feed;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.EntityNotFoundException;

@WebServlet("/upload")
public class UploadImage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        UserDao userOp=new UserOperations();

		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
		List<BlobKey> blobKeys = blobs.get("img");
		String userId=request.getSession().getAttribute("userId").toString();
		if (blobKeys == null || blobKeys.isEmpty()) {
			System.out.println("in if");
			
		    response.sendRedirect("/");
		} 
		else {
			System.out.println("in else");
			try {		
				userOp.udpateImage(userId,blobKeys.get(0).getKeyString());
			} 
			catch (EntityNotFoundException e) {
				e.printStackTrace();
			}
		    response.sendRedirect("/");
		}
		
     
	}

}
