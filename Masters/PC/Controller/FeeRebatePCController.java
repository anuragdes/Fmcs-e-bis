package eBIS.Masters.PC.Controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.eclipse.collections.impl.list.mutable.FastList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import Global.Login.Model.Session;
import Masters.Domain.Scale_Mst_Domain;
import Masters.Domain.State_Mst_Domain;
import Masters.Domain.standard_mst_domain;
import eBIS.Masters.PC.Model.FeeRebatePCModel;
import eBIS.Masters.PC.Service.FeeRebatePCService;
import lombok.var;

@Controller
public class FeeRebatePCController {
	@Autowired
	FeeRebatePCService service;
	@RequestMapping("/feeRebatePC")
	public ModelAndView feeRebatePC(@ModelAttribute("model") FeeRebatePCModel model ,HttpServletRequest request) {
		ModelAndView mv=new ModelAndView();
		HttpSession httpsession = request.getSession(true);
		if(httpsession!=null)
		{
			Session userSession = (Session) httpsession.getAttribute("logged-in");
			if(userSession!=null) {
				List<Scale_Mst_Domain> scaleList = service.getScaleList();
				List<State_Mst_Domain> stateList = service.getStateList();
				List<standard_mst_domain> standardMasterList = service.getStandardMasterList();
				int userId = userSession.getUserid();
				model.setUserid(userId);
				mv.addObject("scaleList", scaleList);
				mv.addObject("stateList", stateList);
				mv.addObject("standardMasterList", standardMasterList);
				String scaleName="";
				String stateName="";
				String standardName="";
				model.setUrl("feeRebatePC");
				FastList<FeeRebatePCModel> modelList = service.feeRebatePC(model);
				for(int i=0;i<modelList.size();i++) {
					FeeRebatePCModel tempmodel = modelList.get(i);
					scaleName = service.getScaleName(tempmodel.getScaleid());
					stateName=service.getStateName(tempmodel.getStateid());
					if(!tempmodel.getStandardnumber().equalsIgnoreCase("ALL")) {
						standardName=service.getStandardName(tempmodel.getStandardnumber());	
					}else {
						standardName="ALL";
					}
					tempmodel.setScalename(scaleName);
					tempmodel.setStatename(stateName);
					String basescaleName = service.getScaleName(tempmodel.getBasescaleid());
					tempmodel.setBasescalename(basescaleName);
					tempmodel.setStandardname(standardName);
				}
				mv.addObject("modelList", modelList);
				mv.addObject("modelListsize", modelList.size());
				mv.setViewName("feeRebatePC");
			}else {
				mv.setViewName("sessionExpire");
			}
		}else {
			mv.setViewName("sessionExpire");
		}
		return mv;
	}

	@RequestMapping("/feeRebatePCvalidate")
	public @ResponseBody String feeRebatePCvalidate(@ModelAttribute("model") FeeRebatePCModel model,HttpServletRequest request) {
		var flag=service.validate(model);
		System.out.println("feeRebatePCvalidateFlag: "+flag);
		return flag;
	}
	
	@RequestMapping(value="/addfeeRebatePC",method = RequestMethod.POST)
	@ResponseBody FastList<FeeRebatePCModel> addfeeRebatePC(@ModelAttribute("model") FeeRebatePCModel model ,HttpServletRequest request) {
		HttpSession httpsession = request.getSession(true);
		Session userSession = (Session) httpsession.getAttribute("logged-in");
		int userId = userSession.getUserid();
		model.setUserid(userId);
		String scaleName="";
		String stateName="";
		String standardName="";
		model.setUrl("feeRebatePC");
		FastList<FeeRebatePCModel> modelList=service.addfeeRebatePC(model);
		for(int i=0;i<modelList.size();i++) {
			FeeRebatePCModel tempmodel = modelList.get(i);
			
			scaleName = service.getScaleName(tempmodel.getScaleid());
			stateName=service.getStateName(tempmodel.getStateid());
			if(!tempmodel.getStandardnumber().equalsIgnoreCase("ALL")) {
				standardName=service.getStandardName(tempmodel.getStandardnumber());	
			}else {
				standardName="ALL";
			}
			String basescaleName = service.getScaleName(tempmodel.getBasescaleid());
			tempmodel.setBasescalename(basescaleName);
			tempmodel.setScalename(scaleName);
			tempmodel.setStatename(stateName);
			tempmodel.setStandardname(standardName);
		}
		return modelList;
	}
	@RequestMapping(value="/deletefeeRebatePC",method = RequestMethod.POST)
	@ResponseBody FastList<FeeRebatePCModel> deletefeeRebatePC(@ModelAttribute("model") FeeRebatePCModel model ,HttpServletRequest request) {
		HttpSession httpsession = request.getSession(true);
		Session userSession = (Session) httpsession.getAttribute("logged-in");
		int userId = userSession.getUserid();
		model.setUserid(userId);
		String scaleName="";
		String stateName="";
		String standardName="";
		model.setUrl("feeRebatePC");
		FastList<FeeRebatePCModel> modelList=service.deletefeeRebatePC(model);
		for(int i=0;i<modelList.size();i++) {
			FeeRebatePCModel tempmodel = modelList.get(i);
			scaleName = service.getScaleName(tempmodel.getScaleid());
			stateName=service.getStateName(tempmodel.getStateid());
			if(!tempmodel.getStandardnumber().equalsIgnoreCase("ALL")) {
				standardName=service.getStandardName(tempmodel.getStandardnumber());	
			}else {
				standardName="ALL";
			}
			String basescaleName = service.getScaleName(tempmodel.getBasescaleid());
			tempmodel.setBasescalename(basescaleName);
			tempmodel.setScalename(scaleName);
			tempmodel.setStatename(stateName);
			tempmodel.setStandardname(standardName);
		}
		return modelList;
	}
}

