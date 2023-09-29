<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<link type="text/css" rel="stylesheet" 	href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/jquery-ui.css">
<link type="text/css" rel="stylesheet" href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/jquery.dataTables.css"> 
<link type="text/css" rel="stylesheet" href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/bootstrap.css">
<link type="text/css" rel="stylesheet" href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/dataTable/css/buttons.dataTables.min.css"> 
<link type="text/css" rel="stylesheet" 	href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/buttons.bootstrap.min.css">
<link type="text/css" rel="stylesheet" 	href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/buttons.bootstrap4.min.css">
<link type="text/css" rel="stylesheet" 	href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/buttons.foundation.min.css">
<link type="text/css" rel="stylesheet" 	href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/buttons.jqueryui.min.css">
<link type="text/css" rel="stylesheet" 	href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/buttons.semanticui.min.css">
<link type="text/css" rel="stylesheet" href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/datepicker3.css">
<link rel="stylesheet" type="text/css" href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/select2.css" />
<link type="text/css" rel="stylesheet" href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/dataTable/css/buttons.dataTables.min.css">
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/bootstrapValidator.js"></script>
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/select2.min.js"></script>
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/bootstrap-datepicker.js"></script>
<script type="text/javascript"	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/jquery.dataTables.min.js"></script>
<script type="text/javascript"	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/dataTables.buttons.min.js"></script>
<script type="text/javascript" 	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/jquery-ui.js"></script>
<script type="text/javascript" 	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/buttons.bootstrap4.min.js"></script>
<script type="text/javascript" 	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/buttons.bootstrap.min.js"></script>
<script type="text/javascript" 	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/buttons.colVis.min.js"></script>
<script type="text/javascript" 	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/buttons.flash.min.js"></script>
<script type="text/javascript" 	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/buttons.foundation.min.js"></script>
 <script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/buttons.print.min.js"></script>
<script type="text/javascript" 	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/buttons.html5.min.js"></script>
<script type="text/javascript" 	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/buttons.print.min.js"></script>
<script type="text/javascript" 	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/buttons.semanticui.min.js"></script>		
<script type="text/javascript"	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/jquery.dataTables.min.js"></script>
<script type="text/javascript"	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/dataTables.buttons.min.js"></script>
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/jszip.min.js"></script>
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/pdfmake.min.js"></script>
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/vfs_fonts.js"></script>
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/buttons.html5.min.js"></script>
<script type="text/javascript">
$(document).ready(function() { 
	var complete_flag="${flag}";
	if(complete_flag==1){
		alert("Data Save Successfully");
		window.location.href = "/MANAK/MobileMaintenance";
	}
	var t1=$("#apk_table").DataTable();
	 $('#apk_table  th[class="textBox"]').each( function () {	       
        $(this).html( '<div class="lighter"><input type="text" style="width:100%;color:black;" /></div>' );
      
  // DataTable
    var table = $('#apk_table').DataTable();
   
    // Apply the search
	     table.columns().eq( 0 ).each( function ( colIdx ) {
        $( 'input', $('.filters th')[colIdx] ).on( 'keyup change', function () {
            table
	                .column( colIdx )
	                .search( this.value )
	                .draw() ;
		
				        } );
				    } );
});
});
function UnderMaintenance(numid){
	  $.ajax({
			type : "POST",
			url : "/MANAK/UpdateMaintenanceStatus",
			data : 'numId='+numid,
			success : function(response) {
				window.location.href = "/MANAK/MobileMaintenance";
			},
			error : function() {
				alert('error');
			}
		});
}
function Live(numid){
	  $.ajax({
			type : "POST",
			url : "/MANAK/LiveMaintenanceStatus",
			data : 'numId='+numid,
			success : function(response) {
				window.location.href = "/MANAK/MobileMaintenance";
			},
			error : function() {
				alert('error');
			}
		});
}
function isNumberKey(evt)
{
  var charCode = (evt.which) ? evt.which : event.keyCode;
 console.log(charCode);
    if (charCode != 46 && charCode != 45 && charCode > 31
    && (charCode < 48 || charCode > 57))
     return false;

  return true;
}
function check_flag(){
	var flag=-1;
	var mobile_maintenance_url=$("#mobile_maintenance_url").val().trim();
	if(mobile_maintenance_url==""){
		alert("Enter URL");
		flag=0;
	}else{
		flag=1;
	}
	return flag;
	}
function isNumberChar(evt) {
    var charCode = (evt.which) ? evt.which : event.keyCode;
    	if ((charCode > 64 && charCode < 91) || (charCode > 96 && charCode < 123) || (charCode < 58 && charCode > 47) || charCode == 32)
        return true;
    return false;
}
function Submit(){
	var flag=check_flag();
	if(flag==1){
		document.frm.submit();
	}
}
	
</script>
<div class="container">
<form:form cssClass="form-horizontal" method="post" id="frm" name="frm" action="MobileMaintenanceSubmit" commandName="model" enctype="multipart/form-data">
<form:hidden path="csrfurl" name="csrfurl" id="csrfurl"/>
<form:hidden path="csrftoken" name="csrftoken" id="csrftoken"/>
<div class="col-md-12 text-center"><h1><strong>Mobile Maintenance Notice</strong></h1></div>
<div class="row">
<div class="col-md-12">
<div class="col-md-2">
URL:<span style="color:red">*</span>
</div>
<div class="col-md-4">
<form:input path="mobile_maintenance_url" id="mobile_maintenance_url" name="mobile_maintenance_url" type="text" class="form-control" onkeypress="return isNumberChar(event)" />
</div>
</div>
</div>
<div class="col-md-12">
<br>
</div>

<div class="col-md-12 text-center">
<input type="button" id="submit1" class="btn btn-primary" value="Submit" onclick="Submit()"> 
<span class="pad-left"><a class="btn btn-primary" href="MobileMaintenance"><i class="fa fa-refresh pad-right half fa-1x"></i>Reset</a></span>
</div>
</form:form>
</div>
<div class="container">
<div class="col-md-12">
<br>
</div>
</div>
<div class="container">
<table class="table bg-head table-bordered" id="apk_table" style="width:100%" >
<thead>
<tr>
<th class="text-center">S.No</th>
<th class="text-center">Maintenance URL</th>
<th class="text-center">Date</th>
<th class="text-center">Status</th>
<th class="text-center">Action</th>
</tr>
</thead>
<!-- 		<thead class="filters"> -->
<!-- 			<tr> -->
<!-- 				<th class=""></th> -->
<!-- 				<th class="textBox"></th> -->
<!-- 				<th class="textBox"></th> -->
<!-- 				<th class="textBox"></th> -->
<!-- 				<th class=""></th> -->
<!-- 			</tr> -->
<!-- 		</thead> -->

		<tbody >
		<c:forEach items="${MobileMaintenanceList}" var="list" varStatus="loop">
		<tr>
		<td class="text-center"><c:out value="${loop.index +1}"></c:out> </td>
		<td class="text-center">${list.mobile_maintenance_url } </td>
		<td class="text-center" ><fmt:formatDate value="${list.mobile_maintenance_date }"/> </td>
		<td class="text-center">
		<c:if test="${list.mobile_maintenance_flag==1 }"> Live</c:if>
		<c:if test="${list.mobile_maintenance_flag==0 }"> Under Maintenance </c:if>
		</td>
		<td class="text-center">
		<c:if test="${list.mobile_maintenance_flag==1 }"> <input type="button" id="submit2" class="btn btn-primary" value="UnderMaintenance" onclick="UnderMaintenance(${list.num_id})"></c:if>
		<c:if test="${list.mobile_maintenance_flag==0 }"> <input type="button" id="submit3" class="btn btn-primary" value="Live" onclick="Live(${list.num_id})"> </c:if>
		</td>
		</tr>
		</c:forEach>
		</tbody>
</table>
</div>