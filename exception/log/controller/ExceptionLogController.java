package exception.log.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
import eBIS.AppConfig.CustomWebExceptionHandler;
import exception.log.domain.ExceptionLog;
import exception.log.model.ExceptionLogModel;
import exception.log.service.ExceptionLogService;

@Controller
public class ExceptionLogController {
	@Autowired 
	ExceptionLogService ExceptionService;
	@CustomWebExceptionHandler()
	@RequestMapping(value = "/ExceptionLog", method = RequestMethod.GET)
	public ModelAndView ManualSurveillancePlanList(@ModelAttribute("model") ExceptionLogModel model, HttpServletRequest request, HttpServletResponse responce) {
		ModelAndView mv =new ModelAndView();
		HttpSession httpsession = request.getSession(false);
		Session userSession;
		userSession = (Session) httpsession.getAttribute("logged-in");
		if (userSession != null) {
			try {
			//String roleId = userSession.getCurrent_role();
			//if(!roleId.equals("3"))
			//{
				List<ExceptionLog> UnsolvedExceptionLogList = ExceptionService.getUnsolvedExceptionLogList();
				mv.addObject("UnsolvedExceptionLogList", UnsolvedExceptionLogList);
				List<ExceptionLog> SolvedExceptionLogList = ExceptionService.getSolvedExceptionLogList();
				mv.addObject("SolvedExceptionLogList", SolvedExceptionLogList);
				mv.setViewName("ExceptionLog");
//			}else {
//				mv.setViewName("AccessDenied");
//			}
		}catch(Exception ex) {
			ExceptionLogModel exmodel =new ExceptionLogModel();
			exmodel.setException_date(new Date());
			exmodel.setException_details(Arrays.toString(ex.getStackTrace()));
			exmodel.setException_remarks(ex.toString());
			exmodel.setIs_valid("1");
			exmodel=ExceptionService.insertexceptionlog(exmodel);
			mv.addObject("id", exmodel.getNum_id());
			mv.addObject("reasons", exmodel.getException_details());
			mv.setViewName("ExceptionPage");
		}
		}else {
			mv.setViewName("sessionExpire");
		}
		return mv;
	}
	@RequestMapping(value = "/Exceptiondetails", method = RequestMethod.POST)
	public @ResponseBody List<ExceptionLog> Exceptiondetails(HttpServletRequest request, HttpServletResponse responce) {
	String numid=request.getParameter("numid");
	List<ExceptionLog> list = ExceptionService.getExceptiondetails(numid);
	return list;
	}
}
