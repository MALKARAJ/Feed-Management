


var getFeeds=()=>{
	var xhr = new XMLHttpRequest();
	xhr.open("GET", "/feed", true);
	xhr.setRequestHeader('Content-Type', 'application/json');
	xhr.send();
	xhr.onload = function() {
	  var data = JSON.parse(this.responseText);
	  console.log(data);
	  appendData(data);
	}
}
/*
var appendData=(data)=> {
		
		var txt="";
        var mainContainer = document.getElementById("myData");
        for (let i = 0; i < data.feeds.length; i++) 
		{
			txt+="<div class=\"feed\">";
			txt+="<h4>"+data["feeds"][i]["content"]+"</h4>";
			txt+="<div id=\"opBar\" class=\"opBars\">";
			txt+="<img alt=\"like\" src=\"images/like.png\" width=\"20\" height=\"20\" id=\"image\" onclick=\"addLike('"+data["feeds"][i]["feedId"]+"','"+data["feeds"][i]["content"]+"','"+data["feeds"][i]["category"]+"')\"> &nbsp &nbsp";
			txt+=data["feeds"][i]["likes"]+"&nbsp &nbsp";	
			txt+="<a onclick=\"toggle('com"+i+"')\">comments</a><br><br>";
			
			txt+="<div class=\"comments\" id=\"com"+i+"\"  style=\"display:none;\">";
			
			for (let j = 0; j < data.feeds[i].comments.length; j++) 
				{
					txt+="<h4>"+data["feeds"][i]["comments"][j]["comment"]+"</h4>";
					txt+="<div id=\"cOpBar\" class=\"opBars\">";
					txt+="<img alt=\"like\" src=\"images/like.png\" width=\"20\" height=\"20\" id=\"image\" onclick=\"addLike('"+data["feeds"][i]["feedId"]+"','"+data["feeds"][i]["content"]+"','"+data["feeds"][i]["category"]+"')\"> &nbsp &nbsp";
					txt+=data["feeds"][i]["comments"][j]["likes"]+"</div>";
				}	
			txt+="</div></div></div>";
		}
   		mainContainer.innerHTML=txt;
}
*/
var appendData=(data)=> {
		
		var txt="";
        var mainContainer = document.getElementById("myData");
        for (let i = 0; i < data.feeds.length; i++) 
		{
			txt+=`<div class="feed">
				<h4>${data["feeds"][i]["content"]}</h4>
				<div id="opBar" class="opBars">
				<img alt="like" src="images/like.png" width="20" height="20" id="image" onclick="addLike('${data["feeds"][i]["feedId"]}','${data["feeds"][i]["content"]}','${data["feeds"][i]["category"]}')"> &nbsp;
				(${data["feeds"][i]["likes"]}) &nbsp; &nbsp;	
				<a onclick="toggle('com${i}')">comments(${data.feeds[i].comments.length})</a> <br><br></div>
				<div class="comments" id="com${i}"  style="display:none;">`;
			
			for (let j = 0; j < data.feeds[i].comments.length; j++) 
				{
					txt+=`<h4>${data["feeds"][i]["comments"][j]["comment"]}</h4>
						  <div id="cOpBar" class="opBars">
						  <img alt="like" src="images/like.png" width="20" height="20" id="image" onclick="addCommentLike('${data["feeds"][i]["feedId"]}','${data["feeds"][i]["content"]}','${data["feeds"][i]["category"]}')"> &nbsp; &nbsp;
						  ${data["feeds"][i]["comments"][j]["likes"]}</div>`;
				}	
			txt+=`</div></div>`;
			console.log(txt);
		}
   		mainContainer.innerHTML=txt;
}


var addLike=(id,content,category)=>{
		var obj = {"feedId":id,"content":content,"category":category,"like":"true"};
		var xhr = new XMLHttpRequest();
		xhr.open("PUT", "/feed/"+id,true);
		xhr.setRequestHeader('Content-Type', 'application/json');
		xhr.send(JSON.stringify(obj));
		xhr.onload = function() {
			 getFeeds();
		}
}

var toggle=(id)=>{
	console.log("in toogle");
	var div1=document.getElementById(id);
	if(div1.style["display"]=="none")
	{

		div1.style["display"]="block";
	}
	else
	{
		div1.style["display"]="none";
	}
}
