package exception.log.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import Global.CommonUtility.DAO.DaoHelper;
import exception.log.domain.ExceptionLog;
import exception.log.model.ExceptionLogModel;

@Repository
public class ExceptionLogDAO {
	@Autowired
	DaoHelper daoHelper;

	public ExceptionLogModel insertexceptionlog(ExceptionLogModel exceptionmodel) {
		ExceptionLog domain=new ExceptionLog();
		domain.setException_details(exceptionmodel.getException_details());
		domain.setUser_remarks(exceptionmodel.getUser_remarks());
		domain.setException_remarks(exceptionmodel.getException_remarks());
		domain.setRolename(exceptionmodel.getRolename());
		domain.setUsername(exceptionmodel.getUsername());
		domain.setBranchid(exceptionmodel.getBranchid());
		domain.setException_url(exceptionmodel.getException_url());
		domain=daoHelper.merge(ExceptionLog.class, domain);	
		exceptionmodel.setException_details(domain.getException_details());
		exceptionmodel.setUser_remarks(domain.getUser_remarks());
		exceptionmodel.setException_remarks(domain.getException_remarks());
		exceptionmodel.setException_date(domain.getException_date());
		exceptionmodel.setIs_valid(domain.getIs_valid());
		exceptionmodel.setNum_id(domain.getNum_id());
		return exceptionmodel;
	}

	public List<ExceptionLog> getUnsolvedExceptionLogList() {
		String sql=" select c from ExceptionLog c where c.is_valid=1 order by exception_date desc";
		List<ExceptionLog> list = daoHelper.findByQuery(sql);
		return list;
	}

	public List<ExceptionLog> getSolvedExceptionLogList() {
		String sql=" select c from ExceptionLog c where c.is_valid=0 order by exception_date desc";
		List<ExceptionLog> list = daoHelper.findByQuery(sql);
		return list;
	}

	public List<ExceptionLog> getExceptiondetails(String numid) {
		String sql=" select c from ExceptionLog c where c.num_id="+numid;
		List<ExceptionLog> list = daoHelper.findByQuery(sql);
		return list;
	}
}
