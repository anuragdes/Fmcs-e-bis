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
@Table(name="ahc_einvoice_auth_info",schema="bis_hall")
public class AHCEInvoiceAuthenticationDomain implements Serializable {

	private static final long serialVersionUID = -6161352923350643521L;
	

	@Id
	@GenericGenerator(name = "authIdSequence",strategy = "sequence",parameters = @Parameter(value = "auth_id_seq", name = "authIdSequence"))
	@GeneratedValue(generator = "authIdSequence")
	@Column(name = "auth_id")
	long authId;
	
	
	@Column(name="status")
	int status;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_date")
	Date create_date;
	
	@Column(name="client_id")
	String clientId;
	
	@Column(name="user_name")
	String userName;
	
	@Column(name="auth_token")
	String authToken;
	
	@Column(name="sek")
	String sek;
	
	@Column(name="token_expiry")
	String tokenExpiry;
	
	@Column(name="str_api_response_payload_json")
	String strAPIResponsePayloadJSON;
	
	@Column(name="app_key")
	String appKey;

	
	@Override
	public String toString() {
		return "AHCEInvoiceAuthenticationDomain [authId=" + authId + ", status=" + status + ", create_date="
				+ create_date + ", clientId=" + clientId + ", userName=" + userName + ", authToken=" + authToken
				+ ", sek=" + sek + ", tokenExpiry=" + tokenExpiry + ", strAPIResponsePayloadJSON="
				+ strAPIResponsePayloadJSON + ", appKey=" + appKey + "]";
	}


	public long getAuthId() {
		return authId;
	}


	public void setAuthId(long authId) {
		this.authId = authId;
	}


	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}


	public Date getCreate_date() {
		return create_date;
	}


	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}


	public String getClientId() {
		return clientId;
	}


	public void setClientId(String clientId) {
		this.clientId = clientId;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getAuthToken() {
		return authToken;
	}


	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}


	public String getSek() {
		return sek;
	}


	public void setSek(String sek) {
		this.sek = sek;
	}


	public String getTokenExpiry() {
		return tokenExpiry;
	}


	public void setTokenExpiry(String tokenExpiry) {
		this.tokenExpiry = tokenExpiry;
	}


	public String getStrAPIResponsePayloadJSON() {
		return strAPIResponsePayloadJSON;
	}


	public void setStrAPIResponsePayloadJSON(String strAPIResponsePayloadJSON) {
		this.strAPIResponsePayloadJSON = strAPIResponsePayloadJSON;
	}


	public String getAppKey() {
		return appKey;
	}


	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	
	
	
	
	
}
