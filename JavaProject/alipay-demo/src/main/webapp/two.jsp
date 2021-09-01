<%--
  Created by IntelliJ IDEA.
  User: NINGMEI
  Date: 2021/5/25
  Time: 14:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <%

        String form="<form action='three.jsp'><input type='submit' value='提交'/></form><script>document.forms[0].submit();</script>";
        out.println(form);
    %>
    <h3>hello</h3>

</body>
</html>
