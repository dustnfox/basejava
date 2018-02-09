package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.storage.SqlStorage;
import ru.javawebinar.basejava.storage.Storage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class ResumeServlet extends HttpServlet {
    private static final String CONF_NAME = "resumes.properties";
    private static final Storage storage = getSqlStorage();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        PrintWriter out = response.getWriter();
        out.write(addCssStyle(request, "/css/style.css"));
        out.write(openTableTag());

        String uuid = request.getParameter("uuid");
        if (uuid == null) {
            out.write(storage.getAllSorted().stream()
                    .map(this::putResumeInTableRow)
                    .reduce("", String::concat));
        } else {
            out.write(putResumeInTableRow(storage.get(uuid)));
        }

        out.write("</table>");
    }

    private String addCssStyle(HttpServletRequest request, String cssPath) {
        return "<link rel='stylesheet' type='text/css' href='" + request.getContextPath() + cssPath + " '/>";
    }

    private String openTableTag() {
        return "<table style='width:80%' border=1 cellspacing=0 cellpadding=10> <tr>" +
                wrapInTag("UUID", "th") +
                wrapInTag("Имя", "th") +
                wrapInTag("Контакты", "th") +
                wrapInTag(SectionType.PERSONAL.getTitle(), "th") +
                wrapInTag(SectionType.OBJECTIVE.getTitle(), "th") +
                wrapInTag(SectionType.QUALIFICATIONS.getTitle(), "th") +
                wrapInTag(SectionType.ACHIEVEMENT.getTitle(), "th") + "</tr>";
    }

    private String wrapInTag(String s, String tag) {
        return String.format("<%1$s>%2$s</%1$s>", tag, s);
    }

    private String putResumeInTableRow(Resume r) {
        StringBuilder sb = new StringBuilder("<tr>");
        sb.append(wrapInTag(r.getUuid(), "td"));
        sb.append(wrapInTag(r.getFullName(), "td"));

        sb.append("<td><ul>");
        r.getContacts().forEach((k, v) -> sb.append(wrapInTag(k.getTitle() + ": " + v, "li")));
        sb.append("</td></ul>");

        putSectionInCell(SectionType.PERSONAL, r.getSection(SectionType.PERSONAL), sb);
        putSectionInCell(SectionType.OBJECTIVE, r.getSection(SectionType.OBJECTIVE), sb);
        putSectionInCell(SectionType.QUALIFICATIONS, r.getSection(SectionType.QUALIFICATIONS), sb);
        putSectionInCell(SectionType.ACHIEVEMENT, r.getSection(SectionType.ACHIEVEMENT), sb);

        return sb.append("</tr>").toString();
    }

    private void putSectionInCell(SectionType type, Section s, StringBuilder sb) {
        sb.append("<td>");
        if (s != null) {
            switch (type) {
                case PERSONAL:
                case OBJECTIVE:
                    TextSection textSection = (TextSection) s;
                    sb.append(textSection.getContent());
                    break;
                case ACHIEVEMENT:
                case QUALIFICATIONS:
                    List<String> items = ((ListSection) s).getItems();
                    sb.append(putInUnnamedList(items));
                    break;
            }
        }
        sb.append("</td>");
    }

    private <T> String putInUnnamedList(List<T> list) {
        StringBuilder sb = new StringBuilder("<ul>");
        list.forEach(e -> sb.append("<li>").append(e.toString()).append("</li>"));
        return sb.append("</ul").toString();
    }

    private static Storage getSqlStorage() {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
        } catch (SQLException e) {
            throw new StorageException("DB Driver load fail", e);
        }
        Properties props = new Properties();
        try {
            props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(CONF_NAME));
            return new SqlStorage(
                    props.getProperty("db.url"),
                    props.getProperty("db.user"),
                    props.getProperty("db.password"));
        } catch (IOException e) {
            throw new StorageException("Can't access properties file", e);
        }
    }
}
