package com.amazon.buspassmanagement.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import com.amazon.buspassmanagement.BusPassSession;
import com.amazon.buspassmanagement.customExceptions.UserException;
import com.amazon.buspassmanagement.db.BusPassDAO;
import com.amazon.buspassmanagement.model.BusPass;
import com.amazon.buspassmanagement.model.User;

public class BusPassService {

	BusPassDAO passDAO = new BusPassDAO();
	
	// Create it as a Singleton 
	private static BusPassService passService = new BusPassService();
	Scanner scanner = new Scanner(System.in);
	
	public static BusPassService getInstance() {
		return passService;
	}
	
	private BusPassService() {
	
	}
	
	// Handler for the Bus Pass :)
	public void requestPass() throws UserException {
		BusPass pass = new BusPass();
		//pass.getDetails(false);
		int routeId= pass.getRouteIdInput();
		pass.uid = BusPassSession.user.id;
		
		// As initially record will be inserted by User where it is a request
		//pass.status = 1; // initial status as requested :)
		if(isPassAppliedForRoute(BusPassSession.user.id, routeId)) {
			throw new UserException("You're applying for the same pass again");
		}
		if(isPassAppliedAndSuspended(BusPassSession.user.id, routeId)) {
			throw new UserException("You've pass suspended on this route");
		}
		
		int result = passDAO.insert(pass);
		String message = (result > 0) ? "Pass Requested Successfully" : "Request for Pass Failed. Try Again.."; 
		System.out.println(message);
	}
	
	public boolean isPassAppliedForRoute(int userId, int routeId) {
		String sql = "SELECT * FROM buspass WHERE uid = '"+userId+"' AND routeId= '"+routeId+"';";
		
		List<BusPass> objects = passDAO.retrieve(sql);
		
		if(objects.size() > 0) {
			return true;
		}	
		
		return false;
	}
	
	public boolean isPassAppliedAndSuspended(int userId, int routeId) {
		String sql = "SELECT * FROM buspass WHERE uid = '"+userId+"' AND routeId= '"+routeId+"' AND status=4;";
		
		List<BusPass> objects = passDAO.retrieve(sql);
		
		if(objects.size() > 0) {
			return true;
		}	
		
		return false;
	}
	
	public void deletePass() {
		BusPass pass = new BusPass();
		System.out.println("Enter Pass ID to be deleted: ");
		pass.id = scanner.nextInt();
		int result = passDAO.delete(pass);
		String message = (result > 0) ? "Pass Deleted Successfully" : "Deleting Pass Failed. Try Again.."; 
		System.out.println(message);
	}
	
	/*
	 
	 	Extra Task:
	 	IFF : You wish to UpSkill :)
	 
	 	Scenario: Open the same application in 2 different terminals
	 	1 logged in by user
	 	another logged in by admin
	 	
	 	If admin, approves or rejects the pass -> User should be notified :)
	 	
	 	Reference Link
	 	https://github.com/ishantk/AmazonAtlas22/blob/master/Session8/src/com/amazon/atlas/casestudy/YoutubeApp.java
	 
	 */
	
	public void approveRejectPassRequest() {
		
		BusPass pass = new BusPass();

		System.out.println("Enter Pass ID: ");
		pass.id = scanner.nextInt();
		
		System.out.println("2: Approve");
		System.out.println("3: Cancel");
		System.out.println("Enter Approval Choice: ");
		pass.status = scanner.nextInt();

    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		Calendar calendar = Calendar.getInstance();
		Date date1 = calendar.getTime();
		pass.approvedRejectedOn = dateFormat.format(date1);
		
		if(pass.status == 2) {
			//calendar.add(Calendar.YEAR, 1);
			Date date2 = calendar.getTime();
			pass.validTill = dateFormat.format(date2);
		}else {
			pass.validTill = pass.approvedRejectedOn;
		}
		
		int result = passDAO.update(pass);
		String message = (result > 0) ? "Pass Request Updated Successfully" : "Updating Pass Request Failed. Try Again.."; 
		System.out.println(message);
	}
	
	public void viewPassRequests() {
		
		System.out.println("Enter Route ID to get All the Pass Reqeuests for a Route");
		System.out.println("Or 0 for All Bus Pass Requests");
		System.out.println("Enter Route ID: ");
		
		int routeId = scanner.nextInt();
		
		List<BusPass> objects = null;
		
		if(routeId == 0) {
			objects = passDAO.retrieve();
		}else {
			String sql = "SELECT * from BusPass where routeId = "+routeId;
			objects = passDAO.retrieve(sql);
		}
		
		for(BusPass object : objects) {
			object.prettyPrint();
		}
	}
	
	public void viewPassRequestsByUser(int uid) {
		
		String sql = "SELECT * from BusPass where uid = "+uid;
		List<BusPass> objects = passDAO.retrieve(sql);
		
		for(BusPass object : objects) {
			object.prettyPrint();
		}
	}
	
	public void viewExpiredPass() {
		
		String sql = "SELECT * from BusPass where status = 'Suspended';";
		List<BusPass> objects = passDAO.retrieve(sql);
		
		for(BusPass object : objects) {
			object.prettyPrint();
		}
	}
	
	public void viewPassWithinRange(String from, String to) {

		String sql = "SELECT * FROM buspass WHERE "
				+ "createdOn BETWEEN "
				+ "'"+from+"' AND '"+to+"';";
		
		List<BusPass> objects = passDAO.retrieve(sql);
		
		for(BusPass object : objects) {
			object.prettyPrint();
		}
	}
}
