package Applicant.Controller;

import java.util.HashMap;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import Applicant.DAO.ApplicantpcDAO;
import Applicant.Model.PCApplicantModel;
import Applicant.Service.ApplicantpcService;
import Masters.Domain.Branch_Master_Domain;
import Masters.Domain.Status_mst_domain;
import Masters.Domain.standard_mst_domain;
import eBIS.AppConfig.CustomWebExceptionHandler;

@Controller
public class PCApplicationController {
	@Autowired	
	ApplicantpcDAO applicationpcstandard;
	@Autowired
	ApplicantpcService pcappservice;
	
	@CustomWebExceptionHandler()
	@RequestMapping(value="/pcapplicationsubmit",method=RequestMethod.POST)
	public @ResponseBody List<HashMap<String,String>> submitDetails(@ModelAttribute("pcapplication") PCApplicantModel pcrmodel, HttpServletRequest request,  RedirectAttributes redirectAttributes) throws MessagingException {
		
		ModelAndView objModelAndView = new ModelAndView();
		String test=pcrmodel.getpcISNO();
		String rev=request.getParameter("rev");
		String fromdate=request.getParameter("fromdate");
		String todate=request.getParameter("todate");
		String isno=request.getParameter("pcISNO");
		String BranchID=request.getParameter("BranchID");
		System.out.println("fromdate"+fromdate+"todate"+todate+"isno"+isno+"branchID"+BranchID+"rev"+rev);
		List<HashMap<String,String>> applicantpcapps = pcappservice.getpcApplication(fromdate,todate,isno,BranchID,rev);
		return applicantpcapps;
		
	}
	
	
	@RequestMapping(value="/pcapplicationform",method=RequestMethod.GET)
	public ModelAndView show(@ModelAttribute("pcapplication") PCApplicantModel pcrmodel,HttpServletRequest request,  RedirectAttributes redirectAttributes) throws MessagingException {
        
		
		List<standard_mst_domain> pcappstandard=applicationpcstandard.getStandards();
		List<Branch_Master_Domain> pcbranch=applicationpcstandard.getBranch();
		List<Status_mst_domain> appstatus=applicationpcstandard.getApplicationStatus();
		//String is_no = pcappstandard.get(0).getStrStandardNo();
		ModelAndView objModelAndView = new ModelAndView();
		
		objModelAndView.addObject("isno",pcappstandard);
		objModelAndView.addObject("pcbranch",pcbranch);
		objModelAndView.addObject("applicationstatus",appstatus);
		objModelAndView.setViewName("pcapplicationSubmitForm");
		return objModelAndView;
		
	}
	
	
	
	
}
