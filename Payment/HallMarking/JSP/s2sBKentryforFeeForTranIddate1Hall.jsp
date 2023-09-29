 <%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
 <%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
 <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
 
 <%@ page isELIgnored="false" %>

    
<link href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/footable.core.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/bootstrapValidator.css">
 <script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/bootstrapValidator.js"></script>
<link type="text/css" rel="stylesheet" href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/datepicker3.css">
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/bootstrap-datepicker.js"></script>
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/bootstrap.min.js"></script> 

 
<script>
	$(document)
			.ready(
					function() {

						$("#messageDiv").delay(4000).fadeOut(800);
						var url=window.location.href;
						var temp=url.split("://")
						var temp2=temp[1].split("/MANAK/");
						$('#hiddenurl').val(temp2[0]);
					});
	</script>

 <div class="container">
 
 <div id="messageDiv">  
		<c:if test="${fail==0}">
			<div class='alert alert-danger'>
				<c:out value="${mg}"></c:out>
			</div>
		</c:if>
	</div>
 		<div class="h3 bold text-center">
 		Backend entry through Transaction Id For HallMarking
        </div>
   	
 		<form:form cssClass="form-horizontal" method="post" id="payform1" name="payform1" action="" commandName="model1">
        <form:hidden path="" id="hiddenurl" name="hiddenurl"/>
 		<fieldset>
 		
 		<div class="col-md-12 form-group"> 		
 		<div class="col-md-2">Transaction No.<font color="red">*</font>:</div>
 		<div class="col-md-4">
 		<form:input path="" type="text" class="form-control" name="tid" id="tid" placeholder="Enter Transaction Number" required="true"/>
 		</div>
 		
 		 	 		
 		 		
				
 		<div class="col-md-2">Success Date<font color="red">*</font>:</div>
 		<div class="col-md-4">
 		<div class='input-group date ' id='datetimepicker1'>
		<form:input  path="" type='text' class="form-control" id="dt" name="dt" required="true"  />
		<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span> </span>
		</div>
 		</div>
 		</div>
 		
		<div class="col-md-12 form-group"> 		
 		<div class="col-md-2">Receipt No.(if required):</div>
 		<div class="col-md-4">
 		<form:input path="" type="text" class="form-control" name="rid" id="rid" placeholder="Enter Receipt Number" required="true"/>
 		</div>
 		</div>	 		
			 		
			 		
			 		
			 		
			 	<div class="form-group col-md-12 col-sm-12 text-center pad-top">			 		
			 		<input id="submit1" class="btn btn-primary" value="Submit" onclick="callwindow()"/>
			 		
				</div>
			 		
 		</fieldset>
 		<br>
 		 <fieldset>
		
		<div class="col-md-12 form-group "> 		
 		<div class="col-md-4 "><label>Update all Failed Transactions of HallMarking:</label></div>
 		<div class="col-md-4">
 		<a href="/MANAK/batchupdatefailedtroldHall"  class="btn btn-primary">Click Here</a>
 		</div>
 		</div>
 		 	</fieldset> 
 		 	
 		 	
 		 	
 		 	
 		<!--  <fieldset>
		
		<div class="col-md-12 form-group "> 		
 		<div class="col-md-4 "><label>Update wrong receipt 2019 Transactions of HallMarking :</label></div>
 		<div class="col-md-4">
 		<a href="/MANAK/wrongreceiptissueHall"  class="btn btn-primary">Click Here</a>
 		</div>
 		</div>
 		 	</fieldset>   -->	
	
 	</form:form>
 				
 		</div> 		  

<script>

function callwindow(){//alert("hi");
var msg=$('#tid').val();
var dt=$('#dt').val();
var rid=$('#rid').val();
//alert(rid);
alert( "transactionid="+msg+"     date ="+dt+"        receipt no="+rid);

 if(confirm("Want to update.")){
	document.payform1.method = "POST";
 	/* document.payform1.action = "/MANAK/s2sBKentryforFeeForTranIddate1?tid="+msg+"&dt="+dt+"&rid="+rid;  */
 	document.payform1.action = "/MANAK/s2sBKentryforFeeForTranIddate1Hall"; 
 	document.payform1.submit();
 	alert("Done..!!");
} 
}
</script>

<script>
	$(function() {
		$('#dob')
        .datepicker({
            format: 'dd-M-yyyy',
          
            	//startDate:'15-JUN-2017',
       		// endDate:'+0d', 
       		
       	/* 	 startDate: '-15d',
       	    endDate: '+0d',
       	 */	 
       		 "autoclose": true
          
            
            
        })
        	
	});
	
	  </script>

	  <script>
	$(function() {
		$('#dob1')
        .datepicker({
            format: 'dd-M-yyyy',
          
          //  	startDate:'15-JUN-2017',
       		// endDate:'+0d', 
       		
       	/* 	 startDate: '-15d',
       	    endDate: '+0d',
       	 */	 
       		 "autoclose": true
          
            
            
        })
        	
	});
	
	  </script>
	  
	  <!-- 
	  <script>
	  function dt(){
		  var frdate=$('#dob').val();
		  var todate=$('#dob1').val();
		  
		  alert(frdate+"   "+todate);
		  
		  if(confirm("Want to update.")){
			  document.payform1.method = "GET";
			 	document.payform1.action = "/MANAK/batchupdatefailedtr?updtall=0&frdate="+frdate+"&todate="+todate; 
			 	document.payform1.submit();
			 	alert("Done..!!");
			} 
	  }
	  
	  
	  </script> -->
	  
	  <script>
	$(function() {
		$('#datetimepicker1')
        .datepicker({
            format: 'dd/mm/yyyy',
          
            //	startDate:'15/06/2017',
       	/* 	 startDate: '-15d',
       	    endDate: '+0d',
       	 */	 
       		 "autoclose": true
          
        });
	});
	
	  </script>
	  