<%@ page language="java" contentType="text/html; charset=utf-8"    pageEncoding="utf-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>
<link rel="stylesheet" type="text/css" href="/MANAK/resources/app_srv/GlobalPages/CommonUtility/CSS/select2.css" />
<script type="text/javascript" src="/MANAK/resources/app_srv/GlobalPages/CommonUtility/JS/select2.min.js"></script>
<script type="text/javascript">
function checkSelected(){
	var allVals = [];
    $(':checkbox:checked').each(function () {
    	allVals.push($(this).val());
    });
    if(allVals=="")
    {
    	alert("Select checkbox");
    }else{
    	$("#SelectFeeNumId").val(allVals);
    	document.frm.submit();
    }
}
</script>
<div class="container">
<form:form cssClass="form-horizontal" method="post" id="frm" name="frm" action="InitiateFeeHMApplicantSubmit" commandName="model">
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
<form:hidden path="schemeId" id="schemeId" name="schemeId" value="2"/>

<c:choose>
<c:when test="${ApplicationDetailHMList.size()>0 }">
<fieldset>
			<legend>Details</legend>
			<div class="col-md-12 form-group">
			<div class="col-md-3 bold">Application No.</div>
				<div class="col-md-3">
					<span> ${ApplicationDetailHMList.get(0).str_app_id} </span>
				</div>
				<div class="col-md-3 bold">CML No.</div>
				<div class="col-md-3">
					<span> ${ApplicationDetailHMList.get(0).cmlno} </span>
				</div>
			</div>
			<div class="col-md-12 form-group">
			<div class="col-md-3 bold">Firm Name</div>
				<div class="col-md-3">
					<span> ${ApplicationDetailHMList.get(0).firmname} </span>
				</div>
				<div class="col-md-3 bold">Firm Address</div>
				<div class="col-md-3">
					<span> ${ApplicationDetailHMList.get(0).address} </span>
				</div>
			</div>
			<div class="col-md-12 form-group">
			<div class="col-md-3 bold">IS No.</div>
				<div class="col-md-3">
					<span> ${ApplicationDetailHMList.get(0).isno} </span>
				</div>
				<div class="col-md-3 bold">Standard Title</div>
				<div class="col-md-3">
					<span> ${ApplicationDetailHMList.get(0).str_standard_title} </span>
				</div>
			</div>
			<div class="col-md-12 form-group">
			<div class="col-md-3 bold">Email</div>
				<div class="col-md-3">
					<span> ${ApplicationDetailHMList.get(0).emailid} </span>
				</div>
				<div class="col-md-3 bold">Contact No</div>
				<div class="col-md-3">
					<span> ${ApplicationDetailHMList.get(0).contactno} </span>
				</div>
			</div>
			</fieldset>
			<fieldset>
			<legend>Dues</legend>
			 <div class="col-md-12">

			 
			 
			 <div class="col-md-6">
  	    <div class="col-md-12 tab-content">
  	    <c:if test="${PreviousDuesDetailHMlist.size()>0}">
             <table class="footable table bg-head table-bordered table-striped" data-page-size="5">
                <thead>
                <tr>
                <th></th>
                    <th>
                    S.No.                        
                    </th>
                    <th>
                       Fee Type
                    </th>
                    <th>
                       Fee Amount
                    </th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${PreviousDuesDetailHMlist}" var="listApp" varStatus="loop">
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
                <fmt:formatNumber type="number" pattern="0.00" value="${listApp.num_Amount}" var="theFormattedValue"/>
                <fmt:formatNumber type="number" pattern="0.00" value="${listApp.num_Amount}"/>
                <form:hidden path="feeAmount" id="feeAmount${loop.index }" name="feeAmount" value="${theFormattedValue}"/>
                </td>
                </tr>
                </c:forEach>
               </tbody>
               </table>
               </c:if>
                </div>
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

