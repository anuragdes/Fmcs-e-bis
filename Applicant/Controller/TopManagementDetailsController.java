package Applicant.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import Applicant.Model.TopManagementDetailsModel;
import Applicant.Service.TopManagementDetailsService;
import Global.Login.Model.Session;
import Global.Login.Service.IMigrateService;
import Global.Login.Service.IUserValidator;
import Schemes.ProductCertification.BO.DAO.RcvdApplicationDODao;
import Schemes.ProductCertification.LicenceGranting.Service.productionReturn_Service;
import eBIS.AppConfig.CustomWebExceptionHandler;

@Controller
public class TopManagementDetailsController {

	@Autowired
	TopManagementDetailsService cserv;

	@Autowired
	IUserValidator iuv;

	@Autowired
	IMigrateService ims;

	@Autowired
	productionReturn_Service prdServ;

	@Autowired
	RcvdApplicationDODao rcvddao;

	private String message = "";
	@CustomWebExceptionHandler()
	@RequestMapping(value = "/topmanagement", method = RequestMethod.GET)
	public ModelAndView ShowCirculars(
			@ModelAttribute("topmanagementModel") TopManagementDetailsModel topmanagementModel,
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
				branchID = Integer.parseInt(ims.Dcrypt(request_p.getParameter("branchId")));
			} catch (Exception ex) {

			}
			mav.addObject("user_id", userid);
			mav.addObject("str_app_id", request_p.getParameter("appId"));
			mav.addObject("str_cml_no", request_p.getParameter("cml_no"));
			mav.addObject("str_branch_id", request_p.getParameter("branchId"));

			List<HashMap<String, String>> licence_Detail_Hm = new ArrayList<HashMap<String, String>>();
			licence_Detail_Hm = prdServ.getLicenceDetail(app_no, branchID);
			mav.addObject("licence_Detail_Hm", licence_Detail_Hm);

			statusLic = rcvddao.getApplicantRemarksnew(licenseNo, branchID);
			mav.addObject("cmlstatus", statusLic);

			List<HashMap<String, String>> listhmcirculardetails = cserv.getLatestTopmanagment(app_no, licenseNo,branchID);
			mav.addObject("topmanagements", listhmcirculardetails);

			String stCSRFToken = iuv.generateCSRFToken("topmanagement", request_p);

			mav.addObject("csrf", stCSRFToken);
			mav.addObject("msg", message);
			message = "";

			mav.setViewName("topmangement_List");
		} else {
			mav.setViewName("sessionExpire");
		}

		return mav;

	}

	@RequestMapping(value = "/savetopmanagement", method = RequestMethod.POST)
	public ModelAndView saveCircular(@ModelAttribute("topmanagementModel") TopManagementDetailsModel topmanagementModel,
			BindingResult result, HttpServletRequest request, RedirectAttributes redirect,
			MultipartHttpServletRequest request_p) {
		ModelAndView mav = new ModelAndView();
		HttpSession httpsession = request.getSession(false);
		Session userSession;
		int Userid = 0;
		userSession = (Session) httpsession.getAttribute("logged-in");
		if (userSession != null) {
			Userid = userSession.getUserid();
			try {

				System.out.println("without document");
				boolean savedSuccessFully = cserv.saveManagementDetails(topmanagementModel);
			} catch (Exception ex) {

			}

		}
		System.out.println("cml  "+ims.Dcrypt(topmanagementModel.getstr_cml_no()));
		
		mav.setViewName("redirect:/topmanagement?appId=" + topmanagementModel.getstr_app_id() + "&branchId="
				+ topmanagementModel.getstr_branch_id() + "&cml_no=" + topmanagementModel.getstr_cml_no());
		return mav;

	}

	
	@RequestMapping(value = "/removeTopManagementDetails", method = RequestMethod.POST)
	public @ResponseBody String removeCircularDetails(
			@ModelAttribute("topmanagementModel") TopManagementDetailsModel topmanagementModel, BindingResult result,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		String returnText = "";
		String stResponse = "";
		System.out.println("IN the remove controller");
		HttpSession httpsession = request.getSession(false);
		Session userSession;
		userSession = (Session) httpsession.getAttribute("logged-in");
		int userid = userSession.getUserid();

		if (userSession != null) {

			returnText = request.getParameter("id");
			System.out.println("returnText NumId===" + returnText);
			stResponse = cserv.removeManagementDetails(returnText, userid);

		}
		return stResponse;

	}

}
