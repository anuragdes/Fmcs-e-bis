package eBIS.CDAC.Utility.Model;

import java.util.Date;

public class QueryExecuterModel {
	private int num_id;
	private Date dt_entry_date;
	private String sql_query_request="";
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
	@Override
	public String toString() {
		return "QueryExecuterModel [num_id=" + num_id + ", dt_entry_date=" + dt_entry_date + ", sql_query_request="
				+ sql_query_request + ", sql_query_response=" + sql_query_response + "]";
	}
	
}
