package Applicant.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface NewDGdashboardservice {

	Map<String, String> gettotalnooflicences(String selectedBranchId, String fromDate, String toDate);

	Map<String, String> getCountforRowFive(String selectedBranchId, String fromDate, String toDate);

	Map<String, String> getCountforRowFour(String selectedBranchId, String fromDate, String toDate);

	Map<String, String> getCountforRowThree(String selectedBranchId, String fromDate, String toDate);

	Map<String, String> getCountforRowTwo(String selectedBranchId, String fromDate, String toDate);

	String getAllBranchIds();

	String getBranchIdsbylocationId(String regionId);

	List<HashMap<String, String>> getBranch(String parameter);
	

	
}
