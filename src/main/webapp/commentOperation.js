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


var updateComment=(fid,cid)=>{
	//var comment=document.getElementById("updateComment"+cid).value;
	//comment=comment.replace(/(\n)/gm," ");
	var lis = document.getElementById("dataComment"+cid).getElementsByTagName("li")[0];
	lis=lis.innerText.replace(/(\n)/gm," ");
	var obj = {"feedId":fid,"comment":lis,"commentId":cid,"like":false};
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
				<input type="button" value="save" onclick="updateComment('${feedId}','${commentId}')">
				<input type="button" value="close" onclick="updateCommentDiv('${feedId}','mainComment${feedId}')">
			</div>`
	element.innerHTML+=txt;

	
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



