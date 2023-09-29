package Applicant.Controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.ServletOutputStream;
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

import Global.CommonUtility.ResourceBundleFile;
import Global.CommonUtility.Model.TextEditor_Model;
import Global.CommonUtility.Service.SendMail;
import Global.Login.Model.Session;
import Global.Login.Service.IMigrateService;
import Schemes.ProductCertification.ApplicationSubmission.Service.applicationSubmissionService;
import Schemes.ProductCertification.LicecneRenewal.Service.DefermentNoticeService;
import Schemes.ProductCertification.LicecneRenewal.Service.JasperReportService;
import Schemes.ProductCertification.LicecneRenewal.Service.RenewalNoticeService;
import Schemes.ProductCertification.LicenceGranting.Service.LicenceGranting_Service;
import eBIS.AppConfig.CustomWebExceptionHandler;

@Controller
public class generatePCApplicationFormController {

	
	private int branch_id=0;
	private String application_id="";
	private int numUserId=0;
	private String cml_no="";
	@Autowired
	JasperReportService reportService;
	
	@Autowired
	RenewalNoticeService noticeService;
	
	@Autowired
	LicenceGranting_Service lg_service;
	
	@Autowired
	DefermentNoticeService def_notice;
	
	@Autowired
		IMigrateService crypt; // cryptography
	
	@Autowired
	applicationSubmissionService appSubService;
	
	@Autowired
	JasperReportService jrs;
	
	@Autowired
	SendMail sm;
	
	@CustomWebExceptionHandler()	
	@RequestMapping(value="/genPCAppForm",method=RequestMethod.GET)
	public void genPCAppForm(HttpServletRequest request,HttpServletResponse response){
	
	
			//cml_no="61930";
		//8138170
//		application_id="8198403";
//  		 branch_id=81;
		
	//	String jrxmlName="LicenceMarkPermit";
			if(request.getParameter("appID")!=null)
				application_id=crypt.Dcrypt(request.getParameter("appID").toString());

			if(request.getParameter("branchId")!=null)
				branch_id=Integer.parseInt(crypt.Dcrypt(request.getParameter("branchId").toString()));
			System.out.println("application_id   "+application_id);
			System.out.println("branch_id    "+branch_id);
			
//			HashMap parameters=new HashMap();
//			 parameters.put("str_cml_no", "8100010882");
//		       parameters.put("num_branch_id", 81);
		  

			
		try{
			reportService.generatePCApplicationFormReport(request, response, application_id, branch_id, "pdf");
		
			//reportService.generateJrxmlReport(request, response, parameters, jrxmlName, "pdf");
			//			try {
//				  String k =  "G:\\BISPROJECT\\JRxmlsReports\\pdf\\PCApplicationForm_8113816.html";
//			
//
//			 
//			        System.out.println( "PDF Created!" );
//			 
//			} catch (Exception e) {
//			    e.printStackTrace();
//			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
			
		
	}
	
	@RequestMapping(value="/genJewelerAppForm",method=RequestMethod.GET)
	public void genJewelerAppForm(HttpServletRequest request,HttpServletResponse response){
	
	
			if(request.getParameter("appID")!=null)
				application_id=crypt.Dcrypt(request.getParameter("appID").toString());

			if(request.getParameter("branchId")!=null)
				branch_id=Integer.parseInt(crypt.Dcrypt(request.getParameter("branchId").toString()));
			System.out.println("application_id   "+application_id);
			System.out.println("branch_id    "+branch_id);
						
		try{
			reportService.generateJewelerApplicationFormReport(request, response, application_id, branch_id, "pdf");
		
		}
		catch (Exception e) {
			e.printStackTrace();
		}
			
		
	}
	
	@RequestMapping(value="/genRenewalNoticeForm",method=RequestMethod.GET)
	public void genRenewalNoticeForm(HttpServletRequest request,HttpServletResponse response)throws MessagingException{
	

		String emailId="";
		int actionId=0;
	String StrConName;
			//cml_no="61930";
		//8138170
			application_id="8198394";
     		 branch_id=81;
			if(request.getParameter("appID")!=null)
				application_id=request.getParameter("appID").toString();

			if(request.getParameter("branchId")!=null)
				branch_id=Integer.parseInt(request.getParameter("branchId").toString());
			
			if(request.getParameter("actionId")!=null)
				actionId=Integer.parseInt(request.getParameter("actionId").toString());
			
			
			if(request.getParameter("cmlNo")!=null)
				cml_no=request.getParameter("cmlNo").toString();
			
			if(request.getParameter("con")!=null)
				StrConName=request.getParameter("con").toString();
			
			System.out.println("application_id   "+application_id);
			System.out.println("branch_id    "+branch_id);
			System.out.println("actionId    "+actionId);
			
		try{
			HttpSession httpsession = request.getSession(false);
			Session userSession;
			userSession = (Session) httpsession.getAttribute("logged-in");
			int userid = userSession.getUserid();
			int roleId = Integer.parseInt(userSession.getCurrent_role());
			
			reportService.generateRenewalNoticeReport(request, response, application_id, branch_id,cml_no, "pdf");
			System.out.println("Notice Generated ");
			noticeService.sendRenewalNotice(application_id, branch_id,cml_no,userid, roleId,actionId);  //save in Pc_application_tracking
			List<HashMap<String, String>> applicationDetail_Hm = new ArrayList<HashMap<String, String>>();
			applicationDetail_Hm =lg_service.getApplicationDetail_Hm(application_id,branch_id);
			if(applicationDetail_Hm!=null)
			{
			System.out.println(" applicationDetail_Hm.size()    "+ applicationDetail_Hm.size());
			System.out.println("factory_email in applicationDetail_Hm     "+applicationDetail_Hm.get(0).get("factory_email"));
		
			}
			
			emailId=applicationDetail_Hm.get(0).get("factory_email");  
            System.out.println("emailId  in send Renewal Notice "+emailId);
			noticeService.saveNoticeSentDtls(application_id, branch_id,cml_no,userid,emailId);                 //save in notice_sent_tracker
			System.out.println("Application Status Updated ");
			//Applicant Email Id from registration master
		//	emailId=reportService.getApplicantEmail(application_id, branch_id);
			
			
			String emailMsgTxt = "Dear Sir,"+"<br/><br/>Please find the attached notice  <br/><br/>Thanks and Regards<br/>BIS";
//			
            String fileName="";    
            String path="";
			path=ResourceBundleFile.getValueFromKey("JRXML_PDF_PATH");
//            path=session.getServletContext().getRealPath("//app_srv//tdc/gl//undertaking");
          fileName = path+"RenewalNotice_"+cml_no+".pdf";   
//            
			//SendMail sm = new SendMail(); commented by imran
			if(emailId!=null && !emailId.equals("") && !emailId.equals("NA")){
            sm.TransferToMailServerAttachFile(emailId,"Renewal Notice ",emailMsgTxt,fileName);
                 System.out.println("Mail sent to Applicant");
			}
			else{
				System.out.println("Mail in Else");
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}
			
			
		
	}
	
	
	
	@RequestMapping(value="/genRenewalIntimationForm",method=RequestMethod.GET)
	public void genRenewalIntimationForm(HttpServletRequest request,HttpServletResponse response)throws MessagingException{
	

		String emailId="";
		int actionId=0;
	
			cml_no="8100010175";
		//8138170
			application_id="8198398";
     		 branch_id=81;
			if(request.getParameter("cml_no")!=null)
				cml_no=request.getParameter("cml_no").toString();
			
			if(request.getParameter("appID")!=null)                            //////////for Email Id of Receiver
				application_id=request.getParameter("appID").toString();

			if(request.getParameter("branchId")!=null)
				branch_id=Integer.parseInt(request.getParameter("branchId").toString());
			
			if(request.getParameter("actionId")!=null)
				actionId=Integer.parseInt(request.getParameter("actionId").toString());
			
			
			System.out.println("cml_no   "+cml_no);
			System.out.println("branch_id    "+branch_id);
			System.out.println("actionId    "+actionId);
			
		try{
			HttpSession httpsession = request.getSession(false);
			Session userSession;
			userSession = (Session) httpsession.getAttribute("logged-in");
			int userid = userSession.getUserid();
			int roleId = Integer.parseInt(userSession.getCurrent_role());
			//Jrxmlname=name of jasper file
			//format=pdf,html
			//parameters=a hashmap containing name of parameteres of you passing
			//reportService.generateJrxmlReport(request, response, parameters, jrxmlName, strReportFormat)
			
			
			reportService.generateRenewalIntimationReport(request, response, cml_no, branch_id, "pdf");
			System.out.println("Renewal Intimation Notice Generated ");
		//	noticeService.sendRenewalNotice(application_id, branch_id, userid, roleId,actionId);
			//System.out.println("Application Status Updated ");
			emailId=reportService.getApplicantEmail(application_id, branch_id);

			String emailMsgTxt = "Dear Sir,"+"<br/><br/>Please find the attached Renewal Intimation Letter  <br/><br/>Thanks and Regards<br/>BIS";
//			
            String fileName="";    
            String path="";
			path=ResourceBundleFile.getValueFromKey("JRXML_PDF_PATH");
//            path=session.getServletContext().getRealPath("//app_srv//tdc/gl//undertaking");
          fileName = path+"RenewalIntimation_"+cml_no+".pdf";   
            
			//SendMail sm = new SendMail(); commented by imran
      sm.TransferToMailServerAttachFile(emailId,"Renewal Intimation Letter ",emailMsgTxt,fileName);
    System.out.println("Mail sent to Applicant");

		}
		catch (Exception e) {
			e.printStackTrace();
		}
			
			
		
	}
	
	
	@RequestMapping(value="/genCLTestRequestForm",method=RequestMethod.GET)
	public void genCLTestRequestForm(HttpServletRequest request,HttpServletResponse response)throws MessagingException{
	

	int num_test_request_id=0;
	int num_sample_map_id=0;
	int flag=0;
	String sampleType="";
		
			if(request.getParameter("num_test_request_id")!=null)
				num_test_request_id=Integer.parseInt(request.getParameter("num_test_request_id").toString());
			
		

			if(request.getParameter("num_sample_map_id")!=null)
				num_sample_map_id=Integer.parseInt(request.getParameter("num_sample_map_id").toString());
			
			if(request.getParameter("flag")!=null)
				flag=Integer.parseInt(request.getParameter("flag").toString());
			
			if(request.getParameter("sampleType")!=null){
				sampleType=request.getParameter("sampleType").toString();
			}

			
		try{
			HttpSession httpsession = request.getSession(false);
			Session userSession;
			userSession = (Session) httpsession.getAttribute("logged-in");
		
			reportService.generateCLTestReqForm(request, response, num_test_request_id, num_sample_map_id,flag,sampleType, "pdf");
			System.out.println("CL Test Request  Notice Generated ");
		
		
		}
		catch (Exception e) {
			e.printStackTrace();
		}
			
			
		
	}
	
	
	@RequestMapping(value="/Cml_genCLTestRequestForm",method=RequestMethod.GET)
	public void Cml_genCLTestRequestForm(HttpServletRequest request,HttpServletResponse response)throws MessagingException{
	

	int num_test_request_id=0;
	int num_sample_map_id=0;
	int flag=0;
	
		
			if(request.getParameter("num_test_request_id")!=null)
				num_test_request_id=Integer.parseInt(request.getParameter("num_test_request_id").toString());
			
		

			if(request.getParameter("num_sample_map_id")!=null)
				num_sample_map_id=Integer.parseInt(request.getParameter("num_sample_map_id").toString());
			
			if(request.getParameter("flag")!=null)
				flag=Integer.parseInt(request.getParameter("flag").toString());

			
		try{
			HttpSession httpsession = request.getSession(false);
			Session userSession;
			userSession = (Session) httpsession.getAttribute("logged-in");
		
			reportService.Cml_generateCLTestReqForm(request, response, num_test_request_id, num_sample_map_id,flag, "pdf");
			System.out.println("Cml CL Test Request  Notice Generated ");
		
		
		}
		catch (Exception e) {
			e.printStackTrace();
		}
			
			
		
	}
	
	@RequestMapping(value="/viewRenewalNotice",method=RequestMethod.GET)
	public void viewFile( HttpServletRequest request,HttpServletResponse response) throws FileNotFoundException
	{
		System.out.println("in viewRenewalNotice controller");
		
		String docName = "";
		
		//String appID = "";
		if(request.getParameter("eappID")!=null)
			application_id=crypt.Dcrypt(request.getParameter("eappID").toString());
		if(request.getParameter("ebranchId")!=null)
			branch_id=Integer.parseInt(crypt.Dcrypt(request.getParameter("ebranchId").toString()));
		
		docName=def_notice.getRenewalNoticeName(application_id, branch_id);
	
		
		System.out.println("Document Name"+docName );
		if(docName!=null && !docName.equals("")){
		 try
	     {
			 String outputfilepath = ResourceBundleFile.getValueFromKey("JRXML_PDF_PATH");
				
			String strFilePath=outputfilepath+docName;
			System.out.println(strFilePath );		
			response.setContentType("application/pdf");
		    response.setHeader("Content-disposition", "inline;filename="+docName);	

		ServletOutputStream out = response.getOutputStream();
		
	    File fileFound = new File(strFilePath);       
		if(fileFound.exists()){
			//System.out.println("file exist");
		}
	    
	    FileInputStream fileinputstream=null;
	    fileinputstream = new FileInputStream(fileFound);
	   
	    ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
	    byte abyte0[] = new byte[0x500000];
	    
	    
	    do
	    {
	        int i = fileinputstream.read(abyte0);
	        if(i!=-1)
	           bytearrayoutputstream.write(abyte0, 0, i);
	        else
	         {
	           byte abyte1[] = bytearrayoutputstream.toByteArray();
	           response.setContentLength(abyte1.length);
	           out.write(abyte1, 0, abyte1.length);
	           out.flush();
	           out.close();
	           return;
	         }
	    } 
	while(true);
	    
	    
	    
	    
	     }
		 catch(Exception e)
	     {
	     
	       e.printStackTrace();
	     }
		}

		
	}	
	
	
	
	
	
	@RequestMapping(value="/getAllFeePaidDetails",method=RequestMethod.GET)
	public void getAllFeePaidDetails(HttpServletRequest request,HttpServletResponse response)throws MessagingException{
		int num_test_request_id=2845;
		int num_sample_map_id=1;
		int pno=1;
		String samType="A"; //A for Application, C-For Licensed Complaints
			if(request.getParameter("num_test_request_id")!=null)
				num_test_request_id=Integer.parseInt(request.getParameter("num_test_request_id").toString());
			if(request.getParameter("num_sample_map_id")!=null)
			num_sample_map_id=Integer.parseInt(request.getParameter("num_sample_map_id").toString());
			if(request.getParameter("pno")!=null)
				pno=Integer.parseInt(request.getParameter("pno").toString());
			if(request.getParameter("samType")!=null)
				samType=request.getParameter("samType").toString();
		try{
			HttpSession httpsession = request.getSession(false);
			Session userSession;
			userSession = (Session) httpsession.getAttribute("logged-in");
			reportService.getAllFeePaidDetails(request, response,"pdf");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@RequestMapping(value="/applicationDataToShowForAppView",method=RequestMethod.GET)
	public ModelAndView applicationDataToShowForAppView(@ModelAttribute("model1") TextEditor_Model textModel, HttpServletRequest request,HttpServletResponse response)
	{
		String stAppId = "";
		int branchId = 0;
		ModelAndView mav = new ModelAndView();
		try{
			HttpSession httpsession = request.getSession(false);
			if(httpsession == null){
				
				mav.setViewName("sessionExpire");
			}else{
				
					Session userSession=null;
					userSession = (Session) httpsession.getAttribute("logged-in");
					
					if(userSession!=null){
					if(request.getParameter("appID")!=null && request.getParameter("appID")!=""){
						
						stAppId = request.getParameter("appID").toString();
					}
					
					
					if(request.getParameter("branchId")!=null && request.getParameter("branchId")!=""){
						
						branchId = Integer.parseInt(request.getParameter("branchId").toString());
					}	
						
				String eappId = "";
					String ebranchId = "";
					if(request.getParameter("eappId")!=null && request.getParameter("eappID")!="")
						{
						    eappId = request.getParameter("eappId").toString();
							stAppId = crypt.Dcrypt(eappId);
						}
					if(request.getParameter("ebranchId")!=null && request.getParameter("ebranchId")!="")
						{
							ebranchId = request.getParameter("ebranchId").toString();
							branchId = Integer.parseInt(crypt.Dcrypt(ebranchId));
						}
					
					
					//System.out.println("thethethe appId ====" + );
					
						int iUserId = userSession.getUserid();
						String stRoleId = userSession.getCurrent_role(); 
						String msg="-";
							
						mav.setViewName("htmlToShow");
						mav.addObject("appId", stAppId);
					    mav.addObject("branchId", branchId+"");
						mav.addObject("msg",msg);
			/*			mav.addObject("msg",stHtml);
					    mav.addObject("appId", stAppId);
					    mav.addObject("branchId", branchId);
			*/			
					
					}
			
				}
			}
			catch(NullPointerException e){
				e.printStackTrace();
				mav.setViewName("redirect:/login");
			}
			return mav;
	}
	
	
	
	
	@RequestMapping(value="/applicationDataToShow",method=RequestMethod.POST)
	public @ResponseBody String applicationDataToShow(@ModelAttribute("model1") TextEditor_Model textModel, HttpServletRequest request,HttpServletResponse response)
	{
		
	
						
						String stAppId = "";
						String stRegionId = "7";
						String stAction = "";
						String stRemarks = "";
						String status = "";
						String cml = "";
						int branchId = 0;
						
						HashMap<String,String> hm = new HashMap<String,String>();
						
						if(request.getParameter("appID")!=null)
							stAppId=request.getParameter("appID").toString();

						if(request.getParameter("branchId")!=null)
							branchId=Integer.parseInt(request.getParameter("branchId").toString());
						
						
					
						String stHtml = "";
						String GenFinalLetter = "";
						
						
							
							DateFormat todaysDate = new SimpleDateFormat("dd/MM/yyyy");
							String date = todaysDate.format(new Date())+"";
							
							/*stAppId = "81100010";
							branchId = 81;*/
							stHtml = appSubService.FetchHtmlForEditor(41, Integer.parseInt(stRegionId), date,stAppId,branchId);
							GenFinalLetter = "";
							//stHtml = "amit oh yeah";
						//System.out.println("con----"+stHtml);
							/*markingFeeModel model = new markingFeeModel();
							model.setUnitVal(stHtml);
							model.setCheck("YES");*/
					
					
			return stHtml;
	}
	
	
}
