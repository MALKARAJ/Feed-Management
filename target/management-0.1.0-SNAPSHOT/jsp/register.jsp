<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Register</title>
<link rel="stylesheet" href="./css/login.css"/>

</head>
<body>
	<div class="container">
		<h4>Register</h4>
	 	<input type="text"  id="email" onkeydown="errorToggle()" placeholder="Email"><br>
	 	<input type="password" id="password" placeholder="Password" onkeydown="errorToggle()" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}" title="Must contain at least one number and one uppercase and lowercase letter, and at least 8 or more characters" required ><br>
	 	<p id="wrong" class="error">Please enter valid email id</p>
	 	<p id="wrongP" class="error">Password should contain 7-14 characters,atleast one letter and one digit</p>

	 	<p id="blank" class="error">Please enter all the details</p>
	 	<p id="dbError" class="error"></p>
	 	<input type="button" value="Register" onclick="userRegister()"><br>
	 	<a onclick="location.href='/login';">Login</a><br>
	</div>
	  <script type="text/javascript" src="./js/user.js"></script>
	
</body>
</html>