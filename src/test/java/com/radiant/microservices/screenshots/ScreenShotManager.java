/**
 * @author Jp
 *
 */
package com.radiant.microservices.screenshots;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.radiant.microservices.util.AppUtil;
import com.radiant.microservices.util.JWebDriver;

import static com.radiant.microservices.common.Constants.SCREEN_SHOTS_REPOSITORY;

public class ScreenShotManager {

  private static ScreenShotManager manager = null;
  
  //==========================================================================
  
  public static synchronized ScreenShotManager getInstance() {
    if(manager == null) {
      manager = new ScreenShotManager();
    }
    return manager;
  }
  
  //==========================================================================
  
  private ScreenShotManager() { }

  //==========================================================================
  
  public String saveScreenShot(String className, String type) {
    File scrFile = null;
    String screenShotLocation = null;
    String screenShotName = null;  //Server Configuration (DevOps Statging server)
    
    try {
      scrFile = ((TakesScreenshot) JWebDriver.getInstance().getWebDriver()).getScreenshotAs(OutputType.FILE);
      
      //Local Machine Configuration
      //screenShotLocation = AppUtil.getScreenShotsRepositoryFilePath(className, type);
      
      //Server Configuration (DevOps Statging server)
      screenShotName = AppUtil.getScreenShotName(className, type);
      screenShotLocation = SCREEN_SHOTS_REPOSITORY .concat(screenShotName);
      
      FileUtils.copyFile(scrFile, new File(screenShotLocation));
    } catch (IOException ioe) {  // need to integrate Exception handling
      ioe.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    //return screenShotLocation; // For Local
    return screenShotName; // For Server configuration
  }
  //==========================================================================
}
