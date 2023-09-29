package Applicant.Service;

/***************************Start of program*****************************\
## Copyright Information		: C-DAC, Noida  
## Project Name					: Integrated Web Portal for BIS
## Name of Developer		 	: Siddharth
## Module Name					: Applicant
## Process/Database Object Name	: BIS_dev
## Purpose						: Different function for getting listing pages for applicant
## Date of Creation				: 01/08/15
## Modification Log				:				
## Modify Date					: 22/01/16
## Reason(CR/PRS)				: added letter linking option on listing page
## Modify By					: Siddharth
*/

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Applicant.DAO.ApplicantDAO;
import Applicant.Domain.appliChngNameDomain;
import Applicant.Model.ApplicantModel;
import Applicant.Model.applicantChngNameModel;
import Global.CommonUtility.ResourceBundleFile;
import Global.CommonUtility.DAO.DaoHelper;
import Global.CommonUtility.DAO.GlobalDao;
import Global.CommonUtility.Domain.TextEditor_Dom;
import Global.Login.DAO.LoginDao;
import Global.Login.Service.IMigrateService;
import Global.Registration.Domain.RegisterDomain;
import Masters.Domain.LotInspection_IStandard_Domain;
import Masters.Domain.standard_mst_domain;
import Schemes.HallMarking.AHC.ApplicationSubmission.Domain.AHC_LOI_Domain;
import Schemes.HallMarking.Jeweller.ApplicationSubmission.DAO.Application_HallMarking_Dao;
import Schemes.HallMarking.Jeweller.ApplicationSubmission.DAO.Application_Hall_Jeweller_Branchdtl_Dao;
import Schemes.HallMarking.Jeweller.ApplicationSubmission.Domain.Application_HallMarking_Domain;
import Schemes.HallMarking.Jeweller.ApplicationSubmission.Domain.Application_HallMarking_Domain_corporateConversion;
import Schemes.HallMarking.Jeweller.ApplicationSubmission.Domain.Hall_Branchdtl_Domain;
import Schemes.HallMarking.Jeweller.ApplicationSubmission.Domain.Hall_Branchdtl_Domain_corporateConversion;
import Schemes.HallMarking.Jeweller.ApplicationSubmission.Domain.Hall_firmDtlsDomain;
import Schemes.HallMarking.Jeweller.ApplicationSubmission.Domain.Hall_firmDtlsDomain_corporateConversion;
import Schemes.HallMarking.Jeweller.ApplicationSubmission.Service.Application_HallMarking_Service;
import Schemes.HallMarking.Jeweller.ApplicationSubmission.Service.Application_Hall_Jeweller_Branchdtl_Service;
import Schemes.HallMarking.Jeweller.HMO.Domain.HMJewApplicationTrackingDomain;
import Schemes.HallMarking.Jeweller.HMO.Model.JewHeadModel;
import Schemes.HallMarking.Jeweller.LicenceRenewal.Domain.LicenceRenewDomain;
import Schemes.HallMarking.Jeweller.LicenceRenewal.Service.LicenceRenewal_Service;
import Schemes.ProductCertification.ApplicationSubmission.Domain.Licence_Brand_Detail_Domain;
import Schemes.ProductCertification.ApplicationSubmission.Domain.PcGOLDomain;
import Schemes.ProductCertification.ApplicationSubmission.Domain.firmDtlsDomain;
import Schemes.ProductCertification.ApplicationSubmission.Domain.user_profile_domain;
import Schemes.ProductCertification.ApplicationSubmission.Service.applicationSubmissionService;
import Schemes.ProductCertification.BO.DAO.RcvdApplicationDODao;
import Schemes.ProductCertification.BO.Domain.PCApplicationTrackingDomain;
import Schemes.ProductCertification.BO.Domain.PCCMLLotInspectionTracking;
import Schemes.ProductCertification.Inspection.Service.planInspectionService;
import Schemes.ProductCertification.LicenceGranting.Domain.Inclusion_Tracking_Domain;
import payment.PC.Service.InitiateFeePCApplicantService;
@SuppressWarnings({"unused","unchecked","rawtypes","deprecation"})
@Service
public class ApplicantServiceImpl implements IApplicantService {

	@Autowired
	applicationSubmissionService appSubService;
	@Autowired
	InitiateFeePCApplicantService service;
	@Autowired
	Application_HallMarking_Dao appHallMarkDao;
	
	@Autowired
	Application_Hall_Jeweller_Branchdtl_Dao appHallMarkbranchDao;
	
	@Autowired
	ApplicantDAO appdao;
	
	@Autowired
	RcvdApplicationDODao DaoObj;
	
	@Autowired
	LoginDao ldao;
	
	@Autowired
	IMigrateService ims;
	
	@Autowired
	GlobalDao gd;
	
	@Autowired
	DaoHelper daoHelper;
	
	@Autowired
	LicenceRenewal_Service LR_Service;
	
	@Autowired
	Application_HallMarking_Service AppHallMarkService;
	
	@Autowired
	Application_Hall_Jeweller_Branchdtl_Service appHMJBD;

	@Autowired
	planInspectionService piServ;

	
	String globalPath = ResourceBundleFile.getValueFromKey("DOCUPLOADPATH");
	static SimpleDateFormat smdate = new SimpleDateFormat("dd/MM/yyyy");
	static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	static SimpleDateFormat smTime = new SimpleDateFormat("hh:mm a");
	static SimpleDateFormat abc = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	public List<HashMap> getApplications(int iUserId_p, int iRoleId_p) {
		List<HashMap> listhmApplicantApps = new ArrayList<HashMap>();
		List<HashMap<String,String>> listAction = new ArrayList<HashMap<String,String>>();
		List applicantapps = appdao.getApplications(iUserId_p);
		if(applicantapps.size()>0){
			for(int i=0; i<applicantapps.size();i++){
				HashMap hm = new HashMap();
				Object [] obj = (Object []) applicantapps.get(i);
				if(obj.length==19){
					if(obj[0]!=null && !obj[0].equals("")){
						String appid = ""+obj[0];
						String EAppid = ims.Jcrypt(appid);
						hm.put("appId", ""+obj[0]);
						hm.put("EappId", EAppid);
					}
					else{
						hm.put("appId", "--");
						hm.put("EappId", "--");
					}

					if(obj[1]!=null && !obj[1].equals("")){
						String dt1="";
						String dt = obj[1].toString();
						String ab = obj[1].toString();
						try {
							dt1 = abc.format(formatter.parse(dt));
						} 
						catch (ParseException e) {
							e.printStackTrace();
						}
						hm.put("submittedDate", ""+dt1);
					}
					else{
						hm.put("submittedDate", "--");
					}
					if(obj[2]!=null && !obj[2].equals("")){
						hm.put("ISNo", ""+obj[2]);
					}
					else{
						hm.put("ISNo", "--");
					}
					
					
					if(obj[3]!=null && !obj[3].equals("")){
						hm.put("FirmName", ""+obj[3]);
					}
					else{
						hm.put("FirmName", "--");
					}
					if(obj[5]!=null && !obj[5].equals("")){
						hm.put("WStatus", ""+obj[5]);
					}
					else{
						hm.put("WStatus", "--");
					}
					if(obj[4]!=null && !obj[4].equals("")){
						hm.put("status", ""+obj[4]);
					}
					else{
						hm.put("status", "--");
					}
					if(obj[4]!=null && !obj[4].equals("")){
						int procedure = Integer.parseInt(obj[8].toString());
						hm.put("proc_id", procedure);
						int statusId= Integer.parseInt(obj[4].toString());
						listAction = DaoObj.getActionList(iRoleId_p,statusId,procedure);
						if(listAction != null && listAction.size()>0)
						{
							hm.put("ListAction",listAction);
						}
						else
						{
							hm.put("ListAction", "-");
						}
					}
					if(obj[6] != null && !obj[6].equals(""))
					{
							String inspecdate = "";
							try {
								inspecdate = abc.format(formatter.parse(obj[6].toString()));
							} catch (ParseException e1) {
								e1.printStackTrace();
							}
							hm.put("lastDate", inspecdate);
					}
					else
					{
						
							hm.put("lastDate", null);
					}
					if(obj[4]!=null && !obj[4].equals("")){
						hm.put("actionstatus", ""+obj[4]);
						hm.put("Eactionstatus", ims.Jcrypt(""+obj[4]));
					}
					else{
						hm.put("actionstatus", "--");
						hm.put("Eactionstatus", ims.Jcrypt("--"));
					}

					if(obj[7] != null && !obj[7].equals(""))
					{
						String branchId = ""+obj[7];
						hm.put("branchId", branchId);
						String EbranchId = ims.Jcrypt(branchId);
						hm.put("EbranchId", EbranchId);
					}
					else{
						hm.put("EbranchId", "--");
						hm.put("branchId", "-1");
					}
					if(obj[9] != null && !obj[9].equals(""))
					{
						String countPendingFee = ""+obj[9];
						hm.put("countPendingFee", countPendingFee);
					}
					else{
						hm.put("countPendingFee", "--");
					}
					if(obj[10] != null && !obj[10].equals(""))
					{
						String count = ""+obj[10];
						int ab = Integer.parseInt(count);
						 
						if(ab >= 1)
						hm.put("clarify", "0");
						else
							hm.put("clarify", "1");	
					}
					else{
						hm.put("clarify", "0");
					}
					if(obj[11] != null && !obj[11].equals(""))
					{
						hm.put("lettername", ""+obj[11]);
					}
					else{
						hm.put("lettername", "");
					}
					if(obj[12] != null && !obj[12].equals(""))
					{
						hm.put("letterId", ""+obj[12]);
					}
					else{
						hm.put("letterId", "");
					}
					
					if(obj[13] != null && !obj[13].equals(""))
					{
						String recdate = "";
						try {
							Date dtReg = (Date) obj[13] ;
							SimpleDateFormat dtl =  new SimpleDateFormat("yyyy-MM-dd");
							recdate = dtl.format(dtReg);
							
							//recdate = smdate.format(formatter.parse(obj[13].toString()));
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						hm.put("recDate", recdate);
					}
					else
					{
						hm.put("recDate", null);
					}
					
					if(obj[14] != null && !obj[14].equals(""))
					{
						hm.put("procedure", obj[14]+"");
					}
					else
					{
						hm.put("procedure", "");
					}
					if(obj[15] != null && !obj[15].equals(""))
					{
						hm.put("ncstatus", obj[15]+"");
					}
					else
					{
						hm.put("ncstatus", "0");
					}
					if(obj[16] != null && !obj[16].equals(""))
					{
						hm.put("Tno", obj[16]+"");
						hm.put("eTno", ims.Jcrypt(obj[16]+""));
					}
					else
					{
						hm.put("Tno", "NA");
					}
					if(obj[17] != null && !obj[17].equals(""))
					{
						hm.put("receipt_no", obj[17]+"");
					}
					else
					{
						hm.put("receipt_no", "NA");
					}
					if(obj[18] != null && !obj[18].equals(""))
					{
						hm.put("insp", obj[18]+"");
					}
					else
					{
						hm.put("insp", "0");
					}
					
				
					String cmlno="0";
					
					/*ashish chg code*/
					
					String applicationid=(obj[0].toString());
					
					String survId=String.valueOf(appdao.getSurveillanceDetailsPc(applicationid,cmlno));
							
				    hm.put("survId", ims.Jcrypt(survId));
					
					Map<String,Object> flagMap= appdao.getSurveillanceFactoryTestReportFlag(applicationid,cmlno,survId);
					
					if(!flagMap.isEmpty()) {
						hm.put("survTestRptFlag", Integer.parseInt(flagMap.get("isfactoryTestingConducted").toString()));
						hm.put("survTestRptFlagMulti", Integer.parseInt(flagMap.get("isFactoryTestingConductedMulti").toString()));
					}
					else
					{
						hm.put("survTestRptFlag", 0 );
						hm.put("survTestRptFlag", 0);
					}
					
					
				}/*if object length=6*/
				listhmApplicantApps.add(hm);
			}/*for loop*/
		}/*if query size > 0*/
		
		return listhmApplicantApps;
	}
	
	
	
	@Override
public int countPendingFee(int userId){
int countPending=0;
List list1= appdao.getPendingFeeCount(userId);
countPending=Integer.parseInt((list1.get(0).toString()));
// 
return countPending;
}

	
	private List<String> getApplicationDateTime(int iUserId_p, int iStatusId_p){
		List<PcGOLDomain> listpcappdom = appdao.getRecentApplicationsData(iUserId_p, iStatusId_p);
		String stDate = "";
		String stTime = "";
		String stApplicationId = "";
		if(listpcappdom.size()>=1){
			PcGOLDomain pcappdom = listpcappdom.get(0);
			stApplicationId = pcappdom.getLnApplicationId();
			try{
				stDate = smdate.format(formatter.parse(pcappdom.getDtRegistration().toString()));
				stTime = smTime.format(formatter.parse(pcappdom.getDtRegistration().toString()));
			}
			catch (ParseException e) {
				e.printStackTrace();
			}
		}
		List<String> stDateTime = new ArrayList();
		stDateTime.add(stDate);
		stDateTime.add(stTime);
		stDateTime.add(stApplicationId);
		return stDateTime;
	}
	
	private int percentProfileCompletion(String stEmail_p){
		int iProfileCompleted = 0;
		List<RegisterDomain> listrdom = ldao.getUserPwd(stEmail_p);
		RegisterDomain rdom = listrdom.get(0);
		String stEmail = rdom.getEmail();
		int iUserId = rdom.getUser_id();			//User's Primary ID
		List<user_profile_domain> listprdom = appdao.getUserProfileDetails(iUserId);
		if(listprdom.size()!=0){
			int iSectionCompletedCount = 0;
			user_profile_domain prdom = listprdom.get(0);
			
			if(prdom.getStr_firm_email()!=null){
				iSectionCompletedCount += 1;
			}
			if(prdom.getStr_firm_mobile()!=null){
				iSectionCompletedCount += 1;
			}
			if(prdom.getStr_factory_email()!=null){
				iSectionCompletedCount += 1;
			}
			if(prdom.getStr_factory_mobile()!=null){
				iSectionCompletedCount += 1;
			}
			if(prdom.getStr_dic_reg_number()!=null){
				iSectionCompletedCount += 1;
			}
			
			if(prdom.getDt_dic_reg_date()!=null){
				iSectionCompletedCount += 1;
			}
			if(prdom.getNum_sector_id()!=0){
				iSectionCompletedCount += 1; //8
			}
			if(prdom.getNum_scale_id()!=0){
				iSectionCompletedCount += 1;
			}
			if(prdom.getStr_Reg_document_name()!=null){
				iSectionCompletedCount += 1;
			}
			if(prdom.getStr_firm_name()!=null){
				iSectionCompletedCount += 1;
			}
			if(prdom.getStr_firm_address_line1()!=null){
				iSectionCompletedCount += 1;
			}
			if(prdom.getNum_firm_country_id()!=0){
				iSectionCompletedCount += 1;
			}
			if(prdom.getNum_firm_state_id()!=0){
				iSectionCompletedCount += 1;
			}
			if(prdom.getNum__firm_district_id()!=0){
				iSectionCompletedCount += 1;
			}
			if(prdom.getStr_firm_city_name()!=null){
				iSectionCompletedCount += 1;
			}
			try {
			if(prdom.getStr_firm_address_document_name()!=null && !prdom.getNum_address_proof_firm_document_id().equals("0")){
				iSectionCompletedCount += 1;
			}
			}
			catch (Exception e) {
				
			}
//			if( prdom.getNum_address_proof_firm_document_id()!="0" || prdom.getNum_address_proof_firm_document_id()!=null ){
//				iSectionCompletedCount += 1;
//			}
		
			if(prdom.getStr_factory_address_line1()!=null){
				iSectionCompletedCount += 1;
			}
			if(prdom.getNum_factory_country_id()!=0){
				iSectionCompletedCount += 1;
			}
			if(prdom.getNum_factory_state_id()!=0){
				iSectionCompletedCount += 1;
			}
			if(prdom.getNum__factory_district_id()!=0){
				iSectionCompletedCount += 1;
			}
			if(prdom.getStr_factory_city_name()!=null){
				iSectionCompletedCount += 1;
			}
			try {
			if(prdom.getStr_factory_address_document_name()!=null && !prdom.getNum_address_proof_factory_document_id().equals("0") ){
				iSectionCompletedCount += 1;
			}
			}
			catch (Exception e) {
				
			}
//			if(prdom.getNum_address_proof_factory_document_id()!="0" || prdom.getNum_address_proof_factory_document_id()!=null){
//				iSectionCompletedCount += 1;
//			}
			if(prdom.getNum_reg_document_id()!=0){
				iSectionCompletedCount += 1;
			}
			if(prdom.getStr_firm_ceo_name()!=null){
				iSectionCompletedCount += 1; 
			}
			
			if(prdom.getNum_women_enterprenaur()!=null) {
				iSectionCompletedCount += 1; 
			}
			if(prdom.getNum_startup()!=null) {
				iSectionCompletedCount += 1; //26
			}
			
			//System.out.println("---iSectionCompletedCount--"+iSectionCompletedCount);
			iProfileCompleted = (iSectionCompletedCount*100/26);
			
			
				//iProfileCompleted = 100;
			
		}
		else{
			iProfileCompleted = 0;
		}
		return iProfileCompleted;
	}
	
	@Override
	public HashMap getApplicantDashboardData(int iUserId_p, String stEmail_p) {
		
		//Commented by Dhruv to remove unnecessary queries being run on Main Dashboard
		/*List submittedapps = appdao.getApplications(iUserId_p);
		int iCountsubmittedapps = submittedapps.size();*/
		
		int iCountsubmittedapps = appdao.getCountPCSubmittedApps(iUserId_p);

		//Commented by Dhruv to remove unnecessary queries being run on Main Dashboard
		/*licenceapps = appdao.getLicenseApps(iUserId_p);
		int iCountLicenceapps = licenceapps.size();*/
	
		int iCountLicenceapps = appdao.getCountPCOperativeLicences(iUserId_p);
		int iCountPendingfee=appdao.getFeePendingFlagForLicense(iUserId_p);
		int iCountPendingfeeApps = appdao.getFeePendingFlagForApps(iUserId_p);
		
		int iSavedApps = appdao.getCountofPCApplications(iUserId_p, 26,0);
		int iSavedCOCApps = appdao.getCountofPCApplicationsCOC(iUserId_p, 26,0);
		int iCountsubmittedappsCOC = appdao.getCountPCSubmittedAppsCOC(iUserId_p);
		int iCountLicenceappsCOC = appdao.getCountCOCOperativeLicences(iUserId_p);
		/*
		Getting the Counts of apps with different status Id
		int iSubmittedApps = appdao.getCountofPCApplications(iUserId_p, 1,1);
		int iNewApps = appdao.getCountofPCApplications(iUserId_p,27,0);
		int iSavedApps = appdao.getCountofPCApplications(iUserId_p, 26,0);
		int iGOLApps = appdao.getCountofPCApplications(iUserId_p, 20,0);
		int iRecordedApps = appdao.getCountofPCApplications(iUserId_p, 4,0);
		int iInspectionFinalizedApps = appdao.getCountofPCApplications(iUserId_p, 62,0);
		
		Getting the Application ID, Registered date and Last Modified time
		//For All Submitted apps
		String stSubmittedDate = getApplicationDateTime(iUserId_p,-1).get(0);
		String stSubmittedTime = getApplicationDateTime(iUserId_p,-1).get(1);
		String stSubmittedApp = getApplicationDateTime(iUserId_p,-1).get(2);
		//For Saved Applications
		String stSavedDate = getApplicationDateTime(iUserId_p,26).get(0);
		String stSavedTime = getApplicationDateTime(iUserId_p,26).get(1);
		String stSavedApp = getApplicationDateTime(iUserId_p,26).get(2);
		//For New Applications
		String stNewDate = getApplicationDateTime(iUserId_p,27).get(0);
		String stNewTime = getApplicationDateTime(iUserId_p,27).get(1);
		String stNewApp = getApplicationDateTime(iUserId_p,27).get(2);
		//For GOL Applications
		String stGOLDate = getApplicationDateTime(iUserId_p,20).get(0);
		String stGOLTime = getApplicationDateTime(iUserId_p,20).get(1);
		String stGOLApp = getApplicationDateTime(iUserId_p,20).get(2);
		//For Recorded Applications
		String stRecordedDate = getApplicationDateTime(iUserId_p,4).get(0);
		String stRecordedTime = getApplicationDateTime(iUserId_p,4).get(1);
		String stRecordedApp = getApplicationDateTime(iUserId_p,4).get(2);
		//For Inspection Finalized Applications
		String stInsFinalDate = getApplicationDateTime(iUserId_p,62).get(0);
		String stInsFinalTime = getApplicationDateTime(iUserId_p,62).get(1);
		String stInsFinalApp = getApplicationDateTime(iUserId_p,62).get(2);*/
		
		
		/*Getting % Profile Completion*/
		int iPercentCompletion = percentProfileCompletion(stEmail_p);
		
		HashMap hm = new HashMap();
		
		hm.put("submitted", iCountsubmittedapps);
		hm.put("saved", iSavedApps);
		hm.put("licence", iCountLicenceapps);
		hm.put("iCountPendingfee", iCountPendingfee);
		hm.put("iCountPendingfeeApps", iCountPendingfeeApps);
		hm.put("iSavedCOCApps", iSavedCOCApps);
		hm.put("iCountsubmittedappsCOC", iCountsubmittedappsCOC);
		hm.put("iCountLicenceappsCOC", iCountLicenceappsCOC);
		/*
		Putting the counts in the HashMap
		hm.put("submitted", ""+iSubmittedApps);
		hm.put("saved", ""+iSavedApps);
		hm.put("NewApps", ""+iNewApps);
		hm.put("gol", ""+iGOLApps);
		hm.put("recorded", ""+iRecordedApps);
		hm.put("inspectionfinalized", ""+iInspectionFinalizedApps);
		
		Putting the date and time in the HashMap
		hm.put("submitteddt", stSubmittedDate);
		hm.put("submittedtm", stSubmittedTime);
		hm.put("submittedapp", stSubmittedApp);
		hm.put("saveddt", stSavedDate);
		hm.put("savedtm", stSavedTime);
		hm.put("savedapp", stSavedApp);
		hm.put("newdt", stNewDate);
		hm.put("newtm", stNewTime);
		hm.put("newapp", stNewApp);
		hm.put("goldt", stGOLDate);
		hm.put("goltm", stGOLTime);
		hm.put("golapp", stGOLApp);
		hm.put("recordeddt", stRecordedDate);
		hm.put("recordedtm", stRecordedTime);
		hm.put("recordedapp", stRecordedApp);
		hm.put("inspdt", stInsFinalDate);
		hm.put("insptm", stInsFinalTime);
		hm.put("inspapp", stInsFinalApp);*/
		
		//Putting % Completion in the HashMap
		hm.put("percent", iPercentCompletion);
		
		return hm;
	}

	@Override
	public List<HashMap<String,String>> getSavedApplications(int iUserId_p) {
		
		List<HashMap<String,String>> listhmsaveddetails = new ArrayList<HashMap<String,String>>();
		List<PcGOLDomain> listpcappdom = appdao.getSavedApplications(iUserId_p);
		if(listpcappdom.size()>0){
			
			for(int i=0;i<listpcappdom.size();i++){
				HashMap<String,String> hm = new HashMap<String, String>();
				PcGOLDomain pcappdom = listpcappdom.get(i);
				String stApplicationDt = "";
				try{
					stApplicationDt = smdate.format(formatter.parse(pcappdom.getDtRegistration().toString()));
				}
				catch (ParseException e) {
					e.printStackTrace();
				}
				String stAppId = pcappdom.getLnApplicationId();
				String stEncAppid = ims.Jcrypt(pcappdom.getLnApplicationId());
				int tempiBranchId = pcappdom.getIntBranchId();
				String iBranchId = ims.Jcrypt(tempiBranchId+"");
				String stAppUrl = "PCApplicationSubmission?appId="+stEncAppid+"&branchId="+iBranchId;
				String stIsNo = pcappdom.getStrISno();
				int iProcedureId = pcappdom.getIntProcedureId();
				String stProcedureName = "-";
				if(iProcedureId!=0){
					stProcedureName = appdao.getProcedureName(iProcedureId);
				}
				
				firmDtlsDomain firmdtalsobj = appSubService.getFirmByAppid(stAppId,tempiBranchId);
				String stFirmName = firmdtalsobj.getStrFirmName();
				
				
				hm.put("AppId", stAppId);
				hm.put("procedure", stProcedureName);
				hm.put("ISNo", stIsNo);
				hm.put("ISyear", Integer.toString(pcappdom.getNumStdYear()));
				hm.put("FirmName", stFirmName);
				hm.put("AppDt", stApplicationDt);
				hm.put("AppUrl", stAppUrl);
				/*hm.put(stApplicationDt, stAppUrl);*/
				listhmsaveddetails.add(hm);
			}
		
		}
		return listhmsaveddetails;
	}
	
	
	@Override
	public List<HashMap<String,String>> getSavedHMApplications(int iUserId_p,String hmType) {
		
		List<HashMap<String,String>> listhmsaveddetails = new ArrayList<HashMap<String,String>>();
		if(hmType.equalsIgnoreCase("HMAH")){
		////////////////////////Get Saved Details for Hallmarking AHC LOI Application 
		List<AHC_LOI_Domain> listhmahappdom = appdao.getSavedApplicationsHMAH(iUserId_p);
		if(listhmahappdom.size()>0){
			
			for(int i=0;i<listhmahappdom.size();i++){
				HashMap<String,String> hm = new HashMap<String, String>();
				AHC_LOI_Domain ahcloidomObj = listhmahappdom.get(i);
				
				String stAppId = ahcloidomObj.getStr_application_id();
				String stEncAppid = ims.Jcrypt(ahcloidomObj.getStr_application_id());
				int tempiBranchId = ahcloidomObj.getNum_branch_id();
				String iBranchId = ims.Jcrypt(ahcloidomObj.getNum_branch_id()+"");
				String stAppUrl = "AHCLOI?appId="+stEncAppid+"&branchId="+iBranchId;
				
				String proAHCName=ahcloidomObj.getStrAHCName();
				String applicantName=ahcloidomObj.getStrApplicantName();
				
				hm.put("AppId", stAppId);
				hm.put("proAHCName", proAHCName);
				hm.put("applicantName",applicantName);
				hm.put("AppUrl", stAppUrl);
				
				listhmsaveddetails.add(hm);
			}
		
		}
		}
		else if(hmType.equalsIgnoreCase("HMJW")){
			List listhmjwappdom=appdao.getSavedApplicationsHMJW(iUserId_p);
			String stAppId="";
			String stEncAppid="";
			int tempiBranchId=0;
			String iBranchId="";
			String stAppUrl="Application_HallMarking";
		
				
			if(listhmjwappdom != null && listhmjwappdom.size() > 0)
			{
				for(int i=0;i<listhmjwappdom.size();i++)
				{
					HashMap hmDetails = new HashMap();
					Object [] obj = (Object [])listhmjwappdom.get(i);
					
			
					if(obj.length > 0)
					{
						if(obj[0] != null && !obj[0].equals(""))
						{
							 stAppId =obj[0].toString();
							 stEncAppid = ims.Jcrypt(obj[0].toString());
							hmDetails.put("AppId",""+stAppId);
							hmDetails.put("EappId", ""+stEncAppid);
						}
						else
						{
							hmDetails.put("AppId","--");
							hmDetails.put("EappId", "--");
						}
						
						if(obj[1] != null && !obj[1].equals(""))
						{
							tempiBranchId =Integer.parseInt(obj[1].toString());
							iBranchId = ims.Jcrypt(tempiBranchId+"");
							hmDetails.put("branchId",""+tempiBranchId);
							hmDetails.put("EbranchId", ""+iBranchId);
						}
						else
						{
							hmDetails.put("branchId","--");
							hmDetails.put("EbranchId", "--");
						}
											
						if(obj[2] != null && !obj[2].equals(""))
						{
							
							hmDetails.put("appFor",""+obj[2].toString());
							
						}
						else
						{
							hmDetails.put("appFor","--");
						}
						
						if(obj[3] != null && !obj[3].equals(""))
						{
							
							hmDetails.put("firmName",""+obj[3].toString());
							
						}
						else
						{
							hmDetails.put("firmName","--");
						}
						if(obj[4] != null && !obj[4].equals(""))
						{
							
							hmDetails.put("strOutletType",""+obj[4].toString());
							
						}
						else
						{
							hmDetails.put("strOutletType","--");
						}
						if(obj[5] != null && !obj[5].equals(""))
						{
							
							hmDetails.put("numOutletNo",""+obj[5].toString());
							
						}
						else
						{
							hmDetails.put("numOutletNo","--");
						}
						
						if(!obj[0].equals("") && !obj[1].equals(""))
						{
							stAppUrl = "Application_HallMarking?Eapp_id="+stEncAppid+"&Ebranchid="+iBranchId;
							hmDetails.put("AppUrl",""+stAppUrl);
						}
						else{
							hmDetails.put("AppUrl",""+stAppUrl);
						}
					}
					listhmsaveddetails.add(hmDetails);
				}
			}
			
		}
		return listhmsaveddetails;
	}

	@Override
	public List<HashMap> getLicenseApps(int userid,int roleId){
		List<HashMap> listhmRcvdApplication = new ArrayList<HashMap>();
		List<HashMap<String,String>> listAction = new ArrayList<HashMap<String,String>>();
		List listApplication = new ArrayList();
		listApplication = appdao.getLicenseApps(userid);
		
		
		if(listApplication != null && listApplication.size() > 0)
		{
			for(int i=0;i<listApplication.size();i++)
			{
				HashMap hmDetails = new HashMap();
				Object [] obj = (Object [])listApplication.get(i);
				if(obj.length > 0)
				{
					if(obj[0] != null && !obj[0].equals(""))
					{
						hmDetails.put("cml_no", ""+obj[0]);
						hmDetails.put("ecml_no", ""+ims.Jcrypt(obj[0].toString()));
						if(appdao.checkConsigneeUploadForQuarter((String) obj[0])) {
							
							hmDetails.put("submitLatestConsigneeDetails", 1);
							hmDetails.put("eDocTypeId",ims.Jcrypt("5")); 

						}else {
							hmDetails.put("submitLatestConsigneeDetails", 0);

						}
						
						int day=appdao.getdaysaftervaliditydate((String) obj[0]);
						System.out.println("days in qwerty"+day);
						if(day > 90) {
							hmDetails.put("linkvisible", 0);
						}
						else {
							hmDetails.put("linkvisible", 1);
						}
						
						
					}
					else
					{
						hmDetails.put("cml_no", "--");
					}
					if(obj[1] != null && !obj[1].equals(""))
					{
						hmDetails.put("firm_name", ""+obj[1]);
					}
					else
					{
						hmDetails.put("firm_name", "--");
					}					
					if(obj[2] != null && !obj[2].equals(""))
					{
						hmDetails.put("app_id", ""+obj[2]);
						hmDetails.put("eapp_id", ""+ims.Jcrypt(obj[2].toString()));
					}
					else
					{
						hmDetails.put("app_id", "--");
					}
					if(obj[3] != null && !obj[3].equals(""))
					{
						String dt = null;
						String dateSample = ""+obj[3].toString();

					   // String newFormat = "dd/MM/yyyy";
					    String oldFormat = "yyyy-MM-dd";

					    SimpleDateFormat sdf1 = new SimpleDateFormat(oldFormat);
					    //SimpleDateFormat sdf2 = new SimpleDateFormat(newFormat);
					    try {
					       dt=sdf1.format(sdf1.parse(dateSample));

					    } catch (ParseException e) {
					        // TODO Auto-generated catch block
					        e.printStackTrace();
					    }
						hmDetails.put("granted_date", dt);
					}
					else
					{
						hmDetails.put("granted_date", "--");
					}
					if(obj[4] != null && !obj[4].equals(""))
					{
						hmDetails.put("branch_id", ""+obj[4]);
						hmDetails.put("ebranch_id", ""+ims.Jcrypt(obj[4].toString()));
					}
					else
					{
						hmDetails.put("branch_id", "--");
					}
					if(obj[5] != null && !obj[5].equals(""))
					{
						hmDetails.put("AppStatus", ""+obj[5]);
						int procedure = Integer.parseInt(obj[8].toString());
						int statusId= Integer.parseInt(obj[5].toString());
						listAction = DaoObj.getActionList(roleId,statusId,procedure);
						if(listAction != null && listAction.size()>0)
						{
							hmDetails.put("ListAction",listAction);
						}
						else{
							hmDetails.put("ListAction", "-");
						}
						
					}
					else
					{
						
						hmDetails.put("AppStatus", "--");
					}
					
					
					List<PCCMLLotInspectionTracking> theLotTrackingList = piServ.getTheStatusFromLotTracking(obj[0].toString());  
					if(theLotTrackingList.size() == 0){
						hmDetails.put("lotSwow", "1");
					}
					
					if(theLotTrackingList.size()>0){
							if(theLotTrackingList.get(0).getNum_app_status() !=534){
								hmDetails.put("lotSwow", "0");
							}else{
								hmDetails.put("lotSwow", "1");
							}
					}
					
					
					if(obj[6] != null && !obj[6].equals(""))
					{
						hmDetails.put("applicant_status", ""+obj[6]);
					}
					else
					{
						hmDetails.put("applicant_status", "--");
					}
					
					if(obj[7] != null && !obj[7].equals(""))
					{
						
						SimpleDateFormat sdfDB = new SimpleDateFormat("yyyy-MM-dd");
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
						
						String dt = null;
						String dateSample = ""+obj[7].toString();

					    //String newFormat = "dd/MM/yyyy";
					    String oldFormat = "yyyy-MM-dd";

					    SimpleDateFormat sdf1 = new SimpleDateFormat(oldFormat);
					  //  SimpleDateFormat sdf2 = new SimpleDateFormat(newFormat);
					    try {
					    	Date renewal = sdfDB.parse(""+obj[7]);
							Date currDt = new Date();
							int period = getMonthsDifference(currDt,renewal);
							if(period <= 3)
							{
								hmDetails.put("period", "1");
							}
							else
							{
								hmDetails.put("period", "0");
							}
					       dt=sdf1.format(sdf1.parse(dateSample));

					    } catch (ParseException e) {
					        e.printStackTrace();
					    }
						hmDetails.put("validityDate", dt);
					}
					else
					{
						hmDetails.put("validityDate", "--");
						hmDetails.put("period", null);
					}
					
					
					if(obj[9] != null && !obj[9].equals(""))
					{
						hmDetails.put("isno", ""+obj[9]);
					}
					else{
						hmDetails.put("isno", "");
					}
					
					if(obj[10] != null && !obj[10].equals(""))
					{
						hmDetails.put("som_status", ""+obj[10]);
					}
					else{
						hmDetails.put("som_status", "0");
					}
					if(obj[11] != null && !obj[11].equals(""))
					{
						hmDetails.put("self_som_id", ""+obj[11]);
					}
					else{
						hmDetails.put("self_som_id", "0");
					}
					if(obj[12] != null && !obj[12].equals(""))
					{
						hmDetails.put("rom_id", ""+obj[12]);
					}
					else{
						hmDetails.put("rom_id", "0");
					}
					
					if(obj[13] != null && !obj[13].equals(""))
					{
						hmDetails.put("NC", "1");
					}
					else
					{
						hmDetails.put("NC", "0");
					}
					System.out.println("***********obj[13]*****************"+obj[13]);
					String countLicPendingFee=service.getInitiateFeePCApplicant(obj[2]+"", obj[4]+"").size()+"";
					hmDetails.put("countLicPendingFee", ""+countLicPendingFee);
//					if(obj[14] != null && !obj[14].equals(""))
//					{
//						String countLicPendingFee = ""+obj[14];
//						hmDetails.put("countLicPendingFee", ""+countLicPendingFee);
//					}
//					else
//					{
//						hmDetails.put("countLicPendingFee", "--");
//					}
					System.out.println("***********obj[14]*****************"+obj[14]);
					System.out.println("***********obj[15]*****************"+obj[15]);
					if(obj[15] != null && !obj[15].equals(""))
					{
						hmDetails.put("inclusion", "1");
					}
					else
					{
						hmDetails.put("inclusion", "0");
					}
					
					System.out.println("***********obj[16]*****************"+obj[16]);
					if(obj[16] != null && !obj[16].equals(""))
					{
						hmDetails.put("renewalp", "1");
					}
					else{
						hmDetails.put("renewalp", "0");
					}
					
					if(obj[17] != null && !obj[17].equals(""))
					{
						hmDetails.put("romtrackstatus", obj[17]+"");
					}
					else{
						hmDetails.put("romtrackstatus", "0");
					}
					
					if(obj[18] != null && !obj[18].equals(""))
					{
						hmDetails.put("lotrackstatus", obj[18]+"");
					}
					else{
						hmDetails.put("lottrackstatus", "0");
					}
					//mansi
					if(obj[19] != null && !obj[19].equals(""))
					{
						hmDetails.put("renewalStatusApplicatView", obj[19]+"");
					}
					else{
						hmDetails.put("renewalStatusApplicatView", "0");
					}
					//mansi
					if(obj[20] != null && !obj[20].equals(""))
					{
						hmDetails.put("num_renewal_tracking", obj[20]+"");
					}
					else{
						hmDetails.put("num_renewal_tracking", "0");
					}
					System.out.println("***********obj[19]**********"+obj[19]);
					System.out.println("***********obj[20]**********"+obj[20]);
				/*List<inclusionVarietyDomain> theInclusionData = appdao.getTheInclusionData(""+obj[0], Integer.parseInt(""+obj[4]));
				if(theInclusionData.size()>0){
					for(int x=0;x<theInclusionData.size();x++){
						if(theInclusionData.get(x).getStr_status_id()!=null){
							if(theInclusionData.get(x).getStr_status_id().equals("26")){
							appdao.deleteFromInclusion(theInclusionData.get(x));
							}// end of the inner if
						}// end of inner if
					}// end of for
				}// end of outermost if
				
				List<componentDetailsDomain> theComponentData =  appdao.getTheComponentData(""+obj[0], Integer.parseInt(""+obj[4]));
				if(theComponentData.size()>0){
					for(int z=0;z<theComponentData.size();z++){
						if(theComponentData.get(z).getStr_status()!=null){
						  if(theComponentData.get(z).getStr_status().equals("26")){
							appdao.deleteFromComponent(theComponentData.get(z));
							}// end of the inner if
						}// end of inner if
					}// end of for
				} //end of if
*/				
					if(obj[21] != null && !obj[21].equals(""))
					{
						//hmDetails.put("isno", ""+obj[20]);	
						String Standard=obj[21]+"";
						String qry2="select b from LotInspection_IStandard_Domain b where b.str_is_no='"+Standard+"' and b.num_is_valid=1 ";	
						List<LotInspection_IStandard_Domain> runQry2 = daoHelper.findByQuery(qry2);	
						if(runQry2.size()>0)
						{		
							hmDetails.put("isnocheck", "1");
				
						}
						else{
							hmDetails.put("isnocheck", "0");
						}
					}
					else{
						hmDetails.put("isnocheck", "0");
					}
					
				List<Inclusion_Tracking_Domain> theInclusionTrackingList = appdao.theInclusionTrackingListOnAppIdAndBranchId(""+obj[2], Integer.parseInt(""+obj[4]));
				if(theInclusionTrackingList!=null && theInclusionTrackingList.size()>0){
				int st = 	theInclusionTrackingList.get(0).getIntAppStatus();
				hmDetails.put("inclu", st+"");
				}else{
					hmDetails.put("inclu", "0");
				}
				
				if((Integer.parseInt(""+obj[5]) == 106) || (Integer.parseInt(""+obj[5]) == 201)){
						hmDetails.put("prod", "1");
				}else{
					hmDetails.put("prod", "0");
				}
				 
				//adding userid into hashmap 
				hmDetails.put("userId", userid);
				hmDetails.put("eschemeId", ims.Jcrypt("1"));
				hmDetails.put("survFlag", appdao.getSurveillanceDate(""+obj[2]));
				hmDetails.put("eDocID", ims.Jcrypt("398"));
				String survId=String.valueOf(appdao.getSurveillanceDetails(obj[2].toString(),obj[0].toString()));
				
				if(survId.equals("0"))
					hmDetails.put("survId", survId);
				else
					hmDetails.put("survId", ims.Jcrypt(survId));
				System.out.println("surv id is :"+survId);
				
				String lastSurvId=String.valueOf(appdao.getLastSurveillanceDetails(obj[2].toString(),obj[0].toString()));
				
				
				if(lastSurvId.equals("0"))
					hmDetails.put("lastSurvId", lastSurvId);
				else
					hmDetails.put("lastSurvId", ims.Jcrypt(lastSurvId));
				
				System.out.println("lastSurvId id is :"+lastSurvId);
				
				Map<String,Object> flagMap= appdao.getSurveillanceFactoryTestReportFlag(obj[2].toString(),obj[0].toString(),survId);
				if(!flagMap.isEmpty()) {
				hmDetails.put("survTestRptFlag", Integer.parseInt(flagMap.get("isfactoryTestingConducted").toString()));
				hmDetails.put("survTestRptFlagMulti", Integer.parseInt(flagMap.get("isFactoryTestingConductedMulti").toString()));
				}
				else
				{
					hmDetails.put("survTestRptFlag", 0 );
					hmDetails.put("survTestRptFlag", 0);
				}
				hmDetails.put("eDocIdSam", ims.Jcrypt("397"));
				hmDetails.put("samIndepTestFlag", appdao.getSampleIndependentTestFlag(obj[2].toString(),obj[0].toString(),survId));
				hmDetails.put("eDocIDManuProcess", ims.Jcrypt("909"));
				hmDetails.put("eDocIDManuLayout", ims.Jcrypt("203"));
				hmDetails.put("eDocIDMachineryList", ims.Jcrypt("908"));
				hmDetails.put("eDocIDTestEquip", ims.Jcrypt("305"));
				hmDetails.put("eDocIDFormOfLabel", ims.Jcrypt("914"));
				hmDetails.put("eDocIDHygenicCondn", ims.Jcrypt("316"));
				hmDetails.put("eflag", ims.Jcrypt("0"));
				hmDetails.put("discrepancyFormFlag", appdao.getDiscrepancyFormFlag(obj[2].toString(),obj[0].toString(),survId));
				}
				listhmRcvdApplication.add(hmDetails);
			}
		}
	return listhmRcvdApplication;
	}
	
	
	@Override
	public List<HashMap> getHMLicenseApps(int userid,int roleId) {
		List<HashMap> listhmRcvdApplication = new ArrayList<HashMap>();
		List<HashMap<String,String>> listAction = new ArrayList<HashMap<String,String>>();
		List<HashMap<String,String>> testRequest = new ArrayList<HashMap<String,String>>();
		List listApplication = new ArrayList();
		listApplication = appdao.getHMLicenseApps(userid);
		
		
		
		if(listApplication != null && listApplication.size() > 0)
		{
			String appId = "";
			int branchId =0;
			String cmlN = "";
			int flag=0;
			for(int i=0;i<listApplication.size();i++)
			{
				HashMap hmDetails = new HashMap();
				Object [] obj = (Object [])listApplication.get(i);
				if(obj.length > 0)
				{
					if(obj[0] != null && !obj[0].equals(""))
					{
						hmDetails.put("str_app_id", ""+obj[0]);
						hmDetails.put("str_Eapp_id", ""+ims.Jcrypt(obj[0].toString()));
					}
					else
					{
						hmDetails.put("str_app_id", "--");
					}
					if(obj[1] != null && !obj[1].equals(""))
					{
						hmDetails.put("str_cml_no", ""+obj[1]);
						hmDetails.put("Ecml_no", ""+ims.Jcrypt(obj[1].toString()));
					    String cmlNum = obj[1]+"";
					    
					}
					else
					{
						hmDetails.put("str_cml_no", "--");
						hmDetails.put("Ecml_no", "--");
					}
					if(obj[2] != null && !obj[2].equals(""))
					{
						String dt = null;
						String dateSample = ""+obj[2].toString();

					    String newFormat = "dd/MM/yyyy";
					    String oldFormat = "yyyy-MM-dd";

					    SimpleDateFormat sdf1 = new SimpleDateFormat(oldFormat);
					    SimpleDateFormat sdf2 = new SimpleDateFormat(newFormat);
					    try {
					       dt=sdf2.format(sdf1.parse(dateSample));

					    } catch (ParseException e) {
					        e.printStackTrace();
					    }
						hmDetails.put("dt_granted_date", ""+obj[2]);
					}
					else
					{
						hmDetails.put("dt_granted_date", "--");
					}
					if(obj[3] != null && !obj[3].equals(""))
					{
						String dt = null;
						String dateSample = ""+obj[3].toString();

					    String newFormat = "dd/MM/yyyy";
					    String oldFormat = "yyyy-MM-dd";

					    SimpleDateFormat sdf1 = new SimpleDateFormat(oldFormat);
					    SimpleDateFormat sdf2 = new SimpleDateFormat(newFormat);
					    try {
					       dt=sdf2.format(sdf1.parse(dateSample));

					    } catch (ParseException e) {
					        e.printStackTrace();
					    }
						hmDetails.put("dt_validity_date", dt);
					}
					else
					{
						hmDetails.put("dt_validity_date", "--");
					}
					if(obj[4] != null && !obj[4].equals(""))
					{
						hmDetails.put("str_product_id", ""+obj[4]);
						//hmDetails.put("ebranch_id", ""+ims.Jcrypt(obj[4].toString()));
					}
					else
					{
						hmDetails.put("str_product_id", "--");
					}
					if(obj[5] != null && !obj[5].equals(""))
					{
						hmDetails.put("str_is_no", ""+obj[5]);
					}
					else
					{
						hmDetails.put("str_is_no", "--");
					}
					if(obj[6] != null && !obj[6].equals(""))
					{
						hmDetails.put("num_branch_id", ""+obj[6]);
						hmDetails.put("ebranch_id", ""+ims.Jcrypt(obj[6].toString()));
					}
					else
					{
						hmDetails.put("num_branch_id", "--");
					}
					if(obj[7] != null && !obj[7].equals(""))
					{
						hmDetails.put("str_firm_name", obj[7]);
					}
					else
					{
						hmDetails.put("str_firm_name", "--");
					}
					if(obj[8] != null && !obj[8].equals(""))
						{
							int procedure = 10;
							int statusId= Integer.parseInt(obj[8].toString());
							listAction = DaoObj.getActionList(roleId,statusId,procedure);
							if(listAction != null && listAction.size()>0)
							{
								hmDetails.put("ListAction",listAction);
							}
							else
							{
								hmDetails.put("ListAction", "-");
							}
						}
					if(obj[8] != null && !obj[8].equals(""))
					{
						hmDetails.put("Status", obj[8]);
					}
					if(obj[9] != null && !obj[9].equals(""))
					{
						hmDetails.put("str_status_name", obj[9]);
					}
					else
					{
						hmDetails.put("str_status_name", "--");
					}
					
					if(obj[10] != null && !obj[10].equals(""))
					{
						hmDetails.put("num_outlet_no", obj[10]);
					}
					else
					{
						hmDetails.put("num_outlet_no", "--");
					}
					
					if(obj[11] != null && !obj[11].equals(""))
					{
						hmDetails.put("num_id", obj[11]);
					}
					else
					{
						hmDetails.put("num_id", " ");
					}
					
					if(obj[12] != null && !obj[12].equals(""))
					{
						hmDetails.put("renewalp", "1");
					}
					else
					{
						hmDetails.put("renewalp", "0");
					}
					
					if(obj[13] != null && !obj[13].equals(""))
					{
						hmDetails.put("curAppStatus", obj[13]);
					}
					else
					{
						hmDetails.put("curAppStatus", "0");
					}
					
					
					if(obj[14] != null && !obj[14].equals(""))
					{
						hmDetails.put("appStatusName", obj[14]);
					}
					else
					{
						hmDetails.put("appStatusName", "NA");
					}
					
					
					if(obj[1] != null && !obj[1].equals(""))
					{
						testRequest = DaoObj.gettestreqlist(String.valueOf(obj[1]));
						if(testRequest != null && testRequest.size()>0)
						{
							hmDetails.put("testRequest",testRequest);
						}
						else
						{
							hmDetails.put("testRequest", "-");
						}
					    
					}					
				}
				appId = obj[0]+"";
				branchId = Integer.parseInt(obj[6]+"");
				cmlN = obj[1]+"";
				
				String trFail=appdao.getTRFailStatus(cmlN);
				if(trFail != null && !trFail.equals(""))
				{
					hmDetails.put("trFail",trFail);
				}else
				{
					hmDetails.put("trFail","--");
				}
				
		/*		List<Hall_Licence_Details_Domain> licenceDetail=appdao.get_licence_Data(appId,branchId,cmlN);
				String dateRenewal ="";
				
				if(licenceDetail.size()>0){
					 
				    if(licenceDetail.get(0).getDt_renewal_date()!=null){
					dateRenewal =  licenceDetail.get(0).getDt_renewal_date().toString();
				    }else{
				    	dateRenewal="0000-00-00";
				    }
				     
				    
				    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				    Calendar cal = Calendar.getInstance();
				    String currentDate = dateFormat.format(cal.getTime());
				
				  String[] dateRenewalData = dateRenewal.split("-");
				  String[] dateCurrentData = currentDate.split("-");
				    
				  int diffYear = Integer.parseInt(dateRenewalData[0]) - Integer.parseInt(dateCurrentData[0]);
				  int diffMonth = Integer.parseInt(dateRenewalData[1]) - Integer.parseInt(dateCurrentData[1]);
				  int diffDays = Integer.parseInt(dateRenewalData[2]) - Integer.parseInt(dateCurrentData[2]);
				  
				  
				  hmDetails.put("renewalCheck", "0");
				  if(diffYear == 0 && diffMonth == 1){
					   hmDetails.put("renewalCheck", "1");
					    
				  }else{
					  hmDetails.put("renewalCheck", "0");
				  }
					 
				  if((diffMonth == 0 && diffYear == 0) || (diffMonth == -1 && diffYear == 0) || (diffMonth == -2 && diffYear == 0) ||(diffMonth == -3 && diffYear == 0)){
					   
					  hmDetails.put("renewalCheck", "1");
				  }
				  
				  if(diffMonth == -3 && diffDays == 0 && diffYear == 0){
					   
					  hmDetails.put("renewalCheck", "0");
				  }
				  
				  if(diffYear == 1 && diffMonth == -11){
					  hmDetails.put("renewalCheck", "1");
				  }
				  
				  if(diffYear == 1 && (diffMonth == 9 || diffMonth == 10 || diffMonth == 11)){
					   hmDetails.put("renewalCheck", "1");
					   flag=1;
				  }
				  
				}
*/				
				hmDetails.put("renewalCheck", "1");
				List<HMJewApplicationTrackingDomain> getTheTrackingForStatus =  appdao.getTheTrackingDtl(appId, branchId);
				if(getTheTrackingForStatus.size()>0){
							 
							if(getTheTrackingForStatus.get(0).getNum_app_status() == 89){
								hmDetails.put("renewalCheck", "0");
							}else{
								hmDetails.put("renewalCheck", "1");
							}
			} // end of if
				
				List<LicenceRenewDomain> theLicenceRenewDomainList = appdao.getTheRenewalList(appId, branchId,cmlN); 
				if(theLicenceRenewDomainList.size()>0){
					 
					for(int d=0;d<theLicenceRenewDomainList.size();d++){
						if(d == theLicenceRenewDomainList.size()-1){
							 
							if(theLicenceRenewDomainList.get(d).getStr_status_id().equals("26")){
								 
								appdao.deleteFromHallRenewalDomain(theLicenceRenewDomainList.get(d));
								hmDetails.put("renewalToShow", "1");
							}else{
								 
								//hmDetails.put("renewalToShow", "0");
								hmDetails.put("renewalToShow", "1");
							}
						}
					}
					//hmDetails.put("renewalToShow", "0");
				    flag=0;
				}else{
					hmDetails.put("renewalToShow", "1");
				    flag=1;
				}
				
				/*if(flag == 1){
					//List<HMJewApplicationTrackingDomain> 
					HMJewApplicationTrackingDomain domain = new HMJewApplicationTrackingDomain();
					domain.setNum_app_status(113);
					domain.setNum_branch_id(branchId);
					domain.setStr_application_id(appId);
					domain.setStr_cml_no(cmlN);
					appdao.saveInTheJewelTrackerRenewel(domain);
				}
				*/
				
				
				listhmRcvdApplication.add(hmDetails);
			}
		}
	return listhmRcvdApplication;
	}
	
	

	public static final int getMonthsDifference(Date date1, Date date2) {
		int m1 = date1.getYear() * 12 + date1.getMonth();
	    int m2 = date2.getYear() * 12 + date2.getMonth();
	    
	    return m2 - m1;
	}

	@Override
	public List<Licence_Brand_Detail_Domain> getBrandlist(String cml,
			int branchid) {
		
		List<Licence_Brand_Detail_Domain> lst = appdao.getBrandlist(cml,branchid);
		
		return lst;
	}

	@Override
	public void setBrandDoc(String cmlNo, String user_File_Name) {

				
		List<Licence_Brand_Detail_Domain> MappingList = new ArrayList<Licence_Brand_Detail_Domain>();
		Licence_Brand_Detail_Domain Mapping = new Licence_Brand_Detail_Domain();
		
		
			String strHQL = "from Licence_Brand_Detail_Domain p where p.str_brand_name LIKE '"+user_File_Name+"'" ;
			MappingList = daoHelper.findByQuery(strHQL);
			
				if(MappingList.size()>0){
					Mapping = MappingList.get(0);
					//daoHelper.maintainRecordInLog(new Category_Mst_Domain_log(), CategoryMapping);
					Mapping.setStr_brand_file_name(user_File_Name+".pdf");
					daoHelper.merge(Licence_Brand_Detail_Domain.class, Mapping);
					Mapping.setStr_brand_file_name(user_File_Name+".pdf");
					daoHelper.merge(Licence_Brand_Detail_Domain.class, Mapping);
					
					
				}
			
	}

	@Override
	public void setBrandDocTrack(String cmlNo, ApplicantModel appModel) {
		
		long id=appModel.getUserid();
		PCApplicationTrackingDomain track = new PCApplicationTrackingDomain();	
		
		track.setStr_cml_no(cmlNo);
		track.setIntAppStatus(85);
		track.setNum_flag_task_complte(1);
		track.setIntBranchId(appModel.getBranchid());
		track.setStrEntryEmpId(id);
		daoHelper.merge(PCApplicationTrackingDomain.class, track);
	}
	
    @Override
	public HashMap getApplicantHallDashboardData(int iUserId_p, String stEmail_p) {
		
	//	List submittedapps = appdao.getApplications(iUserId_p);
		int iCountsubmittedapps =0;
		int singleCountsubmittedapps =0;
		List licenceapps = new ArrayList();
		licenceapps = appdao.getHMLicenseApps(iUserId_p);
		int iCountLicenceapps = licenceapps.size();
		int iCountPendingfee=0;
		int iSavedApps=0;
		
		HashMap hm = new HashMap();
		
		/*if(licenceapps != null && licenceapps.size() > 0)
		{
			for(int i=0;i<licenceapps.size();i++)
			{
				Object [] obj = (Object [])licenceapps.get(i);
				if(obj.length > 0)
				{
					if(obj[11] != null && !obj[11].equals(""))
					{
						int countLicPendingFee =Integer.parseInt(obj[11].toString());
						// 
						if(countLicPendingFee>0){
							iCountPendingfee=countLicPendingFee;
							break;
						}
					}
				}
			}
		}*/
	
		List existAppliedCount=new ArrayList();
		existAppliedCount=appdao.getExistenceAppCount(iUserId_p);
		
		
		
		String existence="";
		if(existAppliedCount.size()>0){
			 
			HashSet test=new HashSet(existAppliedCount);
			if(test.contains(5)){
			existence="HMAH";	
			}
			if(test.contains(10)){
				existence="HMJW";
			}
			if(test.contains(13)){
				existence="HMRE";
			}
//			 Iterator iterator = test.iterator(); 
//		      
//			   // check values
//			   while (iterator.hasNext()){
//			    
//			   }
//			
//			
//			 
			hm.put("existence", existence);
		}
		if(existence.equalsIgnoreCase("HMAH"))
		{
		 iSavedApps = appdao.getCountofAHCLOIApplications(iUserId_p, 26,0);
		 iCountsubmittedapps=appdao.getCountofAHCLOIApplications(iUserId_p, 27,0);
		}
		if(existence.equalsIgnoreCase("HMJW"))
		{
		 iSavedApps=appdao.getCountofJewelApplications(iUserId_p, 26,0);
		 iCountsubmittedapps=appdao.getCountofJewelApplications(iUserId_p, 27,0);
		 singleCountsubmittedapps=appdao.getHMLicenseApps_corp(iUserId_p).size();
		}
		if(existence.equalsIgnoreCase("HMRE"))
		{
		 iSavedApps=appdao.getCountofAhcRecogApplications(iUserId_p, 26,0);
		 iCountsubmittedapps=appdao.getCountofAhcRecogApplications(iUserId_p, 27,0);
		 iCountLicenceapps = appdao.getHMRECLicenseApps(iUserId_p).size();
		}
		int iPercentCompletion = percentProfileCompletion(stEmail_p);
		

		
		
		hm.put("submitted", iCountsubmittedapps);
		hm.put("saved", iSavedApps);
		hm.put("saved", iSavedApps);
		hm.put("licence", iCountLicenceapps);
		hm.put("singleCountsubmittedapps", singleCountsubmittedapps);
	//	hm.put("iCountPendingfee", iCountPendingfee);
		hm.put("percent", iPercentCompletion);
		
		return hm;
	}
    
    
    //tanmay
	@Override
	public void setMergeSingleToCorporateDetails(JewHeadModel jewHeadModel,	String[] appIdarray,int iUserId,String appId_corporate,int branchId_corporate){
	
		List<Application_HallMarking_Domain> d= appHallMarkDao.get_Data_corporateConversion(iUserId,ims.Dcrypt(jewHeadModel.getEstrAppId()),Integer.parseInt(ims.Dcrypt(jewHeadModel.getEstrBranchId())));
		List<Hall_firmDtlsDomain> h= AppHallMarkService.get_firm_data(ims.Dcrypt(jewHeadModel.getEstrAppId()),Integer.parseInt(ims.Dcrypt(jewHeadModel.getEstrBranchId())));	
		
		
		for(int i=0;i<appIdarray.length;i++)
		{
			List<Hall_Branchdtl_Domain> b = appHMJBD.getDetails(appIdarray[i],Integer.parseInt(appIdarray[i].substring(0,2)),iUserId);
			Hall_Branchdtl_Domain_corporateConversion c=new Hall_Branchdtl_Domain_corporateConversion();
			c.setNum_HallMarkApp_Branch_dtId_beforeConversion(b.get(0).getNumHallMarkAppBranchCtId());
			//c.setDtEntryDate(b.get(0).getDtEntryDate());
			c.setStr_app_id(appId_corporate);
			c.setStr_app_id_beforeconversion(b.get(0).getStr_app_id());
			c.setStr_brnhNm(b.get(0).getStr_brnhNm());
			c.setNumCountryId(b.get(0).getNumCountryId());
			c.setStrOffAdd(b.get(0).getStrOffAdd());
			c.setStrOffAdd1(b.get(0).getStrOffAdd1());
			c.setStrCity(b.get(0).getStrCity());
			c.setNumDistrictId(b.get(0).getNumDistrictId());
			c.setNumStateId(b.get(0).getNumStateId());
			c.setStrPinCode(b.get(0).getStrPinCode());
			c.setStrContPersnName(b.get(0).getStrContPersnName())	;
			c.setStrContPersnEmailId(b.get(0).getStrContPersnEmailId());
			c.setStrContPersnNo(b.get(0).getStrContPersnNo());
			c.setStrFax(b.get(0).getStrFax());
			c.setNum_Entry_Emp_Id(b.get(0).getNum_Entry_Emp_Id());
			c.setDtEntryDatebeforeConversion(b.get(0).getDtEntryDate());
			c.setNum_branch_id_beforeConversion(b.get(0).getNum_branch_id());
			c.setNum_branch_id(branchId_corporate);
			c.setNum_Outlet_branch_id(b.get(0).getNum_Outlet_branch_id());
			c.setStr_num_doc(b.get(0).getStr_num_doc());
			c.setStr_renewal_flag(b.get(0).getStr_renewal_flag());
			c.setStrPopulation(b.get(0).getStrPopulation());
			c.setStr_document_type(b.get(0).getStr_document_type());
			appHallMarkbranchDao.addHallMarkApp_corporateconversion(c);
		}
		
		Hall_firmDtlsDomain_corporateConversion f = new Hall_firmDtlsDomain_corporateConversion();
		f.setNum_HallMarkApp_Id(appId_corporate);	
		f.setNum_HallMarkApp_Id_beforeconversion(h.get(0).getNum_HallMarkApp_Id());	
		f.setNum_branch_id_beforeconversion(h.get(0).getNum_branch_id());
		f.setNum_branch_id(branchId_corporate);
		if(jewHeadModel.getStrFirmName() != null){
			f.setStr_Firm_Name(jewHeadModel.getStrFirmName());
		}else{
			f.setStr_Firm_Name(h.get(0).getStr_Firm_Name());
		}		
		f.setStr_firm_intial(h.get(0).getStr_firm_intial());		
		if(jewHeadModel.getStrOffAdd() != null){
			f.setStr_Address_1(jewHeadModel.getStrOffAdd());
		}else{
			f.setStr_Address_1(h.get(0).getStr_Address_1());
		}		
		if(jewHeadModel.getStrOffAdd1() != null){
			f.setStr_Address_2(jewHeadModel.getStrOffAdd1());
		}else{
			f.setStr_Address_2(h.get(0).getStr_Address_2());
		}		
		if(jewHeadModel.getNumCountryId() != 0){
			f.setNum_Country_Id(jewHeadModel.getNumCountryId());
		}else{
			f.setNum_Country_Id(h.get(0).getNum_Country_Id());
		}		
		if(jewHeadModel.getNumStateId() != 0){
			f.setNum_State_Id(jewHeadModel.getNumStateId());
		}else{
			f.setNum_State_Id(h.get(0).getNum_State_Id());
		}		
		if(jewHeadModel.getNumDistrictId() != 0){
			f.setNum_District_Id(jewHeadModel.getNumDistrictId());
		}else{
			f.setNum_District_Id(h.get(0).getNum_District_Id());
		}		
		if(jewHeadModel.getStrCity() != null){
			f.setStr_City(jewHeadModel.getStrCity());
		}else{
			f.setStr_City(h.get(0).getStr_City());
		}		
		if(jewHeadModel.getStrPinCode() != null){
			f.setStr_Pin_code(jewHeadModel.getStrPinCode());
		}else{
			f.setStr_Pin_code(h.get(0).getStr_Pin_code());
		}		
		if(jewHeadModel.getStrContPersnName() != null){
			f.setStr_ContactPerson_Name(jewHeadModel.getStrContPersnName());
		}else{
			f.setStr_ContactPerson_Name(h.get(0).getStr_ContactPerson_Name());
		}		
		if(jewHeadModel.getStrContPersnNo() != null){
			f.setStr_ContactPerson_No(jewHeadModel.getStrContPersnNo());
		}else{
			f.setStr_ContactPerson_No(h.get(0).getStr_ContactPerson_No());
		}		
		if(jewHeadModel.getStrContPersnEmailId() != null){
			f.setStr_ContactPerson_EmailId(jewHeadModel.getStrContPersnEmailId());
		}else{
			f.setStr_ContactPerson_EmailId(h.get(0).getStr_ContactPerson_EmailId());
		}		
		if(jewHeadModel.getStrFax() != null){
			f.setStr_Fax(jewHeadModel.getStrFax());
		}else{
			f.setStr_Fax(h.get(0).getStr_Fax());
		}		
		f.setDtEntryDatebeforeConversion(h.get(0).getDt_entry_date());
		f.setNum_Entry_Emp_Id(h.get(0).getNum_Entry_Emp_Id());	
		appHallMarkbranchDao.addHallMarkfirm_corporateconversion(f);
		
		
		
		Application_HallMarking_Domain_corporateConversion a=new Application_HallMarking_Domain_corporateConversion();
		a.setStr_app_id(appId_corporate);
		a.setStr_app_id_beforeconversion(d.get(0).getStr_app_id());
		if(jewHeadModel.getStrAppName() != null){
			a.setStrAppName(jewHeadModel.getStrAppName());
			a.setStr_product_id(getStandardprodid(jewHeadModel.getStrAppName()));
		}else{
			a.setStrAppName(d.get(0).getStrAppName());
			a.setStr_product_id(getStandardprodid(d.get(0).getStrAppName()));
		}		
		if(jewHeadModel.getNumOutletNo() != 0){
			a.setNumOutletNo(jewHeadModel.getNumOutletNo());
		}else{
			a.setNumOutletNo(d.get(0).getNumOutletNo());
		}		
		if(jewHeadModel.getStrOutletType() != null){
			a.setStrOutletType(jewHeadModel.getStrOutletType());
		}else{
			a.setStrOutletType(d.get(0).getStrOutletType());
		}		
		if(jewHeadModel.getStrOwnerTypeName() != null){
			a.setStrOwnerTypeName(jewHeadModel.getStrOwnerTypeName());
		}else{
			a.setStrOwnerTypeName(d.get(0).getStrOwnerTypeName());
		}		
		if(jewHeadModel.getStrTurnOver() != null){
			a.setStrTurnOver(jewHeadModel.getStrTurnOver());
		}else{
			a.setStrTurnOver(d.get(0).getStrTurnOver());
		}		
		a.setNum_branch_id(branchId_corporate);
		a.setNum_branch_id_beforeconversion(d.get(0).getNum_branch_id());
		a.setStrLatLong(d.get(0).getStrLatLong());
		a.setNum_Entry_Emp_Id(d.get(0).getNum_Entry_Emp_Id());
		a.setDtEntryDatebeforeConversion(d.get(0).getDtEntryDate());
		a.setIntAppStatusId(26);
		
		if(jewHeadModel.getNumSectorId() != 0){
			a.setNum_sector_id(jewHeadModel.getNumSectorId());
		}else{
			a.setNum_sector_id(d.get(0).getNum_sector_id());
		}		
		if(jewHeadModel.getStrstdyear() != null){
			a.setNum_std_yr(Integer.parseInt(jewHeadModel.getStrstdyear().trim()));
		}else{
			a.setNum_std_yr(d.get(0).getNum_std_yr());
		}		
		a.setStr_ApplicenseType(d.get(0).getStr_ApplicenseType());
				
		if(jewHeadModel.getStrAppliedYear() != null){
			a.setStrAppliedYear(jewHeadModel.getStrAppliedYear());
		}else{
			a.setStrAppliedYear(d.get(0).getStrAppliedYear());
		}		
		if(jewHeadModel.getStrPayFeeYear() != null){
			a.setStrPayFeeYear(jewHeadModel.getStrPayFeeYear());
		}else{
			a.setStrPayFeeYear(d.get(0).getStrPayFeeYear());
		}
		appHallMarkbranchDao.addHallMarkApp_corporateconversion(a);
		
	}
	
	
	String getStandardprodid(String is){
		
		String s="";
		String std="select c from standard_mst_domain c where c.strStandardNo='"+is+"'";
		List<standard_mst_domain> stdid = daoHelper.findByQuery(std);
		if(stdid.size()>0){
			s=stdid.get(0).getStrProductId();
		}
		
		return s;
	}
    
    @Override
   	public List<HashMap> getHallHMSubmittedApplications(int iUserId_p, int iRoleId_p,String hm_type) {
   		List<HashMap> listhmApplicantApps = new ArrayList<HashMap>();  //finally returned by method
   		
   		//////////if user type is Jweller for hallmarking
   		if(hm_type.equalsIgnoreCase("HMJW")){
   		
   		List<HashMap<String,String>> listAction = new ArrayList<HashMap<String,String>>();
   		
   		List applicantapps = appdao.getHMJWSubmittedApplications(iUserId_p);
   		if(applicantapps.size()>0){
   			for(int i=0; i<applicantapps.size();i++){
   				HashMap hm = new HashMap();
   				Object [] obj = (Object []) applicantapps.get(i);
   				if(obj.length>0){
   					if(!obj[0].equals("")){
   						hm.put("appId", ""+obj[0]);
   					}
   					else{
   						hm.put("appId", "--");
   					}
   					if(!obj[0].equals(""))
   					{
   						String appid = ""+obj[0];
   						String EAppid = ims.Jcrypt(appid);
   						hm.put("EappId", EAppid);
   					}
   					else{
   						hm.put("EappId", "--");
   					}
   					if(!obj[1].equals("")){
   						String dt1="";
   						String dt = obj[1].toString();
   						String ab = obj[1].toString();
   						try {
   							dt1 = smdate.format(formatter.parse(dt));
   						} 
   						catch (ParseException e) {
   							e.printStackTrace();
   						}
   						hm.put("submittedDate", ""+dt1);
   					}
   					else{
   						hm.put("submittedDate", "--");
   					}
   					if(!obj[2].equals("")){
   						hm.put("ISNo", ""+obj[2]);
   					}
   					else{
   						hm.put("ISNo", "--");
   					}
   					if(!obj[3].equals("")){
   						hm.put("FirmName", ""+obj[3]);
   					}
   					else{
   						hm.put("FirmName", "--");
   					}
   					
   					if(obj[4] != null && !obj[4].equals(""))
   					{
   						String branchId = ""+obj[4];
   						String EbranchId = ims.Jcrypt(branchId);
   						hm.put("branchId", obj[4]);
   						hm.put("EbranchId", EbranchId);
   					
   					}
   					else{
   						hm.put("branchId", "--");
   						hm.put("EbranchId", "--");
   					}
   					if(!obj[5].equals("")){
						hm.put("actionstatus", ""+obj[5]);
					}
					else{
						hm.put("actionstatus", "--");
					}
   					
   					if(obj[7] != null && !obj[7].equals("")){
						hm.put("strOutletType", ""+obj[7]);
					}
					else{
						hm.put("strOutletType", "--");
					}
   					if(obj[8] != null && !obj[8].equals("")){
						hm.put("numOutletNo", ""+obj[8]);
					}
					else{
						hm.put("numOutletNo", "--");
					}
   					
   					if(obj[5] != null && !obj[5].equals(""))
					{
						int procedure = 10;
						int statusId= Integer.parseInt(obj[5].toString());
						listAction = DaoObj.getActionList(iRoleId_p,statusId,procedure);
						if(listAction != null && listAction.size()>0)
						{
							hm.put("ListAction",listAction);
						}
						else
						{
							hm.put("ListAction", "-");
						}
					}
   					if(obj[6] != null && !obj[6].equals(""))
   					{
   						hm.put("statusName", obj[6]);
   						
   					
   					}
   					else{
   						hm.put("statusName", "--");
   						
   					}
   					
   				}/*if object length=6*/
   				listhmApplicantApps.add(hm);
   			}/*for loop*/
   		}/*if query size > 0*/
   		}else if(hm_type.equalsIgnoreCase("HMCC")){

   			appdao.delPreRecrdHM_corporateConversion(iUserId_p);
   			
   	   		List<HashMap<String,String>> listAction = new ArrayList<HashMap<String,String>>();
   	   		
   	   	List applicantapps = appdao.getHMJWSubmittedApplications_corporateConversion(iUserId_p);
   	   		if(applicantapps.size()>0){
   	   			for(int i=0; i<applicantapps.size();i++){
   	   				HashMap hm = new HashMap();
   	   				Object [] obj = (Object []) applicantapps.get(i);
   	   				if(obj.length>0){
   	   					if(!obj[0].equals("")){
   	   						hm.put("appId", ""+obj[0]);
   	   					}
   	   					else{
   	   						hm.put("appId", "--");
   	   					}
   	   					if(!obj[0].equals(""))
   	   					{
   	   						String appid = ""+obj[0];
   	   						String EAppid = ims.Jcrypt(appid);
   	   						hm.put("EappId", EAppid);
   	   					}
   	   					else{
   	   						hm.put("EappId", "--");
   	   					}
   	   					if(!obj[1].equals("")){
   	   						String dt1="";
   	   						String dt = obj[1].toString();
   	   						String ab = obj[1].toString();
   	   						try {
   	   							dt1 = smdate.format(formatter.parse(dt));
   	   						} 
   	   						catch (ParseException e) {
   	   							e.printStackTrace();
   	   						}
   	   						hm.put("submittedDate", ""+dt1);
   	   					}
   	   					else{
   	   						hm.put("submittedDate", "--");
   	   					}
   	   					if(!obj[2].equals("")){
   	   						hm.put("ISNo", ""+obj[2]);
   	   					}
   	   					else{
   	   						hm.put("ISNo", "--");
   	   					}
   	   					if(!obj[3].equals("")){
   	   						hm.put("FirmName", ""+obj[3]);
   	   					}
   	   					else{
   	   						hm.put("FirmName", "--");
   	   					}
   	   					
   	   					if(obj[4] != null && !obj[4].equals(""))
   	   					{
   	   						String branchId = ""+obj[4];
   	   						String EbranchId = ims.Jcrypt(branchId);
   	   						hm.put("branchId", obj[4]);
   	   						hm.put("EbranchId", EbranchId);
   	   					
   	   					}
   	   					else{
   	   						hm.put("branchId", "--");
   	   						hm.put("EbranchId", "--");
   	   					}
   	   					if(!obj[5].equals("")){
   							hm.put("actionstatus", ""+obj[5]);
   						}
   						else{
   							hm.put("actionstatus", "--");
   						}
   	   					
   	   					if(obj[7] != null && !obj[7].equals("")){
   							hm.put("strOutletType", ""+obj[7]);
   						}
   						else{
   							hm.put("strOutletType", "--");
   						}
   	   					if(obj[8] != null && !obj[8].equals("")){
   							hm.put("numOutletNo", ""+obj[8]);
   						}
   						else{
   							hm.put("numOutletNo", "--");
   						}
   	   					
   	   					if(obj[5] != null && !obj[5].equals(""))
   						{
   							int procedure = 10;
   							int statusId= Integer.parseInt(obj[5].toString());
   							listAction = DaoObj.getActionList(iRoleId_p,statusId,procedure);
   							if(listAction != null && listAction.size()>0)
   							{
   								hm.put("ListAction",listAction);
   							}
   							else
   							{
   								hm.put("ListAction", "-");
   							}
   						}
   	   					if(obj[6] != null && !obj[6].equals(""))
   	   					{
   	   						hm.put("statusName", obj[6]);
   	   						
   	   					
   	   					}
   	   					else{
   	   						hm.put("statusName", "--");
   	   						
   	   					}
   	   					
   	   				}/*if object length=6*/
   	   				listhmApplicantApps.add(hm);
   	   			}/*for loop*/
   	   		}/*if query size > 0*/
   		}
   		else{
   		
   //////////if user type is AHC LOI for hallmarking
   		List<HashMap<String,String>> listActionHMAH = new ArrayList<HashMap<String,String>>();
   		
   		List applicantHMAHapps = appdao.getHMAHSubmittedApplications(iUserId_p);
   		
   		/*a.str_application_id, a.date, a.str_is_no,
   		 * a.strAHCName," +
 		"c.num_app_status ,e.str_applicant_status,a.num_branch_id," +
 		"(select count(intApplnStatusId) from Fee_Paid_Detail_Domain_hall f " +
 		"where f.strAppId=a.str_application_id and f.num_branch_id=a.num_branch_id and f.numEntryEmpId <= sysdate ) " +*/
   		
   		
   		
   		if(applicantHMAHapps.size()>0){
   			for(int i=0; i<applicantHMAHapps.size();i++){
   				HashMap hm = new HashMap();
   				Object [] obj = (Object []) applicantHMAHapps.get(i);
   				if(obj.length>0){
   					if(!obj[0].equals("")){
   						hm.put("appId", ""+obj[0]);
   					}
   					else{
   						hm.put("appId", "--");
   					}
   					if(!obj[0].equals(""))
   					{
   						String appid = ""+obj[0];
   						String EAppid = ims.Jcrypt(appid);
   						hm.put("EappId", EAppid);
   					}
   					else{
   						hm.put("EappId", "--");
   					}
   					if(!obj[1].equals("")){
   						String dt1="";
   						String dt = obj[1].toString();
   						String ab = obj[1].toString();
   						try {
   							dt1 = smdate.format(formatter.parse(dt));
   						} 
   						catch (ParseException e) {
   							e.printStackTrace();
   						}
   						hm.put("submittedDate", ""+dt1);
   					}
   					else{
   						hm.put("submittedDate", "--");
   					}
   					if(!obj[2].equals("")){
   						hm.put("ISNo", ""+obj[2]);
   					}
   					else{
   						hm.put("ISNo", "--");
   					}
   					if(!obj[3].equals("")){
   						hm.put("strAHCName", ""+obj[3]);
   					}
   					else{
   						hm.put("strAHCName", "--");
   					}
   					if(!obj[4].equals("")){
   						hm.put("num_app_status", ""+obj[4]);
   					}
   					else{
   						hm.put("num_app_status", "--");
   					}  	
   					if(!obj[5].equals("")){
   						hm.put("str_applicant_status", ""+obj[5]);
   					}
   					else{
   						hm.put("str_applicant_status", "--");
   					}
   					if(obj[4] != null && !obj[4].equals(""))
					{
						int procedure = 13;
						int statusId= Integer.parseInt(obj[4].toString());
						listActionHMAH = DaoObj.getActionList(iRoleId_p,statusId,procedure);
						if(listActionHMAH != null && listActionHMAH.size()>0)
						{
							hm.put("ListAction",listActionHMAH);
						}
						else
						{
							hm.put("ListAction", "-");
						}
					}
   					if(obj[6] != null && !obj[6].equals(""))
   					{
   						hm.put("num_branch_id", obj[6]);
   					}
   					else{
   						hm.put("num_branch_id", "--");
   					}
   					if(obj[6] != null && !obj[6].equals(""))
   					{
   						String branchId = ""+obj[6];
   						String EbranchId = ims.Jcrypt(branchId);
   						hm.put("EbranchId", EbranchId);
   					}
   					else{
   						hm.put("EbranchId", "--");
   					}
   					if(obj[7] != null && !obj[7].equals(""))
   					{
   						String countPendingFee = ""+obj[7];
   						hm.put("countPendingFee", countPendingFee);
   					}
   					else{
   						hm.put("countPendingFee", "--");
   					}
   				}/*if object length=6*/
   				listhmApplicantApps.add(hm);
   			}/*for loop*/
   		}/*if query size > 0*/
   		}
   		
   		return listhmApplicantApps;
   	}

	@Override
	public TextEditor_Dom getLetterView(String appId, String branchId,String letterId) {
		TextEditor_Dom returnObj = null;
		List<TextEditor_Dom> lst = appdao.getLetterView(appId,branchId,letterId);
		if (lst.size() > 0) 
		{
		returnObj = lst.get(0);
		}
		return returnObj;
	}

	@Override
	public int getCountNameChng(int userId) {
		int returnObj = 0;
		List<appliChngNameDomain> lst = appdao.getCountNameChng(userId);
		returnObj = lst.size();
		return returnObj;
	}

	@Override
	public void requestChngName(applicantChngNameModel Model, int UserId,int RoleId) {
		// Genrating application id
		String strAppNo = "";
		String strAppId = "";
		String FinalApplicationId;
		List<String> Appid = appdao.getMaxChngNameID(UserId);
		int prvAppID = 0;
		prvAppID = Model.getId();
				if (prvAppID == 0) {
					String NewId = "0001";
					String userId = Integer.toString(UserId);
					String NewApplicationNo = userId + NewId;
					FinalApplicationId = NewApplicationNo;
				}
				else
				{
					strAppNo = Appid.get(0);
					Long lnAppNo = Long.parseLong(strAppNo);
					Long NewId = lnAppNo + 1;
					strAppId = NewId.toString();
					FinalApplicationId = strAppId;
				}	
				 
		changeModelToApplicantChangeDomain(Model,UserId,FinalApplicationId);
		convertsModelToTrackingDomain(UserId,RoleId,Model,FinalApplicationId);
	}

	private void convertsModelToTrackingDomain(int userId, int roleId,
			applicantChngNameModel model ,String FinalApplicationId) {
		PCApplicationTrackingDomain trackingDomainObj = new PCApplicationTrackingDomain();
		trackingDomainObj.setIntAppStatus(164);
		trackingDomainObj.setIntBranchId(model.getBranchId());
		trackingDomainObj.setLnApplicationId(FinalApplicationId);
		trackingDomainObj.setNum_visible_applicant(1);
		trackingDomainObj.setStrEntryEmpId((long) userId);
		trackingDomainObj.setStrEntryRoleId(roleId);
		trackingDomainObj.setStr_cml_no("0");
		//if(viewRemarksmodel.getPropDate() != null || viewRemarksmodel.getPropDate() != "")
		trackingDomainObj.setStrRemarks("Name Change Request");
		DaoObj.submitRemark(trackingDomainObj);
		
		
	}

	private void changeModelToApplicantChangeDomain(
			applicantChngNameModel model, int userId, String finalApplicationId) {

		appliChngNameDomain domainObj = new appliChngNameDomain();
		domainObj.setStrAppId(finalApplicationId);
		domainObj.setNumEntryEmpId(userId);
		domainObj.setStrNewName(model.getStrNewFirmName());
		domainObj.setStrOldName(model.getStrOldFirmName());
		int id = model.getId();
		String file_name= userId+"_"+id+"nameChangeDoc.pdf";
		String file_Path =  globalPath+userId+File.separator+file_name;
		 
		File check_file = new File(file_Path);
		if(check_file.exists()){
			 
			domainObj.setDocPath(file_name);	
		}
		else{
			 
			domainObj.setDocPath(null);
		}
		appdao.applicantChngName(domainObj);
		
	}



	@Override
	public List<HashMap> getLetterDetails(String appId, String branchId) {
		// TODO Auto-generated method stub
		List<HashMap> listhmRcvdApplication = new ArrayList<HashMap>();
		List listApplication = new ArrayList();
		listApplication = appdao.getLetterDetails(appId,branchId);
		if(listApplication != null && listApplication.size() > 0)
		{
			for(int i=0;i<listApplication.size();i++)
			{
				HashMap hmDetails = new HashMap();
				Object [] obj = (Object [])listApplication.get(i);
				if(obj.length > 0)
				{
					if(obj[0] != null && !obj[0].equals(""))
					{
						hmDetails.put("appId", ""+obj[0]);
						hmDetails.put("eAppId", ""+ims.Jcrypt(obj[0].toString()));
					}
					else
					{
						hmDetails.put("appId", "--");
					}
					if(obj[1] != null && !obj[1].equals(""))
					{
						String dt = null;
						try {
							dt = smdate.format(formatter.parse(obj[1].toString()));
						} catch (ParseException e) {
							e.printStackTrace();
						}
						hmDetails.put("date", ""+dt);
					}
					else
					{
						hmDetails.put("date", "--");
					}
					if(obj[2] != null && !obj[2].equals(""))
					{
						hmDetails.put("letterName", ""+obj[2]);						
					}
					else
					{
						hmDetails.put("letterName", "--");
					}
					if(obj[3] != null && !obj[3].equals(""))
					{
						hmDetails.put("letterId", ""+obj[3]);
					}
					else
					{
						hmDetails.put("letterId", "--");
					}
					if(obj[4] != null && !obj[4].equals(""))
					{
			
						hmDetails.put("branchId", ""+obj[4]);
						hmDetails.put("ebranchId", ""+ims.Jcrypt(obj[4].toString()));
					}
					else
					{
						hmDetails.put("branchId", "--");
					}
			}
				listhmRcvdApplication.add(hmDetails);
			}
		}
	return listhmRcvdApplication;
		
	}



	@Override
	public List<HashMap<String, String>> getTRFailedList(int iUserId,int iRoleId, String licNum) {
		
		List<HashMap<String,String>> listTrFailed = new ArrayList<HashMap<String,String>>();  //finally returned by method
   		
   		List trList = appdao.getFailedTRList(licNum);
   		if(trList.size()>0)
   		{
   			for(int i=0; i<trList.size();i++)
   			{
   				HashMap hm = new HashMap();
   				Object [] obj = (Object []) trList.get(i);
   				if(obj.length>0)
   				{
   					if(!obj[0].equals("")){
   						hm.put("licNum", ""+obj[0]);
   					}
   					else{
   						hm.put("licNum", "--");
   					}
   					if(!obj[1].equals(""))
   					{
   						String newstring = "";
   						String dt = obj[1].toString();
   						try {
   							Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(dt);
   							newstring = new SimpleDateFormat("dd-MM-yyyy").format(date);
   						} catch (ParseException e) {
   							e.printStackTrace();
   						}
   						hm.put("testReqDate", "" + newstring);
   					}
   					else{
   						hm.put("testReqDate", "--");
   					}
   					
   					if(!obj[2].equals("")){
   						hm.put("trStatus", ""+obj[2]);
   					}
   					else{
   						hm.put("trStatus", "--");
   					}
   					if(!obj[3].equals("")){
   						hm.put("marketSerId", ""+obj[3]);
   					}
   					else{
   						hm.put("marketSerId", "--");
   					}
   					
   				}
   				listTrFailed.add(hm);
   			}
   		}
   		return listTrFailed;
	}



	@Override
	public List<HMJewApplicationTrackingDomain> getLatestTrack(String appid,int branchid,
			int status) {
		/*List<HMJewApplicationTrackingDomain> list = new ArrayList<HMJewApplicationTrackingDomain>();  
   		
   		list = appdao.getLatestTrack(appid,branchid,status);*/
		return null;
	}


	@Override
	public List<HashMap> getHMRECLicenseApps(int userid,int roleId) {
		List<HashMap> listhmRcvdApplication = new ArrayList<HashMap>();
		List<HashMap<String,String>> listAction = new ArrayList<HashMap<String,String>>();
		List<HashMap<String,String>> testRequest = new ArrayList<HashMap<String,String>>();
		List listApplication = new ArrayList();
		listApplication = appdao.getHMRECLicenseApps(userid);
		
		
		
		if(listApplication != null && listApplication.size() > 0)
		{
			String appId = "";
			int branchId =0;
			String cmlN = "";
			int flag=0;
			for(int i=0;i<listApplication.size();i++)
			{
				HashMap hmDetails = new HashMap();
				Object [] obj = (Object [])listApplication.get(i);
				if(obj.length > 0)
				{
					if(obj[0] != null && !obj[0].equals(""))
					{
						hmDetails.put("str_app_id", ""+obj[0]);
						hmDetails.put("str_Eapp_id", ""+ims.Jcrypt(obj[0].toString()));
					}
					else
					{
						hmDetails.put("str_app_id", "--");
					}
					if(obj[1] != null && !obj[1].equals(""))
					{
						hmDetails.put("str_cml_no", ""+obj[1]);
					    String cmlNum = obj[1]+"";
					    
					}
					else
					{
						hmDetails.put("str_cml_no", "--");
					}
					if(obj[2] != null && !obj[2].equals(""))
					{
						String dt = null;
						String dateSample = ""+obj[2].toString();

					    String newFormat = "dd/MM/yyyy";
					    String oldFormat = "yyyy-MM-dd";

					    SimpleDateFormat sdf1 = new SimpleDateFormat(oldFormat);
					    SimpleDateFormat sdf2 = new SimpleDateFormat(newFormat);
					    try {
					       dt=sdf2.format(sdf1.parse(dateSample));

					    } catch (ParseException e) {
					        e.printStackTrace();
					    }
						hmDetails.put("dt_granted_date", ""+obj[2]);
					}
					else
					{
						hmDetails.put("dt_granted_date", "--");
					}
					if(obj[3] != null && !obj[3].equals(""))
					{
						
						System.out.println("date.."+obj[3].toString());
						String dt = null;
						String dateSample = ""+obj[3].toString();

					    String newFormat = "dd/MM/yyyy";
					    String oldFormat = "yyyy-MM-dd";

					    SimpleDateFormat sdf1 = new SimpleDateFormat(oldFormat);
					    SimpleDateFormat sdf2 = new SimpleDateFormat(newFormat);
					    try {
					       dt=sdf2.format(sdf1.parse(dateSample));

					    } catch (ParseException e) {
					        e.printStackTrace();
					    }
						hmDetails.put("dt_validity_date", dt);
					}
					else
					{
						hmDetails.put("dt_validity_date", "--");
					}
					if(obj[4] != null && !obj[4].equals(""))
					{
						hmDetails.put("str_product_id", ""+obj[4]);
						//hmDetails.put("ebranch_id", ""+ims.Jcrypt(obj[4].toString()));
					}
					else
					{
						hmDetails.put("str_product_id", "--");
					}
					if(obj[5] != null && !obj[5].equals(""))
					{
						hmDetails.put("str_is_no", ""+obj[5]);
					}
					else
					{
						hmDetails.put("str_is_no", "--");
					}
					if(obj[6] != null && !obj[6].equals(""))
					{
						hmDetails.put("num_branch_id", ""+obj[6]);
						hmDetails.put("ebranch_id", ""+ims.Jcrypt(obj[6].toString()));
					}
					else
					{
						hmDetails.put("num_branch_id", "--");
					}
					if(obj[7] != null && !obj[7].equals(""))
					{
						hmDetails.put("str_firm_name", obj[7]);
					}
					else
					{
						hmDetails.put("str_firm_name", "--");
					}
					if(obj[8] != null && !obj[8].equals(""))
						{
							int procedure = 10;
							int statusId= Integer.parseInt(obj[8].toString());
							listAction = DaoObj.getActionList(roleId,statusId,procedure);
							if(listAction != null && listAction.size()>0)
							{
								hmDetails.put("ListAction",listAction);
							}
							else
							{
								hmDetails.put("ListAction", "-");
							}
						}
					if(obj[8] != null && !obj[8].equals(""))
					{
						hmDetails.put("Status", obj[8]);
					}
					if(obj[9] != null && !obj[9].equals(""))
					{
						hmDetails.put("str_status_name", obj[9]);
					}
					else
					{
						hmDetails.put("str_status_name", "--");
					}
					
					if(obj[10] != null && !obj[10].equals(""))
					{
						hmDetails.put("num_outlet_no", obj[10]);
					}
					else
					{
						hmDetails.put("num_outlet_no", "--");
					}
					
					
					if(obj[1] != null && !obj[1].equals(""))
					{
						testRequest = DaoObj.gettestreqlist(String.valueOf(obj[1]));
						if(testRequest != null && testRequest.size()>0)
						{
							hmDetails.put("testRequest",testRequest);
						}
						else
						{
							hmDetails.put("testRequest", "-");
						}
					    
					}					
				}
				appId = obj[0]+"";
				branchId = Integer.parseInt(obj[6]+"");
				cmlN = obj[1]+"";
				
				String trFail=appdao.getTRFailStatus(cmlN);
				if(trFail != null && !trFail.equals(""))
				{
					hmDetails.put("trFail",trFail);
				}else
				{
					hmDetails.put("trFail","--");
				}
				
					
				hmDetails.put("renewalCheck", "1");
				List<HMJewApplicationTrackingDomain> getTheTrackingForStatus =  appdao.getTheTrackingDtl(appId, branchId);
				if(getTheTrackingForStatus.size()>0){
							 
							if(getTheTrackingForStatus.get(0).getNum_app_status() == 89){
								hmDetails.put("renewalCheck", "0");
							}else{
								hmDetails.put("renewalCheck", "1");
							}
			} // end of if
				
				List<LicenceRenewDomain> theLicenceRenewDomainList = appdao.getTheRenewalList(appId, branchId,cmlN); 
				if(theLicenceRenewDomainList.size()>0){
					 
					for(int d=0;d<theLicenceRenewDomainList.size();d++){
						if(d == theLicenceRenewDomainList.size()-1){
							 
							if(theLicenceRenewDomainList.get(d).getStr_status_id().equals("26")){
								 
								appdao.deleteFromHallRenewalDomain(theLicenceRenewDomainList.get(d));
								hmDetails.put("renewalToShow", "1");
							}else{
								 
								//hmDetails.put("renewalToShow", "0");
								hmDetails.put("renewalToShow", "1");
							}
						}
					}
					//hmDetails.put("renewalToShow", "0");
				    flag=0;
				}else{
					hmDetails.put("renewalToShow", "1");
				    flag=1;
				}
				
				
				
				listhmRcvdApplication.add(hmDetails);
			}
		}
	return listhmRcvdApplication;
	}


	//###########################################################################//
	//###########################################################################//
	//Self SOM Methods By Dhruv									                 //
	//###########################################################################//
	//###########################################################################//
	
	@Override
	public HashMap<String, String> getSelfSOMCMLDetails(String stCmlNo,int iBranchId) {
		HashMap<String, String> hm = appdao.getSelfSOMCMLDetails(stCmlNo, iBranchId);
		return hm;
	}
	
	//###########################################################################//
	//###########################################################################//
	//Self SOM Methods By Dhruv									                 //
	//###########################################################################//
	//###########################################################################//


	@Override
	public String saveMSCDGSTNo(String stGSTNo, int iUserId){
		String stResponse = appdao.saveMSCDGSTNo(stGSTNo, iUserId);
		return stResponse;
	}



	@Override
	public List<Map<String, Object>> getsti_and_marking_fee_mapping(int iUserId) {
		return appdao.getsti_and_marking_fee_mapping(iUserId);
	}



	@Override
	public boolean submit_accptance(int num_id, int acptstidoc, int acptmarkingfee, String remark) {
		return appdao.submit_accptance(num_id,acptstidoc,acptmarkingfee,remark);
		
	}



	@Override
	public List<Map<String, Object>> getchecksum(String isno) {
		return appdao.getchecksum(isno);
	}



	@Override
	public List<HashMap> getApplicationsCOC(int iUserId_p, int iRoleId_p) {
		List<HashMap> listhmApplicantApps = new ArrayList<HashMap>();
		List<HashMap<String,String>> listAction = new ArrayList<HashMap<String,String>>();
		List applicantapps = appdao.getApplicationsCOC(iUserId_p);
		if(applicantapps.size()>0){
			for(int i=0; i<applicantapps.size();i++){
				HashMap hm = new HashMap();
				Object [] obj = (Object []) applicantapps.get(i);
				if(obj.length==16){
					if(obj[0]!=null && !obj[0].equals("")){
						String appid = ""+obj[0];
						String EAppid = ims.Jcrypt(appid);
						hm.put("appId", ""+obj[0]);
						hm.put("EappId", EAppid);
					}
					else{
						hm.put("appId", "--");
						hm.put("EappId", "--");
					}

					if(obj[1]!=null && !obj[1].equals("")){
						String dt1="";
						String dt = obj[1].toString();
						String ab = obj[1].toString();
						try {
							dt1 = abc.format(formatter.parse(dt));
						} 
						catch (ParseException e) {
							e.printStackTrace();
						}
						hm.put("submittedDate", ""+dt1);
					}
					else{
						hm.put("submittedDate", "--");
					}
					if(obj[2]!=null && !obj[2].equals("")){
						hm.put("ISNo", ""+obj[2]);
					}
					else{
						hm.put("ISNo", "--");
					}
					
					
					if(obj[3]!=null && !obj[3].equals("")){
						hm.put("FirmName", ""+obj[3]);
					}
					else{
						hm.put("FirmName", "--");
					}
					if(obj[5]!=null && !obj[5].equals("")){
						hm.put("WStatus", ""+obj[5]);
					}
					else{
						hm.put("WStatus", "--");
					}
					if(obj[4]!=null && !obj[4].equals("")){
						int procedure = Integer.parseInt(obj[8].toString());
						hm.put("proc_id", procedure);
						int statusId= Integer.parseInt(obj[4].toString());
						listAction = DaoObj.getActionList(iRoleId_p,statusId,procedure);
						if(listAction != null && listAction.size()>0)
						{
							hm.put("ListAction",listAction);
						}
						else
						{
							hm.put("ListAction", "-");
						}
					}
					if(obj[6] != null && !obj[6].equals(""))
					{
							String inspecdate = "";
							try {
								inspecdate = abc.format(formatter.parse(obj[6].toString()));
							} catch (ParseException e1) {
								e1.printStackTrace();
							}
							hm.put("lastDate", inspecdate);
					}
					else
					{
						
							hm.put("lastDate", null);
					}
					if(obj[4]!=null && !obj[4].equals("")){
						hm.put("actionstatus", ""+obj[4]);
						hm.put("Eactionstatus", ims.Jcrypt(""+obj[4]));
					}
					else{
						hm.put("actionstatus", "--");
						hm.put("Eactionstatus", ims.Jcrypt("--"));
					}

					if(obj[7] != null && !obj[7].equals(""))
					{
						String branchId = ""+obj[7];
						hm.put("branchId", branchId);
						String EbranchId = ims.Jcrypt(branchId);
						hm.put("EbranchId", EbranchId);
					}
					else{
						hm.put("EbranchId", "--");
						hm.put("branchId", "-1");
					}
					if(obj[9] != null && !obj[9].equals(""))
					{
						String countPendingFee = ""+obj[9];
						hm.put("countPendingFee", countPendingFee);
					}
					else{
						hm.put("countPendingFee", "--");
					}
					if(obj[10] != null && !obj[10].equals(""))
					{
						String count = ""+obj[10];
						int ab = Integer.parseInt(count);
						 
						if(ab >= 1)
						hm.put("clarify", "0");
						else
							hm.put("clarify", "1");	
					}
					else{
						hm.put("clarify", "0");
					}
					if(obj[11] != null && !obj[11].equals(""))
					{
						hm.put("lettername", ""+obj[11]);
					}
					else{
						hm.put("lettername", "");
					}
					if(obj[12] != null && !obj[12].equals(""))
					{
						hm.put("letterId", ""+obj[12]);
					}
					else{
						hm.put("letterId", "");
					}
					
					if(obj[13] != null && !obj[13].equals(""))
					{
						String recdate = "";
						try {
							Date dtReg = (Date) obj[13] ;
							SimpleDateFormat dtl =  new SimpleDateFormat("yyyy-MM-dd");
							recdate = dtl.format(dtReg);
							
							//recdate = smdate.format(formatter.parse(obj[13].toString()));
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						hm.put("recDate", recdate);
					}
					else
					{
						hm.put("recDate", null);
					}
					
					if(obj[14] != null && !obj[14].equals(""))
					{
						hm.put("procedure", obj[14]+"");
					}
					else
					{
						hm.put("procedure", "");
					}
					if(obj[15] != null && !obj[15].equals(""))
					{
						hm.put("ncstatus", obj[15]+"");
					}
					else
					{
						hm.put("ncstatus", "0");
					}
					
					
				}/*if object length=6*/
				listhmApplicantApps.add(hm);
			}/*for loop*/
		}/*if query size > 0*/
		
		return listhmApplicantApps;
	}



	@Override
	public List<HashMap> getLicenseAppsCOC(int userid, int roleId) {
		List<HashMap> listhmRcvdApplication = new ArrayList<HashMap>();
		List<HashMap<String,String>> listAction = new ArrayList<HashMap<String,String>>();
		List listApplication = new ArrayList();
		listApplication = appdao.getLicenseAppsCOC(userid);
		
		
		if(listApplication != null && listApplication.size() > 0)
		{
			for(int i=0;i<listApplication.size();i++)
			{
				HashMap hmDetails = new HashMap();
				Object [] obj = (Object [])listApplication.get(i);
				if(obj.length > 0)
				{
					if(obj[0] != null && !obj[0].equals(""))
					{
						hmDetails.put("cml_no", ""+obj[0]);
						hmDetails.put("ecml_no", ""+ims.Jcrypt(obj[0].toString()));
						if(appdao.checkConsigneeUploadForQuarter((String) obj[0])) {
							
							hmDetails.put("submitLatestConsigneeDetails", 1);
							hmDetails.put("eDocTypeId",ims.Jcrypt("5")); 

						}else {
							hmDetails.put("submitLatestConsigneeDetails", 0);

						}
					}
					else
					{
						hmDetails.put("cml_no", "--");
					}
					if(obj[1] != null && !obj[1].equals(""))
					{
						hmDetails.put("firm_name", ""+obj[1]);
					}
					else
					{
						hmDetails.put("firm_name", "--");
					}					
					if(obj[2] != null && !obj[2].equals(""))
					{
						hmDetails.put("app_id", ""+obj[2]);
						hmDetails.put("eapp_id", ""+ims.Jcrypt(obj[2].toString()));
					}
					else
					{
						hmDetails.put("app_id", "--");
					}
					if(obj[3] != null && !obj[3].equals(""))
					{
						String dt = null;
						String dateSample = ""+obj[3].toString();

					   // String newFormat = "dd/MM/yyyy";
					    String oldFormat = "yyyy-MM-dd";

					    SimpleDateFormat sdf1 = new SimpleDateFormat(oldFormat);
					    //SimpleDateFormat sdf2 = new SimpleDateFormat(newFormat);
					    try {
					       dt=sdf1.format(sdf1.parse(dateSample));

					    } catch (ParseException e) {
					        // TODO Auto-generated catch block
					        e.printStackTrace();
					    }
						hmDetails.put("granted_date", dt);
					}
					else
					{
						hmDetails.put("granted_date", "--");
					}
					if(obj[4] != null && !obj[4].equals(""))
					{
						hmDetails.put("branch_id", ""+obj[4]);
						hmDetails.put("ebranch_id", ""+ims.Jcrypt(obj[4].toString()));
					}
					else
					{
						hmDetails.put("branch_id", "--");
					}
					if(obj[5] != null && !obj[5].equals(""))
					{
						hmDetails.put("AppStatus", ""+obj[5]);
						int procedure = Integer.parseInt(obj[8].toString());
						int statusId= Integer.parseInt(obj[5].toString());
						listAction = DaoObj.getActionList(roleId,statusId,procedure);
						if(listAction != null && listAction.size()>0)
						{
							hmDetails.put("ListAction",listAction);
						}
						else{
							hmDetails.put("ListAction", "-");
						}
						
					}
					else
					{
						
						hmDetails.put("AppStatus", "--");
					}
					
					
					List<PCCMLLotInspectionTracking> theLotTrackingList = piServ.getTheStatusFromLotTracking(obj[0].toString());  
					if(theLotTrackingList.size() == 0){
						hmDetails.put("lotSwow", "1");
					}
					
					if(theLotTrackingList.size()>0){
							if(theLotTrackingList.get(0).getNum_app_status() !=534){
								hmDetails.put("lotSwow", "0");
							}else{
								hmDetails.put("lotSwow", "1");
							}
					}
					
					
					if(obj[6] != null && !obj[6].equals(""))
					{
						hmDetails.put("applicant_status", ""+obj[6]);
					}
					else
					{
						hmDetails.put("applicant_status", "--");
					}
					
					if(obj[7] != null && !obj[7].equals(""))
					{
						
						SimpleDateFormat sdfDB = new SimpleDateFormat("yyyy-MM-dd");
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
						
						String dt = null;
						String dateSample = ""+obj[7].toString();

					    //String newFormat = "dd/MM/yyyy";
					    String oldFormat = "yyyy-MM-dd";

					    SimpleDateFormat sdf1 = new SimpleDateFormat(oldFormat);
					  //  SimpleDateFormat sdf2 = new SimpleDateFormat(newFormat);
					    try {
					    	Date renewal = sdfDB.parse(""+obj[7]);
							Date currDt = new Date();
							int period = getMonthsDifference(currDt,renewal);
							if(period <= 3)
							{
								hmDetails.put("period", "1");
							}
							else
							{
								hmDetails.put("period", "0");
							}
					       dt=sdf1.format(sdf1.parse(dateSample));

					    } catch (ParseException e) {
					        e.printStackTrace();
					    }
						hmDetails.put("validityDate", dt);
					}
					else
					{
						hmDetails.put("validityDate", "--");
						hmDetails.put("period", null);
					}
					
					
					if(obj[9] != null && !obj[9].equals(""))
					{
						hmDetails.put("isno", ""+obj[9]);
					}
					else{
						hmDetails.put("isno", "");
					}
					
					if(obj[10] != null && !obj[10].equals(""))
					{
						hmDetails.put("som_status", ""+obj[10]);
					}
					else{
						hmDetails.put("som_status", "0");
					}
					if(obj[11] != null && !obj[11].equals(""))
					{
						hmDetails.put("self_som_id", ""+obj[11]);
					}
					else{
						hmDetails.put("self_som_id", "0");
					}
					if(obj[12] != null && !obj[12].equals(""))
					{
						hmDetails.put("rom_id", ""+obj[12]);
					}
					else{
						hmDetails.put("rom_id", "0");
					}
					
					if(obj[13] != null && !obj[13].equals(""))
					{
						hmDetails.put("NC", "1");
					}
					else
					{
						hmDetails.put("NC", "0");
					}
					System.out.println("***********obj[13]*****************"+obj[13]);
					if(obj[14] != null && !obj[14].equals(""))
					{
						String countLicPendingFee = ""+obj[14];
						hmDetails.put("countLicPendingFee", ""+countLicPendingFee);
					}
					else
					{
						hmDetails.put("countLicPendingFee", "--");
					}
					System.out.println("***********obj[14]*****************"+obj[14]);
					System.out.println("***********obj[15]*****************"+obj[15]);
					if(obj[15] != null && !obj[15].equals(""))
					{
						hmDetails.put("inclusion", "1");
					}
					else
					{
						hmDetails.put("inclusion", "0");
					}
					
					System.out.println("***********obj[16]*****************"+obj[16]);
					if(obj[16] != null && !obj[16].equals(""))
					{
						hmDetails.put("renewalp", "1");
					}
					else{
						hmDetails.put("renewalp", "0");
					}
					
					if(obj[17] != null && !obj[17].equals(""))
					{
						hmDetails.put("romtrackstatus", obj[17]+"");
					}
					else{
						hmDetails.put("romtrackstatus", "0");
					}
					
					if(obj[18] != null && !obj[18].equals(""))
					{
						hmDetails.put("lotrackstatus", obj[18]+"");
					}
					else{
						hmDetails.put("lottrackstatus", "0");
					}
					//mansi
					if(obj[19] != null && !obj[19].equals(""))
					{
						hmDetails.put("renewalStatusApplicatView", obj[19]+"");
					}
					else{
						hmDetails.put("renewalStatusApplicatView", "0");
					}
					//mansi
					if(obj[20] != null && !obj[20].equals(""))
					{
						hmDetails.put("num_renewal_tracking", obj[20]+"");
					}
					else{
						hmDetails.put("num_renewal_tracking", "0");
					}
					System.out.println("***********obj[19]**********"+obj[19]);
					System.out.println("***********obj[20]**********"+obj[20]);
				/*List<inclusionVarietyDomain> theInclusionData = appdao.getTheInclusionData(""+obj[0], Integer.parseInt(""+obj[4]));
				if(theInclusionData.size()>0){
					for(int x=0;x<theInclusionData.size();x++){
						if(theInclusionData.get(x).getStr_status_id()!=null){
							if(theInclusionData.get(x).getStr_status_id().equals("26")){
							appdao.deleteFromInclusion(theInclusionData.get(x));
							}// end of the inner if
						}// end of inner if
					}// end of for
				}// end of outermost if
				
				List<componentDetailsDomain> theComponentData =  appdao.getTheComponentData(""+obj[0], Integer.parseInt(""+obj[4]));
				if(theComponentData.size()>0){
					for(int z=0;z<theComponentData.size();z++){
						if(theComponentData.get(z).getStr_status()!=null){
						  if(theComponentData.get(z).getStr_status().equals("26")){
							appdao.deleteFromComponent(theComponentData.get(z));
							}// end of the inner if
						}// end of inner if
					}// end of for
				} //end of if
*/				
					if(obj[21] != null && !obj[21].equals(""))
					{
						//hmDetails.put("isno", ""+obj[20]);	
						String Standard=obj[21]+"";
						String qry2="select b from LotInspection_IStandard_Domain b where b.str_is_no='"+Standard+"' and b.num_is_valid=1 ";	
						List<LotInspection_IStandard_Domain> runQry2 = daoHelper.findByQuery(qry2);	
						if(runQry2.size()>0)
						{		
							hmDetails.put("isnocheck", "1");
				
						}
						else{
							hmDetails.put("isnocheck", "0");
						}
					}
					else{
						hmDetails.put("isnocheck", "0");
					}
					
				List<Inclusion_Tracking_Domain> theInclusionTrackingList = appdao.theInclusionTrackingListOnAppIdAndBranchId(""+obj[2], Integer.parseInt(""+obj[4]));
				if(theInclusionTrackingList!=null && theInclusionTrackingList.size()>0){
				int st = 	theInclusionTrackingList.get(0).getIntAppStatus();
				hmDetails.put("inclu", st+"");
				}else{
					hmDetails.put("inclu", "0");
				}
				
				if((Integer.parseInt(""+obj[5]) == 106) || (Integer.parseInt(""+obj[5]) == 201)){
						hmDetails.put("prod", "1");
				}else{
					hmDetails.put("prod", "0");
				}
				 
				//adding userid into hashmap 
				hmDetails.put("userId", userid);
				hmDetails.put("eschemeId", ims.Jcrypt("1"));
				hmDetails.put("survFlag", appdao.getSurveillanceDate(""+obj[2]));
				hmDetails.put("eDocID", ims.Jcrypt("398"));
				String survId=String.valueOf(appdao.getSurveillanceDetails(obj[2].toString(),obj[0].toString()));
				hmDetails.put("survId", ims.Jcrypt(survId));
				System.out.println("surv id is :"+survId);
				Map<String,Object> flagMap= appdao.getSurveillanceFactoryTestReportFlag(obj[2].toString(),obj[0].toString(),survId);
				if(!flagMap.isEmpty()) {
				hmDetails.put("survTestRptFlag", Integer.parseInt(flagMap.get("isfactoryTestingConducted").toString()));
				hmDetails.put("survTestRptFlagMulti", Integer.parseInt(flagMap.get("isFactoryTestingConductedMulti").toString()));
				}
				else
				{
					hmDetails.put("survTestRptFlag", 0 );
					hmDetails.put("survTestRptFlag", 0);
				}
				hmDetails.put("eDocIdSam", ims.Jcrypt("397"));
				hmDetails.put("samIndepTestFlag", appdao.getSampleIndependentTestFlag(obj[2].toString(),obj[0].toString(),survId));
				hmDetails.put("eDocIDManuProcess", ims.Jcrypt("909"));
				hmDetails.put("eDocIDManuLayout", ims.Jcrypt("203"));
				hmDetails.put("eDocIDMachineryList", ims.Jcrypt("908"));
				hmDetails.put("eDocIDTestEquip", ims.Jcrypt("305"));
				hmDetails.put("eDocIDFormOfLabel", ims.Jcrypt("914"));
				hmDetails.put("eDocIDHygenicCondn", ims.Jcrypt("316"));
				hmDetails.put("eflag", ims.Jcrypt("0"));
				hmDetails.put("discrepancyFormFlag", appdao.getDiscrepancyFormFlag(obj[2].toString(),obj[0].toString(),survId));
				}
				listhmRcvdApplication.add(hmDetails);
			}
		}
	return listhmRcvdApplication;
	}

	
}
