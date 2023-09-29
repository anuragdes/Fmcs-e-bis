package eBIS.Utility.Global.Functions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class DateRelatedFunctions {
	public static String Date2String(Date date){
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return dateFormat.format(date);
	}
	public static Date String2Date(String dateString) {
	    Date date=null;
	    try {
	    	date=new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
	    }catch(Exception ex) {
	    	ex.printStackTrace();
	    }
	    return date;
	}
}
