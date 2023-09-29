package eBIS.AppConfig;

import java.util.ResourceBundle;

public class IPPropertiesBundleFile {
	public static String getValueFromKey(String p_strKey) {
        ResourceBundle bundle1 = ResourceBundle.getBundle("propertyFiles.IPS");    
        return bundle1.getString(p_strKey);
}
}
