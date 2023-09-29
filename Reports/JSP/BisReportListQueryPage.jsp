 <%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
 <%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
 <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
 <%@ page isELIgnored="false" %>

 <link type="text/css" rel="stylesheet"
	href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/jquery-ui.css">

<link type="text/css" rel="stylesheet"
	href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/jquery.dataTables.css">

<link type="text/css" rel="stylesheet"
	href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/buttons.bootstrap.min.css">
 <script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/buttons.print.min.js"></script>
<link type="text/css" rel="stylesheet"
	href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/buttons.bootstrap4.min.css">
<link type="text/css" rel="stylesheet"
	href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/buttons.dataTables.min.css">
<link type="text/css" rel="stylesheet"
	href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/buttons.foundation.min.css">
<link type="text/css" rel="stylesheet"
	href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/buttons.jqueryui.min.css">
<link type="text/css" rel="stylesheet"
	href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/buttons.semanticui.min.css">

<script type="text/javascript"
	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/jquery-ui.js"></script>

<script type="text/javascript"
	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/jquery.dataTables.min.js"></script>
<script type="text/javascript"
	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/dataTables.buttons.min.js"></script>
<script type="text/javascript"
	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/buttons.bootstrap4.min.js"></script>
<script type="text/javascript"
	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/buttons.bootstrap.min.js"></script>
<script type="text/javascript"
	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/buttons.colVis.min.js"></script>
<script type="text/javascript"
	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/buttons.flash.min.js"></script>
<script type="text/javascript"
	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/buttons.foundation.min.js"></script>
<script type="text/javascript"
	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/buttons.html5.min.js"></script>
<!-- <script type="text/javascript"
	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/buttons.jqueryui.min.js"></script> -->
<script type="text/javascript"
	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/buttons.print.min.js"></script>
<script type="text/javascript"
	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/buttons.semanticui.min.js"></script>
	<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/jszip.js"></script>
<!-- <script type="text/javascript" src="https://cdn.rawgit.com/bpampuch/pdfmake/0.1.18/build/pdfmake.min.js"></script>
<script type="text/javascript" src="https://cdn.rawgit.com/bpampuch/pdfmake/0.1.18/build/vfs_fonts.js"></script> -->
<!-- <script type="text/javascript" src="https://cdn.datatables.net/buttons/1.1.2/js/buttons.html5.min.js"></script>
<script type="text/javascript" src="https://cdn.datatables.net/buttons/1.1.2/js/buttons.print.min.js"></script> -->
<style>

.blinking{
    -webkit-animation: blink 1s infinite;
    -moz-animation: blink 1s infinite;    
    animation: blink 1s infinite;
}

@-webkit-keyframes blink{

    0%{ opacity:0;}
    100%{opacity:1;}
}

@-moz-keyframes blink{
    
    0%{ opacity:0;}
    100%{opacity:1;}

}
        
@keyframes blink{

    0%{ opacity:0;}
    100%{opacity:1;}

}

</style>	

<style type="text/css">
	/* Profile sidebar */
	.dropdown-keep-left {
  	right: 0; left: auto;
	}
	</style>
	
	<script>
		
			setTimeout(function(){
				$("#message").removeClass("alert alert-success");
				$("#message").html("");
			},7500);
</script>
<script>
  
 
	
	
	// Builds the HTML Table out of myList.
	function buildHtmlTable(selector,myList) {
	  var columns = addAllColumnHeaders(myList, selector);

	  for (var i = 0; i < myList.length; i++) {
	    var row$ = $('<tr/>');
	    for (var colIndex = 0; colIndex < columns.length; colIndex++) {
	      var cellValue = myList[i][columns[colIndex]];
	      if (cellValue == null) cellValue = "";
	      row$.append($('<td/>').html(cellValue));
	    }
	    $(selector).append(row$);
	  }
	}

	// Adds a header row to the table and returns the set of columns.
	// Need to do union of keys from all records as some records may not contain
	// all records.
	function addAllColumnHeaders(myList, selector) {
	  var columnSet = [];
	  var headerTr$ = $('<tr/>');

	  for (var i = 0; i < myList.length; i++) {
	    var rowHash = myList[i];
	    for (var key in rowHash) {
	      if ($.inArray(key, columnSet) == -1) {
	        columnSet.push(key);
	        headerTr$.append($('<th/>').html(key));
	      }
	    }
	  }
	  $(selector).append(headerTr$);

	  return columnSet;
	}
	
	function getUrlParameter(sParam) {
	    var sPageURL = window.location.search.substring(1),
	        sURLVariables = sPageURL.split('&'),
	        sParameterName,
	        i;

	    for (i = 0; i < sURLVariables.length; i++) {
	        sParameterName = sURLVariables[i].split('=');

	        if (sParameterName[0] === sParam) {
	            return sParameterName[1] === undefined ? true : decodeURIComponent(sParameterName[1]);
	        }
	    }
	}
	
	</script>

 
<!-- <script type="text/javascript">
$(function () {

    $('.footable').footable();

});
</script> -->
<script>
$('#myModal').on('shown.bs.modal', function () {
	  $('#myInput').focus();
	});

</script>

    <script>
    $('.dropdown-toggle').dropdown();
    </script>
    
   <script>
$('#myModal').on('shown.bs.modal', function () {
    $(this).find('.modal-dialog').css({width:'auto',
                               height:'auto', 
                              'max-height':'100%'});
});


</script>
   <!--  
    id="tablebody" -->
<body>
<form:form cssClass="form-horizontal" method="post" id="frm" name="frm" enctype="multipart/form-data" action="" commandName="">
<div class="container-fluid">
 <p id="showData"></p>
     <Br/><Br/>
     <div id="jsonValuesEmtpy" >
     <fieldset>
     <p class="text-center text-danger"><strong> ERROR,NO DATA TO SHOW !!! </strong></p>
     </fieldset>
     </div>
      <div class="col-md-12 tab-content">
      <c:choose>
      <c:when test="${not empty selBrnchName && selBrnchName!='Select Branch'}"> 
      <h2 class="text-center "><%=request.getAttribute("StageName")%><%= " ("+request.getAttribute("selBrnchName")+")"%></h2>
      </c:when>
      <c:otherwise>
      <h2 class="text-center "><%=request.getAttribute("StageName")%></h2>
      
      </c:otherwise>
      
      </c:choose>
   	<%-- <h2 class="text-center "><%=request.getAttribute("StageName")%>
   	<c:if test="${not empty selBrnchName}"> <%= "("+request.getAttribute("selBrnchName")+")"%></c:if></h2> --%>
      <table class="footable table bg-head table-bordered table-striped"  id="newtable"  data-page-size="10" data-filter-minimum="3" data-filter="#filter">
      <thead>
     
      <tr id="tablehead" >
                    
        </tr>
      </thead>
      	<tbody id="tablebody">
      
      	</tbody>
      </table>
<%--              <table class="footable table bg-head table-bordered table-striped" id="tableid" data-page-size="10" data-filter-minimum="3" data-filter="#filter">
                <thead>
                <tr>
                    <th data-toggle="true" data-sort-ignore="true">S.No.</th>
                    <th data-hide="phone,tablet">Application No</th>
                    <th data-hide="phone,tablet" >Firm Name</th>
                    <th data-hide="phone,tablet" >Branch Short Name</th>
                    <th data-hide="phone,tablet" >Registration Date</th>
                </tr>
                </thead>
                <tbody>
                <% int j=0; %>
               
                <%int i=0; %>
	              
	                 <c:forEach items="${BRLQL}" var="listCreitReportListQuery">

          <tr>
		                     <td><%i = i+1; %><%= i %><% j=i;%></td>
		                    <td><c:out value="${listCreitReportListQuery.strApplicationId}"></c:out> </td>
		                    <td><c:out value="${listCreitReportListQuery.strFirmName}"></c:out></a></td>
		                    <td><c:out value="${listCreitReportListQuery.strBrancShortName}"></c:out></a></td>
		                    <td><c:out value="${listCreitReportListQuery.dtRegistrationDate}"></c:out></a></td> 
		                    </tr>

	                </c:forEach>
                         
                </tbody>
            </table>  --%>
            <!-- <table id="excelDataTable" border="1">
  </table> -->
     </div>
  
</div>
</form:form>
</body>
<script>

$(document).ready(function()
		{
			//$('.select2-select').select2({ width: '100%' });
			
			//$('table').footable();
			
			$('.clear-filter').on('click',function(e){
				$('table').trigger('footable_clear_filter');	
			});
			$('.blink').addClass("blinking");
			
			var jsonvalues=${BRLQLjson};
			 if(jsonvalues.length==0||jsonvalues.length == null)
				 {
				  $("#jsonValuesEmtpy").show();
				 }
			 else
			{
				 $("#jsonValuesEmtpy").hide();
			generatereport();
			}

});
				
			
		

function generatereport(){		
	//var json = ${BRLQLjson};
	//var myList = json;
	var json = ${BRLQLjson};
	var b=json[0];
	var count = Object.keys(b).length;
	var c=[];
	c=Object.keys(b);
	//var d=Object.values(b);
	for(var i=0;i<count;i++){
	  //console.log(c[i]);
	  $("#tablehead").append("<th>"+c[i]+"</th>");
	}
	
	for(var k=0;k<json.length;k++){
		let jsonValue=json[k];
		let val=Object.values(jsonValue);
		var html1=''; 
		
		/* $("#tablebody").append("<tr>"); */
		for(var j=0;j<count;j++){
			
			html1 += '<td>'+val[j]+'</td>';
			/* $("#tablebody").append("<td>"+val[j]+"</td>"); */
			
		}
		$("#tablebody").append('<tr>'+html1+'</tr>');
	}
    $('#newtable').DataTable(
    		{
    		    "pageLength": 100,
    		    dom: 'Bfrtip',
    		    //"bPaginate": false,
    		   // buttons: ['excel']
    		 buttons: [{
                extend: 'excel',
                text: 'Export To Excel',
                className: 'exportExcel',
                filename: 'ReportExcel',
                exportOptions: { modifier: { page: 'all'} }
            }] 
    		});
//console.log(json);
//buildHtmlTable('#excelDataTable',myList);
}




</script>

