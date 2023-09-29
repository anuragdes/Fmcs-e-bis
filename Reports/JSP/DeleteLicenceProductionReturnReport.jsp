<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false"%>
<%@ page import="Global.Login.Model.Session"%>
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link rel="stylesheet"
	href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/bootstrapValidator.css">
<script type="text/javascript"
	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/bootstrapValidator.js"></script>
<script type="text/javascript"
	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/jquery-ui.js"></script>
<link type="text/css" rel="stylesheet"
	href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/jquery.dataTables.css"> 
<link type="text/css" rel="stylesheet"
	href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/buttons.bootstrap.min.css"> 
<link type="text/css" rel="stylesheet"
	href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/buttons.bootstrap4.min.css"> 
<link type="text/css" rel="stylesheet"
	href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/buttons.dataTables.min.css"> 
<link type="text/css" rel="stylesheet"
	href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/buttons.foundation.min.css"> 
<link type="text/css" rel="stylesheet"
	href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/buttons.jqueryui.min.css"> 
<link type="text/css" rel="stylesheet"
	href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/buttons.semanticui.min.css"> 
 <script type="text/javascript"
	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/jquery.dataTables.min.js"></script>
<script type="text/javascript"
	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/dataTables.buttons.min.js"></script>
<script type="text/javascript"
	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/buttons.bootstrap4.min.js"></script>
<script type="text/javascript"
	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/buttons.bootstrap.min.js"></script>
<script type="text/javascript"
	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/buttons.foundation.min.js"></script>
<script type="text/javascript"
	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/buttons.html5.min.js"></script>
<script type="text/javascript"
	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/buttons.jqueryui.min.js"></script>
<script type="text/javascript"
	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/buttons.print.min.js"></script>
<script type="text/javascript"
	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/buttons.semanticui.min.js"></script>
	  
<link type="text/css" rel="stylesheet" href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/datepicker3.css">
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/bootstrap-datepicker.js"></script>
		
<link rel="stylesheet" type="text/css"
	href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/select2.css" />
<script type="text/javascript"
	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/select2.min.js"></script>
 					
<style>
.error {
	color: #E94646;
	font-style: italic;
	font-size: 70%;
}

tfoot input {
        width: 100%;
        padding: 3px;
        box-sizing: border-box;
    }
</style>
<script type="text/javascript">

	$(document).ready(function() {
			$('.select2-select').select2();
	});
	
	function allowOnlyNumbers(id){
		
		$('#'+id).val($("#"+id).val().replace(/[^0-9-]/g,'')) ;
	}						
</script>

<div class="container">
 		<div id="successMessageDiv">	
			<div class="alert alert-success" id="success" style="display:none">
				<p id="successText"><strong></strong></p>
			</div>
		</div>
		<script>$("#messageDiv").delay(2000).fadeOut(600);</script>
	 	<div id="messageDiv1">			
			<div class="alert alert-danger" id="error" style="display:none" >
				 <p id="errorText"><strong></strong></p>
			</div>
		</div>
<div class="col-md-12">
	<c:if test="${message!=null && message!=''}"><div class='alert alert-success' id="message"><h3><c:out value="${message}"></c:out></h3></div></c:if>
</div>
	<div class="row  clearfix">
		<div class="text-center ">
			<h3 class="b bold">Delete Licence Production Return </h3>
		</div>
	</div>
<input type="hidden" id="roleId" value="${roleId}">
		<div class="col-md-12">
			<fieldset>
				<legend>Enter Licence Number</legend>
				<div class="col-md-12" style="margin-top:3%"  id="licenceId" >
					<div class="col-md-2">
						Licence No.
						<font color="red">*</font>

					</div>
					<div class="col-md-4 form-group">
						<input type="text" class="form-control" name="licenceId" id="licenceId123" onkeyup="allowOnlyNumbers(this.id);" maxlength="10" required="true" maxlength="100" />

					</div>
				</div>
				
				
				<div class="col-md-12" style="margin-left:44%;margin-top:2%; " id="startButton">
					<button type="button" onclick="getLicenceNoStatus();"  class="btn btn-primary"
					value="Save and Continues" >Submit
					</button>
					<span class="pad-left"><a class="btn btn-primary" href="DeleteLicenceProductionReturn"><i class="fa fa-refresh pad-right half fa-1x"></i>Reset</a></span>
				</div>
			</fieldset>
		</div>
		
		<div class="col-md-12" id="firm" style="display:none;">
			<fieldset>
				<legend>Firm Details </legend>
			<div class="col-md-12">
				<div class="col-md-12">
					<div class="col-md-4">
						<spring:message code="label.firmName"></spring:message>
					</div>					
						<div class="col-md-8 form-group">							
						<input name="firmName" type="text" class="form-control" id="firmName" placeholder="Firm Name"/>			
						</div>
					<div class="firmDiv">
						<div class="col-md-4">
							<spring:message code="label.address"></spring:message>
						</div>
						<div class="col-md-8 form-group">	
							<textarea name="address" class="form-control" id="firmAddress" placeholder="Enter Firm Address" ></textarea>	
						</div>
						<div class="col-md-4">
						<spring:message code="label.branch"></spring:message>
					</div>
						<div class="col-md-8 form-group">							
						<input name="branch" type="text" class="form-control" id="branch" placeholder="Branch Name"/>			
						</div>						
					</div>
				</div>
			</div>				
		</div>	
		
		<div class="col-md-12" id="productionreturn" style="display:none;">
			<fieldset>
				<legend>Production Return Done</legend>
			<div class="col-md-12">
                 <table id="tab_logic" class="table table-bordered hidden">	
						<thead>
							<tr style="background-color: #338ec9; color: #ffffff;">
							    <th class="text-center">Sr. No</th>
								<th class="text-center">Brand Name</th>
								<th class="text-center">From Date</th>
								<th class="text-center">To Date</th>
								<th class="text-center">Production Quantity</th>
								<th class="text-center">Production Value</th>
								<th class="text-center">Production Marked</th>
								<th class="text-center">Marked Percentage</th>
								<th class="text-center">Production Covered Value</th>
								<th class="text-center">Marking Fee</th>
								<th class="text-center">Delete</th>
																			
							</tr>														  				
						</thead>	
											
						
						<tbody id="productionreturnDetail">
						  							
						</tbody>					
	              </table>
			</div>				
			</fieldset>
		</div>	
							
 <script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/jquery-ui.js"></script> 
	
</div>

<script>

function getLicenceNoStatus(){	
	
		 var licenceNo=$('#licenceId123').val();
		 $('#licenceId123').attr('readonly',true);
		 $.ajax({
			type : "POST",
			url : "/MANAK/GetFirmDetailsFromLicenceNumber",
			data : "licenceNo=" + licenceNo,
			async:false,
			success : function(response) {				
				if(true){
					$('#firm').css('display','block');
					$('#firmName').val(response.firmName);
					$('#firmName').attr('readonly',true);
					$('#firmAddress').val(response.firmAddress);
					$('#firmAddress').attr('readonly',true);
					
					$('#branch').val(response.branchName);
					$('#branch').attr('readonly',true);
				}
				else{
					alert("Licence Number does not exist !");
				}
			},
			error : function(e) {
				//alert('Session Expire');
			}
	
		});
		 $.ajax({
				type : "POST",
				url : "/MANAK/GetProductionReturnFromLicenceNumber",
				data : "licenceNo=" + licenceNo,
				async:false,
				success : function(response) {	
					console.log();
					if(true){
						if(response.length != 0)
					 {	
						$('#productionreturn').css('display','block');
						//Production Return Table Start      										
	      				$('#tab_logic').removeClass('hidden');
		    			$("#productionreturnDetail").empty();		    
	      				var tableData="";
	      				
	      				
	      				
	      				var fromDate="";
	      				var toDate="";
	      				var brandName = "";
	      				var prodCaValue = "";
	      				var markFee = "";
	      				var prodValue = "";
	      				var prodMarkPer = "";
	      				var prodMark = "";
	      				var prodCaunt = "";
	      			
	      				for(var i=0;i<response.length;i++){	
	      					
			    			
			    			if(response[i].dt_from_dt == "" || response[i].dt_from_dt == null){
			    				dt_from_dt="-";
			    			}else{
			    				dt_from_dt=response[i].dt_from_dt;
			    			}
			    			
			    			if(response[i].dt_to_dt == "" || response[i].dt_to_dt == null){
			    				dt_to_dt="-";
			    			}else{
			    				dt_to_dt=response[i].dt_to_dt;
			    			}
			    			
			    			if(response[i].str_brand_name == "" ||response[i].str_brand_name == null){
			    				brandName="-";
			    			}else{
			    				brandName=response[i].str_brand_name;
			    			}
			    			
			    			if(response[i].str_prod_ca_value == "" ||response[i].str_prod_ca_value == null){
			    				prodCaValue="-";
			    			}else{
			    				prodCaValue=response[i].str_prod_ca_value;
			    			}
			    			
			    			
			    			if(response[i].str_prod_quant == "" ||response[i].str_prod_quant == null){
			    				prodCaunt="-";
			    			}else{
			    				prodCaunt=response[i].str_prod_quant;
			    			}
			    			
			    			if(response[i].num_production_marked== "" ||response[i].num_production_marked == null){
			    				prodMark="-";
			    			}else{
			    				prodMark=response[i].num_production_marked;
			    			}
			    			
			    			if(response[i].str_prod_value == "" ||response[i].str_prod_value == null){
			    				var prodValue="-";
			    			}else{
			    				var prodValue=response[i].str_prod_value;
			    			}
			    			
			    			if(response[i].num_marked_percentage == "" ||response[i].num_marked_percentage == null){
			    				prodMarkPer="-";
			    			}else{
			    				prodMarkPer=response[i].num_marked_percentage;
			    			}
			    			
			    			
			    			if(response[i].num_marking_fee == "" ||response[i].num_marking_fee == null){
			    				markFee="-";
			    			}else{
			    				markFee=response[i].num_marking_fee;
			    			}		    		
			    			
			    			 var deletebutton = "<button type='button' name='active' id='"+response[i].num_id+"' class='btn btn-danger' onclick='deleteProductionReturn(this.id)'>Delete</button>";
			      				tableData += "<tr> <td align='center'>"+ parseInt(parseInt(i)+1) +"</td> <td> "+brandName+"</td>";
			      				tableData = tableData + "<td>" +dt_from_dt+"</td>";
			      				tableData = tableData + "<td>" +dt_to_dt+"</td>";
			      				tableData = tableData + "<td>" +prodCaunt+"</td>";
								tableData = tableData + "<td>" +prodValue+"</td>";
								tableData = tableData + "<td>" +prodMark+"</td>";
								tableData = tableData + "<td>" +prodMarkPer+"</td>";
								tableData = tableData + "<td>" +prodCaValue+"</td>";
								tableData = tableData + "<td>" +markFee+"</td>";
								tableData = tableData + "<td>" +deletebutton+"</td>";														
								
	      				}
	      				$("#productionreturnDetail").append(tableData);
	      				$('#tab_logic').DataTable({
	      					destroy: true,
		    		        dom: 'Bfrtip',
		    		        buttons: [
		    		             'excel' 
		    		        ]         		      	       
	        		    }); 
						
						//END
					 }else{
						alert("Licence Number does not exist !");
					 }
					}		
					else{
						alert("Licence Number does not exist !");
					}
					
					
				},
				error : function(e) {
					//alert('Session Expire');
				}
		
			});
	 	
}


function deleteProductionReturn(id)
{ 
 	swal({		
	      title: "Are you sure?",
	      text: "You will not be able to revert this decision",
	      type: "warning",
	      showCancelButton: true,
	      confirmButtonClass: 'btn-danger',
	      confirmButtonText: 'Yes, Proceed',
	      cancelButtonText: "No, Cancel",
	      closeOnConfirm: false,
	      closeOnCancel: true,
	    },
	    
	    function(isConfirm){
	      if (isConfirm){
	  		//Initiating the AJAX request
	  		
	  		
	  		$.ajax({
	  		  //alert("in ajax");
	  			type : "POST",
	  			url : "/MANAK/deleteProductionReturnValue",
	    		data:"produnumid="+id,	
	      		success : function(response) {
	      			if(response=="Success"){
	      				swal("Done!", "Your action has been finalized", "success");
	      				var t1=$('#tab_logic').DataTable();
	      		  		t1.clear().draw();
	      		  		t1.destroy();
	      				//$("#tab_logic").ajax.reload();
	      				//$("#tab_logic").ajax.reload();
	      				getLicenceNoStatus();
      				} 
	      			if(response=="Failure"){
	      				swal("Error!", "Your are not authorized to remove this record,Only HOD remove this record", "warning");
	      				$(".confirm").click(function(){
	      					//window.location.reload(true);
	      					//$("#filter").val("");
	      				});
	      			}
	      			if(response=="Failed"){
	      				$("#errormsg").addClass("alert alert-danger");
	  					$("#errormsg").html("<b>Error</b>: An Error Occured, Please try Later");
	  					setTimeout(function(){
	  						$("#errormsg").removeClass("alert alert-danger");
	  						$("#errormsg").html("");
	  					},2000);
	      			}
	      		},
	      		error : function(e) {
	      			$("#errormsg").addClass("alert alert-danger");
	  				$("#errormsg").html("<b>Error</b>: An Error Occured, Please try Later");
	  				setTimeout(function(){
	  					$("#errormsg").removeClass("alert alert-danger");
	  					$("#errormsg").html("");
	  				},2000);
	  			}
	  		});
	  		$('#tab_logic').DataTable();
	      } else {
	        alert("Cancelled", "Your action has been cancelled", "error");
	      }
	    });  

}


</script>


