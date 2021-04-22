var mocks={
    "code": "200",
    "success": true,
    "feeds": [
        {
			"feedId":"2f05986e-a343-421a-8d16-deb35ea44aa3",
            "category": "Music",
            "content": "Hello",
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
			data["feedId"]=UUID().toString();
			
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
					delete data.like;
					mocks.feeds[i]=data;
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

module.exports={getServer,postServer,putServer};