package com.feed;

import java.util.Date;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.PostPut;
import com.google.appengine.api.datastore.PrePut;
import com.google.appengine.api.datastore.PutContext;
import com.google.appengine.repackaged.org.joda.time.DateTime;

public class DatastoreCallbacks {
	
	
    static Logger logger = Logger.getLogger("logger");

    @PrePut
    void log(PutContext context) {
    	
        logger.fine("Finished putting " + context.getCurrentElement().getKey());
    }

    @PrePut(kinds = {"Feed","Comment"})
    void updateTimestamp(PutContext context) {
        logger.info(context.getCurrentElement().getProperty("Updation_date").toString());
        Date date=new Date();
    	DateTime d=new DateTime(date);
        context.getCurrentElement().setProperty("Updation_date", d.getMillis());    		
    

    }

}
