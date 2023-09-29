package exception.log.model;

import java.util.Date;

public class ExceptionLogModel {
	private Integer num_id;
	private String user_remarks="";
	private String exception_remarks="";
	private String exception_details="";
	private Date exception_date;
	private String is_valid="";
	private String exception_url="";
	private String username="";
	private String branchid="";
	private String rolename="";
	public Integer getNum_id() {
		return num_id;
	}
	public void setNum_id(Integer num_id) {
		this.num_id = num_id;
	}
	
	public String getUser_remarks() {
		return user_remarks;
	}
	public void setUser_remarks(String user_remarks) {
		this.user_remarks = user_remarks;
	}
	public String getException_remarks() {
		return exception_remarks;
	}
	public void setException_remarks(String exception_remarks) {
		this.exception_remarks = exception_remarks;
	}
	public String getException_details() {
		return exception_details;
	}
	public void setException_details(String exception_details) {
		this.exception_details = exception_details;
	}
	public Date getException_date() {
		return exception_date;
	}
	public void setException_date(Date exception_date) {
		this.exception_date = exception_date;
	}
	public String getIs_valid() {
		return is_valid;
	}
	public void setIs_valid(String is_valid) {
		this.is_valid = is_valid;
	}
	
	public String getException_url() {
		return exception_url;
	}
	public void setException_url(String exception_url) {
		this.exception_url = exception_url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getBranchid() {
		return branchid;
	}
	public void setBranchid(String branchid) {
		this.branchid = branchid;
	}
	public String getRolename() {
		return rolename;
	}
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
	@Override
	public String toString() {
		return "ExceptionLogModel [num_id=" + num_id + ", user_remarks=" + user_remarks + ", exception_remarks="
				+ exception_remarks + ", exception_details=" + exception_details + ", exception_date=" + exception_date
				+ ", is_valid=" + is_valid + ", exception_url=" + exception_url + ", username=" + username
				+ ", branchid=" + branchid + ", rolename=" + rolename + "]";
	}
}
