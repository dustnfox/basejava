<%@ page import="ru.javawebinar.basejava.model.SectionType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--Custom taglib solution for LocalDate formatting.
    Solution source: https://stackoverflow.com/a/35607225/8323154 --%>
<%@ taglib prefix="f" uri="localDateFormatter" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <script src="<c:url value='/scripts/jsutils.js'/>"></script>
    <jsp:useBean id="resume" type="ru.javawebinar.basejava.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt>Имя:</dt>
            <dd><input type="text" name="fullName" size=50 value="${resume.fullName}"></dd>
        </dl>
        <h3>Контакты:</h3>
        <c:forEach var="cType" items="${contactTypes}">
            <dl>
                <dt>${cType.title}</dt>
                <dd><input type="text" name="${cType.name()}" size=30 value="${resume.getContact(cType)}"></dd>
            </dl>
        </c:forEach>


        <c:forEach var="sType" items="${sectionTypes}">
            <jsp:useBean id="sType" type="ru.javawebinar.basejava.model.SectionType"/>
            <c:set var="sTag" value="${sType.name()}" scope="request"/>
            <c:choose>
                <c:when test="${sType == SectionType.EDUCATION}">
                    <h3>${sType.title} &nbsp;
                        <img src="img/add.png" onclick="addOrganization('${sType.name()}')"/>
                    </h3>

                    <c:if test="${not empty resume.getSection(sType)}">
                        <ol id="${sType.name()}">
                            <input type="hidden" name="${sType.name()}_size" id="${sType.name()}_size"
                                   value=${empty resume.getSection(sType) ? 0 : resume.getSection(sType).organizations.size()}>
                            <c:forEach var="org" items="${resume.getSection(sType).organizations}" varStatus="org_i">
                                <li> Наименование:
                                    <input type="text" size="30"
                                           id="${sType.name()}_${org_i.index}_name"
                                           name="${sType.name()}_${org_i.index}_name"
                                           value="${org.homePage.name}"> &nbsp;
                                    URL:
                                    <input type="text" size="30"
                                           id="${sType.name()}_${org_i.index}_url"
                                           name="${sType.name()}_${org_i.index}_url"
                                           value="${org.homePage.url}"> <br>

                                    <h4>Позиции: &nbsp;
                                        <img src="img/add.png" onclick="addPosition('${sType.name()}_${org_i.index}')"/>
                                    </h4>
                                    <ol id="${sType.name()}_${org_i.index}">
                                        <input type="hidden" name="${sType.name()}_${org_i.index}_size"
                                               value=${org.positions.size()}>
                                        <c:forEach var="pos" items="${org.positions}" varStatus="pos_i">
                                            <li> С:
                                                <input type="month"
                                                       name="${sType.name()}_${org_i.index}_${pos_i.index}_sDate"
                                                       value="${f:formatLocalDate(pos.startDate, "uuuu-MM")}"> &nbsp;
                                                По:
                                                <input type="month"
                                                       id="${sType.name()}_${org_i.index}_${pos_i.index}_eDate"
                                                       name="${sType.name()}_${org_i.index}_${pos_i.index}_eDate"
                                                       value="${f:formatLocalDate(pos.endDate, "uuuu-MM")}"
                                                       <c:if test="${pos.isCurrentPosition()}">disabled="disabled"</c:if>
                                                > &nbsp;
                                                <input type="checkbox" type="checkbox"
                                                       id="${sType.name()}_${org_i.index}_${pos_i.index}_isNow"
                                                       name="${sType.name()}_${org_i.index}_${pos_i.index}_isNow"
                                                       value="${pos.isCurrentPosition()}"
                                                       onchange="disableBasedOnState(
                                                               '${sType.name()}_${org_i.index}_${pos_i.index}_eDate',
                                                               '${sType.name()}_${org_i.index}_${pos_i.index}_isNow')">
                                                Текущая<br>

                                                Описание:<br>
                                                <input type="text"
                                                       name="${sType.name()}_${org_i.index}_${pos_i.index}_descr"
                                                       size="80"
                                                       value="${pos.description}">
                                            </li>
                                        </c:forEach>
                                    </ol>
                                </li>
                            </c:forEach>
                        </ol>
                    </c:if>
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