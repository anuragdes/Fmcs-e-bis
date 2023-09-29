package Applicant.Model;

public class applicantChngNameModel {
	
	String strOldFirmName;
	
	String strNewFirmName;
	
	String nameChangeProof;
	
	int id;
	
	int branchId;
	
	
	
	

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStrOldFirmName() {
		return strOldFirmName;
	}

	public void setStrOldFirmName(String strOldFirmName) {
		this.strOldFirmName = strOldFirmName;
	}

	public String getStrNewFirmName() {
		return strNewFirmName;
	}

	public void setStrNewFirmName(String strNewFirmName) {
		this.strNewFirmName = strNewFirmName;
	}

	public String getNameChangeProof() {
		return nameChangeProof;
	}

	public void setNameChangeProof(String nameChangeProof) {
		this.nameChangeProof = nameChangeProof;
	}
	
	
	

}
