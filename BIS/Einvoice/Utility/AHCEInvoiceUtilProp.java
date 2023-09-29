package BIS.Einvoice.Utility;

import java.util.ResourceBundle;

public class AHCEInvoiceUtilProp {

		 public static String getValueFromKey(String p_strKey) {
		        ResourceBundle bundle1 = ResourceBundle.getBundle("BIS.Einvoice.Utility.AHCeinvoiceUtil");    
		        return bundle1.getString(p_strKey);


	}


}
