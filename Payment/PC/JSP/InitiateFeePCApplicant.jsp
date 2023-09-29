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

function checkSelected(){
	var allVals = [];
	var count=0;
    $(':checkbox:checked').each(function () {
    	count=count+1;
    	allVals.push($(this).val());
    });
    if(allVals=="")
    {
    	alert("Select checkbox");
    }else{
    	if(count<4){
    	$("#SelectFeeNumId").val(allVals);
    	document.frm.submit();
    	}else{
    		alert("You can not tick more than 3 checkbox at a time");
    	}
    }
}

</script>
<div class="container">
<form:form cssClass="form-horizontal" method="post" id="frm" name="frm" action="InitiateFeePCApplicantSubmit" commandName="model">
<form:hidden path="eappid" id="eappid" name="eappid" value="${model.eappid}"/>
<form:hidden path="ebranchid" id="ebranchid" name="ebranchid" value="${model.ebranchid}"/>
<form:hidden path="ecml" id="ecml" name="ecml" value="${model.ecml}"/>
<form:hidden path="csrftoken" id="csrftoken" name="csrftoken" value="${model.csrftoken}"/>
<form:hidden path="csrftokenurl" id="csrftokenurl" name="csrftokenurl" value="${model.csrftokenurl}"/>
<form:hidden path="appid" id="appid" name="appid" value="${model.appid}"/>
<form:hidden path="branchid" id="branchid" name="branchid" value="${model.branchid}"/>
<form:hidden path="cml" id="cml" name="cml" value="${model.cml}"/>
<form:hidden path="SelectFeeNumId" id="SelectFeeNumId" name="SelectFeeNumId" value=""/>
<form:hidden path="actionId" id="actionId" name="actionId" value="11"/>
<form:hidden path="schemeId" id="schemeId" name="schemeId" value="1"/>
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
			 <c:if test="${InitiateFeePCApplicantList.size()>0 }">
			 <table class="footable table bg-head table-bordered table-striped" >
			 <thead>
					<tr>
						<th></th>
						<th>S.No.</th>
						<th>Fee Type</th>
						<th>Amount</th>
					</tr>
					</thead>
					<tbody id="pay_table">
					<c:forEach items="${InitiateFeePCApplicantList}" var="listApp" varStatus="loop">
					<tr>
					<td>
					<form:checkbox path="feeTypeId" id="feeTypeId${loop.index }" name="feeTypeId" value="${listApp.num_Id}"/>
					</td>
					<td>${loop.index+1 }</td>
					<td>
					<c:out value="${listApp.str_fee_desc}"></c:out>
					<form:hidden path="feeType" id="feeType${loop.index }" name="feeType" value="${listApp.NUM_Fee_id}"/>
					</td>
					<td>
					<i class="fa fa-inr" ></i> ${listApp.num_Amount }
					<fmt:formatNumber type="number" pattern="0.00" value="${listApp.num_Amount}" var="theFormattedValue"/>
					<form:hidden path="feeAmount" id="feeAmount${loop.index }" name="feeAmount" value="${theFormattedValue}"/>
					
					</td>
					</tr>
					</c:forEach>
					</tbody>
			 </table>
			 </c:if>
			 <div class="text-center">
   		<input type="button" name="Save" class="btn btn-primary" id="Save" value="Pay Fee" onclick="checkSelected()"/> 
		</div>
			 </div>

			 </div>
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

