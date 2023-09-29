package eBIS.Utility.DAO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import Masters.Domain.pc_marking_fee_domain;
import eBIS.AppConfig.SecondaryDaoHelper;

@Repository
public class MarkingFeeDetailsDAO {
	@Autowired
	SecondaryDaoHelper readonlydaohelper;
	public List<pc_marking_fee_domain> getMarkingFeeDetails(String isno) {
		String sql="select c from pc_marking_fee_domain c where c.strStandardNo = '"+isno+"' and c.IsValid=1";
		return readonlydaohelper.findByQuery(sql);
	}
}
