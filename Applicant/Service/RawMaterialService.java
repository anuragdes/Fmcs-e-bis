package Applicant.Service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import Applicant.Model.RawMaterialModel;

public interface RawMaterialService {
	@Transactional(propagation=Propagation.REQUIRED)
	public  List<Map<String, Object>>  getLatestRawMaterila(String app_no, String lic_no, int branchID);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean saveRawMaterialDetails(RawMaterialModel rawraterialrodel);

	@Transactional(propagation=Propagation.REQUIRED)
	public boolean deleteRawMaterial(int id);

}
