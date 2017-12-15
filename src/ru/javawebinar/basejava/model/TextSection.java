package ru.javawebinar.basejava.model;

import java.util.Objects;

public class TextSection implements Section<String> {
    private final String text;

    public TextSection(String text) {
        super();
        Objects.requireNonNull(text, "text must not be null");
        this.text = text;
    }

    @Override
    public String getSectionInfo() {
        return text;
    }

    @Override
    public String toString() {
        return text;
    }
}
