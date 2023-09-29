package eBIS.Masters.Global.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Global.CommonUtility.DAO.GlobalDao;
import Global.CommonUtility.Domain.BisUserSalts;
import Global.CommonUtility.Service.GlobalMethodService;
import Global.CommonUtility.Service.IRandomPwdService;
import Global.CommonUtility.Service.IpasswordUtil;
import Global.Registration.Domain.RegisterDomain;
import Masters.Domain.Design_Mst_Domain;
import Masters.Domain.Discipline_Mst_Domain;
import Masters.Domain.Emp_Mst_Domain;
import Masters.Domain.Emp_Mst_Domain_log;
import Masters.Domain.LocationType_Mst_Domain;
import Masters.Domain.Role_Mst_Domain;
import Masters.Domain.UserRoleMasterDomain;
import Masters.Model.Emp_dept_Mod;
import eBIS.Masters.Global.DAO.EmployeeMasterDAO;
import eBIS.Masters.Global.Model.EmployeeMasterModel;

@Service
public class EmployeeMasterServiceImp implements EmployeeMasterService {
	static int startlpad=0;
	@Autowired
	EmployeeMasterDAO dao;
	@Autowired
	IRandomPwdService randompwd;
	@Autowired
	IpasswordUtil pwdEncode;
	@Autowired
	GlobalMethodService globalserv;
	@Autowired
	public GlobalDao globalDao;
	@Override
	public List<Discipline_Mst_Domain> getDisciplines() {
		return dao.getDisciplines();
	}

	@Override
	public List<Design_Mst_Domain> getDesign() {
		return dao.getDesign();
	}

	@Override
	public List<Role_Mst_Domain> getRoles() {
		return dao.getRoles();
	}

	@Override
	public List<LocationType_Mst_Domain> getdepart() {
		return dao.getdepart();
	}

	@Override
	public List<Map<String, Object>> getEmpDetail() throws IOException, InterruptedException, ExecutionException {
		List<Map<String, Object>> list = dao.getEmpDetail();
		for(int i=0;i<list.size();i++) {
			Map<String, Object> temp = list.get(i);
			String emp_id=temp.get("str_BIS_Emp_Id").toString();
			String OtherRoles = dao.getAllRoleName(emp_id);
			temp.put("OtherRoles", OtherRoles);
		}
		return list;
	}

	@Override
	public List<Emp_dept_Mod> getDepartmentName(int departmentTypeID, String departmentTypeName) {
		List<Map<String, Object>> list = dao.getDepartmentName(departmentTypeID,departmentTypeName);
		List<Emp_dept_Mod> departmentWiseList = new ArrayList<Emp_dept_Mod>();
		for(int i=0;i<list.size();i++) {
			Map<String, Object> tempRow = list.get(i);
			Emp_dept_Mod dmod = new Emp_dept_Mod();
		    String id=tempRow.get("id").toString();
		    String n=String.valueOf(tempRow.get("name"));
		    dmod.setId(id);
		    dmod.setNm(n);
		    departmentWiseList.add(dmod);
		}
		return departmentWiseList;
	}

	@Override
	public int addEmployee(EmployeeMasterModel model) throws Exception {
		int flagarr=-1;
		try {
			String plainpwrd=randompwd.genRandom(10,2);	
			String str[]=pwdEncode.encode(plainpwrd);
			String stUsername = generateUniqueUsername(model.getStrEmpFirstName(), model.getStrEmpLastName());
			
			Emp_Mst_Domain emp_mst_dom = convertEmpMstModelToEmpMstDomain(model,str[1],model.getStrBISEmpId(),stUsername);
			RegisterDomain regDom1 = convertregModToregDom(model,str[1],stUsername);
			BisUserSalts bisSaltObj = new BisUserSalts();
			bisSaltObj.setUser_id1(model.getStrBISEmpId());
			bisSaltObj.setSalts(str[0]);
			int flag = dao.addEmpdetail(emp_mst_dom,bisSaltObj,regDom1);
			if(flag==1) {
				flagarr = dao.adduserrolemapping(model);
				System.out.println(flagarr);
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return flagarr;
	}
	private RegisterDomain convertregModToregDom(EmployeeMasterModel EmpMstMod,String str, String stUsername) {
		RegisterDomain regDom = new RegisterDomain();
		String strUserStatus="";
		String strEmpStatus="A";		
		regDom.setUsername(stUsername);
		regDom.setStrFname(EmpMstMod.getStrEmpFirstName());
		regDom.setStrMname(EmpMstMod.getStrEmpMiddleName());
		regDom.setStrLname(EmpMstMod.getStrEmpLastName());
		regDom.setEmail(EmpMstMod.getStrEmail());
		regDom.setDob(EmpMstMod.getStDob());
		regDom.setUser_id(EmpMstMod.getStrBISEmpId());
		regDom.setSalutation_Id(0);		
		regDom.setStrStatus(strEmpStatus);
			int id=EmpMstMod.getNumDesignId1();
			List<Design_Mst_Domain> designlist = dao.getabbreDesign(id);
			if(designlist.size()>0){
			strUserStatus=designlist.get(0).getStr_emp_type();
			regDom.setStrUserType(strUserStatus);
			}
			if(EmpMstMod.getLabtype()==1 && strUserStatus.equals("")){
			regDom.setStrUserType("E");
			}
		regDom.setStrPass(str);
		regDom.setNumMob(EmpMstMod.getStrMobNo());
	   return regDom;
	}

private String generateUniqueUsername(String stFName, String stLName) {
	String temp = "";
	String temp2 = "";
	if(stLName.length()==0 && stFName.length()>2){
		temp = stFName.substring(0,3).toLowerCase();
	}
	if(stFName.length()==0 && stLName.length()>2){
		temp2 = stLName.substring(0, 3).toLowerCase();
	}
	if(stFName.length()>2 && stLName.length()>2){
		temp = stFName.substring(0,3).toLowerCase();
		temp2 = stLName.substring(0, 3).toLowerCase();
	}
	int maxlenforlpad=5;
	
	String stUsername ;
	
	 List<Map<String, Object>> list = globalDao.generateUniqueUsernamenew(temp,temp2,startlpad,maxlenforlpad);
	 
	 stUsername=list.get(0).get("uname").toString();
	 int iCheckUsername = globalDao.checkUsername(stUsername);
	if(iCheckUsername!=0){
		startlpad=startlpad+1;
		stUsername=generateUniqueUsername(stFName, stLName);
	}
	
	return stUsername;
	}

private Emp_Mst_Domain convertEmpMstModelToEmpMstDomain(EmployeeMasterModel model, String EncryptPwd, int strBISEmpId,	String stUsername) {
	Emp_Mst_Domain EmpMstDom = new Emp_Mst_Domain();
	try
	{					
		String fnm="";
		String mnm="";
		String lnm="";
		
		if(!model.getStrEmpFirstName().isEmpty()){
			fnm=model.getStrEmpFirstName();
			String newStr="";
			String newArr[];
			String temp="";
		        newArr = fnm.split(" ");
		        for(int i = 0 ; i < newArr.length ; i++){
		        	temp = newArr[i].substring(0, 1).toUpperCase() + newArr[i].substring(1).toLowerCase();
		        	newStr+=temp;
		        }       
		        model.setStrEmpFirstName(newStr);
		    fnm=newStr;
		}
		
		if(!model.getStrEmpMiddleName().isEmpty()){
			mnm=model.getStrEmpMiddleName();
			String newStr="";
			String newArr[];
			String temp="";
		        newArr = mnm.split(" ");
		        for(int i = 0 ; i < newArr.length ; i++){
		        	temp = newArr[i].substring(0, 1).toUpperCase() + newArr[i].substring(1).toLowerCase();
		        	newStr+=temp;
		        }       
		        model.setStrEmpMiddleName(newStr);
		    mnm=newStr;
		}
		
		if(!model.getStrEmpLastName().isEmpty()){
			lnm=model.getStrEmpLastName();
			String newStr="";
			String newArr[];
			String temp="";
		        newArr = lnm.split(" ");
		        for(int i = 0 ; i < newArr.length ; i++){
		        	temp = newArr[i].substring(0, 1).toUpperCase() + newArr[i].substring(1).toLowerCase();
		        	newStr+=temp;
		        }       
		        model.setStrEmpLastName(newStr);
		    lnm=newStr;
		}
		
	EmpMstDom.setStrEmpFirstName(fnm);
	EmpMstDom.setStrEmpMiddleName(mnm);
	EmpMstDom.setStrEmpLastName(lnm);
	
	EmpMstDom.setStrEncryptPwd(EncryptPwd);
	EmpMstDom.setNumLocationId(Integer.parseInt(model.getNumDeptNameId()));
	EmpMstDom.setNumLocationTypeId(Integer.parseInt(model.getNumDeptId1()));			
	EmpMstDom.setStrEmail(model.getStrEmail());
	EmpMstDom.setStrBISEmpId(model.getStrBISEmpId());
	EmpMstDom.setUser_id(model.getStrBISEmpId());
	EmpMstDom.setIsValid(1);
	EmpMstDom.setNumEntryEmpId(model.getUserid());
	EmpMstDom.setNum_is_osl_employee(model.getLabtype());
	
	if(model.getLabtype()==1){
		EmpMstDom.setStrDob("01/01/0001");
		EmpMstDom.setNumDesignId(0);
		EmpMstDom.setNumDisciplineId(0);
		EmpMstDom.setStrMob("");
		EmpMstDom.setNum_foreign_inspection_applicable(0);
		EmpMstDom.setStr_pan("");
	}else{
	EmpMstDom.setStrDob(model.getStDob());		
	EmpMstDom.setNumDesignId(model.getNumDesignId1());		
	EmpMstDom.setNumDisciplineId(model.getNumDisciplineId());		
	EmpMstDom.setStr_pan(model.getStr_pan().toUpperCase());
	EmpMstDom.setNum_foreign_inspection_applicable(model.getNum_foreign_investigation());	
	EmpMstDom.setStrMob(model.getStrMobNo());
	}
	}
	catch(Exception e)
	{
		e.printStackTrace(); 
	}
	return EmpMstDom;
	
}

@Override
public List<Map<String, Object>> getEmpDetailforBISUserID(String bis_user_id) {
	List<Map<String, Object>> list = dao.getEmpDetailforBISUserID(bis_user_id);
	for(int i=0;i<list.size();i++) {
		Map<String, Object> temp = list.get(i);
		String emp_id=temp.get("str_BIS_Emp_Id").toString();
		String OtherRoles = dao.getAllRoleName(emp_id);
		String OtherRolesId = dao.getAllRoleId(emp_id);
		temp.put("OtherRoles", OtherRoles);
		temp.put("OtherRolesId", OtherRolesId);
	}
	return list;
}

@Override
public int editEmployee(EmployeeMasterModel model)throws Exception {
	List<Emp_Mst_Domain> list = dao.getEmployeeMasterDetails(model.getStrBISEmpId());
	int flag=-1;
	int flag1=-1;
	int flag2=-1;
	int flag3=-1;
	int flag4=-1;
	if(list.size()>0)
	{
		Emp_Mst_Domain_log Log = EmployeeMasterLog(list.get(0),model.getUserid());
		flag1 = dao.EmployeeMasterLog(Log);
		if(flag1==1) {
				Emp_Mst_Domain EmpMstDom = list.get(0);
				String fnm="";
				String mnm="";
				String lnm="";
				
				if(!model.getStrEmpFirstName().isEmpty()){
					fnm=model.getStrEmpFirstName();
					String newStr="";
					String newArr[];
					String temp="";
				        newArr = fnm.split(" ");
				        for(int i1 = 0 ; i1 < newArr.length ; i1++){
				        	temp = newArr[i1].substring(0, 1).toUpperCase() + newArr[i1].substring(1).toLowerCase();
				        	newStr+=temp;
				        }       
				        model.setStrEmpFirstName(newStr);
				    fnm=newStr;
				}
				
				if(!model.getStrEmpMiddleName().isEmpty()){
					mnm=model.getStrEmpMiddleName();
					String newStr="";
					String newArr[];
					String temp="";
				        newArr = mnm.split(" ");
				        for(int i1 = 0 ; i1 < newArr.length ; i1++){
				        	temp = newArr[i1].substring(0, 1).toUpperCase() + newArr[i1].substring(1).toLowerCase();
				        	newStr+=temp;
				        }       
				        model.setStrEmpMiddleName(newStr);
				    mnm=newStr;
				}
				
				if(!model.getStrEmpLastName().isEmpty()){
					lnm=model.getStrEmpLastName();
					String newStr="";
					String newArr[];
					String temp="";
				        newArr = lnm.split(" ");
				        for(int i1 = 0 ; i1 < newArr.length ; i1++){
				        	temp = newArr[i1].substring(0, 1).toUpperCase() + newArr[i1].substring(1).toLowerCase();
				        	newStr+=temp;
				        }       
				        model.setStrEmpLastName(newStr);
				    lnm=newStr;
				}
				EmpMstDom.setStrEmpFirstName(fnm);
				EmpMstDom.setStrEmpMiddleName(mnm);
				EmpMstDom.setStrEmpLastName(lnm);
				EmpMstDom.setNumLocationId(Integer.parseInt(model.getNumDeptNameId()));
				EmpMstDom.setNumLocationTypeId(Integer.parseInt(model.getNumDeptId1()));			
				EmpMstDom.setStrEmail(model.getStrEmail());
				EmpMstDom.setStrBISEmpId(model.getStrBISEmpId());
				EmpMstDom.setUser_id(model.getStrBISEmpId());
				EmpMstDom.setIsValid(1);
				EmpMstDom.setNumEntryEmpId(model.getUserid());
				EmpMstDom.setNum_is_osl_employee(model.getLabtype());
				
				if(model.getLabtype()==1){
					EmpMstDom.setStrDob("01/01/0001");
					EmpMstDom.setNumDesignId(0);
					EmpMstDom.setNumDisciplineId(0);
					EmpMstDom.setStrMob("");
					EmpMstDom.setNum_foreign_inspection_applicable(0);
					EmpMstDom.setStr_pan("");
				}else{
				EmpMstDom.setStrDob(model.getStDob());		
				EmpMstDom.setNumDesignId(model.getNumDesignId1());		
				EmpMstDom.setNumDisciplineId(model.getNumDisciplineId());		
				EmpMstDom.setStr_pan(model.getStr_pan().toUpperCase());
				EmpMstDom.setNum_foreign_inspection_applicable(model.getNum_foreign_investigation());	
				EmpMstDom.setStrMob(model.getStrMobNo());
				}
				EmpMstDom.setDt_Entry_Date(new Date());
				flag2=dao.editEmployeeMaster(EmpMstDom);
				if(flag2==1) {
					flag3=editReg(model);
					if(flag3==1) {
						try {
						dao.DisableRoleMaster(model);
						String roleId[]=model.getNumRoleId().split(",");
						for(int d=0;d<roleId.length;d++){
							int iRoleId = Integer.parseInt(roleId[d]);
							List<Integer> ai=dao.getRoleTileMapping(iRoleId);
							UserRoleMasterDomain Mapping2= new UserRoleMasterDomain();
							if(ai.size()>0) {
							for(int i=0;i<ai.size();i++){
								Mapping2.setCrsdata("0");
								Mapping2.setDate(new Date());
								Mapping2.setNum_entry_emp_id(Long.parseLong(String.valueOf(model.getStrBISEmpId())));
								Mapping2.setNum_isvalid(1);
								Mapping2.setNum_process_id(Integer.parseInt(ai.get(i).toString()));
								Mapping2.setNum_role_id(iRoleId);
								Mapping2.setNum_user_id(model.getStrBISEmpId());
								if(model.getPriorityRoleId() == iRoleId)
									{
									Mapping2.setPriority_id(1);
									}
								else
									{
									Mapping2.setPriority_id(0);
									}
								dao.EditUserRoleMaster(Mapping2);
							}
							}else {
								Mapping2.setCrsdata("0");
								Mapping2.setDate(new Date());
								Mapping2.setNum_entry_emp_id(Long.parseLong(String.valueOf(model.getStrBISEmpId())));
								Mapping2.setNum_isvalid(1);
								Mapping2.setNum_process_id(972);
								Mapping2.setNum_role_id(iRoleId);
								Mapping2.setNum_user_id(model.getStrBISEmpId());
								if(model.getPriorityRoleId() == iRoleId)
								{
								Mapping2.setPriority_id(1);
								}
							else
								{
								Mapping2.setPriority_id(0);
								}
								dao.EditUserRoleMaster(Mapping2);
							}
						}
						flag4=1;
						}catch(Exception ex) {
							ex.printStackTrace();
							flag4=0;
						}
					}
				}
			
		}
	}
	if(flag1==1 && flag2==1 && flag3==1 && flag4==1) {
		flag=1;
	}else {
		flag=0;
	}
	return flag;
}

private int editReg(EmployeeMasterModel model) throws Exception {
	List<RegisterDomain> list = dao.editReg(model);
	String fnm="";
	String mnm="";
	String lnm="";
	int flag=-1;
	if(!model.getStrEmpFirstName().isEmpty()){
		fnm=model.getStrEmpFirstName();
		String newStr="";
		String newArr[];
		String temp="";
	        newArr = fnm.split(" ");
	        for(int i1 = 0 ; i1 < newArr.length ; i1++){
	        	temp = newArr[i1].substring(0, 1).toUpperCase() + newArr[i1].substring(1).toLowerCase();
	        	newStr+=temp;
	        }       
	        model.setStrEmpFirstName(newStr);
	    fnm=newStr;
	}
	
	if(!model.getStrEmpMiddleName().isEmpty()){
		mnm=model.getStrEmpMiddleName();
		String newStr="";
		String newArr[];
		String temp="";
	        newArr = mnm.split(" ");
	        for(int i1 = 0 ; i1 < newArr.length ; i1++){
	        	temp = newArr[i1].substring(0, 1).toUpperCase() + newArr[i1].substring(1).toLowerCase();
	        	newStr+=temp;
	        }       
	        model.setStrEmpMiddleName(newStr);
	    mnm=newStr;
	}
	
	if(!model.getStrEmpLastName().isEmpty()){
		lnm=model.getStrEmpLastName();
		String newStr="";
		String newArr[];
		String temp="";
	        newArr = lnm.split(" ");
	        for(int i1 = 0 ; i1 < newArr.length ; i1++){
	        	temp = newArr[i1].substring(0, 1).toUpperCase() + newArr[i1].substring(1).toLowerCase();
	        	newStr+=temp;
	        }       
	        model.setStrEmpLastName(newStr);
	    lnm=newStr;
	}
	if(list.size()>0) {
		RegisterDomain updateDoamin = list.get(0);
		updateDoamin.setEmail(model.getStrEmail());
		updateDoamin.setStrFname(fnm);
		updateDoamin.setStrMname(mnm);
		updateDoamin.setStrLname(lnm);
		updateDoamin.setNum_password_change(1);
		updateDoamin.setDob(model.getStDob());
		int did=model.getNumDesignId1();
		List<Design_Mst_Domain> designlist = dao.getabbreDesign(did);
		String strUserStatus=designlist.get(0).getStr_emp_type();
		updateDoamin.setStrUserType(strUserStatus);
		updateDoamin.setNumMob(model.getStrMobNo());
		flag=dao.updatereg(updateDoamin);
	}
	return flag;
}


private Emp_Mst_Domain_log EmployeeMasterLog(Emp_Mst_Domain domain, int userid) {
	Emp_Mst_Domain_log log=new Emp_Mst_Domain_log();
	log.setLogdate(new Date());
	log.setDt_Entry_Date(domain.getDt_Entry_Date());
	log.setIsValid(domain.getIsValid());
	log.setNumDepartmentId(domain.getNumDepartmentId());
	log.setNumDesignId(domain.getNumDesignId());
	log.setNumDisciplineId(domain.getNumDisciplineId());
	log.setNumEmpIdauto(domain.getNumEmpIdauto());
	log.setLog_USER_ID(userid+"");
	log.setNumEntryEmpId(domain.getNumEntryEmpId());
	log.setNumLocationId(domain.getNumLocationId());
	log.setNumLocationIdOld(domain.getNumLocationIdOld());
	log.setNumLocationTypeId(domain.getNumLocationTypeId());
	log.setNumLocationTypeIdOld(domain.getNumLocationTypeIdOld());
	log.setStrBISEmpId(domain.getStrBISEmpId());
	log.setCrsdata(domain.getCrsdata());
	log.setExpDocName(domain.getExpDocName());
	log.setIsOutsourceAgency(domain.getIsOutsourceAgency());
	log.setJoinDt(domain.getJoinDt());
	log.setNum_foreign_inspection_applicable(domain.getNum_foreign_inspection_applicable());
	log.setNum_is_osl_employee(domain.getNum_is_osl_employee());
	log.setNum_post_id(domain.getNum_post_id());
	log.setOsaFromEmpMst(domain.getOsaFromEmpMst());
	log.setQualification(domain.getQualification());
	log.setRelievDt(domain.getRelievDt());
	log.setStr_pan(domain.getStr_pan());
	log.setStrBISEmpId(domain.getStrBISEmpId());
	log.setStrDob(domain.getStrDob());
	log.setStrEmail(domain.getStrEmail());
	log.setStrEmpFirstName(domain.getStrEmpFirstName());
	log.setStrEmpLastName(domain.getStrEmpLastName());
	log.setStrEmpMiddleName(domain.getStrEmpMiddleName());
	log.setStrEncryptPwd(domain.getStrEncryptPwd());
	log.setStrMob(domain.getStrMob());
	log.setTotalExperience(domain.getTotalExperience());
	log.setUser_id(domain.getUser_id());
	return log;
}

@Override
public int DeleteEmployeeMaster(String bis_user_id) {
	int flag=-1;
	try {
		dao.deleteEmployee(bis_user_id);
		dao.delregisterEmp(bis_user_id);
		dao.delUserRoleMasterEntryEmp(bis_user_id);
		flag=1;
	}catch(Exception ex) {
		ex.printStackTrace();
		flag=0;
	}
	
	return flag;
}

}
