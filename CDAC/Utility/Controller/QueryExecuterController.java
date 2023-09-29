package eBIS.CDAC.Utility.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import Global.Login.Model.Session;
import eBIS.AppConfig.CustomWebExceptionHandler;
import eBIS.CDAC.Utility.Model.QueryExecuterModel;
import eBIS.CDAC.Utility.Service.QueryExecuterService;
import mobileAPI.controller.MobileCmlInspPeriodicDtlDetailsStockController;

@Controller
public class QueryExecuterController {
	
	@Autowired 
	QueryExecuterService service;
	@Autowired
	MobileCmlInspPeriodicDtlDetailsStockController controller;
	@CustomWebExceptionHandler
	@RequestMapping(value="/QueryExecuter",method= {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView QueryExecuter(@ModelAttribute("model") QueryExecuterModel model ,HttpServletRequest request){
		ModelAndView mv=new ModelAndView();
		HttpSession httpsession = request.getSession(true);
		if(httpsession!=null)
		{
			Session userSession = (Session) httpsession.getAttribute("logged-in");
			if(userSession!=null) {
				mv.addObject("BRLQLjson", "0");
				mv.setViewName("QueryExecuter");
			}else {
				mv.setViewName("sessionExpire");
			}
		}else {
			mv.setViewName("sessionExpire");
		}
		return mv;
	}
	
	@CustomWebExceptionHandler()@RequestMapping(value="/QueryExecuterSubmit",method=RequestMethod.POST)
	public ModelAndView QueryExecuterSubmit(@ModelAttribute("model") QueryExecuterModel model ,HttpServletRequest request) throws Exception{
		ModelAndView mv=new ModelAndView();
		HttpSession httpsession = request.getSession(true);
		if(httpsession!=null)
		{
			Session userSession = (Session) httpsession.getAttribute("logged-in");
			if(userSession!=null) {
				Gson gson = new Gson();
				List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
				String SQLQueryRequest = model.getSql_query_request();
				try {
				list = service.QueryExecuter(SQLQueryRequest);
				}catch(Exception ex) {
						HashMap<String, Object> temp=new HashMap<String, Object>();
						temp.put("Exception", ex.getClass().getSimpleName());
						temp.put("ExceptionMsg", ex.getMessage());
						temp.put("ExceptionDetail", Arrays.toString(ex.getStackTrace()));
						list.add(temp);
				}
				model.setSql_query_response(gson.toJson(list));
				service.submitLog(model);
				mv.addObject("BRLQLjson", gson.toJson(list));
				mv.setViewName("QueryExecuter");
			}else {
				mv.setViewName("sessionExpire");
			}
		}else {
			mv.setViewName("sessionExpire");
		}
		return mv;
	}
	
}
