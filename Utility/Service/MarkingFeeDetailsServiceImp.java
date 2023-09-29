package eBIS.Utility.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import Masters.Domain.pc_marking_fee_domain;
import eBIS.Utility.DAO.MarkingFeeDetailsDAO;
import lombok.var;

@Service
public class MarkingFeeDetailsServiceImp implements MarkingFeeDetailsService {

	@Autowired
	MarkingFeeDetailsDAO dao;
	@Override
	public JsonObject getMarkingFeeDetails(String standardNumber, String scaleId) {
		JsonObject dataJson = new JsonObject();
		JsonArray dataJsonlistarray = new JsonArray();
			List<pc_marking_fee_domain> list = dao.getMarkingFeeDetails(standardNumber);
			if(list.size()>0) {
				for(int i=0;i<list.size();i++) {
					JsonObject dataJsonlist = new JsonObject();
					pc_marking_fee_domain tempdomain = list.get(i);
					String modelscale =scaleId+"";
					if(modelscale.equalsIgnoreCase("3")) {
						dataJsonlist.addProperty("Scale", "Large");
						dataJsonlist.addProperty("MinimunMarkingFee","₹ "+ String.format("%.2f",tempdomain.getLsminmf()));
						dataJsonlist.addProperty("Unit", tempdomain.getStrUnit().toString().trim());
						if(tempdomain.getSlab1qty()==-1)
						{
							dataJsonlist.addProperty("slab1quantity", "All");
							dataJsonlist.addProperty("slab1rate", "₹ "+String.format("%.6f",tempdomain.getSlab1rate()));
						}else {
							dataJsonlist.addProperty("slab1quantity", tempdomain.getSlab1qty().toString());
							dataJsonlist.addProperty("slab1rate", "₹ "+String.format("%.6f",tempdomain.getSlab1rate()));
							if(tempdomain.getSlab2qty()==-1) {
								dataJsonlist.addProperty("slab2quantity", "Remaining");
								dataJsonlist.addProperty("slab2rate", "₹ "+String.format("%.6f",tempdomain.getSlab2rate()));	
							}else {
								dataJsonlist.addProperty("slab2quantity", tempdomain.getSlab2qty().toString());
								dataJsonlist.addProperty("slab2rate", "₹ "+String.format("%.6f",tempdomain.getSlab2rate()));	
								dataJsonlist.addProperty("slab3quantity", "Remaining");
								dataJsonlist.addProperty("slab3rate", "₹ "+String.format("%.6f",tempdomain.getSlab3rate()));	
							}
						}
						
					}
					if(modelscale.equalsIgnoreCase("1")) {
						dataJsonlist.addProperty("Scale", "Medium");
						dataJsonlist.addProperty("MinimunMarkingFee","₹ "+ String.format("%.2f",tempdomain.getSsminmf()));
						dataJsonlist.addProperty("Unit", tempdomain.getStrUnit().toString().trim());
						dataJsonlist.addProperty("slabquantity", "All");
						dataJsonlist.addProperty("slabrate", "₹ "+String.format("%.6f",tempdomain.getMediumSlab1rate()));	
					}
					if(modelscale.equalsIgnoreCase("4")) {
						dataJsonlist.addProperty("Scale", "Small");
						dataJsonlist.addProperty("MinimunMarkingFee","₹ "+ String.format("%.2f",tempdomain.getSmallscaleminmf()));
						dataJsonlist.addProperty("Unit", tempdomain.getStrUnit().toString().trim());
						dataJsonlist.addProperty("slabquantity", "All");
						dataJsonlist.addProperty("slabrate", "₹ "+String.format("%.6f",tempdomain.getSmallSlab1rate()));
					}
					if(modelscale.equalsIgnoreCase("5")) {
						dataJsonlist.addProperty("Scale", "Micro");
						dataJsonlist.addProperty("MinimunMarkingFee","₹ "+ String.format("%.2f",tempdomain.getMicroscaleminmf()));
						dataJsonlist.addProperty("Unit", tempdomain.getStrUnit().toString().trim());
						dataJsonlist.addProperty("slabquantity", "All");
						dataJsonlist.addProperty("slabrate", "₹ "+String.format("%.6f",tempdomain.getMicroSlab1rate()));
					}
					dataJsonlistarray.add(dataJsonlist);
				}
				dataJson.addProperty("ResponseCode","200");
				dataJson.addProperty("ResponseException",false);
				dataJson.add("ResponseData", dataJsonlistarray);
			}else {
				dataJson.addProperty("ResponseCode","0");
				dataJson.addProperty("ResponseException",false);
				dataJson.addProperty("ResponseData","No data found");
			}
		return dataJson;
	
	}
	@Override
	public String getMarkingFeeDetailsHTML(String standardNumber, String scaleId) {
		JsonObject dataJson = new JsonObject();
		JsonArray dataJsonlistarray = new JsonArray();
			List<pc_marking_fee_domain> list = dao.getMarkingFeeDetails(standardNumber);
			if(list.size()>0) {
				for(int i=0;i<list.size();i++) {
					JsonObject dataJsonlist = new JsonObject();
					pc_marking_fee_domain tempdomain = list.get(i);
					String modelscale =scaleId+"";
					if(modelscale.equalsIgnoreCase("3")) {
						dataJsonlist.addProperty("Scale", "Large");
						dataJsonlist.addProperty("MinimunMarkingFee","₹ "+ String.format("%.2f",tempdomain.getLsminmf()));
						dataJsonlist.addProperty("Unit", tempdomain.getStrUnit().toString().trim());
						if(tempdomain.getSlab1qty()==-1)
						{
							dataJsonlist.addProperty("slab1quantity", "All");
							dataJsonlist.addProperty("slab1rate", "₹ "+String.format("%.2f",tempdomain.getSlab1rate()));
						}else {
							dataJsonlist.addProperty("slab1quantity", tempdomain.getSlab1qty().toString());
							dataJsonlist.addProperty("slab1rate", "₹ "+String.format("%.2f",tempdomain.getSlab1rate()));
							if(tempdomain.getSlab2qty()==-1) {
								dataJsonlist.addProperty("slab2quantity", "Remaining");
								dataJsonlist.addProperty("slab2rate", "₹ "+String.format("%.2f",tempdomain.getSlab2rate()));	
							}else {
								dataJsonlist.addProperty("slab2quantity", tempdomain.getSlab2qty().toString());
								dataJsonlist.addProperty("slab2rate", "₹ "+String.format("%.2f",tempdomain.getSlab2rate()));	
								dataJsonlist.addProperty("slab3quantity", "Remaining");
								dataJsonlist.addProperty("slab3rate", "₹ "+String.format("%.2f",tempdomain.getSlab3rate()));	
							}
						}
						
					}
					if(modelscale.equalsIgnoreCase("1")) {
						dataJsonlist.addProperty("Scale", "Medium");
						dataJsonlist.addProperty("MinimunMarkingFee","₹ "+ String.format("%.2f",tempdomain.getSsminmf()));
						dataJsonlist.addProperty("Unit", tempdomain.getStrUnit().toString().trim());
						dataJsonlist.addProperty("slabquantity", "All");
						if(tempdomain.getMediumSlab1rate()!=null)
						{
							dataJsonlist.addProperty("slabrate", "₹ "+String.format("%.2f",tempdomain.getMediumSlab1rate()));
						}else {
							dataJsonlist.addProperty("slabrate", "₹ "+String.format("%.2f",0.00));
						}
					}
					if(modelscale.equalsIgnoreCase("4")) {
						dataJsonlist.addProperty("Scale", "Small");
						dataJsonlist.addProperty("MinimunMarkingFee","₹ "+ String.format("%.2f",tempdomain.getSmallscaleminmf()));
						dataJsonlist.addProperty("Unit", tempdomain.getStrUnit().toString().trim());
						dataJsonlist.addProperty("slabquantity", "All");
						if(tempdomain.getSmallSlab1rate()!=null)
						{
							dataJsonlist.addProperty("slabrate", "₹ "+String.format("%.2f",tempdomain.getSmallSlab1rate()));
						}else {
							dataJsonlist.addProperty("slabrate", "₹ "+String.format("%.2f",0.00));
						}
					}
					if(modelscale.equalsIgnoreCase("5")) {
						dataJsonlist.addProperty("Scale", "Micro");
						dataJsonlist.addProperty("MinimunMarkingFee","₹ "+ String.format("%.2f",tempdomain.getMicroscaleminmf()));
						dataJsonlist.addProperty("Unit", tempdomain.getStrUnit().toString().trim());
						dataJsonlist.addProperty("slabquantity", "All");
						if(tempdomain.getMicroSlab1rate()!=null)
						{
							dataJsonlist.addProperty("slabrate", "₹ "+String.format("%.2f",tempdomain.getMicroSlab1rate()));
						}else {
							dataJsonlist.addProperty("slabrate", "₹ "+String.format("%.2f",0.00));
						}
					}
					dataJsonlistarray.add(dataJsonlist);
				}
				dataJson.addProperty("ResponseCode","200");
				dataJson.addProperty("ResponseException",false);
				dataJson.add("ResponseData", dataJsonlistarray);
			}else {
				dataJson.addProperty("ResponseCode","0");
				dataJson.addProperty("ResponseException",false);
				dataJson.addProperty("ResponseData","No data found");
			}
			var markingFee="";
			JsonElement ResponseCode=dataJson.get("ResponseCode");
			if(ResponseCode.getAsString().equals("200")) {
				JsonArray reponsedataArray=dataJson.get("ResponseData").getAsJsonArray();
				for(int i=0;i<reponsedataArray.size();i++)
				{
					String MinimunMarkingFee=reponsedataArray.get(i).getAsJsonObject().get("MinimunMarkingFee").getAsString();
					String Unit=reponsedataArray.get(i).getAsJsonObject().get("Unit").getAsString();
					JsonElement scaleid=reponsedataArray.get(i).getAsJsonObject().get("Scale");
					JsonElement slab1quantity=reponsedataArray.get(i).getAsJsonObject().get("slab1quantity");
					JsonElement slab1rate=reponsedataArray.get(i).getAsJsonObject().get("slab1rate");
					JsonElement slab2quantity=reponsedataArray.get(i).getAsJsonObject().get("slab2quantity");
					JsonElement slab2rate=reponsedataArray.get(i).getAsJsonObject().get("slab2rate");
//					JsonElement slab3quantity=reponsedataArray.get(i).getAsJsonObject().get("slab3quantity");
					JsonElement slab3rate=reponsedataArray.get(i).getAsJsonObject().get("slab3rate");
					JsonElement slabrate=reponsedataArray.get(i).getAsJsonObject().get("slabrate");
					markingFee = "I hereby agree to pay marking fee to Bureau of Indian Standards after grant of licence to use the Standard Mark according to the following rates and in the manner stipulated as under :";
					markingFee += "<br><br> <b>i)</b> Minimum Marking fee= " + MinimunMarkingFee;   		 
					markingFee += "<br><br> The Actual marking fee to be calculated as under";
					
					if(scaleid.getAsString().equals("Large")) {
					if(slab1quantity.getAsString().equals("All")) {
					markingFee += "<br>" +""+ slab1rate.getAsString() + " per unit for all units";  
					}else{
					markingFee += "<br>" +""+ slab1rate.getAsString() + " per unit for 1st " + slab1quantity.getAsString() + " units";
					}
					if(slab2quantity!=null) {
						if(!slab1quantity.getAsString().equals("All") && slab2quantity.getAsString().equals("Remaining")){
							markingFee += "<br>" +  ""+ slab2rate.getAsString()+ " per unit for remaining units" ;
						}
						if(!slab1quantity.getAsString().equals("All") && slab2quantity.getAsString().equals("Remaining")){
							markingFee += "<br>" +  ""+ slab2rate.getAsString() + " per unit for next " + slab2quantity.getAsString() + " units" ;
						}   
						if(slab3rate!=null) {
							if(!slab2quantity.getAsString().equals("All") && slab3rate.getAsString().equals("0.00")){
							markingFee += "<br>" +  ""+ slab3rate.getAsString() + " per unit for remaining units.." ;
						}
					}
					}
					   

					
					}
					else {
						markingFee += "<br>" +""+ slabrate.getAsString() + " per unit for all units";
					}
					markingFee += "<br><b>" +  "1 Unit = "+ Unit+"</b>";

					markingFee += "<br> <br>" +  "<b>ii)</b> The marking fee is payable as follows :";
					markingFee +=  "<br> <br>" + "<b>a)</b> Minimum marking fee for one operative year payable in advance which will be carried over to next renewal(s).";
					markingFee += "<br>" + "<b>b)</b> Actual marking fee for the first nine months of the operative period calculated on the unit rate on the production marked or the minimum fee whichever is higher shall be payable at the time of the first renewal of the licence. For subsequent renewals, the actual marking fee for 12 months period consisting of last three months of previous operative year and the first nine months of the current operative year or the minimum fee whichever is higher, shall be payable.";
				}
				
			}else {
				markingFee="Marking Fee will be further notified.";
			}
		return markingFee;
	
	}
}
