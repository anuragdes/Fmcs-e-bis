package Global.Config.Controller;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import Global.CommonUtility.ResourceBundleFile;
import Global.Config.Model.ConfigModel;
import Global.Config.Service.IConfigService;
import Global.Login.Model.Session;
import Global.Login.Model.UpdatePwdModel;
import Global.Login.Service.IUserValidator;
import Global.Registration.Domain.RegisterDomain;
import Schemes.ProductCertification.LicecneRenewal.Service.ConnInfo;
import eBIS.AppConfig.CustomWebExceptionHandler;
import lab.domain.Lab_Type_Domain;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

@Controller
public class ConfigController {

	@Autowired
	IConfigService configserv;
	
	@Autowired
	IUserValidator iuv;
	
	@CustomWebExceptionHandler()	
	@RequestMapping(value="/UnblockUser",method=RequestMethod.GET)
	public ModelAndView ApplicantDashboard(@ModelAttribute("unblock") ConfigModel lmod_p, HttpServletRequest request_p){
		ModelAndView mav = new ModelAndView();
		if(iuv.CheckUserForm("UnblockUser", request_p)){
			try{
				HttpSession httpsession = request_p.getSession(false);
				if(httpsession == null){
					mav.setViewName("sessionExpire");
				}else{
					Session userSession=null;
					userSession = (Session) httpsession.getAttribute("logged-in");
						if(userSession!=null){
							//Getting Location ID and Location Type ID of the configurator
							int iLocationId = userSession.getLocation_id();
							int iLocationTypeId = userSession.getLocation_type();
							
							//Sending the configurator Location to get the list of blocked users
							List<RegisterDomain> blockedUsersList = configserv.getBlockedUsers(iLocationId, iLocationTypeId);
							mav.addObject("blist", blockedUsersList);
							mav.setViewName("unblockusers");
							
							//CSRF
							String stCSRFToken = iuv.generateCSRFToken("UnblockUser", request_p);
							mav.addObject("csrftoken", stCSRFToken);
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
		}
		else{
			mav.addObject("forgot_head","Access Denied.");
			mav.addObject("forgot_response","You have no access to this process, Kindly contact the administrator.");
			mav.setViewName("recoversuccess");
		}	
		return mav;
		
	}
	
	@RequestMapping(value="/getUsernames",method=RequestMethod.POST)
	public @ResponseBody List<ConfigModel> getUsernamesOfBlockedUsers(HttpServletRequest request_p){
		String stEmail = request_p.getParameter("stEmail");
		List<RegisterDomain> lrd = configserv.getUsernamesOfBlockedUsers(stEmail);
		
		List<ConfigModel> lcm = new ArrayList<ConfigModel>();
		
		for(int i=0;i<lrd.size();i++){
			ConfigModel cm = new ConfigModel();
			cm.setStUsername(lrd.get(i).getUsername());
			lcm.add(cm);
		}
		
		for(int i=0;i<lcm.size();i++){
			System.out.println("thethe====" + lcm.get(i).getStUsername());
		}
		
		
		return lcm;
	}
	
	@RequestMapping(value="/UnblockUser",method=RequestMethod.POST)
	public ModelAndView UnblockUser(@ModelAttribute("unblock") ConfigModel lmod_p, HttpServletRequest request_p){
		ModelAndView mav = new ModelAndView();
		try{
			HttpSession httpsession = request_p.getSession(false);
			if(httpsession == null){
				mav.setViewName("sessionExpire");
			}else{
				Session userSession=null;
				userSession = (Session) httpsession.getAttribute("logged-in");
					if(userSession!=null){
						if(iuv.authenticateCSRFToken("UnblockUser", lmod_p.getStCSRFToken(), request_p)){
							int iEmailUsernameFlag = lmod_p.getiEmailUserNameFlag();
							int userId=userSession.getUserid();
							String stUsername = lmod_p.getStUsername().trim();
							int iLocationId = userSession.getLocation_id();
							int iLocationTypeId = userSession.getLocation_type();
							if(iEmailUsernameFlag==0){
								String stEmail = lmod_p.getStEmail().trim();
								//Unblock with Email and Username
								if(stEmail.equals("") || stEmail.equals("0")){
									mav.addObject("error", "Please enter a valid Email");
								}
								else if(stUsername.equals("") || stUsername.equals("0")){
									mav.addObject("error", "Please enter a valid Username");
								}
								else{
									String stResponse = configserv.unblockedUser(stEmail,stUsername,userId);
									if(stResponse.equals("Success")){
										mav.addObject("success", "User Unblocked Successfully");
									}
									else{
										mav.addObject("error", "There was a problem unblocking this user");
									}
								}
							}
							else if(iEmailUsernameFlag==1){
								//Unblock with just username
								if(stUsername.equals("") || stUsername.equals("0")){
									mav.addObject("error", "Please enter a valid Username");
								}
								else{
									String stResponse = configserv.unblockedUser("0",stUsername,userId);
									if(stResponse.equals("Success")){
										mav.addObject("success", "User Unblocked Successfully");
									}
									else{
										mav.addObject("error", "There was a problem unblocking this user");
									}
								}
							}
							else{
								//Don't unblock at all
								mav.addObject("error", "Internal Error, please try later");
							}
							
							//CSRF
							String stCSRFToken = iuv.generateCSRFToken("UnblockUser", request_p);
							mav.addObject("csrftoken", stCSRFToken);
							
							List<RegisterDomain> blockedUsersList = configserv.getBlockedUsers(iLocationId, iLocationTypeId);
							mav.addObject("blist", blockedUsersList);
							mav.setViewName("unblockusers");
						}
						else{
							mav.setViewName("csrfErrorPage");
						}
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
	
	@RequestMapping(value="/resendVerificationLink", method=RequestMethod.GET)
	public ModelAndView resendVerificationLink(@ModelAttribute("resend") UpdatePwdModel uPwdmod_p, HttpServletRequest request_p){
		ModelAndView mav = new ModelAndView();
		if(iuv.CheckUserForm("resendVerificationLink", request_p)){
			try{
				HttpSession httpsession = request_p.getSession(false);
				if(httpsession == null){
					mav.setViewName("sessionExpire");
				}else{
					Session userSession=null;
					userSession = (Session) httpsession.getAttribute("logged-in");
						if(userSession!=null){
							
							mav.setViewName("resendVerificationLink");
							
							//CSRF
							String stCSRFToken = iuv.generateCSRFToken("resendVerificationLink", request_p);
							mav.addObject("csrftoken", stCSRFToken);
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
		}
		else{
			mav.addObject("forgot_head","Access Denied.");
			mav.addObject("forgot_response","You have no access to this process, Kindly contact the administrator.");
			mav.setViewName("recoversuccess");
		}	
		return mav;
	}
	
	
	@RequestMapping(value="/resendVerificationLink",method=RequestMethod.POST)
	public ModelAndView resendVerificationLinkPost(@ModelAttribute("resend") UpdatePwdModel uPwdmod_p, HttpServletRequest request_p)throws MessagingException{
		ModelAndView mav = new ModelAndView();
		try{
			HttpSession httpsession = request_p.getSession(false);
			if(httpsession == null){
				mav.setViewName("sessionExpire");
			}else{
				Session userSession=null;
				userSession = (Session) httpsession.getAttribute("logged-in");
					if(userSession!=null){
						if(iuv.authenticateCSRFToken("resendVerificationLink", uPwdmod_p.getStCsrfToken(), request_p)){
							int userId=userSession.getUserid();
							String stEmail = uPwdmod_p.getStPwd().trim();
							String stUsername = uPwdmod_p.getStRPwd().trim();
							
							String stResponse = configserv.resendVerificationLink(stEmail, stUsername,userId);
							
							mav.addObject("success", stResponse);
							
							//CSRF
							String stCSRFToken = iuv.generateCSRFToken("resendVerificationLink", request_p);
							mav.addObject("csrftoken", stCSRFToken);
							
							mav.setViewName("resendVerificationLink");
						}
						else{
							mav.setViewName("csrfErrorPage");
						}
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
	
	@RequestMapping(value="/credentialSummry", method=RequestMethod.GET)
	public ModelAndView overallCredentialDetail(@ModelAttribute("credentialSummryModel") ConfigModel configModel, HttpServletRequest request_p){
		 ModelAndView  summryMv = new ModelAndView();
			try{
				HttpSession httpsession = request_p.getSession(false);
				if(httpsession == null){
					summryMv.setViewName("sessionExpire");
				}
				else{
					List<HashMap<String, Object>> credentialDetail = new ArrayList<HashMap<String,Object>>();
					credentialDetail = configserv.getCredentialSummry();		 
/*					System.out.println("credentialDetail size - - -- - " + credentialDetail.size());
					System.out.println("credentialDetail size - - -- - " + credentialDetail.get(0).get("TotalLicences"));
					System.out.println("credentialDetail size - - -- - " + credentialDetail.get(0).get("TotalApplications"));*/
					summryMv.addObject("credentialDetail",credentialDetail);
					summryMv.addObject("scheme","PC");
					summryMv.setViewName("credentialSummry");
				}
		
	         }
			 catch(NullPointerException e){
				e.printStackTrace();
				summryMv.setViewName("ErrorPage");
			}
			return summryMv;
	}
// ADD BY IMRAN 
	@RequestMapping(value="/credentialSummryHM", method=RequestMethod.GET)
	public ModelAndView overallCredentialDetailHM(@ModelAttribute("credentialSummryModel") ConfigModel configModel, HttpServletRequest request_p){
		 ModelAndView  summryMv = new ModelAndView();
			try{
				HttpSession httpsession = request_p.getSession(false);
				if(httpsession == null){
					summryMv.setViewName("sessionExpire");
				}
				else{
					List<HashMap<String, Object>> credentialDetail = new ArrayList<HashMap<String,Object>>();
					credentialDetail = configserv.getCredentialSummryHM();		 
				System.out.println("credentialDetail size - - -- - " + credentialDetail.size());
					System.out.println("credentialDetail size - - -- - " + credentialDetail.get(0).get("TotalLicences"));
					System.out.println("credentialDetail size - - -- - " + credentialDetail.get(0).get("TotalApplications"));
					summryMv.addObject("credentialDetail",credentialDetail);
					summryMv.addObject("scheme","HM");
					summryMv.setViewName("credentialSummry");
				}
		
	         }
			 catch(NullPointerException e){
				e.printStackTrace();
				summryMv.setViewName("ErrorPage");
			}
			return summryMv;
	}
	
	
	
	///MCR Report
	@RequestMapping(value="/MCR_report",method=RequestMethod.GET)
	public ModelAndView showMCRRportForm(@ModelAttribute("mcr") ConfigModel lmod_p, HttpServletRequest request_p){
		ModelAndView mav = new ModelAndView();
		try{
			HttpSession httpsession = request_p.getSession(false);
			if(httpsession == null){
				mav.setViewName("redirect:/sessionExpire");
			}else{
				Session userSession=null;
				userSession = (Session) httpsession.getAttribute("logged-in");
					if(userSession!=null){
						//CSRF
						String stCSRFToken = iuv.generateCSRFToken("MCR_report", request_p);
						mav.addObject("csrftoken", stCSRFToken);
						
						mav.setViewName("mcr_report");
					}
					else if(userSession==null){
						mav.setViewName("redirect:/sessionExpire");
					}
			}
		}
		catch(NullPointerException e){
			e.printStackTrace();
			mav.setViewName("ErrorPage");
		}
		
		return mav;
		
	}
	
	public Map<TimeUnit,Long> computeDateDiff(Date date1, Date date2) {
	    long diffInMillies = date2.getTime() - date1.getTime();
	    List<TimeUnit> units = new ArrayList<TimeUnit>(EnumSet.allOf(TimeUnit.class));
	    Collections.reverse(units);
	    Map<TimeUnit,Long> result = new LinkedHashMap<TimeUnit,Long>();
	    long milliesRest = diffInMillies;
	    for ( TimeUnit unit : units ) {
	        long diff = unit.convert(milliesRest,TimeUnit.MILLISECONDS);
	        long diffInMilliesForUnit = unit.toMillis(diff);
	        milliesRest = milliesRest - diffInMilliesForUnit;
	        result.put(unit,diff);
	    }
	    return result;
	}
	
	
	@RequestMapping(value="/MCR_report",method=RequestMethod.POST)
	public void printMCRRport(@ModelAttribute("mcr") ConfigModel lmod_p, HttpServletRequest request_p, HttpServletResponse response){
		try{
			HttpSession httpsession = request_p.getSession(false);
			if(httpsession != null){
				Session userSession=null;
				userSession = (Session) httpsession.getAttribute("logged-in");
					if(userSession!=null){
						String stCSRFToken = lmod_p.getStCSRFToken();
						//CSRF
						if(iuv.authenticateCSRFToken("MCR_report", stCSRFToken, request_p)){
							Date startDate = new Date();
							System.out.println("Job Started on "+startDate);
							//From date
							String stFromDate = lmod_p.getStEmail();
							
							//To Date
							String stToDate = lmod_p.getStUsername();
							
							//Initializing connection
							ConnInfo connInfo=new ConnInfo();
							
							try{
								//Date Conversion Logic
								SimpleDateFormat smdate = new SimpleDateFormat("dd-MM-yyyy");
								SimpleDateFormat mmdate = new SimpleDateFormat("dd-MMM-yyyy");
								
								String stFinalFromDate = mmdate.format(smdate.parse(stFromDate));
								String stFinalToDate = mmdate.format(smdate.parse(stToDate));
								
								//Calling JRXML
								String outputfile="MCR.xls";
								response.setContentType("application/vnd.ms-excel");
								response.setHeader("Content-disposition","attachment; filename=\""+outputfile+"\"");
								
								
								String jrxmlpath = ResourceBundleFile.getValueFromKey("JRXML_PATH");
								String outputfilepath = ResourceBundleFile.getValueFromKey("JRXML_PDF_PATH");
								
								String stFullPath = outputfilepath+outputfile;
								
								Connection _conn=connInfo.getConnection();
								
								//Adding Params
								Map parameters = new HashMap();
								parameters.put("date1", stFinalFromDate);
								parameters.put("date2", stFinalToDate);
								
								parameters.put(JRParameter.IS_IGNORE_PAGINATION,Boolean.FALSE);

								JasperPrint printFileName = JasperFillManager.fillReport(jrxmlpath+"mcr_report.jasper",parameters,_conn);

								JRExporter exporter=null;
								if(printFileName!=null){
									exporter=new JRXlsExporter();
				                	exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, printFileName);
				                	exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
				                	exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
				                	exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, true);
				                	exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
				                	exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
				                	exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, true);
				                	exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, stFullPath);
				                	exporter.exportReport();
								}
								
								
								response.setHeader("Expires", "0");
								response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
								response.setHeader("Pragma", "public");
								
								FileInputStream inputstream=new FileInputStream(stFullPath);
							    
								OutputStream out=response.getOutputStream();
							    FileCopyUtils.copy(inputstream, out);
							    out.close();
							}
							catch(Exception e){
								e.printStackTrace();
							}
							finally{
								connInfo.close();
								Date endDate = new Date();
								System.out.println("Job Finished on "+endDate);
								System.out.println("Job Duration "+computeDateDiff(startDate,endDate));
							}
						}
						
					}
					
			}
		}
		catch(NullPointerException e){
			e.printStackTrace();
		}
		
		//return "redirect:/MCR_report";
		
	}
	
	
	
	//All India Branch Wise Status
	@RequestMapping(value="/applicationHistoryAndReport",method=RequestMethod.GET)
	public ModelAndView showApplicationHistoryAndReportForm(@ModelAttribute("mcr") ConfigModel lmod_p, HttpServletRequest request_p){
		ModelAndView mav = new ModelAndView();
		try{
			HttpSession httpsession = request_p.getSession(false);
			if(httpsession == null){
				mav.setViewName("redirect:/sessionExpire");
			}else{
				Session userSession=null;
				userSession = (Session) httpsession.getAttribute("logged-in");
					if(userSession!=null){
						if(request_p.getParameter("report")!=null){
							//CSRF
							String stCSRFToken = iuv.generateCSRFToken("applicationHistoryAndReport", request_p);
							mav.addObject("csrftoken", stCSRFToken);
							
							mav.setViewName("applicationHistoryAndReport");
							
							int iReportId = Integer.parseInt(request_p.getParameter("report"));
							
							mav.addObject("reportId", iReportId);
							
							if(iReportId==1){		
								mav.addObject("reportText", "Branch wise all India application status");
							}
							else if(iReportId==2){
								mav.addObject("reportText", "Date wise all India application history");
							}
							else if(iReportId==3){
								mav.addObject("reportText", "Day wise all India application history");
							}
							else{
								mav.setViewName("redirect:/login");
							}
							
						}
						else{
							mav.setViewName("redirect:/login");
						}
					}
					else if(userSession==null){
						mav.setViewName("redirect:/sessionExpire");
					}
			}
		}
		catch(NullPointerException e){
			e.printStackTrace();
			mav.setViewName("ErrorPage");
		}
		catch (Exception e) {
			mav.setViewName("redirect:/login");
		}
		
		return mav;
		
	}
	
	
	@RequestMapping(value="/applicationHistoryAndReport",method=RequestMethod.POST)
	public void printApplicationHistoryAndReport(@ModelAttribute("mcr") ConfigModel lmod_p, HttpServletRequest request_p, HttpServletResponse response){
		try{
			HttpSession httpsession = request_p.getSession(false);
			if(httpsession != null){
				Session userSession=null;
				userSession = (Session) httpsession.getAttribute("logged-in");
					if(userSession!=null){
						String stCSRFToken = lmod_p.getStCSRFToken();
						//CSRF
						if(iuv.authenticateCSRFToken("applicationHistoryAndReport", stCSRFToken, request_p)){
							
							int iReportId = lmod_p.getiEmailUserNameFlag();
							
							if(iReportId > 0 && iReportId < 4){
								
								String stJasperToUse = "";
								String stOutputFilename = "";
								
								if(iReportId==1){
									stJasperToUse = "branchwiseallindiahistory.jasper";
									stOutputFilename = "All_India_History.xls";
								}
								else if(iReportId==2){
									stJasperToUse = "datewiseapplicationhistory.jasper";
									stOutputFilename = "Date_wise_History.xls";
								}
								else if(iReportId==3){
									stJasperToUse = "daywiseapplicationhistory.jasper";
									stOutputFilename = "Day_wise_History.xls";
								}
								
								Date startDate = new Date();
								System.out.println("Job Started on "+startDate);
								//From date
								String stFromDate = lmod_p.getStEmail();
								
								//To Date
								String stToDate = lmod_p.getStUsername();
								
								//Initializing connection
								ConnInfo connInfo=new ConnInfo();
								
								try{
									//Date Conversion Logic
									SimpleDateFormat smdate = new SimpleDateFormat("dd-MM-yyyy");
									SimpleDateFormat mmdate = new SimpleDateFormat("dd-MMM-yyyy");
									
									String stFinalFromDate = mmdate.format(smdate.parse(stFromDate));
									String stFinalToDate = mmdate.format(smdate.parse(stToDate));
									
									//Calling JRXML
									response.setContentType("application/vnd.ms-excel");
									response.setHeader("Content-disposition","attachment; filename=\""+stOutputFilename+"\"");
									
									
									String jrxmlpath = ResourceBundleFile.getValueFromKey("JRXML_PATH");
									String outputfilepath = ResourceBundleFile.getValueFromKey("JRXML_PDF_PATH");
									
									String stFullPath = outputfilepath+stOutputFilename;
									
									Connection _conn=connInfo.getConnection();
									
									//Adding Params
									Map parameters = new HashMap();
									parameters.put("date1", stFinalFromDate);
									parameters.put("date2", stFinalToDate);
									
									parameters.put(JRParameter.IS_IGNORE_PAGINATION,Boolean.FALSE);

									JasperPrint printFileName = JasperFillManager.fillReport(jrxmlpath+stJasperToUse,parameters,_conn);

									JRExporter exporter=null;
									if(printFileName!=null){
										exporter=new JRXlsExporter();
					                	exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, printFileName);
					                	exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
					                	exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
					                	exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, true);
					                	exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
					                	exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
					                	exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, true);
					                	exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, stFullPath);
					                	exporter.exportReport();
									}
									
									
									response.setHeader("Expires", "0");
									response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
									response.setHeader("Pragma", "public");
									
									FileInputStream inputstream=new FileInputStream(stFullPath);
								    
									OutputStream out=response.getOutputStream();
								    FileCopyUtils.copy(inputstream, out);
								    out.close();
								}
								catch(Exception e){
									e.printStackTrace();
								}
								finally{
									connInfo.close();
									Date endDate = new Date();
									System.out.println("Job Finished on "+endDate);
									System.out.println("Job Duration "+computeDateDiff(startDate,endDate));
								}
							}
							
						}
						
					}
					
			}
		}
		catch(NullPointerException e){
			e.printStackTrace();
		}
		
	}
	
	
	//All India Branch Wise Status
	@RequestMapping(value="/pcConsolidateReport",method=RequestMethod.GET)
	public ModelAndView showPcConsolidateReport(@ModelAttribute("mcr") ConfigModel lmod_p, HttpServletRequest request_p){
		
		
		ModelAndView mav = new ModelAndView();
		try{
			
			HttpSession httpsession = request_p.getSession(false);
			if(httpsession == null){
				mav.setViewName("redirect:/sessionExpire");
			}else{
				
				Session userSession=null;
				userSession = (Session) httpsession.getAttribute("logged-in");
				
					if(userSession!=null){
						
						  int   branch = userSession.getLocation_id();
						  int   role_id = Integer.parseInt(userSession.getCurrent_role());
						  int  userid = userSession.getUserid();
						  
						if(role_id==2 || role_id==4 || role_id==5){
							//CSRF
							String stCSRFToken = iuv.generateCSRFToken("pcConsolidateReport", request_p);
							mav.addObject("csrftoken", stCSRFToken);
							
							mav.setViewName("pcConsolidateReport");						
							
							if(role_id==2){		
								mav.addObject("reportText", "Product certification Consolidate Report");
							}
							else if(role_id==4){
								mav.addObject("reportText", "Product certification Consolidate Report");
							}
							else if(role_id==5){
								mav.addObject("reportText", "Product certification Consolidate Report");
							}
							else{
								mav.setViewName("redirect:/login");
							}
							
						}
						else{
							mav.setViewName("redirect:/login");
						}
					}
					else if(userSession==null){
						mav.setViewName("redirect:/sessionExpire");
					}
			}
		}
		catch(NullPointerException e){
			e.printStackTrace();
			mav.setViewName("ErrorPage");
		}
		catch (Exception e) {
			mav.setViewName("redirect:/login");
		}
		
		return mav;
		
	}
	
	
	@RequestMapping(value="/pcConsolidateReport",method=RequestMethod.POST)
	public @ResponseBody List<HashMap<String,String>> printPcConsolidateReport(@ModelAttribute("mcr") ConfigModel lmod_p, HttpServletRequest request_p, HttpServletResponse response){
		
		List<HashMap<String,String>> data=null;			
		try{
			HttpSession httpsession = request_p.getSession(false);
			if(httpsession != null){
				Session userSession=null;
				userSession = (Session) httpsession.getAttribute("logged-in");
				
					if(userSession!=null){
						  int   branch = userSession.getLocation_id();
						  int   role_id = Integer.parseInt(userSession.getCurrent_role());
						  int  userid = userSession.getUserid();					
						  
						  String fromDt=request_p.getParameter("fromDate");
						  String toDt=request_p.getParameter("toDate");
						  String stCSRFToken=request_p.getParameter("tocken");	
						 
						System.out.println("fromDt:::::::"+fromDt+"toDt::"+toDt+"::stCSRFToken"+stCSRFToken);
						//CSRF
						if(iuv.authenticateCSRFToken("pcConsolidateReport", stCSRFToken, request_p)){
							
							
							if(role_id > 0 && role_id < 5){
								
								Date startDate = new Date();
								System.out.println("Job Started on "+startDate);
																
								try{
									//Date Conversion Logic
									SimpleDateFormat smdate = new SimpleDateFormat("dd-mm-yyyy");
									SimpleDateFormat mmdate = new SimpleDateFormat("yyyymmdd");
									
									String stFinalFromDate = mmdate.format(smdate.parse(fromDt));
									String stFinalToDate = mmdate.format(smdate.parse(toDt));
									
									 data = configserv.getPcConsolidateRptData(branch, role_id, userid, stFinalFromDate, stFinalToDate);
									 								
								}
								catch(Exception e){
									e.printStackTrace();
								}
								finally{
								
								}
							}
							
						}
						
					}
					
			}
		}
		catch(NullPointerException e){
			e.printStackTrace();
		}
		return data;	
	}
	
	
	//labConsolidatedReport
	@RequestMapping(value="/LabConsolidateReport",method=RequestMethod.GET)
	public ModelAndView showLabConsolidatedReportForm(@ModelAttribute("mcr") ConfigModel lmod_p, HttpServletRequest request_p){
		ModelAndView mav = new ModelAndView();
		try{
			HttpSession httpsession = request_p.getSession(false);
			if(httpsession == null){
				mav.setViewName("redirect:/sessionExpire");
			}else{
				Session userSession;
				userSession = (Session) httpsession.getAttribute("logged-in");
				String roleId="";
				String locationId ="";
				String locationTypeId ="";
					if(userSession!=null){
						//CSRF
						String stCSRFToken = iuv.generateCSRFToken("labConsolidatedReport", request_p);
						mav.addObject("csrftoken", stCSRFToken);
						
						roleId = userSession.getCurrent_role();
						locationId = String.valueOf(userSession.getLocation_id());
						locationTypeId = String.valueOf(userSession.getLocation_type());
						
						List<Lab_Type_Domain> labTypeList=configserv.getLabTypeList();
						mav.addObject("labTypeList",labTypeList);
						mav.addObject("roleId",roleId);
						mav.addObject("locationId",locationId);
						mav.addObject("locationTypeId",locationTypeId);
						//List<Branch_Master_Domain> Branch = configserv.getBo();
						//mav.addObject("Branch", Branch);
						mav.setViewName("labConsolidatedReport");

					}
					else if(userSession==null){
						mav.setViewName("redirect:/sessionExpire");
					}
			}
		}
		catch(NullPointerException e){
			e.printStackTrace();
			mav.setViewName("ErrorPage");
		}
		catch (Exception e) {
			mav.setViewName("redirect:/login");
		}
		
		return mav;
		
	}
	
	@RequestMapping(value="/gettLabNameReport",method=RequestMethod.POST,  produces="application/json")
	public @ResponseBody List getLabName(HttpServletRequest request, HttpServletResponse response){
		
		String labId = request.getParameter("labId");
		List labList = new ArrayList();
		labList = configserv.getLabName(labId);
		return labList;
	}
	
	@RequestMapping(value="/getLabNameFrHead",method=RequestMethod.POST,  produces="application/json")
	public @ResponseBody List getBISLabNameFrHead(HttpServletRequest request, HttpServletResponse response){
		
		String locationId = request.getParameter("locationId");
		List labList = new ArrayList();
		labList = configserv.getBISLabNameFrHead(locationId);
		return labList;
	}
	
	@RequestMapping(value="/gettBranchName",method=RequestMethod.POST,  produces="application/json")
	public @ResponseBody List gettBranchName(HttpServletRequest request, HttpServletResponse response){
		
		String branchId = request.getParameter("branchId");
		List branchList = new ArrayList();
		branchList = configserv.getBo();
		return branchList;
	}
	
	
	@RequestMapping(value="/labConsolidatedReport",method=RequestMethod.POST)
	public void printLabConsolidatedReport(@ModelAttribute("mcr") ConfigModel lmod_p, HttpServletRequest request_p, HttpServletResponse response){
		try{
			HttpSession httpsession = request_p.getSession(false);
			if(httpsession != null){
				Session userSession=null;
				userSession = (Session) httpsession.getAttribute("logged-in");
					if(userSession!=null){
						String stCSRFToken = lmod_p.getStCSRFToken();
						//CSRF
						if(iuv.authenticateCSRFToken("labConsolidatedReport", stCSRFToken, request_p)){
							
							int iLabId = userSession.getLocation_id();
							int iRoleId = Integer.parseInt(userSession.getCurrent_role());
							int locationId = userSession.getLocation_id();
							int locationTypeId = userSession.getLocation_type();
							
							String stJasperToUse = "labConsolidatedReport.jasper";
							String stOutputFilename = "labReport.xls";
							
							Date startDate = new Date();
							System.out.println("Job Started on "+startDate);
							//From date
							String stFromDate = lmod_p.getStEmail();
							
							//To Date
							String stToDate = lmod_p.getStUsername();
							
							//Lab Type
							String labType="";
							if(iRoleId == 11){
								if(locationTypeId == 3){
									labType = "1";	
								}else if(locationTypeId == 4){
									labType = "2";
								}
							
							}else{
								 labType = lmod_p.getLabType();
							}
							
							//Lab Name
							String labName = lmod_p.getLabName();
							
							//Branch Type
							String branchType =lmod_p.getBranch();
							
							//Branch Name
							String branchName = lmod_p.getBranchName();
							
							System.out.println("labType :"+labType+"\t"+"labName :"+labName);
							System.out.println("branchType :"+branchType+"\t"+"branchName :"+branchName);
							//Initializing connection
							ConnInfo connInfo=new ConnInfo();
							
							try{
								//Date Conversion Logic
								SimpleDateFormat smdate = new SimpleDateFormat("dd-MM-yyyy");
								SimpleDateFormat mmdate = new SimpleDateFormat("dd-MMM-yyyy");
								
								String stFinalFromDate = mmdate.format(smdate.parse(stFromDate));
								String stFinalToDate = mmdate.format(smdate.parse(stToDate));
								
								//Calling JRXML
								response.setContentType("application/vnd.ms-excel");
								response.setHeader("Content-disposition","attachment; filename=\""+stOutputFilename+"\"");
								
								
								String jrxmlpath = ResourceBundleFile.getValueFromKey("JRXML_PATH");
								String outputfilepath = ResourceBundleFile.getValueFromKey("JRXML_PDF_PATH");
								
								String stFullPath = outputfilepath+stOutputFilename;
								
								Connection _conn=connInfo.getConnection();
								
								//Adding Params
								Map parameters = new HashMap();
								/*parameters.put("date1", stFinalFromDate);
								parameters.put("date2", stFinalToDate);
								parameters.put("roleid", iRoleId);
								parameters.put("labid", iLabId);*/
								
								parameters.put("From_date", stFinalFromDate);
								parameters.put("To_date", stFinalToDate);
								parameters.put("branchId", ""+iLabId);
								parameters.put("roleId", ""+iRoleId);
								parameters.put("labType", labType);
								parameters.put("labName", labName);
								parameters.put("branchType", ""+branchType);
								parameters.put("branchName", ""+branchName);

								
								parameters.put(JRParameter.IS_IGNORE_PAGINATION,Boolean.FALSE);

								JasperPrint printFileName = JasperFillManager.fillReport(jrxmlpath+stJasperToUse,parameters,_conn);

								JRExporter exporter=null;
								if(printFileName!=null){
									exporter=new JRXlsExporter();
				                	exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, printFileName);
				                	exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
				                	exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
				                	exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, true);
				                	exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
				                	exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
				                	exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, true);
				                	exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, stFullPath);
				                	exporter.exportReport();
								}
								
								
								response.setHeader("Expires", "0");
								response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
								response.setHeader("Pragma", "public");
								
								FileInputStream inputstream=new FileInputStream(stFullPath);
							    
								OutputStream out=response.getOutputStream();
							    FileCopyUtils.copy(inputstream, out);
							    out.close();
							}
							catch(Exception e){
								e.printStackTrace();
							}
							finally{
								connInfo.close();
								Date endDate = new Date();
								System.out.println("Job Finished on "+endDate);
								System.out.println("Job Duration "+computeDateDiff(startDate,endDate));
							}
							
						}
						
					}
					
			}
		}
		catch(NullPointerException e){
			e.printStackTrace();
		}
		
	}


}
