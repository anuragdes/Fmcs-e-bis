<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 <%@ page isELIgnored="false" %>
 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/select2.min.js"></script>
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
<!--  <script type="text/javascript"
	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/buttons.jqueryui.min.js"></script>  -->
<script type="text/javascript"
	src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/buttons.print.min.js"></script>
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/buttons.semanticui.min.js"></script>
 <script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/bootstrapValidator.js"></script> 		
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/jquery-ui.js"></script>
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/jquery.ui.datepicker.js"></script>
<link type="text/css" rel="stylesheet" href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/jquery.ui.datepicker.css">
<!--jszip script is required to export datatable into xlxs format  -->
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/jszip.js"></script>

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
<style>
#loadingimage {
width: 100%;
height: 100%;
top: 0px;
left: 0px;
position: fixed;
display: block;
opacity: 0.7;
background-color: white;
z-index: 99;
text-align: center;
}

#loading{
position: absolute;
top: 250px;
left: 550px;
z-index: 100;
}
</style>
<style type="text/css">
	/* Profile sidebar */
	.dropdown-keep-left {
  	right: 0; left: auto;
	}
	</style>
	
<%
String flag="";
if(request.getAttribute("flag") != "")
	{
		flag = request.getAttribute("flag").toString();
	}

String Reportname ="";
String filterFlag ="";
if(request.getAttribute("reportName") != null)
{
	Reportname = request.getAttribute("reportName").toString();
	filterFlag = request.getAttribute("filterFlag").toString();
	//System.out.println("filterflag is"+filterFlag);
}



%>


<script>
  
 
	$(document).ready(function()
			{
				$('.select2-select').select2({ width: '130%' });
				
				/* $('tableid').footable();
				
				$('.clear-filter').on('click',function(e){
					$('tableid').trigger('footable_clear_filter');	
				});
				$('.blink').addClass("blinking"); */
				 
				
			});
	</script>

 

<script type="text/javascript">
$(function () {

    $('.footable').footable();

});

function openreport(value,frm_date,to_date,loc_id,count,selectedBranchName){
	//window.location.href="CreitReportList3/"+value+"/"+frm_date+"/"+to_date;

	if(count==0)
		{
		  alert("Select a valid one,no data to show.");
		}
	else
	window.open("BisReportList/"+value+"/"+frm_date+"/"+to_date+"/"+loc_id+"/"+selectedBranchName);
	
}
</script>


<body onload="ShowHide()">
	<form:form cssClass="form-horizontal" method="post" id="frm" name="frm" enctype="multipart/form-data" action="" commandName="bisReportModel">
	<form:hidden path="reportName" class="form-control" name="reportname" id="reportname"  value="${reportName}"/>
	<form:hidden path="selectedBranchName" id="selectedBranchName" name="selectedBranchName"/>
	
<div id="loadingimage" > 
	  <div align="center" id="loading"><img align="middle"  src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/Images/loading.gif" />
	  <br>
                <div align="center">
                     <font color="Black" size="3" ><strong>Please Wait,It may take few minutes.<br>
                      Do not Close or Refresh this Window.</strong>
                      </font>
              </div>
              </div>
	  </div>
<div class="container-fluid pad-bottom pad-top">

		<fieldset id="filterDiv">
	      <div class="col-md-12 pad-bottom pad-top ">
	      <div class="col-md-3 form-group">
	      <div class="col-md-4 form-group"></div>
 						<div class="col-md-1"> </div>
 			 			<div class="col-md-7 form-group pad-left"> 
 				
 				
 				       	 	<select class="form-group col-md-7 zero select2-select" path="strReportType" name="strReportType" id="strReportType"  selectcheck="true" >
										<option value="0">Select Report Section</option>
										<option value="All">All</option>
 							 <c:forEach items="${ReportTypes}" var="rt">
 							<option value="${rt}">${rt}</option>
							</c:forEach>
							</select> 
						</div>
	      </div>
	      
	     <div class="col-md-3 form-group" id="filterofBranches">
	      	  <c:choose>
	      	  
	      	  
 				<c:when test="${RoleID =='4' || RoleID == '5' || RoleID == '62'}" >
 				<div class="col-md-2"></div>
 				<div class="col-md-10 form-group pull-right">
 				<Strong>Please Select From Date and To Date :</Strong>
 				</div>
							</c:when>
							
							<c:otherwise>
							<c:choose>
							<c:when test="${RoleID =='7'}">
							<div class="col-md-4 form-group">  </div>
 						<div class="col-md-1"> </div>
 			 			<div class="col-md-7 form-group pad-left"> 
 				
 				       	 	<select class="form-group col-md-7 zero select2-select" path="branchId" name="branchId" id="branchId"  selectcheck="true" selected="true">
										<option value="0">Select Branch</option>
										 <option value="1">ALL BRANCHES</option>
 							 <c:forEach items="${BranchList}" var="b">
 							<option value="${b.branch_id}">${b.branch}</option>
							</c:forEach>
							</select> 
						</div>
							</c:when>
							<c:otherwise>
							<div class="col-md-4 form-group"> </div>
 						<div class="col-md-1"> </div>
 			 			<div class="col-md-7 form-group pad-left"> 
 				
 				       	 	<select class="form-group col-md-7 zero select2-select" path="branchId" name="branchId" id="branchId"  selectcheck="true" selected="true">
										<option value="0" >Select Branch</option>
										 <option value="1" >ALL BRANCHES</option>
										 <option value="8" >CRO</option>
										 <option value="5" >ERO</option>
										 <option value="9" >NRO</option>
										 <option value="6" >SRO</option>
										  <option value="7" >WRO</option>
 							 <c:forEach items="${BranchList}" var="b">
 							<option value="${b.branch_id}" >${b.branch}</option>
							</c:forEach>
							</select> 
						</div>
							</c:otherwise>
							</c:choose>
          			  
         					</c:otherwise>
         </c:choose>
 				</div> 
 				
					 
					<!-- </div>  -->
					<div class="col-md-1"></div>
			<div class="col-md-2 form-group"  id="fromdtdiv">
			<div class='input-group date' id='datetimepicker1'>
						<form:input path="fromDt" type="text" class="form-control fromDate" 
							placeholder="From Date" name="fromdate1" id="fromdate1" readonly="true" />
							<span class="input-group-addon"><span
													class="glyphicon glyphicon-calendar"></span> </span>
													</div>							 
					</div>
					<div class="col-md-1" ></div>
					
					<div class="col-md-2 form-group"  id="todtdiv">
					<div class='input-group date' id='datetimepicker2'>
						<form:input path="toDt" type="text" class="form-control toDate" 
							placeholder="To Date" name="toDate1" id="toDate1" readonly="true" />
							<span class="input-group-addon"><span
													class="glyphicon glyphicon-calendar"></span> </span>		
													</div>					 
					</div>
					
					
 			</div>
 			
 			<div class="col-md-12 pad-bottom pad-top" align="center">
 			
					<button type="button" name="btn" id="btn" class="btn btn-primary" value="btn" >Submit</button>
					
 			</div>
 			</fieldset>
 			<fieldset id="datatablelist">
 			
 			<c:choose>
 			<c:when test="${RoleID =='4' || RoleID == '5' || RoleID == '62'}"><h3 class="text-center"><%=request.getAttribute("ReportType")+" "+Reportname.replaceAll("PC_","").replaceAll("_"," ")+" Reports "%></h3></c:when>
 			<c:otherwise><c:choose>
      <c:when test="${ filterFlag!='hide'}"> 
     <h3 class="text-center "><%=request.getAttribute("ReportType")+" "+Reportname.replaceAll("PC_","").replaceAll("_"," ")+" Reports of "+request.getAttribute("selBranchNme")%></h3>
      </c:when>
      <c:otherwise>
      <h3 class="text-center"><%=Reportname.replaceAll("PC_","").replaceAll("_"," ")+" Reports "%></h3>
      </c:otherwise>
      </c:choose> </c:otherwise>
 			
 			</c:choose>
 			
      <%-- <c:if test="${not empty selBranchNme}"><h3 class="text-center "><%=Reportname.replaceAll("PC_","").replaceAll("_"," ")+" Reports of "+request.getAttribute("selBranchNme")%></h3></c:if> --%>
      <div class="col-md-12 tab-content">
             <table class="footable table bg-head table-bordered table-striped" id="tableid" data-page-size="10" data-filter-minimum="3" data-filter="#filter">
                <thead>
                <tr>
                    <th data-toggle="true" data-sort-ignore="true">S.No.</th>
                    <th data-hide="phone,tablet">Stage Name</th>
                    <th data-hide="phone,tablet" >Count/Link</th>
                </tr>
                </thead>
                <tbody>
                <% int j=0; %>
               
                <%int i=0; %>
	                <%-- <c:forEach items="${Applications}" var="listApp"> --%>
	                 <c:forEach items="${BRML}" var="listBisReport">
		                 <tr>
		                     <td><%i = i+1; %><%= i %><% j=i;%></td>
		                    <td><c:out value="${listBisReport.strStage}"></c:out> </td>
		                    <td><a style="cursor: pointer;" id="<%=i%>" onclick=openreport("${listBisReport.encnum_Id}","${listBisReport.encfromDt}","${listBisReport.enctoDt}","${listBisReport.encnumlocationId}","${listBisReport.strCountofQuery}","${listBisReport.selectedBranchName}");><c:out value="${listBisReport.strCountofQuery}"></c:out></a></td>
		                </tr>
	                </c:forEach>
          	
              	
              	
                </tbody>
      
            </table>
            
     </div>
  </fieldset>
</div>
</form:form>
 </body>
<script>
	 $(".fromDate").datepicker({ 
	        dateFormat: 'dd/mm/yy', 
	        changeMonth: true, 
	        changeYear: true,
	        yearRange: "-100:+0",
	        maxDate: '0',
	     

	    }); 
	
		$('body').on('focus',".fromDate", function(){
		    $(this).datepicker({
		    	dateFormat: 'dd/mm/yy',
		    	maxDate: '0', 
		    	
		    	changeMonth: true, 
		        changeYear: true, 
		       
	       		"autoclose": true
		      });
		});
		
		
		
		$(".toDate").datepicker({ 
	        dateFormat: 'dd/mm/yy', 
	        changeMonth: true, 
	        changeYear: true,
	        yearRange: "-100:+0",
	        maxDate: '0',
	        

	    }); 
	
		$('body').on('focus',".toDate", function(){
		    $(this).datepicker({
		    	dateFormat: 'dd/mm/yy',
		    	maxDate: '0', 
		    	
		    	changeMonth: true, 
		        changeYear: true, 
		       
	       		"autoclose": true
		      });
		});
</script>



<script>
function ShowHide(){
	
	var filterFlag = "<%=filterFlag%>";
	
	if(filterFlag=="hide")
	{
	  HideDivofFilter();
	}
	else
		{
		  ShowDivofFilterNdata();
		}
    }
    
    
	function HideDivofFilter()
	{
		var flag= "<%=flag%>";
		
		$("#filterDiv").hide();
		 $("#datatablelist").show();
         $("#loadingimage").hide();
         
         if(flag!="valid")
        	 $("#btn").click();
		
	}
	
	function ShowDivofFilterNdata()
	{
		var flag= "<%=flag%>";
		if(flag=="valid")
	       {
		
	         $("#datatablelist").show();
	         $("#loadingimage").hide();
	        }
	      else
		   {
		     $("#datatablelist").hide();
		     $("#loadingimage").hide();
		   }
		
	}
	
</script>




<script type="text/javascript">

$("#btn").click(function() 
{
	
	var fromDate=$("#fromdate1").datepicker().val();
    var toDate=$("#toDate1").datepicker().val();
     
    var branchname=$("#branchId option:selected").text();
    
    var newfromDate=fromDate.split("/");
    var newtoDate=toDate.split("/");
   var dt1=new Date(newfromDate[2], newfromDate[1] - 1, newfromDate[0]);
   var dt2=new Date(newtoDate[2], newtoDate[1] - 1, newtoDate[0]);

	if(dt1>dt2)
		{
		  alert ("From Date is greater than To Date");
		}
	else
		{
	$("#loadingimage").show();
	var reportNm=$("#reportname").val();
	$("#selectedBranchName").val(branchname);
document.frm.method = "POST";
document.frm.action = "/MANAK/BisReportGet?reportName="+reportNm;
document.frm.submit(); 
		}
		});

</script>
<script>

      $('#tableid').DataTable({
         dom: 'Bfrtip',
          "bPaginate": false,  
         buttons: [{
             extend: 'excelHtml5',
             text: 'Export To Excel',
             className: 'exportExcel',
             filename: 'ReportExcel',
             exportOptions: { modifier: { page: 'all'} }
         }]
     });
		

</script>

<script>
//this makes datepicker year in reverse order 
	
	$(document.body).delegate('select.ui-datepicker-year', 'mousedown', function() {
		  (function(sel) {
		    var el = $(sel);
		    var ops = $(el).children().get();
		    if ( ops.length > 0 && $(ops).first().val() < $(ops).last().val() ) {
		      $(el).empty();
		      $(el).html(ops.reverse());
		    }
		  })(this);
		});

</script>