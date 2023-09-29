  <%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
 <%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
 <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
 <%@ page isELIgnored="false" %>


	<%


String strAuthStatus = "";



	if(request.getAttribute("AS") != null)
	{
		strAuthStatus = request.getAttribute("AS").toString();
	}
	System.out.println("Inside response strAuthStatus = "+strAuthStatus);
	
	%>
	
	
	
	
	<script>
  function submittedApp(str)
  {
	   if(str == 'P')
		  {
		  	//document.getElementById('payform').action="/MANAK/OnlinePayment.do?strAppStatus=P&SchemeId=${SchemeId}&ActionId=${ActionId}";
		  	document.getElementById('payform').method = "GET";
		  	document.getElementById('payform').action="/MANAK/ApplicantHM";
		  	document.getElementById('payform').submit();
		  }
	  if(str == 'F')
		  {
		  	//document.getElementById('payform').action="/MANAK/OnlinePayment.do?strAppStatus=F&SchemeId=${SchemeId}&ActionId=${ActionId}";
		  	document.getElementById('payform').method = "GET";
		  	document.getElementById('payform').action="/MANAK/ApplicantHM";
		  	document.getElementById('payform').submit();
		  }
	 
  }
  
  </script>
	
	
	
<div class="container clearfix">

<div class="row">

<form:form cssClass="form-horizontal" method="post" id="payform" name="payform" action="" commandName="model1"> 
<div class="whole padded">
<%if(strAuthStatus.equals("0300") || strAuthStatus.equals("300"))
	{
	
	%>
	<c:if test="${flag==1 }">
	<fieldset>
	<div class="form-group col-md-12 bold">
		Payment has been successfully made.
	</div>
	
	
	<div class="form-group col-md-12">
		Transaction Details
	</div>
	<div class="form-group col-md-12">
		<div class="col-md-4 bold">Transaction Reference No. :</div>
		<div class="col-md-8">
			<c:if test="${strTxnReferenceNo!=null}">
			<c:out value="${strTxnReferenceNo }"></c:out>
			</c:if>
		</div>
	</div>
	<div class="form-group col-md-12">
		<div class="col-md-4 bold">Transaction Amount :</div>
		<div class="col-md-8"><c:if test="${strTxnAmount!=null}"><c:out value="${strTxnAmount }"></c:out></c:if></div>
	</div>
	<div class="form-group col-md-12">
		<div class="col-md-4 bold">Bank Id :</div>
		<div class="col-md-8"><c:if test="${strBankId!=null}"><c:out value="${strBankId }"></c:out></c:if></div>
	</div>
	<div class="form-group col-md-12">
		<div class="col-md-4 bold">Transaction Type :</div>
		<div class="col-md-8"><c:if test="${strTxnType!=null}"><c:out value="${strTxnType }"></c:out></c:if></div>
	</div>
	<div class="form-group col-md-12">
		<div class="col-md-4 bold">Currency Name :</div>
		<div class="col-md-8"><c:if test="${strCurrencyName!=null}"><c:out value="${strCurrencyName }"></c:out></c:if></div>
	</div>
	<div class="form-group col-md-12">
		<div class="col-md-4 bold">Transaction Date :</div>
		<div class="col-md-8"><c:if test="${strTxnDate!=null}"><c:out value="${strTxnDate }"></c:out></c:if></div>
	</div>
<!-- 	<div class="form-group col-md-12"> -->
<!-- 		<div class="col-md-4 bold">Receipt No. :</div> -->
<%-- 		<div class="col-md-8"><c:if test="${strReceiptNo!=null}"><c:out value="${strReceiptNo}"></c:out></c:if></div> --%>
<!-- 	</div> -->
	

</fieldset>
</c:if>
	<c:if test="${flag==0 }">
	<fieldset>
	<div class="form-group col-md-12 bold">
		we'll notify you when the payment confirmation is received
	</div>
	</fieldset>
	</c:if>
	<%
	}else if(strAuthStatus.equals("0002") || strAuthStatus.equals("2"))
{
	%>
	<div class="form-group col-md-12 bold">
		BillDesk is waiting for Response from Bank.
	</div>
	<% 
}else if(strAuthStatus.equals("0399")){%>
	<div class="form-group col-md-12 bold">
		Payment process has been cancelled. 
	</div>	
<%}else
	{%>
	<div class="form-group col-md-12 bold">
		There was error while payment. 
	</div>	
	<%}%>
	 	
</div>

<%if(strAuthStatus.equals("0300") || strAuthStatus.equals("300"))/*  || strAuthStatus.equals("0002") || strAuthStatus.equals("2") */
	{%>
	<div class="form-group col-md-4 text-center">
		<!-- <button value="OK" onclick="submittedApp('P')">Continue</button> -->
		<button  type="button" name="fail" onclick="submittedApp('P')" class="btn btn-primary" class="pull-right blue tooltip button-tooltip"><i class="fa fa-refresh pad-right half fa-1x" ></i>Continue...</button>
	</div>
	<c:if test="${flag==1 }">
<!-- 	<div class="form-group col-md-4 text-center">	 -->
<%-- 		<a href="/MANAK/applicantHMGrantedLicense"><c:out value="Download Receipt"></c:out></a> --%>
<!-- 	</div> -->
	
	<c:if test="${schemeid == 1 && actionid == 1}">
	<div class="form-group col-md-4 text-center">	
		<%-- <a target="_blank" href="/MANAK/genPCAppForm.do?appID=${eappid}&branchId=${ebid}" ><c:out value="View Application Form"></c:out> </a> --%>
		<a target="_blank" href="/MANAK/applicationDataToShowForAppView?eappId=${eappid}&ebranchId=${ebid}"><c:out value="View Application Form"></c:out></a>
	</div>
	</c:if>
	
	<c:if test="${schemeid == 2 && actionid == 1}">
	<div class="form-group col-md-4 text-center">	
		<a target="_blank" href="/MANAK/getJewelerApplicationDetailsLicence.do?EappId=${eappid}&EbranchId=${ebid}currStatus=201"  target="_blank" >View Application Form</a>
	</div>
	</c:if>
	</c:if>
	
	<%}else{%>
	<div class="form-group col-md-6 text-center">
		<!-- <button value="OK" onclick="submittedApp('F')">Continue</button> -->
		<button  type="button" name="pass" onclick="submittedApp('F')" class="btn btn-primary" class="pull-right blue tooltip button-tooltip"><i class="fa fa-refresh pad-right half fa-1x" ></i>Continue...</button>
	</div>
	<%}%>
	


</form:form>
 </div>
  </div>