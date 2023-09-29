package Applicant.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import Applicant.Model.RawMaterialModel;
import Applicant.Service.RawMaterialService;
import Global.CommonUtility.DAO.DaoHelper;
import Global.Login.Model.Session;
import Global.Login.Service.IMigrateService;
import Global.Login.Service.IUserValidator;
import Schemes.ProductCertification.BO.DAO.RcvdApplicationDODao;
import Schemes.ProductCertification.LicenceGranting.Service.productionReturn_Service;
import eBIS.AppConfig.CustomWebExceptionHandler;

@Controller
public class RawMaterialsController {

	

	@Autowired
	public DaoHelper daoHelper;

	@Autowired
	IUserValidator iuv;

	@Autowired
	IMigrateService ims;

	@Autowired
	productionReturn_Service prdServ;

	@Autowired
	RcvdApplicationDODao rcvddao;

	@Autowired
	RawMaterialService cserv;
	
	private String message = "";
	private String eBranchId = "";
	private String eCmlNo = "";
	private String eAppId = "";
	private boolean flag=false;
	@CustomWebExceptionHandler()
	@RequestMapping(value = "/rawmaterial", method = RequestMethod.GET)
	public ModelAndView ShowCirculars( @ModelAttribute("rawmaterialmodel") RawMaterialModel rawmaterialmodel,
			HttpServletRequest request_p) {
		ModelAndView mav = new ModelAndView();
		HttpSession httpsession = request_p.getSession(false);
		Session userSession;
		String app_no = null;
		String licenseNo = null;
		String statusLic = null;
		int userid = 0;
		int branchID = 0;
		userSession = (Session) httpsession.getAttribute("logged-in");
		if (userSession != null) {

			try {
				userid = userSession.getUserid();
				app_no = ims.Dcrypt(request_p.getParameter("appId"));
				licenseNo = ims.Dcrypt(request_p.getParameter("cml_no"));
				branchID = Integer.parseInt(ims.Dcrypt(request_p
						.getParameter("branchId")));
			} catch (Exception ex) {

			}
			mav.addObject("user_id", userid);
			mav.addObject("str_app_id", request_p.getParameter("appId"));
			mav.addObject("str_cml_no", request_p.getParameter("cml_no"));
			mav.addObject("str_branch_id", request_p.getParameter("branchId"));

			List<HashMap<String, String>>  licence_Detail_Hm = prdServ.getLicenceDetail(app_no, branchID);
			mav.addObject("licence_Detail_Hm", licence_Detail_Hm);

			statusLic = rcvddao.getApplicantRemarksnew(licenseNo, branchID);
			mav.addObject("cmlstatus", statusLic);

			List<Map<String, Object>>  listhmcirculardetails = cserv.getLatestRawMaterila(app_no, licenseNo, branchID);
			System.out.println("listhmcirculardetails   "+listhmcirculardetails);
			mav.addObject("techmanagements", listhmcirculardetails);

			String stCSRFToken = iuv.generateCSRFToken("techmanagement",
					request_p);

			mav.addObject("csrf", stCSRFToken);
			mav.addObject("msg", message);
			message = "";

			mav.setViewName("rawmaterial_List");
		} else {
			mav.setViewName("sessionExpire");
		}

		return mav;

	}

	@RequestMapping(value="/saverawmaterial",method=RequestMethod.POST)
	public String saveCircular(@ModelAttribute("rawmaterialmodel") RawMaterialModel rawmaterialmodel,BindingResult result,HttpServletRequest request,RedirectAttributes redirect)
	{
		ModelAndView mav = new ModelAndView();
		HttpSession httpsession = request.getSession(false);
		Session userSession;
		userSession = (Session) httpsession.getAttribute("logged-in");
		eBranchId = rawmaterialmodel.getstr_branch_id();
		eCmlNo = rawmaterialmodel.getstr_cml_no();
		eAppId = rawmaterialmodel.getStr_app_id();
		if(userSession != null) {
			try{					
				rawmaterialmodel.setUser_id(userSession.getUserid());
				flag=cserv.saveRawMaterialDetails(rawmaterialmodel);
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
		else {
		return "redirect:/sessionExpire";
		}
		return "redirect:/rawmaterial?appId="+eAppId+"&branchId="+eBranchId+"&cml_no="+eCmlNo;
}
	
	
	  @RequestMapping(value="/deleteRawMaterial",method=RequestMethod.POST)
	  	public @ResponseBody boolean  deleteRawMaterial(String numId,HttpServletRequest request)
	   {
		   int id=Integer.parseInt(numId);
		   HttpSession httpsession = request.getSession(false);
		 
		   if(httpsession!=null) {
			   Session userSession=null;
			   userSession = (Session) httpsession.getAttribute("logged-in");

			   if(userSession!=null){
				    flag=cserv.deleteRawMaterial(id);
			   }
		   }
		   return flag;
	  
	   }
}
