package eBIS.Masters.Global.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import Masters.Domain.Design_Mst_Domain;
import Masters.Domain.Discipline_Mst_Domain;
import Masters.Domain.LocationType_Mst_Domain;
import Masters.Domain.Role_Mst_Domain;
import Masters.Model.Emp_dept_Mod;
import eBIS.Masters.Global.Model.EmployeeMasterModel;

public interface EmployeeMasterService {

	List<Discipline_Mst_Domain> getDisciplines();

	List<Design_Mst_Domain> getDesign();

	List<Role_Mst_Domain> getRoles();

	List<LocationType_Mst_Domain> getdepart();

	 List<Map<String, Object>>  getEmpDetail() throws IOException, InterruptedException, ExecutionException;

	List<Emp_dept_Mod> getDepartmentName(int departmentTypeID, String departmentTypeName);

	@Transactional(propagation=Propagation.REQUIRED)
	int addEmployee(EmployeeMasterModel model) throws Exception;

	List<Map<String, Object>> getEmpDetailforBISUserID(String bis_user_id);
	@Transactional(propagation=Propagation.REQUIRED)
	int editEmployee(EmployeeMasterModel model)throws Exception;
	
	@Transactional(propagation=Propagation.REQUIRED)
	int DeleteEmployeeMaster(String bis_user_id);

}
