package Global.Dashboard.DAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import Global.CommonUtility.DAO.DaoHelper;
import lab.domain.Lab_Master_Domain;

@Repository
public class DGDashboardLabDao {
	@Autowired
	DaoHelper daoHelper;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	DGDashboardPCDao dgpcdao;
	
	public List<HashMap<String,String>> getPendingReportsAtBisLab(int iRoleId, int iRegionId, int iBranchId, int iLabId, int iLabTypeId){
		List<HashMap<String,String>> lhm = new ArrayList<HashMap<String,String>>();
		
		String qry = "select sum(total) as count,labName from (select count(*) total,b.str_lab_name as labName " +
				" from bislab.lab_test_request_sample_dt a,bislab.lab_mst b where a.dt_entry_date > '14-May-2017' " +
				" and a.num_status_id in (59,80,44,45,46,310) and a.num_isvalid=1 and b.num_isvalid=1 and a.num_lab_type_id=1 " +
				" and a.num_lab_id=b.str_lab_code and a.num_lab_id <> 0 and b.num_lab_type = 1 and b.num_lab_type like decode("+iLabTypeId+",0,'%',"+iLabTypeId+")" +
				" and a.num_lab_id like decode("+iRoleId+",8,'%',30,'%',21,'%',51,'%',24,'%',11,decode("+iLabId+",0,'%', "+iLabId+"),4,decode("+iLabId+",0,'%', "+iLabId+"),7,decode("+iLabId+",0,'%', "+iLabId+")) " +
				" and a.num_app_branch_id in (" +
				" SELECT num_branch_id FROM bis_dev.gblt_branch_mst WHERE num_isvalid = 1 AND num_branch_id <> 41 AND "+iRoleId+"  IN ( 8,30,21,51,24,11 ) " +
				" AND num_ro_id LIKE Decode("+iRegionId+", '4', '%',"+iRegionId+") AND num_branch_id LIKE Decode("+iBranchId+", '0', '%',"+iBranchId+") " +
				" UNION  " +
				" SELECT num_branch_id FROM bis_dev.gblt_branch_mst  WHERE num_isvalid = 1 AND "+iRoleId+" IN ( 7 ) AND num_ro_id = 4 AND num_branch_id LIKE Decode("+iBranchId+", '0', '%',"+iBranchId+")" +
				" UNION " +
				" SELECT "+iBranchId+" FROM DUAL WHERE "+iRoleId+" IN ( 4 )) group by b.str_lab_name" +
				" union all " +
				" select count(*) total,b.str_lab_name as labName from bislab.cml_lab_test_request_sample_dt a,bislab.lab_mst b where a.dt_entry_date > '14-May-2017' " +
				" and a.num_status_id in (59,80,44,45,46,310) and a.num_isvalid=1 and a.num_lab_type_id=1 and b.num_isvalid=1 and a.num_lab_id=b.str_lab_code and a.num_lab_id <> 0  " +
				" and b.num_lab_type = 1 and b.num_lab_type like decode("+iLabTypeId+",0,'%',"+iLabTypeId+")" +
				" and a.num_lab_id like decode("+iRoleId+",8,'%',30,'%',21,'%',51,'%',24,'%',11,decode("+iLabId+",0,'%', "+iLabId+"),4,decode("+iLabId+",0,'%', "+iLabId+"),7,decode("+iLabId+",0,'%', "+iLabId+")) " +
				" and a.num_lic_branch_id in (" +
				" SELECT num_branch_id FROM bis_dev.gblt_branch_mst WHERE num_isvalid = 1 AND num_branch_id <> 41 AND "+iRoleId+"  IN ( 8,30,21,51,24,11 ) " +
				" AND num_ro_id LIKE Decode("+iRegionId+", '4', '%',"+iRegionId+") AND num_branch_id LIKE Decode("+iBranchId+", '0', '%',"+iBranchId+") " +
				" UNION  " +
				" SELECT num_branch_id FROM bis_dev.gblt_branch_mst  WHERE num_isvalid = 1 AND "+iRoleId+" IN ( 7 ) AND num_ro_id = 4 AND num_branch_id LIKE Decode("+iBranchId+", '0', '%',"+iBranchId+")" +
				" UNION " +
				" SELECT "+iBranchId+" FROM DUAL WHERE "+iRoleId+" IN ( 4 )) group by b.str_lab_name) group by labName";
		
		List<Map<String,Object>> runQry = jdbcTemplate.queryForList(qry);
		if(runQry.size()>0){
			for(int i=0;i<runQry.size();i++){
				Map<String,Object> temp = runQry.get(i);
				HashMap<String,String> hm = new HashMap<String,String>();
				hm.put("count", temp.get("count")+"");
				hm.put("labName", temp.get("labName")+"");
				lhm.add(hm);
			}
		}
		
		return lhm;
	}
	
	
	
	public List<HashMap<String,String>> getPendingReportsAtOSLLab(int iRoleId, int iRegionId, int iBranchId, int iLabId, int iLabTypeId){
		List<HashMap<String,String>> lhm = new ArrayList<HashMap<String,String>>();
		
		String qry = "select sum(total) as count,labName from (select count(*) total,b.str_lab_name as labName from " +
				" bislab.lab_test_request_sample_dt a,bislab.lab_mst b where a.dt_entry_date > '14-May-2017' " +
				" and a.num_status_id in (59,80,44,45,46,310) and a.num_isvalid=1 and b.num_isvalid=1" +
				" and a.num_lab_type_id=2 and a.num_lab_type_id like decode("+iLabTypeId+",0,'%',"+iLabTypeId+") and a.num_lab_id=b.str_lab_code and a.num_lab_id <> 0 " +
				" and a.num_lab_id like decode("+iRoleId+",8,'%',30,'%',21,'%',51,'%',24,'%',11,decode("+iLabId+",0,'%', "+iLabId+"),4,decode("+iLabId+",0,'%', "+iLabId+"),7,decode("+iLabId+",0,'%', "+iLabId+")) " +
				" and a.num_app_branch_id in (" +
				" SELECT num_branch_id FROM bis_dev.gblt_branch_mst WHERE num_isvalid = 1 AND num_branch_id <> 41 AND "+iRoleId+"  " +
				" IN ( 8,30,21,51,24,11 ) " +
				" AND num_ro_id LIKE Decode("+iRegionId+", '4', '%',"+iRegionId+") AND num_branch_id LIKE Decode("+iBranchId+", '0', '%',"+iBranchId+") " +
				" UNION " +
				" SELECT num_branch_id FROM bis_dev.gblt_branch_mst  WHERE num_isvalid = 1 AND "+iRoleId+" IN ( 7 ) " +
				" AND num_ro_id = 4 AND num_branch_id LIKE Decode("+iBranchId+", '0', '%',"+iBranchId+") " +
				" UNION " +
				" SELECT "+iBranchId+" FROM DUAL WHERE "+iRoleId+" IN ( 4 )) group by b.str_lab_name" +
				" union all " +
				" select count(*) total,b.str_lab_name as labName from bislab.cml_lab_test_request_sample_dt a,bislab.lab_mst b " +
				" where a.dt_entry_date > '14-May-2017' " +
				" and a.num_status_id in (59,80,44,45,46,310) and a.num_isvalid=1 and b.num_isvalid=1 " +
				" and a.num_lab_type_id=2 and a.num_lab_type_id like decode("+iLabTypeId+",0,'%',"+iLabTypeId+") and a.num_lab_id=b.str_lab_code and a.num_lab_id <> 0 " +
				" and a.num_lab_id like decode("+iRoleId+",8,'%',30,'%',21,'%',51,'%',24,'%',11,decode("+iLabId+",0,'%', "+iLabId+"),4,decode("+iLabId+",0,'%', "+iLabId+"),7,decode("+iLabId+",0,'%', "+iLabId+")) " +
				" and a.num_lic_branch_id in (" +
				" SELECT num_branch_id FROM bis_dev.gblt_branch_mst WHERE num_isvalid = 1 AND num_branch_id <> 41 AND "+iRoleId+"  IN ( 8,30,21,51,24,11 ) " +
				" AND num_ro_id LIKE Decode("+iRegionId+", '4', '%',"+iRegionId+") AND num_branch_id LIKE Decode("+iBranchId+", '0', '%',"+iBranchId+") " +
				" UNION  " +
				" SELECT num_branch_id FROM bis_dev.gblt_branch_mst  WHERE num_isvalid = 1 AND "+iRoleId+" IN ( 7 ) AND num_ro_id = 4 AND num_branch_id LIKE Decode("+iBranchId+", '0', '%',"+iBranchId+")" +
				" UNION " +
				" SELECT "+iBranchId+" FROM DUAL WHERE "+iRoleId+" IN ( 4 )) group by b.str_lab_name) group by labName limit 20";
		
		List<Map<String,Object>> runQry = jdbcTemplate.queryForList(qry);
		if(runQry.size()>0){
			for(int i=0;i<runQry.size();i++){
				Map<String,Object> temp = runQry.get(i);
				HashMap<String,String> hm = new HashMap<String,String>();
				hm.put("count", temp.get("count")+"");
				hm.put("labName", temp.get("labName")+"");
				lhm.add(hm);
			}
		}
		
		return lhm;
	}
	
	
	public List<HashMap<String,String>> getUploadedReportsAtBisLab(int iRoleId, int iRegionId, int iBranchId, int iLabId, int iLabTypeId){
		List<HashMap<String,String>> lhm = new ArrayList<HashMap<String,String>>();
		
		String qry = "select sum(total) as count,labName from (select count(*) total,b.str_lab_name as labName " +
				" from bislab.lab_test_request_sample_dt a,bislab.lab_mst b where a.dt_entry_date > '14-May-2017' " +
				" and a.num_status_id in (60,130,131,132) and a.num_isvalid=1 and b.num_isvalid=1 and a.num_lab_type_id=1 " +
				" and a.num_lab_id=b.str_lab_code and a.num_lab_id <> 0 and b.num_lab_type = 1 and b.num_lab_type like decode("+iLabTypeId+",0,'%',"+iLabTypeId+")" +
				" and a.num_lab_id like decode("+iRoleId+",8,'%',30,'%',21,'%',51,'%',24,'%',11,decode("+iLabId+",0,'%', "+iLabId+"),4,decode("+iLabId+",0,'%', "+iLabId+"),7,decode("+iLabId+",0,'%', "+iLabId+")) " +
				" and a.num_app_branch_id in (" +
				" SELECT num_branch_id FROM bis_dev.gblt_branch_mst WHERE num_isvalid = 1 AND num_branch_id <> 41 " +
				" AND "+iRoleId+"  IN ( 8,30,21,51,24,11 ) AND num_ro_id LIKE Decode("+iRegionId+", '4', '%',"+iRegionId+") " +
				" AND num_branch_id LIKE Decode("+iBranchId+", '0', '%',"+iBranchId+") " +
				" UNION  " +
				" SELECT num_branch_id FROM bis_dev.gblt_branch_mst  WHERE num_isvalid = 1 AND "+iRoleId+" IN ( 7 ) " +
				" AND num_ro_id = 4 AND num_branch_id LIKE Decode("+iBranchId+", '0', '%',"+iBranchId+")" +
				" UNION " +
				" SELECT "+iBranchId+" FROM DUAL WHERE "+iRoleId+" IN ( 4 )) group by b.str_lab_name " +
				" union all " +
				" select count(*) total,b.str_lab_name as labName from bislab.cml_lab_test_request_sample_dt a,bislab.lab_mst b " +
				" where a.dt_entry_date > '14-May-2017' " +
				" and a.num_status_id in (60,130,131,132) and a.num_isvalid=1 and a.num_lab_type_id=1 and a.num_lab_type_id like decode("+iLabTypeId+",0,'%',"+iLabTypeId+") and b.num_isvalid=1 " +
				" and a.num_lab_id=b.str_lab_code and a.num_lab_id <> 0 and b.num_lab_type = 1" +
				" and a.num_lab_id like decode("+iRoleId+",8,'%',30,'%',21,'%',51,'%',24,'%',11,decode("+iLabId+",0,'%', "+iLabId+"),4,decode("+iLabId+",0,'%', "+iLabId+"),7,decode("+iLabId+",0,'%', "+iLabId+")) " +
				" and a.num_lic_branch_id in (" +
				" SELECT num_branch_id FROM bis_dev.gblt_branch_mst WHERE num_isvalid = 1 AND num_branch_id <> 41 " +
				" AND "+iRoleId+"  IN ( 8,30,21,51,24,11 ) AND num_ro_id LIKE Decode("+iRegionId+", '4', '%',"+iRegionId+") " +
				" AND num_branch_id LIKE Decode("+iBranchId+", '0', '%',"+iBranchId+") " +
				" UNION " +
				" SELECT num_branch_id FROM bis_dev.gblt_branch_mst  WHERE num_isvalid = 1 AND "+iRoleId+" IN ( 7 ) AND num_ro_id = 4 " +
				" AND num_branch_id LIKE Decode("+iBranchId+", '0', '%',"+iBranchId+")" +
				" UNION " +
				" SELECT "+iBranchId+" FROM DUAL WHERE "+iRoleId+" IN ( 4 )) group by b.str_lab_name) group by labName";
		
		List<Map<String,Object>> runQry = jdbcTemplate.queryForList(qry);
		if(runQry.size()>0){
			for(int i=0;i<runQry.size();i++){
				Map<String,Object> temp = runQry.get(i);
				HashMap<String,String> hm = new HashMap<String,String>();
				hm.put("count", temp.get("count")+"");
				hm.put("labName", temp.get("labName")+"");
				lhm.add(hm);
			}
		}
		
		return lhm;
	}
	
	
	public List<HashMap<String,String>> getUploadedReportsAtOSLLab(int iRoleId, int iRegionId, int iBranchId, int iLabId, int iLabTypeId){
		List<HashMap<String,String>> lhm = new ArrayList<HashMap<String,String>>();
		
		String qry = "select sum(total) as count,labName from (select count(*) total,b.str_lab_name as labName " +
				" from bislab.lab_test_request_sample_dt a,bislab.lab_mst b where a.dt_entry_date > '14-May-2017' " +
				" and a.num_status_id in (60,130,131,132) and a.num_isvalid=1 and b.num_isvalid=1" +
				" and a.num_lab_type_id=2 and a.num_lab_type_id like decode("+iLabTypeId+",0,'%',"+iLabTypeId+") and a.num_lab_id=b.str_lab_code and a.num_lab_id <> 0 " +
				" and a.num_lab_id like decode("+iRoleId+",8,'%',30,'%',21,'%',51,'%',24,'%',11,decode("+iLabId+",0,'%', "+iLabId+"),4,decode("+iLabId+",0,'%', "+iLabId+"),7,decode("+iLabId+",0,'%', "+iLabId+")) " +
				" and a.num_app_branch_id in (" +
				" SELECT num_branch_id FROM bis_dev.gblt_branch_mst WHERE num_isvalid = 1 AND num_branch_id <> 41 " +
				" AND "+iRoleId+"  IN ( 8,30,21,51,24,11 ) " +
				" AND num_ro_id LIKE Decode("+iRegionId+", '4', '%',"+iRegionId+") AND num_branch_id LIKE Decode("+iBranchId+", '0', '%',"+iBranchId+") " +
				" UNION  " +
				" SELECT num_branch_id FROM bis_dev.gblt_branch_mst  WHERE num_isvalid = 1 AND "+iRoleId+" IN ( 7 ) " +
				" AND num_ro_id = 4 AND num_branch_id LIKE Decode("+iBranchId+", '0', '%',"+iBranchId+")" +
				" UNION " +
				" SELECT "+iBranchId+" FROM DUAL WHERE "+iRoleId+" IN ( 4 )) group by b.str_lab_name" +
				" union all " +
				" select count(*) total,b.str_lab_name as labName from bislab.cml_lab_test_request_sample_dt a,bislab.lab_mst b " +
				" where a.dt_entry_date > '14-May-2017' and a.num_status_id in (60,130,131,132) and a.num_isvalid=1 " +
				" and b.num_isvalid=1 " +
				" and a.num_lab_type_id=2 and a.num_lab_type_id like decode("+iLabTypeId+",0,'%',"+iLabTypeId+") and a.num_lab_id=b.str_lab_code and a.num_lab_id <> 0 " +
				" and a.num_lab_id like decode("+iRoleId+",8,'%',30,'%',21,'%',51,'%',24,'%',11,decode("+iLabId+",0,'%', "+iLabId+"),4,decode("+iLabId+",0,'%', "+iLabId+"),7,decode("+iLabId+",0,'%', "+iLabId+")) " +
				" and a.num_lic_branch_id in (" +
				" SELECT num_branch_id FROM bis_dev.gblt_branch_mst WHERE num_isvalid = 1 AND num_branch_id <> 41 AND "+iRoleId+"  IN ( 8,30,21,51,24,11 ) " +
				" AND num_ro_id LIKE Decode("+iRegionId+", '4', '%',"+iRegionId+") AND num_branch_id LIKE Decode("+iBranchId+", '0', '%',"+iBranchId+") " +
				" UNION " +
				" SELECT num_branch_id FROM bis_dev.gblt_branch_mst  WHERE num_isvalid = 1 AND "+iRoleId+" IN ( 7 ) AND num_ro_id = 4 " +
				" AND num_branch_id LIKE Decode("+iBranchId+", '0', '%',"+iBranchId+")" +
				" UNION  " +
				" SELECT "+iBranchId+" FROM DUAL WHERE "+iRoleId+" IN ( 4 ))group by b.str_lab_name) group by labName limit 20";
		
		List<Map<String,Object>> runQry = jdbcTemplate.queryForList(qry);
		if(runQry.size()>0){
			for(int i=0;i<runQry.size();i++){
				Map<String,Object> temp = runQry.get(i);
				HashMap<String,String> hm = new HashMap<String,String>();
				hm.put("count", temp.get("count")+"");
				hm.put("labName", temp.get("labName")+"");
				lhm.add(hm);
			}
		}
		
		return lhm;
	}
	
	
	public List<Lab_Master_Domain> getLabList(int iLabType){
		String qry = "SELECT p FROM Lab_Master_Domain p WHERE p.isValid=1 and p.numLabType="+iLabType;
		List<Lab_Master_Domain> runQry = daoHelper.findByQuery(qry);
		return runQry;
	}

}
