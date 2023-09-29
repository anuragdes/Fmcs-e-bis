package eBIS.Utility.Service;

import com.google.gson.JsonObject;

public interface MarkingFeeDetailsService {
	JsonObject getMarkingFeeDetails(String standardNumber,String scalidId);
	String getMarkingFeeDetailsHTML(String standardNumber, String scaleId);
}
