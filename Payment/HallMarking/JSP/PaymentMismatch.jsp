  <%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
 <%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
 <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
  <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
 
 <%@ page isELIgnored="false" %>
 
<div class="container">
 	<div class="h3 bold text-center " style="color:red">Payment Mismatch</div>
 	<div class="col-md-12">Transaction Number: <c:out value="${trNumber}"></c:out></div>
</div> 



