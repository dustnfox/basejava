<%@ page import="ru.javawebinar.basejava.model.SectionType" %><%--suppress HtmlFormInputWithoutLabel --%>
<%@ page import="ru.javawebinar.basejava.model.ContactType" %><%--suppress HtmlFormInputWithoutLabel --%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--Custom taglib solution for LocalDate formatting.
    Solution source: https://stackoverflow.com/a/35607225/8323154 --%>
<%@ taglib prefix="f" uri="localDateFormatter" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <script src="<c:url value='/scripts/jsutils.js'/>"></script>
    <jsp:useBean id="isNew" type="java.lang.String" scope="request"/>
    <jsp:useBean id="resume" type="ru.javawebinar.basejava.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="new" value="${isNew}">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt>Имя:</dt>
            <dd><input type="text" name="fullName" size=50 value="${resume.fullName}"></dd>
        </dl>
        <h3>Контакты:</h3>

        <c:forEach var="cType" items="${ContactType.values()}">
            <dl>
                <dt>${cType.title}</dt>
                <dd><input type="text" name="${cType.name()}" size=30 value="${resume.getContact(cType)}"></dd>
            </dl>
        </c:forEach>
        <br>
        <h3>Разделы:</h3>

        <c:forEach var="sType" items="${SectionType.values()}">
            <jsp:useBean id="sType" type="ru.javawebinar.basejava.model.SectionType"/>
            <c:choose>

                <c:when test="<%= sType == SectionType.OBJECTIVE%>">
                    <dl>
                        <dt>${sType.title}</dt>
                        <dd><input type="text" name="${sType.name()}"
                                   value="${empty resume.getSection(sType) ? '' : resume.getSection(sType).content}">
                        </dd>
                    </dl>
                </c:when>

                <c:when test="<%=sType == SectionType.PERSONAL %>">
                    <dl>
                        <dt>${sType.title}</dt>
                        <dd><textarea cols=80 rows=5 name="${sType.name()}"
                        >${empty resume.getSection(sType) ? '' : resume.getSection(sType).content}</textarea>
                        </dd>
                    </dl>
                </c:when>

                <c:when test="<%=sType == SectionType.QUALIFICATIONS || sType == SectionType.ACHIEVEMENT%>">
                    <dl>
                        <dt>${sType.title} <img src="img/add.png" onclick="addEmptyListItem('${sType.name()}')"/></dt>
                        <dd>
                            <ul id="${sType.name()}">
                                <input type="hidden" name="${sType.name()}_size"
                                       value=${empty resume.getSection(sType) ? 0 : resume.getSection(sType).items.size()}>
                                <c:if test="${not empty resume.getSection(sType)}">
                                    <c:forEach var="item" items="${resume.getSection(sType).items}" varStatus="i_i">
                                        <li>
                                            <input size="30" name="${sType.name()}_item" type="text" value="${item}"/>
                                        </li>
                                    </c:forEach>
                                </c:if>
                            </ul>
                        </dd>
                        <br>
                        <button type="button" onclick="removeLastChild('${sType.name()}')">Удалить последний элемент
                            <img
                                src="img/delete.png"></button>
                    </dl>
                </c:when>

                <c:when test="<%=sType == SectionType.EXPERIENCE || sType == SectionType.EDUCATION%>">
                    <dl>
                        <dt>${sType.title} &nbsp; <img src="img/add.png"
                                                       onclick="addEmptyOrganization('${sType.name()}')"/></dt>

                        <dd>
                            <ul id="${sType.name()}">
                            <input type="hidden" name="${sType.name()}_size" id="${sType.name()}_size"
                                   value=${empty resume.getSection(sType) ? 0 : resume.getSection(sType).organizations.size()}>
                                <c:if test="${not empty resume.getSection(sType)}">
                                    <c:forEach var="org" items="${resume.getSection(sType).organizations}"
                                               varStatus="org_i">
                                        <c:set var="oPref" value="${sType.name()}_${org_i.index}"/>
                                        <li>
                                            <table>
                                                <tr>
                                                    <td>Наименование:</td>
                                                    <td><input name="${oPref}_name" type="text"
                                                               value="${org.homePage.name}"></td>
                                                </tr>
                                                <tr>
                                                    <td>URL:</td>
                                                    <td><input name="${oPref}_url" type="text"
                                                               value="${org.homePage.url}">
                                                    </td>
                                                </tr>
                                            </table>
                                            <p>Позиции <img src="img/add.png" onclick="addEmptyPosition('${oPref}')"/>
                                            </p>
                                            <ul id="${oPref}">
                                                <input name="${oPref}_size" type="hidden"
                                                       value="${org.positions.size()}">

                                        <c:forEach var="pos" items="${org.positions}" varStatus="pos_i">
                                            <c:set var="pPref" value="${oPref}_${pos_i.index}"/>
                                            <c:set var="sDate"
                                                   value="${f:formatLocalDate(pos.startDate, \"uuuu-MM\")}"/>
                                            <c:set var="eDate" value="${f:formatLocalDate(pos.endDate, \"uuuu-MM\")}"/>
                                            <li>
                                                <table>
                                                    <tr>
                                                        <td>С:</td>
                                                        <td><input type="month" name="${pPref}_sDate"
                                                                   value="${sDate}"></td>
                                                        <td>По:</td>
                                                        <td><input type="month" name="${pPref}_eDate"
                                                                   id="${pPref}_eDate"
                                                                   value="${eDate}" ${pos.isCurrentPosition() ? " disabled" : ""}/>
                                                        </td>
                                                        <td><input type="checkbox" name="${pPref}_isNow"
                                                                   id="${pPref}_isNow"
                                                            ${pos.isCurrentPosition() ? "checked" : ""}
                                                                   onchange="disableBasedOnState('${pPref}_eDate','${pPref}_isNow')">
                                                        </td>
                                                        <td>Текущая позиция</td>
                                                    </tr>
                                                    <tr>
                                                        <td>Название:</td>
                                                        <td colspan="5">
                                                            <input size="80" type="text" name="${pPref}_title"
                                                                   value="${pos.title}">
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>Описание:</td>
                                                        <td colspan="5">
                                                            <textarea cols=80 rows=5 name="${pPref}_descr"
                                                            >${pos.description}</textarea>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </li>
                                        </c:forEach>
                                            </ul>
                                            <button type="button" onclick="removeLastChild('${oPref}')">
                                                Удалить последнюю позицию <img src="img/delete.png">
                                            </button>
                                        </li>
                                    </c:forEach>
                                </c:if>
                            </ul>
                        </dd>
                        <br>
                        <button type="button" onclick="removeLastChild('${sType.name()}')">
                            Удалить последнюю организацию <img src="img/delete.png">
                        </button>
                    </dl>
                </c:when>

            </c:choose>
        </c:forEach>
        <hr>
        <button type="submit">Сохранить</button>
        <button onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>