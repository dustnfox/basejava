package ru.javawebinar.basejava.model;

import java.util.List;
import java.util.Objects;

public class TextSection implements Section {
    private final String text;

    public TextSection(String text) {
        super();
        Objects.requireNonNull(text, "text must not be null");
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public List<String> getListOfStrings() {
        return null;
    }

    @Override
    public List<SectionElement> getListOfElements() {
        return null;
    }
}
