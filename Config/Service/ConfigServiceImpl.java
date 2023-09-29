package Global.Config.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Global.CommonUtility.ResourceBundleFile;
import Global.CommonUtility.Service.IRandomPwdService;
import Global.CommonUtility.Service.SendMail;
import Global.Config.DAO.ConfigDao;
import Global.Login.DAO.ForgotPwdDao;
import Global.Login.DAO.LoginDao;
import Global.Registration.Domain.RegisterDomain;
import Masters.DAO.addApplicationAndLicenceDao;
import lab.domain.Lab_Type_Domain;

@Service
public class ConfigServiceImpl implements IConfigService {

	@Autowired
	addApplicationAndLicenceDao addAppLicDao;
	
	@Autowired
	ConfigDao cd;
	
	@Autowired
	LoginDao ldao;
	
	@Autowired
	IRandomPwdService rps;
	
	@Autowired
	ForgotPwdDao fpd;
	
	@Autowired
	SendMail sm;
	
	@Override
	public List<RegisterDomain> getBlockedUsers(int iLocationId, int iLocationTypeId) {
		List<RegisterDomain> listBlockedUsers = cd.getBlockedUsers(iLocationId,iLocationTypeId);
		return listBlockedUsers;
	}
	
	@Override
	public List<RegisterDomain> getUsernamesOfBlockedUsers(String stEmail_p) {
		List<RegisterDomain> lrd = cd.getUsernamesOfBlockedUsers(stEmail_p);
		return lrd;
	}
	
	@Override
	public String unblockedUser(String stEmail, String stUsername_p,int userId) {
		String stReturnMsg = "error";
		int iError = 0;
		if(stEmail.equals("0")){
			iError = cd.clearAttempts(stUsername_p,userId);
		}
		else{
			iError = cd.clearAttempts(stEmail, stUsername_p,userId);
		}
		if(iError==1){
			stReturnMsg = "Success";
		}
		return stReturnMsg;
	}

	@Override
	public String resendVerificationLink(String stEmail, String stUsername_p,int userId) throws MessagingException {
		String stResponse = "Verification Link Sent";
		//Generate SUT
		String stSUT = rps.genRandom(20, 2);
		//Store SUT
		fpd.storeSUT(stUsername_p, stSUT,userId);
		String stURL = "http://"+ResourceBundleFile.getValueFromKey("BASE_URL")+"/MANAK/verify?sut="+stSUT;
		String emailMsgTxt = "Dear "+stUsername_p+"<br/><br/>Kindly click the below URL to Verify. If you can't click the link, copy and paste the below URL on your web browser.<br/><br/>"+ stURL;
		//SendMail sm = new SendMail(); commented by imran
		sm.TransferToMailServer(stEmail,"Register verfication",emailMsgTxt);
		return stResponse;
	}
	
	// method to get credential summry
		// Query is written in addApplicationAndLicenceDao ;
		@Override
		public List<HashMap<String, Object>> getCredentialSummry() {
			List<HashMap<String, Object>> credentialDetail = new ArrayList<HashMap<String,Object>>();
			
			List<Integer> branchIDs = new ArrayList<Integer>();
			branchIDs = addAppLicDao.getAllBranchIds();  // get list of availale branch
			System.out.println("branchIDs size" +branchIDs.size());		
			for(int i= 0; i<branchIDs.size();i++){
				HashMap<String,Object> branchSummry = new HashMap<String,Object>();
				int branchId =branchIDs.get(i) ;
				branchSummry =  addAppLicDao.getBranchSummry(branchId);
				credentialDetail.add(branchSummry);
			}
			HashMap<String,Object> allBranchSummry = new HashMap<String,Object>();			
			allBranchSummry =  addAppLicDao.getallBranchSummry();
			credentialDetail.add(allBranchSummry);
			
			
			System.out.println("credentialDetail size dao : " + credentialDetail.size());
			return credentialDetail;
		}
	//get crediential hallmarking
		@Override
		public List<HashMap<String, Object>> getCredentialSummryHM() {
			List<HashMap<String, Object>> credentialDetail12 = new ArrayList<HashMap<String,Object>>();
			
			List<Integer> branchIDs = new ArrayList<Integer>();
			branchIDs = addAppLicDao.getAllBranchIds();  // get list of availale branch
			System.out.println("branchIDs size" +branchIDs.size());		
			for(int i= 0; i<branchIDs.size();i++){
				HashMap<String,Object> branchSummry = new HashMap<String,Object>();
				int branchId =branchIDs.get(i) ;
				branchSummry =  addAppLicDao.getBranchSummryHM(branchId);
				credentialDetail12.add(branchSummry);
				System.out.println("brancid..."+branchId+"      -   "+i);
			}
			HashMap<String,Object> allBranchSummry = new HashMap<String,Object>();			
			allBranchSummry =  addAppLicDao.getallBranchSummryHM();
			credentialDetail12.add(allBranchSummry);
			
			
			System.out.println("credentialDetail size dao : " + credentialDetail12.size());
			return credentialDetail12;
		}

		@Override
		public List<RegisterDomain> getUser(String stUsername) {
			//  Auto-generated method stub
			List<RegisterDomain> userRecords = cd.getUser(stUsername);
			return userRecords;
		}

		@Override
		public List<HashMap<String, String>> getPcConsolidateRptData(int branch, int role_id, int UserId, String frmdate,String todate)
		
		{
			List<HashMap<String,String>>  listdata= new ArrayList<HashMap<String,String>>();
			List<Map<String, Object>> sampleLog = new ArrayList();
			
			sampleLog = cd.getPcConsolidateData(branch,role_id,UserId,frmdate,todate);
			
			
			if (sampleLog != null && sampleLog.size() > 0) {
						
						for (int i = 0; i < sampleLog.size(); i++) {
							
							
							Map<String, Object> temp = sampleLog.get(i);
							HashMap hmDetails = new HashMap();			
						    hmDetails.put("branch_name",""+temp.get("str_branc_short_name"));	
						    
						    hmDetails.put("total_application",""+temp.get("total_application"));
						    hmDetails.put("total_draft_application",""+temp.get("total_draft_application"));
						    hmDetails.put("total_submitted_application",""+temp.get("total_submitted_application"));
						    hmDetails.put("total_normal_submitted_application",""+temp.get("total_normal_submitted_application"));
						    hmDetails.put("total_simplified_submitted_application",""+temp.get("total_simplified_submitted_application"));
						    hmDetails.put("Final_test_request_on_Application",""+temp.get("Final_test_request_on_Application"));
						    hmDetails.put("Test_report_uploaded_by_BIS_LAB_on_application",""+temp.get("Test_report_uploaded_by_BIS_LAB_on_application"));
						    hmDetails.put("Test_report_uploaded_by_OSL_LAB_on_application",""+temp.get("Test_report_uploaded_by_OSL_LAB_on_application"));
						    
						    hmDetails.put("online_payment_collection",""+temp.get("online_payment_collection"));
						    
						    hmDetails.put("total_license_granted",""+temp.get("total_license_granted"));
						    hmDetails.put("operative_license_as_on_date",""+temp.get("operative_license_as_on_date"));
						    hmDetails.put("deferred_license_as_on_date",""+temp.get("deferred_license_as_on_date"));
						    hmDetails.put("cancelled_license_as_on_date",""+temp.get("cancelled_license_as_on_date"));
						    hmDetails.put("expired_license_as_on_date",""+temp.get("expired_license_as_on_date"));
						    
						    hmDetails.put("Final_test_request_on_License",""+temp.get("Final_test_request_on_License"));						  
						    hmDetails.put("Test_report_uploaded_by_BIS_LAB_on_license",""+temp.get("Test_report_uploaded_by_BIS_LAB_on_license"));
						    hmDetails.put("Test_report_uploaded_by_OSL_LAB_on_license",""+temp.get("Test_report_uploaded_by_OSL_LAB_on_license"));
						  
						    hmDetails.put("inclusion_requested_submitted",""+temp.get("inclusion_requested_submitted"));
						    hmDetails.put("inclusion_granted",""+temp.get("inclusion_granted"));
						    
						    hmDetails.put("renewal_requested_submitted",""+temp.get("renewal_requested_submitted"));
						    hmDetails.put("renewal_granted",""+temp.get("renewal_granted"));
						    
						   
						    hmDetails.put("under_stop_marking_as_on_date",""+temp.get("under_stop_marking_as_on_date"));
						    hmDetails.put("under_self_stop_marking_as_on_date",""+temp.get("under_self_stop_marking_as_on_date"));
						  
						    
						    
						    System.out.println(""+temp.get("online_payment_collection"));
						   							
						    listdata.add(hmDetails);
							}
						
						//System.out.println("::::query excured");
					
					
					
				}
			else
				listdata=null;

			return listdata;


}
		
		@Override
		public List<Lab_Type_Domain> getLabTypeList() {
			return cd.getLabTypeList();
		}
		
		//@Override
		//public List<Branch_Master_Domain> getBo() {
		//	List<Branch_Master_Domain> bls = cd.getBo(); 			
		//	return bls;
		//}
		
		@Override
		public List<HashMap<String, String>> getBo() {
			List branchList = new ArrayList();		
			List<HashMap<String, String>> finalBranchList = new ArrayList<HashMap<String, String>>();
			
			branchList=cd.getBo();
			
			if (branchList != null && branchList.size() > 0) {
				for (int i = 0; i < branchList.size(); i++) {
					HashMap hmDetails = new HashMap();
					Object[] obj = (Object[]) branchList.get(i);
					if (obj[0] != null && !obj[0].equals("")) {
						hmDetails.put("branchId", "" + obj[0]);
					} else {
						hmDetails.put("branchId", "");
					}
					if (obj[1] != null && !obj[1].equals("")) {
						hmDetails.put("shortBranchNm", "" + obj[1]);
					} else {
						hmDetails.put("shortBranchNm", "");
					}
					finalBranchList.add(hmDetails);
				}
			}
			return finalBranchList;
			
		}
		
		@Override
		public List<HashMap<String, String>> getLabName(String labId) {
			List labList = new ArrayList();
			
			List<HashMap<String, String>> finalLsbList = new ArrayList<HashMap<String, String>>();
			
			labList=cd.getLabName(labId);
			
			if (labList != null && labList.size() > 0) {
				for (int i = 0; i < labList.size(); i++) {
					HashMap hmDetails = new HashMap();
					Object[] obj = (Object[]) labList.get(i);
						if (obj[0] != null && !obj[0].equals("")) {
							hmDetails.put("labName", "" + obj[0]);
						} else {
							hmDetails.put("labName", "");
						}
						if (obj[1] != null && !obj[1].equals("")) {
							hmDetails.put("labCode", "" + obj[1]);
						} else {
							hmDetails.put("labCode", "");
						}
						if (obj[2] != null && !obj[2].equals("")) {
							hmDetails.put("labAdd1", "" + obj[2]);
						} else {
							hmDetails.put("labAdd1", "");
						}
						if (obj[3] != null && !obj[3].equals("")) {
							hmDetails.put("labAdd2", "" + obj[3]);
						} else {
							hmDetails.put("labAdd2", "");
						}
						if (obj[4] != null && !obj[4].equals("")) {
							hmDetails.put("labCity", "" + obj[4]);
						} else {
							hmDetails.put("labCity", "");
						}
						if (obj[5] != null && !obj[5].equals("")) {
							hmDetails.put("labState", "" + obj[5]);
						} else {
							hmDetails.put("labState", "");
						}
						if (obj[6] != null && !(obj[6]==(Object)0)) {
							hmDetails.put("labPin", "" + obj[6]);
						} else {
							hmDetails.put("labPin", "");
						}
					finalLsbList.add(hmDetails);
				}
			}
			return finalLsbList;
			
		}
		
		@Override
		public List<HashMap<String, String>> getBISLabNameFrHead(String labId) {
			List labList = new ArrayList();
			
			List<HashMap<String, String>> finalLsbList = new ArrayList<HashMap<String, String>>();
			
			labList=cd.getBISLabNameFrHead(labId);
			
			if (labList != null && labList.size() > 0) {
				for (int i = 0; i < labList.size(); i++) {
					HashMap hmDetails = new HashMap();
					Object[] obj = (Object[]) labList.get(i);
						if (obj[0] != null && !obj[0].equals("")) {
							hmDetails.put("labName", "" + obj[0]);
						} else {
							hmDetails.put("labName", "");
						}
						if (obj[1] != null && !obj[1].equals("")) {
							hmDetails.put("labCode", "" + obj[1]);
						} else {
							hmDetails.put("labCode", "");
						}
						if (obj[2] != null && !obj[2].equals("")) {
							hmDetails.put("labAdd1", "" + obj[2]);
						} else {
							hmDetails.put("labAdd1", "");
						}
						if (obj[3] != null && !obj[3].equals("")) {
							hmDetails.put("labAdd2", "" + obj[3]);
						} else {
							hmDetails.put("labAdd2", "");
						}
						if (obj[4] != null && !obj[4].equals("")) {
							hmDetails.put("labCity", "" + obj[4]);
						} else {
							hmDetails.put("labCity", "");
						}
						if (obj[5] != null && !obj[5].equals("")) {
							hmDetails.put("labState", "" + obj[5]);
						} else {
							hmDetails.put("labState", "");
						}
						if (obj[6] != null && !(obj[6]==(Object)0)) {
							hmDetails.put("labPin", "" + obj[6]);
						} else {
							hmDetails.put("labPin", "");
						}
					finalLsbList.add(hmDetails);
				}
			}
			return finalLsbList;
			
		}
		
		
}
