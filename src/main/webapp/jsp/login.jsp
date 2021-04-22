<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<link rel="stylesheet" href="./css/login.css"/>
</head>
<body>
	<div class="container">
		<h4>Login</h4>
	 	<input type="text"  id="email" placeholder="Email"><br>
	 	<input type="password" id="password" placeholder="Password"><br>
	 	<input type="button" value="Login" onclick="userLogin()"><br>
	 	<a onclick="location.href='/register';">Register</a><br>
	</div>
	  <script type="text/javascript" src="./js/user.js"></script>
	
</body>
</html>