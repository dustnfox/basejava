package ru.javawebinar.basejava.model;

import java.util.List;

public interface Section {

    String getText();

    List<String> getListOfStrings();

    List<SectionElement> getListOfElements();

}
