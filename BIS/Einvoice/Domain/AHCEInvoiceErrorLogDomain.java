package BIS.Einvoice.Domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name="ahc_einv_error_log_dtls",schema="bis_log")
public class AHCEInvoiceErrorLogDomain implements Serializable{

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="ahc_einv_error_log_dtls")
	@TableGenerator(initialValue=1, allocationSize=1, name="ahc_einv_error_log_dtls" )
	@Column(name="num_id")
	long numId;
	
	@Column(name="dt_log_date")
	Date dtLogDate;
	
	@Column(name="str_api_status")
	int status;
	
	@Column(name="str_api_error_code")
	String strApiErrorCode;
	
	@Column(name="str_api_error_message",length=500)
	String strApiErrorMessage;
	
	@Column(name="transaction_number")
	String transactionNumber;
	
	@Column(name="str_error_Occured_at")
	String strErrorOccuredAt;
	
	@Column(name="str_exception_or_error_reason")
	String strExceptionorErrorReason;
	
	public long getNumId() {
		return numId;
	}

	public void setNumId(long numId) {
		this.numId = numId;
	}

	public Date getDtLogDate() {
		return dtLogDate;
	}

	public void setDtLogDate(Date dtLogDate) {
		this.dtLogDate = dtLogDate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStrApiErrorCode() {
		return strApiErrorCode;
	}

	public void setStrApiErrorCode(String strApiErrorCode) {
		this.strApiErrorCode = strApiErrorCode;
	}

	public String getStrApiErrorMessage() {
		return strApiErrorMessage;
	}

	public void setStrApiErrorMessage(String strApiErrorMessage) {
		this.strApiErrorMessage = strApiErrorMessage;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public String getStrErrorOccuredAt() {
		return strErrorOccuredAt;
	}

	public void setStrErrorOccuredAt(String strErrorOccuredAt) {
		this.strErrorOccuredAt = strErrorOccuredAt;
	}

	
	public String getStrExceptionorErrorReason() {
		return strExceptionorErrorReason;
	}

	public void setStrExceptionorErrorReason(String strExceptionorErrorReason) {
		this.strExceptionorErrorReason = strExceptionorErrorReason;
	}

	@Override
	public String toString() {
		return "AHCEInvoiceErrorLogDomain [numId=" + numId + ", dtLogDate=" + dtLogDate + ", status=" + status
				+ ", strApiErrorCode=" + strApiErrorCode + ", strApiErrorMessage=" + strApiErrorMessage
				+ ", transactionNumber=" + transactionNumber + ", strErrorOccuredAt=" + strErrorOccuredAt
				+ ", strExceptionorErrorReason=" + strExceptionorErrorReason + "]";
	}

	
	
	
	
	
}
