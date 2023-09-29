 <%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
 <%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
 <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
  <%@ page isELIgnored="false" %>
 <c:set var="context" value="${pageContext.request.contextPath}" />
    
<link href="${context}/resources/app_srv/GlobalPages/CommonUtility/CSS/footable.core.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="${context}/resources/app_srv/GlobalPages/CommonUtility/CSS/bootstrapValidator.css">
 <script type="text/javascript" src="${context}/resources/app_srv/GlobalPages/CommonUtility/JS/bootstrapValidator.js"></script>
<link type="text/css" rel="stylesheet" href="${context}/resources/app_srv/GlobalPages/CommonUtility/CSS/datepicker3.css">
<script type="text/javascript" src="${context}/resources/app_srv/GlobalPages/CommonUtility/JS/bootstrap-datepicker.js"></script>
<script type="text/javascript" src="${context}/resources/app_srv/GlobalPages/CommonUtility/JS/bootstrap.min.js"></script> 
<script type="text/javascript">
function isNumberChar(evt) {
    var charCode = (evt.which) ? evt.which : event.keyCode;
    	if ((charCode > 64 && charCode < 91) || (charCode > 96 && charCode < 123) || (charCode < 58 && charCode > 47) || charCode == 32)
        return true;
    return false;
}
function isNumberKey(evt)
{
var charCode = (evt.which) ? evt.which : event.keyCode;
console.log(charCode);
 if ((charCode < 48 || charCode > 57))
  return false;

return true;
}
function call(){
	var transactionNo=$("#transactionNo").val().trim();
	var flag1=-1;
	if(transactionNo!=null && transactionNo!=""){
		flag1=1;
	}else{
		flag1=0;
		alert("Enter Transaction ID");
	}
	if(flag1==1){
		document.forms[0].action="${context}/ManualFeeReceiptGenerateCOC?transactionNo="+transactionNo;
		document.forms[0].method="post";			
		document.forms[0].submit();
	}
}
</script>
 <div class="container">
 		<div class="h3 bold text-center">
 		COC Fee Receipt Generator
        </div>
   	
 		<form:form cssClass="form-horizontal" method="post" id="form" name="form" commandName="model">
 		<fieldset>
 		<legend>Enter</legend>
 		<div class="col-md-12 form-group"> 		
 		<div class="col-md-2">Transaction No. :</div>
 		<div class="col-md-4">
 		<form:input path="transactionNo" type="text" class="form-control" name="transactionNo" id="transactionNo" placeholder="Enter Transaction Number" onkeypress="return isNumberKey(event);" required="true"/>
 		</div>
 		</div>
			 	<div class="form-group col-md-12 col-sm-12 text-center pad-top">			 		
			 		<input id="submit1" class="btn btn-primary" value="button" onclick="call()"/>
			 		
				</div>
			 		
 		</fieldset>
 		
		<br>	
 	</form:form>
 				
 		</div> 		  