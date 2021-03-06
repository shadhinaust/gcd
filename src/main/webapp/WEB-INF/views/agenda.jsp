<%--
  Created by IntelliJ IDEA.
  User: Mohit Chowdhury
  Date: 9/20/2020
  Time: 2:12 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Google Calendar Events</title>
</head>
<body>
    <h3>${message}</h3>
    <table>
        <c:forEach items="${agendas}" var="agenda">
            <tr>
                <td>${agenda.getTime()}</td>
                <td>${agenda.getSummary()}</td>
            </tr>
        </c:forEach>
    </table>
    <h3>Free slots</h3>
    <table>
        <c:forEach items="${freeSlots}" var="freeSlot">
            <tr>
                <td>${freeSlot.getTime()}</td>
            </tr>
        </c:forEach>
    </table>
    <form method="get" action="/agenda">
        <p>
            <input type="submit" value="Refresh"/>
        </p>
    </form>
    <form method="get" action="/">
        <p>
            <input type="submit" value="Home"/>
        </p>
    </form>
</body>
</html>
