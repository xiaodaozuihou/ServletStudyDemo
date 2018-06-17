<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.Date" %><%--
  Created by IntelliJ IDEA.
  User: 亚旭
  Date: 2018/4/18
  Time: 21:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Today's date</title>
</head>
<body>
    <%
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
        String s = dateFormat.format(new Date());
        out.println("Today is " + s);
    %>
</body>
</html>
