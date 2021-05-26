var cache=new Map()
var Cache={
	has:(key)=>{
	    return  cache.has(key);
	},
	set:(key, value)=>{
	    return  cache.set(key, value);
	},
	get:(key)=> {
	    return  cache.get(key);
	},
	del:(key)=>{
	    return  cache.delete(key);
	},
	clear:()=>{
	    return  cache.clear();
	  },
}
