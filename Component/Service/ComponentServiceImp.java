package Component.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Component.DAO.ComponentDAO;

@SuppressWarnings({"unused","unchecked","rawtypes","deprecation"})
@Service
public class ComponentServiceImp implements ComponentService {

	@Autowired
	ComponentDAO coDao;
	
	@SuppressWarnings("rawtypes")
	
	@Override
	public List<HashMap<String, String>> getTableData() {
		
		
		List<HashMap<String, String>> brcnh = new ArrayList<HashMap<String, String>>();
		List<Map<String,Object>> branchList= coDao.getTableData();
		if(branchList.size()!=0){
			for (Map<String, Object> tempRow : branchList) {
				HashMap brh = new HashMap();
				brh.put("num_sno",tempRow.get("num_sno")+"");
				brh.put("str_clause_desc",tempRow.get("str_clause_desc")+"");
				brh.put("str_component_type",tempRow.get("str_component_type")+"");
				brh.put("visibility_class",tempRow.get("visibility_class")+"");
				brh.put("observation_desc",tempRow.get("observation_desc")+"");
				brh.put("clause_seq_no",tempRow.get("clause_seq_no")+"");
				brh.put("is_remark_val",tempRow.get("is_remark_val")+"");
				
				brcnh.add(brh);
			}
		}
		
		return brcnh;
		
		//String ListofISNO = "";
        
//		List SILIst = new ArrayList();
//		SILIst = coDao.getTableData();
//		
//		HashMap hmDetails = new HashMap();
//		hmDetails.put("value", SILIst);
//		
//		ListofISNO.add(hmDetails);
//		
//		return ListofISNO;
	
	}
	
    public List<HashMap<String, String>> getTableDataEdit(String FinalApplicationId , int branchId) {
		List<HashMap<String, String>> brcnh = new ArrayList<HashMap<String, String>>();
		List<Map<String,Object>> branchList= coDao.getTableDataEdit(FinalApplicationId , branchId);
		if(branchList.size()!=0){
			for (Map<String, Object> tempRow : branchList) {
				HashMap brh = new HashMap();
				brh.put("num_sno",tempRow.get("num_sno")+"");
				brh.put("str_clause_desc",tempRow.get("str_clause_desc")+"");
				brh.put("str_component_type",tempRow.get("str_component_type")+"");
				brh.put("visibility_class",tempRow.get("visibility_class")+"");
				brh.put("observation_desc",tempRow.get("observation_desc")+"");
				brh.put("clause_seq_no",tempRow.get("clause_seq_no")+"");
				brh.put("is_remark_val",tempRow.get("is_remark_val")+"");
				
				
				if(tempRow.get("str_value")!=null)
				  brh.put("str_field_val",tempRow.get("str_value")+"");
				else
					brh.put("str_field_val","");
				
				if(tempRow.get("str_remarks")!=null)
					brh.put("str_remark_val",tempRow.get("str_remarks")+"");
				else
					brh.put("str_remark_val","");
				
				brcnh.add(brh);
			}
		}
		return brcnh;
	}
    
    public boolean isProdIs(String FinalApplicationId , int branchId) {
    	boolean isProdIsFlag = false;
		isProdIsFlag = coDao.getIsProdIs(FinalApplicationId , branchId);
		return isProdIsFlag;
	}
}
