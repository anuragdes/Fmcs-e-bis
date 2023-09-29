package eBIS.Masters.PC.Entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import eBIS.Utility.Global.Functions.DateRelatedFunctions;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Entity
@Table(name = "FeeRebatePC", schema = "bis_masters")
public class FeeRebatePC {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FeeRebatePCGenerator")
	@SequenceGenerator(name = "FeeRebatePCGenerator",schema="bis_masters", sequenceName = "bis_masters.FeeRebatePCGeneratorSequence", initialValue = 1, allocationSize = 1)
	
	@Column(name="num_id")
	private Long numid;
	
	@Column(name="dt_entry_date")
	private Date entrydate=new Date();
	
	@Column(name="str_state")
	private String stateid="0";
	
	@Column(name="str_scale_id")
	private String scaleid="0";
	
	@Column(name="num_base_scale")
	private String basescaleid="0";
	
	@Column(name="num_recurring")
	private String recurringnumber="0";
	
	@Column(name="str_standard")
	@Type(type="text")
	private String standardnumber="0";
	
	private Integer optionaldiscount=0;
	
	@Column(name="num_women")
	private String womenentrepreneurFlag="0";
	
	@Column(name="num_startup")
	private String startupFlag="0";
	
	@Column(name="num_discount")
	private Float discountpercentage=Float.parseFloat("0.0");
	
	@Column(name="num_base_amount")
	private Float baseamount=Float.parseFloat("0.0");
	
	@Column(name="num_app_lic")
	private String applicenseflag="0";
	
	@Column(name="num_entry_emp_id")
	private Integer userid=0;
	
	@Column(name="num_isvalid")
	private Integer activeflag=1;
	
	@Column(name="dt_from_date")
	private Date fromdate=DateRelatedFunctions.String2Date("01/01/0001");
	
	@Column(name="dt_to_date")
	private Date todate=DateRelatedFunctions.String2Date("01/01/0001");
	
	private String discountdescription="";

}
