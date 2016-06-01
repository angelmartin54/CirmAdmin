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
public class 311-Responsive{
	//Variables for Post deploymnet tests
	//can be used across methods.
	private Selenium selenium;
//	private String site = "https://s0020284/html/startup.html#";
	private String site = "https://311hub.miamidade.gov/#";
	private String loginUserID = "c203036";
	private String pass = "hahahaha147";
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
			selenium.type("id=iUsername", loginUserID);
			selenium.type("id=iPassword", pass);
			selenium.click("id=btnLogin");
			Thread.sleep(8000);
			selenium.isTextPresent("Popular Searches");
		}catch (Exception e){
            System.out.println(e);
            SendEmail.send("angel.martin@miamidade.gov", "test", "**login test has failed**<br><br>Screen shot on failure can be found at File:///C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/Login.png<br><br><br>To manually test this follow the steps below<br>* Open Chorme and navigate to the CiRM application<br>* Fill in the User and Password boxes<br>* Then click the Login button and wait 8-10 seconds for the application to load if the page loads the test has passed<br><br>"+e.getMessage());
            selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/Login.png");
            Assert.fail();
        }}
	@Test
	public void ValidateAddress() throws Exception {
		try{
		login();
		selenium.click("//input[@value='']");
		selenium.type("//input[@value='']", "9920 sw 73rd st");
		selenium.click("//input[@value='Search']");
		Thread.sleep(4000);
		selenium.isVisible("css=#answer_hub > div:nth-child(1) > span > input.ic_valid.button_icon.visibility_visible");
		}catch (Exception e){
            System.out.println(e);
            selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/Validateaddress.png");
            SendEmail.send("angel.martin@miamidade.gov", "test", "Validate address"+e.getMessage());
            Assert.fail();
        }}
	@Test
	public void addloop() throws Exception {
		selenium.open(site);
		System.out.print("[");
		selenium.type("id=iUsername", loginUserID);
		selenium.type("id=iPassword", pass);
		selenium.click("id=btnLogin");
		Thread.sleep(8000);
		selenium.click("//input[@value='']");
		selenium.type("//input[@value='']", "9920 sw 73rd st");
		selenium.click("//input[@value='Search']");
		Thread.sleep(2000);
		if((selenium.isVisible("css=#answer_hub > div:nth-child(1) > span > input.ic_valid.button_icon.visibility_visible"))==true){
			System.out.print("=");
			selenium.refresh();
			selenium.type("id=iUsername", loginUserID);
			selenium.type("id=iPassword", pass);
			selenium.click("id=btnLogin");
			Thread.sleep(8000);
			selenium.click("//input[@value='']");
			selenium.type("//input[@value='']", "9920 sw 73rd st");
			selenium.click("//input[@value='Search']");
			Thread.sleep(2000);
		if((selenium.isVisible("css=#answer_hub > div:nth-child(1) > span > input.ic_valid.button_icon.visibility_visible"))==true){
			System.out.print("=");
			selenium.refresh();
			selenium.type("id=iUsername", loginUserID);
			selenium.type("id=iPassword", pass);
			selenium.click("id=btnLogin");
			Thread.sleep(8000);
			selenium.click("//input[@value='']");
			selenium.type("//input[@value='']", "9920 sw 73rd st");
			selenium.click("//input[@value='Search']");
			Thread.sleep(2000);
		if((selenium.isVisible("css=#answer_hub > div:nth-child(1) > span > input.ic_valid.button_icon.visibility_visible"))==true){
			System.out.print("=");
			selenium.refresh();
			selenium.type("id=iUsername", loginUserID);
			selenium.type("id=iPassword", pass);
			selenium.click("id=btnLogin");
			Thread.sleep(8000);
			selenium.click("//input[@value='']");
			selenium.type("//input[@value='']", "9920 sw 73rd st");
			selenium.click("//input[@value='Search']");
			Thread.sleep(2000);
			if((selenium.isVisible("css=#answer_hub > div:nth-child(1) > span > input.ic_valid.button_icon.visibility_visible"))==true){
				System.out.print("]: Test PASSED");
				}
		} else { 
			ln("adress did not validate");
			ln(selenium.isElementPresent("css=#answer_hub > div:nth-child(1) > span > input.ic_valid.button_icon.visibility_visible"));
			Assert.fail();
		}}}}			
	@Test
	public void GeoInfoTab() throws Exception {
		try{
		ValidateAddress();
		selenium.click("id=geo_info_district");
		assertTrue(selenium.isTextPresent("District"));
		}catch (Exception e){
            System.out.println(e);
            selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/GeoInfoTab.png");
            SendEmail.send("angel.martin@miamidade.gov", "test", "Geo Info Tab"+e.getMessage());
            Assert.fail();
        }}
	@Test
	public void OpenSRInAnswerHub() throws Exception {
		try{
		ValidateAddress();
		selenium.type("xpath=(//input[@type='text'])[6]", "Bulky");
		selenium.click("css=#answer_hub > div:nth-child(5) > span > input.submit.h32.button.blue");
		String Address = selenium.getText("css=#answer_hub > div:nth-child(1) > span > input.ic_field.h24.address_reset.color_green");
		selenium.click("link=BULKY TRASH REQUEST");
		Thread.sleep(2500);
		selenium.click("css=body > div:nth-child(13) > div.ui-dialog-buttonpane.ui-widget-content.ui-helper-clearfix > div > button:nth-child(1) > span");
		selenium.isTextPresent(Address);
		}catch (Exception e){
            System.out.println(e);
            selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/OpenSrInAnswerHub.png");
            SendEmail.send("angel.martin@miamidade.gov", "test", "Open Sr in Answer Hub"+e.getMessage());
            Assert.fail();
        }}	
	@Test
	public void ValidateinWCS() throws Exception {
		try{
		login();
		selenium.type("css=#answer_hub > div:nth-child(1) > span > input.ic_field.h24.address_reset", "9910 sw 73rd st");
		selenium.click("css=#answer_hub > div:nth-child(1) > span > input.submit.h32.button.blue");
		Thread.sleep(5000);
		selenium.click("css=#wcsTabLi > a");
		selenium.click("css=#wcs_right > input:nth-child(1)");
		Thread.sleep(1000);
		selenium.isTextPresent("11558262");
		}catch (Exception e){
			System.out.println(e);
			selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/ValidateWCS.png");
			SendEmail.send("angel.martin@miamidade.gov", "test", "Validate WCS"+e.getMessage());
			Assert.fail();
		}}	
	@Test
	public void MasterClr() throws Exception {
		try{
		OpenSRInAnswerHub();
		selenium.click("css=textarea.tooltip");
		selenium.type("css=textarea.tooltip", "test");
		selenium.click("css=div.grid_3.alpha > input");
		selenium.type("css=div.grid_3.alpha > input", "tester");
		selenium.type("css=div.grid_3.omega > input", "last tester");
		selenium.click("//div[@id='sr_details']/span/div[7]/input");
		selenium.type("//div[@id='sr_details']/span/div[7]/input", "test@test.com");
		selenium.click("css=span > div.grid_2 > input..error");
		selenium.type("css=span > div.grid_2 > input", "3051111111");
		selenium.click("link=WCS");
		selenium.click("css=#wcs_right > input.button.blue");
		Thread.sleep(8000);
		selenium.click("id=sendToSRButtonID");
		selenium.click("css=select..error");
		selenium.select("css=div.input_margins > div.grid_5.alpha > select", "label=No");
		selenium.click("xpath=(//button[@type='button'])[3]");
		selenium.click("//div[@id='sr_details']/div[23]/div/div[10]/select");
		selenium.select("//div[@id='sr_details']/div[23]/div/div[10]/select", "label=No");
		selenium.click("//div[@id='sr_details']/div[23]/div/div[14]/select");
		selenium.select("//div[@id='sr_details']/div[23]/div/div[14]/select", "label=Yes");
		selenium.click("//div[@id='sr_details']/div[23]/div/div[18]/select");
		selenium.select("//div[@id='sr_details']/div[23]/div/div[18]/select", "label=Yes");
		selenium.select("//div[@id='sr_details']/div[23]/div/div[22]/select", "label=FR (Front of)");
		selenium.select("//div[@id='sr_details']/div[23]/div/div[26]/select", "label=Yes");
		selenium.click("xpath=(//button[@type='button'])[3]");
		selenium.click("//div[@id='sr_details']/div[23]/div/div[26]/select");
		selenium.click("//div[@id='sr_details']/div[23]/div/div[30]/select");
		selenium.select("//div[@id='sr_details']/div[23]/div/div[30]/select", "label=No");
		selenium.click("css=input.tooltip.error");
		selenium.type("css=input.tooltip", "3331111111");
		selenium.click("//div[@id='sr_details']/div[23]/div/div[38]/select");
		selenium.select("//div[@id='sr_details']/div[23]/div/div[38]/select", "label=Yes");
		selenium.click("//input[contains(@id,'dp')]");
		selenium.type("//input[contains(@id,'dp')]", "04/15/2015");
		selenium.addSelection("css=select[title=\"To select multiple options - press CTRL and select the options you wish to.\"]", "label=Tree Cuttings");
		selenium.click("css=option[value=\"http://www.miamidade.gov/cirm/legacy#BULKYTRA_WHATTYPE_BTPTYPE_BTPTREE\"]");
		selenium.click("xpath=(//button[@type='button'])[3]");
		selenium.click("//div[@id='sr_details']/div[23]/div/div[50]/input");
		selenium.type("//div[@id='sr_details']/div[23]/div/div[50]/input", "3305111234");
		selenium.click("css=#sr_details > div.grid_2.omega > input.button.blue");
		selenium.click("xpath=(//button[@type='button'])[5]");
		selenium.click("css=#sr_details > div:nth-child(2) > input");
		selenium.click("css=body > div:nth-child(15) > div.ui-dialog-buttonpane.ui-widget-content.ui-helper-clearfix > div > button:nth-child(1) > span");
		assertFalse(selenium.isTextPresent("Contact Infromation"));
		}catch (Exception e){
            System.out.println(e);
            selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/MasterCLR.png");
            SendEmail.send("angel.martin@miamidade.gov", "test", "Mater CLR"+e.getMessage());
            Assert.fail();
        }}
// 	@Test	
 	public void SaveSr() throws Exception {
//		try{
 		OpenSRInAnswerHub();
		selenium.click("css=textarea.tooltip");
		selenium.type("css=textarea.tooltip", "test");
		selenium.click("css=div.grid_3.alpha > input");
		selenium.type("css=div.grid_3.alpha > input", "tester");
		selenium.type("css=div.grid_3.omega > input", "last tester");
		selenium.click("//div[@id='sr_details']/span/div[7]/input");
		selenium.type("//div[@id='sr_details']/span/div[7]/input", "test@test.com");
		selenium.click("css=span > div.grid_2 > input..error");
		selenium.type("css=span > div.grid_2 > input", "3051111111");
		selenium.click("link=WCS");
		selenium.click("css=#wcs_right > input.button.blue");
		Thread.sleep(8000);
		selenium.click("id=sendToSRButtonID");
		selenium.click("css=select..error");
		selenium.select("css=div.input_margins > div.grid_5.alpha > select", "label=No");
		selenium.click("xpath=(//button[@type='button'])[3]");
		selenium.click("//div[@id='sr_details']/div[23]/div/div[10]/select");
		selenium.select("//div[@id='sr_details']/div[23]/div/div[10]/select", "label=No");
		selenium.click("//div[@id='sr_details']/div[23]/div/div[14]/select");
		selenium.select("//div[@id='sr_details']/div[23]/div/div[14]/select", "label=Yes");
		selenium.click("//div[@id='sr_details']/div[23]/div/div[18]/select");
		selenium.select("//div[@id='sr_details']/div[23]/div/div[18]/select", "label=Yes");
		selenium.select("//div[@id='sr_details']/div[23]/div/div[22]/select", "label=FR (Front of)");
		selenium.select("//div[@id='sr_details']/div[23]/div/div[26]/select", "label=Yes");
		selenium.click("xpath=(//button[@type='button'])[3]");
		selenium.click("//div[@id='sr_details']/div[23]/div/div[26]/select");
		selenium.click("//div[@id='sr_details']/div[23]/div/div[30]/select");
		selenium.select("//div[@id='sr_details']/div[23]/div/div[30]/select", "label=No");
		selenium.click("css=input.tooltip.error");
		selenium.type("css=input.tooltip", "3331111111");
		selenium.click("//div[@id='sr_details']/div[23]/div/div[38]/select");
		selenium.select("//div[@id='sr_details']/div[23]/div/div[38]/select", "label=Yes");
		selenium.click("//input[contains(@id,'dp')]");
		selenium.type("//input[contains(@id,'dp')]", "04/15/2015");
		selenium.addSelection("css=select[title=\"To select multiple options - press CTRL and select the options you wish to.\"]", "label=Tree Cuttings");
		selenium.click("css=option[value=\"http://www.miamidade.gov/cirm/legacy#BULKYTRA_WHATTYPE_BTPTYPE_BTPTREE\"]");
		selenium.click("xpath=(//button[@type='button'])[3]");
		selenium.click("//div[@id='sr_details']/div[23]/div/div[50]/input");
		selenium.type("//div[@id='sr_details']/div[23]/div/div[50]/input", "3305111234");
//		selenium.click("css=#save");
//		selenium.getValue("css=#editorDiv > div.app_container > div.right_column.grid_2 > span > input.search");
//		System.out.println(selenium.getValue("css=#editorDiv > div.app_container > div.right_column.grid_2 > span > input.search"));
//		selenium.getValue("css=#editorDiv > div.app_container > div.right_column.grid_2 > span > input.search");
//		}catch (Exception e){
//            System.out.println(e);
//            selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failed test/SaveSr.png");
//            SendEmail.send("angel.martin@miamidade.gov", "test", e.getMessage());
//            Assert.fail();
//        }
		} 	
	@Test	
 	public void OpenSrBasicSearch() throws Exception {
 		try{
 		login();
 		selenium.click("link=Basic Search");
 		selenium.click("name=ServiceRequestType");
		selenium.type("name=ServiceRequestType", "BULKY TRASH REQUEST - MD");
		selenium.click("id=createdStartDate");
		selenium.type("id=createdStartDate", "-6");
		selenium.click("css=#advSearch_right > input[name=\"search\"]");
		Thread.sleep(6000);
		selenium.click("css=#advSearchResults > table > tbody > tr:nth-child(1) > td:nth-child(1) > a");
		selenium.click("css=body > div:nth-child(11) > div.ui-dialog-buttonpane.ui-widget-content.ui-helper-clearfix > div > button:nth-child(1) > span");
		Thread.sleep(5000);
		System.out.println(selenium.getValue("css=#editorDiv > div.app_container > div.right_column.grid_2 > span > input.search"));
		selenium.getValue("css=#editorDiv > div.app_container > div.right_column.grid_2 > span > input.search");
 		}catch (Exception e){
            System.out.println(e);
            selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/OpenSrBasicSearch.png");
            SendEmail.send("angel.martin@miamidade.gov", "test", "Open SR Basic Search"+e.getMessage());
            Assert.fail();
        }}
 	@Test
	public void FeildSort() throws Exception {
		try{
		selenium.open(site);
		selenium.type("id=iUsername", "c203036");
		selenium.type("id=iPassword", pass);
		selenium.click("id=btnLogin");
		Thread.sleep(8000);
		selenium.click("css=body > div.container_12 > div.banner.grid_12 > ul > li:nth-child(6) > a");
		selenium.type("name=ServiceRequestType", "POTHOLE - MD");
		selenium.click("id=showMoreFieldsId");
		selenium.select("css=#advsearch_moreDetails > div:nth-child(3) > select", "Locked");
		selenium.type("id=createdStartDate", "-1");
		selenium.click("css=#advSearch_right > input:nth-child(1)");
		Thread.sleep(4500);
		String sr1 = selenium.getText("css=#advSearchResults > table > tbody > tr:nth-child(1) > td:nth-child(1) > a");
		String[] parts = sr1.split("-");
		ln("Sr1= " + parts[1]);
		int sr01 = Integer.parseInt(parts[1]);
		selenium.click("css=#advSearchResults > table > thead > tr > th:nth-child(1) > img");
		String sr2 = selenium.getText("css=#advSearchResults > table > tbody > tr:nth-child(1) > td:nth-child(1) > a");
		parts = sr2.split("-");
		int sr02 = Integer.parseInt(parts[1]);
		ln("Sr2= "+sr02);
		int test= sr01-sr02; 
		if(test >0 ) {
						ln("SUCCESSFUL SORT IS OPERATIONAL.");
		}
		else{ 
			ln("FAIL!!! SORT DID NOT WORK");
	        throw new RuntimeException();
			} 
		ln("FeildSort= Done");	
		}catch (Exception e){
          System.out.println(e);
          selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/FeildSort.png");
          SendEmail.send("angel.martin@miamidade.gov", "test", "Feild Sort"+e.getMessage());
          Assert.fail();
        }}
    @Test
 	public void ViewReport() throws Exception {
 		try{
 		login();
 		selenium.click("link=Basic Search");
		selenium.click("name=ServiceRequestType");
		selenium.type("name=ServiceRequestType", "BULKY TRASH REQUEST - MD");
		selenium.click("id=createdStartDate");
		selenium.type("id=createdStartDate", "-1");
		selenium.click("id=showMoreFieldsId");
		selenium.select("css=#advsearch_moreDetails > div:nth-child(3) > select", "Locked");
		selenium.click("css=#advSearch_right > input[name=\"search\"]");
		Thread.sleep(1800);
		selenium.click("css=#advSearchResults > table > tbody > tr:nth-child(1) > td:nth-child(1) > a");
		selenium.click("css=body > div:nth-child(11) > div.ui-dialog-buttonpane.ui-widget-content.ui-helper-clearfix > div > button:nth-child(2) > span");
		Thread.sleep(2000);
//		ln(selenium.getAllWindowNames());
		String[] names = selenium.getAllWindowNames();
		String windowName = null;
//		try{
//		assumes only one main window and one pop over
			windowName = names[1];
			selenium.selectWindow(names[1]);
			(windowName).equalsIgnoreCase("frompop_1461961429332");
		} catch (Exception e){
			System.out.println(e);
			selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/ViewReport.png");
			SendEmail.send("angel.martin@miamidade.gov", "test", "View Report"+e.getMessage());
			Assert.fail();
		}}
//		StringBuilder builder = new StringBuilder();
//		for(String s : name) {
//		    builder.append(s);
//		}
//		return builder.toString(name1);
//		
//		
//		selenium.selectPopUp(name);
//		selenium.getLocation();
//		ln(selenium.getLocation());
 		
//		}catch (Exception e){
//            System.out.println(e);
//            selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/ViewReport.png");
//            SendEmail.send("angel.martin@miamidade.gov", "test", "View Report"+e.getMessage());
//            Assert.fail();
//        }}
 	@Test
 	public void Duplicate() throws Exception {
// 		try{
 		login();
 		selenium.click("css=body > div.container_12 > div.banner.grid_12 > ul > li:nth-child(6) > a");
 		selenium.type("css=#srTypeListAdvSearch > input", "BULKY TRASH REQUEST - MD");
 		selenium.type("id=createdStartDate", "-2");
 		selenium.click("css=#advSearch_right > input:nth-child(1)");
 		Thread.sleep(4500);
 		String Address = selenium.getText("css=#advSearchResults > table > tbody > tr:nth-child(1) > td:nth-child(3)");
 		selenium.click("css=#advSearchResults > table > tbody > tr:nth-child(1) > td:nth-child(1) > a");
 		selenium.click("css=body > div:nth-child(11) > div.ui-dialog-buttonpane.ui-widget-content.ui-helper-clearfix > div > button:nth-child(1) > span");
 		Thread.sleep(1000);
 		selenium.click("css=#sr_details_left > ul > div > li:nth-child(1) > a");
 		Thread.sleep(500);
 		ln(Address);
 		selenium.click("css=#sr_details > div:nth-child(2) > input");
 		Thread.sleep(500);
 		selenium.click("css=body > div:nth-child(12) > div.ui-dialog-buttonpane.ui-widget-content.ui-helper-clearfix > div > button:nth-child(1) > span");
 		selenium.type("id=srTypeID", "BULKY TRASH REQUEST - MD");
 		selenium.click("css=#srTypeList > span > input.submit.h23_submit.button.blue");
 		selenium.click("css=body > div:nth-child(13) > div.ui-dialog-buttonpane.ui-widget-content.ui-helper-clearfix > div > button:nth-child(1) > span");
 		selenium.type("css=#sr_details > div:nth-child(7) > span > input.ic_field.h24.error", Address);
 		selenium.click("css=#sr_details > div:nth-child(7) > span > input.submit.h32.button.blue");
 		Thread.sleep(6000);
		assertTrue(selenium.isVisible("css=#sr_details_right > input.red_button.button.blue"));
 		}
// 	catch (Exception e){
//            System.out.println(e);
//            selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/Duplicate.png");
//            SendEmail.send("angel.martin@miamidade.gov", "test", "Duplicate"+e.getMessage());
//            Assert.fail();
//        }}
 	@Test
 	public void OutofServiceArea() throws Exception {
 		try{
 		login();
 		selenium.click("link=Service Hub");
		selenium.click("id=srTypeID");
		selenium.type("id=srTypeID", "BOAT STORAGE - MD");
		selenium.click("css=#srTypeList > span > input.submit.h23_submit.button.blue");
		selenium.type("css=#sr_details > div.grid_5.alpha > span.input_clear > input.ic_field.h24", "1022 adams drive");
		selenium.click("xpath=(//input[@value='Search'])[6]");
		Thread.sleep(7000);
		selenium.isTextPresent("Outside Service Area.");
 		}catch (Exception e){
            System.out.println(e);
            selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/OutofServiceArea.png");
            SendEmail.send("angel.martin@miamidade.gov", "test", "Out of Service Area"+e.getMessage());
            Assert.fail();
        }} 	
 	@Test
 	public void apporvalProcess() throws Exception {
 		try{
 		selenium.open(site);
 		selenium.type("id=iUsername", "c203036");
		selenium.type("id=iPassword", pass);
		selenium.click("id=btnLogin");
		Thread.sleep(8000);
 		selenium.click("link=Basic Search");
 		Thread.sleep(3000);
 		selenium.click("id=showMoreFieldsId");
 		selenium.click("css=#advsearch_moreDetails > div.grid_2 > select.f_left");
		selenium.select("css=#advsearch_moreDetails > div.grid_2 > select.f_left", "label=Pending");
		selenium.click("css=#advSearch_right > input:nth-child(1)");
		Thread.sleep(5500);
		selenium.click("css=#advSearchResults > table > tbody > tr:nth-child(3) > td:nth-child(1) > a");
		selenium.click("css=body > div:nth-child(11) > div.ui-dialog-buttonpane.ui-widget-content.ui-helper-clearfix > div > button:nth-child(1) > span");
		Thread.sleep(5000);
		selenium.isTextPresent("The following Service Request has been identified as a self-service request and is 'Pending Approval'. Please review the request and make appropriate changes. When complete, set the status to 'Open' then save.");
 		}catch (Exception e){
            System.out.println(e);
            selenium.captureScreenshot("C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/ApprovalProcess.png");
            SendEmail.send("angel.martin@miamidade.gov", "test", "Apporval Process <br>file:///C://Users/angel.martin.MIAMIDADE/Desktop/failedtest/ApprovalProcess.png"+e.getMessage());
            Assert.fail();
        }}
// 	@Test
	public void message() throws Exception {
		SendEmail.send("angel.martin@miamidade.gov","Hello my Friend", "it works jejejejejeje");
	}
	@After
	public void tearDown() throws Exception {
	 selenium.stop();
	 selenium.shutDownSeleniumServer();
	 ln("server successfully shut down.");
	
	}
}
