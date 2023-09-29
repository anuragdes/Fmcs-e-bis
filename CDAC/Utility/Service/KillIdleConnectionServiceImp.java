package eBIS.CDAC.Utility.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eBIS.CDAC.Utility.DAO.KillIdleConnectionDAO;

@Service
public class KillIdleConnectionServiceImp implements KillIdleConnectionService {

	@Autowired
	KillIdleConnectionDAO killIdleConnectionDAO;
	@Override
	public int KillIdleConnection() {
		return killIdleConnectionDAO.KillIdleConnection();
		
	}

}
