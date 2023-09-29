package Component.DAO;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import Global.CommonUtility.DAO.DaoHelper;
import Schemes.ProductCertification.ApplicationSubmission.Domain.ComponentMasterDomain;
import Schemes.ProductCertification.ApplicationSubmission.Domain.PcGOLDomain;
import Schemes.ProductCertification.ApplicationSubmission.Domain.componentsDomain;

@SuppressWarnings({"unused","rawtypes","deprecation"})
@Repository
public class ComponentDAO {
	@Autowired
	public DaoHelper daoHelper;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	
	@SuppressWarnings("rawtypes")
	public List<Map<String,Object>> getTableData()
	{
		    String query = "SELECT  num_sno,str_clause_desc,str_component_type, test_observation_mst_id, visibility_class,(select STRING_AGG(co.observation_id||'#'||co.observation_desc,',') from gblt_sub_is_comp_mapping co where to_char(co.observation_id) in((SELECT foo FROM regexp_split_to_table(test_observation_mst_id,',') AS foo)) and co.is_valid=1) observation_desc,to_char(clause_seq_no) clause_seq_no,is_remark_val FROM gblt_is_comp_mapping_master WHERE stan_id='14543' order by to_number(to_char(clause_sub_seq_no))";
		    List<Map<String,Object>> runQry = new ArrayList<Map<String,Object>>();
			System.out.println("::final query ::::::::::::::::::::::"+query);
			try{
				runQry=jdbcTemplate.queryForList(query);
		  		return runQry;
			}
			catch(Exception e)
			{
				System.out.println(":::::::::::"+e);
				return null;
			}
	}
	
	public void addComponentDetail(componentsDomain DomainObj) {
		daoHelper.merge(componentsDomain.class, DomainObj);
	}
	
	public void deleteComponentDtls(String FinalApplicationId , int branchId) {
		String query="delete from componentsDomain r where r.str_application_id='"+FinalApplicationId+"' and r.num_branch_id ="+branchId+"";
		daoHelper.deleteByQuery(query);
	}
	
	public List<Map<String,Object>> getTableDataEdit(String FinalApplicationId , int branchId)
	{
		    String isStan = "";
		    long gblUserid = 0;
		    String qry = "SELECT apps FROM PcGOLDomain apps WHERE apps.lnApplicationId ='"+FinalApplicationId+"'";
		    List<PcGOLDomain> runQry1 = daoHelper.findByQuery(qry);
		    if(runQry1.size()>0){
		    	isStan = runQry1.get(0).getStrISno();
		    	gblUserid = runQry1.get(0).getStrGblUserId();	
			}
		
		    String query = "(SELECT  num_sno,str_clause_desc,str_component_type, test_observation_mst_id, visibility_class,(select STRING_AGG(co.observation_id||'#'||co.observation_desc,',') from gblt_sub_is_comp_mapping co where to_char(co.observation_id) in((SELECT foo FROM regexp_split_to_table(test_observation_mst_id,',') AS foo)) and co.is_valid=1) observation_desc,to_char(clause_seq_no) clause_seq_no,is_remark_val,str_value, str_remarks FROM gblt_is_comp_mapping_master left join gblt_sub_comp_data on num_sno=ref_num_sno and str_application_id='"+FinalApplicationId+"' and num_branch_id='"+branchId+"'  WHERE stan_id='"+isStan+"' AND entry_emp_id="+gblUserid+" order by to_number(to_char(clause_sub_seq_no))) union all (SELECT  num_sno,str_clause_desc,str_component_type, test_observation_mst_id, visibility_class,(select STRING_AGG(co.observation_id||'#'||co.observation_desc,',') from gblt_sub_is_comp_mapping co where to_char(co.observation_id) in((SELECT foo FROM regexp_split_to_table(test_observation_mst_id,',') AS foo)) and co.is_valid=1) observation_desc,to_char(clause_seq_no) clause_seq_no,is_remark_val,null,null FROM gblt_is_comp_mapping_master  WHERE stan_id='"+isStan+"' AND str_entry_emp_id="+gblUserid+" order by to_number(to_char(clause_sub_seq_no)))";
		    
		    List<Map<String,Object>> runQry = new ArrayList<Map<String,Object>>();
			try{
				runQry=jdbcTemplate.queryForList(query);
		  		return runQry;
			}
			catch(Exception e)
			{
				System.out.println(":::::::::::"+e);
				return null;
			}
	}
	
	public boolean getIsProdIs(String FinalApplicationId , int branchId)
	{
		    String isStan = "";
		    long gblUserid = 0;
		    String qry = "SELECT apps FROM PcGOLDomain apps WHERE apps.lnApplicationId ='"+FinalApplicationId+"'";
		    List<PcGOLDomain> runQry1 = daoHelper.findByQuery(qry);
		    if(runQry1.size()>0){
		    	isStan = runQry1.get(0).getStrISno();
		    	gblUserid = runQry1.get(0).getStrGblUserId();	
			}
		
			
			String query = "SELECT apps FROM ComponentMasterDomain apps WHERE apps.stan_id='"+isStan+"' AND apps.str_entry_emp_id="+gblUserid;
		    List<ComponentMasterDomain> runQry = daoHelper.findByQuery(query);
			
			if(runQry.size()>0){
				return true;
			} else {
				return false;
			}
	}
	
	public void deleteTestReportOptionOne(String FinalApplicationId , int branchId) {
		String query="delete from rawmatconformityestabopt2GOLdomain r where r.lnApplicationId='"+FinalApplicationId+"' and r.branchId ="+branchId+"";
		daoHelper.deleteByQuery(query);
	}
	
	
}
