package Applicant.Model;

public class RawMaterialModel {
	   	private int numIsValid;
		private String str_RawMaterila;
		private String str_Supplier;
		private String str_Conformity;
		private String str_NaturePackage;
		private String str_Records;
		private String str_Others;
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
		
		public String getStr_Others() {
			return str_Others;
		}
		public void setStr_Others(String str_Others) {
			this.str_Others = str_Others;
		}
		public String getStr_cml_no() {
			return str_cml_no;
		}
		public void setStr_cml_no(String str_cml_no) {
			this.str_cml_no = str_cml_no;
		}
		public String getStr_app_id() {
			return str_app_id;
		}
		public void setStr_app_id(String str_app_id) {
			this.str_app_id = str_app_id;
		}
		public String getStr_branch_id() {
			return str_branch_id;
		}
		public void setStr_branch_id(String str_branch_id) {
			this.str_branch_id = str_branch_id;
		}
		public int getUser_id() {
			return user_id;
		}
		public void setUser_id(int user_id) {
			this.user_id = user_id;
		}
		public String getStr_RawMaterila() {
			return str_RawMaterila;
		}
		public void setStr_RawMaterila(String str_RawMaterila) {
			this.str_RawMaterila = str_RawMaterila;
		}
		public String getStr_Supplier() {
			return str_Supplier;
		}
		public void setStr_Supplier(String str_Supplier) {
			this.str_Supplier = str_Supplier;
		}
		public String getStr_Conformity() {
			return str_Conformity;
		}
		public void setStr_Conformity(String str_Conformity) {
			this.str_Conformity = str_Conformity;
		}
		public String getStr_NaturePackage() {
			return str_NaturePackage;
		}
		public void setStr_NaturePackage(String str_NaturePackage) {
			this.str_NaturePackage = str_NaturePackage;
		}

		public String getStr_Records() {
			return str_Records;
		}
		public void setStr_Records(String str_Records) {
			this.str_Records = str_Records;
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
		
		@Override
		public String toString() {
			return "RawMaterialModel [numIsValid=" + numIsValid + ", str_RawMaterila=" + str_RawMaterila
					+ ", str_Supplier=" + str_Supplier + ", str_Conformity=" + str_Conformity + ", str_NaturePackage="
					+ str_NaturePackage + ", str_Records=" + str_Records + ", str_Others=" + str_Others
					+ ", str_cml_no=" + str_cml_no + ", str_app_id=" + str_app_id + ", str_branch_id=" + str_branch_id
					+ ", user_id=" + user_id + ", csrfTok=" + csrfTok + "]";
		}
}
