package com.amazon.buspassmanagement;

import org.junit.Assert;
import org.junit.Test;

import com.amazon.buspassmanagement.controller.AuthenticationService;
import com.amazon.buspassmanagement.model.User;

// Reference Link to Use JUnit as Testing Tool in your Project
// https://maven.apache.org/surefire/maven-surefire-plugin/examples/junit.html

public class AppTest {
    
	AuthenticationService authService = AuthenticationService.getInstance();
	
	// UNIT TESTS
	
	@Test
	public void testUserLogin() {
		
		User user = new User();
		user.email = "vish@gmail.com";
		//user.password = "vish1234";
		user.password = "hYCRBwCSVDKcPPczObOXbLZmNCwfqiUY/e0xF1zI0Qg=";
		
		boolean result = authService.loginUser(user);
		
		// Assertion -> Either Test Cases Passes or It will Fail :)
		Assert.assertEquals(true, result);
		
	}
	
	@Test
	public void testAdminLogin() {
		
		User user = new User();
		user.email = "vman@amazon.com";
		user.password = "5S1g1C5XFd93zLpiFNwyyMOj+tuhgtGS8+ETXflElN0=";
		
		boolean result = authService.loginUser(user);
		
		// Assertion -> Either Test Cases Passes or It will Fail :)
		Assert.assertEquals(true, result);
		
	}
	
}
