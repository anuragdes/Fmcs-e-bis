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
 		<%-- <spring:message code="label.Branch"></spring:message>
 		<spring:message code="label.detail"></spring:message> --%>
        </div>
        
        <c:if test="${schemeId==2}">
	<c:if test="${numActionId==1}">
		<%-- <div class="col-md-12 text-center">
			<%@include file="/app_srv/Scheme/HallMarking/stepNavigation/forms/Tr_step5_Form.jsp"%>
		</div> --%>
		<div class="progress">
<div class="circle done">
<span class="label">1</span>
<span class="title">Fill_Details</span>
</div>
<span class="bar done"></span>
<div class="circle done">
<span class="label">2</span>
<span class="title">Affidavit</span>
</div>
<span class="bar done"></span>
<div class="circle done">
<span class="label">3</span>
<span class="title">Documents</span>
</div>
<span class="bar done"></span>
<div class="circle done">
<span class="label">4</span>
<span class="title">Outlet_Details</span>
</div>
<span class="bar done"></span>
<div class="circle done">
<span class="label">5</span>
<span class="title">Payment_Details</span>
</div>
</div>
		
		<br>	
	</c:if>
</c:if> 
<c:if test="${schemeId==13}">
	<c:if test="${numActionId==1}">
		<%-- <div class="col-md-12 text-center">
			<%@include file="/app_srv/Scheme/HallMarking/stepNavigation/forms/Tr_step5_Form.jsp"%>
		</div> --%>
		<div class="progress">
<div class="circle done">
<span class="label">1</span>
<span class="title">Contact_Details</span>
</div>
<span class="bar done"></span>
<div class="circle done">
<span class="label">2</span>
<span class="title">Equipment_&_Employee_Details</span>
</div>
<span class="bar done"></span>
<div class="circle done">
<span class="label">3</span>
<span class="title">Undertaking</span>
</div>
<span class="bar done"></span>
<div class="circle done">
<span class="label">4</span>
<span class="title">Upload_Documents</span>
</div>
<span class="bar done"></span>
<div class="circle done">
<span class="label">5</span>
<span class="title">Payment_Details</span>
</div>
</div>
		
		<br>	
	</c:if>
</c:if> 
 		<c:if test="${schemeId==13}">
 		 <c:set var="formAction" value="AHCFee"/>
 		</c:if>
 		<c:if test="${schemeId != 13}">
 		<c:set var="formAction" value="Fee"/>
 		</c:if>
 		
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
			<%-- <form:hidden path="ebranchId" id="ebranchId" value="${ebranchId}"></form:hidden> --%>
			<%-- <form:hidden path="str_url" id="str_url" value="${url}"></form:hidden> --%>
			<%-- <form:hidden path="bi" value="${bi}"></form:hidden> --%>
			 <form:hidden path="tempId1" id="tempId1" value="${tempId1}"></form:hidden>
			<form:hidden path="tempAmt1" id="tempAmt1" value="${tempAmt1}"></form:hidden>
			<form:hidden path="totalvaljs" id="totalvaljs" value="${totalvaljs}"></form:hidden>
			<form:hidden path="totalrs" id="totalrs" value="${totalrs}"></form:hidden> 
			<form:hidden path="inclusionid" id="inclusionid" value="${inclusionid}"></form:hidden> 
			
			<form:hidden path="utgst" id="utgst" name="utgst"/>
 			<form:hidden path="igst" id="igst" name="igst"/>
 			<form:hidden path="cgst" id="cgst" name="cgst"/>
 			<form:hidden path="sgst" id="sgst" name="sgst"/>
 		<%-- 	<form:hidden path="discount" id="discount" name="discount"/>
 			<form:hidden path="discuntpart" id="discuntpart" name="discuntpart"/> --%>
 			<form:hidden path="numTotalincludegst" id="numTotalincludegst" name="numTotalincludegst"/>
 				<form:hidden path="cml_no" id="cml_no" name="cml_no"/>
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
 
 	<table class="footable blue table table-striped table-bordered table-hover dataTable sortable bg-head" id="feeTable">		
 	<thead>
          <tr>
          
          	<th><spring:message code="label.fee.discription"></spring:message></th>
          	<th class="text-right"><spring:message code="label.fee"></spring:message> ( <i class="fa fa-inr"></i> )</th>
          	<th class="text-center hide"><form:checkbox path="" class="CheckBox hide" name="CheckBox" value="" id="chk1" /></th>
          </tr>
	</thead>
 	<%int index=0;%>
 	 <c:forEach items="${lsFeeType}" var="list"  varStatus="loop">
 		  
          <tr id="${list.numFeeId}">
          <c:set var="type" value="${list.strAmountType}"/>
  <%-- <c:choose> --%>
        <c:if test="${type == 'Rs.' || type == 'rs.' || type == 'Rs' || type == 'rs'}">
           	 <form:input path="strArrfeeId1" id='<%="hidenFee"+index%>' value="${list.numFeeId}" class="hide" />
           	
            <td class="nobreak">
           

            <%-- <c:if test="${corporateDiscount>0.0 && list.numFeeId==24}"> --%>
            <c:out value="${list.strFeeDesc}"></c:out>
            <c:if test="${corporateDiscount>0.0 && list.numFeeId==24}">
            <h5><b><c:out value="(Rs. ${corporateDiscount} deducted as corporate discount)"></c:out></b></h5>
            </c:if>        
            	
            </td>
            
            
            <td class="nobreak text-right">
            
            <c:choose>
           	 <c:when test="${schemeId == 1 && numActionId == 2 && list.numFeeId != 7}">           	 
           	 <c:set var="year" value="${year}"/>
           	 <c:choose>
           		<c:when test="${list.numFeeId == 6}">
           				<fmt:formatNumber type="number"  pattern="##.##" maxFractionDigits="2" value="${list.numAmount}"  var="theFormattedValue"/>
           			<form:input  path="tempActualMArkingFee" min="1" value="${theFormattedValue}" onblur="calculateTotal()" id="amt_${list.numFeeId}" maxlength="10"/>
           		</c:when>
           		<c:otherwise>
           			<label id="amt_${list.numFeeId}"><fmt:formatNumber type="number" pattern="0.00" value="${list.numAmount}"/>
           		</c:otherwise>
           	</c:choose> 
           	<%--  <label id="amt_${list.numFeeId}"><fmt:formatNumber type="number" pattern="0.00" value="${list.numAmount}"/> --%>
           	
           	<%-- <c:out value="${year*list.numAmount}"></c:out> --%></label>
            <%--  <form:input path="strArrAmtId1" id='<%="hidenAmount"+index%>' value="${list.numAmount}" class="hide" /> --%>
           	
           	<form:input path="strArrAmtId1" id='hidenAmount${loop.index}' value="${list.numAmount}" class="hide" />
           	
           	 </c:when>
           	  <c:otherwise>  
           	  				<c:choose>
           	  				
           	 					<c:when test="${schemeId == 1 && numActionId == 1 && procedureId == 3}">
           	 					      
           						  	   <label id="amt_${list.numFeeId}"><fmt:formatNumber type="number" pattern="0.00" value="${2*list.numAmount}"/><%-- <c:out value="${2*list.numAmount}"></c:out> --%></label>
				          			   <form:input path="strArrAmtId1" id='<%="hidenAmount"+index%>' value="${2*list.numAmount}" class="hide" />
             					</c:when>
             					<c:otherwise>             					
             					<label id="amt_${list.numFeeId}"><fmt:formatNumber type="number" pattern="0.00" value="${list.numAmount}"/><%-- <c:out value="${list.numAmount}"></c:out> --%></label>
				          	   <%--  <form:input path="strArrAmtId1" id='<%="hidenAmount"+index%>' value="${list.numAmount}" class="hide" /> --%>
				          	   <form:input path="strArrAmtId1" id='hidenAmount${loop.index}' value="${list.numAmount}" class="hide" />
             					 </c:otherwise>
             				</c:choose>		
             
             
           	  </c:otherwise>
           	 </c:choose>
            	
            </td>
            <td class="hide">
            <label id="type_${list.numFeeId}"><c:out value="${list.strAmountType}"></c:out></label>
            </td>
            <td class="text-center hide">
          	   <form:checkbox path="strArrPaidId1" class="CheckBox checkAll" name="CheckBox"  value="${list.numFeeId}" id="chk_${list.numFeeId}" onchange="calculateTotal()"/>
          	</td>
    </c:if>
    <%--   </c:when>
      	      	
    <c:otherwise>    </c:otherwise>
 </c:choose> --%>
      	</tr>
      	
      	</c:forEach>
     </table> 
     
      <table class="footable table">
       <%-- <c:if test="${corporateDiscount>0.0}">
 			<tr>
          	<th width="85%" class="nobreak" data-class="expand" data-sort-initial="true">Corporate Discount (Rs. <fmt:formatNumber type="number" pattern="0.00" value="${corporateDiscount}"/> deducted from certification fee</th>
          	<th width="15%" class="nobreak text-right" data-class="expand" data-sort-initial="true" id="corporateDiscount" align="right">- <fmt:formatNumber type="number" pattern="0.00" value="${corporateDiscount}"/></th>
        	<!-- <th width="7%"></th> -->
        	</tr>
        </c:if>  --%>	
      	 <c:forEach items="${lsFeeType}" var="list"  varStatus="loop">		 
         	
          <tr id="${list.numFeeId}">
          <c:set var="type" value="${list.strAmountType}"/>
      	
      	
  <%-- 	<c:choose>   --%>  	
          	<c:if test="${type == 'percentage' || type == '%'}">
          	 <form:input path="strArrfeeId1" id='<%="hidenFee"+index%>' value="${list.numFeeId}" class="hide" />          	
      		<td class="nobreak text-left" width="50%">
            	<c:out value="${list.strFeeDesc}"></c:out>(<c:out value="${list.numAmount}  ${list.strAmountType}"></c:out>)           
            </td>
            <td class="nobreak text-right" width="41%">           
            	<div class="hide">
            	<label id="amt_${list.numFeeId}"><c:out value="${list.numAmount}  ${list.strAmountType}"></c:out></label>
            	<%-- <form:input path="strArrAmtId1" id='<%="hidenAmount"+index%>' value="${list.numAmount}" class="hide" /></div> --%>
            	<form:input path="strArrAmtId1" id='hidenAmount${loop.index}' value="${list.numAmount}" class="hide" /></div>
            	<label id="perAmt_${list.numFeeId}"></label>
           </td>
            <td class="hide">
            <label id="type_${list.numFeeId}"><c:out value="${list.strAmountType}"></c:out></label>
            </td>
            <td class="text-center hide" width="12%">
          	   <form:checkbox path="strArrPaidId1" class="CheckBox checkAll" name="CheckBox"  value="${list.numFeeId}" id="chk_${list.numFeeId}" onchange="calculateTotal()"/>
          	</td>
      
    <%--   	 <c:otherwise>    </c:otherwise>
 	</c:choose> --%>
 	</c:if>
       	</tr>
      	
   <%--    	<%index=index+1;%>	 --%>
    
      	</c:forEach>      	
 	</table>
 			
 	<table class="table table-hover">
 		<c:if test="${jkoutlettotal>0.0}">
 			<tr>
          	<th width="85%" class="nobreak" data-class="expand" data-sort-initial="true">Taxes deduced from Total For J & K State</th>
          	<th width="15%" class="nobreak text-right" data-class="expand" data-sort-initial="true" id="jkoutlettotal" align="right">- <c:out value="${jkoutlettotal}" ></c:out></th>
        	<!-- <th width="7%"></th> -->
        	</tr>
        </c:if> 
         <form:hidden  path="jkdiscount" id="jkdiscount" value="${jkoutlettotal}"></form:hidden>
       <%--  <c:if test="${corporateDiscount>0.0}">
 			<tr>
          	<th width="85%" class="nobreak" data-class="expand" data-sort-initial="true">Corporate Discount</th>
          	<th width="15%" class="nobreak text-right" data-class="expand" data-sort-initial="true" id="corporateDiscount" align="right">- <c:out value="${corporateDiscount}" ></c:out></th>
        	<!-- <th width="7%"></th> -->
        	</tr>
        </c:if> 
         	 --%>
 			<tr  style="background-color:#CEECF5">
          	<th width="35%" class="nobreak" data-class="expand" data-sort-initial="true"><spring:message code="label.total"></spring:message></th>
          	<th width="65%" class="nobreak text-right" data-class="expand" data-sort-initial="true" id="totalValue" align="right"></th>
          	<!-- <th width="7%"></th> -->
          	</tr>
          	
          	<tr id="changeRemarks" class="hidden">
          		<th>Remarks For Change of Actual Marking Fee</th>
          		<td><form:textarea path="actualFeeChangeRemarks"
          		id="changeActualFee" class="form-control" cols="150" required="true" value="N/A"></form:textarea></td>
          	</tr>
          	
            <form:hidden  path="numTotal" id="numTotalValue"></form:hidden>
            <form:hidden  path="numrsTotal" id="numrsTotal"></form:hidden>
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
          	 <%-- <c:if test="${schemeId==0 || schemeId==1}">
           <th width="30%" class="nobreak text-left" data-class="expand" data-sort-initial="true" >
          		<form:radiobutton path="paymentCorp" value="2" /> Offline Payment
          	</th> 
          	</c:if> --%> 
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
				  		<%-- 
				  		<c:if test="${numActionId==2}">
				  			<span class="pad-left"> <a class="btn btn-primary" href="performance"><spring:message code="label.back"></spring:message></a></span>
				  		</c:if> --%>
			
			<%-- <c:out value="Due to Some Maintainence work, Online payment stopped for some time."></c:out> --%>
						
						<button type="submit" name="Submit" id="Submit" value="Submit" class="btn btn-primary">
						<spring:message code="label.proceed_to_payment"></spring:message>
						</button>
						
			  			 
			</div>
		<br>	
		
		
		
 	</form:form>
 				
 		</div>
 	
<input type="hidden" name="StrCompFeeId" id="StrCompFeeId" value="${StrCompfee}"/>
<script>

$(document).ready(function() {
	
	var url=window.location.href;
	var temp=url.split("://")
	var temp2=temp[1].split("/MANAK/");
	//debugger;
	$('#hiddenurl').val(temp2[0]);
	
	$("#alertt").hide();
	/*$("#Submit").hide();
	$("#chk1").click(function(){
		$("#Submit").toggle();
	});*/
	
	$('#changeActualFee').hide().prop('required',false);
	
	$("#docAuthenPreFirmId input").change(function(){
		var temp = $("#docAuthenPreFirmId input").prop("checked");
		if(temp==true){
			$("#Submit").show();
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
	//alert(schemid);
	//alert(actionid);
	//alert(yr);
	$("#chk1").prop("checked", false);
	if(schemid == 1){//alert("app submit1");
		if(actionid == 1 )
			{//alert("app submit11");
				$('#amt_9').val('${sum}');
				$('#amt_9').text('${sum}');
			}
	}
	/* $('#amt_6').val('${sum12}');
	$('#amt_6').text('${sum12}');
	 */
/* 	if(schemid == 1 && actionid == 2){//alert("app submit2");
		if(actionid == 2)
	{ //alert("app submit22");
	if(yr == 1)
	{
		$('#amt_6').val('${sum12}');
		$('#amt_6').text('${sum12}');
		
	}else if(yr == 2)
		{
		$('#amt_6').val('${year*sum12}');
		$('#amt_6').text('${year*sum12}');
		
		}
	} else{//alert("app submit21");
		$('#amt_6').val('${sum12}');
		$('#amt_6').text('${sum12}');		
	}
	} */	
	//Checked
	var StrCompFeeId=$('#StrCompFeeId').val();
	
	var strCompArr=StrCompFeeId.split(',');
	for(var i=0;i<strCompArr.length;i++){
		$('#chk_'+strCompArr[i]).prop("checked", true);
		$('#chk_'+strCompArr[i]).addClass('compCheckBox');
		  
	}
	
	calculateTotal();
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

function calculateTotal(){
	
	var schemeId = $('#schemeId').val();
	var numActionId = $('#numActionId').val();
	if(schemeId == 1 && numActionId == 2){
		var amt= $('#amt_6').val();
		if(!amt){
			$('#Submit').addClass('hidden');
			sweetSuccess("Attention","Actual Marking Fee can not be blank");	
			$('#amt_6').focus();
			return false;
		}else{
			var condition = /^([0-9]{0,7})+([.][0-9]{1,2})?$/;
			if(condition.test(amt)){
				$('#Submit').removeClass('hidden');
			}else{
				sweetSuccess("Attention", "Actual Marking Fee can be numeric and upto 2 decimal place allowed");
				 return (false);
			}	
			
			
		}
	}
	
	var StrCompFeeId=$('#StrCompFeeId').val();	
	var strCompArr=StrCompFeeId.split(',');
	for(var i=0;i<strCompArr.length;i++){
		 $('#chk_'+strCompArr[i]).prop("checked", true); 
		 $('#chk_'+strCompArr[i]).attr('readonly',true); 
		$('#chk_'+strCompArr[i]).addClass('compCheckBox');
		/* $('.compCheckBox').click(function(event) { 
			$(this).prop("checked", true)
		});  */
	}
	
	
	
	var i=0;
	var temp=0;
	var tempAmount=0.00;
	var tempPercent=0.00;
	var total=0.00;  
	var tempAmts=0.00;
	
	var tempPerAmt=0.00;
	var tempAmount1=0.00;
	$('#feeTable tr').each(function(index){ //table loop		
		//console.log(index);
         if(temp!=0){
         i=i+1;
         var appId=$(this).prop("id");
         //var corporatedis="${corporateDiscount}";
         tempAmount1=tempAmount;
								if ($("#chk_" + appId).is(':checked')) 
								{
									if ($("#type_" + appId).text() == "Rs." || $("#type_" + appId).text()== "rs." || $("#type_" + appId).text()== "Rs" ||$("#type_" + appId).text()== "rs") 
									{
											var textFieldValue=	$("#amt_" + appId).val();	
											
											
											if(textFieldValue){
												tempAmount = tempAmount + parseFloat(textFieldValue);												
												if(parseFloat(textFieldValue) == parseFloat($('#hidenAmount'+(index-1)).val())){
													//open Text Area of remarks
													$('#changeRemarks').addClass('hidden');
													/* $('#changeActualFee').addClass('hidden'); */
													$('#changeActualFee').hide().prop('required',false);
													$('#changeActualFee').text('N/A');
												}else{
													$('#changeRemarks').removeClass('hidden');
													$('#changeActualFee').text('');
													/* $('#changeActualFee').removeClass('hidden'); */
													$('#changeActualFee').show().prop('required',true);
												}
											}else{
											tempAmount = tempAmount + parseFloat($("#amt_" + appId).text()) || 0;
											}
											//alert("tempAmount"+tempAmount);
									} 
									else if ($("#type_" + appId).text() == "%" ||$("#type_" + appId).text()==  "percentage") 
									{
										tempPercent = tempPercent + parseFloat($("#amt_" + appId).text()) || 0;//alert("tempPercent="+tempPercent);
										tempAmts = (tempAmount*tempPercent)/100;
										
										tempPerAmt=((tempAmount1*parseFloat($("#amt_" + appId).text()))/100).toFixed(2);
										
										var text="<font>"+tempPerAmt;
										$('#perAmt_'+appId).html(text);
										//alert("tempPerAmt="+tempPerAmt);
									}
								}
							}
							temp = temp + 1;
						});
	$('#numrsTotal').val(tempAmount);
	//alert("rs="+tempAmount);
	//alert("%="+tempAmts);
	var minusjktax='${jkoutlettotal}';

	//alert("minusjktax="+minusjktax);
		total = tempAmount + tempAmts - minusjktax;
		
	   var totall = total.toFixed(2);
		$('#totalValue').text(totall);
		$('#numTotalValue').val(totall);
		//alert("total  "+totall);
	}
</script>

<script>
$('#chk1').click(function(event) { 
	 
	 if(this.checked){
		   $('.checkAll').each(function() { //loop through each checkbox
	             this.checked = true;  //select all checkboxes with class "checkbox1" 	            
	         });
		   calculateTotal();
	   }else{
		   $('.checkAll').each(function() { //loop through each checkbox
			   
			   this.checked = false;  //select all checkboxes with class "checkbox1"       
			   
			   				
	         });
		   calculateTotal();
		   
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
