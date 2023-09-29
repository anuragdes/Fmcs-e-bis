package eBIS.AppConfig;

import java.util.ResourceBundle;

public class FTPProperties {
	public static String getValueFromKey(String p_strKey) {
        ResourceBundle bundle1 = ResourceBundle.getBundle("propertyFiles.FTP");    
        return bundle1.getString(p_strKey);
}
}
