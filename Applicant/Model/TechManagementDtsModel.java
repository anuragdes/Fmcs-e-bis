package Applicant.Model;

public class TechManagementDtsModel {
    private int numIsValid;
	private String str_name;
	private String str_desi;
	private String str_qul;
	private String str_exp;
	private String str_photo;
	private String str_cml_no;
	private String str_app_id;
	private String str_branch_id;
	private int user_id; 
	private String qualification;
	
	public String getQualification() {
		return qualification;
	}
	public void setQualification(String qualification) {
		this.qualification = qualification;
	}
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
	public String getStr_qul() {
		return str_qul;
	}
	public void setStr_Qul(String str_qul) {
		this.str_qul = str_qul;
	}
	public String getStr_exp() {
		return str_exp;
	}
	public void setStr_exp(String str_exp) {
		this.str_exp = str_exp;
	}

	public String getStr_photo() {
		return str_photo;
	}
	public void setStr_photo(String str_photo) {
		this.str_photo = str_photo;
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

