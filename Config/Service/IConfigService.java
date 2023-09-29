package Global.Config.Service;

import java.util.HashMap;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import Global.Registration.Domain.RegisterDomain;
import lab.domain.Lab_Type_Domain;

public interface IConfigService {

	@Transactional(propagation=Propagation.REQUIRED)
	public List<RegisterDomain> getBlockedUsers(int iLocationId, int iLocationTypeId);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public List<RegisterDomain> getUsernamesOfBlockedUsers(String stEmail_p);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public String unblockedUser(String stEmail, String stUsername_p, int userId);
	
	@Transactional(propagation=Propagation.REQUIRED)
	public String resendVerificationLink(String stEmail, String stUsername_p, int userId) throws MessagingException;

	@Transactional(propagation=Propagation.REQUIRED)
	public List<HashMap<String, Object>> getCredentialSummry();
	
	@Transactional(propagation=Propagation.REQUIRED)
	public List<HashMap<String, Object>> getCredentialSummryHM();

	@Transactional(propagation=Propagation.REQUIRED)
	public List<RegisterDomain> getUser(String stUsername);

	@Transactional(propagation=Propagation.REQUIRED)
	public List<HashMap<String, String>> getPcConsolidateRptData(int branch,int role_id,int UserId,String frmdate,String todate);
	
	//Mahendra
	@Transactional(propagation=Propagation.REQUIRED)
	List<Lab_Type_Domain> getLabTypeList();

    @Transactional(propagation=Propagation.REQUIRED)
	List<HashMap<String, String>> getBo();
    
    @Transactional(propagation=Propagation.REQUIRED)
	List<HashMap<String, String>> getLabName(String labId);
    
    @Transactional(propagation=Propagation.REQUIRED)
	List<HashMap<String, String>> getBISLabNameFrHead(String labId);
    
}
