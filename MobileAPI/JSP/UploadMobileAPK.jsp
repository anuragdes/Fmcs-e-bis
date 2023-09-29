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
		window.location.href = "/MANAK/UploadMobileAPK";
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
function isNumberKey(evt)
{
  var charCode = (evt.which) ? evt.which : event.keyCode;
 console.log(charCode);
    if (charCode != 46 && charCode != 45 && charCode > 31
    && (charCode < 48 || charCode > 57))
     return false;

  return true;
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
function check_flag(){
	var flag=-1;
	var flag1=-1;
	var flag2=-1;
	var flag3=-1;
	var apk_name=$("#apk_name").val().trim();
	var apk_version=$("#apk_version").val().trim();
	var apk_file=$("#apk_file").val().trim();
	if(apk_name!=""){
		flag1=1
	}else{
		alert("Enter APK Name");
		flag1=0;
	}
	if(apk_version!=""){
		flag2=1
	}else{
		alert("Enter APK Version");
		flag2=0;
	}
	if(apk_file!=""){
		flag3=1
	}else{
		alert("Enter APK File");
		flag3=0;
	}
	if(flag1==1 && flag2==1 && flag3==1){
		flag=1;
	}else{
		flag=0;
	}
	return flag;
}
function loadfile(checksum){		
	
	document.forms[0].method = "post";
	document.forms[0].action = "/MANAK/DownloadMobileAPK?checksum="+checksum;
	document.forms[0].submit();			
}	
</script>
<div class="container">
<form:form cssClass="form-horizontal" method="post" id="frm" name="frm" action="UploadMobileAPKSubmit" commandName="model" enctype="multipart/form-data">
<form:hidden path="stCSRFToken" name="stCSRFToken" id="stCSRFToken"/>
<form:hidden path="url" name="url" id="url"/>
<div class="col-md-12 text-center"><h1><strong>UPLOAD MOBILE APK</strong></h1></div>
<div class="row">
<div class="col-md-12">
<div class="col-md-2">
APK Name:<span style="color:red">*</span>
</div>
<div class="col-md-4">
<form:input path="apk_name" id="apk_name" name="apk_name" type="text" class="form-control" onkeypress="return isNumberChar(event)" />
</div>
<div class="col-md-2">
APK Version:<span style="color:red">*</span>
</div>
<div class="col-md-4">
<form:input path="apk_version" id="apk_version" name="apk_version" type="text" class="form-control" onkeypress="return isNumberKey(event)"/>
</div>
</div>
</div>
<div class="col-md-12">
<br>
</div>
<div class="row">
<div class="col-md-12">
<div class="col-md-2">
Upload APK:<span style="color:red">*</span>
</div>
<div class="col-md-4">
<form:input path="apk_file" id="apk_file" name="apk_file" type="file" accept=".apk" />
</div>

</div>
</div>
<div class="col-md-12 text-center">
<input type="button" id="submit1" class="btn btn-primary" value="Submit" onclick="Submit()"> 
<span class="pad-left"><a class="btn btn-primary" href="UploadMobileAPK"><i class="fa fa-refresh pad-right half fa-1x"></i>Reset</a></span>
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
<th class="text-center">APK Name</th>
<th class="text-center">Upload Date</th>
<th class="text-center">Download</th>
</tr>
</thead>
		<thead class="filters">
			<tr>
				<th class="textBox"></th>
				<th class="textBox"></th>
				<th class="textBox"></th>
				<th class=""></th>
			</tr>
		</thead>

		<tbody >
		<c:forEach items="${apklist}" var="list" varStatus="loop">
		<tr>
		<td><c:out value="${loop.index +1}"></c:out> </td>
		<td><c:out value="${list.apk_name}"></c:out> Version :- <c:out value="${list.apk_version}"></c:out></td>
		<td><fmt:formatDate value="${list.dt_entry_date }"/> </td>
		<td><a href="javascript:loadfile('${list.checksum}')" > Download </a>
		</td>
		</tr>
		</c:forEach>
		</tbody>
</table>
</div>