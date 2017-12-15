package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.List;

public class TimedListSection implements Section{
    private List<SectionElement> list;

    public TimedListSection(List<SectionElement> list) {
        this.list = new ArrayList<>(list);
    }

    @Override
    public String getText() {
        return null;
    }

    @Override
    public List<String> getListOfStrings() {
        return null;
    }

    @Override
    public List<SectionElement> getListOfElements() {
        return new ArrayList<>(list);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (SectionElement e : list) {
            sb.append(e.toString()).append('\n').append('\n');
        }
        if (sb.length() > 0)
            sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }
}
