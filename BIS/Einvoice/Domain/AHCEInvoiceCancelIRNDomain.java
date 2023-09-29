package BIS.Einvoice.Domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name="ahc_einv_cancel_irn_dtls",schema = "bis_hall")
public class AHCEInvoiceCancelIRNDomain implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long numId;
	private Date entryDate;
	private String apiResponsePayloadJson;
	private String apiRequestPayloadJson;
	private int apiStatus;
	private String appKey;
	private String authToken;
	private String irnNumber;
	private String remarks;
	private String sek;
	private String tokenExpiary;
	private int transactionNumber;
	
	@Id
	@GenericGenerator(name = "cancelIRNIdSequence",strategy = "sequence",parameters = @Parameter(value = "cancel_irn_id_seq", name = "cancelIRNIdSequence"))
	@GeneratedValue(generator = "cancelIRNIdSequence")
	@Column(name = "num_id")
	public long getNumId() {
		return numId;
	}
	public void setNumId(long numId) {
		this.numId = numId;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dt_entry_date")
	public Date getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}
	
	@Column(name = "str_api_response_payload_json")
	public String getApiResponsePayloadJson() {
		return apiResponsePayloadJson;
	}
	public void setApiResponsePayloadJson(String apiResponsePayloadJson) {
		this.apiResponsePayloadJson = apiResponsePayloadJson;
	}
	
	@Column(name = "str_api_request_payload_json")
	public String getApiRequestPayloadJson() {
		return apiRequestPayloadJson;
	}
	public void setApiRequestPayloadJson(String apiRequestPayloadJson) {
		this.apiRequestPayloadJson = apiRequestPayloadJson;
	}
	
	@Column(name = "str_api_status")
	public int getApiStatus() {
		return apiStatus;
	}
	public void setApiStatus(int apiStatus) {
		this.apiStatus = apiStatus;
	}
	
	@Column(name = "str_app_key")
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	
	@Column(name = "str_auth_token")
	public String getAuthToken() {
		return authToken;
	}
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
	
	@Column(name = "str_irn")
	public String getIrnNumber() {
		return irnNumber;
	}
	public void setIrnNumber(String irnNumber) {
		this.irnNumber = irnNumber;
	}
	
	@Column(name = "str_remarks")
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	@Column(name = "str_sek")
	public String getSek() {
		return sek;
	}
	public void setSek(String sek) {
		this.sek = sek;
	}
	
	@Column(name = "str_token_expiry")
	public String getTokenExpiary() {
		return tokenExpiary;
	}
	public void setTokenExpiary(String tokenExpiary) {
		this.tokenExpiary = tokenExpiary;
	}
	
	@Column(name = "transaction_number")
	public int getTransactionNumber() {
		return transactionNumber;
	}
	public void setTransactionNumber(int transactionNumber) {
		this.transactionNumber = transactionNumber;
	}
	
}
