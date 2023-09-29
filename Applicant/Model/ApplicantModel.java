package Applicant.Model;

public class ApplicantModel {
	
	String strArrbrandNm[];
	
	int branchid;
	
	int userid;
	String csrf;
	
	
	
	public String getCsrf() {
		return csrf;
	}

	public void setCsrf(String csrf) {
		this.csrf = csrf;
	}

	public int getBranchid() {
		return branchid;
	}

	public void setBranchid(int branchid) {
		this.branchid = branchid;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String[] getStrArrbrandNm() {
		return strArrbrandNm;
	}

	public void setStrArrbrandNm(String[] strArrbrandNm) {
		this.strArrbrandNm = strArrbrandNm;
	}
	
	

}
