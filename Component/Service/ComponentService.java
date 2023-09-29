package Component.Service;

import java.util.HashMap;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface ComponentService {
	@SuppressWarnings("rawtypes")
	@Transactional(propagation=Propagation.REQUIRED)
	public List<HashMap<String,String>> getTableData();
	public List<HashMap<String,String>> getTableDataEdit(String FinalApplicationId , int branchId);
	public boolean isProdIs(String FinalApplicationId , int branchId);
}
