package Global.Dashboard.DAO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import Global.CommonUtility.DAO.DaoHelper;
import Masters.Domain.Branch_Master_Domain;

@Repository
public class DGDashboardRevenueDao {

	@Autowired
	DaoHelper daoHelper;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	DGDashboardPCDao ddpd;
	
	public List<HashMap<String,String>> getPaidApplications(int iRoleId,int iRegionId,int iBranchId,String fromDate,String toDate,String status){
		List<HashMap<String,String>> lhm = new ArrayList<HashMap<String,String>>();
		
		String stBranchIds = ddpd.getDynamicBranchIds(iRoleId,iRegionId,iBranchId);
		
		String qry="SELECT 'New Application' as name, "+
				   "(SELECT CAST(sum(a.amount) AS DECIMAL(20,2))) as amount "+
				   "FROM gblt_payment_gateway_dtls a where "+ 
				   "a.branch_id IN ("+stBranchIds+") and  a.payment_status='"+status+"' and "+ 
				   "a.payment_url like '%|1|1|%' and " +
				   "to_date(substr(a.tr_date,1,10) ,'DD/MM/YYYY HH24:MI:SS')>=to_date('"+toDate+"','DD/MM/YYYY') AND to_date(substr(a.tr_date,1,10) ,'DD/MM/YYYY HH24:MI:SS')<=to_date('"+fromDate+"','DD/MM/YYYY') ";
		
		List<Map<String,Object>> runQry = jdbcTemplate.queryForList(qry);
		if(runQry.size()>0){
			for(int i=0;i<runQry.size();i++){
				HashMap<String,String> hm = new HashMap<String,String>();
				Map<String,Object> temp = runQry.get(i);
				hm.put("name", temp.get("name")+"");
				hm.put("amount", temp.get("amount")+"");
				lhm.add(hm);
			}
		}

		
		return lhm;
	}
		
	public List<HashMap<String,String>> getPaidRenewal(int iRoleId,int iRegionId,int iBranchId,String fromDate,String toDate,String status){
		List<HashMap<String,String>> lhm = new ArrayList<HashMap<String,String>>();
		
		String stBranchIds = ddpd.getDynamicBranchIds(iRoleId,iRegionId,iBranchId);
		
		String qry="SELECT 'New Application' as name, "+
				   "(SELECT CAST(sum(a.amount) AS DECIMAL(20,2))) as amount "+
				   "FROM gblt_payment_gateway_dtls a where "+ 
				   "a.branch_id IN ("+stBranchIds+") and  a.payment_status='"+status+"' and "+ 
				   "a.payment_url like '%|1|2|%' and " +
				   "to_date(substr(a.tr_date,1,10) ,'DD/MM/YYYY HH24:MI:SS')>=to_date('"+toDate+"','DD/MM/YYYY') AND to_date(substr(a.tr_date,1,10) ,'DD/MM/YYYY HH24:MI:SS')<=to_date('"+fromDate+"','DD/MM/YYYY') ";
		
		List<Map<String,Object>> runQry = jdbcTemplate.queryForList(qry);
		if(runQry.size()>0){
			for(int i=0;i<runQry.size();i++){
				HashMap<String,String> hm = new HashMap<String,String>();
				Map<String,Object> temp = runQry.get(i);
				hm.put("name", temp.get("name")+"");
				hm.put("amount", temp.get("amount")+"");
				lhm.add(hm);
			}
		}

		
		return lhm;
	}
		
	public List<HashMap<String,String>> getPaidInclusion(int iRoleId,int iRegionId,int iBranchId,String fromDate,String toDate,String status){
		List<HashMap<String,String>> lhm = new ArrayList<HashMap<String,String>>();
		
		String stBranchIds = ddpd.getDynamicBranchIds(iRoleId,iRegionId,iBranchId);
		
		String qry="SELECT 'New Application' as name, "+
				   "(SELECT CAST(sum(a.amount) AS DECIMAL(20,2))) as amount "+
				   "FROM gblt_payment_gateway_dtls a where "+ 
				   "a.branch_id IN ("+stBranchIds+") and  a.payment_status='"+status+"' and "+ 
				   "a.payment_url like '%|1|2|%' and " +
				   "to_date(substr(a.tr_date,1,10) ,'DD/MM/YYYY HH24:MI:SS')>=to_date('"+toDate+"','DD/MM/YYYY') AND to_date(substr(a.tr_date,1,10) ,'DD/MM/YYYY HH24:MI:SS')<=to_date('"+fromDate+"','DD/MM/YYYY') ";
		
		List<Map<String,Object>> runQry = jdbcTemplate.queryForList(qry);
		if(runQry.size()>0){
			for(int i=0;i<runQry.size();i++){
				HashMap<String,String> hm = new HashMap<String,String>();
				Map<String,Object> temp = runQry.get(i);
				hm.put("name", temp.get("name")+"");
				hm.put("amount", temp.get("amount")+"");
				lhm.add(hm);
			}
		}

		
		return lhm;
	}

	public List<HashMap<String,String>> getPaidOther(int iRoleId,int iRegionId,int iBranchId, int feeid,String fromDate,String toDate){
		List<HashMap<String,String>> lhm = new ArrayList<HashMap<String,String>>();
		String stBranchIds ="";
		try{
			String strDateFormat = "dd-MMM-yyyy"; //Date format is Specified
			  SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat);
			if(fromDate.equals("0")){
				fromDate="14-May-2017";				
			}
			if(toDate.equals("0")){				
				toDate=objSDF.format(new Date())+"";
			}
			
			
		try{
		stBranchIds = ddpd.getDynamicBranchIds(iRoleId,iRegionId,iBranchId);
		}catch(Exception e){
			e.printStackTrace();
		}			
		
		String qry ="SELECT  (SELECT c.str_fee_desc FROM pc_fee_mst c where c.num_fee_id=b.num_fee_id) name1, " +
				"	(case "+
		"when length(round( Sum(num_amount) ))<4 then 'ACTUAL_'||round( Sum(num_amount) ) "+
		"when length(round( Sum(num_amount) ))>=4 and length(round( Sum(num_amount) )) <6 then 'IN HUNDREDS_'||round( Sum(num_amount) )/100 "+
		"when length(round( Sum(num_amount) ))>=6 and length(round( Sum(num_amount) )) <=7 then 'IN THOUSANDS_'||round( Sum(num_amount) )/1000 "+
		"when length(round( Sum(num_amount) ))>=8 and length(round( Sum(num_amount) )) <9 then 'IN TEN THOUSANDS_'||round( Sum(num_amount) )/10000 "+
		"when length(round( Sum(num_amount) ))>=9 and length(round( Sum(num_amount) )) <10 then 'IN LACS_'||round( Sum(num_amount) )/100000 "+
		"when length(round( Sum(num_amount) ))>=10 and length(round( Sum(num_amount) )) <12 then 'IN TEN LACS_'||round( Sum(num_amount) )/1000000 "+
		"else 'IN TEN CRORES_'||round( Sum(num_amount) )/100000000 "+
		"   end) amount" +
					" from bis_dev.gblt_payment_gateway_dtls a,bis_dev.pc_fee_paid_detail b "+
					"where a.transaction_number=b.transaction_number " +
					"and b.num_payment_status_id=1 " +
					"and  payment_status='0300' ";
					if(stBranchIds.length()>1 && stBranchIds!=null){
						qry+="and a.branch_id in ("+stBranchIds+")";
					}
					if(!fromDate.equals("0") && !toDate.equals("0")){
						qry+="and to_date(tr_date) between '"+fromDate+"' and '"+toDate+"'  ";
					}
					qry+="group by (SELECT c.str_fee_desc FROM pc_fee_mst c where c.num_fee_id=b.num_fee_id)  "+
					"order by length(round( Sum(num_amount) )) ASC "; 
									
		List<Map<String,Object>> runQry = jdbcTemplate.queryForList(qry);
		if(runQry.size()>0){
			for(int i=0;i<runQry.size();i++){
				HashMap<String,String> hm = new HashMap<String,String>();
				Map<String,Object> temp = runQry.get(i);
				String t=String.valueOf(temp.get("amount"));
				String tem[]=t.split("_");
				hm.put("name", temp.get("name1")+"");
				hm.put("amountIn", tem[0]);
				hm.put("amount", tem[1]);
				lhm.add(hm);
			}
		}
		
		}catch(Exception e1){
			e1.printStackTrace();
		}
		
		return lhm;
	}
	
	public List<HashMap<String,String>> getPaymentDetail_Status(int iRoleId,int iRegionId,int iBranchId,String fromDate,String toDate,String status){
		List<HashMap<String,String>> lhm = new ArrayList<HashMap<String,String>>();
		
		String stBranchIds = ddpd.getDynamicBranchIds(iRoleId,iRegionId,iBranchId);
		
		String qry="SELECT 'Refund' as name, "+
				   "(SELECT CAST(sum(a.amount) AS DECIMAL(20,2))) as amount "+
				   "FROM gblt_payment_gateway_dtls a where "+ 
				   "a.branch_id IN ("+stBranchIds+") and  a.payment_status='"+status+"' and "+ 
				   "to_date(substr(a.tr_date,1,10) ,'DD/MM/YYYY HH24:MI:SS')>=to_date('"+toDate+"','DD/MM/YYYY') AND to_date(substr(a.tr_date,1,10) ,'DD/MM/YYYY HH24:MI:SS')<=to_date('"+fromDate+"','DD/MM/YYYY') ";
		
		List<Map<String,Object>> runQry = jdbcTemplate.queryForList(qry);
		if(runQry.size()>0){
			for(int i=0;i<runQry.size();i++){
				HashMap<String,String> hm = new HashMap<String,String>();
				Map<String,Object> temp = runQry.get(i);
				hm.put("name", temp.get("name")+"");
				hm.put("amount", temp.get("amount")+"");
				lhm.add(hm);
			}
		}

		
		return lhm;
	}
	
	
	public List<HashMap<String,String>> getPaidAmtAtBranch(int iRoleId,int iRegionId,int iBranchId, int feeid,String fromDate,String toDate){
		List<HashMap<String,String>> lhm = new ArrayList<HashMap<String,String>>();
		String stBranchIds ="";
		try{
			String strDateFormat = "dd-MMM-yyyy"; //Date format is Specified
			  SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat);
			if(fromDate.equals("0")){
				fromDate="14-May-2017";				
			}
			if(toDate.equals("0")){				
				toDate=objSDF.format(new Date())+"";
			}
			
			
		try{
		stBranchIds = ddpd.getDynamicBranchIds(iRoleId,iRegionId,iBranchId);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		
		
		String qry =" SELECT decode(instr(String_agg(amount, '_'),'L'),0, decode(substr(String_agg(amount, '_'),1,1),'A',String_agg(amount, '_'),'A#0_'||String_agg(amount, '_'))||'_L#0',decode(substr(String_agg(amount, '_'),1,1),'A',String_agg(amount, '_'),'A#0_'||String_agg(amount, '_'))) amt, branch_id ";
			   qry+=" FROM   (SELECT Decode(Sign(Date_part('days', ( To_date(tr_date) ) - Nvl((SELECT ( dt_granted_date )  FROM  bis_dev.cml_licence_detail  b  WHERE a.application_id = str_app_id ";
			   qry+=" AND  a.branch_id = b.num_branch_id), sysdate))), 1, 'L',  '-1', 'A',  0, 'A')||'#'||Round(Avg(amount) / 1000)  amount,branch_id    ";    
			   qry+=" FROM   bis_dev.gblt_payment_gateway_dtls a ";
			   qry+="  WHERE  payment_status = '0300' AND branch_id NOT IN ( 41, 10 )  ";
					   if(!fromDate.equals("0") && !toDate.equals("0")){
							qry+="and to_date(tr_date) between '"+fromDate+"' and '"+toDate+"'  ";
						}
					   if(stBranchIds.length()>0){
							qry+="and a.branch_id IN ("+stBranchIds+")  ";
						}
			   qry+="  GROUP  BY Decode(Sign(Date_part('days', ( To_date(tr_date) ) - Nvl((SELECT ( dt_granted_date ) FROM   bis_dev.cml_licence_detail   b  WHERE a.application_id = str_app_id ";
			   qry+=" AND a.branch_id = b.num_branch_id), sysdate))), 1, 'L','-1', 'A',0, 'A'),branch_id ";
			   qry+=" ORDER  BY 2  ,1 ";
			   qry+=" )GROUP  BY branch_id ORDER  BY (SELECT str_branc_short_name FROM bis_dev.gblt_branch_mst where num_branch_id=branch_id)   ";
									
		List<Map<String,Object>> runQry = jdbcTemplate.queryForList(qry);
		if(runQry.size()>0){
			for(int i=0;i<runQry.size();i++){
				HashMap<String,String> hm = new HashMap<String,String>();
				Map<String,Object> temp = runQry.get(i);
				String t=String.valueOf(temp.get("amt"));
				String tem[]=t.split("_");
				String a[] = tem[0].split("#");
				String l[] = tem[1].split("#");
				String amtA = a[1];
				String typeA = a[0];
				String amtl = l[1];
				String typel = l[0];
				//Double tot = Double.parseDouble(amtA)+ Double.parseDouble(amtl);
				hm.put("application", amtA+"");
				hm.put("licence", amtl+"");
								
				String qrytot = "SELECT round(sum(a.amount)/1000000) tot FROM bis_dev.gblt_payment_gateway_dtls a where  a.payment_status='0300' and a.branch_id ="+temp.get("branch_id")+" ";
				if(!fromDate.equals("0") && !toDate.equals("0")){
					qrytot+="and to_date(tr_date) between '"+fromDate+"' and '"+toDate+"'  ";
				}
				
				List<Map<String, Object>> t1=jdbcTemplate.queryForList(qrytot);
				if(t1.size()>0){
					
						HashMap<String,String> hm1 = new HashMap<String,String>();
						Map<String,Object> temp1 = t1.get(0);
						String t11=String.valueOf(temp1.get("tot"));
						hm.put("amount", t11+"");
					
				}
				
				
				String q1="SELECT a FROM Branch_Master_Domain a where a.numBranchId="+temp.get("branch_id")+" ";
				List<Branch_Master_Domain> bdom=daoHelper.findByQuery(q1);
				hm.put("bid", bdom.get(0).getStrBrShortName());
				lhm.add(hm);
			}
		}
		
		}catch(Exception e1){
			e1.printStackTrace();
		}
		
		return lhm;
	}

	
}
