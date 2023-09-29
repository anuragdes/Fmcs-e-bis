package BIS.Einvoice.Domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name="ahc_ro_gst_credentials_einvoicing", schema = "bis_hall")
public class EinvoicingGSTCredentailsDomain implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long credentialId;
	private int regionId;
	private String regionName;
	private String userId;
	private String gstPassword;
	private String gstNumber;
	
	
	@Id
	@GenericGenerator(name = "einvoicingCredentialIdSeq",strategy = "sequence",parameters = @Parameter(value = "einvoicing_credential_id_seq", name = "einvoicingCredentialIdSeq"))
	@GeneratedValue(generator = "einvoicingCredentialIdSeq")
	@Column(name = "einvoicing_credential_id")
	public long getCredentialId() {
		return credentialId;
	}
	public void setCredentialId(long credentialId) {
		this.credentialId = credentialId;
	}
	
	@Column(name = "region_id")
	public int getRegionId() {
		return regionId;
	}
	public void setRegionId(int regionId) {
		this.regionId = regionId;
	}
	
	@Column(name = "region_short_name")
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	
	@Column(name = "user_id")
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Column(name = "gst_password")
	public String getGstPassword() {
		return gstPassword;
	}
	public void setGstPassword(String gstPassword) {
		this.gstPassword = gstPassword;
	}
	
	@Column(name = "gst_number")
	public String getGstNumber() {
		return gstNumber;
	}
	public void setGstNumber(String gstNumber) {
		this.gstNumber = gstNumber;
	}
}
