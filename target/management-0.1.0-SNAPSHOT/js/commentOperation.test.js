

var server=require("./mockServer.js")
var fixture=require("./feedFixture.js")

var validate=require("./commentOperation.js")
var feed=require("./feedOperation.js")




beforeEach(() => {
		document.body.innerHTML=fixture.fixture()
});

afterEach(()=>{
		jest.clearAllMocks();

})

describe('Mock CRUD operations',()=>
{
	

	
	
	
	test('Post Requests', () =>
	{
		window.XMLHttpRequest = jest.fn().mockImplementation(server.postCommentServer);
		server.postCommentServer.open("POST", "/feed/comments", true);
		var obj={"comment":"Hello","feedId":"feed1","userId":"user123123"};
		expect(validate.isValidComment(obj)).toBe(true);
		server.postCommentServer.send(obj);
		var data=JSON.parse(server.postCommentServer.onload());
		//console.log(data);
		expect(data["success"]).toBe(true);
		expect(data["feeds"][0]["comments"].length==2);


	});
	
		test('Put Requests', () =>
	{
		window.XMLHttpRequest = jest.fn().mockImplementation(server.putCommentServer);
		server.putCommentServer.open("POST", "/feed/comments", true);
		var obj={"comment":"this is the first comment","feedId":"feed1","commentId":"comment1","userId":"user123123","like":false};
		expect(validate.isValidCommentUpdate(obj)).toBe(true);
		server.putCommentServer.send(obj);
		var data=JSON.parse(server.putCommentServer.onload());
		//console.log(data);
		expect(data["success"]).toBe(true);
		expect(data["feeds"][0]["comments"].length==2);


	});

	
	
});




describe("Testing UI elements for comments",()=>{

			
			
			
			var getFeeds=jest.fn(()=>{
					window.XMLHttpRequest = jest.fn().mockImplementation(server.getServer);
					server.getServer.open("Get", "/feed", true);
					server.getServer.send();
					var data=JSON.parse(server.getServer.onload());
					return data
			
			})
			

			
			var addComment=jest.fn(()=>{
					window.XMLHttpRequest = jest.fn().mockImplementation(server.postCommentServer);
					server.postCommentServer.open("POST", "/feed/comments", true);
					var obj={"comment":"this is the new comment","userId":"user123123","feedId":"feed1"};
					server.postCommentServer.send(obj);
			
			})
			

						
			
			test(`The postFeed button`, () => {
			  var data=getFeeds()
			  feed.appendData(data)
			  validate.toggle("mainCommentfeed1")
			  var postComment=document.getElementById("addCommentfeed1")
			  postComment.onclick=addComment
			  postComment.click()
			  var data=getFeeds()
			  feed.appendData(data)
			  expect(getFeeds).toHaveBeenCalled()
			  expect(getFeeds).toHaveBeenCalledTimes(2)
			  expect(addComment).toHaveBeenCalled()
			  expect(document.getElementById("dataCommentcomment3").querySelectorAll("li")[0].innerHTML).toBe("this is the new comment")
			  expect(addComment).toHaveBeenCalledTimes(1)
			})
			
			
			
			var addSubmitButton=()=>{
				var container=document.getElementById("dataCommentcomment3")
				txt=`<div class="editer" style="display:flex;">
				<input type="button" id="updateCcomment3" value="save">
				<input type="button" value="close" onclick="getFeeds()">
				</div>`;			
				container.innerHTML+=txt
				
				}
			
			
			
			var updateComment=jest.fn(()=>{
					window.XMLHttpRequest = jest.fn().mockImplementation(server.putCommentServer);
					server.putCommentServer.open("POST", "/feed/comments", true);
					var obj={"comment":"this is the latest comment","userId":"user123123","feedId":"feed1","commentId":"comment3"};
					server.putCommentServer.send(obj);
			
			})			
		
						
			test(`The putFeed button`, () => {
			  var data=getFeeds()
			  feed.appendData(data)
			  var editButton=document.getElementById("editCommentcomment3")
			  editButton.onclick=addSubmitButton()
			  editButton.click()
	
			  var putComment=document.getElementById("updateCcomment3")
			  putComment.onclick=updateComment
			  putComment.click()
	
			  var data=getFeeds()
			  feed.appendData(data)
	
	
			  expect(getFeeds).toHaveBeenCalled()
			  expect(getFeeds).toHaveBeenCalledTimes(2)
			  expect(updateComment).toHaveBeenCalled()
			  expect(document.getElementById("dataCommentcomment3").querySelectorAll("li")[0].innerHTML).toBe("this is the latest comment")
			  expect(updateComment).toHaveBeenCalledTimes(1)
			})
			
				
	
})

