 <%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
 <%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
 <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
 
 <%@ page isELIgnored="false" %>

    
<link rel="stylesheet" href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/bootstrapValidator.css">
 <script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/bootstrapValidator.js"></script>
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/bootstrap-datepicker.js"></script>
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/bootstrap.min.js"></script> 
<link type="text/css" rel="stylesheet"	href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/jquery.dataTables.css"> 
<script type="text/javascript"	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/jquery.dataTables.min.js"></script> 
<script type="text/javascript"	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/jquery-ui.js"></script>
<!-- <script src="/MANAK/resources/app_srv/Scheme/HallMarking/Jeweller/ApplicationSubmission/JS/Datatable_jeweller.js" type="text/javascript"></script> -->
    

<script>
	$(document)
			.ready(
					function() {

						$("#messageDiv").delay(4000).fadeOut(800);
					});
	</script>
 <div class="container-fluid">
 
 <div id="messageDiv">  
		<c:if test="${fail==0}">
			<div class="alert alert-danger">
				<c:out value="${mg}"></c:out>
			</div>
		</c:if>
	</div>
	
 
 
 		<div class="h3 bold text-center">
 		Transaction Information
        </div>
   	
 		<form:form cssClass="form-horizontal" method="post" id="payform1" name="payform1" action="" commandName="model1"><div class="col-md-1"></div>
		<div class="col-md-10">
 		<fieldset class="text-center">
 		
 		<div class="col-md-12 form-group"> 		
 		<div class="col-md-2">Transaction No. :</div>
 		<div class="col-md-4">
 		<form:input path="" type="text" class="form-control" name="tid" id="tid" placeholder="Enter Transaction Number" required="true"/>
 		</div>
 		</div> 		 	 					 		
			 		
			 	<div class="form-group col-md-12 col-sm-12 text-center pad-top">			 		
			 		<input type="button" id="submit1" class="btn btn-primary" value="View Details" onclick="calltable()"/>			 		
				</div>
		 		
 		</fieldset>
 		</div>
 	</form:form>
 	<div class="col-md-12"></div>
 	<fieldset class="text-center">
 	<div id="tab1">
 	<table  id="tab_logic" class="table table-bordered" >
                <thead class="filters">
                <tr  style="background-color: #338ec9; color: #ffffff;">
                 	<th data-toggle="true" data-sort-ignore="true">&nbsp;</th> 
                 	<th>
                        Application No.
                    </th>
                    <th>
                        Firm Name
                    </th>
                    <th>
                        Branch No.
                    </th>
                    <th data-hide="phone,tablet" data-sort-ignore="true">
                       Date
                    </th>                   
                    <th data-hide="phone,tablet">
                     Status</th>                      
                    <th data-hide="phone,tablet" data-sort-ignore="true">
                    transaction id                    	
                    </th> 
                     <th data-hide="phone,tablet" data-sort-ignore="true">
                    amount                   	
                    </th>                    
                     <th data-hide="phone,tablet" data-sort-ignore="true">
                    receipt no.                    	
                    </th> 
                    <th data-hide="phone,tablet" data-sort-ignore="true">
                    Payment for               	
                    </th> 
                                    
                    <th data-hide="phone,tablet" data-sort-ignore="true">
                    Payment Through                   	
                    </th>
                    
                    
                    <th data-hide="phone,tablet" data-sort-ignore="true">
                          Pay <i class="glyphicon glyphicon-arrow-down" data-toggle="dropdown" aria-expanded="false"></i>
                    </th>
                    <th data-hide="phone,tablet" data-sort-ignore="true">
                          Status <i class="glyphicon glyphicon-arrow-down" data-toggle="dropdown" aria-expanded="false"></i>
                    </th>
                    </tr>
                </thead>
                <tbody id="tab1body">
 	
 				</tbody>
              
            </table>
 	</div>
 	</fieldset>
 				
 		</div> 		  

<script>
function callwindow(id){
var msg=id;

 if(confirm("Want to update transaction ID = "+ msg+ " ")){
	document.payform1.method = "POST";
 	document.payform1.action = "/MANAK/s2sBKentryforFeeForBis1Hall?tid="+msg; 
 	document.payform1.submit();
 	alert("Done..!!");
} 
}
</script>
<script>
function query(id){
	//alert(id);
	
	 window.open("/MANAK/queryAPIWithServlet?trno="+id+"&type=normal", "_blank", "toolbar=yes,top=200,left=200,width=600,height=500");
	
		
}
</script>
<script>
function querycorp(trno){
    var url="/MANAK/queryAPIWithServlet?trno="+trno;
    var myWindow =window.open(url,'Query API','child', 'scrollbars,width=200,height=80,top=200,left=200');
    myWindow.resizeTo(600, 500); 
    myWindow.moveTo(100, 200);
     myWindow.focus();
}
</script>

<script>
function calltable(){
	var msg=$('#tid').val();
	//alert(msg+" ok");
	$.ajax({
		type : "post",
		url : "/MANAK/gets2sBKentRyforFeeByBiS1ForHall?tid="+msg,
		data : "tid="+msg,
		
		success : function(response){			
			
			$('#tab1body').html('');
       	 
						//alert("yes");	 							
						theData ="";						                
						//alert("yes1");	
						 for(var i = 0; i < response.length; i++){		//alert("yes21");				 
							 var ArrNames = response[i].payment_url.split("|");   
							 var Name1 = ArrNames[20];
							 var Name2 = ArrNames[0];
							 var payfor='';
							 var s1='';
							 if(Name1=='1'){
								 payfor='Application Submission';
							 }else if(Name1 =='2'){
								 payfor='Renewal';
							 }else if(Name1=='11'){
								 payfor='Previous Dues';
							 }else if(Name1=='12'){
								 payfor='Production Return';
							 }else if(Name1=='13'){
								 payfor='Inclusion';
							 }
							 
							 
							// var ArrNamesid = ArrNames[18].split("t");
							// alert(ArrNamesid[2]);
							 
							// var ArrNamesid1 = ArrNamesid[2];
						//	alert(ArrNamesid1);
							// var len = ArrNamesid1.length-6;//alert(len);
							// var s = ArrNamesid1.substr(0,len);
							 var tid=response[i].transactionNumber;
// 							 s=s.replace("k", ",");
// 							 s=s.replace("k", ",");
// 							 s=s.replace("k", ",");
// 							 s=s.replace("k", ",");
						//	alert(s);
							 
// 							 if(Name1=='11'){
// 								 s1=" (id="+s+")";
// 							 }
						
							 var paythrough='';
							 if(Name2=='BUREAUSIND'){
								 paythrough='Retail Banking';
							 }else if(Name2 =='BUREAUSIN'){
								 paythrough='Corporate Banking';
							 }
							 
							theData += "<tr>";	
							
							 theData +="<td>";
				    		 theData += i+1;
							 theData +="</td>";
							 
							 
							 
							 theData +="<td>";
							 theData += response[i].applicationId;
							 theData +="</td>";
							 
							 theData +="<td>";
							 theData += response[i].firm_name;
							 theData +="</td>";
							 
							 theData +="<td>";
							 theData += response[i].branch_id;
							 theData +="</td>";
							 
							 theData +="<td>";
							 theData += response[i].tr_date;
							 theData +="</td>";
							 
							 theData +="<td>";
							 theData += response[i].payment_status;
							 theData +="</td>";
							 
							 theData +="<td>";
							 theData += response[i].transactionNumber;
							 theData +="</td>";
							 
							 theData +="<td>";
							 theData += response[i].amount;
							 theData +="</td>";
							 
							 theData +="<td>";
							 if(response[i].receipt_no!=null)
						 /* 	theData +="<a target='_blank' href='/MANAK/viewPreviousFeeReceipt?docName=FeeReceipt_"+tid+".pdf'>"+response[i].receipt_no+"</a>"; */
							 	theData +="<a target='_blank' href='/MANAK/genFeeReceiptForm_HallMarking?numTransactionNumber="+tid+"&applicationid="+response[i].applicationId+"&branchid="+response[i].branch_id+"&SchemeId=2&ActionId=1'>"+response[i].receipt_no+"</a>"; 
							 	else
								 theData += "";
							 theData +="</td>";
							 
							 theData +="<td>";
							 theData += payfor+s1;
							 theData +="</td>";
							 
							 theData +="<td>";
							 theData += paythrough;
							 theData +="</td>";
							 
							 theData +="<td>";
							 if(response[i].payment_status=='0'){
							 theData += "<input type='button' id='submit1' class='btn btn-primary' value='Pay Fee' onclick='callwindow("+response[i].transactionNumber+")'/>";
							 }
							 theData +="</td><td>";
							 if(paythrough=='Retail Banking'){
							 theData += "<input type='button' id='query' class='btn btn-primary' value='Query' onclick='query("+response[i].transactionNumber+")'/>";
							 }else{
								 theData += "<input type='button' id='query' class='btn btn-primary' value='Query' onclick='querycorp("+response[i].transactionNumber+")'/>";
							 }
							 theData +="</td>";
							 
							 theData +="</tr>";
						 }	
				  
				  $('#tab1body').append(theData);	
			
		},
		error : function(e){
			alert("No Data Found.");
		}		
	});
}
</script>
 		