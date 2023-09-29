
package eBIS.Masters.PC.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Masters.Domain.Scale_Mst_Domain;
import Masters.Domain.State_Mst_Domain;
import Masters.Domain.standard_mst_domain;
import eBIS.Masters.PC.DAO.FeeRebatePCDAO;
import eBIS.Masters.PC.Entity.FeeRebatePC;
import eBIS.Masters.PC.Model.FeeRebatePCModel;
import lombok.var;

@Service
public class FeeRebatePCServiceImp implements FeeRebatePCService {

	@Autowired
	FeeRebatePCDAO dao;
	@Override
	public FastList<FeeRebatePCModel> feeRebatePC(FeeRebatePCModel model) {
		List<FeeRebatePC> list = dao.feeRebatePC();
		dao.updateTileCount(list,model);
		FastList<FeeRebatePCModel> fastlist = ConvertEntityToModel(list);
		return fastlist;
	}
	private FastList<FeeRebatePCModel> ConvertEntityToModel(List<FeeRebatePC> list) {
		FastList<FeeRebatePCModel> model=new FastList<FeeRebatePCModel>();
		for(int i=0;i<list.size();i++) {
			FeeRebatePC domain = list.get(i);
			FeeRebatePCModel tempModel=new FeeRebatePCModel();
			tempModel.setActiveflag(domain.getActiveflag());
			tempModel.setApplicenseflag(domain.getApplicenseflag());
			tempModel.setBaseamount(domain.getBaseamount());
			tempModel.setDiscountpercentage(domain.getDiscountpercentage());
			tempModel.setFromdate(Date2String(domain.getFromdate()));
			tempModel.setScaleid(domain.getScaleid());
			tempModel.setStandardnumber(domain.getStandardnumber());
			tempModel.setOptionaldiscount(domain.getOptionaldiscount());
			tempModel.setStateid(domain.getStateid());
			tempModel.setTodate(Date2String(domain.getTodate()));
			tempModel.setUserid(domain.getUserid());
			tempModel.setBasescaleid(domain.getBasescaleid());
			tempModel.setRecurringnumber(domain.getRecurringnumber());
			tempModel.setNumid(domain.getNumid());
			tempModel.setDiscountdescription(domain.getDiscountdescription());
			model.add(tempModel);
		}
		return model;
	}
	
	private FeeRebatePC ConvertModelToEntity(FeeRebatePCModel model) {
		FeeRebatePC domain = new FeeRebatePC();
		domain.setApplicenseflag(model.getApplicenseflag());
		domain.setBaseamount(model.getBaseamount());
		domain.setDiscountpercentage(model.getDiscountpercentage());
		domain.setFromdate(String2Date(model.getFromdate()));
		domain.setScaleid(model.getScaleid());
		domain.setStandardnumber(model.getStandardnumber());
		  int optionalDiscount = model.getOptionaldiscount();
		domain.setOptionaldiscount(optionalDiscount);
		if(optionalDiscount==1) {
			domain.setWomenentrepreneurFlag("1");
			domain.setStartupFlag("0,1");
		}else {
			if(optionalDiscount==2) {
				domain.setWomenentrepreneurFlag("0,1");
				domain.setStartupFlag("1");	
			}else {
				if(optionalDiscount==3) {
					domain.setWomenentrepreneurFlag("1");
					domain.setStartupFlag("1");
				}else {
					if(optionalDiscount==4) {
						domain.setWomenentrepreneurFlag("0");
						domain.setStartupFlag("0");	
					}else {
					domain.setWomenentrepreneurFlag("0,1");
					domain.setStartupFlag("0,1");
					}
				}
			}
		}
		domain.setDiscountdescription(model.getDiscountdescription());
		domain.setStateid(model.getStateid());
		domain.setTodate(String2Date(model.getTodate().toString()));
		domain.setUserid(model.getUserid());
		domain.setBasescaleid(model.getBasescaleid());
		domain.setRecurringnumber(model.getRecurringnumber());
		return domain;
	}
	Date String2Date(String dateString) {
	    Date date=null;
	    try {
	    	date=new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
	    }catch(Exception ex) {
	    	ex.printStackTrace();
	    }
	    return date;
	}
	@Override
	public List<Scale_Mst_Domain> getScaleList() {
		return dao.getScaleList();
	}
	@Override
	public List<State_Mst_Domain> getStateList() {
		return dao.getStateList();
	}
	@Override
	public List<standard_mst_domain> getStandardMasterList() {
		return dao.getStandardMasterList();
		
	}
	@Override
	public FastList<FeeRebatePCModel> addfeeRebatePC(FeeRebatePCModel model) {
		FeeRebatePC domain = ConvertModelToEntity(model);
		dao.addfeeRebatePC(domain);
		return feeRebatePC(model);
		
	}
	@Override
	public String getScaleName(String scaleid) {
		String scaleName="";
		if(!scaleid.equalsIgnoreCase("all")) {
			scaleName=dao.getScaleName(scaleid);
		}else {
			scaleName="ALL";
		}
		
		return scaleName;
		
	}
	@Override
	public String getStateName(String stateid) {
		String statename="";
		if(stateid.equalsIgnoreCase("all")){
			statename="ALL";
		}else {
			if(stateid.equalsIgnoreCase("36,35,34,32,31,29,28,27,26,21,20,19,18,17,16,15,14,13,12,11,10,9,8,7,6,5,2,1,37")) {
				statename="ALL Non NRE States";
			}else {
				if(stateid.equalsIgnoreCase("33,30,25,24,23,22,4,3")) {
					statename="ALL NRE States";
				}else {
					statename=dao.getStateName(stateid);		
				}
			}
			
		}
		return statename;

	}
	@Override
	public String getStandardName(String standardnumber) {
		var standardName="";
		if(standardnumber.equalsIgnoreCase("all")) {
			standardName="ALL";
		}else {
			standardName=standardnumber;	
		}
		
		return standardName;
	}
	String Date2String(Date date){
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return dateFormat.format(date);
	}
	@Override
	public FastList<FeeRebatePCModel> deletefeeRebatePC(FeeRebatePCModel model) {
		dao.deletefeeRebatePC(model);
		return feeRebatePC(model);
	}
	@Override
	public String validate(FeeRebatePCModel model) {
		return dao.validate(model);
		
	}

}
