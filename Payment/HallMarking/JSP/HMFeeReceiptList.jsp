 <%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
 <%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
 <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
 <%@ page isELIgnored="false" %>
<link rel="stylesheet" href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/bootstrapValidator.css">
 <script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/bootstrapValidator.js"></script>
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/bootstrap-datepicker.js"></script>
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/bootstrap.min.js"></script> 
<link type="text/css" rel="stylesheet"	href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/jquery.dataTables.css"> 
<script type="text/javascript"	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/jquery.dataTables.min.js"></script> 
<script type="text/javascript"	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/jquery-ui.js"></script>
<script type="text/javascript">
$(document).ready(function()
		{
	$('#fee_receipt_table').DataTable({
		"order": [[ 1, "desc" ]]
	});
		});
		function downloadreceipt(transcationno){
  var applicationId=$('#appid').val(); 
  var branchId=$('#branchid').val();
 
			$.ajax({
	 			type: "POST",
	 			url: "DownloadHMFeeReceipt",
	 			data: {"transcationno" : transcationno,"applicationId" : applicationId,"branchId" : branchId },
	 			success: function(response){
	 				///alert(response);
	 				if(response==''){
	 					alert("Receipt not found");
	 				}else{
	 					document.forms[0].method = "post";
	 					document.forms[0].action = "/MANAK/DownloadHMFeeReceipt?transcationno="+transcationno+"&branchId="+branchId+"&applicationId="+applicationId;
	 					document.forms[0].submit(); 
	 				}
	 			},
	 			error: function(e){
	 				/* alert("Error Occured"); */
	 			}
	 		});
		}
</script>
 <div class="container-fluid">
 
 		<div class="h3 bold text-center">
 		Fee Receipts
        </div>
        <form:form>
        
        
        </form:form>
        <input type="hidden"  id="appid" value="${appid}">
        <input type="hidden"  id="branchid" value="${branchid}">
   	<div class="col-md-12">
   	<table id="fee_receipt_table" class="footable table bg-head table-bordered table-striped">
   	<thead>
   	<tr>
   	<th>Date</th>
   	<th>Transaction No</th>
   	<th>Receipt No</th>
   	<th>Action</th>
   	</tr>
   	</thead>
   	<tbody>
   	<c:if test="${feereceiptlist!=null }">
   	<c:forEach items="${feereceiptlist}" var="fee_list">
   	<tr>
   	<td>
   	<c:out value="${fee_list.tr_date }"></c:out>
   	</td>
   	<td><c:out value="${fee_list.transaction_number }"></c:out></td>
   	<td><c:out value="${fee_list.receipt_no }"></c:out></td>
   	<td><span style="color:blue;cursor:pointer" onclick="downloadreceipt(${fee_list.transaction_number})">Download Receipt</span></td>
   	</tr>
   	</c:forEach>
   	</c:if>
   	</tbody>
   	</table>
   	</div>
</div> 		