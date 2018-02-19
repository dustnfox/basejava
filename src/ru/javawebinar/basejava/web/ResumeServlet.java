package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.exception.NotExistStorageException;
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
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        Resume r = new Resume(uuid, fullName);

        updateResumeFromRequest(r, request);

        try {
            storage.update(r);
        } catch (NotExistStorageException e) {
            storage.save(r);
        }

        response.sendRedirect("resume");
    }

    private void updateResumeFromRequest(Resume r, HttpServletRequest request) {
        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
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
                    String val = request.getParameter(type.name());
                    if (!val.trim().isEmpty()) {
                        section = new TextSection(val);
                    }
                    break;
                case QUALIFICATIONS:
                case ACHIEVEMENT:
                    String[] values = request.getParameterValues(type.name() + "_item");
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
                    int sectionSize = Integer.parseInt(request.getParameter(type.name() + "_size"));
                    if (sectionSize > 0) {
                        List<Organization> organizations = new ArrayList<>(sectionSize);
                        for (int i = 0; i < sectionSize; i++) {
                            Organization org = getOrganization(request, type.name() + "_" + i);
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

    private Organization getOrganization(HttpServletRequest request, String prefix) {
        String name = request.getParameter(prefix + "_name").trim();
        String url = request.getParameter(prefix + "_url").trim();

        int orgSize = Integer.parseInt(request.getParameter(prefix + "_size"));
        if (orgSize == 0) {
            return new Organization(name, url);
        }

        List<Organization.Position> positions = new ArrayList<>(orgSize);
        for (int i = 0; i < orgSize; i++) {
            String startDate = request.getParameter(prefix + "_" + i + "_sDate") + "-01";
            String title = request.getParameter(prefix + "_" + i + "_title").trim();
            String description = request.getParameter(prefix + "_" + i + "_descr").trim();
            boolean isNow = request.getParameter(prefix + "_" + i + "_isNow") != null;

            if (title.length() == 0) {
                continue;
            }

            LocalDate sd = LocalDate.parse(startDate);
            if (isNow) {
                positions.add(new Organization.Position(sd.getYear(), sd.getMonth(), title, description));
            } else {
                String endDate = request.getParameter(prefix + "_" + i + "_eDate") + "-01";
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
            case "create":
                r = new Resume("");
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