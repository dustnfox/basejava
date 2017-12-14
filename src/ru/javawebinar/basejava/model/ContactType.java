package ru.javawebinar.basejava.model;

public enum ContactType {
    ADDRESS("Проживание"),
    PHONE("Телефон"),
    EMAIL("Электронная почта"),
    SKYPE("Skype"),
    GITHUB("Профиль на GitHub"),
    LINKEDIN("Профиль на LinkedIn"),
    STACKOVERFLOW("Профиль на Stackoverflow"),
    PERSONAL_SITE("Сайт");

    private String title;

    ContactType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}