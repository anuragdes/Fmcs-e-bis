package BIS.Einvoice.Model;

public class AuthenticationResponseModel {

	private int status;
	private String clientId;
	private String userName;
	private String AuthToken;
	private String sek;
	private String tokenExpiry;
	private String errorDetails;
	private String infoDetails;
	private long authId;
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
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
		return AuthToken;
	}
	public void setAuthToken(String authToken) {
		AuthToken = authToken;
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
	public String getErrorDetails() {
		return errorDetails;
	}
	public void setErrorDetails(String errorDetails) {
		this.errorDetails = errorDetails;
	}
	public String getInfoDetails() {
		return infoDetails;
	}
	public void setInfoDetails(String infoDetails) {
		this.infoDetails = infoDetails;
	}
	public long getAuthId() {
		return authId;
	}
	public void setAuthId(long authId) {
		this.authId = authId;
	}
	
	
}
