


var getFeeds=(cursor="")=>{



	if(cursor=="" && Cache.has("Feeds"))
	{   
		document.getElementById("myData").innerHTML="";
		console.log("From cache");
		console.log(Cache.get("Feeds"))
		done=false;
		window.onscroll = ()=> {
		    if (done==false && (window.innerHeight + window.scrollY) >= document.body.scrollHeight) {
		         //console.log("Bottom of page");
				 done=true;
				 getFeeds(Cache.get("cursor"))
				
		     }
		}
		appendData(Cache.get("Feeds"),Cache.get("cursor"),1)
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
				console.log(Cache.has("Feeds"))
				if(Cache.has("Feeds"))
				{
					console.log("in")
					var r=Cache.get("Feeds")
					for(let i=0;i<data.feeds.length;i++)
					{
						r["feeds"].push(data.feeds[i])
					}
					Cache.set("cursor",data.cursor);	
					delete r["cursor"]				
					Cache.set("Feeds",r);
										
				}
				else
				{
					console.log("in else")
					Cache.set("cursor",data.cursor);					
					delete data["cursor"]
					Cache.set("Feeds",data);
				}

				console.log(Cache.get("Feeds"))
				appendData(data,data.cursor,1);				
				selectFeed()
				done=false;
				window.onscroll = ()=> {
				    if (done==false && (window.innerHeight + window.scrollY) >= document.body.scrollHeight) {
				         //console.log("Bottom of page");
						 done=true;
						 getFeeds(Cache.get("cursor"))
						
				     }
				}
			}
	
		}	
	}
		
}


var add1000Feeds=(userId)=>{

		for(let i=0;i<5;i++){
			var obj = {"content":"Feed"+i,"category":"Music","userId":userId};
			var xhr = new XMLHttpRequest();
			xhr.open("POST", "/feed",true);
			xhr.setRequestHeader('Content-Type', 'application/json');
			xhr.send(JSON.stringify(obj));
			
		}
	
		for(let i=0;i<5;i++){
			var obj = {"content":"Feed"+(5+i),"category":"Movies","userId":userId};
			var xhr = new XMLHttpRequest();
			xhr.open("POST", "/feed",true);
			xhr.setRequestHeader('Content-Type', 'application/json');
			xhr.send(JSON.stringify(obj));
			
		}
	
		for(let i=0;i<5;i++){
			var obj = {"content":"Feed"+(10+i),"category":"Technology","userId":userId};
			var xhr = new XMLHttpRequest();
			xhr.open("POST", "/feed",true);
			xhr.setRequestHeader('Content-Type', 'application/json');
			xhr.send(JSON.stringify(obj));
		
		}
	
		for(let i=0;i<5;i++)
		{
			var obj = {"content":"Feed"+(15+i),"category":"Sports","userId":userId};
			var xhr = new XMLHttpRequest();
			xhr.open("POST", "/feed",true);
			xhr.setRequestHeader('Content-Type', 'application/json');
			xhr.send(JSON.stringify(obj));
			
		}
		Cache.clear()
		getFeeds()
	}
	
var reset=()=>{
		document.getElementById("myData").innerHTML="";

}

var getCategoryFeed=(cursor="")=>{
		var category=document.getElementById("sort").selectedOptions[0].value;
			var xhr = new XMLHttpRequest();
			if(category==="All")
			{
				//document.getElementById("myData").innerHTML="";

				getFeeds();

			}
			else
			{			
				console.log("From server")
				xhr.open("GET", "/feed/category/"+category+"?cursor="+cursor, true);
				xhr.setRequestHeader('Content-Type', 'application/json');
				xhr.send();
				xhr.onload = function() {
				  var data = JSON.parse(this.responseText);
				  console.log("cat "+data)
				  appendData(data,data.cursor,2);
				done=false;

				window.onscroll = ()=> {
						    if (done==false && (window.innerHeight + window.scrollY) >= document.body.scrollHeight) {
						         //console.log("Bottom of page");
								 done=true;
								 getCategoryFeed(data.cursor)
								
						     }
					}
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
			    var data = JSON.parse(this.responseText);
				data.data["comments"]=[]
				if(cache.has("Feeds"))
				{
					cache.get("Feeds")["feeds"].unshift(data.data)

				}

				document.getElementById("addFeed").value="";

				//Cache.clear()
				getFeeds();
			}
		}
	else{
		console.log("error");
	}
}


var updateFeed=(id)=>{
	
		//var content=document.getElementById("updateText"+id).value;
		var lis = document.getElementById("datac"+id).getElementsByTagName("li")[0];
		//var category=document.getElementById("cat"+id).selectedOptions[0].value;
		lis=lis.innerText.replace(/(\n)/gm," ");
		var userId=document.getElementById("userId").value;
		var category=document.getElementById("sort"+id).selectedOptions[0].value;
		var obj = {"feedId":id,"content":lis,"category":category,"like":"false","userId":userId};
		document.getElementById("tag"+id).style.display="block"
		if(isValidFeedUpdate(obj)){
			
		console.log(obj);
		var xhr = new XMLHttpRequest();
		xhr.open("PUT", "/feed",true);
		xhr.setRequestHeader('Content-Type', 'application/json');
		xhr.send(JSON.stringify(obj));
		xhr.onload = function() {
			    var data = JSON.parse(this.responseText);

				appendSingleFeed(data);
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
						var data = JSON.parse(this.responseText);
						var like=document.getElementById("flike"+id);
 	  					like.innerText=data.feed.likes;			
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



var updateSingleFeed=(fid)=>{

	var xhr = new XMLHttpRequest();
	xhr.open("GET", "/feed/"+fid, true);
	xhr.setRequestHeader('Content-Type', 'application/json');
	xhr.send();
	xhr.onload = function() {
	  Cache.clear();
	  var data = JSON.parse(this.responseText);
	  appendSingleFeed(data)
/*	  var like=document.getElementById("flike"+fid);
	  console.log(data)
	  like.innerText=data.feed.likes;	
	  var element=document.getElementById("datac"+fid);
	  txt=`	<ul>
				<li>${data["feed"]["feed_content"]}</li>
				<li>${data["feed"]["date"]}</li>
				<li class="tag" id="tag${fid}">${data["feed"]["category"]}</li>

			</ul>`
	  element.innerHTML=txt;
*/
	}
}



var appendSingleFeed=(data)=>{
				var userId=document.getElementById("userId").value;

				txt=`
						<div id="datac${data["feed"]["feed_id"]}" class="datac">
						<ul>
							<li>${data["feed"]["feed_content"]}</li>
							<li>${data["feed"]["date"]}</li>
							<li class="tag" id="tag${data.feed["feed_id"]}">${data["feed"]["category"]}</li>
						</ul>
						</div>
						<div id="bar${data["feed"]["feed_id"]}" class="bar">
							
						  <div id="opBar${data["feed"]["feed_id"]}" class="opBars">	
							<img alt="like" src="images/like.png" width="20" height="20" id="image" onclick="addLike('${data["feed"]["userId"]}','${data["feed"]["feed_id"]}','${data["feed"]["feed_content"]}','${data["feed"]["category"]}')"> &nbsp;
							<wr id="flike${data["feed"]["feed_id"]}">${data["feed"]["likes"]}</wr> &nbsp; &nbsp;	
							<img src="images/comment.png" width="20" height="20" id="image${data["feed"]["feed_id"]}" onclick="toggle('mainComment${data["feed"]["feed_id"]}')"> &nbsp</a> <br><br>
						  </div>
						  <div id="dbBar${data["feed"]["feed_id"]}" class=opBars1>`
							if(data["feed"]["userId"]==userId){
							txt+=`	<a id="editFeed${data["feed"]["feed_id"]}" onclick="editFeed('datac${data["feed"]["feed_id"]}','${data["feed"]["feed_id"]}','${data["feed"]["category"]}')">edit</a> &nbsp;&nbsp;&nbsp;
								<a onclick="deleteFeed('${data["feed"]["feed_id"]}')">delete</a>`
								}
						txt+=`		
						  </div>
							
						</div>	
							`
								
					document.getElementById("mainFeed"+data.feed.feed_id).innerHTML=txt;
}




var appendData=(data,nextCursor,fn)=> {
		var txt="";
		var userId=document.getElementById("userId").value;
        var mainContainer = document.getElementById("myData");
		var noFeed=document.getElementById("noFeed");
		//console.log("in append function");
		var loadMore = document.getElementById("load_more");
		//console.log("feed length="+data.feeds.length);
		if(data.feeds.length!=0)
		{
		//console.log("Success");
		//console.log(data)
		noFeed.style.display="none";
        for (let i = 0; i < data.feeds.length && data.feeds[i]!=null;i++) 
		{
			txt+=`<div class="feed" id="${data["feeds"][i]["feed_id"]}">
						<div id="mainFeed${data["feeds"][i]["feed_id"]}">
						<div id="datac${data["feeds"][i]["feed_id"]}" class="datac">
						<ul>
							<li>${data["feeds"][i]["feed_content"]}</li>
							<li>${data["feeds"][i]["date"]}</li>
							<li class="tag" id="tag${data.feeds[i]["feed_id"]}">${data["feeds"][i]["category"]}</li>
						</ul>
						</div>
						<div id="bar${data["feeds"][i]["feed_id"]}" class="bar">
							
						  <div id="opBar${data["feeds"][i]["feed_id"]}" class="opBars">	
							<img alt="like" src="images/like.png" width="20" height="20" id="image" onclick="addLike('${data["feeds"][i]["userId"]}','${data["feeds"][i]["feed_id"]}','${data["feeds"][i]["feed_content"]}','${data["feeds"][i]["category"]}')"> &nbsp;
							<wr id="flike${data["feeds"][i]["feed_id"]}">${data["feeds"][i]["likes"]}</wr> &nbsp; &nbsp;	
							<img src="images/comment.png" width="20" height="20" id="image${data["feeds"][i]["feed_id"]}" onclick="toggle('mainComment${data["feeds"][i]["feed_id"]}')"> &nbsp</a> <br><br>
						  </div>
						  <div id="dbBar${data["feeds"][i]["feed_id"]}" class=opBars1>`
							if(data["feeds"][i]["userId"]==userId){
							txt+=`	<a id="editFeed${data["feeds"][i]["feed_id"]}" onclick="editFeed('datac${data["feeds"][i]["feed_id"]}','${data["feeds"][i]["feed_id"]}','${data["feeds"][i]["category"]}')">edit</a> &nbsp;&nbsp;&nbsp;
								<a onclick="deleteFeed('${data["feeds"][i]["feed_id"]}')">delete</a>`
								}
						txt+=`		
						  </div>
						</div>	
						</div>
							<div id="mainComment${data["feeds"][i]["feed_id"]}"  style="display:none;"><br>
		  						<textarea class="addComment" id="addComment${data["feeds"][i]["feed_id"]}" placeholder="Enter you comment for this feed"></textarea>
		 						<input type="button" value="share" id="addComment${data["feeds"][i]["feed_id"]}" onclick="addComment('${data["feeds"][i]["feed_id"]}')">`
				if(data.feeds[i].comments.length>0)
				{
								txt+=`<div class="commentContainer">`;
								
								for (let j = 0; j < data.feeds[i].comments.length; j++) 
									{
										txt+=`
										   	        <div class="comments" id="com${data["feeds"][i]["comments"][j]["comment_id"]}">
														<div id="dataComment${data["feeds"][i]["comments"][j]["comment_id"]}" class="dataComment">
															<ul>
																<li>${data["feeds"][i]["comments"][j]["comment"]}</li>
																<li>${data["feeds"][i]["comments"][j]["date"]}</li>
															</ul>
														</div>
														<div id="Cbar${data["feeds"][i]["comments"][j]["comment_id"]}" class="Cbar">
												  			<div id="cOpBar${data["feeds"][i]["comments"][j]["comment_id"]}" class="opBars">
												  				<img alt="like" src="images/like.png" width="20" height="20" id="image" onclick="addCommentLike('${data["feeds"][i]["comments"][j]["userId"]}','mainComment${data["feeds"][i]["feed_id"]}','${data["feeds"][i]["feed_id"]}','${data["feeds"][i]["comments"][j]["comment_id"]}','${data["feeds"][i]["comments"][j]["comment"]}')"> &nbsp; &nbsp;
												  				<wr id="clike${data["feeds"][i]["comments"][j]["comment_id"]}">${data["feeds"][i]["comments"][j]["likes"]}</wr>
															</div>`
															if(userId==data["feeds"][i]["comments"][j]["userId"]){
															txt+=`<div id="cdbBars${data["feeds"][i]["comments"][j]["comment_id"]}" class="cOpBars1">
																<a id="editComment${data["feeds"][i]["comments"][j]["comment_id"]}" onclick="editComment('dataComment${data["feeds"][i]["comments"][j]["comment_id"]}','${data["feeds"][i]["comments"][j]["comment_id"]}','${data["feeds"][i]["feed_id"]}')">edit</a> &nbsp;&nbsp;&nbsp;
																<a onclick="deleteComment('mainComment${data["feeds"][i]["feed_id"]}','${data["feeds"][i]["feed_id"]}','${data["feeds"][i]["comments"][j]["comment_id"]}')">delete</a>
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
							console.log(fn)
							if(fn==1)
							{
								txt1=`<a onclick="getFeeds('${nextCursor}')">Load more</a>`

							}
							else
							{
								txt1=`<a onclick="getCategoryFeed('${nextCursor}')">Load more</a>`

							}
							loadMore.innerHTML=txt1;	
						}
						else
						{
							var loadMore = document.getElementById("load_more");
							loadMore.innerHTML="";
						}
						mainContainer.innerHTML+=txt;
						


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
			txt+=`<div class="feed" id="${data["feeds"][i]["feed_id"]}">
						<div id="datac${data["feeds"][i]["feed_id"]}" class="datac">
						<ul>
							<li>${data["feeds"][i]["feed_content"]}</li>
							<li>${data["feeds"][i]["date"]}</li>
						</ul>
						</div>
						<div id="bar${data["feeds"][i]["feed_id"]}" class="bar">
							
						  <div id="opBar${data["feeds"][i]["feed_id"]}" class="opBars">	
							<img alt="like" src="images/like.png" width="20" height="20" id="image" > &nbsp;
							(${data["feeds"][i]["likes"]}) &nbsp; &nbsp;	
							<img src="images/comment.png" width="20" height="20" id="image" onclick="toggle('mainComment${data["feeds"][i]["feed_id"]}')"> &nbsp</a> <br><br>
						  </div>

						</div>`
													if(data.feeds[i].comments.length>0){

								txt+=`<div class="commentContainer">`;
			
			for (let j = 0; j < data.feeds[i].comments.length; j++) 
				{
					txt+=`
					   	        <div class="comments" id="com${data["feeds"][i]["comments"][j]["comment_id"]}">
									<div id="dataComment${data["feeds"][i]["comments"][j]["comment_id"]}" class="dataComment">
										<ul>
											<li>${data["feeds"][i]["comments"][j]["comment"]}</li>
											<li>${data["feeds"][i]["comments"][j]["date"]}</li>
										</ul>
									</div>
									<div id="Cbar${data["feeds"][i]["comments"][j]["comment_id"]}" class="Cbar">
							  			<div id="cOpBar${data["feeds"][i]["comments"][j]["comment_id"]}" class="opBars">
							  				<img alt="like" src="images/like.png" width="20" height="20" id="image"> &nbsp; &nbsp;
							  				<wr id="clike${data["feeds"][i]["comments"][j]["comment_id"]}">${data["feeds"][i]["comments"][j]["likes"]}</wr>
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





var editFeed=(feedDivId,feedId,cat)=>{
	
	var lis = document.getElementById(feedDivId).getElementsByTagName("li");
	var element=document.getElementById(feedDivId);	
	var editb=document.getElementById("editFeed"+feedId);	
	var text=lis[0].innerText;
	document.getElementById("tag"+feedId).style.display="none"
	lis[0].contentEditable=true;
	lis[0].style["border"]="1px solid black";
	//editb.remove();
	txt=`<div class="editer" style="display:flex;">
	 					<label for="category">Tag :</label>
						<select class="catTag" name="category" id="sort${feedId}">
						  <option value="Music">Music</option>
						  <option value="Movies">Movies</option>
						  <option value="Technology">Technology</option>
						  <option value="Sports">Sports</option>
						</select><br>
				<input type="button" id="update${feedId}" value="save" onclick="updateFeed('${feedId}')">
				<input type="button" value="close" onclick="updateSingleFeed('${feedId}')">
		</div>`;
	element.innerHTML+=txt;

	
}






var deleteAllFeeds=()=>{
	
	//var check=document.getElementById("selection");
	var r=Cache.get("Feeds")
	var n=r["feeds"].length
	var feeds={}
	var single={}
	var data=[]
	var comments={}
	var cData=[]
	for(let i=0;i<=50 && i<n;i++)
	{
		single={}
		cData=[]
		if(r["feeds"][i]!=null)
		{
			single["feedId"]=r["feeds"][i]["feed_id"]
			var m=r["feeds"][i]["comments"].length
			for(let j=0;j<m;j++)
			{
				comments={}
				comments["commentId"]=r["feeds"][i]["comments"][j]["comment_id"]
				cData.push(comments)
				
			}
			single["comments"]=cData
			
			data.push(single)			
		}

	}
	feeds["Feeds"]=data
	console.log(feeds)
	var xhr = new XMLHttpRequest();
	xhr.open("POST", "/enqueue",true);
	xhr.send(JSON.stringify(feeds));	
	xhr.onload=function (){
			if(cache.get("Feeds")["feeds"].length>=50)
			{
				cache.get("Feeds")["feeds"]=cache.get("Feeds")["feeds"].splice(50,cache.get("Feeds")["feeds"].length-1)
				deleteAllFeeds()	
			}
			else
			{
				Cache.del("Feeds")

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
		//getFeeds();

	  }, 5000);
	document.getElementById("myData").innerHTML=""
	console.log("cursor  "+ Cache.get("cursor"))
	getFeeds(Cache.get("cursor"))
	/*var inter=setInterval (()=>{document.getElementById("myData").innerHTML="";Cache.clear();getFeeds()}, 2000);
	window.setTimeout(function () {
				console.log("done");
	
 		 clearInterval(inter);

	  }, 8000);*/

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
	feed.onclick=function(){toggleBin("bin");getDeletedFeeds()};
	feed.src="images/delete.png";
}
var toggleBin=(id)=>{
	var bin=document.getElementById(id);
	document.getElementById("myData").innerHTML="";
	bin.onclick=function(){toggleFeed("bin");getFeeds("")};
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

