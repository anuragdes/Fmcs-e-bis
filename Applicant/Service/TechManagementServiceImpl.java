package Applicant.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Applicant.Model.TechManagementDtsModel;
import Global.CommonUtility.DAO.DaoHelper;
import Global.Login.Service.IMigrateService;
import Schemes.ProductCertification.ApplicationSubmission.Domain.qualityManagementDtlsDomain;

@Service
public class TechManagementServiceImpl implements TechManagementService {

	@Autowired
	public DaoHelper daoHelper;

	@Autowired
	IMigrateService ims;
	static String os = System.getProperty("os.name").toLowerCase();
	@Override
	public List<HashMap<String, String>> getLatestTopmanagment(String app_no,
			String lic_no, int branchID) {

		String query = "Select s from qualityManagementDtlsDomain s where s.str_application_id='"
				+ app_no + "' and s.isValid=1";
		List<qualityManagementDtlsDomain> listtopmanagement = daoHelper
				.findByQuery(query);

		List<HashMap<String, String>> listhmdetails = new ArrayList<HashMap<String, String>>();
		if (listtopmanagement.size() != 0) {
			for (int i = 0; i < listtopmanagement.size(); i++) {
				HashMap<String, String> hm = new HashMap<String, String>();
				qualityManagementDtlsDomain cmdomain = listtopmanagement.get(i);
				hm.put("str_name", String.valueOf(cmdomain.getStr_name()));
				hm.put("str_desi", cmdomain.getStr_designation());
				hm.put("str_quali", cmdomain.getStr_qualification());
				hm.put("str_exp", cmdomain.getExperiencePerson());
				hm.put("num_techID", "" + cmdomain.getNum_id());
				
				byte [] image = cmdomain.getPhotographPersonNew();
				try{
				if(image.equals(null))
					hm.put("num_image", ""+0);
				else
					hm.put("num_image", ""+1);	
				}catch(Exception ex)
				{
					hm.put("num_image", ""+0);
				}
				listhmdetails.add(hm);
			}
		}
		//  Auto-generated method stub
		return listhmdetails;
	}

	@Override
	public boolean saveManagementDetails(TechManagementDtsModel TechMgmtModel,
			byte[] imageData) {

		qualityManagementDtlsDomain tmd = new qualityManagementDtlsDomain();
		try {
			tmd.setIsValid(1);
			tmd.setNum_branch_id(Integer.parseInt(ims.Dcrypt(TechMgmtModel
					.getstr_branch_id())));
			tmd.setStr_name(TechMgmtModel.getstr_name());
			tmd.setStrGblUserId(TechMgmtModel.getuser_id());
			tmd.setStr_designation(TechMgmtModel.getStr_desi());
			tmd.setStr_qualification(TechMgmtModel.getQualification());
			tmd.setExperiencePerson(TechMgmtModel.getStr_exp());
			tmd.setPhotographPersonNew(imageData);
			tmd.setDate(new Date());
			tmd.setStr_application_id(ims.Dcrypt(TechMgmtModel.getstr_app_id()));
			tmd.setStr_cml_no(ims.Dcrypt(TechMgmtModel.getstr_cml_no()));

		} catch (Exception ex) {
			System.out.println(ex);
		}

		return saveTopManagement(tmd);
	}

	public boolean saveTopManagement(qualityManagementDtlsDomain TopMgmtDomain) {

		try {

			daoHelper.persist(qualityManagementDtlsDomain.class, TopMgmtDomain);
			return true;
		} catch (Exception e) {

			e.printStackTrace();
			return false;
		}
	}

	@Override
	public String removeManagementDetails(String returnText, int userid) {
		String id = returnText;
//		String stResponse = "";
		qualityManagementDtlsDomain tmd = new qualityManagementDtlsDomain();
		try {
			String qry = "SELECT p FROM qualityManagementDtlsDomain p WHERE p.num_id="
					+ id + " and p.isValid=1";

			List<qualityManagementDtlsDomain> runQry = daoHelper
					.findByQuery(qry);
			System.out.println("Select Delete Id===" + qry);
			for (int i = 0; i < runQry.size(); i++) {
				tmd = runQry.get(i);
				tmd.setIsValid(0);
				tmd.setDate(new Date());

				daoHelper.merge(qualityManagementDtlsDomain.class, tmd);
//				stResponse = "Success";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}



	@Override
	public List<qualityManagementDtlsDomain> getCircularsId(int url) {
		//  Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getDetailsById(int id) {
		//  Auto-generated method stub
		return null;
	}

}
