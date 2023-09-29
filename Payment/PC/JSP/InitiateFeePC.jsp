<%@ page language="java" contentType="text/html; charset=utf-8"    pageEncoding="utf-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>
<link rel="stylesheet" type="text/css" href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/select2.css" />
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/select2.min.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	$('.select2-select').select2({width:'100%'});
});
function addrow(){
	var table = document.getElementById("pay_table");
	var totalRowCount = $("#pay_table tr").length;
	var newrowcount=totalRowCount;
	var feeType=$("#feeType"+(parseInt(newrowcount)-1)).val();
	var feeAmount=$("#feeAmount"+(parseInt(newrowcount)-1)).val().trim();
	var flag=check_flag(feeType,feeAmount);
	if(flag==1){
		var table_append = "";
		table_append += "<tr>";
		table_append += "<td><select name='feeType'  class='col-md-12 zero select2-select'  id='feeType"+ newrowcount + "' onchange='getFeeAmount(this.value,"+newrowcount+")' ><option selected='selected' value='0'>Select</option><c:forEach items='${feeTypeList}' var='tm'><option value='${tm.numFeeId}'> ${tm.strFeeDesc}</option> </c:forEach> </select></td>";
		table_append += "<td><input name='feeAmount' maxlength='800' type='text' id='feeAmount"+newrowcount+"' placeholder='Enter Fee Amount'  class='form-control' /></td>";
		table_append += "</tr>";
		$("#pay_table").append(table_append);
		$(".select2-select").select2();
	}
}
function deleteFeeIdPC(feeNumId){
	var eappid=$("#eappid").val();
	var ebranchid=$("#ebranchid").val();
	var ecml=$("#ecml").val();
	var result = confirm("Want to delete this fee?");	
	if (result) {
		 document.forms[0].method="POST";
	 	 document.forms[0].action="/MANAK/DeleteInitateFeePC?FeeId="+feeNumId+"&eappid="+eappid+"&ebranchid="+ebranchid+"&ecml="+ecml;
		 document.forms[0].submit(); 
		}
}
function getFeeAmount(feeid,count){
	$.ajax({
		url: "/MANAK/getFeeAmountPC?FeeId="+feeid,
		type : "POST",
		success: function(result){
	       if(result>0){
	    	   $("#feeAmount"+count).val(result);
	       }else{
	    	   $("#feeAmount"+count).val(result);
	    	   $("#feeAmount"+count).removeAttr("readonly");
	       }
	       
}})
}
function check_flag(feeType,feeAmount){
	var flag1=-1;
	var flag2=-1;
	var flag=-1;
	if(feeType=="0"){
		alert("Select Fee Type");
		flag1=0;
	}else{
		flag1=1;
	}
	if(feeAmount==""){
		alert("Enter Fee Amount");
		flag2=0;
	}else{
		flag2=1;
	}
	if(flag1==1 && flag2==1){
		flag=1;
	}else{
		flag=0;
	}
	return flag;
}
function checkPCreceipt(transactionNumber){
	$.ajax({
		type : "POST",
			url : "${context}/CheckPCReceipt",
			data: "transactionnumber="+transactionNumber,
			async: false,
			success : function(response) {
			if(response=="0"){
					alert("Payment Receipt Not found. Contact Bureau of Indian Standards");
				}
				else
					{
					window.open("${context}/DownloadPCReceipt?transactionnumber="+transactionNumber, "_blank");
					}
			}
			
	});
}

</script>
<div class="container">
<form:form cssClass="form-horizontal" method="post" id="frm" name="frm" action="InitiateFeePCSubmit" commandName="model">
<form:hidden path="eappid" id="eappid" name="eappid" value="${model.eappid}"/>
<form:hidden path="ebranchid" id="ebranchid" name="ebranchid" value="${model.ebranchid}"/>
<form:hidden path="ecml" id="ecml" name="ecml" value="${model.ecml}"/>
<form:hidden path="csrftoken" id="csrftoken" name="csrftoken" value="${model.csrftoken}"/>
<form:hidden path="csrftokenurl" id="csrftokenurl" name="csrftokenurl" value="${model.csrftokenurl}"/>
<form:hidden path="appid" id="appid" name="appid" value="${model.appid}"/>
<form:hidden path="branchid" id="branchid" name="branchid" value="${model.branchid}"/>
<form:hidden path="cml" id="cml" name="cml" value="${model.cml}"/>
<c:choose>
<c:when test="${ApplicationDetailPCList.size()>0 }">
<fieldset>
			<legend>Details</legend>
			<div class="col-md-12 form-group">
			<div class="col-md-3 bold">Application No.</div>
				<div class="col-md-3">
					<span> ${ApplicationDetailPCList.appid} </span>
				</div>
				<div class="col-md-3 bold">CML No.</div>
				<div class="col-md-3">
					<span> ${ApplicationDetailPCList.cml} </span>
				</div>
			</div>
			<div class="col-md-12 form-group">
			<div class="col-md-3 bold">Firm Name</div>
				<div class="col-md-3">
					<span> ${ApplicationDetailPCList.FirmName} </span>
				</div>
				<div class="col-md-3 bold">Firm Address</div>
				<div class="col-md-3">
					<span> ${ApplicationDetailPCList.FirmAddress} </span>
				</div>
			</div>
			<div class="col-md-12 form-group">
			<div class="col-md-3 bold">IS No.</div>
				<div class="col-md-3">
					<span> ${ApplicationDetailPCList.isnumber} </span>:<span> ${ApplicationDetailPCList.isyear} </span>
				</div>
				<div class="col-md-3 bold">Standard Title</div>
				<div class="col-md-3">
					<span> ${ApplicationDetailPCList.istitle} </span>
				</div>
			</div>
			<div class="col-md-12 form-group">
			<div class="col-md-3 bold">Email</div>
				<div class="col-md-3">
					<span> ${ApplicationDetailPCList.FirmEmail} </span>
				</div>
				<div class="col-md-3 bold">Contact No</div>
				<div class="col-md-3">
					<span> ${ApplicationDetailPCList.FirmContact} </span>
				</div>
			</div>
			</fieldset>
			<fieldset>
			<legend>Dues</legend>
			 <div class="col-md-12">
			 <div class="col-md-6">
			 <table class="footable table bg-head table-bordered table-striped" >
			 <thead>
						<tr>

							<th>Fee Type</th>
							<th>Amount</th>
						
							<th></th>

						</tr>
					</thead>
					<tbody id="pay_table">
					<tr>
					<td>
					<form:select class="col-md-12 zero select2-select" 
						name="feeType" id="feeType0" multiple="false"
						path="feeType" selectcheck="true" onchange="getFeeAmount(this.value,0)">
						<option value="0">Select Fee</option>
						<c:forEach items="${feeTypeList}" var="c">
						
							<option value='${c.numFeeId}'>${c.strFeeDesc}</option>
							
						</c:forEach>
					</form:select>
					</td>
					<td>
					<form:input path="feeAmount" id="feeAmount0" class="form-control"
						name="feeAmount" />
					</td>
					<td>
					<span class="glyphicon glyphicon-plus btn btn-default" onclick="addrow()"
								id="addMore"></span>
					</td>
					</tr>
					</tbody>
			 </table>
			 <div class="text-center">
   		<input type="submit" name="Save" class="btn btn-primary" id="Save" value="Add Fee"/> 
		</div>
			 </div>
			 <div class="col-md-6">
  	    <div class="col-md-12 tab-content">
  	    <c:if test="${PreviousDuesDetailPClist.size()>0}">
             <table class="footable table bg-head table-bordered table-striped" data-page-size="5">
                <thead>
                <tr>
                    <th>
                    S.No.                        
                    </th>
                    <th>
                       Fee Type
                    </th>
                    <th>
                       Fee Due
                    </th>
                  <th>Delete</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${PreviousDuesDetailPClist}" var="listApp" varStatus="loop">
                <tr>
                <td>${loop.index+1 }</td>
                <td>
                <c:out value="${listApp.str_fee_desc}"></c:out>
                </td>
                <td>
                <fmt:formatNumber type="number" pattern="0.00" value="${listApp.num_Amount}"/>
                </td>
                <td>
                <button type="button" name="deleteFee" id="deleteFee${loop.index}" class="btn btn-danger" onclick="deleteFeeIdPC('${listApp.num_Id}')" ><i class="glyphicon glyphicon-remove"></i></button>
                </td>
                </tr>
                </c:forEach>
               </tbody>
               </table>
               </c:if>
                </div>
			 </div>
			 </div>
			</fieldset>
			 <fieldset>
 <legend>Fee Paid
 </legend>
 <%int i=1;%>
 <%int y=1;%>
		<fieldset>    
 <div class="col-md-12">
<c:forEach items="${paidTransList}" var="l">
<c:if test="${l.TransactionNumber != null}">
     <div class="panel panel-default">
    
      <div class="panel-heading bold hover">
          
            <a data-toggle="collapse" data-parent="#accordian" href="#collapse<%=i%>">
            <table>            
            <tbody>
          <tr > 
          <td> 	 <label><font color="white">   Payment Date -   <c:out value="${l.PaymentDate}"></c:out>,</font>  </label></td>       
          <td>	<label>	<font color="white">    Transaction ID -  <c:out value="${l.TransactionNumber}"></c:out>, &nbsp;</font> </label></td></font>
          <td>	<label>	<font color="white">    Receipt Number - <a target='_blank' onclick="checkPCreceipt(${l.TransactionNumber})" href="#"> <c:out value="${l.Receipt}"></a></c:out></font></label></td></font>
          </tr>
          
          <tr>
          <%-- <td>		   Receipt No. -     <c:out value="${l.receipt}"></c:out> , </td> --%>
          <td>	<label>	<font color="white">    Total -          <fmt:formatNumber type="number" pattern="0.00" value="${l.TotalAmmount}"/></font></label></td>
          </tr>
          </tbody>
           </table>
        		   
          </a>
          
      </div>       
      <div id="collapse<%=i%>" class="panel-collapse collapse">
      <div class="panel-body">
         <fieldset>		
                   <table  class="footable table bg-head table-bordered table-striped"	data-page-size="5" > 
                   <thead>
                   <tr>
                   <td align="left" width="60%">Fee Type</td>
                   <td align="center" width="40%">Amount</td>
                   <!-- <td align="right">Payment Date</td> -->
                   </tr>
                   </thead>
                   <tbody>  
                            
				<c:forEach items="${l.list}" var="list1">
				<tr>
<%-- 				<c:if test="${list1.feetype=='Rs.'}"> --%>
				
<%-- 				<td><c:out value="${list1.strFeeDesc}"></c:out></td> --%>
<%-- 				<td><c:out value="${list1.num_Amount}"></c:out></td> --%>
				
<%-- 				</c:if>				 --%>
							
<%-- 				<c:if test="${list1.feetype=='%'}"> --%>
				
<%-- 				<td><c:out value="${list1.strFeeDesc}"></c:out></td> --%>
<%-- 				<td><c:out value="${list1.num_Amount}"></c:out></td> --%>
				
<%-- 				</c:if> --%>
				<td><c:out value="${list1[4]}"></c:out></td>
				<td><c:out value="${list1[1]}"></c:out></td>	
				</tr>
				</c:forEach>
				
				</tbody>			
                </table>
                
                <table>
                <tr>
<%--                 <td><a target="_blank" href="/MANAK/viewPreviousFeeReceipt?docName=FeeReceipt_${l.TransactionNumber}.pdf"><c:out value="Download Receipt"></c:out></a></td>  --%>
               <%-- <td> <button class='glyphicon glyphicon-download btn btn-default required'  onclick='popup_to_upload(${l.transaction_number})' type='button' id='upld5' name='upld5'  ></button></td> --%>
                </tr>
                </table>

        </fieldset>
    </div>       
    </div>
    </div> 
   
    <%
						i++;
    %>
	<%y++; %>
	</c:if>
</c:forEach>
</div>
</fieldset>
 
</fieldset>
</c:when>
<c:otherwise>
<div class="col-md-12 text-center">
License Details not found
</div>
</c:otherwise>
</c:choose>
</form:form>
</div>

