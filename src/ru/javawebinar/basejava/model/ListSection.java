package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class ListSection<SE> implements Section<List<SE>> {
    private final List<SE> list;


    public ListSection(List<SE> list) {
        super();
        Objects.requireNonNull(list, "section must not be null");
        this.list = new ArrayList<>(list);
    }

    @Override
    public List<SE> getSectionInfo() {
        return new ArrayList<>(this.list);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (SE e : list) {
            sb.append(e.toString()).append('\n').append('\n');
        }
        if (sb.length() > 0)
            sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }
}
