<%--
  Created by IntelliJ IDEA.
  User: Mohit Chowdhury
  Date: 9/20/2020
  Time: 1:52 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Google Calendar Events</title>
</head>
<body>
<form method="get" action="/auth">
    <p>
        <input type="submit" value="Sign in with Google"/>
    </p>
</form>
<form method="get" action="/agenda">
    <p>
        <input type="submit" value="Agenda"/>
    </p>
</form>
</body>
</html>
