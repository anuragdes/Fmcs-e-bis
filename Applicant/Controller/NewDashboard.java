package Applicant.Controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import Applicant.DAO.NewDGDashboardDAO;
import Applicant.Model.AllCmlReportModel;
import Applicant.Service.NewDGdashboardservice;
import Global.Login.Model.Session;
import Global.Login.Service.IMigrateService;
import Schemes.ProductCertification.reports.Service.BisReportService;

@Controller
public class NewDashboard {

	@Autowired
	NewDGDashboardDAO DGdao;
	
	@Autowired
	NewDGdashboardservice DGserv;

	@Autowired
	NewDGDashboardDAO inserDAO;
	
	@Autowired
	IMigrateService crypt; 
	
	@Autowired
	BisReportService bisReportService;
	
//	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
//	private static final SimpleDateFormat parseDateFormat = new SimpleDateFormat("dd/MM/yyyy");

//	private static final int Apr = 0;
	@RequestMapping(value = "/DashboardNew", method = RequestMethod.GET)
	public ModelAndView DGDashboard(@ModelAttribute("model1") AllCmlReportModel cmlModel, HttpServletRequest request)
	{
	    ModelAndView modelAndView = new ModelAndView();
//        String regionId1 = "";

	    try
	    {
	        HttpSession httpsession = request.getSession(false);
	        Session userSession;
	        userSession = (Session) httpsession.getAttribute("logged-in");
	        if(userSession != null)
	        {
	            int roleId = Integer.parseInt(userSession.getCurrent_role());
	            if(roleId != 3)
	            {
	                modelAndView.addObject("roleId", roleId);

	                int locationid = userSession.getLocation_id();
	                modelAndView.addObject("locationid", locationid);
	                Map<String,String> resultList=new HashMap<String,String>();

	                int locationtype = userSession.getLocation_type();
	                modelAndView.addObject("locationtype", locationtype);

//	                String locId = "";
//	                List<HashMap<String, String>> branchIds = new ArrayList<HashMap<String, String>>();
	                String fromDate = cmlModel.getFromDt();
	                String toDate = cmlModel.getToDt();
	                String selectedBranchId = cmlModel.getBranchId();
	                String regionId = cmlModel.getRegionId();
	                modelAndView.addObject("branchIdList", selectedBranchId);
	                String reg_id = "" + userSession.getLocation_id();
	                if(regionId != null)
	                {
	                    modelAndView.addObject("regionId1", regionId);
	                }
	                else
	                {
	                    modelAndView.addObject("regionId1", 0);
	                }

	                modelAndView.addObject("branch_id", reg_id);
	                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
	                if(cmlModel.getFromDt() == null && cmlModel.getToDt() == null )
	                {
	                	fromDate = dtf.format(LocalDate.now().withDayOfMonth(1));
	                	toDate= dtf.format(LocalDate.now());
	                	modelAndView.addObject("fromDate", fromDate);
	                    modelAndView.addObject("toDate", toDate);
	                }
	                else
	                {
	                    	LocalDate fromdate1 = LocalDate.parse(fromDate, dtf);
	                        fromDate = dtf.format(fromdate1);
	                        LocalDate todate1 = LocalDate.parse(toDate, dtf);
	                        toDate = dtf.format(todate1);
	                }

	                if(roleId != 7 && roleId != 4 && roleId != 5 )
	                {
	                    System.out.println("inside this");
	                    if(regionId == null && selectedBranchId == null || regionId.equals("All") && selectedBranchId.equals("All"))
	                    {
	                        selectedBranchId = DGserv.getAllBranchIds();
	                        modelAndView.addObject("selectedBranchId11", crypt.Jcrypt("All"));
	                    }
	                    else if(!regionId.equals("All") && selectedBranchId.equals("All")  )
	                    {
	                        selectedBranchId = DGserv.getBranchIdsbylocationId(regionId);
	                        modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
	                    }
	                    else
	                    {
	                        modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
	                    }
	                }
	                else if(roleId == 7)
	                {
	                    if(regionId == null && selectedBranchId == null || regionId == null && selectedBranchId.equals("All")  )
	                    {
	                        selectedBranchId = DGserv.getBranchIdsbylocationId(String.valueOf(userSession.getLocation_id()));
	                        modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
	                    }
	                    else
	                    {
	                        modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
	                    }

	                }
	                else
	                {
	                    selectedBranchId = String.valueOf(userSession.getLocation_id());
	                    modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(String.valueOf(userSession.getLocation_id())));
	                }

//	                if(regionId == null)
//	                {
//	                    regionId1 = "All";
//	                }
//	                else
//	                {
//	                    regionId1 = regionId;
//	                }

	                
	                resultList =  DGserv.gettotalnooflicences(selectedBranchId,fromDate,toDate);
	             
	                modelAndView.addObject("SelectedRegionid", regionId);
	                modelAndView.addObject("standards_man", resultList.get("standards_man"));
	                modelAndView.addObject("standards_vol", resultList.get("standards_vol"));
	                modelAndView.addObject("operative_licenses_total", resultList.get("operative_licenses_total"));
	                modelAndView.addObject("operative_licenses_period", resultList.get("operative_licenses_period"));
	                modelAndView.addObject("applications_underprocess", resultList.get("applications_underprocess"));
	                modelAndView.addObject("applications_received", resultList.get("applications_received"));
	                modelAndView.addObject("all_india_first_underprocess", resultList.get("all_india_first_underprocess"));
	                modelAndView.addObject("all_india_first_period", resultList.get("all_india_first_period"));
	                modelAndView.addObject("income_fy", resultList.get("income_fy"));
	                modelAndView.addObject("income_period", resultList.get("income_period"));
	                modelAndView.addObject("fromDate1", crypt.Jcrypt(fromDate));
	                modelAndView.addObject("toDate1", crypt.Jcrypt(toDate));
	                modelAndView.addObject("selectedBranchId1", crypt.Jcrypt(selectedBranchId));
	                modelAndView.addObject("selBranchName", crypt.Jcrypt("Select Branch"));
	                modelAndView.addObject("selectedBranchId", selectedBranchId);
	                modelAndView.setViewName("DashboardNew");

	            }
	            else
	            {
	                modelAndView.setViewName("AccessDenied");
	            }
	        }
	        else
	        {

	            modelAndView.setViewName("sessionExpire");
	        }


	    }
	    catch (Exception e)
	    {
	        e.printStackTrace();
	    }
	    return modelAndView;
	}
	
	
	@RequestMapping(value = "/dataforrow2", method = RequestMethod.POST)
	public @ResponseBody  List<String> divdata2(HttpServletRequest request) throws IOException, InterruptedException, ExecutionException{

	List<String> listofdiv2 = new ArrayList<String>();
	String fromDate=crypt.Dcrypt(request.getParameter("fromDate1"));
	String toDate=crypt.Dcrypt(request.getParameter("toDate1"));
	String selectedBranchId=crypt.Dcrypt(request.getParameter("selectedBranchId1"));
	Map<String,String> resultList=new HashMap<String,String>();
	resultList =  DGserv.getCountforRowTwo(selectedBranchId,fromDate,toDate);
	listofdiv2.add(resultList.get("renewals_period"));
	listofdiv2.add(resultList.get("renewals_due"));
	listofdiv2.add(resultList.get("inclusions_under_process"));
	listofdiv2.add(resultList.get("inclusions_granted"));
	listofdiv2.add(resultList.get("deferments_total"));
	listofdiv2.add(resultList.get("deferments_90days"));
	listofdiv2.add(resultList.get("expired_fy"));
	listofdiv2.add(resultList.get("expired_due"));
	listofdiv2.add(resultList.get("suspensions"));
	listofdiv2.add(resultList.get("suspensions_180"));
	System.out.println("In Dic 2"+listofdiv2);
	return listofdiv2;
}

	@RequestMapping(value = "/dataforrow3", method = RequestMethod.POST)
	public @ResponseBody  List<String> divdata3(HttpServletRequest request) throws IOException, InterruptedException, ExecutionException{

		List<String> listofdiv3 = new ArrayList<String>();

		String fromDate=crypt.Dcrypt(request.getParameter("fromDate1"));
		String toDate=crypt.Dcrypt(request.getParameter("toDate1"));
		String selectedBranchId=crypt.Dcrypt(request.getParameter("selectedBranchId1"));
		Map<String,String> resultList=new HashMap<String,String>();
		resultList =  DGserv.getCountforRowThree(selectedBranchId,fromDate,toDate);
		listofdiv3.add(resultList.get("factory_surveillances_co_assign"));
		listofdiv3.add(resultList.get("factory_surveillances_co_completed"));
		listofdiv3.add(resultList.get("factory_surveillances_osa_assign"));
		listofdiv3.add(resultList.get("factory_surveillances_osa_completed"));
		listofdiv3.add(resultList.get("factory_surveillances_spl_assign"));
		listofdiv3.add(resultList.get("factory_surveillances_spl_completed"));
		listofdiv3.add(resultList.get("cancelled_licenses_total"));
		listofdiv3.add(resultList.get("cancelled_licenses_due"));
		listofdiv3.add(resultList.get("pre_application_tr_rec"));
		listofdiv3.add(resultList.get("pre_application_tr_dis"));
		System.out.println("In Dic 3"+listofdiv3);
		return listofdiv3;
}

	@RequestMapping(value = "/dataforrow4", method = RequestMethod.POST)
	public @ResponseBody  List<String> divdata4(HttpServletRequest request) throws IOException, InterruptedException, ExecutionException{
		List<String> listofdiv4 = new ArrayList<String>();
		String fromDate=crypt.Dcrypt(request.getParameter("fromDate1"));
		String toDate=crypt.Dcrypt(request.getParameter("toDate1"));
		String selectedBranchId=crypt.Dcrypt(request.getParameter("selectedBranchId1"));
		Map<String,String> resultList=new HashMap<String,String>();
		resultList =  DGserv.getCountforRowFour(selectedBranchId,fromDate,toDate);
		listofdiv4.add(resultList.get("lot_insp_assign"));
		listofdiv4.add(resultList.get("lot_insp_completed"));
		listofdiv4.add(resultList.get("sample_sent_Market"));
		listofdiv4.add(resultList.get("sample_sent_factory"));
		listofdiv4.add(resultList.get("spl_sample_sent_Market"));
		listofdiv4.add(resultList.get("spl_sample_sent_factory"));
		listofdiv4.add(resultList.get("market_surveillances_co_assign"));
		listofdiv4.add(resultList.get("market_surveillances_co_completed"));
		listofdiv4.add(resultList.get("test_report_awaited"));
		listofdiv4.add(resultList.get("test_report_uploaded"));
		System.out.println("In Dic 4"+listofdiv4);
		return listofdiv4;
}
	
	@RequestMapping(value = "/dataforrow5", method = RequestMethod.POST)
	public @ResponseBody  List<String> divdata5(HttpServletRequest request) throws IOException, InterruptedException, ExecutionException{
	List<String> listofdiv5 = new ArrayList<String>();
	String fromDate=crypt.Dcrypt(request.getParameter("fromDate1"));
	String toDate=crypt.Dcrypt(request.getParameter("toDate1"));
	String selectedBranchId=crypt.Dcrypt(request.getParameter("selectedBranchId1"));
	Map<String,String> resultList=new HashMap<String,String>();
	resultList =  DGserv.getCountforRowFive(selectedBranchId,fromDate,toDate);
	listofdiv5.add(resultList.get("articles_received_hallmarking"));
	listofdiv5.add(resultList.get("articles_received_hallmarked"));
	listofdiv5.add(resultList.get("rop_under_process"));
	listofdiv5.add(resultList.get("rop_due"));
	System.out.println("In Dic 5"+listofdiv5);
	return listofdiv5;
}


//@RequestMapping(value = "/getBranchDtls", method = RequestMethod.GET)
//public @ResponseBody List getBranchDtls(HttpServletRequest request)
//{
//	List<HashMap<String,String>> branchIds = new ArrayList<HashMap<String,String>>();
//	if(request.getParameter("regionId").equals("ALL"))
//	{
//		branchIds=DGserv.getBranch(request.getParameter("5,8,7,9,6"));
//	}
//	else
//	{
//	branchIds = DGserv.getBranch(request.getParameter("regionId"));
//	}
//	return branchIds;
//}

}