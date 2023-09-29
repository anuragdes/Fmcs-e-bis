package Applicant.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import Schemes.ProductCertification.ApplicationSubmission.Domain.Licence_Details_Domain;

public interface DGdashboardservice {
	
	
	
	@Transactional(propagation=Propagation.REQUIRED)
	public HashMap<String,Integer> getDGdashborddata(String fromDate,String toDate);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public List getMonthwiseApp();

	@Transactional(propagation=Propagation.REQUIRED)
	public List getMonthwiseCML();
	
	@Transactional(propagation=Propagation.REQUIRED)
     public ArrayList getlicenceISwise();
	
	@Transactional(propagation=Propagation.REQUIRED)
    public ArrayList getapplicationMonthwise();
	
	@Transactional(propagation=Propagation.REQUIRED)
    public ArrayList getapplicationYearwise();
	
	@Transactional(propagation=Propagation.REQUIRED)
    public ArrayList getravenueyearwise();
	
	@Transactional(propagation=Propagation.REQUIRED)
    public ArrayList getmonthwiseapp();
	
	@Transactional(propagation=Propagation.REQUIRED)
	public List<HashMap<String,String>> getBranch(int locationId);
	

	@Transactional(propagation=Propagation.REQUIRED)
	public String getHeaderReportListQuery(String value);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Map<String,Object>> getHearderListQueryResult(String ListQuery,String loc_id,String to_date ,String FYFrom);

	@Transactional(propagation=Propagation.REQUIRED)
	public List<HashMap> getEfficiencyIndex(int userid, int roleId, String branch, String month1, String year1);

	public List<HashMap> getEfficiencyIndexEmp(int userid, int roleId,
			String branch, String month1, String year1);
	@Transactional(propagation=Propagation.REQUIRED)

	public List<HashMap<String, String>> getAhcDtls(int regionID);
	@Transactional(propagation=Propagation.REQUIRED)

	public List<HashMap<String, String>> getBranchDtlsbyahc(int ahcId);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public List<HashMap<String, String>> getBranch1(String regionID);
	
	List<HashMap> getEfficiencyIndexEmpTwo(int userid, int roleId, String branch, String month1, String year1);

	
	/*
	 * @Transactional(propagation=Propagation.REQUIRED) public String
	 * getBisReportListQueryDash(String value);
	 * 
	 * @Transactional(propagation=Propagation.REQUIRED) public String
	 * getStageNameDash(String value);
	 */
	

	@Transactional(propagation=Propagation.REQUIRED)
	String getStageNameDgreports(String value);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public String getBisReportListQueryDGreports(String value);

	@Transactional(propagation=Propagation.REQUIRED)
	public String getStageNameDgreportsAIF(String parentId, String currntid);

	@Transactional(propagation=Propagation.REQUIRED)
	public String getBisReportListQueryDGreportsAIF(String parentId, String currntid);

	@Transactional(propagation=Propagation.REQUIRED)
	public List<Map<String, Object>> getBisListQueryResultAIF(String listQuery, String from_date, String branchid,
			String to_date, String locationId);

	
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Map<String, Object>> getdataCon(String listQuery, String cmlno, String branchId);
	
}
