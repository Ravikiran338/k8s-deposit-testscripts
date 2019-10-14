/**
 * @author Chandu
 */
package com.radiant.microservices.testscripts;

import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.radiant.microservices.common.Constants;
import com.radiant.microservices.db.TAFDBManagerHelper;
import com.radiant.microservices.db.TestCaseDetails;
import com.radiant.microservices.db.TestSuiteDetails;
import com.radiant.microservices.exceptions.TAFException;
import com.radiant.microservices.model.WebElementDataDetails;
import com.radiant.microservices.model.WebElementDetails;
import com.radiant.microservices.pageobjects.Login;
import com.radiant.microservices.util.AppUtil;
import com.radiant.microservices.util.JExcelParser;
import com.radiant.microservices.util.JWebDriver;
import com.radiant.microservices.util.JXMLParser;

import junit.framework.Assert;

public class LoginTestScript {
	protected transient final Log log = LogFactory.getLog(getClass());
	private List<WebElementDetails> webElementsList = null;
	private List<WebElementDataDetails> webElementsData = null; 
	private TestCaseDetails testCaseDetails = null;
	AppUtil apt = new AppUtil();
	TestSuiteDetails suiteDetails;

	// ==========================================================================
	
	public LoginTestScript(TestSuiteDetails testSuiteDetails) {

		this.suiteDetails = testSuiteDetails;
		testCaseDetails = new TestCaseDetails();
		testCaseDetails.setTestSuiteDetailsId(testSuiteDetails.getTestSuiteDetailsId());
	}

	// ==========================================================================

	@BeforeTest
	public void beforeTest() {
		log.info("START of the method beforeTest");
		log.info("END of the method beforeTest");
	}

	// ==========================================================================

	private void setPrerequisites() throws InterruptedException {
		testCaseDetails.setTestCaseName(LoginTestScript.class.getSimpleName());
		if (webElementsList == null) {
			webElementsList = JXMLParser.getInstance().getWebElements(Login.class.getSimpleName());
		}
		if (webElementsData == null) {
	      webElementsData = JExcelParser.getInstance().getDataSet(Login.class.getSimpleName(), Login.class.getSimpleName());
	   }
	}
	
	

	// ==========================================================================

	@Test(description = "Login to the TNC Applilcation")
	public void login() throws InterruptedException {
		log.info("START of the method login");
		Login login = new Login();
		String customMessage = null;
		WebDriver driver = null;
		try {
			testCaseDetails.setMethodName(AppUtil.getMethodName());
			setPrerequisites();
			
			driver = JWebDriver.getInstance().getWebDriver();
			ResourceBundle resourceBundle = ResourceBundle.getBundle("ApplicationResources");
			System.out.println(resourceBundle.getString("application.url"));
			driver.get(resourceBundle.getString("application.url"));
			
			driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
			WebElementDetails loginMenuWebElementObj = webElementsList.get(3);
			login.userNameTxtBox(loginMenuWebElementObj).click();
			Thread.sleep(3000);
			
			if (webElementsList != null && webElementsList.size() > 0) {
				if (webElementsData!=null) {
					for (WebElementDataDetails webElementDataDetails : webElementsData) {
						if (webElementDataDetails.isExecute()) {
							
							customMessage = "Enter UserName";
							WebElementDetails userNameWebElementObj = webElementsList.get(0);
							login.userNameTxtBox(userNameWebElementObj).click();
							login.userNameTxtBox(userNameWebElementObj).sendKeys(webElementDataDetails.getDataSet().get(0));
							Thread.sleep(1000);
							
							customMessage = "Enter password";
							WebElementDetails passwordWebElementObj = webElementsList.get(1);
							login.userNameTxtBox(passwordWebElementObj).click();
							login.userNameTxtBox(passwordWebElementObj).sendKeys(webElementDataDetails.getDataSet().get(1));
							Thread.sleep(1000);
							
							customMessage = "Click on Login Btn";
							WebElementDetails LoginBtnWebElementObj = webElementsList.get(2);
							login.userNameTxtBox(LoginBtnWebElementObj).click();
							testCaseDetails.setStatus(Constants.PASS);
							Thread.sleep(3000);
							
							
						}
					}
				} else {
					log.info(" Unable to execute the script Test data is empty");
				}
			//Assert.assertTrue(false);	
			} else {
				log.info(" Unable to execute the script as some or all the mandatory objects or values are null");
			}
		} catch (java.lang.AssertionError e) {
			suiteDetails.setTestStatusSuccess(false);
			testCaseDetails = new TAFException().handleException(e, testCaseDetails, customMessage);
		} catch (Exception e) {
			suiteDetails.setTestStatusSuccess(false);
			testCaseDetails = new TAFException().handleException(e, testCaseDetails, customMessage);
		} finally {
			TAFDBManagerHelper.getInstance().saveTestCaseDetails(testCaseDetails);
		}
		log.info("END of the method login");
	}

	// ==========================================================================

	@AfterTest
	public void afterTest() {
		log.info("START of the method afterTest");
		log.info("END of the method afterTest");
	}

	// ==========================================================================
}