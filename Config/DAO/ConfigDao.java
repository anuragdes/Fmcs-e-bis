package Global.Config.DAO;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import Global.CommonUtility.DAO.DaoHelper;
import Global.Login.DAO.LoginDao;
import Global.Registration.Domain.RegisterDomain;
import lab.domain.Lab_Type_Domain;

@Repository
public class ConfigDao {

	@Autowired
	public DaoHelper daoHelper;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	LoginDao ldao;
	
	public List<RegisterDomain> getBlockedUsers(int iLocationId, int iLocationTypeId){
		String qry = "";
		/*if(iLocationTypeId==1 && iLocationId==4){*/
			qry = "SELECT users FROM RegisterDomain users WHERE users.faliure_attempt=5";
		/*}else{
			qry = "SELECT a FROM RegisterDomain a, Emp_Mst_Domain b WHERE a.user_id = b.strBISEmpId AND b.numLocationTypeId="+iLocationTypeId+" AND b.numLocationId="+iLocationId+" AND a.faliure_attempt=5";
		}*/
		List<RegisterDomain> runQry = daoHelper.findByQuery(qry);
		return runQry;
	}
	
	public List<RegisterDomain> getUsernamesOfBlockedUsers(String stEmail_p){
		String qry = "SELECT usernames FROM RegisterDomain usernames WHERE usernames.email='"+stEmail_p+"'";
		List<RegisterDomain> runQry = daoHelper.findByQuery(qry);
		return runQry;
	}
	
	public List<RegisterDomain> getUser(String userName){
		String qry = "SELECT usernames FROM RegisterDomain usernames WHERE usernames.strUserType='P' and usernames.username='"+userName+"'";
		List<RegisterDomain> runQry = daoHelper.findByQuery(qry);
		return runQry;
	}
	
	public int clearAttempts(String stEmail_p, String stUsername_p,int userId){
		int iErrorStatus = 0;
		List<RegisterDomain> regdomlist = new ArrayList <RegisterDomain>();
		String qry = "SELECT regDom FROM RegisterDomain regDom WHERE regDom.email='"+stEmail_p+"' AND regDom.username='"+stUsername_p+"'";
		regdomlist = daoHelper.findByQuery(qry);
		if(regdomlist.size()>0){
			RegisterDomain newregDom = new RegisterDomain();
			newregDom = regdomlist.get(0);
			newregDom.setFaliure_attempt(0);
			newregDom.setEntry_Emp_Id(userId);
			daoHelper.merge(RegisterDomain.class, newregDom);
			iErrorStatus = 1;
			ldao.updateblockuser(newregDom.getNumBisUserId());
		}
		
		return iErrorStatus;
	}
	
	public int clearAttempts(String stUsername_p,int userId){
		int iErrorStatus = 0;
		List<RegisterDomain> regdomlist = new ArrayList <RegisterDomain>();
		String qry = "SELECT regDom FROM RegisterDomain regDom WHERE regDom.username='"+stUsername_p+"'";
		regdomlist = daoHelper.findByQuery(qry);
		if(regdomlist.size()>0){
			RegisterDomain newregDom = new RegisterDomain();
			newregDom = regdomlist.get(0);
			newregDom.setFaliure_attempt(0);
			newregDom.setEntry_Emp_Id(userId);
			daoHelper.merge(RegisterDomain.class, newregDom);
			ldao.updateblockuser(newregDom.getNumBisUserId());
			iErrorStatus = 1;
		}
		return iErrorStatus;
	}
	
	public List getPcConsolidateData(int branch,int role_id,int UserId,String frmdate,String todate){
		
		System.out.println("branch  -"+branch+"  role_id --"+role_id+"  userid"+UserId+"  frmdate  "+frmdate+"  todate  -"+todate);
		
		String strQuery= "select str_branc_short_name, (select count(*) from bis_dev.pc_application_submission b where b.num_location_id=a.num_branch_id and to_char(b.dt_registration_date,'yyyymmdd') >'20170514'  and to_char(b.dt_registration_date,'yyyymmdd') >'"+frmdate+"' and to_char(b.dt_registration_date,'yyyymmdd') <'"+todate+"') total_application, "+
                         " (select count(*) from bis_dev.pc_application_submission b where b.num_location_id=a.num_branch_id and to_char(b.dt_registration_date,'yyyymmdd') >'20170514'  and to_char(b.dt_registration_date,'yyyymmdd') >'"+frmdate+"' and to_char(b.dt_registration_date,'yyyymmdd') <'"+todate+"' and num_application_status_id=26) total_draft_application, "+
                         " (select count(*) from bis_dev.pc_application_submission b where b.num_location_id=a.num_branch_id and to_char(b.dt_registration_date,'yyyymmdd') >'20170514'  and to_char(b.dt_registration_date,'yyyymmdd') >'"+frmdate+"' and to_char(b.dt_registration_date,'yyyymmdd') <'"+todate+"' and num_application_status_id=27) total_submitted_application, "+
                         " (select count(*) from bis_dev.pc_application_submission b where b.num_location_id=a.num_branch_id and to_char(b.dt_registration_date,'yyyymmdd') >'20170514'  and to_char(b.dt_registration_date,'yyyymmdd') >'"+frmdate+"' and to_char(b.dt_registration_date,'yyyymmdd') <'"+todate+"' and num_application_status_id=27 and num_procedure_id=1) total_normal_submitted_application, "+
                         " (select count(*) from bis_dev.pc_application_submission b where b.num_location_id=a.num_branch_id and to_char(b.dt_registration_date,'yyyymmdd') >'20170514'  and to_char(b.dt_registration_date,'yyyymmdd') >'"+frmdate+"' and to_char(b.dt_registration_date,'yyyymmdd') <'"+todate+"' and num_application_status_id=27 and num_procedure_id=2) total_simplified_submitted_application,"+ 
				          "(select nvl(round(to_number(sum(amount)),2),0) from gblt_payment_gateway_dtls b where b.branch_id=a.num_branch_id and receipt_no is not null and payment_status='0300' and TO_CHAR(to_date(substr(b.tr_date,1,10),'DD/MM/YYYY'),'YYYYMMDD') >'20170514'   and TO_CHAR(to_date(substr(b.tr_date,1,10),'DD/MM/YYYY'),'YYYYMMDD') >'"+frmdate+"' and TO_CHAR(to_date(substr(b.tr_date,1,10),'DD/MM/YYYY'),'YYYYMMDD') <'"+todate+"') online_payment_collection,"+
                          "(select count(*) from bis_dev.cml_licence_detail b where b.num_branch_id=a.num_branch_id and to_char(b.dt_granted_date,'yyyymmdd') >'20170514'  and to_char(b.dt_granted_date,'yyyymmdd') >'"+frmdate+"' and to_char(b.dt_granted_date,'yyyymmdd') <'"+todate+"')total_license_granted, "+
                          "(select count(*) from bislab.lab_test_request_mst b where num_req_status in (59,80,44,46,310,60,130,131,132) and b.num_isvalid=1 and b.num_app_branch_id=a.num_branch_id   and to_char(b.dt_entry_date,'yyyymmdd') >'20170514'  and to_char(b.dt_entry_date,'yyyymmdd') >'"+frmdate+"' and to_char(b.dt_entry_date,'yyyymmdd') <'"+todate+"' ) Final_test_request_on_Application, "+
                          "(select count(*) from bislab.cml_lab_test_request_mst b where num_req_status in (59,80,44,46,310,60,130,131,132) and b.num_isvalid=1 and b.num_lic_branch_id=a.num_branch_id and to_char(b.dt_entry_date,'yyyymmdd') >'20170514'  and to_char(b.dt_entry_date,'yyyymmdd') >'"+frmdate+"' and to_char(b.dt_entry_date,'yyyymmdd') <'"+todate+"' ) Final_test_request_on_License, "+
                          "(select count(*) from bislab.lab_test_request_mst aa,bislab.lab_test_request_sample_dt b where aa.num_req_status in (60,130,131,132)  and aa.num_test_request_id=b.num_test_request_id and aa.str_application_id=b.str_application_id and aa.num_app_branch_id=b.num_app_branch_id and b.num_lab_type_id=1 and aa.num_app_branch_id=a.num_branch_id and to_char(aa.dt_entry_date,'yyyymmdd') >'20170514'  and to_char(aa.dt_entry_date,'yyyymmdd') >'"+frmdate+"' and to_char(aa.dt_entry_date,'yyyymmdd') <'"+todate+"') Test_report_uploaded_by_BIS_LAB_on_application, "+
                          "(select count(*) from bislab.lab_test_request_mst aa,bislab.lab_test_request_sample_dt b where aa.num_req_status in (60,130,131,132)  and aa.num_test_request_id=b.num_test_request_id and aa.str_application_id=b.str_application_id and aa.num_app_branch_id=b.num_app_branch_id and b.num_lab_type_id=2 and aa.num_app_branch_id=a.num_branch_id and to_char(aa.dt_entry_date,'yyyymmdd') >'20170514'  and to_char(aa.dt_entry_date,'yyyymmdd') >'"+frmdate+"' and to_char(aa.dt_entry_date,'yyyymmdd') <'"+todate+"') Test_report_uploaded_by_OSL_LAB_on_application, "+
                          " (select count(*) from bislab.cml_lab_test_request_mst aa, bislab.cml_lab_test_request_sample_dt b where aa.num_req_status in (60,130,131,132)  and aa.num_test_request_id=b.num_test_request_id and aa.str_licence_num=b.str_licence_number and aa.num_lic_branch_id=b.num_lic_branch_id "+
                          " and b.num_lab_type_id=1 and aa.num_lic_branch_id=a.num_branch_id and to_char(aa.dt_entry_date,'yyyymmdd') >'20170514'  and to_char(aa.dt_entry_date,'yyyymmdd') >'"+frmdate+"' and to_char(aa.dt_entry_date,'yyyymmdd') <'"+todate+"') Test_report_uploaded_by_BIS_LAB_on_license,  " +
                          "(select count(*) from bislab.cml_lab_test_request_mst aa, bislab.cml_lab_test_request_sample_dt b where aa.num_req_status in (60,130,131,132)  and aa.num_test_request_id=b.num_test_request_id and aa.str_licence_num=b.str_licence_number and aa.num_lic_branch_id=b.num_lic_branch_id "+
                          " and b.num_lab_type_id=2 and aa.num_lic_branch_id=a.num_branch_id and to_char(aa.dt_entry_date,'yyyymmdd') >'20170514'  and to_char(aa.dt_entry_date,'yyyymmdd') >'"+frmdate+"' and to_char(aa.dt_entry_date,'yyyymmdd') <'"+todate+"') Test_report_uploaded_by_OSL_LAB_on_license, "+
                          " (select count(*) from bis_dev.cml_inclusion_tracking b where num_app_status=144 and b.num_branch_id=a.num_branch_id and to_char(b.dt_entry_date,'yyyymmdd') >'"+frmdate+"' and to_char(b.dt_entry_date,'yyyymmdd') <'"+todate+"'  )inclusion_requested_submitted, "+
                          "(select count(*) from bis_dev.cml_inclusion_tracking b where num_app_status=542 and b.num_branch_id=a.num_branch_id and to_char(b.dt_entry_date,'yyyymmdd') >'"+frmdate+"' and to_char(b.dt_entry_date,'yyyymmdd') <'"+todate+"'  )inclusion_granted, "+
                          " (select count(*) from bis_dev.cml_renewal_tracking b where num_RENEWAL_TRACKING=89 and b.num_branch_id=a.num_branch_id and to_char(b.dt_renewal_tracking,'yyyymmdd') >'"+frmdate+"' and to_char(b.dt_renewal_tracking,'yyyymmdd') <'"+todate+"'  )renewal_requested_submitted, "+
                          "(select count(*) from bis_dev.cml_renewal_tracking b where num_RENEWAL_TRACKING=100 and b.num_branch_id=a.num_branch_id and to_char(b.dt_renewal_tracking,'yyyymmdd') >'"+frmdate+"' and to_char(b.dt_renewal_tracking,'yyyymmdd') <'"+todate+"' )renewal_granted, "+
                          " (select count(*) from bis_dev.cml_licence_status_dtl b where b.num_branch_id=a.num_branch_id and num_cml_status_id=201)operative_license_as_on_date, "+
                           " (select count(*) from bis_dev.cml_licence_status_dtl b where b.num_branch_id=a.num_branch_id and num_cml_status_id=202)deferred_license_as_on_date, "+
                            " (select count(*) from bis_dev.cml_licence_status_dtl b where b.num_branch_id=a.num_branch_id and num_cml_status_id=204)under_stop_marking_as_on_date, "+
                          " (select count(*) from bis_dev.cml_licence_status_dtl b where b.num_branch_id=a.num_branch_id and num_cml_status_id=200)under_self_stop_marking_as_on_date, "+
                          " (select count(*) from bis_dev.cml_licence_status_dtl b where b.num_branch_id=a.num_branch_id and num_cml_status_id=205)cancelled_license_as_on_date, "+
                          " (select count(*) from bis_dev.cml_licence_status_dtl b where b.num_branch_id=a.num_branch_id and num_cml_status_id=206)expired_license_as_on_date ";
                        					
		strQuery+=" from bis_dev.gblt_branch_mst a where num_isvalid=1  and a.num_branch_id  !=41";
	    if(role_id==4)
		{
	    	strQuery+=" and num_branch_id= " +branch;
		}
		
		strQuery+=" order by 1";
		
		System.out.println("strQuery:::::::::"+strQuery);
		
		return jdbcTemplate.queryForList(strQuery);
	}
	
	public List<Lab_Type_Domain> getLabTypeList()
	{
		String qry="Select r from Lab_Type_Domain r where r.isValid=1 and numLabTypeId <> '3' order by r.strLabTypeName";
		return daoHelper.findByQuery(qry);
	}

	public List getBo() {
		String qry = "Select p.numBranchId,p.strBrShortName from Branch_Master_Domain p where IsValid = 1 order by p.strBranchName ";
		return daoHelper.findByQuery(qry);
	}
	
	public List getLabName(String labId) {
		String qry = "SELECT c.numLabTypeName,c.strLabCode,c.strLabAddress1,c.strLabAddress2,c.strCity,st.strStateName,c.numPincode from Lab_Master_Domain c,State_Mst_Domain st where c.isValid = 1 and c.numLabType = '"+labId+"' and st.numStateId=c.numStateId order by c.numLabTypeName";
		return daoHelper.findByQuery(qry);
	}
	
	public List getBISLabNameFrHead(String labId) {
		String qry = "SELECT c.numLabTypeName,c.strLabCode,c.strLabAddress1,c.strLabAddress2,c.strCity,st.strStateName,c.numPincode from Lab_Master_Domain c,State_Mst_Domain st where c.isValid = 1 and c.strLabCode = '"+labId+"' and st.numStateId=c.numStateId order by c.numLabTypeName";
		return daoHelper.findByQuery(qry);
	}
	
}
