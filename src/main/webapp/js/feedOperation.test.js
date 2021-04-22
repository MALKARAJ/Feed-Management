

var server=require("./mockServer.js")

var validate=require("/src/main/webapp/feedOperation.js")

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
		console.log(data);
		expect(data["success"]).toBe(true);
		jest.clearAllMocks();

	});
	
	
	
	
	test('Post Requests', () =>
	{
		window.XMLHttpRequest = jest.fn().mockImplementation(server.postServer);
		server.postServer.open("POST", "/feed", true);
		var obj={"category":"Music","content":"Hello"};
		expect(validate.isValidFeed(obj)).toBe(true);
		server.postServer.send(obj);
		var data=JSON.parse(server.postServer.onload());
		console.log(data);
		expect(data["success"]).toBe(true);
		expect(data["feeds"].length==2);
		jest.clearAllMocks();


	});

	
	test('Put Requests', () =>
	{
		window.XMLHttpRequest = jest.fn().mockImplementation(server.putServer);
		server.putServer.open("PUT", "/feed", true);
		var obj={"feedId": "2f05986e-a343-421a-8d16-deb35ea44aa3","category": "Music","content": "hey","like":"false"};
		expect(validate.isValidFeedUpdate(obj)).toBe(true);
		server.putServer.send(obj);
		expect(server.putServer.open).toBeCalledWith('PUT', '/feed', true);
		var data=JSON.parse(server.putServer.onload());
		console.log(data);
		expect(data["success"]).toBe(true);
		expect(data["feeds"][0]["content"]=="Hey");
		jest.clearAllMocks();

	});
	
	
});



