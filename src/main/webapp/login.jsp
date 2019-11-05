<%--
  Created by IntelliJ IDEA.
  User: Harry
  Date: 2019/11/5
  Time: 10:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>login</title>
</head>
<body>
    <center>
        <form action="/user/doLogin" method="post">
            姓名：<input type="text" name="name"><br>
            密码：<input type="text" name="password"><br>
            <input type="submit" value="登陆">&nbsp;&nbsp;
            <input type="reset" value="重置">
        </form>
    </center>
</body>
</html>
