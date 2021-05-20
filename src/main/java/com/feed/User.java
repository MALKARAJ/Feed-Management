package com.feed;

import java.util.Date;
import java.util.UUID;

public class User {

		 String email;
		 String password;
		 String userId;
		 String image;
		 Boolean active;
		 Date date;
		 String error;
		public Date getDate() {
			return date;
		}
		public void setDate(Date date) {
			this.date = date;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public void setUserId(String userId) {
			this.userId=userId;
		}
		public String getUserId() {
			return userId;
		}
		public String getError() {
			return error;
		}
		public void setError(String error) {
			this.error = error;
		}
		public String getImage() {
			return image;
		}
		public void setImage(String image) {
			this.image = image;
		}
		public Boolean getActive() {
			return active;
		}
		public void setActive(Boolean active) {
			this.active = active;
		}
		

		
}
