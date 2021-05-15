var addComment=(fId)=>{
		var userId=document.getElementById("userId").value;
		console.log(userId);
		var comment=document.getElementById("addComment"+fId).value;
		comment=comment.replace(/(\n)/gm," ");
		var obj = {"userId":userId,"feedId":fId,"comment":comment};
		if(isValidComment(obj)){
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
		else{
		console.log("error");
		}
}


var updateComment=(fid,cid)=>{
	//var comment=document.getElementById("updateComment"+cid).value;
	//comment=comment.replace(/(\n)/gm," ");
	var userId=document.getElementById("userId").value;
	
	var lis = document.getElementById("dataComment"+cid).getElementsByTagName("li")[0];
	lis=lis.innerText.replace(/(\n)/gm," ");
	var obj = {"userId":userId,"feedId":fid,"comment":lis,"commentId":cid,"like":false};
	console.log(obj);
	if(isValidCommentUpdate(obj)){
		var xhr = new XMLHttpRequest();
		xhr.open("PUT", "/feed/comments/",true);
		xhr.setRequestHeader('Content-Type', 'application/json');
		xhr.send(JSON.stringify(obj));
		xhr.onload = function() {
				 updateCommentDiv(fid,"mainComment"+fid);
		}
	}
	else{
		console.log("error");
	}
}








var addCommentLike=(uid,comId,fId,cId,comment)=>{
		var obj = {"userId":uid,"feedId":fId,"commentId":cId,"comment":comment,"like":true};
		var xhr = new XMLHttpRequest();
		xhr.open("PUT", "/feed/comments",true);
		xhr.setRequestHeader('Content-Type', 'application/json');
		xhr.send(JSON.stringify(obj));
		xhr.onload = function() {
			 updateSingleCommentDiv(fId,cId,comId);

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

var editComment=(commentDivId,commentId,feedId)=>{
	console.log(commentDivId);
	console.log(commentId);
	console.log(feedId);
	var lis = document.getElementById(commentDivId).getElementsByTagName("li");
	var element=document.getElementById(commentDivId);	
	lis[0].contentEditable=true;
	lis[0].style["border"]="1px solid black";
	var editb=document.getElementById("editComment"+commentId);	
	editb.remove();
	txt=`
			<div class="editer" style="display:flex;">
				<input type="button" value="save" id="updateC${commentId}" onclick="updateComment('${feedId}','${commentId}')">
				<input type="button" value="close" onclick="updateCommentDiv('${feedId}','mainComment${feedId}')">
			</div>`
	element.innerHTML+=txt;

	
}



var updateCommentDiv=(fid,commentId)=>{
	var xhr = new XMLHttpRequest();
	var userId=document.getElementById("userId").value;
	xhr.open("GET", "/feed/comments/"+fid, true);
	xhr.setRequestHeader('Content-Type', 'application/json');
	xhr.send();
	xhr.onload = function() {
	  var data = JSON.parse(this.responseText);
	  var txt="";
      var mainContainer = document.getElementById(commentId);
	  txt+=`<textarea class="addComment" id="addComment${data["feed"]["feed_id"]}" placeholder="Enter you comment for this feed"></textarea>
		    <input type="button" value="share" onclick="addComment('${data["feed"]["feed_id"]}')">`;
				if(data.feed.comments.length>0){

 			txt+=`<div class="commentContainer">`;

		for(let i=0;i<data["feed"]["comments"].length;i++){
			  txt+=`
			<div class="comments" id="com${data["feed"]["comments"][i]["comment_id"]}">
			  <div id="dataComment${data["feed"]["comments"][i]["comment_id"]}" class="dataComment">
				<ul>
					<li>${data["feed"]["comments"][i]["comment"]}</li>
					<li>${data["feed"]["comments"][i]["date"]}</li>
				</ul>
			  </div>
			  <div id="Cbar${data["feed"]["comments"][i]["comment_id"]}" class="Cbar">
					<div id="cOpBar${data["feed"]["comments"][i]["comment_id"]}" class="opBars">
						<img alt="like" src="images/like.png" width="20" height="20" id="image" onclick="addCommentLike('${data["feed"]["comments"][i]["userId"]}','mainComment${data["feed"]["feed_id"]}','${data["feed"]["feed_id"]}','${data["feed"]["comments"][i]["comment_id"]}','${data["feed"]["comments"][i]["comment"]}')"> &nbsp; &nbsp;
						<wr id="clike${data["feed"]["comments"][i]["comment_id"]}">${data["feed"]["comments"][i]["likes"]}</wr>
					</div>
					`
					if(data["feed"]["comments"][i]["userId"]==userId){
						
					txt+=`<div id="cdbBars${data["feed"]["comments"][i]["comment_id"]}" class="cOpBars1">
						<a id="editComment${data["feed"]["comments"][i]["comment_id"]}" onclick="editComment('dataComment${data["feed"]["comments"][i]["comment_id"]}','${data["feed"]["comments"][i]["comment_id"]}','${data["feed"]["feed_id"]}')">edit</a> &nbsp;&nbsp;&nbsp;
						<a onclick="deleteComment('mainComment${data["feed"]["feed_id"]}','${data["feed"]["feed_id"]}','${data["feed"]["comments"][i]["comment_id"]}')">delete</a>
					</div>
					`}
					txt+=`
			 </div>
		</div>`
			}
		}
		txt+=`</div>`
		
   		mainContainer.innerHTML=txt;
	}
}


var updateSingleCommentDiv=(fid,cid,commentId)=>{
	var xhr = new XMLHttpRequest();
	console.log(fid)
	console.log(cid)
	var like=document.getElementById("clike"+cid);
	xhr.open("GET", "/feed/comments/"+fid+"/"+cid, true);
	xhr.setRequestHeader('Content-Type', 'application/json');
	xhr.send();
	xhr.onload = function() {
	  var data = JSON.parse(this.responseText);
	  like.innerText=data.comment["likes"];	  
	}
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





var isValidComment=(obj)=>{
	
	if(Object.keys(obj).length!=3||obj["userId"].replace(/\s/g, '')==""||obj["feedId"].replace(/\s/g, '')==""||obj["comment"].replace(/\s/g, '')=="")
	{
		return false;
	}
	return true;
	
}
var isValidCommentUpdate=(obj)=>{
	
	if(Object.keys(obj).length!=5||obj["userId"].replace(/\s/g, '')==""||obj["feedId"].replace(/\s/g, '')==""||obj["comment"].replace(/\s/g, '')==""||obj["commentId"].replace(/\s/g, '')=="")
	{
		return false;
	}
	return true;
	
}


module.exports={isValidComment,isValidCommentUpdate,toggle,updateCommentDiv};
