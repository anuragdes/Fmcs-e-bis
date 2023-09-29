package exception.log.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import exception.log.dao.ExceptionLogDAO;
import exception.log.domain.ExceptionLog;
import exception.log.model.ExceptionLogModel;

@Service
public class ExceptionLogServiceImp implements ExceptionLogService {
	@Autowired
	ExceptionLogDAO dao;

	@Override
	public ExceptionLogModel insertexceptionlog(ExceptionLogModel model) {
		return dao.insertexceptionlog(model);
	}

	@Override
	public List<ExceptionLog> getUnsolvedExceptionLogList() {
		return dao.getUnsolvedExceptionLogList();
	}

	@Override
	public List<ExceptionLog> getSolvedExceptionLogList() {
		return dao.getSolvedExceptionLogList();
	}

	@Override
	public List<ExceptionLog> getExceptiondetails(String numid) {
		return dao.getExceptiondetails(numid);
	}

}
