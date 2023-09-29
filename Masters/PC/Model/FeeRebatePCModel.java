package eBIS.Masters.PC.Model;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class FeeRebatePCModel {

	private String stateid="0";
	private String scaleid="0";
	private String statename="";
	private String scalename="";
	private String url="";
	private String basescaleid="0";
	private String basescalename="";
	private String recurringnumber="1";
	private String standardnumber="0";
	private String standardname="";
	private int optionaldiscount=0;
	private float discountpercentage=0;
	private float baseamount=0;
	private String applicenseflag="0";
	private int userid=0;
	private int activeflag=0;
	private String fromdate="";
	private String todate="";
	private long numid=0;
	private String discountdescription="";
	Date String2Date() {
		String dateString="01/01/0001";
	    Date date=null;
	    try {
	    	date=new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
	    }catch(Exception ex) {
	    	ex.printStackTrace();
	    }
	    return date;
	}
}
