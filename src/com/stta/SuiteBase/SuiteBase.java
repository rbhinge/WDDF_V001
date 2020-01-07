//Find More Tutorials On WebDriver at -> http://software-testing-tutorials-automation.blogspot.com
package com.stta.SuiteBase;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import com.stta.utility.Read_XLS;

public class SuiteBase {	
	public static Read_XLS TestSuiteListExcel=null;
	public static Read_XLS TestCaseListExcelOne=null;
	public static Read_XLS TestCaseListExcelTwo=null;
	public boolean BrowseralreadyLoaded=false;
	public static Properties Param = null;
	public static Properties Object = null;
	public static WebDriver driver=null;
	public static WebDriver ExistingchromeBrowser;
	public static WebDriver ExistingmozillaBrowser;
	public static WebDriver ExistingIEBrowser;
	
	public void init() throws IOException{
	
		//Please change file's path strings bellow If you have stored them at location other than bellow.
		//Initializing Test Suite List(TestSuiteList.xls) File Path Using Constructor Of Read_XLS Utility Class.
		TestSuiteListExcel = new Read_XLS(System.getProperty("user.dir")+"\\src\\com\\stta\\ExcelFiles\\TestSuiteList.xls");
		//Initializing Test Suite One(SuiteOne.xls) File Path Using Constructor Of Read_XLS Utility Class.
		TestCaseListExcelOne = new Read_XLS(System.getProperty("user.dir")+"\\src\\com\\stta\\ExcelFiles\\SuiteOne.xls");
		//Initializing Test Suite Two(SuiteTwo.xls) File Path Using Constructor Of Read_XLS Utility Class.
		TestCaseListExcelTwo = new Read_XLS(System.getProperty("user.dir")+"\\src\\com\\stta\\ExcelFiles\\SuiteTwo.xls");
		//Bellow given syntax will Insert log In applog.log file.
		
		System.out.println("All Excel Files Initialised successfully.");
		
		//Create object of Java Properties class
		Param = new Properties();
		FileInputStream fip = new FileInputStream(System.getProperty("user.dir")+"//src//com//stta//property//Param.properties");
		Param.load(fip);
		System.out.println("Param.properties file loaded successfully.");
		
		// Initialize Objects.properties file.
		Object = new Properties();
		fip = new FileInputStream(System.getProperty("user.dir") + "//src//com//stta//property//Objects.properties");
		Object.load(fip);
		System.out.println("Objects.properties file loaded successfully.");
	}
	
	public void loadWebBrowser(){
		//Check If any previous webdriver browser Instance Is exist then run new test In that existing webdriver browser Instance.
			if(Param.getProperty("testBrowser").equalsIgnoreCase("Mozilla") && ExistingmozillaBrowser!=null){
				driver = ExistingmozillaBrowser;
				return;
			}else if(Param.getProperty("testBrowser").equalsIgnoreCase("chrome") && ExistingchromeBrowser!=null){
				driver = ExistingchromeBrowser;
				return;
			}else if(Param.getProperty("testBrowser").equalsIgnoreCase("IE") && ExistingIEBrowser!=null){
				driver = ExistingIEBrowser;
				return;
			}		
		
		
			if(Param.getProperty("testBrowser").equalsIgnoreCase("Mozilla")){
				//To Load Firefox driver Instance. 
				driver = new FirefoxDriver();
				ExistingmozillaBrowser=driver;
				System.out.println("Firefox Driver Instance loaded successfully.");
				
			}else if(Param.getProperty("testBrowser").equalsIgnoreCase("Chrome")){
				//To Load Chrome driver Instance.
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"//BrowserDrivers//chromedriver.exe");
				driver = new ChromeDriver();
				ExistingchromeBrowser=driver;
				System.out.println("Chrome Driver Instance loaded successfully.");
				
			}else if(Param.getProperty("testBrowser").equalsIgnoreCase("IE")){
				//To Load IE driver Instance.
				System.setProperty("webdriver.ie.driver", System.getProperty("user.dir")+"//BrowserDrivers//IEDriverServer.exe");
				driver = new InternetExplorerDriver();
				ExistingIEBrowser=driver;
				System.out.println("IE Driver Instance loaded successfully.");
				
			}			
			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
			driver.manage().window().maximize();			
	}
	
	public void closeWebBrowser(){
		driver.close();
		//null browser Instance when close.
		ExistingchromeBrowser=null;
		ExistingmozillaBrowser=null;
		ExistingIEBrowser=null;
	}
	
	//getElementByXPath function for static xpath
		public WebElement getElementByXPath(String Key){
			try{
				//This block will find element using Key value from web page and return It.
				return driver.findElement(By.xpath(Object.getProperty(Key)));
			}catch(Throwable t){
				//If element not found on page then It will return null.
				System.out.println("Object not found for key --"+Key);
				return null;
			}
		}
		
		//getElementByXPath function for dynamic xpath
		public WebElement getElementByXPath(String Key1, int val, String key2){
			try{
				//This block will find element using values of Key1, val and key2 from web page and return It.
				return driver.findElement(By.xpath(Object.getProperty(Key1)+val+Object.getProperty(key2)));
			}catch(Throwable t){
				//If element not found on page then It will return null.
				System.out.println("Object not found for custom xpath");
				return null;
			}
		}
		
		//Call this function to locate element by ID locator.
		public WebElement getElementByID(String Key){
			try{
				return driver.findElement(By.id(Object.getProperty(Key)));
			}catch(Throwable t){
			
              System.out.println("Object not found for key --"+Key);
				return null;
			}
		}
		
		//Call this function to locate element by Name Locator.
		public WebElement getElementByName(String Key){
			try{
				return driver.findElement(By.name(Object.getProperty(Key)));
			}catch(Throwable t){
				System.out.println("Object not found for key --"+Key);
				return null;
			}
		}
		
		//Call this function to locate element by cssSelector Locator.
		public WebElement getElementByCSS(String Key){
			try{
				return driver.findElement(By.cssSelector(Object.getProperty(Key)));
			}catch(Throwable t){
				
				System.out.println("Object not found for key" +Key);
				return null;
			}
		}
		
		//Call this function to locate element by ClassName Locator.
		public WebElement getElementByClass(String Key){
			try{
				return driver.findElement(By.className(Object.getProperty(Key)));
			}catch(Throwable t){
			
				System.out.println("Object not found for key"+Key);
				return null;
			}
		}
		
		//Call this function to locate element by tagName Locator.
		public WebElement getElementByTagName(String Key){
			try{
				return driver.findElement(By.tagName(Object.getProperty(Key)));
			}catch(Throwable t){
				System.out.println("Object not found for key "+Key);
				return null;
			}
		}
		
		//Call this function to locate element by link text Locator.
		public WebElement getElementBylinkText(String Key){
			try{
				return driver.findElement(By.linkText(Object.getProperty(Key)));
			}catch(Throwable t){
				
				System.out.println("Object not found for key "+Key);
				return null;
			}
		}
		
		//Call this function to locate element by partial link text Locator.
		public WebElement getElementBypLinkText(String Key){
			try{
				return driver.findElement(By.partialLinkText(Object.getProperty(Key)));
			}catch(Throwable t){
				
				System.out.println("Object not found for key --"+Key);
				return null;
			}
		}
	
	
}
