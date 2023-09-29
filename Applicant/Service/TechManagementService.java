package Applicant.Service;

import java.util.HashMap;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import Applicant.Model.TechManagementDtsModel;
import Schemes.ProductCertification.ApplicationSubmission.Domain.qualityManagementDtlsDomain;

public interface TechManagementService {
	@Transactional(propagation=Propagation.REQUIRED)
	public List<HashMap<String,String>> getLatestTopmanagment(String app_no, String lic_no, int branchID);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean saveManagementDetails(TechManagementDtsModel TechMgmtModel, byte[] imageData);

	@Transactional(propagation=Propagation.REQUIRED)
	public String removeManagementDetails(String returnText, int userid);

	@Transactional(propagation=Propagation.REQUIRED)
	public byte[] getDetailsById(int id);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public List<qualityManagementDtlsDomain> getCircularsId(int url);

}
