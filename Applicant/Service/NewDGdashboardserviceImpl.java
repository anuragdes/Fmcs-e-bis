package Applicant.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Applicant.DAO.NewDGDashboardDAO;


@Service
public class NewDGdashboardserviceImpl implements NewDGdashboardservice{
	
	@Autowired
	NewDGDashboardDAO DGdao;

	@Override
	public Map<String, String> gettotalnooflicences(String selectedBranchId, String fromDate, String toDate) {
		return DGdao.gettotalnooflicences(selectedBranchId,fromDate,toDate);
	}

	@Override
	public Map<String, String> getCountforRowFive(String selectedBranchId, String fromDate, String toDate) {
			return DGdao.getCountforRowFive(selectedBranchId,fromDate,toDate);
	}

	@Override
	public Map<String, String> getCountforRowFour(String selectedBranchId, String fromDate, String toDate) {
		return DGdao.getCountforRowFour(selectedBranchId,fromDate,toDate);
	}

	@Override
	public Map<String, String> getCountforRowThree(String selectedBranchId, String fromDate, String toDate) {
		return DGdao.getCountforRowThree(selectedBranchId,fromDate,toDate);
	}

	@Override
	public Map<String, String> getCountforRowTwo(String selectedBranchId, String fromDate, String toDate) {
		return DGdao.getCountforRowTwo(selectedBranchId,fromDate,toDate);
	}

	@Override
	public String getAllBranchIds() {
		return DGdao.getAllBranchIds();
	}

	@Override
	public String getBranchIdsbylocationId(String regionId) {
		return DGdao.getBranchIdsbylocationId(regionId);
	}

	@Override
	public List<HashMap<String, String>> getBranch(String locationId) {
		List<HashMap<String, String>> brcnh = new ArrayList<HashMap<String, String>>();
		List<Map<String,Object>> branchList= DGdao.getBranch(locationId);
		if(branchList.size()!=0){
			for (Map<String, Object> tempRow : branchList) {
				HashMap<String,String> brh = new HashMap<String,String>();
				brh.put("branch",tempRow.get("name")+"");
				brh.put("branch_id",tempRow.get("bid")+"");
				brcnh .add(brh);
			}
		}
		return brcnh;
	}


}







