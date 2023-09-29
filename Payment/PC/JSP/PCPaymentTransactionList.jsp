<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="context" value="${pageContext.request.contextPath}" />
<link rel="stylesheet" href="${context}/static/jquery-ui-1.13.1/jquery-ui.css">
<script src="${context}/static/jquery.3.6.0/jquery-3.6.0.min.js"></script>
<script src="${context}/static/jquery-ui-1.13.1/jquery-ui.js"></script>
<link rel="stylesheet" href="${context}/static/select2-4.0.13/css/select2.css">
<script src="${context}/static/select2-4.0.13/js/select2.js"></script>
<link rel="stylesheet" href="${context}/static/DataTables/datatables.css">
<script src="${context}/static/DataTables/datatables.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	$("#successfulTransactionListTable").DataTable();
	$("#pendingTransactionListTable").DataTable();
});
function querycorp(trno){
    var url="/MANAK/queryAPIWithServlet?trno="+trno;
    var myWindow =window.open(url,'Query API','child', 'scrollbars,width=200,height=80,top=200,left=200');
    myWindow.resizeTo(600, 500); 
    myWindow.moveTo(100, 200);
     myWindow.focus();
}
function query(trno){
    var url="/MANAK/queryAPIWithServlet?trno="+trno+"&type=normal";
    var myWindow =window.open(url,'Query API','child', 'scrollbars,width=200,height=80,top=200,left=200');
    myWindow.resizeTo(600, 500); 
    myWindow.moveTo(100, 200);
     myWindow.focus();
}
function checkPCreceipt(etransactionNumber){
	$.ajax({
		type : "POST",
			url : "${context}/CheckPCReceipt",
			data: "enctransactionnumber="+etransactionNumber,
			async: false,
			success : function(response) {
			if(response=="0"){
					alert("Payment Receipt Not found. Contact Bureau of Indian Standards");
				}
				else
					{
					window.open("${context}/DownloadPCReceipt?enctransactionnumber="+etransactionNumber, "_blank");
					}
			}
			
	});
}
</script>
<div class="container-fluid">
<div class="h3 bold text-center">Successful Transaction List</div>
<table id="successfulTransactionListTable"  class="table bg-head table-bordered ">
<thead>
<tr>
<th>S.No.</th>
<th>Application/License No.</th>
<th>Transaction No.</th>
<th>Receipt No.</th>
<th>Transaction Date</th>
<th>Action</th>
</tr>
</thead>
<tbody>
<c:forEach items="${successfulTransactionList}" var="list" varStatus="loop">
<tr>
<td>${loop.index+1 }</td>
<td>CMA-${list.application_id }
<c:if test="${list.cmlno !='NA'}">
<br>CML-${list.cmlno }
</c:if>
</td>
<td>${list.transaction_number }</td>
<td>${list.receipt_no }</td>
<td>${list.tr_date }</td>
<td>
<button type="button" class="btn btn-primary"  onclick="checkPCreceipt('${list.etransaction}','${list.eappid}','${list.ebranchid}')"><i class="fas fa-receipt"></i>Download Payment Receipt</button>
</td>
</tr>
</c:forEach>
</tbody>
</table>
<div class="h3 bold text-center">Pending Transaction List of last 45 days</div>
<table id="pendingTransactionListTable"  class="table bg-head table-bordered ">
<thead>
<tr>
<th>S.No.</th>
<th>Application/License No.</th>
<th>Transaction No.</th>
<th>Transaction Date</th>
<th>Action</th>
</tr>
</thead>
<tbody>
<c:forEach items="${pendingTransactionList}" var="list" varStatus="loop">
<tr>
<td>${loop.index+1 }</td>
<td>CMA-${list.application_id }
<c:if test="${list.cmlno !='NA'}">
<br>CML-${list.cmlno }
</c:if>
</td>
<td>${list.transaction_number }</td>
<td>${list.tr_date }</td>
<td>
<c:choose>
<c:when test="${list.flag=='C' }">
<button type="button" class="btn btn-primary"  onclick="querycorp('${list.transaction_number}')"><i class="fas fa-receipt"></i>Check Payment Status</button>
</c:when>
<c:otherwise>
<button type="button" class="btn btn-primary"  onclick="query('${list.transaction_number}')"><i class="fas fa-receipt"></i>Check Payment Status</button>
</c:otherwise>
</c:choose>

</td>
</tr>
</c:forEach>
</tbody>
</table>
</div>
