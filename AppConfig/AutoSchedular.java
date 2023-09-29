package eBIS.AppConfig;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import lab.service.LIMPSAutoSchedularService;


public class AutoSchedular {
	
	@Autowired
	LIMPSAutoSchedularService service;
	
	@Scheduled(cron="0 0 2 * * ?")
    public void LIMPSAutoSchedular() throws JSONException {
		System.out.println("LIMPSAutoSchedular Starts at "+ new Date());
		List<Map<String, Object>> list=service.LIMPSAutoSchedular();
		System.out.println("Updated List "+ list);
		System.out.println("LIMPSAutoSchedular Ends at "+ new Date());
    }
}
