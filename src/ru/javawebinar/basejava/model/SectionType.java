package ru.javawebinar.basejava.model;

public enum SectionType {
    PERSONAL("Личные качества"),
    OBJECTIVE("Позиция"),
    ACHIEVEMENT("Достижения"),
    QUALIFICATIONS("Квалификация"),
    EXPERIENCE("Опыт работы"),
    EDUCATION("Образование");

    private String title;

    SectionType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public boolean isMultiline() {
        return this != SectionType.PERSONAL && this != SectionType.OBJECTIVE;
    }

    public boolean hasDate() {
        return this == SectionType.EXPERIENCE || this == SectionType.EDUCATION;
    }
}

