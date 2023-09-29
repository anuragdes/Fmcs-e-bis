package Applicant.DAO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import Global.CommonUtility.DAO.DaoHelper;
import Masters.Domain.Branch_Master_Domain;
import Masters.Domain.Status_mst_domain;
import Masters.Domain.standard_mst_domain;

@Repository
public class ApplicantpcDAO {

	@Autowired
	public DaoHelper daoHelper;
	
	static SimpleDateFormat smdate = new SimpleDateFormat("dd/MM/yyyy");
	static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	static SimpleDateFormat smTime = new SimpleDateFormat("hh:mm a");
	
	public List<standard_mst_domain> getStandards(){
		String qry="SELECT p FROM standard_mst_domain p";
		List<standard_mst_domain> runQry = daoHelper.findByQuery(qry);
		return runQry;
	}
	
	public List<Branch_Master_Domain> getBranch(){
		String qry="SELECT p FROM Branch_Master_Domain p";
		List<Branch_Master_Domain> runQry = daoHelper.findByQuery(qry);
		return runQry;
	}
	
	public List<Status_mst_domain> getApplicationStatus() {
		String qry="from Status_mst_domain ";		
		return daoHelper.findByQuery(qry);
	}
	
	
	public List<HashMap<String,String>> getPCApplications(String fromdate,String todate,String isno,String BranchID,String rev)
	{  
        System.out.println("pcfromdate"+fromdate+"pctodate"+todate+"pcisno"+isno+"pcBranchID"+BranchID+"pcrev"+rev);
		List<Object[]> listApplication = new ArrayList<Object[]>();
	    List<HashMap<String,String>> hmEmpList = new ArrayList<HashMap<String,String>>();
	 
	    Calendar now = Calendar.getInstance();
	    int year=now.get(Calendar.YEAR) - (now.get(Calendar.MONTH) < 4?1:0);
	    String start=year + "-04-01";
	    String end=(year+1) + "-03-31";
		int pcBranchID=Integer.parseInt(BranchID);
		int appstatus=Integer.parseInt(rev);
       
		if(isno.equals("ALLisno") && BranchID.equals("-1") && rev.equals("-2"))
        {	
	    String qry = "SELECT a.lnApplicationId, a.dtRegistration, a.strISno ||':'|| a.numStdYear, b.strFirmName,c.intAppStatus , ";
		qry=qry+"(select y.strStatusName from Status_mst_domain y where y.numStatusId=c.intAppStatus) ";
        qry = qry + " FROM PcGOLDomain a, firmDtlsDomain b, PCApplicationTrackingDomain c, Status_mst_domain d,bisApplicantStatusDomain e WHERE ";
		qry = qry + " a.lnApplicationId=b.lnApplicationId AND a.lnApplicationId=c.lnApplicationId AND c.intAppStatus=d.numStatusId and c.intAppStatus=e.intBisStatus ";
		qry = qry + " AND c.num_id=(select max(num_id) from PCApplicationTrackingDomain m where m.lnApplicationId=c.lnApplicationId and m.intBranchId=a.intBranchId) ";
		qry = qry + " and a.lnApplicationId not in (select ldd.str_app_id  from Licence_Details_Domain ldd , PcGOLDomain pcd where  ldd.str_app_id = pcd.lnApplicationId and ldd.num_branch_id = pcd.intBranchId )";
		qry = qry + " and to_char(c.date,'dd-mm-yyyy') between '"+fromdate+"' and '"+todate+"'  ORDER BY a.dtRegistration desc";
	    listApplication = daoHelper.findByQuery(qry);
        }
		
		else if(isno.equals("ALLisno") && BranchID.equals("-1"))
        {	
	    String qry = "SELECT a.lnApplicationId, a.dtRegistration, a.strISno ||':'|| a.numStdYear, b.strFirmName,c.intAppStatus , ";
		qry=qry+"(select y.strStatusName from Status_mst_domain y where y.numStatusId=c.intAppStatus) ";
        qry = qry + " FROM PcGOLDomain a, firmDtlsDomain b, PCApplicationTrackingDomain c, Status_mst_domain d,bisApplicantStatusDomain e WHERE ";
		qry = qry + " a.lnApplicationId=b.lnApplicationId AND a.lnApplicationId=c.lnApplicationId AND c.intAppStatus=d.numStatusId and c.intAppStatus=e.intBisStatus ";
		qry = qry + " AND c.num_id=(select max(num_id) from PCApplicationTrackingDomain m where m.lnApplicationId=c.lnApplicationId and m.intBranchId=a.intBranchId) ";
		qry = qry + " and a.lnApplicationId not in (select ldd.str_app_id  from Licence_Details_Domain ldd , PcGOLDomain pcd where  ldd.str_app_id = pcd.lnApplicationId and ldd.num_branch_id = pcd.intBranchId )";
		qry = qry + " and to_char(c.date,'dd-mm-yyyy') between '"+fromdate+"' and '"+todate+"' and d.numStatusId in ('"+appstatus+"') ORDER BY a.dtRegistration desc";
	    listApplication = daoHelper.findByQuery(qry);
        }
		
		else if(isno.equals("ALLisno") && rev.equals("-2"))
        {	
	    String qry = "SELECT a.lnApplicationId, a.dtRegistration, a.strISno ||':'|| a.numStdYear, b.strFirmName,c.intAppStatus , ";
		qry=qry+"(select y.strStatusName from Status_mst_domain y where y.numStatusId=c.intAppStatus) ";
        qry = qry + " FROM PcGOLDomain a, firmDtlsDomain b, PCApplicationTrackingDomain c, Status_mst_domain d,bisApplicantStatusDomain e WHERE ";
		qry = qry + " a.lnApplicationId=b.lnApplicationId AND a.lnApplicationId=c.lnApplicationId AND c.intAppStatus=d.numStatusId and c.intAppStatus=e.intBisStatus ";
		qry = qry + " AND c.num_id=(select max(num_id) from PCApplicationTrackingDomain m where m.lnApplicationId=c.lnApplicationId and m.intBranchId=a.intBranchId) ";
		qry = qry + " and a.lnApplicationId not in (select ldd.str_app_id  from Licence_Details_Domain ldd , PcGOLDomain pcd where  ldd.str_app_id = pcd.lnApplicationId and ldd.num_branch_id = pcd.intBranchId )";
		qry = qry + " and to_char(c.date,'dd-mm-yyyy') between '"+fromdate+"' and '"+todate+"' and  a.intBranchId in ('"+pcBranchID+"') ORDER BY a.dtRegistration desc";
	    listApplication = daoHelper.findByQuery(qry);
        }
		
		else if(BranchID.equals("-1") && rev.equals("-2"))
        {	
	    String qry = "SELECT a.lnApplicationId, a.dtRegistration, a.strISno ||':'|| a.numStdYear, b.strFirmName,c.intAppStatus , ";
		qry=qry+"(select y.strStatusName from Status_mst_domain y where y.numStatusId=c.intAppStatus) ";
        qry = qry + " FROM PcGOLDomain a, firmDtlsDomain b, PCApplicationTrackingDomain c, Status_mst_domain d,bisApplicantStatusDomain e WHERE ";
		qry = qry + " a.lnApplicationId=b.lnApplicationId AND a.lnApplicationId=c.lnApplicationId AND c.intAppStatus=d.numStatusId and c.intAppStatus=e.intBisStatus ";
		qry = qry + " AND c.num_id=(select max(num_id) from PCApplicationTrackingDomain m where m.lnApplicationId=c.lnApplicationId and m.intBranchId=a.intBranchId) ";
		qry = qry + " and a.lnApplicationId not in (select ldd.str_app_id  from Licence_Details_Domain ldd , PcGOLDomain pcd where  ldd.str_app_id = pcd.lnApplicationId and ldd.num_branch_id = pcd.intBranchId )";
		qry = qry + " and to_char(c.date,'dd-mm-yyyy') between '"+fromdate+"' and '"+todate+"' and  a.strISno in ('"+isno+"') ORDER BY a.dtRegistration desc";
	    listApplication = daoHelper.findByQuery(qry);
        }
		
		else if(isno.equals("ALLisno"))
        {	
	    String qry = "SELECT a.lnApplicationId, a.dtRegistration, a.strISno ||':'|| a.numStdYear, b.strFirmName,c.intAppStatus , ";
		qry=qry+"(select y.strStatusName from Status_mst_domain y where y.numStatusId=c.intAppStatus) ";
        qry = qry + " FROM PcGOLDomain a, firmDtlsDomain b, PCApplicationTrackingDomain c, Status_mst_domain d,bisApplicantStatusDomain e WHERE ";
		qry = qry + " a.lnApplicationId=b.lnApplicationId AND a.lnApplicationId=c.lnApplicationId AND c.intAppStatus=d.numStatusId and c.intAppStatus=e.intBisStatus ";
		qry = qry + " AND c.num_id=(select max(num_id) from PCApplicationTrackingDomain m where m.lnApplicationId=c.lnApplicationId and m.intBranchId=a.intBranchId) ";
		qry = qry + " and a.lnApplicationId not in (select ldd.str_app_id  from Licence_Details_Domain ldd , PcGOLDomain pcd where  ldd.str_app_id = pcd.lnApplicationId and ldd.num_branch_id = pcd.intBranchId )";
		qry = qry + " and  a.intBranchId in ('"+pcBranchID+"') and to_char(c.date,'dd-mm-yyyy') between '"+fromdate+"' and '"+todate+"' and d.numStatusId in ('"+appstatus+"') ORDER BY a.dtRegistration desc";
		listApplication = daoHelper.findByQuery(qry);
		
        }
		
		else if(BranchID.equals("-1"))
        {	
	    String qry = "SELECT a.lnApplicationId, a.dtRegistration, a.strISno ||':'|| a.numStdYear, b.strFirmName,c.intAppStatus , ";
		qry=qry+"(select y.strStatusName from Status_mst_domain y where y.numStatusId=c.intAppStatus) ";
        qry = qry + " FROM PcGOLDomain a, firmDtlsDomain b, PCApplicationTrackingDomain c, Status_mst_domain d,bisApplicantStatusDomain e WHERE ";
		qry = qry + " a.lnApplicationId=b.lnApplicationId AND a.lnApplicationId=c.lnApplicationId AND c.intAppStatus=d.numStatusId and c.intAppStatus=e.intBisStatus ";
		qry = qry + " AND c.num_id=(select max(num_id) from PCApplicationTrackingDomain m where m.lnApplicationId=c.lnApplicationId and m.intBranchId=a.intBranchId) ";
		qry = qry + " and a.lnApplicationId not in (select ldd.str_app_id  from Licence_Details_Domain ldd , PcGOLDomain pcd where  ldd.str_app_id = pcd.lnApplicationId and ldd.num_branch_id = pcd.intBranchId )";
		qry = qry + " and  a.strISno in ('"+isno+"') and to_char(c.date,'dd-mm-yyyy') between '"+fromdate+"' and '"+todate+"' and d.numStatusId in ('"+appstatus+"') ORDER BY a.dtRegistration desc";
		listApplication = daoHelper.findByQuery(qry);
		
        }
		
		else if(rev.equals("-2"))
        {	
	    String qry = "SELECT a.lnApplicationId, a.dtRegistration, a.strISno ||':'|| a.numStdYear, b.strFirmName,c.intAppStatus , ";
		qry=qry+"(select y.strStatusName from Status_mst_domain y where y.numStatusId=c.intAppStatus) ";
        qry = qry + " FROM PcGOLDomain a, firmDtlsDomain b, PCApplicationTrackingDomain c, Status_mst_domain d,bisApplicantStatusDomain e WHERE ";
		qry = qry + " a.lnApplicationId=b.lnApplicationId AND a.lnApplicationId=c.lnApplicationId AND c.intAppStatus=d.numStatusId and c.intAppStatus=e.intBisStatus ";
		qry = qry + " AND c.num_id=(select max(num_id) from PCApplicationTrackingDomain m where m.lnApplicationId=c.lnApplicationId and m.intBranchId=a.intBranchId) ";
		qry = qry + " and a.lnApplicationId not in (select ldd.str_app_id  from Licence_Details_Domain ldd , PcGOLDomain pcd where  ldd.str_app_id = pcd.lnApplicationId and ldd.num_branch_id = pcd.intBranchId )";
		qry = qry + " and  a.strISno in ('"+isno+"') and  a.intBranchId in ('"+pcBranchID+"') and to_char(c.date,'dd-mm-yyyy') between '"+fromdate+"' and '"+todate+"'  ORDER BY a.dtRegistration desc";
		listApplication = daoHelper.findByQuery(qry);
		
        }
		
		else 
        {	
	    String qry = "SELECT a.lnApplicationId, a.dtRegistration, a.strISno ||':'|| a.numStdYear, b.strFirmName,c.intAppStatus , ";
		qry=qry+"(select y.strStatusName from Status_mst_domain y where y.numStatusId=c.intAppStatus) ";
        qry = qry + " FROM PcGOLDomain a, firmDtlsDomain b, PCApplicationTrackingDomain c, Status_mst_domain d,bisApplicantStatusDomain e WHERE ";
		qry = qry + " a.lnApplicationId=b.lnApplicationId AND a.lnApplicationId=c.lnApplicationId AND c.intAppStatus=d.numStatusId and c.intAppStatus=e.intBisStatus ";
		qry = qry + " AND c.num_id=(select max(num_id) from PCApplicationTrackingDomain m where m.lnApplicationId=c.lnApplicationId and m.intBranchId=a.intBranchId) ";
		qry = qry + " and a.lnApplicationId not in (select ldd.str_app_id  from Licence_Details_Domain ldd , PcGOLDomain pcd where  ldd.str_app_id = pcd.lnApplicationId and ldd.num_branch_id = pcd.intBranchId )";
		qry = qry + " and  a.strISno in ('"+isno+"') and  a.intBranchId in ('"+pcBranchID+"') and to_char(c.date,'dd-mm-yyyy') between '"+fromdate+"' and '"+todate+"' and d.numStatusId in ('"+appstatus+"') ORDER BY a.dtRegistration desc";
		listApplication = daoHelper.findByQuery(qry);
		
        }
 
		if(listApplication != null && listApplication.size() > 0)
		{
			for(int i=0;i<listApplication.size();i++)
			{  
				//Object [] objList = (Object []) listApplication.get(i);
				Object [] objList = listApplication.get(i);
				String status="";
				status=objList[5].toString();
				objList = listApplication.get(i);
				HashMap<String,String> hmlist = new HashMap<String,String>();
				//System.out.println("action id:"+objList[0].toString()+" next status:"+objList[1].toString()+" action name:"+objList[2].toString());
				hmlist.put("appId",objList[0].toString());
				hmlist.put("FirmName",objList[3].toString());
				
				String dt1="";
				String dt = objList[1].toString();
				
				try {
					dt1 = smdate.format(formatter.parse(dt));
				} 
				catch (ParseException e) {
					e.printStackTrace();
				}
				hmlist.put("submittedDate", ""+dt1);
				hmlist.put("ISNo",objList[2].toString());
				hmlist.put("WStatus",status);
				hmEmpList.add(i,hmlist);
			}
		}
		
		
		
		return hmEmpList;
		
		
		
	}
	
	
	
	
}
