<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page isELIgnored="false" %>
<link rel="stylesheet" type="text/css" href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/select2.css" />
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/select2.min.js"></script>
<script type="text/javascript">
function check(){
			document.frm.submit();
}
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
$(document).ready(function(){
	var url=window.location.href;
	var temp=url.split("://")
	var temp2=temp[1].split("/MANAK/");
	$('#hiddenurl').val(temp2[0]);
	$("#ratailPayButton").prop( "checked", true );	
});


</script> 
 <div class="container">
 <div class="h3 bold text-center">
 		<spring:message code="label.application"></spring:message>
 		<spring:message code="label.fee"></spring:message>
 		<spring:message code="label.and"></spring:message>
 		
 		<spring:message code="label.contactbis"></spring:message>

        </div>
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
	 					<span><b>DISTRICT :</b></span><c:out value="${fn:toUpperCase(Branchxtraaddress)}"></c:out>
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
 		<form:form cssClass="form-horizontal" method="post" id="frm" name="frm" action="COCFeeRenewalSubmit" commandName="model">
 		<form:hidden path="feeid" value="${model.feeid}"/>
 		<form:hidden path="fee_description" value="${model.fee_description}"/>
 		<form:hidden path="fee_amount" value="${model.fee_amount}"/>
 		<form:hidden path="csrftokenurl" value="${model.csrftokenurl}"/>
 		<form:hidden path="csrftoken" value="${model.csrftoken}"/>
 		<form:hidden path="eBranchId" value="${model.eBranchId}" id="eBranchId" name="eBranchId"/>
 		<form:hidden path="eAppid" value="${model.eAppid}" id="eAppid" name="eAppid"/>
 		<form:hidden path="eScheme" value="${model.eScheme}"/>
 		<form:hidden path="eActionId" value="${model.eActionId}"/>
 		<form:hidden path="branchId" value="${model.branchId}" />
 		<form:hidden path="eLicenceNumber" value="${model.eLicenceNumber}" />
 		<form:hidden path="appid" value="${model.appid}" />
 		<form:hidden path="scheme" value="${model.scheme}"/>
 		<form:hidden path="actionId" value="${model.actionId}"/>
 		<form:hidden path="hiddenurl" value=""/>
 		<fieldset>
 			
 			<legend><spring:message code="label.fee.details"></spring:message>:</legend>


 			<br>
 			<br>
 			<div id="FullMarkingFee" >
		<table
			class="footable blue table table-striped table-bordered table-hover dataTable sortable bg-head" id="FullMarkingFeeTable">
			<thead>
				<tr>
					<th class="text-center">S.No</th>
					<th class="text-center">Fee Description</th>
					<th class="text-right" >Fee Amount ( <i class="fa fa-inr"></i> )</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${FullMarkingFee}" var="list" varStatus="loop">
			<tr>
			<td class="text-center">${loop.index+1}</td>
			<td>
			<c:choose>
			<c:when test="${list.feeid==50||list.feeid==48||list.feeid==49||list.feeid==51 }">
			<c:out value="${list.fee}"></c:out>(<c:out value="${list.gstpercentage}"></c:out>%)
			 </c:when>
			<c:otherwise>
			  <c:out value="${list.fee}"></c:out>
			</c:otherwise>
			</c:choose>
			</td>
			<td class="text-right">
			<c:choose>
			<c:when test="${list.feeid==50||list.feeid==48||list.feeid==49||list.feeid==51 }">
			<i class="fa fa-inr"></i> <c:out value="${list.ammount}"></c:out>
			 </c:when>
			<c:otherwise>
			 <i class="fa fa-inr"></i> <c:out value="${list.ammount}"></c:out>
			</c:otherwise>
			</c:choose>
			</td>
			</tr>
			</c:forEach>
			</tbody>
		</table>
		</div>
	<table class="table table-hover">
 		<tr>
          	<th width="10%" class="nobreak" data-class="expand" data-sort-initial="true">Payment Mode</th>
          	<th width="20%" class="nobreak text-left" data-class="expand" data-sort-initial="true" >
          		<form:radiobutton path="payment_type" value="0" selected="selected"  id="ratailPayButton"/> Individual/Retail Banking
          	</th>
          	<th width="30%" class="nobreak text-left" data-class="expand" data-sort-initial="true" class="hide">
          		<form:radiobutton path="payment_type" value="1" id="corpPayButton" /> With Corporate Netbanking
          	</th>
        </tr>
 	</table>
 			<div class="alert alert-info" id="corpPayNotificationDiv">
  				<strong>Info!</strong> <span id="infoID">In case of payment failure, if money is deducted from your account, the same would be refunded within 7 working days. Please contact your bank in case of further queries. Kindly do not make another attempt for payment unless there is a failure.</span>
		</div>
		<script>
         $("#corpPayButton").click(function(){
        	 $("#infoID").text("The success of this transaction is subject to confirmation from your bank which may take up to 48 hours. Kindly do not make another attempt for payment unless there is a failure.");
         }); 
         $("#ratailPayButton").click(function(){
        	 $("#infoID").text("In case of payment failure, if money is deducted from your account, the same would be refunded within 7 working days. Please contact your bank in case of further queries. Kindly do not make another attempt for payment unless there is a failure.");
         }); 
    </script>
    

			<div class="col-md-12 text-center pad-top pad-bottom">		
 			  			<span class="pad-left"> <a class="btn btn-primary" href="${url}"><spring:message code="label.back"></spring:message></a></span>
						<button type="button" name="Submit" id="Submit" value="Submit" class="btn btn-primary" onclick="check()" >
						<spring:message code="label.proceed_to_payment"></spring:message>
						</button>
						
			  			 
			</div>
			</fieldset>
			</form:form>
 		</div>