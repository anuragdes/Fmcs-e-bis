package Applicant.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Applicant.Model.TopManagementDetailsModel;
import Global.CommonUtility.DAO.DaoHelper;
import Global.Login.Service.IMigrateService;
import Schemes.ProductCertification.ApplicationSubmission.Domain.topManagementDtlsDomain;

@Service
public class TopManagementDetailsServiceImpl implements
		TopManagementDetailsService {
 
	@Autowired
	public DaoHelper daoHelper;
	
	@Autowired
	IMigrateService ims;

	@Override
	public boolean saveManagementDetails(
		TopManagementDetailsModel TopMgmtModel) {
		topManagementDtlsDomain tmd = new topManagementDtlsDomain();
		try{
			tmd.setIsValid(1);
			tmd.setNum_branch_id(Integer.parseInt(ims.Dcrypt(TopMgmtModel.getstr_branch_id())));
			tmd.setStr_name(TopMgmtModel.getstr_name());
			tmd.setNumEntryEmpId(TopMgmtModel.getuser_id());
			tmd.setStr_designation(TopMgmtModel.getStr_desi());
			tmd.setStr_contact_no(TopMgmtModel.getStr_phone());
			tmd.setStr_email_id(TopMgmtModel.getStr_email());
			tmd.setStr_DIN(TopMgmtModel.getStr_din());
			tmd.setDate(new Date());
			try{
			tmd.setStr_application_id(ims.Dcrypt(TopMgmtModel.getstr_app_id()));
			
			tmd.setStr_cml_no(ims.Dcrypt(TopMgmtModel.getstr_cml_no()));
			}catch(Exception ex)
			{}
			
		}catch(Exception ex)
		{
			System.out.println(ex);
		}

	return saveTopManagement(tmd);
	}

	@Override
	public String removeManagementDetails(String returnText, int userid) {
		String id= returnText;
		String stResponse="";
		topManagementDtlsDomain tmd = new topManagementDtlsDomain();
			try {
				String qry = "SELECT p FROM topManagementDtlsDomain p WHERE p.num_id="+id+" and p.isValid=1";

				List<topManagementDtlsDomain> runQry = daoHelper.findByQuery(qry);
				System.out.println("Select Delete Id==="+qry);
				for(int i=0;i<runQry.size();i++)
				{
					tmd = runQry.get(i);
					tmd.setIsValid(0);
					tmd.setDate(new Date());
							
					daoHelper.merge(topManagementDtlsDomain.class, tmd);
					stResponse = "Success";
				}		
				
			
				
			
			}catch(Exception e){
				e.printStackTrace();
			}
			System.out.println(stResponse);
			return id;
	}

	@Override
	public void addUpload(topManagementDtlsDomain udd) {
		//  Auto-generated method stub

	}

	@Override
	public List<topManagementDtlsDomain> getDetailsById() {
		//  Auto-generated method stub
		return null;
	}

	@Override
	public List<topManagementDtlsDomain> getCircularsId(int url) {
		//  Auto-generated method stub
		return null;
	}

	@Override
	public List<HashMap<String, String>> getLatestTopmanagment(String app_no,
			String lic_no, int branchID) {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date myDate = new Date(System.currentTimeMillis());
		String stToDt = dateFormat.format(myDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(myDate);
		cal.add(Calendar.DATE, -10);
		String stFromDt = dateFormat.format(cal.getTime());

		String query = "Select s from topManagementDtlsDomain s where s.str_application_id='"+ app_no + "' and s.isValid=1";
		List<topManagementDtlsDomain> listtopmanagement = daoHelper
				.findByQuery(query);

		List<HashMap<String, String>> listhmdetails = new ArrayList<HashMap<String, String>>();
		if (listtopmanagement.size() != 0) {
			for (int i = 0; i < listtopmanagement.size(); i++) {
				HashMap<String, String> hm = new HashMap<String, String>();
				topManagementDtlsDomain cmdomain = listtopmanagement.get(i);
				 hm.put("str_name",String.valueOf(cmdomain.getStr_name())); 
				 hm.put("str_desi", cmdomain.getStr_designation());
				 hm.put("str_phone",cmdomain.getStr_contact_no());
				 hm.put("str_email", cmdomain.getStr_email_id());
				 hm.put("str_din", cmdomain.getStr_DIN());
				 hm.put("num_topID", ""+cmdomain.getNum_id());

				listhmdetails.add(hm);
			}

		}
		System.out.println(stToDt);
		System.out.println(stFromDt);
		return listhmdetails;
	}

	public boolean saveTopManagement(topManagementDtlsDomain TopMgmtDomain) {
		
		try {
			
			daoHelper.persist(topManagementDtlsDomain.class, TopMgmtDomain);
			return true;
		} catch (Exception e) {
			
			e.printStackTrace();
			return false;
		}
	}
}
