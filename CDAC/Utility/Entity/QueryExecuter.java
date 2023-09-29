package eBIS.CDAC.Utility.Entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Type;

@Entity
@Table(name="QueryExecuter", schema="bis_log")
public class QueryExecuter implements Serializable {
	private static final long serialVersionUID = -7283538366040130696L;

	@Id			
	@GeneratedValue(strategy=GenerationType.TABLE, generator="queryexecuter")
	@TableGenerator(name="queryexecuter", initialValue=1, allocationSize=1)
	private int num_id;

	private Date dt_entry_date=new Date();
	
	@Type(type="text")
	private String sql_query_request="";
	
	@Type(type="text")
	private String sql_query_response="";

	public int getNum_id() {
		return num_id;
	}

	public void setNum_id(int num_id) {
		this.num_id = num_id;
	}

	public Date getDt_entry_date() {
		return dt_entry_date;
	}

	public void setDt_entry_date(Date dt_entry_date) {
		this.dt_entry_date = dt_entry_date;
	}

	public String getSql_query_request() {
		return sql_query_request;
	}

	public void setSql_query_request(String sql_query_request) {
		this.sql_query_request = sql_query_request;
	}

	public String getSql_query_response() {
		return sql_query_response;
	}

	public void setSql_query_response(String sql_query_response) {
		this.sql_query_response = sql_query_response;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "QueryExecuter [num_id=" + num_id + ", dt_entry_date=" + dt_entry_date + ", sql_query_request="
				+ sql_query_request + ", sql_query_response=" + sql_query_response + "]";
	}
	
}
