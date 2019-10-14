/**
 * @author Chandu
 */
package com.radiant.microservices.testscripts;

import java.util.List;

import junit.framework.Assert;

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
import com.radiant.microservices.model.WebElementDetails;
import com.radiant.microservices.pageobjects.Login;
import com.radiant.microservices.util.AppUtil;
import com.radiant.microservices.util.JWebDriver;
import com.radiant.microservices.util.JXMLParser;

@SuppressWarnings("deprecation")
public class LogoutTestScript {

	protected transient final Log log = LogFactory.getLog(getClass());
	private List<WebElementDetails> webElementsList = null;
	private TestCaseDetails testCaseDetails = null;
	AppUtil apt = new AppUtil();
	TestSuiteDetails suiteDetails;

	// ==========================================================================

	public LogoutTestScript(TestSuiteDetails testSuiteDetails) {

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
		testCaseDetails.setTestCaseName(LogoutTestScript.class.getSimpleName());
		if (webElementsList == null) {
			webElementsList = JXMLParser.getInstance().getWebElements(Login.class.getSimpleName());
		}
	}

	// ==========================================================================

	@Test(description = "Logout from the TNC Application")
	public void logout() {
		log.info("START of the method logout");
		Login login = new Login();
		String customMessage = null;
		WebDriver driver = null;

		try {
			testCaseDetails.setMethodName(AppUtil.getMethodName());
			setPrerequisites();
			if (webElementsList != null && webElementsList.size() > 0) {
				driver = JWebDriver.getInstance().getWebDriver();
				Thread.sleep(1000);
				customMessage = "click on logout button";
				WebElementDetails logoutWebElementObj = webElementsList.get(4);
				login.logoutBtn(logoutWebElementObj).click();
				Thread.sleep(3000);
				customMessage = "Check logout is successfull or not";
				Assert.assertTrue(driver.getCurrentUrl().contains("login"));
				driver.close();
				testCaseDetails.setStatus(Constants.PASS);
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
		log.info("END of the method logout");
	}

	// ==========================================================================

	@AfterTest
	public void afterTest() {
		log.info("START of the method afterTest");
		log.info("END of the method afterTest");
	}

	// ==========================================================================

}
