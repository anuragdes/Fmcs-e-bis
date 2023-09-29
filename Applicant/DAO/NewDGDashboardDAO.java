package Applicant.DAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import Global.CommonUtility.DAO.DaoHelper;
import lombok.var;

@Repository
public class NewDGDashboardDAO {
	@Autowired
//    @Qualifier(value="jdbcTemplateReadonly")
	JdbcTemplate jdbcTemplate;

	@Autowired
	DaoHelper daoHelper;
	
	public Map<String,String> gettotalnooflicences(String selectedBranchId, String fromDate, String toDate) {
		Map<String,String> resultList=new ConcurrentHashMap<String, String>();
		List<HashMap<String,String>> lhm = new ArrayList<HashMap<String,String>>();
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("standards_vol","1012");
		hm.put("standards_man","1011");
		hm.put("operative_licenses_total","51");
		hm.put("operative_licenses_period","41");
		hm.put("applications_underprocess","10");
		hm.put("applications_received","1");
		hm.put("all_india_first_underprocess","114");
		hm.put("all_india_first_period","44");
		hm.put("income_fy","178");
		hm.put("income_period","104");
		
		HashMap<String, String> hmap=null;
//		String str="";
		for (Map.Entry<String,String> mapElement : hm.entrySet()) {
            hmap=new HashMap<String, String> ();
            hmap.put(mapElement.getKey(), mapElement.getValue());
			lhm.add(hmap);
        }
		lhm.parallelStream().forEach(x->{ 
			HashMap<?, ?> hmTemp=executeMethod(x,selectedBranchId,fromDate,toDate);
			resultList.put(hmTemp.keySet().toArray()[0].toString(),hmTemp.get(hmTemp.keySet().toArray()[0].toString()).toString());
	    });
		 
		System.out.println("\n End Result : 1\n"+resultList);
		return resultList;
	}
	
	HashMap<String,String> executeMethod(HashMap<String,String> hm, String branched, String fromDt, String toDt) {
			long count = 0;
			String key = hm.keySet().toArray()[0].toString();// k;
			String qry = "select count_query from bis_report_mst where num_id='"+hm.get(key)+"'";
			String query = jdbcTemplate.queryForObject(qry, String.class);
			query = query.replaceAll("replaceToDate", toDt).replaceAll("replaceFromDate", fromDt).replaceAll("loc_id",
					branched);
			count = jdbcTemplate.queryForObject(query, Long.class);
			var hmFinal=new HashMap<String,String>();
			hmFinal.put(key, count+"");
		return hmFinal;
	}
	
	
	public  Map<String,String> getCountforRowTwo(String selectedBranchId, String fromDate, String toDate) {
		System.out.println(selectedBranchId+ "getCountforRowTwo:: "+fromDate+"  TO "+toDate);
		Map<String,String> resultList=new ConcurrentHashMap<String, String>();
		List<HashMap<String,String>> lhm = new ArrayList<HashMap<String,String>>();
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("renewals_period","1036");
		hm.put("renewals_due","64");
		hm.put("inclusions_under_process","168");
		hm.put("inclusions_granted","169");
		hm.put("deferments_total","57");
		hm.put("deferments_90days","69");
		hm.put("expired_fy","172");
		hm.put("expired_due","174");
		hm.put("suspensions","54");
		hm.put("suspensions_180","167");
		
		HashMap<String, String> hmap=null;
//		String str="";
		for (Map.Entry<String,String> mapElement : hm.entrySet()) {
            hmap=new HashMap<String, String>();
            hmap.put(mapElement.getKey(), mapElement.getValue());
			lhm.add(hmap);
        }
		lhm.parallelStream().forEach(x->{ 
			HashMap<?, ?> hmTemp=executeMethod(x,selectedBranchId,fromDate,toDate);
			resultList.put(hmTemp.keySet().toArray()[0].toString(),hmTemp.get(hmTemp.keySet().toArray()[0].toString()).toString());
	    });
		 
		System.out.println("\n End Result : 2\n"+resultList);
		return resultList;
	}
	public  Map<String,String> getCountforRowThree(String selectedBranchId, String fromDate, String toDate) {
		System.out.println(selectedBranchId+ "getCountforRowThree:: "+fromDate+"  TO "+toDate);
		Map<String,String> resultList=new ConcurrentHashMap<String, String>();
		List<HashMap<String,String>> lhm = new ArrayList<HashMap<String,String>>();
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("factory_surveillances_co_assign","170");
		hm.put("factory_surveillances_co_completed","74");
		hm.put("factory_surveillances_osa_assign","351");
		hm.put("factory_surveillances_osa_completed","352");
		hm.put("factory_surveillances_spl_assign","359");
		hm.put("factory_surveillances_spl_completed","360");
		hm.put("cancelled_licenses_total","173");
		hm.put("cancelled_licenses_due","175");
		hm.put("pre_application_tr_rec","1037");
		hm.put("pre_application_tr_dis","1038");
		
		HashMap<String, String> hmap=null;
//		String str="";
		for (Map.Entry<String,String> mapElement : hm.entrySet()) {
            hmap=new HashMap<String, String>();
            hmap.put(mapElement.getKey(), mapElement.getValue());
			lhm.add(hmap);
        }
		lhm.parallelStream().forEach(x->{ 
			HashMap<?, ?> hmTemp=executeMethod(x,selectedBranchId,fromDate,toDate);
			resultList.put(hmTemp.keySet().toArray()[0].toString(),hmTemp.get(hmTemp.keySet().toArray()[0].toString()).toString());
	    });
		 
		System.out.println("\n End Result : 3\n"+resultList);
		return resultList;
	}
	
	public  Map<String,String> getCountforRowFour(String selectedBranchId, String fromDate, String toDate) {
		System.out.println(selectedBranchId+ "getCountforRowFour:: "+fromDate+"  TO "+toDate);
		Map<String,String> resultList=new ConcurrentHashMap<String, String>();
		List<HashMap<String,String>> lhm = new ArrayList<HashMap<String,String>>();
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("lot_insp_assign","357");
		hm.put("lot_insp_completed","358");
		hm.put("sample_sent_Market","353");
		hm.put("sample_sent_factory","356");
		hm.put("spl_sample_sent_Market","361");
		hm.put("spl_sample_sent_factory","361");
		hm.put("market_surveillances_co_assign","1051");
		hm.put("market_surveillances_co_completed","1052");
		hm.put("test_report_awaited","177");
		hm.put("test_report_uploaded","176");
		
		HashMap<String, String> hmap=null;
//		String str="";
		for (Map.Entry<String,String> mapElement : hm.entrySet()) {
            hmap=new HashMap<String, String>();
            hmap.put(mapElement.getKey(), mapElement.getValue());
			lhm.add(hmap);
        }
		lhm.parallelStream().forEach(x->{ 
			HashMap<?, ?> hmTemp=executeMethod(x,selectedBranchId,fromDate,toDate);
			resultList.put(hmTemp.keySet().toArray()[0].toString(),hmTemp.get(hmTemp.keySet().toArray()[0].toString()).toString());
	    });
		 
		System.out.println("\n End Result 4: \n"+resultList);
		return resultList;
	}
	
	public  Map<String,String> getCountforRowFive(String selectedBranchId, String fromDate, String toDate) {
		System.out.println(selectedBranchId+ "getCountforRowFive:: "+fromDate+"  TO "+toDate);
		Map<String,String> resultList=new ConcurrentHashMap<String, String>();
		List<HashMap<String,String>> lhm = new ArrayList<HashMap<String,String>>();
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("articles_received_hallmarking","1055");
		hm.put("articles_received_hallmarked","1054");
		hm.put("rop_under_process","171");
		hm.put("rop_due","171");
		HashMap<String, String> hmap=null;
//		String str="";
		for (Map.Entry<String,String> mapElement : hm.entrySet()) {
            hmap=new HashMap<String, String>();
            hmap.put(mapElement.getKey(), mapElement.getValue());
			lhm.add(hmap);
        }
		lhm.parallelStream().forEach(x->{ 
			HashMap<?, ?> hmTemp=executeMethod(x,selectedBranchId,fromDate,toDate);
			resultList.put(hmTemp.keySet().toArray()[0].toString(),hmTemp.get(hmTemp.keySet().toArray()[0].toString()).toString());
	    });
		 
		System.out.println("\n End Result 5: \n"+resultList);
		return resultList;
	}
	
	public String getAllBranchIds() {
//		List<?> queryresult = new ArrayList();
		String query = "select BM.numBranchId from Branch_Master_Domain BM where IsValid=1";
		String queryresults = "";
		List<Object> queryresult = daoHelper.findByQuery(query);
		for (int j = 0; j < queryresult.size(); j++) {
			queryresults += String.valueOf(queryresult.get(j));
		}
		queryresults = queryresults.replaceAll("..(?!$)", "$0,");
		return queryresults;
	}
	
	public String getBranchIdsbylocationId(String locationId) {
//		List queryresult = new ArrayList();
		String query = "select BM.numBranchId from Branch_Master_Domain BM where BM.numRoId='" + locationId
				+ "' and BM.IsValid = 1";
		String queryresults = "";
		System.out.println("queryresultsqueryresults" + query);
		List<Object> queryresult = daoHelper.findByQuery(query);
		for (int j = 0; j < queryresult.size(); j++) {
			queryresults += String.valueOf(queryresult.get(j));
		}
		queryresults = queryresults.replaceAll("..(?!$)", "$0,");
		return queryresults;
	}

	public List<Map<String, Object>> getBranch(String locationId) {
		String query = "select num_branch_id as bid,str_branc_short_name as name from gblt_branch_mst where num_ro_id in ("
				+ locationId + ") and num_isvalid=1 order by str_branch_detail";
		List<Map<String, Object>> runQry = new ArrayList<Map<String, Object>>();
		runQry = jdbcTemplate.queryForList(query);
		return runQry;
	}

}
