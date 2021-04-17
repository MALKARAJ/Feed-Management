
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

var addComment=(fId)=>{
		var comment=document.getElementById("addComment"+fId).value;
		comment=comment.replace(/(\n)/gm," ");
		var obj = {"feedId":fId,"comment":comment};
		console.log(fId);
		console.log(comment);
		var xhr = new XMLHttpRequest();
		xhr.open("POST", "/feed/comments",true);
		xhr.setRequestHeader('Content-Type', 'application/json');
		xhr.send(JSON.stringify(obj));
		xhr.onload = function() {
			 updateCommentDiv(fId,"mainComment"+fId);
		}
}

var updateFeed=(id)=>{
	var content=document.getElementById("updateText"+id).value;
	var category=document.getElementById("cat"+id).selectedOptions[0].value;
	content=content.replace(/(\n)/gm," ");
	var obj = {"feedId":id,"content":content,"category":category,"like":"false"};
	console.log(obj);
	var xhr = new XMLHttpRequest();
	xhr.open("PUT", "/feed",true);
	xhr.setRequestHeader('Content-Type', 'application/json');
	xhr.send(JSON.stringify(obj));
	xhr.onload = function() {
		 getFeeds();
	}
}


var updateComment=(fid,cid)=>{
	var comment=document.getElementById("updateComment"+cid).value;
	comment=comment.replace(/(\n)/gm," ");
	var obj = {"feedId":fid,"comment":comment,"commentId":cid,"like":false};
	console.log(obj);
	var xhr = new XMLHttpRequest();
	xhr.open("PUT", "/feed/comments/",true);
	xhr.setRequestHeader('Content-Type', 'application/json');
	xhr.send(JSON.stringify(obj));
	xhr.onload = function() {
			 updateCommentDiv(fid,"mainComment"+fid);
	}
}


var addCommentLike=(comId,fId,cId,comment)=>{
		console.log(comId);
		var obj = {"feedId":fId,"commentId":cId,"comment":comment,"like":true};
		var xhr = new XMLHttpRequest();
		xhr.open("PUT", "/feed/comments",true);
		xhr.setRequestHeader('Content-Type', 'application/json');
		xhr.send(JSON.stringify(obj));
		xhr.onload = function() {
			 updateCommentDiv(fId,comId);

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
var deleteComment=(commentDivId,fId,cId)=>{
		var xhr = new XMLHttpRequest();
		xhr.open("DELETE", "/feed/comments/"+fId+"/"+cId,true);
		xhr.setRequestHeader('Content-Type', 'application/json');
		xhr.send();
		xhr.onload = function() {
			 updateCommentDiv(fId,commentDivId);
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
							(${data["feeds"][i]["likes"]}) &nbsp; &nbsp;	
							<img src="images/comment.png" width="20" height="20" id="image" onclick="toggle('mainComment${data["feeds"][i]["feedId"]}')"> &nbsp</a> <br><br>
						  </div>
						  <div id="dbBar${data["feeds"][i]["feedId"]}" class=opBars1>
								<a id="editFeed${data["feeds"][i]["feedId"]}" onclick="editFeed('datac${data["feeds"][i]["feedId"]}','${data["feeds"][i]["feedId"]}')">edit</a> &nbsp;&nbsp;&nbsp;
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




var updateCommentDiv=(fid,commentId)=>{
	var xhr = new XMLHttpRequest();
	xhr.open("GET", "/feed/comments/"+fid, true);
	xhr.setRequestHeader('Content-Type', 'application/json');
	xhr.send();
	xhr.onload = function() {
	  var data = JSON.parse(this.responseText);
	  var txt="";
      var mainContainer = document.getElementById(commentId);
	  console.log(data);
	  txt+=`<textarea class="addComment" id="addComment${data["feed"]["feedId"]}" placeholder="Enter you comment for this feed"></textarea>
		    <input type="button" value="share" onclick="addComment('${data["feed"]["feedId"]}')">`;
				if(data.feed.comments.length>0){

 			txt+=`<div class="commentContainer">`;

		for(let i=0;i<data["feed"]["comments"].length;i++){
			  txt+=`
			<div class="comments" id="com${data["feed"]["comments"][i]["commentId"]}">
			  <div id="dataComment${data["feed"]["comments"][i]["commentId"]}" class="dataComment">
				<ul>
					<li>${data["feed"]["comments"][i]["comment"]}</li>
					<li>${data["feed"]["comments"][i]["date"]}</li>
				</ul>
			  </div>
			  <div id="Cbar${data["feed"]["comments"][i]["commentId"]}" class="Cbar">
					<div id="cOpBar" class="opBars">
						<img alt="like" src="images/like.png" width="20" height="20" id="image" onclick="addCommentLike('mainComment${data["feed"]["feedId"]}','${data["feed"]["feedId"]}','${data["feed"]["comments"][i]["commentId"]}','${data["feed"]["comments"][i]["comment"]}')"> &nbsp; &nbsp;
						${data["feed"]["comments"][i]["likes"]}
					</div>
					<div id="cdbBars${data["feed"]["comments"][i]["commentId"]}" class="cOpBars1">
						<a id="editComment${data["feed"]["comments"][i]["commentId"]}" onclick="editComment('dataComment${data["feed"]["comments"][i]["commentId"]}','${data["feed"]["comments"][i]["commentId"]}','${data["feed"]["feedId"]}')">edit</a> &nbsp;&nbsp;&nbsp;
						<a onclick="deleteComment('mainComment${data["feed"]["feedId"]}','${data["feed"]["feedId"]}','${data["feed"]["comments"][i]["commentId"]}')">delete</a>
					</div>
			 </div>
		</div>`
			}
		}
		txt+=`</div>`
		
   		mainContainer.innerHTML=txt;
	}
}

var editFeed=(feedDivId,feedId)=>{
	
	var lis = document.getElementById(feedDivId).getElementsByTagName("li");
	var element=document.getElementById(feedDivId);	
	var editb=document.getElementById("editFeed"+feedId);	
	editb.remove();
	txt=`<textarea class="addComment" id="updateText${feedId}" placeholder="update your feed here" >${lis[0].innerText}</textarea>
		 <label for="category">Category:</label>
			<select name="category" id="cat${feedId}" style="width:100px;">
			  <option value="Music">Music</option>
			  <option value="Movies">Movies</option>
			  <option value="Technology">Technology</option>
			  <option value="Sports">Sports</option>
			</select>
		  <input type="button" value="share" onclick="updateFeed('${feedId}')">`
	element.innerHTML=txt;

	
}

var editComment=(commentDivId,commentId,feedId)=>{
	console.log(commentDivId);
	console.log(commentId);
	console.log(feedId);
	var lis = document.getElementById(commentDivId).getElementsByTagName("li");
	var element=document.getElementById(commentDivId);	
	var editb=document.getElementById("editComment"+commentId);	
	editb.remove();
	txt=`<textarea class="addComment" id="updateComment${commentId}" placeholder="update your comment here" >${lis[0].innerText}</textarea>

		  <input type="button" value="share" onclick="updateComment('${feedId}','${commentId}')">`
	element.innerHTML=txt;

	
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

var toggle=(id)=>{
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




















/*
var appendAllComment=(data,commentId)=> {
		console.log(commentId);
		var txt="";
        var mainContainer = document.getElementById(commentId);
		txt+=`<textarea class="addComment" id="addComment${data["feed"]["feedId"]}" placeholder="Enter you comment for this feed"></textarea>
			  <input type="button" value="share" onclick="addComment('${data["feed"]["feedId"]}')">`;
		for(let i=0;i<data["feed"]["comments"].length;i++){
		txt+=`<div class="comments" id="com${data["feed"]["comments"][i]["commentId"]}">
			  <h4>${data["feed"]["comments"][i]["comment"]}</h4>
			  <div id="cOpBar" class="opBars">
			  <img alt="like" src="images/like.png" width="20" height="20" id="image" onclick="addCommentLike('${commentId}','${data["feed"]["feedId"]}','${data["feed"]["comments"][i]["commentId"]}','${data["feed"]["comments"][i]["comment"]}')"> &nbsp; &nbsp;
			  ${data["feed"]["comments"][i]["likes"]}</div></div>`;
		}
   		mainContainer.innerHTML=txt;
}
*/
