package Applicant.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import Applicant.Model.RawMaterialModel;
import Global.CommonUtility.DAO.DaoHelper;
import Global.Login.Service.IMigrateService;
import Schemes.ProductCertification.ApplicationSubmission.Domain.manufacturingprocessdomain;

@Service
public class RawMaterialServiceImpl implements RawMaterialService {
	
	@Autowired
	public DaoHelper daoHelper;
	
	@Autowired
	JdbcTemplate jdbctemplete;

	@Autowired
	IMigrateService ims;
	
	@Override
	public List<Map<String, Object>> getLatestRawMaterila(String app_no,String lic_no, int branchID) {
		
		String qry="select num_id,str_raw_material,str_name_supplier,str_conformity_of_material,str_nature_of_package,str_records_maintained from bis_dev.pc_manufacturing_process_dtls m  where m.str_application_id='"+app_no+"' and m.num_branch_id='"+branchID+"'";
	//	System.out.println("#33    ----------- "+qry);
		return jdbctemplete.queryForList(qry);
	}

	@Override
	public boolean saveRawMaterialDetails(RawMaterialModel rawraterialrodel) {

		try {
			//System.out.println("#41   "+ rawraterialrodel);
		//	System.out.println("cml  "+ims.Dcrypt(rawraterialrodel.getStr_cml_no()));
		//	System.out.println("app  "+ims.Dcrypt(rawraterialrodel.getstr_app_id()));
			manufacturingprocessdomain dom = new manufacturingprocessdomain();
			dom.setBranchId(Integer.parseInt(ims.Dcrypt(rawraterialrodel.getstr_branch_id()).trim()));
			dom.setConformityMaterial(rawraterialrodel.getStr_Conformity());
			dom.setDateentryBy(new Date());
			dom.setIsValid(1);
			dom.setLnApplicationId(ims.Dcrypt(rawraterialrodel.getstr_app_id()).trim());
			dom.setNameSupplier(rawraterialrodel.getStr_Supplier());
			dom.setNaturePackage(rawraterialrodel.getStr_NaturePackage());
			dom.setRawMaterial(rawraterialrodel.getStr_RawMaterila());
			dom.setRecordsMaintained(rawraterialrodel.getStr_Records());
			dom.setStrGblUserId(rawraterialrodel.getuser_id());
			dom.setStr_conformity_of_material_other_text(rawraterialrodel.getStr_Others());
			dom.setStr_cml_no(ims.Dcrypt(rawraterialrodel.getStr_cml_no()).trim());
			daoHelper.merge(manufacturingprocessdomain.class, dom);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public boolean deleteRawMaterial(int id) {
		try {			
			String qry="DELETE from manufacturingprocessdomain where Id="+id;
			daoHelper.deleteByQuery(qry);
			return true;			
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	

}
