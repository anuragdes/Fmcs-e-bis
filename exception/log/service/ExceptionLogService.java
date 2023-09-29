package exception.log.service;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import exception.log.domain.ExceptionLog;
import exception.log.model.ExceptionLogModel;

public interface ExceptionLogService {
	@Transactional(propagation=Propagation.REQUIRED)
	ExceptionLogModel insertexceptionlog(ExceptionLogModel exceptionmodel);

	List<ExceptionLog> getUnsolvedExceptionLogList();

	List<ExceptionLog> getSolvedExceptionLogList();

	List<ExceptionLog> getExceptiondetails(String numid);
}
