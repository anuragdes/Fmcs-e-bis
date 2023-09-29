package Applicant.DAO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import Applicant.Domain.appliChngNameDomain;
import Global.CommonUtility.DAO.DaoHelper;
import Global.CommonUtility.Domain.TextEditor_Dom;
import Global.Login.Service.IMigrateService;
import Masters.Domain.Procedure_Mst_Domain;
import Schemes.HallMarking.AHC.ApplicationSubmission.Domain.AHC_LOI_Domain;
import Schemes.HallMarking.Jeweller.ApplicationSubmission.Domain.Application_HallMarking_Domain;
import Schemes.HallMarking.Jeweller.ApplicationSubmission.Domain.Application_HallMarking_Domain_corporateConversion;
import Schemes.HallMarking.Jeweller.ApplicationSubmission.Domain.Hall_Licence_Details_Domain;
import Schemes.HallMarking.Jeweller.ApplicationSubmission.Domain.Renewal_HallMarking_Domain;
import Schemes.HallMarking.Jeweller.HMO.Domain.HMJewApplicationTrackingDomain;
import Schemes.HallMarking.Jeweller.LicenceRenewal.Domain.LicenceRenewDomain;
import Schemes.MSCD.Profile.Domain.MSCD_GST_Applicant;
import Schemes.ProductCertification.ApplicationSubmission.Domain.Licence_Brand_Detail_Domain;
import Schemes.ProductCertification.ApplicationSubmission.Domain.PcGOLDomain;
import Schemes.ProductCertification.ApplicationSubmission.Domain.user_profile_domain;
import Schemes.ProductCertification.LicenceGranting.Domain.Inclusion_Tracking_Domain;
import Schemes.ProductCertification.LicenceGranting.Domain.componentDetailsDomain;
import Schemes.ProductCertification.LicenceGranting.Domain.inclusionVarietyDomain;
import Schemes.ProductCertification.LicenseCancellation.Domain.cmlstatusdetails_Domain;
import Schemes.ProductCertification.LicenseCancellation.Domain.cmlstatusdetails_Domain_log;
import lab.domain.Lab_Hall_Test_Request_Master_Domain;
import lombok.var;
@SuppressWarnings({"unused","rawtypes","deprecation"})
@Repository
public class ApplicantDAO {

	@Autowired
	public DaoHelper daoHelper;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	 
	
	@Autowired
	IMigrateService ims;

	public cmlstatusdetails_Domain getLatestCMLstatusEntry(String stCMLNo, int iBranchId) {
		String qry = "SELECT p FROM cmlstatusdetails_Domain p WHERE p.str_cml_no='"+stCMLNo+"' AND p.num_branch_id="+iBranchId;
		List<cmlstatusdetails_Domain> lcsdd = daoHelper.findByQuery(qry);
		cmlstatusdetails_Domain csdd = new cmlstatusdetails_Domain();
		if (lcsdd.size() == 1) {
			csdd = lcsdd.get(0);
		}
		return csdd;
	}

	public void updateCMLStatusTable(cmlstatusdetails_Domain csdd){
		daoHelper.merge(cmlstatusdetails_Domain.class, csdd);
	}

	public void applicantChngName(appliChngNameDomain domainObj){
		daoHelper.merge(appliChngNameDomain.class, domainObj);
	}
	
	public List getMaxChngNameID(int UserId) {
		String qry = "SELECT max(c.strAppId) from appliChngNameDomain c where c.numEntryEmpId = "+ UserId + "";
		return daoHelper.findByQuery(qry);
	}
	
	public List getApplications(int iUserId){
		
		Calendar now = Calendar.getInstance();
	    int year=now.get(Calendar.YEAR) - (now.get(Calendar.MONTH) < 4?1:0);
	    String start=year + "-04-01";
	    String end=(year+1) + "-03-31";
	    String qry = "SELECT a.lnApplicationId, a.dtRegistration, a.strISno ||':'|| a.numStdYear, b.strFirmName,c.intAppStatus ,e.str_applicant_status," +
				" (select atr.proposeDate from PCApplicationTrackingDomain atr where a.lnApplicationId=atr.lnApplicationId and a.intBranchId=atr.intBranchId and  atr.num_id = (select max(num_id) from PCApplicationTrackingDomain btr where a.lnApplicationId=btr.lnApplicationId and a.intBranchId=btr.intBranchId and btr.proposeDate is not null))," +
				" a.intBranchId, a.intProcedureId, " +
				" (select count(intApplnStatusId) from Fee_Paid_Detail_Domain f where f.strAppId=a.lnApplicationId and f.num_branch_id=a.intBranchId and f.intApplnStatusId=0 and f.num_Amount <> '0' and f.isValid=1 and to_char(f.dt_Entry_Date,'yyyy-mm-dd') >='2017-05-15' and f.dt_Entry_Date <= sysdate and f.numFeeId not in (1,7 ,28) and f.numEntryEmpId<>"+iUserId+" and f.numFeeId in (select b.numFeeId from Fee_Type_Domain b where b.strAmountType='Rs.' and b.isValid=1))," +
				" (select count(num_id) from applicationChklistDomain f where f.str_application_id=a.lnApplicationId and f.int_branch_id=a.intBranchId and f.str_applicant_reply_status =0 ), " +
				" (select lfd.strLetterName from TextEditor_Dom ted,letterFormatDomain lfd where ted.str_app_no = a.lnApplicationId and ted.num_branch_id = a.intBranchId and ted.num_letter_id=lfd.id), " +
				" (select ted.num_letter_id from TextEditor_Dom ted,letterFormatDomain lfd where ted.str_app_no = a.lnApplicationId and ted.num_branch_id = a.intBranchId and ted.num_letter_id=lfd.id), " +
				" (select min(atr1.date) from PCApplicationTrackingDomain atr1 where a.lnApplicationId=atr1.lnApplicationId and a.intBranchId=atr1.intBranchId and atr1.intAppStatus=4)," +
				" (select pr.procedure_Name FROM Procedure_Mst_Domain pr WHERE pr.procedure_Id=a.intProcedureId)," +
				" (SELECT nc.num_nc_tracking FROM PCNCTrackingDomain nc WHERE nc.num_id=(select max(num_id) FROM PCNCTrackingDomain ncaux WHERE ncaux.str_application_id=a.lnApplicationId and ncaux.num_branch_id=a.intBranchId)),"
				+ "( select t.transactionNumber from  PaymentDetailsDomain t where t.num_id= (select max(p.num_id) from PaymentDetailsDomain p where p.applicationId=a.lnApplicationId and p.branch_id=a.intBranchId and p.payment_for='A' and p.payment_status in ('0300','300'))) as Tno,"
				+ "( select t.receipt_no from  PaymentDetailsDomain t where t.num_id= (select max(p.num_id) from PaymentDetailsDomain p where p.applicationId=a.lnApplicationId and p.branch_id=a.intBranchId and p.payment_for='A' and p.payment_status in ('0300','300'))) as receipt_no, " 
				+ "(select  max(gsp1.id) from  GlobalSurveillancePlan gsp1 where  gsp1.applicationid=a.lnApplicationId  and gsp1.branchid=a.intBranchId) " +
				" FROM PcGOLDomain a, firmDtlsDomain b, PCApplicationTrackingDomain c,bisApplicantStatusDomain e  " +
				" WHERE a.strGblUserId="+iUserId+" AND a.strISno not like 'COC%' AND a.lnApplicationId=b.lnApplicationId AND a.intBranchId=b.intFirmBranchId AND a.lnApplicationId=c.lnApplicationId " +
				" AND a.intBranchId=c.intBranchId and c.intAppStatus=e.intBisStatus  " +
				" AND c.num_id=(select max(num_id) from PCApplicationTrackingDomain m where m.lnApplicationId=c.lnApplicationId and m.intBranchId=a.intBranchId) " +
				"and (a.lnApplicationId,a.intBranchId) not in (select ldd.str_app_id,ldd.num_branch_id  from Licence_Details_Domain ldd where  ldd.str_app_id = a.lnApplicationId and ldd.num_branch_id = a.intBranchId ) " +
				"ORDER BY a.dtRegistration desc";
		System.out.println("getApplications: "+qry);
		List runQry = daoHelper.findByQuery(qry);
		return runQry;
	}
	
	//mansi modify
	//listApplication = appdao.getLicenseApps(userid);
	public List getLicenseApps(int iUserId){
		Calendar now = Calendar.getInstance();
	     int year=now.get(Calendar.YEAR) - (now.get(Calendar.MONTH) < 4?1:0);
	     String start=year + "-04-01";
	     String end=(year+1) + "-03-31";
	     
	
	    String qry = "select l.str_cml_no,l.str_firm_name,a.lnApplicationId,l.dt_granted_date,a.intBranchId,c.num_cml_status_id,e.str_applicant_status," +
	    		" l.dt_validity_date,a.intProcedureId ,l.str_is_no ||':'|| l.is_year," +
	    		" (select s.num_app_status from PCCMLSOMROMCancellationTracking s where s.num_id=(select max(num_id) FROM PCCMLSOMROMCancellationTracking ss WHERE ss.str_cml_no=l.str_cml_no and ss.num_branch_id=l.num_branch_id))," +
	    		" (select s.num_id from PCCMLSOMROMCancellationTracking s where s.num_id=(select max(num_id) FROM PCCMLSOMROMCancellationTracking ss WHERE ss.str_cml_no=l.str_cml_no and ss.num_branch_id=l.num_branch_id and ss.num_app_status=301))," +
	    		" (select s.num_id from PCCMLSOMROMCancellationTracking s where s.num_id=(select max(num_id) FROM PCCMLSOMROMCancellationTracking ss WHERE ss.str_cml_no=l.str_cml_no and ss.num_branch_id=l.num_branch_id and ss.num_app_status=104))," +
	    		" (select max(p.sno) from DiscrepancyDomain p where p.appId = l.str_app_id and p.numBranchId = l.num_branch_id)," +
	    		" (select count(intApplnStatusId) from Fee_Paid_Detail_Domain pcFee where pcFee.strAppId=l.str_app_id and pcFee.isValid=1 and pcFee.num_branch_id=l.num_branch_id and pcFee.intApplnStatusId=0 and pcFee.num_Amount <> '0' and to_char(pcFee.dt_Entry_Date,'yyyy-mm-dd') >='2017-05-15' and pcFee.dt_Entry_Date <= sysdate and  pcFee.numFeeId not in (1,7,28) and pcFee.numEntryEmpId<>"+iUserId+" and pcFee.numFeeId in (select b.numFeeId from Fee_Type_Domain b where b.strAmountType='Rs.' and b.isValid=1))," +
	    		" (select max(ss.userId) from inclusionVarietyDomain ss where l.str_cml_no = ss.cmlNumber and  l.num_branch_id = ss.branchId ), " +
	    		/*" (select aa.dt_validity_date from Licence_Details_Domain aa where trunc(sysdate)>=add_months(trunc(dt_validity_date),-3) and  trunc(sysdate)<=add_months(trunc(dt_validity_date),3) and 1>(select count(*) from LicenceRenewal_Performance_Domain b where aa.str_cml_no=b.str_cml_no and aa.num_branch_id=b.num_branch_id and b.num_Status_Id=27  and trunc(b.dt_entry_date)  between add_months(trunc(dt_validity_date),-3) and  add_months(trunc(dt_validity_date),3))  and aa.str_cml_no=l.str_cml_no and aa.num_branch_id=l.num_branch_id)  " +*/
	    		" (select aa.dt_validity_date from Licence_Details_Domain aa where trunc(sysdate)>=add_months(trunc(dt_validity_date),-3) " +
	    		" and 1=(select 1 from cmlstatusdetails_Domain cs where cs.str_cml_no=l.str_cml_no and cs.num_branch_id=l.num_branch_id and num_cml_status_id not in (205,206))" +
	    		" and 1>(select count(*) from PCRenewalTrackingDomain b where aa.str_cml_no=b.str_cml_no and aa.num_branch_id=b.num_branch_id  " +	    		
	    		" and trunc(b.dt_renewal_tracking) > add_months(trunc(dt_validity_date),-3) and dt_validity_date<(select coalesce(max(dt_renewal_tracking) ,'31-Dec-2099') from PCRenewalTrackingDomain c where c.str_cml_no=b.str_cml_no and c.num_branch_id=b.num_branch_id and c.num_renewal_tracking=100 " +
	    		" and num_id=(select max(zz.num_id) from PCRenewalTrackingDomain zz where   c.str_cml_no=zz.str_cml_no and c.num_branch_id=zz.num_branch_id) ) and b.num_renewal_tracking=89 " + 
	    		
	    		" and 1=(select 1 from cmlstatusdetails_Domain cs where cs.str_cml_no=l.str_cml_no and cs.num_branch_id=l.num_branch_id and num_cml_status_id not in (205,206))) " +
	    		" and aa.str_cml_no=l.str_cml_no and aa.num_branch_id=l.num_branch_id),(select somtrack.num_app_status FROM PCCMLSOMROMCancellationTracking somtrack WHERE somtrack.num_id=(select max(num_id) FROM PCCMLSOMROMCancellationTracking romtrack WHERE romtrack.str_cml_no=l.str_cml_no and romtrack.num_branch_id=l.num_branch_id)),  " +
	    		
	    		" (select max(lot.num_app_status) from PCCMLLotInspectionTracking lot where lot.num_id=(select max(lp.num_id) from PCCMLLotInspectionTracking lp where lp.str_cml_no=l.str_cml_no and lp.num_branch_id=l.num_branch_id)), "+
	    		"(select rr.num_renew_visible_applicant from PCRenewalTrackingDomain rr where rr.num_id=(select max(tt.num_id) FROM PCRenewalTrackingDomain tt WHERE tt.str_cml_no=l.str_cml_no and "+
	    		"tt.num_branch_id=l.num_branch_id and tt.num_renewal_tracking=604)), "	+
	    		"(select rr.num_renewal_tracking from PCRenewalTrackingDomain rr where rr.num_id=(select max(tt.num_id) FROM PCRenewalTrackingDomain tt WHERE tt.str_cml_no=l.str_cml_no and "+		
	    		"tt.num_branch_id=l.num_branch_id and tt.num_renewal_tracking=604)) ,l.str_is_no "	+
	    		" from PcGOLDomain a,Licence_Details_Domain l,cmlstatusdetails_Domain c,bisApplicantStatusDomain e" +
	    		" where a.lnApplicationId=l.str_app_id and a.intBranchId = l.num_branch_id and l.str_is_no not like 'COC%'" +
	    		" and l.str_cml_no = c.str_cml_no and l.num_branch_id = c.num_branch_id and c.num_cml_status_id = e.intBisStatus " +
	    		" and a.strGblUserId="+iUserId+ 
	    		" ORDER BY l.dt_granted_date desc";
		
		System.out.println("Applicant Query :::"+qry);		
		List runQry = daoHelper.findByQuery(qry);		 
		return runQry;
	}
	
	public List getHMLicenseApps(int iUserId){
		Calendar now = Calendar.getInstance();
	    	     
		    String qry = 	" select a.str_app_id,a.str_cml_no,a.dt_granted_date,a.dt_renewal_date, "+
		    				" a.str_product_id,a.str_is_no,a.num_branch_id,a.str_firm_name,b.num_cml_status_id, "+
		    				" (select q.strStatusName from Status_mst_domain q where q.numStatusId=b.num_cml_status_id),c.numOutletNo,   "+
		    				" (select a.numHallMarkAppBranchCtId from Hall_Branchdtl_Domain a where a.numHallMarkAppBranchCtId=(select max(numHallMarkAppBranchCtId) from Hall_Branchdtl_Domain br where br.str_app_id=c.str_app_id and br.num_branch_id=c.num_branch_id)), " +
		    				" (select aa.dt_validity_date from Hall_Licence_Details_Domain aa where aa.str_app_id=a.str_app_id and aa.num_branch_id=a.num_branch_id and  trunc(sysdate)>=add_months(trunc(a.dt_validity_date),-3)), " +
		    				" (select  hm.num_app_status from HMJewApplicationTrackingDomain hm where hm.num_id =(select max(b.num_id)  from HMJewApplicationTrackingDomain b where b.str_application_id=a.str_app_id and b.num_branch_id=a.num_branch_id)),"+
		    				" (select q.strStatusName from Status_mst_domain q where q.numStatusId=(select num_app_status from HMJewApplicationTrackingDomain where num_id =(select max(b.num_id)  from HMJewApplicationTrackingDomain b where b.str_application_id=a.str_app_id and b.num_branch_id=a.num_branch_id))) "+
		    				" FROM Hall_Licence_Details_Domain a, "+
		    				" hall_cmlstatusdetails_Domain b, "+
		    				" Application_HallMarking_Domain c "+
		    				" where c.num_Entry_Emp_Id = "+iUserId+"  "+
		    				" and b.num_id = (select max(num_id) from hall_cmlstatusdetails_Domain where str_cml_no=a.str_cml_no and num_branch_id=a.num_branch_id )  "+
		    				" and a.str_app_id=c.str_app_id  "+
		    				" and a.num_branch_id=c.num_branch_id "+
		    				" order by b.dt_entry_date desc ";
		    	   
	    List runQry = daoHelper.findByQuery(qry);	
		return runQry;
	}
	
	public List getHMLicenseApps_corp(int iUserId){
		Calendar now = Calendar.getInstance();
	     int year=now.get(Calendar.YEAR) - (now.get(Calendar.MONTH) < 4?1:0);
	  
		    String strQuery = " SELECT distinct d.str_application_id, a.str_cml_no,trunc(a.dt_operative_date)as dt_operative_date, ";
			 strQuery +="a.str_is_no,a.str_Firm_Name,(select c.str_status_name from bis_dev.gblt_status_mst c where c.num_status_id=b.num_cml_status_id) str_status_name, ";
			 strQuery +=			 	"	a.num_branch_id,b.num_cml_status_id num_app_status, ";
			 strQuery +=				"		d.num_Outlet_No,f.str_brnhnm, ";
			 strQuery +=				"		(e.str_address_1||','||e.str_address_2||','||e.str_city||','||(SELECT z.str_district_name ";
			 strQuery +=				"		  FROM gblt_district_mst z where z.num_district_id=e.num_district_id)||','||(SELECT x.str_state_name ";
			 strQuery +=				"		  FROM gblt_state_mst x where x.num_state_id=e.num_state_id)||','||(SELECT y.str_country_name ";
			 strQuery +=			"		  FROM gblt_country_mst y where y.num_country_id=e.num_country_id)||'-'||e.str_pin_code) firmadd, ";
			 strQuery +=			"		(f.str_address_1||','||f.str_address_2||','||f.str_city||','||(SELECT z.str_district_name ";
			 strQuery +=			"		  FROM gblt_district_mst z where z.num_district_id=f.num_district_id)||','||(SELECT x.str_state_name ";
			 strQuery +=			"		  FROM gblt_state_mst x where x.num_state_id=f.num_state_id)||','||(SELECT y.str_country_name ";
			 strQuery +=			"		  FROM gblt_country_mst y where y.num_country_id=f.num_country_id)||'-'||f.str_pincode) outletadd  ";
			 strQuery +=				"		FROM bis_hall.hall_cml_licence_detail a,  ";
			 strQuery +=				"		bis_hall.hall_cml_licence_status_dtl b, ";
			 strQuery +=				"		bis_hall.hall_appsub_jeweller d, "; 
			 strQuery +=				"		bis_hall.hall_appsub_jeweller_firm_details e, ";
			 strQuery +=			"		bis_hall.hall_appsub_jeweller_branch_details f  ";
			 strQuery +=				"		where a.str_cml_no=b.str_cml_no  ";
			 strQuery +=				"		and a.num_branch_id=b.num_branch_id  ";
			 strQuery +=				"		and a.str_app_id=d.str_application_id  ";
			 strQuery +=				"		and a.num_branch_id=d.num_location_id  ";
			 strQuery +=				"		and a.str_app_id=e.num_hallmarkapp_id  ";
			 strQuery +=				"		and a.num_branch_id=e.num_branch_id ";
			 strQuery +=				"		and a.str_app_id=f.num_hallmarkapp_id  ";
			 strQuery +=				"		and a.num_branch_id=f.num_branch_id ";
			 strQuery +=				"		and d.num_gbl_user_id="+iUserId+" and d.num_Outlet_No=1 ";		
			 strQuery += 				"		and b.num_id=(select max(q.num_id) from bis_hall.hall_cml_licence_status_dtl q where q.str_cml_no=a.str_cml_no and q.num_branch_id=a.num_branch_id) and b.num_cml_status_id not in (541)";
			 strQuery += 				"		ORDER BY trunc(a.dt_entry_date) desc ";


		System.out.println("userid Jeweller.."+iUserId);	   
	    List  runQry=jdbcTemplate.queryForList(strQuery);		
		return runQry;
	}

	public List getHMRECLicenseApps(int iUserId){
		Calendar now = Calendar.getInstance();
	     int year=now.get(Calendar.YEAR) - (now.get(Calendar.MONTH) < 4?1:0);
	     String start=year + "-04-01";
	     String end=(year+1) + "-03-31";
	     
	
	    
	     
		    String qry = 	"select a.str_app_id,a.str_cml_no,a.dt_granted_date,a.dt_renewal_date,a.str_product_id,a.str_is_no,a.num_region_id,a.str_ahc_name," +
		    		"b.num_app_status,(select q.strStatusName from Status_mst_domain q where q.numStatusId=b.num_app_status),c.strCenterName  " +
		    		"from licence_details_domain_ahcreg a," +
		    		"AHCRecog_ApplicationTrackingDomain b," +
		    		"ahcRecogApplicationSubDomain c  " +
		    		"where c.numEntryEmpId="+ iUserId +" " +
		    		"and c.strAppId=a.str_app_id "+
		    		"and c.numRegionId=a.num_region_id " +
		    		"and a.str_app_id=b.str_application_id and a.num_region_id=b.num_region_id " +
		    		"and b.num_id=(select max(num_id) from AHCRecog_ApplicationTrackingDomain m where m.str_application_id=a.str_app_id and m.num_region_id=a.num_region_id) " +
		    		"and a.num_id=(select min(num_id) from licence_details_domain_ahcreg q where q.str_app_id=a.str_app_id and q.num_region_id=a.num_region_id) " + 
		    		"ORDER BY b.dt_entry_date DESC";
	    System.out.println("qry rec.."+qry);
	   
	    List runQry = daoHelper.findByQuery(qry);
		
		// 
		 
		return runQry;
	}
	
	
	public List getLetterDetails(String appId,String branchId){
		String qry = "select ted.str_app_no,ted.date,lfd.strLetterName,ted.num_letter_id,ted.num_branch_id from TextEditor_Dom ted,letterFormatDomain lfd " +
					 "where ted.str_app_no = '"+appId+"' and ted.num_branch_id = "+branchId+" and ted.num_letter_id=lfd.id" ;
		List runQry = daoHelper.findByQuery(qry);
		 
		return runQry;
	}
	
	
	public List<PcGOLDomain> getSavedApplications(int iUserId){
		String qry = "SELECT apps FROM PcGOLDomain apps WHERE apps.intApplnStatusId =26 AND apps.strISno not like 'COC%' AND apps.strGblUserId="+iUserId+""
				+ " order by apps.dtRegistration desc ";
		List<PcGOLDomain> runQry = daoHelper.findByQuery(qry);
		return runQry;
	}
	
	public String getProcedureName(int iProcedureId){
		String stProcedureName = "";
		String qry = "SELECT p FROM Procedure_Mst_Domain p WHERE p.procedure_Id="+iProcedureId;
		List<Procedure_Mst_Domain> runQry = daoHelper.findByQuery(qry);
		if(runQry.size()>0){
			stProcedureName = runQry.get(0).getProcedure_Name();
			
		}
		return stProcedureName;
	}
	
	///get saved application list for Hallmarking(AHC) 
	public List<AHC_LOI_Domain> getSavedApplicationsHMAH(int iUserId){
		String qry = "SELECT apps FROM AHC_LOI_Domain apps WHERE apps.num_application_status_id=26 AND apps.numEntryEmpId="+iUserId;
		List<AHC_LOI_Domain> runQry = daoHelper.findByQuery(qry);
		 
		return runQry;
	}
	
	
	public List<TextEditor_Dom> getLetterView(String appId, String branchId,String letterId){
		String qry = "SELECT ted FROM TextEditor_Dom ted WHERE ted.str_app_no = '"+appId+"' and ted.num_branch_id="+Integer.parseInt(branchId)+" and ted.num_letter_id='"+letterId+"'" ;
		List<TextEditor_Dom> runQry = daoHelper.findByQuery(qry);
		return runQry;
	}
	
	public List<appliChngNameDomain> getCountNameChng(int userId){
		String qry = "SELECT ted FROM appliChngNameDomain ted WHERE ted.numEntryEmpId = "+userId+"" ;
		List<appliChngNameDomain> runQry = daoHelper.findByQuery(qry);
		return runQry;
	}
	
	
	///get saved application list for Hallmarking(Jeweller) 
		public List getSavedApplicationsHMJW(int iUserId){
//			select distinct str_app_id,num_brannch_id,str_app_name,b.str_firm_name from hall_appsub_jeweller as Application_HallMarking_Domain,hall_appsub_jeweller_firm_details as Hall_firmDtlsDomain where 
//			a.str_app_id=b.num_hallmarkapp_id
//			and a.num_branch_id=b.num_branch_id
//			and a.num_application_status_id=26 and a.num_entry_emp_id=33
			
			
			String qry = "SELECT distinct a.str_app_id,a.num_branch_id,a.strAppName,b.str_Firm_Name,coalesce(a.strOutletType,'-'),coalesce(a.numOutletNo,'0') FROM Application_HallMarking_Domain a,Hall_firmDtlsDomain b WHERE a.str_app_id=b.num_HallMarkApp_Id and a.num_branch_id=b.num_branch_id and a.intAppStatusId=26 AND a.num_Entry_Emp_Id="+iUserId+" order by a.dtEntryDate desc";
			List runQry = daoHelper.findByQuery(qry);
			 
			return runQry;
		}
	
	public int getCountofPCApplications(int iUserId_p, int iStatusId_p, int iSubmittedApps_p){
		String qry="select count(*) FROM bis_dev.pc_application_submission WHERE num_application_status_id="+iStatusId_p+" and num_gbl_user_id="+iUserId_p+" and str_is_number not like 'COC%'";
		int iApplications = jdbcTemplate.queryForObject(qry, Integer.class);
		return iApplications;
	}
	
	public List<PcGOLDomain> getRecentApplicationsData(int iUserId_p, int iStatusId_p){
		String qry = "";
		if(iStatusId_p == -1){
			qry = "SELECT apps FROM PcGOLDomain apps WHERE apps.intApplnStatusId !=26 AND apps.strGblUserId="+iUserId_p+"ORDER BY apps.dtRegistration DESC";
		}
		else{
			qry = "SELECT apps FROM PcGOLDomain apps WHERE apps.intApplnStatusId="+iStatusId_p+" AND apps.strGblUserId="+iUserId_p+"ORDER BY apps.dtRegistration DESC";
		}		
		List<PcGOLDomain> runQry = daoHelper.findByQuery(qry);
		return runQry;
	}
	
	public List<user_profile_domain> getUserProfileDetails(int iUserId_p){
		String query = "select r from user_profile_domain r where r.num_id =(select max(b.num_id) from user_profile_domain b where b.num_entry_user_id="+iUserId_p+") ";
		List<user_profile_domain> runQry = daoHelper.findByQuery(query);
		return runQry;
	}
	
	public  List<Licence_Brand_Detail_Domain> getBrandlist(String cml,int branchid){
		
		String qry = "SELECT apps FROM Licence_Brand_Detail_Domain apps WHERE apps.str_cml_num="+cml+" AND apps.num_branch_id="+branchid+" AND num_isvalid =1";

		 List<Licence_Brand_Detail_Domain> lst= daoHelper.findByQuery(qry);
		
		return lst;
	}


	public void addCMLStatusEntryinLog(cmlstatusdetails_Domain csdd) {
		daoHelper.maintainRecordInLog(new cmlstatusdetails_Domain_log(), csdd);
	}

	public List getPendingFeeCount(int userId){
		String qry="";
		//
//select count(distinct a.str_application_id) from pc_fee_paid_detail a,pc_application_submission b where num_payment_status_id=0
//and a.str_application_id=b.str_application_id
//and a.num_branch_id=b.num_location_id
//and b.num_gbl_user_id=212
		Calendar now = Calendar.getInstance();
	     int year=now.get(Calendar.YEAR) - (now.get(Calendar.MONTH) < 4?1:0);
//	      
	     String start=year + "-04-01";
	//      
	     String end=(year+1) + "-03-31";
	  //    
	     
		qry=qry+"select count(distinct a.strAppId) from Fee_Paid_Detail_Domain a,PcGOLDomain b where a.intApplnStatusId=0 and a.num_Amount <> '0' "+
				" and to_char(a.dt_Entry_Date,'yyyy-mm-dd') >='"+start+"' and a.dt_Entry_Date <= sysdate "+
		" and a.strAppId=b.lnApplicationId  and a.num_branch_id=b.intBranchId and b.strGblUserId="+userId;
		 	
		return daoHelper.findByQuery(qry);
	}
	
	
	public int getCountofAHCLOIApplications(int iUserId_p, int iStatusId_p, int iSubmittedApps_p){
		String qry="";
		if(iSubmittedApps_p==1){
			qry="SELECT apps FROM AHC_LOI_Domain apps WHERE apps.num_application_status_id !=26 AND apps.numEntryEmpId="+iUserId_p;
		}
		else{
			qry="SELECT apps FROM AHC_LOI_Domain apps WHERE apps.num_application_status_id="+iStatusId_p+" AND apps.numEntryEmpId="+iUserId_p;
		}
		List<AHC_LOI_Domain> runQry = daoHelper.findByQuery(qry);
		int iApplications = runQry.size();
		 
		return iApplications;
	}
	
	
	public int getCountofJewelApplications(int iUserId_p, int iStatusId_p, int iSubmittedApps_p){
		String qry="";
		if(iSubmittedApps_p==1){
			qry="SELECT apps FROM Application_HallMarking_Domain apps WHERE apps.intAppStatusId !=26 AND apps.num_Entry_Emp_Id="+iUserId_p;
		}
		else{
			qry="SELECT apps FROM Application_HallMarking_Domain apps WHERE apps.intAppStatusId="+iStatusId_p+" AND apps.num_Entry_Emp_Id="+iUserId_p+
	    		"and apps.str_app_id not in (select q.str_app_id from Hall_Licence_Details_Domain q where q.str_app_id=apps.str_app_id and q.num_branch_id=apps.num_branch_id)";
		}
		List<Application_HallMarking_Domain> runQry = daoHelper.findByQuery(qry);
		int iApplications = runQry.size();
		 
		return iApplications;
	}
	
	public int getsingleCountofJewelApplications(int iUserId_p, int iStatusId_p, int iSubmittedApps_p){
		String qry="";
		if(iSubmittedApps_p==1){
			qry="SELECT apps FROM Application_HallMarking_Domain apps WHERE apps.intAppStatusId !=26 AND apps.num_Entry_Emp_Id="+iUserId_p;
		}
		else{
			qry="SELECT apps FROM Application_HallMarking_Domain apps WHERE apps.intAppStatusId="+iStatusId_p+" AND apps.num_Entry_Emp_Id="+iUserId_p+
	    		"and apps.str_app_id not in (select q.str_app_id from Hall_Licence_Details_Domain q where q.str_app_id=apps.str_app_id and q.num_branch_id=apps.num_branch_id)";
		}
		List<Application_HallMarking_Domain> runQry = daoHelper.findByQuery(qry);
		int iApplications = runQry.size();
		 
		return iApplications;
	}
	
	
	//by siddharth for ahc recog
	 public int getCountofAhcRecogApplications(int iUserId_p, int iStatusId_p, int iSubmittedApps_p){
		String qry="";
		if(iSubmittedApps_p==1){
			qry="SELECT apps FROM Application_HallMarking_Domain apps WHERE apps.intAppStatusId !=26 AND apps.num_Entry_Emp_Id="+iUserId_p;
		}
		else{
			qry="SELECT apps FROM ahcRecogApplicationSubDomain apps WHERE apps.numStatus="+iStatusId_p+" AND apps.numEntryEmpId="+iUserId_p;
		}
		List<Application_HallMarking_Domain> runQry = daoHelper.findByQuery(qry);
		int iApplications = runQry.size();
		 
		return iApplications;
		}
	
	
	public List getExistenceAppCount(int userid){
		String qry="";
//		select count(num_sub_scheme_id) from user_sub_scheme_map
//		where num_user_id=22

		qry="SELECT c.num_sub_scheme_id from UserSubSchemeMappingDomain c where num_user_id="+userid;
		
		return daoHelper.findByQuery(qry);
	}
	
	
public List getHMApplications(int iUserId){
		
	/////////////// Query for Submiited Apps by Jeweller
//	select distinct str_app_id,str_app_name,b.str_firm_name,a.dt_entry_date,a.num_application_status_id from hall_appsub_jeweller a,hall_appsub_jeweller_firm_details b where 
//	a.str_app_id=b.num_hallmarkapp_id
//			and a.num_branch_id=b.num_branch_id
//			and a.num_entry_emp_id=33
	
		Calendar now = Calendar.getInstance();
	     int year=now.get(Calendar.YEAR) - (now.get(Calendar.MONTH) < 4?1:0);
//	      
	     String start=year + "-04-01";
	//      
	     String end=(year+1) + "-03-31";
	  //    
		
		String qry = "SELECT a.lnApplicationId, a.dtRegistration, a.strISno, b.strFirmName,c.intAppStatus ,e.str_applicant_status,";
		qry =qry + "(select atr.proposeDate from PCApplicationTrackingDomain atr where a.lnApplicationId=atr.lnApplicationId and a.intBranchId=atr.intBranchId and ";
		qry =qry + " atr.num_id = (select max(num_id) from PCApplicationTrackingDomain btr where a.lnApplicationId=btr.lnApplicationId and a.intBranchId=btr.intBranchId and btr.proposeDate is not null))";
		qry=qry+",a.intBranchId, a.intProcedureId,";
//		(select count(num_payment_status_id) from pc_fee_paid_detail f where f.str_application_id=a.str_application_id and f.num_branch_id=a.num_location_id and num_payment_status_id=0)
		qry=qry+"(select count(intApplnStatusId) from Fee_Paid_Detail_Domain f where f.strAppId=a.lnApplicationId and f.num_branch_id=a.intBranchId and f.intApplnStatusId=0 and f.num_Amount <> '0' and to_char(f.dt_Entry_Date,'yyyy-mm-dd') >='"+start+"' and f.dt_Entry_Date <= sysdate ) ";
		qry = qry + " FROM PcGOLDomain a, firmDtlsDomain b, PCApplicationTrackingDomain c, Status_mst_domain d,bisApplicantStatusDomain e WHERE a.strGblUserId="+iUserId+"";
		qry = qry + " AND a.lnApplicationId=b.lnApplicationId AND a.lnApplicationId=c.lnApplicationId AND c.intAppStatus=d.numStatusId and c.intAppStatus=e.intBisStatus ";
		qry = qry + " AND c.num_id=(select max(num_id) from PCApplicationTrackingDomain m where m.lnApplicationId=c.lnApplicationId and m.intBranchId=a.intBranchId)ORDER BY a.lnApplicationId ASC";
		List runQry = daoHelper.findByQuery(qry);
		return runQry;
	}
	


public List getHMJWSubmittedApplications(int iUserId){
	
	/////////////// Query for Submiited Apps by Jeweller
//	select distinct a.str_app_id,a.num_branch_id,str_app_name,b.str_firm_name,a.dt_entry_date,a.num_application_status_id,c.str_status_name
//	from hall_appsub_jeweller a,hall_appsub_jeweller_firm_details b,bis_dev.gblt_status_mst c
//	where a.str_app_id=b.num_hallmarkapp_id
//	and a.num_branch_id=b.num_branch_id
//	and a.num_application_status_id=c.num_status_id
//	and a.num_entry_emp_id=33

	/*SELECT distinct a.str_application_id,a.dt_entry_date,a.str_IS_number,a.num_location_id,
	coalesce(a.str_Outlet_Type,'-'),
		coalesce(a.num_Outlet_No,'0'),
		b.str_Firm_Name,d.num_app_status,
		c.str_applicant_status
		
		FROM bis_hall.hall_appsub_jeweller a,
		     bis_hall.hall_appsub_jeweller_firm_details b,
		     gblt_bis_applicant_status c,
	             bis_hall.hm_jeweler_application_tracking d,
		     gblt_status_mst e 
		 
	  where  a.num_gbl_user_id=33
	  and a.str_application_id=b.num_HallMarkApp_Id 
	  and  a.num_location_id=b.num_branch_id
	  and a.num_location_id=d.num_branch_id 
	  and a.str_application_id=d.str_application_id
	  and c.num_bis_status=d.num_app_status
	  and d.num_app_status=e.num_status_id
	  and c.num_bis_status=d.num_app_status
	  and 
	d.num_id=(select max(num_id) from bis_hall.hm_jeweler_application_tracking m where m.str_application_id=d.str_application_id and m.num_branch_id=a.num_location_id)		   
	order by 1*/
	
		Calendar now = Calendar.getInstance();
	     int year=now.get(Calendar.YEAR) - (now.get(Calendar.MONTH) < 4?1:0);
//	      
	     String start=year + "-04-01";
	//      
	     String end=(year+1) + "-03-31";
	  //    
	     String qry = "SELECT distinct a.str_app_id,a.dtEntryDate,a.strAppName,b.str_Firm_Name,a.num_branch_id," +
	     		"d.num_app_status,c.str_applicant_status,coalesce(a.strOutletType,'-'),coalesce(a.numOutletNo,'0') " +
	     		"FROM Application_HallMarking_Domain a,Hall_firmDtlsDomain b,bisApplicantStatusDomain c," +
	     		"HMJewApplicationTrackingDomain d,Status_mst_domain e  "+
	     		"where  a.num_Entry_Emp_Id="+iUserId+
	    		 " and a.str_app_id=b.num_HallMarkApp_Id "+
	    		 "and  a.num_branch_id=b.num_branch_id "+
	    		  "and a.num_branch_id=d.num_branch_id "+
	    		  "and a.str_app_id=d.str_application_id "+
	    		  "and c.intBisStatus=d.num_app_status "+
	    		 "and d.num_app_status=e.numStatusId "+
	    		 " and c.intBisStatus=d.num_app_status"+
	    		  " and "+
	    		"d.num_id=(select max(m.num_id) from HMJewApplicationTrackingDomain m where m.str_application_id=d.str_application_id and m.num_branch_id=a.num_branch_id) " +
	    		"and a.str_app_id not in (select q.str_app_id from Hall_Licence_Details_Domain q where q.str_app_id=a.str_app_id and q.num_branch_id=a.num_branch_id) "+	   
	    		"order by d.dt_entry_date desc";
	     
	      
	     
	     
			List runQry = daoHelper.findByQuery(qry);
		return runQry;
	}


public List getHMJWSubmittedApplications_corporateConversion(int iUserId){
		
		Calendar now = Calendar.getInstance();
	     int year=now.get(Calendar.YEAR) - (now.get(Calendar.MONTH) < 4?1:0);
	      
	     String start=year + "-04-01";
	     
	     String end=(year+1) + "-03-31";
	   
	     String qry = "SELECT distinct a.str_app_id,a.dtEntryDate,a.strAppName,b.str_Firm_Name,a.num_branch_id," +
	     		"d.num_app_status,c.str_applicant_status,coalesce(a.strOutletType,'-'),coalesce(a.numOutletNo,'0') " +
	     		"FROM Application_HallMarking_Domain_corporateConversion a,Hall_firmDtlsDomain_corporateConversion b,bisApplicantStatusDomain c," +
	     		"HMJewApplicationTrackingDomain d,Status_mst_domain e  "+
	     		"where  a.num_Entry_Emp_Id="+iUserId+
	    		 " and a.str_app_id=b.num_HallMarkApp_Id "+
	    		 "and  a.num_branch_id=b.num_branch_id "+
	    		  "and a.num_branch_id=d.num_branch_id "+
	    		  "and a.str_app_id=d.str_application_id "+
	    		  "and c.intBisStatus=d.num_app_status "+
	    		 "and d.num_app_status=e.numStatusId  and a.intAppStatusId="+27+" "+
	    		 //" and c.intBisStatus=d.num_app_status"+
	    		  " and "+
	    		"d.num_id=(select max(m.num_id) from HMJewApplicationTrackingDomain m where m.str_application_id=d.str_application_id and m.num_branch_id=a.num_branch_id) " +
	    		//"and a.str_app_id not in (select q.str_app_id from Hall_Licence_Details_Domain q where q.str_app_id=a.str_app_id and q.num_branch_id=a.num_branch_id) "+	   
	    		"order by d.dt_entry_date desc";
	     
	     
			List runQry = daoHelper.findByQuery(qry);
		return runQry;
	}

public void delPreRecrdHM_corporateConversion(int iUserId_p)
{
	
	String qry = "Select c from Application_HallMarking_Domain_corporateConversion c where c.is_valid=1 and c.intAppStatusId=26 and num_Entry_Emp_Id="+iUserId_p+" ";
	List<Application_HallMarking_Domain_corporateConversion> c = new ArrayList<Application_HallMarking_Domain_corporateConversion>();
	if(daoHelper.findByQuery(qry).size()>0){
		c=daoHelper.findByQuery(qry);
		for(int i=0;i<daoHelper.findByQuery(qry).size();i++){
			String qry1="DELETE FROM Application_HallMarking_Domain_corporateConversion c where c.str_app_id='"+c.get(0).getStr_app_id()+"' and c.num_branch_id="+c.get(0).getNum_branch_id()+" ";
			daoHelper.deleteByQuery(qry1);
			
			String qry2="DELETE FROM Hall_firmDtlsDomain_corporateConversion c where c.num_HallMarkApp_Id='"+c.get(0).getStr_app_id()+"' and c.num_branch_id="+c.get(0).getNum_branch_id()+" ";
			daoHelper.deleteByQuery(qry2);
			
			String qry3="DELETE FROM Hall_Branchdtl_Domain_corporateConversion c where c.str_app_id='"+c.get(0).getStr_app_id()+"' and c.num_branch_id="+c.get(0).getNum_branch_id()+" ";
			daoHelper.deleteByQuery(qry3);
		}
	}
	
}
public List getHMAHSubmittedApplications(int iUserId){
	
	/////////////// Query for Submiited Apps by AHC LOI
//	select distinct str_app_id,str_app_name,b.str_firm_name,a.dt_entry_date,a.num_application_status_id from hall_appsub_jeweller a,hall_appsub_jeweller_firm_details b where 
//	a.str_app_id=b.num_hallmarkapp_id
//			and a.num_branch_id=b.num_branch_id
//			and a.num_entry_emp_id=33
	
		Calendar now = Calendar.getInstance();
	     int year=now.get(Calendar.YEAR) - (now.get(Calendar.MONTH) < 4?1:0);
//	      
	     String start=year + "-04-01";
	//      
	     String end=(year+1) + "-03-31";
	  //    
		
		/*String qry = "SELECT a.lnApplicationId, a.dtRegistration, a.strISno," +
				" b.strFirmName," +
				"c.intAppStatus ," +
				"e.str_applicant_status,";
		qry =qry + "(select atr.proposeDate from PCApplicationTrackingDomain atr where a.lnApplicationId=atr.lnApplicationId and a.intBranchId=atr.intBranchId and ";
		qry =qry + " atr.num_id = (select max(num_id) from PCApplicationTrackingDomain btr where a.lnApplicationId=btr.lnApplicationId and a.intBranchId=btr.intBranchId and btr.proposeDate is not null))";
		qry=qry+",a.intBranchId, a.intProcedureId,";
//		(select count(num_payment_status_id) from pc_fee_paid_detail f where f.str_application_id=a.str_application_id and f.num_branch_id=a.num_location_id and num_payment_status_id=0)
		qry=qry+"(select count(intApplnStatusId) from Fee_Paid_Detail_Domain f where f.strAppId=a.lnApplicationId and f.num_branch_id=a.intBranchId and f.intApplnStatusId=0 and f.num_Amount <> '0' and to_char(f.dt_Entry_Date,'yyyy-mm-dd') >='"+start+"' and f.dt_Entry_Date <= sysdate ) ";
		qry = qry + " FROM PcGOLDomain a, firmDtlsDomain b, PCApplicationTrackingDomain c, Status_mst_domain d,bisApplicantStatusDomain e WHERE a.strGblUserId="+iUserId+"";
		qry = qry + " AND a.lnApplicationId=b.lnApplicationId AND a.lnApplicationId=c.lnApplicationId AND c.intAppStatus=d.numStatusId and c.intAppStatus=e.intBisStatus ";
		qry = qry + " AND c.num_id=" +
				"(select max(num_id) from PCApplicationTrackingDomain m " +
				"where m.lnApplicationId=c.lnApplicationId and m.intBranchId=a.intBranchId)ORDER BY a.lnApplicationId ASC";*/
	     
	     
	     

/*SELECT a.str_application_id, a.dt_entry_date, a.str_is_no,a.str_ahc_name,c.num_app_status ,e.str_applicant_status,a.num_branch_id,
	(select count(num_payment_status_id) from bis_hall.hall_fee_paid_detail f where f.str_application_id=a.str_application_id and f.num_branch_id=a.num_branch_id and f.dt_entry_date <= sysdate ) 
FROM bis_hall.hall_ahc_loi a, bis_hall.hm_AHCLOI_application_tracking c, gblt_status_mst d, Gblt_bis_applicant_status e 
 WHERE a.num_entry_emp_id="+iUserId+" AND a.str_application_id=c.str_application_id AND	c.num_app_status=d.num_status_id AND c.num_app_status=e.num_bis_status AND
 c.num_id=(select max(num_id) from bis_hall.hm_AHCLOI_application_tracking m where m.str_application_id=c.str_application_id and m.num_branch_id=a.num_branch_id)
 ORDER BY c.dt_entry_date DESC*/
	     
	     
	     String qry = "SELECT a.str_application_id, a.date, a.str_is_no,a.strAHCName," +
	     		"c.num_app_status ,e.str_applicant_status,a.num_branch_id," +
	     		"(select count(intApplnStatusId) from Fee_Paid_Detail_Domain_hall f " +
	     		"where f.strAppId=a.str_application_id and f.num_branch_id=a.num_branch_id and f.numEntryEmpId <= sysdate ) " +
	     		"FROM AHC_LOI_Domain a, HM_AHCLOI_ApplicationTrackingDomain c, Status_mst_domain d, bisApplicantStatusDomain e " +
	     		"WHERE a.numEntryEmpId="+iUserId+" " +
	     				"AND a.str_application_id=c.str_application_id " +
	     				"AND c.num_app_status=d.numStatusId " +
	     				"AND c.num_app_status=e.intBisStatus " +
	     				"AND c.num_id=(select max(num_id) from HM_AHCLOI_ApplicationTrackingDomain m " +
	     				"where m.str_application_id=c.str_application_id " +
	     				"and m.num_branch_id=a.num_branch_id) " +
	     				"ORDER BY c.dt_entry_date DESC";  
	     
	     
		List runQry = daoHelper.findByQuery(qry);
		return runQry;
	}

public List<LicenceRenewDomain> getTheRenewalList(String appId, int branchId, String cmlNo){
	
	String qry = "SELECT m FROM LicenceRenewDomain m WHERE m.application_id like trim('"+appId+"') AND m.boid='"+branchId+"' and m.cmlno='"+cmlNo+"'";
	return daoHelper.findByQuery(qry);
	
}

public List<Hall_Licence_Details_Domain> get_licence_Data(String appID,
		int branch_Id, String cmlNum) {
	String query = "select m from Hall_Licence_Details_Domain m where m.str_app_id='"+appID+"' and m.num_branch_id='"+branch_Id+"' and m.str_cml_no='"+cmlNum+"'";
	 
	return daoHelper.findByQuery(query);
}	

public void saveInTheJewelTrackerRenewel(HMJewApplicationTrackingDomain domain){
	
	daoHelper.merge(HMJewApplicationTrackingDomain.class, domain);
	
}

/*public String getTRFailStatus(String cmlN) 
{
	String status="";
	String str="Select c from Lab_Hall_Test_Request_Master_Domain c where c.strLicenceNum='"+cmlN+"' and (c.numTestReportStatus=282 || c.numTestReportStatus=283)";
	List<Lab_Hall_Test_Request_Master_Domain> listData=daoHelper.findByQuery(str);
	if(listData.size()>0)
	{
		status="fail";
	}else
	{
		status="pass";
	}
	return status;
}*/


public String getTRFailStatus(String cmlN) 
{
	
	String status="";
	String str="Select c from Lab_Hall_Test_Request_Master_Domain c where c.strLicenceNum='"+cmlN+"' and (c.numTestReportStatus=282 or c.numTestReportStatus=283)";
	List<Lab_Hall_Test_Request_Master_Domain> listData=daoHelper.findByQuery(str);
	 listData=daoHelper.findByQuery(str);
	if(listData.size()>0)
	{
		status="fail";
	}else
	{
		status="pass";
	}
	return status;
}



public List getFailedTRList(String licNum) 
{
	String str="Select c.strLicenceNum,c.sampleDate,c.numTestReportStatus,c.strMarketServillanceId from Lab_Hall_Test_Request_Master_Domain c where c.strLicenceNum='"+licNum+"' and c.numTestReportStatus=131";
	List listData=daoHelper.findByQuery(str);
	return listData;
}

public List<HMJewApplicationTrackingDomain> getLatestTrack(String appid,int branchid,int status){
	
	/*String qry="select c from HMJewApplicationTrackingDomain c where  " +
			"c.num_id=(select c from HMJewApplicationTrackingDomain c where c.str_application_id='"+appid+" and c.num_branch_id="+branchid+" and c. )";*/
	
	return null;
	
}

public void deleteFromHallRenewalDomain(LicenceRenewDomain domain){
	
	daoHelper.delete(domain);
	
}

public List<HMJewApplicationTrackingDomain> getTheTrackingDtl(String appId, int branchId){
	//String qry = "Select p from HMJewApplicationTrackingDomain p where p.str_application_id = '"+appId+"' and p.num_branch_id = '"+branchId+"'";
	String qry="SELECT p FROM HMJewApplicationTrackingDomain p WHERE p.num_id = (SELECT max(a.num_id) FROM HMJewApplicationTrackingDomain a WHERE a.str_application_id='"+appId+"' and a.num_branch_id = '"+branchId+"')";
	return daoHelper.findByQuery(qry);
}


public List<inclusionVarietyDomain> getTheInclusionData(String cml, int branchId){
	
	String qry = "Select p from inclusionVarietyDomain p where p.cmlNumber = '"+cml+"' and p.branchId = '"+branchId+"'";
	return daoHelper.findByQuery(qry);
}

public void deleteFromInclusion(inclusionVarietyDomain domain){
	
	daoHelper.delete(domain);
	
}

public List<componentDetailsDomain> getTheComponentData(String cml, int branchId){
	
	String qry = "Select p from componentDetailsDomain p where p.cmlNumber = '"+cml+"' and p.branchId = '"+branchId+"'";
	return daoHelper.findByQuery(qry);
	
}
//change by amit
public void deleteFromComponent(componentDetailsDomain domain){
	
	daoHelper.delete(domain);
}


//##################################################################################################//
//	Methods added by Dhruv to get number of Applications in Submitted and Drafts as well as licenses
//##################################################################################################//

public int getCountPCSubmittedApps(int iUserId){
	int iCount = 0;
	String qry = "select count(*) from bis_dev.pc_application_submission a " +
			" where num_application_status_id in (27,285) and num_gbl_user_id="+iUserId+
			" and (a.str_application_id,a.num_location_id) not in (select str_app_id,num_branch_id from bis_dev.cml_licence_detail)"
			+ " and a.str_is_number not like 'COC%'";
	
	
	iCount = jdbcTemplate.queryForObject(qry, Integer.class);
	
	return iCount;
}

public int getCountPCOperativeLicences(int iUserId){
	int iCount = 0;
	String qry = "select count(*) from bis_dev.cml_licence_detail a, pc_application_submission b " +
			"where a.str_app_id=b.str_application_id " +
			"and a.num_branch_id=b.num_location_id and b.num_gbl_user_id="+iUserId;
	
	iCount = jdbcTemplate.queryForObject(qry, Integer.class);
	
	return iCount;
}

public int getFeePendingFlagForLicense(int iUserId){
	int iFlag = 0;
	/*String qry = " select distinct nvl(1,0) from pc_fee_paid_detail a,pc_application_submission b " +
			" where a.str_application_id=b.str_application_id and a.num_branch_id=b.num_location_id and  num_gbl_user_id="+iUserId+
			" and num_payment_status_id=0 and num_isvalid=1 " +
			" and (b.str_application_id,b.num_location_id)  in (select str_app_id,num_branch_id from bis_dev.cml_licence_detail)";*/
	String qry = "select count(*)" +
			" from pc_fee_paid_detail a,pc_application_submission b,pc_application_tracking c " +
			" where a.str_application_id=b.str_application_id" +
			" and a.num_branch_id=b.num_location_id " +
			" and a.str_application_id=c.str_application_id " +
			" and a.num_branch_id=c.num_branch_id " +
			" and c.num_id=(select max(num_id) from pc_application_tracking cc where cc.str_application_id=c.str_application_id " +
			" and cc.num_branch_id=c.num_branch_id) " +
			" and c.num_app_status<>3 and a.NUM_Amount<>'0'" +
			" and trunc(a.DT_ENTRY_DATE)<=trunc(sysdate) and num_gbl_user_id="+iUserId+" and num_payment_status_id=0 and num_isvalid=1" +
			" and ( b.str_application_id,b.num_location_id)  in (select str_app_id,num_branch_id from bis_dev.cml_licence_detail)" +
			" and ( a.NUM_Fee_id  in (select fee_type_d1_.num_fee_id" +
			" from bis_dev.pc_fee_mst fee_type_d1_  where fee_type_d1_.Str_Amount_Type='Rs.'  and fee_type_d1_.NUM_ISVALID=1))and a.num_entry_emp_id <>"+iUserId;
	try{
		iFlag = jdbcTemplate.queryForObject(qry, Integer.class);
	}
	catch(EmptyResultDataAccessException ex){
		 
	}
	catch(Exception e){
		 
	}
	
	
	return iFlag;
}

public int getFeePendingFlagForApps(int iUserId){
	int iFlag = 0;
	/*String qry = "select distinct nvl(1,0) from pc_fee_paid_detail a,pc_application_submission b " +
			" where a.str_application_id=b.str_application_id and a.num_branch_id=b.num_location_id and num_gbl_user_id="+iUserId+
			" and num_payment_status_id=0 and num_isvalid=1 " +
			" and (b.str_application_id,b.num_location_id) not in (select str_app_id,num_branch_id from bis_dev.cml_licence_detail)";*/
	String qry = "select count(*)" +
			" from pc_fee_paid_detail a,pc_application_submission b,pc_application_tracking c " +
			" where a.str_application_id=b.str_application_id" +
			" and a.num_branch_id=b.num_location_id " +
			" and a.str_application_id=c.str_application_id " +
			" and a.num_branch_id=c.num_branch_id " +
			" and c.num_id=(select max(num_id) from pc_application_tracking cc where cc.str_application_id=c.str_application_id " +
			" and cc.num_branch_id=c.num_branch_id) " +
			" and c.num_app_status<>3 and a.NUM_Amount<>'0'" +
			" and trunc(a.DT_ENTRY_DATE)<=trunc(sysdate ) and num_gbl_user_id="+iUserId+" and num_payment_status_id=0 and num_isvalid=1" +
			" and ( b.str_application_id,b.num_location_id) not in (select str_app_id,num_branch_id from bis_dev.cml_licence_detail)" +
			" and ( a.NUM_Fee_id  in (select fee_type_d1_.num_fee_id" +
			" from bis_dev.pc_fee_mst fee_type_d1_  where fee_type_d1_.Str_Amount_Type='Rs.'  and fee_type_d1_.NUM_ISVALID=1))and a.num_entry_emp_id <>"+iUserId;
	System.out.println("qry.."+qry);
	try{
		iFlag = jdbcTemplate.queryForObject(qry, Integer.class);
	}
	catch(EmptyResultDataAccessException ex){
		 
	}
	catch(Exception e){
		 
	}
	
	
	return iFlag;
}


//###########################################################################//
//###########################################################################//
// New Marking Label Upload Code By Dhruv									 //
//###########################################################################//
//###########################################################################//	
public List<HashMap<String,String>> getBrandData(String stCmlNo, int iBranchId){
	List<HashMap<String,String>> lhm = new ArrayList<HashMap<String,String>>();
	String qry = "SELECT p FROM Licence_Brand_Detail_Domain p WHERE p.str_cml_num='"+stCmlNo+"' and p.num_branch_id="+iBranchId+" and p.num_isvalid=1";
	List<Licence_Brand_Detail_Domain> runQry = daoHelper.findByQuery(qry);
	if(runQry.size()>0){
		for(int i=0;i<runQry.size();i++){
			HashMap<String,String> hm = new HashMap<String,String>();
			Licence_Brand_Detail_Domain lbdd = runQry.get(i);
			hm.put("id", ims.Jcrypt(lbdd.getNum_real_brand_id()+""));
			hm.put("name", lbdd.getStr_brand_name());
			lhm.add(hm);
		}
		
	}
	return lhm;
}



//###########################################################################//
//###########################################################################//
// New Marking Label Upload Code By Dhruv									 //
//###########################################################################//
//###########################################################################//	

//###########################################################################//
//###########################################################################//
//Self SOM Methods By Dhruv									                 //
//###########################################################################//
//###########################################################################//
public HashMap<String,String> getSelfSOMCMLDetails(String stCmlNo, int iBranchId){
	HashMap<String,String> hm = new HashMap<String,String>();
	
	String qry = "SELECT p.str_firm_name, p.dt_granted_date, p.str_fac_address1||','||p.str_fac_address2||','||p.str_fac_city_name||','||p.stDistrictName||','||p.stStateName||','||p.stCountryName||' '||p.num_fac_pin_code FROM Licence_Details_Domain p WHERE p.str_cml_no='"+stCmlNo+"' and p.num_branch_id="+iBranchId;
	List runQry = daoHelper.findByQuery(qry);
	if(runQry.size()>0){
		SimpleDateFormat smdate = new SimpleDateFormat("dd-MM-yyyy");
		Object[] ldd = (Object[]) runQry.get(0);
		hm.put("firm_name", ldd[0]+"");
		Date dtGrantedDate = (Date) ldd[1];
		String StGrantedDate = smdate.format(dtGrantedDate);
		hm.put("granted_date", StGrantedDate);
		hm.put("fac_address", ldd[2]+"");
	}
	
	return hm;
}

//###########################################################################//
//###########################################################################//
//Self SOM Methods By Dhruv									                 //
//###########################################################################//
//###########################################################################//

public List<Inclusion_Tracking_Domain> theInclusionTrackingListOnAppIdAndBranchId(String appId, int branchId){
	
	String qry = "SELECT p from Inclusion_Tracking_Domain p where p.num_id = (select max(m.num_id) from Inclusion_Tracking_Domain m where m.lnApplicationId = '"+appId+"' and m.intBranchId = '"+branchId+"')";
	return daoHelper.findByQuery(qry);
	
}



public String saveMSCDGSTNo(String stGstNo, int iUserId){
	String stResponse = "";
	
	MSCD_GST_Applicant a=new MSCD_GST_Applicant();
	try{
		a.setGstin_no(stGstNo);
		a.setUser_id(iUserId+"");
		daoHelper.merge(MSCD_GST_Applicant.class, a);	
		stResponse = "Success";
	}
	catch(Exception e){
		stResponse = "Error";
	}
	
	return stResponse;
}

public List<Map<String, Object>> getsti_and_marking_fee_mapping(int iUserId) {
	String sql_query="select b.num_id, app_id::text, do_check::text, applicant_check::text, split_part(b.remark_by_do,'--',1) as remark_by_do from pc_application_submission a, gblt_sti_and_marking_fee_mapping b where a.str_application_id=b.app_id and b.applicant_check =0 and b.do_check=1 and a.num_gbl_user_id="+iUserId;
	System.out.println("getsti_and_marking_fee_mapping: "+sql_query);
	return jdbcTemplate.queryForList(sql_query);
}

public boolean submit_accptance(int num_id, int acptstidoc, int acptmarkingfee, String remark) {
	String sql_query="update bis_dev.gblt_sti_and_marking_fee_mapping set applicant_check=1 , sti_doc_check_by_application="+acptstidoc+", marking_fee_check_by_application="+acptmarkingfee+", remark_by_applicant='"+remark+"', reply_date=LOCALTIMESTAMP where num_id="+num_id;
	boolean result=false;
	System.out.println("submit_accptance: "+sql_query);
	jdbcTemplate.execute(sql_query);
	result=true;
	return result;
}

public List<Map<String, Object>> getchecksum(String ISNO) {
	//String[] tempisno=ISNO.split(":");
	String sql_query="select str_doc_chksum_name,str_sti_doc from gblt_sti_document_mst where str_standard_no='"+ISNO+"' and num_isvalid=1";
	System.out.println("getchecksum: "+sql_query);
	return jdbcTemplate.queryForList(sql_query);
}

public List<Renewal_HallMarking_Domain> get_Renewal_Data(String appID,int branch_Id) {
	String query = "select m from Renewal_HallMarking_Domain m where m.str_app_id='"+appID+"' and m.num_branch_id='"+branch_Id+"' order by  num_id DESC ";
	System.out.println("flag   query "+query);	 
	return daoHelper.findByQuery(query);
}

public int getSurveillanceDate(String appId) {
	try {
//	String query="select * from gblt_surveillance_plan where applicationid='"+appId+"' and num_is_valid=1 and to_char(inspection_to_be_carried_on)=to_char(CURRENT_DATE)";
	
		String query="select * from gblt_surveillance_plan where applicationid= '"+appId+"'\r\n" + 
				"and num_is_valid=1 and ((trunc(inspection_to_be_carried_on)=trunc(CURRENT_DATE))\r\n" + 
				"or (select 1 from dual where trunc(sysdate) in (select to_date(trim(regexp_split_to_table(replace(replace(inspectation_date,'[',null),']',null),',')) )\r\n" + 
				"	  from bis_mobile.mobile_cml_insp_periodic_dtl_general_info where application_id='"+appId+"'))=1)";
		
		if(!jdbcTemplate.queryForList(query).isEmpty()) {
		return 1;
	}
	else {
		return 0;
	}
	}
	catch(Exception e) {
		e.printStackTrace();
		return 0;
	}
	
}

/*
 * public int getSurveillanceDetails(String appId, String cmlNo) { String
 * query="select nvl(num_id,0) from gblt_surveillance_plan a where a.applicationid='"
 * +appId+"' and a.cmlno='"
 * +cmlNo+"' and a.num_is_valid=1 and a.year = EXTRACT (YEAR FROM ADD_MONTHS (sysdate, -3)) and inspection_to_be_carried_on is not null "
 * + " union\r\n" +
 * " select 0 from dual where 1>(select count(*) from gblt_surveillance_plan where applicationid='"
 * +appId+"' and cmlno='"
 * +cmlNo+"' and num_is_valid=1 and year = EXTRACT (YEAR FROM ADD_MONTHS (sysdate, -3))\r\n"
 * + ") "; System.out.println("query is :"+query);
 * System.out.println("data is  :"+jdbcTemplate.queryForObject(query,
 * Integer.class));
 * 
 * 
 * return jdbcTemplate.queryForObject(query, Integer.class); }
 */

public int getSurveillanceDetails(String appId, String cmlNo) {
	try {
		String query = "select num_id from (select m.num_id ,nvl((select 1 from dual where trunc(sysdate) in (select to_date(regexp_split_to_table (replace(replace(replace(inspectation_date,' ',null),']',null),'[',null),','))  " + 
				"from bis_mobile.mobile_cml_insp_periodic_dtl_general_info km where km.inspectation_id=m.num_id " + 
				"union " + 
				"select trunc(inspection_to_be_carried_on))),0) as flag " + 
				"from bis_dev.gblt_surveillance_plan m where inspection_type = 4 and applicationid='"+appId+"' and cmlno='"+cmlNo+"') where flag=1";
		System.out.println("query is :" + query);
		//System.out.println("data is  :" + jdbcTemplate.queryForObject(query, Integer.class));
		if (!jdbcTemplate.queryForList(query).isEmpty()) {
			return jdbcTemplate.queryForObject(query, Integer.class);
		} else {
			return 0;
		}
	} catch (Exception e) {
		e.printStackTrace();
		return 0;
	}

}

//ashish new  add new method factory test report....

public int getSurveillanceDetailsPc(String appId, String cmlNo) {
	try {
		String query = "select num_id from (select m.num_id ,nvl((select 1 from dual where trunc(sysdate) in\r\n"
				+ "(select to_date(regexp_split_to_table (replace(replace(replace(inspectation_date,' ',null),']',null),'[',null),','))\r\n"
				+ "from bis_mobile.mobile_preliminary_insp_general_info km where km.insp_id=m.num_id union select trunc(inspection_to_be_carried_on))),0) \r\n"
				+ "as flag from bis_dev.gblt_surveillance_plan m where inspection_type in(1,6) and num_plan_status=1007 and applicationid='"+appId+"')";
		
		System.out.println("query is :" + query);
		System.out.println("data is  :" + jdbcTemplate.queryForObject(query, Integer.class));
		if (!jdbcTemplate.queryForList(query).isEmpty()) {
			return jdbcTemplate.queryForObject(query, Integer.class);
		} else {
			return 0;
		}
	} catch (Exception e) {
		e.printStackTrace();
		return 0;
	}

}



public Map<String,Object> getSurveillanceFactoryTestReportFlag(String appID, String cmlNo, String survId) {
	try {
	String query ="Select num_isfactoryTestingConducted as isfactoryTestingConducted ,num_isfactoryTestingConductedMulti as isFactoryTestingConductedMulti  from bis_dev.pc_is_factory_testing_conducted where str_cmlNo= '"+cmlNo+"' and str_surviellanceId= '"+survId+"' and str_application_id='"+appID+"'";
	System.out.println("query is :"+query);
	 List<Map<String, Object>> list = jdbcTemplate.queryForList(query);
	if(list.size()>0 && !list.isEmpty()) {
		return jdbcTemplate.queryForMap(query);	
	}else {
		return new HashMap<>();
	}
	
	}catch(Exception e) {
		e.printStackTrace();
		return new HashMap<>();
	}
}

public int getSampleIndependentTestFlag(String appID, String cmlNo, String survId) {
	try {
	String query ="Select num_isIndependentTestingConducted as isIndependentTestingConducted from bis_dev.pc_is_independent_testing_conducted where str_cmlNo= '"+cmlNo+"' and str_surviellanceId= '"+survId+"'";
	System.out.println("query is :"+query);
	int flag=0;
	if(jdbcTemplate.queryForList(query).size()>0)
	{
		flag=jdbcTemplate.queryForObject(query, Integer.class);
	}else {
		flag=0;
	}
	return flag;
	}
	catch(Exception e) {
		e.printStackTrace();
		return 0;
	}
}
public int getDiscrepancyFormFlag(String appID, String cmlNo, String survId) {
	try {
	String query ="Select num_isvalid as flag from bis_dev.gblt_nc_report_tracking where str_cml_no= '"+cmlNo+"' and num_insp_id= '"+survId+"'";
	System.out.println("query for discrepancy :"+query);
	int flag=0;
	if(jdbcTemplate.queryForList(query).size()>0)
	{
		flag=jdbcTemplate.queryForObject(query, Integer.class);
	}
	return flag;
	}
	catch(Exception e) {
		e.printStackTrace();
		return 0;
	}
}

public boolean checkConsigneeUploadForQuarter(String cmlno) {
	
	String query="select distinct nvl(dt_entry_date,to_date('01-01-1990','DD-MM-YYYY')) as date,dt_entry_date from cml_prod_consignee_dtl where str_cml_no = "+cmlno+" and num_isvalid = 1 order by dt_entry_date desc ";
	System.out.println("checkConsigneeUploadForQuarter "+query);
	
	List list =jdbcTemplate.queryForList(query);
	
	if(list.isEmpty()) {
	return true;
	}
	Date date = (Date) ((Map) list.get(0)).get("date");
	
	Date currDate = new Date();
	Integer datePartMonth =date.getMonth()+1;
	Integer datePartYear =date.getYear()+1900;
	Integer datePartQuarter =0;
	
	
	if(datePartMonth>=1 && datePartMonth<=3) {
		datePartQuarter = 1;
	}else if(datePartMonth>=4 && datePartMonth<=6) {
		datePartQuarter = 2;
	}else if(datePartMonth>=7 && datePartMonth<=9) {
		datePartQuarter = 3;
	}else if(datePartMonth>=10 && datePartMonth<=12) {
		datePartQuarter = 4;
	}
	
	try {
		
	
	Integer currentMonth = currDate.getMonth()+1;
	Integer currentQuarter = 0;
	Integer currentYear = currDate.getYear()+1900;
	
	if(currentMonth>=1 && currentMonth<=3) {
		currentQuarter = 1;
	}else if(currentMonth>=4 && currentMonth<=6) {
		currentQuarter = 2;
	}else if(currentMonth>=7 && currentMonth<=9) {
		currentQuarter = 3;
	}else if(currentMonth>=10 && currentMonth<=12) {
		currentQuarter = 4;
	}
	SimpleDateFormat sdformat = new SimpleDateFormat("dd-MM-yyyy");
	if(date.compareTo(sdformat.parse("01-01-1990")) == 0) {//dummy date to avoid null
		return true;
	}else if((datePartQuarter<currentQuarter && datePartYear<=currentYear) || (datePartQuarter>currentQuarter && datePartYear<currentYear)) {
		return true;
	}else if((datePartQuarter==currentQuarter && datePartYear<currentYear)) {
		return true;
	}
	}
	catch (Exception e) {
		e.printStackTrace();
	}
	
	return false;
	
}
public void updateInsertFeeSuccessStatus(String applicationID, int branchId, int role, int userId) {
	String updateqry = "update bis_dev.pc_application_submission set num_application_status_id = 27 where str_application_id = '"+ applicationID + "' and num_location_id = " + branchId;
	System.out.println("update qry is :: "+updateqry);
	jdbcTemplate.update(updateqry);
	
	
		int numId = 0;
		Date date = new Date();
		String qry = "SELECT max(num_id) FROM bis_dev.pc_application_tracking";
		numId = jdbcTemplate.queryForObject(qry, Integer.class);
		numId = numId + 1;
		
		String insertQuery = "INSERT INTO bis_dev.pc_application_tracking (num_id, str_application_id, num_branch_id, num_app_status, dt_entry_date, num_entry_role_id, num_visible_applicant, num_entry_emp_id) VALUES(" + numId + ",'" + applicationID + "' ," + branchId + ", 27, '" +  date + "', "+role+", 1, "+userId+")";
		jdbcTemplate.execute(insertQuery);
		String updateSequence = "update bis_dev.hibernate_sequences set sequence_next_hi_value = (select (sequence_next_hi_value)+1 from bis_dev.hibernate_sequences where sequence_name = 'pc_application_tracking') where sequence_name = 'pc_application_tracking'";
		jdbcTemplate.execute(updateSequence);
		System.out.println("insertQuery qry is :: "+insertQuery);
		System.out.println("updateSequence qry is :: "+updateSequence);
		
//		PCApplicationTrackingDomain dom = new PCApplicationTrackingDomain();
//		dom.setIntBranchId(branchId);
//		dom.setLnApplicationId(applicationID);
//		dom.setIntAppStatus(27);
//		dom.setStrEntryRoleId(role);
//		dom.setNum_visible_applicant(1);
		
//		dom.setStrEntryEmpId(Long.parseLong(""+userId));
		
//		daoHelper.merge(PCApplicationTrackingDomain.class, dom);
	

}

public int getCountofPCApplicationsCOC(int iUserId_p, int iStatusId_p, int iSubmittedApps_p) {
	String qry="select count(*) FROM bis_dev.pc_application_submission WHERE num_application_status_id="+iStatusId_p+" and num_gbl_user_id="+iUserId_p+" and str_is_number like 'COC%'";
	
	
	int iApplications = jdbcTemplate.queryForObject(qry, Integer.class);
	return iApplications;
}

public int getCountPCSubmittedAppsCOC(int iUserId_p) {
	int iCount = 0;
	String qry = "select count(*) from bis_dev.pc_application_submission a " +
			" where num_application_status_id in (27,285) and num_gbl_user_id="+iUserId_p+
			" and (a.str_application_id,a.num_location_id) not in (select str_app_id,num_branch_id from bis_dev.cml_licence_detail)"
			+ " and str_is_number like 'COC%'";
	System.out.println(qry);
	iCount = jdbcTemplate.queryForObject(qry, Integer.class);
	
	return iCount;
}

public List getApplicationsCOC(int iUserId_p) {
	Calendar now = Calendar.getInstance();
    int year=now.get(Calendar.YEAR) - (now.get(Calendar.MONTH) < 4?1:0);
    String start=year + "-04-01";
    String end=(year+1) + "-03-31";
	
	String qry = "SELECT a.lnApplicationId, a.dtRegistration, a.strISno ||':'|| a.numStdYear, b.strFirmName,c.intAppStatus ,e.str_applicant_status," +
			" (select atr.proposeDate from PCApplicationTrackingDomain atr where a.lnApplicationId=atr.lnApplicationId and a.intBranchId=atr.intBranchId and  atr.num_id = (select max(num_id) from PCApplicationTrackingDomain btr where a.lnApplicationId=btr.lnApplicationId and a.intBranchId=btr.intBranchId and btr.proposeDate is not null))," +
			" a.intBranchId, a.intProcedureId, " +
			" (select count(intApplnStatusId) from Fee_Paid_Detail_Domain f where f.strAppId=a.lnApplicationId and f.num_branch_id=a.intBranchId and f.intApplnStatusId=0 and f.num_Amount <> '0' and f.isValid=1 and to_char(f.dt_Entry_Date,'yyyy-mm-dd') >='2017-05-15' and f.dt_Entry_Date <= sysdate and f.numFeeId not in (1,7 ,28) and f.numEntryEmpId<>"+iUserId_p+" and f.numFeeId in (select b.numFeeId from Fee_Type_Domain b where b.strAmountType='Rs.' and b.isValid=1))," +
			" (select count(num_id) from applicationChklistDomain f where f.str_application_id=a.lnApplicationId and f.int_branch_id=a.intBranchId and f.str_applicant_reply_status =0 ), " +
			" (select lfd.strLetterName from TextEditor_Dom ted,letterFormatDomain lfd where ted.str_app_no = a.lnApplicationId and ted.num_branch_id = a.intBranchId and ted.num_letter_id=lfd.id), " +
			" (select ted.num_letter_id from TextEditor_Dom ted,letterFormatDomain lfd where ted.str_app_no = a.lnApplicationId and ted.num_branch_id = a.intBranchId and ted.num_letter_id=lfd.id), " +
			" (select min(atr1.date) from PCApplicationTrackingDomain atr1 where a.lnApplicationId=atr1.lnApplicationId and a.intBranchId=atr1.intBranchId and atr1.intAppStatus=4)," +
			" (select pr.procedure_Name FROM Procedure_Mst_Domain pr WHERE pr.procedure_Id=a.intProcedureId)," +
			" (SELECT nc.num_nc_tracking FROM PCNCTrackingDomain nc WHERE nc.num_id=(select max(num_id) FROM PCNCTrackingDomain ncaux WHERE ncaux.str_application_id=a.lnApplicationId and ncaux.num_branch_id=a.intBranchId)) " +
			" FROM PcGOLDomain a, firmDtlsDomain b, PCApplicationTrackingDomain c,bisApplicantStatusDomain e  " +
			" WHERE a.strGblUserId="+iUserId_p+" AND a.strISno like 'COC%' AND a.lnApplicationId=b.lnApplicationId AND a.intBranchId=b.intFirmBranchId AND a.lnApplicationId=c.lnApplicationId " +
			" AND a.intBranchId=c.intBranchId and c.intAppStatus=e.intBisStatus  " +
			" AND c.num_id=(select max(num_id) from PCApplicationTrackingDomain m where m.lnApplicationId=c.lnApplicationId and m.intBranchId=a.intBranchId) " +
			"and (a.lnApplicationId,a.intBranchId) not in (select ldd.str_app_id,ldd.num_branch_id  from Licence_Details_Domain ldd where  ldd.str_app_id = a.lnApplicationId and ldd.num_branch_id = a.intBranchId ) " +
			"ORDER BY a.dtRegistration desc";
	System.out.println("getApplications: "+qry);
	List runQry = daoHelper.findByQuery(qry);
	return runQry;
}

public List getLicenseAppsCOC(int iUserId) {
	Calendar now = Calendar.getInstance();
    int year=now.get(Calendar.YEAR) - (now.get(Calendar.MONTH) < 4?1:0);
    String start=year + "-04-01";
    String end=(year+1) + "-03-31";
    

   String qry = "select l.str_cml_no,l.str_firm_name,a.lnApplicationId,l.dt_granted_date,a.intBranchId,c.num_cml_status_id,e.str_applicant_status," +
   		" l.dt_validity_date,a.intProcedureId ,l.str_is_no ||':'|| l.is_year," +
   		" (select s.num_app_status from PCCMLSOMROMCancellationTracking s where s.num_id=(select max(num_id) FROM PCCMLSOMROMCancellationTracking ss WHERE ss.str_cml_no=l.str_cml_no and ss.num_branch_id=l.num_branch_id))," +
   		" (select s.num_id from PCCMLSOMROMCancellationTracking s where s.num_id=(select max(num_id) FROM PCCMLSOMROMCancellationTracking ss WHERE ss.str_cml_no=l.str_cml_no and ss.num_branch_id=l.num_branch_id and ss.num_app_status=301))," +
   		" (select s.num_id from PCCMLSOMROMCancellationTracking s where s.num_id=(select max(num_id) FROM PCCMLSOMROMCancellationTracking ss WHERE ss.str_cml_no=l.str_cml_no and ss.num_branch_id=l.num_branch_id and ss.num_app_status=104))," +
   		" (select max(p.sno) from DiscrepancyDomain p where p.appId = l.str_app_id and p.numBranchId = l.num_branch_id)," +
   		" (select count(intApplnStatusId) from Fee_Paid_Detail_Domain pcFee where pcFee.strAppId=l.str_app_id and pcFee.isValid=1 and pcFee.num_branch_id=l.num_branch_id and pcFee.intApplnStatusId=0 and pcFee.num_Amount <> '0' and to_char(pcFee.dt_Entry_Date,'yyyy-mm-dd') >='2017-05-15' and pcFee.dt_Entry_Date <= sysdate and  pcFee.numFeeId not in (1,7,28) and pcFee.numEntryEmpId<>"+iUserId+" and pcFee.numFeeId in (select b.numFeeId from Fee_Type_Domain b where b.strAmountType='Rs.' and b.isValid=1))," +
   		" (select max(ss.userId) from inclusionVarietyDomain ss where l.str_cml_no = ss.cmlNumber and  l.num_branch_id = ss.branchId ), " +
   		/*" (select aa.dt_validity_date from Licence_Details_Domain aa where trunc(sysdate)>=add_months(trunc(dt_validity_date),-3) and  trunc(sysdate)<=add_months(trunc(dt_validity_date),3) and 1>(select count(*) from LicenceRenewal_Performance_Domain b where aa.str_cml_no=b.str_cml_no and aa.num_branch_id=b.num_branch_id and b.num_Status_Id=27  and trunc(b.dt_entry_date)  between add_months(trunc(dt_validity_date),-3) and  add_months(trunc(dt_validity_date),3))  and aa.str_cml_no=l.str_cml_no and aa.num_branch_id=l.num_branch_id)  " +*/
   		" (select aa.dt_validity_date from Licence_Details_Domain aa where trunc(sysdate)>=add_months(trunc(dt_validity_date),-3) " +
   		" and 1=(select 1 from cmlstatusdetails_Domain cs where cs.str_cml_no=l.str_cml_no and cs.num_branch_id=l.num_branch_id and num_cml_status_id not in (205,206))" +
   		" and 1>(select count(*) from PCRenewalTrackingDomain b where aa.str_cml_no=b.str_cml_no and aa.num_branch_id=b.num_branch_id  " +	    		
   		" and trunc(b.dt_renewal_tracking) > add_months(trunc(dt_validity_date),-3) and dt_validity_date<(select coalesce(max(dt_renewal_tracking) ,'31-Dec-2099') from PCRenewalTrackingDomain c where c.str_cml_no=b.str_cml_no and c.num_branch_id=b.num_branch_id and c.num_renewal_tracking=100 " +
   		" and num_id=(select max(zz.num_id) from PCRenewalTrackingDomain zz where   c.str_cml_no=zz.str_cml_no and c.num_branch_id=zz.num_branch_id) ) and b.num_renewal_tracking=89 " + 
   		
   		" and 1=(select 1 from cmlstatusdetails_Domain cs where cs.str_cml_no=l.str_cml_no and cs.num_branch_id=l.num_branch_id and num_cml_status_id not in (205,206))) " +
   		" and aa.str_cml_no=l.str_cml_no and aa.num_branch_id=l.num_branch_id),(select somtrack.num_app_status FROM PCCMLSOMROMCancellationTracking somtrack WHERE somtrack.num_id=(select max(num_id) FROM PCCMLSOMROMCancellationTracking romtrack WHERE romtrack.str_cml_no=l.str_cml_no and romtrack.num_branch_id=l.num_branch_id)),  " +
   		
   		" (select max(lot.num_app_status) from PCCMLLotInspectionTracking lot where lot.num_id=(select max(lp.num_id) from PCCMLLotInspectionTracking lp where lp.str_cml_no=l.str_cml_no and lp.num_branch_id=l.num_branch_id)), "+
   		"(select rr.num_renew_visible_applicant from PCRenewalTrackingDomain rr where rr.num_id=(select max(tt.num_id) FROM PCRenewalTrackingDomain tt WHERE tt.str_cml_no=l.str_cml_no and "+
   		"tt.num_branch_id=l.num_branch_id and tt.num_renewal_tracking=604)), "	+
   		"(select rr.num_renewal_tracking from PCRenewalTrackingDomain rr where rr.num_id=(select max(tt.num_id) FROM PCRenewalTrackingDomain tt WHERE tt.str_cml_no=l.str_cml_no and "+		
   		"tt.num_branch_id=l.num_branch_id and tt.num_renewal_tracking=604)) ,l.str_is_no "	+
   		" from PcGOLDomain a,Licence_Details_Domain l,cmlstatusdetails_Domain c,bisApplicantStatusDomain e" +
   		" where a.lnApplicationId=l.str_app_id and a.intBranchId = l.num_branch_id " +
   		" and l.str_cml_no = c.str_cml_no and l.num_branch_id = c.num_branch_id and c.num_cml_status_id = e.intBisStatus " +
   		" and a.strGblUserId="+iUserId+" and l.str_is_no like 'COC%'"+ 
   		" ORDER BY l.dt_granted_date desc";
	
	System.out.println("Applicant Query \t"+qry);		
	List runQry = daoHelper.findByQuery(qry);		 
	return runQry;
}

public int getCountCOCOperativeLicences(int iUserId_p) {
	int iCount = 0;
	String qry = "select count(*) from bis_dev.cml_licence_detail a, pc_application_submission b " +
			"where a.str_app_id=b.str_application_id " +
			"and a.num_branch_id=b.num_location_id and b.num_gbl_user_id="+iUserId_p+" and str_is_no like 'COC%'";
	
	iCount = jdbcTemplate.queryForObject(qry, Integer.class);
	
	return iCount;
}

public int getdaysaftervaliditydate(String obj) {
	
		int iCount = 0;
	String qry = "select date_part('days',trunc(sysdate) - trunc(dt_validity_date) ) from cml_licence_detail where str_cml_no='"+obj+"'";
	
	System.out.println("query for days "+qry);
	iCount = jdbcTemplate.queryForObject(qry, Integer.class);
	
	return iCount;
	
	
	
	
	
}

public Integer getLastSurveillanceDetails(String appId, String cmlNo) {
	try {
		String query = "select max(num_id) from (select m.num_id from bis_dev.gblt_surveillance_plan m\r\n"
				+ "where inspection_type = 4 and applicationid = '"+appId+"' and cmlno = '"+cmlNo+"')"; 
		
		System.out.println("Last Surv Id Query :" + query);
		
		if (!jdbcTemplate.queryForList(query).isEmpty()) {
			return jdbcTemplate.queryForObject(query, Integer.class);
		} else {
			return 0;
		}
	} catch (Exception e) {
		e.printStackTrace();
		return 0;
	}
}


}
