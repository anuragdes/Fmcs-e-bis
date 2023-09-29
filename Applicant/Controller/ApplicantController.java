package Applicant.Controller;

/***************************Start of program*****************************\
## Copyright Information		: C-DAC, Noida  
## Project Name					: Integrated Web Portal for BIS
## Name of Developer		 	: Siddharth Nangia
## Module Name					: Applicant
## Process/Database Object Name	: BIS_dev
## Purpose						: Different function for getting listing pages for applicant
## Date of Creation				: 01/08/15
## Modification Log				:				
## Modify Date					: 01/09/21
## Reason(CR/PRS)				: changed the user profile condition
## Modify By					: Siddharth Nangia
*/


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import Applicant.DAO.ApplicantDAO;
import Applicant.Model.ApplicantModel;
import Applicant.Model.SelfSuspensionModel;
import Applicant.Model.applicantChngNameModel;
import Applicant.Service.IApplicantService;
import Global.CommonUtility.ResourceBundleFile;
import Global.CommonUtility.Controller.GlobalUploadDownloadHandler;
import Global.CommonUtility.DAO.DaoHelper;
import Global.CommonUtility.DAO.GlobalDao;
import Global.CommonUtility.Domain.TextEditor_Dom;
import Global.CommonUtility.Service.GlobalMethodService;
import Global.Login.Model.Session;
import Global.Login.Service.IMigrateService;
import Global.Login.Service.IUserValidator;
import Masters.Domain.District_Mst_Domain;
import Schemes.HallMarking.Jeweller.ApplicationSubmission.DAO.Application_HallMarking_Dao;
import Schemes.HallMarking.Jeweller.ApplicationSubmission.Service.Application_HallMarking_Service;
import Schemes.HallMarking.Jeweller.HMO.DAO.RcvdApplicationHMDODao;
import Schemes.HallMarking.Jeweller.HMO.Service.HMHeadService;
import Schemes.ProductCertification.ApplicationSubmission.DAO.applicationSubmissionDao;
import Schemes.ProductCertification.ApplicationSubmission.Domain.Licence_Brand_Detail_Domain;
import Schemes.ProductCertification.ApplicationSubmission.Domain.user_profile_domain;
import Schemes.ProductCertification.ApplicationSubmission.Model.FileMeta;
import Schemes.ProductCertification.ApplicationSubmission.Service.applicationSubmissionService;
import Schemes.ProductCertification.ApplicationSubmission.Service.user_profile_service;
import Schemes.ProductCertification.BO.Model.RcvdApplicationDOModel;
import Schemes.ProductCertification.BO.Service.PCCMLParallelProcessingService;
import Schemes.ProductCertification.BO.Service.RcvdApplicationDOService;
import Schemes.ProductCertification.LicecneRenewal.Service.LicenceRenewal_Application_Service;
import eBIS.AppConfig.CustomWebExceptionHandler;
@SuppressWarnings({"unused","unchecked","rawtypes"})
@Controller
public class ApplicantController {

	@Autowired	
	RcvdApplicationDOService iRcvdApplicationService;
	
	@Autowired
	applicationSubmissionDao appSubDao;
	
	@Autowired
	IApplicantService appserv;
	
	@Autowired
	LicenceRenewal_Application_Service lr_App_service;
	
	@Autowired
	applicationSubmissionService appSubService;
	
	@Autowired
	IMigrateService ims;
	
	@Autowired
	GlobalDao gd;
	
	@Autowired
	GlobalUploadDownloadHandler gudh;
	
	 @Autowired
     IUserValidator iuv;
	 
	 @Autowired	
	 GlobalMethodService globalMethodServoce;
		
	@Autowired
	HMHeadService iHmHeadService;
		
	@Autowired
	RcvdApplicationHMDODao d1;
	
	@Autowired
	Application_HallMarking_Service AppHallMarkService;
	
	@Autowired
	Application_HallMarking_Dao appHallMarkDao;
	
	@Autowired
	ApplicantDAO ad;
	
	@Autowired 
	PCCMLParallelProcessingService pcpserv;
	
	@Autowired
	user_profile_service ups;
	
	@Autowired
	DaoHelper daohelper;
	
	
	String globalPath = ResourceBundleFile.getValueFromKey("DOCUPLOADPATH");
	//String cmlNo = "";
	@CustomWebExceptionHandler()
	@RequestMapping(value="/ApplicantPCSubmittedApps",method=RequestMethod.GET)
	public ModelAndView ApplicationRcvdList(@ModelAttribute("RcvdApplicationDO")RcvdApplicationDOModel RcvdAppModel,HttpServletRequest request_p){
		ModelAndView mav = new ModelAndView();
		 
		try{
			HttpSession httpsession = request_p.getSession(false);
			if(httpsession == null){
				mav.setViewName("ErrorPage");
			}else{
				Session userSession=null;
				userSession = (Session) httpsession.getAttribute("logged-in");
					if(userSession!=null){
						int iRoleId = Integer.parseInt(userSession.getCurrent_role());
						int iUserId = userSession.getUserid();
						user_profile_domain user_profile_domainObjs = ups.getUserDtls(iUserId);
						String stCSRFToken = iuv.generateCSRFToken("ApplicantPCSubmittedApps", request_p);
						List<Map<String, Object>> result = appserv.getsti_and_marking_fee_mapping(iUserId);
						//Check for escape Sequence
						if(request_p.getParameter("escape")!=null){
							String stEscapeSeq = request_p.getParameter("escape").trim();
							boolean stResponse = iuv.authenticateCSRFToken("gstapplicantescapesequence", stEscapeSeq, request_p);
							if(stResponse){
								
								List<HashMap> applicantapps = appserv.getApplications(iUserId, iRoleId);
								String EschemeId = ims.Jcrypt("1");		//Sending Encrypted Scheme ID For signed Copy Upload for PC
								String EDocId = ims.Jcrypt("301");	//Adding Encrypting DocID for signed Copy Upload
								mav.addObject("schemeId",EschemeId);
								mav.addObject("docId",EDocId);
								for(int i=0;i<applicantapps.size();i++){
									HashMap templist1 = applicantapps.get(i);
									String app_id = templist1.get("appId").toString();
									for(int j=0;j<result.size();j++) {
										Map<String, Object> templist2 = result.get(j);
										String rapp_id = templist2.get("app_id").toString();		
										if(app_id.equalsIgnoreCase(rapp_id)) {
											templist1.put("do_check", Integer.parseInt(templist2.get("do_check").toString()));
											templist1.put("applicant_check", Integer.parseInt(templist2.get("applicant_check").toString()));
											templist1.put("remark_by_do", templist2.get("remark_by_do").toString());
											templist1.put("encnum_id", ims.Jcrypt(templist2.get("num_id").toString()));
										}
									}										
								}
								String encappeal_doc=ims.Jcrypt("367");
								mav.addObject("appeal_doc",encappeal_doc);
								mav.setViewName("applicantPCDashboardSubmittedApps");
								mav.addObject("controllerName","ApplicantPCSubmittedApps");
								mav.addObject("Applications", applicantapps);
							}
							else{
								mav.setViewName("redirect:/ApplicantPC");
							}
						}
						else{
							//Escape Sequence Not provided
							//Check if User Profile is submitted
							int iProfileCompletionStatus = ups.getProfileCompletionStatus(iUserId);
							if((iRoleId==3) && iProfileCompletionStatus==0){
								//Redirect to User Profile
//								mav.addObject("redirect", "1");
								mav.setViewName("redirect:/PCOrganizationProfile");
							}
							else if ((iRoleId==3) && user_profile_domainObjs.getStr_firm_name() == null
									|| user_profile_domainObjs.getNum_scale_id() == 0
									|| user_profile_domainObjs.getNum_sector_id() == 0
									|| user_profile_domainObjs.getStr_firm_ceo_name()==null
									|| user_profile_domainObjs.getNum_address_proof_factory_document_id() == null
									|| user_profile_domainObjs.getNum__factory_district_id() == 0
									|| user_profile_domainObjs.getNum__firm_district_id() == 0
									|| user_profile_domainObjs.getNum_address_proof_firm_document_id().equals("0")
									|| user_profile_domainObjs.getNum_address_proof_factory_document_id().equals("0")
									|| user_profile_domainObjs.getStr_firm_address_document_name() == null
									|| user_profile_domainObjs.getStr_factory_address_document_name() == null
									|| user_profile_domainObjs.getNum_women_enterprenaur()==null
									|| user_profile_domainObjs.getNum_startup()==null)	{
								System.out.println("-----"+user_profile_domainObjs.getStr_firm_ceo_name());
								//mav.setViewName("redirect:/editUserProfilePage");
								mav.setViewName("redirect:/PCOrganizationProfile");
							}
							else{
								
								//Checking for GST No
								int iGstCheck = ups.checkIfGSTAdded(iUserId);
								mav.addObject("gst", iGstCheck);
								
								if(iGstCheck==0){
									//GST No Not added, redirect to /getGSTNumber
									mav.setViewName("redirect:/getGSTNumber?url=ApplicantPCSubmittedApps");
								}
								else{
									//Profile already submitted and GST already added, proceed as normal
									iRoleId = Integer.parseInt(userSession.getCurrent_role());
									List<HashMap> applicantapps = appserv.getApplications(iUserId, iRoleId);
									for(int i=0;i<applicantapps.size();i++){
										HashMap temp = applicantapps.get(i);
										String appId = temp.get("appId").toString();
										int branchId = Integer.parseInt(temp.get("branchId").toString());
										String tempflag = gudh.CheckFileUploadForDocId(appId, branchId,301, 1);
										System.out.println("tempflag: "+tempflag);
										int flag=-1;
										if(tempflag.equals("Not Exists")){
											flag=0;
										}else{
											flag=1;
										}
										temp.put("flag",flag);
										
									}
									String encappeal_doc=ims.Jcrypt("367");
									mav.addObject("appeal_doc",encappeal_doc);
									String EschemeId = ims.Jcrypt("1");		//Sending Encrypted Scheme ID For signed Copy Upload for PC
									String EDocId = ims.Jcrypt("301");	//Adding Encrypting DocID for signed Copy Upload
									for(int i=0;i<applicantapps.size();i++){
										HashMap templist1 = applicantapps.get(i);
										String app_id = templist1.get("appId").toString();
										for(int j=0;j<result.size();j++) {
											Map<String, Object> templist2 = result.get(j);
											String rapp_id = templist2.get("app_id").toString();		
											if(app_id.equalsIgnoreCase(rapp_id)) {
												templist1.put("do_check", Integer.parseInt(templist2.get("do_check").toString()));
												templist1.put("applicant_check", Integer.parseInt(templist2.get("applicant_check").toString()));
												templist1.put("remark_by_do", templist2.get("remark_by_do").toString());
												templist1.put("encnum_id", ims.Jcrypt(templist2.get("num_id").toString()));
											}
										}										
									}
									System.out.println("applicantapps: "+applicantapps.toString());
									mav.addObject("schemeId",EschemeId);
									mav.addObject("stCSRFToken",stCSRFToken);
									mav.addObject("docId",EDocId);
									mav.setViewName("applicantPCDashboardSubmittedApps");
									mav.addObject("controllerName","ApplicantPCSubmittedApps");
									mav.addObject("Applications", applicantapps);
								}
								
							}
						}
						
					}
					else if(userSession==null){
						mav.setViewName("ErrorPage");
					}
			}
		}
		catch(NullPointerException e){
			e.printStackTrace();
			mav.setViewName("homepage");
		}
		return mav;
	}
	
	@RequestMapping(value="/getGSTNumber",method=RequestMethod.GET)
	public ModelAndView getGSTNumber(@ModelAttribute("RcvdApplicationDO")RcvdApplicationDOModel RcvdAppModel,HttpServletRequest request_p){
		ModelAndView mav = new ModelAndView();
		 
		try{
			HttpSession httpsession = request_p.getSession(false);
			if(httpsession == null){
				mav.setViewName("ErrorPage");
			}else{
				Session userSession=null;
				userSession = (Session) httpsession.getAttribute("logged-in");
				if(userSession!=null){
					String stUrl = "ApplicantPC";
					if(request_p.getParameter("url")!=null){
						stUrl = request_p.getParameter("url").trim();
						//Generating Escape Sequence
						String stEscapeSequence = iuv.generateCSRFToken("gstapplicantescapesequence", request_p);
						mav.addObject("escape",stEscapeSequence);
					}
					mav.addObject("url",stUrl);
					mav.setViewName("getGSTNumberPage");
				}
					
			}
		}
		catch(NullPointerException e){
			e.printStackTrace();
			mav.setViewName("homepage");
		}
		return mav;
	}

	
	//author:siddharth , getting application of which license have been granted
	@RequestMapping(value="/applicantGrantedLicense",method=RequestMethod.GET)
	public ModelAndView applicantGrantedLicense(HttpServletRequest request_p){
		ModelAndView mav = new ModelAndView();
		try{
			HttpSession httpsession = request_p.getSession(false);
			if(httpsession == null){
				mav.setViewName("ErrorPage");
			}else{
				Session userSession=null;
				userSession = (Session) httpsession.getAttribute("logged-in");
					if(userSession!=null){
						int iUserId = userSession.getUserid();
						user_profile_domain user_profile_domainObjs = ups.getUserDtls(iUserId);
						//Check for escape Sequence
						if(request_p.getParameter("escape")!=null){
							String stEscapeSeq = request_p.getParameter("escape").trim();
							boolean stResponse = iuv.authenticateCSRFToken("gstapplicantescapesequence", stEscapeSeq, request_p);
							if(stResponse){
								int iRoleId = Integer.parseInt(userSession.getCurrent_role());
								List<HashMap> applicantGrantedLicense = appserv.getLicenseApps(iUserId,iRoleId);

								mav.setViewName("applicantLicenseApps");
								mav.addObject("applicantGrantedLicense", applicantGrantedLicense);	
								mav.addObject("controllerName","applicantGrantedLicense");
							}
							else{
								mav.setViewName("redirect:/ApplicantPC");
							}
						}
						else{
							//Check if User Profile is submitted
							int iProfileCompletionStatus = ups.getProfileCompletionStatus(iUserId);
							if(iProfileCompletionStatus==0){
								//Redirect to User Profile
//								mav.addObject("redirect", "1");
								mav.setViewName("redirect:/PCOrganizationProfile");
							}
							else if (user_profile_domainObjs.getStr_firm_name() == null
									|| user_profile_domainObjs.getNum_scale_id() == 0
									|| user_profile_domainObjs.getNum_sector_id() == 0
									|| user_profile_domainObjs.getStr_firm_ceo_name()==null
									|| user_profile_domainObjs.getNum_address_proof_factory_document_id() == null
									|| user_profile_domainObjs.getNum__factory_district_id() == 0
									|| user_profile_domainObjs.getNum__firm_district_id() == 0
									|| user_profile_domainObjs.getNum_address_proof_firm_document_id().equals("0")
									|| user_profile_domainObjs.getNum_address_proof_factory_document_id().equals("0")
									|| user_profile_domainObjs.getStr_firm_address_document_name() == null
									|| user_profile_domainObjs.getStr_factory_address_document_name() == null
									|| user_profile_domainObjs.getNum_women_enterprenaur()==null
									|| user_profile_domainObjs.getNum_startup()==null)	{
								System.out.println("-----"+user_profile_domainObjs.getStr_firm_ceo_name());
								//mav.setViewName("redirect:/editUserProfilePage");
//								mav.setViewName("redirect:/editUserProfilePage?editFlagAlert=1");
								mav.setViewName("redirect:/PCOrganizationProfile");
							}
							else{
								
								//Checking for GST No
								int iGstCheck = ups.checkIfGSTAdded(iUserId);
								mav.addObject("gst", iGstCheck);
								
								if(iGstCheck==0){
									//GST No Not added, redirect to /getGSTNumber
									mav.setViewName("redirect:/getGSTNumber?url=applicantGrantedLicense");
								}
								else{
									int iRoleId = Integer.parseInt(userSession.getCurrent_role());
									List<HashMap> applicantGrantedLicense = appserv.getLicenseApps(iUserId,iRoleId);

									mav.setViewName("applicantLicenseApps");
									mav.addObject("applicantGrantedLicense", applicantGrantedLicense);	
									mav.addObject("controllerName","applicantGrantedLicense");
								}
							}
						}

					}
					else if(userSession==null){
						mav.setViewName("ErrorPage");
					}
			}
		}
		catch(NullPointerException e){
			e.printStackTrace();
			mav.setViewName("ErrorPage");
		}
		return mav;
	}
	
	//@RequestMapping(value="/ApplicantPCSaveAsDraft",method=RequestMethod.GET)
	public ModelAndView ApplicationSavedList(HttpServletRequest request_p){
		ModelAndView mav = new ModelAndView();
		try{
			HttpSession httpsession = request_p.getSession(false);
			
			if(httpsession == null){
				mav.setViewName("ErrorPage");
			}else{
				Session userSession=null;
				userSession = (Session) httpsession.getAttribute("logged-in");
					if(userSession!=null){
						int iUserId = userSession.getUserid();
						user_profile_domain user_profile_domainObjs = ups.getUserDtls(iUserId);
						//Check for escape Sequence
						if(request_p.getParameter("escape")!=null){
							String stEscapeSeq = request_p.getParameter("escape").trim();
							boolean stResponse = iuv.authenticateCSRFToken("gstapplicantescapesequence", stEscapeSeq, request_p);
							if(stResponse){
								int iRoleId = Integer.parseInt(userSession.getCurrent_role());
								List<HashMap<String,String>> applicantapps = appserv.getSavedApplications(iUserId);
								mav.setViewName("applicantPCDashboardSavedApps");
								mav.addObject("Applications", applicantapps);
							}
							else{
								mav.setViewName("redirect:/ApplicantPC");
							}
						}
						else{
							//Check if User Profile is submitted
							int iProfileCompletionStatus = ups.getProfileCompletionStatus(iUserId);
							if(iProfileCompletionStatus==0){
								//Redirect to User Profile
								mav.addObject("redirect", "1");
								mav.setViewName("redirect:/user_profile");
							}
							
							else if (user_profile_domainObjs.getStr_firm_name() == null
									|| user_profile_domainObjs.getNum_scale_id() == 0
									|| user_profile_domainObjs.getNum_sector_id() == 0
									|| user_profile_domainObjs.getStr_firm_ceo_name()==null
									|| user_profile_domainObjs.getNum_address_proof_factory_document_id() == null
									|| user_profile_domainObjs.getNum__factory_district_id() == 0
									|| user_profile_domainObjs.getNum__firm_district_id() == 0
									|| user_profile_domainObjs.getNum_address_proof_firm_document_id().equals("0")
									|| user_profile_domainObjs.getNum_address_proof_factory_document_id().equals("0")
									|| user_profile_domainObjs.getStr_firm_address_document_name() == null
									|| user_profile_domainObjs.getStr_factory_address_document_name() == null
									|| user_profile_domainObjs.getNum_women_enterprenaur()==null
									|| user_profile_domainObjs.getNum_startup()==null)	{
								System.out.println("-----"+user_profile_domainObjs.getStr_firm_ceo_name());
								//mav.setViewName("redirect:/editUserProfilePage");
								mav.setViewName("redirect:/editUserProfilePage?editFlagAlert=1");
							} else{
								int iGstCheck = ups.checkIfGSTAdded(iUserId);
								mav.addObject("gst", iGstCheck);
								
								if(iGstCheck==0){
									//GST No Not added, redirect to /getGSTNumber
									mav.setViewName("redirect:/getGSTNumber?url=ApplicantPCSaveAsDraft");
								}
								else{
									int iRoleId = Integer.parseInt(userSession.getCurrent_role());
									List<HashMap<String,String>> applicantapps = appserv.getSavedApplications(iUserId);
									mav.setViewName("applicantPCDashboardSavedApps");
									mav.addObject("Applications", applicantapps);		
								}
								
							}
						}

					}
					else if(userSession==null){
						mav.setViewName("ErrorPage");
					}
			}
		}
		catch(NullPointerException e){
			e.printStackTrace();
			mav.setViewName("ErrorPage");
		}
		return mav;
	}

	//author:siddharth , getting details for changing name
		@RequestMapping(value="/changeName",method=RequestMethod.GET)
		public ModelAndView changeName(@ModelAttribute("changeName")applicantChngNameModel Model,HttpServletRequest request_p){
			ModelAndView mav = new ModelAndView();
			try{
				HttpSession httpsession = request_p.getSession(false);
				if(httpsession == null){
					mav.setViewName("ErrorPage");
				}else{
					Session userSession=null;
					userSession = (Session) httpsession.getAttribute("logged-in");
						if(userSession!=null){
							int UserId = userSession.getUserid();
							int iRoleId = Integer.parseInt(userSession.getCurrent_role());
							user_profile_domain user_profile_domainObjs = appSubService.getUserDtls(UserId);
							List<District_Mst_Domain> obj = appSubDao.getBranchIdFrmAppid(user_profile_domainObjs.getNum__factory_district_id());
							int BranchID = obj.get(0).getNumBranchId();
							
							
							int id =0;
							id = appserv.getCountNameChng(UserId);
							String firmDist = appSubService.getFirmDist(user_profile_domainObjs.getNum__firm_district_id());
							String firmState = appSubService.getFirmState(user_profile_domainObjs.getNum_firm_state_id());
							String firmCountry = appSubService.getFirmCountry(user_profile_domainObjs.getNum_firm_country_id());
							
							mav.setViewName("applicantChangeName");
							mav.addObject("firmDist",firmDist);
							mav.addObject("branchId",BranchID);
							mav.addObject("firmState",firmState);
							mav.addObject("firmCountry",firmCountry);
							mav.addObject("UserId",UserId);
							mav.addObject("id",id);
							
							mav.addObject("userProfile", user_profile_domainObjs);	
							mav.addObject("controllerName","applicantGrantedLicense");
							//mav.addObject("brandlist",brandlist);
						}
						else if(userSession==null){
							mav.setViewName("ErrorPage");
						}
				}
			}
			catch(NullPointerException e){
				e.printStackTrace();
				mav.setViewName("ErrorPage");
			}
			return mav;
		}
		
		
//author:siddharth , SAVING details for changing name
@RequestMapping(value="/changeName",method=RequestMethod.POST)
public ModelAndView changeNamePost(@ModelAttribute("changeName")applicantChngNameModel	Model,HttpServletRequest request_p){
	ModelAndView mav = new ModelAndView();
	try{
		HttpSession httpsession = request_p.getSession(false);
		if(httpsession == null){
			mav.setViewName("ErrorPage");
		}else{
			Session userSession=null;
			userSession = (Session) httpsession.getAttribute("logged-in");
				if(userSession!=null){
					int UserId = userSession.getUserid();
					int RoleId = Integer.parseInt(userSession.getCurrent_role());
					appserv.requestChngName(Model, UserId,RoleId);
					mav.setViewName("redirect:/changeName");
					//mav.addObject("brandlist",brandlist);
				}
				else if(userSession==null){
					mav.setViewName("ErrorPage");
				}
		}
	}
	catch(NullPointerException e){
		e.printStackTrace();
		mav.setViewName("ErrorPage");
	}
	return mav;
}
	
	
	@RequestMapping(value="/getLetterView",method=RequestMethod.GET)
	public ModelAndView getLetterView(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		try{
			HttpSession httpsession = request.getSession(false);
			if(httpsession == null){
				mav.setViewName("ErrorPage");
			}else{
				Session userSession=null;
				userSession = (Session) httpsession.getAttribute("logged-in");
					if(userSession!=null){
						int iUserId = userSession.getUserid();
						int iRoleId = Integer.parseInt(userSession.getCurrent_role());
						String appId="";
						if(request.getParameter("appId")!=null)
						{
						appId = request.getParameter("appId");
						}
						String branchId="";
						if(request.getParameter("branchId")!=null)
						{
						branchId = request.getParameter("branchId");
						}
						String letterId="";
						if(request.getParameter("letterId")!=null)
						{
							letterId = request.getParameter("letterId");
						}
						
						TextEditor_Dom domainObj = appserv.getLetterView(appId,branchId,letterId);
						
						int a=0;
						int b=a;
						mav.setViewName("letterView");	
						mav.addObject("letterText", domainObj.getStrHtmlTxt());
					}
					else if(userSession==null){
						mav.setViewName("ErrorPage");
					}
			}
		}
		catch(NullPointerException e){
			e.printStackTrace();
			mav.setViewName("ErrorPage");
		}
		return mav;
	}
	
	
	
	@RequestMapping(value="/getLetterDetails",method=RequestMethod.GET)
	public ModelAndView getLetterDetails(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		try{
			HttpSession httpsession = request.getSession(false);
			if(httpsession == null){
				mav.setViewName("ErrorPage");
			}else{
				Session userSession=null;
				userSession = (Session) httpsession.getAttribute("logged-in");
					if(userSession!=null){
						int iUserId = userSession.getUserid();
						int iRoleId = Integer.parseInt(userSession.getCurrent_role());
						String appId="";
						if(request.getParameter("appId")!=null)
						{
						appId = request.getParameter("appId");
						}
						String branchId="";
						if(request.getParameter("branchId")!=null)
						{
						branchId = request.getParameter("branchId");
						}
						String letterId="";
						if(request.getParameter("letterId")!=null)
						{
							letterId = request.getParameter("letterId");
						}
						
						List<HashMap> applicantLetters = appserv.getLetterDetails(appId,branchId);
						
						mav.setViewName("letterDetails");	
						mav.addObject("applicantLetters", applicantLetters);
					}
					else if(userSession==null){
						mav.setViewName("ErrorPage");
					}
			}
		}
		catch(NullPointerException e){
			e.printStackTrace();
			mav.setViewName("ErrorPage");
		}
		return mav;
	}
			
	@RequestMapping(value="/ApplicantPC",method=RequestMethod.GET)
	public ModelAndView ApplicantDashboard(@ModelAttribute("dashApp")ApplicantModel appModel,HttpServletRequest request_p){
		ModelAndView mav = new ModelAndView();

		try{
			HttpSession httpsession = request_p.getSession(false);
			if(httpsession == null){
				mav.setViewName("sessionExpire");
			}else{
				Session userSession=null;
				userSession = (Session) httpsession.getAttribute("logged-in");
					if(userSession!=null){
						int iUserId = userSession.getUserid();
						String stName = "";
						if(userSession.getFname().length()>1 && userSession.getLname().length()>1){
							stName = userSession.getFname().substring(0, 1).toUpperCase()+userSession.getFname().substring(1)+" "+userSession.getLname().substring(0, 1).toUpperCase()+userSession.getLname().substring(1);
						}
						else{
							stName = userSession.getFname()+" "+userSession.getLname(); 
						}
						String stEmail_p = userSession.getUsername();
						HashMap hm = appserv.getApplicantDashboardData(iUserId,stEmail_p);
						int countPendingFee=appserv.countPendingFee(iUserId);
						String pc_usermanual = ResourceBundleFile.getValueFromKey("PC_UserManual");
						
						
						//Check if User Profile is submitted
						int iProfileCompletionStatus = ups.getProfileCompletionStatus(iUserId);
						if(iProfileCompletionStatus==0){
							//Redirect to User Profile
							mav.addObject("profile", "0");
						}else{
							int iGstCheck = ups.checkIfGSTAdded(iUserId);
							
							if(iGstCheck==0){
								//GST No Not added, redirect to /getGSTNumber
								mav.addObject("profile", "1");
							}
							else{
								mav.addObject("profile", "2");	
							}
							
						}
						
						mav.setViewName("appdash");
						mav.addObject("pc_usermanual", pc_usermanual);	
						mav.addObject("appcounts", hm);				
						mav.addObject("countPendingFee", countPendingFee);
						mav.addObject("Name", stName);
					}
					else if(userSession==null){
						mav.setViewName("sessionExpire");
					}
			}
		}
		catch(NullPointerException e){
			e.printStackTrace();
			mav.setViewName("ErrorPage");
		}
		return mav;
		
	}
	
	@ExceptionHandler(Exception.class)
	public String exception(HttpServletRequest request,Exception e) {
		
		e.printStackTrace();
		return "ErrorPage";
			
	}
	
	//###########################################################################//
	//###########################################################################//
	// 					Self SOM Code By Dhruv									 //
	//###########################################################################//
	//###########################################################################//	
	
	@RequestMapping(value="/SelfSuspension",method=RequestMethod.GET)
	public ModelAndView selfSuspensionShowForm(@ModelAttribute("Suspension")SelfSuspensionModel susModel,HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		try{
			HttpSession httpsession = request.getSession(false);
			if(httpsession == null){
				mav.setViewName("ErrorPage");
			}else{
				Session userSession=null;
				userSession = (Session) httpsession.getAttribute("logged-in");
					if(userSession!=null){
						if(request.getParameter("LicenceNo")!= null && request.getParameter("Bid")!= null){
							String stLicenceNo = request.getParameter("LicenceNo").trim();
							String stBranchId = request.getParameter("Bid").trim();
							mav.addObject("Licence",stLicenceNo);
							mav.addObject("Branch",stBranchId);
							stLicenceNo = ims.Dcrypt(stLicenceNo);
							stBranchId = ims.Dcrypt(stBranchId);
							
							int iApplicationMaxStatus = gd.getMaxApplicationStatusId(stLicenceNo, Integer.parseInt(stBranchId));
							if(iApplicationMaxStatus==201){
								String stCSRFToken = iuv.generateCSRFToken("SelfSuspension", request);
								mav.addObject("csrf", stCSRFToken);
								HashMap<String,String> hm = appserv.getSelfSOMCMLDetails(stLicenceNo, Integer.parseInt(stBranchId));
								mav.addObject("details", hm);
								mav.addObject("licence_no", stLicenceNo);
								mav.setViewName("SelfSuspension");
							}
							else{
								String stSuccessMsg = "<p>Your Licence needs to be in operative state for this process</p>";
								mav.addObject("forgot_head", "Restricted");
								mav.addObject("forgot_response",stSuccessMsg);
								mav.setViewName("recoversuccess");
							}
						}
						else{
							mav.setViewName("applicantLicenseApps");
						}		
					}
					else if(userSession==null){
						mav.setViewName("ErrorPage");
					}
			}
		}
		catch(NullPointerException e){
			e.printStackTrace();
			mav.setViewName("ErrorPage");
		}
		
		return mav;
	}
	
	@RequestMapping(value="/SelfSuspension",method=RequestMethod.POST)
	public ModelAndView selfSuspensionSubmitForm(@ModelAttribute("Suspension")SelfSuspensionModel susModel,HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		try{
			HttpSession httpsession = request.getSession(false);
			if(httpsession == null){
				mav.setViewName("ErrorPage");
			}else{
				Session userSession=null;
				userSession = (Session) httpsession.getAttribute("logged-in");
					if(userSession!=null){
						if(iuv.authenticateCSRFToken("SelfSuspension", susModel.getStCSRF(), request)){
							int iUserId = userSession.getUserid();
							int iRoleId = Integer.parseInt(userSession.getCurrent_role());
							
							HashMap<String,String> hm = new HashMap<String,String>();
							hm.put("cml",ims.Dcrypt(susModel.getStLicenceNo()));
							hm.put("branchId",ims.Dcrypt(susModel.getiBranchId()+""));
							hm.put("iUserId",iUserId+"");
							hm.put("iRoleId",iRoleId+"");
							hm.put("visible","1");
							hm.put("remarks",susModel.getReasonforsuspension());
							hm.put("next_status","122");
							hm.put("identifier","5");

							pcpserv.PCAddCMLTrackingStatus(hm);
							
							
							String stSuccessMsg = "<p>Your request has been logged successfully</p>";
							mav.addObject("forgot_head", "Success");
							mav.addObject("loginCheck", "1");
							mav.addObject("forgot_response",stSuccessMsg);
							mav.setViewName("recoversuccess");
						}
						else{
							mav.setViewName("redirect:/csrfErrorPage");
						}

					}
					else if(userSession==null){
						mav.setViewName("ErrorPage");
					}
			}
		}
		catch(NullPointerException e){
			e.printStackTrace();
			mav.setViewName("ErrorPage");
		}
		
		return mav;
	}
	
	//###########################################################################//
	//###########################################################################//
	// 					Self SOM Code By Dhruv									 //
	//###########################################################################//
	//###########################################################################//	

	//Author: Tanmay; POST:Function to upload brand documents 
	@RequestMapping(value="/uploadMlabel",method=RequestMethod.POST)
	public ModelAndView uploadMlabel(@ModelAttribute("uploadTR")ApplicantModel appModel,HttpServletRequest request,MultipartHttpServletRequest requestF)
	{
		String Message="";
		String MessageErr="";
		ModelAndView mv = new ModelAndView();
		HttpSession httpsession = request.getSession(false);
		Session userSession;
		String cmlNo = "";
		userSession = (Session) httpsession.getAttribute("logged-in");
		if (userSession != null) 
		{// 
			int userid=userSession.getUserid();
			int roleId=Integer.parseInt(userSession.getCurrent_role());

				Iterator<String> itr =  requestF.getFileNames();
				
				MultipartFile mpf = null;
				String fileNm="";
				FileMeta proposalFileMeta = new FileMeta();	
				int i=0;
				while(itr.hasNext())
				{
					 fileNm = appModel.getStrArrbrandNm()[i];
					 fileNm=fileNm.trim();
					 
					 fileNm=fileNm.replaceAll(" ","_");
					 mpf = requestF.getFile(itr.next());
					  
					 proposalFileMeta.setFileName(mpf.getOriginalFilename());
					 proposalFileMeta.setFileSize(mpf.getSize()/1024+" Kb");			
					 proposalFileMeta.setFileType(mpf.getContentType());
				 
			    if(proposalFileMeta.getFileType().equalsIgnoreCase("application/pdf"))			 
			    {
					if(Integer.parseInt(proposalFileMeta.getFileSize().split(" ")[0]) < 10*1024)
						{			
							FileOutputStream outputStream = null;
							String path = globalPath+cmlNo+File.separator;
							String finalName=path+fileNm+".pdf";
							// 
							// 
							File file = new File(finalName);
							 i++;
							 if(file.exists()){
								  file.delete();
							  }else{
								   
							  }
							 try {
								File tmp = new File(finalName);
								tmp.getParentFile().mkdirs();
								if (!tmp.exists()) {
										tmp.createNewFile();					
								}					
								outputStream = new FileOutputStream(tmp);
								outputStream.write(mpf.getBytes());
								appserv.setBrandDoc(cmlNo,fileNm);
								Message = "success";
								outputStream.close();
								}
								catch (IOException e) {
									e.printStackTrace();
								}		
							}
							else{
										
								MessageErr = "size_exceed";					
							}
					 }
					 else{
	
						 MessageErr = "not_pdf";
						}
							   
		}
				appserv.setBrandDocTrack(cmlNo,appModel);
		}
		mv.addObject("Message", Message);
		mv.addObject("MessageErr", MessageErr);
		mv.setViewName("uploadMarkingLbl");
		//mv.addObject("cmlNo", cmlNo);
		//mv.addObject("user_File_Name", user_File_Name);
		return mv;
	}
			
			
	//Author: Tanmay; GET:Function to upload brand documents 		
	@RequestMapping(value="/uploadMlabel",method=RequestMethod.GET)
	public ModelAndView getuploadTestReports(@ModelAttribute("uploadTR")ApplicantModel appModel,HttpServletRequest request){
		
		ModelAndView mv = new ModelAndView();
		HttpSession httpsession = request.getSession(false);
		Session userSession;
		userSession = (Session) httpsession.getAttribute("logged-in");
		int userid = -1;
		String cmlNo = "";
		String user_File_Name = "";
		int lab_id=-1;
		int trId=-1;
		int forwardId=-1;
		int branchid=81;
		if (userSession != null) 
		{
			userid=userSession.getUserid();
			if(request.getParameter("docID")!=null)
			{
				cmlNo=request.getParameter("docID");
				//user_File_Name=cmlNo+"_m_label";
			}
			
			List<Licence_Brand_Detail_Domain> brandlist = appserv.getBrandlist(cmlNo, branchid);
			
			for(int count=1;count<=brandlist.size();count++)
			{
				user_File_Name=cmlNo+brandlist.get(0).getStr_brand_name()+"-";
			}
		
		
		mv.setViewName("uploadMarkingLbl");
		mv.addObject("cmlNo", cmlNo);
		mv.addObject("user_File_Name", user_File_Name);
		mv.addObject("brandlist",brandlist);
		mv.addObject("branchid",branchid);
		mv.addObject("userid",userid);
		
		
		
		
		}
		return mv;
	}
	
	
	//###########################################################################//
	//###########################################################################//
	// New Marking Label Upload Code By Dhruv									 //
	//###########################################################################//
	//###########################################################################//	
	@RequestMapping(value="/getBrandDataForLicense",method=RequestMethod.POST)
	public @ResponseBody List<HashMap<String,String>> getBrandDataForLicense(HttpServletRequest request_p){
		List<HashMap<String,String>> lhm = new ArrayList<HashMap<String,String>>();
		try{
			HttpSession httpsession = request_p.getSession(false);
			if(httpsession != null){
				Session userSession=null;
				userSession = (Session) httpsession.getAttribute("logged-in");
				if(userSession!=null){
					String stCmlNo = "";
					int iBranchId = 0;
					int iErrors = 0;
					if(request_p.getParameter("cml")!=null){
						stCmlNo = ims.Dcrypt(request_p.getParameter("cml").trim());
					}
					else{
						iErrors = 1;
					}
					if(request_p.getParameter("branch")!=null){
						iBranchId = Integer.parseInt(ims.Dcrypt(request_p.getParameter("branch").trim()));
					}
					else{
						iErrors = 1;
					}
					if(iErrors==0){
						lhm = ad.getBrandData(stCmlNo, iBranchId);
					}
				}
			}
		}
		catch(NullPointerException e){
			e.printStackTrace();
		}
		return lhm;
		
	}
	//###########################################################################//
	//###########################################################################//
	// New Marking Label Upload Code By Dhruv									 //
	//###########################################################################//
	//###########################################################################//	
	
	/*@RequestMapping(value="/ApplicantHM",method=RequestMethod.GET)
	public ModelAndView ApplicantHMDashboard(@ModelAttribute("dashApp")ApplicantModel appModel,HttpServletRequest request_p){
		ModelAndView mav = new ModelAndView();
		int scheme_id=0;
		try{
			HttpSession httpsession = request_p.getSession(false);
			if(httpsession == null){
				mav.setViewName("sessionExpire");
			}else{
				Session userSession=null;
				userSession = (Session) httpsession.getAttribute("logged-in");
					if(userSession!=null){
						String stCSRFToken = iuv.generateCSRFToken("ApplicantHM", request_p);
						mav.addObject("csrftoken", stCSRFToken);
						int iUserId = userSession.getUserid();
						String stEmail_p = userSession.getUsername();
						HashMap hm = appserv.getApplicantHallDashboardData(iUserId,stEmail_p);
				//		int countPendingFee=appserv.countPendingFee(iUserId);
						//mav.setViewName("appdash");
						mav.setViewName("appdashhm");
						mav.addObject("appcounts", hm);				
					//	mav.addObject("countPendingFee", countPendingFee);
						mav.addObject("schemeId", "2");
					}
					else if(userSession==null){
						mav.setViewName("sessionExpire");
					}
			}
		}
		catch(NullPointerException e){
			e.printStackTrace();
			mav.setViewName("ErrorPage");
		}
		return mav;
		
	}
	
	
	@RequestMapping(value="/ApplicantHMSaveAsDraft",method=RequestMethod.GET)
	public ModelAndView ApplicationSavedListHM(@RequestParam("hmType") String hmType,@ModelAttribute("dashDraftApp")ApplicantModel appModel,HttpServletRequest request_p){
		ModelAndView mav = new ModelAndView();
		try{
			HttpSession httpsession = request_p.getSession(false);
			Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request_p);
			String message="";
			if(httpsession == null){
				mav.setViewName("ErrorPage");
			}else{
				Session userSession=null;
				userSession = (Session) httpsession.getAttribute("logged-in");
					if(userSession!=null){
						String stCSRFToken = iuv.generateCSRFToken("ApplicantHMSaveAsDraft", request_p);
						int iUserId = userSession.getUserid();
						mav.addObject("csrftoken", stCSRFToken);
						int iRoleId = Integer.parseInt(userSession.getCurrent_role());
						if(hmType==null){
							hmType="";
							mav.setViewName("ErrorPage");
						}
						else{
							
						
						if (inputFlashMap != null){
							  message=inputFlashMap.get("saveStatus").toString();
						}
						// 
						List<HashMap<String,String>> applicantapps = appserv.getSavedHMApplications(iUserId,hmType);
						mav.setViewName("applicantHMDashboardSavedApps");
						mav.addObject("Applications", applicantapps);				
						mav.addObject("hmType", hmType);
						mav.addObject("Message",message);
						}
					}
					else if(userSession==null){
						mav.setViewName("ErrorPage");
					}
			}
		}
		catch(NullPointerException e){
			e.printStackTrace();
			mav.setViewName("ErrorPage");
		}
		return mav;
	}
	
	
	@RequestMapping(value="/ApplicantHMSubmittedApps",method=RequestMethod.GET)
	public ModelAndView ApplicationRcvdHMList(HttpServletRequest request_p){
		ModelAndView mav = new ModelAndView();
		try{
			String hm_type="";
			if(request_p.getParameter("hmType")!=null)
				hm_type=request_p.getParameter("hmType").toString();
			
			HttpSession httpsession = request_p.getSession(false);
			if(httpsession == null){
				mav.setViewName("ErrorPage");
			}else{
				Session userSession=null;
				userSession = (Session) httpsession.getAttribute("logged-in");
					if(userSession!=null){
						int iUserId = userSession.getUserid();
						//int iUserId = 66;
						int iRoleId = Integer.parseInt(userSession.getCurrent_role());
						//int iRoleId = 3;
						List<HashMap> applicantapps = appserv.getHallHMSubmittedApplications(iUserId, iRoleId,hm_type);
						mav.setViewName("applicantHMDashboardSubmittedApps");
						mav.addObject("controllerName","SubmittedApps?hmType="+hm_type+"");
						mav.addObject("Applications", applicantapps);
						mav.addObject("hm_type", hm_type);
					}
					else if(userSession==null){
						mav.setViewName("ErrorPage");
					}
			}
		}
		catch(NullPointerException e){
			e.printStackTrace();
			mav.setViewName("homepage");
		}
		return mav;
	}

	//author:tanmay , getting application of which license have been granted
			@RequestMapping(value="/applicantHMGrantedLicense",method=RequestMethod.GET)
			public ModelAndView applicantHMGrantedLicense(HttpServletRequest request_p){
				ModelAndView mav = new ModelAndView();
				try{
					HttpSession httpsession = request_p.getSession(false);
					if(httpsession == null){
						mav.setViewName("ErrorPage");
					}else{
						Session userSession=null;
						userSession = (Session) httpsession.getAttribute("logged-in");
							if(userSession!=null){
								int Inclusion_statusno=0;
								int iUserId = userSession.getUserid();
								int iRoleId = Integer.parseInt(userSession.getCurrent_role());
								List<HashMap> applicantHMGrantedLicense = appserv.getHMLicenseApps(iUserId,iRoleId);
								//List<HMJewApplicationTrackingDomain> inclusion=appserv.getLatestTrack(Inclusion_statusno);
								mav.setViewName("applicantHMLicenseApps");
								mav.addObject("applicantGrantedLicense", applicantHMGrantedLicense);	
								mav.addObject("controllerName","applicantHMGrantedLicense");
								
								
								//mav.addObject("brandlist",brandlist);
							}
							else if(userSession==null){
								mav.setViewName("ErrorPage");
							}
					}
				}
				catch(NullPointerException e){
					e.printStackTrace();
					mav.setViewName("ErrorPage");
				}
				return mav;
			}
			
			//author :imran,getting application for which licensee be granted AHC recognition
		
			@RequestMapping(value="/applicantHMRECGrantedLicense",method=RequestMethod.GET)
			public ModelAndView applicantHMRECGrantedLicense(HttpServletRequest request_p){
				ModelAndView mav = new ModelAndView();
				try{
					HttpSession httpsession = request_p.getSession(false);
					if(httpsession == null){
						mav.setViewName("ErrorPage");
					}else{
						Session userSession=null;
						userSession = (Session) httpsession.getAttribute("logged-in");
							if(userSession!=null){
								int Inclusion_statusno=0;
								int iUserId = userSession.getUserid();
								int iRoleId = Integer.parseInt(userSession.getCurrent_role());
								List<HashMap> applicantHMRECGrantedLicense = appserv.getHMRECLicenseApps(iUserId,iRoleId);
								//List<HMJewApplicationTrackingDomain> inclusion=appserv.getLatestTrack(Inclusion_statusno);
								
								mav.addObject("applicantGrantedLicenseREC", applicantHMRECGrantedLicense);	
								mav.addObject("controllerName","applicantHMRECGrantedLicense");
								mav.setViewName("applicantHMRECLicenseApps");
								
								//mav.addObject("brandlist",brandlist);
							}
							else if(userSession==null){
								mav.setViewName("ErrorPage");
							}
					}
				}
				catch(NullPointerException e){
					e.printStackTrace();
					mav.setViewName("ErrorPage");
				}
				return mav;
			}
			
			@RequestMapping(value="/showFailedTR",method=RequestMethod.GET)
			public ModelAndView showFailedTR(HttpServletRequest request_p){
				ModelAndView mav = new ModelAndView();
				try{
					HttpSession httpsession = request_p.getSession(false);
					if(httpsession == null){
						mav.setViewName("ErrorPage");
					}else{
						Session userSession=null;
						userSession = (Session) httpsession.getAttribute("logged-in");
							if(userSession!=null){
								int iUserId = userSession.getUserid();
								int iRoleId = Integer.parseInt(userSession.getCurrent_role());
								String licNum="";
								String controller="";
								
								if(request_p.getParameter("licNum")!=null)
								{
									licNum=request_p.getParameter("licNum");
								}
								if(request_p.getParameter("controller")!=null)
								{
									controller=request_p.getParameter("controller");
								}
								
								List<HashMap<String,String>> trFailedList = appserv.getTRFailedList(iUserId,iRoleId,licNum);
								
								
								mav.setViewName("trFailedList");
								mav.addObject("trFailedList", trFailedList);	
								mav.addObject("controllerName",controller);
								
								
								//mav.addObject("brandlist",brandlist);
							}
							else if(userSession==null){
								mav.setViewName("ErrorPage");
							}
					}
				}
				catch(NullPointerException e){
					e.printStackTrace();
					mav.setViewName("ErrorPage");
				}
				return mav;
			}
			
			

			@RequestMapping(value="/SingleToCorporateConvertion",method=RequestMethod.GET)
			public ModelAndView getSingleToCorporateConvertion(@ModelAttribute("JewHeadModel")JewHeadModel jewHeadModel,HttpServletRequest request){
				ModelAndView mv = new ModelAndView();
				List<HashMap> listhmApplications = new ArrayList<HashMap>();
				Session session = new Session();
				int numUserId = 0;
				String numRoleId = "";
								
					if(request.getSession() != null)
					{
						session = (Session)request.getSession().getAttribute("logged-in");
						if(session != null)
						{
							numUserId = session.getUserid();
							numRoleId = session.getCurrent_role();
							listhmApplications = d1.getSingleToCorporateConvertion(numUserId,Integer.parseInt(numRoleId));
								mv.setViewName("viewSingleToCorporateConvertion");
								mv.addObject("applicationList", listhmApplications);
								mv.addObject("controllerName","ListExistingJewApplications_existtab");
								mv.addObject("numUserId", numUserId);
								mv.addObject("numRoleId", numRoleId);
						}else {
							mv.setViewName("sessionExpire");
						}
						
					}else {
						mv.setViewName("sessionExpire");
					}
								
				return mv;

			}
			

			@RequestMapping(value="/SingleToCorporateConvertion1",method=RequestMethod.GET)
			public ModelAndView getSingleToCorporateConvertion1(@ModelAttribute("JewHeadModel")JewHeadModel jewHeadModel,HttpServletRequest request){
				ModelAndView mv = new ModelAndView();
				List<HashMap> listhmApplications = new ArrayList<HashMap>();
				Session session = new Session();
				int numUserId = 0;
				String numRoleId = "";
						String EappIdarray="";	
						String appIdarray[];
						String appids="";
					if(request.getSession() != null)
					{
						session = (Session)request.getSession().getAttribute("logged-in");
						if(session != null)
						{
							if(request.getParameter("apparray")!= null && !request.getParameter("apparray").isEmpty())
							{			
								EappIdarray =request.getParameter("apparray");
								appIdarray=EappIdarray.split(",");
								
								for(int i=0;i<appIdarray.length;i++){
									appids +="'"+ims.Dcrypt(appIdarray[i])+"',";
								}
							}
							
							
							numUserId = session.getUserid();
							numRoleId = session.getCurrent_role();
							
							List<CountryMaster> country= AppHallMarkService.getCountry();	
							List<State_Mst_Domain> state = AppHallMarkService.getState();
							List<District_Mst_Domain> district = AppHallMarkService.getDistrict();	
							List<Sector_Mst_Domain> sector=AppHallMarkService.getsectordetails();

							
							listhmApplications = d1.getSingleToCorporateConvertion1(numUserId,Integer.parseInt(numRoleId),appids);
								mv.setViewName("viewSingleToCorporateConvertion1");
								mv.addObject("applicationList", listhmApplications);
								mv.addObject("controllerName","ListExistingJewApplications_existtab");
								mv.addObject("numUserId", numUserId);
								mv.addObject("numRoleId", numRoleId);
								mv.addObject("EappIdarray", EappIdarray);
								mv.addObject("country", country);
								mv.addObject("sector", sector);
								mv.addObject("state", state);
								mv.addObject("district", district);
						}else {
							mv.setViewName("sessionExpire");
						}
						
					}else {
						mv.setViewName("sessionExpire");
					}
								
				return mv;

			}
			
		
			@RequestMapping(value="/MergeSingleToCorporate", method=RequestMethod.POST)
		    public String DeleteState(@Valid @ModelAttribute("JewHeadModel")JewHeadModel jewHeadModel, BindingResult result,HttpServletRequest request, RedirectAttributes redirectAttributes)
			{
				
				String EappIdstring="";
				String EappIdarray[];
				String appIdarray[];
				String ab="";
					HttpSession httpsession = request.getSession(false);
					Session userSession;
					if(request.getParameter("apparray")!= null && !request.getParameter("apparray").isEmpty())
					{			
						EappIdstring =request.getParameter("apparray");
						EappIdarray=EappIdstring.split(",");
						
						for(int i=0;i<EappIdarray.length;i++){
							ab +=	ims.Dcrypt(EappIdarray[i])+",";
						}
					}
					
					int branchId_corporate=0;
					String appId_corporate="";
					
					userSession = (Session) httpsession.getAttribute("logged-in");
					if(userSession!=null)
					{	
						
						if(jewHeadModel.getNumDistrictId()>0){
							branchId_corporate=AppHallMarkService.getBranchID(jewHeadModel.getNumDistrictId());
							jewHeadModel.setStrBranchId(String.valueOf(branchId_corporate));
							if(branchId_corporate>0)
							appId_corporate=AppIdGenerate(jewHeadModel);
							Application_HallMarking_Domain AppHallMarkDom = new Application_HallMarking_Domain();
							AppHallMarkDom.setStr_app_id(appId_corporate);
							AppHallMarkDom.setNum_branch_id(branchId_corporate);
							AppHallMarkService.addHallMarkApp(AppHallMarkDom);
							
						}	
						
						ab=ab.substring(0,ab.length()-1);
						appIdarray=ab.split(",");
						int iUserId = userSession.getUserid();
						appserv.setMergeSingleToCorporateDetails(jewHeadModel,appIdarray,iUserId,appId_corporate,branchId_corporate);

				} else {
					return "redirect:/sessionExpire";
				}
					
				return "redirect:/uploadDoc?appId="+ims.Jcrypt(appId_corporate)+"&actionId=5&branchId="+ims.Jcrypt(String.valueOf(branchId_corporate))+"&scheme=2&url=uploadDoc&con=SingleToCorporateConvertion1?apparray="+EappIdstring+"&next_con=Fee&procedure=0&scale=0&form_id=4";
			}	
			
			
			public String AppIdGenerate(JewHeadModel jewHeadModel){
				// Genrating application id
						String strAppNo = "";
						int factBranchID = Integer.parseInt(jewHeadModel.getStrBranchId());
						List<String> Appid = appHallMarkDao.getMaxApplicationID(factBranchID);
						String strAppId = "";
						String FinalApplicationId;
						
							if (Appid.size()>0) {							
								strAppNo = Appid.get(0).substring(6, Appid.get(0).length());
								Long lnAppNo = Long.parseLong(strAppNo);
								
									Long NewId = lnAppNo + 1;
									strAppId = factBranchID +"HMJW"+NewId.toString();
									FinalApplicationId = strAppId;							
							}else{
								String NewId = "1001";
								String NewBranchId = Integer.toString(factBranchID);
								String NewApplicationNo = NewBranchId +"HMJW"+ NewId;
								FinalApplicationId = NewApplicationNo;
							}
						return FinalApplicationId;							
						
				}*/
			
			
			@RequestMapping(value="/ApplicantMSCD",method=RequestMethod.GET)
			public ModelAndView ApplicantDashboardMSCD(@ModelAttribute("dashApp")ApplicantModel appModel,HttpServletRequest request_p, RedirectAttributes redirectAttributes){
				ModelAndView mav = new ModelAndView();

				try{
					HttpSession httpsession = request_p.getSession(false);
					if(httpsession == null){
						mav.setViewName("sessionExpire");
					}else{
						Session userSession=null;
						userSession = (Session) httpsession.getAttribute("logged-in");
							if(userSession!=null){
								int iUserId = userSession.getUserid();
								
								mav.setViewName("appdashmscd");
							}
							else if(userSession==null){
								mav.setViewName("sessionExpire");
							}
					}
				}
				catch(NullPointerException e){
					e.printStackTrace();
					mav.setViewName("ErrorPage");
				}
				return mav;
				
			}
			
			@RequestMapping(value="/getMSCDGSTNumber",method=RequestMethod.GET)
			public ModelAndView getMSCDGSTNumber(@ModelAttribute("dashApp")ApplicantModel appModel,HttpServletRequest request_p){
				ModelAndView mav = new ModelAndView();
				 
				try{
					HttpSession httpsession = request_p.getSession(false);
					if(httpsession == null){
						mav.setViewName("ErrorPage");
					}else{
						Session userSession=null;
						userSession = (Session) httpsession.getAttribute("logged-in");
						if(userSession!=null){
							int iUserId = userSession.getUserid();
							String qry = "select c from MSCD_GST_Applicant c where c.user_id='"+iUserId+"' and c.num_is_valid=1";	
							if(daohelper.findByQuery(qry).size()>0){
								mav.setViewName("OnlineApplication");
							}else{
								
							String stUrl = "ApplicantMSCD";
							if(request_p.getParameter("url")!=null){
								stUrl = request_p.getParameter("url").trim();
								//Generating Escape Sequence
								String stEscapeSequence = iuv.generateCSRFToken("gstapplicantescapesequence", request_p);
								mav.addObject("escape",stEscapeSequence);
							}
							mav.addObject("url",stUrl);
							mav.setViewName("getMSCDGSTNumberPage");
						}
						}
					}
				}
				catch(NullPointerException e){
					e.printStackTrace();
					mav.setViewName("homepage");
				}
				return mav;
			}

			@RequestMapping(value="/saveMSCDGSTNumber", method=RequestMethod.POST)
			public @ResponseBody String saveMSCDGSTNumber(HttpServletRequest request_p){
				String stResponse = "length";
				try{
					HttpSession httpsession = request_p.getSession(false);
					if(httpsession != null){
						Session userSession=null;
						userSession = (Session) httpsession.getAttribute("logged-in");
						
						if(userSession!=null){
							int iUserId = userSession.getUserid();
							String stGSTNo = "";
							if(request_p.getParameter("gstno")!=null){
								stGSTNo = request_p.getParameter("gstno").trim();
								if(stGSTNo.length()==15){
									stResponse = appserv.saveMSCDGSTNo(stGSTNo, iUserId);
									
								}
							}
							
							
						}
					}
				}
				catch(Exception e){
					e.printStackTrace();
				}
				
				return stResponse;
			}

			@RequestMapping(value="/submit_accptance",method=RequestMethod.POST)
			public ModelAndView submit_accptance(HttpServletRequest request_p){
				ModelAndView mav = new ModelAndView();
				try{
					HttpSession httpsession = request_p.getSession(false);
					if(httpsession == null){
						mav.setViewName("ErrorPage");
					}else{
						Session userSession=null;
						userSession = (Session) httpsession.getAttribute("logged-in");
							if(userSession!=null){
								String stCSRFToken = request_p.getParameter("CSRFToken");
								if (iuv.authenticateCSRFToken("ApplicantPCSubmittedApps", stCSRFToken, request_p)){
									int num_id = Integer.parseInt(ims.Dcrypt(request_p.getParameter("num_id")));
									int acptstidoc = Integer.parseInt(request_p.getParameter("acptstidoc"));
									int acptmarkingfee = Integer.parseInt(request_p.getParameter("acptmarkingfee"));
									String remark = request_p.getParameter("remark");
									boolean result = appserv.submit_accptance(num_id,acptstidoc,acptmarkingfee,remark);
									System.out.println("result: "+result);
								}
								else{
									mav.setViewName("csrfErrorPage");
									}
								System.out.println("stCSRFToken3: "+stCSRFToken);
								mav.setViewName("redirect:/ApplicantPCSubmittedApps");
							}
							else if(userSession==null){
								mav.setViewName("ErrorPage");
							}
					}
				}
				catch(NullPointerException e){
					e.printStackTrace();
					mav.setViewName("ErrorPage");
				}
				return mav;
			}
			@RequestMapping(value="/getchecksum", method=RequestMethod.POST)
			public @ResponseBody String getchecksum(HttpServletRequest request_p){
				String stResponse = "N/A";
				try{
					HttpSession httpsession = request_p.getSession(false);
					if(httpsession != null){
						Session userSession=null;
						userSession = (Session) httpsession.getAttribute("logged-in");
						
						if(userSession!=null){
							int iUserId = userSession.getUserid();
							String isno = "";
							if(request_p.getParameter("isno")!=null){
								isno = request_p.getParameter("isno").trim();
								List<Map<String, Object>> checksum = appserv.getchecksum(isno);
								stResponse=checksum.get(0).get("str_doc_chksum_name").toString()+":"+checksum.get(0).get("str_sti_doc").toString();
							}
							
							
						}
					}
				}
				catch(Exception e){
					e.printStackTrace();
				}
				
				return stResponse;
			}
//			@RequestMapping(value="/check_signed_copy",method=RequestMethod.POST)
//			public @ResponseBody String check_signed_copy(HttpServletRequest request)
//			{
//				int branchid = Integer.parseInt(ims.Dcrypt(request.getParameter("eBranchId")));
//				String appId=ims.Dcrypt(request.getParameter("eappId"));
//				int stDocId = Integer.parseInt(ims.Dcrypt(request.getParameter("stDocId")));
//				int stSchemeId = Integer.parseInt(ims.Dcrypt(request.getParameter("stSchemeId")));
//				String temp = gudh.CheckFileUploadForDocId(appId, branchid,stDocId, stSchemeId);
//				int flag=-1;
//				if(temp.equals("Not Exists")){
//					flag=0;
//				}else{
//					flag=1;
//				}
//				System.out.println("checkftrupload: "+flag);
//				return ""+flag;
//			}
			
			
			//fmcd vivekthandle temporary
			@RequestMapping(value="/ApplicantFMCS",method=RequestMethod.GET)
			public ModelAndView applicantFMCSDashBoard(@ModelAttribute("dashApp")ApplicantModel appModel,HttpServletRequest request_p){
				ModelAndView mav = new ModelAndView();
				int scheme_id=0;
				try{
					HttpSession httpsession = request_p.getSession(false);
					if(httpsession == null){
						mav.setViewName("sessionExpire");
					}else{
						Session userSession=null;
						userSession = (Session) httpsession.getAttribute("logged-in");
							if(userSession!=null){
								String stCSRFToken = iuv.generateCSRFToken("ApplicantFMCS", request_p);
								mav.addObject("csrftoken", stCSRFToken);
								int iUserId = userSession.getUserid();
								String stEmail_p = userSession.getUsername();
								
								mav.setViewName("appdashfmcs");
								mav.addObject("schemeId", "11");
							}
							else if(userSession==null){
								mav.setViewName("sessionExpire");
							}
					}
				}
				catch(NullPointerException e){
					e.printStackTrace();
					mav.setViewName("ErrorPage");
				}
				return mav;
				
			}
			
			@RequestMapping(value="/ApplicantTestSample",method=RequestMethod.GET)
			public ModelAndView ApplicantTestSample(@ModelAttribute("dashApp")ApplicantModel appModel,HttpServletRequest request_p){
				ModelAndView mav = new ModelAndView();

				try{
					HttpSession httpsession = request_p.getSession(false);
					if(httpsession == null){
						mav.setViewName("sessionExpire");
					}else{
						Session userSession=null;
						userSession = (Session) httpsession.getAttribute("logged-in");
							if(userSession!=null){
								int iUserId = userSession.getUserid();
								String stName = "";
								if(userSession.getFname().length()>1 && userSession.getLname().length()>1){
									stName = userSession.getFname().substring(0, 1).toUpperCase()+userSession.getFname().substring(1)+" "+userSession.getLname().substring(0, 1).toUpperCase()+userSession.getLname().substring(1);
								}
								else{
									stName = userSession.getFname()+" "+userSession.getLname(); 
								}
								String stEmail_p = userSession.getUsername();
								HashMap hm = appserv.getApplicantDashboardData(iUserId,stEmail_p);
								int countPendingFee=appserv.countPendingFee(iUserId);
								String pc_usermanual = ResourceBundleFile.getValueFromKey("PC_UserManual");
								
								
								//Check if User Profile is submitted
								int iProfileCompletionStatus = ups.getProfileCompletionStatus(iUserId);
								if(iProfileCompletionStatus==0){
									//Redirect to User Profile
									mav.addObject("profile", "0");
								}else{
									int iGstCheck = ups.checkIfGSTAdded(iUserId);
									
									if(iGstCheck==0){
										//GST No Not added, redirect to /getGSTNumber
										mav.addObject("profile", "1");
									}
									else{
										mav.addObject("profile", "2");	
									}
									
								}
								
								mav.setViewName("appdash_testreport");
								mav.addObject("pc_usermanual", pc_usermanual);	
								mav.addObject("appcounts", hm);				
								mav.addObject("countPendingFee", countPendingFee);
								mav.addObject("Name", stName);
							}
							else if(userSession==null){
								mav.setViewName("sessionExpire");
							}
					}
				}
				catch(NullPointerException e){
					e.printStackTrace();
					mav.setViewName("ErrorPage");
				}
				return mav;
				
			}
			
			
}



