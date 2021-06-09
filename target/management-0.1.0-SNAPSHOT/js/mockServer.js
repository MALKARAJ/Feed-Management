var mocks={
    "code": "200",
    "success": true,
    "feeds": [
        {
			"feedId":"feed1",
            "category": "Music",
            "content": "Hello",
			"comments":[{"commentId":"comment1","userId":"user123123","comment":"hi"}],
			"userId":"user123123"
        }
    ]
};

let getServer = {
    open: jest.fn(),
    setRequestHeader: jest.fn(),
    onload: ()=>{return JSON.stringify(mocks)},
    send: jest.fn(),
    status: 200
}

let postServer = {
    open: jest.fn(),
    setRequestHeader: jest.fn(),

    send: (data)=>{
			data["feedId"]="feed"+(mocks.feeds.length+1)
			data["comments"]=[]
			mocks.feeds.push(data);

		},
    onload: ()=>{
					return JSON.stringify(mocks);
				},
    status: 200
}



let putServer = {
    open: jest.fn(),
    setRequestHeader: jest.fn(),

    send: (data)=>{
			
			for(let i=0;i<mocks.feeds.length;i++)
			{
				if(mocks.feeds[i]["feedId"]==data["feedId"])
				{
					//delete data.like;
					mocks.feeds[i]["content"]=data["content"];
				}
			}
		},
    onload: ()=>{
					return JSON.stringify(mocks);
				},
    status: 200
}


let getCommentServer = {
    open: jest.fn(),
    setRequestHeader: jest.fn(),
    onload: ()=>{return JSON.stringify(mocks)},
    send: jest.fn(),
    status: 200
}

let postCommentServer = {
    open: jest.fn(),
    setRequestHeader: jest.fn(),

    send: (data)=>{
			
			for(let i=0;i<mocks["feeds"].length;i++)
			{
				if(mocks.feeds[i].feedId==data["feedId"])
				{
					data["commentId"]="comment"+(mocks.feeds[i].comments.length+1)
					mocks.feeds[i].comments.push(data);
				}
			}
		},
    onload: ()=>{
					return JSON.stringify(mocks);
				},
    status: 200
}



let putCommentServer = {
    open: jest.fn(),
    setRequestHeader: jest.fn(),

    send: (data)=>{
			
			for(let i=0;i<mocks.feeds[0].comments.length;i++)
			{
				if(mocks.feeds[0].comments[i]["commentId"]==data["commentId"])
				{
					mocks.feeds[0].comments[i]["comment"]=data["comment"];
				}
			}
		},
    onload: ()=>{
					return JSON.stringify(mocks);
				},
    status: 200
}









function UUID(){
    var dt = new Date().getTime();
    var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = (dt + Math.random()*16)%16 | 0;
        dt = Math.floor(dt/16);
        return (c=='x' ? r :(r&0x3|0x8)).toString(16);
    });
    return uuid;
}

module.exports={getServer,postServer,putServer,postCommentServer,putCommentServer};