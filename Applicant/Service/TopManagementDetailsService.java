package Applicant.Service;

import java.util.HashMap;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import Applicant.Model.TopManagementDetailsModel;
import Schemes.ProductCertification.ApplicationSubmission.Domain.topManagementDtlsDomain;

public interface TopManagementDetailsService {
	
	@Transactional(propagation=Propagation.REQUIRED)
	public List<HashMap<String,String>> getLatestTopmanagment(String app_no, String lic_no, int branchID);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean saveManagementDetails(TopManagementDetailsModel TopMgmtModel);

	@Transactional(propagation=Propagation.REQUIRED)
	public String removeManagementDetails(String returnText, int userid);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void addUpload(topManagementDtlsDomain udd);
  
	@Transactional(propagation=Propagation.REQUIRED)
	public List<topManagementDtlsDomain> getDetailsById();
	
	@Transactional(propagation=Propagation.REQUIRED)
	public List<topManagementDtlsDomain> getCircularsId(int url);
	
	
}
