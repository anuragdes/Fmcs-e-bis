package eBIS.CDAC.Utility.Service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eBIS.CDAC.Utility.Model.QueryExecuterModel;

public interface QueryExecuterService {

	List<Map<String, Object>> QueryExecuter(String sQLQueryRequest) throws Exception;
	
	@Transactional(propagation=Propagation.REQUIRED)
	void submitLog(QueryExecuterModel model);

}
