


var getFeeds=(cursor="")=>{



	if(Cache.has(cursor))
	{
		
		console.log("From cache");
		done=false;
		window.onscroll = ()=> {
		    if (done==false && (window.innerHeight + window.scrollY) >= document.body.scrollHeight) {
		         //console.log("Bottom of page");
				 done=true;
				 getFeeds(Cache.get(cursor)["cursor"])
				
		     }
		}
		appendData(Cache.get(cursor),cursor);
		selectFeed()	
	}
	else
	{
		console.log("from server");
		var xhr = new XMLHttpRequest();
		xhr.open("GET", "/feed?cursor="+cursor, true);
		xhr.setRequestHeader('Content-Type', 'application/json');
		xhr.send();
		xhr.onload = function() 
		{
			var data = JSON.parse(this.responseText);
			if(data["success"]==true){
				Cache.set(cursor,data);
				appendData(data,data.cursor);				
				selectFeed()
				done=false;
				window.onscroll = ()=> {
				    if (done==false && (window.innerHeight + window.scrollY) >= document.body.scrollHeight) {
				         //console.log("Bottom of page");
						 done=true;
						 getFeeds(Cache.get(cursor)["cursor"])
						
				     }
				}
			}
	
		}	
	}
		
}


var add1000Feeds=(userId)=>{

		for(let i=0;i<250;i++){
			var obj = {"content":"Feed"+i,"category":"Music","userId":userId};
			var xhr = new XMLHttpRequest();
			xhr.open("POST", "/feed",true);
			xhr.setRequestHeader('Content-Type', 'application/json');
			xhr.send(JSON.stringify(obj));
			
		}
	
		for(let i=0;i<250;i++){
			var obj = {"content":"Feed"+(250+i),"category":"Movies","userId":userId};
			var xhr = new XMLHttpRequest();
			xhr.open("POST", "/feed",true);
			xhr.setRequestHeader('Content-Type', 'application/json');
			xhr.send(JSON.stringify(obj));
			
		}
	
		for(let i=0;i<250;i++){
			var obj = {"content":"Feed"+(500+i),"category":"Technology","userId":userId};
			var xhr = new XMLHttpRequest();
			xhr.open("POST", "/feed",true);
			xhr.setRequestHeader('Content-Type', 'application/json');
			xhr.send(JSON.stringify(obj));
		
		}
	
		for(let i=0;i<250;i++)
		{
			var obj = {"content":"Feed"+(750+i),"category":"Sports","userId":userId};
			var xhr = new XMLHttpRequest();
			xhr.open("POST", "/feed",true);
			xhr.setRequestHeader('Content-Type', 'application/json');
			xhr.send(JSON.stringify(obj));
			
		}
		Cache.clear()
		getFeeds()
	}
	


var getCategoryFeed=()=>{
		var category=document.getElementById("sort").selectedOptions[0].value;
document.getElementById("myData").innerHTML="";
			var xhr = new XMLHttpRequest();
			if(category==="All")
			{
				//document.getElementById("myData").innerHTML="";
				getFeeds();

			}
			else
			{			
				console.log("From server")
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
	
	
	
	if(Cache.has("deleted"))
	{
		console.log("From cache");
		//console.log(Cache.get(cursor))
		toggleBin("bin");
		appendDeletedData(Cache.get("deleted"));
	}
	else
	{
	
			console.log("From server")	
			var xhr = new XMLHttpRequest();
			xhr.open("GET", "/feed/trash", true);
			xhr.setRequestHeader('Content-Type', 'application/json');
			xhr.send();
			xhr.onload = function() {
			    var data = JSON.parse(this.responseText);
				if(data.code==200){
					Cache.set("deleted",data);
				}
				console.log("deleted data");
				console.log(data);
			    toggleBin("bin");
			    appendDeletedData(data);
			}		
	}
}


var addFeed=()=>{
		var content=document.getElementById("addFeed").value;
		var category=document.getElementById("cat").selectedOptions[0].value;
		content=content.replace(/(\n)/gm," ");
		var userId=document.getElementById("userId").value;
		var obj = {"content":content,"category":category,"userId":userId};
		if(isValidFeed(obj))
		{
			var xhr = new XMLHttpRequest();
			xhr.open("POST", "/feed",true);
			xhr.setRequestHeader('Content-Type', 'application/json');
			xhr.send(JSON.stringify(obj));
			xhr.onload = function() {
			document.getElementById("addFeed").value="";
			document.getElementById("myData").innerHTML="";
			Cache.clear();
			getFeeds();
			}
		}
	else{
		console.log("error");
	}
}


var updateFeed=(currentCursor,id,cat)=>{
	
		//var content=document.getElementById("updateText"+id).value;
		var lis = document.getElementById("datac"+id).getElementsByTagName("li")[0];
		//var category=document.getElementById("cat"+id).selectedOptions[0].value;
		lis=lis.innerText.replace(/(\n)/gm," ");
		var userId=document.getElementById("userId").value;
		var obj = {"feedId":id,"content":lis,"category":cat,"like":"false","userId":userId};
		if(isValidFeedUpdate(obj)){
			
		console.log(obj);
		var xhr = new XMLHttpRequest();
		xhr.open("PUT", "/feed",true);
		xhr.setRequestHeader('Content-Type', 'application/json');
		xhr.send(JSON.stringify(obj));
		xhr.onload = function() {
		console.log(currentCursor)
		console.log(cache)
		Cache.del(currentCursor);
		console.log(cache)
		updateSingleFeed(id);
		}
	}
}


var addLike=(uid,id,content,category)=>{
		//var userId=document.getElementById("userId").value;

		var obj = {"userId":uid,"feedId":id,"content":content,"category":category,"like":"true"};
		var xhr = new XMLHttpRequest();
		xhr.open("PUT", "/feed",true);
		xhr.setRequestHeader('Content-Type', 'application/json');
		xhr.send(JSON.stringify(obj));
		xhr.onload = function() {
						Cache.clear();

			 updateSingleFeed(id);
		}
}


var deleteFeed=(id)=>{

		var xhr = new XMLHttpRequest();
		xhr.open("DELETE", "/feed/"+id,true);
		xhr.setRequestHeader('Content-Type', 'application/json');
		xhr.send();
		xhr.onload = function() {
		console.log(cache)
		//console.log(cOrder)
		Cache.clear();
		document.getElementById(id).remove()
		//getFeeds(cOrder.get(2));
		}
}


/*
var updateSingleFeed=(fid)=>{

	var xhr = new XMLHttpRequest();
	xhr.open("GET", "/feed/"+fid, true);
	xhr.setRequestHeader('Content-Type', 'application/json');
	xhr.send();
	xhr.onload = function() {
	  Cache.clear();
	  var data = JSON.parse(this.responseText);
	  var like=document.getElementById("flike"+fid);
		like.innerText=data.feed.likes;			

	}
}
*/

var updateSingleFeed=(fid)=>{

	var xhr = new XMLHttpRequest();
	xhr.open("GET", "/feed/"+fid, true);
	xhr.setRequestHeader('Content-Type', 'application/json');
	xhr.send();
	xhr.onload = function() {
	  Cache.clear();
	  var data = JSON.parse(this.responseText);
	  var like=document.getElementById("flike"+fid);
	  console.log(data)
	  like.innerText=data.feed.likes;	
	  var element=document.getElementById("datac"+fid);
	  txt=`	<ul>
				<li>${data["feed"]["content"]}</li>
				<li>${data["feed"]["date"]}</li>
			</ul>`
	  element.innerHTML=txt;

	}
}


var appendData=(data,currentCursor)=> {
		var txt="";
		var userId=document.getElementById("userId").value;
        var mainContainer = document.getElementById("myData");
		var noFeed=document.getElementById("noFeed");
		//console.log("in append function");
		var loadMore = document.getElementById("load_more");
		//console.log("feed length="+data.feeds.length);
		if(data.code=="200" && data.feeds.length!=0)
		{
		//console.log("Success");
		//console.log(data)
		noFeed.style.display="none";
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
							<img alt="like" src="images/like.png" width="20" height="20" id="image" onclick="addLike('${data["feeds"][i]["userId"]}','${data["feeds"][i]["feedId"]}','${data["feeds"][i]["content"]}','${data["feeds"][i]["category"]}')"> &nbsp;
							<wr id="flike${data["feeds"][i]["feedId"]}">${data["feeds"][i]["likes"]}</wr> &nbsp; &nbsp;	
							<img src="images/comment.png" width="20" height="20" id="image${data["feeds"][i]["feedId"]}" onclick="toggle('mainComment${data["feeds"][i]["feedId"]}')"> &nbsp</a> <br><br>
						  </div>
						  <div id="dbBar${data["feeds"][i]["feedId"]}" class=opBars1>`
							if(data["feeds"][i]["userId"]==userId){
							txt+=`	<a id="editFeed${data["feeds"][i]["feedId"]}" onclick="editFeed('${currentCursor}','datac${data["feeds"][i]["feedId"]}','${data["feeds"][i]["feedId"]}','${data["feeds"][i]["category"]}')">edit</a> &nbsp;&nbsp;&nbsp;
								<a onclick="deleteFeed('${data["feeds"][i]["feedId"]}')">delete</a>`
								}
						txt+=`		
						  </div>
						</div>	
							<div id="mainComment${data["feeds"][i]["feedId"]}"  style="display:none;"><br>
		  						<textarea class="addComment" id="addComment${data["feeds"][i]["feedId"]}" placeholder="Enter you comment for this feed"></textarea>
		 						<input type="button" value="share" id="addComment${data["feeds"][i]["feedId"]}" onclick="addComment('${data["feeds"][i]["feedId"]}')">`
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
												  			<div id="cOpBar${data["feeds"][i]["comments"][j]["commentId"]}" class="opBars">
												  				<img alt="like" src="images/like.png" width="20" height="20" id="image" onclick="addCommentLike('${data["feeds"][i]["comments"][j]["userId"]}','mainComment${data["feeds"][i]["feedId"]}','${data["feeds"][i]["feedId"]}','${data["feeds"][i]["comments"][j]["commentId"]}','${data["feeds"][i]["comments"][j]["comment"]}')"> &nbsp; &nbsp;
												  				<wr id="clike${data["feeds"][i]["comments"][j]["commentId"]}">${data["feeds"][i]["comments"][j]["likes"]}</wr>
															</div>`
															if(userId==data["feeds"][i]["comments"][j]["userId"]){
															txt+=`<div id="cdbBars${data["feeds"][i]["comments"][j]["commentId"]}" class="cOpBars1">
																<a id="editComment${data["feeds"][i]["comments"][j]["commentId"]}" onclick="editComment('dataComment${data["feeds"][i]["comments"][j]["commentId"]}','${data["feeds"][i]["comments"][j]["commentId"]}','${data["feeds"][i]["feedId"]}')">edit</a> &nbsp;&nbsp;&nbsp;
																<a onclick="deleteComment('mainComment${data["feeds"][i]["feedId"]}','${data["feeds"][i]["feedId"]}','${data["feeds"][i]["comments"][j]["commentId"]}')">delete</a>
															</div>
															`}
															
															txt+=`
														</div>
												
													</div>`;
									}	
											txt+=`</div>`;
								}
								txt+=`</div></div>`;
						}

						if(data.feeds.length=30)
						{
							txt1=`<a onclick="getFeeds('${data.cursor}')">Load more</a>`
							loadMore.innerHTML=txt1;	
						}
						else
						{
							var loadMore = document.getElementById("load_more");
							loadMore.innerHTML="";
						}
						if(currentCursor=="")
						{
							mainContainer.innerHTML=txt;
						
						}
						else
						{
							mainContainer.innerHTML+=txt;
						}


		}
		else
		{
			
			console.log("Fail");
			noFeed.style.display="block";
			mainContainer.innerHTML="";
			loadMore.innerHTML="";

		}


}



var appendDeletedData=(data)=> {
		
		var txt="";
        var mainContainer = document.getElementById("myData");
		
		txt+=`<h3 style="margin-left:300px;">Deleted Feeds:</h3>`
		if(data.feeds.length>0 && data.code=="200")
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
							<img alt="like" src="images/like.png" width="20" height="20" id="image" > &nbsp;
							(${data["feeds"][i]["likes"]}) &nbsp; &nbsp;	
							<img src="images/comment.png" width="20" height="20" id="image" onclick="toggle('mainComment${data["feeds"][i]["feedId"]}')"> &nbsp</a> <br><br>
						  </div>

						</div>	 {data["feeds"][i]["feedId"]}"  style="display:none;">`
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
							  			<div id="cOpBar${data["feeds"][i]["comments"][j]["commentId"]}" class="opBars">
							  				<img alt="like" src="images/like.png" width="20" height="20" id="image"> &nbsp; &nbsp;
							  				<wr id="clike${data["feeds"][i]["comments"][j]["commentId"]}">${data["feeds"][i]["comments"][j]["likes"]}</wr>
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
		var loadMore = document.getElementById("load_more");
		loadMore.innerHTML="";
   		mainContainer.innerHTML=txt;
}





var editFeed=(currentCursor,feedDivId,feedId,cat)=>{
	
	var lis = document.getElementById(feedDivId).getElementsByTagName("li");
	var element=document.getElementById(feedDivId);	
	var editb=document.getElementById("editFeed"+feedId);	
	var text=lis[0].innerText;
	lis[0].contentEditable=true;
	lis[0].style["border"]="1px solid black";
	//editb.remove();
	txt=`<div class="editer" style="display:flex;">
				<input type="button" id="update${feedId}" value="save" onclick="updateFeed('${currentCursor}','${feedId}','${cat}')">
				<input type="button" value="close" onclick="updateSingleFeed('${feedId}')">
		</div>`;
	element.innerHTML+=txt;

	
}






var deleteAllFeeds=()=>{
	var check=document.getElementById("selection");

	if(check.checked==true)
	{
		Cache.clear();
		var xhr = new XMLHttpRequest();
		xhr.open("POST", "/enqueue",true);
		xhr.send();	
		xhr.onload=function (){
			console.log("in delete")
			showMessage()
		}
	}
}
var showMessage=()=>{
	var ele=document.getElementById("message");
	var check=document.getElementById("selection");
	check.checked=false;
	selectFeed();
	ele.style["display"]="block";
	window.setTimeout(function () {
				console.log("done");

	     ele.style["display"] = "none";
		getFeeds();

	  }, 5000);
	var inter=setInterval (()=>{Cache.clear();getFeeds()}, 2000);
	window.setTimeout(function () {
				console.log("done");
	
 		 clearInterval(inter);

	  }, 8000);

}

var selectFeed=()=>{
	var check=document.getElementById("selection");
	//var feed=document.getElementsByClassName("feed");
	const feeds = document.querySelectorAll('.feed');

	if(check.checked==true)
	{
		feeds.forEach(element => {
	 	 element.style["border"] = "3px solid black";
		 //element.style["background-color"]="#c8ebeb";
		});		
	}
	else if(check.checked==false)
	{
		feeds.forEach(element => {
	 	 element.style["border"] = "1px solid black";
		 //element.style["background-color"]="";

		});	
	}

}



var toggleFeed=(id)=>{
	var feed=document.getElementById(id);
	document.getElementById("myData").innerHTML="";
	feed.onclick=function(){getDeletedFeeds()};
	feed.src="images/delete.png";
}
var toggleBin=(id)=>{
	var bin=document.getElementById(id);
	document.getElementById("myData").innerHTML="";
	bin.onclick=function(){toggleFeed("bin");getFeeds()};
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




var isValidFeed=(obj)=>{
	
	if(Object.keys(obj).length!=3||obj["userId"].replace(/\s/g, '')==""||obj["category"].replace(/\s/g, '')==""||obj["content"].replace(/\s/g, '')=="")
	{
		return false;
	}
	return true;
	
}
var isValidFeedUpdate=(obj)=>{
	
	if(Object.keys(obj).length!=5||obj["userId"].replace(/\s/g, '')==""||obj["category"].replace(/\s/g, '')==""||obj["content"].replace(/\s/g, '')==""||obj["feedId"].replace(/\s/g, '')==""||obj["like"].replace(/\s/g, '')=="")
	{
		return false;
	}
	return true;
	
}



module.exports={isValidFeed,isValidFeedUpdate,appendData,getFeeds,updateFeed,addFeed,isValidFeedUpdate,isValidFeed,toggleAdd,toggleFeed,toggleBin};

