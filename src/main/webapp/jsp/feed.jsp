<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>


<%
//response.setHeader("Cache-Control", "private,max-age=15000");
HttpSession session1 =request.getSession(false);
System.out.println(session1.getAttribute("userId"));
if (session1!=null && session1.getAttribute("userId")!=null ){
%>

<head>
<meta charset="ISO-8859-1">

<meta http-equiv="Cache-control" content="private">
<title>Feed Management</title>
<link rel="stylesheet" href="./css/feeds.css">
<link rel="stylesheet" href="./css/profile.css">
</head>
<body  onload="getFeeds()">
	<div class="container">
			<div class="sideBar">
				<div class="imageHover" id="profileButton">
				<img src="/images/profile.png" height="50px" width="50px" onclick="getProfile();setActive('profileButton');toggleProfile();" />
				</div>
				<div class="imageHover active" id="feedButton">
				<img src="/images/feed.png" height="50px" width="50px" onclick="getFeeds();setActive('feedButton');toggleToFeed();"/>
				</div>
			</div>
			<div class="feedContainer" id="feedContainer">
			<div class="profileHeader" id="profileHeader"></div>
			 <div class="headerContainer" id="headerContainer">
			  <div class="title"><h1>Feeds</h1></div>
			  <img alt="add" src="images/plus.png" width="50" height="50" class="add" id="add"  style="display:block;" onclick="toggleAdd()">
			  <img alt="sub" src="images/sub.png" width="40" height="50" class="sub" id="sub"  style="display:none;" onclick="toggleAdd()"><br><br>
			  <input type="hidden" id="userId" value="<%out.print(session1.getAttribute("userId"));%>"/>
			  <div class="addData" id="adder" style="display:none;">
			  		<textarea class="addFeed" id="addFeed" placeholder="Want to share anything?"></textarea>
			  		<div class="data">
			  		    <label for="category">Category:</label>
			
						<select name="category" id="cat">
						  <option value="Music">Music</option>
						  <option value="Movies">Movies</option>
						  <option value="Technology">Technology</option>
						  <option value="Sports">Sports</option>
						</select>
			  			<input type="button" value="share" onclick="addFeed()">
			  		</div>
			   </div>
			    <div class="projection">
			
				  <img src="images/delete.png" onclick="getDeletedFeeds()" id="bin" width="20" height="20">&nbsp;
				  <input type="button" style="display:block;" onclick="add1000Feeds('<%out.print(session1.getAttribute("userId"));%>')" value="addFeeds"/>
				  &nbsp;<input type="button" onclick="showMessage(),deleteAllFeeds()" value="Delete All"/>
				  <input type="checkbox" onclick="selectFeed()" name="selection" id="selection"/>
				  
				  <div class="dataPro">
				  	    <label for="category">Sort by:</label>
						<select name="category" id="sort">
						  <option value="All">All</option>
						  <option value="Music">Music</option>
						  <option value="Movies">Movies</option>
						  <option value="Technology">Technology</option>
						  <option value="Sports">Sports</option>
						</select>
				  		<input type="button" value="search" onclick="getCategoryFeed()"/>
				  		<input type="button" id="logout" value="logout" onclick="logout()"/>
				  		
				  </div>
				 
			
			 </div>
			
			  <p id="message" style="display:none;">Your feeds will be deleted</p>
			  <br>
			   <div class="feed" id="noFeed" style="text-align:center" style="display:none;"><h4>There are no feeds to display</h4></div>
			  </div>
			  <div id="myData"></div>
			  <div id="myProfile"></div>
			    <img src="/images/arrow.png" id="arrow"  onclick="window.scroll(0,1);">
		  
		  <div id="load_more"></div>
		  </div>

  </div>
  
  <script type="text/javascript" src="./js/Cache.js"></script>  
  <script type="text/javascript" src="./js/feedOperation.js"></script>
  <script type="text/javascript" src="./js/feedOperation.test.js"></script>
    <script type="text/javascript" src="./js/profileOperation.js"></script>
  
  <script type="text/javascript" src="./js/user.js"></script>
  <script type="text/javascript" src="./js/commentOperation.js"></script>
  <img src="images/like.png"  id="bin" width="20" height="20" style="display:none">
  <img src="images/comment.png"  id="bin" width="20" height="20" style="display:none">
    
  
</body>
</html>
<%}
else{
	response.sendRedirect("/login");
	} %>
	
	
	
	
	
