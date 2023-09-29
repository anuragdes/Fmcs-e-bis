package BIS.Einvoice.DAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import BIS.Einvoice.Domain.AHCEInvoiceAuthenticationDomain;
import BIS.Einvoice.Domain.AHCEInvoiceCancelIRNDomain;
import BIS.Einvoice.Domain.AHCEInvoiceErrorLogDomain;
import BIS.Einvoice.Domain.AHCEInvoiceIRNDtlsDomain;
import BIS.Einvoice.Domain.AHCEinvoiceAuthJsonDataDomain;
import BIS.Einvoice.Domain.EinvoicingGSTCredentailsDomain;
import Global.CommonUtility.DAO.DaoHelper;
@Repository
public class AHCEInvoiceDao {

	@Autowired
	DaoHelper daoHelper;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	
	public int getTransactionDtlsCount(int transactionNumber) {
		try {
		System.out.println("inside transaction details function");
		String query="SELECT COUNT(d) from AHCEInvoiceIRNDtlsDomain d where d.transactionNumber="+transactionNumber+" and d.isValid=1";
		System.out.println("Query is "+query);
		return Integer.parseInt(daoHelper.findByQuery(query).get(0).toString());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return 0;
		}
		
	}

	public boolean checkTransactionAmountsForAHC(Integer transactionNumber) {
		boolean result=false;
		try {
		List<Map<String,Object>> transactionList = null;
		Double paymentdtlsAmount =0.00;
		Double feepaidamount=0.00;
		Double limit =2.00;
		
		
	 String query="SELECT  a.transaction_number transactionno,a.amount paymentdtlamount,nvl((SELECT TRUNC(to_number(sum(b.num_amount)),2)  FROM bis_hall.hall_fee_paid_detail_ahcreg b where" + 
	" b.num_payment_status_id=1 and b.transaction_number=a.transaction_number ) ,0) pcfeepaidamount" + 
	" FROM bis_hall.paymentdetailsdomain_ahc a where payment_status='0300'" + 
	" and (to_number(a.amount))<>(nvl((SELECT to_number(sum(b.num_amount))  FROM" + 
	" bis_hall.hall_fee_paid_detail_ahcreg b where b.num_payment_status_id=1 and" + 
	" b.transaction_number=a.transaction_number ) ,0)) and a.transaction_number="+transactionNumber+"" + 
	" order by 1";
	
	transactionList= jdbcTemplate.queryForList(query);
	
	if(!transactionList.isEmpty() && transactionList.size()==1) {
	paymentdtlsAmount= Double.parseDouble(transactionList.get(0).get("paymentdtlamount").toString());
	feepaidamount= Double.parseDouble(transactionList.get(0).get("pcfeepaidamount").toString());
	System.out.println("paymentdtlsAmount AHC "+paymentdtlsAmount);
	System.out.println("pcfeepaidamount AHC"+feepaidamount);
	}
	
	double diff = Math.abs(paymentdtlsAmount - feepaidamount);
	System.out.println("difference is"+diff);
	
	if(diff < limit) {
		result= true;
	}
	else {
		result= false;
	}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
   return result;
	}
	
	
	public void logEInvoiceErrorLog(AHCEInvoiceErrorLogDomain errorlog) {
		daoHelper.persist(AHCEInvoiceErrorLogDomain.class, errorlog);
	}
	
	public List<Map<String,Object>> getTransactionDetailsOFPC(Integer transactionNumber) {
		String query="SELECT to_char(b.dt_entry_date,'DD-MON-YYYY')date, a.transaction_number, b.num_receipt_no receipt_no," + 
				" decode(nvl((select 1 from bis_dev.cml_licence_detail zz where zz.str_app_id=a.application_id and zz.num_branch_id=a.branch_id),2),1,'Licence No','Application No')lic_app," + 
				" nvl((select 'CM/L-'::character varying  from bis_dev.cml_licence_detail zz where zz.str_app_id=a.application_id and zz.num_branch_id=a.branch_id),'CM/A-'::character varying)||" + 
				" nvl((select str_cml_no from bis_dev.cml_licence_detail zz where zz.str_app_id=a.application_id and zz.num_branch_id=a.branch_id),a.application_id)application_id," + 
				" decode(nvl(a.payment_status,'NA'),'0300','Payment Success','0399','Cancelled by User','0002','Unable to Process','NA','Not Available')payment_status," + 
				" d.str_address1 branch_add_1,d.str_address2 branch_add_2,str_city," + 
				" (select initcap(str_district_name) from bis_dev.gblt_district_mst dd where dd.num_district_id=d.num_district_id)||'-'||num_pincode||'('||(select initcap(str_state_name) from bis_dev.gblt_state_mst ss where ss.num_state_id=d.num_state_id)||')' branch_pin," + 
				" (select initcap(str_state_name) from bis_dev.gblt_state_mst ss where ss.num_state_id=d.num_state_id)branch_state," + 
				" (select initcap(str_state_code) from bis_dev.gblt_state_mst ss where ss.num_state_id=d.num_state_id)branch_state_code," + 
				" nvl(d.str_branch_contact,'NA')branch_contact,nvl(d.str_branch_fax,'NA')branch_fax,nvl(d.str_branch_mail,'NA')branch_email," + 
				" 'AAATB0431G' branch_pan,nvl(d.str_gst_no,'NA')branch_gst,'998349' branch_sac,a.branch_id,b.num_receipt_no,d.str_branch_fax," + 
				" d.str_branch_mail,qq.str_application_id,decode(nvl(" + 
				" (select 1 from bis_dev.cml_licence_detail zz where zz.str_app_id=a.application_id and zz.num_branch_id=a.branch_id)" + 
				",2),1,(select str_firm_name from bis_dev.cml_licence_detail cd where cd.str_app_id=a.application_id and cd.num_branch_id=a.branch_id)," + 
				" (nvl(a.firm_name,'NA')))firm_name,decode(nvl(" + 
				" (select 1 from bis_dev.cml_licence_detail zz where zz.str_app_id=a.application_id and zz.num_branch_id=a.branch_id)" + 
				",2),1,(select str_fac_address1 from bis_dev.cml_licence_detail cd where cd.str_app_id=a.application_id and cd.num_branch_id=a.branch_id)," + 
				"(select str_address1 from bis_dev.pc_application_factory_dtl pd where pd.str_application_id=a.application_id and pd.num_branch_id=a.branch_id))factory_add_1," + 
				" nvl(decode(nvl(" + 
				" (select 1 from bis_dev.cml_licence_detail zz where zz.str_app_id=a.application_id and zz.num_branch_id=a.branch_id)" + 
				",2),1,(select str_fac_address2 from bis_dev.cml_licence_detail cd where cd.str_app_id=a.application_id and cd.num_branch_id=a.branch_id)," + 
				" (select str_address2 from bis_dev.pc_application_factory_dtl pd where pd.str_application_id=a.application_id and pd.num_branch_id=a.branch_id)),'')factory_add_2," + 
				" decode(nvl(" + 
				" (select 1 from bis_dev.cml_licence_detail zz where zz.str_app_id=a.application_id and zz.num_branch_id=a.branch_id)" + 
				",2),1,(select str_fac_city_name from bis_dev.cml_licence_detail cd where cd.str_app_id=a.application_id and cd.num_branch_id=a.branch_id)," + 
				" (select str_city_name from bis_dev.pc_application_factory_dtl pd where pd.str_application_id=a.application_id and pd.num_branch_id=a.branch_id))fac_city_name," + 
				" (select str_district_name from bis_dev.gblt_district_mst dm where dm.num_district_id=" + 
				" decode(nvl(" + 
				" (select 1 from bis_dev.cml_licence_detail zz where zz.str_app_id=a.application_id and zz.num_branch_id=a.branch_id)" + 
				",2),1,(select num_fac_district_id from bis_dev.cml_licence_detail cd where cd.str_app_id=a.application_id and cd.num_branch_id=a.branch_id)," + 
				" (select num_district_id from bis_dev.pc_application_factory_dtl pd where pd.str_application_id=a.application_id and pd.num_branch_id=a.branch_id))) ||'-'||" + 
				" decode(nvl(" + 
				" (select 1 from bis_dev.cml_licence_detail zz where zz.str_app_id=a.application_id and zz.num_branch_id=a.branch_id)" + 
				",2),1,(select num_fac_pin_code from bis_dev.cml_licence_detail cd where cd.str_app_id=a.application_id and cd.num_branch_id=a.branch_id)," + 
				" (select num_pin_code from bis_dev.pc_application_factory_dtl pd where pd.str_application_id=a.application_id and pd.num_branch_id=a.branch_id))||' '||" + 
				" (select str_state_name from bis_dev.gblt_state_mst sm where sm.num_state_id=" + 
				" decode(nvl(" + 
				" (select 1 from bis_dev.cml_licence_detail zz where zz.str_app_id=a.application_id and zz.num_branch_id=a.branch_id)" + 
				",2),1,(select num_fac_state_id from bis_dev.cml_licence_detail cd where cd.str_app_id=a.application_id and cd.num_branch_id=a.branch_id)," + 
				" (select num_state_id from bis_dev.pc_application_factory_dtl pd where pd.str_application_id=a.application_id and pd.num_branch_id=a.branch_id)))fac_district," + 
				" (select str_country_name from bis_dev.gblt_country_mst cm where cm.num_country_id=" + 
				" decode(nvl(" + 
				" (select 1 from bis_dev.cml_licence_detail zz where zz.str_app_id=a.application_id and zz.num_branch_id=a.branch_id)" + 
				",2),1,(select num_fac_country_id from bis_dev.cml_licence_detail cd where cd.str_app_id=a.application_id and cd.num_branch_id=a.branch_id)," + 
				" (select num_country_id from bis_dev.pc_application_factory_dtl pd where pd.str_application_id=a.application_id and pd.num_branch_id=a.branch_id)))fac_country," + 
				" nvl((select distinct str_gst_num from bis_dev.user_profile_details hh where num_entry_user_id=num_gbl_user_id and num_isvalid=1 and num_id=(select max(num_id) from" + 
				" bis_dev.user_profile_details hhh where hhh.num_entry_user_id=hh.num_entry_user_id)),'NA')factory_gst," + 
				" (select str_state_name from bis_dev.gblt_state_mst sm where sm.num_state_id=" + 
				" decode(nvl(" + 
				" (select 1 from bis_dev.cml_licence_detail zz where zz.str_app_id=a.application_id and zz.num_branch_id=a.branch_id)" + 
				",2),1,(select num_fac_state_id from bis_dev.cml_licence_detail cd where cd.str_app_id=a.application_id and cd.num_branch_id=a.branch_id)," + 
				" (select num_state_id from bis_dev.pc_application_factory_dtl pd where pd.str_application_id=a.application_id and pd.num_branch_id=a.branch_id)))fac_state," + 
				" decode(nvl(" + 
				" (select 1 from bis_dev.cml_licence_detail zz where zz.str_app_id=a.application_id and zz.num_branch_id=a.branch_id)" + 
				" ,2),1,(select initcap(str_state_code) from bis_dev.gblt_state_mst ss where ss.num_state_id=(select num_fac_state_id from bis_dev.cml_licence_detail cd where cd.str_app_id=a.application_id and cd.num_branch_id=a.branch_id))," + 
				" (select initcap(str_state_code) from bis_dev.gblt_state_mst ss where ss.num_state_id=(select num_state_id from bis_dev.pc_application_factory_dtl pd where pd.str_application_id=a.application_id and pd.num_branch_id=a.branch_id)))fac_state_code," + 
				" to_char(num_total_amount,'999999999999.99')num_total_amount," + 
				" bis_dev.digit_to_string(num_total_amount::numeric)total_amount," + 
				" (select distinct num_next_total_installment_amount_with_discount" + 
				" from pc_fee_paid_detail kk where num_fee_id in (6,9) and transaction_number=a.transaction_number and num_opt_for_installments=1) nextinstallmentAmount," + 
				" (select distinct num_opt_for_installments" + 
				" from pc_fee_paid_detail kk where num_fee_id in (6,9) and transaction_number=a.transaction_number ) installmentFlag" + 
				" FROM bis_dev.gblt_payment_gateway_dtls a,bis_dev.pc_fee_detail b,bis_dev.gblt_branch_mst d,bis_dev.pc_application_submission qq" + 
				" WHERE a.transaction_number = "+transactionNumber+"" + 
				" and a.application_id=qq.str_application_id and a.branch_id=qq.num_location_id and a.transaction_number=b.transaction_number" + 
				" and a.branch_id=d.num_branch_id";
		return jdbcTemplate.queryForList(query);
	}
	
	
	public List<Map<String,Object>> getFeePaidDtlsList(int transactionNumber){
		String query="select rownum as rowid,num_receipt_no,amount,fee_name,fee_id,installmentflag,discountgiven,actualamount," + 
				" discountonactualamount,scalee,(select case when trunc(sysdate)" + 
				" <= (select trunc(upto_valid_date) from bis_dev.pc_fee_table_for_installments_or_rebate where scale_id=scalee) then 1 else 0 end )" + 
				" upto_valid_date from(select nvl( b.num_receipt_no,'0') num_receipt_no," + 
				" (to_char(c.num_amount,9999999999999.99)) amount , (select str_fee_desc from bis_dev.pc_fee_mst q" + 
				" where q.num_fee_id=c.num_fee_id)||' '||((select decode(str_amount_type,'%',('('||num_amount||'%)'),' ')" + 
				" from bis_dev.pc_fee_mst q where q.num_fee_id=c.num_fee_id)) fee_name," + 
				" (select str_amount_type from bis_dev.pc_fee_mst q where q.num_fee_id=c.num_fee_id) str_amount_type,c.num_fee_id as fee_id," + 
				" c.num_opt_for_installments installmentflag,c.num_discount as discountgiven , c.num_actual_original_amount actualamount," + 
				" c.num_discount_amount_on_numactualoriginalamount discountonactualamount," + 
				" (select  num_scale_id from user_profile_details upd where upd.num_entry_user_id" + 
				" =(select num_gbl_user_id from pc_application_submission pas where pas.str_application_id=b.str_application_id" + 
				" and pas.num_location_id=b.num_branch_id)" + 
				" and upd.num_id=(select max(num_id) from user_profile_details upd1 where upd.num_entry_user_id=upd1.num_entry_user_id)" + 
				" )scalee from bis_dev.pc_fee_detail b, bis_dev.pc_fee_paid_detail c" + 
				" where   b.num_receipt_no=c.num_receipt_no and b.transaction_number=c.transaction_number" + 
				" and c.num_amount>0 and b.transaction_number="+transactionNumber+" and c.num_payment_status_id=1 and c.num_isvalid=1 and b.num_isvalid=1 and c.num_fee_id in (48,49,50,51)" + 
				" order by str_amount_type desc,num_fee_id)";
		
		return jdbcTemplate.queryForList(query);
	}

	public void saveAuthAPIResponse(AHCEInvoiceAuthenticationDomain eIAD) {	
		daoHelper.persist(AHCEInvoiceAuthenticationDomain.class,eIAD );
	}

	public void saveIRNAPIResponseData(AHCEInvoiceIRNDtlsDomain eIRNDD) {
		daoHelper.persist(AHCEInvoiceIRNDtlsDomain.class,eIRNDD );
	}
	
	public void saveAuthJsonResponse(AHCEinvoiceAuthJsonDataDomain ahcEinvoiceAuthJsonDataDomain)
	{
		daoHelper.persist(AHCEinvoiceAuthJsonDataDomain.class, ahcEinvoiceAuthJsonDataDomain);
	}
	
	public AHCEinvoiceAuthJsonDataDomain getAuthJsonResponse(int transactionNumber)
	{
		
		String query ="select d from AHCEinvoiceAuthJsonDataDomain d where d.transactionNumber="+transactionNumber+" order by d.jsonDataId desc";
		List<AHCEinvoiceAuthJsonDataDomain> ahcEinvoiceAuthJsonDataDomainList = new ArrayList<AHCEinvoiceAuthJsonDataDomain>();		
		try {
		ahcEinvoiceAuthJsonDataDomainList = daoHelper.findByQuery(query);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		if(ahcEinvoiceAuthJsonDataDomainList.size()>0)
		{
			return ahcEinvoiceAuthJsonDataDomainList.get(0);
		}else
		{
			return new AHCEinvoiceAuthJsonDataDomain();
		}
	}
	
	public EinvoicingGSTCredentailsDomain getGSTCredentails(int regionId)
	{
		List<EinvoicingGSTCredentailsDomain> gstCredentialDomainList = new ArrayList<EinvoicingGSTCredentailsDomain>();
		String query= "select c from EinvoicingGSTCredentailsDomain c where c.regionId="+regionId;
		gstCredentialDomainList = daoHelper.findByQuery(query);
		if(gstCredentialDomainList.size()>0)
		{
			return gstCredentialDomainList.get(0);
		}
		else
		{
			return new EinvoicingGSTCredentailsDomain();
		}
	}
	
	public AHCEInvoiceIRNDtlsDomain getIRNDetails(int transactionNumber)
	{
		List<AHCEInvoiceIRNDtlsDomain> ahcEInvoiceIRNDtlsDomainList = new ArrayList<AHCEInvoiceIRNDtlsDomain>();
		String query ="from AHCEInvoiceIRNDtlsDomain where transactionNumber="+transactionNumber+" and isValid=1";
		ahcEInvoiceIRNDtlsDomainList = daoHelper.findByQuery(query);
		if(ahcEInvoiceIRNDtlsDomainList.size()>0)
		{
			return ahcEInvoiceIRNDtlsDomainList.get(0);
		}
		else
		{
			return new AHCEInvoiceIRNDtlsDomain();
		}
	}
	
	public AHCEInvoiceErrorLogDomain getLogData(int transactionNumber)
	{
		List<AHCEInvoiceErrorLogDomain> ahcEInvoiceErrorLogDomain = new ArrayList<AHCEInvoiceErrorLogDomain>();
		String query ="select c from AHCEInvoiceErrorLogDomain c where c.strApiErrorCode='2150' and c.transactionNumber="+transactionNumber+"  order by c.dtLogDate desc";
		ahcEInvoiceErrorLogDomain =daoHelper.findByQuery(query);
		if(ahcEInvoiceErrorLogDomain.size()>0)
		{
			return ahcEInvoiceErrorLogDomain.get(0);
		}else
		{
			return new AHCEInvoiceErrorLogDomain();
		}
		
	}
	
	public void saveIRNCancelDetails(AHCEInvoiceCancelIRNDomain ahcEInvoiceCancelIRNDomain)
	{
		if(ahcEInvoiceCancelIRNDomain!=null)
		{
			daoHelper.persist(AHCEInvoiceCancelIRNDomain.class, ahcEInvoiceCancelIRNDomain);
			AHCEInvoiceIRNDtlsDomain ahcEInvoiceIRNDtlsDomain= new AHCEInvoiceIRNDtlsDomain();
			ahcEInvoiceIRNDtlsDomain = getIRNDetails(ahcEInvoiceCancelIRNDomain.getTransactionNumber());
			if(ahcEInvoiceIRNDtlsDomain.getNumId()!=0)
			{
				AHCEInvoiceIRNDtlsDomain domainObj= daoHelper.findById(AHCEInvoiceIRNDtlsDomain.class, ahcEInvoiceIRNDtlsDomain.getNumId());
				if(domainObj!=null)
				{
					domainObj.setIsValid(0);
					daoHelper.merge(AHCEInvoiceIRNDtlsDomain.class, domainObj);					
				}
				
			}
		}
			
	}
}
