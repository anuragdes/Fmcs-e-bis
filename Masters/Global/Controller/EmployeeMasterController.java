package eBIS.Masters.Global.Controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import Global.Login.Model.Session;
import Global.Login.Service.IUserValidator;
import Masters.Domain.Design_Mst_Domain;
import Masters.Domain.Discipline_Mst_Domain;
import Masters.Domain.LocationType_Mst_Domain;
import Masters.Domain.Role_Mst_Domain;
import Masters.Model.Emp_dept_Mod;
import eBIS.AppConfig.CustomWebExceptionHandler;
import eBIS.Masters.Global.Model.EmployeeMasterModel;
import eBIS.Masters.Global.Service.EmployeeMasterService;

@Controller
public class EmployeeMasterController {
	@Autowired
	IUserValidator iuvObj;
	@Autowired
	EmployeeMasterService service;
	@Autowired
	IUserValidator iuv;
	@CustomWebExceptionHandler()
	@RequestMapping(value = "/EmployeeMaster", method = RequestMethod.GET)
	public ModelAndView EmployeeMaster(@ModelAttribute("model") EmployeeMasterModel model, HttpServletRequest request) throws IOException, InterruptedException, ExecutionException 
	{
		ModelAndView mv=new ModelAndView();
		HttpSession httpsession = request.getSession(false);
		if(httpsession!=null) {
			Session userSession = (Session) httpsession.getAttribute("logged-in");
			if(userSession!=null) {
				String URL="EmployeeMaster";
				String CSRFToken = iuvObj.generateCSRFToken(URL,request);
				model.setUrl(URL);
				model.setCsrftoken(CSRFToken);
				List<Discipline_Mst_Domain> dtlist = service.getDisciplines();
				List<Design_Mst_Domain> dsls = service.getDesign();
				List<Role_Mst_Domain> role = service.getRoles();
				List<LocationType_Mst_Domain> depart = service.getdepart();
				List<Map<String,Object>> empList = service.getEmpDetail();
				int flag;
				if(request.getParameter("flag") != null) {
					flag=Integer.parseInt(request.getParameter("flag"));
				}else {
					flag=-1;
				}
				mv.addObject("empList", empList);
				mv.addObject("dtlist", dtlist);
				mv.addObject("dsls", dsls);
				mv.addObject("role", role);
				mv.addObject("depart", depart);
				mv.addObject("flag", flag);
				mv.setViewName("EmployeeMaster");
				System.out.println("role count: "+role.size());
			}else {
				mv.setViewName("sessionExpire");	
			}
		}else {
			mv.setViewName("sessionExpire");
		}
		return mv;
	}
	@CustomWebExceptionHandler()
	@RequestMapping(value = "/EmployeeMasterList", method = RequestMethod.GET)
	public @ResponseBody List<Map<String,Object>> EmployeeMasterList( HttpServletRequest request,HttpServletResponse response) throws IOException, InterruptedException, ExecutionException,Exception
	{
		List<Map<String, Object>> empList = service.getEmpDetail();
		return empList;
	}
	
	@CustomWebExceptionHandler()
	@RequestMapping(value = "/EmployeeMasterSubmit", method = RequestMethod.POST)
	public ModelAndView EmployeeMasterSubmit(@ModelAttribute("model") EmployeeMasterModel model, HttpServletRequest request) throws Exception,org.springframework.dao.DataIntegrityViolationException
	{
		ModelAndView mv=new ModelAndView();
		HttpSession httpsession = request.getSession(false);
		if(httpsession!=null) {
			Session userSession = (Session) httpsession.getAttribute("logged-in");
			if(userSession!=null) {
				String URL=model.getUrl();
				String CSRFToken = model.getCsrftoken();
				if (iuv.authenticateCSRFToken(URL, CSRFToken, request)){
					int userid = userSession.getUserid();
					model.setUserid(userid);
					int flag=-1;
					int editflag = model.getEditflag();
					if(editflag==0)
					{
						flag = service.addEmployee(model);
					}else {
						flag = service.editEmployee(model);
					}
					mv.addObject("flag", flag);
					mv.setViewName("redirect:/EmployeeMaster");
					System.out.println("model: "+model.toString());
				}
				else{
					mv.setViewName("redirect:/csrfErrorPage");
				}
				
			}else {
				mv.setViewName("sessionExpire");	
			}
		}else {
			mv.setViewName("sessionExpire");
		}
		return mv;
	}
	@CustomWebExceptionHandler()
	@RequestMapping(value = "/EmpDepartment", method = RequestMethod.POST)
	public @ResponseBody List<Emp_dept_Mod> EmpDepartment(HttpServletRequest request)
	{
		List<Emp_dept_Mod> departmentName=null;
		if(request.getParameter("departmentTypeName")!=null && !request.getParameter("departmentTypeName").equals("") && !request.getParameter("departmentTypeName").equals("0"))
		{
			String departmentTypeName=request.getParameter("departmentTypeName");
			int departmentTypeID=Integer.parseInt(request.getParameter("departmentTypeID").toString());
			departmentName = service.getDepartmentName(departmentTypeID,departmentTypeName);
		}

		return departmentName;
	}
	@CustomWebExceptionHandler()
	@RequestMapping(value = "/RoleList", method = RequestMethod.POST)
	public @ResponseBody List<Role_Mst_Domain> RoleList(HttpServletRequest request)
	{
		List<Role_Mst_Domain> roles = service.getRoles();
		return roles;
	}
	@CustomWebExceptionHandler()
	@RequestMapping(value = "/DepartmentList", method = RequestMethod.POST)
	public @ResponseBody List<LocationType_Mst_Domain> DepartmentList(HttpServletRequest request)
	{
		List<LocationType_Mst_Domain> depart = service.getdepart();
		return depart;
	}
	@CustomWebExceptionHandler()
	@RequestMapping(value = "/EditEmployeeMaster", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> EditEmployeeMaster( HttpServletRequest request) throws IOException, InterruptedException, ExecutionException
	{
		List<Map<String, Object>> empList=null;
		if(request.getParameter("bis_user_id")!=null && !request.getParameter("bis_user_id").equals("") && !request.getParameter("bis_user_id").equals("0")) {
			String bis_user_id = request.getParameter("bis_user_id").toString();
			empList = service.getEmpDetailforBISUserID(bis_user_id);
		}
		
		return empList;
	}
	@CustomWebExceptionHandler()
	@RequestMapping(value = "/DeleteEmployeeMaster", method = RequestMethod.POST)
	public @ResponseBody int DeleteEmployeeMaster( HttpServletRequest request) throws IOException, InterruptedException, ExecutionException
	{
		int flag=-1;
		if(request.getParameter("bis_user_id")!=null && !request.getParameter("bis_user_id").equals("") && !request.getParameter("bis_user_id").equals("0")) {
			String bis_user_id = request.getParameter("bis_user_id").toString();
			flag=service.DeleteEmployeeMaster(bis_user_id);
		}
		
		return flag;
	}
}
