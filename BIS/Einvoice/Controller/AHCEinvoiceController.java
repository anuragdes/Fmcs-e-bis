package BIS.Einvoice.Controller;

import java.io.File;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BIS.Einvoice.Domain.AHCEInvoiceIRNDtlsDomain;
import BIS.Einvoice.Service.AHCEInvoiceService;
import Global.CommonUtility.ResourceBundleFile;
import Global.CommonUtility.Controller.UploadFiles;
import eBIS.AppConfig.CustomWebExceptionHandler;

@Controller
public class AHCEinvoiceController {

	@Autowired
	AHCEInvoiceService ahcEInvoiceService;
	@Autowired
	UploadFiles uploadFiles;
	
	 String jrxmlpath = ResourceBundleFile.getValueFromKey("JRXML_PATH");
	 String outputfilepath = ResourceBundleFile.getValueFromKey("JRXML_PDF_PATH");	
	 @CustomWebExceptionHandler()
	@RequestMapping(value="/eInvoiceController", method = RequestMethod.GET)
	public void initiateEinvoicingProcedure(HttpServletRequest request, HttpServletResponse response)
	{
		System.out.println("Authantication Procedure Starts");
		int transactionNumber=Integer.parseInt(request.getParameter("transactionNumber").toString()==""?"0":request.getParameter("transactionNumber").toString());
		int schemeId=13;
		int regionId=Integer.parseInt(request.getParameter("regionId").toString()==""?"0":request.getParameter("regionId").toString());
		String applicationId=request.getParameter("applicationId").toString();
		if(transactionNumber==0 || regionId==0 || applicationId==null || applicationId.trim().length()==0)
		{
			//Do Nothing
		}
		else
		{
			 String fileName="";    
            String path="";
			path=ResourceBundleFile.getValueFromKey("JRXML_PDF_PATH");
	        fileName = path+File.separator+"FeeTaxReceipt_"+transactionNumber+".pdf";
			//String strOutputFileName="FeeTaxReceipt_" + transactionNumber + ".pdf";		
			ahcEInvoiceService.initiateEnvoiceGeneration( transactionNumber,  schemeId,  applicationId,  regionId);
		}
		System.out.println("Authantication Procedure Ends");
	}
	
	@RequestMapping(value="/eInvoiceCancelIRNController", method = RequestMethod.GET)
	public void eInvoiceCancelIRNController(HttpServletRequest request, HttpServletResponse response)
	{
		System.out.println("Authantication Procedure Starts");
		int transactionNumber=Integer.parseInt(request.getParameter("transactionNumber").toString()==""?"0":request.getParameter("transactionNumber").toString());
		int regionId=Integer.parseInt(request.getParameter("regionId").toString()==""?"0":request.getParameter("regionId").toString());
		if(transactionNumber==0 || regionId==0 )
		{
			//Do Nothing
		}
		else
		{	
			ahcEInvoiceService.cancelIRN( transactionNumber,regionId);
		}
		System.out.println("Authantication Procedure Ends");
	}
	
	@RequestMapping(value="/eInvoiceGetIRNDetailController", method = RequestMethod.GET)
	public void eInvoiceGetIRNDetailController(HttpServletRequest request, HttpServletResponse response)
	{
		System.out.println("Authantication Procedure Starts");
		int transactionNumber=Integer.parseInt(request.getParameter("transactionNumber").toString()==""?"0":request.getParameter("transactionNumber").toString());
		int regionId=Integer.parseInt(request.getParameter("regionId").toString()==""?"0":request.getParameter("regionId").toString());
		String applicationId=request.getParameter("applicationId").toString();
		if(transactionNumber==0 || regionId==0 )
		{
			//Do Nothing
		}
		else
		{	
			ahcEInvoiceService.getIRNDetails( transactionNumber,regionId,applicationId);
		}
		System.out.println("Authantication Procedure Ends");
	}
	
	
	
	@RequestMapping(value="/generateQRCodeFeeReceiptAHC_view", method = RequestMethod.GET)
	public int generateQRCodeFeeReceiptAHC_view(HttpServletRequest request, HttpServletResponse response)
	{
		String strJasperPath = jrxmlpath;
		strJasperPath = strJasperPath + "AHCPaymentTaxFeeReceipt.jasper";
		int transactionNumber=Integer.parseInt(request.getParameter("transactionNumber").toString()==""?"0":request.getParameter("transactionNumber").toString());
		int schemeId=13;
		int regionId=Integer.parseInt(request.getParameter("regionId").toString()==""?"0":request.getParameter("regionId").toString());
		String applicationId=request.getParameter("applicationId").toString();
		if(transactionNumber==0)
		{
			//Do Nothing
		}
		else
		{	
			String strOutputFileName="";    
            String path="";
            path=ResourceBundleFile.getValueFromKey("JRXML_PDF_PATH");
			strOutputFileName = path+File.separator+"FeeTaxReceipt_"+transactionNumber+".pdf";
			AHCEInvoiceIRNDtlsDomain eIRNDD = new AHCEInvoiceIRNDtlsDomain();
			eIRNDD= ahcEInvoiceService.getSavedIRNDetails(transactionNumber);
			if(eIRNDD.getNumId()==0)
			{
				//ahcEInvoiceService.getIRNDetails(transactionNumber,regionId,applicationId);
				//eIRNDD= ahcEInvoiceService.getSavedIRNDetails(transactionNumber);
				//if(eIRNDD.getNumId()==0)
				//{
				//	return 0;
				//}
				
				generateB2CQRCodeFeeReceiptFromUrl(request,response);
			}
			else
			{
				HashMap<Object,Object> parameters = new HashMap<Object,Object>();
				parameters=ahcEInvoiceService.generatePayloadJSON(transactionNumber, schemeId,applicationId,regionId);
				parameters.put("irnNumber", eIRNDD.getStrIRN());
				parameters.put("AckNo", Long.valueOf(eIRNDD.getStrAckNo()));
				parameters.put("AckDt", eIRNDD.getStrAckDt());
				parameters.put("SignedQRCode", eIRNDD.getStrSignedQRCode());
				path=ResourceBundleFile.getValueFromKey("JRXML_PDF_PATH");
				parameters.put("strOutputFileName", strOutputFileName);
				try {
					if(uploadFiles.fileExistsOnFTP(path+"/"+strOutputFileName)=="file_not_found"){	
						ahcEInvoiceService.generateOutputFileAHC(strJasperPath,parameters,strOutputFileName,"pdf");
					}
					ahcEInvoiceService.generateQRCodeFeeReceiptAHC_view(request, response, transactionNumber, "pdf");
				} catch (Exception e) {
					//  Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		System.out.println("Authantication Procedure Ends");
		return 0;
	}
	
	
	@RequestMapping(value="/generateB2CQRCodeFeeReceiptFromUrl", method = RequestMethod.GET)
	public void generateB2CQRCodeFeeReceiptFromUrl(HttpServletRequest request, HttpServletResponse response)
	{
		int transactionNumber=Integer.parseInt(request.getParameter("transactionNumber").toString()==""?"0":request.getParameter("transactionNumber").toString());
		int schemeId=13;
		int regionId=Integer.parseInt(request.getParameter("regionId").toString()==""?"0":request.getParameter("regionId").toString());
		String applicationId=request.getParameter("applicationId").toString();
		if(transactionNumber==0)
		{
			//Do Nothing
		}
		else
		{	
			ahcEInvoiceService.generateB2CQRCodeFeeReceiptFromUrl(request,response,transactionNumber,schemeId, applicationId, regionId);
		}
	}
}
