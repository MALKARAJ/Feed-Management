

var server=require("./mockServer.js")
var fixture=require("./feedFixture.js")
var validate=require("./feedOperation.js")


beforeEach(() => {
		document.body.innerHTML=fixture.fixture()
});

afterEach(()=>{
		jest.clearAllMocks();

})

describe('Mock CRUD operations',()=>
{
	
		
	test('Get Requests', () =>
	{
		window.XMLHttpRequest = jest.fn().mockImplementation(server.getServer);
		server.getServer.open("Get", "/feed", true);
		server.getServer.send();
		expect(server.getServer.open).toBeCalledWith("Get", "/feed", true)
		expect(server.getServer.send).toBeCalled();
		var data=JSON.parse(server.getServer.onload());
		expect(data["success"]).toBe(true);

	});
	
	
	
	
	test('Post Requests', () =>
	{
		window.XMLHttpRequest = jest.fn().mockImplementation(server.postServer);
		server.postServer.open("POST", "/feed", true);
		var obj={"category":"Music","content":"Hello","userId":"user123123"};
		expect(validate.isValidFeed(obj)).toBe(true);
		server.postServer.send(obj);
		var data=JSON.parse(server.postServer.onload());
		expect(data["success"]).toBe(true);
		expect(data["feeds"].length==2);


	});

	
	test('Put Requests', () =>
	{
		window.XMLHttpRequest = jest.fn().mockImplementation(server.putServer);
		server.putServer.open("PUT", "/feed", true);
		var obj={"userId":"user123123","feedId": "feed1","category": "Music","content": "hey","like":"false"};
		expect(validate.isValidFeedUpdate(obj)).toBe(true);
		server.putServer.send(obj);
		expect(server.putServer.open).toBeCalledWith('PUT', '/feed', true);
		var data=JSON.parse(server.putServer.onload());
		expect(data["success"]).toBe(true);
		expect(data["feeds"][0]["content"]=="hey");

	});
	
	
});



describe("Testing UI elements for feeds",()=>{
	
	
				test(`The getFeeds button`, () => {
				    window.XMLHttpRequest = jest.fn().mockImplementation(server.getServer);
					server.getServer.open("Get", "/feed", true);
					server.getServer.send();
					var data=JSON.parse(server.getServer.onload());
					validate.appendData(data)
					expect(document.getElementById("datacfeed1").querySelectorAll("li")[0].innerHTML).toBe("hey")
			})
			
			
			
			
			var getFeeds=jest.fn(()=>{
					window.XMLHttpRequest = jest.fn().mockImplementation(server.getServer);
					server.getServer.open("Get", "/feed", true);
					server.getServer.send();
					var data=JSON.parse(server.getServer.onload());
					//console.log(data)
					return data
			
			})
			
			var addFeed=jest.fn(()=>{
					window.XMLHttpRequest = jest.fn().mockImplementation(server.postServer);
					server.postServer.open("POST", "/feed", true);
					var obj={"category":"Music","content":"this is the new content","userId":"user123123"};
					server.postServer.send(obj);
			
			})
			
			

						
			
			test(`The postFeed button`, () => {
			
			  var postFeeds=document.getElementById("submitFeed")
			  postFeeds.onclick=addFeed
			  postFeeds.click()
			  var data=getFeeds()
			  validate.appendData(data)
			  expect(getFeeds).toHaveBeenCalled()
			  expect(getFeeds).toHaveBeenCalledTimes(1)
			  expect(addFeed).toHaveBeenCalled()
			  expect(document.getElementById("datacfeed3").querySelectorAll("li")[0].innerHTML).toBe("this is the new content")
			  expect(addFeed).toHaveBeenCalledTimes(1)
			})
			
			
			
			var addSubmitButton=()=>{
				var container=document.getElementById("datacfeed3")
				txt=`<div class="editer" style="display:flex;">
				<input type="button" id="updatefeed3" value="save" onclick="updateFeed()">
				<input type="button" value="close" onclick="getFeeds()">
				</div>`;			
				container.innerHTML+=txt
				
				}
				
			var updateFeed=jest.fn(()=>{
					window.XMLHttpRequest = jest.fn().mockImplementation(server.putServer);
					server.putServer.open("PUT", "/feed", true);
					var obj={"feedId":"feed3","category":"Music","content":"this is the latest content","userId":"user123123"};
					server.putServer.send(obj);
			
			})
						
			test(`The putFeed button`, () => {
			  var data=getFeeds()
			  validate.appendData(data)
	
			  var editButton=document.getElementById("editFeedfeed3")
			  editButton.onclick=addSubmitButton()
			  editButton.click()
	
			  var putFeed=document.getElementById("updatefeed3")
			  putFeed.onclick=updateFeed
			  putFeed.click()
	
			  var data=getFeeds()
			  validate.appendData(data)
	
	
	
			  expect(getFeeds).toHaveBeenCalled()
			  expect(getFeeds).toHaveBeenCalledTimes(2)
			  expect(updateFeed).toHaveBeenCalled()
			  expect(document.getElementById("datacfeed3").querySelectorAll("li")[0].innerHTML).toBe("this is the latest content")
			  expect(updateFeed).toHaveBeenCalledTimes(1)
			})
			
				
	
})
