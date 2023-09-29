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
@Table(name="dashboard_role_scheme_mapping",schema="dashboard")
public class DashboardRoleSchemeMappingDomain implements Serializable 
{
	
	private static final long serialVersionUID = -6161352923350643521L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="dashboard_scheme_mst")
	@TableGenerator(initialValue=1, allocationSize=1, name="dashboard_scheme_mst")
	@Column(name="num_id")
	int num_id;
		
	@Column(name="num_role_id",columnDefinition = "numeric(3)")
	int num_role_id;
	
	@Column(name="num_scheme_id", nullable=false,columnDefinition = "numeric(3)")
	int num_scheme_id;
	
	@Column(name="num_is_valid",columnDefinition = "numeric(1)")
	int num_is_valid=1;

	

	public int getNum_id() {
		return num_id;
	}



	public void setNum_id(int num_id) {
		this.num_id = num_id;
	}


	public int getNum_role_id() {
		return num_role_id;
	}



	public void setNum_role_id(int num_role_id) {
		this.num_role_id = num_role_id;
	}



	public int getNum_scheme_id() {
		return num_scheme_id;
	}



	public void setNum_scheme_id(int num_scheme_id) {
		this.num_scheme_id = num_scheme_id;
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
	
	
	
	

}

