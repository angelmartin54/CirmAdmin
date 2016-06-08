package com.example.tests;


import static org.junit.Assert.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import junit.framework.Assert;

import org.bouncycastle.cms.PasswordRecipientInfoGenerator;
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
	private int failedSRCounter;
	private int pageNumber;
	private String site = "https://s0144549.miamidade.gov/html/cirmadmin/app/index.html#/login";
//	private String site = "https://s0144654/html/cirmadmin/app/index.html#/login";
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
		
		failedSRCounter = 0;
		pageNumber = 1;
		
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
	
	private void sendScreenshot (){
		failedSRCounter++;
		SendEmail.send("angel.martin@miamidade.gov", "CiRM-Admin-Test-FAILED", "While checking to see if the SR Questions loaded Q/A test found that SR questions did not load a SR ascreenshot of the case can be found at the link below<br>File:///C://Users/angel.martin.MIAMIDADE/Desktop/failedtest");
		selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/SR_ques_"+failedSRCounter+".png");
	}
	
	private void clickNext(){
		selenium.click("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > div > div > div > ul > li:nth-child(11) > a");
	}
	
	private void gotToPage(int aPageNumber){
		for (int i=1; i<aPageNumber; i++){
			clickNext();
		}
	}
	
	private boolean checkSrQuestions() throws Exception {
		for (int loop = 1; loop <= 10; loop++){
			if (selenium.isElementPresent("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > table > tbody > tr:nth-child("+loop+") > td:nth-child(1) > a")){
				selenium.click("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > table > tbody > tr:nth-child("+loop+") > td:nth-child(1) > a");
				selenium.click("id=nav-questions");
				Thread.sleep(5000);
				if (!selenium.isElementPresent("id=container-0")){
					sendScreenshot();
				}
				selenium.click("id=left-nav-sr");
				gotToPage(pageNumber);
			} else {
				return false;
			}
		}		
		
		return true;
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
	
	@Test
	public void Demo() throws Exception {
		login();
		
		boolean goNextPage = false;
		
		do{
			goNextPage = checkSrQuestions();
			
			if (goNextPage) {
				pageNumber++;
				clickNext();
			}
			
		} while (goNextPage);
		
	}
	
	
	
	@Test
	public void SRQuestions() throws Exception {
		login();
		selenium.click("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > table > tbody > tr:nth-child(1) > td:nth-child(1) > a");
		selenium.click("id=nav-questions");
		Thread.sleep(5000);
		if ((selenium.isElementPresent("id=container-0"))) {} //Do smthn
		else {
          		SendEmail.send("angel.martin@miamidade.gov", "CiRM-Admin-Test-FAILED", "test");
				selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/SR_ques_1.png");
			 }
		selenium.click("id=left-nav-sr");
		selenium.click("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > table > tbody > tr:nth-child(2) > td:nth-child(1) > a");
		selenium.click("id=nav-questions");
		Thread.sleep(5000);
		if ((selenium.isElementPresent("id=container-0"))) {} //Do smthn
		else {
          		SendEmail.send("angel.martin@miamidade.gov", "CiRM-Admin-Test-FAILED", "test");
				selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/Login.png");
			 }
		selenium.click("id=left-nav-sr");
		selenium.click("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > table > tbody > tr:nth-child(3) > td:nth-child(1) > a");
		selenium.click("id=nav-questions");
		Thread.sleep(5000);
		if ((selenium.isElementPresent("id=container-0"))) {} //Do smthn
		else {
          		SendEmail.send("angel.martin@miamidade.gov", "CiRM-Admin-Test-FAILED", "test");
				selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/Login.png");
			 }
		selenium.click("id=left-nav-sr");
		selenium.click("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > table > tbody > tr:nth-child(4) > td:nth-child(1) > a");
		selenium.click("id=nav-questions");
		Thread.sleep(5000);
		if ((selenium.isElementPresent("id=container-0"))) {} //Do smthn
		else {
          		SendEmail.send("angel.martin@miamidade.gov", "CiRM-Admin-Test-FAILED", "test");
				selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/Login.png");
			 }
		selenium.click("id=left-nav-sr");
		selenium.click("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > table > tbody > tr:nth-child(5) > td:nth-child(1) > a");
		selenium.click("id=nav-questions");
		Thread.sleep(5000);
		if ((selenium.isElementPresent("id=container-0"))) {} //Do smthn
		else {
          		SendEmail.send("angel.martin@miamidade.gov", "CiRM-Admin-Test-FAILED", "test");
				selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/Login.png");
			 }
		selenium.click("id=left-nav-sr");
		selenium.click("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > table > tbody > tr:nth-child(6) > td:nth-child(1) > a");
		selenium.click("id=nav-questions");
		Thread.sleep(5000);
		if ((selenium.isElementPresent("id=container-0"))) {} //Do smthn
		else {
          		SendEmail.send("angel.martin@miamidade.gov", "CiRM-Admin-Test-FAILED", "test");
				selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/Login.png");
			 }
		selenium.click("id=left-nav-sr");
		selenium.click("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > table > tbody > tr:nth-child(7) > td:nth-child(1) > a");
		selenium.click("id=nav-questions");
		Thread.sleep(5000);
//		2008 Structure Registration
		if ((selenium.isElementPresent("id=container-0"))) {} //Do smthn
		else {
          		SendEmail.send("angel.martin@miamidade.gov", "CiRM-Admin-Test-FAILED", "test");
				selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/Login.png");
			 }
        selenium.click("id=left-nav-sr");
		selenium.click("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > table > tbody > tr:nth-child(8) > td:nth-child(1) > a");
		selenium.click("id=nav-questions");
		Thread.sleep(5000);
//		2008 Vacant Lot Registration
		if ((selenium.isElementPresent("id=container-0"))) {} //Do smthn
		else {
          		SendEmail.send("angel.martin@miamidade.gov", "test", "test");
				selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/Login.png");}
          		
		selenium.click("id=left-nav-sr");
		selenium.click("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > table > tbody > tr:nth-child(9) > td:nth-child(1) > a");
		selenium.click("id=nav-questions");
		Thread.sleep(5000);
		if ((selenium.isElementPresent("id=container-0"))) {} //Do smthn
		else {
          		SendEmail.send("angel.martin@miamidade.gov", "CiRM-Admin-Test-FAILED", "test");
				selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/Login.png");
			 }
		selenium.click("id=left-nav-sr");
		selenium.click("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > table > tbody > tr:nth-child(10) > td:nth-child(1) > a");
		selenium.click("id=nav-questions");
		Thread.sleep(5000);
		if ((selenium.isElementPresent("id=container-0"))) {} //Do smthn
		else {
          		SendEmail.send("angel.martin@miamidade.gov", "CiRM-Admin-Test-FAILED", "test");
				selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/Login.png");
			 }
		selenium.click("id=left-nav-sr");
//		click next page
		selenium.click("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > div > div > div > ul > li:nth-child(11) > a");
		selenium.click("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > table > tbody > tr:nth-child(1) > td:nth-child(1) > a");
		selenium.click("id=nav-questions");
		Thread.sleep(5000);
		if ((selenium.isElementPresent("id=container-0"))) {} //Do smthn
		else {
          		SendEmail.send("angel.martin@miamidade.gov", "CiRM-Admin-Test-FAILED", "test");
				selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/SR_ques_1.png");
			 }
		selenium.click("id=left-nav-sr");
//		click next page
		selenium.click("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > div > div > div > ul > li:nth-child(11) > a");
		selenium.click("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > table > tbody > tr:nth-child(2) > td:nth-child(1) > a");
		selenium.click("id=nav-questions");
		Thread.sleep(5000);
		if ((selenium.isElementPresent("id=container-0"))) {} //Do smthn
		else {
          		SendEmail.send("angel.martin@miamidade.gov", "CiRM-Admin-Test-FAILED", "test");
				selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/Login.png");
			 }
		selenium.click("id=left-nav-sr");
		selenium.click("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > div > div > div > ul > li:nth-child(11) > a");
		selenium.click("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > table > tbody > tr:nth-child(3) > td:nth-child(1) > a");
		selenium.click("id=nav-questions");
		Thread.sleep(5000);
		if ((selenium.isElementPresent("id=container-0"))) {} //Do smthn
		else {
          		SendEmail.send("angel.martin@miamidade.gov", "CiRM-Admin-Test-FAILED", "test");
				selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/Login.png");
			 }
		selenium.click("id=left-nav-sr");
		selenium.click("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > div > div > div > ul > li:nth-child(11) > a");
		selenium.click("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > table > tbody > tr:nth-child(4) > td:nth-child(1) > a");
		selenium.click("id=nav-questions");
		Thread.sleep(5000);
		if ((selenium.isElementPresent("id=container-0"))) {} //Do smthn
		else {
          		SendEmail.send("angel.martin@miamidade.gov", "CiRM-Admin-Test-FAILED", "test");
				selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/Login.png");
			 }
		selenium.click("id=left-nav-sr");
		selenium.click("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > div > div > div > ul > li:nth-child(11) > a");
		selenium.click("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > table > tbody > tr:nth-child(5) > td:nth-child(1) > a");
		selenium.click("id=nav-questions");
		Thread.sleep(5000);
		if ((selenium.isElementPresent("id=container-0"))) {} //Do smthn
		else {
          		SendEmail.send("angel.martin@miamidade.gov", "CiRM-Admin-Test-FAILED", "test");
				selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/Login.png");
			 }
		selenium.click("id=left-nav-sr");
		selenium.click("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > div > div > div > ul > li:nth-child(11) > a");
		selenium.click("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > table > tbody > tr:nth-child(6) > td:nth-child(1) > a");
		selenium.click("id=nav-questions");
		Thread.sleep(5000);
		if ((selenium.isElementPresent("id=container-0"))) {} //Do smthn
		else {
          		SendEmail.send("angel.martin@miamidade.gov", "CiRM-Admin-Test-FAILED", "test");
				selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/Login.png");
			 }
		selenium.click("id=left-nav-sr");
		selenium.click("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > div > div > div > ul > li:nth-child(11) > a");
		selenium.click("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > table > tbody > tr:nth-child(7) > td:nth-child(1) > a");
		selenium.click("id=nav-questions");
		Thread.sleep(5000);
		if ((selenium.isElementPresent("id=container-0"))) {} //Do smthn
		else {
          		SendEmail.send("angel.martin@miamidade.gov", "CiRM-Admin-Test-FAILED", "test");
				selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/Login.png");
			 }
		selenium.click("id=left-nav-sr");
		selenium.click("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > div > div > div > ul > li:nth-child(11) > a");
		selenium.click("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > table > tbody > tr:nth-child(8) > td:nth-child(1) > a");
		selenium.click("id=nav-questions");
		Thread.sleep(5000);
		if ((selenium.isElementPresent("id=container-0"))) {} //Do smthn
		else {
          		SendEmail.send("angel.martin@miamidade.gov", "CiRM-Admin-Test-FAILED", "test");
				selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/Login.png");
			 }
		selenium.click("id=left-nav-sr");
		selenium.click("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > div > div > div > ul > li:nth-child(11) > a");
		selenium.click("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > table > tbody > tr:nth-child(9) > td:nth-child(1) > a");
		selenium.click("id=nav-questions");
		Thread.sleep(5000);
		if ((selenium.isElementPresent("id=container-0"))) {} //Do smthn
		else {
          		SendEmail.send("angel.martin@miamidade.gov", "CiRM-Admin-Test-FAILED", "test");
				selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/Login.png");
			 }
		selenium.click("id=left-nav-sr");
		selenium.click("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > div > div > div > ul > li:nth-child(11) > a");
		selenium.click("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > table > tbody > tr:nth-child(10) > td:nth-child(1) > a");
		selenium.click("id=nav-questions");
		Thread.sleep(5000);
		if ((selenium.isElementPresent("id=container-0"))) {} //Do smthn
		else {
          		SendEmail.send("angel.martin@miamidade.gov", "CiRM-Admin-Test-FAILED", "test");
				selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/Login.png");
			 }
		selenium.click("id=left-nav-sr");
	}
	
	
	
	@Test
	public void enabledCheck() throws Exception {
		try{
			login();
			selenium.click("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div:nth-child(2) > div:nth-child(5) > div > div > label:nth-child(2)");
			selenium.click("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > div > div > div > div > button:nth-child(4) > span");
			Thread.sleep(500);
			selenium.isTextPresent("Disable");
			selenium.getText("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body");
//			selenium.isVisible("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > table > tbody > tr:nth-child(1) > td:nth-child(5) > button");
		}catch (Exception e){
			
		}
	}
	
	
	
	@After
	public void tearDown() throws Exception {
//	 selenium.stop();
//	 selenium.shutDownSeleniumServer();
//	 ln("server successfully shut down.");
	
	}
}
