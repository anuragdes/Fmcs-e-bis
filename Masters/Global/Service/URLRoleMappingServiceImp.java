package eBIS.Masters.Global.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eBIS.Masters.Global.DAO.URLRoleMappingDAO;

@Service
public class URLRoleMappingServiceImp implements URLRoleMappingService {

	@Autowired
	URLRoleMappingDAO urlRoleMappingDAO;

	@Override
	public int checkRoleAuthorization(String urlName,String roleId) {
		return urlRoleMappingDAO.checkRoleAuthorization(urlName,roleId);
		
	}
}
