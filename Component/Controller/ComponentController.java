package Component.Controller;

import java.util.ArrayList;
import java.util.HashMap;

/***************************Program Start*****************************\
## Copyright Information		: C-DAC, Noida  
## Project Name					: Integrated Web Portal for BIS
## Name of Developer		 	: Abhishek Shrivastava
## Module Name					: Component
## Process/Database Object Name	: BIS_dev
## Purpose						: Different function for getting listing pages for Component
## Date of Creation				: 12/10/20
## Modify By					: Abhishek Shrivastava
*/


/*import java.util.HashMap;*/
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import Component.Service.ComponentService;
import Global.Login.Domain.LoginPageMessageDomain;
import Global.Login.Service.IloginService;
import Masters.Service.ICircular_Mst_Service;
import eBIS.AppConfig.CustomWebExceptionHandler;


@Controller
public class ComponentController {

	@Autowired
	IloginService lserv;
	
	@Autowired
	ICircular_Mst_Service cms;
	
	@Autowired
	ComponentService cs;
	
	/*Abhishek code*/
	@CustomWebExceptionHandler()
	@RequestMapping(value="/TestLinkPage", method=RequestMethod.GET)
	public ModelAndView testLinkPage( HttpServletRequest request_p){
		System.out.println("inside eBISLogin_component1");
		ModelAndView mav = new ModelAndView();
		String loginmsg="";
		String cmmsg = "";
		try{
		cmmsg = cms.getCircularsforLoginPage();
		}catch(Exception e){
			e.printStackTrace();
		}
	List<LoginPageMessageDomain> msg = lserv.getLoginPageMsgList();
	for(int i=0;i<msg.size();i++){
		loginmsg=loginmsg + msg.get(i).getStr_message()+", ";
	
	}
	if(loginmsg.length()>2){
	loginmsg=loginmsg.substring(0, loginmsg.length()-2);
	}
       mav.addObject("msgscroll", loginmsg);
		mav.addObject("scroll", cmmsg);
		
		System.out.println("test");	
		

		List<HashMap<String,String>> tableData = new ArrayList<HashMap<String,String>>();
		tableData = cs.getTableData();
		
		System.out.println("::::::::::"+tableData.toString());	
				
		mav.addObject("tableData",tableData);
		
		mav.setViewName("viewtestLinkPage");
		return mav;
	}
	

			
}



