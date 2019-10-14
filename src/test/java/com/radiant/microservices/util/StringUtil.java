/**
 * @author Jp
 *
 */
package com.radiant.microservices.util;

public class StringUtil {

  //==========================================================================

  public static boolean isNull(String sourceString) {
    if (sourceString == null || sourceString.trim().equals("")) {
      return true;
    } else {
      return false;
    }

  }

  //==========================================================================
  
  public static boolean isNotNull(String sourceString) {
    if (sourceString != null && (!"".equals(sourceString.trim()))) {
      return true;
    } else {
      return false;
    }
  }

  //==========================================================================
  
  public static String trim(String param) {
    String returnParam = param;
    if (null != param) {
      returnParam = param.trim();
    }
    return returnParam;
  }
  
  //==========================================================================
  
  public static String getSubString(String s, int count) {
    if(isNotNull(s)) {
      if(s.length() > count) {
        s = s.substring(0, count);
      }
    }
    return s;
  }
  //==========================================================================
}