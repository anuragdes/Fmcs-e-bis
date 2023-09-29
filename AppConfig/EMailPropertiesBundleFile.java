package eBIS.AppConfig;

import java.util.ResourceBundle;

public class EMailPropertiesBundleFile {
	public static String getValueFromKey(String p_strKey) {
        ResourceBundle bundle1 = ResourceBundle.getBundle("propertyFiles.EMail");    
        return bundle1.getString(p_strKey);
}
}
