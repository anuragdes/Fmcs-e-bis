package eBIS.Masters.PC.DAO;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import Masters.Domain.Scale_Mst_Domain;
import Masters.Domain.State_Mst_Domain;
import Masters.Domain.standard_mst_domain;
import eBIS.AppConfig.PrimaryDaoHelper;
import eBIS.AppConfig.SecondaryDaoHelper;
import eBIS.Masters.PC.Entity.FeeRebatePC;
import eBIS.Masters.PC.Entity.TileCountMaster;
import eBIS.Masters.PC.Model.FeeRebatePCModel;

@Repository
public class FeeRebatePCDAO {

	@Autowired
	PrimaryDaoHelper primaryDaoHelper;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	SecondaryDaoHelper secondaryDaoHelper;
	public List<FeeRebatePC> feeRebatePC() {
		String hql="select c from FeeRebatePC c where c.activeflag=1 order by scaleid ";
		List<FeeRebatePC> list = primaryDaoHelper.findByQuery(hql);
		return list;
	}
	public List<Scale_Mst_Domain> getScaleList() {
		String hql="select c from Scale_Mst_Domain c where c.isValid=1 order by c.scale_Name desc";
		List<Scale_Mst_Domain> list = secondaryDaoHelper.findByQuery(hql);
		return list;
	}

	public List<State_Mst_Domain> getStateList() {
		String hql="select c from State_Mst_Domain c where c.IsValid=1 order by c.strStateName";
		List<State_Mst_Domain> list = secondaryDaoHelper.findByQuery(hql);
		return list;
	}
	public List<standard_mst_domain> getStandardMasterList() {
		String hql = "SELECT c from standard_mst_domain c where c.strPcHallOthers in ('PC','SDOC') and c.numIsvalid = 1 and c.strSupersededByis is null order by c.intProductId";
		List<standard_mst_domain> list = secondaryDaoHelper.findByQuery(hql);
		return list;
	}
	public int addfeeRebatePC(FeeRebatePC domain) {
		int flag = primaryDaoHelper.persist(domain);
		return flag;
	}
	public String getScaleName(String scaleid) {
		String hql="select c from Scale_Mst_Domain c where c.isValid=1 and c.scale_Id in("+scaleid+") order by c.scale_Name desc";
		List<Scale_Mst_Domain> list = secondaryDaoHelper.findByQuery(hql);
		String scaleName="";
		String[] scaleNameArray=new String[scaleid.split(",").length];
		if(scaleid.split(",").length>1) {
			for(int i=0;i<scaleNameArray.length;i++) {
				scaleNameArray[i]=list.get(i).getScale_Name();
			}
			scaleName=Arrays.toString(scaleNameArray).replace("[", "").replace("]", "").replace("{", "").replace("}", "");
		}else {
			scaleName=list.get(0).getScale_Name();
		}
		return scaleName;
	}
	public String getStateName(String stateid) {
		String hql="select c from State_Mst_Domain c where c.IsValid=1 and c.numStateId in("+stateid+") order by c.strStateName";
		List<State_Mst_Domain> list = secondaryDaoHelper.findByQuery(hql);
		String stateName="";
		String[] stateNameArray=new String[stateid.split(",").length];
		if(stateid.split(",").length>1) {
			for(int i=0;i<stateNameArray.length;i++) {
				stateNameArray[i]=list.get(i).getStrStateName();
			}
			stateName=Arrays.toString(stateNameArray).replace("[", "").replace("]", "").replace("{", "").replace("}", "");
		}else {
			stateName=list.get(0).getStrStateName();
		}
		return stateName;
	}
	public String getStandardName(int standardnumber) {
		String hql = "SELECT c from standard_mst_domain c where c.strPcHallOthers in ('PC','SDOC') and c.numStandardId="+standardnumber+" and c.numIsvalid = 1 and c.strSupersededByis is null order by c.intProductId";
		List<standard_mst_domain> list = secondaryDaoHelper.findByQuery(hql);
		return list.get(0).getStrStandardNo()+":"+list.get(0).getIntStdYear();
	}
	public void deletefeeRebatePC(FeeRebatePCModel model) {
		String hql="select c from FeeRebatePC c where c.activeflag=1 and  c.numid="+model.getNumid();
		List<FeeRebatePC> list = primaryDaoHelper.findByQuery(hql);
		for(int i=0;i<list.size();i++) {
			FeeRebatePC domain = list.get(i);
			domain.setActiveflag(0);
			primaryDaoHelper.merge(domain);
		}
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
	public void updateTileCount(List<FeeRebatePC> list, FeeRebatePCModel model) {
		String hql="select c from TileCountMaster c where c.url='"+model.getUrl()+"'";
		List<TileCountMaster> getlist = primaryDaoHelper.findByQuery(hql);
		if(getlist.size()>0) {
			for(int i=0;i<getlist.size();i++)
			{
				TileCountMaster domain = getlist.get(i);
				if(domain.getCount()!=list.size()) {
					domain.setCount(list.size());	
					primaryDaoHelper.merge(domain);
				}
			}
		}else {
			TileCountMaster domain=new TileCountMaster();
			domain.setUrl(model.getUrl());
			domain.setCount(list.size());
			primaryDaoHelper.merge(domain);	
		}
		
	}
	public String validate(FeeRebatePCModel model) {
		 int optionalDiscount = model.getOptionaldiscount();
		 String startupFlag="0";
		 String womenentrepreneurFlag="0";
		 if(optionalDiscount==1) {
			 womenentrepreneurFlag="1";
			 startupFlag="0";
			}else {
				if(optionalDiscount==2) {
					 womenentrepreneurFlag="0";
					 startupFlag="1";
				}else {
					if(optionalDiscount==3) {
						 womenentrepreneurFlag="1";
						 startupFlag="1";
					}else {
						if(optionalDiscount==4) {
							 womenentrepreneurFlag="0,0";
							 startupFlag="0,0";
						}else {
							 womenentrepreneurFlag="0,1";
							 startupFlag="0,1";
						}
					}
				}
			}
		 String stateId="";
		 if(model.getStateid().equalsIgnoreCase("all")) {
			 stateId="36,35,34,32,31,29,28,27,26,21,20,19,18,17,16,15,14,13,12,11,10,9,8,7,6,5,2,1,37,33,30,25,24,23,22,4,3";
		 }else {
			 stateId=model.getStateid();
		 }
		 String scaleId="";
		 if(model.getScaleid().equalsIgnoreCase("all")) {
			 scaleId="1,3,4,5";
		 }else {
			 scaleId=model.getScaleid();
		 }
		String sql="select count(*) as flag from bis_masters.feerebatepc\r\n"
				+ "where  (select string_to_array(str_scale_id,',')) && array[string_to_array('"+scaleId+"', ',')]\r\n"
				+ "and (select string_to_array(str_state,',')) && array[string_to_array('"+stateId+"', ',')]\r\n"
				+ "and (select string_to_array(num_startup,','))&& array[string_to_array('"+startupFlag+"', ',')]\r\n"
				+ "and (select string_to_array(num_app_lic,','))&& array[string_to_array('"+model.getApplicenseflag()+"', ',')]\r\n"
				+ "and (select string_to_array(num_women,',')) && array[string_to_array('"+womenentrepreneurFlag+"', ',')]\r\n"
				+ "and (select string_to_array(str_standard,',')) && array[string_to_array('"+model.getStandardnumber()+"', ',')]\r\n"
				+ "and (trunc(to_date('"+model.getFromdate()+"')) between trunc(dt_from_date) and trunc(dt_to_date)\r\n"
				+ "or \r\n"
				+ "(trunc(to_date('"+model.getTodate()+"')) between trunc(dt_from_date) and trunc(dt_to_date)\r\n"
				+ "))\r\n"
				+ "and num_isvalid =1";
		System.out.println("validate: "+sql);
		return jdbcTemplate.queryForObject(sql, String.class);
	}
}
