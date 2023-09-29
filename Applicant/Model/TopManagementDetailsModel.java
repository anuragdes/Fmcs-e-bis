package Applicant.Model;

public class TopManagementDetailsModel {
	private int numIsValid;
	private String str_name;
	private String str_desi;
	private String str_phone;
	private String str_email;
	private String str_din;
	private String str_cml_no;
	private String str_app_id;
	private String str_branch_id;
	private int user_id;
	 
	String  csrfTok;
	
	public int getuser_id() {
		return user_id;
	}
	public void setuser_id(int user_id) {
		this.user_id = user_id;
	}
	
	public int getNumIsValid() {
		return numIsValid;
	}
	public void setNumIsValid(int numIsValid) {
		this.numIsValid = numIsValid;
	}
	
	public String getstr_name() {
		return str_name;
	}
	public void setStr_name(String str_name) {
		this.str_name = str_name;
	}
	public String getStr_desi() {
		return str_desi;
	}
	public void setStr_desi(String str_desi) {
		this.str_desi = str_desi;
	}
	public String getStr_phone() {
		return str_phone;
	}
	public void setStr_phone(String str_phone) {
		this.str_phone = str_phone;
	}
	public String getStr_email() {
		return str_email;
	}
	public void setStr_email(String str_email) {
		this.str_email = str_email;
	}

	public String getStr_din() {
		return str_din;
	}
	public void setStr_din(String str_din) {
		this.str_din = str_din;
	}

	public String getstr_cml_no() {
		return str_cml_no;
	}
	public void setstr_cml_no(String str_cml_no) {
		this.str_cml_no = str_cml_no;
	}

	public String getstr_app_id() {
		return str_app_id;
	}
	
	public void setstr_app_id(String str_app_id) {
		this.str_app_id = str_app_id;
	}
	
	public String getstr_branch_id() {
		return str_branch_id;
	}
	
	public void setstr_branch_id(String str_branch_id) {
		this.str_branch_id = str_branch_id;
	}

	
	public String getCsrfTok() {
		return csrfTok;
	}
	public void setCsrfTok(String csrfTok) {
		this.csrfTok = csrfTok;
	}
	
}
