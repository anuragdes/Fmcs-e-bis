package Applicant.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Applicant.DAO.ApplicantpcDAO;

@Service
public class ApplicantpcServiceImp implements ApplicantpcService{
	@Autowired
	ApplicantpcDAO apppcdao;
	
	
	public List<HashMap<String,String>> getpcApplication(String fromdate,String todate,String isno,String BranchID,String rev)
	{
		
		List<HashMap<String,String>> listAction = new ArrayList<HashMap<String,String>>();
		listAction= apppcdao.getPCApplications(fromdate,todate,isno,BranchID,rev);
		HashMap hm = new HashMap();
		return listAction;
		
		
		
	}

}
