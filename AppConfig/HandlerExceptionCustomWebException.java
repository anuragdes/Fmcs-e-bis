package eBIS.AppConfig;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import Global.Login.Model.Session;
import exception.log.model.ExceptionLogModel;
import exception.log.service.ExceptionLogService;

public class HandlerExceptionCustomWebException implements HandlerExceptionResolver {
	@Autowired
	ExceptionLogService exceptionservice;
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		
		if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            CustomWebExceptionHandler errorView = hm.getMethodAnnotation(CustomWebExceptionHandler.class);
            ModelAndView mv =new ModelAndView();
             if (errorView != null) {
            	 
            	 HttpSession httpsession = request.getSession(false);
         		if(httpsession == null){
         			
         			mv.setViewName("sessionExpire");
         		}else{
         			Session userSession = (Session) httpsession.getAttribute("logged-in");
         			
         			if(userSession!=null){
         				String url=request.getRequestURL().toString();
        				String UserName = userSession.getUsername();
        				String RoleName = userSession.getStrRoleName();
        				String RoleId=userSession.getCurrent_role();
        				int BranchID = userSession.getLocation_id();
        				String Name = userSession.getFname()+" "+userSession.getMname()+" "+userSession.getLname();
        				Name=Name.replace("  ", " ");
        				ExceptionLogModel exceptionmodel=new ExceptionLogModel();
        				exceptionmodel.setUser_remarks(ex.getClass().getSimpleName());
        				exceptionmodel.setException_remarks(ex.getMessage());
        				exceptionmodel.setException_details(Arrays.toString(ex.getStackTrace()));
        				exceptionmodel.setBranchid(BranchID+"");
        				exceptionmodel.setException_url(url);
        				exceptionmodel.setRolename(RoleName);
        				exceptionmodel.setUsername(UserName);
        				exceptionmodel = exceptionservice.insertexceptionlog(exceptionmodel);
        				mv.addObject("id", exceptionmodel.getNum_id());
        				mv.addObject("reasons", exceptionmodel.getUser_remarks());
        				mv.addObject("url", url);
        				mv.addObject("UserName", UserName);
        				mv.addObject("RoleName", RoleName);
        				mv.addObject("BranchID", BranchID);
        				mv.addObject("RoleId", RoleId);
        				mv.addObject("details", exceptionmodel.getException_details());
        				mv.setViewName("ExceptionPage");
         			}else {
         				mv.setViewName("sessionExpire");
         			}
         		}
             }
             ex.printStackTrace();
             return mv;
         }
		
		return null;
	}
}
