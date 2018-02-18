package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.storage.Storage;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ResumeServlet extends HttpServlet {

    private Storage storage; // = Config.get().getStorage();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.get().getStorage();
        // Solution for iteration on enumeration.
        // According to https://stackoverflow.com/a/6740072/8323154
        config.getServletContext().setAttribute("contactTypes", ContactType.values());
        config.getServletContext().setAttribute("sectionTypes", SectionType.values());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");

        Resume r = uuid == null ? new Resume(fullName) : storage.get(uuid);

        updateResumeFromRequest(r, request.getParameterMap());

        if (uuid == null) {
            storage.save(r);
        } else {
            storage.update(r);
        }

        response.sendRedirect("resume");
    }

    private void updateResumeFromRequest(Resume r, Map<String, String[]> paramMap) {
        r.setFullName(paramMap.get("fullName")[0]);
        for (ContactType type : ContactType.values()) {
            String value = paramMap.get(type.name())[0];
            if (value != null && !value.trim().isEmpty()) {
                r.addContact(type, value);
            } else {
                r.getContacts().remove(type);
            }
        }

        for (SectionType type : SectionType.values()) {
            Section section = null;

            switch (type) {
                case PERSONAL:
                case OBJECTIVE:
                    String val = paramMap.get(type.name())[0];
                    if (!val.trim().isEmpty()) {
                        section = new TextSection(val);
                    }
                    break;
                case QUALIFICATIONS:
                case ACHIEVEMENT:
                    String[] values = paramMap.get(type.name() + "_item");
                    if (values != null) {
                        List<String> items = Arrays.stream(values)
                                .map(String::trim)
                                .filter(s -> s.length() > 0)
                                .collect(Collectors.toList());
                        if (!items.isEmpty()) {
                            section = new ListSection(items);
                        }
                    }
                    break;
                case EXPERIENCE:
                case EDUCATION:
                    int sectionSize = Integer.parseInt(paramMap.get(type.name() + "_size")[0]);
                    if (sectionSize > 0) {
                        List<Organization> organizations = new ArrayList<>(sectionSize);
                        for (int i = 0; i < sectionSize; i++) {
                            Organization org = getOrganization(paramMap, type.name() + "_" + i);
                            if (org != null) {
                                organizations.add(org);
                            }
                        }
                        section = new OrganizationSection(organizations);
                    }
                    break;
            }
            if (section == null) {
                r.getSections().remove(type);
            } else {
                r.addSection(type, section);
            }
        }
    }

    private Organization getOrganization(Map<String, String[]> paramMap, String prefix) {
        String name = paramMap.get(prefix + "_name")[0].trim();
        String url = paramMap.get(prefix + "_url")[0].trim();

        if (name.length() == 0) {
            return null;
        }

        int orgSize = Integer.parseInt(paramMap.get(prefix + "_size")[0]);
        if (orgSize == 0) {
            return new Organization(name, url);
        }

        List<Organization.Position> positions = new ArrayList<>(orgSize);
        for (int i = 0; i < orgSize; i++) {
            String startDate = paramMap.get(prefix + "_" + i + "_sDate")[0] + "-01";
            String title = paramMap.get(prefix + "_" + i + "_title")[0].trim();
            String description = paramMap.get(prefix + "_" + i + "_descr")[0].trim();
            boolean isNow = paramMap.get(prefix + "_" + i + "_isNow") == null;

            if (title.length() == 0) {
                continue;
            }

            LocalDate sd = LocalDate.parse(startDate);
            if (isNow) {
                positions.add(new Organization.Position(sd.getYear(), sd.getMonth(), title, description));
            } else {
                String endDate = paramMap.get(prefix + "_" + i + "_eDate")[0] + "-01";
                LocalDate ed = LocalDate.parse(endDate);
                positions.add(new Organization.Position(sd, ed, title, description));
            }
        }

        return new Organization(new Link(name, url), positions);
    }



    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        Resume r;
        switch (action) {
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "view":
            case "edit":
                r = storage.get(uuid);
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("resume", r);
        request.getRequestDispatcher(
                ("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
        ).forward(request, response);
    }
}