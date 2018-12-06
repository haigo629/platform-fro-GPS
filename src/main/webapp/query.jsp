<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*" %>
<%@ taglib prefix="c" 
           uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<script type="text/javascript">
</script>
<body>
<%
	try{
		String time =null;
		String time1 =null;
		if(null!=request.getAttribute("time")){
			time = request.getAttribute("time").toString();
			time1 = request.getAttribute("time1").toString();
		}
	}catch(Exception e){
		
	}
%>
    <form action="demo?action=queryJSP" method="post">      
        <input type="submit" value="查询测试" />  
    </form>
    	lat:<%= request.getAttribute("lat").toString() %><br>  
        lng:<%= request.getAttribute("lng").toString() %><br>
        <c:if test="${time!=null}">
        time:${time}<br> 
        </c:if>
        
    <form action="demo?action=queryAllKey" method="post">      
        <input type="submit" value="查询所有key" />  
    </form>
    <form action="demo?action=queryDevByKey" method="post">
    	<select name="key">
    		<%
    			List<String> keys = (List<String>)request.getAttribute("keys");
    		%>
    		<c:forEach items="${keys}" var="key">
		     	<option value="${key}">${key}</option><br>
		     </c:forEach>
    	</select>      
        <input type="submit" value="查询单个设备位置" />  
    </form>
    	lat1:<%= request.getAttribute("lat1").toString() %><br>  
        lng1:<%= request.getAttribute("lng1").toString() %><br>
        <c:if test="${time1!=null}">
        time1:${time1}<br> 
        </c:if>    	  
</body>
</html>