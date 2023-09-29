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
	var flag1=-1;
	var flag2=-1;
	var appID= "${model.appid}";
	var branchId= "${model.branchId}";
	var docid= 358;
	var schemeid= 1;
	debugger;
	if($("#box1").prop('checked') == true){
		flag1=1;
		if($("#optForInstallmentsFlag").val()=="1")
		{
			if($("#box2").prop('checked') == true){
				$.ajax({
					type : "POST",
						url : "/MANAK/checkForAnnexureIUpload",
						data: "appId="+appID+"&branchId="+branchId+"&docId="+docid+"&schemeId="+schemeid,
						async: false,
						success : function(response) {
							if(response=="Exists"){
								flag2=1;
							}
							else
								{
								alert("Upload filled and scanned undertaking (Annexure-1) on firm's letter head");
								e.preventDefault();
								flag2=0;
								}
						}
						
				});
			}else{
				flag2=0;
				alert("Please tick on checkbox");
			}
		}else{
			flag2=1;
		}
	}else{
		alert("Please tick on checkbox");
		flag1=0;
	}
	if(flag1==1 && flag2==1){
		document.frm.submit();
	}
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
	$('.select2-select').select2({ width: '100%' });
	$('#optForInstallmentsFlag').select2('val',0);
	optForInstallmentsFlag_change(0);
	if("${OriginalMarkingFee}"==0){
		$('#installmentsDiv').removeClass('show').addClass('hide');	
	}else{
		$('#installmentsDiv').removeClass('hide').addClass('show');
	}
	$("#box1").prop( "checked", true );
	
	$("#ratailPayButton").prop( "checked", true );	
});
function popup_annexure_upload(value){
	 var docID =  value;
	
	var eBranchId=$("#eBranchId").val();
	var eappId=$("#eAppid").val();
	var schemeId=$("#eScheme").val();
	
	
	
		newwindow =  window.open("/MANAK/uploadDocument?docID="+docID+"&appID="+eappId+"&branchID="+eBranchId+"&schemeId="+schemeId, 'window', 'width=1200,height=400,scrollbars=yes');
		if (window.focus) {newwindow.focus();   }           
	   if (!newwindow.closed) {newwindow.focus();}
	 return false; 	 	
}
function optForInstallmentsFlag_change(value){
	if(value==0){
	$('#FullMarkingFee').removeClass('hide').addClass('show');
	$('#MarkingFee50Percente').removeClass('show').addClass('hide');
	$('#TwoInstallments').removeClass('show').addClass('hide');
	$("#box2").prop( "checked", false );
	}else{
		$('#MarkingFee50Percente').removeClass('hide').addClass('show');
		$('#FullMarkingFee').removeClass('show').addClass('hide');	
		$('#TwoInstallments').removeClass('hide').addClass('show');
		//$("#box2").prop( "checked", true );
	}
}
</script> 
 <div class="container">
 <div class="h3 bold text-center">
 		<spring:message code="label.application"></spring:message>
 		<spring:message code="label.fee"></spring:message>
 		<spring:message code="label.and"></spring:message>
 		
 		<spring:message code="label.contactbis"></spring:message>
 		<%-- <spring:message code="label.Branch"></spring:message>
 		<spring:message code="label.detail"></spring:message> --%>
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
 		<form:form cssClass="form-horizontal" method="post" id="frm" name="frm" action="PCFeeApplicationSubmit" commandName="model">
 		<form:hidden path="feeid" value="${model.feeid}"/>
 		<form:hidden path="fee_description" value="${model.fee_description}"/>
 		<form:hidden path="fee_amount" value="${model.fee_amount}"/>
 		<form:hidden path="fee_amount50Percent" value="${model.fee_amount50Percent}"/>
 		<form:hidden path="csrftokenurl" value="${model.csrftokenurl}"/>
 		<form:hidden path="csrftoken" value="${model.csrftoken}"/>
 		<form:hidden path="eBranchId" value="${model.eBranchId}" id="eBranchId" name="eBranchId"/>
 		<form:hidden path="eAppid" value="${model.eAppid}" id="eAppid" name="eAppid"/>
 		<form:hidden path="eScale" value="${model.eScale}"/>
 		<form:hidden path="eScheme" value="${model.eScheme}"/>
 		<form:hidden path="eActionId" value="${model.eActionId}"/>
 		<form:hidden path="branchId" value="${model.branchId}" />
 		<form:hidden path="appid" value="${model.appid}" />
 		<form:hidden path="scale" value="${model.scale}"/>
 		<form:hidden path="scheme" value="${model.scheme}"/>
 		<form:hidden path="actionId" value="${model.actionId}"/>
 		<form:hidden path="womenEnterprenaurFlag" value="${model.womenEnterprenaurFlag}"/>
 		<form:hidden path="startupFlag" value="${model.startupFlag}"/>
 		<form:hidden path="nreFlag" value="${model.nreFlag}"/>
 		<form:hidden path="actualmarkingfee" value="${model.actualmarkingfee}"/>
 		<form:hidden path="hiddenurl" value=""/>
 		<fieldset>
 			
 			<legend><spring:message code="label.fee.details"></spring:message>:</legend>

			<div id="installmentsDiv">
 			<div>
 			<font  color="red" size="3px"></font><br><i class="fa fa-info-circle" style="font-size:24px"></i><a id ="DownloadCircularForTwoInstallments" class="blinking1" href="DownloadCircularForTwoInstallments" style="font-size:15px"> Relaxation Provision for MSME Units by BIS</a>
 			</div>
 			<div class="col-md-12">
 			<div class="col-md-3 bold">
 			Opt for Two Installments
 			</div>
 			<div class="col-md-1">
 			<form:select path="optForInstallmentsFlag" id="optForInstallmentsFlag" name="optForInstallmentsFlag" class="select2-select" onchange="optForInstallmentsFlag_change(this.value)">
 			<option value="0">No</option>
 			<option value="1">Yes</option>
 			</form:select>
 			</div>
 			</div>
 			</div>

 			<br>
 			<br>
 			<div id="FullMarkingFee" class="hide">
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
		<div id="MarkingFee50Percente" class="hide">
	<table
			class="footable blue table table-striped table-bordered table-hover dataTable sortable bg-head" id="MarkingFee50PercenteTable">
			<thead>
				<tr>
					<th class="text-center">S.No</th>
					<th class="text-center">Fee Description</th>
					<th class="text-right" >Fee Amount ( <i class="fa fa-inr"></i> )</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${MarkingFee50Percente}" var="list" varStatus="loop">
			<tr>
			<td class="text-center">${loop.index+1}</td>
			<td>
			<c:choose>
			<c:when test="${list.feeid==50||list.feeid==48||list.feeid==49||list.feeid==51 }">
			<c:out value="${list.fee}"></c:out>(<c:out value="${list.gstpercentage}"></c:out>%)
			 </c:when>
			 <c:when test="${list.feeid==9}">
			 ${list.fee} (50% of <i class="fa fa-inr"></i> ${OriginalMarkingFee} )
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
    
    <div class="col-md-12" id="docAuthenPreFirmId">
				 <div class="col-md-12 col-sm-12">
				
				   <p><input type="checkbox" required name="box1" id="box1" > <span style="background-color:#D8D8D8"> <spring:message code="label.t&C"></spring:message></span></p>
				
				</div>
			</div>
 			<div class="col-md-12">
				<div class="col-md-12 col-sm-12" >
					<textarea rows="1" cols="50"  class="col-md-12" disabled><spring:message code="label.feeChngNote"></spring:message></textarea>
				</div>
			</div>
			<fieldset id="TwoInstallments" class="hide">
			<legend>Undertaking For Two Installments</legend>
			<div class="col-md-12" id="divforInstallments">
				 <div class="col-md-12 col-sm-12">
				
				   <p style="color:red;font-size:17px"><input type="checkbox"  id="box2" name="box2" required >   <b>I agree to the Terms and Conditions of Undertaking<a href="DownloadAnnexure?annexureFlag=1" style="font-size:17px"> (Annexure - 1) </a> for payment of Minimum Marking Fee in two equal installments </b></p>
				   <!-- <p style="color:red;font-size:17px"><b> Click on <a href="DownloadAnnexure?annexureFlag=1" style="font-size:17px">Annexure-I</a> to download it, fill it and upload it below. </b> </p> -->
				   <p style="color:red;font-size:17px" ><b>* Upload filled and scanned undertaking (Annexure-1) on firm's letter head</b>&nbsp;&nbsp; <button class='glyphicon glyphicon-upload btn btn-default'  onclick='popup_annexure_upload(this.id)' id='${eAnnexureIid}' type='button'></button> </p>
				</div>
			</div>
 			</fieldset>
			<div class="col-md-12 text-center pad-top pad-bottom">		
 			  			<span class="pad-left"> <a class="btn btn-primary" href="${url}"><spring:message code="label.back"></spring:message></a></span>
						<button type="button" name="Submit" id="Submit" value="Submit" class="btn btn-primary" onclick="check()" >
						<spring:message code="label.proceed_to_payment"></spring:message>
						</button>
						
			  			 
			</div>
			</fieldset>
			</form:form>
 		</div>