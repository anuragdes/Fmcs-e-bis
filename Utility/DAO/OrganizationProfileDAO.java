package eBIS.Utility.DAO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import Masters.Domain.City_Mst_Domain;
import Masters.Domain.District_Mst_Domain;
import Masters.Domain.Scale_Mst_Domain;
import Masters.Domain.Sector_Mst_Domain;
import Masters.Domain.State_Mst_Domain;
import Schemes.ProductCertification.ApplicationSubmission.Domain.address_proof_doc_mst;
import Schemes.ProductCertification.ApplicationSubmission.Domain.profile_Reg_Dtl_Doc_Domain;
import Schemes.ProductCertification.ApplicationSubmission.Domain.user_profile_domain;
import eBIS.AppConfig.PrimaryDaoHelper;

@Repository
public class OrganizationProfileDAO {
	@Autowired
	PrimaryDaoHelper daoHelper;
	
	public List<user_profile_domain> getOrganizationProfileDetails(long userId) {
		String query = "select r from user_profile_domain r where r.num_id =(select max(b.num_id) from user_profile_domain b where b.num_entry_user_id="+userId+") ";
		return daoHelper.findByQuery(query);

	}

	public String getsectorName(int num_sector_id) {
		String query="select c from Sector_Mst_Domain c where c.sectorId="+num_sector_id;
		List<Sector_Mst_Domain> list = daoHelper.findByQuery(query);
		String sectorName="";
		if(list.size()>0) {
			sectorName=list.get(0).getSector_name();
		}
		return sectorName;
	}
	public String getscaleName(int num_scale_id) {
		String query="select c from Scale_Mst_Domain c where c.scale_Id="+num_scale_id;
		List<Scale_Mst_Domain> list = daoHelper.findByQuery(query);
		String scaleName="";
		if(list.size()>0) {
			scaleName=list.get(0).getScale_Name();
		}
		return scaleName;
	}
	public String getstateName(int num_scale_id) {
		String query="select c from State_Mst_Domain c where c.numStateId="+num_scale_id;
		List<State_Mst_Domain> list = daoHelper.findByQuery(query);
		String stateName="";
		if(list.size()>0) {
			stateName=list.get(0).getStrStateName();
		}
		return stateName;
	}
	public String getcityName(int num_scale_id) {
		String query="select c from City_Mst_Domain c where c.numCityId="+num_scale_id;
		List<City_Mst_Domain> list = daoHelper.findByQuery(query);
		String cityName="";
		if(list.size()>0) {
			cityName=list.get(0).getStrCityName();
		}
		return cityName;
	}
	public String getdistrictName(int num_scale_id) {
		String query="select c from District_Mst_Domain c where c.numDistrictId="+num_scale_id;
		List<District_Mst_Domain> list = daoHelper.findByQuery(query);
		String districtName="";
		if(list.size()>0) {
			districtName=list.get(0).getDistrictname();
		}
		return districtName;
	}

	public String getRegDocName(int proofEstFirmid) {
		String query="select c from profile_Reg_Dtl_Doc_Domain c where c.num_id="+ proofEstFirmid;
		List<profile_Reg_Dtl_Doc_Domain> list = daoHelper.findByQuery(query);
		String regDocName="";
		if(list.size()>0) {
			regDocName=list.get(0).getStr_document_type();
		}
		return regDocName;
	}

	public String getAddressDocName(String factoryAddressDocId) {
		String query="select c from address_proof_doc_mst c where c.num_id="+ factoryAddressDocId;
		List<address_proof_doc_mst> list = daoHelper.findByQuery(query);
		String factoryAddressDocName="";
		if(list.size()>0) {
			factoryAddressDocName=list.get(0).getStr_document_type();
		}
		return factoryAddressDocName;
	}
	
}
