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

import Global.Dashboard.DAO.DGDashboardHQDao;
import Global.Login.Model.Session;
import eBIS.AppConfig.CustomWebExceptionHandler;

@Controller
public class DGDashboardHQController {

	@Autowired
	DGDashboardHQDao dpcdao;
	@CustomWebExceptionHandler()
	@RequestMapping(value="/DGHQDashboard", method=RequestMethod.POST)
	public @ResponseBody HashMap<String,List<HashMap<String,String>>> showROPForm(HttpServletRequest request){
		HashMap<String,List<HashMap<String,String>>> hlhm = new HashMap<String,List<HashMap<String,String>>>();
		try {
			HttpSession httpsession = request.getSession(false);
			Session userSession;
			userSession = (Session) httpsession.getAttribute("logged-in");
			if(userSession!=null){
				
				//Getting and Setting Data
				hlhm.put("first", dpcdao.getStateWisePendingApplications());
				hlhm.put("second", dpcdao.getHQPCPieData());
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return hlhm;
	}
	
}
