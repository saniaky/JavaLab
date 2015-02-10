<%@page language="java" pageEncoding="UTF-8" %>
<%-- Импортировать JSTL-библиотеку --%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%-- Импортировать собственную библиотеку тегов --%>
<%@taglib prefix="ad" uri="http://tag/ad" %>
<%-- Указать, что мы ожидаем данные в кодировке UTF-8 --%>
<fmt:requestEncoding value="UTF-8"/>
<%-- Очистить переменную перед использованием --%>
<c:remove var="adData"/>


<%-- Получить bean требуемого объявления по переданному параметру id --%>
<ad:getAds id="${param.id}" var="ad"/>

<jsp:useBean id="user" class="entity.Bid" scope="session"/>

<%-- Скопировать в bean параметры bid из HTTP-запроса --%>
<jsp:setProperty name="adData" property="bid"/>


<c:set var="currentCountryCode"
       value="${KualiForm.document.developmentProposalList[0].proposalPersons[personIndex].countryCode}"/>
<jsp:setProperty name="KualiForm" property="currentPersonCountryCode" value="${currentCountryCode }"/>


<%-- Проверить сообщение об ошибке, чтобы узнать результат операции --%>
<c:choose>
    <c:when test="${sessionScope.errorMessage==null}">
        <%-- Ошибок не возникло, очистить временные данные --%>
        <c:remove var="adData" scope="session"/>
        <%-- Переадресовать на страницу личного кабинета --%>
        <%--<c:redirect url="/cabinet.jsp"/>--%>
    </c:when>
    <c:otherwise>
        <%-- Переадресовать на страницу редактирования объявления --%>
        <c:redirect url="/updateAd.jsp">
            <%-- В качестве параметра при переадресации передать id объявления --%>
            <c:param name="id" value="${adData.id}"/>
        </c:redirect>
    </c:otherwise>
</c:choose>