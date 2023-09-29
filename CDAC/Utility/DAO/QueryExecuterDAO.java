package eBIS.CDAC.Utility.DAO;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import Global.CommonUtility.DAO.DaoHelper;
import eBIS.CDAC.Utility.Entity.QueryExecuter;
import eBIS.CDAC.Utility.Model.QueryExecuterModel;
import mobileAPI.controller.MobileCmlInspPeriodicDtlDetailsStockController;

@Repository
public class QueryExecuterDAO {

	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	public DaoHelper daoHelper;
	@Autowired
	MobileCmlInspPeriodicDtlDetailsStockController controller;
	public List<Map<String, Object>> QueryExecuter(String sQLQueryRequest) throws Exception {
		String sql="select * from ( "+sQLQueryRequest+" ) limit 100";
		  List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		  return list;
	}

	public void submitLog(QueryExecuterModel model) {
		QueryExecuter domain=new QueryExecuter();
		domain.setSql_query_request(model.getSql_query_request());
		domain.setSql_query_response(model.getSql_query_response());
		daoHelper.merge(QueryExecuter.class, domain);
	}

}
