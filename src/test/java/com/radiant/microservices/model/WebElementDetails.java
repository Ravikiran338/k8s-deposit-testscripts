/**
 * @author Jp
 *
 */
package com.radiant.microservices.model;

import java.io.Serializable;
import java.util.List;

public class WebElementDetails implements Serializable {
  private static final long serialVersionUID = 2283468388193587092L;
  private String category;
  private int no;
  private String type;
  private String id;
  private String name;
  private String className;
  private String cssSelector;
  private String xPath;
  private String linkText;
  private String data;
  private List<WebElementDetails> parallelSubElements; // This property is only for the purpose of holding the list of parallel sub elements.
  private WebElementDetails innerSubWebElementDetails;// This property is only for the purpose of holding the inner sub element.

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
   this.category = category;
  }

  public int getNo() {
    return no;
  }

  public void setNo(int no) {
    this.no = no;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getCssSelector() {
    return cssSelector;
  }

  public void setCssSelector(String cssSelector) {
    this.cssSelector = cssSelector;
  }

  public String getxPath() {
    return xPath;
  }

  public void setxPath(String xPath) {
    this.xPath = xPath;
  }

  
  public String getLinkText() {
    return linkText;
  }

  public void setLinkText(String linkText) {
    this.linkText = linkText;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public List<WebElementDetails> getParallelSubElements() {
    return parallelSubElements;
  }
  
  public void setParallelSubElements(List<WebElementDetails> parallelSubElements) {
    this.parallelSubElements = parallelSubElements;
  }
  
  public WebElementDetails getInnerSubWebElementDetails() {
    return innerSubWebElementDetails;
  }
  
  public void setInnerSubWebElementDetails(WebElementDetails innerSubWebElementDetails) {
    this.innerSubWebElementDetails = innerSubWebElementDetails;
  }
}