package com.feed;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class CredentialValidator {
	
	
	
	
	public boolean isValidateCredentials(String email)
	{
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";  
        Pattern pattern = Pattern.compile(regex);  
        Matcher matcher = pattern.matcher(email);  
        if(!matcher.matches())
        {
        	return false;
        }
        if(email=="")
        {
        	return false;
        }
		return true;
	}
}
