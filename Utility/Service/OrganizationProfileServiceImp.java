package eBIS.Utility.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Schemes.ProductCertification.ApplicationSubmission.Domain.user_profile_domain;
import eBIS.AppConfig.FileUploadPathPropertiesBundleFile;
import eBIS.AppConfig.FtpConfiguration;
import eBIS.PC.OrganizationProfile.DAO.PCOrganizationProfileDAO;
import eBIS.Utility.DAO.OrganizationProfileDAO;
import eBIS.Utility.Model.OrganizationProfileModel;
import lombok.var;

@Service
public class OrganizationProfileServiceImp implements OrganizationProfileService {
	@Autowired
	OrganizationProfileDAO dao;
	@Autowired
	PCOrganizationProfileDAO pcOrganizationProfileDAO;
	final String PCProfileUploadPath = FileUploadPathPropertiesBundleFile.getValueFromKey("pc.profile.upload.path");
	@Autowired
	FtpConfiguration ftpConfiguration;
	@Override
	public List<user_profile_domain> getOrganizationProfileDetails(long userId) {
		return dao.getOrganizationProfileDetails(userId);
	}
	@Override
	public FastList<OrganizationProfileModel> getOrganizationProfileModelDetails(long userId) {
		var list= dao.getOrganizationProfileDetails(userId);
		return ConvertOrganizationProfileEntitytoModel(list);

	}
	private FastList<OrganizationProfileModel> ConvertOrganizationProfileEntitytoModel(List<user_profile_domain> list) {
		FastList<OrganizationProfileModel> model=new FastList<OrganizationProfileModel>();
		for(int i=0;i<list.size();i++) {
			var tempmodel=list.get(i);
			OrganizationProfileModel temp=new OrganizationProfileModel();
			temp.setDicRegNumber(tempmodel.getStr_dic_reg_number());
			temp.setDicRegNumberDate(tempmodel.getDt_dic_reg_date());
			temp.setGstNumber(tempmodel.getStr_gst_num());
			temp.setPanNumber(tempmodel.getStr_PAN_Number());
			var sectorName=dao.getsectorName(tempmodel.getNum_sector_id());
			temp.setSectorId(tempmodel.getNum_sector_id()+"");
			temp.setSectorName(sectorName);
			temp.setScaleId(tempmodel.getNum_scale_id()+"");
			var scaleName=dao.getscaleName(tempmodel.getNum_scale_id());
			temp.setScaleName(scaleName);
			temp.setUserId(tempmodel.getNum_entry_user_id()+"");
			temp.setFactoryAddress1(tempmodel.getStr_factory_address_line1());
			temp.setFactoryAddress2(tempmodel.getStr_factory_address_line2());
			temp.setFirmAddress1(tempmodel.getStr_firm_address_line1());
			temp.setFirmAddress2(tempmodel.getStr_firm_address_line2());
			temp.setBusinessLicenceNumber(tempmodel.getStr_business_license_no());
			if(tempmodel.getStr_scale_document_name()!=null ) {
				if(!tempmodel.getStr_scale_document_name().trim().equalsIgnoreCase(""))
				{
					temp.setScaleDoc(tempmodel.getStr_scale_document_name());
				}else {
					temp.setScaleDoc("0");	
				}
			}else {
				temp.setScaleDoc("0");
			}
			temp.setWomenEnterprenaurFlag(tempmodel.getNum_women_enterprenaur());
			if(tempmodel.getNum_women_enterprenaur()!=null) {
				temp.setWomenEnterprenaurFlag(tempmodel.getNum_women_enterprenaur());	
			}else {
				temp.setWomenEnterprenaurFlag("");
			}
			if(tempmodel.getNum_startup()!=null)
			{
				temp.setStartupFlag(tempmodel.getNum_startup());
			}else {
				temp.setStartupFlag("");
			}
			if(tempmodel.getStr_women_enterprenaur_doc()!=null ) {
				if(!tempmodel.getStr_women_enterprenaur_doc().trim().equalsIgnoreCase(""))
				{
					temp.setWomenEnterprenaurDoc(tempmodel.getStr_women_enterprenaur_doc());
				}else {
					temp.setWomenEnterprenaurDoc("0");	
				}
			}else {
				temp.setWomenEnterprenaurDoc("0");
			}
			if(tempmodel.getStr_startup_doc()!=null ) {
				if(!tempmodel.getStr_startup_doc().trim().equalsIgnoreCase(""))
				{
					temp.setStartupDoc(tempmodel.getStr_startup_doc());
				}else {
					temp.setStartupDoc("0");	
				}
			}else {
				temp.setStartupDoc("0");
			}
			if(tempmodel.getStr_nature_of_firm()!=null) {
				String natureFirm=tempmodel.getStr_nature_of_firm().trim().toString();
				if(!natureFirm.equalsIgnoreCase("")) {
					if(natureFirm.equalsIgnoreCase("other")) {
						temp.setNatureFirm(natureFirm+"<br>"+tempmodel.getStr_nature_of_firm_other_text());
					}else {
						temp.setNatureFirm(natureFirm);	
					}
				}else {
					temp.setNatureFirm("");	
				}
			}else {
				temp.setNatureFirm("");
			}
			var proofEstFirm=tempmodel.getNum_reg_document_id();

			if(proofEstFirm!=0) {
				var regDocName=dao.getRegDocName(proofEstFirm);
				if(proofEstFirm==8) {
					temp.setProofEstFirm(regDocName+"<br>"+tempmodel.getStr_proof_of_est_firm_value_other_text());

				}else {
				temp.setProofEstFirm(regDocName);	
				}
			}else {
				temp.setProofEstFirm("");
			}
			if(tempmodel.getStr_Reg_document_name()!=null) {
				String regDocName = tempmodel.getStr_Reg_document_name().trim();
				if(!regDocName.equalsIgnoreCase("")) {
					temp.setProofEstFirmOtherDoc(regDocName);
				}else {
					temp.setProofEstFirmOtherDoc("0");	
				}
			}else {
				temp.setProofEstFirmOtherDoc("0");
			}
			temp=factoryDetails(tempmodel,temp);
			temp=firmDetails(tempmodel,temp);
			model.add(temp);
		}
		return model;
	}
	private OrganizationProfileModel factoryDetails(user_profile_domain tempmodel, OrganizationProfileModel temp) {
		String factoryAddress=tempmodel.getStr_factory_address_line1()+"<br>"+tempmodel.getStr_factory_address_line2();
		temp.setFactoryAddress(factoryAddress);
		if(tempmodel.getStr_factory_address_hindi()!=null) {
			if(!tempmodel.getStr_factory_address_hindi().trim().equalsIgnoreCase("")) {
				temp.setFactoryAddressHindi(tempmodel.getStr_factory_address_hindi());
			}else {
				temp.setFactoryAddressHindi("");
			}
		}else {
			temp.setFactoryAddressHindi("");
		}
		var factoryStateId=tempmodel.getNum_factory_state_id();
		if(factoryStateId!=0) {
			temp.setFactoryStateId(factoryStateId+"");
			String factoryStateName=dao.getstateName(factoryStateId);
			temp.setFactoryStateName(factoryStateName);
		}else {
			temp.setFactoryStateName("");
		}
		var factoryDistrictId=tempmodel.getNum__factory_district_id();
		if(factoryDistrictId!=0) {
			temp.setFactoryDistrictId(factoryDistrictId+"");
			String factoryDistrictName = dao.getdistrictName(factoryDistrictId);
			temp.setFactoryDistrictName(factoryDistrictName);
		}else {
			temp.setFactoryDistrictId("0");
			temp.setFactoryDistrictName("");
		}
		if(tempmodel.getNum_factory_pincode()!=null) {
		String factorypinCode=	tempmodel.getNum_factory_pincode().trim();
		if(!factorypinCode.equalsIgnoreCase("")) {
			temp.setFactoryPinCode(factorypinCode);
		}else {
			temp.setFactoryPinCode("");
		}
		}else {
			temp.setFactoryPinCode("");
		}
		if(tempmodel.getStr_factory_city_name()!=null) {
			String factoryCityname=	tempmodel.getStr_factory_city_name().trim();
				if(!factoryCityname.equalsIgnoreCase("")) {
					temp.setFactoryCityName(factoryCityname);
				}else {
					temp.setFactoryCityName("");
				}
			}else {
				temp.setFactoryCityName("");
		}
		if(tempmodel.getStr_factory_email()!=null) {
			String factoryEmail=	tempmodel.getStr_factory_email().trim();
				if(!factoryEmail.equalsIgnoreCase("")) {
					temp.setFactoryEmail(factoryEmail);
				}else {
					temp.setFactoryEmail("");
				}
			}else {
				temp.setFactoryEmail("");
		}
		var factoryLandline ="";
		var factoryMobile ="";
		var factoryLandlineFlag=-1;
		var factoryMobileFlag=-1;
		if(tempmodel.getStr_factory_landline()!=null) {
			factoryLandline =   tempmodel.getStr_factory_landline().trim();
			if(!factoryLandline.equalsIgnoreCase(""))
			{
				factoryLandlineFlag=1;
			}else {
				factoryLandlineFlag=0;
			}
		}else {
			factoryLandlineFlag=0;
		}
		if(tempmodel.getStr_factory_mobile()!=null) {
			factoryMobile = tempmodel.getStr_factory_mobile().trim();
			if(!factoryMobile.equalsIgnoreCase(""))
			{
				factoryMobileFlag=1;
			}else {
				factoryMobileFlag=0;
			}
		}else {
			factoryMobileFlag=0;
		}
		if(factoryMobileFlag==1 && factoryLandlineFlag==1) {
			temp.setFactoryContactNumber("LandLine: "+tempmodel.getStr_factory_std_code()+"-"+factoryLandline+"<br>Mobile: "+factoryMobile);
		}else {
			if(factoryLandlineFlag==1) {
				temp.setFactoryContactNumber("LandLine: "+factoryLandline);
			}
			if(factoryMobileFlag==1) {
				temp.setFactoryContactNumber("Mobile: "+factoryMobile);	
			}
		}
		if(tempmodel.getLongitude()!=null) {
			temp.setFactoryLongitude(tempmodel.getLongitude()+"");
		}else {
			temp.setFactoryLongitude("");
		}
		if(tempmodel.getLatitude()!=null) {
			temp.setFactoryLatitude(tempmodel.getLatitude()+"");
		}else {
			temp.setFactoryLatitude("");
		}
		if(tempmodel.getNum_address_proof_factory_document_id()!=null) {
			String factoryAddressDocId = tempmodel.getNum_address_proof_factory_document_id().trim();
			
			if(!factoryAddressDocId.equalsIgnoreCase("")) {
				
				var factoryAddressDocName=dao.getAddressDocName(factoryAddressDocId);
				if(factoryAddressDocId.equalsIgnoreCase("18")) {
					temp.setFactoryAddressDocId(factoryAddressDocId);
					temp.setFactoryAddressDocName(factoryAddressDocName+"<br>"+tempmodel.getStr_factory_address_proof_other_Text());
				}else {
					temp.setFactoryAddressDocId(factoryAddressDocId);
					temp.setFactoryAddressDocName(factoryAddressDocName);	
				}
				
			}else {
				temp.setFactoryAddressDocName("");
			}
		}else {
			temp.setFactoryAddressDocName("");
		}
		if(tempmodel.getStr_factory_address_document_name()!=null) {
			var factoryAddressDocFile=tempmodel.getStr_factory_address_document_name().trim();
			if(!factoryAddressDocFile.equalsIgnoreCase("")) {
				temp.setFactoryAddressDocFile(factoryAddressDocFile);
			}else {
				temp.setFactoryAddressDocFile("0");	
			}
		}else {
			temp.setFactoryAddressDocFile("0");
		}
		temp.setFactoryLatitude(tempmodel.getFactorylatitude());
		temp.setFactoryLongitude(tempmodel.getFactorylongitude());
		
		return temp;
	}
	private OrganizationProfileModel firmDetails(user_profile_domain domain, OrganizationProfileModel temp) {
		String FirmAddress=domain.getStr_firm_address_line1()+"<br>"+domain.getStr_firm_address_line2();
		temp.setFirmAddress(FirmAddress);
		if(domain.getStr_firm_name_hindi()!=null) {
			if(!domain.getStr_firm_name_hindi().trim().equalsIgnoreCase("")) {
				temp.setFirmNameHindi(domain.getStr_firm_name_hindi());
			}else {
				temp.setFirmNameHindi("");
			}
		}else {
			temp.setFirmNameHindi("");
		}
		
		if(domain.getStr_firm_address_hindi()!=null) {
			if(!domain.getStr_firm_address_hindi().trim().equalsIgnoreCase("")) {
				temp.setFirmAddressHindi(domain.getStr_firm_address_hindi());
			}else {
				temp.setFirmAddressHindi("");
			}
		}else {
			temp.setFirmAddressHindi("");
		}
		if(domain.getStr_firm_name()!=null)
		{
			temp.setFirmName(domain.getStr_firm_name());
		}else {
			temp.setFirmName("");
		}
		if(domain.getStr_firm_ceo_name()!=null)
		{
			temp.setFirmCEOName(domain.getStr_firm_ceo_name());
		}else {
			temp.setFirmCEOName("");
		}
		var FirmStateId=domain.getNum_firm_state_id();
		if(FirmStateId!=0) {
			temp.setFirmStateId(FirmStateId+"");
			String FirmStateName=dao.getstateName(FirmStateId);
			temp.setFirmStateName(FirmStateName);
		}else {
			temp.setFirmStateName("");
		}
		var FirmDistrictId=domain.getNum__firm_district_id();
		if(FirmDistrictId!=0) {
			temp.setFirmDistrictId(FirmDistrictId+"");
			String FirmDistrictName = dao.getdistrictName(FirmDistrictId);
			temp.setFirmDistrictName(FirmDistrictName);
		}else {
			temp.setFirmDistrictId("0");
			temp.setFirmDistrictName("");
		}
		if(domain.getNum_firm_pincode()!=null) {
		String FirmpinCode=	domain.getNum_firm_pincode().trim();
		if(!FirmpinCode.equalsIgnoreCase("")) {
			temp.setFirmPinCode(FirmpinCode);
		}else {
			temp.setFirmPinCode("");
		}
		}else {
			temp.setFirmPinCode("");
		}
		if(domain.getStr_firm_city_name()!=null) {
			String FirmCityname=	domain.getStr_firm_city_name().trim();
				if(!FirmCityname.equalsIgnoreCase("")) {
					temp.setFirmCityName(FirmCityname);
				}else {
					temp.setFirmCityName("");
				}
			}else {
				temp.setFirmCityName("");
		}
		if(domain.getStr_firm_email()!=null) {
			String FirmEmail=	domain.getStr_firm_email().trim();
				if(!FirmEmail.equalsIgnoreCase("")) {
					temp.setFirmEmail(FirmEmail);
				}else {
					temp.setFirmEmail("");
				}
			}else {
				temp.setFirmEmail("");
		}
		var FirmLandline ="";
		var FirmMobile ="";
		var FirmLandlineFlag=-1;
		var FirmMobileFlag=-1;
		if(domain.getStr_firm_landline()!=null) {
			FirmLandline =   domain.getStr_firm_landline().trim();
			if(!FirmLandline.equalsIgnoreCase(""))
			{
				FirmLandlineFlag=1;
			}else {
				FirmLandlineFlag=0;
			}
		}else {
			FirmLandlineFlag=0;
		}
		if(domain.getStr_firm_mobile()!=null) {
			FirmMobile = domain.getStr_firm_mobile().trim();
			if(!FirmMobile.equalsIgnoreCase(""))
			{
				FirmMobileFlag=1;
			}else {
				FirmMobileFlag=0;
			}
		}else {
			FirmMobileFlag=0;
		}
		if(FirmMobileFlag==1 && FirmLandlineFlag==1) {
			temp.setFirmContactNumber("LandLine: "+domain.getStr_firm_std_code()+"-"+FirmLandline+"<br>Mobile: "+FirmMobile);
		}else {
			if(FirmLandlineFlag==1) {
				temp.setFirmContactNumber("LandLine: "+FirmLandline);
			}
			if(FirmMobileFlag==1) {
				temp.setFirmContactNumber("Mobile: "+FirmMobile);	
			}
		}
		if(domain.getLongitude()!=null) {
			temp.setFirmLongitude(domain.getLongitude()+"");
		}else {
			temp.setFirmLongitude("");
		}
		if(domain.getLatitude()!=null) {
			temp.setFirmLatitude(domain.getLatitude()+"");
		}else {
			temp.setFirmLatitude("");
		}
		if(domain.getNum_address_proof_firm_document_id()!=null) {
			String FirmAddressDocId = domain.getNum_address_proof_firm_document_id().trim();
			
			if(!FirmAddressDocId.equalsIgnoreCase("")) {
				
				var FirmAddressDocName=dao.getAddressDocName(FirmAddressDocId);
				if(FirmAddressDocId.equalsIgnoreCase("18")) {
					temp.setFirmAddressDocId(FirmAddressDocId);
					temp.setFirmAddressDocName(FirmAddressDocName+"<br>"+domain.getFirmAddressProofDocOthertext());
				}else {
					temp.setFirmAddressDocId(FirmAddressDocId);
					temp.setFirmAddressDocName(FirmAddressDocName);	
				}
				
			}else {
				temp.setFirmAddressDocName("");
			}
		}else {
			temp.setFirmAddressDocName("");
		}
		if(domain.getStr_firm_address_document_name()!=null) {
			var FirmAddressDocFile=domain.getStr_firm_address_document_name().trim();
			if(!FirmAddressDocFile.equalsIgnoreCase("")) {
				temp.setFirmAddressDocFile(FirmAddressDocFile);
			}else {
				temp.setFirmAddressDocFile("0");	
			}
		}else {
			temp.setFirmAddressDocFile("0");
		}
		return temp;
	}
	@Override
	public InputStream DownloadPCOrgProfileFile(FTPClient ftpClient ,String userID, String fileName) throws FTPConnectionClosedException, IOException, Exception {
		String path=PCProfileUploadPath+userID+"/";
		var istream=ftpConfiguration.downloadfromFtp(path, fileName);//removed argument type ftpClient on 5/12/22 because argument no declare in method is 2 only
		return istream;
	}
	@Override
	public int checkOrganizationProfile(long userId) {
		var list = getOrganizationProfileModelDetails(userId);
		var editFlag = -1;
		for (int i = 0; i < list.size(); i++) {
			var model = list.get(i);
			if (model.getFirmName().equalsIgnoreCase("") || model.getScaleId().equalsIgnoreCase("0")
					|| model.getSectorId().equalsIgnoreCase("0") || model.getFirmCEOName().equalsIgnoreCase("")
					|| model.getFirmDistrictId().equalsIgnoreCase("0")
					|| model.getFirmDistrictName().equalsIgnoreCase("")
					|| model.getFirmAddressDocFile().equalsIgnoreCase("0")
					|| model.getFactoryDistrictId().equalsIgnoreCase("0")
					|| model.getFactoryDistrictName().equalsIgnoreCase("")
					|| model.getFactoryAddressDocFile().equalsIgnoreCase("0")
					|| model.getStartupFlag().equalsIgnoreCase("")
					|| model.getWomenEnterprenaurFlag().equalsIgnoreCase("")
					|| model.getFirmLongitude().equalsIgnoreCase("")
					|| model.getFirmLatitude().equalsIgnoreCase("")
					|| model.getFactoryLatitude().equalsIgnoreCase("")
					|| model.getFactoryLatitude().equalsIgnoreCase("")
			) {
				editFlag = 1;
			} else {
				editFlag = 0;
			}
		}
		return editFlag;
	}



}