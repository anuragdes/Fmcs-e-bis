package Global.Dashboard.Controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import Global.Dashboard.DAO.DGDashboardPCDao;
import Global.Login.Model.Session;
import eBIS.AppConfig.CustomWebExceptionHandler;

@Controller
public class DGDashboardPCController {

	@Autowired
	DGDashboardPCDao dpcdao;
	@CustomWebExceptionHandler()
	@RequestMapping(value="/DGPCDashboard", method=RequestMethod.POST)
	public @ResponseBody HashMap<String,List<HashMap<String,String>>> showROPForm(HttpServletRequest request){
		HashMap<String,List<HashMap<String,String>>> hlhm = new HashMap<String,List<HashMap<String,String>>>();
		try {
			HttpSession httpsession = request.getSession(false);
			Session userSession;
			userSession = (Session) httpsession.getAttribute("logged-in");
			if(userSession!=null){
				int iRoleId = Integer.parseInt(userSession.getCurrent_role());
				int iLocationId = userSession.getLocation_id();
				int iLocationTypeId = userSession.getLocation_type();
				int iBranchId = 0;
				int iRegionId = 0;
				int iStateId = 0;
				if(iLocationTypeId==1){
					iRegionId = iLocationId;
				}
				else if(iLocationTypeId==2){
					iBranchId = iLocationId;
				}
				
				if(request.getParameter("bid")!=null){
					if(!request.getParameter("bid").equals("null") && !request.getParameter("bid").equals("0")){
						iBranchId = Integer.parseInt(request.getParameter("bid"));
					}
				}
				if(request.getParameter("rid")!=null){
					if(!request.getParameter("rid").equals("null") && !request.getParameter("rid").equals("0")){
						iRegionId = Integer.parseInt(request.getParameter("rid"));
					}
				}
				if(request.getParameter("sid")!=null){
					if(!request.getParameter("sid").equals("null")){
						iStateId = Integer.parseInt(request.getParameter("sid"));
					}
				}
				
				//Getting and Setting Data
				hlhm.put("first", dpcdao.getPendingApplications(iRoleId, iRegionId, iBranchId,iStateId));
				hlhm.put("second", dpcdao.getPendingLicences(iRoleId, iRegionId, iBranchId,iStateId));
				hlhm.put("third", dpcdao.getStatusWiseBreakdown(iRoleId, iRegionId, iBranchId,iStateId));
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return hlhm;
	}
	
}
