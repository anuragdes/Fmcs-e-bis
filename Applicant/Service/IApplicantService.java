package Applicant.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import Applicant.Model.ApplicantModel;
import Applicant.Model.applicantChngNameModel;
import Global.CommonUtility.Domain.TextEditor_Dom;
import Schemes.HallMarking.Jeweller.HMO.Domain.HMJewApplicationTrackingDomain;
import Schemes.HallMarking.Jeweller.HMO.Model.JewHeadModel;
import Schemes.ProductCertification.ApplicationSubmission.Domain.Licence_Brand_Detail_Domain;

public interface IApplicantService {
	
	@Transactional(propagation=Propagation.REQUIRED)
	public List<HashMap> getApplications(int iUserId_p, int iRoleId_p);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public List<HashMap> getLetterDetails(String appId, String branchId);
	
	
	@Transactional(propagation=Propagation.REQUIRED)
	public List<HashMap<String,String>> getSavedApplications(int iUserId_p);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public HashMap getApplicantDashboardData(int iUserId_p, String stEmail_p);
	
	@Transactional(propagation=Propagation.REQUIRED)
	List<HashMap> getLicenseApps(int userid,int roleId);

	@Transactional(propagation=Propagation.REQUIRED)
	public List<Licence_Brand_Detail_Domain> getBrandlist(String cml,int branchid);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void setBrandDoc(String cmlNo,String user_File_Name);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void setBrandDocTrack(String cmlNo,ApplicantModel appModel);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void requestChngName(applicantChngNameModel Model,int UserId,int RoleId);

	@Transactional(propagation=Propagation.REQUIRED)
	public int countPendingFee(int userId);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public int getCountNameChng(int userId);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public TextEditor_Dom getLetterView(String appId,String branchId,String letterId);

	@Transactional(propagation=Propagation.REQUIRED)
	public HashMap getApplicantHallDashboardData(int iUserId_p, String stEmail_p);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public List<HashMap<String, String>> getSavedHMApplications(int iUserId,String hmType);

	@Transactional(propagation=Propagation.REQUIRED)
	public List<HashMap> getHallHMSubmittedApplications(int iUserId, int iRoleId,String hm_type);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public List<HashMap> getHMLicenseApps(int userid,int roleId);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public List<HMJewApplicationTrackingDomain> getLatestTrack(String appid,int branchid,int status);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public List<HashMap<String, String>> getTRFailedList(int iUserId,int iRoleId, String licNum);

	@Transactional(propagation=Propagation.REQUIRED)
	public void setMergeSingleToCorporateDetails(JewHeadModel jewHeadModel,	String[] appIdarray,int iUserId,String appid,int branchid);

	@Transactional(propagation=Propagation.REQUIRED)
	public List<HashMap> getHMRECLicenseApps(int userid,int roleId);
	
	//Added by Dhruv For Self SOM
	@Transactional(propagation=Propagation.REQUIRED)
	public HashMap<String,String> getSelfSOMCMLDetails(String stCmlNo,int iBranchId);

	@Transactional(propagation=Propagation.REQUIRED)
	public String saveMSCDGSTNo(String stGSTNo, int iUserId);

	@Transactional(propagation=Propagation.REQUIRED)
	public List<Map<String, Object>> getsti_and_marking_fee_mapping(int iUserId);

	@Transactional(propagation=Propagation.REQUIRED)
	public boolean submit_accptance(int num_id, int acptstidoc, int acptmarkingfee, String remark);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Map<String, Object>> getchecksum(String isno);

	@Transactional(propagation=Propagation.REQUIRED)
	public List<HashMap> getApplicationsCOC(int iUserId, int iRoleId);

	@Transactional(propagation=Propagation.REQUIRED)
	public List<HashMap> getLicenseAppsCOC(int iUserId, int iRoleId);

	
	
	
}
