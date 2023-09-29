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
 function disableRefresh(netscape){
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
	} 
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
  $(document).ready(function() {
	  var url=window.location.href;
		var temp=url.split("://")
		var temp2=temp[1].split("/MANAK/");
		$('#hiddenurl').val(temp2[0]);
		$("#ratailPayButton").prop("checked", true);
  });
</script>
  <div class="container">
 		<div class="h3 bold text-center">
 		<spring:message code="label.application"></spring:message>
 		<spring:message code="label.fee"></spring:message>
 		<spring:message code="label.and"></spring:message>
 		<spring:message code="label.contactbis"></spring:message>
 		</div>
 		<form:form cssClass="form-horizontal" method="post" id="frm" name="frm" action="PayInitiateFeeHMApplicantSubmit" commandName="model">
 		<form:hidden path="hiddenurl" id="hiddenurl" name="hiddenurl" value=""/>
 		<form:hidden path="numTotal" id="numTotal" name="numTotal"/>
 			<form:hidden path="utgst" id="utgst" name="utgst"/>
 			<form:hidden path="igst" id="igst" name="igst"/>
 			<form:hidden path="cgst" id="cgst" name="cgst"/>
 			<form:hidden path="sgst" id="sgst" name="sgst"/>
 			<form:hidden path="numTotalincludegst" id="numTotalincludegst" name="numTotalincludegst"/>
 			<form:hidden path="feeType" id="feeType" name="feeType"/>
 			<form:hidden path="feeTypeId" id="feeTypeId" name="feeTypeId"/>
 			<form:hidden path="csrftokenurl" id="csrftokenurl" name="csrftokenurl"/>
 			<form:hidden path="csrftoken" id="csrftoken" name="csrftoken"/>
 			<form:hidden path="eappid" id="eappid" name="eappid"/>
 			<form:hidden path="ebranchid" id="ebranchid" name="ebranchid"/>
 			<form:hidden path="ecml" id="ecml" name="ecml"/>
 			<form:hidden path="appid" id="appid" name="appid"/>
 			<form:hidden path="branchid" id="branchid" name="branchid"/>
 			<form:hidden path="cml" id="cml" name="cml"/>
 			<form:hidden path="actionId" id="actionId" name="actionId"/>
 			<form:hidden path="schemeId" id="schemeId" name="schemeId"/>
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
          		<form:radiobutton path="paymentCorp" value="0" id="ratailPayButton"/> Individual/Retail Banking
          	</th>
          	<th width="30%" class="nobreak text-left" data-class="expand" data-sort-initial="true" class="hide">
          		<form:radiobutton path="paymentCorp" value="1" id="corpPayButton" /> With Corporate Netbanking
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
 			<div class="col-md-12 text-center pad-top pad-bottom">		
		  				<span class="pad-left"> <a class="btn btn-primary" href="${backurl}"><spring:message code="label.back"></spring:message></a></span>
						<button type="submit" name="Submit" id="Submit" value="Submit" onclick="proceed_payment()" class="btn btn-primary">
						<spring:message code="label.proceed_to_payment"></spring:message>
						</button>
						
			  			 
			</div>
 		</form:form>
</div>
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
</script>