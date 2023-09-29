package Global.Dashboard.Controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import Global.Dashboard.Service.DGDashboardSchemesService;
import Global.Login.Model.Session;
import Schemes.ProductCertification.Inspection.Model.ReviewOfPerformanceModel;
import eBIS.AppConfig.CustomWebExceptionHandler;

@Controller
public class DGDashboardSchemeController {

	@Autowired
	DGDashboardSchemesService dgService;
	@CustomWebExceptionHandler()
	@RequestMapping(value="/getDGDashboardSchemes", method=RequestMethod.POST)
	public @ResponseBody List<LinkedHashMap<String, String>> showROPForm(@ModelAttribute("ropmodel") ReviewOfPerformanceModel rpModel,HttpServletRequest request){
		List<LinkedHashMap<String, String>> lhm = new ArrayList<LinkedHashMap<String, String>>();
		try {
			HttpSession httpsession = request.getSession(false);
			Session userSession;
			userSession = (Session) httpsession.getAttribute("logged-in");
			if(userSession!=null){
				int iRoleId = Integer.parseInt(userSession.getCurrent_role());
				lhm = dgService.getSchemesForRoleId(iRoleId);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return lhm;
	}
}
