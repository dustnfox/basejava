package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TextSection implements Section {
    private final String text;

    public TextSection(String text) {
        super();
        Objects.requireNonNull(text, "text must not be null");
        this.text = text;
    }

    public List<SectionElement> getSection() {
        return new ArrayList<>(Arrays.asList(new SectionElement(text)));
    }

    @Override
    public String toString() {
        return text;
    }
}
