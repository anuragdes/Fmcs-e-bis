package Global.Config.Model;

public class ConfigModel {
	String stEmail;
	String stUsername;
	int iEmailUserNameFlag; 
	String stCSRFToken;
	
	//added by shivani fro mcr report config
	String strFrmDate;
	String strToDate;
	//added by Mahendra for Lab ConsolidatedReport
	String labType;
	String labName;
	String branch;
	String branchName="";
	
	public String getStrFrmDate() {
		return strFrmDate;
	}
	public void setStrFrmDate(String strFrmDate) {
		this.strFrmDate = strFrmDate;
	}
	public String getStrToDate() {
		return strToDate;
	}
	public void setStrToDate(String strToDate) {
		this.strToDate = strToDate;
	}
	public String getStEmail() {
		return stEmail;
	}
	public void setStEmail(String stEmail) {
		this.stEmail = stEmail;
	}
	public String getStUsername() {
		return stUsername;
	}
	public void setStUsername(String stUsername) {
		this.stUsername = stUsername;
	}
	public int getiEmailUserNameFlag() {
		return iEmailUserNameFlag;
	}
	public void setiEmailUserNameFlag(int iEmailUserNameFlag) {
		this.iEmailUserNameFlag = iEmailUserNameFlag;
	}
	public String getStCSRFToken() {
		return stCSRFToken;
	}
	public void setStCSRFToken(String stCSRFToken) {
		this.stCSRFToken = stCSRFToken;
	}
	public String getLabType() {
		return labType;
	}
	public void setLabType(String labType) {
		this.labType = labType;
	}
	public String getLabName() {
		return labName;
	}
	public void setLabName(String labName) {
		this.labName = labName;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	
	
	
}
