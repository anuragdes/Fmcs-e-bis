package eBIS.CDAC.Utility.Service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eBIS.CDAC.Utility.DAO.QueryExecuterDAO;
import eBIS.CDAC.Utility.Model.QueryExecuterModel;

@Service
public class QueryExecuterServiceImp implements QueryExecuterService {

	@Autowired
	QueryExecuterDAO dao;
	@Override
	public List<Map<String, Object>> QueryExecuter(String sQLQueryRequest) throws Exception{
		return dao.QueryExecuter(sQLQueryRequest);
	}
	@Override
	public void submitLog(QueryExecuterModel model) {
		dao.submitLog(model);
		
	}

}
