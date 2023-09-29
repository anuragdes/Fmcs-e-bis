package Global.Dashboard.Controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import Global.Dashboard.DAO.DGDashboardRevenueDao;
import Global.Login.Model.Session;
import eBIS.AppConfig.CustomWebExceptionHandler;

@Controller
public class DGDashboardRevenueController {

	@Autowired
	DGDashboardRevenueDao dpcdao;
	@CustomWebExceptionHandler()
	@RequestMapping(value="/DGRevenueDashboard", method=RequestMethod.POST)
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
				int ifeeId = 0;
				String todate =new Date()+"";
				String fromdate =new Date()+"";

				if(iLocationTypeId==1){
					iRegionId = iLocationId;
				}
				else if(iLocationTypeId==2){
					iBranchId = iLocationId;
				}
				
				if(request.getParameter("bid")!=null && !request.getParameter("bid").equals("0")){
					iBranchId = Integer.parseInt(request.getParameter("bid"));
				}
				if(request.getParameter("rid")!=null && !request.getParameter("rid").equals("0")){
					iRegionId = Integer.parseInt(request.getParameter("rid"));
				}
				if(request.getParameter("fid")!=null){
					ifeeId = Integer.parseInt(request.getParameter("fid"));
				}
				if(request.getParameter("todate")!=null){
					todate = request.getParameter("todate");
				}
				if(request.getParameter("fromdate")!=null){
					fromdate = request.getParameter("fromdate");
				}
				
				String status = "0300";
				if(request.getParameter("status")!=null){
					status = request.getParameter("status");
				}
				//Getting Data	
				//1.Amount paid for application submission
				//hlhm.put("paidApp", dpcdao.getPaidApplications(iRoleId,iRegionId,iBranchId,fromdate,todate,status));
				//2.Amount paid for Renewal
				//hlhm.put("paidRenewal", dpcdao.getPaidRenewal(iRoleId,iRegionId,iBranchId,fromdate,todate, status));
				//3.Amount paid for Inclusion
				//hlhm.put("paidInclusion", dpcdao.getPaidInclusion(iRoleId,iRegionId,iBranchId, fromdate,todate, status));
				
				//4.Amount paid for Different Header
				hlhm.put("paidOther", dpcdao.getPaidOther(iRoleId,iRegionId,iBranchId, ifeeId, fromdate, todate));
				
				//5.for refund
				//hlhm.put("refundamt", dpcdao.getPaymentDetail_Status(iRoleId,iRegionId,iBranchId,fromdate,todate,"2000"));
				
				//6.Total amount at branch(application+licence)
				hlhm.put("branchWiseRevenue", dpcdao.getPaidAmtAtBranch(iRoleId,iRegionId,iBranchId, ifeeId, fromdate, todate));
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return hlhm;
	}
	
}
