package eBIS.AppConfig;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.springframework.stereotype.Component;

@Component
public class CheckhostIP {

	private String ProductionServerIP=IPPropertiesBundleFile.getValueFromKey("production.server.ip");
	private String UATServerIP=IPPropertiesBundleFile.getValueFromKey("uat.server.ip");

public int gethost() {
	String hostname = "";
	int uatflag=0;
	int productionflag=0;
	int localflag=0;
	int flag=-1;
	String[] ProductionServerIPArray = ProductionServerIP.split(",");
	String[] UATServerIPArray = UATServerIP.split(",");
	hostname = LocalIP();
	for(int i=0;i<ProductionServerIPArray.length;i++) {
		if(hostname.contains(ProductionServerIPArray[i])) {
			productionflag=1;
		}
	}
	for(int i=0;i<UATServerIPArray.length;i++) {
		if(hostname.contains(UATServerIPArray[i])) {
			uatflag=1;
		}
	}
	if(productionflag==0 && uatflag==0) {
		localflag=1;
	}
	if(productionflag==1) {
		flag=1;
	}
	if(uatflag==1) {
		flag=2;
	}
	if(localflag==1) {
		flag=3;
	}
	return flag;
}

public static String LocalIP() {
    Process p;
    StringBuilder output = new StringBuilder();
    try {
        p = Runtime.getRuntime().exec("hostname -I");
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String s = "";
        while ((s = br.readLine()) != null)
        	output.append(s);
        p.waitFor();
        p.destroy();
    } catch (Exception e) {
    	
    }
    return output.toString();
}
}
