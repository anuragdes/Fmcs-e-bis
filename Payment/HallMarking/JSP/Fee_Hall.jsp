 <%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
 <%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
 <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
 
 <%@ page isELIgnored="false" %>

    
<link href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/footable.core.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/bootstrapValidator.css">
 <script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/bootstrapValidator.js"></script>

 <link rel="stylesheet" href="/MANAK/resources/app_srv/Scheme/HallMarking/stepNavigation/forms/hall_stepNavigation.css">
  <script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/CSRF2.js"></script>  
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/md5.js"></script> 
<script>
//Disable F5 and Ctrl+R on Google Chrome
  document.onkeydown = function() 
{
    switch (event.keyCode) 
    {
        case 116 : //F5 button
            event.returnValue = false;
            alert("F5 Click Disabled");
            event.keyCode = 0;
           
            return false;
        case 82 : //R button
            if (event.ctrlKey) 
            {
            	alert("Ctrl Click Disabled");
                event.returnValue = false;                
                event.keyCode = 0;               
                return false;
            } 
    }
};  

//Disable F5 and Ctrl+R in FireFox
/* function disableRefresh(netscape){
	var F5=(netscape||event).keyCode;
		if(F5 == 116){
			if(!netscape){
				event.keyCode=0;
				}
			return false;
			}
		if(F5 == 82){
			if(!netscape){
				event.keyCode=0;
				}
			return false;
			}
	} */
//document.onkeydown=document.onkeypress=disableRefresh;

 //Disable right click script on all Browser GC,MF,IE
 var message = "";
function clickIE() {
    if (document.all) {
        (message);
        return false;
    }
}

function clickNS(e) {
    if (document.layers || (document.getElementById && !document.all)) {
        if (e.which == 2 || e.which == 3) {
        	alert("Mouse Right Click Disabled");
            (message);
            return false;
        }
    }
}
if (document.layers) {
    document.captureEvents(Event.MOUSEDOWN);
    document.onmousedown = clickNS;
} else {
    document.onmouseup = clickNS;
    document.oncontextmenu = clickIE;
}
document.oncontextmenu = new Function("return false"); 
</script>   
<script>

  function showKeyCode(event) {	
	if(event.keyCode==116 || event.keyCode==17){
	 	event.returnValue = false;
        event.keyCode = 0;
        return false;
	}
	if (event.ctrlKey ) {
       return false;
    }
}   
</script>


  <div class="container">
 		<div class="h3 bold text-center">
 		<spring:message code="label.application"></spring:message>
 		<spring:message code="label.fee"></spring:message>
 		<spring:message code="label.and"></spring:message>
 		<spring:message code="label.contactbis"></spring:message>
        </div>

 		<form:form cssClass="form-horizontal" method="post" id="frm" name="frm" action="Fee_HallMarking" commandName="model1">
 		
 		<form:hidden path="tempId1Imp" value="${tempId1Imp}"/>
		<form:hidden path="tempAmt1Imp" value="${tempAmt1Imp}"/>
 		<input type="hidden" id='hiddenurl' name='hiddenurl' />
 		
 		
			<form:hidden path="numFeeId" id="numFeeId"></form:hidden>
			<form:hidden path="strAppId" id="strAppId" value="${AppId}"></form:hidden>
			<form:hidden path="strCmlId" id="strCmlId" value="${cmlNo}"></form:hidden>
			<form:hidden path="schemeId" id="schemeId" value="${schemeId}"></form:hidden>
			<form:hidden path="numActionId" id="numActionId" value="${numActionId}"></form:hidden>
			 <form:hidden path="branchId"  value="${branchId}"></form:hidden> 
			<form:input id="csrfTok" path="csrfTok" class="hide" name="csrfTok" value="${csrftoken}" />
			 <form:hidden path="tempId1" id="tempId1" value="${tempId1}"></form:hidden>
			<form:hidden path="tempAmt1" id="tempAmt1" value="${tempAmt1}"></form:hidden>
			<form:hidden path="totalvaljs" id="totalvaljs" value="${totalvaljs}"></form:hidden>
			<form:hidden path="totalrs" id="totalrs" value="${totalrs}"></form:hidden> 
			<form:hidden path="inclusionid" id="inclusionid" value="${inclusionid}"></form:hidden> 
			<div id="duplicateFeeId">
 		<fieldset>
 			
 			<legend><spring:message code="label.contactbis"></spring:message>:</legend>
 			
 			<div class="col-md-12 ">
				<div class="col-md-12 col-sm-12 ">
					<div class="col-md-3 ">
						<label><spring:message code="label.address"></spring:message>:</label>
					
					</div>
					
					<div class="col-md-9 ">
						<c:out value="${fn:toUpperCase(Branchaddress)}"></c:out>
					</div>
				</div>
 			</div>
 			
 			<c:if test="${Branchxtraaddress != ''}">
 			<div class="col-md-12 ">
				<div class="col-md-12 col-sm-12 ">
					<div class="col-md-3 ">
					
					</div>
					<div class="col-md-9 ">
	 					<span><b>DISTRICT :</b></span><span id="bxadd"><c:out value="${fn:toUpperCase(Branchxtraaddress)}"></c:out></span>
					</div>
				</div>
 			</div>
 			</c:if>
 			
 			<div class="col-md-12 ">
				<div class="col-md-12 col-sm-12 ">
					<div class="col-md-3 ">
 				<label><spring:message code="label.branchcontact"></spring:message>:</label>			
					</div>
					<div class="col-md-9 ">
 				<c:out value="${Branchcontact}"></c:out>
					</div>
				</div>
 			</div>
 			
 			<div class="col-md-12 ">
				<div class="col-md-12 col-sm-12 ">
					<div class="col-md-3 ">
 				<label><spring:message code="label.email"></spring:message>:</label>
					</div>
					<div class="col-md-9 ">
 				<c:out value="${Branchmail}"></c:out>
					</div>
				</div>
 			</div>
 			
 			<div class="col-md-12 ">
				<div class="form-group col-md-12 col-sm-12 ">
					<div class="col-md-6 ">
					<h6><spring:message code="label.fee.note"></spring:message></h6>	
						</div>
				</div>
 			</div>
 			
 			
 		</fieldset>
 			
 				<br/>
 			 			
 		<fieldset>
 			<legend><spring:message code="label.application.fee.details"></spring:message>:</legend> 
 			<form:hidden path="numTotal" id="numTotal" name="numTotal"/>
 			<form:hidden path="utgst" id="utgst" name="utgst"/>
 			<form:hidden path="igst" id="igst" name="igst"/>
 			<form:hidden path="cgst" id="cgst" name="cgst"/>
 			<form:hidden path="sgst" id="sgst" name="sgst"/>
 			<form:hidden path="discount" id="discount" name="discount"/>
 			<form:hidden path="discuntpart" id="discuntpart" name="discuntpart"/>
 			<form:hidden path="numTotalincludegst" id="numTotalincludegst" name="numTotalincludegst"/>
 			<form:hidden path="feeid" id="feeid" name="feeid"/>           	  				
 			<form:hidden path="fee_description" id="fee_description" name="fee_description"/>
 			<form:hidden path="fee_amount" id="fee_amount" name="fee_amount"/>
 			<form:hidden path="cml_no" id="cml_no" name="cml_no"/>
 <table class="footable blue table table-striped table-bordered table-hover dataTable sortable bg-head" id="payment_table">
 <thead>
 <tr>
 <th><spring:message code="label.fee.discription"></spring:message></th>
 <th class="text-right"><spring:message code="label.fee"></spring:message> ( <i class="fa fa-inr"></i> )</th>
 </tr>
 </thead>
 <tbody>
 <c:forEach items="${payment}" var="list" >
 <tr>
 <td>
 <c:choose>
 <c:when test="${list.feeid==50||list.feeid==48||list.feeid==49||list.feeid==51 }">
 <c:out value="${list.fee}"></c:out>(<c:out value="${list.gstpercentage}"></c:out>%)
 </c:when>
 <c:when test="${list.feeid==74 }">
 <c:out value="${list.fee}"></c:out>(<c:out value="${list.gstpercentage}"></c:out>%)
 </c:when>
 <c:otherwise>
 <c:out value="${list.fee}"></c:out>
 </c:otherwise>
 </c:choose>
</td>
<td class="text-right">

<strong><c:out  value="${list.ammount}"></c:out></strong>
</td>
 </tr>
 </c:forEach>
 </tbody>
 </table>

 			

 	<table class="table table-hover">
 		<tr>
          	<th width="10%" class="nobreak" data-class="expand" data-sort-initial="true">Payment Mode</th>
          	<th width="20%" class="nobreak text-left" data-class="expand" data-sort-initial="true" >
          		<form:radiobutton path="paymentCorp" value="0" selected="selected"  id="ratailPayButton"/> Individual/Retail Banking
          	</th>
          	<th width="30%" class="nobreak text-left" data-class="expand" data-sort-initial="true" class="hide">
          		<form:radiobutton path="paymentCorp" value="1" selected="selected"  id="corpPayButton" /> With Corporate Netbanking
          	</th>
        </tr>
 	</table>
 			<div class="alert alert-info" id="corpPayNotificationDiv">
  				<strong>Info!</strong> <span id="infoID">In case of payment failure, if money is deducted from your account, the same would be refunded within 7 working days. Please contact your bank in case of further queries. Kindly do not make another attempt for payment unless there is a failure.</span>
		</div>
 	<script>
         $("#corpPayButton").click(function(){
        	 //$("#corpPayNotificationDiv").removeClass("hide").addClass("show");
        	 $("#infoID").text("The success of this transaction is subject to confirmation from your bank which may take up to 48 hours. Kindly do not make another attempt for payment unless there is a failure.");
         }); 
         $("#ratailPayButton").click(function(){
        	 //$("#corpPayNotificationDiv").removeClass("show").addClass("hide");
        	 $("#infoID").text("In case of payment failure, if money is deducted from your account, the same would be refunded within 7 working days. Please contact your bank in case of further queries. Kindly do not make another attempt for payment unless there is a failure.");
         }); 
    </script>
 				</fieldset>
 			<div class="col-md-12" id="docAuthenPreFirmId">
				 <div class="col-md-12 col-sm-12">
				
				   <p><input type="checkbox" required name="box1" > <span style="background-color:#D8D8D8"> <spring:message code="label.t&C"></spring:message></span></p>
				
				</div>
			</div>
 			<div class="col-md-12">
				<div class="col-md-12 col-sm-12" >
					<textarea rows="1" cols="50"  class="col-md-12" disabled><spring:message code="label.feeChngNote"></spring:message></textarea>
				</div>
			</div>
			
			
 			
			</div>
			
			<div class="col-md-12 bold h4" id="alertt" align="center">
			Not allowed to pay fee for same fee type at the same time.
			</div>
			
 			<div class="col-md-12 text-center pad-top pad-bottom">		
								
 						
						<c:if test="${schemeId!=0 && numActionId!=13}">
 			  				<span class="pad-left"> <a class="btn btn-primary" href="${url}"><spring:message code="label.back"></spring:message></a></span>
				  		</c:if>	 
						<button type="submit" name="Submit" id="Submit" value="Submit" onclick="proceed_payment()" class="btn btn-primary">
						<spring:message code="label.proceed_to_payment"></spring:message>
						</button>
						
			  			 
			</div>
		<br>	
		
		
		
 	</form:form>
 				
 		</div>
 	
<input type="hidden" name="StrCompFeeId" id="StrCompFeeId" value="${StrCompfee}"/>
<script>
function proceed_payment(){
$('#Submit').prop('disabled',true);	
var temp = $("#docAuthenPreFirmId input").prop("checked");
if(temp==true)
{
	document.frm.submit();
}else{
	alert("Please agree Terms and Conditions.");
}
}
$(document).ready(function() {
	var url=window.location.href;
	var temp=url.split("://")
	var temp2=temp[1].split("/MANAK/");
	//debugger;
	$('#hiddenurl').val(temp2[0]);
	$("#alertt").hide();
	$('#changeActualFee').hide().prop('required',false);
	
	$("#docAuthenPreFirmId input").change(function(){
		var temp = $("#docAuthenPreFirmId input").prop("checked");
		if(temp==true){
			$("#Submit").show();
			$('#Submit').prop('disabled',false);	
		}
		else{
			$("#Submit").hide();
		}
	});
	var procid='${procedureId}';
	var schemid='${schemeId}';
	var actionid='${numActionId}'; 
	var yr='${year}';
	
	var a='${duplicateFeeId}';
	if(a == 1){
		$("#duplicateFeeId").hide();
		$("#Submit").hide();
		$("#alertt").show();
	}
	$("#chk1").prop("checked", false);
	if(schemid == 1){//alert("app submit1");
		if(actionid == 1 )
			{//alert("app submit11");
				$('#amt_9').val('${sum}');
				$('#amt_9').text('${sum}');
			}
	}
	var StrCompFeeId=$('#StrCompFeeId').val();
	
	var strCompArr=StrCompFeeId.split(',');
	for(var i=0;i<strCompArr.length;i++){
		$('#chk_'+strCompArr[i]).prop("checked", true);
		$('#chk_'+strCompArr[i]).addClass('compCheckBox');
		  
	}
	
	//calculateTotal();
});


</script>



<script>
function sweetSuccess(title,msg){
	swal({
		  title: title,
		  text: msg,
		  timer: 3000
		});
}

$('#chk1').click(function(event) { 
	 
	 if(this.checked){
		   $('.checkAll').each(function() { //loop through each checkbox
	             this.checked = true;  //select all checkboxes with class "checkbox1" 	            
	         });
		   //calculateTotal();
	   }else{
		   $('.checkAll').each(function() { //loop through each checkbox
			   
			   this.checked = false;  //select all checkboxes with class "checkbox1"       
			   
			   				
	         });
		   //calculateTotal();
		   
	   } 
	
});   

</script>

<script>

  function checkForm(form)
  {
    
    if(!form.box1.checked) {
      alert("Please indicate that you accept the Terms and Conditions");
      form.box1.focus();
      return false;
    }
    return true;
  }

  $('.compCheckBox').click(function(event) { 
		$(this).prop("checked", true)
	});
</script>
