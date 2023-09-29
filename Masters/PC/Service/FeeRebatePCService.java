package eBIS.Masters.PC.Service;

import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;

import Masters.Domain.Scale_Mst_Domain;
import Masters.Domain.State_Mst_Domain;
import Masters.Domain.standard_mst_domain;
import eBIS.Masters.PC.Model.FeeRebatePCModel;

public interface FeeRebatePCService {

	FastList<FeeRebatePCModel> feeRebatePC(FeeRebatePCModel model);

	List<Scale_Mst_Domain> getScaleList();
	
	List<State_Mst_Domain> getStateList();

	List<standard_mst_domain> getStandardMasterList();

	FastList<FeeRebatePCModel> addfeeRebatePC(FeeRebatePCModel model);

	String getScaleName(String scaleid);

	String getStateName(String stateid);

	String getStandardName(String standardnumber);

	FastList<FeeRebatePCModel> deletefeeRebatePC(FeeRebatePCModel model);

	String validate(FeeRebatePCModel model);

	

}
