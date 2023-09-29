package BIS.Einvoice.Domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name="ahc_einvoice_auth_json_data",schema = "bis_hall")
public class AHCEinvoiceAuthJsonDataDomain implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long jsonDataId;
	private String jsonData;
	private int transactionNumber;
	private Date entryDate;
	
	@Id
	@GenericGenerator(name = "authJsonIdSequence",strategy = "sequence",parameters = @Parameter(value = "auth_json_id_seq;", name = "authJsonIdSequence"))
	@GeneratedValue(generator = "authJsonIdSequence")
	@Column(name = "json_data_id")
	public long getJsonDataId() {
		return jsonDataId;
	}
	public void setJsonDataId(long jsonDataId) {
		this.jsonDataId = jsonDataId;
	}
	
	@Column(name="json_data")
	public String getJsonData() {
		return jsonData;
	}
	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}
	
	@Column(name="transacction_number")
	public int getTransactionNumber() {
		return transactionNumber;
	}
	public void setTransactionNumber(int transactionNumber) {
		this.transactionNumber = transactionNumber;
	}
	
	
	@Column(name="entry_date")
	public Date getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}
}
