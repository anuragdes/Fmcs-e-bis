package Global.Dashboard.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import Global.Dashboard.DAO.DGDashboardLabDao;
import Global.Login.Model.Session;
import eBIS.AppConfig.CustomWebExceptionHandler;
import lab.domain.Lab_Master_Domain;

@Controller
public class DGDashboardLabController {
	
	@Autowired
	DGDashboardLabDao dpcdao;
	@CustomWebExceptionHandler()
	@RequestMapping(value="/DGLabDashboard", method=RequestMethod.POST)
	public @ResponseBody HashMap<String,List<HashMap<String,String>>> getLabData(HttpServletRequest request){
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
				int iLabId = 0;
				int iLabTypeId = 0;
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
				
				if(request.getParameter("tid")!=null){
					if(!request.getParameter("tid").equals("null") && !request.getParameter("tid").equals("0")){
						iLabTypeId = Integer.parseInt(request.getParameter("tid"));
					}
				}
				
				if(request.getParameter("lid")!=null){
					if(!request.getParameter("lid").equals("null")){
						iLabId = Integer.parseInt(request.getParameter("lid"));
					}
				}
				
				//Getting and Setting Data
				hlhm.put("first", dpcdao.getPendingReportsAtBisLab(iRoleId, iRegionId, iBranchId, iLabId,iLabTypeId));
				hlhm.put("second", dpcdao.getPendingReportsAtOSLLab(iRoleId, iRegionId, iBranchId, iLabId,iLabTypeId));
				hlhm.put("third", dpcdao.getUploadedReportsAtBisLab(iRoleId, iRegionId, iBranchId, iLabId,iLabTypeId));
				hlhm.put("fourth", dpcdao.getUploadedReportsAtOSLLab(iRoleId, iRegionId, iBranchId, iLabId,iLabTypeId));
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return hlhm;
	}
	
	
	@RequestMapping(value="/getLabs", method=RequestMethod.POST)
	public @ResponseBody List<Lab_Master_Domain> getLabs(HttpServletRequest request){
		List<Lab_Master_Domain> lrmd = new ArrayList<Lab_Master_Domain>();
		try {
			HttpSession httpsession = request.getSession(false);
			Session userSession;
			userSession = (Session) httpsession.getAttribute("logged-in");
			if(userSession!=null){
				int iLabType = 0;
				
				if(request.getParameter("labType")!=null){
					iLabType = Integer.parseInt(request.getParameter("labType"));
				}
				lrmd = dpcdao.getLabList(iLabType);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return lrmd;
	}
}
