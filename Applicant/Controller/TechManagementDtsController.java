package Applicant.Controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import Applicant.Model.TechManagementDtsModel;
import Applicant.Service.TechManagementService;
import Global.CommonUtility.DAO.DaoHelper;
import Global.Login.Model.Session;
import Global.Login.Service.IMigrateService;
import Global.Login.Service.IUserValidator;
import Schemes.ProductCertification.ApplicationSubmission.Domain.qualityManagementDtlsDomain;
import Schemes.ProductCertification.BO.DAO.RcvdApplicationDODao;
import Schemes.ProductCertification.LicenceGranting.Service.productionReturn_Service;
import eBIS.AppConfig.CustomWebExceptionHandler;

@Controller
public class TechManagementDtsController {

	@Autowired
	TechManagementService cserv;

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

	private String message = "";
	@CustomWebExceptionHandler()
	@RequestMapping(value = "/techmanagement", method = RequestMethod.GET)
	public ModelAndView ShowCirculars(
		@ModelAttribute("techmanagementModel") TechManagementDtsModel techmanagementModel,
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

			List<HashMap<String, String>> licence_Detail_Hm = new ArrayList<HashMap<String, String>>();
			licence_Detail_Hm = prdServ.getLicenceDetail(app_no, branchID);
			mav.addObject("licence_Detail_Hm", licence_Detail_Hm);

			statusLic = rcvddao.getApplicantRemarksnew(licenseNo, branchID);
			mav.addObject("cmlstatus", statusLic);

			List<HashMap<String, String>> listhmcirculardetails = cserv
					.getLatestTopmanagment(app_no, licenseNo, branchID);
			mav.addObject("techmanagements", listhmcirculardetails);

			String stCSRFToken = iuv.generateCSRFToken("techmanagement",
					request_p);

			mav.addObject("csrf", stCSRFToken);
			mav.addObject("msg", message);
			message = "";

			mav.setViewName("techmangement_List");
		} else {
			mav.setViewName("sessionExpire");
		}

		return mav;

	}

	@RequestMapping(value = "/savetechmanagement", method = RequestMethod.POST)
	public String saveCircular(
			@ModelAttribute("techmanagementModel") TechManagementDtsModel techmanagementModel,
			BindingResult result, HttpServletRequest request,MultipartHttpServletRequest request_p,
			RedirectAttributes redirect){
		ModelAndView mav = new ModelAndView();
		HttpSession httpsession = request.getSession(false);
		Session userSession;
		int Userid = 0;
		userSession = (Session) httpsession.getAttribute("logged-in");
		if (userSession != null) {
			Userid = userSession.getUserid();
			MultipartFile mpf = request_p.getFile("str_photo");
			try{
			byte[] imageData = mpf.getBytes();
			boolean savedSuccessFully =cserv.saveManagementDetails(techmanagementModel, imageData);
			
			} catch (Exception ex) {
 
			}

		}

		return "redirect:/techmanagement?appId="
				+ techmanagementModel.getstr_app_id()+ "&branchId="
				+ techmanagementModel.getstr_branch_id() + "&cml_no="
				+ techmanagementModel.getstr_cml_no();
	}

	@RequestMapping(value = "/removetechManagementDetails", method = RequestMethod.POST)
	public @ResponseBody
	String removeCircularDetails(
			@ModelAttribute("techmanagementModel") TechManagementDtsModel topmanagementModel,
			BindingResult result, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
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
	
	@RequestMapping(value="/TechMgmatPhoto", method=RequestMethod.GET)
	public void AuditorPhoto(HttpServletRequest request_p,HttpServletResponse response) throws ServletException, IOException{
		ModelAndView mav = new ModelAndView();
		try{
			HttpSession httpsession = request_p.getSession(false);
			if(httpsession == null){
				System.out.println("HttpSession is null");
			}else{
				Session userSession=null;
				userSession = (Session) httpsession.getAttribute("logged-in");
				if(userSession!=null){
					int iUserId = Integer.parseInt(request_p.getParameter("userId").toString());
					System.out.println("TechPhoto iUserId: "+iUserId);
					ServletOutputStream out = response.getOutputStream();
					 byte[] fileinputstream = null;
					 InputStream targetStream=null;
					 int checkuploadphotoflag = 1;
					 System.out.println("checkuploadphotoflag: "+checkuploadphotoflag);
					 String filePath="";
					 if(checkuploadphotoflag==1){
						 
						 String qry="select p from qualityManagementDtlsDomain p where p.num_id="+iUserId;
						 List<qualityManagementDtlsDomain> runQry = daoHelper.findByQuery(qry);
						 //return runQry.get(0).getUser_photo();
						 
						 fileinputstream=runQry.get(0).getPhotographPersonNew();
						 
						 targetStream = new ByteArrayInputStream(fileinputstream);
						 
						 ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
				            byte abyte0[] = new byte[0x500000];
				            do {
				              int i = targetStream.read(abyte0);
				              if (i != -1){
				            	  bytearrayoutputstream.write(abyte0, 0, i);
				              }
				              else{
				                byte abyte1[] = bytearrayoutputstream.toByteArray();
				                response.setContentLength(abyte1.length);
				                out.write(abyte1, 0, abyte1.length);
				                out.flush();
				                out.close();
				                return;
				              }
				            } 
				            while (true);
					 }

		           
				}/*if usersession!=null*/
				else if(userSession==null){
					System.out.println("UserSession Is Null");
				}/*else if*/
			}/*else*/
		}/*try*/
		catch(Exception e){
			e.printStackTrace();
			System.out.println("exception");
		}
	}

}
