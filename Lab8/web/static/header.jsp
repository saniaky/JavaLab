<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- Обработать параметр сортировки --%>
<c:if test="${param.sort!=null}">
    <c:set var="sort" scope="session" value="${param.sort}"/>
</c:if>
<%-- Обработать параметр направления сортировки --%>
<c:if test="${param.dir!=null}">
    <c:set var="dir" scope="session" value="${param.dir}"/>
</c:if>
<%-- Общая декоративная "шапка" для всех страниц --%>
<div style="background-color: #a0c8ff; padding: 10px; ">
    <a href="index.jsp"><img src="resources/ad.png" width="50" height="53" border="0" align="left"></a>

    <div style="font-family: 'Trebuchet'; font-size: 30px; height: 53px; margin-left: 80px;">
        Доска объявлений "Хороший листок" v.1.1.0
    </div>
</div>
<%-- Панель отображается если пользователь аутентифицирован --%>
<c:if test="${sessionScope.authUser!=null}">
    <div style="background-color: #ccc; padding: 5px">
        <div style="float: right; margin-right: 5px">
            <a href="cabinet.jsp">Личный кабинет</a>
            [ <a href="<c:url value="doLogout.jsp" />">Выйти</a> ]
        </div>
        Вы вошли как:
        <c:out value="${sessionScope.authUser.name}"/>
        ( <c:out value="${sessionScope.authUser.login}"></c:out> )
    </div>
</c:if>