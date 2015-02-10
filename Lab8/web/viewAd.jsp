<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%-- Импортировать JSTL-библиотеки --%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%-- Импортировать собственную библиотеку теговых файлов --%>
<%@taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%-- Импортировать собственную библиотеку тегов --%>
<%@taglib prefix="ad" uri="http://tag/ad" %>
<%-- Получить bean требуемого объявления по переданному параметру id --%>
<ad:getAds id="${param.id}" var="ad"/>
<jsp:useBean id="today" class="java.util.Date" scope="page"/>
<c:set var="adDate" value="${ad.lastModifiedDate}"/>
<html>
<body>
<%-- Подключить заголовок страницы --%>
<jsp:include page="/static/header.jsp"></jsp:include>
<%-- Вставить разметку 1-колоночной страницы --%>
<my:layout1Column>
    <%-- Вывести тему объявления крупными буквами как заголовок --%>
    <h1><c:out value="${ad.subject}"/></h1>
    <%-- Вывести служебную информацию об объявлении - кто автор? когда изменено? --%>
    <div style="text-decoration: italic; font-size: 10px">
        Автор: <c:out value="${ad.author.name}"/>,
        последняя дата редактирования:
        <fmt:formatDate pattern="hh:mm:ss dd-MM-yyyy" value="${ad.lastModifiedDate}"/>
    </div>
    <%-- Отобразить текст объявления в отдельной рамке --%>
    <div style="border: 1px solid black; padding: 20px;">
        <c:out value="${ad.body}"/>
    </div>
    <%-- Если пользователь аутентифицирован - показать ставки --%>
    system info: from ${ad.endTime} till  ${today.time} <br>
    <c:if test="${sessionScope.authUser!=null}">
        <c:choose>
            <c:when test="${ad.endTime > today.time}">
                <h3>Ставки</h3>

                <p>Количество ставок: <c:out value="${ad.bidsCount}"/></p>

                <div style="border: 1px solid black; padding: 20px;">
                    <my:errorMessage/>
                    <form action="makeBid" name="bidForm" method="post" accept-charset="UTF-8">
                        Введите вашу ставку: <input type="text" name="bidValue"
                                                    value="<fmt:formatNumber value="${ad.lastBid.bidValue * 1.02}" type="number" pattern="#.##" />"><br>
                        Комментарий к ставке: <input type="text" name="bidComment" value=""><br>
                        <input type="hidden" name="adId" value="${param.id}">
                        <input type="submit" text="submit">
                    </form>
                    <p>Bids: </p>
                    <c:forEach var="cBid" items="${ad.bids}">
                        <c:if test="${not empty cBid.author.name}">
                            <c:out value="${cBid.author.name}"/> сделал ставку в размере <c:out
                                value="${cBid.bidValue}"/>.
                            <c:if test="${cBid.comment != ''}">
                                Комментарий: <c:out value="${cBid.comment}"/>
                            </c:if>
                            <br>
                        </c:if>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                Аукцион закончен!<br>
                <c:choose>
                    <c:when test="${empty cBid.author.name}">
                        <p>Победителя нету :(</p>
                    </c:when>
                    <c:otherwise>
                        <p>Победил: ${ad.lastBid.author.name}, $${ad.lastBid.bidValue}</p>
                    </c:otherwise>
                </c:choose>
            </c:otherwise>
        </c:choose>

    </c:if>
</my:layout1Column>
<%-- Вставить нижний заголовок страницы --%>
<%@ include file="/static/footer.jsp" %>
</body>
<head>
    <%-- Вывести тему объявления как заголовок страницы --%>
    <title><c:out value="${ad.subject}"/></title>
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8'/>
</head>
</html>