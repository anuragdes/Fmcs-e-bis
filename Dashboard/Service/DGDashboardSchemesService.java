package Global.Dashboard.Service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface DGDashboardSchemesService {
	@Transactional(propagation=Propagation.REQUIRED)
	public List<LinkedHashMap<String, String>> getSchemesForRoleId(int iRoleId);

}
