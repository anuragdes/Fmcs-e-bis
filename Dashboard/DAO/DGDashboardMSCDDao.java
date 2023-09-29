package Global.Dashboard.DAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DGDashboardMSCDDao {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public List<HashMap<String,String>> getApplications(int iRoleId,int iRegionId){
		List<HashMap<String,String>> lhm = new ArrayList<HashMap<String,String>>();
		
		String qry = "SELECT count(c.str_ro_name) count,c.str_ro_name region FROM bis_mscd.mscd_application_hq_details a, " +
				" bis_dev.gblt_regional_mst c,bis_mscd.mscd_application_tracking g" +
				" where a.is_valid=1 and a.num_licensesgranted=0 and  c.num_ro_id=a.num_region_id  " +
				"and g.str_application_id=a.num_id and g.num_region_id=a.num_region_id" +
				" and g.num_id=(select max(num_id) from bis_mscd.mscd_application_tracking gg where gg.str_application_id=g.str_application_id and gg.num_region_id=g.num_region_id) " +
				" and a.num_region_id LIKE Decode("+iRegionId+", '4', '%',"+iRegionId+") " +
				"and a.num_region_id LIKE Decode("+iRoleId+", '8', '%','30','%', '44', '%','18','%',"+iRegionId+")" +
				" GROUP BY c.str_ro_name";
		
		System.out.println(qry);
		
		List<Map<String,Object>> runQry = jdbcTemplate.queryForList(qry);
		if(runQry.size()>0){
			for(int i=0;i<runQry.size();i++){
				HashMap<String,String> hm = new HashMap<String,String>();
				Map<String,Object> temp = runQry.get(i);
				hm.put("region", temp.get("region")+"");
				hm.put("cnt", temp.get("count")+"");
				lhm.add(hm);
			}
		}

		
		return lhm;
	}
	
	
	public List<HashMap<String,String>> getLicences(int iRoleId,int iRegionId){
		List<HashMap<String,String>> lhm = new ArrayList<HashMap<String,String>>();
		
		String qry = "SELECT count(c.str_ro_name) count ,c.str_ro_name region" +
				" FROM bis_mscd.mscd_license_hq_details a,  bis_dev.gblt_regional_mst c " +
				" where c.num_ro_id=a.num_region_id and a.num_region_id LIKE Decode("+iRegionId+", '4', '%',"+iRegionId+")" +
				" and a.num_region_id LIKE Decode("+iRoleId+", '8', '%','30','%', '44', '%','18', '%',"+iRegionId+") GROUP BY c.str_ro_name";
		
		System.out.println(qry);
		
		List<Map<String,Object>> runQry = jdbcTemplate.queryForList(qry);
		if(runQry.size()>0){
			for(int i=0;i<runQry.size();i++){
				HashMap<String,String> hm = new HashMap<String,String>();
				Map<String,Object> temp = runQry.get(i);
				hm.put("region", temp.get("region")+"");
				hm.put("cnt", temp.get("count")+"");
				lhm.add(hm);
			}
		}
		
		return lhm;
	}

}
