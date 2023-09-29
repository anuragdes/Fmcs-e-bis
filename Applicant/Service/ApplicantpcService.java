package Applicant.Service;

import java.util.HashMap;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface ApplicantpcService {

	@Transactional(propagation=Propagation.REQUIRED)
	public List<HashMap<String,String>> getpcApplication(String fromdate,String todate,String isno,String BranchID,String rev);
	
	
}
