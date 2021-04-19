
var getFeeds=()=>{
	var xhr = new XMLHttpRequest();
	xhr.open("GET", "/feed", true);
	xhr.setRequestHeader('Content-Type', 'application/json');
	xhr.send();
	xhr.onload = function() {
	  var data = JSON.parse(this.responseText);
	  toggleFeed("bin");
	  appendData(data);
	}
}


var getCategoryFeed=()=>{
	
	var xhr = new XMLHttpRequest();
	var category=document.getElementById("sort").selectedOptions[0].value;
	if(category==="All")
	{
		getFeeds();
	}
	else
	{
		xhr.open("GET", "/feed/category/"+category, true);
		xhr.setRequestHeader('Content-Type', 'application/json');
		xhr.send();
		xhr.onload = function() {
		  var data = JSON.parse(this.responseText);
		  toggleFeed("bin");
		  appendData(data);
		}
	}
}


var getDeletedFeeds=()=>{
	var xhr = new XMLHttpRequest();
	xhr.open("GET", "/feed/trash", true);
	xhr.setRequestHeader('Content-Type', 'application/json');
	xhr.send();
	xhr.onload = function() {
	  var data = JSON.parse(this.responseText);
	  toggleBin("bin");
	  appendDeletedData(data);
	}
}


var addFeed=()=>{
		var content=document.getElementById("addFeed").value;
		var category=document.getElementById("cat").selectedOptions[0].value;
		content=content.replace(/(\n)/gm," ");
		var obj = {"content":content,"category":category};
		var xhr = new XMLHttpRequest();
		xhr.open("POST", "/feed",true);
		xhr.setRequestHeader('Content-Type', 'application/json');
		xhr.send(JSON.stringify(obj));
		xhr.onload = function() {
			 document.getElementById("addFeed").value="";
			 getFeeds();
		}
	
}


var updateFeed=(id,cat)=>{
	//var content=document.getElementById("updateText"+id).value;
	var lis = document.getElementById("datac"+id).getElementsByTagName("li")[0];
	//var category=document.getElementById("cat"+id).selectedOptions[0].value;
	lis=lis.innerText.replace(/(\n)/gm," ");
	var obj = {"feedId":id,"content":lis,"category":cat,"like":"false"};
	console.log(obj);
	var xhr = new XMLHttpRequest();
	xhr.open("PUT", "/feed",true);
	xhr.setRequestHeader('Content-Type', 'application/json');
	xhr.send(JSON.stringify(obj));
	xhr.onload = function() {
		 getFeeds();
	}
}


var addLike=(id,content,category)=>{
		var obj = {"feedId":id,"content":content,"category":category,"like":"true"};
		var xhr = new XMLHttpRequest();
		xhr.open("PUT", "/feed",true);
		xhr.setRequestHeader('Content-Type', 'application/json');
		xhr.send(JSON.stringify(obj));
		xhr.onload = function() {
			 getFeeds();
		}
}


var deleteFeed=(id)=>{
		var xhr = new XMLHttpRequest();
		xhr.open("DELETE", "/feed/"+id,true);
		xhr.setRequestHeader('Content-Type', 'application/json');
		xhr.send();
		xhr.onload = function() {
			 getFeeds();
		}
}




var appendData=(data)=> {
		
		var txt="";
        var mainContainer = document.getElementById("myData");
		if(data.code=="200")
		{
        for (let i = 0; i < data.feeds.length; i++) 
		{
			txt+=`<div class="feed" id="${data["feeds"][i]["feedId"]}">
						<div id="datac${data["feeds"][i]["feedId"]}" class="datac">
						<ul>
							<li>${data["feeds"][i]["content"]}</li>
							<li>${data["feeds"][i]["date"]}</li>
						</ul>
						</div>
						<div id="bar${data["feeds"][i]["feedId"]}" class="bar">
							
						  <div id="opBar${data["feeds"][i]["feedId"]}" class="opBars">	
							<img alt="like" src="images/like.png" width="20" height="20" id="image" onclick="addLike('${data["feeds"][i]["feedId"]}','${data["feeds"][i]["content"]}','${data["feeds"][i]["category"]}')"> &nbsp;
							${data["feeds"][i]["likes"]} &nbsp; &nbsp;	
							<img src="images/comment.png" width="20" height="20" id="image" onclick="toggle('mainComment${data["feeds"][i]["feedId"]}')"> &nbsp</a> <br><br>
						  </div>
						  <div id="dbBar${data["feeds"][i]["feedId"]}" class=opBars1>
								<a id="editFeed${data["feeds"][i]["feedId"]}" onclick="editFeed('datac${data["feeds"][i]["feedId"]}','${data["feeds"][i]["feedId"]}','${data["feeds"][i]["category"]}')">edit</a> &nbsp;&nbsp;&nbsp;
								<a onclick="deleteFeed('${data["feeds"][i]["feedId"]}')">delete</a>
						  </div>
						</div>	
							<div id="mainComment${data["feeds"][i]["feedId"]}"  style="display:none;">
		  						<textarea class="addComment" id="addComment${data["feeds"][i]["feedId"]}" placeholder="Enter you comment for this feed"></textarea>
		 						<input type="button" value="share" onclick="addComment('${data["feeds"][i]["feedId"]}')">`
				if(data.feeds[i].comments.length>0)
				{

								txt+=`<div class="commentContainer">`;
								
								for (let j = 0; j < data.feeds[i].comments.length; j++) 
									{
										txt+=`
										   	        <div class="comments" id="com${data["feeds"][i]["comments"][j]["commentId"]}">
														<div id="dataComment${data["feeds"][i]["comments"][j]["commentId"]}" class="dataComment">
															<ul>
																<li>${data["feeds"][i]["comments"][j]["comment"]}</li>
																<li>${data["feeds"][i]["comments"][j]["date"]}</li>
															</ul>
														</div>
														<div id="Cbar${data["feeds"][i]["comments"][j]["commentId"]}" class="Cbar">
												  			<div id="cOpBar" class="opBars">
												  				<img alt="like" src="images/like.png" width="20" height="20" id="image" onclick="addCommentLike('mainComment${data["feeds"][i]["feedId"]}','${data["feeds"][i]["feedId"]}','${data["feeds"][i]["comments"][j]["commentId"]}','${data["feeds"][i]["comments"][j]["comment"]}')"> &nbsp; &nbsp;
												  				${data["feeds"][i]["comments"][j]["likes"]}
															</div>
															<div id="cdbBars${data["feeds"][i]["comments"][j]["commentId"]}" class="cOpBars1">
																<a id="editComment${data["feeds"][i]["comments"][j]["commentId"]}" onclick="editComment('dataComment${data["feeds"][i]["comments"][j]["commentId"]}','${data["feeds"][i]["comments"][j]["commentId"]}','${data["feeds"][i]["feedId"]}')">edit</a> &nbsp;&nbsp;&nbsp;
																<a onclick="deleteComment('mainComment${data["feeds"][i]["feedId"]}','${data["feeds"][i]["feedId"]}','${data["feeds"][i]["comments"][j]["commentId"]}')">delete</a>
															</div>
														</div>
												
													</div>`;
									}	
											txt+=`</div>`;
								}
								txt+=`</div></div>`;
						}

		}
		else
		{
			txt+=`<div class="feed" style="text-align:center"><h4>There are no feeds to display</h4></div>`;
		}
   		mainContainer.innerHTML=txt;
}




var appendDeletedData=(data)=> {
		
		var txt="";
        var mainContainer = document.getElementById("myData");
	
		txt+=`<h3 style="margin-left:300px;">Deleted Feeds:</h3>`
        for (let i = 0; i < data.feeds.length; i++) 
		{
			txt+=`<div class="feed" id="${data["feeds"][i]["feedId"]}">
						<div id="datac${data["feeds"][i]["feedId"]}" class="datac">
						<ul>
							<li>${data["feeds"][i]["content"]}</li>
							<li>${data["feeds"][i]["date"]}</li>
						</ul>
						</div>
						<div id="bar${data["feeds"][i]["feedId"]}" class="bar">
							
						  <div id="opBar${data["feeds"][i]["feedId"]}" class="opBars">	
							<img alt="like" src="images/like.png" width="20" height="20" id="image" > &nbsp;
							(${data["feeds"][i]["likes"]}) &nbsp; &nbsp;	
							<img src="images/comment.png" width="20" height="20" id="image" onclick="toggle('mainComment${data["feeds"][i]["feedId"]}')"> &nbsp</a> <br><br>
						  </div>

						</div>	
							<div id="mainComment${data["feeds"][i]["feedId"]}"  style="display:none;">`
													if(data.feeds[i].comments.length>0){

								txt+=`<div class="commentContainer">`;
			
			for (let j = 0; j < data.feeds[i].comments.length; j++) 
				{
					txt+=`
					   	        <div class="comments" id="com${data["feeds"][i]["comments"][j]["commentId"]}">
									<div id="dataComment${data["feeds"][i]["comments"][j]["commentId"]}" class="dataComment">
										<ul>
											<li>${data["feeds"][i]["comments"][j]["comment"]}</li>
											<li>${data["feeds"][i]["comments"][j]["date"]}</li>
										</ul>
									</div>
									<div id="Cbar${data["feeds"][i]["comments"][j]["commentId"]}" class="Cbar">
							  			<div id="cOpBar" class="opBars">
							  				<img alt="like" src="images/like.png" width="20" height="20" id="image"> &nbsp; &nbsp;
							  				${data["feeds"][i]["comments"][j]["likes"]}
										</div>

									</div>
							
								</div>`;
				}	
						txt+=`</div>`;
			}
			txt+=`</div></div>`;
		}
		
   		mainContainer.innerHTML=txt;
}





var editFeed=(feedDivId,feedId,cat)=>{
	
	var lis = document.getElementById(feedDivId).getElementsByTagName("li");
	var element=document.getElementById(feedDivId);	
	var editb=document.getElementById("editFeed"+feedId);	
	lis[0].contentEditable=true;
	lis[0].style["border"]="1px solid black";
	editb.remove();
	txt=`<div class="editer" style="display:flex;">
				<input type="button" value="save" onclick="updateFeed('${feedId}','${cat}')">
				<input type="button" value="close" onclick="getFeeds()">
		</div>`;
	element.innerHTML+=txt;

	
}






var toggleFeed=(id)=>{
	var feed=document.getElementById(id);
	feed.onclick=function(){getDeletedFeeds()};
	feed.src="images/delete.png";
}
var toggleBin=(id)=>{
	var bin=document.getElementById(id);
	bin.onclick=function(){getFeeds()};
	bin.src="images/feed.png"
	}



var toggleAdd=()=>{
	var add=document.getElementById("add");
	var sub=document.getElementById("sub");
	var adder=document.getElementById("adder");
	if(adder.style["display"]=="none")
	{
		adder.style["animation"]="fadeIn 0.5s ease-in";
		adder.style["display"]="block";
		add.style["display"]="none";
		sub.style["display"]="block";
		adder.style["opacity"]=1;

	}
	else
	{
		adder.style["animation"]="fadeOut 0.5s ease-out";
		setTimeout(()=>{
			 			adder.style["display"]="none";
		 				add.style["display"]="block";
						sub.style["display"]="none";}, 500);

		adder.style["opacity"]=0;
	}
}

