package eBIS.AppConfig;

import java.util.ResourceBundle;

public class FileUploadPathPropertiesBundleFile {
	public static String getValueFromKey(String p_strKey) {
        ResourceBundle bundle1 = ResourceBundle.getBundle("propertyFiles.FileUploadPath");    
        return bundle1.getString(p_strKey);
}
}
