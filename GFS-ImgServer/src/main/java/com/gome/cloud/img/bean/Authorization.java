/**
 * 
 */
package com.gome.cloud.img.bean;

import java.util.Date;

/**
 * @author blaiu
 *
 */
public class Authorization {
	
	private String authCode;
	private String orgName;
	private String authStatus;
	private Date createDatetime;
	
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getAuthStatus() {
		return authStatus;
	}
	public void setAuthStatus(String authStatus) {
		this.authStatus = authStatus;
	}
	public Date getCreateDatetime() {
		return createDatetime;
	}
	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}
	
	
	
}
