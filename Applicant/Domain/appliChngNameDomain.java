package Applicant.Domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;


@Entity
@Table(name="pc_applicant_change_name",schema="bis_dev")
public class appliChngNameDomain {

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="degree")
	@TableGenerator(initialValue=1, allocationSize=1, name="degree")
	@Column(name="num_id")
	int num_Id;
	
	@Column(name="numentryempId", length = 10)
	long numEntryEmpId;
	
	@Column(name="strnewname", length=100)
	String strNewName;
	
	@Column(name="strappid", length=100)
	String strAppId;
	
	@Column(name="stroldname", length=100)
	String strOldName;
	
	@Column(name="docpath", length=100)
	String docPath;
	
	@Column(name="DT_ENTRY_DATE")
	Date date=new Date();
	
	@Column(name="NUM_ISVALID", length = 1)
	int isValid=1;
	
	

	public String getStrAppId() {
		return strAppId;
	}

	public void setStrAppId(String strAppId) {
		this.strAppId = strAppId;
	}

	public String getDocPath() {
		return docPath;
	}

	public void setDocPath(String docPath) {
		this.docPath = docPath;
	}

	public int getNum_Id() {
		return num_Id;
	}

	public void setNum_Id(int num_Id) {
		this.num_Id = num_Id;
	}

	public long getNumEntryEmpId() {
		return numEntryEmpId;
	}

	public void setNumEntryEmpId(long numEntryEmpId) {
		this.numEntryEmpId = numEntryEmpId;
	}

	public String getStrNewName() {
		return strNewName;
	}

	public void setStrNewName(String strNewName) {
		this.strNewName = strNewName;
	}

	public String getStrOldName() {
		return strOldName;
	}

	public void setStrOldName(String strOldName) {
		this.strOldName = strOldName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getIsValid() {
		return isValid;
	}

	public void setIsValid(int isValid) {
		this.isValid = isValid;
	}
	
	
	
	
}
