 <%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
 <%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
 <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
 <c:set var="context" value="${pageContext.request.contextPath}" />
 <%@ page isELIgnored="false" %>
 <link href="${context}/resources/app_srv/GlobalPages/CommonUtility/CSS/footable.core.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="${context}/resources/app_srv/GlobalPages/CommonUtility/CSS/bootstrapValidator.css">
 <script type="text/javascript" src="${context}/resources/app_srv/GlobalPages/CommonUtility/JS/bootstrapValidator.js"></script>
<link type="text/css" rel="stylesheet" href="${context}/resources/app_srv/GlobalPages/CommonUtility/CSS/datepicker3.css">
<script type="text/javascript" src="${context}/resources/app_srv/GlobalPages/CommonUtility/JS/bootstrap-datepicker.js"></script>
<script type="text/javascript" src="${context}/resources/app_srv/GlobalPages/CommonUtility/JS/bootstrap.min.js"></script> 

 <div class="container">
 <div class="h3 bold text-center">
 		Update Transactions
</div>
<div class="col-md-12">
<fieldset>
<legend>Transaction Details</legend>
<form:form cssClass="form-horizontal" id="form" name="form" commandName="model">
<form:hidden path="csrftoken" id="csrftoken" value="${model.csrftoken }"/>
<form:hidden path="csrfurl" id="csrfurl" value="${model.csrfurl }"/>
<div class="col-md-12 form-group"> 		
 		<div class="col-md-2">Transaction No.<font color="red">*</font>:</div>
 		<div class="col-md-4">
 		<form:input path="transaction_id" type="text" class="form-control" name="transaction_id" onkeypress="return isNumberKey(event);" id="transaction_id"  placeholder="Enter Transaction Number" required="true"/>
 		</div>
 		<div class="col-md-2">Success Date<font color="red">*</font>:</div>
 		<div class="col-md-4">
 		<div class='input-group date ' id='datetimepicker1'>
		<form:input  path="sccess_date" type='text' class="form-control" id="sccess_date" name="sccess_date" required="true" readonly="true" />
		<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span> </span>
		</div>
 		</div>
 		</div>
 		
		<div class="col-md-12 form-group"> 		
 		<div class="col-md-2">Receipt No.<font color="red">*</font>:</div>
 		<div class="col-md-4">
 		<form:input path="receipt_no" type="text" class="form-control" name="receipt_no" id="receipt_no" onkeypress="return isNumberChar(event);" placeholder="Enter Receipt Number" required="true"/>
 		</div>
 		</div>
 			<div class="form-group col-md-12 col-sm-12 text-center pad-top">
 			<input type="button" class="btn btn-primary" value="Submit" onclick="validate()">			 					 		
				</div>	
</form:form>
</fieldset>
</div>
 </div>
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
 $(function() {
		$('#datetimepicker1')
        .datepicker({
            format: 'dd/mm/yyyy',
       		 "autoclose": true,
       		endDate: "today"
          
        });
	});
 function validate(){
		var transaction_id=$("#transaction_id").val().trim();
		var sccess_date=$("#sccess_date").val().trim();
		var receipt_no=$("#receipt_no").val().trim();
		var flag1=-1;
		var flag2=-1;
		var flag3=-1;
		if(transaction_id!=null && transaction_id!=""){
			flag1=1;
		}else{
			flag1=0;
			alert("Enter Transaction ID");
		}
		if(sccess_date!=null && sccess_date!=""){
			flag2=1;
		}else{
			flag2=0;
			alert("Select Date");
		}
		if(receipt_no!=null && receipt_no!=""){
			flag3=1;
		}else{
			flag3=0;
			alert("Enter Receipt No");
		}
		if(flag1==1 && flag2==1 && flag3==1){
			document.forms[0].action="${context}/BackentryPCTransactionUpdateSubmit?transaction_id="+transaction_id+"&sccess_date="+sccess_date+"&receipt_no="+receipt_no;
			document.forms[0].method="post";			
			document.forms[0].submit();
// 				type : "POST",
// 				url : "/MANAK/BackentryPCTransactionUpdateSubmit",
// 				success : function(response) {
// 				},
// 				error : function() {
// 					alert('error');
// 				}
// 			});
		}
	}
	  </script>