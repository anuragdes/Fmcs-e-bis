package Global.Dashboard.Controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import Global.Dashboard.DAO.DGDashboardDao;
import Global.Login.Model.Session;
import Masters.Domain.Branch_Master_Domain;
import Masters.Domain.Regional_Mst_Domain;
import Masters.Domain.State_Mst_Domain;
import Schemes.ProductCertification.Inspection.Model.ReviewOfPerformanceModel;
import eBIS.AppConfig.CustomWebExceptionHandler;
import lab.domain.Lab_Type_Domain;

@Controller
public class DGDashboardController {
	
	@Autowired
	DGDashboardDao dgdashdao;

	@CustomWebExceptionHandler()
	@RequestMapping(value="/Dashboard", method=RequestMethod.GET)
	public ModelAndView showDGDashboard(@ModelAttribute("ropmodel") ReviewOfPerformanceModel rpModel,HttpServletRequest request){
		ModelAndView mv = new ModelAndView();
		try {
			HttpSession httpsession = request.getSession(false);
			Session userSession;
			userSession = (Session) httpsession.getAttribute("logged-in");
			if(userSession!=null){
				int iRoleId = Integer.parseInt(userSession.getCurrent_role());

				if(dgdashdao.checkDashboardAccess(iRoleId)){
					int iRegionId = 0;
					int iLocationId = userSession.getLocation_id();
					int iLocationTypeId = userSession.getLocation_type();
					if(iLocationTypeId==1){
						iRegionId = iLocationId;
					}
					List<Regional_Mst_Domain> lrmd = dgdashdao.getDynamicRegions(iRoleId,iLocationId);
					List<State_Mst_Domain> lstates= dgdashdao.getStates();
					
					List<Branch_Master_Domain> branches = dgdashdao.getDynamicBranches(iRoleId, iLocationId, iRegionId);
					List<Lab_Type_Domain> labTypes = dgdashdao.getLabTypes();
					
					mv.addObject("roleId",iRoleId);
					mv.addObject("regions",lrmd);
					mv.addObject("states",lstates);
					
					mv.addObject("branches",branches);
					mv.addObject("labTypes",labTypes);
					mv.setViewName("dashboarddcgi");
				}
				else{
					mv.setViewName("redirect:/login");
				}
				
			}else{
				mv.setViewName("redirect:/sessionExpire");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			mv.setViewName("redirect:/login");
		}

		return mv;
	}
	
	@RequestMapping(value="/getDynamicRegions", method=RequestMethod.POST)
	public @ResponseBody List<Regional_Mst_Domain> getDynamicRegions(HttpServletRequest request){
		List<Regional_Mst_Domain> lrmd = new ArrayList<Regional_Mst_Domain>();
		try {
			HttpSession httpsession = request.getSession(false);
			Session userSession;
			userSession = (Session) httpsession.getAttribute("logged-in");
			if(userSession!=null){
				int iRoleId = Integer.parseInt(userSession.getCurrent_role());
				int iLocationId = userSession.getLocation_id();
				
				lrmd = dgdashdao.getDynamicRegions(iRoleId,iLocationId);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return lrmd;
	}
	
	@RequestMapping(value="/getDynamicBranches", method=RequestMethod.POST)
	public @ResponseBody List<Branch_Master_Domain> getDynamicBranches(HttpServletRequest request){
		List<Branch_Master_Domain> lrmd = new ArrayList<Branch_Master_Domain>();
		try {
			HttpSession httpsession = request.getSession(false);
			Session userSession;
			userSession = (Session) httpsession.getAttribute("logged-in");
			if(userSession!=null){
				int iRoleId = Integer.parseInt(userSession.getCurrent_role());
				int iLocationId = userSession.getLocation_id();
				int iRegionId = 0;
				
				if(request.getParameter("regionId")!=null){
					iRegionId = Integer.parseInt(request.getParameter("regionId"));
				}
			
				lrmd = dgdashdao.getDynamicBranches(iRoleId,iLocationId,iRegionId);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return lrmd;
	}
}
