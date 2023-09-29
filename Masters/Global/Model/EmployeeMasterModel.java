package eBIS.Masters.Global.Model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class EmployeeMasterModel {
	private String url = "";
	private String csrftoken = "";
	private String strEmpFirstName;
	private String strEmpMiddleName;
	private String strEmpLastName;
	private String numDeptId1;
	private String numDeptNameId;
	private int numDesignId1;
	private String strEmail;
	private int numDisciplineId;
	private int strBISEmpId;
	private String stDob;
	private String strMobNo;
	private String str_pan;
	private int num_foreign_investigation;
	private String numRoleId;
	private int priorityRoleId;
	private int labtype;
	private int editflag;
	private int userid;
	private int num_id=0;
	
}
