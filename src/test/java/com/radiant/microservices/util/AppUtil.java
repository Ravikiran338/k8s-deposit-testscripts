/**
 * @author Jp
 *
 */
package com.radiant.microservices.util;

import static com.radiant.microservices.common.Constants.*;

import java.io.File;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

public class AppUtil {

	// ==========================================================================

	public static String getXMLFilePath(String xmlFileName) {
		StringBuffer xmlFilePath = new StringBuffer("");
		xmlFilePath.append(System.getProperty("user.dir"));
		xmlFilePath.append(XML_REPOSITORY);
		xmlFilePath.append(xmlFileName);
		xmlFilePath.append(XML);
		return xmlFilePath.toString();
	}

	// ==========================================================================

	public static String getExcelFilePath(String excelFileName) {
		StringBuffer excelFilePath = new StringBuffer("");
		excelFilePath.append(System.getProperty("user.dir"));
		excelFilePath.append(EXCEL_REPOSITORY);
		excelFilePath.append(excelFileName);
		excelFilePath.append(EXCEL);
		return excelFilePath.toString();
	}

	// ==========================================================================

	public static String getScreenShotsRepositoryFilePath(String className, String type) {
		StringBuffer screenShotsRepositoryFilePath = new StringBuffer("");
		// Comment the below line for Server configuration
		// screenShotsRepositoryFilePath.append(System.getProperty("user.dir"));
		screenShotsRepositoryFilePath.append(SCREEN_SHOTS_REPOSITORY);
		screenShotsRepositoryFilePath.append(className);
		screenShotsRepositoryFilePath.append(UNDER_SCORE);
		screenShotsRepositoryFilePath.append(type);
		screenShotsRepositoryFilePath.append(UNDER_SCORE);
		screenShotsRepositoryFilePath.append(System.currentTimeMillis());
		screenShotsRepositoryFilePath.append(PNG);
		return screenShotsRepositoryFilePath.toString();
	}

	// ==========================================================================

	// Server Configuration (DevOps Statging server)
	public static String getScreenShotName(String className, String type) {
		StringBuffer screenShotsRepositoryFilePath = new StringBuffer("");
		screenShotsRepositoryFilePath.append(className);
		screenShotsRepositoryFilePath.append(UNDER_SCORE);
		screenShotsRepositoryFilePath.append(type);
		screenShotsRepositoryFilePath.append(UNDER_SCORE);
		screenShotsRepositoryFilePath.append(System.currentTimeMillis());
		screenShotsRepositoryFilePath.append(PNG);
		return screenShotsRepositoryFilePath.toString();
	}

	// ==========================================================================

	public static String getMethodName() {
		return Thread.currentThread().getStackTrace()[2].getMethodName();
	}

	// ===========================================================================

	public static boolean isErrorMessageExist(List<WebElement> errorMessagesList, String messageToCheck) {
		boolean isErrorMessageExist = false;

		if (errorMessagesList != null && errorMessagesList.size() > 0) {
			for (WebElement webElement : errorMessagesList) {
				if (messageToCheck.equalsIgnoreCase(webElement.getText())) {
					isErrorMessageExist = true;
					break;
				}
			}
		}
		return isErrorMessageExist;
	}

	// ===========================================================================

	public static boolean isManadatoryFieldExist(List<WebElement> errorMessagesList) {
		boolean isMandatoryFieldExist = false;

		if (errorMessagesList != null && errorMessagesList.size() > 0) {
			for (WebElement webElement : errorMessagesList) {
				if (webElement.getText().contains("required")) {
					isMandatoryFieldExist = true;
					break;
				}
			}
		}
		return isMandatoryFieldExist;
	}

	// ===========================================================================
	
	public static void safeJavaScriptClick(WebElement element, WebDriver driver) throws Exception {
        try {
            if (element.isEnabled() && element.isDisplayed()) {
                System.out.println("Clicking on element with using java script click");
                ((JavascriptExecutor)  driver).executeScript("arguments[0].click();", element);
            } else {
                System.out.println("Unable to click on element");
            }
        } catch (StaleElementReferenceException e) {
            System.out.println("Element is not attached to the page document " + e.getStackTrace());
        } catch (NoSuchElementException e) {
            System.out.println("Element was not found in DOM " + e.getStackTrace());
        } catch (Exception e) {
            System.out.println("Unable to click on element " + e.getStackTrace());
        }
    }
	
	// ===========================================================================
	
	public static void safeJavaScriptSendKeys(WebElement element, WebDriver driver, String attribStr) throws Exception {
        try {
            if (element.isEnabled() && element.isDisplayed()) {
                System.out.println("Clicking on element with using java script click");
                ((JavascriptExecutor)  driver).executeScript("arguments[0].click();", element);
                ((JavascriptExecutor)  driver).executeScript("arguments[0].value=arguments[1]",element,attribStr);
                
            } else {
                System.out.println("Unable to click on element");
            }
        } catch (StaleElementReferenceException e) {
            System.out.println("Element is not attached to the page document " + e.getStackTrace());
        } catch (NoSuchElementException e) {
            System.out.println("Element was not found in DOM " + e.getStackTrace());
        } catch (Exception e) {
            System.out.println("Unable to click on element " + e.getStackTrace());
        }
    }


/*	public static void main(String[] args) throws InterruptedException {
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setJavascriptEnabled(true);                
		caps.setCapability("takesScreenshot", true);  
		caps.setCapability(
            PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
            System.getProperty("user.dir") + "\\Drivers\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe"
        );
		
	    WebDriver dr = new  PhantomJSDriver(caps); 
		
		dr.get("http://a3f8883b2daa511e98ab40af567036dc-1624512425.us-east-1.elb.amazonaws.com:8090/index.html");
		dr.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		dr.findElement(By.cssSelector(".navbar-default .navbar-nav > li > a")).click();
		
		//System.out.println(dr.getPageSource());
		Thread.sleep(10000);
		Dimension d = new Dimension(1124,850);
		//Resize the current window to the given dimension
		dr.manage().window().setSize(d);
		Thread.sleep(3000);
		
		
		Actions act = new Actions(dr);
		Thread.sleep(1000);
		
		act.moveToElement(dr.findElement(By.xpath(".//input[@name='username']"))).build().perform();
		act.click(dr.findElement(By.xpath(".//input[@name='username']"))).build().perform();
		act.sendKeys(dr.findElement(By.xpath(".//input[@name='username']")), "anilkumar").build().perform();
		Thread.sleep(1000);
		
		act.moveToElement(dr.findElement(By.xpath(".//input[@name='password']"))).build().perform();
		act.click(dr.findElement(By.xpath(".//input[@name='password']"))).build().perform();
		act.sendKeys(dr.findElement(By.xpath(".//input[@name='password']")), "123456789").build().perform();
		Thread.sleep(1000);
		
		act.moveToElement(dr.findElement(By.cssSelector(".btn.btn-primary"))).build().perform();
		act.click(dr.findElement(By.cssSelector(".btn.btn-primary"))).build().perform();
		Thread.sleep(3000);
				
		System.out.println(dr.getCurrentUrl());
        dr.quit();
        System.out.println("Came out of loop");
        
		
	}
*/
	}
