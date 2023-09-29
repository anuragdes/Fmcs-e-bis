package Global.Dashboard.Domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;


@Entity
@Table(name="dashboard_scheme_mst",schema="dashboard")
public class DashboardSchemeMasterDomain implements Serializable 
{
	
	private static final long serialVersionUID = -6161352923350643521L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="dashboard_scheme_mst")
	@TableGenerator(initialValue=1, allocationSize=1, name="dashboard_scheme_mst")
	@Column(name="num_id")
	int num_id;
		
	@Column(name="str_scheme_name", nullable=false)
	String str_scheme_name;
	
	@Column(name="num_is_valid",columnDefinition = "numeric(1)")
	int num_is_valid=1;

	@Column(name="str_scheme_icon", nullable=false)
	String str_scheme_icon;
	

	public int getNum_id() {
		return num_id;
	}



	public void setNum_id(int num_id) {
		this.num_id = num_id;
	}



	public String getStr_scheme_name() {
		return str_scheme_name;
	}



	public void setStr_scheme_name(String str_scheme_name) {
		this.str_scheme_name = str_scheme_name;
	}



	public int getNum_is_valid() {
		return num_is_valid;
	}



	public void setNum_is_valid(int num_is_valid) {
		this.num_is_valid = num_is_valid;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	public String getStr_scheme_icon() {
		return str_scheme_icon;
	}



	public void setStr_scheme_icon(String str_scheme_icon) {
		this.str_scheme_icon = str_scheme_icon;
	}
	
	
	
	

}

