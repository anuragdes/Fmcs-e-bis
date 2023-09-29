package eBIS.Utility.Global.Functions;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import lombok.var;

@Component
public class ModelEncoderDecoder {
	public static Object encoder(Object model) throws IllegalArgumentException, IllegalAccessException {
		Field fields[] = model.getClass().getDeclaredFields();
		for(int j=0;j<fields.length;j++) {
			Field f = fields[j];
        	Object value = f.get(model);
        	if(value==null) {
        		value=" ";
        	}
        	if(value.equals("")) {
        		value=" ";
        	}
        	String encodedJson=new String(Base64.getEncoder().encode(value.toString().getBytes()));
        	f.set(model, encodedJson);
        	value = f.get(model);
		}
	return model;
}
	public static Object decoder(Object model) throws IllegalArgumentException, IllegalAccessException {
		Field fields[] = model.getClass().getDeclaredFields();
		for(int j=0;j<fields.length;j++) {
			Field f = fields[j];
        	Object value = f.get(model);
        	if(value==null) {
        		value="";
        	}
        	if(value.equals("")) {
        		value=" ";
        	}
        	String encodedJson=new String(Base64.getDecoder().decode(value.toString().getBytes()));
        	f.set(model, encodedJson);
        	value = f.get(model);
		}
	return model;
}
	public static  List<HashMap<String, String>> encoderListHashMap(List<HashMap<String, String>> model) {
		for(int i=0;i<model.size();i++) {
			HashMap<String, String> map = model.get(i);
			  for(Entry<String, String> m : map.entrySet()){
				  String encodedValue=new String(Base64.getEncoder().encode(m.getValue().toString().getBytes()));
				  m.setValue(encodedValue); 
				   } 
		}
		return model;
	}
	public static List<HashMap<String, String>> decoderListHashMap(List<HashMap<String, String>> model) {
		for(int i=0;i<model.size();i++) {
			HashMap<String, String> map = model.get(i);
			  for(Entry<String, String> m : map.entrySet()){
				  String decodedValue=new String(Base64.getEncoder().encode(m.getValue().toString().getBytes()));
				  m.setValue(decodedValue); 
 				   } 
		}
		return model;
	}
	public static  List<Map<String, Object>> encoderListMap(List<Map<String, Object>> existingLicenseDetailsList) {
		for(int i=0;i<existingLicenseDetailsList.size();i++) {
			var map = existingLicenseDetailsList.get(i);
			  for(Entry<String, Object> m : map.entrySet()){
				  String encodedValue=new String(Base64.getEncoder().encode(m.getValue().toString().getBytes()));
				  m.setValue(encodedValue); 
				   } 
		}
		return existingLicenseDetailsList;
	}
	public static List<Map<String, Object>> decoderListMap(List<Map<String, Object>> model) {
		for(int i=0;i<model.size();i++) {
			var map = model.get(i);
			  for(Entry<String, Object> m : map.entrySet()){
				  String decodedValue=new String(Base64.getEncoder().encode(m.getValue().toString().getBytes()));
				  m.setValue(decodedValue); 
 				   } 
		}
		return model;
	}
}
