package ru.javawebinar.basejava.model;

import java.time.LocalDate;

public class SectionElement {
    private final String title;
    private final String text;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private static final String NOW_STRING = "Сейчас";


    public SectionElement(LocalDate startDate, LocalDate endDate, String title, String text) {
        this.title = title;
        this.text = text;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public SectionElement(LocalDate startDate, LocalDate endDate, String title) {
        this(startDate, endDate, title, null);
    }

    public SectionElement(LocalDate startDate, String title, String text) {
        this(startDate, null, title, text);
    }

    public SectionElement(LocalDate startDate, String title) {
        this(startDate, null, title, null);
    }

    public SectionElement(String title, String text) {
        this(null, null, title, text);
    }

    public SectionElement(String text) {
        this(null, null, null, text);
    }


    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (startDate != null) {
            sb.append(startDate + " - ");
            sb.append(endDate != null ? endDate : NOW_STRING);
            sb.append('\n');
        }

        if (title != null) {
            sb.append(title);
            sb.append('\n');
        }

        if (text != null) {
            sb.append(text);
            sb.append('\n');
        }

        if (sb.length() > 0)
            sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }
}
