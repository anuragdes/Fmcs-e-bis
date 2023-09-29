package eBIS.CDAC.Utility.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import eBIS.AppConfig.PrimaryDaoHelper;

@Repository
public class KillIdleConnectionDAO {

	@Autowired
	PrimaryDaoHelper primaryDaoHelper;

	public int KillIdleConnection() {
		String Sql="SELECT pg_terminate_backend(pid)\r\n"
				+ "FROM pg_stat_activity\r\n"
				+ "WHERE datname = 'bismanak'\r\n"
				+ "AND pid <> pg_backend_pid()\r\n"
				+ "AND state = 'idle'";
		return primaryDaoHelper.jdbcExecuteQuery(Sql);
	}
}
