<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page isELIgnored="false"%>
<%
	String applicationPath = "/MANAK";
%>
<!--
<link rel="stylesheet" type="text/css" 	href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/select2.css" />
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/select2.min.js"></script>

<link type="text/css" rel="stylesheet" href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/datepicker3.css">
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/bootstrap-datepicker.js"></script>

<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/footable.js"></script>
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/footable.sort.js"></script>
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/footable.filter.js"></script>
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/footable.paginate.js"></script>
<link rel="stylesheet" type="text/css" href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/footable.core.css"/>
-->
<link rel="stylesheet" type="text/css" href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/jquery-ui.css"/>
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/jquery-ui.js"></script>

<!--<link rel="stylesheet" type="text/css" href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/jquery.dataTables.css"/>-->
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/jquery.dataTables.min.js"></script>
<!--
<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.13/css/dataTables.jqueryui.min.css"/>
<script type="text/javascript" src="https://cdn.datatables.net/1.10.13/js/dataTables.jqueryui.min.js"></script>
-->
<link rel="stylesheet" type="text/css" href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/dataTables.bootstrap.css"/>
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/dataTables.bootstrap.min.js"></script>

<link rel="stylesheet" type="text/css" href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/responsive.bootstrap.min.css"/>
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/dataTables.responsive.min.js"></script>
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/responsive.bootstrap.min.js"></script>

<!--<link rel="stylesheet" type="text/css" href="<%=applicationPath%>/resources/app_srv/GlobalPages/CommonUtility/CSS/buttons.dataTables.min.css">-->
<script type="text/javascript" src="<%=applicationPath%>/resources/app_srv/GlobalPages/CommonUtility/JS/dataTables.buttons.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=applicationPath%>/resources/app_srv/GlobalPages/CommonUtility/CSS/buttons.jqueryui.min.css">
<script type="text/javascript" src="<%=applicationPath%>/resources/app_srv/GlobalPages/CommonUtility/JS/buttons.jqueryui.min.js"></script>
<!--<link rel="stylesheet" type="text/css" href="<%=applicationPath%>/resources/app_srv/GlobalPages/CommonUtility/CSS/buttons.bootstrap.min.css">
<script type="text/javascript" src="<%=applicationPath%>/resources/app_srv/GlobalPages/CommonUtility/JS/buttons.bootstrap.min.js"></script>-->

<script type="text/javascript" src="<%=applicationPath%>/resources/app_srv/GlobalPages/CommonUtility/JS/buttons.flash.min.js"></script>
<script type="text/javascript" src="<%=applicationPath%>/resources/app_srv/GlobalPages/CommonUtility/JS/buttons.html5.min.js"></script>
<script type="text/javascript" src="<%=applicationPath%>/resources/app_srv/GlobalPages/CommonUtility/JS/buttons.print.min.js"></script>
<!--<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jszip/3.1.3/jszip.min.js"></script>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.36/pdfmake.min.js"></script>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.36/vfs_fonts.js"></script>-->
<script type="text/javascript" src="<%=applicationPath%>/resources/app_srv/GlobalPages/CommonUtility/JS/jszip.min.js"></script>
<script type="text/javascript" src="<%=applicationPath%>/resources/app_srv/GlobalPages/CommonUtility/JS/pdfmake.min.js"></script>
<script type="text/javascript" src="<%=applicationPath%>/resources/app_srv/GlobalPages/CommonUtility/JS/vfs_fonts.js"></script>

<!--<link rel="stylesheet" type="text/css" href="<%=applicationPath%>/resources/app_srv/GlobalPages/CommonUtility/CSS/rowGroup.bootstrap.min.css"/>
<script type="text/javascript" src="<%=applicationPath%>/resources/app_srv/GlobalPages/CommonUtility/JS/dataTables.rowGroup.min.js"></script>
<script type="text/javascript" src="https://cdn.rawgit.com/ashl1/datatables-rowsgroup/fbd569b8768155c7a9a62568e66a64115887d7d0/dataTables.rowsGroup.js"></script>-->
<script type="text/javascript" src="<%=applicationPath%>/resources/app_srv/GlobalPages/CommonUtility/JS/dataTables.rowsGroup.js"></script>

<style>
            table.dataTable tbody td {
                vertical-align: middle;
                text-align:center;
            }
            table.dataTable th {
                vertical-align: middle;
                text-align:center;
            }
</style>
<style>
            div.paginationDiv{
                float: right;
            }
            div.paginationDiv ul{
                right: 0;
                border:0;
                margin:0;
                padding:0;
                list-style: none;
            }
            #paginationLinks li{
                border:0; margin:0; padding:0;

                list-style:none;
                display: inline;
            }
            #paginationLinks a{
                border:solid 1px #DDDDDD;
                margin-right:2px;
            }

</style>
<style>

            /* Center the loader */
            #loader {
                /* position: absolute; */
                position: relative;
                left: 60%;
                top: 50%;
                z-index: 1;
                width: 150px;
                height: 150px;
                margin: -75px 0 0 -75px;
                border: 8px solid #f3f3f3;
                border-radius: 50%;
                border-top: 8px solid #3498db;
                border-bottom: 8px solid palevioletred;
                width: 55px;
                height: 55px;
                -webkit-animation: spin 1s linear infinite;
                animation: spin 1s linear infinite;
            }
            /* Safari */
            @-webkit-keyframes spin {
                0% { -webkit-transform: rotate(0deg); }
                100% { -webkit-transform: rotate(360deg); }
            }

            @keyframes spin {
                0% { transform: rotate(0deg); }
                100% { transform: rotate(360deg); }
            }

            #modal-msg {
                /* background-color: #ffa73f;  Change */
                position: relative;

                float: left;
                top: 30%;
                left: 50%;
                transform: translate(-50%, -50%);
            }
        </style>     

        <style>

            /* Start by setting display:none to make this hidden.
   Then we position it in relation to the viewport window
   with position:fixed. Width, height, top and left speak
   for themselves. Background we set to 80% white with
   our animation centered, and no-repeating */
            .waitmodal {
                display:    none;
                position:   fixed;
                z-index:    1000;

                top:        50%;
                left:       35%;
                height:     20%;
                width:      30%;
                background: rgba( 255, 255, 255, .8 ) 
                    // url('<%=applicationPath%>/images/ajax-loader.gif')
                    // url('<%=applicationPath%>/images/widget-loader.gif')
                    50% 50% 
                    no-repeat;
                /**/
            }
            #modal-content {

                position: relative;
                float: left;
                top: 60%;
                left: 50%;
                transform: translate(-50%, -50%);
            }

            /* When the body has the loading class, we turn
               the scrollbar off with overflow:hidden */
            body.loading {
                overflow: hidden;   
            }

            /* Anytime the body has the loading class, our
               modal element will be visible */
            body.loading .waitmodal {
                display: block;
            }
            /* Gradientfor background 
            #grad1 {
                 background: linear-gradient(lightblue, white); /* Standard syntax (must be last) 
            
            } */ 
        </style>

<script type="text/javascript">
            /*
             $(document).ready(function(){
             });
             */
             $(document).ready(function (e) {
                 pageLoad();

                 getsearchresults();

             });
             
             $(document).ajaxStart(function () {
                 startloading();
             }).ajaxStop(function () {
                 stoploading();
             });

             function startloading() {
                 $body = $("body");
                 //$("#modal-msg").show();
                 $body.addClass("loading");
             }

             function stoploading() {
                 $body = $("body");
                 $body.removeClass("loading");
                 //$("#modal-msg").hide();
             }

             function formatNumber(num) {
                 var n1, n2;
                 num = parseFloat(num).toFixed(0) + '' || '';
                 // works for integer and floating as well
                 n1 = num.split('.');
                 n2 = n1[1] || null;
                 n1 = n1[0].replace(/(\d)(?=(\d\d)+\d$)/g, "$1,");
                 num = n2 ? n1 + '.' + n2 : n1;
                 return num;
             }
             
             function pageLoad() {
                 //$('#pcul').css('display', 'block');
                 //$('li').removeClass('active');
                 //$('#pcsli1').addClass('active');
                 //$('#pcsli1a').addClass('toggled');
                 //$('#pcahref').addClass('toggled');
                 //document.getElementById("chkschemes").checked = false;

                 //$("#sel_length").hide();
             }
             
             function getsearchresults() {

                         //var postData = $("#form1").serialize();
                         //alert(postData);

                         //$("#sel_length").show();
                         var formUrl = $("#form1").attr('action') + "?ajaxSource=true";
                         $.ajax({
                             url: formUrl,
                             dataType: 'html', // what to expect back from the controller
                             cache: false,
                             data: $("#form1").serialize(),
                             type: $("#form1").attr('method'),
                             success: function (response) {
                                 //alert(response);
                                 $('#frmresult').empty();
                                 $('#frmresult').html(response); // display success response from the controller
                                 $('#tblresult').DataTable({
                                     "dom": 'Bfrt',
                                     "buttons": [
                                                 {
                                                     extend: 'collection',
                                                     text: 'Export',
                                                     "buttons": ['csv', 'excel',
                                                         {
                                                             extend: 'pdf',
                                                             text: 'Save Visible Columns(PDF)',
                                                             exportOptions: {
                                                                 "columns": ':visible'
                                                             }
                                                         }
                                                     ]
                                                 },
                                                 'copy', 'print'],
                                     //"buttons": ['copy', 'csv', 'excel', 'pdf', 'print'],
                                     //"select": [{"style": 'multi',selector: 'td:first-child'}],
                                     //"select": 'multi',
                                     //retrieve: true,
                                     "responsive": true,
                                     //"autoWidth": false,
                                     //"rowsGroup": [2,1],
                                     //"rowGroup": {dataSrc: '2'},
                                     //"columnDefs": [{"className": 'select-checkbox', targets: 0}],
                                     "paging": false,
                                     "info": false
                                 });
                             },
                             error: function (e) {
                                 alert('Error: ' + e);
                             }
                         });
             }
             
             function getsearchpageresults(pagenum) {
                 //var postData = $("#form1").serialize();
                 //alert(postData);
                 //alert(pagenum);

                 var formUrl = $("#form1").attr('action') + "?ajaxSource=true&pageNo=" + pagenum;
                 $.ajax({
                     url: formUrl,
                     dataType: 'html', // what to expect back from the controller
                     cache: false,
                     data: $("#form1").serialize(),
                     type: $("#form1").attr('method'),
                     success: function (response) {
                         $('#frmresult').empty();
                         $('#frmresult').html(response); // display success response from the controller
                         $('#tblresult').DataTable({
                             "dom": 'Bfrt',
                             "buttons": [
                                         {
                                             extend: 'collection',
                                             text: 'Export',
                                             "buttons": ['csv', 'excel',
                                                 {
                                                     extend: 'pdf',
                                                     text: 'Save Visible Columns(PDF)',
                                                     exportOptions: {
                                                         "columns": ':visible'
                                                     }
                                                 }
                                             ]
                                         },
                                         'copy', 'print'],
                             //"buttons": ['copy', 'csv', 'excel', 'pdf', 'print'],
                             //"select": [{"style": 'multi',selector: 'td:first-child'}],
                             //"select": 'multi',
                             //retrieve: true,
                             "responsive": true,
                             //"autoWidth": false,
                             //"rowsGroup": [2,1],
                             //"rowGroup": {dataSrc: '2'},
                             //"columnDefs": [{"className": 'select-checkbox', targets: 0}],
                             "paging": false,
                             "info": false
                         });
                     },
                     error: function (e) {
                    	 //$('#frmresult').empty();
                         alert('Error: ' + e);
                     }
                 });
             }
             /*
             $(function () {
                 $('[data-toggle="tooltip"]').tooltip()
             });
             */
</script>
<!--<div class="container">-->
<div class="container-fluid">
	<!--<div class="h3 bold text-center">Fees Due</div>-->
	<div class="h3 bold text-center">
		<spring:message code="label.feeDueTitle"></spring:message>
	</div>
	<!-- Main row -->
	<div class="row" id="form-box">
		<form cssClass="form-horizontal" action="<%=applicationPath%>/pc/feesDue" method="post" class="form-horizontal" id="form1" name="frmSelect" role="form">
			<div class="col-md-12 tab-content pad-top" id="results-box">
				<!--<div id="sel_length" class="row form form-group form-inline">-->
				<div id="sel_length" class="col-md-12 pad-top double">
                     <div class="col-md-6">
                          <!--<div class="dataTables_length">-->
                          <span class="col-md-1"> Show </span>
                          <div class="text-left col-md-2">
                            <!--   <label>Show -->
                                    <select id="customizeshow" name="numshow" class="form-control input-sm" onchange="getsearchresults();">
                                        <option value="10" selected>10</option>
                                        <option value="25">25</option>
                                        <option value="50">50</option>
                                        <option value="100">100</option>
                                     </select>
                               <!--      entries
                               </label>-->
                           </div>
                               <span class="col-md-2"> entries&nbsp; </span>
                               <span class="col-md-2">
                               <a href="<%=applicationPath%>/pc/exportfeesDue?numshow=ALL&type=xls" target="_blank" id="btnDnAll" class="btn btn-info btn-sm" title=" Download all data ">
								<span class="pagetxt">Download all data</span>
							</a>
                               <br></br>
                               </span>
                           <!--</div>-->
                           
                      </div>
                      <!--<div class="col-md-5" id="btnstable">
                      
                      </div>-->
                                        
                </div>
				
				<div class="col-md-12 tab-content" id="frmresult">
					<!-- Table -->
				</div>

			</div>
		</form>
		<!--</form>-->

	</div>
	<!-- /.form-box -->
	<!-- /.row (main row) -->
	<div class="waitmodal">
		<!-- Loading -->
            <div id="loader" class="overlay"></div>
        <!-- end loading -->
	</div>
</div>