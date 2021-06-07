<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Login</title>
<link rel="stylesheet" href="./css/login.css"/>
<meta name="google-signin-client_id" content="330149014446-918mp1mtuakch6p0vokknncpj276tm1m.apps.googleusercontent.com">
</head>
<body>
	<div class="container">
		<h4>Login</h4>
	 	<input type="text"  id="email" placeholder="Email" onkeydown="errorToggle()"><br>
	 	<input type="password" id="password" onkeydown="errorToggle()" placeholder="Password"><br>
	 	<p id="blank" class="error">Please enter all the fields</p>
	 	<p id="wrong" class="error">Please enter valid email id</p>
	 	<p id="wrongP" class="error">Password should contain 7-14 characters,atleast one letter and one digit</p>

	 	<p id="dbError" class="error"></p>
	 	<input type="button" value="Login" onclick="userLogin()"><br>
	 	<div class="g-signin2" data-onsuccess="onSignIn"></div>
	 	
	 	<a onclick="location.href='/register';">Register</a><br>
	 	
	</div>

		
		<a href="#" onclick="signOut();">Sign out</a>
		<script>
		  function signOut() {
		    var auth2 = gapi.auth2.getAuthInstance();
		    auth2.signOut().then(function () {
		      console.log('User signed out.');
		    });
		  }
		</script>
	  <script src="https://apis.google.com/js/platform.js" async defer></script>
	
	  <script type="text/javascript" src="./js/user.js"></script>
	
</body>
</html>