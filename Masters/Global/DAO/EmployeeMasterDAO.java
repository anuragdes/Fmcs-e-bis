package eBIS.Masters.Global.DAO;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import Global.CommonUtility.DAO.DaoHelperReadonly;
import Global.CommonUtility.Domain.BisUserSalts;
import Global.Registration.Domain.RegisterDomain;
import Masters.Domain.Design_Mst_Domain;
import Masters.Domain.Discipline_Mst_Domain;
import Masters.Domain.Emp_Mst_Domain;
import Masters.Domain.Emp_Mst_Domain_log;
import Masters.Domain.LocationType_Mst_Domain;
import Masters.Domain.Role_Mst_Domain;
import Masters.Domain.UserRoleMasterDomain;
import eBIS.AppConfig.PrimaryDaoHelper;
import eBIS.Masters.Global.Model.EmployeeMasterModel;

@Repository
public class EmployeeMasterDAO {
	@Autowired 
	PrimaryDaoHelper daoHelper;
	@Autowired 
	DaoHelperReadonly daoHelperreradonly;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
    @Qualifier(value="jdbcTemplateReadonly")
	JdbcTemplate jdbcTemplateReadonly;
	
	public List<Discipline_Mst_Domain> getDisciplines() {
		String qry="SELECT s from Discipline_Mst_Domain s where s.isValid=1 order by s.discipline_Name";
		return daoHelperreradonly.findByQuery(qry);
	}

	public List<Design_Mst_Domain> getDesign() {
		String qry="Select p from Design_Mst_Domain p where IsValid = 1 order by p.strDesignName";
		return daoHelperreradonly.findByQuery(qry);
	}

	public List<Role_Mst_Domain> getRoles() {
		String qry="SELECT p FROM Role_Mst_Domain p WHERE p.isValid=1 order by p.role_Name";
		return daoHelperreradonly.findByQuery(qry);
	}

	public List<LocationType_Mst_Domain> getdepart() {
		String qry="SELECT c FROM LocationType_Mst_Domain c WHERE c.locationType_Id <> 6 AND c.isValid = 1 ORDER BY c.locationType_Name";
		return daoHelperreradonly.findByQuery(qry);
	}

	public List<Map<String, Object>> getEmpDetail() {
		String sql="--EXPLAIN ANALYSE \r\n"
				+ "select distinct a.num_emp_id_auto as num_id, a.NUM_USER_ID as num_user_id, a.str_BIS_Emp_Id as str_bis_emp_id, \r\n"
				+ "replace((nvl(a.STR_EMP_FIRSTNAME,'')  ||' '|| nvl(a.STR_EMP_MIDLENAME,'') ||' '|| nvl(a.STR_EMP_LASTNAME,'') ),'  ',' ') as emp_name, \r\n"
				+ "a.STR_EMAIL as str_email, to_char(to_date(replace((nvl(replace(a.STR_DOB,' 00:00:00',''),'0001-01-01')),'00-00-0000','0001-01-01')),'yyyy/MM/dd') as str_dob, a.NUM_LOCATIONTYPE_ID as num_locationtype_id, \r\n"
				+ "a.NUM_LOCATION_ID as num_location_id, a.num_Department_Id as num_department_id, \r\n"
				+ "nvl(decode(a.NUM_LOCATIONTYPE_ID,\r\n"
				+ "1,(SELECT nvl(str_ro_name,'--') FROM gblt_regional_mst WHERE num_isvalid = 1 and num_ro_id=a.NUM_LOCATION_ID ),\r\n"
				+ "2,(SELECT nvl(str_branc_short_name,'--') FROM  gblt_branch_mst WHERE num_isvalid = 1 and num_branch_id=a.NUM_LOCATION_ID ),\r\n"
				+ "3,(SELECT nvl(str_lab_name,'--') FROM bislab.lab_mst  WHERE str_lab_code <> 0 AND num_lab_type=1 and str_lab_code=a.NUM_LOCATION_ID ),\r\n"
				+ "4,(SELECT nvl(str_lab_name,'--') FROM bislab.lab_mst  WHERE str_lab_code <> 0 AND num_lab_type=2  and str_lab_code=a.NUM_LOCATION_ID ),\r\n"
				+ "5,(SELECT nvl(str_dept_name,'--') FROM  gblt_department_mst  WHERE num_isvalid=1 and num_dept_id=a.NUM_LOCATION_ID ),\r\n"
				+ "(select to_char('--') from dual)\r\n"
				+ "),'--') as dep_name, nvl((select k.str_desig_name from bis_dev.gblt_designation_mst k where k.num_desig_id=a.num_Design_Id and k.num_isvalid=1),'--') as str_design_name,\r\n"
				+ "a.num_Discipline_Id as num_Discipline_Id, a.num_Design_Id as num_Design_Id, nvl(a.str_pan,'--') as str_pan, \r\n"
				+ "a.NUM_MOBILE as NUM_MOBILE, a.num_foreign_inspection_applicable as num_foreign_inspection_applicable, \r\n"
				+ "a.num_Department_Id as num_Department_Id, (select locationty1_.STR_LOCATION_TYPE_NAME from bis_dev.gblt_location_type_mst locationty1_ \r\n"
				+ "where locationty1_.NUM_LOCATION_TYPE_ID=a.NUM_LOCATIONTYPE_ID) as dep_type, \r\n"
				+ "nvl(a.num_is_osl_employee::text,'') as num_is_osl_employee,\r\n"
				+ "nvl((select role_mst_d2_.STR_ROLE_NAME from bis_dev.gblt_role_mst role_mst_d2_ \r\n"
				+ "where role_mst_d2_.NUM_ROLE_ID=(select userrolema5_.NUM_ROLE_ID from bis_dev.gblt_user_role_mapping userrolema5_ where \r\n"
				+ "userrolema5_.NUM_ISVALID=1 and userrolema5_.PRIORITY_ID=1\r\n"
				+ "and to_char(userrolema5_.NUM_USER_ID)=a.str_BIS_Emp_Id limit 1)),'NA') as str_role_name,\r\n"
				+ "nvl((select userrolema5_.NUM_ROLE_ID from bis_dev.gblt_user_role_mapping userrolema5_ where \r\n"
				+ "userrolema5_.NUM_ISVALID=1 and userrolema5_.PRIORITY_ID=1\r\n"
				+ "and to_char(userrolema5_.NUM_USER_ID)=a.str_BIS_Emp_Id limit 1),'0') as num_role_id\r\n"
				+ "from bis_dev.bis_employee_mst a where a.NUM_ISVALID=1 and \r\n"
				+ "(a.str_BIS_Emp_Id not like '11000%') and\r\n"
				+ "(a.str_BIS_Emp_Id not like '1200%') order by emp_name";
		return jdbcTemplate.queryForList(sql);
	}


	public String getRoleName(String emp_id) {
		String sql="select nvl((select role_mst_d2_.STR_ROLE_NAME from bis_dev.gblt_role_mst role_mst_d2_ \r\n"
				+ " where role_mst_d2_.NUM_ROLE_ID=(select userrolema5_.NUM_ROLE_ID from bis_dev.gblt_user_role_mapping userrolema5_ where \r\n"
				+ "userrolema5_.NUM_ISVALID=1 and userrolema5_.PRIORITY_ID=1\r\n"
				+ " and to_char(userrolema5_.NUM_USER_ID)='"+emp_id+"' limit 1)),'NA') as STR_ROLE_NAME";
		int size = jdbcTemplate.queryForList(sql).size();
		String name="No Role";
		if(size>0) {
			name=jdbcTemplate.queryForList(sql).get(0).get("STR_ROLE_NAME").toString();
		}
		return name;
	}
	public String getAllRoleName(String emp_id){
		String sql="select nvl(String_agg(DISTINCT r.NUM_ROLE_ID,','),'0') nm from gblt_user_role_mapping r where r.NUM_USER_ID='"+emp_id+"' and r.NUM_ISVALID=1";
		String name="No Role";
		if(!jdbcTemplate.queryForList(sql).get(0).get("nm").toString().equals("0")) {
			String[] temp = jdbcTemplate.queryForList(sql).get(0).get("nm").toString().split(",");
				sql="select String_agg(role_mst_d2_.STR_ROLE_NAME,',') AS STR_ROLE_NAME from bis_dev.gblt_role_mst role_mst_d2_ \r\n"
						+ "where role_mst_d2_.NUM_ROLE_ID in ("+Arrays.toString(temp).replace("[", "").replace("]", "")+")";
				name=jdbcTemplate.queryForList(sql).get(0).get("STR_ROLE_NAME").toString();
		}
		return name;
	}
	public String getAllRoleId(String emp_id){
		String sql="select nvl(String_agg(DISTINCT r.NUM_ROLE_ID,','),'0') nm from gblt_user_role_mapping r where r.NUM_USER_ID='"+emp_id+"' and r.NUM_ISVALID=1";
		String name="0";
		if(!jdbcTemplate.queryForList(sql).get(0).get("nm").toString().equals("0")) {
			String[] temp = jdbcTemplate.queryForList(sql).get(0).get("nm").toString().split(",");
				sql="select String_agg(role_mst_d2_.NUM_ROLE_ID,',') AS NUM_ROLE_ID from bis_dev.gblt_role_mst role_mst_d2_ \r\n"
						+ "where role_mst_d2_.NUM_ROLE_ID in ("+Arrays.toString(temp).replace("[", "").replace("]", "")+")";
				name=jdbcTemplate.queryForList(sql).get(0).get("NUM_ROLE_ID").toString();
		}
		return name;
	}

	public List<Map<String, Object>> getDepartmentName(int departmentTypeID, String departmentTypeName) {
		String sql = 
				 (departmentTypeID == 1 && departmentTypeName.equalsIgnoreCase("HQ")) ?
						"SELECT to_char(num_ro_id) as id ,str_ro_name as name,num_isvalid "+
						"FROM gblt_regional_mst WHERE num_ro_id = 4 AND num_isvalid = 1 ORDER BY str_ro_name"
				:(departmentTypeID == 1 && !departmentTypeName.equalsIgnoreCase("HQ")) ? 
						"SELECT to_char(num_ro_id) as id ,str_ro_name as name,num_isvalid "+
						"FROM gblt_regional_mst WHERE num_ro_id <> 4 AND num_isvalid = 1 ORDER BY str_ro_name" 
				:(departmentTypeID == 2) ? 
						"SELECT to_char(num_branch_id) as id ,str_branc_short_name as name,num_isvalid "+
						"FROM gblt_branch_mst WHERE num_isvalid = 1 ORDER BY str_branc_short_name" 
				:(departmentTypeID == 3) ? 
						"SELECT str_lab_code as id, str_lab_name || '(Lab Code-'||str_lab_code||')' as name "+
						"FROM bislab.lab_mst  WHERE str_lab_code <> 0 AND num_lab_type=1 AND num_isvalid=1 order by name" 
				:(departmentTypeID == 4) ? 
						"SELECT str_lab_code as id, str_lab_name|| '(Lab Code-'||str_lab_code||')' as name "+
						"FROM bislab.lab_mst  WHERE str_lab_code <> 0 AND num_lab_type=2 AND num_isvalid=1 order by name"    //As Per Client Requirment IsValid=1 added
				:(departmentTypeID == 5) ? 
						"SELECT to_char(num_dept_id ) as id,str_dept_name as name,num_isvalid "+
						"FROM gblt_department_mst  WHERE num_isvalid=1 ORDER BY (name)" 
				:(departmentTypeID == 7) ?
						"SELECT str_lab_code as id, str_lab_name|| '(Lab Code-'||str_lab_code||')' as name "+
						"FROM bislab.lab_mst  WHERE str_lab_code <> 0 AND num_lab_type=2 AND num_isvalid=1 order by name" 
				:"";
		System.out.println("application_Detail_qry: "+sql);
		return jdbcTemplateReadonly.queryForList(sql);
	}

	public List<Integer> rtmlist(int roleid) {
		List<Integer> lsDomain = new ArrayList<Integer>();	
    	String strHQL = "Select distinct c.num_process_id from Role_Tile_Mapping c where c.num_role_id="+roleid+" and c.num_isValid=1";
		lsDomain = daoHelper.findByQuery(strHQL);		
		
		return lsDomain;
	}

	public List<Design_Mst_Domain> getabbreDesign(int id) {
		String strHQL = "Select c from Design_Mst_Domain c where c.numDesignId="+id+"";
		
		return daoHelper.findByQuery(strHQL);
	}

	public int addEmpdetail(Emp_Mst_Domain doamin, BisUserSalts bisSaltObj, RegisterDomain regDom1) throws Exception {
		int flag=-1;
		try {
			daoHelper.merge( doamin);
			daoHelper.merge( bisSaltObj);
			daoHelper.merge(regDom1);
			flag=1;
		}catch(Exception ex) {
			ex.printStackTrace();
			flag=0;
		}
		return flag;
	}

	public int adduserrolemapping(EmployeeMasterModel model) throws Exception {
		List<Integer> rtmlist=new ArrayList<Integer>();
		String str_role=model.getNumRoleId();
		String role[]=str_role.split(",");
		int tempflag=0;
		int tempflag2=0;
		int tempflag3=0;
		if(str_role.length()>0){
			for(int j=0;j<role.length;j++){				
					rtmlist=rtmlist(Integer.parseInt(role[j]));
					if(rtmlist.size()>0){
						for(int i=0;i<rtmlist.size();i++){
							
							UserRoleMasterDomain urmd=new UserRoleMasterDomain();
							urmd.setCrsdata("0");
							
							if(Integer.parseInt(role[j])==model.getPriorityRoleId()){
								urmd.setPriority_id(1);
							}else{
								urmd.setPriority_id(0);
							}
							
							urmd.setNum_entry_emp_id(Long.parseLong(model.getUserid()+""));
							urmd.setNum_isvalid(1);
							urmd.setNum_process_id(rtmlist.get(i));
							urmd.setNum_role_id(Integer.parseInt(role[j]));
							urmd.setNum_user_id(model.getStrBISEmpId());
							int flag = addUserRoleTileMap(urmd);
							tempflag=tempflag+flag;
							tempflag3=tempflag3+1;
					
						}
					}
			}
		}
		if(tempflag==tempflag3) {
			tempflag2=1;
		}else {
			tempflag2=0;
		}
		return tempflag2;
	}

	private int addUserRoleTileMap(UserRoleMasterDomain urmd) {
		int flag=-1;
		try {
			daoHelper.merge(urmd);
			flag=1;
		}catch(Exception ex) {
			ex.printStackTrace();
			flag=0;
		}
		return flag;
		
	}

	public List<Map<String, Object>> getEmpDetailforBISUserID(String bis_user_id) {
		String sql="--EXPLAIN ANALYSE \r\n"
				+ "select distinct a.num_emp_id_auto as num_id, a.NUM_USER_ID as num_user_id, a.str_BIS_Emp_Id as str_bis_emp_id, \r\n"
				+ "replace((nvl(a.STR_EMP_FIRSTNAME,'')  ||' '|| nvl(a.STR_EMP_MIDLENAME,'') ||' '|| nvl(a.STR_EMP_LASTNAME,'') ),'  ',' ') as emp_name, \r\n"
				+ "replace(nvl(a.STR_EMP_FIRSTNAME,''),'  ',' ') as STR_EMP_FIRSTNAME,\r\n"
				+ "replace(nvl(a.STR_EMP_MIDLENAME,''),'  ',' ') as STR_EMP_MIDLENAME,\r\n"
				+ "replace(nvl(a.STR_EMP_LASTNAME,''),'  ',' ') as STR_EMP_LASTNAME,\r\n"
				+ "a.STR_EMAIL as str_email, to_char(to_date(replace((nvl(replace(a.STR_DOB,' 00:00:00',''),'0001-01-01')),'00-00-0000','0001-01-01')),'yyyy/MM/dd') as str_dob, a.NUM_LOCATIONTYPE_ID as num_locationtype_id, \r\n"
				+ "a.NUM_LOCATION_ID as num_location_id, a.num_Department_Id as num_department_id, \r\n"
				+ "nvl(decode(a.NUM_LOCATIONTYPE_ID,\r\n"
				+ "1,(SELECT nvl(str_ro_name,'--') FROM gblt_regional_mst WHERE num_isvalid = 1 and num_ro_id=a.NUM_LOCATION_ID ),\r\n"
				+ "2,(SELECT nvl(str_branc_short_name,'--') FROM  gblt_branch_mst WHERE num_isvalid = 1 and num_branch_id=a.NUM_LOCATION_ID ),\r\n"
				+ "3,(SELECT nvl(str_lab_name,'--') FROM bislab.lab_mst  WHERE str_lab_code <> 0 AND num_lab_type=1 and str_lab_code=a.NUM_LOCATION_ID ),\r\n"
				+ "4,(SELECT nvl(str_lab_name,'--') FROM bislab.lab_mst  WHERE str_lab_code <> 0 AND num_lab_type=2  and str_lab_code=a.NUM_LOCATION_ID ),\r\n"
				+ "5,(SELECT nvl(str_dept_name,'--') FROM  gblt_department_mst  WHERE num_isvalid=1 and num_dept_id=a.NUM_LOCATION_ID ),\r\n"
				+ "(select to_char('--') from dual)\r\n"
				+ "),'--') as dep_name, nvl((select k.str_desig_name from bis_dev.gblt_designation_mst k where k.num_desig_id=a.num_Design_Id and k.num_isvalid=1),'--') as str_design_name,\r\n"
				+ "a.num_Discipline_Id as num_Discipline_Id, a.num_Design_Id as num_Design_Id, nvl(a.str_pan,'--') as str_pan, \r\n"
				+ "a.NUM_MOBILE as NUM_MOBILE, a.num_foreign_inspection_applicable as num_foreign_inspection_applicable, \r\n"
				+ "a.num_Department_Id as num_Department_Id, (select locationty1_.STR_LOCATION_TYPE_NAME from bis_dev.gblt_location_type_mst locationty1_ \r\n"
				+ "where locationty1_.NUM_LOCATION_TYPE_ID=a.NUM_LOCATIONTYPE_ID) as dep_type, \r\n"
				+ "nvl(a.num_is_osl_employee::text,'0') as num_is_osl_employee,\r\n"
				+ "nvl((select role_mst_d2_.STR_ROLE_NAME from bis_dev.gblt_role_mst role_mst_d2_ \r\n"
				+ "where role_mst_d2_.NUM_ROLE_ID=(select userrolema5_.NUM_ROLE_ID from bis_dev.gblt_user_role_mapping userrolema5_ where \r\n"
				+ "userrolema5_.NUM_ISVALID=1 and userrolema5_.PRIORITY_ID=1\r\n"
				+ "and to_char(userrolema5_.NUM_USER_ID)=a.str_BIS_Emp_Id limit 1)),'NA') as str_role_name,\r\n"
				+ "nvl((select userrolema5_.NUM_ROLE_ID from bis_dev.gblt_user_role_mapping userrolema5_ where \r\n"
				+ "userrolema5_.NUM_ISVALID=1 and userrolema5_.PRIORITY_ID=1\r\n"
				+ "and to_char(userrolema5_.NUM_USER_ID)=a.str_BIS_Emp_Id limit 1),'0') as num_role_id\r\n"
				+ "from bis_dev.bis_employee_mst a where a.NUM_ISVALID=1 and \r\n"
				+ "(a.str_BIS_Emp_Id not like '11000%') and\r\n"
				+ "(a.str_BIS_Emp_Id not like '1200%')\r\n"
				+ "and a.str_BIS_Emp_Id='"+bis_user_id+"'\r\n"
				+ "order by emp_name";
		return jdbcTemplate.queryForList(sql);
	}

	public List<Emp_Mst_Domain> getEmployeeMasterDetails(int strBISEmpId)throws Exception {
		String sql="select c from Emp_Mst_Domain c where c.isValid=1 and  c.strBISEmpId="+strBISEmpId+" order by c.numEmpIdauto desc";
		List<Emp_Mst_Domain> list = daoHelper.findByQuery(sql);
		return list;
	}

	public int EmployeeMasterLog(Emp_Mst_Domain_log log)throws Exception {
		int flag=-1;
		try {
			daoHelper.merge( log);
			flag=1;
		}
		catch(Exception ex) {
			flag=0;
			ex.printStackTrace();
		}
		return flag;
	}

	public int editEmployeeMaster(Emp_Mst_Domain empMstDom)throws Exception {
		int flag=-1;
		try {
			daoHelper.merge(empMstDom);
			flag=1;
		}
		catch(Exception ex) {
			flag=0;
			ex.printStackTrace();
		}
		return flag;
		
	}

	public List<RegisterDomain> editReg(EmployeeMasterModel model) {
		String hql="select m from RegisterDomain m where m.user_id="+model.getStrBISEmpId() +" order by m.numBisUserId desc";
		return daoHelper.findByQuery(hql);
	}

	public List<Discipline_Mst_Domain> getabbrDescipline(int id1) {
		List<Discipline_Mst_Domain> lsDomain = new ArrayList<Discipline_Mst_Domain>();
		String strHQL = "Select c from Discipline_Mst_Domain c where c.discipline_Id="+id1+"";
		lsDomain = daoHelper.findByQuery(strHQL);
		return lsDomain;
		
		
	}

	public int updatereg(RegisterDomain empMstDom)throws Exception {
		int flag=-1;
		try {
			daoHelper.merge(empMstDom);
			flag=1;
		}
		catch(Exception ex) {
			flag=0;
			ex.printStackTrace();
		}
		return flag;
		
	}

	public void DisableRoleMaster(EmployeeMasterModel model) {
		String hql="select r from UserRoleMasterDomain r where r.num_user_id="+model.getStrBISEmpId()+" and r.num_isvalid=1";
		List<UserRoleMasterDomain> list = daoHelper.findByQuery(hql);
		for(int i=0;i<list.size();i++){
			UserRoleMasterDomain d = list.get(i);
			d.setNum_isvalid(0);
			daoHelper.merge(d);
			
		}
	}

	public List<Integer> getRoleTileMapping(int iRoleId) {
		String hql="Select distinct num_process_id from Role_Tile_Mapping where num_role_id="+iRoleId;
		return daoHelper.findByQuery(hql);
	}

	public void EditUserRoleMaster(UserRoleMasterDomain mapping) {
		daoHelper.merge(mapping);
		
	}

	public void deleteEmployee(String bis_user_id) {
List<Emp_Mst_Domain> empList = new ArrayList<Emp_Mst_Domain>();
		
		Emp_Mst_Domain empList1 = new Emp_Mst_Domain();
		try{
			String strHQL = "from Emp_Mst_Domain p where p.strBISEmpId="+bis_user_id;
			empList = daoHelper.findByQuery(strHQL);
			
			
				empList1 = empList.get(0);
				daoHelper.maintainRecordInLog(new Emp_Mst_Domain_log(),empList1 );
				empList1.setIsValid(0);
								
				daoHelper.merge(empList1);
			
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}

	public void delregisterEmp(String bis_user_id) {
List<RegisterDomain> empList = new ArrayList<RegisterDomain>();
		
		RegisterDomain empList1 = new RegisterDomain();
		try{
			String strHQL = "from RegisterDomain p where p.user_id="+bis_user_id;
			empList = daoHelper.findByQuery(strHQL);
			
			for(int i=0;i<empList.size();i++)
			{
				empList1 = empList.get(i);
				daoHelper.maintainRecordInLog(new Emp_Mst_Domain_log(),empList1 );
				empList1.setIsValid(0);
				empList1.setStrStatus("D");				
				daoHelper.merge(empList1);
			}
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void delUserRoleMasterEntryEmp(String bis_user_id) {
		try{
			String strHQL2 = "from UserRoleMasterDomain p where p.num_user_id="+bis_user_id;
			List<UserRoleMasterDomain> MappingList1 = new ArrayList<UserRoleMasterDomain>();
			UserRoleMasterDomain Mapping1= new UserRoleMasterDomain();
			MappingList1 = daoHelper.findByQuery(strHQL2);
			if(MappingList1.size()>0){
				
				for(int i=0;i<MappingList1.size();i++)
				{
				Mapping1 = MappingList1.get(i);
				Mapping1.setNum_isvalid(0);
				daoHelper.merge( Mapping1);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
}
