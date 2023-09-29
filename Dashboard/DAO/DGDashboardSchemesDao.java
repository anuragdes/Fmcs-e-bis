package Global.Dashboard.DAO;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import Global.CommonUtility.DAO.DaoHelper;

@Repository
public class DGDashboardSchemesDao {

	@Autowired
	DaoHelper daoHelper;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public List<LinkedHashMap<String, String>> getSchemesForRoleId(int iRoleId) {
		List<LinkedHashMap<String, String>> lhm = new ArrayList<LinkedHashMap<String, String>>();
		
		String qry = "SELECT a.num_id, a.str_scheme_icon, a.str_scheme_name " +
				" FROM dashboard.dashboard_scheme_mst a, dashboard.dashboard_role_scheme_mapping b" +
				" where a.num_is_valid=b.num_is_valid and a.num_is_valid=1 and a.num_id=b.num_scheme_id and b.num_role_id="+iRoleId+" ORDER BY a.num_id";
		
		List<Map<String,Object>> runQry = jdbcTemplate.queryForList(qry);
		if(runQry.size()>0){
			for(int i=0;i<runQry.size();i++){
				LinkedHashMap<String,String> hm = new LinkedHashMap<String,String>();
				Map<String,Object> temp = runQry.get(i);
				hm.put("num_id", temp.get("num_id")+"");
				hm.put("str_scheme_icon", temp.get("str_scheme_icon")+"");
				hm.put("str_scheme_name", temp.get("str_scheme_name")+"");
				
				lhm.add(hm);
			}
		}
		
		return lhm;
	}
	
}
