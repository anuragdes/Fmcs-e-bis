package Applicant.Service;





import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Applicant.DAO.DGdashboardDAO;
import Global.CommonUtility.ResourceBundleFile;


@Service
public class DGdashboardserviceImpl implements DGdashboardservice{
	
	@Autowired
	DGdashboardDAO DGdao;
	
public HashMap<String,Integer> getDGdashborddata(String fromDate,String toDate){
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy"); 
	     String Date = formatter.format(new Date());
	      
		  String filePath= ResourceBundleFile.getValueFromKey("query_CountforDG");
		  String fileName = "DGdashborddata"+".txt";
		  System.out.println("file name is "+fileName);
		  String finalPath=filePath+File.separator+fileName;
		  
		  System.out.println(finalPath);
		  
        File queryCountFile = new File(finalPath);
        //queryCountFile.delete();
		System.out.println("file exists "+queryCountFile.exists());
		HashMap<String,Integer> hmePgeData1= new HashMap<String,Integer>();
		if(queryCountFile.exists())
		{
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(queryCountFile));
				String textoffirstline= bufferedReader.readLine();
				if(textoffirstline.equals(Date)||textoffirstline.contains(Date))
				{// date match hu to file se data utayenge
					//System.out.println("Date is matched "+textoffirstline);
					String[] keyNames = new String[]{"LicenseCount","PendingAppCount","InclusionPending","PendingPICount","surviellanceCount","PendingLotInspection","PendingCMLROPCOUNT","RenewalAppPendingatbranch","RenewalAppPendingatlicencee","PendingAppTestReport","PendingCMLTestReport"}; 
					/*while(textoffirstline != null)*/ 
					for(int i=0;i<=15;i++)
					{
				       
				        textoffirstline = bufferedReader.readLine();
				       // System.out.println("text is"+textoffirstline);
				        if(textoffirstline!=null && !textoffirstline.equals("")){
				           hmePgeData1.put(keyNames[i], Integer.valueOf(textoffirstline));
				         //  System.out.println(hmePgeData1.get(keyNames[i]));
				        }
				        else
				        {
				        	 hmePgeData1.put(keyNames[i], 0);
				        }
				    }
					
					
				}
				else
				{// data not matched -write again
					//System.out.println("Date is not matching");
					queryCountFile.delete();// delete old file when date is not exact of current date
					queryCountFile.createNewFile();//create new file to write data
					//hmePgeData1=getDGdashborddata1( fromDate, toDate);
					BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(queryCountFile));
					 bufferedWriter.write(Date);
					 bufferedWriter.newLine();
					    bufferedWriter.write(hmePgeData1.get("LicenseCount").toString());
					 
					    bufferedWriter.newLine();
					    bufferedWriter.write(hmePgeData1.get("PendingAppCount").toString());
					    bufferedWriter.newLine();
					    bufferedWriter.write(hmePgeData1.get("InclusionPending").toString());
					    bufferedWriter.newLine();
					    bufferedWriter.write(hmePgeData1.get("PendingPICount").toString());
					    bufferedWriter.newLine();
					    bufferedWriter.write(hmePgeData1.get("surviellanceCount").toString());
					    bufferedWriter.newLine();
					    bufferedWriter.write(hmePgeData1.get("PendingLotInspection").toString());
					    bufferedWriter.newLine();
					    bufferedWriter.write(hmePgeData1.get("PendingCMLROPCOUNT").toString());
					    bufferedWriter.newLine();
					    bufferedWriter.write(hmePgeData1.get("RenewalAppPendingatbranch").toString());   
					    bufferedWriter.newLine();
					    bufferedWriter.write(hmePgeData1.get("RenewalAppPendingatlicencee").toString());   
					    bufferedWriter.newLine();
					    bufferedWriter.write(hmePgeData1.get("PendingAppTestReport").toString());
					    bufferedWriter.newLine();
					    bufferedWriter.write(hmePgeData1.get("PendingCMLTestReport").toString());
					   
					    bufferedWriter.close();
				    
				}
				bufferedReader.close();
			} catch (Exception e) {
				//  Auto-generated catch block
				e.printStackTrace();
			} 
		}
		else
		{
			System.out.println("file doesnt exist");
			try {
				//file nahi mila to file create karenge aur data write karenge
				queryCountFile.createNewFile();
				//hmePgeData1=getDGdashborddata1(fromDate, toDate);
			    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(queryCountFile));
			 
			    bufferedWriter.write(Date);
			    
			    bufferedWriter.newLine();
			    bufferedWriter.write(hmePgeData1.get("LicenseCount").toString());
			    bufferedWriter.newLine();
			    bufferedWriter.write(hmePgeData1.get("PendingAppCount").toString());
			    bufferedWriter.newLine();
			    bufferedWriter.write(hmePgeData1.get("InclusionPending").toString());
			    bufferedWriter.newLine();
			    bufferedWriter.write(hmePgeData1.get("PendingPICount").toString());
			    bufferedWriter.newLine();
			    bufferedWriter.write(hmePgeData1.get("surviellanceCount").toString());
			    bufferedWriter.newLine();
			    bufferedWriter.write(hmePgeData1.get("PendingLotInspection").toString());
			    bufferedWriter.newLine();
			    bufferedWriter.write(hmePgeData1.get("PendingCMLROPCOUNT").toString());
			    bufferedWriter.newLine();
			    bufferedWriter.write(hmePgeData1.get("RenewalAppPendingatbranch").toString());   
			    bufferedWriter.newLine();
			    bufferedWriter.write(hmePgeData1.get("RenewalAppPendingatlicencee").toString());   
			    bufferedWriter.newLine();
			    bufferedWriter.write(hmePgeData1.get("PendingAppTestReport").toString());
			    bufferedWriter.newLine();
			    bufferedWriter.write(hmePgeData1.get("PendingCMLTestReport").toString());
			    
			   
			    bufferedWriter.close();
				
			} catch (IOException e) {
				//  Auto-generated catch block
				e.printStackTrace();
			}
		}
		return hmePgeData1;
		
	}

/*public HashMap<String,Integer> getDGdashborddata1(String fromDate,String toDate)
{
	HashMap<String,Integer> data = new HashMap<String,Integer>();
	//System.out.println((int)(ldao.getactiveappsCount()));
	//data.put("LicenseCount", (int)(DGdao.gettotalnooflicences1()));
	//data.put("grantCount", (int)(DGdao.gettotalnooflicencesGrant()));
	//data.put("CancelLicCount", (int)(DGdao.gettotalnooflicencesCancel()));
	data.put("LicenseCount", (int)(DGdao.gettotalnooflicences(fromDate, toDate)));
	data.put("PendingAppCount", (int)(DGdao.getpendingapplication()));
	data.put("InclusionPending", (int)(DGdao.getInclsuionPendingCount()));
	data.put("PendingPICount", (int)(DGdao.getpendingAppPI()));
	data.put("surviellanceCount", (int)(DGdao.getsurveillancePendingCount()));
	data.put("PendingLotInspection", (int)(DGdao.getPendingLOTInsp()));
	data.put("PendingCMLROPCOUNT", (int)(DGdao.getPendingCMLROP()));
	data.put("RenewalAppPendingatbranch", (int)(DGdao.getRenewalAppPendingatbranch()));
	data.put("RenewalAppPendingatlicencee", (int)(DGdao.getRenewalAppNotSubmitted()));
	data.put("PendingAppTestReport", (int)(DGdao.getpendingapptestreport()));
	data.put("PendingCMLTestReport", (int)(DGdao.getpendingCMLtestreport()));
	
	data.put("pendingRenBranch", (int)(DGdao.getPendingrenewalBranch()));
	data.put("pendingTestRptApp", (int)(DGdao.getPendingTestReportapp()));
	data.put("pendingTestRptLic", (int)(DGdao.getPendingTestReportlic()));
	System.out.println("RenewalAppPendingatlicenceehihihi"  +data);
	
	
	
	

	return data;
}*/


@Override
public List<HashMap<String, String>> getMonthwiseApp() {
	System.out.println("month wise");
	

	
	List sampleLog = new ArrayList();
	sampleLog=DGdao.getMonthwiseApp();
	


	return sampleLog;
}

@Override
public List<HashMap<String, String>> getMonthwiseCML() {
	System.out.println("month wise");
	
	
	List listdatacml = new ArrayList();
	listdatacml=DGdao.getMonthwiseCML();
	
	
	return listdatacml;
}

@Override
public ArrayList getlicenceISwise() {
	System.out.println("in impl");
	ArrayList licenceISwise = new ArrayList();
	licenceISwise=DGdao.mazorIScml();
	System.out.println("in impl2");
	return licenceISwise;
}

@Override
public ArrayList getmonthwiseapp() {
	System.out.println("in impl");
	ArrayList monthwiseapp = new ArrayList();
	monthwiseapp=DGdao.monthwiseapp();
		return monthwiseapp;
}

@Override
public ArrayList getapplicationMonthwise() {
	System.out.println("in impl");
	ArrayList applicationMonthwise = new ArrayList();
	applicationMonthwise=DGdao.applicationMonthwise();
	System.out.println("in impl2");
	return applicationMonthwise;
}

@Override
public ArrayList getapplicationYearwise() {
	System.out.println("in impl");
	ArrayList applicationYearwise = new ArrayList();
	applicationYearwise=DGdao.applicationYearwise();
	System.out.println("in impl2");
	return applicationYearwise;
}

@Override
public ArrayList getravenueyearwise() {
	System.out.println("in impl");
	ArrayList revenueyearwise = new ArrayList();
	revenueyearwise=DGdao.revenueyearwise();
	System.out.println("in impl2");
	return revenueyearwise;
}
public List<HashMap<String, String>> getBranch(int locationId) {
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

public List<HashMap<String, String>> getBranch1(String locationId) {
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

public String getHeaderReportListQuery(String value){
	
	String query=DGdao.getHeaderReportListQueryId(value);
	return query;
	
}

@Override
public List<Map<String, Object>> getHearderListQueryResult(String ListQuery,String loc_id, String to_date,String FYFrom)
{
	List<Map<String,Object>> bisReportQueryListModel=new ArrayList<Map<String,Object>>();
	
	try{
		bisReportQueryListModel=DGdao.getAllHeaderReportQueryListDtl(ListQuery,loc_id,to_date,FYFrom);
        }
	catch (Exception e) {
		e.printStackTrace();
		}
	return bisReportQueryListModel;
}
@Override
public List<HashMap> getEfficiencyIndex(int userid, int roleId,String branch,String month1, String year1) {
	
	List<HashMap> listhmRcvdApplication = new ArrayList<HashMap>();
	try{
		listhmRcvdApplication=DGdao.getEfficiencyIndex(userid,roleId,branch,month1,year1);
		
	}
	catch(Exception e){
		e.printStackTrace();
	}
	return listhmRcvdApplication;
}

@Override
public List<HashMap> getEfficiencyIndexEmp(int userid, int roleId,String branch,String month1, String year1) {
	
	List<HashMap> listhmRcvdApplication = new ArrayList<HashMap>();
	try{
		listhmRcvdApplication=DGdao.getEfficiencyIndexEmp(userid,roleId,branch,month1,year1);
		
	}
	catch(Exception e){
		e.printStackTrace();
	}
	return listhmRcvdApplication;
}
@Override
public List<HashMap> getEfficiencyIndexEmpTwo(int userid, int roleId,String branch,String month1, String year1) {
	
	List<HashMap> listhmRcvdApplication = new ArrayList<HashMap>();
	try{
		listhmRcvdApplication=DGdao.getEfficiencyIndexEmpTwo(userid,roleId,branch,month1,year1);
		
	}
	catch(Exception e){
		e.printStackTrace();
	}
	return listhmRcvdApplication;
}
//Author priya
@Override
public List<HashMap<String, String>> getAhcDtls(int regionID) {
	List<HashMap<String, String>> ahc = new ArrayList<HashMap<String, String>>();
	List<Map<String,Object>> ahcList= DGdao.getahc(regionID);
	if(ahcList.size()!=0){
		for (Map<String, Object> tempRow : ahcList) {
			HashMap<String,String> brh = new HashMap<String,String>();
			brh.put("ahc",tempRow.get("name")+"");
			brh.put("ahc_id",tempRow.get("bid")+"");
			ahc .add(brh);
		}
	}
	
	return ahc;
}

@Override
public List<HashMap<String, String>> getBranchDtlsbyahc(int ahcId) {
	List<HashMap<String, String>> branch = new ArrayList<HashMap<String, String>>();
	List<Map<String,Object>> ahcList= DGdao.getBranchDtlsbyahc(ahcId);
	if(ahcList.size()!=0){
		for (Map<String, Object> tempRow : ahcList) {
			HashMap<String,String> brh = new HashMap<String,String>();
			brh.put("branch",tempRow.get("name")+"");
			brh.put("branch_id",tempRow.get("bid")+"");
			branch .add(brh);
		}
	}
	
	return branch;
}




/*
 * @Override public String getBisReportListQueryDash(String value) {
 * 
 * 
 * String query=DGdao.getBisReportListQuerybyIdforDash(value); return query;
 * 
 * 
 * }
 * 
 * @Override public String getStageNameDash(String value) {
 * 
 * String allBranchIds=DGdao.getStageNamebyValueDash(value); return
 * allBranchIds;
 * 
 * }
 */

public String getStageNameDgreports(String value) {
	
	String allBranchIds=DGdao.getStageNamebyValueDgreports(value);
	return allBranchIds;
	
}

@Override
public String getBisReportListQueryDGreports(String value) {
	String query=DGdao.getBisReportListQuerybyIdforDGreports(value);
	return query;
}

@Override
public String getStageNameDgreportsAIF(String parentId, String currntid) {
	String stage=DGdao.getStageNamebyValueDgreportsAIF(parentId,currntid);
	return stage;
}

@Override
public String getBisReportListQueryDGreportsAIF(String parentId, String currntid) {
	String query=DGdao.getBisReportListQuerybyIdforDGreportsAIF(parentId,currntid);
	return query;
}

@Override
public List<Map<String, Object>> getBisListQueryResultAIF(String listQuery, String from_date, String branchid,
		String to_date, String locationId) {
	List<Map<String,Object>> bisReportQueryListModel=new ArrayList<Map<String,Object>>();
	
	try{
		bisReportQueryListModel=DGdao.getAllBisReportQueryListDtlAIF(listQuery,from_date,branchid,to_date);
        }
	catch (Exception e) {
		e.printStackTrace();
		}
	return bisReportQueryListModel;
}

@Override
public List<Map<String, Object>> getdataCon(String listQuery, String cmlno, String branchId) {
	
List<Map<String,Object>> bisReportQueryListModel=new ArrayList<Map<String,Object>>();
	
	try{
		bisReportQueryListModel=DGdao.getAllBisReportCons(listQuery,cmlno,branchId);
        }
	catch (Exception e) {
		e.printStackTrace();
		}
	return bisReportQueryListModel;
}







}







