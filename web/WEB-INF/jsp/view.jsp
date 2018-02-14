<%@ page import="ru.javawebinar.basejava.model.SectionType" %>
<%@ page import="ru.javawebinar.basejava.model.TextSection" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="f" uri="localDateFormatter" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="ru.javawebinar.basejava.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"></a></h2>
    <p>
        <c:forEach var="contactEntry" items="${resume.contacts}">
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<ru.javawebinar.basejava.model.ContactType, java.lang.String>"/>
                <%=contactEntry.getKey().toHtml(contactEntry.getValue())%><br/>
        </c:forEach>
    </p>


    <c:forEach var="e" items="${resume.sections}">
        <jsp:useBean id="e"
                     type="java.util.Map.Entry<ru.javawebinar.basejava.model.SectionType, ru.javawebinar.basejava.model.Section>"/>
        <h3><%=e.getKey().getTitle()%>
        </h3>
        <c:choose>
            <c:when test="${e.key == SectionType.PERSONAL || e.key == SectionType.OBJECTIVE}">
                <p>${e.value}</p>
            </c:when>

            <c:when test="${e.key == SectionType.ACHIEVEMENT || e.key == SectionType.QUALIFICATIONS}">
                <ul>
                    <c:forEach var="item" items="${e.value.items}">
                        <jsp:useBean id="item" type="java.lang.String"/>
                        <li>${item}</li>
                    </c:forEach>
                </ul>
            </c:when>

            <c:when test="${e.key == SectionType.EXPERIENCE || e.key == SectionType.EDUCATION}">
                <c:forEach var="org" items="${e.value.organizations}">
                    <jsp:useBean id="org" type="ru.javawebinar.basejava.model.Organization"/>
                    <table cellspacing="10px">
                        <tr>
                            <td>
                                <h4>
                                    <c:choose>
                                        <c:when test="${empty org.homePage.url}">
                                            <c:out value="${org.homePage.name}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="${org.homePage.url}">${org.homePage.name}</a>
                                        </c:otherwise>
                                    </c:choose>
                                </h4>
                            </td>
                            <td>
                                <ul>
                                    <c:forEach var="pos" items="${org.positions}">
                                        <jsp:useBean id="pos"
                                                     type="ru.javawebinar.basejava.model.Organization.Position"/>
                                        <li>
                                                ${f:formatLocalDate(pos.startDate, "MM.uuuu")} -
                                            <c:choose>
                                                <c:when test="${pos.currentPosition}">
                                                    Now
                                                </c:when>
                                                <c:otherwise>
                                                    ${f:formatLocalDate(pos.endDate, "MM.uuuu")}
                                                </c:otherwise>
                                            </c:choose>
                                            : ${pos.description} </li>
                                    </c:forEach>
                                </ul>
                            </td>
                        </tr>
                    </table>
                </c:forEach>
            </c:when>
        </c:choose>
    </c:forEach>

</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
