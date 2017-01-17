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
import org.omg.CORBA.SystemException;
import org.openqa.selenium.server.RemoteControlConfiguration;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.webdriven.commands.IsTextPresent;


@SuppressWarnings("deprecation")
public class CiRMAdminJavaTestCase {
	private Selenium selenium;
	private int failedSRCounter;
	private int pageNumber;
//	private String site = "http://10.10.32.14/app/index.html";
//	private String site = "https://s0144654.miamidade.gov/html/cirmadmin/app/index.html#/login";
	private String site = "https://cirmadmin-test.miamidade.gov/html/cirmadmin/app/index.html#/login";
//	private String site = "https://s0144654/html/cirmadmin/app/index.html#/login";
	private String loginUserID = "c203036";
	private String pass = "blah";
	private String longPwd = "something"; 
	private String recipients = "rajiv@miamidade.gov";
	private void ln (Object test){
		System.out.println(test);
	}
	Object empty = new Object();
		
	
	public class SimpleOnFailed extends TestWatcher {
	    @Override
	    protected void failed(Throwable e, Description description) {
	    	ln("failed");
	     }
	}

	
	

	
	
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
	
	public void lineCheck(){
		ln("inside linecheck");
		String val = selenium.getText("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > table > tbody > tr:nth-child(4) > td:nth-child(3)");
		ln("here1"+ val);
		if (val!=null && val.equals("")){
			ln("It is empty");
		}
		else{
			ln("Not Empty");
		}
		
		ln("made it to vis");
	}	
	
	private boolean checkSrDepartment() throws Exception {
	ln("insidecheckdepartment");
		for (int loop = 1; loop <= 10; loop++){
			Thread.sleep(250);
			if (selenium.isElementPresent("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > table > tbody > tr:nth-child("+loop+") > td:nth-child(2) > a")==true)
				{
					String val = selenium.getText("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > table > tbody > tr:nth-child("+loop+") > td:nth-child(4)");
					if ((val==null==true) || (val.equals("")==true)){
						String srType = selenium.getText("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > table > tbody > tr:nth-child("+loop+") > td:nth-child(2) > a");
						SendEmail.send("angel.martin@miamidade.gov", "Sr Department Check", "The following service request did not retun a department: "+srType);
						ln(srType);
						}
				} else {
					return false;
			}
		}		
		return true;
	}
	
	private void sendScreenshot (){
//		ln("insidescreenshot");
		failedSRCounter++;
		String url = selenium.getLocation();
		String[] array = url.split("/");
		String array7 = array[7];				
		String srType = selenium.getText("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-heading > h3");
		SendEmail.send("angel.martin@miamidade.gov", "CiRM-Admin-Test-FAILED", "While checking if SR Questions loaded Q/A test found that SR questions did not load,<br>A screenshot of the case can be found at the link below<br><br>File:///C://Users/angel.martin.MIAMIDADE/Desktop/failedtest<br><br>"+ "SR Type in ERROR: " + srType);
		selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/SR_ques_"+failedSRCounter+".png");
		ln(srType+","+array7);
		
	}
	
	private void clickNext(){
//		ln("insideclicknext");
		int i = 0;
		do{
		selenium.focus("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > div > div > div > ul > li:nth-child(11) > a");
		selenium.click("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > div > div > div > ul > li:nth-child(11) > a");
		}while(selenium.isElementPresent("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > div > div > div > ul > li:nth-child(11) > a")==false&i<3);
	}
	
	private void gotToPage(int aPageNumber){
//		ln("insidegotopage");
		for (int i=1; i<aPageNumber; i++){
			clickNext();
		}
	}
	
	private boolean checkSrQuestions() throws Exception {
		for (int loop = 1; loop <= 10; loop++){
			if (selenium.isElementPresent("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > table > tbody > tr:nth-child("+loop+") > td:nth-child(2) > a"))
				{
					selenium.click("css=#main-wrapper > ui-view > section > section > div > div > ui-view > div > div.panel-body > div.form-inline > div > table > tbody > tr:nth-child("+loop+") > td:nth-child(2) > a");
					selenium.click("id=nav-questions");
					Thread.sleep(5000);
					if (!selenium.isElementPresent("css=#expListLi")){
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
				int i = 0;	
			do{
				selenium.open(site);
				selenium.type("css=#user", loginUserID);
				selenium.type("css=#password", pass);
				selenium.click("id=login-button");
				Thread.sleep(4000);
				i++;
				System.out.println(i);
				ln(selenium.isTextPresent("Service Request"));
			} while (selenium.isTextPresent("Service Request")==false & i < 3);
			}catch (Exception e){
					Assert.fail();}
			}
	
	
	@Test
	public void srQuestions() throws Exception {
		try {
			login();

			boolean goNextPage = false;

			do {
				goNextPage = checkSrQuestions();

				if (goNextPage) {
					pageNumber++;
					clickNext();
				}

			} while (goNextPage);
		} catch (Exception e) {
			selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/demoFailure.png");
			ln(e);
			ln(e.getMessage());
			SendEmail.send("angel.martin@miamidade.gov,chirino@miamidade.gov", "SR Questions Test Failed",
					"Check test failed");
		}
	}
				
		@Test
		public void srDepartment() throws Exception {
			login();
			boolean goNextPage = false;
			
			do{
				goNextPage = checkSrDepartment();
				
				if (goNextPage) {
					pageNumber++;
					clickNext();
				}
				
			} while (goNextPage);
			
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
