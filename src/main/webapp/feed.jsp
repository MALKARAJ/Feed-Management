<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Feed Management</title>
<link rel="stylesheet" href="feeds.css">
</head>
<body  onload="getFeeds()">
  <div class="title"><h1>Feeds</h1></div>
  <img alt="like" src="images/plus.png" width="50" height="50" class="add" id="add"  style="display:block;" onclick="toggleAdd()">
  <img alt="like" src="images/sub.png" width="40" height="50" class="sub" id="sub"  style="display:none;" onclick="toggleAdd()"><br><br>

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
	  <img src="images/delete.png" onclick="getDeletedFeeds()" id="bin" width="20" height="20">
	  <div class="dataPro">
	  	    <label for="category">Sort by:</label>
			<select name="category" id="sort">
			  <option value="All">All</option>
			  <option value="Music">Music</option>
			  <option value="Movies">Movies</option>
			  <option value="Technology">Technology</option>
			  <option value="Sports">Sports</option>
			</select>
	  		<input type="button" value="search" onclick="getCategoryFeed()">
	  </div>
 </div>
  
  <div id="myData"></div>
  <script type="text/javascript" src="feedOperation.js"></script>
  <script type="text/javascript" src="commentOperation.js"></script>
</body>
</html>
