
package com.stta.SuiteOne;

import java.io.IOException;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.stta.utility.Read_XLS;
import com.stta.utility.SuiteUtility;

//SuiteOneCaseTwo Class Inherits From SuiteOneBase Class.
//So, SuiteOneCaseTwo Class Is Child Class Of SuiteOneBase Class And SuiteBase Class.
public class SuiteOneCaseTwo extends SuiteOneBase{
	Read_XLS FilePath = null;	
	String SheetName = null;
	String TestCaseName = null;	
	String ToRunColumnNameTestCase = null;
	String ToRunColumnNameTestData = null;
    String[] TestDataToRun = null;
    static boolean TestCasePass=true;
    
	static int DataSet = -1;	
	static boolean Testskip=false;
	static boolean Testfail = false;
	SoftAssert s_assert =null;
	
	@BeforeTest
	public void checkCaseToRun() throws IOException {
		// Called init() function from SuiteBase class to Initialize .xls Files
		init();
		// To set SuiteOne.xls file's path In FilePath Variable.
		FilePath = TestCaseListExcelOne;
		System.out.println("FilePath Is : " + FilePath);
		TestCaseName = this.getClass().getSimpleName();
		System.out.println("TestCaseName Is : " + TestCaseName);

		// SheetName to check CaseToRun flag against test case.
		SheetName = "TestCasesList";
		// Name of column In Excel sheet.
		ToRunColumnNameTestCase = "CaseToRun";
		
		//Name of column In Test Case Data sheets.
			ToRunColumnNameTestData = "DataToRun";

		// To check test case's CaseToRun = Y or N In related excel sheet.
		// If CaseToRun = N or blank, Test case will skip execution. Else It will be
		// executed.
		if (!SuiteUtility.checkToRunUtility(FilePath, SheetName, ToRunColumnNameTestCase, TestCaseName)) {
			throw new SkipException(
					TestCaseName + "'s CaseToRun Flag Is 'N' Or Blank. So Skipping Execution Of " + TestCaseName);
		}
		//To retrieve DataToRun flags of all data set lines from related test data sheet.
				TestDataToRun = SuiteUtility.checkToRunUtilityOfData(FilePath, TestCaseName, ToRunColumnNameTestData);

	}
	
	//Accepts 4 column's String data In every Iteration.
	@Test(dataProvider="SuiteOneCaseTwoData")
	public void SuiteOneCaseTwoTest(String DataCol1,String DataCol2,String DataCol3,String ExpectedResult){
		
		DataSet++;
		//If found DataToRun = "N" for data set then execution will be skipped for that data set.
		
		//Created object of testng SoftAssert class.
				s_assert = new SoftAssert();	
				
		//If found DataToRun = "N" for data set then execution will be skipped for that data set.		
		if(!TestDataToRun[DataSet].equalsIgnoreCase("Y")){	
			Testskip=true;
			throw new SkipException("DataToRun for row number "+DataSet+" Is No Or Blank. So Skipping Its Execution.");
		}
		
		System.out.println("Value Of DataCol1 = "+DataCol1);
		System.out.println("Value Of DataCol2 = "+DataCol2);
		System.out.println("Value Of DataCol3 = "+DataCol3);
		System.out.println("Value Of ExpectedResult = "+ExpectedResult);
		
		//If found DataToRun = "Y" for data set then bellow given lines will be executed.
		//To Convert data from String to Integer
		int ValueOne = Integer.parseInt(DataCol1);
		int ValueTwo = Integer.parseInt(DataCol2);
		int ValueThree = Integer.parseInt(DataCol3);
		int ExpectedResultInt =  Integer.parseInt(ExpectedResult);
		
		//Subtract the values.
		int ActualResult = ValueOne-ValueTwo-ValueThree;
		
		//Compare actual and expected values.
		if(!(ActualResult==ExpectedResultInt)){
			//If expected and actual results not match, Set flag Testfail=true.
			Testfail=true;
			//If result Is fail then test failure will be captured Inside s_assert object reference.
			//This soft assertion will not stop your test execution.
			s_assert.assertEquals(ActualResult, ExpectedResultInt, ActualResult+" And "+ExpectedResultInt+" Not Match");
			
			//To Initialize Firefox browser.
			loadWebBrowser();
			//To navigate to URL.
			driver.get(Param.getProperty("siteURL"));
			
			if(Testfail){
				//At last, test data assertion failure will be reported In testNG reports and It will mark your test data, test case and test suite as fail.
				s_assert.assertAll();		
			}
			
				
	}	
	}
	
	//@AfterMethod method will be executed after execution of @Test method every time.
	@AfterMethod
	public void reporterDataResults(){		
		if(Testskip)
			//If found Testskip = true, Result will be reported as SKIP against data set line In excel sheet.
			SuiteUtility.WriteResultUtility(FilePath, TestCaseName, "Pass/Fail/Skip", DataSet+1, "SKIP");
		else if(Testfail){
			//To make object reference null after reporting In report.
			s_assert = null;
			//Set TestCasePass = false to report test case as fail In excel sheet.
			TestCasePass=false;
			//If found Testfail = true, Result will be reported as FAIL against data set line In excel sheet.
			SuiteUtility.WriteResultUtility(FilePath, TestCaseName, "Pass/Fail/Skip", DataSet+1, "FAIL");			
		}else{
			//If found Testskip = false and Testfail = false, Result will be reported as PASS against data set line In excel sheet.
			SuiteUtility.WriteResultUtility(FilePath, TestCaseName, "Pass/Fail/Skip", DataSet+1, "PASS");
		}
		//At last make both flags as false for next data set.
		Testskip=false;
		Testfail=false;
	}

	
	
	
	
	
	//This data provider method will return 4 column's data one by one In every Iteration.
	@DataProvider
	public Object[][] SuiteOneCaseTwoData(){
		//To retrieve data from Data 1 Column,Data 2 Column,Data 3 Column and Expected Result column of SuiteOneCaseTwo data Sheet.
		//Last two columns (DataToRun and Pass/Fail/Skip) are Ignored programatically when reading test data.
		return SuiteUtility.GetTestDataUtility(FilePath, TestCaseName);
	}
	
	//To report result as pass or fail for test cases In TestCasesList sheet.
		@AfterTest
		public void closeBrowser(){
			// to close browser
			closeWebBrowser();
			if(TestCasePass){
				SuiteUtility.WriteResultUtility(FilePath, SheetName, "Pass/Fail/Skip", TestCaseName, "PASS");
			}
			else{
				SuiteUtility.WriteResultUtility(FilePath, SheetName, "Pass/Fail/Skip", TestCaseName, "FAIL");
				
			}
		}
	
	
	
}
