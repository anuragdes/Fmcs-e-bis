package Applicant.Controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import Applicant.DAO.DGdashboardDAO;
import Applicant.Model.AllCmlReportModel;
import Applicant.Service.DGdashboardservice;
import Global.Login.Model.Session;
import Global.Login.Service.IMigrateService;
import Schemes.ProductCertification.reports.Service.BisReportService;
import eBIS.AppConfig.CustomWebExceptionHandler;




@Controller
/*@Component("dgdashchedulerBean")*/
public class Dg_dashboard {

	@Autowired
	DGdashboardDAO DGdao;
	
	@Autowired
	DGdashboardservice DGserv;

	@Autowired
	IMigrateService crypt; 
	
	@Autowired
	BisReportService bisReportService;
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
	private static final SimpleDateFormat parseDateFormat = new SimpleDateFormat("dd/MM/yyyy");

	private static final int Apr = 0;
	@CustomWebExceptionHandler()
	@RequestMapping(value = "/DGDashboard", method = RequestMethod.GET)
	public ModelAndView DGDashboard(@ModelAttribute("model1") AllCmlReportModel cmlModel,HttpServletRequest request){
		
	System.out.println(":::::::::::::::::::::::::in ApplicationLicenceRelatedrpt");
		ModelAndView modelAndView = new ModelAndView();
		

		try{
			HttpSession httpsession = request.getSession(false);
			Session userSession;
			userSession = (Session) httpsession.getAttribute("logged-in");
			if(userSession!=null)
			{		
				int roleId=Integer.parseInt(userSession.getCurrent_role());
				if(roleId!=3){
				
					System.out.println("roleId :"+roleId);
					modelAndView.addObject("roleId",roleId);
					
					int locationid=userSession.getLocation_id();
					modelAndView.addObject("locationid",locationid);
					System.out.println("locationid:::::::"+locationid);
					
					int locationtype=userSession.getLocation_type();
					modelAndView.addObject("locationtype",locationtype);
					System.out.println("locationtype:::::::"+locationtype);
					
					
					String locId="";
					
					
				
					List<HashMap<String,String>> branchIds = new ArrayList<HashMap<String,String>>();
					
					
					String fromDate=cmlModel.getFromDt();
					String toDate=cmlModel.getToDt();
					String selectedBranchId=cmlModel.getBranchId();
					
					/*if(selectedBranchId!=null){
						modelAndView.addObject("selectedBranchId",selectedBranchId);
						}
						else
						{
							modelAndView.addObject("selectedBranchId",0);
						}*/
					
					String regionId =cmlModel.getRegionId();
					modelAndView.addObject("branchIdList",selectedBranchId);
					String reg_id=""+userSession.getLocation_id();
					if(regionId!=null){
					modelAndView.addObject("regionId1",regionId);
					}
					else
					{
						modelAndView.addObject("regionId1",0);
					}
					
					modelAndView.addObject("branch_id",reg_id);
					System.out.println("branch id is "+selectedBranchId+"regionid"+regionId);
				
					SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy"); 
			  	       String Date = formatter.format(new Date());//this is present date
			  	 
			  	    Calendar c = Calendar.getInstance();   
			  	    c.set(Calendar.DAY_OF_MONTH, 1); 
			  	    String startdateofMonth= formatter.format(c.getTime());
			  	    System.out.println("start date is "+startdateofMonth);//Month ka start date hai
			  	    
			  	    
			  	    
			  	    
					
					
					if(cmlModel.getFromDt()==null && cmlModel.getToDt()==null )
					{
						
						fromDate=startdateofMonth;
						toDate=Date;
						modelAndView.addObject("fromDate",fromDate);
						modelAndView.addObject("toDate",toDate);
						 System.out.println("fromDate date is "+fromDate);
						 System.out.println("toDate date is "+toDate);
						
					
					}
					else
					{
						    try {
								Date fromdate1 = parseDateFormat.parse(fromDate);
								fromDate=sdf.format(fromdate1);
								System.out.println("fromDate"+fromDate);
								
								
							} catch (ParseException e) {
							       e.printStackTrace();
							}
						    
						    try {
								
								Date todate1=parseDateFormat.parse(toDate);
								toDate=sdf.format(todate1);
								System.out.println("toDate"+toDate);
								
							} catch (ParseException e) {
							       e.printStackTrace();
							}
					}
					
					if(roleId != 7 && roleId != 4 && roleId != 5 ){
						System.out.println("inside this");
						  if(regionId==null && selectedBranchId==null || regionId.equals("All") && selectedBranchId.equals("All")){
				        	   selectedBranchId=DGdao.getAllBranchIds();
				        	   System.out.println("all selected branch id's are "+selectedBranchId);
				        		modelAndView.addObject("selectedBranchId11",crypt.Jcrypt("All"));
				           }
						  else if(!regionId.equals("All") && selectedBranchId.equals("All")  ){
        	   System.out.println("selectedBranchId123"+selectedBranchId);
						selectedBranchId=DGdao.getBranchIdsbylocationId(regionId);
						System.out.println("selectedBranchId"+selectedBranchId);
						 modelAndView.addObject("selectedBranchId11",crypt.Jcrypt(selectedBranchId));
					}

          
           else {
        	   
        	   modelAndView.addObject("selectedBranchId11",crypt.Jcrypt(selectedBranchId));
           }
           
					}
					else if(roleId==7){
						if(regionId==null && selectedBranchId==null ||regionId==null && selectedBranchId.equals("All")  ){
				        	   
										selectedBranchId=DGdao.getBranchIdsbylocationId(String.valueOf(userSession.getLocation_id()));
										System.out.println("selectedBranchId"+selectedBranchId);
										 modelAndView.addObject("selectedBranchId11",crypt.Jcrypt(selectedBranchId));
									}
						else{
							modelAndView.addObject("selectedBranchId11",crypt.Jcrypt(selectedBranchId));
						}
						
					}
					else
					{
						System.out.println("inside else");
						selectedBranchId=String.valueOf(userSession.getLocation_id());	
						 modelAndView.addObject("selectedBranchId11",crypt.Jcrypt(String.valueOf(userSession.getLocation_id())));
					}
           long PendingAppCount=0;
           long PendingAppCountRcvdthismonth=0;
           long RenewalAppPendingatbranch=0;
           long RenewalAppPendingatbranchdue=0;
           long InspectionAssigned=0;
           long InspectionCompleted=0;
           long CMLunderSOM=0;
           long CMLunderSOM180=0;
           long LicenseCount=0;
           long LicenseCountThismonth=0;
           long ReceivedarticleCount=0;
           long Articlehallmarked=0;
           String regionId1="";
           if(regionId==null) {
        	   regionId1="All";
           }else {
        	   regionId1=regionId;
        			   }
        	   
           final Future<Long>  ReceivedarticleCount1=DGdao.getReceivedarticleCount(fromDate, toDate,regionId1,selectedBranchId);
           final Future<Long>  Articlehallmarked1=DGdao.getArticlehallmarked(fromDate, toDate,regionId1);
           
           while(!ReceivedarticleCount1.isDone() ){

        	   ReceivedarticleCount = ReceivedarticleCount1.get();

			}
           while(!Articlehallmarked1.isDone() ){

        	   Articlehallmarked = Articlehallmarked1.get();
               System.out.println("article hallmarked ::::::  "+Articlehallmarked);

			}
           System.out.println("article hallmarked ::::::  "+Articlehallmarked);           
           final Future<Long>  pendingAppCount=DGdao.getpendingapplication1(fromDate, toDate,selectedBranchId);
           final Future<Long>  pendingAppCountRcvdthismonth=DGdao.getpendingapplicationThismonth1(fromDate, toDate,selectedBranchId);
			
			while(!pendingAppCount.isDone() || !pendingAppCountRcvdthismonth.isDone()){

				PendingAppCount = pendingAppCount.get();
				PendingAppCountRcvdthismonth = pendingAppCountRcvdthismonth.get();

			}
			
			final Future<Long> renewalAppPendingatbranch=DGdao.getRenewalAppPendingatbranch1(fromDate, toDate,selectedBranchId);
			final Future<Long> renewalAppPendingatbranchdue=DGdao.getRenewalAppPendingatbranchDue1(fromDate, toDate,selectedBranchId);
			
			while(!renewalAppPendingatbranch.isDone() || !renewalAppPendingatbranchdue.isDone()){

				RenewalAppPendingatbranch = renewalAppPendingatbranch.get();
				RenewalAppPendingatbranchdue = renewalAppPendingatbranchdue.get();
				System.out.println("RenewalAppPendingatbranch::::::"+RenewalAppPendingatbranch);
				System.out.println("RenewalAppPendingatbranch::::::"+RenewalAppPendingatbranch);
			}
			
			final Future<Long> inspectionAssigned=DGdao.getInspectionAssigned1(fromDate, toDate,selectedBranchId);
			final Future<Long> inspectionCompleted=DGdao.getInspectionCompleted1(fromDate, toDate,selectedBranchId);
			
			while(!inspectionAssigned.isDone() || !inspectionCompleted.isDone()){

				InspectionAssigned = inspectionAssigned.get();
				InspectionCompleted = inspectionCompleted.get();

			}
			
			final Future<Long> cMLunderSOM=DGdao.getCMLunderSOM1(fromDate, toDate,selectedBranchId);
			final Future<Long> cMLunderSOM180=DGdao.CMLunderSOM1801(fromDate, toDate,selectedBranchId);
			
			while(!cMLunderSOM.isDone() || !cMLunderSOM180.isDone()){

				CMLunderSOM = cMLunderSOM.get();
				CMLunderSOM180 = cMLunderSOM180.get();

			}
			
			final Future<Long> licenseCount=DGdao.gettotalnooflicences1(fromDate, toDate,selectedBranchId);
			final Future<Long> licenseCountThismonth=DGdao.gettotalnooflicencesThismonth1(fromDate, toDate,selectedBranchId);
			
			while(!licenseCount.isDone() || !licenseCountThismonth.isDone()){

				LicenseCount = licenseCount.get();
				LicenseCountThismonth = licenseCountThismonth.get();

			}
			System.out.println("LicenseCount::::::::"+LicenseCount);
			
			if(roleId==7){
				branchIds = DGserv.getBranch(userSession.getLocation_id());
				modelAndView.addObject("branchIds",branchIds);
			}
			
			modelAndView.addObject("Articlehallmarked",Articlehallmarked);
			modelAndView.addObject("ReceivedarticleCount",ReceivedarticleCount);
			modelAndView.addObject("SelectedRegionid",regionId);
			
			modelAndView.addObject("LicenseCount",LicenseCount);
			modelAndView.addObject("PendingAppCount",PendingAppCount);
			modelAndView.addObject("RenewalAppPendingatbranch",RenewalAppPendingatbranch);
		
			modelAndView.addObject("LicenseCountThismonth",LicenseCountThismonth);
			modelAndView.addObject("PendingAppCountRcvdthismonth",PendingAppCountRcvdthismonth);
			modelAndView.addObject("RenewalAppPendingatbranchdue",RenewalAppPendingatbranchdue);
			
			modelAndView.addObject("InspectionAssigned",InspectionAssigned);
			modelAndView.addObject("InspectionCompleted",InspectionCompleted);
			modelAndView.addObject("CMLunderSOM",CMLunderSOM);
			modelAndView.addObject("CMLunderSOM180",CMLunderSOM180);
			
			modelAndView.addObject("fromDate1",crypt.Jcrypt(fromDate));
			modelAndView.addObject("toDate1",crypt.Jcrypt(toDate));
			modelAndView.addObject("selectedBranchId1",crypt.Jcrypt(selectedBranchId));
			modelAndView.addObject("selBranchName",crypt.Jcrypt("Select Branch"));
			modelAndView.addObject("selectedBranchId",selectedBranchId);
		
			System.out.println("selectedBranchId::::::::::::..."+selectedBranchId);
			
			
            // modelAndView.setViewName("DGdashboard");
	        modelAndView.setViewName("DGdashboard2");
				
				}
				else{
					modelAndView.setViewName("AccessDenied");
				}
			} 
                else{
				
                  	modelAndView.setViewName("sessionExpire");
			}			
		
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return modelAndView;
	}
	






@RequestMapping(value = "/datafordiv3", method = RequestMethod.POST)
public @ResponseBody  List<Long> piechartData(HttpServletRequest request)throws IOException, InterruptedException, ExecutionException{
{

List<Long> listofdiv2 = new ArrayList<Long>();

String fromDate=crypt.Dcrypt(request.getParameter("fromDate1"));
String toDate=crypt.Dcrypt(request.getParameter("toDate1"));
String selectedBranchId=crypt.Dcrypt(request.getParameter("selectedBranchId1"));

long AllIndiaFirstApp=0;
long AllIndiaFirstAppthismonth=0;
long TotalDeferment=0;
long DefermentMoreThan90days=0;
long MarketSurAssigned=0;
long MarketSurCompleted=0;
long ROPUnderprocess=0;
long ROPDue=0;
long RevenueFY=0;
long RevenueThisMonth=0;
long FactorySurAssigned=0;
long FactorySurCompleted=0;
long MarketSurPlanned=0;
long MarketSurveCompleted=0;
long FactorySamplesSent=0;
long MarketSamplesSent=0;
long LotInspectionsPlanned=0;
long LotInspectionsCompleted=0;
long SpecialFactorySurveillancePlanned=0;
long SpecialFactorySurveillanceCompleted=0;
long SpecialSamplesSentFactory=0;
long SpecialSamplesSentMarket=0;

long PreApplicationTestRequestsReceived =0;
long PreApplicationTestRequestsDisposed=0;

long MarketSampleBOAssign =0;
long MarketSampleBOCompleted=0;

final Future<Long>  allIndiaFirstApp=DGdao.getAllIndiaFirstApp1(fromDate, toDate,selectedBranchId);
final Future<Long>  allIndiaFirstAppthismonth=DGdao.getAllIndiaFirstAppthismonth1(fromDate, toDate,selectedBranchId);

while(!allIndiaFirstApp.isDone() || !allIndiaFirstAppthismonth.isDone()){

	AllIndiaFirstApp = allIndiaFirstApp.get();
	AllIndiaFirstAppthismonth = allIndiaFirstAppthismonth.get();

}


final Future<Long>  totalDeferment=DGdao.getTotalDeferment1(fromDate, toDate,selectedBranchId);
final Future<Long>  defermentMoreThan90days=DGdao.getDefermentMoreThan90days1(fromDate, toDate,selectedBranchId);

while(!totalDeferment.isDone() || !defermentMoreThan90days.isDone()){

	TotalDeferment = totalDeferment.get();
	DefermentMoreThan90days = defermentMoreThan90days.get();

}

final Future<Long>  marketSurAssigned=DGdao.getMarketSurAssigned1(fromDate, toDate,selectedBranchId);
final Future<Long>  marketSurCompleted=DGdao.getMarketSurCompleted1(fromDate, toDate,selectedBranchId);

while(!marketSurAssigned.isDone() || !marketSurCompleted.isDone()){

	MarketSurAssigned = marketSurAssigned.get();
	MarketSurCompleted = marketSurCompleted.get();

}

final Future<Long>  rOPUnderprocess=DGdao.getROPUnderprocess1(fromDate, toDate,selectedBranchId);
final Future<Long>  rOPDue=DGdao.getROPUnderprocessdue1(fromDate, toDate,selectedBranchId);

while(!rOPUnderprocess.isDone() || !rOPDue.isDone()){

	ROPUnderprocess = rOPUnderprocess.get();
	ROPDue = rOPDue.get();

}

final Future<Long>  revenueFY=DGdao.getRevenueFY1(fromDate, toDate,selectedBranchId);
final Future<Long>  revenueThisMonth=DGdao.getRevenueThisMonth1(fromDate, toDate,selectedBranchId);

while(!revenueFY.isDone() || !revenueThisMonth.isDone()){

	RevenueFY = revenueFY.get();
	RevenueThisMonth = revenueThisMonth.get();
System.out.println("RevenueFY::::::::"+RevenueFY);
}

final Future<Long>  factorySurAssigned=DGdao.getFactorySurAssigned1(fromDate, toDate,selectedBranchId);
final Future<Long>  factorySurCompleted=DGdao.getFactorySurCompleted1(fromDate, toDate,selectedBranchId);

while(!factorySurAssigned.isDone() || !factorySurCompleted.isDone()){

	FactorySurAssigned = factorySurAssigned.get();
	FactorySurCompleted = factorySurCompleted.get();

}

final Future<Long>  marketSurPlanned=DGdao.getMarketSurPlanned1(fromDate, toDate,selectedBranchId);
final Future<Long>  marketSurveCompleted=DGdao.getMarketSurveCompleted1(fromDate, toDate,selectedBranchId);

while(!marketSurPlanned.isDone() || !marketSurveCompleted.isDone()){

	MarketSurPlanned = marketSurPlanned.get();
	MarketSurveCompleted = marketSurveCompleted.get();

}

final Future<Long>  factorySamplesSent=DGdao.getFactorySamplesSent1(fromDate, toDate,selectedBranchId);
final Future<Long>  marketSamplesSent=DGdao.getMarketSamplesSent1(fromDate, toDate,selectedBranchId);

while(!factorySamplesSent.isDone() || !marketSamplesSent.isDone()){

	FactorySamplesSent = factorySamplesSent.get();
	MarketSamplesSent = marketSamplesSent.get();

}

final Future<Long>  lotInspectionsPlanned=DGdao.getLotInspectionsPlanned1(fromDate, toDate,selectedBranchId);
final Future<Long>  lotInspectionsCompleted=DGdao.getLotInspectionsCompleted1(fromDate, toDate,selectedBranchId);

while(!lotInspectionsPlanned.isDone() || !lotInspectionsCompleted.isDone()){

	LotInspectionsPlanned = lotInspectionsPlanned.get();
	LotInspectionsCompleted = lotInspectionsCompleted.get();

}

final Future<Long>  specialFactorySurveillancePlanned=DGdao.getSpecialFactorySurveillancePlanned1(fromDate, toDate,selectedBranchId);
final Future<Long>  specialFactorySurveillanceCompleted=DGdao.getSpecialFactorySurveillanceCompleted1(fromDate, toDate,selectedBranchId);

while(!specialFactorySurveillancePlanned.isDone() || !specialFactorySurveillanceCompleted.isDone()){

	SpecialFactorySurveillancePlanned = specialFactorySurveillancePlanned.get();
	SpecialFactorySurveillanceCompleted = specialFactorySurveillanceCompleted.get();

}

final Future<Long>  specialSamplesSentFactory=DGdao.getSpecialSamplesSentFactory1(fromDate, toDate,selectedBranchId);
final Future<Long>  specialSamplesSentMarket=DGdao.getSpecialSamplesSentMarket1(fromDate, toDate,selectedBranchId);

while(!specialSamplesSentFactory.isDone() || !specialSamplesSentMarket.isDone()){

	SpecialSamplesSentFactory = specialSamplesSentFactory.get();
	SpecialSamplesSentMarket = specialSamplesSentMarket.get();

}


final Future<Long>  preApplicationTestRequestsReceived=DGdao.getpreApplicationTestRequestsReceived1(fromDate, toDate,selectedBranchId);
final Future<Long>  preApplicationTestRequestsDisposed=DGdao.getpreApplicationTestRequestsDisposed1(fromDate, toDate,selectedBranchId);

while(!preApplicationTestRequestsReceived.isDone() || !preApplicationTestRequestsDisposed.isDone()){

	PreApplicationTestRequestsReceived = preApplicationTestRequestsReceived.get();
	PreApplicationTestRequestsDisposed = preApplicationTestRequestsDisposed.get();

}



final Future<Long>  metMarketSampleBOAssign=DGdao.getmetMarketSampleBOAssign(fromDate, toDate,selectedBranchId);
final Future<Long>  metMarketSampleBOCompleted=DGdao.getmetMarketSampleBOCompltd(fromDate, toDate,selectedBranchId);

while(!metMarketSampleBOAssign.isDone() || !metMarketSampleBOCompleted.isDone()){

	MarketSampleBOAssign = metMarketSampleBOAssign.get();
	MarketSampleBOCompleted = metMarketSampleBOCompleted.get();

}


listofdiv2.add(AllIndiaFirstApp);
listofdiv2.add(AllIndiaFirstAppthismonth);
listofdiv2.add(TotalDeferment);
listofdiv2.add(DefermentMoreThan90days);
listofdiv2.add(MarketSurAssigned);
listofdiv2.add(MarketSurCompleted);
listofdiv2.add(ROPUnderprocess);
listofdiv2.add(ROPDue);
listofdiv2.add(RevenueFY);
listofdiv2.add(RevenueThisMonth);
listofdiv2.add(FactorySurAssigned);
listofdiv2.add(FactorySurCompleted);
listofdiv2.add(MarketSurPlanned);
listofdiv2.add(MarketSurveCompleted);
listofdiv2.add(FactorySamplesSent);
listofdiv2.add(MarketSamplesSent);
listofdiv2.add(LotInspectionsPlanned);
listofdiv2.add(LotInspectionsCompleted);
listofdiv2.add(SpecialFactorySurveillancePlanned);
listofdiv2.add(SpecialFactorySurveillanceCompleted);
listofdiv2.add(SpecialSamplesSentFactory);
listofdiv2.add(SpecialSamplesSentMarket);

listofdiv2.add(PreApplicationTestRequestsReceived);
listofdiv2.add(PreApplicationTestRequestsDisposed);

listofdiv2.add(MarketSampleBOAssign);
listofdiv2.add(MarketSampleBOCompleted);

return listofdiv2;
}}

@RequestMapping(value = "/datafordiv2", method = RequestMethod.POST)
public @ResponseBody  List<Long> divdata3(HttpServletRequest request) throws IOException, InterruptedException, ExecutionException{

List<Long> listofdiv3 = new ArrayList<Long>();

String fromDate=crypt.Dcrypt(request.getParameter("fromDate1"));
String toDate=crypt.Dcrypt(request.getParameter("toDate1"));
String selectedBranchId=crypt.Dcrypt(request.getParameter("selectedBranchId1"));


long InclusionPending=0;
long InclusionPendingThismonth=0;
long TotalExpiry=0;
long ExpiryDue=0;
long TRAwaited=0;
long TRUploaded=0;
long TotalCancelation=0;
long TotalCancelationDUE=0;
long StandardUnderCertMAN=0;
long StandardUnderCertVOL=0;
final Future<Long> inclusionPending=DGdao.getIncslusionApplication(fromDate, toDate, selectedBranchId);
final Future<Long> inclusionPendingThisMonth=DGdao.getInclusionApplicationThisMonth(fromDate, toDate, selectedBranchId);

while(!inclusionPending.isDone() || !inclusionPendingThisMonth.isDone()){

	InclusionPending = inclusionPending.get();
	InclusionPendingThismonth = inclusionPendingThisMonth.get();

}


/*long InclusionPending=DGdao.getInclsuionapplication(fromDate, toDate,selectedBranchId);
long InclusionPendingThismonth=DGdao.getInclsuionapplicationThismonth(fromDate, toDate,selectedBranchId);*/
final Future<Long> totalExpiry=DGdao.getTotalExpirys(fromDate, toDate,selectedBranchId);
final Future<Long> expiryDue=DGdao.getExpiryDues(fromDate, toDate,selectedBranchId);

while(!totalExpiry.isDone() || !expiryDue.isDone()){

	TotalExpiry = totalExpiry.get();
	ExpiryDue = expiryDue.get();

}

final Future<Long> tRAwaited=DGdao.getTRAwaiteds(fromDate, toDate,selectedBranchId);
final Future<Long> tRUploaded=DGdao.getTRUploadeds(fromDate, toDate,selectedBranchId);

while(!tRAwaited.isDone() || !tRUploaded.isDone()){

	TRAwaited = tRAwaited.get();
	TRUploaded = tRUploaded.get();

}

final Future<Long> totalCancelation=DGdao.getTotalCancelations(fromDate, toDate,selectedBranchId);
final Future<Long> totalCancelationDUE=DGdao.getTotalCancelationDUEs(fromDate, toDate,selectedBranchId);

while(!totalCancelation.isDone() || !totalCancelationDUE.isDone()){

	TotalCancelation = totalCancelation.get();
	TotalCancelationDUE = totalCancelationDUE.get();

}

final Future<Long> standardUnderCertMAN=DGdao.getStandardUnderCertMANs(fromDate, toDate,selectedBranchId);
final Future<Long> standardUnderCertVOL=DGdao.getStandardUnderCertVOLs(fromDate, toDate,selectedBranchId);

while(!standardUnderCertMAN.isDone() || !standardUnderCertVOL.isDone()){

	StandardUnderCertMAN = standardUnderCertMAN.get();
	StandardUnderCertVOL = standardUnderCertVOL.get();


}


listofdiv3.add(InclusionPending);
listofdiv3.add(InclusionPendingThismonth);
listofdiv3.add(TotalExpiry);
listofdiv3.add(ExpiryDue);
listofdiv3.add(TRAwaited);
listofdiv3.add(TRUploaded);
listofdiv3.add(TotalCancelation);
listofdiv3.add(TotalCancelationDUE);
listofdiv3.add(StandardUnderCertMAN);
listofdiv3.add(StandardUnderCertVOL);

return listofdiv3;
}





@RequestMapping(value = "/majorIScml", method = RequestMethod.POST,  produces="application/json")
public @ResponseBody List majorIScml(HttpServletRequest request){
	System.out.println("jijijij");
	ArrayList licenceISwise = new ArrayList();
licenceISwise=DGserv.getlicenceISwise();
System.out.println("jijijijiji...jsp"   +   licenceISwise);
return licenceISwise;
}

@RequestMapping(value = "/monthwiseAppIncRen", method = RequestMethod.POST, produces="application/json")
public @ResponseBody List monthwiseAppIncRen(HttpServletRequest request){
	System.out.println("jijijij");
	ArrayList applicationMonthwise = new ArrayList();
	applicationMonthwise=DGserv.getapplicationMonthwise();
    return applicationMonthwise;
}

@RequestMapping(value = "/yearwiseAppIncRen", method = RequestMethod.POST, produces="application/json")
public @ResponseBody List yearwiseAppIncRen(HttpServletRequest request){
	
	ArrayList applicationyearwise = new ArrayList();
	applicationyearwise=DGserv.getapplicationYearwise();
    return applicationyearwise;
}

@RequestMapping(value = "/yearwiseRevenue", method = RequestMethod.POST, produces="application/json")
public @ResponseBody List yearwiseRevenue(HttpServletRequest request){
	
	ArrayList ravenueyearwise = new ArrayList();
	ravenueyearwise=DGserv.getravenueyearwise();
    return ravenueyearwise;
}


@RequestMapping(value = "/monthwiseapp", method = RequestMethod.POST)
public @ResponseBody List monthwiseapp(HttpServletRequest request){
	
ArrayList monthwiseapp = new ArrayList();
monthwiseapp=DGserv.getlicenceISwise();
System.out.println("jijijijiji...jsp"   +   monthwiseapp);
return monthwiseapp;
}



@RequestMapping(value = "/getBranchDtls", method = RequestMethod.GET)
public @ResponseBody List getBranchDtls(HttpServletRequest request)
{
	int regionID=Integer.parseInt(request.getParameter("regionId"));

	List<HashMap<String,String>> branchIds = new ArrayList<HashMap<String,String>>();
	branchIds = DGserv.getBranch(regionID);
	return branchIds;
}
//author priya

@RequestMapping(value = "/getAhcDtls1", method = RequestMethod.GET)
public @ResponseBody List getAhcDtls(HttpServletRequest request)
{ 
	System.out.println("get ahc call--------");
	int regionID=Integer.parseInt(request.getParameter("regionId"));

	 List<HashMap<String,String>> ahcs = new ArrayList<HashMap<String,String>>();
	 ahcs = DGserv.getAhcDtls(regionID);
	
	return ahcs;
}
@RequestMapping(value = "/getBranchDtlsbyahc", method = RequestMethod.GET)
public @ResponseBody List getBranchDtlsbyahc(HttpServletRequest request)
{
	int AhcId=Integer.parseInt(request.getParameter("regionId"));

	List<HashMap<String,String>> branchIds = new ArrayList<HashMap<String,String>>();
	branchIds = DGserv.getBranchDtlsbyahc(AhcId);
	return branchIds;
}


@RequestMapping(value = "/DashBoardListingPage/{value}/{from_date}/{to_date}/{loc_id}/{selectedBranchName}",method={RequestMethod.GET,RequestMethod.POST})
public ModelAndView getDashBoardListingPage(@PathVariable("value") String value,@PathVariable("from_date") String from_date,@PathVariable("to_date") String to_date,@PathVariable("loc_id") String locId, 
		@PathVariable("selectedBranchName") String selBrnchName,HttpServletRequest request,HttpServletResponse response)
{
	ModelAndView mv = new ModelAndView();
	try{
	String json ="";
	
	HttpSession httpsession = request.getSession(false);
	Session userSession;
	userSession = (Session) httpsession.getAttribute("logged-in");
	value=String.valueOf(value);
	selBrnchName =crypt.Dcrypt(selBrnchName);
	
	from_date=crypt.Dcrypt(from_date);
	to_date=crypt.Dcrypt(to_date);
	locId=crypt.Dcrypt(locId);
	String locationId="";
	if(locId.equalsIgnoreCase("All")) {
		locationId=DGdao.getAllBranchIds();
	}
	else{
		locationId=locId;
	}
	Gson gson = new Gson();
	
	
	
	
	if (userSession != null) {
	String StageName=bisReportService.getStageName(value);
	String ListQuery=bisReportService.getBisReportListQuery(value);
	System.out.println("ListQuery>>>>>>>>>>>>>"+ListQuery);
	List<Map<String,Object>> bisReportQueryListModel=bisReportService.getBisListQueryResult(ListQuery,from_date,to_date,locationId);
	request.setAttribute("BRLQL", bisReportQueryListModel);
	
	json = gson.toJson(bisReportQueryListModel);
	request.setAttribute("BRLQLjson",json);
	request.setAttribute("StageName", StageName);
	request.setAttribute("value", value);
	request.setAttribute("locationId", locationId);
	request.setAttribute("selBrnchName", selBrnchName.replaceAll("_", " "));
	mv.setViewName("dashboardReportListQueryPage");
	}
	else
	{
		
	
	
	
	
	if(Integer.parseInt(value)>=1000)
	{   
		String StageName=bisReportService.getStageName(value);
		String ListQuery=bisReportService.getBisReportListQuery(value);
		List<Map<String,Object>> bisReportQueryListModel=bisReportService.getBisListQueryResult(ListQuery,from_date,to_date,locationId);
		request.setAttribute("BRLQL", bisReportQueryListModel);
		
		json = gson.toJson(bisReportQueryListModel);
		request.setAttribute("BRLQLjson",json);
		request.setAttribute("StageName", StageName);
		request.setAttribute("value", value);
		request.setAttribute("locationId", locationId);
		request.setAttribute("selBrnchName", selBrnchName.replaceAll("_", " "));
		mv.setViewName("dashboardReportListQueryPage");
	}
	else
	{
		mv.setViewName("sessionExpire");
	}
}
}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	return mv;
}


@RequestMapping(value = "/DashBoardheaderlist/{value}/{headerid}/{loc_id}/{to_date}",method={RequestMethod.GET,RequestMethod.POST})
public ModelAndView DashBoardheaderlist(@PathVariable("value") String value,@PathVariable("headerid") String headerid,@PathVariable("loc_id") String locId,@PathVariable("to_date") String to_date,HttpServletRequest request,HttpServletResponse response)
{
	ModelAndView mv = new ModelAndView();
	try{
	String json ="";
	String callfromurl = request.getHeader("Referer");
	System.out.println("callfromurl is "+callfromurl);
	HttpSession httpsession = request.getSession(false);
	Session userSession;
	userSession = (Session) httpsession.getAttribute("logged-in");
	value=String.valueOf(value);
	


	 int CurrentYear = Calendar.getInstance().get(Calendar.YEAR);
	    int CurrentMonth = (Calendar.getInstance().get(Calendar.MONTH)+1);
	    String FYFrom="";
	    String financiyalYearTo="";
	    if(CurrentMonth<4)
	    {
	    	FYFrom="01-Apr-"+(CurrentYear-1);
	        financiyalYearTo="31-Mar-"+(CurrentYear);
	    }
	    else
	    {
	    	FYFrom="01-Apr-"+(CurrentYear);
	        financiyalYearTo="31-Mar-"+(CurrentYear+1);
	    }


	
    request.setAttribute("FYFrom", FYFrom);
	headerid=String.valueOf(headerid);
	request.setAttribute("headerid", headerid);
	System.out.println("headerid::::::"+headerid);
	to_date=crypt.Dcrypt(to_date);
	request.setAttribute("date", to_date);
	locId=crypt.Dcrypt(locId);
	String locationId="";
	if(locId.equalsIgnoreCase("All")) {
		locationId=DGdao.getAllBranchIds();
	}
	else{
		locationId=locId;
	}
	request.setAttribute("locationId", locationId);
	System.out.println("locationId::::::"+locationId);
	Gson gson = new Gson();
	
	
	
	
	if (userSession != null) {
	String StageName=DGdao.getStageName(headerid);
	String ListQuery=DGserv.getHeaderReportListQuery(value);
	List<Map<String,Object>> bisReportQueryListModel=DGserv.getHearderListQueryResult(ListQuery,locationId,to_date,FYFrom);
	request.setAttribute("BRLQL", bisReportQueryListModel);
	
	
	List Statuswise = new ArrayList();
	Statuswise =DGdao.getStage(headerid);
	request.setAttribute("Statuswise", Statuswise);
	List Pendencywise = new ArrayList();
	Pendencywise =DGdao.getPendencywise(headerid);
	request.setAttribute("Pendencywise", Pendencywise);
	System.out.println("Pendencywise"+Pendencywise );
	json = gson.toJson(bisReportQueryListModel);
	request.setAttribute("BRLQLjson",json);
	request.setAttribute("StageName", StageName);
	/*request.setAttribute("selBrnchName", selBrnchName.replaceAll("_", " "));
	mv.addObject("to_date", to_date);*/
	mv.setViewName("DashBoardheaderlist1");
	//mv.setViewName("BisReportListQueryPage");
	}
	else
	{
		
	if(Integer.parseInt(value)>=1000)
	{   
		String StageName=DGdao.getStageName(headerid);
		String ListQuery=DGserv.getHeaderReportListQuery(value);
		List<Map<String,Object>> bisReportQueryListModel=DGserv.getHearderListQueryResult(ListQuery,locationId,to_date,FYFrom);
		request.setAttribute("BRLQL", bisReportQueryListModel);
		/*String appId = (String) bisReportQueryListModel.get(0).get("Application ID");
		System.out.println("Appppppppppppppppp1"+appId);
		String EappId = crypt.Jcrypt(appId);
		System.out.println("Appppppppppppppppp1"+EappId);
		request.setAttribute("EappId", EappId);

		String branchId = Integer.toString((Integer) bisReportQueryListModel.get(0).get("num_location_id"));
		System.out.println("Appppppppppppppppp"+branchId);
		String EbranchId = crypt.Jcrypt(branchId);
		System.out.println("Appppppppppppppppp"+EbranchId);
		request.setAttribute("EbranchId", EbranchId);*/
		
		
		List Statuswise = new ArrayList();
		Statuswise =DGdao.getStage(headerid);
		request.setAttribute("Statuswise", Statuswise);
		
		List Pendencywise = new ArrayList();
		Pendencywise =DGdao.getPendencywise(headerid);
		System.out.println("Pendencywise"+Pendencywise );
		request.setAttribute("Pendencywise", Pendencywise);
		
		json = gson.toJson(bisReportQueryListModel);
		request.setAttribute("BRLQLjson",json);
		request.setAttribute("StageName", StageName);
		//request.setAttribute("selBrnchName", selBrnchName.replaceAll("_", " "));
		
		
		
		
		mv.setViewName("DashBoardheaderlist1");
	}
	else
	{
		mv.setViewName("sessionExpire");
	}
}
}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	return mv;
}





@RequestMapping(value = "/getRegions",method={RequestMethod.GET,RequestMethod.POST})
public @ResponseBody List<Map<String,Object>> getRegions(HttpServletRequest request,HttpServletResponse response)
{
	List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
	data = DGdao.getallRegions();
	System.out.println("data" +data);
	return data;
	
}

@RequestMapping(value = "/getBranches", method = RequestMethod.POST)
public @ResponseBody List getBranches(HttpServletRequest request)
{
	int regionID=Integer.parseInt(request.getParameter("regionId"));

	List<HashMap<String,String>> branchIds = new ArrayList<HashMap<String,String>>();
	branchIds = DGserv.getBranch(regionID);
	return branchIds;
}

@RequestMapping(value = "/CmlStandarddetails",method={RequestMethod.GET})
public ModelAndView CmlStandarddetails(HttpServletRequest request)
{
	ModelAndView mv = new ModelAndView();
	
	try{
	HttpSession httpsession = request.getSession(false);
	Session userSession;
	userSession = (Session) httpsession.getAttribute("logged-in");
	if (userSession != null)
	
	{
		
		 StringBuilder encodedStringisnofull = new StringBuilder(request.getParameter("isno"));
		
		 String isnofull = new String((new sun.misc.BASE64Decoder()).decodeBuffer(encodedStringisnofull.toString()));
	
		 StringBuilder encodedStringmanvol = new StringBuilder(request.getParameter("manvol"));
			
		 String manvol = new String((new sun.misc.BASE64Decoder()).decodeBuffer(encodedStringmanvol.toString()));
		
		 
		String loc_id = request.getParameter("locationId");
		System.out.println("loc_id"+loc_id);
     	System.out.println("isno"+isnofull);
     	System.out.println("manvol"+manvol);
	    
        String[] a = isnofull.split(":");
        String isno="";
        if(a.length==2){
        	isno=a[0];	
        }
        if(a.length==3){
        	isno=a[0]+":"+a[1];	
        }
        if(a.length==4){
        	isno=a[0]+":"+a[1]+":"+a[2];	
        }
        System.out.println("onlyisno"+isno);
        
        List GetCmlDetails = new ArrayList();	
        GetCmlDetails=DGdao.getIScmlDetailsList(isno,manvol,loc_id);
        
        
        mv.addObject("appLicTransferList",GetCmlDetails);
        System.out.println("String appLicTransferList..."   +GetCmlDetails);
        
        mv.setViewName("BranchWiseCMLdetail");
        



	}
	}
	catch(Exception e){
		
		e.printStackTrace();
	}
	return mv;
	
}


@RequestMapping(value = "/DG_reports", method = RequestMethod.GET)
public ModelAndView DG_reports(@ModelAttribute("model1") AllCmlReportModel cmlModel,HttpServletRequest request){
	
System.out.println(":::::::::::::::::::::::::in ApplicationLicenceRelatedrpt");
	ModelAndView modelAndView = new ModelAndView();
	

	try{
		HttpSession httpsession = request.getSession(false);
		Session userSession;
		userSession = (Session) httpsession.getAttribute("logged-in");
		if(userSession!=null)
		{		
			int roleId=Integer.parseInt(userSession.getCurrent_role());
			if(roleId!=3){
			
				System.out.println("roleId :"+roleId);
				modelAndView.addObject("roleId",roleId);
				
				int locationid=userSession.getLocation_id();
				modelAndView.addObject("locationid",locationid);
				System.out.println("locationid:::::::"+locationid);
				
				int locationtype=userSession.getLocation_type();
				modelAndView.addObject("locationtype",locationtype);
				System.out.println("locationtype:::::::"+locationtype);
				
				
				String locId="";
				
				
			
				List<HashMap<String,String>> branchIds = new ArrayList<HashMap<String,String>>();
				
				
				String fromDate=cmlModel.getFromDt();
				String toDate=cmlModel.getToDt();
				String selectedBranchId=cmlModel.getBranchId();
				
				/*if(selectedBranchId!=null){
					modelAndView.addObject("selectedBranchId",selectedBranchId);
					}
					else
					{
						modelAndView.addObject("selectedBranchId",0);
					}*/
				
				String regionId =cmlModel.getRegionId();
				modelAndView.addObject("branchIdList",selectedBranchId);
				String reg_id=""+userSession.getLocation_id();
				if(regionId!=null){
				modelAndView.addObject("regionId1",regionId);
				}
				else
				{
					modelAndView.addObject("regionId1",0);
				}
				
				modelAndView.addObject("branch_id",reg_id);
				System.out.println("branch id is "+selectedBranchId+"regionid"+regionId);
			
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy"); 
		  	       String Date = formatter.format(new Date());//this is present date
		  	 
		  	    Calendar c = Calendar.getInstance();   
		  	    c.set(Calendar.DAY_OF_MONTH, 1); 
		  	    String startdateofMonth= formatter.format(c.getTime());
		  	    System.out.println("start date is "+startdateofMonth);//Month ka start date hai
		  	    
		  	    
		  	    
		  	    
				
				
				if(cmlModel.getFromDt()==null && cmlModel.getToDt()==null )
				{
					
					fromDate=startdateofMonth;
					toDate=Date;
					modelAndView.addObject("fromDate",fromDate);
					modelAndView.addObject("toDate",toDate);
					 System.out.println("fromDate date is "+fromDate);
					 System.out.println("toDate date is "+toDate);
					
				
				}
				else
				{
					    try {
							Date fromdate1 = parseDateFormat.parse(fromDate);
							fromDate=sdf.format(fromdate1);
							System.out.println("fromDate"+fromDate);
							Date todate1=parseDateFormat.parse(toDate);
							toDate=sdf.format(todate1);
							System.out.println("toDate"+toDate);
							
						} catch (ParseException e) {
						       e.printStackTrace();
						}
				}
				
				if(roleId != 7 && roleId != 4 && roleId != 5 ){
					System.out.println("inside this");
					  if(regionId==null && selectedBranchId==null || regionId.equals("All") && selectedBranchId.equals("All")){
			        	   selectedBranchId=DGdao.getAllBranchIds();
			        	   System.out.println("all selected branch id's are "+selectedBranchId);
			        		modelAndView.addObject("selectedBranchId11",crypt.Jcrypt("All"));
			           }
					  else if(!regionId.equals("All") && selectedBranchId.equals("All")  ){
    	   System.out.println("selectedBranchId123"+selectedBranchId);
					selectedBranchId=DGdao.getBranchIdsbylocationId(regionId);
					System.out.println("selectedBranchId"+selectedBranchId);
					 modelAndView.addObject("selectedBranchId11",crypt.Jcrypt(selectedBranchId));
				}

      
       else {
    	   
    	   modelAndView.addObject("selectedBranchId11",crypt.Jcrypt(selectedBranchId));
       }
       
				}
				else if(roleId==7){
					if(regionId==null && selectedBranchId==null ||regionId==null && selectedBranchId.equals("All")  ){
			        	   
									selectedBranchId=DGdao.getBranchIdsbylocationId(String.valueOf(userSession.getLocation_id()));
									System.out.println("selectedBranchId"+selectedBranchId);
									 modelAndView.addObject("selectedBranchId11",crypt.Jcrypt(selectedBranchId));
								}
					else{
						modelAndView.addObject("selectedBranchId11",crypt.Jcrypt(selectedBranchId));
					}
					
				}
				else
				{
					System.out.println("inside else");
					selectedBranchId=String.valueOf(userSession.getLocation_id());	
					 modelAndView.addObject("selectedBranchId11",crypt.Jcrypt(String.valueOf(userSession.getLocation_id())));
				}
      
	
		System.out.println("selectedBranchId::::::::::::..."+selectedBranchId);
		
		modelAndView.addObject("fromDate1",crypt.Jcrypt(fromDate));
		modelAndView.addObject("toDate1",crypt.Jcrypt(toDate));
		modelAndView.addObject("selectedBranchId1",crypt.Jcrypt(selectedBranchId));
		modelAndView.addObject("selBranchName",crypt.Jcrypt("Select Branch"));
		modelAndView.addObject("selectedBranchId",selectedBranchId);
	
		
       
        modelAndView.setViewName("DG_reports");
			
			}
			else{
				modelAndView.setViewName("AccessDenied");
			}
		} 
            else{
			
              	modelAndView.setViewName("sessionExpire");
		}			
	
		
	}
	catch (Exception e) {
		e.printStackTrace();
	}
	return modelAndView;
}
@RequestMapping(value="/EfficiencyIndex",method=RequestMethod.GET)
public ModelAndView ApplicationRcvdJewAccept (HttpServletRequest request){
	ModelAndView mv = new ModelAndView();
	
	try{
		HttpSession httpsession = request.getSession(false);
		Session userSession;
		userSession = (Session) httpsession.getAttribute("logged-in");
		if(userSession!=null)
		{	
			
			//code for last day date of previous months
			 /*Calendar calendar = Calendar.getInstance();
		        calendar.add(Calendar.MONTH, -1);

		        int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		        calendar.set(Calendar.DAY_OF_MONTH, max);

		        Date mukul= calendar.getTime();
			System.out.println("mukulmukul:::"+mukul);
			SimpleDateFormat sm1 = new SimpleDateFormat("dd-MM-yyyy");
		
			String strDate1 = sm1.format(mukul);
			System.out.println("mukulmukul:::"+strDate1);
			*/
			
			
			Date referenceDate = new Date();
			Calendar c = Calendar.getInstance(); 
			c.setTime(referenceDate); 
			c.add(Calendar.MONTH, -1);
			Date a= c.getTime();
			System.out.println("dateInString:::"+a);
			
			SimpleDateFormat sm = new SimpleDateFormat("dd-MM-yyyy");
			
			String strDate = sm.format(a);
			
			String[] b = strDate.split("-");
			String mnth=b[1];
			String yr=b[2];
			System.out.println("mnth::::"+mnth);
			System.out.println("mnth::::"+yr);
			
			
			System.out.println("strDate::::"+strDate);
			
			String Month1=mnth;
			if(request.getParameter("year")!=null)
			{
				 Month1=request.getParameter("month");
			}
			String Year1=yr;
			if(request.getParameter("year")!=null)
			{
				 Year1=request.getParameter("year");
			}
			
			
			
	
	     List<HashMap> listhmAcceptedApplications = new ArrayList<HashMap>();
	     String Branch="";
	     int userid = userSession.getUserid();
	     int roleId = Integer.parseInt(userSession.getCurrent_role());
	     List Month=new ArrayList();
	     Month= DGdao.getmonth();
	     System.out.println("months"+Month);
	     
	     List Year=new ArrayList();
	     Year= DGdao.getYear();
	     System.out.println("months"+Year);
	    
	     
	     if(roleId != 7 && roleId != 4 && roleId != 5 && roleId != 6 ){
			
	    	  Branch=DGdao.getAllBranchIds();
	    	  System.out.println("branch for dg:::::::::"+Branch);
		        	  
	     }
	     else if(roleId == 7 ){
				
			 //Branch=DGdao.getAllBranchIds();
			 Branch=DGdao.getBranchIdsbylocationId(String.valueOf(userSession.getLocation_id()));
			 System.out.println("branch for ddgr:::::::::"+Branch);
	     }
	     else {
	     Branch=String.valueOf(userSession.getLocation_id());
	     System.out.println("branch for hod,do:::::::::"+Branch);
	     }
		        	   
	     
	     //Object[] strLoginEmployee = new String[3];
	
	     listhmAcceptedApplications = DGserv.getEfficiencyIndex(userid,roleId,Branch,Month1,Year1);
	     mv.addObject("efficiencyModel", new AllCmlReportModel());
	     mv.setViewName("Efficiency");
	     mv.addObject("applicationAcceptedList", listhmAcceptedApplications);
	     mv.addObject("Month", Month);
	     mv.addObject("Year", Year);
	
		}
	}
	catch(Exception e){
		e.printStackTrace();
	}
	return mv;

}

@RequestMapping(value="/EfficiencyIndexEmp",method=RequestMethod.GET)
public ModelAndView EfficiencyIndexEmp (HttpServletRequest request){
	ModelAndView mv = new ModelAndView();
	
	try{
		HttpSession httpsession = request.getSession(false);
		Session userSession;
		userSession = (Session) httpsession.getAttribute("logged-in");
		if(userSession!=null)
		{	
			
			
			
			
			Date referenceDate = new Date();
			Calendar c = Calendar.getInstance(); 
			c.setTime(referenceDate); 
			c.add(Calendar.MONTH, -1);
			Date a= c.getTime();
			System.out.println("dateInString:::"+a);
			
			SimpleDateFormat sm = new SimpleDateFormat("dd-MM-yyyy");
			
			String strDate = sm.format(a);
			
			String[] b = strDate.split("-");
			String mnth=b[1];
			String yr=b[2];
			System.out.println("mnth::::"+mnth);
			System.out.println("mnth::::"+yr);
			
			
			System.out.println("strDate::::"+strDate);
			
			String Month1=mnth;
			if(request.getParameter("year")!=null)
			{
				 Month1=request.getParameter("month");
			}
			String Year1=yr;
			if(request.getParameter("year")!=null)
			{
				 Year1=request.getParameter("year");
			}
			
			
			
	
	     List<HashMap> listhmAcceptedApplications = new ArrayList<HashMap>();
	     String Branch="";
	     int userid = userSession.getUserid();
	     int roleId = Integer.parseInt(userSession.getCurrent_role());
	     List Month=new ArrayList();
	     Month= DGdao.getmonthEmp();
	     System.out.println("months"+Month);
	     
	     List Year=new ArrayList();
	     Year= DGdao.getYearEmp();
	     System.out.println("months"+Year);
	    
	     
	     if(roleId != 7 && roleId != 4 && roleId != 5 && roleId != 6 ){
			
	    	  Branch=DGdao.getAllBranchIds();
	    	  System.out.println("branch for dg:::::::::"+Branch);
		        	  
	     }
	     else if(roleId == 7 ){
				
			 //Branch=DGdao.getAllBranchIds();
			 Branch=DGdao.getBranchIdsbylocationId(String.valueOf(userSession.getLocation_id()));
			 System.out.println("branch for ddgr:::::::::"+Branch);
	     }
	     else {
	     Branch=String.valueOf(userSession.getLocation_id());
	     System.out.println("branch for hod,do:::::::::"+Branch);
	     }
		        	   
	     
	     //Object[] strLoginEmployee = new String[3];
	
	     listhmAcceptedApplications = DGserv.getEfficiencyIndexEmp(userid,roleId,Branch,Month1,Year1);
	     mv.addObject("efficiencyModel", new AllCmlReportModel());
	     mv.setViewName("EfficiencyEmp");
	     mv.addObject("applicationAcceptedList", listhmAcceptedApplications);
	     mv.addObject("Month", Month);
	     mv.addObject("Year", Year);
	
		}
	}
	catch(Exception e){
		e.printStackTrace();
	}
	return mv;

}




@RequestMapping(value="/EfficiencyIndexEmpTwo",method=RequestMethod.GET)
public ModelAndView EfficiencyIndexEmpTwo (HttpServletRequest request){
	ModelAndView mv = new ModelAndView();
	
	try{
		HttpSession httpsession = request.getSession(false);
		Session userSession;
		userSession = (Session) httpsession.getAttribute("logged-in");
		if(userSession!=null)
		{	
			
			
			
			
			Date referenceDate = new Date();
			Calendar c = Calendar.getInstance(); 
			c.setTime(referenceDate); 
			c.add(Calendar.MONTH, -1);
			Date a= c.getTime();
			System.out.println("dateInString:::"+a);
			
			SimpleDateFormat sm = new SimpleDateFormat("dd-MM-yyyy");
			
			String strDate = sm.format(a);
			
			String[] b = strDate.split("-");
			String mnth=b[1];
			String yr=b[2];
			System.out.println("mnth::::"+mnth);
			System.out.println("mnth::::"+yr);
			
			
			System.out.println("strDate::::"+strDate);
			
			String Month1=mnth;
			if(request.getParameter("year")!=null)
			{
				 Month1=request.getParameter("month");
			}
			String Year1=yr;
			if(request.getParameter("year")!=null)
			{
				 Year1=request.getParameter("year");
			}
			
			
			
	
	     List<HashMap> listhmAcceptedApplications = new ArrayList<HashMap>();
	     String Branch="";
	     int userid = userSession.getUserid();
	     int roleId = Integer.parseInt(userSession.getCurrent_role());
	     List Month=new ArrayList();
	     Month= DGdao.getmonthEmp();
	     System.out.println("months"+Month);
	     
	     List Year=new ArrayList();
	     Year= DGdao.getYearEmp();
	     System.out.println("months"+Year);
	    
	     
	     if(roleId != 7 && roleId != 4 && roleId != 5 && roleId != 6 ){
			
	    	  Branch=DGdao.getAllBranchIds();
	    	  System.out.println("branch for dg:::::::::"+Branch);
		        	  
	     }
	     else if(roleId == 7 ){
				
			 //Branch=DGdao.getAllBranchIds();
			 Branch=DGdao.getBranchIdsbylocationId(String.valueOf(userSession.getLocation_id()));
			 System.out.println("branch for ddgr:::::::::"+Branch);
	     }
	     else {
	     Branch=String.valueOf(userSession.getLocation_id());
	     System.out.println("branch for hod,do:::::::::"+Branch);
	     }
		        	   
	     
	     //Object[] strLoginEmployee = new String[3];
	
	     listhmAcceptedApplications = DGserv.getEfficiencyIndexEmpTwo(userid,roleId,Branch,Month1,Year1);
	     mv.addObject("efficiencyModel", new AllCmlReportModel());
	     mv.setViewName("EfficiencyEmpTwo");
	     mv.addObject("applicationAcceptedList", listhmAcceptedApplications);
	     mv.addObject("Month", Month);
	     mv.addObject("Year", Year);
	
		}
	}
	catch(Exception e){
		e.printStackTrace();
	}
	return mv;

}



@RequestMapping(value = "/DashBoardListingPageReport/{value}/{from_date}/{to_date}/{loc_id}/{selectedBranchName}",method={RequestMethod.GET,RequestMethod.POST})
public ModelAndView getDashBoardListingPageReport(@PathVariable("value") String value,@PathVariable("from_date") String from_date,@PathVariable("to_date") String to_date,@PathVariable("loc_id") String locId, 
		@PathVariable("selectedBranchName") String selBrnchName, @ModelAttribute("model1") AllCmlReportModel cmlModel, HttpServletRequest request,HttpServletResponse response)
{
	ModelAndView mv = new ModelAndView();
	try{
	String json ="";
	
	HttpSession httpsession = request.getSession(false);
	Session userSession;
	userSession = (Session) httpsession.getAttribute("logged-in");
	value=String.valueOf(value);
	selBrnchName =crypt.Dcrypt(selBrnchName);
	
	from_date=crypt.Dcrypt(from_date);
	to_date=crypt.Dcrypt(to_date);
	locId=crypt.Dcrypt(locId);
	String locationId="";
	
	System.out.println("all parameters"+from_date+to_date+locId);
	if(locId.equalsIgnoreCase("All")) {
		locationId=DGdao.getAllBranchIds();
	}
	else{
		locationId=locId;
	}
	Gson gson = new Gson();
	
	

   
   
	
	if (userSession != null) {
	String StageName=bisReportService.getStageName(value);
	String ListQuery=bisReportService.getBisReportListQuery(value);
	System.out.println("ListQuery>>>>>>>>>>>>>"+ListQuery);
	List<Map<String,Object>> bisReportQueryListModel=bisReportService.getBisListQueryResult(ListQuery,from_date,to_date,locationId);
	request.setAttribute("BRLQL", bisReportQueryListModel);
	
	json = gson.toJson(bisReportQueryListModel);
	request.setAttribute("BRLQLjson",json);
	request.setAttribute("StageName", StageName);
	request.setAttribute("value", value);
	/*request.setAttribute("toDate", Date);
	request.setAttribute("fromDate", startdateofMonth);*/
	request.setAttribute("locationId", locationId);
	request.setAttribute("selBrnchName", selBrnchName.replaceAll("_", " "));
	mv.setViewName("dashboardReportListQueryPageReports");
	}
	else
	{
		
	
	
	
	
	if(Integer.parseInt(value)>=1000)
	{   
		String StageName=bisReportService.getStageName(value);
		String ListQuery=bisReportService.getBisReportListQuery(value);
		List<Map<String,Object>> bisReportQueryListModel=bisReportService.getBisListQueryResult(ListQuery,from_date,to_date,locationId);
		request.setAttribute("BRLQL", bisReportQueryListModel);
		
		json = gson.toJson(bisReportQueryListModel);
		request.setAttribute("BRLQLjson",json);
		request.setAttribute("StageName", StageName);
		request.setAttribute("value", value);
		/*request.setAttribute("toDate", Date);
		request.setAttribute("fromDate", startdateofMonth);*/
		request.setAttribute("locationId", locationId);
		request.setAttribute("selBrnchName", selBrnchName.replaceAll("_", " "));
		mv.setViewName("dashboardReportListQueryPage");
	}
	else
	{
		mv.setViewName("sessionExpire");
	}
}
}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	return mv;
}
@RequestMapping(value = "/getBranchDtls1", method = RequestMethod.GET)
public @ResponseBody List getBranchDtls1(HttpServletRequest request)
{
	String regionID=request.getParameter("regionId");

	List<HashMap<String,String>> branchIds = new ArrayList<HashMap<String,String>>();
	branchIds = DGserv.getBranch1(regionID);
	System.out.println("branchIds::::"+branchIds);
	request.setAttribute("branchIdList", branchIds);
	return branchIds;
}


}