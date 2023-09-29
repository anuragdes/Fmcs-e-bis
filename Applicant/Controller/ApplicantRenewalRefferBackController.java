package Applicant.Controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import Global.CommonUtility.ResourceBundleFile;
import Global.CommonUtility.Controller.GlobalTextEditorAction;
import Global.CommonUtility.Controller.GlobalUploadDownloadHandler;
import Global.CommonUtility.Controller.UploadFiles;
import Global.CommonUtility.DAO.DaoHelper;
import Global.Login.Model.Session;
import Global.Login.Service.IMigrateService;
import Global.Registration.Domain.RegisterDomain;
import Schemes.ProductCertification.ApplicationSubmission.Domain.Licence_Brand_Detail_Domain;
import Schemes.ProductCertification.ApplicationSubmission.Domain.Licence_Details_Domain;
import Schemes.ProductCertification.ApplicationSubmission.Domain.UploadDoc_Domain_Inclusion_renewal;
import Schemes.ProductCertification.BO.Domain.PCRenewalTrackingDomain;
import Schemes.ProductCertification.BO.Service.IBranchHeadService;
import Schemes.ProductCertification.LicecneRenewal.DAO.LicenceRenewal_Performance_DAO;
import Schemes.ProductCertification.LicecneRenewal.Domain.LR_Performance_Address_Domain;
import Schemes.ProductCertification.LicecneRenewal.Domain.LicenceRenewal_Performance_Domain;
import Schemes.ProductCertification.LicecneRenewal.Model.LicenceRenewal_Application_Model;
import Schemes.ProductCertification.LicecneRenewal.Model.LicenceRenewal_Performance_Model;
import Schemes.ProductCertification.LicecneRenewal.Service.LicenceRenewal_BlueForm_Service;
import Schemes.ProductCertification.LicecneRenewal.Service.LicenceRenewal_Performance_Service;
import Schemes.ProductCertification.LicenceGranting.Service.productionReturn_Service;
import Schemes.ProductCertification.SurveillanceDue.Dao.SurveillanceDueDao;
import eBIS.AppConfig.CustomWebExceptionHandler;

@Controller
public class ApplicantRenewalRefferBackController {
	
	@Autowired
	SurveillanceDueDao survDao;
	
	@Autowired
	DaoHelper daoHelper;

	@Autowired
	LicenceRenewal_Performance_Service LR_Performance_Service;	 	
	
	@Autowired
	LicenceRenewal_Performance_DAO performanceDAO;
	@Autowired
	IMigrateService dcrypt;
	
	@Autowired
	GlobalUploadDownloadHandler gudh;
	
	@Autowired	
	IBranchHeadService iBranchHeadService;
	
	@Autowired
    GlobalTextEditorAction globalTextEditor;
	
	@Autowired
	productionReturn_Service prdServ;
	
	@Autowired
	 GlobalUploadDownloadHandler theUpload;
	
	@Autowired
	LicenceRenewal_BlueForm_Service lr_bf_Service;
	
	
	@Autowired
	UploadFiles ftpFiles;
	
	String globalPath = ResourceBundleFile.getValueFromKey("DOCUPLOADPATH");
	@SuppressWarnings({"unused"})
	@CustomWebExceptionHandler()
	@RequestMapping(value="/applicantRenewalRefferBack",method=RequestMethod.GET)
	public ModelAndView applicantRenewalRefferBack(HttpServletRequest request){
		
		String cmlNo=request.getParameter("cmlNo");
		String branchId=request.getParameter("branchId");
		return new ModelAndView("applicantRenewalRefferBack");
	}
	// Open performance jsp page mansi modify
	@RequestMapping(value="/performanceDetail",method=RequestMethod.GET)
	public ModelAndView performanceDetail(@ModelAttribute("performance") LicenceRenewal_Performance_Model LR_Performance_Model,@ModelAttribute("application") LicenceRenewal_Application_Model LR_Application_Model, HttpServletRequest request)
	{		
		System.out.println("************abc****************");
	    ModelAndView performance_Mv = new ModelAndView();
	    int branch_Id=0;
	    String licence_No = ""; 
	    String appID = "";
	    String encrypted_ln ="";
	    String encrypted_branch ="";
	    int Userid=0; 
	    String ErenewalId="";
	    int renewalId=0;
	    //String message="";	
	    encrypted_ln =request.getParameter("number");    
	    licence_No = dcrypt.Dcrypt(encrypted_ln); 
	    encrypted_branch = request.getParameter("branch");
	    branch_Id = Integer.parseInt(dcrypt.Dcrypt(encrypted_branch));   
	    System.out.println("***********licence_No*********"+licence_No);
	    System.out.println("***********branch_Id*********"+branch_Id);
	    List<Licence_Details_Domain> licence_Detail = performanceDAO.getLicenceData(licence_No,branch_Id);	
		 appID = licence_Detail.get(0).getStr_app_id();
		 performance_Mv.addObject("branchId",branch_Id);
			performance_Mv.addObject("licence_No",licence_No);
	      try { 
			HttpSession httpsession = request.getSession(false);
			Session userSession;		
			String role="";		
			userSession = (Session) httpsession.getAttribute("logged-in");
			
			if(userSession!=null)
			{			
				//String period_from ="";
				String app_id="";
				String str_period_from="";
				String str_period_to="";
				String period_from2="";
	            String period_to2="";
				Userid=userSession.getUserid();
				List<HashMap<String,String>> listhmLicence = new ArrayList<HashMap<String,String>>();
				listhmLicence = LR_Performance_Service.getLicenceDetailHm(licence_No,branch_Id);	
				System.out.println("*************listhmLicence*************"+listhmLicence.size());
				if(listhmLicence.size()==0){
					performance_Mv.setViewName("markingFeeMsg");
				}
				else if(listhmLicence.size()>0){
					app_id = listhmLicence.get(0).get("app_id");
					String cml_no = listhmLicence.get(0).get("cml_no");
					str_period_from = listhmLicence.get(0).get("period_from");
					str_period_to = 	listhmLicence.get(0).get("period_to");	
					performance_Mv.addObject("listhmLicence",listhmLicence);
				//}
				
					List<Map<String,Object>> licenceList = performanceDAO.getprod_date(licence_No,branch_Id);
					System.out.println("*************licenceList*************"+licenceList.size());
					if(licenceList.size()>0){
						Map<String, Object> tempRow =licenceList.get(0);
						String dates = tempRow.get("prod_date")+"";
						//String from_date=licenceList.get(0).get("prod_date");
			            System.out.println("dates.."+dates); 
			            String period_from1 []=dates.split("#");
			            
			            if(period_from1.length == 1){
			            	period_from2=period_from1[0];
			            }else if(period_from1.length>=2){
			            	period_from2=period_from1[0];
			            	period_to2=period_from1[1];
			            	performance_Mv.addObject("period_from2",period_from2);
			            	performance_Mv.addObject("period_to2",period_to2);
			            }
			             
						
					}
			
				List<Licence_Brand_Detail_Domain> cml_brand_detail = LR_Performance_Service.getBrandDetail(licence_No,branch_Id);			
				String brand_list_result = "";
				System.out.println("*************cml_brand_detail*************"+cml_brand_detail.size());
				if(cml_brand_detail.size()>0){
				Set<String> brand_list = new HashSet<String>();
				for (int icount = 0; icount < cml_brand_detail.size(); icount++) {
					brand_list.add(""+ cml_brand_detail.get(icount).getStr_brand_name() + "");
				}

				String separator = ",";
				int total = brand_list.size() * separator.length();
				for (String s : brand_list) {
					total += s.length();
					
				}

				StringBuilder sb = new StringBuilder(total);
				for (String s : brand_list) {
					sb.append(separator).append(s);
				}
				 brand_list_result = sb.substring(separator.length());
				 performance_Mv.addObject("brand_list_result",brand_list_result);
				}
				
	          
				List<HashMap<String,String>> return_file_list_hm = new ArrayList<HashMap<String,String>>();
				//return_file_list_hm = LR_Performance_Service.getReturn_File_Detail_Hm(licence_No,branch_Id,period_from2,period_to2);		
				System.out.println("*************return_file_list_hm*************"+return_file_list_hm.size());
				if(return_file_list_hm.size()>0){
					performance_Mv.addObject("return_file_list_hm",return_file_list_hm);
				}
				
				// get sum of return file data to fill in the performance from
				List<HashMap<String,String>> return_file_sum = new ArrayList<HashMap<String,String>>();
				//return_file_sum = LR_Performance_Service.getReturn_File_Sum_Hm(licence_No,branch_Id,period_from2,period_to2);
				long totalQuantity =0;
				//float totalQuantity =0.0f;
				if(return_file_sum.size()>0){
					HashMap<String, String> map = return_file_sum.get(0);
					String total_production_quantity = map.get("total_production_quantity");
					String total_production_marked = map.get("total_production_marked");
					if(!total_production_quantity.equals("NA") && !total_production_marked.equals("NA")){					
						if(Float.parseFloat(total_production_quantity) >= Float.parseFloat(total_production_marked)){
						totalQuantity = (long) (Float.parseFloat(total_production_quantity) - Float.parseFloat(total_production_marked));
						}
						performance_Mv.addObject("return_file_sum",return_file_sum);
					}
					System.out.println("*************return_file_sum*************"+return_file_sum.size());	
				}
				performance_Mv.addObject("totalQuantity", totalQuantity);
				

				//production return get consinee
				List<RegisterDomain> theRegisterDtls = prdServ.getTheRegisterDtls(Userid);
	  	  	 	int countryId = 0;
	  	  	 	if(theRegisterDtls.size()>0){
	  	  	 		countryId = theRegisterDtls.get(0).getNumCountryId(); 
	  	  	 	}  	  	 		
	  	  	 	List<HashMap<String,String>> lsHashMap2 = LR_Performance_Service.getConsigneeDtls(licence_No,countryId);
	  	  	    List<HashMap<String,String>> lsHashMapimport = LR_Performance_Service.getConsigneeImport(licence_No,countryId);  	  	 	
			
		//get drafted data		
	  	  	    List<LicenceRenewal_Performance_Domain> draft_Data= LR_Performance_Service.get_reffer_back_Data(licence_No,branch_Id,str_period_from);
	  	  	    if(draft_Data.size()>0){	 	 
		
					 int foreign_key = draft_Data.get(0).getNum_id();
					 List<LR_Performance_Address_Domain>  purchaser_Address = LR_Performance_Service.getPurchaserAddress(foreign_key);				 
					 ArrayList<LR_Performance_Address_Domain>  purchaser_Address_Return = new ArrayList<LR_Performance_Address_Domain>();	 	
					 if(purchaser_Address.size()>0){				 
						for(int icount=0;icount<purchaser_Address.size();icount++){		
						LR_Performance_Address_Domain purchaser_Address_Object = new LR_Performance_Address_Domain();		
						purchaser_Address_Object.setStr_Name(purchaser_Address.get(icount).getStr_Name());
						purchaser_Address_Object.setStr_Address( purchaser_Address.get(icount).getStr_Address());
						purchaser_Address_Object.setNum_Address_id(purchaser_Address.get(icount).getNum_Address_id());
						//purchaser_Address_Object.set 		
						 String name= purchaser_Address.get(icount).getStr_Name();
						 String address= purchaser_Address.get(icount).getStr_Address();			
						 purchaser_Address_Return.add(purchaser_Address_Object); 	
						 }
						 performance_Mv.addObject("purchaser_Address_Return", purchaser_Address_Return);	 
					 }	
					 System.out.println("*************purchaser_Address_Return*************"+purchaser_Address_Return.size());	
					 ArrayList<LR_Performance_Address_Domain> importer_Address_Return = new ArrayList<LR_Performance_Address_Domain>();
					 List<LR_Performance_Address_Domain> importer_Address   = LR_Performance_Service.getImporterAddress(foreign_key);	
					 if(importer_Address.size()>0){
						 for(int icount=0; icount<importer_Address.size();icount++){
						 LR_Performance_Address_Domain importer_Address_Object = new LR_Performance_Address_Domain();
						 importer_Address_Object.setStr_Name(importer_Address.get(icount).getStr_Name());			 
						 importer_Address_Object.setStr_Address(importer_Address.get(icount).getStr_Address());
						 importer_Address_Object.setNum_Address_id(importer_Address.get(icount).getNum_Address_id());
						 importer_Address_Return.add(importer_Address_Object);		 
						 String name= importer_Address.get(icount).getStr_Name();
						String address=	 importer_Address.get(icount).getStr_Address();
						
						 }
						 
						 performance_Mv.addObject("importer_Address_Return", importer_Address_Return);
					 } 
					 System.out.println("*************importer_Address_Return*************"+importer_Address_Return.size());
					 performance_Mv.addObject("fee_to_be_paid", BigDecimal.valueOf(draft_Data.get(0).getNum_Marking_Fee())); // send fee in decimal format
					 performance_Mv.addObject("draft_Data", draft_Data);
		 
	  	  	    	}
	 

				List<LicenceRenewal_Performance_Domain> lr_performance_detail = new ArrayList<LicenceRenewal_Performance_Domain>();
				
				
				if(draft_Data.size()>0){
					//Previous Details will be updated
					LicenceRenewal_Performance_Domain performanceDomain = 	draft_Data.get(0);
					//lr_performance_detail =LR_Performance_Service.getLr_performance_detail(cml_no,branch_Id);
					renewalId=performanceDomain.getNum_renewal_id();
					ErenewalId= dcrypt.Jcrypt(Integer.toString(renewalId));	
				}else{	
					lr_performance_detail =LR_Performance_Service.getLr_performance_detail(cml_no,branch_Id);
					if(lr_performance_detail.size()>0){
						renewalId=lr_performance_detail.get(0).getNum_renewal_id();
						renewalId+=1;
						ErenewalId= dcrypt.Jcrypt(Integer.toString(renewalId));	
					}else{
						renewalId=1;
						ErenewalId= dcrypt.Jcrypt(Integer.toString(renewalId));	
					}	
				}
				List<LicenceRenewal_Performance_Domain> performance_max_detail=LR_Performance_Service.getLr_performance_detail_max_record(cml_no,branch_Id);
				performance_Mv.addObject("performance_max_detail",performance_max_detail);
				List<PCRenewalTrackingDomain> cml_max_detail=LR_Performance_Service.getcml_renewal_max_record(cml_no,branch_Id);
				System.out.println("*****************cml_max_detail*************************"+cml_max_detail.get(0).getStr_renewal_applicant_remarks());
				performance_Mv.addObject("cml_max_detail",cml_max_detail);
				performance_Mv.addObject("ErenewalId",ErenewalId);
				
				System.out.println("RenewalId \t"+renewalId);
				
				String ecertificate_to_upload=dcrypt.Jcrypt("320");
				String eConsignee=dcrypt.Jcrypt("334");
				String EschemeId=dcrypt.Jcrypt("1");
				String EappId = dcrypt.Jcrypt(app_id);
				String CA_Authentication = gudh.CheckFileUploadForDocId(app_id, branch_Id, 320, 1);	
				List<UploadDoc_Domain_Inclusion_renewal> uploadrenewal =performanceDAO.getuploadRenewal(licence_No,branch_Id,renewalId);
				if(uploadrenewal.size()>0){
					System.out.println("uploadrenewal"+uploadrenewal.size());
					performance_Mv.addObject("uploadrenewal", uploadrenewal.size());
				}
				else{
					performance_Mv.addObject("uploadrenewal", "0");
				}
				 System.out.println("*************uploadrenewal*************"+uploadrenewal.size());
				
				List<UploadDoc_Domain_Inclusion_renewal> uploadConsignee =performanceDAO.getuploadconsignee(licence_No,branch_Id,renewalId);
				System.out.println("*************uploadConsignee*************"+uploadConsignee.size());
				if(uploadConsignee.size()>0){
					
					performance_Mv.addObject("uploadConsignee", uploadConsignee.size());
				}
				else{
					performance_Mv.addObject("uploadConsignee", "0");
				}
				
				if(request.getParameter("message")!=null){	
					performance_Mv.addObject("message", request.getParameter("message").trim());
				}
				//mansi
				 List<PCRenewalTrackingDomain> getcml_tracking_data_status_604 =lr_bf_Service.getcml_tracking_data_status_604(cml_no,branch_Id);
				 performance_Mv.addObject("getcml_tracking_data_status_604", getcml_tracking_data_status_604);
				 int scaleId =performanceDAO.getScaleID(Userid);
				 performance_Mv.addObject("scaleId",scaleId+"");
				//performance_Mv.addObject("message", status_msg);
				performance_Mv.addObject("ecertificate_to_upload",ecertificate_to_upload);
				performance_Mv.addObject("EschemeId",EschemeId);
				performance_Mv.addObject("EappId",EappId);
				performance_Mv.addObject("encrypted_branch",encrypted_branch);
				performance_Mv.addObject("CA_Authentication",CA_Authentication);
				
				performance_Mv.addObject("appID",appID);
				performance_Mv.addObject("lsHashMap2", lsHashMap2);
				performance_Mv.addObject("lsHashMap3", lsHashMapimport);
				performance_Mv.addObject("ecmlno", encrypted_ln);
				performance_Mv.addObject("eConsignee",eConsignee);
				performance_Mv.addObject("eflag",dcrypt.Jcrypt("1"));
				//message="";
				performance_Mv.setViewName("applicantRenewalRefferBack");
				
				
				}
						}	
				else{
						
						performance_Mv.setViewName("sessionExpire");
						
					}
				
						}
				catch (Exception e) 
					{
						e.printStackTrace();
					}
				
				return performance_Mv;
				
				   
				 
				}
	//save_As_Draft implementation

	@RequestMapping(value="/save_As_reffer_back",method=RequestMethod.POST)
	public ModelAndView save_As_Draft(@ModelAttribute("performance") LicenceRenewal_Performance_Model LR_Performance_Model,MultipartHttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectattribute)
	{                                 
		
		ModelAndView save_as_draft_mv = new ModelAndView();		
		int Userid=0;
		HttpSession httpsession = request.getSession(false);
		Session userSession;	
		userSession = (Session) httpsession.getAttribute("logged-in");
		Userid=userSession.getUserid();
		String user_File_Name = request.getParameter("file_name");	
		int branch_Id =LR_Performance_Model.getBranchId();
		String encrypted_branch = dcrypt.Jcrypt(Integer.toString(branch_Id));
		String licence_No = LR_Performance_Model.getLicence_no();
		String encrypted_ln =dcrypt.Jcrypt(licence_No);
		String appID = LR_Performance_Model.getAppID();	
		String file_name = appID+"_"+licence_No+"_CA_Authentication"+"_"+LR_Performance_Model.getDt_StartDate().trim()+".pdf";
		System.out.println("*******************file_name***********"+file_name);
		String path = globalPath+appID;
		System.out.println("*******************path***********"+path);
		String final_path=path.substring(path.indexOf("/")+1, path.length());
		System.out.println("*******************final_path***********"+final_path);
		System.out.println("Final String    ......   "+final_path+"/"+file_name);
		LR_Performance_Model.setStatus(27);
		//if(!ftpFiles.fileExistsOnFTP(final_path+"/"+file_name).equals("file_not_found")){
			
			System.out.println("file found on ftp forward-------------------------------");
			
			 List<PCRenewalTrackingDomain> getcml_tracking_data =lr_bf_Service.getcml_tracking_data(licence_No, branch_Id);
			 if(getcml_tracking_data.size()>0){
				 PCRenewalTrackingDomain pcRenewalTrackingDomain= getcml_tracking_data.get(0);
				 pcRenewalTrackingDomain.setNum_renew_visible_applicant(0);
				 pcRenewalTrackingDomain.setStr_renewal_remarks(LR_Performance_Model.getApplicant_remark());
				 lr_bf_Service.saveCmlRenewalTrackingDetails(pcRenewalTrackingDomain);
			 }
			LR_Performance_Service.save_As_Draft_Data(LR_Performance_Model,file_name,licence_No,Userid,branch_Id,appID);// save data to database	
			//save_as_draft_mv.setViewName("redirect:/performance?number="+encrypted_ln+"&branch="+encrypted_branch);
		/*}
		else{
			System.out.println("file not found on ftp"  );
			file_name="";
			LR_Performance_Service.save_As_Draft_Data(LR_Performance_Model,file_name,licence_No,Userid,branch_Id,appID);
			
		}	*/
		save_as_draft_mv.setViewName("redirect:/applicantGrantedLicense");
		return save_as_draft_mv;

	}
	
	@RequestMapping(value="/checkApplication",method=RequestMethod.POST)
	public @ResponseBody String checkApplication(String licenceNo,int branchId){
		
		int status=LR_Performance_Service.checkApplication(licenceNo,branchId);
		
		return status==604?"true":"false";
		
	}
	
	
}
