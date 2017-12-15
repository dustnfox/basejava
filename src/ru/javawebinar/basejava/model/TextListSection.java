package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.List;

public class TextListSection implements Section {
    private List<String> list;

    public TextListSection(List<String> list) {
            this.list = new ArrayList<>(list);
    }

    @Override
    public String getText() {
        return null;
    }

    @Override
    public List<String> getListOfStrings() {
        return new ArrayList<>(list);
    }

    @Override
    public List<SectionElement> getListOfElements() {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (String e : list) {
            sb.append(e.toString()).append('\n').append('\n');
        }
        if (sb.length() > 0)
            sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }
}
