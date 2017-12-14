package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListSection implements Section {
    private final List<SectionElement> list;


    public ListSection(List<SectionElement> list) {
        super();
        Objects.requireNonNull(list, "section must not be null");
        this.list = new ArrayList<>(list);
    }

    @Override
    public List<SectionElement> getSection() {
        return new ArrayList<>(this.list);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (SectionElement e : list) {
            sb.append(e.toString());
            sb.append('\n');
            sb.append('\n');
        }
        if (sb.length() > 0)
            sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }
}
