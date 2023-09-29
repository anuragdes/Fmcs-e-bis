package BIS.Einvoice.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PublicKey;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;

import BIS.Einvoice.DAO.AHCEInvoiceDao;
import BIS.Einvoice.Domain.AHCEInvoiceAuthenticationDomain;
import BIS.Einvoice.Domain.AHCEInvoiceCancelIRNDomain;
import BIS.Einvoice.Domain.AHCEInvoiceErrorLogDomain;
import BIS.Einvoice.Domain.AHCEInvoiceIRNDtlsDomain;
import BIS.Einvoice.Domain.AHCEinvoiceAuthJsonDataDomain;
import BIS.Einvoice.Domain.EinvoicingGSTCredentailsDomain;
import BIS.Einvoice.Utility.AHCEInvoiceUtil;
import BIS.Einvoice.Utility.AHCEInvoiceUtilProp;
import BIS.Einvoice.Utility.AHCQRCodeUtil;
import Global.CommonUtility.ResourceBundleFile;
import Global.CommonUtility.Controller.UploadFiles;
import Global.CommonUtility.DAO.GlobalDao;
import Global.CommonUtility.Service.SendMail;
import Masters.Domain.Branch_Master_Domain;
import Masters.Domain.GSTMasterDomain;
import Masters.Domain.Regional_Mst_Domain;
import Masters.Domain.State_Mst_Domain;
import Schemes.HallMarking.AHC.ApplicationSubmission.DAO.AHCFeeDao;
import Schemes.HallMarking.AHC.ApplicationSubmission.Domain.PaymentDetailsDomain_AHC;
import Schemes.HallMarking.AHC.ApplicationSubmission.Domain.ahcRecogApplicationSubDomain;
import Schemes.HallMarking.AHC.ApplicationSubmission.Model.FeePaidDetailsModel;
import Schemes.HallMarking.AHC.ApplicationSubmission.Service.AHC_LOI_Service;
import Schemes.ProductCertification.LicecneRenewal.Service.ConnInfo;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;
import sun.misc.BASE64Decoder;

@Service
public class AHCEInvoiceServImpl implements AHCEInvoiceService {

	@Autowired
	AHCEInvoiceDao einvoiceDao;
	@Autowired
	public GlobalDao globalDao;
	@Autowired
	UploadFiles ftpFiles;
	@Autowired
	AHC_LOI_Service ahcloiserv;
	@Autowired
	AHCFeeDao feeDao;
	@Autowired
	SendMail sm;
	@Autowired
	UploadFiles uploadFiles;
	 
	   ConnInfo connInfo=new ConnInfo();
	   String jrxmlpath = ResourceBundleFile.getValueFromKey("JRXML_PATH");
	   String outputfilepath = ResourceBundleFile.getValueFromKey("JRXML_PDF_PATH");	  
	   String pdf_Path = ResourceBundleFile.getValueFromKey("DOCUPLOADPATH");
	  
	
	private static final String CONTENT_TYPE = "text/html; charset=windows-1252";
	public static final String CHARACTER_ENCODING = "UTF-8";

	private static final String clientId = AHCEInvoiceUtilProp.getValueFromKey("CLIENT_ID").trim();
	private static final String clientSecret = AHCEInvoiceUtilProp.getValueFromKey("CLIENT_SECRET").trim();
	private static final String emailid = AHCEInvoiceUtilProp.getValueFromKey("EMAIL_ID").trim();
	//private static final String username = AHCEInvoiceUtilProp.getValueFromKey("USERNAME").trim();
	//private static final String password = AHCEInvoiceUtilProp.getValueFromKey("PASSWORD").trim();

	//private static final SimpleDateFormat parseDateFormat = new SimpleDateFormat("dd/MM/yyyy");
	private static SimpleDateFormat dtformatter = new SimpleDateFormat("dd-MM-yyyy");
	//private static final String QR_CODE_IMAGE_PATH = "F:\\MyQRCode.png";

	@Override
	public String initiateEnvoiceGeneration(int transactionNumber, int schemeId, String applicationId, int regionId) {

		/**
		 * Step 1 : Receive Transaction Number , Scheme Id and check in db to see that
		 * Invoice for Transaction number is generated already or not. Step 2 : Verify
		 * that the payment records are OK to generate invoice or not log into table to
		 * note that transactions are not OK and invoice is not generated. Step 3 :
		 * Generate JSON for generating invoice. Step 4 : Validate JSON. Step 4 : Check
		 * authentication token is valid and expired or not other wise regenerate the
		 * authentication token and save it in db. Step 5 : After authentication
		 * generate irn and qr code and save it into db.
		 **/

		try {
			int countsize = -1;
			AHCEInvoiceErrorLogDomain errorlog = null;
			JSONObject payloadJson = null;
			ahcRecogApplicationSubDomain ahcRecogDomain = ahcloiserv.getSaveAppDtlsAhcRecog(applicationId, regionId);
			if(ahcRecogDomain.getStrGstin()==null || ahcRecogDomain.getStrGstin().trim().length()==0 )
			{
				generateB2CQRCodeFeeReceipt(transactionNumber, schemeId,applicationId,regionId);
			}
			else
			{				
				if (transactionNumber != 0 && schemeId != 0) {
					countsize = einvoiceDao.getTransactionDtlsCount(transactionNumber);
					System.out.println("size of transaction dtls is" + countsize);
					String strOutputFileName="";    
		            String path="";
					path=ResourceBundleFile.getValueFromKey("JRXML_PDF_PATH");
					strOutputFileName = path+File.separator+"FeeTaxReceipt_"+transactionNumber+".pdf";
					//strOutputFileName="FeeTaxReceipt_" + transactionNumber + ".pdf";	
					if (countsize == 0) {
	
					/*	if (verifyTransactionAmount(transactionNumber, schemeId))*/ 
						if (true){
							//payloadJson = generatePayloadJSON(transactionNumber, schemeId,applicationId,regionId,strOutputFileName);
							HashMap<Object,Object> parameters = new HashMap<Object,Object>();
							parameters=generatePayloadJSON(transactionNumber, schemeId,applicationId,regionId);
							parameters.put("strOutputFileName", strOutputFileName);
							String status =doAuthentication(transactionNumber,regionId); // step1
							if(status.equalsIgnoreCase("Failure"))
							{
								return status;
							}
							boolean returnStatus=generateIRN(payloadJson, transactionNumber,parameters,regionId); // step2
							/*
							 * try { //Sending Mail
							 * //sm.TransferToMailServer(officeEmailId,"Fee Paid",emailMsgTxt);
							 * if(returnStatus) { //sm.TransferToMailServerAttachFile(
							 * "trilokbasista@yahoo.com","Tax Fee Receipt","Tax Fee Receipt"
							 * ,strOutputFileName); } } catch (MessagingException e) {
							 * sm.TransferToMailServer("ahcsupport@bis.gov.in","Tax Fee Receipt Exception",e
							 * .toString()); e.printStackTrace(); }
							 */		
						} else {	
							errorlog = setEInvoiceErrorLogData(transactionNumber, "At Process before Authentication", -1,
									"-1", "No error in Api", "Transaction Amount's didnt match");
							einvoiceDao.logEInvoiceErrorLog(errorlog);
						}
				 	}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				sm.TransferToMailServer("ahcsupport@bis.gov.in","Tax Fee Receipt Exception",e.toString());
			} catch (MessagingException e1) {
				//  Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return "Success";
	}

	public boolean verifyTransactionAmount(int transactionNumber, int schemeId) {
		System.out.println("inside verifying transaction amount method");
		boolean result = false;
		try {
			if (schemeId != 0) {

				result = einvoiceDao.checkTransactionAmountsForAHC(transactionNumber);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public AHCEInvoiceErrorLogDomain setEInvoiceErrorLogData(int transactionNumber, String strErrorOccuredAt,
			int strApiStatus, String strApiErrorCode, String strApiErrorMessage, String strExceptionorErrorReason) {
		AHCEInvoiceErrorLogDomain errorlog = new AHCEInvoiceErrorLogDomain();
		try {
			errorlog.setDtLogDate(new Date());
			errorlog.setStatus(strApiStatus);// error or exception in code
			errorlog.setStrApiErrorCode(strApiErrorCode);
			errorlog.setStrApiErrorMessage(strApiErrorMessage);
			errorlog.setStrErrorOccuredAt(strErrorOccuredAt);
			errorlog.setStrExceptionorErrorReason(strExceptionorErrorReason);
			errorlog.setTransactionNumber(String.valueOf(transactionNumber));
		} catch (Exception e) {
			try {
				sm.TransferToMailServer("ahcsupport@bis.gov.in","Tax Fee Receipt Error",errorlog.toString());
			} catch (MessagingException e1) {
				//  Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return errorlog;

	}

	@Override
	public HashMap<Object,Object> generatePayloadJSON(int transactionNumber, int schemeId, String applicationId, int regionId) {

		HashMap<Object,Object> parameters = new HashMap<Object,Object>();
		JSONObject payloadJson = new JSONObject();
			try {				
				if (schemeId == 13) {
					ahcRecogApplicationSubDomain ahcRecogDomain = ahcloiserv.getSaveAppDtlsAhcRecog(applicationId, regionId);
					List<Regional_Mst_Domain> regionalDtls = feeDao.getRegionalDetails(applicationId, regionId);
					int districtId = 0;
					int stateId = 0;
					String productId = "";
					if (regionalDtls.size() > 0) {
						districtId = regionalDtls.get(0).getNumDistrictId();
						stateId = regionalDtls.get(0).getNumStateId();
						String stateName = "";
						/*if (ahcRecogDomain.getStrRecogScope().equalsIgnoreCase("gold")) {
							productId = "1417";
						} else if (ahcRecogDomain.getStrRecogScope().equalsIgnoreCase("silver")) {
							productId = "2112";
						} else if (ahcRecogDomain.getStrRecogScope().equalsIgnoreCase("Gold & Silver Both")) {
							productId = "1417";
						}*/
						
						if (ahcRecogDomain.getStrRecogScope().toLowerCase().contains("Gold & Silver Both".toLowerCase())) {
							productId = "1417,2112";
						} else if (ahcRecogDomain.getStrRecogScope().toLowerCase().contains("Silver".toLowerCase())) {
							productId = "2112";
						} else if (ahcRecogDomain.getStrRecogScope().toLowerCase().contains("Gold".toLowerCase())) {
							productId = "1417";
						}
						Branch_Master_Domain branchMasterDomain = ahcloiserv.getBranchDetails(regionId, stateId, districtId,productId);// brachId==regionId
						List<State_Mst_Domain> stateDetail = feeDao.getstatedetails(stateId);
						if (stateDetail.size() > 0) {
							stateName = stateDetail.get(0).getStrStateName();
						}

						List<FeePaidDetailsModel> feePaidDetailsModelList = feeDao.getPaidFeeDetailsAHC(String.valueOf(transactionNumber));
						List<PaymentDetailsDomain_AHC> paymentDetailsDomainAHC = feeDao.getPaymentDetails(String.valueOf(transactionNumber));
						EinvoicingGSTCredentailsDomain gstCredentailDomain = new EinvoicingGSTCredentailsDomain();
						gstCredentailDomain = einvoiceDao.getGSTCredentails(regionId);
						String branchGSTNumber="";
						if(gstCredentailDomain!=null)
						{
							branchGSTNumber=gstCredentailDomain.getGstNumber();
						}
						parameters.put("branchAddress1", branchMasterDomain.getAddress1());
						parameters.put("branchAddress2", branchMasterDomain.getAddress2());
						parameters.put("branchCity", branchMasterDomain.getStrCity());
						parameters.put("branchPincode", branchMasterDomain.getNumPincode());
						parameters.put("branchStateCode", branchMasterDomain.getNumStateId());
						parameters.put("branchStateName", stateName);
						parameters.put("branchContact", branchMasterDomain.getStrBranchContact());
						parameters.put("branchFax", branchMasterDomain.getStrBranchFax());
						parameters.put("branchEmail", branchMasterDomain.getStrBranchMail());
						parameters.put("branchPan", branchMasterDomain.getPanNumber());
						parameters.put("branchGST", branchGSTNumber);
						parameters.put("branchSAC", "998349");
						parameters.put("branchCountry", "India");
						parameters.put("invoiceNo", feePaidDetailsModelList.get(0).getReceiptNumber());
						parameters.put("transactionNo", String.valueOf(transactionNumber));
						parameters.put("recognitionNumber", 0);
						parameters.put("applicationId", ahcRecogDomain.getStrAppId());
						String paymentStatus = "Not Available";
						if (paymentDetailsDomainAHC.get(0).getPayment_status().equalsIgnoreCase("0300")) {
							paymentStatus = "Payment Success";
						} else if (paymentDetailsDomainAHC.get(0).getPayment_status().equalsIgnoreCase("0399")) {
							paymentStatus = "Cancelled by User";
						} else if (paymentDetailsDomainAHC.get(0).getPayment_status().equalsIgnoreCase("0002")) {
							paymentStatus = "Unable to Process";
						} else if (paymentDetailsDomainAHC.get(0).getPayment_status().equalsIgnoreCase("NA")) {
							paymentStatus = "Not Available";
						}
						parameters.put("paymentStatus", paymentStatus);
						parameters.put("ahcName", ahcRecogDomain.getStrCenterName());
						parameters.put("ahcAddress1", ahcRecogDomain.getStrOffAddr1());
						parameters.put("ahcAddress2", ahcRecogDomain.getStrOffAddr2());
						parameters.put("ahcCityName", ahcloiserv.getCityName(ahcRecogDomain.getOffCity()));
						parameters.put("ahcDistrict",feeDao.getdistrictdetails(ahcRecogDomain.getNumOffDistrictId()).get(0).getDistrictname());
						parameters.put("ahcCountry",ahcloiserv.getCountryById(ahcRecogDomain.getNumOffCountryId()).get(0).getStrCountryName());
						parameters.put("ahcState",feeDao.getstatedetails(ahcRecogDomain.getNumOffStateId()).get(0).getStrStateName());
						parameters.put("ahcStateCode", ahcRecogDomain.getNumOffStateId());
						parameters.put("ahcPincode", ahcRecogDomain.getAhcPincode());
						parameters.put("ahcContactDetails", ahcRecogDomain.getStrOffNo());
						parameters.put("ahcEmail", ahcRecogDomain.getStrOffmail());
						parameters.put("receiptDate", feePaidDetailsModelList.get(0).getFeeReceiptDate());
						double totalAmount = 0.0;
						for (FeePaidDetailsModel tempObj : feePaidDetailsModelList) {
							totalAmount += tempObj.getFeeAmount();
						}
						parameters.put("totalAmount", totalAmount);
						parameters.put("amountInNumber", feeDao.getTotalInWords(totalAmount));
						parameters.put("feePaidDetailsDS", new JRBeanArrayDataSource(feePaidDetailsModelList.toArray()));
						parameters.put("LicenceOrApplication", "Application No.");
						parameters.put("feePaidDetails", feePaidDetailsModelList);
						parameters.put("applicantGSTN", ahcRecogDomain.getStrGstin());
					}

				}
				
			} catch (Exception e) {
				System.out.println("inside exception of generate output file call");
				e.printStackTrace();
			}


				payloadJson = createJSON(parameters,transactionNumber);
				parameters.put("payloadJson",payloadJson);
			return parameters;
	}

	public JSONObject createJSON(HashMap<Object,Object> parameters, int transactionNumber) {
		JSONObject jsonPayload = new JSONObject();
		String Date = dtformatter.format(new Date());// this is present date
		try {			
				JSONObject TranDtls = new JSONObject();				
				TranDtls.put("TaxSch", "GST");
				if(parameters.get("applicantGSTN")==null || parameters.get("applicantGSTN").toString().length()==0)
				{
					TranDtls.put("SupTyp", "B2B");
				}else				
				{
					TranDtls.put("SupTyp", "B2C");	
				}

				JSONObject DocDtls = new JSONObject();
				DocDtls.put("Typ", "INV");
				DocDtls.put("No", parameters.get("invoiceNo").toString().substring(3));
				String[] receiptDate =parameters.get("receiptDate").toString().substring(0,10).split("-");
				DocDtls.put("Dt",(receiptDate[2].concat("/").concat(receiptDate[1].concat("/").concat(receiptDate[0]))));

				JSONObject SellerDtls = new JSONObject();
				SellerDtls.put("Gstin", parameters.get("branchGST").toString());
				//SellerDtls.put("Gstin", "29AABCI2764F000");
				SellerDtls.put("LglNm", "BUREAU OF INDIAN STANDARDS");
				SellerDtls.put("Addr1",  parameters.get("branchAddress1").toString()+""+parameters.get("branchAddress2").toString());
				SellerDtls.put("Loc", parameters.get("branchCity").toString().trim().length()==0?feeDao.getstatedetails(Integer.parseInt(parameters.get("branchStateCode").toString())).get(0).getStrStateName():parameters.get("branchCity").toString());
				SellerDtls.put("Pin", Integer.parseInt(parameters.get("branchPincode").toString()));
				//SellerDtls.put("Pin", 560007);
				SellerDtls.put("Stcd", feeDao.getstatedetails(Integer.parseInt(parameters.get("branchStateCode").toString())).get(0).getStrStateCode());
				//SellerDtls.put("Stcd", "29");
				//SellerDtls.put("Ph",parameters.get("branchContact").toString());
				//SellerDtls.put("Em",parameters.get("branchEmail").toString());

				JSONObject BuyerDtls = new JSONObject();
				if(parameters.get("applicantGSTN")==null || parameters.get("applicantGSTN").toString().length()==0)
				{
					BuyerDtls.put("Gstin", "");
				}else
				{
					BuyerDtls.put("Gstin", parameters.get("applicantGSTN").toString());
				}
				//BuyerDtls.put("Gstin", "33AABCI2764F003");
				BuyerDtls.put("LglNm", parameters.get("ahcName").toString());
				BuyerDtls.put("Pos", feeDao.getstatedetails(Integer.parseInt(parameters.get("ahcStateCode").toString())).get(0).getStrStateCode());
				BuyerDtls.put("Addr1", parameters.get("ahcAddress1").toString()+""+parameters.get("ahcAddress2").toString());
				BuyerDtls.put("Loc", parameters.get("ahcCityName").toString().trim().length()==0?feeDao.getstatedetails(Integer.parseInt(parameters.get("ahcStateCode").toString())).get(0).getStrStateName():parameters.get("ahcCityName").toString());
				BuyerDtls.put("Pin", Integer.parseInt(parameters.get("ahcPincode").toString()));
				//BuyerDtls.put("Pin",600001);
				BuyerDtls.put("Stcd",feeDao.getstatedetails(Integer.parseInt(parameters.get("ahcStateCode").toString())).get(0).getStrStateCode());
				//BuyerDtls.put("Stcd","33");
				BuyerDtls.put("Ph", parameters.get("ahcContactDetails").toString());				
				BuyerDtls.put("Em", parameters.get("ahcEmail").toString());				
				int stateCode = Integer.parseInt(parameters.get("ahcStateCode").toString());
				List<FeePaidDetailsModel> feePaidDetailsModelList = new ArrayList<FeePaidDetailsModel>();
				feePaidDetailsModelList= (List<FeePaidDetailsModel>) parameters.get("feePaidDetails");
				boolean isIGST=false, isCGST=false,isSGST;
				double igstRate=0,cgstRate=0,sgstRate=0;
				double igstAmount=0,cgstAmount=0,sgstAmount=0;
				double totalAmountWithoutTax = 0.0;
				for (FeePaidDetailsModel tempObj : feePaidDetailsModelList) {
					if(tempObj.getFeeType().equalsIgnoreCase("Rs."))
					{						
						totalAmountWithoutTax += tempObj.getFeeAmount();						
					}
					if(tempObj.getFeeId()==48)//CGST
					{
						isCGST=true;
						cgstAmount= (double) tempObj.getFeeAmount();
						List<GSTMasterDomain> gstRate = feeDao.getGSTFee(48,stateCode);
						cgstRate= gstRate.get(0).getTaxRate();
					}
					if(tempObj.getFeeId()==49)//SGST
					{
						isSGST=true;
						sgstAmount= (double) tempObj.getFeeAmount();
						List<GSTMasterDomain> gstRate = feeDao.getGSTFee(49,stateCode);
						sgstRate=gstRate.get(0).getTaxRate();
					}
					if(tempObj.getFeeId()==51)//IGST
					{
						isIGST=true;
						igstAmount= (double) tempObj.getFeeAmount();
						List<GSTMasterDomain> gstRate = feeDao.getGSTFee(51,stateCode);
						igstRate=gstRate.get(0).getTaxRate();
					}
				}
				
				double gstRate= cgstRate+sgstRate+igstRate;
				JSONArray ItemList = new JSONArray();
				JSONObject itemlistSubMap = new JSONObject();
				itemlistSubMap.put("SlNo", "1");
				itemlistSubMap.put("IsServc", "Y");
				itemlistSubMap.put("HsnCd", "998349");
				itemlistSubMap.put("Qty", 1);
				itemlistSubMap.put("Unit", "NOS");
				itemlistSubMap.put("UnitPrice", totalAmountWithoutTax);
				itemlistSubMap.put("TotAmt", totalAmountWithoutTax);
				itemlistSubMap.put("AssAmt", totalAmountWithoutTax);
				itemlistSubMap.put("GstRt", gstRate);
				//itemlistSubMap.put("IgstAmt", cgstAmount+sgstAmount);//For Testing on UAT Only
				itemlistSubMap.put("IgstAmt", igstAmount);
				itemlistSubMap.put("CgstAmt", cgstAmount);
				itemlistSubMap.put("SgstAmt", sgstAmount);
				//itemlistSubMap.put("CgstAmt", 0);
				//itemlistSubMap.put("SgstAmt", 0);
				itemlistSubMap.put("TotItemVal", Double.parseDouble(parameters.get("totalAmount").toString()));
				ItemList.put(itemlistSubMap);

				JSONObject ValDtls = new JSONObject();

				ValDtls.put("AssVal",totalAmountWithoutTax);
				ValDtls.put("IgstVal", igstAmount);
				ValDtls.put("CgstVal", cgstAmount);
				ValDtls.put("SgstVal", sgstAmount);
				//ValDtls.put("IgstVal", cgstAmount+sgstAmount);
				ValDtls.put("CesVal", 0);
				// ValDtls.put("TotInvVal",
				// transactionDetailsList.get(0).get("num_total_amount").toString().trim());
				ValDtls.put("TotInvVal", Double.parseDouble(parameters.get("totalAmount").toString()));

				jsonPayload.put("Version", "1.1");
				jsonPayload.put("TranDtls", TranDtls);
				jsonPayload.put("DocDtls", DocDtls);

				jsonPayload.put("SellerDtls", SellerDtls);
				jsonPayload.put("BuyerDtls", BuyerDtls);
				jsonPayload.put("ItemList", ItemList);
				jsonPayload.put("ValDtls", ValDtls);

				System.out.println("json is " + jsonPayload.toString());			

		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonPayload;
	}

	@Override
	public String doAuthentication(int transactionNumber, int regionId) {

		PublicKey public_key = null;
		String encryptedPassword = "";
		StringBuilder responseData = new StringBuilder();
		Map authOutputJson = new HashMap();
		JSONObject finalJsonObject = null;
		Gson gson = new Gson();
		AHCEInvoiceAuthenticationDomain eIAD = new AHCEInvoiceAuthenticationDomain();
		String returnString = "";
		String gstUserName="", gstUserPassword="";
		String userGSTINNumber="";
		if (true) {
			try {
				EinvoicingGSTCredentailsDomain gstCredentailDomain = new EinvoicingGSTCredentailsDomain();
				gstCredentailDomain = einvoiceDao.getGSTCredentails(regionId);
				if(gstCredentailDomain.getUserId().trim().length()==0)
				{
					return "Failure";
				}
				gstUserName=gstCredentailDomain.getUserId();
				gstUserPassword=gstCredentailDomain.getGstPassword();
				userGSTINNumber= gstCredentailDomain.getGstNumber();
				String AuthURL = AHCEInvoiceUtilProp.getValueFromKey("AUTH_URL").trim();

				HttpURLConnection con = (HttpURLConnection) new URL(AuthURL).openConnection();

				System.out.println("setting request properties started");

				con.setRequestProperty("client-id", clientId);
				con.setRequestProperty("client-secret", clientSecret);
				con.setRequestProperty("emailid", emailid);
				con.setRequestProperty("Gstin", userGSTINNumber);
				con.setRequestMethod("POST"); // can set GET, POST, HEAD
				con.setRequestProperty("Content-Type", "application/json; utf-8");
				con.setRequestProperty("Accept", "application/json");
				con.setRequestProperty("Content-Language", "en-US");
				con.setUseCaches(false);
				con.setDoOutput(true);

				System.out.println("setting request properties completed");

				try {

					File publicKeyFile = new File(AHCEInvoiceUtilProp.getValueFromKey("PUBLIC_KEY_FILE_PATH").trim());
					public_key = AHCEInvoiceUtil.loadPublicKeyFromFile(publicKeyFile);
					//encryptedPassword = AHCEInvoiceUtil.Encrypt("Imageinfo@2308", public_key);
					encryptedPassword = AHCEInvoiceUtil.Encrypt(gstUserPassword, public_key);

				} catch (Exception e) {
					e.printStackTrace();
				}

				byte[] appKeyInBytes = null;

				String appKey = AHCEInvoiceUtilProp.getValueFromKey("APP_KEY").trim();
				// AHCEInvoiceUtil.createAESKey().toString(); // or generate app key with the
				// method

				try {

					appKeyInBytes = AHCEInvoiceUtil.decodeBase64StringTOByte(appKey);

				} catch (Exception e) {

					e.printStackTrace();

				}

				String appKeyEncryptedAndCoded = "";
				try {

					appKeyEncryptedAndCoded = AHCEInvoiceUtil.encrypt(appKeyInBytes, public_key);

				} catch (Exception e) {

					e.printStackTrace();

				}

				JSONObject data = new JSONObject();
				//data.put("UserName", "anandnat1");
				data.put("UserName", gstUserName);
				data.put("ForceRefreshAccessToken", true);
				//data.put("Password", encryptedPassword);
				//data.put("AppKey", appKeyEncryptedAndCoded);
				data.put("Password", gstUserPassword);
				data.put("AppKey", appKey);
				byte[] dataByteArray=data.toString().getBytes();
				String base64ConversionOfData=Base64.encodeBase64String(dataByteArray);
				System.out.println("JSON data: " + data.toString());
				String dataJsonEncryptedAndCoded = "";
				try {
					dataJsonEncryptedAndCoded = AHCEInvoiceUtil.encrypt(base64ConversionOfData.getBytes(), public_key);
				} catch (Exception e) {
					e.printStackTrace();
				}

				
				String finalJSONData = "{\"Data\":\"" + dataJsonEncryptedAndCoded + "\"}";
				System.out.println("finalJSONData:=="+finalJSONData);
				try (OutputStream os = con.getOutputStream()) {
					byte[] input = finalJSONData.getBytes("UTF-8");
					os.write(input, 0, input.length);
				} catch (Exception e) {
					e.printStackTrace();
				}

				try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"))) {

					String responseLine = null;
					while ((responseLine = br.readLine()) != null) {
						responseData.append(responseLine.trim());
					}
					System.out.println(responseData.toString());

					authOutputJson = gson.fromJson(responseData.toString(), Map.class);
					if (authOutputJson.get("Status").equals(1.0)) {
						returnString = "Success";
					} else {
						returnString = "Failure";
					}
					finalJsonObject = new JSONObject(responseData.toString());

					// setting authtoken to session
					//userSession.setAuthJSONMap(authOutputJson);
					AHCEinvoiceAuthJsonDataDomain ahcEinvoiceAuthJsonDataDomain = new AHCEinvoiceAuthJsonDataDomain();
					ahcEinvoiceAuthJsonDataDomain.setJsonData(responseData.toString());
					ahcEinvoiceAuthJsonDataDomain.setTransactionNumber(transactionNumber);
					ahcEinvoiceAuthJsonDataDomain.setEntryDate(new Date());
					einvoiceDao.saveAuthJsonResponse(ahcEinvoiceAuthJsonDataDomain);
					// save to db
					eIAD = convertAuthJSONmain(finalJsonObject, appKey, gstUserName);
					einvoiceDao.saveAuthAPIResponse(eIAD);

				} catch (Exception e) {
					returnString = "Exception";
					e.printStackTrace();
				}

			} catch (Exception e) {
				returnString = "Exception";
				e.printStackTrace();
			}
		}

		return returnString;
	}

	public AHCEInvoiceAuthenticationDomain convertAuthJSONmain(JSONObject finalJsonObject, String appKey, String gstUserName) {
		AHCEInvoiceAuthenticationDomain eIAD = new AHCEInvoiceAuthenticationDomain();
		try {
			JSONObject data = new JSONObject(finalJsonObject.get("Data").toString());
			eIAD.setClientId(clientId);
			eIAD.setCreate_date(new Date());
			eIAD.setStrAPIResponsePayloadJSON(finalJsonObject.toString());
			eIAD.setUserName(gstUserName);
			eIAD.setAppKey(appKey);
			eIAD.setStatus(Integer.parseInt(finalJsonObject.get("Status").toString()));
			eIAD.setAuthToken(data.get("AuthToken").toString());
			eIAD.setSek(data.get("Sek").toString());
			eIAD.setTokenExpiry(data.get("TokenExpiry").toString());
		} catch (NumberFormatException | JSONException e) {
			e.printStackTrace();
		}
		return eIAD;

	}

	@Override
	public boolean generateIRN(JSONObject payloadPlainJson, int transactionNumber,HashMap<Object,Object> parameters, int regionId) {
		
		boolean functionReturnStatus=false;
		StringBuilder responseData = new StringBuilder();
		AHCEInvoiceIRNDtlsDomain eIRNDD = new AHCEInvoiceIRNDtlsDomain();
		Connection _conn= null;
		String strFormat = "";		
		String strJasperPath = jrxmlpath;
		parameters.put("Transact_Id", transactionNumber + "");
		strJasperPath = strJasperPath + "AHCPaymentTaxFeeReceipt.jasper";
		String output_result = "";
		//HttpSession httpsession = request.getSession(false);
		//Session userSession = (Session) httpsession.getAttribute("logged-in");

		if (true) {
			try {

				String AuthURL = AHCEInvoiceUtilProp.getValueFromKey("Generate_IRN_URL").trim();
				//String GSTIN = AHCEInvoiceUtilProp.getValueFromKey("GSTIN").trim();
				EinvoicingGSTCredentailsDomain gstCredentailDomain = new EinvoicingGSTCredentailsDomain();
				gstCredentailDomain = einvoiceDao.getGSTCredentails(regionId);
				if(gstCredentailDomain.getUserId().trim().length()==0)
				{
					//Do Nothing
				}else
					{
					String gstUserName=gstCredentailDomain.getUserId();
					AHCEinvoiceAuthJsonDataDomain ahcEinvoiceAuthJsonDataDomain=einvoiceDao.getAuthJsonResponse(transactionNumber);
					Gson gson = new Gson();
					//JSONObject jsonObject = new JSONObject(ahcEinvoiceAuthJsonDataDomain.getJsonData());
					Map authJSONMap = gson.fromJson(ahcEinvoiceAuthJsonDataDomain.getJsonData(),Map.class);
					//Object authData = userSession.getAuthJSONMap().get("Data");
					Object authData = authJSONMap.get("Data");
	
					Map authTokenMap = (Map) authData;
					String authToken = authTokenMap.get("AuthToken").toString();
					String Sek = authTokenMap.get("Sek").toString();
	
					System.out.println("Auth token is " + authToken);
					System.out.println("sek is " + Sek);
	
					HttpURLConnection con = (HttpURLConnection) new URL(AuthURL).openConnection();
	
					System.out.println("setting request properties started");
	
					con.setRequestProperty("client-id", clientId);
					con.setRequestProperty("client-secret", clientSecret);
					con.setRequestProperty("emailid", emailid);
					con.setRequestProperty("Gstin", gstCredentailDomain.getGstNumber());
					con.setRequestProperty("user_name", gstUserName);
					con.setRequestProperty("AuthToken", authToken);
	
					con.setRequestMethod("POST");
					con.setRequestProperty("Content-Type", "application/json; utf-8");
					con.setRequestProperty("Accept", "application/json");
					con.setRequestProperty("Content-Language", "en-US");
					con.setUseCaches(false);
					con.setDoOutput(true);
	
					System.out.println("setting request properties completed");
	
					byte[] appKeyInBytes = null;
	
					String appKey = AHCEInvoiceUtilProp.getValueFromKey("APP_KEY").trim();
					// AHCEInvoiceUtil.createAESKey().toString(); // or generate app key with the
					// method
	
					try {
	
						appKeyInBytes = AHCEInvoiceUtil.decodeBase64StringTOByte(appKey);
	
					} catch (Exception e) {
	
						e.printStackTrace();
	
					}
	
					String AuthSek = AHCEInvoiceUtil.decrptyBySyymetricKey(Sek, appKeyInBytes);
	
					// generate json and encrypted in
					// String payloadPlainJson= generateIRNPayloadJson();
					String payloadPlainJsontring = parameters.get("payloadJson").toString();
					String encryptedPayload = AHCEInvoiceUtil.encryptBySymmetricKey(payloadPlainJsontring, AuthSek);
	
					JSONObject data = new JSONObject();
					data.put("Data", encryptedPayload);
					// System.out.println("JSON data: "+data.toString());
					String finalJSONData = data.toString();
					System.out.println("finalJSONData   " + finalJSONData);
	
					try (OutputStream os = con.getOutputStream()) {
						byte[] input = finalJSONData.getBytes("UTF-8");
						os.write(input, 0, input.length);
					} catch (Exception e) {
						e.printStackTrace();
					}
	
					try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"))) {
	
						String responseLine = null;
						while ((responseLine = br.readLine()) != null) {
							responseData.append(responseLine.trim());
						}
						System.out.println(responseData.toString());
					} catch (Exception e) {
						e.printStackTrace();
					}
	
					final JSONObject objGSTIRN = new JSONObject(responseData.toString());
					
					if(Integer.parseInt(objGSTIRN.get("Status").toString())==1)
						{
							functionReturnStatus=true;	
							String dataInResponse = AHCEInvoiceUtil.decryptBySymmentricKey(objGSTIRN.get("Data").toString(), AuthSek);
							System.out.println("dataInResponse     " + dataInResponse);
			
							JSONObject decodedData = new JSONObject(dataInResponse);
			
							byte[] encodedQRCode = decodeSignedJWT(decodedData.get("SignedQRCode").toString());
							
							eIRNDD = convertIRNJSONmain(decodedData, payloadPlainJsontring, objGSTIRN, transactionNumber,authTokenMap, appKey, encodedQRCode);
							einvoiceDao.saveIRNAPIResponseData(eIRNDD);
							JSONObject responseDataJson = new JSONObject(dataInResponse);
							parameters.put("irnNumber", responseDataJson.get("Irn"));
							parameters.put("AckNo", Long.valueOf(responseDataJson.get("AckNo").toString()));
							parameters.put("AckDt", responseDataJson.get("AckDt"));
							parameters.put("SignedQRCode", responseDataJson.get("SignedQRCode"));
							System.out.println("Final Status=" + output_result);
						}
					else
						{
							JSONObject errorCodeJsonObject = new JSONObject(objGSTIRN.get("ErrorDetails").toString().replaceAll("\\[", "").replaceAll("\\]", ""));
							AHCEInvoiceErrorLogDomain errorlog=setEInvoiceErrorLogData(transactionNumber, "Generate IRN API Response",
									Integer.parseInt(objGSTIRN.get("Status").toString()),errorCodeJsonObject.get("ErrorCode").toString(),errorCodeJsonObject.get("ErrorMessage").toString(), objGSTIRN.get("InfoDtls").toString());
							einvoiceDao.logEInvoiceErrorLog(errorlog);
						}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		String strOutputFileName = String.valueOf(parameters.get("strOutputFileName"));
		//genearatePDFFile(_conn, strJasperPath, parameters, strOutputFileName,"pdf");
		return functionReturnStatus;
	}
	
	public void genearatePDFFile(String strJasperPath,Map parameters,String strOutputFilePath,String strReportFormat)
	{
		
		try {
			String result = generateOutputFileAHC(strJasperPath, parameters, strOutputFilePath,"pdf");
			System.out.println("Finasl Result of AHC PDF Generation="+result);
		} catch (Exception e) {
			//  Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public AHCEInvoiceIRNDtlsDomain convertIRNJSONmain(JSONObject decodedResponseJSON, String plainRequestJSON,
			JSONObject originalResponseJSON, int transactionNumber, Map authTokenMap, String appKey, byte[] qrcode) {
		AHCEInvoiceIRNDtlsDomain eIRNDD = new AHCEInvoiceIRNDtlsDomain();
		try {
			eIRNDD.setDtEntryDate(new Date());
			eIRNDD.setIsValid(1);
			eIRNDD.setStrAckDt(decodedResponseJSON.get("AckDt").toString());
			eIRNDD.setStrAckNo(decodedResponseJSON.get("AckNo").toString());
			eIRNDD.setStrApiRequestPayloadJSON(plainRequestJSON.toString());
			eIRNDD.setStrAPIResponsePayloadJSON(decodedResponseJSON.toString());
			eIRNDD.setStrApiStatus(Integer.parseInt(originalResponseJSON.get("Status").toString()));
			eIRNDD.setStrIRN(decodedResponseJSON.get("Irn").toString());
			eIRNDD.setStrSignedInvoice(decodedResponseJSON.get("Irn").toString());
			eIRNDD.setStrSignedQRCode(decodedResponseJSON.get("Irn").toString());
			eIRNDD.setStrStatus(decodedResponseJSON.get("Status").toString());
			eIRNDD.setStrAuthToken(authTokenMap.get("AuthToken")==null?"":authTokenMap.get("AuthToken").toString());
			eIRNDD.setStrSek(authTokenMap.get("Sek").toString());
			eIRNDD.setStrTokenExpiry(authTokenMap.get("TokenExpiry").toString());
			eIRNDD.setTransactionNumber(transactionNumber);
			eIRNDD.setStrAppKey(appKey);
			eIRNDD.setQrcode(qrcode);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return eIRNDD;
	}

	private byte[] decodeSignedJWT(String signedText) {
		byte[] encodedQRCode = null;
		try {

			BASE64Decoder decoder = new BASE64Decoder();
			String[] splitSignedText = signedText.split("\\.");
			String decodedSigned = new String(decoder.decodeBuffer(splitSignedText[0]));
			String decodedContent = new String(decoder.decodeBuffer(splitSignedText[1]));

			decodedContent = decodedContent.replaceAll("\\\"", "\"");

			decodedSigned = decodedSigned + "\n Content:" + decodedContent;
			decodedSigned.replaceAll("\\\"", "\"");
			System.out.println("\nDecoded Text:" + decodedSigned);

			System.out.println("decode content is :" + decodedContent);

			JSONObject data = new JSONObject(decodedContent);
			System.out.println("json data is " + data.get("data").toString());

			encodedQRCode = createQRCode(data.get("data").toString());
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return encodedQRCode;
	}

	public byte[] createQRCode(String jsonString) {
		byte[] qrcodeinBytes = null;
		String base64EncodedQrCode = "";
		try {
			try {
				qrcodeinBytes = AHCQRCodeUtil.getQRCodeImage(jsonString, 350, 350);
			} catch (com.google.zxing.WriterException e) {
				//  Auto-generated catch block
				e.printStackTrace();
			}
		}
		catch (IOException e) {
			System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
		}
		return qrcodeinBytes;
	}

	@Override
	public String generateOutputFileAHC(String strJasperPath,Map parameters,String strOutputFilePath,String strReportFormat) throws Exception
	{
		String strStatus = "";
		// System.out.println("outputfilepath+File.separator------ :
		// "+pdf_Path+File.separator+strOutputFilePath);
		// System.out.println("pdf_Path------ : "+pdf_Path);

		// System.out.println("strOutputFilePath"+strOutputFilePath);
		try {
			parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.FALSE);
			if (strReportFormat.equalsIgnoreCase("html"))
				parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);
			JasperPrint printFileName = JasperFillManager.fillReport(strJasperPath, parameters,
					new JREmptyDataSource());

			JRExporter exporter = null;
			if (printFileName != null) {
				if (strReportFormat.equalsIgnoreCase("docx")) {

					exporter = new JRDocxExporter();
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, printFileName);
					exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, strOutputFilePath);
				} else if (strReportFormat.equalsIgnoreCase("pdf")) {

					JRFileVirtualizer fileVirtualizer = new JRFileVirtualizer(10);
					exporter = new JRPdfExporter();
					// parameters.put(JRParameter.REPORT_VIRTUALIZER, fileVirtualizer);
					// System.out.println(" ----- - strReportFormat.equalsIgnoreCase(pdf)");
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, printFileName);
					exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, strOutputFilePath);
					// exporter.setParameter(JRPdfExporterParameter.PAGE_INDEX, true);

					exporter.exportReport();

					// System.out.println("going to send file on ftp ");
					byte[] content = null;
					Path path = Paths.get(strOutputFilePath);
					content = Files.readAllBytes(path);
					String name = "jrxml.pdf";
					String originalFileName = "jrxml.pdf";
					String contentType = "application/pdf";
					MultipartFile multipartFile = new MockMultipartFile(name, originalFileName, contentType, content);

					// replace last forward slash (/) with backword slash (\);
					StringBuilder outputFilePath = new StringBuilder(strOutputFilePath);
					outputFilePath = outputFilePath.replace(strOutputFilePath.lastIndexOf("/"),
							strOutputFilePath.lastIndexOf("/") + 1, File.separator);
					// String strPath = strFilePath.substring(strFilePath.indexOf("/")+1,
					// strFilePath.lastIndexOf(File.separator));
					System.out.println("outputFilePath AHC QR Code - -- -" + outputFilePath);

					System.out.println("placing files on ftp");
					ftpFiles.upload(outputFilePath.toString(), multipartFile); // place file on ftp server

				} else if (strReportFormat.equalsIgnoreCase("html")) {
					HttpServletResponse response = null;

					exporter = new JRHtmlExporter();

					exporter.setParameter(JRExporterParameter.JASPER_PRINT, printFileName);
					exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, strOutputFilePath);
					exporter.setParameter(JRHtmlExporterParameter.FLUSH_OUTPUT, true);
					exporter.setParameter(JRHtmlExporterParameter.FRAMES_AS_NESTED_TABLES, true);
					exporter.setParameter(JRHtmlExporterParameter.IS_WHITE_PAGE_BACKGROUND, true);
					exporter.setParameter(JRHtmlExporterParameter.IS_WRAP_BREAK_WORD, true);
					exporter.setParameter(JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, false);
					exporter.setParameter(JRHtmlExporterParameter.IGNORE_PAGE_MARGINS, true);
					exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, false);
					exporter.setParameter(JRHtmlExporterParameter.ZOOM_RATIO, 1.5F);
					exporter.setParameter(JRHtmlExporterParameter.CHARACTER_ENCODING, "UTF8");
				} else if (strReportFormat.equalsIgnoreCase("xls")) {
					exporter = new JRXlsExporter();
					exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, printFileName);
					exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
					exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
					exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, true);
					exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
					exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
					exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, true);
					exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, strOutputFilePath);
				}
				exporter.exportReport();

				strStatus = "File Created Successfully";
			}

		} catch (Exception e) {
			System.out.println("Inside Exception close");
			e.printStackTrace();
			strStatus = "Error in creating ";
		}

		finally {
			parameters.clear();
			// System.out.println("Final Status="+strStatus);

		}
		return strStatus;
	}
	
	@Override
	public String cancelIRN(int transactionNumber, int regionId)
	{
		String status="Success";
		try {
		StringBuilder responseData = new StringBuilder();
		AHCEInvoiceIRNDtlsDomain eIRNDD = new AHCEInvoiceIRNDtlsDomain();
		String cancelIRNURL = AHCEInvoiceUtilProp.getValueFromKey("Cancel_IRN_URL").trim();
		EinvoicingGSTCredentailsDomain gstCredentailDomain = new EinvoicingGSTCredentailsDomain();
		//regionId=8;
		gstCredentailDomain = einvoiceDao.getGSTCredentails(regionId);
		status =doAuthentication(transactionNumber,regionId); // step1
		if(status.equalsIgnoreCase("Failure"))
		{
			return status;
		}
		if(gstCredentailDomain.getUserId().trim().length()==0)
		{
			//Do Nothing
		}else
			{
				eIRNDD= einvoiceDao.getIRNDetails(transactionNumber);
				/* if(eIRNDD.getTransactionNumber()!=0) */
				 if(true) 
				{
					String gstUserName=gstCredentailDomain.getUserId();
					AHCEinvoiceAuthJsonDataDomain ahcEinvoiceAuthJsonDataDomain=einvoiceDao.getAuthJsonResponse(transactionNumber);
					Gson gson = new Gson();
					Map authJSONMap = gson.fromJson(ahcEinvoiceAuthJsonDataDomain.getJsonData(),Map.class);
					Object authData = authJSONMap.get("Data");
					Map authTokenMap = (Map) authData;
					String authToken = authTokenMap.get("AuthToken").toString();
					String Sek = authTokenMap.get("Sek").toString();
	
					System.out.println("Auth token is " + authToken);
					System.out.println("sek is " + Sek);
					
					HttpURLConnection con = (HttpURLConnection) new URL(cancelIRNURL).openConnection();
					
					System.out.println("setting request properties started");
	
					con.setRequestProperty("client-id", clientId);
					con.setRequestProperty("client-secret", clientSecret);
					con.setRequestProperty("emailid", emailid);
					con.setRequestProperty("Gstin", gstCredentailDomain.getGstNumber());
					//con.setRequestProperty("Gstin", "29AABCI2764F000");
					con.setRequestProperty("user_name", gstUserName);
					//con.setRequestProperty("user_name", "anandnat1");
					con.setRequestProperty("AuthToken", authToken);
	
					con.setRequestMethod("POST");
					con.setRequestProperty("Content-Type", "application/json; utf-8");
					con.setRequestProperty("Accept", "application/json");
					con.setRequestProperty("Content-Language", "en-US");
					con.setUseCaches(false);
					con.setDoOutput(true);
					
					byte[] appKeyInBytes = null;
					
					String appKey = AHCEInvoiceUtilProp.getValueFromKey("APP_KEY").trim();
					
					try {
						
						appKeyInBytes = AHCEInvoiceUtil.decodeBase64StringTOByte(appKey);
	
					} catch (Exception e) {
						status="Exception";
						e.printStackTrace();
	
					}
					
					String AuthSek = AHCEInvoiceUtil.decrptyBySyymetricKey(Sek, appKeyInBytes);
					JSONObject payloadPlainJsontring = new JSONObject();
					String IRNNumber = eIRNDD.getStrIRN();
					payloadPlainJsontring.put("Irn", IRNNumber);
					payloadPlainJsontring.put("CnlRsn", "2"); //"description": "Cancel Reason 1- Duplicate, 2 - Data entry mistake, 3- Order Cancelled, 4 - Others"
					payloadPlainJsontring.put("CnlRem", "Wrong entry");//"description": "Cancel Remarks"
					
					String encryptedPayload = AHCEInvoiceUtil.encryptBySymmetricKey(payloadPlainJsontring.toString(), AuthSek);
					JSONObject data = new JSONObject();
					data.put("Data", encryptedPayload);
					String finalJSONData = data.toString();
					System.out.println("finalJSONData   " + finalJSONData);
					
					try (OutputStream os = con.getOutputStream()) {
						byte[] input = finalJSONData.getBytes("UTF-8");
						os.write(input, 0, input.length);
					} catch (Exception e) {
						e.printStackTrace();
					}
	
					try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"))) {
	
						String responseLine = null;
						while ((responseLine = br.readLine()) != null) {
							responseData.append(responseLine.trim());
						}
						System.out.println(responseData.toString());
					} catch (Exception e) {
						e.printStackTrace();
					}
	
					final JSONObject objGSTIRN = new JSONObject(responseData.toString());
					if(Integer.parseInt(objGSTIRN.get("Status").toString())==1)
					{
						String dataInResponse = AHCEInvoiceUtil.decryptBySymmentricKey(objGSTIRN.get("Data").toString(), AuthSek);
						System.out.println("dataInResponse     " + dataInResponse);
						JSONObject decodedData = new JSONObject(dataInResponse);
						
						AHCEInvoiceCancelIRNDomain ahcEInvoiceCancelIRNDomain= new AHCEInvoiceCancelIRNDomain();
						ahcEInvoiceCancelIRNDomain.setApiRequestPayloadJson(finalJSONData);
						ahcEInvoiceCancelIRNDomain.setApiResponsePayloadJson(responseData.toString());
						ahcEInvoiceCancelIRNDomain.setApiStatus(Integer.parseInt(objGSTIRN.get("Status").toString()));
						ahcEInvoiceCancelIRNDomain.setAuthToken(authTokenMap.get("AuthToken").toString());
						ahcEInvoiceCancelIRNDomain.setEntryDate(new Date());
						ahcEInvoiceCancelIRNDomain.setIrnNumber(IRNNumber);
						ahcEInvoiceCancelIRNDomain.setRemarks("");
						ahcEInvoiceCancelIRNDomain.setSek(Sek);
						ahcEInvoiceCancelIRNDomain.setTokenExpiary(authTokenMap.get("TokenExpiry").toString());
						ahcEInvoiceCancelIRNDomain.setTransactionNumber(transactionNumber);
						einvoiceDao.saveIRNCancelDetails(ahcEInvoiceCancelIRNDomain);
					}
					else
					{
						JSONObject errorCodeJsonObject = new JSONObject(objGSTIRN.get("ErrorDetails").toString().replaceAll("\\[", "").replaceAll("\\]", ""));
						AHCEInvoiceErrorLogDomain errorlog=setEInvoiceErrorLogData(transactionNumber, "Cancel IRN API Response",
								Integer.parseInt(objGSTIRN.get("Status").toString()),errorCodeJsonObject.get("ErrorCode").toString(),errorCodeJsonObject.get("ErrorMessage").toString(), objGSTIRN.get("InfoDtls").toString());
						einvoiceDao.logEInvoiceErrorLog(errorlog);
					}
				}
			}
		} catch (Exception e) {
			status="Exception";
			e.printStackTrace();
		}
		return status;
	}
	
	@Override
	public String getIRNDetails(int transactionNumber, int regionId, String applicationId)
	{
		String status="Success";
		try {
		StringBuilder responseData = new StringBuilder();
		AHCEInvoiceErrorLogDomain eIRNDD = new AHCEInvoiceErrorLogDomain();
		
		EinvoicingGSTCredentailsDomain gstCredentailDomain = new EinvoicingGSTCredentailsDomain();
		//regionId=8;
		gstCredentailDomain = einvoiceDao.getGSTCredentails(regionId);
		status =doAuthentication(transactionNumber,regionId); // step1
		if(status.equalsIgnoreCase("Failure"))
		{
			return status;
		}
		if(gstCredentailDomain.getUserId().trim().length()==0)
		{
			//Do Nothing
		}else
			{
				eIRNDD= einvoiceDao.getLogData(transactionNumber);
				if(eIRNDD.getTransactionNumber()!=null || eIRNDD.getTransactionNumber().length()!=0)
				/* if(true) */
				{
					String gstUserName=gstCredentailDomain.getUserId();
					
					AHCEinvoiceAuthJsonDataDomain ahcEinvoiceAuthJsonDataDomain=einvoiceDao.getAuthJsonResponse(transactionNumber);
					Gson gson1 = new Gson();
					//JSONObject jsonObject = new JSONObject(ahcEinvoiceAuthJsonDataDomain.getJsonData());
					Map authJSONMap1 = gson1.fromJson(ahcEinvoiceAuthJsonDataDomain.getJsonData(),Map.class);
					//Object authData = userSession.getAuthJSONMap().get("Data");
					Object authData1 = authJSONMap1.get("Data");
	
					Map authTokenMap1 = (Map) authData1;
					String authToken = authTokenMap1.get("AuthToken").toString();
					String Sek = authTokenMap1.get("Sek").toString();
					
					Gson gson = new Gson();
					Map authJSONMap = gson.fromJson(eIRNDD.getStrExceptionorErrorReason().replace("[", "").replace("]", ""),Map.class);
					Object authData = authJSONMap.get("Desc");
					Map authTokenMap = (Map) authData;
					String IRNNumber = authTokenMap.get("Irn").toString();
					String getIRNURL = AHCEInvoiceUtilProp.getValueFromKey("GET_IRN_URL").trim()+"?IRN="+IRNNumber;
					
					HttpURLConnection con = (HttpURLConnection) new URL(getIRNURL).openConnection();
					
					System.out.println("setting request properties started");
	
					con.setRequestProperty("client-id", clientId);
					con.setRequestProperty("client-secret", clientSecret);
					con.setRequestProperty("emailid", emailid);
					con.setRequestProperty("Gstin", gstCredentailDomain.getGstNumber());
					//con.setRequestProperty("Gstin", "29AABCI2764F000");
					con.setRequestProperty("user_name", gstUserName);
					//con.setRequestProperty("user_name", "anandnat1");
					con.setRequestProperty("AuthToken", authToken);
					//con.setRequestProperty("IRN", AHCEInvoiceUtil.decodeBase64StringTOByte(IRNNumber).toString());
					con.setRequestMethod("GET");
					con.setRequestProperty("Content-Type", "application/json; utf-8");
					con.setRequestProperty("Accept", "application/json");
					con.setRequestProperty("Content-Language", "en-US");
					con.setUseCaches(false);
					con.setDoOutput(true);
					
					byte[] appKeyInBytes = null;
					
					String appKey = AHCEInvoiceUtilProp.getValueFromKey("APP_KEY").trim();
					
					try {
						
						appKeyInBytes = AHCEInvoiceUtil.decodeBase64StringTOByte(appKey);
	
					} catch (Exception e) {
						status="Exception";
						e.printStackTrace();
	
					}
					
					String AuthSek = AHCEInvoiceUtil.decrptyBySyymetricKey(Sek, appKeyInBytes);
					
					// generate json and encrypted in
					// String payloadPlainJson= generateIRNPayloadJson();
					String payloadPlainJsontring = "";
					
					//String encryptedPayload = AHCEInvoiceUtil.encryptBySymmetricKey(payloadPlainJsontring.toString(), AuthSek);
					
					/*
					 * try (OutputStream os = con.getOutputStream()) { //byte[] input =
					 * finalJSONData.getBytes("UTF-8"); //os.write(input, 0, input.length); } catch
					 * (Exception e) { e.printStackTrace(); }
					 */
	
					try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"))) {
	
						String responseLine = null;
						while ((responseLine = br.readLine()) != null) {
							responseData.append(responseLine.trim());
						}
						System.out.println(responseData.toString());
					} catch (Exception e) {
						e.printStackTrace();
					}
	
					final JSONObject objGSTIRN = new JSONObject(responseData.toString());
					
					if(Integer.parseInt(objGSTIRN.get("Status").toString())==1)
						{
							
							String dataInResponse = AHCEInvoiceUtil.decryptBySymmentricKey(objGSTIRN.get("Data").toString(), AuthSek);
							System.out.println("dataInResponse     " + dataInResponse);
			
							JSONObject decodedData = new JSONObject(dataInResponse);
			
							byte[] encodedQRCode = decodeSignedJWT(decodedData.get("SignedQRCode").toString());
							AHCEInvoiceIRNDtlsDomain eIRNDD1 = new AHCEInvoiceIRNDtlsDomain();
							eIRNDD1 = convertIRNJSONmain(decodedData, payloadPlainJsontring.toString(), objGSTIRN, transactionNumber,authTokenMap1, appKey, encodedQRCode);
							einvoiceDao.saveIRNAPIResponseData(eIRNDD1);
							JSONObject responseDataJson = new JSONObject(dataInResponse);
							System.out.println("AHC GetIRN Details dataInResponse="+dataInResponse);
							
						}
					else
						{
							JSONObject errorCodeJsonObject = new JSONObject(objGSTIRN.get("ErrorDetails").toString().replaceAll("\\[", "").replaceAll("\\]", ""));
							AHCEInvoiceErrorLogDomain errorlog=setEInvoiceErrorLogData(transactionNumber, "Generate IRN API Response",
									Integer.parseInt(objGSTIRN.get("Status").toString()),errorCodeJsonObject.get("ErrorCode").toString(),errorCodeJsonObject.get("ErrorMessage").toString(), objGSTIRN.get("InfoDtls").toString());
							einvoiceDao.logEInvoiceErrorLog(errorlog);
						}
					status="foundInLog";
				}else
				{
					//initiateEnvoiceGeneration( transactionNumber,  13,  applicationId,  regionId);
					status="notFoundInLog";
				}
			}
		} catch (Exception e) {
			status="Exception";
			e.printStackTrace();
		}
		return status;
	}
	
	@Override
	public void generateQRCodeFeeReceiptAHC_view(HttpServletRequest request, HttpServletResponse response, int numTransactionNumber, String strReportFormat) {
		
		System.out.println("generateFeeReceipt Form");
		
			   String outputfile="";
		   if(strReportFormat.equalsIgnoreCase("pdf")){

		   outputfile="FeeTaxReceipt_"+numTransactionNumber+".pdf";
		   response.setContentType("application/pdf");
			response.setHeader("Content-disposition","attachment; filename=\""+outputfile+"\"");
		   }
		   else if(strReportFormat.equalsIgnoreCase("docx")){

		    outputfile="FeeReceipt_"+numTransactionNumber+".docx";
		    response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
			response.setHeader("Content-disposition","attachment; filename=\""+outputfile+"\"");
		   }
		   else if(strReportFormat.equalsIgnoreCase("html")){
			   outputfile="FeeReceipt_"+numTransactionNumber+".html";
			   response.setContentType("text/html");
			   response.setHeader("Content-disposition","inline; filename=\""+outputfile+"\"");
		   }
		   //File fileFound = new File(outputfilepath+File.separator+outputfile);
		   
		
	try{  
		  
		    response.setHeader("Expires", "0");
			response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
					response.setHeader("Pragma", "public");
			
			
			System.out.println("check 3 ");
			
			//DataSource source = new FileDataSource(uploadFiles.copyFileFromFTP(outputfile));  
			FileInputStream inputstream=new FileInputStream(uploadFiles.copyFileFromFTP(outputfilepath+File.separator+outputfile));
		    
			System.out.println("check 4 ");
			OutputStream out=response.getOutputStream();
			System.out.println("out####======"+out.toString());
		    FileCopyUtils.copy(inputstream, out);
		    out.close();
		    System.out.println("check 5 ");
	}
	catch(Exception e) {
	System.out.println("inside Exception of writing file in response");
	e.printStackTrace();
	}
	finally
	{
		connInfo.close();
//		if(fileFound.exists()){
//			fileFound.delete();
//			System.out.println("File Deleted ");
//		}
		
	}
 }
	
	@Override
	public AHCEInvoiceIRNDtlsDomain getSavedIRNDetails(int transactionNumber)
	{
		return einvoiceDao.getIRNDetails(transactionNumber);
	}
	
	public void generateB2CQRCodeFeeReceipt(int transactionNumber, int schemeId, String applicationId, int regionId)
	{

		String strOutputFileName="";    
        String path="";
        String strJasperPath = jrxmlpath;
		strJasperPath = strJasperPath + "AHCPaymentTaxFeeReceipt.jasper";
		path=ResourceBundleFile.getValueFromKey("JRXML_PDF_PATH");
		strOutputFileName = path+File.separator+"FeeTaxReceipt_"+transactionNumber+".pdf";
		HashMap<Object,Object> parameters = new HashMap<Object,Object>();
		parameters=generatePayloadJSON(transactionNumber, schemeId,applicationId,regionId);
		parameters.put("strOutputFileName", strOutputFileName);
		parameters.put("irnNumber", "");
		parameters.put("AckNo",Long.valueOf("0"));
		parameters.put("AckDt", "");
		Map<String, Object> qrCodeDetails = new HashMap<String, Object>();
		qrCodeDetails.put("SellerGstin", parameters.get("branchGST").toString());
		qrCodeDetails.put("DocNo", parameters.get("invoiceNo").toString().substring(3));
		qrCodeDetails.put("DocTyp", "INV");
		String[] receiptDate =parameters.get("receiptDate").toString().substring(0,10).split("-");
		qrCodeDetails.put("DocDt",(receiptDate[2].concat("/").concat(receiptDate[1].concat("/").concat(receiptDate[0]))));
		qrCodeDetails.put("TotInvVal", Double.parseDouble(parameters.get("totalAmount").toString()));
		qrCodeDetails.put("ItemCnt", 1);
		qrCodeDetails.put("HsnCode", "998349");		
		parameters.put("SignedQRCode",qrCodeDetails.toString());
		
		try {
			generateOutputFileAHC(strJasperPath,parameters,strOutputFileName,"pdf");
			//generateQRCodeFeeReceiptAHC_view(request, response, transactionNumber, "pdf");
		} catch (Exception e) {
			//  Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void generateB2CQRCodeFeeReceiptFromUrl(HttpServletRequest request, HttpServletResponse response,int transactionNumber, int schemeId, String applicationId, int regionId)
	{

		String strOutputFileName="";    
        String path="";
        String strJasperPath = jrxmlpath;
		strJasperPath = strJasperPath + "AHCPaymentTaxFeeReceipt.jasper";
		path=ResourceBundleFile.getValueFromKey("JRXML_PDF_PATH");
		strOutputFileName = path+File.separator+"FeeTaxReceipt_"+transactionNumber+".pdf";
		HashMap<Object,Object> parameters = new HashMap<Object,Object>();
		parameters=generatePayloadJSON(transactionNumber, schemeId,applicationId,regionId);
		parameters.put("strOutputFileName", strOutputFileName);
		parameters.put("irnNumber", "");
		parameters.put("AckNo",Long.valueOf("0"));
		parameters.put("AckDt", "");
		Map<String, Object> qrCodeDetails = new HashMap<String, Object>();
		qrCodeDetails.put("SellerGstin", parameters.get("branchGST").toString());
		qrCodeDetails.put("DocNo", parameters.get("invoiceNo").toString().substring(3));
		qrCodeDetails.put("DocTyp", "INV");
		String[] receiptDate =parameters.get("receiptDate").toString().substring(0,10).split("-");
		qrCodeDetails.put("DocDt",(receiptDate[2].concat("/").concat(receiptDate[1].concat("/").concat(receiptDate[0]))));
		qrCodeDetails.put("TotInvVal", Double.parseDouble(parameters.get("totalAmount").toString()));
		qrCodeDetails.put("ItemCnt", 1);
		qrCodeDetails.put("HsnCode", "998349");		
		parameters.put("SignedQRCode",qrCodeDetails.toString());
		
		try {
			generateOutputFileAHC(strJasperPath,parameters,strOutputFileName,"pdf");
			generateQRCodeFeeReceiptAHC_view(request, response, transactionNumber, "pdf");
		} catch (Exception e) {
			//  Auto-generated catch block
			e.printStackTrace();
		}
	}
}