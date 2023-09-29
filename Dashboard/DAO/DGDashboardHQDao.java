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
public class DGDashboardHQDao {

	@Autowired
	DaoHelper daoHelper;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public List<HashMap<String,String>> getStateWisePendingApplications(){
		List<HashMap<String,String>> lhm = new ArrayList<HashMap<String,String>>();
		
		String qry = "SELECT Count(a.str_application_id) AS cnt,decode( (SELECT Initcap(s.str_state_name)" +
				" FROM bis_dev.gblt_state_mst s " +
				" WHERE s.num_state_id = decode(b.num_state_id,32,2,27,31,8,21,9,12,b.num_state_id)),'Jammu & Kashmir','Jammu and Kashmir','Odisha','Orissa','Uttarakhand','IN-UT','Andaman  & Nicobar','Andaman and Nicobar Islands'," +
				" (SELECT Initcap(s.str_state_name) FROM bis_dev.gblt_state_mst s WHERE  s.num_state_id = decode(b.num_state_id,32,2,27,31,8,21,9,12,b.num_state_id))) state" +
				" FROM bis_dev.pc_application_tracking a,bis_dev.pc_application_factory_dtl b,bis_dev.pc_application_submission c" +
				" WHERE a.str_application_id = b.str_application_id AND a.num_branch_id = b.num_branch_id" +
				" AND a.str_application_id = c.str_application_id AND a.num_branch_id = c.num_location_id " +
				" AND a.num_id = (SELECT Max(num_id) FROM bis_dev.pc_application_tracking b WHERE  a.str_application_id = b.str_application_id" +
				" AND a.num_branch_id = b.num_branch_id) AND a.num_app_status NOT IN ( 3, 201 ) " +
				" AND ( a.str_application_id, a.num_branch_id ) NOT IN (SELECT x.str_app_id, x.num_branch_id FROM bis_dev.cml_licence_detail x)" +
				" AND c.num_application_status_id = 27 AND c.num_location_id != 41" +
				" GROUP  BY  (SELECT Initcap(s.str_state_name) FROM   bis_dev.gblt_state_mst s WHERE  s.num_state_id = decode(b.num_state_id,32,2,27,31,8,21,9,12,b.num_state_id)) ORDER  BY state";
		
		List<Map<String,Object>> runQry = jdbcTemplate.queryForList(qry);
		if(runQry.size()>0){
			for(int i=0;i<runQry.size();i++){
				HashMap<String,String> hm = new HashMap<String,String>();
				Map<String,Object> temp = runQry.get(i);
				hm.put("state", temp.get("state")+"");
				hm.put("cnt", temp.get("cnt")+"");
				lhm.add(hm);
			}
		}

		
		return lhm;
	}
	
	
	public List<HashMap<String,String>> getHQPCPieData(){
		List<HashMap<String,String>> lhm = new ArrayList<HashMap<String,String>>();
		
		String qry = "select 'Pending Applications' as l,count(a.str_application_id) as cnt from bis_dev.pc_application_tracking a where a.num_id=(select max(num_id) from bis_dev.pc_application_tracking b where a.str_application_id=b.str_application_id and a.num_branch_id=b.num_branch_id)" +
				" and a.num_app_status!=3 and a.num_branch_id !=41 and (a.str_application_id,a.num_branch_id) not in (select x.str_app_id,x.num_branch_id from bis_dev.cml_licence_detail x)" +
				" union " +
				" select 'Operative Licences' as l,count(a.str_cml_no) as cnt from bis_dev.cml_licence_status_dtl a where a.num_cml_status_id=201 and a.num_branch_id !=41" +
				" union " +
				" select 'Deferred Licences' as l,count(a.str_cml_no) as cnt from bis_dev.cml_licence_status_dtl a where a.num_cml_status_id=202 and a.num_branch_id !=41" +
				" union " +
				" select 'Stop Marked Licences' as l,count(a.str_cml_no) as cnt from bis_dev.cml_licence_status_dtl a where a.num_cml_status_id in (200,204) and a.num_branch_id !=41";
		
		List<Map<String,Object>> runQry = jdbcTemplate.queryForList(qry);
		if(runQry.size()>0){
			for(int i=0;i<runQry.size();i++){
				HashMap<String,String> hm = new HashMap<String,String>();
				Map<String,Object> temp = runQry.get(i);
				hm.put("status", temp.get("l")+"");
				hm.put("cnt", temp.get("cnt")+"");
				lhm.add(hm);
			}
		}
		
		return lhm;
	}
	
}
