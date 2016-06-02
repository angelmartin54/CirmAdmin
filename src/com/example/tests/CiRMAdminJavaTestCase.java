package com.example.tests;


import static org.junit.Assert.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.server.RemoteControlConfiguration;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;


@SuppressWarnings("deprecation")
public class CiRMAdminJavaTestCase {
	//Variables for Post deploymnet tests
	//can be used across methods.
	private Selenium selenium;
//	private String site = "https://s0144654/html/cirmadmin/app/index.html#/login";
	private String site = "https://s0144654/html/cirmadmin/app/index.html#/login";
	private String loginUserID = "c203036";
	private String pass = "blah";
	private String longPwd = "something"; 
	private String recipients = "rajiv@miamidade.gov";
	private void ln (Object test){
		System.out.println(test);
	}
		
	//private String longPwd = "password";
	//private String pageLoadTime= "50000";
	public class SimpleOnFailed extends TestWatcher {
	    @Override
	    protected void failed(Throwable e, Description description) {
	    	ln("failed");
	     }
	}
		
	//private String longPwd = "password";
	//private String pageLoadTime= "50000";
	
	
	public static boolean isMyServerUp(){
		try {
			URL uri = new URL ("http://localhost:4444/wd/hub/status");
		
			HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
//			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept-Charset", "UTF-8");
			connection.getInputStream();
			int HttpResult = connection.getResponseCode();
		    if(HttpResult == HttpURLConnection.HTTP_OK) return true;
		    else return false;
		  
		}catch (Exception e){
			return false;
		} 		
	}
	
	private Thread myThread = new Thread() {
	    public void run() {
	        try {
	        	Process P = Runtime.getRuntime().exec("cmd /c start javaw -jar C:\\users\\angel.martin.MIAMIDADE\\Downloads\\selenium-java-2.52.0\\selenium-2.52.0\\selenium-server-standalone-2.52.0.jar -trustAllSSLCertificates");
	        	P.waitFor();
				System.out.println("Sucessfully started selenium server");
	        	
	        } catch(Exception e) {
	            System.out.println(e);
	        }
	    }  
	};
	 
	
	@Before
	public void startServer () throws Exception {
//		SendEmail.send("rajiv@miamidade.gov","test is starting", "test is starting");
		
		myThread.start();

		int c = 0;
		do {
			Thread.sleep(1000);
			c++;
			} while (!isMyServerUp() && c < 10);
		
		if (c>10) throw new RuntimeException("Failure to contact selenium sever after ten attempts");
	 
		RemoteControlConfiguration settings = new RemoteControlConfiguration();
		settings.setTrustAllSSLCertificates(true);
		
		selenium = new DefaultSelenium("localhost", 4444, "*googlechrome C:/Program Files (x86)/Google/Chrome/Application/chrome.exe" , site);              
         
		
        selenium.start();
	}
	@Test
	public void login() throws Exception {
		try{
			selenium.open(site);
			selenium.type("css=#user", loginUserID);
			selenium.type("css=#password", pass);
			selenium.click("id=login-button");
			Thread.sleep(8000);
			selenium.isTextPresent("Service Request");
		}catch (Exception e){
            System.out.println(e);
            SendEmail.send("angel.martin@miamidade.gov", "test", "**login test has failed**<br><br>Screen shot on failure can be found at File:///C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/Login.png<br><br><br>To manually test this follow the steps below<br>* Open Chorme and navigate to the CiRM application<br>* Fill in the User and Password boxes<br>* Then click the Login button and wait 8-10 seconds for the application to load if the page loads the test has passed<br><br>"+e.getMessage());
            selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/Login.png");
            Assert.fail();}
		}
	@After
	public void tearDown() throws Exception {
	 selenium.stop();
	 selenium.shutDownSeleniumServer();
	 ln("server successfully shut down.");
	
	}
}
