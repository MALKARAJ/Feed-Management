package com.feed;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;

public class Bcrypt {

	@Test
	public void test() {
		UUID id=UUID.randomUUID();
		String password="george112233";
		String hash=BCrypt.hashpw(password.toString(),BCrypt.gensalt(10));
		String hash2=BCrypt.hashpw(hash,BCrypt.gensalt(10));
		System.out.println(hash);
		assertFalse(BCrypt.checkpw(password, hash2));
	}

}
