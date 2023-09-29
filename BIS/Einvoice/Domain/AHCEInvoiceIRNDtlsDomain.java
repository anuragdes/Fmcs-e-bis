package BIS.Einvoice.Domain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name="ahc_einv_irn_dtls",schema="bis_hall")
public class AHCEInvoiceIRNDtlsDomain implements Serializable {
	
private static final long serialVersionUID = -6161352923350643521L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="ahc_einv_irn_dtls")
	@TableGenerator(initialValue=1, allocationSize=1, name="ahc_einv_irn_dtls" )
	@Column(name="num_id")
	long numId;
	
	@Column(name="transaction_number")
	Integer transactionNumber;
	
	@Column(name="str_app_key",length=500)
	String strAppKey;
	
	@Column(name="str_auth_token",length=500)
	String strAuthToken;
	
	@Column(name="str_sek",length=500)
	String strSek;
	
	@Column(name="str_token_expiry")
	String strTokenExpiry;
	
	@Column(name="str_api_request_payload_json",columnDefinition = "text")
	String strApiRequestPayloadJSON;
	
	@Column(name="str_api_response_payload_json",columnDefinition = "text")
	String strAPIResponsePayloadJSON;
	
	@Column(name="str_api_status")
	int strApiStatus;
	
	@Column(name="str_ack_no",length=200)
	String strAckNo;
	
	@Column(name="str_ack_dt")
	String strAckDt;
	
	@Column(name="str_irn",length=500)
	String strIRN;
	
	@Column(name="str_signed_invoice",columnDefinition = "text")
	String strSignedInvoice;
	
	@Column(name="str_signed_qrcode",columnDefinition = "text")
	String strSignedQRCode;
	
	@Column(name="str_status")
	String strStatus;
	
	@Column(name="str_ewb_no")
	String strEWBNo;
	
	@Column(name="str_ewb_dt")
	String strEWBDt;
	
	@Column(name="str_ewb_valid_till")
	String strEWBValidTill;
	
	@Column(name="str_remarks")
	String strRemarks;
	
	@Column(name="dt_entry_date")
	Date dtEntryDate;
	
	@Column(name="is_valid")
	int isValid;
	
	@Column(name="qrcode")
	byte[] qrcode;

	public long getNumId() {
		return numId;
	}

	public void setNumId(long numId) {
		this.numId = numId;
	}

	
	public Integer getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(Integer transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public String getStrAppKey() {
		return strAppKey;
	}

	public void setStrAppKey(String strAppKey) {
		this.strAppKey = strAppKey;
	}

	public String getStrAuthToken() {
		return strAuthToken;
	}

	public void setStrAuthToken(String strAuthToken) {
		this.strAuthToken = strAuthToken;
	}

	public String getStrSek() {
		return strSek;
	}

	public void setStrSek(String strSek) {
		this.strSek = strSek;
	}

	public String getStrTokenExpiry() {
		return strTokenExpiry;
	}

	public void setStrTokenExpiry(String strTokenExpiry) {
		this.strTokenExpiry = strTokenExpiry;
	}

	public String getStrApiRequestPayloadJSON() {
		return strApiRequestPayloadJSON;
	}

	public void setStrApiRequestPayloadJSON(String strApiRequestPayloadJSON) {
		this.strApiRequestPayloadJSON = strApiRequestPayloadJSON;
	}

	public String getStrAPIResponsePayloadJSON() {
		return strAPIResponsePayloadJSON;
	}

	public void setStrAPIResponsePayloadJSON(String strAPIResponsePayloadJSON) {
		this.strAPIResponsePayloadJSON = strAPIResponsePayloadJSON;
	}

	

	public int getStrApiStatus() {
		return strApiStatus;
	}

	public void setStrApiStatus(int strApiStatus) {
		this.strApiStatus = strApiStatus;
	}

	public String getStrAckNo() {
		return strAckNo;
	}

	public void setStrAckNo(String strAckNo) {
		this.strAckNo = strAckNo;
	}

	public String getStrAckDt() {
		return strAckDt;
	}

	public void setStrAckDt(String strAckDt) {
		this.strAckDt = strAckDt;
	}

	public String getStrIRN() {
		return strIRN;
	}

	public void setStrIRN(String strIRN) {
		this.strIRN = strIRN;
	}

	public String getStrSignedInvoice() {
		return strSignedInvoice;
	}

	public void setStrSignedInvoice(String strSignedInvoice) {
		this.strSignedInvoice = strSignedInvoice;
	}

	public String getStrSignedQRCode() {
		return strSignedQRCode;
	}

	public void setStrSignedQRCode(String strSignedQRCode) {
		this.strSignedQRCode = strSignedQRCode;
	}

	public String getStrStatus() {
		return strStatus;
	}

	public void setStrStatus(String strStatus) {
		this.strStatus = strStatus;
	}

	public String getStrEWBNo() {
		return strEWBNo;
	}

	public void setStrEWBNo(String strEWBNo) {
		this.strEWBNo = strEWBNo;
	}

	public String getStrEWBDt() {
		return strEWBDt;
	}

	public void setStrEWBDt(String strEWBDt) {
		this.strEWBDt = strEWBDt;
	}

	public String getStrEWBValidTill() {
		return strEWBValidTill;
	}

	public void setStrEWBValidTill(String strEWBValidTill) {
		this.strEWBValidTill = strEWBValidTill;
	}

	public String getStrRemarks() {
		return strRemarks;
	}

	public void setStrRemarks(String strRemarks) {
		this.strRemarks = strRemarks;
	}

	public Date getDtEntryDate() {
		return dtEntryDate;
	}

	public void setDtEntryDate(Date dtEntryDate) {
		this.dtEntryDate = dtEntryDate;
	}

	public int getIsValid() {
		return isValid;
	}

	public void setIsValid(int isValid) {
		this.isValid = isValid;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	public byte[] getQrcode() {
		return qrcode;
	}

	public void setQrcode(byte[] qrcode) {
		this.qrcode = qrcode;
	}

	@Override
	public String toString() {
		return "AHCEInvoiceIRNDtlsDomain [numId=" + numId + ", transactionNumber=" + transactionNumber + ", strAppKey="
				+ strAppKey + ", strAuthToken=" + strAuthToken + ", strSek=" + strSek + ", strTokenExpiry="
				+ strTokenExpiry + ", strApiRequestPayloadJSON=" + strApiRequestPayloadJSON
				+ ", strAPIResponsePayloadJSON=" + strAPIResponsePayloadJSON + ", strApiStatus=" + strApiStatus
				+ ", strAckNo=" + strAckNo + ", strAckDt=" + strAckDt + ", strIRN=" + strIRN + ", strSignedInvoice="
				+ strSignedInvoice + ", strSignedQRCode=" + strSignedQRCode + ", strStatus=" + strStatus + ", strEWBNo="
				+ strEWBNo + ", strEWBDt=" + strEWBDt + ", strEWBValidTill=" + strEWBValidTill + ", strRemarks="
				+ strRemarks + ", dtEntryDate=" + dtEntryDate + ", isValid=" + isValid + ", qrcode="
				+ Arrays.toString(qrcode) + "]";
	}

	

	
	
	
	
	
	

}
