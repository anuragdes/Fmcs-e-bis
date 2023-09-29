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

import Global.Dashboard.DAO.DGDashboardMSCDDao;
import Global.Login.Model.Session;
import eBIS.AppConfig.CustomWebExceptionHandler;

@Controller
public class DGDashboardMSCDController {

	@Autowired
	DGDashboardMSCDDao dmscdcdao;
	@CustomWebExceptionHandler()
	@RequestMapping(value="/DGMSCDDashboard", method=RequestMethod.POST)
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
				int iRegionId = 0;
				if(iLocationTypeId==1){
					iRegionId = iLocationId;
				}
				
				if(request.getParameter("rid")!=null){
					if(!request.getParameter("rid").equals("null") && !request.getParameter("rid").equals("0")){
						iRegionId = Integer.parseInt(request.getParameter("rid"));
					}
				}
				
				//Getting and Setting Data
				hlhm.put("first", dmscdcdao.getApplications(iRoleId, iRegionId));
				hlhm.put("second", dmscdcdao.getLicences(iRoleId, iRegionId));
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return hlhm;
	}
	
}
