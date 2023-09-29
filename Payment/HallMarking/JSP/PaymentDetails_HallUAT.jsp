  <%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
 <%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
 <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
  <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
 
 <%@ page isELIgnored="false" %>
 
 	<script>
	function callwindow(){
	//alert(document.getElementById('paymentMessage').value);
	//window.open('https://pgi.billdesk.com/pgidsk/PGIMerchantPayment', 'PayGov');
	$('#pay').prop('disabled',true);
	document.getElementById('payform').submit();
	
	}
	</script>
 
<div class="container">
 	<div class="h3 bold text-center">Online Payment</div>
 		 <form:form cssClass="form-horizontal" method="post" id="payform" name="payform" action="https://uat.billdesk.com/pgidsk/PGIMerchantPayment" commandName="model1"> 
 
 		<fieldset>
 		<legend>Payment Details</legend>
 			<div class="form-group col-md-12">
 				<div class="col-md-2 bold">Firm Name : </div>
 				<div class="col-md-10"><c:out value="${FirmName }"></c:out></div>
 			</div>
 			<div class="form-group col-md-12">
 				<%-- <div class="col-md-2 bold">Transaction No : </div>
 				<div class="col-md-4"><c:out value="${trNumber }"></c:out></div> --%>
 			
 				<div class="col-md-2 bold">Transaction Amount : </div>
 				<div class="col-md-10"><fmt:formatNumber type="number" pattern="0.00" value="${paymentAmount}"/></div>
 			</div>
 				<input type="hidden" id="msg" name="msg" value="${msg}" />
 			<div class="form-group col-md-12 text-center pad-top">
		

<c:if test="${theCooperate != '1'}">		
		
	<div class="col-md-12">
		<div class="col-md-1"></div>
		<div class="col-md-11 form-group zero">
			<table id="feeInst" class="bg-head table table-bordered table-striped"  >
				<thead>
		    		<tr>
		    			<th data-hide="phone,tablet"><c:out value="Mode of Payment"></c:out></th>
		    			<th data-hide="phone,tablet"><c:out value="Transaction Processing Fee"></c:out></th>
		    		</tr>
				</thead>
				<tbody>
					<tr>
						<td>
							<label ><c:out value="Credit Card"></c:out></label>
						</td>
						<td class="text-left">
							<label ><c:out value="1.20% of the Payment Amount through Visa/Master Credit Card"></c:out></label>
							<label ><c:out value="2.75%+GST of the Payment Amount through American Express Card"></c:out></label>
						</td>
					</tr>
					<tr>
						<td>
							<label ><c:out value="Debit Card"></c:out></label>
						</td>
						<td class="text-left">
							<label ><c:out value="0.75% of the Payment Amount for transaction up to Rs 2000 through Visa/Master/Rupay Debit Card
								1% of the Payment Amount for transaction more than Rs 2000 through Visa/Master/Rupay Debit Card
								Service Taxes and other Govt Levies as applicable would be charged extra on the above"></c:out></label>
								<label ><c:out value="2.75%+GST of the Payment Amount through American Express Card"></c:out></label>
						</td>
					</tr>
					<tr>
						<td>
							<label ><c:out value="Netbanking"></c:out></label>
						</td>
						<td class="text-left">
							<label ><c:out value="Nil"></c:out></label>
						</td>
					</tr>	
				</tbody>
			</table>
		</div>
	</div>
		
	</c:if>	
		<form:button id="pay" class="btn btn-primary" onClick="callwindow()" >Pay Now</form:button></div>
 		</fieldset>
		
 		 </form:form> 
</div> 


