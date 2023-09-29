package Global.Dashboard.DAO;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import Global.CommonUtility.DAO.DaoHelper;
import Global.Dashboard.Domain.DashboardRoleSchemeMappingDomain;
import Masters.Domain.Branch_Master_Domain;
import Masters.Domain.Regional_Mst_Domain;
import Masters.Domain.State_Mst_Domain;
import lab.domain.Lab_Type_Domain;

@Repository
public class DGDashboardDao {
	
	@Autowired
	DaoHelper daoHelper;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public boolean checkDashboardAccess(int iRoleId){
		boolean bResponse = false;
		String qry = "SELECT p FROM DashboardRoleSchemeMappingDomain p WHERE p.num_is_valid=1 and p.num_role_id="+iRoleId;
		List<DashboardRoleSchemeMappingDomain> runQry = daoHelper.findByQuery(qry);
		if(runQry.size()>0){
			bResponse = true;
		}
		
		return bResponse;
	}
	
	public List<Regional_Mst_Domain> getDynamicRegions(int iRoleId, int iLocationId){
		List<Regional_Mst_Domain> lrmd = new ArrayList<Regional_Mst_Domain>();
		String qry = "";
		if(iRoleId==7){
			//DDGR, limit to region
			qry = "SELECT p FROM Regional_Mst_Domain p WHERE p.IsValid=1 and p.numRegionId="+iLocationId+" order by p.strRoName";
		}
		else if(iRoleId==4){
			//HOD, limit to region
			qry = "SELECT p FROM Regional_Mst_Domain p WHERE p.IsValid=1 and p.numRegionId=(select x.numRoId FROM Branch_Master_Domain x WHERE x.numBranchId="+iLocationId+" and x.IsValid=1) order by p.strRoName";
		}
		else{
			//HQ
			qry = "SELECT p FROM Regional_Mst_Domain p WHERE p.IsValid=1 and p.numRegionId!=4 order by p.strRoName";
		}
		
		List<Regional_Mst_Domain> runQry = daoHelper.findByQuery(qry);
		if(runQry.size()>0){
			lrmd = runQry;
		}
		
		return lrmd;
	}
	
	public List<Branch_Master_Domain> getDynamicBranches(int iRoleId, int iLocationId, int iRegionId){
		List<Branch_Master_Domain> lrmd = new ArrayList<Branch_Master_Domain>();
		String qry = "";
		
		if(iRoleId==4){
			qry = "select x FROM Branch_Master_Domain x WHERE x.numBranchId="+iLocationId+" and x.numBranchId!=41 and x.IsValid=1 order by x.strBrShortName";
		}
		else if(iRoleId==7){
			qry = "SELECT p FROM Branch_Master_Domain p WHERE p.IsValid=1 and p.numBranchId!=41 and p.numRoId="+iRegionId+" order by p.strBrShortName";
		}
		else{
			if(iRegionId==4){
				qry = "SELECT p FROM Branch_Master_Domain p WHERE p.IsValid=1 and p.numBranchId!=41 order by p.strBrShortName";
			}
			else{
				qry = "SELECT p FROM Branch_Master_Domain p WHERE p.IsValid=1 and p.numBranchId!=41 and p.numRoId="+iRegionId+" order by p.strBrShortName";
			}
		}
		
		List<Branch_Master_Domain> runQry = daoHelper.findByQuery(qry);
		if(runQry.size()>0){
			lrmd = runQry;
		}
		
		return lrmd;
	}

	
	public List<State_Mst_Domain> getStates(){
		List<State_Mst_Domain> lrmd = new ArrayList<State_Mst_Domain>();
		String qry =  "select x FROM State_Mst_Domain x WHERE x.IsValid=1 ORDER BY x.strStateName";
		
		List<State_Mst_Domain> runQry = daoHelper.findByQuery(qry);
		if(runQry.size()>0){
			lrmd = runQry;
		}
		
		return lrmd;
	}
	
	public List<Lab_Type_Domain> getLabTypes(){
		List<Lab_Type_Domain> lrmd = new ArrayList<Lab_Type_Domain>();
		String qry = "SELECT p FROM Lab_Type_Domain p WHERE p.isValid=1 and p.numLabTypeId!=3 order by p.numLabTypeId";
		
		List<Lab_Type_Domain> runQry = daoHelper.findByQuery(qry);
		if(runQry.size()>0){
			lrmd = runQry;
		}
		
		return lrmd;
	}
}
