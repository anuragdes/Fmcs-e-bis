package Applicant.Controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import com.google.gson.Gson;
import Applicant.DAO.DGdashboardDAO;
import Applicant.Service.DGdashboardservice;
import Applicant.Model.AllCmlReportModel;
import Global.Login.Model.Session;
import Global.Login.Service.IMigrateService;

import Schemes.ProductCertification.reports.Service.BisReportService;


@Controller
/* @Component("dgdashchedulerBean") */
public class DG_ReportsNew {

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

//-------------------------------------------------------------//
//---------------ALL INDIA FIRST APLLICATION-------------------//
//-------------------------------------------------------------//
	
	@RequestMapping(value = "/DG_reports1", method = RequestMethod.GET)
	public ModelAndView DG_reports1(@ModelAttribute("model1") AllCmlReportModel cmlModel, HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView();

		try {
			HttpSession httpsession = request.getSession(false);
			Session userSession;
			userSession = (Session) httpsession.getAttribute("logged-in");
			if (userSession != null) {
				int roleId = Integer.parseInt(userSession.getCurrent_role());
				if (roleId != 3) {
					//String value = "505";
					String currentId="1";
					String parentId="0";
					modelAndView.addObject("roleId", roleId);
            
					int locationid = userSession.getLocation_id();
					modelAndView.addObject("locationid", locationid);
					System.out.println("locationid:::::::" + locationid);

					int locationtype = userSession.getLocation_type();
					modelAndView.addObject("locationtype", locationtype);
					System.out.println("locationtype:::::::" + locationtype);
					String locId = "";
					List<HashMap<String, String>> branchIds = new ArrayList<HashMap<String, String>>();

					String fromDate = cmlModel.getFromDt();
					String toDate = cmlModel.getToDt();
					String selectedBranchId = cmlModel.getBranchId();
					String regionId = cmlModel.getRegionId();
					modelAndView.addObject("branchIdList", selectedBranchId);
					String reg_id = "" + userSession.getLocation_id();
					if (regionId != null) {
						modelAndView.addObject("regionId1", regionId);
					} else {
						modelAndView.addObject("regionId1", 0);
					}

					modelAndView.addObject("branch_id", reg_id);
					System.out.println("branch id is " + selectedBranchId + "regionid" + regionId);

					SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
					String Date = formatter.format(new Date());// this is present date

					Calendar c = Calendar.getInstance();
					c.set(Calendar.DAY_OF_MONTH, 1);
					String startdateofMonth = formatter.format(c.getTime());
					System.out.println("start date is " + startdateofMonth);// Month ka start date hai
					
					if (cmlModel.getFromDt() == null && cmlModel.getToDt() == null) {
						System.out.println("Inside date is null");
						System.out.println("::::::::::::::::a::" + cmlModel.getFromDt());
						System.out.println("::::::::::::::::b::" + cmlModel.getToDt());
						fromDate = startdateofMonth;
						toDate = Date;
						modelAndView.addObject("fromDate", fromDate);
						modelAndView.addObject("toDate", toDate);
						System.out.println("fromDate date is " + fromDate);
						System.out.println("toDate date is " + toDate);
					} else {
						try {
							Date fromdate1 = parseDateFormat.parse(fromDate);
							fromDate = sdf.format(fromdate1);
							System.out.println("fromDate" + fromDate);
							Date todate1 = parseDateFormat.parse(toDate);
							toDate = sdf.format(todate1);
							System.out.println("toDate" + toDate);

						} catch (ParseException e) {
							e.printStackTrace();
						}
					}

					if (roleId != 7 && roleId != 4 && roleId != 5) {
						System.out.println("inside this");
						if (regionId == null && selectedBranchId == null
								|| regionId.equals("All") && selectedBranchId.equals("All")) {
							selectedBranchId = DGdao.getAllBranchIds();
							System.out.println("all selected branch id's are " + selectedBranchId);
							modelAndView.addObject("selectedBranchId11", crypt.Jcrypt("All"));
						} else if (!regionId.equals("All") && selectedBranchId.equals("All")) {
							System.out.println("selectedBranchId123" + selectedBranchId);
							selectedBranchId = DGdao.getBranchIdsbylocationId(regionId);
							System.out.println("selectedBranchId" + selectedBranchId);
							modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
						}

						else {

							modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
						}

					} else if (roleId == 7) {
						if (regionId == null && selectedBranchId == null
								|| regionId == null && selectedBranchId.equals("All")) {

							selectedBranchId = DGdao
									.getBranchIdsbylocationId(String.valueOf(userSession.getLocation_id()));
							System.out.println("selectedBranchId" + selectedBranchId);
							modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
						} else {
							modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
						}
					} else {
						System.out.println("inside else");
						selectedBranchId = String.valueOf(userSession.getLocation_id());
						modelAndView.addObject("selectedBranchId11",
								crypt.Jcrypt(String.valueOf(userSession.getLocation_id())));
					}
					String StageName = DGserv.getStageNameDgreportsAIF(parentId,currentId);

					String ListQuery = DGserv.getBisReportListQueryDGreportsAIF(parentId,currentId);

					List<Map<String, Object>> bisReportQueryListModel = bisReportService
							.getBisListQueryResult(ListQuery, fromDate, toDate, selectedBranchId);
					request.setAttribute("BRLQL", bisReportQueryListModel);
					request.setAttribute("StageName", StageName);
					request.setAttribute("value", currentId);
					request.setAttribute("locationId", selectedBranchId);
					request.setAttribute("selBrnchName", selectedBranchId.replaceAll("_", " "));
					System.out.println("selectedBranchId::::::::::::..." + selectedBranchId);
					modelAndView.addObject("fromDate1", crypt.Jcrypt(fromDate));
					modelAndView.addObject("toDate1", crypt.Jcrypt(toDate));
					modelAndView.addObject("selectedBranchId1", crypt.Jcrypt(selectedBranchId));
					modelAndView.addObject("selBranchName", crypt.Jcrypt("Select Branch"));
					modelAndView.addObject("selectedBranchId", selectedBranchId);
					modelAndView.setViewName("AIFREPORT");
				} else {
					modelAndView.setViewName("AccessDenied");
				}
			} else {

				modelAndView.setViewName("sessionExpire");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return modelAndView;
	}
	
	
	@RequestMapping(value = "/DgReportAIFStatus/{branch_id}/{parentId}/{currntid}/{from_date}/{to_date}/{loc_id}/{selectedBranchName}",method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView getDgReportList(@PathVariable("branch_id") String branchid,@PathVariable("parentId") String parentId,@PathVariable("currntid") String currntid,@PathVariable("from_date") String from_date,@PathVariable("to_date") String to_date,@PathVariable("loc_id") String locId, 
			@PathVariable("selectedBranchName") String selBrnchName,@ModelAttribute("model1") AllCmlReportModel cmlModel,HttpServletRequest request,HttpServletResponse response)
	{
		ModelAndView mv = new ModelAndView();
		System.out.println(branchid);
		try{
		String json ="";
		HttpSession httpsession = request.getSession(false);
		Session userSession;
		userSession = (Session) httpsession.getAttribute("logged-in");
		parentId=String.valueOf(parentId);
		currntid=String.valueOf(currntid);
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
		String StageName=DGserv.getStageNameDgreportsAIF(parentId,currntid);
		String ListQuery=DGserv.getBisReportListQueryDGreportsAIF(parentId,currntid);
		System.out.println("ListQuery>>>>>>>>>>>>>"+ListQuery);
		List<Map<String,Object>> bisReportQueryListModel=DGserv.getBisListQueryResultAIF(ListQuery,from_date,branchid,to_date,locationId);
		request.setAttribute("BRLQL", bisReportQueryListModel);
		json = gson.toJson(bisReportQueryListModel);
		request.setAttribute("BRLQLjson",json);
		request.setAttribute("StageName", StageName);
		request.setAttribute("value", currntid);
		request.setAttribute("locationId", locationId);
		request.setAttribute("selBrnchName", selBrnchName.replaceAll("_", " "));
				
		if(parentId.equals("1002"))
		{
			mv.setViewName("dashboardReportListQueryPage1");
		}
		
		/*
		 * else if(parentId.equals("89")||parentId.equals("96")) {
		 * mv.setViewName("dashboardReportListQueryPage2"); }
		 */
		else
		{
			mv.setViewName("dashboardReportListQueryPage");
		}
		}
		else
		{
		if(Integer.parseInt(parentId)>=1000)
		{   
			String StageName=DGserv.getStageNameDgreportsAIF(parentId,currntid);
			String ListQuery=DGserv.getBisReportListQueryDGreportsAIF(parentId,currntid);
			List<Map<String,Object>> bisReportQueryListModel=bisReportService.getBisListQueryResult(ListQuery,from_date,to_date,locationId);
			request.setAttribute("BRLQL", bisReportQueryListModel);
			json = gson.toJson(bisReportQueryListModel);
			request.setAttribute("BRLQLjson",json); 
			request.setAttribute("StageName", StageName);
			request.setAttribute("value", currntid);
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
	
	//-------------------------------------------------------------//
	//-----------ALL INDIA FIRST APLLICATION PENDING---------------//
	//-------------------------------------------------------------//
	
	
	@RequestMapping(value = "/DG_reportAIFPending", method = RequestMethod.GET)
	public ModelAndView DG_reportAIFPending(@ModelAttribute("model1") AllCmlReportModel cmlModel, HttpServletRequest request) {

		ModelAndView modelAndView = new ModelAndView();

		try {
			HttpSession httpsession = request.getSession(false);
			Session userSession;
			userSession = (Session) httpsession.getAttribute("logged-in");
			if (userSession != null) {
				int roleId = Integer.parseInt(userSession.getCurrent_role());
				if (roleId != 3) {
					String currentId="8";
					String parentId="0";
					
					modelAndView.addObject("roleId", roleId);
					int locationid = userSession.getLocation_id();
					modelAndView.addObject("locationid", locationid);
					System.out.println("locationid:::::::" + locationid);
					int locationtype = userSession.getLocation_type();
					modelAndView.addObject("locationtype", locationtype);
					System.out.println("locationtype:::::::" + locationtype);
					String locId = "";
					List<HashMap<String, String>> branchIds = new ArrayList<HashMap<String, String>>();

					String fromDate = cmlModel.getFromDt();
					String toDate = cmlModel.getToDt();
					String selectedBranchId = cmlModel.getBranchId();
					String regionId = cmlModel.getRegionId();
					modelAndView.addObject("branchIdList", selectedBranchId);
					String reg_id = "" + userSession.getLocation_id();
					if (regionId != null) {
						modelAndView.addObject("regionId1", regionId);
					} else {
						modelAndView.addObject("regionId1", 0);
					}

					modelAndView.addObject("branch_id", reg_id);
					System.out.println("branch id is " + selectedBranchId + "regionid" + regionId);

					SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
					String Date = formatter.format(new Date());// this is present date

					Calendar c = Calendar.getInstance();
					c.set(Calendar.DAY_OF_MONTH, 1);
					String startdateofMonth = formatter.format(c.getTime());
					System.out.println("start date is " + startdateofMonth);// Month ka start date hai
					

					if (cmlModel.getFromDt() == null && cmlModel.getToDt() == null) {
						System.out.println("Inside date is null");
						System.out.println("::::::::::::::::a::" + cmlModel.getFromDt());
						System.out.println("::::::::::::::::b::" + cmlModel.getToDt());
						fromDate = startdateofMonth;
						toDate = Date;
						modelAndView.addObject("fromDate", fromDate);
						modelAndView.addObject("toDate", toDate);
						System.out.println("fromDate date is " + fromDate);
						System.out.println("toDate date is " + toDate);
					} else {
						try {
							Date fromdate1 = parseDateFormat.parse(fromDate);
							fromDate = sdf.format(fromdate1);
							System.out.println("fromDate" + fromDate);
							Date todate1 = parseDateFormat.parse(toDate);
							toDate = sdf.format(todate1);
							System.out.println("toDate" + toDate);

						} catch (ParseException e) {
							e.printStackTrace();
						}
					}

					if (roleId != 7 && roleId != 4 && roleId != 5) {
						System.out.println("inside this");
						if (regionId == null && selectedBranchId == null
								|| regionId.equals("All") && selectedBranchId.equals("All")) {
							selectedBranchId = DGdao.getAllBranchIds();
							System.out.println("all selected branch id's are " + selectedBranchId);
							modelAndView.addObject("selectedBranchId11", crypt.Jcrypt("All"));
						} else if (!regionId.equals("All") && selectedBranchId.equals("All")) {
							System.out.println("selectedBranchId123" + selectedBranchId);
							selectedBranchId = DGdao.getBranchIdsbylocationId(regionId);
							System.out.println("selectedBranchId" + selectedBranchId);
							modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
						}

						else {

							modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
						}

					} else if (roleId == 7) {
						if (regionId == null && selectedBranchId == null
								|| regionId == null && selectedBranchId.equals("All")) {

							selectedBranchId = DGdao
									.getBranchIdsbylocationId(String.valueOf(userSession.getLocation_id()));
							System.out.println("selectedBranchId" + selectedBranchId);
							modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
						} else {
							modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
						}
					} else {
						System.out.println("inside else");
						selectedBranchId = String.valueOf(userSession.getLocation_id());
						modelAndView.addObject("selectedBranchId11",
								crypt.Jcrypt(String.valueOf(userSession.getLocation_id())));
					}
					String StageName = DGserv.getStageNameDgreportsAIF(parentId,currentId);

					String ListQuery = DGserv.getBisReportListQueryDGreportsAIF(parentId,currentId);

					List<Map<String, Object>> bisReportQueryListModel = bisReportService
							.getBisListQueryResult(ListQuery, fromDate, toDate, selectedBranchId);
					request.setAttribute("BRLQL", bisReportQueryListModel);

					request.setAttribute("StageName", StageName);
					request.setAttribute("value", currentId);
					request.setAttribute("locationId", selectedBranchId);
					request.setAttribute("selBrnchName", selectedBranchId.replaceAll("_", " "));

					System.out.println("selectedBranchId::::::::::::..." + selectedBranchId);

					modelAndView.addObject("fromDate1", crypt.Jcrypt(fromDate));
					modelAndView.addObject("toDate1", crypt.Jcrypt(toDate));
					modelAndView.addObject("selectedBranchId1", crypt.Jcrypt(selectedBranchId));
					modelAndView.addObject("selBranchName", crypt.Jcrypt("Select Branch"));
					modelAndView.addObject("selectedBranchId", selectedBranchId);

					modelAndView.setViewName("AIFREPORTPending");

					

				} else {
					modelAndView.setViewName("AccessDenied");
				}
			} else {

				modelAndView.setViewName("sessionExpire");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return modelAndView;
	}

	     //-------------------------------------------------------------//
		 //------Status of applications under simplified procedure------//
		 //-------------------------------------------------------------//
		
		
		@RequestMapping(value = "/DG_reportAppSimpPPro", method = RequestMethod.GET)
		public ModelAndView DG_reportAppSimpPPro(@ModelAttribute("model1") AllCmlReportModel cmlModel, HttpServletRequest request) {

			ModelAndView modelAndView = new ModelAndView();

			try {
				HttpSession httpsession = request.getSession(false);
				Session userSession;
				userSession = (Session) httpsession.getAttribute("logged-in");
				if (userSession != null) {
					int roleId = Integer.parseInt(userSession.getCurrent_role());
					if (roleId != 3) {
						String currentId="15";
						String parentId="0";
						
						modelAndView.addObject("roleId", roleId);

						int locationid = userSession.getLocation_id();
						modelAndView.addObject("locationid", locationid);
						System.out.println("locationid:::::::" + locationid);

						int locationtype = userSession.getLocation_type();
						modelAndView.addObject("locationtype", locationtype);
						System.out.println("locationtype:::::::" + locationtype);
						String locId = "";
						List<HashMap<String, String>> branchIds = new ArrayList<HashMap<String, String>>();

						String fromDate = cmlModel.getFromDt();
						String toDate = cmlModel.getToDt();
						String selectedBranchId = cmlModel.getBranchId();
						String regionId = cmlModel.getRegionId();
						modelAndView.addObject("branchIdList", selectedBranchId);
						String reg_id = "" + userSession.getLocation_id();
						if (regionId != null) {
							modelAndView.addObject("regionId1", regionId);
						} else {
							modelAndView.addObject("regionId1", 0);
						}

						modelAndView.addObject("branch_id", reg_id);
						System.out.println("branch id is " + selectedBranchId + "regionid" + regionId);

						SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
						String Date = formatter.format(new Date());// this is present date

						Calendar c = Calendar.getInstance();
						c.set(Calendar.DAY_OF_MONTH, 1);
						String startdateofMonth = formatter.format(c.getTime());
						System.out.println("start date is " + startdateofMonth);// Month ka start date hai
						

						if (cmlModel.getFromDt() == null && cmlModel.getToDt() == null) {
							System.out.println("Inside date is null");
							System.out.println("::::::::::::::::a::" + cmlModel.getFromDt());
							System.out.println("::::::::::::::::b::" + cmlModel.getToDt());
							fromDate = startdateofMonth;
							toDate = Date;
							modelAndView.addObject("fromDate", fromDate);
							modelAndView.addObject("toDate", toDate);
							System.out.println("fromDate date is " + fromDate);
							System.out.println("toDate date is " + toDate);
						} else {
							try {
								Date fromdate1 = parseDateFormat.parse(fromDate);
								fromDate = sdf.format(fromdate1);
								System.out.println("fromDate" + fromDate);
								Date todate1 = parseDateFormat.parse(toDate);
								toDate = sdf.format(todate1);
								System.out.println("toDate" + toDate);

							} catch (ParseException e) {
								e.printStackTrace();
							}
						}

						if (roleId != 7 && roleId != 4 && roleId != 5) {
							System.out.println("inside this");
							if (regionId == null && selectedBranchId == null
									|| regionId.equals("All") && selectedBranchId.equals("All")) {
								selectedBranchId = DGdao.getAllBranchIds();
								System.out.println("all selected branch id's are " + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt("All"));
							} else if (!regionId.equals("All") && selectedBranchId.equals("All")) {
								System.out.println("selectedBranchId123" + selectedBranchId);
								selectedBranchId = DGdao.getBranchIdsbylocationId(regionId);
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

							else {

								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

						} else if (roleId == 7) {
							if (regionId == null && selectedBranchId == null
									|| regionId == null && selectedBranchId.equals("All")) {

								selectedBranchId = DGdao
										.getBranchIdsbylocationId(String.valueOf(userSession.getLocation_id()));
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							} else {
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}
						} else {
							System.out.println("inside else");
							selectedBranchId = String.valueOf(userSession.getLocation_id());
							modelAndView.addObject("selectedBranchId11",
									crypt.Jcrypt(String.valueOf(userSession.getLocation_id())));
						}
						String StageName = DGserv.getStageNameDgreportsAIF(parentId,currentId);

						String ListQuery = DGserv.getBisReportListQueryDGreportsAIF(parentId,currentId);

						List<Map<String, Object>> bisReportQueryListModel = bisReportService
								.getBisListQueryResult(ListQuery, fromDate, toDate, selectedBranchId);
						request.setAttribute("BRLQL", bisReportQueryListModel);

						request.setAttribute("StageName", StageName);
						request.setAttribute("value", currentId);
						request.setAttribute("locationId", selectedBranchId);
						request.setAttribute("selBrnchName", selectedBranchId.replaceAll("_", " "));

						System.out.println("selectedBranchId::::::::::::..." + selectedBranchId);

						modelAndView.addObject("fromDate1", crypt.Jcrypt(fromDate));
						modelAndView.addObject("toDate1", crypt.Jcrypt(toDate));
						modelAndView.addObject("selectedBranchId1", crypt.Jcrypt(selectedBranchId));
						modelAndView.addObject("selBranchName", crypt.Jcrypt("Select Branch"));
						modelAndView.addObject("selectedBranchId", selectedBranchId);

						modelAndView.setViewName("ApplicationSimpPROCEDURE");

						

					} else {
						modelAndView.setViewName("AccessDenied");
					}
				} else {

					modelAndView.setViewName("sessionExpire");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return modelAndView;
		}
		 //-------------------------------------------------------------//
		 //---------------Consignee Report(Last Updated)----------------//
		 //-------------------------------------------------------------//
		
		@RequestMapping(value = "/DG_reportAppSimpPProPending", method = RequestMethod.GET)
		public ModelAndView DG_reportAppSimpPProPending(@ModelAttribute("model1") AllCmlReportModel cmlModel, HttpServletRequest request) {

			ModelAndView modelAndView = new ModelAndView();

			try {
				HttpSession httpsession = request.getSession(false);
				Session userSession;
				userSession = (Session) httpsession.getAttribute("logged-in");
				if (userSession != null) {
					int roleId = Integer.parseInt(userSession.getCurrent_role());
					if (roleId != 3) {
						String currentId="28";
						String parentId="0";
						
						modelAndView.addObject("roleId", roleId);

						int locationid = userSession.getLocation_id();
						modelAndView.addObject("locationid", locationid);
						System.out.println("locationid:::::::" + locationid);

						int locationtype = userSession.getLocation_type();
						modelAndView.addObject("locationtype", locationtype);
						System.out.println("locationtype:::::::" + locationtype);
						String locId = "";
						List<HashMap<String, String>> branchIds = new ArrayList<HashMap<String, String>>();

						String fromDate = cmlModel.getFromDt();
						String toDate = cmlModel.getToDt();
						String selectedBranchId = cmlModel.getBranchId();
						String regionId = cmlModel.getRegionId();
						modelAndView.addObject("branchIdList", selectedBranchId);
						String reg_id = "" + userSession.getLocation_id();
						if (regionId != null) {
							modelAndView.addObject("regionId1", regionId);
						} else {
							modelAndView.addObject("regionId1", 0);
						}

						modelAndView.addObject("branch_id", reg_id);
						System.out.println("branch id is " + selectedBranchId + "regionid" + regionId);

						SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
						String Date = formatter.format(new Date());// this is present date

						Calendar c = Calendar.getInstance();
						c.set(Calendar.DAY_OF_MONTH, 1);
						String startdateofMonth = formatter.format(c.getTime());
						System.out.println("start date is " + startdateofMonth);// Month ka start date hai
						

						if (cmlModel.getFromDt() == null && cmlModel.getToDt() == null) {
							System.out.println("Inside date is null");
							System.out.println("::::::::::::::::a::" + cmlModel.getFromDt());
							System.out.println("::::::::::::::::b::" + cmlModel.getToDt());
							fromDate = startdateofMonth;
							toDate = Date;
							modelAndView.addObject("fromDate", fromDate);
							modelAndView.addObject("toDate", toDate);
							System.out.println("fromDate date is " + fromDate);
							System.out.println("toDate date is " + toDate);
						} else {
							try {
								Date fromdate1 = parseDateFormat.parse(fromDate);
								fromDate = sdf.format(fromdate1);
								System.out.println("fromDate" + fromDate);
								Date todate1 = parseDateFormat.parse(toDate);
								toDate = sdf.format(todate1);
								System.out.println("toDate" + toDate);

							} catch (ParseException e) {
								e.printStackTrace();
							}
						}

						if (roleId != 7 && roleId != 4 && roleId != 5) {
							System.out.println("inside this");
							if (regionId == null && selectedBranchId == null
									|| regionId.equals("All") && selectedBranchId.equals("All")) {
								selectedBranchId = DGdao.getAllBranchIds();
								System.out.println("all selected branch id's are " + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt("All"));
							} else if (!regionId.equals("All") && selectedBranchId.equals("All")) {
								System.out.println("selectedBranchId123" + selectedBranchId);
								selectedBranchId = DGdao.getBranchIdsbylocationId(regionId);
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

							else {

								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

						} else if (roleId == 7) {
							if (regionId == null && selectedBranchId == null
									|| regionId == null && selectedBranchId.equals("All")) {

								selectedBranchId = DGdao
										.getBranchIdsbylocationId(String.valueOf(userSession.getLocation_id()));
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							} else {
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}
						} else {
							System.out.println("inside else");
							selectedBranchId = String.valueOf(userSession.getLocation_id());
							modelAndView.addObject("selectedBranchId11",
									crypt.Jcrypt(String.valueOf(userSession.getLocation_id())));
						}
						String StageName = DGserv.getStageNameDgreportsAIF(parentId,currentId);

						String ListQuery = DGserv.getBisReportListQueryDGreportsAIF(parentId,currentId);

						List<Map<String, Object>> bisReportQueryListModel = bisReportService
								.getBisListQueryResult(ListQuery, fromDate, toDate, selectedBranchId);
						request.setAttribute("BRLQL", bisReportQueryListModel);

						request.setAttribute("StageName", StageName);
						request.setAttribute("value", currentId);
						request.setAttribute("locationId", selectedBranchId);
						request.setAttribute("selBrnchName", selectedBranchId.replaceAll("_", " "));

						System.out.println("selectedBranchId::::::::::::..." + selectedBranchId);

						modelAndView.addObject("fromDate1", crypt.Jcrypt(fromDate));
						modelAndView.addObject("toDate1", crypt.Jcrypt(toDate));
						modelAndView.addObject("selectedBranchId1", crypt.Jcrypt(selectedBranchId));
						modelAndView.addObject("selBranchName", crypt.Jcrypt("Select Branch"));
						modelAndView.addObject("selectedBranchId", selectedBranchId);

						modelAndView.setViewName("AppSimpPProPending");

						

					} else {
						modelAndView.setViewName("AccessDenied");
					}
				} else {

					modelAndView.setViewName("sessionExpire");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return modelAndView;
		}
		
		
		@RequestMapping(value = "/ClosedAppSimpPro", method = RequestMethod.GET)
		public ModelAndView ClosedAppSimpPro(@ModelAttribute("model1") AllCmlReportModel cmlModel, HttpServletRequest request) {

			ModelAndView modelAndView = new ModelAndView();

			try {
				HttpSession httpsession = request.getSession(false);
				Session userSession;
				userSession = (Session) httpsession.getAttribute("logged-in");
				if (userSession != null) {
					int roleId = Integer.parseInt(userSession.getCurrent_role());
					if (roleId != 3) {
						String currentId="29";
						String parentId="0";
						
						modelAndView.addObject("roleId", roleId);

						int locationid = userSession.getLocation_id();
						modelAndView.addObject("locationid", locationid);
						System.out.println("locationid:::::::" + locationid);

						int locationtype = userSession.getLocation_type();
						modelAndView.addObject("locationtype", locationtype);
						System.out.println("locationtype:::::::" + locationtype);
						String locId = "";
						List<HashMap<String, String>> branchIds = new ArrayList<HashMap<String, String>>();

						String fromDate = cmlModel.getFromDt();
						String toDate = cmlModel.getToDt();
						String selectedBranchId = cmlModel.getBranchId();
						String regionId = cmlModel.getRegionId();
						modelAndView.addObject("branchIdList", selectedBranchId);
						String reg_id = "" + userSession.getLocation_id();
						if (regionId != null) {
							modelAndView.addObject("regionId1", regionId);
						} else {
							modelAndView.addObject("regionId1", 0);
						}

						modelAndView.addObject("branch_id", reg_id);
						System.out.println("branch id is " + selectedBranchId + "regionid" + regionId);

						SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
						String Date = formatter.format(new Date());// this is present date

						Calendar c = Calendar.getInstance();
						c.set(Calendar.DAY_OF_MONTH, 1);
						String startdateofMonth = formatter.format(c.getTime());
						System.out.println("start date is " + startdateofMonth);// Month ka start date hai
						

						if (cmlModel.getFromDt() == null && cmlModel.getToDt() == null) {
							System.out.println("Inside date is null");
							System.out.println("::::::::::::::::a::" + cmlModel.getFromDt());
							System.out.println("::::::::::::::::b::" + cmlModel.getToDt());
							fromDate = startdateofMonth;
							toDate = Date;
							modelAndView.addObject("fromDate", fromDate);
							modelAndView.addObject("toDate", toDate);
							System.out.println("fromDate date is " + fromDate);
							System.out.println("toDate date is " + toDate);
						} else {
							try {
								Date fromdate1 = parseDateFormat.parse(fromDate);
								fromDate = sdf.format(fromdate1);
								System.out.println("fromDate" + fromDate);
								Date todate1 = parseDateFormat.parse(toDate);
								toDate = sdf.format(todate1);
								System.out.println("toDate" + toDate);

							} catch (ParseException e) {
								e.printStackTrace();
							}
						}

						if (roleId != 7 && roleId != 4 && roleId != 5) {
							System.out.println("inside this");
							if (regionId == null && selectedBranchId == null
									|| regionId.equals("All") && selectedBranchId.equals("All")) {
								selectedBranchId = DGdao.getAllBranchIds();
								System.out.println("all selected branch id's are " + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt("All"));
							} else if (!regionId.equals("All") && selectedBranchId.equals("All")) {
								System.out.println("selectedBranchId123" + selectedBranchId);
								selectedBranchId = DGdao.getBranchIdsbylocationId(regionId);
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

							else {

								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

						} else if (roleId == 7) {
							if (regionId == null && selectedBranchId == null
									|| regionId == null && selectedBranchId.equals("All")) {

								selectedBranchId = DGdao
										.getBranchIdsbylocationId(String.valueOf(userSession.getLocation_id()));
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							} else {
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}
						} else {
							System.out.println("inside else");
							selectedBranchId = String.valueOf(userSession.getLocation_id());
							modelAndView.addObject("selectedBranchId11",
									crypt.Jcrypt(String.valueOf(userSession.getLocation_id())));
						}
						String StageName = DGserv.getStageNameDgreportsAIF(parentId,currentId);

						String ListQuery = DGserv.getBisReportListQueryDGreportsAIF(parentId,currentId);

						List<Map<String, Object>> bisReportQueryListModel = bisReportService
								.getBisListQueryResult(ListQuery, fromDate, toDate, selectedBranchId);
						request.setAttribute("BRLQL", bisReportQueryListModel);

						request.setAttribute("StageName", StageName);
						request.setAttribute("value", currentId);
						request.setAttribute("locationId", selectedBranchId);
						request.setAttribute("selBrnchName", selectedBranchId.replaceAll("_", " "));

						System.out.println("selectedBranchId::::::::::::..." + selectedBranchId);

						modelAndView.addObject("fromDate1", crypt.Jcrypt(fromDate));
						modelAndView.addObject("toDate1", crypt.Jcrypt(toDate));
						modelAndView.addObject("selectedBranchId1", crypt.Jcrypt(selectedBranchId));
						modelAndView.addObject("selBranchName", crypt.Jcrypt("Select Branch"));
						modelAndView.addObject("selectedBranchId", selectedBranchId);

						modelAndView.setViewName("ClosedAppSimpPro");

						

					} else {
						modelAndView.setViewName("AccessDenied");
					}
				} else {

					modelAndView.setViewName("sessionExpire");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return modelAndView;
		}
		
		
		@RequestMapping(value = "/CMLGrantPro2", method = RequestMethod.GET)
		public ModelAndView CMLGrantPro2(@ModelAttribute("model1") AllCmlReportModel cmlModel, HttpServletRequest request) {

			ModelAndView modelAndView = new ModelAndView();

			try {
				HttpSession httpsession = request.getSession(false);
				Session userSession;
				userSession = (Session) httpsession.getAttribute("logged-in");
				if (userSession != null) {
					int roleId = Integer.parseInt(userSession.getCurrent_role());
					if (roleId != 3) {
						String currentId="37";
						String parentId="0";
						
						modelAndView.addObject("roleId", roleId);

						int locationid = userSession.getLocation_id();
						modelAndView.addObject("locationid", locationid);
						System.out.println("locationid:::::::" + locationid);

						int locationtype = userSession.getLocation_type();
						modelAndView.addObject("locationtype", locationtype);
						System.out.println("locationtype:::::::" + locationtype);
						String locId = "";
						List<HashMap<String, String>> branchIds = new ArrayList<HashMap<String, String>>();

						String fromDate = cmlModel.getFromDt();
						String toDate = cmlModel.getToDt();
						String selectedBranchId = cmlModel.getBranchId();
						String regionId = cmlModel.getRegionId();
						modelAndView.addObject("branchIdList", selectedBranchId);
						String reg_id = "" + userSession.getLocation_id();
						if (regionId != null) {
							modelAndView.addObject("regionId1", regionId);
						} else {
							modelAndView.addObject("regionId1", 0);
						}

						modelAndView.addObject("branch_id", reg_id);
						System.out.println("branch id is " + selectedBranchId + "regionid" + regionId);

						SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
						String Date = formatter.format(new Date());// this is present date

						Calendar c = Calendar.getInstance();
						c.set(Calendar.DAY_OF_MONTH, 1);
						String startdateofMonth = formatter.format(c.getTime());
						System.out.println("start date is " + startdateofMonth);// Month ka start date hai
						

						if (cmlModel.getFromDt() == null && cmlModel.getToDt() == null) {
							System.out.println("Inside date is null");
							System.out.println("::::::::::::::::a::" + cmlModel.getFromDt());
							System.out.println("::::::::::::::::b::" + cmlModel.getToDt());
							fromDate = startdateofMonth;
							toDate = Date;
							modelAndView.addObject("fromDate", fromDate);
							modelAndView.addObject("toDate", toDate);
							System.out.println("fromDate date is " + fromDate);
							System.out.println("toDate date is " + toDate);
						} else {
							try {
								Date fromdate1 = parseDateFormat.parse(fromDate);
								fromDate = sdf.format(fromdate1);
								System.out.println("fromDate" + fromDate);
								Date todate1 = parseDateFormat.parse(toDate);
								toDate = sdf.format(todate1);
								System.out.println("toDate" + toDate);

							} catch (ParseException e) {
								e.printStackTrace();
							}
						}

						if (roleId != 7 && roleId != 4 && roleId != 5) {
							System.out.println("inside this");
							if (regionId == null && selectedBranchId == null
									|| regionId.equals("All") && selectedBranchId.equals("All")) {
								selectedBranchId = DGdao.getAllBranchIds();
								System.out.println("all selected branch id's are " + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt("All"));
							} else if (!regionId.equals("All") && selectedBranchId.equals("All")) {
								System.out.println("selectedBranchId123" + selectedBranchId);
								selectedBranchId = DGdao.getBranchIdsbylocationId(regionId);
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

							else {

								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

						} else if (roleId == 7) {
							if (regionId == null && selectedBranchId == null
									|| regionId == null && selectedBranchId.equals("All")) {

								selectedBranchId = DGdao
										.getBranchIdsbylocationId(String.valueOf(userSession.getLocation_id()));
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							} else {
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}
						} else {
							System.out.println("inside else");
							selectedBranchId = String.valueOf(userSession.getLocation_id());
							modelAndView.addObject("selectedBranchId11",
									crypt.Jcrypt(String.valueOf(userSession.getLocation_id())));
						}
						String StageName = DGserv.getStageNameDgreportsAIF(parentId,currentId);

						String ListQuery = DGserv.getBisReportListQueryDGreportsAIF(parentId,currentId);

						List<Map<String, Object>> bisReportQueryListModel = bisReportService
								.getBisListQueryResult(ListQuery, fromDate, toDate, selectedBranchId);
						request.setAttribute("BRLQL", bisReportQueryListModel);

						request.setAttribute("StageName", StageName);
						request.setAttribute("value", currentId);
						request.setAttribute("locationId", selectedBranchId);
						request.setAttribute("selBrnchName", selectedBranchId.replaceAll("_", " "));

						System.out.println("selectedBranchId::::::::::::..." + selectedBranchId);

						modelAndView.addObject("fromDate1", crypt.Jcrypt(fromDate));
						modelAndView.addObject("toDate1", crypt.Jcrypt(toDate));
						modelAndView.addObject("selectedBranchId1", crypt.Jcrypt(selectedBranchId));
						modelAndView.addObject("selBranchName", crypt.Jcrypt("Select Branch"));
						modelAndView.addObject("selectedBranchId", selectedBranchId);

						modelAndView.setViewName("CMLGrantPro2");

						

					} else {
						modelAndView.setViewName("AccessDenied");
					}
				} else {

					modelAndView.setViewName("sessionExpire");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return modelAndView;
		}
		
		
		 //-------------------------------------------------------------//
		 //------Status of applications under simplified procedure------//
		 //-------------------------------------------------------------//
		
		
		@RequestMapping(value = "/AppNormPro", method = RequestMethod.GET)
		public ModelAndView AppNormPro(@ModelAttribute("model1") AllCmlReportModel cmlModel, HttpServletRequest request) {

			ModelAndView modelAndView = new ModelAndView();

			try {
				HttpSession httpsession = request.getSession(false);
				Session userSession;
				userSession = (Session) httpsession.getAttribute("logged-in");
				if (userSession != null) {
					int roleId = Integer.parseInt(userSession.getCurrent_role());
					if (roleId != 3) {
						String currentId="44";
						String parentId="0";
						
						modelAndView.addObject("roleId", roleId);

						int locationid = userSession.getLocation_id();
						modelAndView.addObject("locationid", locationid);
						System.out.println("locationid:::::::" + locationid);

						int locationtype = userSession.getLocation_type();
						modelAndView.addObject("locationtype", locationtype);
						System.out.println("locationtype:::::::" + locationtype);
						String locId = "";
						List<HashMap<String, String>> branchIds = new ArrayList<HashMap<String, String>>();

						String fromDate = cmlModel.getFromDt();
						String toDate = cmlModel.getToDt();
						String selectedBranchId = cmlModel.getBranchId();
						String regionId = cmlModel.getRegionId();
						modelAndView.addObject("branchIdList", selectedBranchId);
						String reg_id = "" + userSession.getLocation_id();
						if (regionId != null) {
							modelAndView.addObject("regionId1", regionId);
						} else {
							modelAndView.addObject("regionId1", 0);
						}

						modelAndView.addObject("branch_id", reg_id);
						System.out.println("branch id is " + selectedBranchId + "regionid" + regionId);

						SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
						String Date = formatter.format(new Date());// this is present date

						Calendar c = Calendar.getInstance();
						c.set(Calendar.DAY_OF_MONTH, 1);
						String startdateofMonth = formatter.format(c.getTime());
						System.out.println("start date is " + startdateofMonth);// Month ka start date hai
						

						if (cmlModel.getFromDt() == null && cmlModel.getToDt() == null) {
							System.out.println("Inside date is null");
							System.out.println("::::::::::::::::a::" + cmlModel.getFromDt());
							System.out.println("::::::::::::::::b::" + cmlModel.getToDt());
							fromDate = startdateofMonth;
							toDate = Date;
							modelAndView.addObject("fromDate", fromDate);
							modelAndView.addObject("toDate", toDate);
							System.out.println("fromDate date is " + fromDate);
							System.out.println("toDate date is " + toDate);
						} else {
							try {
								Date fromdate1 = parseDateFormat.parse(fromDate);
								fromDate = sdf.format(fromdate1);
								System.out.println("fromDate" + fromDate);
								Date todate1 = parseDateFormat.parse(toDate);
								toDate = sdf.format(todate1);
								System.out.println("toDate" + toDate);

							} catch (ParseException e) {
								e.printStackTrace();
							}
						}

						if (roleId != 7 && roleId != 4 && roleId != 5) {
							System.out.println("inside this");
							if (regionId == null && selectedBranchId == null
									|| regionId.equals("All") && selectedBranchId.equals("All")) {
								selectedBranchId = DGdao.getAllBranchIds();
								System.out.println("all selected branch id's are " + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt("All"));
							} else if (!regionId.equals("All") && selectedBranchId.equals("All")) {
								System.out.println("selectedBranchId123" + selectedBranchId);
								selectedBranchId = DGdao.getBranchIdsbylocationId(regionId);
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

							else {

								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

						} else if (roleId == 7) {
							if (regionId == null && selectedBranchId == null
									|| regionId == null && selectedBranchId.equals("All")) {

								selectedBranchId = DGdao
										.getBranchIdsbylocationId(String.valueOf(userSession.getLocation_id()));
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							} else {
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}
						} else {
							System.out.println("inside else");
							selectedBranchId = String.valueOf(userSession.getLocation_id());
							modelAndView.addObject("selectedBranchId11",
									crypt.Jcrypt(String.valueOf(userSession.getLocation_id())));
						}
						String StageName = DGserv.getStageNameDgreportsAIF(parentId,currentId);

						String ListQuery = DGserv.getBisReportListQueryDGreportsAIF(parentId,currentId);

						List<Map<String, Object>> bisReportQueryListModel = bisReportService
								.getBisListQueryResult(ListQuery, fromDate, toDate, selectedBranchId);
						request.setAttribute("BRLQL", bisReportQueryListModel);

						request.setAttribute("StageName", StageName);
						request.setAttribute("value", currentId);
						request.setAttribute("locationId", selectedBranchId);
						request.setAttribute("selBrnchName", selectedBranchId.replaceAll("_", " "));

						System.out.println("selectedBranchId::::::::::::..." + selectedBranchId);

						modelAndView.addObject("fromDate1", crypt.Jcrypt(fromDate));
						modelAndView.addObject("toDate1", crypt.Jcrypt(toDate));
						modelAndView.addObject("selectedBranchId1", crypt.Jcrypt(selectedBranchId));
						modelAndView.addObject("selBranchName", crypt.Jcrypt("Select Branch"));
						modelAndView.addObject("selectedBranchId", selectedBranchId);

						modelAndView.setViewName("AppNormPro");

						

					} else {
						modelAndView.setViewName("AccessDenied");
					}
				} else {

					modelAndView.setViewName("sessionExpire");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return modelAndView;
		}
		
		@RequestMapping(value = "/AppNormProPending", method = RequestMethod.GET)
		public ModelAndView AppNormProPending(@ModelAttribute("model1") AllCmlReportModel cmlModel, HttpServletRequest request) {

			ModelAndView modelAndView = new ModelAndView();

			try {
				HttpSession httpsession = request.getSession(false);
				Session userSession;
				userSession = (Session) httpsession.getAttribute("logged-in");
				if (userSession != null) {
					int roleId = Integer.parseInt(userSession.getCurrent_role());
					if (roleId != 3) {
						String currentId="51";
						String parentId="0";
						
						modelAndView.addObject("roleId", roleId);

						int locationid = userSession.getLocation_id();
						modelAndView.addObject("locationid", locationid);
						System.out.println("locationid:::::::" + locationid);

						int locationtype = userSession.getLocation_type();
						modelAndView.addObject("locationtype", locationtype);
						System.out.println("locationtype:::::::" + locationtype);
						String locId = "";
						List<HashMap<String, String>> branchIds = new ArrayList<HashMap<String, String>>();

						String fromDate = cmlModel.getFromDt();
						String toDate = cmlModel.getToDt();
						String selectedBranchId = cmlModel.getBranchId();
						String regionId = cmlModel.getRegionId();
						modelAndView.addObject("branchIdList", selectedBranchId);
						String reg_id = "" + userSession.getLocation_id();
						if (regionId != null) {
							modelAndView.addObject("regionId1", regionId);
						} else {
							modelAndView.addObject("regionId1", 0);
						}

						modelAndView.addObject("branch_id", reg_id);
						System.out.println("branch id is " + selectedBranchId + "regionid" + regionId);

						SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
						String Date = formatter.format(new Date());// this is present date

						Calendar c = Calendar.getInstance();
						c.set(Calendar.DAY_OF_MONTH, 1);
						String startdateofMonth = formatter.format(c.getTime());
						System.out.println("start date is " + startdateofMonth);// Month ka start date hai
						

						if (cmlModel.getFromDt() == null && cmlModel.getToDt() == null) {
							System.out.println("Inside date is null");
							System.out.println("::::::::::::::::a::" + cmlModel.getFromDt());
							System.out.println("::::::::::::::::b::" + cmlModel.getToDt());
							fromDate = startdateofMonth;
							toDate = Date;
							modelAndView.addObject("fromDate", fromDate);
							modelAndView.addObject("toDate", toDate);
							System.out.println("fromDate date is " + fromDate);
							System.out.println("toDate date is " + toDate);
						} else {
							try {
								Date fromdate1 = parseDateFormat.parse(fromDate);
								fromDate = sdf.format(fromdate1);
								System.out.println("fromDate" + fromDate);
								Date todate1 = parseDateFormat.parse(toDate);
								toDate = sdf.format(todate1);
								System.out.println("toDate" + toDate);

							} catch (ParseException e) {
								e.printStackTrace();
							}
						}

						if (roleId != 7 && roleId != 4 && roleId != 5) {
							System.out.println("inside this");
							if (regionId == null && selectedBranchId == null
									|| regionId.equals("All") && selectedBranchId.equals("All")) {
								selectedBranchId = DGdao.getAllBranchIds();
								System.out.println("all selected branch id's are " + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt("All"));
							} else if (!regionId.equals("All") && selectedBranchId.equals("All")) {
								System.out.println("selectedBranchId123" + selectedBranchId);
								selectedBranchId = DGdao.getBranchIdsbylocationId(regionId);
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

							else {

								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

						} else if (roleId == 7) {
							if (regionId == null && selectedBranchId == null
									|| regionId == null && selectedBranchId.equals("All")) {

								selectedBranchId = DGdao
										.getBranchIdsbylocationId(String.valueOf(userSession.getLocation_id()));
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							} else {
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}
						} else {
							System.out.println("inside else");
							selectedBranchId = String.valueOf(userSession.getLocation_id());
							modelAndView.addObject("selectedBranchId11",
									crypt.Jcrypt(String.valueOf(userSession.getLocation_id())));
						}
						String StageName = DGserv.getStageNameDgreportsAIF(parentId,currentId);

						String ListQuery = DGserv.getBisReportListQueryDGreportsAIF(parentId,currentId);

						List<Map<String, Object>> bisReportQueryListModel = bisReportService
								.getBisListQueryResult(ListQuery, fromDate, toDate, selectedBranchId);
						request.setAttribute("BRLQL", bisReportQueryListModel);

						request.setAttribute("StageName", StageName);
						request.setAttribute("value", currentId);
						request.setAttribute("locationId", selectedBranchId);
						request.setAttribute("selBrnchName", selectedBranchId.replaceAll("_", " "));

						System.out.println("selectedBranchId::::::::::::..." + selectedBranchId);

						modelAndView.addObject("fromDate1", crypt.Jcrypt(fromDate));
						modelAndView.addObject("toDate1", crypt.Jcrypt(toDate));
						modelAndView.addObject("selectedBranchId1", crypt.Jcrypt(selectedBranchId));
						modelAndView.addObject("selBranchName", crypt.Jcrypt("Select Branch"));
						modelAndView.addObject("selectedBranchId", selectedBranchId);

						modelAndView.setViewName("AppNormProPending");

						

					} else {
						modelAndView.setViewName("AccessDenied");
					}
				} else {

					modelAndView.setViewName("sessionExpire");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return modelAndView;
		}
		
		@RequestMapping(value = "/AppNormProClosed", method = RequestMethod.GET)
		public ModelAndView AppNormProClosed(@ModelAttribute("model1") AllCmlReportModel cmlModel, HttpServletRequest request) {

			ModelAndView modelAndView = new ModelAndView();

			try {
				HttpSession httpsession = request.getSession(false);
				Session userSession;
				userSession = (Session) httpsession.getAttribute("logged-in");
				if (userSession != null) {
					int roleId = Integer.parseInt(userSession.getCurrent_role());
					if (roleId != 3) {
						String currentId="58";
						String parentId="0";
						
						modelAndView.addObject("roleId", roleId);

						int locationid = userSession.getLocation_id();
						modelAndView.addObject("locationid", locationid);
						System.out.println("locationid:::::::" + locationid);

						int locationtype = userSession.getLocation_type();
						modelAndView.addObject("locationtype", locationtype);
						System.out.println("locationtype:::::::" + locationtype);
						String locId = "";
						List<HashMap<String, String>> branchIds = new ArrayList<HashMap<String, String>>();

						String fromDate = cmlModel.getFromDt();
						String toDate = cmlModel.getToDt();
						String selectedBranchId = cmlModel.getBranchId();
						String regionId = cmlModel.getRegionId();
						modelAndView.addObject("branchIdList", selectedBranchId);
						String reg_id = "" + userSession.getLocation_id();
						if (regionId != null) {
							modelAndView.addObject("regionId1", regionId);
						} else {
							modelAndView.addObject("regionId1", 0);
						}

						modelAndView.addObject("branch_id", reg_id);
						System.out.println("branch id is " + selectedBranchId + "regionid" + regionId);

						SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
						String Date = formatter.format(new Date());// this is present date

						Calendar c = Calendar.getInstance();
						c.set(Calendar.DAY_OF_MONTH, 1);
						String startdateofMonth = formatter.format(c.getTime());
						System.out.println("start date is " + startdateofMonth);// Month ka start date hai
						

						if (cmlModel.getFromDt() == null && cmlModel.getToDt() == null) {
							System.out.println("Inside date is null");
							System.out.println("::::::::::::::::a::" + cmlModel.getFromDt());
							System.out.println("::::::::::::::::b::" + cmlModel.getToDt());
							fromDate = startdateofMonth;
							toDate = Date;
							modelAndView.addObject("fromDate", fromDate);
							modelAndView.addObject("toDate", toDate);
							System.out.println("fromDate date is " + fromDate);
							System.out.println("toDate date is " + toDate);
						} else {
							try {
								Date fromdate1 = parseDateFormat.parse(fromDate);
								fromDate = sdf.format(fromdate1);
								System.out.println("fromDate" + fromDate);
								Date todate1 = parseDateFormat.parse(toDate);
								toDate = sdf.format(todate1);
								System.out.println("toDate" + toDate);

							} catch (ParseException e) {
								e.printStackTrace();
							}
						}

						if (roleId != 7 && roleId != 4 && roleId != 5) {
							System.out.println("inside this");
							if (regionId == null && selectedBranchId == null
									|| regionId.equals("All") && selectedBranchId.equals("All")) {
								selectedBranchId = DGdao.getAllBranchIds();
								System.out.println("all selected branch id's are " + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt("All"));
							} else if (!regionId.equals("All") && selectedBranchId.equals("All")) {
								System.out.println("selectedBranchId123" + selectedBranchId);
								selectedBranchId = DGdao.getBranchIdsbylocationId(regionId);
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

							else {

								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

						} else if (roleId == 7) {
							if (regionId == null && selectedBranchId == null
									|| regionId == null && selectedBranchId.equals("All")) {

								selectedBranchId = DGdao
										.getBranchIdsbylocationId(String.valueOf(userSession.getLocation_id()));
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							} else {
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}
						} else {
							System.out.println("inside else");
							selectedBranchId = String.valueOf(userSession.getLocation_id());
							modelAndView.addObject("selectedBranchId11",
									crypt.Jcrypt(String.valueOf(userSession.getLocation_id())));
						}
						String StageName = DGserv.getStageNameDgreportsAIF(parentId,currentId);

						String ListQuery = DGserv.getBisReportListQueryDGreportsAIF(parentId,currentId);

						List<Map<String, Object>> bisReportQueryListModel = bisReportService
								.getBisListQueryResult(ListQuery, fromDate, toDate, selectedBranchId);
						request.setAttribute("BRLQL", bisReportQueryListModel);

						request.setAttribute("StageName", StageName);
						request.setAttribute("value", currentId);
						request.setAttribute("locationId", selectedBranchId);
						request.setAttribute("selBrnchName", selectedBranchId.replaceAll("_", " "));

						System.out.println("selectedBranchId::::::::::::..." + selectedBranchId);

						modelAndView.addObject("fromDate1", crypt.Jcrypt(fromDate));
						modelAndView.addObject("toDate1", crypt.Jcrypt(toDate));
						modelAndView.addObject("selectedBranchId1", crypt.Jcrypt(selectedBranchId));
						modelAndView.addObject("selBranchName", crypt.Jcrypt("Select Branch"));
						modelAndView.addObject("selectedBranchId", selectedBranchId);

						modelAndView.setViewName("AppNormProClosed");

						

					} else {
						modelAndView.setViewName("AccessDenied");
					}
				} else {

					modelAndView.setViewName("sessionExpire");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return modelAndView;
		}
		
		
		@RequestMapping(value = "/CMLGrantPro1", method = RequestMethod.GET)
		public ModelAndView CMLGrantProNorm(@ModelAttribute("model1") AllCmlReportModel cmlModel, HttpServletRequest request) {

			ModelAndView modelAndView = new ModelAndView();

			try {
				HttpSession httpsession = request.getSession(false);
				Session userSession;
				userSession = (Session) httpsession.getAttribute("logged-in");
				if (userSession != null) {
					int roleId = Integer.parseInt(userSession.getCurrent_role());
					if (roleId != 3) {
						String currentId="66";
						String parentId="0";
						
						modelAndView.addObject("roleId", roleId);

						int locationid = userSession.getLocation_id();
						modelAndView.addObject("locationid", locationid);
						System.out.println("locationid:::::::" + locationid);

						int locationtype = userSession.getLocation_type();
						modelAndView.addObject("locationtype", locationtype);
						System.out.println("locationtype:::::::" + locationtype);
						String locId = "";
						List<HashMap<String, String>> branchIds = new ArrayList<HashMap<String, String>>();

						String fromDate = cmlModel.getFromDt();
						String toDate = cmlModel.getToDt();
						String selectedBranchId = cmlModel.getBranchId();
						String regionId = cmlModel.getRegionId();
						modelAndView.addObject("branchIdList", selectedBranchId);
						String reg_id = "" + userSession.getLocation_id();
						if (regionId != null) {
							modelAndView.addObject("regionId1", regionId);
						} else {
							modelAndView.addObject("regionId1", 0);
						}

						modelAndView.addObject("branch_id", reg_id);
						System.out.println("branch id is " + selectedBranchId + "regionid" + regionId);

						SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
						String Date = formatter.format(new Date());// this is present date

						Calendar c = Calendar.getInstance();
						c.set(Calendar.DAY_OF_MONTH, 1);
						String startdateofMonth = formatter.format(c.getTime());
						System.out.println("start date is " + startdateofMonth);// Month ka start date hai
						

						if (cmlModel.getFromDt() == null && cmlModel.getToDt() == null) {
							System.out.println("Inside date is null");
							System.out.println("::::::::::::::::a::" + cmlModel.getFromDt());
							System.out.println("::::::::::::::::b::" + cmlModel.getToDt());
							fromDate = startdateofMonth;
							toDate = Date;
							modelAndView.addObject("fromDate", fromDate);
							modelAndView.addObject("toDate", toDate);
							System.out.println("fromDate date is " + fromDate);
							System.out.println("toDate date is " + toDate);
						} else {
							try {
								Date fromdate1 = parseDateFormat.parse(fromDate);
								fromDate = sdf.format(fromdate1);
								System.out.println("fromDate" + fromDate);
								Date todate1 = parseDateFormat.parse(toDate);
								toDate = sdf.format(todate1);
								System.out.println("toDate" + toDate);

							} catch (ParseException e) {
								e.printStackTrace();
							}
						}

						if (roleId != 7 && roleId != 4 && roleId != 5) {
							System.out.println("inside this");
							if (regionId == null && selectedBranchId == null
									|| regionId.equals("All") && selectedBranchId.equals("All")) {
								selectedBranchId = DGdao.getAllBranchIds();
								System.out.println("all selected branch id's are " + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt("All"));
							} else if (!regionId.equals("All") && selectedBranchId.equals("All")) {
								System.out.println("selectedBranchId123" + selectedBranchId);
								selectedBranchId = DGdao.getBranchIdsbylocationId(regionId);
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

							else {

								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

						} else if (roleId == 7) {
							if (regionId == null && selectedBranchId == null
									|| regionId == null && selectedBranchId.equals("All")) {

								selectedBranchId = DGdao
										.getBranchIdsbylocationId(String.valueOf(userSession.getLocation_id()));
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							} else {
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}
						} else {
							System.out.println("inside else");
							selectedBranchId = String.valueOf(userSession.getLocation_id());
							modelAndView.addObject("selectedBranchId11",
									crypt.Jcrypt(String.valueOf(userSession.getLocation_id())));
						}
						String StageName = DGserv.getStageNameDgreportsAIF(parentId,currentId);

						String ListQuery = DGserv.getBisReportListQueryDGreportsAIF(parentId,currentId);

						List<Map<String, Object>> bisReportQueryListModel = bisReportService
								.getBisListQueryResult(ListQuery, fromDate, toDate, selectedBranchId);
						request.setAttribute("BRLQL", bisReportQueryListModel);

						request.setAttribute("StageName", StageName);
						request.setAttribute("value", currentId);
						request.setAttribute("locationId", selectedBranchId);
						request.setAttribute("selBrnchName", selectedBranchId.replaceAll("_", " "));

						System.out.println("selectedBranchId::::::::::::..." + selectedBranchId);

						modelAndView.addObject("fromDate1", crypt.Jcrypt(fromDate));
						modelAndView.addObject("toDate1", crypt.Jcrypt(toDate));
						modelAndView.addObject("selectedBranchId1", crypt.Jcrypt(selectedBranchId));
						modelAndView.addObject("selBranchName", crypt.Jcrypt("Select Branch"));
						modelAndView.addObject("selectedBranchId", selectedBranchId);

						modelAndView.setViewName("CMLGrantPro1");

						

					} else {
						modelAndView.setViewName("AccessDenied");
					}
				} else {

					modelAndView.setViewName("sessionExpire");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return modelAndView;
		}
		
		
		@RequestMapping(value = "/CMLReneRprt", method = RequestMethod.GET)
		public ModelAndView CMLReneRprt(@ModelAttribute("model1") AllCmlReportModel cmlModel, HttpServletRequest request) {

			ModelAndView modelAndView = new ModelAndView();

			try {
				HttpSession httpsession = request.getSession(false);
				Session userSession;
				userSession = (Session) httpsession.getAttribute("logged-in");
				if (userSession != null) {
					int roleId = Integer.parseInt(userSession.getCurrent_role());
					if (roleId != 3) {
						String currentId="73";
						String parentId="0";
						
						modelAndView.addObject("roleId", roleId);

						int locationid = userSession.getLocation_id();
						modelAndView.addObject("locationid", locationid);
						System.out.println("locationid:::::::" + locationid);

						int locationtype = userSession.getLocation_type();
						modelAndView.addObject("locationtype", locationtype);
						System.out.println("locationtype:::::::" + locationtype);
						String locId = "";
						List<HashMap<String, String>> branchIds = new ArrayList<HashMap<String, String>>();

						String fromDate = cmlModel.getFromDt();
						String toDate = cmlModel.getToDt();
						String selectedBranchId = cmlModel.getBranchId();
						String regionId = cmlModel.getRegionId();
						modelAndView.addObject("branchIdList", selectedBranchId);
						String reg_id = "" + userSession.getLocation_id();
						if (regionId != null) {
							modelAndView.addObject("regionId1", regionId);
						} else {
							modelAndView.addObject("regionId1", 0);
						}

						modelAndView.addObject("branch_id", reg_id);
						System.out.println("branch id is " + selectedBranchId + "regionid" + regionId);

						SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
						String Date = formatter.format(new Date());// this is present date

						Calendar c = Calendar.getInstance();
						c.set(Calendar.DAY_OF_MONTH, 1);
						String startdateofMonth = formatter.format(c.getTime());
						System.out.println("start date is " + startdateofMonth);// Month ka start date hai
						

						if (cmlModel.getFromDt() == null && cmlModel.getToDt() == null) {
							System.out.println("Inside date is null");
							System.out.println("::::::::::::::::a::" + cmlModel.getFromDt());
							System.out.println("::::::::::::::::b::" + cmlModel.getToDt());
							fromDate = startdateofMonth;
							toDate = Date;
							modelAndView.addObject("fromDate", fromDate);
							modelAndView.addObject("toDate", toDate);
							System.out.println("fromDate date is " + fromDate);
							System.out.println("toDate date is " + toDate);
						} else {
							try {
								Date fromdate1 = parseDateFormat.parse(fromDate);
								fromDate = sdf.format(fromdate1);
								System.out.println("fromDate" + fromDate);
								Date todate1 = parseDateFormat.parse(toDate);
								toDate = sdf.format(todate1);
								System.out.println("toDate" + toDate);

							} catch (ParseException e) {
								e.printStackTrace();
							}
						}

						if (roleId != 7 && roleId != 4 && roleId != 5) {
							System.out.println("inside this");
							if (regionId == null && selectedBranchId == null
									|| regionId.equals("All") && selectedBranchId.equals("All")) {
								selectedBranchId = DGdao.getAllBranchIds();
								System.out.println("all selected branch id's are " + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt("All"));
							} else if (!regionId.equals("All") && selectedBranchId.equals("All")) {
								System.out.println("selectedBranchId123" + selectedBranchId);
								selectedBranchId = DGdao.getBranchIdsbylocationId(regionId);
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

							else {

								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

						} else if (roleId == 7) {
							if (regionId == null && selectedBranchId == null
									|| regionId == null && selectedBranchId.equals("All")) {

								selectedBranchId = DGdao
										.getBranchIdsbylocationId(String.valueOf(userSession.getLocation_id()));
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							} else {
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}
						} else {
							System.out.println("inside else");
							selectedBranchId = String.valueOf(userSession.getLocation_id());
							modelAndView.addObject("selectedBranchId11",
									crypt.Jcrypt(String.valueOf(userSession.getLocation_id())));
						}
						String StageName = DGserv.getStageNameDgreportsAIF(parentId,currentId);

						String ListQuery = DGserv.getBisReportListQueryDGreportsAIF(parentId,currentId);

						List<Map<String, Object>> bisReportQueryListModel = bisReportService
								.getBisListQueryResult(ListQuery, fromDate, toDate, selectedBranchId);
						request.setAttribute("BRLQL", bisReportQueryListModel);

						request.setAttribute("StageName", StageName);
						request.setAttribute("value", currentId);
						request.setAttribute("locationId", selectedBranchId);
						request.setAttribute("selBrnchName", selectedBranchId.replaceAll("_", " "));

						System.out.println("selectedBranchId::::::::::::..." + selectedBranchId);

						modelAndView.addObject("fromDate1", crypt.Jcrypt(fromDate));
						modelAndView.addObject("toDate1", crypt.Jcrypt(toDate));
						modelAndView.addObject("selectedBranchId1", crypt.Jcrypt(selectedBranchId));
						modelAndView.addObject("selBranchName", crypt.Jcrypt("Select Branch"));
						modelAndView.addObject("selectedBranchId", selectedBranchId);

						modelAndView.setViewName("CMLReneRprt");

						

					} else {
						modelAndView.setViewName("AccessDenied");
					}
				} else {

					modelAndView.setViewName("sessionExpire");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return modelAndView;
		}
		
		
		
		@RequestMapping(value = "/StatusofInclusion", method = RequestMethod.GET)
		public ModelAndView StatusofInclusion(@ModelAttribute("model1") AllCmlReportModel cmlModel, HttpServletRequest request) {

			ModelAndView modelAndView = new ModelAndView();

			try {
				HttpSession httpsession = request.getSession(false);
				Session userSession;
				userSession = (Session) httpsession.getAttribute("logged-in");
				if (userSession != null) {
					int roleId = Integer.parseInt(userSession.getCurrent_role());
					if (roleId != 3) {
						String currentId="133";
						String parentId="0";
						
						modelAndView.addObject("roleId", roleId);

						int locationid = userSession.getLocation_id();
						modelAndView.addObject("locationid", locationid);
						System.out.println("locationid:::::::" + locationid);

						int locationtype = userSession.getLocation_type();
						modelAndView.addObject("locationtype", locationtype);
						System.out.println("locationtype:::::::" + locationtype);
						String locId = "";
						List<HashMap<String, String>> branchIds = new ArrayList<HashMap<String, String>>();

						String fromDate = cmlModel.getFromDt();
						String toDate = cmlModel.getToDt();
						String selectedBranchId = cmlModel.getBranchId();
						String regionId = cmlModel.getRegionId();
						modelAndView.addObject("branchIdList", selectedBranchId);
						String reg_id = "" + userSession.getLocation_id();
						if (regionId != null) {
							modelAndView.addObject("regionId1", regionId);
						} else {
							modelAndView.addObject("regionId1", 0);
						}

						modelAndView.addObject("branch_id", reg_id);
						System.out.println("branch id is " + selectedBranchId + "regionid" + regionId);

						SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
						String Date = formatter.format(new Date());// this is present date

						Calendar c = Calendar.getInstance();
						c.set(Calendar.DAY_OF_MONTH, 1);
						String startdateofMonth = formatter.format(c.getTime());
						System.out.println("start date is " + startdateofMonth);// Month ka start date hai
						

						if (cmlModel.getFromDt() == null && cmlModel.getToDt() == null) {
							System.out.println("Inside date is null");
							System.out.println("::::::::::::::::a::" + cmlModel.getFromDt());
							System.out.println("::::::::::::::::b::" + cmlModel.getToDt());
							fromDate = startdateofMonth;
							toDate = Date;
							modelAndView.addObject("fromDate", fromDate);
							modelAndView.addObject("toDate", toDate);
							System.out.println("fromDate date is " + fromDate);
							System.out.println("toDate date is " + toDate);
						} else {
							try {
								Date fromdate1 = parseDateFormat.parse(fromDate);
								fromDate = sdf.format(fromdate1);
								System.out.println("fromDate" + fromDate);
								Date todate1 = parseDateFormat.parse(toDate);
								toDate = sdf.format(todate1);
								System.out.println("toDate" + toDate);

							} catch (ParseException e) {
								e.printStackTrace();
							}
						}

						if (roleId != 7 && roleId != 4 && roleId != 5) {
							System.out.println("inside this");
							if (regionId == null && selectedBranchId == null
									|| regionId.equals("All") && selectedBranchId.equals("All")) {
								selectedBranchId = DGdao.getAllBranchIds();
								System.out.println("all selected branch id's are " + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt("All"));
							} else if (!regionId.equals("All") && selectedBranchId.equals("All")) {
								System.out.println("selectedBranchId123" + selectedBranchId);
								selectedBranchId = DGdao.getBranchIdsbylocationId(regionId);
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

							else {

								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

						} else if (roleId == 7) {
							if (regionId == null && selectedBranchId == null
									|| regionId == null && selectedBranchId.equals("All")) {

								selectedBranchId = DGdao
										.getBranchIdsbylocationId(String.valueOf(userSession.getLocation_id()));
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							} else {
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}
						} else {
							System.out.println("inside else");
							selectedBranchId = String.valueOf(userSession.getLocation_id());
							modelAndView.addObject("selectedBranchId11",
									crypt.Jcrypt(String.valueOf(userSession.getLocation_id())));
						}
						String StageName = DGserv.getStageNameDgreportsAIF(parentId,currentId);

						String ListQuery = DGserv.getBisReportListQueryDGreportsAIF(parentId,currentId);

						List<Map<String, Object>> bisReportQueryListModel = bisReportService
								.getBisListQueryResult(ListQuery, fromDate, toDate, selectedBranchId);
						request.setAttribute("BRLQL", bisReportQueryListModel);

						request.setAttribute("StageName", StageName);
						request.setAttribute("value", currentId);
						request.setAttribute("locationId", selectedBranchId);
						request.setAttribute("selBrnchName", selectedBranchId.replaceAll("_", " "));

						System.out.println("selectedBranchId::::::::::::..." + selectedBranchId);

						modelAndView.addObject("fromDate1", crypt.Jcrypt(fromDate));
						modelAndView.addObject("toDate1", crypt.Jcrypt(toDate));
						modelAndView.addObject("selectedBranchId1", crypt.Jcrypt(selectedBranchId));
						modelAndView.addObject("selBranchName", crypt.Jcrypt("Select Branch"));
						modelAndView.addObject("selectedBranchId", selectedBranchId);

						modelAndView.setViewName("StatusofInclusion");

						

					} else {
						modelAndView.setViewName("AccessDenied");
					}
				} else {

					modelAndView.setViewName("sessionExpire");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return modelAndView;
		}
		
		 //--------------AppNormPro-----------------------------------------------//
		
		
		@RequestMapping(value = "/LicenseStatusChanged", method = RequestMethod.GET)
		public ModelAndView LicenseStatusChanged(@ModelAttribute("model1") AllCmlReportModel cmlModel, HttpServletRequest request) {

			ModelAndView modelAndView = new ModelAndView();

			try {
				HttpSession httpsession = request.getSession(false);
				Session userSession;
				userSession = (Session) httpsession.getAttribute("logged-in");
				if (userSession != null) {
					int roleId = Integer.parseInt(userSession.getCurrent_role());
					if (roleId != 3) {
						String currentId="81";
						String parentId="0";
						
						modelAndView.addObject("roleId", roleId);

						int locationid = userSession.getLocation_id();
						modelAndView.addObject("locationid", locationid);
						System.out.println("locationid:::::::" + locationid);

						int locationtype = userSession.getLocation_type();
						modelAndView.addObject("locationtype", locationtype);
						System.out.println("locationtype:::::::" + locationtype);
						String locId = "";
						List<HashMap<String, String>> branchIds = new ArrayList<HashMap<String, String>>();

						String fromDate = cmlModel.getFromDt();
						String toDate = cmlModel.getToDt();
						String selectedBranchId = cmlModel.getBranchId();
						String regionId = cmlModel.getRegionId();
						modelAndView.addObject("branchIdList", selectedBranchId);
						String reg_id = "" + userSession.getLocation_id();
						if (regionId != null) {
							modelAndView.addObject("regionId1", regionId);
						} else {
							modelAndView.addObject("regionId1", 0);
						}

						modelAndView.addObject("branch_id", reg_id);
						System.out.println("branch id is " + selectedBranchId + "regionid" + regionId);

						SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
						String Date = formatter.format(new Date());// this is present date

						Calendar c = Calendar.getInstance();
						c.set(Calendar.DAY_OF_MONTH, 1);
						String startdateofMonth = formatter.format(c.getTime());
						System.out.println("start date is " + startdateofMonth);// Month ka start date hai
						

						if (cmlModel.getFromDt() == null && cmlModel.getToDt() == null) {
							System.out.println("Inside date is null");
							System.out.println("::::::::::::::::a::" + cmlModel.getFromDt());
							System.out.println("::::::::::::::::b::" + cmlModel.getToDt());
							fromDate = startdateofMonth;
							toDate = Date;
							modelAndView.addObject("fromDate", fromDate);
							modelAndView.addObject("toDate", toDate);
							System.out.println("fromDate date is " + fromDate);
							System.out.println("toDate date is " + toDate);
						} else {
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

						if (roleId != 7 && roleId != 4 && roleId != 5) {
							System.out.println("inside this");
							if (regionId == null && selectedBranchId == null
									|| regionId.equals("All") && selectedBranchId.equals("All")) {
								selectedBranchId = DGdao.getAllBranchIds();
								System.out.println("all selected branch id's are " + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt("All"));
							} else if (!regionId.equals("All") && selectedBranchId.equals("All")) {
								System.out.println("selectedBranchId123" + selectedBranchId);
								selectedBranchId = DGdao.getBranchIdsbylocationId(regionId);
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}
							else {

								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

						} else if (roleId == 7) {
							if (regionId == null && selectedBranchId == null
									|| regionId == null && selectedBranchId.equals("All")) {

								selectedBranchId = DGdao
										.getBranchIdsbylocationId(String.valueOf(userSession.getLocation_id()));
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							} else {
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}
						} else {
							System.out.println("inside else");
							selectedBranchId = String.valueOf(userSession.getLocation_id());
							modelAndView.addObject("selectedBranchId11",
									crypt.Jcrypt(String.valueOf(userSession.getLocation_id())));
						}
						String StageName = DGserv.getStageNameDgreportsAIF(parentId,currentId);

						String ListQuery = DGserv.getBisReportListQueryDGreportsAIF(parentId,currentId);

						List<Map<String, Object>> bisReportQueryListModel = bisReportService
								.getBisListQueryResult(ListQuery, fromDate, toDate, selectedBranchId);
						request.setAttribute("BRLQL", bisReportQueryListModel);
						request.setAttribute("StageName", StageName);
						request.setAttribute("value", currentId);
						request.setAttribute("locationId", selectedBranchId);
						request.setAttribute("selBrnchName", selectedBranchId.replaceAll("_", " "));
						System.out.println("selectedBranchId::::::::::::..." + selectedBranchId);
						modelAndView.addObject("fromDate1", crypt.Jcrypt(fromDate));
						modelAndView.addObject("toDate1", crypt.Jcrypt(toDate));
						modelAndView.addObject("selectedBranchId1", crypt.Jcrypt(selectedBranchId));
						modelAndView.addObject("selBranchName", crypt.Jcrypt("Select Branch"));
						modelAndView.addObject("selectedBranchId", selectedBranchId);
						modelAndView.setViewName("LicenseStatusChanged");

					
					} else {
						modelAndView.setViewName("AccessDenied");
					}
				} else {

					modelAndView.setViewName("sessionExpire");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return modelAndView;
		}
		

		
		
		//---------------Consignee Report(Last Updated)----------------//
		//-------------------------------------------------------------//
	
		@RequestMapping(value = "/ConsigneeReport", method = RequestMethod.GET)
		public ModelAndView ConsigneeReport(@ModelAttribute("model1") AllCmlReportModel cmlModel, HttpServletRequest request) {

			ModelAndView modelAndView = new ModelAndView();

			try {
				HttpSession httpsession = request.getSession(false);
				Session userSession;
				userSession = (Session) httpsession.getAttribute("logged-in");
				if (userSession != null) {
					int roleId = Integer.parseInt(userSession.getCurrent_role());
					if (roleId != 3) {
						String currentId="1000";
						String parentId="0";
						
						modelAndView.addObject("roleId", roleId);

						int locationid = userSession.getLocation_id();
						modelAndView.addObject("locationid", locationid);
						System.out.println("locationid:::::::" + locationid);

						int locationtype = userSession.getLocation_type();
						modelAndView.addObject("locationtype", locationtype);
						System.out.println("locationtype:::::::" + locationtype);
						String locId = "";
						List<HashMap<String, String>> branchIds = new ArrayList<HashMap<String, String>>();

						String fromDate = cmlModel.getFromDt();
						String toDate = cmlModel.getToDt();
						String selectedBranchId = cmlModel.getBranchId();
						String regionId = cmlModel.getRegionId();
						modelAndView.addObject("branchIdList", selectedBranchId);
						String reg_id = "" + userSession.getLocation_id();
						if (regionId != null) {
							modelAndView.addObject("regionId1", regionId);
						} else {
							modelAndView.addObject("regionId1", 0);
						}

						modelAndView.addObject("branch_id", reg_id);
						System.out.println("branch id is " + selectedBranchId + "regionid" + regionId);

						SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
						String Date = formatter.format(new Date());// this is present date

						Calendar c = Calendar.getInstance();
						c.set(Calendar.DAY_OF_MONTH, 1);
						String startdateofMonth = formatter.format(c.getTime());
						System.out.println("start date is " + startdateofMonth);// Month ka start date hai
						

						if (cmlModel.getFromDt() == null && cmlModel.getToDt() == null) {
							System.out.println("Inside date is null");
							System.out.println("::::::::::::::::a::" + cmlModel.getFromDt());
							System.out.println("::::::::::::::::b::" + cmlModel.getToDt());
							fromDate = startdateofMonth;
							toDate = Date;
							modelAndView.addObject("fromDate", fromDate);
							modelAndView.addObject("toDate", toDate);
							System.out.println("fromDate date is " + fromDate);
							System.out.println("toDate date is " + toDate);
						} else {
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

						if (roleId != 7 && roleId != 4 && roleId != 5) {
							System.out.println("inside this");
							if (regionId == null && selectedBranchId == null
									|| regionId.equals("All") && selectedBranchId.equals("All")) {
								selectedBranchId = DGdao.getAllBranchIds();
								System.out.println("all selected branch id's are " + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt("All"));
							} else if (!regionId.equals("All") && selectedBranchId.equals("All")) {
								System.out.println("selectedBranchId123" + selectedBranchId);
								selectedBranchId = DGdao.getBranchIdsbylocationId(regionId);
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

							else {

								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

						} else if (roleId == 7) {
							if (regionId == null && selectedBranchId == null
									|| regionId == null && selectedBranchId.equals("All")) {

								selectedBranchId = DGdao
										.getBranchIdsbylocationId(String.valueOf(userSession.getLocation_id()));
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							} else {
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}
						} else {
							System.out.println("inside else");
							selectedBranchId = String.valueOf(userSession.getLocation_id());
							modelAndView.addObject("selectedBranchId11",
									crypt.Jcrypt(String.valueOf(userSession.getLocation_id())));
						}
						String StageName = DGserv.getStageNameDgreportsAIF(parentId,currentId);

						String ListQuery = DGserv.getBisReportListQueryDGreportsAIF(parentId,currentId);

						List<Map<String, Object>> bisReportQueryListModel = bisReportService
								.getBisListQueryResult(ListQuery, fromDate, toDate, selectedBranchId);
						request.setAttribute("BRLQL", bisReportQueryListModel);

						request.setAttribute("StageName", StageName);
						request.setAttribute("value", currentId);
						request.setAttribute("locationId", selectedBranchId);
						request.setAttribute("selBrnchName", selectedBranchId.replaceAll("_", " "));

						System.out.println("selectedBranchId::::::::::::..." + selectedBranchId);

						modelAndView.addObject("fromDate1", crypt.Jcrypt(fromDate));
						modelAndView.addObject("toDate1", crypt.Jcrypt(toDate));
						modelAndView.addObject("selectedBranchId1", crypt.Jcrypt(selectedBranchId));
						modelAndView.addObject("selBranchName", crypt.Jcrypt("Select Branch"));
						modelAndView.addObject("selectedBranchId", selectedBranchId);

						modelAndView.setViewName("ConsigneeReport");

						

					} else {
						modelAndView.setViewName("AccessDenied");
					}
				} else {

					modelAndView.setViewName("sessionExpire");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return modelAndView;
		}
		
		@RequestMapping(value = "/CMLUndrDeferment", method = RequestMethod.GET)
		public ModelAndView CMLUndrDeferment(@ModelAttribute("model1") AllCmlReportModel cmlModel, HttpServletRequest request) {

			ModelAndView modelAndView = new ModelAndView();

			try {
				HttpSession httpsession = request.getSession(false);
				Session userSession;
				userSession = (Session) httpsession.getAttribute("logged-in");
				if (userSession != null) {
					int roleId = Integer.parseInt(userSession.getCurrent_role());
					if (roleId != 3) {
						String currentId="89";
						String parentId="0";
						
						modelAndView.addObject("roleId", roleId);

						int locationid = userSession.getLocation_id();
						modelAndView.addObject("locationid", locationid);
						System.out.println("locationid:::::::" + locationid);

						int locationtype = userSession.getLocation_type();
						modelAndView.addObject("locationtype", locationtype);
						System.out.println("locationtype:::::::" + locationtype);
						String locId = "";
						List<HashMap<String, String>> branchIds = new ArrayList<HashMap<String, String>>();

						String fromDate = cmlModel.getFromDt();
						String toDate = cmlModel.getToDt();
						String selectedBranchId = cmlModel.getBranchId();
						String regionId = cmlModel.getRegionId();
						modelAndView.addObject("branchIdList", selectedBranchId);
						String reg_id = "" + userSession.getLocation_id();
						if (regionId != null) {
							modelAndView.addObject("regionId1", regionId);
						} else {
							modelAndView.addObject("regionId1", 0);
						}

						modelAndView.addObject("branch_id", reg_id);
						System.out.println("branch id is " + selectedBranchId + "regionid" + regionId);

						SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
						String Date = formatter.format(new Date());// this is present date

						Calendar c = Calendar.getInstance();
						c.set(Calendar.DAY_OF_MONTH, 1);
						String startdateofMonth = formatter.format(c.getTime());
						System.out.println("start date is " + startdateofMonth);// Month ka start date hai
						

						if (cmlModel.getFromDt() == null && cmlModel.getToDt() == null) {
							System.out.println("Inside date is null");
							System.out.println("::::::::::::::::a::" + cmlModel.getFromDt());
							System.out.println("::::::::::::::::b::" + cmlModel.getToDt());
							fromDate = startdateofMonth;
							toDate = Date;
							modelAndView.addObject("fromDate", fromDate);
							modelAndView.addObject("toDate", toDate);
							System.out.println("fromDate date is " + fromDate);
							System.out.println("toDate date is " + toDate);
						} else {
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

						if (roleId != 7 && roleId != 4 && roleId != 5) {
							System.out.println("inside this");
							if (regionId == null && selectedBranchId == null
									|| regionId.equals("All") && selectedBranchId.equals("All")) {
								selectedBranchId = DGdao.getAllBranchIds();
								System.out.println("all selected branch id's are " + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt("All"));
							} else if (!regionId.equals("All") && selectedBranchId.equals("All")) {
								System.out.println("selectedBranchId123" + selectedBranchId);
								selectedBranchId = DGdao.getBranchIdsbylocationId(regionId);
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

							else {

								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

						} else if (roleId == 7) {
							if (regionId == null && selectedBranchId == null
									|| regionId == null && selectedBranchId.equals("All")) {

								selectedBranchId = DGdao
										.getBranchIdsbylocationId(String.valueOf(userSession.getLocation_id()));
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							} else {
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}
						} else {
							System.out.println("inside else");
							selectedBranchId = String.valueOf(userSession.getLocation_id());
							modelAndView.addObject("selectedBranchId11",
									crypt.Jcrypt(String.valueOf(userSession.getLocation_id())));
						}
						String StageName = DGserv.getStageNameDgreportsAIF(parentId,currentId);

						String ListQuery = DGserv.getBisReportListQueryDGreportsAIF(parentId,currentId);

						List<Map<String, Object>> bisReportQueryListModel = bisReportService
								.getBisListQueryResult(ListQuery, fromDate, toDate, selectedBranchId);
						request.setAttribute("BRLQL", bisReportQueryListModel);

						request.setAttribute("StageName", StageName);
						request.setAttribute("value", currentId);
						request.setAttribute("locationId", selectedBranchId);
						request.setAttribute("selBrnchName", selectedBranchId.replaceAll("_", " "));

						System.out.println("selectedBranchId::::::::::::..." + selectedBranchId);

						modelAndView.addObject("fromDate1", crypt.Jcrypt(fromDate));
						modelAndView.addObject("toDate1", crypt.Jcrypt(toDate));
						modelAndView.addObject("selectedBranchId1", crypt.Jcrypt(selectedBranchId));
						modelAndView.addObject("selBranchName", crypt.Jcrypt("Select Branch"));
						modelAndView.addObject("selectedBranchId", selectedBranchId);

						modelAndView.setViewName("CMLUndrDeferment");

						

					} else {
						modelAndView.setViewName("AccessDenied");
					}
				} else {

					modelAndView.setViewName("sessionExpire");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return modelAndView;
		}
		
		
		
		@RequestMapping(value = "/CMLUndrsuspension", method = RequestMethod.GET)
		public ModelAndView CMLUndrsuspension(@ModelAttribute("model1") AllCmlReportModel cmlModel, HttpServletRequest request) {

			ModelAndView modelAndView = new ModelAndView();

			try {
				HttpSession httpsession = request.getSession(false);
				Session userSession;
				userSession = (Session) httpsession.getAttribute("logged-in");
				if (userSession != null) {
					int roleId = Integer.parseInt(userSession.getCurrent_role());
					if (roleId != 3) {
						String currentId="96";
						String parentId="0";
						
						modelAndView.addObject("roleId", roleId);

						int locationid = userSession.getLocation_id();
						modelAndView.addObject("locationid", locationid);
						System.out.println("locationid:::::::" + locationid);

						int locationtype = userSession.getLocation_type();
						modelAndView.addObject("locationtype", locationtype);
						System.out.println("locationtype:::::::" + locationtype);
						String locId = "";
						List<HashMap<String, String>> branchIds = new ArrayList<HashMap<String, String>>();

						String fromDate = cmlModel.getFromDt();
						String toDate = cmlModel.getToDt();
						String selectedBranchId = cmlModel.getBranchId();
						String regionId = cmlModel.getRegionId();
						modelAndView.addObject("branchIdList", selectedBranchId);
						String reg_id = "" + userSession.getLocation_id();
						if (regionId != null) {
							modelAndView.addObject("regionId1", regionId);
						} else {
							modelAndView.addObject("regionId1", 0);
						}

						modelAndView.addObject("branch_id", reg_id);
						System.out.println("branch id is " + selectedBranchId + "regionid" + regionId);

						SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
						String Date = formatter.format(new Date());// this is present date

						Calendar c = Calendar.getInstance();
						c.set(Calendar.DAY_OF_MONTH, 1);
						String startdateofMonth = formatter.format(c.getTime());
						System.out.println("start date is " + startdateofMonth);// Month ka start date hai
						

						if (cmlModel.getFromDt() == null && cmlModel.getToDt() == null) {
							System.out.println("Inside date is null");
							System.out.println("::::::::::::::::a::" + cmlModel.getFromDt());
							System.out.println("::::::::::::::::b::" + cmlModel.getToDt());
							fromDate = startdateofMonth;
							toDate = Date;
							modelAndView.addObject("fromDate", fromDate);
							modelAndView.addObject("toDate", toDate);
							System.out.println("fromDate date is " + fromDate);
							System.out.println("toDate date is " + toDate);
						} else {
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

						if (roleId != 7 && roleId != 4 && roleId != 5) {
							System.out.println("inside this");
							if (regionId == null && selectedBranchId == null
									|| regionId.equals("All") && selectedBranchId.equals("All")) {
								selectedBranchId = DGdao.getAllBranchIds();
								System.out.println("all selected branch id's are " + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt("All"));
							} else if (!regionId.equals("All") && selectedBranchId.equals("All")) {
								System.out.println("selectedBranchId123" + selectedBranchId);
								selectedBranchId = DGdao.getBranchIdsbylocationId(regionId);
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

							else {

								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

						} else if (roleId == 7) {
							if (regionId == null && selectedBranchId == null
									|| regionId == null && selectedBranchId.equals("All")) {

								selectedBranchId = DGdao
										.getBranchIdsbylocationId(String.valueOf(userSession.getLocation_id()));
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							} else {
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}
						} else {
							System.out.println("inside else");
							selectedBranchId = String.valueOf(userSession.getLocation_id());
							modelAndView.addObject("selectedBranchId11",
									crypt.Jcrypt(String.valueOf(userSession.getLocation_id())));
						}
						String StageName = DGserv.getStageNameDgreportsAIF(parentId,currentId);

						String ListQuery = DGserv.getBisReportListQueryDGreportsAIF(parentId,currentId);

						List<Map<String, Object>> bisReportQueryListModel = bisReportService
								.getBisListQueryResult(ListQuery, fromDate, toDate, selectedBranchId);
						request.setAttribute("BRLQL", bisReportQueryListModel);

						request.setAttribute("StageName", StageName);
						request.setAttribute("value", currentId);
						request.setAttribute("locationId", selectedBranchId);
						request.setAttribute("selBrnchName", selectedBranchId.replaceAll("_", " "));

						System.out.println("selectedBranchId::::::::::::..." + selectedBranchId);

						modelAndView.addObject("fromDate1", crypt.Jcrypt(fromDate));
						modelAndView.addObject("toDate1", crypt.Jcrypt(toDate));
						modelAndView.addObject("selectedBranchId1", crypt.Jcrypt(selectedBranchId));
						modelAndView.addObject("selBranchName", crypt.Jcrypt("Select Branch"));
						modelAndView.addObject("selectedBranchId", selectedBranchId);

						modelAndView.setViewName("CMLUndrsuspension");

						

					} else {
						modelAndView.setViewName("AccessDenied");
					}
				} else {

					modelAndView.setViewName("sessionExpire");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return modelAndView;
		}
		
		
		@RequestMapping(value = "/CMLUndrsCancellation", method = RequestMethod.GET)
		public ModelAndView CMLUndrsCancellation(@ModelAttribute("model1") AllCmlReportModel cmlModel, HttpServletRequest request) {

			ModelAndView modelAndView = new ModelAndView();

			try {
				HttpSession httpsession = request.getSession(false);
				Session userSession;
				userSession = (Session) httpsession.getAttribute("logged-in");
				if (userSession != null) {
					int roleId = Integer.parseInt(userSession.getCurrent_role());
					if (roleId != 3) {
						String currentId="104";
						String parentId="0";
						
						modelAndView.addObject("roleId", roleId);

						int locationid = userSession.getLocation_id();
						modelAndView.addObject("locationid", locationid);
						System.out.println("locationid:::::::" + locationid);

						int locationtype = userSession.getLocation_type();
						modelAndView.addObject("locationtype", locationtype);
						System.out.println("locationtype:::::::" + locationtype);
						String locId = "";
						List<HashMap<String, String>> branchIds = new ArrayList<HashMap<String, String>>();

						String fromDate = cmlModel.getFromDt();
						String toDate = cmlModel.getToDt();
						String selectedBranchId = cmlModel.getBranchId();
						String regionId = cmlModel.getRegionId();
						modelAndView.addObject("branchIdList", selectedBranchId);
						String reg_id = "" + userSession.getLocation_id();
						if (regionId != null) {
							modelAndView.addObject("regionId1", regionId);
						} else {
							modelAndView.addObject("regionId1", 0);
						}

						modelAndView.addObject("branch_id", reg_id);
						System.out.println("branch id is " + selectedBranchId + "regionid" + regionId);

						SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
						String Date = formatter.format(new Date());// this is present date

						Calendar c = Calendar.getInstance();
						c.set(Calendar.DAY_OF_MONTH, 1);
						String startdateofMonth = formatter.format(c.getTime());
						System.out.println("start date is " + startdateofMonth);// Month ka start date hai
						

						if (cmlModel.getFromDt() == null && cmlModel.getToDt() == null) {
							System.out.println("Inside date is null");
							System.out.println("::::::::::::::::a::" + cmlModel.getFromDt());
							System.out.println("::::::::::::::::b::" + cmlModel.getToDt());
							fromDate = startdateofMonth;
							toDate = Date;
							modelAndView.addObject("fromDate", fromDate);
							modelAndView.addObject("toDate", toDate);
							System.out.println("fromDate date is " + fromDate);
							System.out.println("toDate date is " + toDate);
						} else {
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

						if (roleId != 7 && roleId != 4 && roleId != 5) {
							System.out.println("inside this");
							if (regionId == null && selectedBranchId == null
									|| regionId.equals("All") && selectedBranchId.equals("All")) {
								selectedBranchId = DGdao.getAllBranchIds();
								System.out.println("all selected branch id's are " + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt("All"));
							} else if (!regionId.equals("All") && selectedBranchId.equals("All")) {
								System.out.println("selectedBranchId123" + selectedBranchId);
								selectedBranchId = DGdao.getBranchIdsbylocationId(regionId);
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

							else {

								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

						} else if (roleId == 7) {
							if (regionId == null && selectedBranchId == null
									|| regionId == null && selectedBranchId.equals("All")) {

								selectedBranchId = DGdao
										.getBranchIdsbylocationId(String.valueOf(userSession.getLocation_id()));
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							} else {
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}
						} else {
							System.out.println("inside else");
							selectedBranchId = String.valueOf(userSession.getLocation_id());
							modelAndView.addObject("selectedBranchId11",
									crypt.Jcrypt(String.valueOf(userSession.getLocation_id())));
						}
						String StageName = DGserv.getStageNameDgreportsAIF(parentId,currentId);

						String ListQuery = DGserv.getBisReportListQueryDGreportsAIF(parentId,currentId);

						List<Map<String, Object>> bisReportQueryListModel = bisReportService
								.getBisListQueryResult(ListQuery, fromDate, toDate, selectedBranchId);
						request.setAttribute("BRLQL", bisReportQueryListModel);

						request.setAttribute("StageName", StageName);
						request.setAttribute("value", currentId);
						request.setAttribute("locationId", selectedBranchId);
						request.setAttribute("selBrnchName", selectedBranchId.replaceAll("_", " "));

						System.out.println("selectedBranchId::::::::::::..." + selectedBranchId);

						modelAndView.addObject("fromDate1", crypt.Jcrypt(fromDate));
						modelAndView.addObject("toDate1", crypt.Jcrypt(toDate));
						modelAndView.addObject("selectedBranchId1", crypt.Jcrypt(selectedBranchId));
						modelAndView.addObject("selBranchName", crypt.Jcrypt("Select Branch"));
						modelAndView.addObject("selectedBranchId", selectedBranchId);

						modelAndView.setViewName("CMLUndrsCancellation");

						

					} else {
						modelAndView.setViewName("AccessDenied");
					}
				} else {

					modelAndView.setViewName("sessionExpire");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return modelAndView;
		}
		
		
		@RequestMapping(value = "/Technicaldepartmentnumberlicenses", method = RequestMethod.GET)
		public ModelAndView Technicaldepartmentnumberlicenses(@ModelAttribute("model1") AllCmlReportModel cmlModel, HttpServletRequest request) {

			ModelAndView modelAndView = new ModelAndView();

			try {
				HttpSession httpsession = request.getSession(false);
				Session userSession;
				userSession = (Session) httpsession.getAttribute("logged-in");
				if (userSession != null) {
					int roleId = Integer.parseInt(userSession.getCurrent_role());
					if (roleId != 3) {
						String currentId="111";
						String parentId="0";
						
						modelAndView.addObject("roleId", roleId);

						int locationid = userSession.getLocation_id();
						modelAndView.addObject("locationid", locationid);
						System.out.println("locationid:::::::" + locationid);

						int locationtype = userSession.getLocation_type();
						modelAndView.addObject("locationtype", locationtype);
						System.out.println("locationtype:::::::" + locationtype);
						String locId = "";
						List<HashMap<String, String>> branchIds = new ArrayList<HashMap<String, String>>();

						String fromDate = cmlModel.getFromDt();
						String toDate = cmlModel.getToDt();
						String selectedBranchId = cmlModel.getBranchId();
						String regionId = cmlModel.getRegionId();
						modelAndView.addObject("branchIdList", selectedBranchId);
						String reg_id = "" + userSession.getLocation_id();
						if (regionId != null) {
							modelAndView.addObject("regionId1", regionId);
						} else {
							modelAndView.addObject("regionId1", 0);
						}

						modelAndView.addObject("branch_id", reg_id);
						System.out.println("branch id is " + selectedBranchId + "regionid" + regionId);

						SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
						String Date = formatter.format(new Date());// this is present date

						Calendar c = Calendar.getInstance();
						c.set(Calendar.DAY_OF_MONTH, 1);
						String startdateofMonth = formatter.format(c.getTime());
						System.out.println("start date is " + startdateofMonth);// Month ka start date hai
						

						if (cmlModel.getFromDt() == null && cmlModel.getToDt() == null) {
							System.out.println("Inside date is null");
							System.out.println("::::::::::::::::a::" + cmlModel.getFromDt());
							System.out.println("::::::::::::::::b::" + cmlModel.getToDt());
							fromDate = startdateofMonth;
							toDate = Date;
							modelAndView.addObject("fromDate", fromDate);
							modelAndView.addObject("toDate", toDate);
							System.out.println("fromDate date is " + fromDate);
							System.out.println("toDate date is " + toDate);
						} else {

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

						if (roleId != 7 && roleId != 4 && roleId != 5) {
							System.out.println("inside this");
							if (regionId == null && selectedBranchId == null
									|| regionId.equals("All") && selectedBranchId.equals("All")) {
								selectedBranchId = DGdao.getAllBranchIds();
								System.out.println("all selected branch id's are " + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt("All"));
							} else if (!regionId.equals("All") && selectedBranchId.equals("All")) {
								System.out.println("selectedBranchId123" + selectedBranchId);
								selectedBranchId = DGdao.getBranchIdsbylocationId(regionId);
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

							else {

								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

						} else if (roleId == 7) {
							if (regionId == null && selectedBranchId == null
									|| regionId == null && selectedBranchId.equals("All")) {

								selectedBranchId = DGdao
										.getBranchIdsbylocationId(String.valueOf(userSession.getLocation_id()));
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							} else {
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}
						} else {
							System.out.println("inside else");
							selectedBranchId = String.valueOf(userSession.getLocation_id());
							modelAndView.addObject("selectedBranchId11",
									crypt.Jcrypt(String.valueOf(userSession.getLocation_id())));
						}
						String StageName = DGserv.getStageNameDgreportsAIF(parentId,currentId);

						String ListQuery = DGserv.getBisReportListQueryDGreportsAIF(parentId,currentId);

						List<Map<String, Object>> bisReportQueryListModel = bisReportService
								.getBisListQueryResult(ListQuery, fromDate, toDate, selectedBranchId);
						request.setAttribute("BRLQL", bisReportQueryListModel);

						request.setAttribute("StageName", StageName);
						request.setAttribute("value", currentId);
						request.setAttribute("locationId", selectedBranchId);
						request.setAttribute("selBrnchName", selectedBranchId.replaceAll("_", " "));

						System.out.println("selectedBranchId::::::::::::..." + selectedBranchId);

						modelAndView.addObject("fromDate1", crypt.Jcrypt(fromDate));
						modelAndView.addObject("toDate1", crypt.Jcrypt(toDate));
						modelAndView.addObject("selectedBranchId1", crypt.Jcrypt(selectedBranchId));
						modelAndView.addObject("selBranchName", crypt.Jcrypt("Select Branch"));
						modelAndView.addObject("selectedBranchId", selectedBranchId);

						modelAndView.setViewName("Technicaldepartmentnumberlicenses");

						

					} else {
						modelAndView.setViewName("AccessDenied");
					}
				} else {

					modelAndView.setViewName("sessionExpire");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return modelAndView;
		}

		
		
		
		@RequestMapping(value = "/Industryscaleandcategorylicenses", method = RequestMethod.GET)
		public ModelAndView Industryscaleandcategorylicenses(@ModelAttribute("model1") AllCmlReportModel cmlModel, HttpServletRequest request) {

			ModelAndView modelAndView = new ModelAndView();

			try {
				HttpSession httpsession = request.getSession(false);
				Session userSession;
				userSession = (Session) httpsession.getAttribute("logged-in");
				if (userSession != null) {
					int roleId = Integer.parseInt(userSession.getCurrent_role());
					if (roleId != 3) {
						String currentId="127";
						String parentId="0";
						
						modelAndView.addObject("roleId", roleId);

						int locationid = userSession.getLocation_id();
						modelAndView.addObject("locationid", locationid);
						System.out.println("locationid:::::::" + locationid);

						int locationtype = userSession.getLocation_type();
						modelAndView.addObject("locationtype", locationtype);
						System.out.println("locationtype:::::::" + locationtype);
						String locId = "";
						List<HashMap<String, String>> branchIds = new ArrayList<HashMap<String, String>>();

						String fromDate = cmlModel.getFromDt();
						String toDate = cmlModel.getToDt();
						String selectedBranchId = cmlModel.getBranchId();
						String regionId = cmlModel.getRegionId();
						modelAndView.addObject("branchIdList", selectedBranchId);
						String reg_id = "" + userSession.getLocation_id();
						if (regionId != null) {
							modelAndView.addObject("regionId1", regionId);
						} else {
							modelAndView.addObject("regionId1", 0);
						}

						modelAndView.addObject("branch_id", reg_id);
						System.out.println("branch id is " + selectedBranchId + "regionid" + regionId);

						SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
						String Date = formatter.format(new Date());// this is present date

						Calendar c = Calendar.getInstance();
						c.set(Calendar.DAY_OF_MONTH, 1);
						String startdateofMonth = formatter.format(c.getTime());
						System.out.println("start date is " + startdateofMonth);// Month ka start date hai
						

						if (cmlModel.getFromDt() == null && cmlModel.getToDt() == null) {
							System.out.println("Inside date is null");
							System.out.println("::::::::::::::::a::" + cmlModel.getFromDt());
							System.out.println("::::::::::::::::b::" + cmlModel.getToDt());
							fromDate = startdateofMonth;
							toDate = Date;
							modelAndView.addObject("fromDate", fromDate);
							modelAndView.addObject("toDate", toDate);
							System.out.println("fromDate date is " + fromDate);
							System.out.println("toDate date is " + toDate);
						} else {
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

						if (roleId != 7 && roleId != 4 && roleId != 5) {
							System.out.println("inside this");
							if (regionId == null && selectedBranchId == null
									|| regionId.equals("All") && selectedBranchId.equals("All")) {
								selectedBranchId = DGdao.getAllBranchIds();
								System.out.println("all selected branch id's are " + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt("All"));
							} else if (!regionId.equals("All") && selectedBranchId.equals("All")) {
								System.out.println("selectedBranchId123" + selectedBranchId);
								selectedBranchId = DGdao.getBranchIdsbylocationId(regionId);
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

							else {

								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

						} else if (roleId == 7) {
							if (regionId == null && selectedBranchId == null
									|| regionId == null && selectedBranchId.equals("All")) {

								selectedBranchId = DGdao
										.getBranchIdsbylocationId(String.valueOf(userSession.getLocation_id()));
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							} else {
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}
						} else {
							System.out.println("inside else");
							selectedBranchId = String.valueOf(userSession.getLocation_id());
							modelAndView.addObject("selectedBranchId11",
									crypt.Jcrypt(String.valueOf(userSession.getLocation_id())));
						}
						String StageName = DGserv.getStageNameDgreportsAIF(parentId,currentId);

						String ListQuery = DGserv.getBisReportListQueryDGreportsAIF(parentId,currentId);

						List<Map<String, Object>> bisReportQueryListModel = bisReportService
								.getBisListQueryResult(ListQuery, fromDate, toDate, selectedBranchId);
						request.setAttribute("BRLQL", bisReportQueryListModel);

						request.setAttribute("StageName", StageName);
						request.setAttribute("value", currentId);
						request.setAttribute("locationId", selectedBranchId);
						request.setAttribute("selBrnchName", selectedBranchId.replaceAll("_", " "));

						System.out.println("selectedBranchId::::::::::::..." + selectedBranchId);

						modelAndView.addObject("fromDate1", crypt.Jcrypt(fromDate));
						modelAndView.addObject("toDate1", crypt.Jcrypt(toDate));
						modelAndView.addObject("selectedBranchId1", crypt.Jcrypt(selectedBranchId));
						modelAndView.addObject("selBranchName", crypt.Jcrypt("Select Branch"));
						modelAndView.addObject("selectedBranchId", selectedBranchId);

						modelAndView.setViewName("Industryscaleandcategorylicenses");

						

					} else {
						modelAndView.setViewName("AccessDenied");
					}
				} else {

					modelAndView.setViewName("sessionExpire");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return modelAndView;
		}
		
		
		
		
		@RequestMapping(value = "/AgencyBranchPerformancereport", method = RequestMethod.GET)
		public ModelAndView AgencyBranchPerformancereport(@ModelAttribute("model1") AllCmlReportModel cmlModel, HttpServletRequest request) {

			ModelAndView modelAndView = new ModelAndView();

			try {
				HttpSession httpsession = request.getSession(false);
				Session userSession;
				userSession = (Session) httpsession.getAttribute("logged-in");
				if (userSession != null) {
					int roleId = Integer.parseInt(userSession.getCurrent_role());
					if (roleId != 3) {
						String currentId="133";
						String parentId="0";
						
						modelAndView.addObject("roleId", roleId);

						int locationid = userSession.getLocation_id();
						modelAndView.addObject("locationid", locationid);
						System.out.println("locationid:::::::" + locationid);

						int locationtype = userSession.getLocation_type();
						modelAndView.addObject("locationtype", locationtype);
						System.out.println("locationtype:::::::" + locationtype);
						String locId = "";
						List<HashMap<String, String>> branchIds = new ArrayList<HashMap<String, String>>();

						String fromDate = cmlModel.getFromDt();
						String toDate = cmlModel.getToDt();
						String selectedBranchId = cmlModel.getBranchId();
						String regionId = cmlModel.getRegionId();
						modelAndView.addObject("branchIdList", selectedBranchId);
						String reg_id = "" + userSession.getLocation_id();
						if (regionId != null) {
							modelAndView.addObject("regionId1", regionId);
						} else {
							modelAndView.addObject("regionId1", 0);
						}

						modelAndView.addObject("branch_id", reg_id);
						System.out.println("branch id is " + selectedBranchId + "regionid" + regionId);

						SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
						String Date = formatter.format(new Date());// this is present date

						Calendar c = Calendar.getInstance();
						c.set(Calendar.DAY_OF_MONTH, 1);
						String startdateofMonth = formatter.format(c.getTime());
						System.out.println("start date is " + startdateofMonth);// Month ka start date hai
						

						if (cmlModel.getFromDt() == null && cmlModel.getToDt() == null) {
							System.out.println("Inside date is null");
							System.out.println("::::::::::::::::a::" + cmlModel.getFromDt());
							System.out.println("::::::::::::::::b::" + cmlModel.getToDt());
							fromDate = startdateofMonth;
							toDate = Date;
							modelAndView.addObject("fromDate", fromDate);
							modelAndView.addObject("toDate", toDate);
							System.out.println("fromDate date is " + fromDate);
							System.out.println("toDate date is " + toDate);
						} else {
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

						if (roleId != 7 && roleId != 4 && roleId != 5) 
						{
							System.out.println("inside this");
							if (regionId == null && selectedBranchId == null
									|| regionId.equals("All") && selectedBranchId.equals("All")) {
								selectedBranchId = DGdao.getAllBranchIds();
								System.out.println("all selected branch id's are " + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt("All"));
							} else if (!regionId.equals("All") && selectedBranchId.equals("All")) {
								System.out.println("selectedBranchId123" + selectedBranchId);
								selectedBranchId = DGdao.getBranchIdsbylocationId(regionId);
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

							else 
							{
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

						}
						
						else if (roleId == 7) 
						{
							if (regionId == null && selectedBranchId == null
									|| regionId == null && selectedBranchId.equals("All")) {

								selectedBranchId = DGdao
										.getBranchIdsbylocationId(String.valueOf(userSession.getLocation_id()));
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							} else {
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}
						} 
						else
						{
							System.out.println("inside else");
							selectedBranchId = String.valueOf(userSession.getLocation_id());
							modelAndView.addObject("selectedBranchId11",
									crypt.Jcrypt(String.valueOf(userSession.getLocation_id())));
						}
						String StageName = DGserv.getStageNameDgreportsAIF(parentId,currentId);

						String ListQuery = DGserv.getBisReportListQueryDGreportsAIF(parentId,currentId);

						List<Map<String, Object>> bisReportQueryListModel = bisReportService
								.getBisListQueryResult(ListQuery, fromDate, toDate, selectedBranchId);
						request.setAttribute("BRLQL", bisReportQueryListModel);

						request.setAttribute("StageName", StageName);
						request.setAttribute("value", currentId);
						request.setAttribute("locationId", selectedBranchId);
						request.setAttribute("selBrnchName", selectedBranchId.replaceAll("_", " "));

						System.out.println("selectedBranchId::::::::::::..." + selectedBranchId);

						modelAndView.addObject("fromDate1", crypt.Jcrypt(fromDate));
						modelAndView.addObject("toDate1", crypt.Jcrypt(toDate));
						modelAndView.addObject("selectedBranchId1", crypt.Jcrypt(selectedBranchId));
						modelAndView.addObject("selBranchName", crypt.Jcrypt("Select Branch"));
						modelAndView.addObject("selectedBranchId", selectedBranchId);

						modelAndView.setViewName("Agencybranchofficerperformance");

						

					} else {
						modelAndView.setViewName("AccessDenied");
					}
				} else {

					modelAndView.setViewName("sessionExpire");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return modelAndView;
		}
		
				
		
     @RequestMapping(value = "/PendingInclusions", method = RequestMethod.GET)
		public ModelAndView PendingInclusions(@ModelAttribute("model1") AllCmlReportModel cmlModel, HttpServletRequest request) {

			ModelAndView modelAndView = new ModelAndView();

			try {
				HttpSession httpsession = request.getSession(false);
				Session userSession;
				userSession = (Session) httpsession.getAttribute("logged-in");
				if (userSession != null) {
					int roleId = Integer.parseInt(userSession.getCurrent_role());
					if (roleId != 3) {
						String currentId="139";
						String parentId="0";
						
						modelAndView.addObject("roleId", roleId);

						int locationid = userSession.getLocation_id();
						modelAndView.addObject("locationid", locationid);
						System.out.println("locationid:::::::" + locationid);

						int locationtype = userSession.getLocation_type();
						modelAndView.addObject("locationtype", locationtype);
						System.out.println("locationtype:::::::" + locationtype);
						String locId = "";
						List<HashMap<String, String>> branchIds = new ArrayList<HashMap<String, String>>();

						String fromDate = cmlModel.getFromDt();
						String toDate = cmlModel.getToDt();
						String selectedBranchId = cmlModel.getBranchId();
						String regionId = cmlModel.getRegionId();
						modelAndView.addObject("branchIdList", selectedBranchId);
						String reg_id = "" + userSession.getLocation_id();
						if (regionId != null) {
							modelAndView.addObject("regionId1", regionId);
						} else {
							modelAndView.addObject("regionId1", 0);
						}

						modelAndView.addObject("branch_id", reg_id);
						System.out.println("branch id is " + selectedBranchId + "regionid" + regionId);

						SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
						String Date = formatter.format(new Date());// this is present date

						Calendar c = Calendar.getInstance();
						c.set(Calendar.DAY_OF_MONTH, 1);
						String startdateofMonth = formatter.format(c.getTime());
						System.out.println("start date is " + startdateofMonth);// Month ka start date hai
						

						if (cmlModel.getFromDt() == null && cmlModel.getToDt() == null) {
							System.out.println("Inside date is null");
							System.out.println("::::::::::::::::a::" + cmlModel.getFromDt());
							System.out.println("::::::::::::::::b::" + cmlModel.getToDt());
							fromDate = startdateofMonth;
							toDate = Date;
							modelAndView.addObject("fromDate", fromDate);
							modelAndView.addObject("toDate", toDate);
							System.out.println("fromDate date is " + fromDate);
							System.out.println("toDate date is " + toDate);
						} else {
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

						if (roleId != 7 && roleId != 4 && roleId != 5) {
							System.out.println("inside this");
							if (regionId == null && selectedBranchId == null
									|| regionId.equals("All") && selectedBranchId.equals("All")) {
								selectedBranchId = DGdao.getAllBranchIds();
								System.out.println("all selected branch id's are " + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt("All"));
							} else if (!regionId.equals("All") && selectedBranchId.equals("All")) {
								System.out.println("selectedBranchId123" + selectedBranchId);
								selectedBranchId = DGdao.getBranchIdsbylocationId(regionId);
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

							else {

								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

						} else if (roleId == 7) {
							if (regionId == null && selectedBranchId == null
									|| regionId == null && selectedBranchId.equals("All")) {

								selectedBranchId = DGdao
										.getBranchIdsbylocationId(String.valueOf(userSession.getLocation_id()));
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							} else {
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}
						} else {
							System.out.println("inside else");
							selectedBranchId = String.valueOf(userSession.getLocation_id());
							modelAndView.addObject("selectedBranchId11",
									crypt.Jcrypt(String.valueOf(userSession.getLocation_id())));
						}
						String StageName = DGserv.getStageNameDgreportsAIF(parentId,currentId);

						String ListQuery = DGserv.getBisReportListQueryDGreportsAIF(parentId,currentId);

						List<Map<String, Object>> bisReportQueryListModel = bisReportService.getBisListQueryResult(ListQuery, fromDate, toDate, selectedBranchId);
						request.setAttribute("BRLQL", bisReportQueryListModel);
						request.setAttribute("StageName", StageName);
						request.setAttribute("value", currentId);
						request.setAttribute("locationId", selectedBranchId);
						request.setAttribute("selBrnchName", selectedBranchId.replaceAll("_", " "));
						System.out.println("selectedBranchId::::::::::::..." + selectedBranchId);
						modelAndView.addObject("fromDate1", crypt.Jcrypt(fromDate));
						modelAndView.addObject("toDate1", crypt.Jcrypt(toDate));
						modelAndView.addObject("selectedBranchId1", crypt.Jcrypt(selectedBranchId));
						modelAndView.addObject("selBranchName", crypt.Jcrypt("Select Branch"));
						modelAndView.addObject("selectedBranchId", selectedBranchId);
						modelAndView.setViewName("PendingInclusions");
					} else {
						modelAndView.setViewName("AccessDenied");
					}
				} else {

					modelAndView.setViewName("sessionExpire");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return modelAndView;
		}
		
     
     
     
     
     
     
     
     
     
 	
     @RequestMapping(value = "/JewelerMarketSurveillanceCompleted", method = RequestMethod.GET)
		public ModelAndView JewelerMarketSurveillanceCompleted(@ModelAttribute("model1") AllCmlReportModel cmlModel, HttpServletRequest request) {

			ModelAndView modelAndView = new ModelAndView();

			try {
				HttpSession httpsession = request.getSession(false);
				Session userSession;
				userSession = (Session) httpsession.getAttribute("logged-in");
				if (userSession != null) {
					int roleId = Integer.parseInt(userSession.getCurrent_role());
					if (roleId != 3) {
						String currentId="1002";
						String parentId="0";
						
						modelAndView.addObject("roleId", roleId);

						int locationid = userSession.getLocation_id();
						modelAndView.addObject("locationid", locationid);
						System.out.println("locationid:::::::" + locationid);

						int locationtype = userSession.getLocation_type();
						modelAndView.addObject("locationtype", locationtype);
						System.out.println("locationtype:::::::" + locationtype);
						String locId = "";
						List<HashMap<String, String>> branchIds = new ArrayList<HashMap<String, String>>();

						String fromDate = cmlModel.getFromDt();
						String toDate = cmlModel.getToDt();
						String selectedBranchId = cmlModel.getBranchId();
						String regionId = cmlModel.getRegionId();
						modelAndView.addObject("branchIdList", selectedBranchId);
						String reg_id = "" + userSession.getLocation_id();
						if (regionId != null) {
							modelAndView.addObject("regionId1", regionId);
						} else {
							modelAndView.addObject("regionId1", 0);
						}

						modelAndView.addObject("branch_id", reg_id);
						System.out.println("branch id is " + selectedBranchId + "regionid" + regionId);

						SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
						String Date = formatter.format(new Date());// this is present date

						Calendar c = Calendar.getInstance();
						c.set(Calendar.DAY_OF_MONTH, 1);
						String startdateofMonth = formatter.format(c.getTime());
						System.out.println("start date is " + startdateofMonth);// Month ka start date hai
						

						if (cmlModel.getFromDt() == null && cmlModel.getToDt() == null) {
							System.out.println("Inside date is null");
							System.out.println("::::::::::::::::a::" + cmlModel.getFromDt());
							System.out.println("::::::::::::::::b::" + cmlModel.getToDt());
							fromDate = startdateofMonth;
							toDate = Date;
							modelAndView.addObject("fromDate", fromDate);
							modelAndView.addObject("toDate", toDate);
							System.out.println("fromDate date is " + fromDate);
							System.out.println("toDate date is " + toDate);
						} else {
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

						if (roleId != 7 && roleId != 4 && roleId != 5) {
							System.out.println("inside this");
							if (regionId == null && selectedBranchId == null
									|| regionId.equals("All") && selectedBranchId.equals("All")) {
								selectedBranchId = DGdao.getAllBranchIds();
								System.out.println("all selected branch id's are " + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt("All"));
							} else if (!regionId.equals("All") && selectedBranchId.equals("All")) {
								System.out.println("selectedBranchId123" + selectedBranchId);
								selectedBranchId = DGdao.getBranchIdsbylocationId(regionId);
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

							else {

								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

						} else if (roleId == 7) {
							if (regionId == null && selectedBranchId == null
									|| regionId == null && selectedBranchId.equals("All")) {

								selectedBranchId = DGdao
										.getBranchIdsbylocationId(String.valueOf(userSession.getLocation_id()));
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							} else {
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}
						} else {
							System.out.println("inside else");
							selectedBranchId = String.valueOf(userSession.getLocation_id());
							modelAndView.addObject("selectedBranchId11",
									crypt.Jcrypt(String.valueOf(userSession.getLocation_id())));
						}
						String StageName = DGserv.getStageNameDgreportsAIF(parentId,currentId);

						String ListQuery = DGserv.getBisReportListQueryDGreportsAIF(parentId,currentId);

						List<Map<String, Object>> bisReportQueryListModel = bisReportService.getBisListQueryResult(ListQuery, fromDate, toDate, selectedBranchId);
						request.setAttribute("BRLQL", bisReportQueryListModel);
						request.setAttribute("StageName", StageName);
						request.setAttribute("value", currentId);
						request.setAttribute("locationId", selectedBranchId);
						request.setAttribute("selBrnchName", selectedBranchId.replaceAll("_", " "));
						System.out.println("selectedBranchId::::::::::::..." + selectedBranchId);
						modelAndView.addObject("fromDate1", crypt.Jcrypt(fromDate));
						modelAndView.addObject("toDate1", crypt.Jcrypt(toDate));
						modelAndView.addObject("selectedBranchId1", crypt.Jcrypt(selectedBranchId));
						modelAndView.addObject("selBranchName", crypt.Jcrypt("Select Branch"));
						modelAndView.addObject("selectedBranchId", selectedBranchId);
						modelAndView.setViewName("JewelerMarketSurveillanceCompleted");
					} else {
						modelAndView.setViewName("AccessDenied");
					}
				} else {

					modelAndView.setViewName("sessionExpire");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return modelAndView;
		}
     

	
   		  
     @RequestMapping(value = "/MarketSurveillanceBo", method = RequestMethod.GET)
		public ModelAndView MarketSurveillanceBo(@ModelAttribute("model1") AllCmlReportModel cmlModel, HttpServletRequest request) {

			ModelAndView modelAndView = new ModelAndView();

			try {
				HttpSession httpsession = request.getSession(false);
				Session userSession;
				userSession = (Session) httpsession.getAttribute("logged-in");
				if (userSession != null) {
					int roleId = Integer.parseInt(userSession.getCurrent_role());
					if (roleId != 3) {
						String currentId="1005";
						String parentId="0";
						
						modelAndView.addObject("roleId", roleId);

						int locationid = userSession.getLocation_id();
						modelAndView.addObject("locationid", locationid);
						System.out.println("locationid:::::::" + locationid);

						int locationtype = userSession.getLocation_type();
						modelAndView.addObject("locationtype", locationtype);
						System.out.println("locationtype:::::::" + locationtype);
						String locId = "";
						List<HashMap<String, String>> branchIds = new ArrayList<HashMap<String, String>>();

						String fromDate = cmlModel.getFromDt();
						String toDate = cmlModel.getToDt();
						String selectedBranchId = cmlModel.getBranchId();
						String regionId = cmlModel.getRegionId();
						modelAndView.addObject("branchIdList", selectedBranchId);
						String reg_id = "" + userSession.getLocation_id();
						if (regionId != null) {
							modelAndView.addObject("regionId1", regionId);
						} else {
							modelAndView.addObject("regionId1", 0);
						}

						modelAndView.addObject("branch_id", reg_id);
						System.out.println("branch id is " + selectedBranchId + "regionid" + regionId);

						SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
						String Date = formatter.format(new Date());// this is present date

						Calendar c = Calendar.getInstance();
						c.set(Calendar.DAY_OF_MONTH, 1);
						String startdateofMonth = formatter.format(c.getTime());
						System.out.println("start date is " + startdateofMonth);// Month ka start date hai
						

						if (cmlModel.getFromDt() == null && cmlModel.getToDt() == null) {
							System.out.println("Inside date is null");
							System.out.println("::::::::::::::::a::" + cmlModel.getFromDt());
							System.out.println("::::::::::::::::b::" + cmlModel.getToDt());
							fromDate = startdateofMonth;
							toDate = Date;
							modelAndView.addObject("fromDate", fromDate);
							modelAndView.addObject("toDate", toDate);
							System.out.println("fromDate date is " + fromDate);
							System.out.println("toDate date is " + toDate);
						} else {
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

						if (roleId != 7 && roleId != 4 && roleId != 5) {
							System.out.println("inside this");
							if (regionId == null && selectedBranchId == null
									|| regionId.equals("All") && selectedBranchId.equals("All")) {
								selectedBranchId = DGdao.getAllBranchIds();
								System.out.println("all selected branch id's are " + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt("All"));
							} else if (!regionId.equals("All") && selectedBranchId.equals("All")) {
								System.out.println("selectedBranchId123" + selectedBranchId);
								selectedBranchId = DGdao.getBranchIdsbylocationId(regionId);
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

							else {

								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

						} else if (roleId == 7) {
							if (regionId == null && selectedBranchId == null
									|| regionId == null && selectedBranchId.equals("All")) {

								selectedBranchId = DGdao
										.getBranchIdsbylocationId(String.valueOf(userSession.getLocation_id()));
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							} else {
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}
						} else {
							System.out.println("inside else");
							selectedBranchId = String.valueOf(userSession.getLocation_id());
							modelAndView.addObject("selectedBranchId11",
									crypt.Jcrypt(String.valueOf(userSession.getLocation_id())));
						}
						String StageName = DGserv.getStageNameDgreportsAIF(parentId,currentId);

						String ListQuery = DGserv.getBisReportListQueryDGreportsAIF(parentId,currentId);

						List<Map<String, Object>> bisReportQueryListModel = bisReportService.getBisListQueryResult(ListQuery, fromDate, toDate, selectedBranchId);
						request.setAttribute("BRLQL", bisReportQueryListModel);
						request.setAttribute("StageName", StageName);
						request.setAttribute("value", currentId);
						request.setAttribute("locationId", selectedBranchId);
						request.setAttribute("selBrnchName", selectedBranchId.replaceAll("_", " "));
						System.out.println("selectedBranchId::::::::::::..." + selectedBranchId);
						modelAndView.addObject("fromDate1", crypt.Jcrypt(fromDate));
						modelAndView.addObject("toDate1", crypt.Jcrypt(toDate));
						modelAndView.addObject("selectedBranchId1", crypt.Jcrypt(selectedBranchId));
						modelAndView.addObject("selBranchName", crypt.Jcrypt("Select Branch"));
						modelAndView.addObject("selectedBranchId", selectedBranchId);
						modelAndView.setViewName("MarketSurveillanceBo");
					} else {
						modelAndView.setViewName("AccessDenied");
					}
				} else {

					modelAndView.setViewName("sessionExpire");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return modelAndView;
		}
     

     
     
     
     
     @RequestMapping(value = "/FactorySurveillanceCo", method = RequestMethod.GET)
		public ModelAndView FactorySurveillanceCo(@ModelAttribute("model1") AllCmlReportModel cmlModel, HttpServletRequest request) {

			ModelAndView modelAndView = new ModelAndView();

			try {
				HttpSession httpsession = request.getSession(false);
				Session userSession;
				userSession = (Session) httpsession.getAttribute("logged-in");
				if (userSession != null) {
					int roleId = Integer.parseInt(userSession.getCurrent_role());
					if (roleId != 3) {
						String currentId="1008";
						String parentId="0";
						
						modelAndView.addObject("roleId", roleId);

						int locationid = userSession.getLocation_id();
						modelAndView.addObject("locationid", locationid);
						System.out.println("locationid:::::::" + locationid);

						int locationtype = userSession.getLocation_type();
						modelAndView.addObject("locationtype", locationtype);
						System.out.println("locationtype:::::::" + locationtype);
						String locId = "";
						List<HashMap<String, String>> branchIds = new ArrayList<HashMap<String, String>>();

						String fromDate = cmlModel.getFromDt();
						String toDate = cmlModel.getToDt();
						String selectedBranchId = cmlModel.getBranchId();
						String regionId = cmlModel.getRegionId();
						modelAndView.addObject("branchIdList", selectedBranchId);
						String reg_id = "" + userSession.getLocation_id();
						if (regionId != null) {
							modelAndView.addObject("regionId1", regionId);
						} else {
							modelAndView.addObject("regionId1", 0);
						}

						modelAndView.addObject("branch_id", reg_id);
						System.out.println("branch id is " + selectedBranchId + "regionid" + regionId);

						SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
						String Date = formatter.format(new Date());// this is present date

						Calendar c = Calendar.getInstance();
						c.set(Calendar.DAY_OF_MONTH, 1);
						String startdateofMonth = formatter.format(c.getTime());
						System.out.println("start date is " + startdateofMonth);// Month ka start date hai
						

						if (cmlModel.getFromDt() == null && cmlModel.getToDt() == null) {
							System.out.println("Inside date is null");
							System.out.println("::::::::::::::::a::" + cmlModel.getFromDt());
							System.out.println("::::::::::::::::b::" + cmlModel.getToDt());
							fromDate = startdateofMonth;
							toDate = Date;
							modelAndView.addObject("fromDate", fromDate);
							modelAndView.addObject("toDate", toDate);
							System.out.println("fromDate date is " + fromDate);
							System.out.println("toDate date is " + toDate);
						} else {
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

						if (roleId != 7 && roleId != 4 && roleId != 5) {
							System.out.println("inside this");
							if (regionId == null && selectedBranchId == null
									|| regionId.equals("All") && selectedBranchId.equals("All")) {
								selectedBranchId = DGdao.getAllBranchIds();
								System.out.println("all selected branch id's are " + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt("All"));
							} else if (!regionId.equals("All") && selectedBranchId.equals("All")) {
								System.out.println("selectedBranchId123" + selectedBranchId);
								selectedBranchId = DGdao.getBranchIdsbylocationId(regionId);
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

							else {

								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

						} else if (roleId == 7) {
							if (regionId == null && selectedBranchId == null
									|| regionId == null && selectedBranchId.equals("All")) {

								selectedBranchId = DGdao
										.getBranchIdsbylocationId(String.valueOf(userSession.getLocation_id()));
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							} else {
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}
						} else {
							System.out.println("inside else");
							selectedBranchId = String.valueOf(userSession.getLocation_id());
							modelAndView.addObject("selectedBranchId11",
									crypt.Jcrypt(String.valueOf(userSession.getLocation_id())));
						}
						String StageName = DGserv.getStageNameDgreportsAIF(parentId,currentId);

						String ListQuery = DGserv.getBisReportListQueryDGreportsAIF(parentId,currentId);

						List<Map<String, Object>> bisReportQueryListModel = bisReportService.getBisListQueryResult(ListQuery, fromDate, toDate, selectedBranchId);
						request.setAttribute("BRLQL", bisReportQueryListModel);
						request.setAttribute("StageName", StageName);
						request.setAttribute("value", currentId);
						request.setAttribute("locationId", selectedBranchId);
						request.setAttribute("selBrnchName", selectedBranchId.replaceAll("_", " "));
						System.out.println("selectedBranchId::::::::::::..." + selectedBranchId);
						modelAndView.addObject("fromDate1", crypt.Jcrypt(fromDate));
						modelAndView.addObject("toDate1", crypt.Jcrypt(toDate));
						modelAndView.addObject("selectedBranchId1", crypt.Jcrypt(selectedBranchId));
						modelAndView.addObject("selBranchName", crypt.Jcrypt("Select Branch"));
						modelAndView.addObject("selectedBranchId", selectedBranchId);
						modelAndView.setViewName("FactorySurveillanceCo");
					} else {
						modelAndView.setViewName("AccessDenied");
					}
				} else {

					modelAndView.setViewName("sessionExpire");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return modelAndView;
		}
  
     
     
     @RequestMapping(value = "/FactorySurveillanceOsa", method = RequestMethod.GET)
 		public ModelAndView FactorySurveillanceOsa(@ModelAttribute("model1") AllCmlReportModel cmlModel, HttpServletRequest request) {

 			ModelAndView modelAndView = new ModelAndView();

 			try {
 				HttpSession httpsession = request.getSession(false);
 				Session userSession;
 				userSession = (Session) httpsession.getAttribute("logged-in");
 				if (userSession != null) {
 					int roleId = Integer.parseInt(userSession.getCurrent_role());
 					if (roleId != 3) {
 						String currentId="1011";
 						String parentId="0";
 						
 						modelAndView.addObject("roleId", roleId);

 						int locationid = userSession.getLocation_id();
 						modelAndView.addObject("locationid", locationid);
 						System.out.println("locationid:::::::" + locationid);

 						int locationtype = userSession.getLocation_type();
 						modelAndView.addObject("locationtype", locationtype);
 						System.out.println("locationtype:::::::" + locationtype);
 						String locId = "";
 						List<HashMap<String, String>> branchIds = new ArrayList<HashMap<String, String>>();

 						String fromDate = cmlModel.getFromDt();
 						String toDate = cmlModel.getToDt();
 						String selectedBranchId = cmlModel.getBranchId();
 						String regionId = cmlModel.getRegionId();
 						modelAndView.addObject("branchIdList", selectedBranchId);
 						String reg_id = "" + userSession.getLocation_id();
 						if (regionId != null) {
 							modelAndView.addObject("regionId1", regionId);
 						} else {
 							modelAndView.addObject("regionId1", 0);
 						}

 						modelAndView.addObject("branch_id", reg_id);
 						System.out.println("branch id is " + selectedBranchId + "regionid" + regionId);

 						SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
 						String Date = formatter.format(new Date());// this is present date

 						Calendar c = Calendar.getInstance();
 						c.set(Calendar.DAY_OF_MONTH, 1);
 						String startdateofMonth = formatter.format(c.getTime());
 						System.out.println("start date is " + startdateofMonth);// Month ka start date hai
 						

 						if (cmlModel.getFromDt() == null && cmlModel.getToDt() == null) {
 							System.out.println("Inside date is null");
 							System.out.println("::::::::::::::::a::" + cmlModel.getFromDt());
 							System.out.println("::::::::::::::::b::" + cmlModel.getToDt());
 							fromDate = startdateofMonth;
 							toDate = Date;
 							modelAndView.addObject("fromDate", fromDate);
 							modelAndView.addObject("toDate", toDate);
 							System.out.println("fromDate date is " + fromDate);
 							System.out.println("toDate date is " + toDate);
 						} else {
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

 						if (roleId != 7 && roleId != 4 && roleId != 5) {
 							System.out.println("inside this");
 							if (regionId == null && selectedBranchId == null
 									|| regionId.equals("All") && selectedBranchId.equals("All")) {
 								selectedBranchId = DGdao.getAllBranchIds();
 								System.out.println("all selected branch id's are " + selectedBranchId);
 								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt("All"));
 							} else if (!regionId.equals("All") && selectedBranchId.equals("All")) {
 								System.out.println("selectedBranchId123" + selectedBranchId);
 								selectedBranchId = DGdao.getBranchIdsbylocationId(regionId);
 								System.out.println("selectedBranchId" + selectedBranchId);
 								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
 							}

 							else {

 								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
 							}

 						} else if (roleId == 7) {
 							if (regionId == null && selectedBranchId == null
 									|| regionId == null && selectedBranchId.equals("All")) {

 								selectedBranchId = DGdao
 										.getBranchIdsbylocationId(String.valueOf(userSession.getLocation_id()));
 								System.out.println("selectedBranchId" + selectedBranchId);
 								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
 							} else {
 								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
 							}
 						} else {
 							System.out.println("inside else");
 							selectedBranchId = String.valueOf(userSession.getLocation_id());
 							modelAndView.addObject("selectedBranchId11",
 									crypt.Jcrypt(String.valueOf(userSession.getLocation_id())));
 						}
 						String StageName = DGserv.getStageNameDgreportsAIF(parentId,currentId);

 						String ListQuery = DGserv.getBisReportListQueryDGreportsAIF(parentId,currentId);

 						List<Map<String, Object>> bisReportQueryListModel = bisReportService.getBisListQueryResult(ListQuery, fromDate, toDate, selectedBranchId);
 						request.setAttribute("BRLQL", bisReportQueryListModel);
 						request.setAttribute("StageName", StageName);
 						request.setAttribute("value", currentId);
 						request.setAttribute("locationId", selectedBranchId);
 						request.setAttribute("selBrnchName", selectedBranchId.replaceAll("_", " "));
 						System.out.println("selectedBranchId::::::::::::..." + selectedBranchId);
 						modelAndView.addObject("fromDate1", crypt.Jcrypt(fromDate));
 						modelAndView.addObject("toDate1", crypt.Jcrypt(toDate));
 						modelAndView.addObject("selectedBranchId1", crypt.Jcrypt(selectedBranchId));
 						modelAndView.addObject("selBranchName", crypt.Jcrypt("Select Branch"));
 						modelAndView.addObject("selectedBranchId", selectedBranchId);
 						modelAndView.setViewName("FactorySurveillanceOsa");
 					} else {
 						modelAndView.setViewName("AccessDenied");
 					}
 				} else {

 					modelAndView.setViewName("sessionExpire");
 				}

 			} catch (Exception e) {
 				e.printStackTrace();
 			}
 			return modelAndView;
 		}
     
     //factory Surveillance Bo Inspection
     
     @RequestMapping(value = "/FactorySurveillanceBoInspection", method = RequestMethod.GET)
 		public ModelAndView FactorySurveillanceBoInspection(@ModelAttribute("model1") AllCmlReportModel cmlModel, HttpServletRequest request) {

 			ModelAndView modelAndView = new ModelAndView();

 			try {
 				HttpSession httpsession = request.getSession(false);
 				Session userSession;
 				userSession = (Session) httpsession.getAttribute("logged-in");
 				if (userSession != null) {
 					int roleId = Integer.parseInt(userSession.getCurrent_role());
 					if (roleId != 3) {
 						String currentId="1015";
 						String parentId="0";
 						
 						modelAndView.addObject("roleId", roleId);

 						int locationid = userSession.getLocation_id();
 						modelAndView.addObject("locationid", locationid);
 						System.out.println("locationid:::::::" + locationid);

 						int locationtype = userSession.getLocation_type();
 						modelAndView.addObject("locationtype", locationtype);
 						System.out.println("locationtype:::::::" + locationtype);
 						String locId = "";
 						List<HashMap<String, String>> branchIds = new ArrayList<HashMap<String, String>>();

 						String fromDate = cmlModel.getFromDt();
 						String toDate = cmlModel.getToDt();
 						String selectedBranchId = cmlModel.getBranchId();
 						String regionId = cmlModel.getRegionId();
 						modelAndView.addObject("branchIdList", selectedBranchId);
 						String reg_id = "" + userSession.getLocation_id();
 						if (regionId != null) {
 							modelAndView.addObject("regionId1", regionId);
 						} else {
 							modelAndView.addObject("regionId1", 0);
 						}

 						modelAndView.addObject("branch_id", reg_id);
 						System.out.println("branch id is " + selectedBranchId + "regionid" + regionId);

 						SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
 						String Date = formatter.format(new Date());// this is present date

 						Calendar c = Calendar.getInstance();
 						c.set(Calendar.DAY_OF_MONTH, 1);
 						String startdateofMonth = formatter.format(c.getTime());
 						System.out.println("start date is " + startdateofMonth);// Month ka start date hai
 						

 						if (cmlModel.getFromDt() == null && cmlModel.getToDt() == null) {
 							System.out.println("Inside date is null");
 							System.out.println("::::::::::::::::a::" + cmlModel.getFromDt());
 							System.out.println("::::::::::::::::b::" + cmlModel.getToDt());
 							fromDate = startdateofMonth;
 							toDate = Date;
 							modelAndView.addObject("fromDate", fromDate);
 							modelAndView.addObject("toDate", toDate);
 							System.out.println("fromDate date is " + fromDate);
 							System.out.println("toDate date is " + toDate);
 						} else {
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

 						if (roleId != 7 && roleId != 4 && roleId != 5) {
 							System.out.println("inside this");
 							if (regionId == null && selectedBranchId == null
 									|| regionId.equals("All") && selectedBranchId.equals("All")) {
 								selectedBranchId = DGdao.getAllBranchIds();
 								System.out.println("all selected branch id's are " + selectedBranchId);
 								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt("All"));
 							} else if (!regionId.equals("All") && selectedBranchId.equals("All")) {
 								System.out.println("selectedBranchId123" + selectedBranchId);
 								selectedBranchId = DGdao.getBranchIdsbylocationId(regionId);
 								System.out.println("selectedBranchId" + selectedBranchId);
 								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
 							}

 							else {

 								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
 							}

 						} else if (roleId == 7) {
 							if (regionId == null && selectedBranchId == null
 									|| regionId == null && selectedBranchId.equals("All")) {

 								selectedBranchId = DGdao
 										.getBranchIdsbylocationId(String.valueOf(userSession.getLocation_id()));
 								System.out.println("selectedBranchId" + selectedBranchId);
 								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
 							} else {
 								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
 							}
 						} else {
 							System.out.println("inside else");
 							selectedBranchId = String.valueOf(userSession.getLocation_id());
 							modelAndView.addObject("selectedBranchId11",
 									crypt.Jcrypt(String.valueOf(userSession.getLocation_id())));
 						}
 						String StageName = DGserv.getStageNameDgreportsAIF(parentId,currentId);

 						String ListQuery = DGserv.getBisReportListQueryDGreportsAIF(parentId,currentId);

 						List<Map<String, Object>> bisReportQueryListModel = bisReportService.getBisListQueryResult(ListQuery, fromDate, toDate, selectedBranchId);
 						request.setAttribute("BRLQL", bisReportQueryListModel);
 						request.setAttribute("StageName", StageName);
 						request.setAttribute("value", currentId);
 						request.setAttribute("locationId", selectedBranchId);
 						request.setAttribute("selBrnchName", selectedBranchId.replaceAll("_", " "));
 						System.out.println("selectedBranchId::::::::::::..." + selectedBranchId);
 						modelAndView.addObject("fromDate1", crypt.Jcrypt(fromDate));
 						modelAndView.addObject("toDate1", crypt.Jcrypt(toDate));
 						modelAndView.addObject("selectedBranchId1", crypt.Jcrypt(selectedBranchId));
 						modelAndView.addObject("selBranchName", crypt.Jcrypt("Select Branch"));
 						modelAndView.addObject("selectedBranchId", selectedBranchId);
 						modelAndView.setViewName("FactorySurveillanceBoInspection");
 					} else {
 						modelAndView.setViewName("AccessDenied");
 					}
 				} else {

 					modelAndView.setViewName("sessionExpire");
 				}

 			} catch (Exception e) {
 				e.printStackTrace();
 			}
 			return modelAndView;
 		}
     
     
     //factory Surveillance Bo sample
     
     @RequestMapping(value = "/FactorySurveillanceBoSample", method = RequestMethod.GET)
 		public ModelAndView FactorySurveillanceBoSample(@ModelAttribute("model1") AllCmlReportModel cmlModel, HttpServletRequest request) {

 			ModelAndView modelAndView = new ModelAndView();

 			try {
 				HttpSession httpsession = request.getSession(false);
 				Session userSession;
 				userSession = (Session) httpsession.getAttribute("logged-in");
 				if (userSession != null) {
 					int roleId = Integer.parseInt(userSession.getCurrent_role());
 					if (roleId != 3) {
 						String currentId="1014";
 						String parentId="0";
 						
 						modelAndView.addObject("roleId", roleId);

 						int locationid = userSession.getLocation_id();
 						modelAndView.addObject("locationid", locationid);
 						System.out.println("locationid:::::::" + locationid);

 						int locationtype = userSession.getLocation_type();
 						modelAndView.addObject("locationtype", locationtype);
 						System.out.println("locationtype:::::::" + locationtype);
 						String locId = "";
 						List<HashMap<String, String>> branchIds = new ArrayList<HashMap<String, String>>();

 						String fromDate = cmlModel.getFromDt();
 						String toDate = cmlModel.getToDt();
 						String selectedBranchId = cmlModel.getBranchId();
 						String regionId = cmlModel.getRegionId();
 						modelAndView.addObject("branchIdList", selectedBranchId);
 						String reg_id = "" + userSession.getLocation_id();
 						if (regionId != null) {
 							modelAndView.addObject("regionId1", regionId);
 						} else {
 							modelAndView.addObject("regionId1", 0);
 						}

 						modelAndView.addObject("branch_id", reg_id);
 						System.out.println("branch id is " + selectedBranchId + "regionid" + regionId);

 						SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
 						String Date = formatter.format(new Date());// this is present date

 						Calendar c = Calendar.getInstance();
 						c.set(Calendar.DAY_OF_MONTH, 1);
 						String startdateofMonth = formatter.format(c.getTime());
 						System.out.println("start date is " + startdateofMonth);// Month ka start date hai
 						

 						if (cmlModel.getFromDt() == null && cmlModel.getToDt() == null) {
 							System.out.println("Inside date is null");
 							System.out.println("::::::::::::::::a::" + cmlModel.getFromDt());
 							System.out.println("::::::::::::::::b::" + cmlModel.getToDt());
 							fromDate = startdateofMonth;
 							toDate = Date;
 							modelAndView.addObject("fromDate", fromDate);
 							modelAndView.addObject("toDate", toDate);
 							System.out.println("fromDate date is " + fromDate);
 							System.out.println("toDate date is " + toDate);
 						} else {
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

 						if (roleId != 7 && roleId != 4 && roleId != 5) {
 							System.out.println("inside this");
 							if (regionId == null && selectedBranchId == null
 									|| regionId.equals("All") && selectedBranchId.equals("All")) {
 								selectedBranchId = DGdao.getAllBranchIds();
 								System.out.println("all selected branch id's are " + selectedBranchId);
 								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt("All"));
 							} else if (!regionId.equals("All") && selectedBranchId.equals("All")) {
 								System.out.println("selectedBranchId123" + selectedBranchId);
 								selectedBranchId = DGdao.getBranchIdsbylocationId(regionId);
 								System.out.println("selectedBranchId" + selectedBranchId);
 								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
 							}

 							else {

 								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
 							}

 						} else if (roleId == 7) {
 							if (regionId == null && selectedBranchId == null
 									|| regionId == null && selectedBranchId.equals("All")) {

 								selectedBranchId = DGdao
 										.getBranchIdsbylocationId(String.valueOf(userSession.getLocation_id()));
 								System.out.println("selectedBranchId" + selectedBranchId);
 								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
 							} else {
 								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
 							}
 						} else {
 							System.out.println("inside else");
 							selectedBranchId = String.valueOf(userSession.getLocation_id());
 							modelAndView.addObject("selectedBranchId11",
 									crypt.Jcrypt(String.valueOf(userSession.getLocation_id())));
 						}
 						String StageName = DGserv.getStageNameDgreportsAIF(parentId,currentId);

 						String ListQuery = DGserv.getBisReportListQueryDGreportsAIF(parentId,currentId);

 						List<Map<String, Object>> bisReportQueryListModel = bisReportService.getBisListQueryResult(ListQuery, fromDate, toDate, selectedBranchId);
 						request.setAttribute("BRLQL", bisReportQueryListModel);
 						request.setAttribute("StageName", StageName);
 						request.setAttribute("value", currentId);
 						request.setAttribute("locationId", selectedBranchId);
 						request.setAttribute("selBrnchName", selectedBranchId.replaceAll("_", " "));
 						System.out.println("selectedBranchId::::::::::::..." + selectedBranchId);
 						modelAndView.addObject("fromDate1", crypt.Jcrypt(fromDate));
 						modelAndView.addObject("toDate1", crypt.Jcrypt(toDate));
 						modelAndView.addObject("selectedBranchId1", crypt.Jcrypt(selectedBranchId));
 						modelAndView.addObject("selBranchName", crypt.Jcrypt("Select Branch"));
 						modelAndView.addObject("selectedBranchId", selectedBranchId);
 						modelAndView.setViewName("FactorySurveillanceBoSample");
 					} else {
 						modelAndView.setViewName("AccessDenied");
 					}
 				} else {

 					modelAndView.setViewName("sessionExpire");
 				}

 			} catch (Exception e) {
 				e.printStackTrace();
 			}
 			return modelAndView;
 		}
     
     
     //MonthlyMarketSampleTarget
     @RequestMapping(value = "/MonthlyMarketSampleTarget", method = RequestMethod.GET)
		public ModelAndView MonthlyMarketSampleTarget(@ModelAttribute("model1") AllCmlReportModel cmlModel, HttpServletRequest request) {

			ModelAndView modelAndView = new ModelAndView();

			try {
				HttpSession httpsession = request.getSession(false);
				Session userSession;
				userSession = (Session) httpsession.getAttribute("logged-in");
				if (userSession != null) {
					int roleId = Integer.parseInt(userSession.getCurrent_role());
					if (roleId != 3) {
						String currentId="1052";
						String parentId="0";
						
						modelAndView.addObject("roleId", roleId);

						int locationid = userSession.getLocation_id();
						modelAndView.addObject("locationid", locationid);
						System.out.println("locationid:::::::" + locationid);

						int locationtype = userSession.getLocation_type();
						modelAndView.addObject("locationtype", locationtype);
						System.out.println("locationtype:::::::" + locationtype);
						String locId = "";
						List<HashMap<String, String>> branchIds = new ArrayList<HashMap<String, String>>();

						String fromDate = cmlModel.getFromDt();
						String toDate = cmlModel.getToDt();
						String selectedBranchId = cmlModel.getBranchId();
						String regionId = cmlModel.getRegionId();
						modelAndView.addObject("branchIdList", selectedBranchId);
						String reg_id = "" + userSession.getLocation_id();
						if (regionId != null) {
							modelAndView.addObject("regionId1", regionId);
						} else {
							modelAndView.addObject("regionId1", 0);
						}

						modelAndView.addObject("branch_id", reg_id);
						System.out.println("branch id is " + selectedBranchId + "regionid" + regionId);

						SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
						String Date = formatter.format(new Date());// this is present date

						Calendar c = Calendar.getInstance();
						c.set(Calendar.DAY_OF_MONTH, 1);
						String startdateofMonth = formatter.format(c.getTime());
						System.out.println("start date is " + startdateofMonth);// Month ka start date hai
						

						if (cmlModel.getFromDt() == null && cmlModel.getToDt() == null) {
							System.out.println("Inside date is null");
							System.out.println("::::::::::::::::a::" + cmlModel.getFromDt());
							System.out.println("::::::::::::::::b::" + cmlModel.getToDt());
							fromDate = startdateofMonth;
							toDate = Date;
							modelAndView.addObject("fromDate", fromDate);
							modelAndView.addObject("toDate", toDate);
							System.out.println("fromDate date is " + fromDate);
							System.out.println("toDate date is " + toDate);
						} else {
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

						if (roleId != 7 && roleId != 4 && roleId != 5) {
							System.out.println("inside this");
							if (regionId == null && selectedBranchId == null
									|| regionId.equals("All") && selectedBranchId.equals("All")) {
								selectedBranchId = DGdao.getAllBranchIds();
								System.out.println("all selected branch id's are " + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt("All"));
							} else if (!regionId.equals("All") && selectedBranchId.equals("All")) {
								System.out.println("selectedBranchId123" + selectedBranchId);
								selectedBranchId = DGdao.getBranchIdsbylocationId(regionId);
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

							else {

								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}

						} else if (roleId == 7) {
							if (regionId == null && selectedBranchId == null
									|| regionId == null && selectedBranchId.equals("All")) {

								selectedBranchId = DGdao
										.getBranchIdsbylocationId(String.valueOf(userSession.getLocation_id()));
								System.out.println("selectedBranchId" + selectedBranchId);
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							} else {
								modelAndView.addObject("selectedBranchId11", crypt.Jcrypt(selectedBranchId));
							}
						} else {
							System.out.println("inside else");
							selectedBranchId = String.valueOf(userSession.getLocation_id());
							modelAndView.addObject("selectedBranchId11",
									crypt.Jcrypt(String.valueOf(userSession.getLocation_id())));
						}
						String StageName = DGserv.getStageNameDgreportsAIF(parentId,currentId);

						String ListQuery = DGserv.getBisReportListQueryDGreportsAIF(parentId,currentId);

						List<Map<String, Object>> bisReportQueryListModel = bisReportService
								.getBisListQueryResult(ListQuery, fromDate, toDate, selectedBranchId);
						request.setAttribute("BRLQL", bisReportQueryListModel);

						request.setAttribute("StageName", StageName);
						request.setAttribute("value", currentId);
						request.setAttribute("locationId", selectedBranchId);
						request.setAttribute("selBrnchName", selectedBranchId.replaceAll("_", " "));

						System.out.println("selectedBranchId::::::::::::..." + selectedBranchId);

						modelAndView.addObject("fromDate1", crypt.Jcrypt(fromDate));
						modelAndView.addObject("toDate1", crypt.Jcrypt(toDate));
						modelAndView.addObject("selectedBranchId1", crypt.Jcrypt(selectedBranchId));
						modelAndView.addObject("selBranchName", crypt.Jcrypt("Select Branch"));
						modelAndView.addObject("selectedBranchId", selectedBranchId);

						modelAndView.setViewName("MonthlyMarketSampleTarget");

						

					} else {
						modelAndView.setViewName("AccessDenied");
					}
				} else {

					modelAndView.setViewName("sessionExpire");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return modelAndView;
		}
		
     
     
     
		@RequestMapping(value="/CMLConsineeDtl" ,method=RequestMethod.GET)
		public ModelAndView CMLConsineeDtl(@ModelAttribute("model1") AllCmlReportModel cmlModel,HttpServletRequest request,ModelAndView objModelAndView){	
			
			ModelAndView modelAndView = new ModelAndView();
			try{
			HttpSession httpsession = request.getSession(false);
			Session userSession;
			userSession = (Session) httpsession.getAttribute("logged-in");
			String currentId="1001";
			String parentId="1000";
			String from_date="01-01-2000";
			String to_date="01-01-2000";
			String json ="";
			
			if(userSession!=null)
			{	

				String cmlno="";
				String branchId="";		
				if(request.getParameter("cml")!=null){
					cmlno=request.getParameter("cml");		
				}					
				if(request.getParameter("branchId")!=null)
				{
				branchId=request.getParameter("branchId");
				}
				Gson gson = new Gson();
				String StageName=DGserv.getStageNameDgreportsAIF(parentId,currentId);
				
				String ListQuery=DGserv.getBisReportListQueryDGreportsAIF(parentId,currentId);
				System.out.println("ListQuery>>>>>>>>>>>>>"+ListQuery);
				List<Map<String,Object>> bisReportQueryListModel=DGserv.getdataCon(ListQuery,cmlno,branchId);
				request.setAttribute("BRLQL", bisReportQueryListModel);
				json = gson.toJson(bisReportQueryListModel);
				request.setAttribute("BRLQLjson",json);
				request.setAttribute("StageName", StageName);
				request.setAttribute("value", currentId);
				//request.setAttribute("locationId", locationId);
				//request.setAttribute("selBrnchName", selBrnchName.replaceAll("_", " "));
				modelAndView.setViewName("dashboardReportListQueryPage");
			   
			}
			else{		
				objModelAndView.setViewName("sessionExpire");	
			}
			}catch (Exception e) {
				objModelAndView.setViewName("sessionExpire");	
			}
			return modelAndView;
			
		}
	

}