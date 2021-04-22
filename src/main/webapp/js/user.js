
var userLogin=()=>{
	var xhr = new XMLHttpRequest();
	xhr.open("POST", "/login", true);
	xhr.setRequestHeader('Content-Type', 'application/json');
	var email=document.getElementById("email").value;
	var pass=document.getElementById("password").value;
	console.log(email);
	console.log(pass);
	var obj={"email":email,"password":pass};
	xhr.send(JSON.stringify(obj));
	xhr.onload = function() {
	  var data = JSON.parse(this.responseText);
	  if(data["success"]==true)
		{
			window.location.href = "/";

		}
	console.log(data);
	}
}


var userRegister=()=>{
	var xhr = new XMLHttpRequest();
	xhr.open("POST", "/register", true);
	xhr.setRequestHeader('Content-Type', 'application/json');
	var email=document.getElementById("email").value;
	var pass=document.getElementById("password").value;
	console.log(email);
	console.log(pass);
	var obj={"email":email,"password":pass};
	console.log(JSON.stringify(obj));
	xhr.send(JSON.stringify(obj));
	xhr.onload = function() 
	{
		var data=JSON.parse(this.responseText);
	    if(data["success"]==true)
		{
			window.location.href = "/login";

		}
		
	}
}




var logout=()=>{
	var xhr = new XMLHttpRequest();
	xhr.open("GET", "/logout", true);
	xhr.setRequestHeader('Content-Type', 'application/json');
	xhr.send();
	xhr.onload= function (){
		var data=JSON.parse(this.responseText);
		if(data["success"]==true)
		{
			window.location.href = "/login";

		}
		
	};

}