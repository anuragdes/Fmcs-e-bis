package BIS.Einvoice.Service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import BIS.Einvoice.Domain.AHCEInvoiceIRNDtlsDomain;

public interface AHCEInvoiceService {
	
	@Transactional(propagation=Propagation.REQUIRED)
	public String doAuthentication(int transactionNumber, int regionId);

	@Transactional(propagation=Propagation.REQUIRED)
	boolean generateIRN(JSONObject payloadPlainJson, int transactionNumber,HashMap<Object,Object> parameters, int regionId);

	@Transactional(propagation=Propagation.REQUIRED)
	String initiateEnvoiceGeneration(int transactionNumber, int schemeId,String applicationId, int regionId);

	@Transactional(propagation=Propagation.REQUIRED)
	String cancelIRN(int transactionNumber, int regionId);

	@Transactional(propagation=Propagation.REQUIRED)
	String getIRNDetails(int transactionNumber, int regionId,String applicationId);

	@Transactional(propagation=Propagation.REQUIRED)
	void generateQRCodeFeeReceiptAHC_view(HttpServletRequest request, HttpServletResponse response,	int numTransactionNumber, String strReportFormat);

	HashMap<Object, Object> generatePayloadJSON(int transactionNumber, int schemeId, String applicationId,int regionId);

	@Transactional(propagation=Propagation.REQUIRED)
	String generateOutputFileAHC(String strJasperPath, Map parameters, String strOutputFilePath, String strReportFormat)throws Exception;

	@Transactional(propagation=Propagation.REQUIRED)
	AHCEInvoiceIRNDtlsDomain getSavedIRNDetails(int transactionNumber);

	void generateB2CQRCodeFeeReceiptFromUrl(HttpServletRequest request, HttpServletResponse response,
			int transactionNumber, int schemeId, String applicationId, int regionId);

	
}