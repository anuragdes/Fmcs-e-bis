<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page isELIgnored="false" %>

<table id="tblresult" class="blue bg-head table table-striped table-bordered table-hover dt-responsive nowrap dataTable no-footer dtr-inline collapsed">
	<thead class="filters">
		<tr style="background-color: #338ec9; color: #ffffff;">
				<%--<th data-toggle="true">S.No.</th>--%>
			<th data-toggle="true"><spring:message code="label.Sno."></spring:message></th>
				<%--<th>CML No.</th>--%>
			<th><spring:message code="label.cml"></spring:message> / <spring:message code="label.cma"></spring:message></th>
				<%--<th>Firm/Organization Name</th>--%>
			<th><spring:message code="label.firmOrganization"></spring:message></th>
				<%--<th>Fee Type</th>--%>
			<th><spring:message code="label.fee.discription"></spring:message></th>
				<%--<th>Amount (INR)</th>--%>
			<th><spring:message code="label.fee.inr"></spring:message></th>
				<%--<th data-hide="phone,tablet">Entry Date</th>--%>
			<th><spring:message code="label.initiateDate"></spring:message></th>
				<%--<th data-priority="1">More</th>--%>
			<%--<th><spring:message code="label.actions"></spring:message></th>--%>
		</tr>
	</thead>
	<tbody id="tblresultbody" class="container">
		<c:if test="${applicationList != null }">
			<%-- <%int i=0; %> --%>
			<c:set var="sno" value="${numlastitem}" />
			<c:forEach items="${applicationList}" var="listApp">
				<tr id="${listApp.strAppId}_${listApp.num_branch_id}_${listApp.numFeeId}_tr">
					<%-- <%i = i+1; %> --%>
					<%-- <td><%= i %></td> --%>
					<td><c:set var="sno" value="${sno + 1}" />${sno}</td>
					<td><c:out value="${listApp.strFileNum}"></c:out></td>
					<%-- <td><a href="/MANAK/getApplicationsDetails.do?EappId=${listApp.EappId }&strBranchId=${listApp.Ebranchid}" target="_blank"><c:out value="${listApp.str_firm_name }"></c:out></a></td> --%>
					<td>${listApp.strFirmName }</td>
					<td>${listApp.strFeeDesc }</td>
					<%--<td>${listApp.num_Amount }</td>--%>
					<td><span><fmt:formatNumber type = "number" minFractionDigits = "2" maxFractionDigits = "2" value = "${listApp.num_Amount }" /></span></td>
					<td><span><fmt:formatDate pattern="yyyy-MM-dd" value="${listApp.dt_Entry_Date }" /></span></td>
					<%--<td class="links"></td>--%>
				</tr>
			</c:forEach>
		</c:if>
	</tbody>
</table>
<br>
<div id="rescount" style="text-align: center; vertical-align: middle;">
	<span class="message" style="font-size: 1.1em;">${numresults} items</span>
</div>


<div id="pages" class="pull-right paginationDiv">
	<ul id="paginationLinks" class="pagination pagination-sm inline">
		<li class="paginationLi">
			<a href="#" id="btnPrev" class="btnpage" title=" Previous Page " onclick="getsearchpageresults(${prevpageno });">
				<span class="pagetxt" style="color: #337ab7">[&lt;&lt;&nbsp;Prev]</span>
			</a>
		</li>
		<li class="paginationLi">
			<a href="#" id="btnNext" class="btnpage" title=" Next Page " onclick="getsearchpageresults(${nextpageno });">
				<span class="pagetxt" style="color: #337ab7">[Next&nbsp;&gt;&gt;]</span>
			</a>
		</li>
		</br>
		<br>
		<li class="paginationLi">
			<a href="#" id="btnFirst" class="btnpage" title=" First Page " onclick="getsearchpageresults(1);">
				<span class="pagetxt" style="color: #337ab7">[First]</span>
			</a>
		</li>
		<c:set var="currpagelink" value="<strong> ${currentpageno} </strong>" />
		<c:forEach var="i" begin="${minpagerange}" end="${maxpagerange}">
			<c:set var="numpagelink"
				value='<a href="#" class="btnpage" onclick="getsearchpageresults(${i });"><span class="pagetxt">${i}</span></a>' />
			<li class="paginationLi">
				<span> ${i == currentpageno ? currpagelink : numpagelink} </span>
			</li>
		</c:forEach>
		<li class="paginationLi">
			<a href="#" id="btnLast" class="btnpage" title=" Last Page " onclick="getsearchpageresults(${lastpageno });">
				<span class="pagetxt" style="color: #337ab7">[Last]</span>
			</a>
		</li>
	</ul>
</div>