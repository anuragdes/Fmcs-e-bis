 <%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
 <%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
 <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
 <%@ page isELIgnored="false" %>
<div class="container">
<c:choose>
<c:when test="${strAuthStatus=='0300' || strAuthStatus=='300' }">
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
	<div class="form-group col-md-12">
		<div class="col-md-4 bold">Receipt No. :</div>
		<div class="col-md-8"><c:if test="${strReceiptNo!=null}"><c:out value="${strReceiptNo}"></c:out></c:if></div>
	</div>
	

</fieldset>
</c:when>
<c:when test="${strAuthStatus=='0002' || strAuthStatus=='2' }">
<div class="form-group col-md-12 bold">
		BillDesk is waiting for Response from Bank.
</div>
</c:when>
<c:when test="${strAuthStatus=='0399' }">
<div class="form-group col-md-12 bold">
		Payment process has been cancelled. 
</div>
</c:when>
<c:when test="${strAuthStatus=='0001' }">
<div class="form-group col-md-12 bold">
		Error at BillDesk 
</div>
</c:when>
<c:otherwise>
<div class="form-group col-md-12 bold">
		Invalid Input in the Request Message
</div>
</c:otherwise>
</c:choose>
</div>