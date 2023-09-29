package Global.Dashboard.DAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import Global.CommonUtility.DAO.DaoHelper;

@Repository
public class DGDashboardPCDao {

	@Autowired
	DaoHelper daoHelper;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public String getDynamicBranchIds(int iRoleId,int iRegionId,int iBranchId){
		String stBranchIds = "";
		String qry = "select string_agg(num_branch_id,',') as branches from(SELECT num_branch_id FROM bis_dev.gblt_branch_mst WHERE num_isvalid = 1 AND num_branch_id <> 41" +
				" AND "+iRoleId+" NOT IN ( 4, 7 ) AND num_ro_id LIKE Decode('"+iRegionId+"', '4', '%','"+iRegionId+"') AND num_branch_id LIKE Decode('"+iBranchId+"', '0', '%','"+iBranchId+"')" +
				" UNION " +
				" SELECT num_branch_id FROM bis_dev.gblt_branch_mst " +
				" WHERE num_isvalid = 1 AND "+iRoleId+" IN ( 7 ) AND num_ro_id = "+iRegionId+" AND num_branch_id LIKE Decode('"+iBranchId+"', '0', '%','"+iBranchId+"')" +
				" UNION " +
				" SELECT "+iBranchId+" FROM DUAL WHERE "+iRoleId+" IN ( 4 ))";
		List<Map<String,Object>> runQry = jdbcTemplate.queryForList(qry);
		if(runQry.size()>0){
			Map<String,Object> temp = runQry.get(0);
			stBranchIds = temp.get("branches")+"";
		}
		
		return stBranchIds;
	}
	
	public List<HashMap<String,String>> getPendingApplications(int iRoleId,int iRegionId,int iBranchId, int iStateId){
		List<HashMap<String,String>> lhm = new ArrayList<HashMap<String,String>>();
		
		String stBranchIds = getDynamicBranchIds(iRoleId,iRegionId,iBranchId);
		
		String qry = " SELECT (SELECT x.str_branc_short_name FROM bis_dev.gblt_branch_mst x WHERE x.num_branch_id = a.num_branch_id) AS branch," +
				" Count(*) AS count FROM bis_dev.pc_application_tracking a, bis_dev.pc_application_factory_dtl b" +
				" WHERE a.str_application_id=b.str_application_id and a.num_branch_id=b.num_branch_id and a.num_id = (SELECT Max(num_id)" +
				" FROM bis_dev.pc_application_tracking b WHERE  b.str_application_id = a.str_application_id AND b.num_branch_id = a.num_branch_id)" +
				" AND ( a.str_application_id, a.num_branch_id ) NOT IN (SELECT str_app_id, num_branch_id FROM bis_dev.cml_licence_detail) AND a.num_app_status NOT IN ( 3, 21, 201 )" +
				" AND a.num_branch_id IN ("+stBranchIds+") and num_state_id like Decode('"+iStateId+"', '0', '%','"+iStateId+"')" +
				" GROUP  BY a.num_branch_id ORDER  BY 2 DESC LIMIT  10";
		
		List<Map<String,Object>> runQry = jdbcTemplate.queryForList(qry);
		if(runQry.size()>0){
			for(int i=0;i<runQry.size();i++){
				HashMap<String,String> hm = new HashMap<String,String>();
				Map<String,Object> temp = runQry.get(i);
				hm.put("branch", temp.get("branch")+"");
				hm.put("cnt", temp.get("count")+"");
				lhm.add(hm);
			}
		}

		
		return lhm;
	}
	
	
	public List<HashMap<String,String>> getPendingLicences(int iRoleId,int iRegionId,int iBranchId, int iStateId){
		List<HashMap<String,String>> lhm = new ArrayList<HashMap<String,String>>();
		
		String stBranchIds = getDynamicBranchIds(iRoleId,iRegionId,iBranchId);
		
		String qry = "select count,(SELECT str_status_name FROM   bis_dev.gblt_status_mst WHERE  num_status_id = status) as status from (" +
				" SELECT Count(*)AS count, decode(num_cml_status_id,200,204,num_cml_status_id) AS status FROM bis_dev.cml_licence_detail a," +
				" bis_dev.cml_licence_status_dtl b WHERE a.str_cml_no = b.str_cml_no AND a.num_branch_id = b.num_branch_id AND b.num_cml_status_id IN ( 201, 200, 202, 204 )" +
				" AND a.num_branch_id IN ("+stBranchIds+") and a.num_fac_state_id like Decode('"+iStateId+"', '0', '%','"+iStateId+"') GROUP  BY decode(num_cml_status_id,200,204,num_cml_status_id))";
		
		List<Map<String,Object>> runQry = jdbcTemplate.queryForList(qry);
		if(runQry.size()>0){
			for(int i=0;i<runQry.size();i++){
				HashMap<String,String> hm = new HashMap<String,String>();
				Map<String,Object> temp = runQry.get(i);
				hm.put("status", temp.get("status")+"");
				hm.put("cnt", temp.get("count")+"");
				lhm.add(hm);
			}
		}
		
		return lhm;
	}
	
	
	public List<HashMap<String,String>> getStatusWiseBreakdown(int iRoleId,int iRegionId,int iBranchId, int iStateId){
		List<HashMap<String,String>> lhm = new ArrayList<HashMap<String,String>>();
		
		String stBranchIds = getDynamicBranchIds(iRoleId,iRegionId,iBranchId);
		
		String qry = " SELECT 'Officer Assignment Pending at HOD' AS status,Nvl(Sum(cnt), 0) AS sum" +
				" FROM (SELECT Count(*) AS cnt FROM bis_dev.pc_application_tracking a, bis_dev.pc_application_factory_dtl b" +
				" WHERE a.str_application_id = b.str_application_id AND a.num_branch_id = b.num_branch_id" +
				" AND a.num_id = (SELECT Max(num_id) FROM bis_dev.pc_application_tracking c" +
				" WHERE c.str_application_id = b.str_application_id AND c.num_branch_id = b.num_branch_id)" +
				" AND a.num_app_status = 27 AND a.num_branch_id IN ("+stBranchIds+")" +
				" AND num_state_id LIKE Decode('"+iStateId+"', '0', '%','"+iStateId+"')" +
				" GROUP  BY a.num_branch_id)" +
			" UNION " +
				" SELECT 'License Granted, Licence No Generation Pending at DO' AS status, Nvl(Sum(cnt), 0) AS sum" +
				" FROM (SELECT Count(*) AS cnt FROM bis_dev.pc_application_tracking a," +
				" bis_dev.pc_application_factory_dtl b" +
				" WHERE a.str_application_id = b.str_application_id AND a.num_branch_id = b.num_branch_id" +
				" AND a.num_id = (SELECT Max(num_id) FROM bis_dev.pc_application_tracking c" +
				" WHERE c.str_application_id = b.str_application_id " +
				" AND c.num_branch_id = b.num_branch_id) AND a.num_app_status = 296 AND a.num_branch_id IN ("+stBranchIds+" )" +
				" AND num_state_id LIKE Decode('"+iStateId+"', '0', '%','"+iStateId+"') GROUP  BY a.num_branch_id)" +
			" UNION" +
				" SELECT 'Query raised to Applicant' AS status," +
				" Nvl(Sum(cnt), 0) AS sum FROM (SELECT Count(*) AS cnt " +
				" FROM bis_dev.pc_application_tracking a, bis_dev.pc_application_factory_dtl b" +
				" WHERE a.str_application_id = b.str_application_id AND a.num_branch_id = b.num_branch_id" +
				" AND a.num_id = (SELECT Max(num_id) FROM bis_dev.pc_application_tracking c" +
				" WHERE c.str_application_id = b.str_application_id" +
				" AND c.num_branch_id = b.num_branch_id) AND a.num_app_status = 48 AND a.num_branch_id IN ("+stBranchIds+")" +
				" AND num_state_id LIKE Decode('"+iStateId+"', '0', '%','"+iStateId+"') GROUP  BY a.num_branch_id)" +
			" UNION " +
				" SELECT 'Scrutiny Pending' AS status, Nvl(Sum(cnt), 0) AS sum" +
				" FROM (SELECT Count(*) AS cnt FROM bis_dev.pc_application_tracking a," +
				" bis_dev.pc_application_factory_dtl b" +
				" WHERE a.str_application_id = b.str_application_id " +
				" AND a.num_branch_id = b.num_branch_id " +
				" AND a.num_id = (SELECT Max(num_id) FROM bis_dev.pc_application_tracking c WHERE c.str_application_id = b.str_application_id " +
				" AND c.num_branch_id = b.num_branch_id) AND a.num_app_status IN ( 158, 49 ) AND a.num_branch_id IN ("+stBranchIds+") " +
				" AND num_state_id LIKE Decode('"+iStateId+"', '0', '%','"+iStateId+"') GROUP  BY a.num_branch_id) " +
			" UNION " +
				" SELECT 'PI Date Acceptance Pending at Applicant' AS status, Nvl(Sum(cnt), 0) AS sum" +
				" FROM (SELECT Count(*) AS cnt" +
				" FROM bis_dev.pc_application_tracking a,bis_dev.pc_application_factory_dtl b" +
				" WHERE  a.str_application_id = b.str_application_id" +
				" AND a.num_branch_id = b.num_branch_id" +
				" AND a.num_id = (SELECT Max(num_id) FROM bis_dev.pc_application_tracking c WHERE" +
				" c.str_application_id = b.str_application_id AND c.num_branch_id = b.num_branch_id)" +
				" AND a.num_app_status IN ( 5, 54 ) AND a.num_branch_id IN ("+stBranchIds+") AND num_state_id " +
				" LIKE Decode('"+iStateId+"', '0', '%','"+iStateId+"')" +
				" GROUP  BY a.num_branch_id) " +
			" UNION" +
				" SELECT 'Grace Period Acceptance Pending at IO' AS status," +
				" Nvl(Sum(cnt), 0)  AS sum FROM (SELECT Count(*) AS cnt" +
				" FROM bis_dev.pc_application_tracking a,bis_dev.pc_application_factory_dtl b" +
				" WHERE  a.str_application_id = b.str_application_id AND a.num_branch_id = b.num_branch_id" +
				" AND a.num_id = (SELECT Max(num_id)" +
				" FROM bis_dev.pc_application_tracking c WHERE" +
				" c.str_application_id = b.str_application_id AND c.num_branch_id = b.num_branch_id) AND a.num_app_status IN ( 53, 55 )" +
				" AND a.num_branch_id IN ("+stBranchIds+") AND num_state_id LIKE Decode('"+iStateId+"', '0', '%','"+iStateId+"') GROUP  BY a.num_branch_id)" +
			" UNION" +
				" SELECT 'Inspection Pending' AS status, Nvl(Sum(cnt), 0) AS sum" +
				" FROM (SELECT Count(*) AS cnt FROM bis_dev.pc_application_tracking a, bis_dev.pc_application_factory_dtl b" +
				" WHERE a.str_application_id = b.str_application_id AND a.num_branch_id = b.num_branch_id" +
				" AND a.num_id = (SELECT Max(num_id) FROM bis_dev.pc_application_tracking c" +
				" WHERE c.str_application_id = b.str_application_id AND c.num_branch_id = b.num_branch_id) " +
				" AND a.num_app_status IN ( 4, 52, 56 ) AND a.num_branch_id IN ("+stBranchIds+") " +
				" AND num_state_id LIKE Decode('"+iStateId+"', '0', '%','"+iStateId+"') GROUP  BY a.num_branch_id) " +
			" UNION " +
				" SELECT 'Red Form Generation Pending at DO' AS status,((select Nvl(Sum(cnt), 0) AS sum" +
				" FROM (SELECT Count(*) AS cnt FROM   bis_dev.pc_application_tracking a, " +
				" bis_dev.pc_application_factory_dtl b WHERE  a.str_application_id = b.str_application_id" +
				" AND a.num_branch_id = b.num_branch_id AND a.num_id = (SELECT Max(num_id) " +
				" FROM bis_dev.pc_application_tracking c WHERE c.str_application_id = b.str_application_id " +
				" AND c.num_branch_id = b.num_branch_id) AND a.num_app_status IN ( 69, 110, 509 ) " +
				" AND a.num_branch_id IN ( "+stBranchIds+" ) AND num_state_id LIKE Decode('"+iStateId+"', '0', '%','"+iStateId+"') GROUP  BY a.num_branch_id))+(SELECT Nvl(Sum(cnt), 0) " +
				" FROM (SELECT Count(*) AS cnt " +
				" FROM bis_dev.pc_application_tracking a, bis_dev.pc_application_factory_dtl b " +
				" WHERE  a.str_application_id = b.str_application_id " +
				" AND a.num_branch_id = b.num_branch_id " +
				" AND a.num_id = (SELECT Max(num_id) FROM bis_dev.pc_application_tracking c WHERE" +
				" c.str_application_id = b.str_application_id AND c.num_branch_id = b.num_branch_id) " +
				" AND a.num_app_status IN ( 71,175 ) and (select str_is_number from bis_dev.pc_application_submission z " +
				" where z.str_application_id=a.str_application_id and z.num_location_id=a.num_branch_id  ) " +
				" in (select str_is_no from bis_dev.pc_ftr_standard_list) AND a.num_branch_id IN ( "+stBranchIds+" )" +
				" AND num_state_id LIKE Decode('"+iStateId+"', '0', '%','"+iStateId+"') GROUP  BY a.num_branch_id)))" +
			" UNION " +
				" SELECT 'Preliminary Inspection Completed' AS status, Nvl(Sum(cnt), 0) AS sum" +
				" FROM (SELECT Count(*) AS cnt FROM bis_dev.pc_application_tracking a, bis_dev.pc_application_factory_dtl b" +
				" WHERE  a.str_application_id = b.str_application_id AND a.num_branch_id = b.num_branch_id " +
				" AND a.num_id = (SELECT Max(num_id) FROM bis_dev.pc_application_tracking c WHERE " +
				" c.str_application_id = b.str_application_id AND c.num_branch_id = b.num_branch_id) " +
				" AND a.num_app_status IN ( 71,175 ) and (select str_is_number from bis_dev.pc_application_submission z where " +
				" z.str_application_id=a.str_application_id and z.num_location_id=a.num_branch_id  ) " +
				" not in (select str_is_no from bis_dev.pc_ftr_standard_list) AND a.num_branch_id IN ( "+stBranchIds+" )" +
				" AND num_state_id LIKE Decode('"+iStateId+"', '0', '%','"+iStateId+"') GROUP  BY a.num_branch_id)" +
			" UNION " +
				" SELECT 'Recommended for Closure at HOD' AS status, Nvl(Sum(cnt), 0) AS sum " +
				" FROM (SELECT Count(*) AS cnt FROM bis_dev.pc_application_tracking a, bis_dev.pc_application_factory_dtl b" +
				" WHERE a.str_application_id = b.str_application_id AND a.num_branch_id = b.num_branch_id" +
				" AND a.num_id = (SELECT Max(num_id) FROM bis_dev.pc_application_tracking c WHERE " +
				" c.str_application_id = b.str_application_id AND c.num_branch_id = b.num_branch_id) AND a.num_app_status = 30 " +
				" AND a.num_branch_id IN ( "+stBranchIds+" ) AND num_state_id LIKE Decode('"+iStateId+"', '0', '%','"+iStateId+"') GROUP  BY a.num_branch_id)" +
			" UNION " +
				" SELECT 'Summarily Rejected Applications ' AS status,Nvl(Sum(cnt), 0) AS sum" +
				" FROM (SELECT Count(*) AS cnt FROM bis_dev.pc_application_tracking a,bis_dev.pc_application_factory_dtl b" +
				" WHERE a.str_application_id = b.str_application_id AND a.num_branch_id = b.num_branch_id" +
				" AND a.num_id = (SELECT Max(num_id) FROM bis_dev.pc_application_tracking c WHERE " +
				" c.str_application_id = b.str_application_id AND c.num_branch_id = b.num_branch_id) AND a.num_app_status = 160" +
				" AND a.num_branch_id IN ( "+stBranchIds+" ) AND num_state_id LIKE Decode('"+iStateId+"', '0', '%','"+iStateId+"')" +
				" GROUP  BY a.num_branch_id) " +
			" UNION" +
				" SELECT 'Appleal against Closure' AS status, Nvl(Sum(cnt), 0) AS sum" +
				" FROM (SELECT Count(*) AS cnt FROM   bis_dev.pc_application_tracking a, bis_dev.pc_application_factory_dtl b" +
				" WHERE  a.str_application_id = b.str_application_id" +
				" AND a.num_branch_id = b.num_branch_id " +
				" AND a.num_id = (SELECT Max(num_id) FROM   bis_dev.pc_application_tracking c WHERE " +
				" c.str_application_id = b.str_application_id AND c.num_branch_id = b.num_branch_id) AND a.num_app_status = 61" +
				" AND a.num_branch_id IN ( "+stBranchIds+" ) AND num_state_id LIKE Decode('"+iStateId+"', '0', '%','"+iStateId+"') GROUP  BY a.num_branch_id) " +
			" UNION" +
				" SELECT 'Recommended for closure notice at HOD' AS status," +
				" Nvl(Sum(cnt), 0) AS sum FROM (SELECT Count(*) AS cnt" +
				" FROM   bis_dev.pc_application_tracking a, bis_dev.pc_application_factory_dtl b " +
				" WHERE  a.str_application_id = b.str_application_id AND a.num_branch_id = b.num_branch_id " +
				" AND a.num_id = (SELECT Max(num_id) FROM   bis_dev.pc_application_tracking c WHERE " +
				" c.str_application_id = b.str_application_id AND c.num_branch_id = b.num_branch_id) AND a.num_app_status = 31 " +
				" AND a.num_branch_id IN ( "+stBranchIds+" ) AND num_state_id LIKE Decode('"+iStateId+"', '0', '%','"+iStateId+"') GROUP  BY a.num_branch_id)" +
			" UNION " +
				" SELECT 'Red Form pending at HOD' AS status, Nvl(Sum(cnt), 0) AS sum" +
				" FROM (SELECT Count(*) AS cnt FROM bis_dev.pc_application_tracking a, bis_dev.pc_application_factory_dtl b" +
				" WHERE  a.str_application_id = b.str_application_id AND a.num_branch_id = b.num_branch_id" +
				" AND a.num_id = (SELECT Max(num_id) FROM   bis_dev.pc_application_tracking c WHERE c.str_application_id = b.str_application_id AND c.num_branch_id = b.num_branch_id)" +
				" AND a.num_app_status = 295 AND a.num_branch_id IN ( "+stBranchIds+" ) AND num_state_id LIKE Decode('"+iStateId+"', '0', '%','"+iStateId+"')GROUP  BY a.num_branch_id)" +
			" UNION" +
				" SELECT 'Closure Notice Issued' AS status, Nvl(Sum(cnt), 0) AS sum" +
				" FROM (SELECT Count(*) AS cnt FROM   bis_dev.pc_application_tracking a, bis_dev.pc_application_factory_dtl b " +
				" WHERE  a.str_application_id = b.str_application_id AND a.num_branch_id = b.num_branch_id AND a.num_id = (SELECT Max(num_id)FROM   bis_dev.pc_application_tracking c " +
				" WHERE c.str_application_id = b.str_application_id AND c.num_branch_id = b.num_branch_id)" +
				" AND a.num_app_status = 24 AND a.num_branch_id IN ( "+stBranchIds+" ) AND num_state_id LIKE Decode('"+iStateId+"', '0', '%','"+iStateId+"') GROUP  BY a.num_branch_id) ORDER  BY 2 DESC,1 ASC  ";
		
		List<Map<String,Object>> runQry = jdbcTemplate.queryForList(qry);
		if(runQry.size()>0){
			for(int i=0;i<runQry.size();i++){
				HashMap<String,String> hm = new HashMap<String,String>();
				Map<String,Object> temp = runQry.get(i);
				hm.put("status", temp.get("status")+"");
				hm.put("cnt", temp.get("sum")+"");
				lhm.add(hm);
			}
		}
		
		return lhm;
	}
}
