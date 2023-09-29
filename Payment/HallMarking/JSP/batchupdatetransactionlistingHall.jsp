
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ page isELIgnored="false"%>
<link type="text/css" rel="stylesheet"
	href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/jquery.dataTables.css">
<script type="text/javascript"
	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/jquery.dataTables.min.js"></script>

<script
	src="/MANAK/resources/app_srv/Scheme/ProductCertification/ApplicationSubmission/JS/Datatable_pc.js"
	type="text/javascript"></script>


<script>
function callwindow(id){
var msg=id;

 if(confirm("Want to update transaction ID = "+ msg+ " ")){
	document.payform1.method = "POST";
 	document.payform1.action = "/MANAK/s2sBKentryforFeeForBis1?tid="+msg; 
 	document.payform1.submit();
 	alert("Done..!!");
} 
}
</script>
<script>
function query(id){
	//alert(id);
	
	 window.open("/MANAK/queryAPIWithServlet?trno="+id+"&type=normal", "_blank", "toolbar=yes,top=200,left=200,width=600,height=500");
	
		
}
</script>
<script>
function querycorp(trno){
    var url="/MANAK/queryAPIWithServlet?trno="+trno;
    var myWindow =window.open(url,'Query API','child', 'scrollbars,width=200,height=80,top=200,left=200');
    myWindow.resizeTo(600, 500); 
    myWindow.moveTo(100, 200);
     myWindow.focus();
}
</script>


<div class="container-fluid">
	<div class="col-md-12 table-responsive">
		
		<div class="h3 bold text-center"> QueryAPI Transactions Status Detail of HallMarking</div>
		<table id="tab_logic" class="table table-bordered">
			<thead class="filters">
				<tr style="background-color: #338ec9; color: #ffffff;">
					<th>S No.</th>
					<th>Trans. No.</th>
					<th data-hide="phone,tablet">Application No.</th>
					<th data-hide="phone,tablet">Branch No.</th>
					<th data-toggle="true">Amount</th>
					<th data-hide="phone,tablet" data-sort-ignore="true">Date</th>
					<th style="width: 30%">Response get from Billdesk</th>
					<th data-hide="phone,tablet">Payment Status</th>					
					<th>Pay</th>
					<th>Query</th>

				</tr>
			</thead>
			<tbody>
				<c:if test="${paylist != null }">
					<%
						int i = 1;
					%>
					<c:forEach items="${paylist}" var="listApp">
						<tr>
							<td><c:out value="<%=i%>"></c:out>
							</td>
							<td><c:out value="${listApp.tsno}"></c:out>
							</td>
							<td><c:out value="${listApp.application_id}"></c:out>
							</td>
							<td><c:out value="${listApp.branch_id}"></c:out>
							</td>
							<td><c:out value="${listApp.trans_amount}"></c:out>
							</td>
							<td><c:out value="${listApp.trans_date}"></c:out>
							</td>
							<td><c:out value="${listApp.payment_for} (${listApp.qry_api_response})"></c:out>
							</td>
							<td><c:out value="${listApp.status_desc}"></c:out>
							
							<td><%-- <input type='button' id='submit1' class='btn btn-primary' value='Pay Fee' onclick="callwindow(${listApp.tsno})"/> --%></td>
							<td>
							<c:if test="${listApp.paythrough=='0'}">
							 	<input type='button' id='query' class='btn btn-primary' value='Retail Query' onclick="query(${listApp.tsno})"/>
							 </c:if>
							 <c:if test="${listApp.paythrough=='1'}">
								 <input type='button' id='query' class='btn btn-primary' value='Corporate Query' onclick="querycorp(${listApp.tsno})"/>
							 </c:if>
							 
							</td>
							<% i++; %>
						</tr>
					</c:forEach>
				</c:if>
			</tbody>

		</table>

	</div>



</div>

