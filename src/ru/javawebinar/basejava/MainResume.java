package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class MainResume {
    private static final String DOUBLE_SPLITTER = "=============================================";
    private static final String SPLITTER = "---------------------------------------------";

    private static final String FULL_NAME = "Mikhail Rassadin";
    private static final String ADDRESS = "Ivanovo";
    private static final String EMAIL = "dustnfox@gmail.com";
    private static final String SKYPE = "dustnfox";

    private static final String PERSONAL_TEXT = "Some text about personal.";
    private static final String OBJECTIVE_TEXT = "Some text about objectives.";

    private static final String[] ACHIEVEMENTS = new String[]{
            "Achievement one.",
            "Achievement two.",
            "Achievement three."};
    private static final List<String> ACHIEVMENTS_LIST = Arrays.asList(ACHIEVEMENTS);

    private static final String[] QUALIFICATIONS = new String[]{
            "Qualification one.",
            "Qualification two.",
            "Qualification three."};
    private static final List<String> QUALIFICATION_LIST = Arrays.asList(QUALIFICATIONS);

    private static final SectionElement EXPERIENCE_ELEMENT_1 =
            new SectionElement(
                    LocalDate.of(2005, 10, 1),
                    LocalDate.of(2007, 11, 2),
                    "Company 1",
                    "Company one work description");

    private static final SectionElement EXPERIENCE_ELEMENT_2 =
            new SectionElement(
                    LocalDate.of(2007, 12, 4),
                    LocalDate.of(2010, 1, 5),
                    "Company 2",
                    "Company two work description");

    private static final SectionElement EXPERIENCE_ELEMENT_3 =
            new SectionElement(
                    LocalDate.of(2010, 2, 6),
                    "Company 2",
                    "Company two work description");

    private static final List<SectionElement> EXPERIENCE_LIST =
            Arrays.asList(EXPERIENCE_ELEMENT_1, EXPERIENCE_ELEMENT_2, EXPERIENCE_ELEMENT_3);

    private static final SectionElement EDUCATION_ELEMENT_1 =
            new SectionElement(
                    LocalDate.of(1999, 9, 1),
                    LocalDate.of(2004, 7, 2),
                    "University 1");

    private static final SectionElement EDUCATION_ELEMENT_2 =
            new SectionElement(
                    LocalDate.of(2004, 9, 7),
                    LocalDate.of(2005, 1, 15),
                    "University 2");

    private static final SectionElement EDUCATION_ELEMENT_3 =
            new SectionElement(
                    LocalDate.of(2011, 10, 21),
                    LocalDate.of(2012, 1, 1),
                    "University 2");

    private static final List<SectionElement> EDUCATION_LIST =
            Arrays.asList(EDUCATION_ELEMENT_1, EDUCATION_ELEMENT_2, EDUCATION_ELEMENT_3);

    public static void main(String[] args) {
        Resume r = initResumeWithStaticData();

        printResume(r);

    }

    private static Resume initResumeWithStaticData() {
        Resume r = new Resume(FULL_NAME);

        r.addContact(ContactType.ADDRESS, ADDRESS);
        r.addContact(ContactType.EMAIL, EMAIL);
        r.addContact(ContactType.SKYPE, SKYPE);

        r.addSection(SectionType.PERSONAL, new TextSection(PERSONAL_TEXT));
        r.addSection(SectionType.OBJECTIVE, new TextSection(OBJECTIVE_TEXT));

        r.addSection(SectionType.ACHIEVEMENT, new TextListSection(ACHIEVMENTS_LIST));
        r.addSection(SectionType.QUALIFICATIONS, new TextListSection(QUALIFICATION_LIST));

        r.addSection(SectionType.EXPERIENCE, new TimedListSection(EXPERIENCE_LIST));
        r.addSection(SectionType.EDUCATION, new TimedListSection(EDUCATION_LIST));

        return r;
    }

    private static void printResume(Resume r) {
        System.out.println(DOUBLE_SPLITTER);

        System.out.println(r.getFullName());

        System.out.println(SPLITTER);

        for (ContactType t : ContactType.values()) {
            String c = r.getContact(t);
            if (c != null)
                System.out.println(t.getTitle() + ": " + c);
        }

        System.out.println(SPLITTER);

        for (SectionType t : SectionType.values()) {
            System.out.println(t.getTitle() + '\n');
            Section s = r.getSection(t);
            if (s != null) {
                System.out.println(s);
                System.out.println(SPLITTER);
            }
        }
        System.out.println(DOUBLE_SPLITTER);
        System.out.println("\n");
    }
}
