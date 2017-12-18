package ru.javawebinar.basejava.storage;

import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.*;

import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public abstract class AbstractStorageTest {
    protected Storage storage;

    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String UUID_4 = "uuid4";

    private static final String FULL_NAME_1 = "fullName1";
    private static final String FULL_NAME_2 = "fullName2";
    private static final String FULL_NAME_3 = "fullName3";
    private static final String FULL_NAME_4 = "fullName4";

    private static final String PHONE_1 = "phone1";
    private static final String PHONE_2 = "phone2";
    private static final String PHONE_3 = "phone3";
    private static final String PHONE_4 = "phone4";

    private static final String OBJECTIVE_1 = "Objective text 1";
    private static final String OBJECTIVE_2 = "Objective text 2";
    private static final String OBJECTIVE_3 = "Objective text 3";
    private static final String OBJECTIVE_4 = "Objective text 4";
    
    private static final String PERSONAL_1 = "Personal text 1";
    private static final String PERSONAL_2 = "Personal text 2";
    private static final String PERSONAL_3 = "Personal text 3";
    private static final String PERSONAL_4 = "Personal text 4";

    private static final String[] ACHIEVEMENT_1 = {"Achievment 1"};
    private static final String[] ACHIEVEMENT_2 = {"Achievment 2"};
    private static final String[] ACHIEVEMENT_3 = {"Achievment 3"};
    private static final String[] ACHIEVEMENT_4 = {"Achievment 4"};

    private static final String[] QUALIFICATIONS_1 = {"Qualification 1"};
    private static final String[] QUALIFICATIONS_2 = {"Qualification 2"};
    private static final String[] QUALIFICATIONS_3 = {"Qualification 3"};
    private static final String[] QUALIFICATIONS_4 = {"Qualification 4"};

    private static final Position[] POSITION_IN_ORG_1 = {new Position(
            2014, Month.JANUARY,
            2015, Month.FEBRUARY,
            "Position title 1", "Position description 1")};
    private static final Position[] POSITION_IN_ORG_2 = {new Position(
            2015, Month.FEBRUARY, 
            2016, Month.MARCH,
            "Position title 2", "Position description 2")};
    private static final Position[] POSITION_IN_ORG_3 = {new Position(
            2016, Month.MARCH, 
            2017, Month.MAY,
            "Position title 3", "Position description 3")};
    private static final Position[] POSITION_IN_ORG_4 = {new Position(
            2017, Month.JUNE,
            2017, Month.OCTOBER,
            "Position title 4", "Position description 4")};

    
    private static final Position[] POSITION_IN_EDU_1 = {new Position(
            2014, Month.JANUARY,
            2015, Month.FEBRUARY,
            "Position title 1", null)};
    private static final Position[] POSITION_IN_EDU_2 = {new Position(
            2015, Month.FEBRUARY,
            2016, Month.MARCH,
            "Position title 2", null)};
    private static final Position[] POSITION_IN_EDU_3 = {new Position(
            2016, Month.MARCH,
            2017, Month.MAY,
            "Position title 3", null)};
    private static final Position[] POSITION_IN_EDU_4 = {new Position(
            2017, Month.JUNE,
            2017, Month.OCTOBER,
            "Position title 4", null)};

    private static final String ORGANIZATION_NAME_1 = "Organization name 1";
    private static final String ORGANIZATION_NAME_2 = "Organization name 2";
    private static final String ORGANIZATION_NAME_3 = "Organization name 3";
    private static final String ORGANIZATION_NAME_4 = "Organization name 4";

    private static final String ORGANIZATION_URL_1 = "https://www.name1.com";
    private static final String ORGANIZATION_URL_2 = "https://www.name2.com";
    private static final String ORGANIZATION_URL_3 = "https://www.name3.com";
    private static final String ORGANIZATION_URL_4 = "https://www.name4.com";

    private static final Organization[] EXPERIENCE_1 = {new Organization(
            ORGANIZATION_NAME_1, ORGANIZATION_URL_1,
            Arrays.asList(POSITION_IN_ORG_1))};
    private static final Organization[] EXPERIENCE_2 = {new Organization(
            ORGANIZATION_NAME_2, ORGANIZATION_URL_2,
            Arrays.asList(POSITION_IN_ORG_2))};
    private static final Organization[] EXPERIENCE_3 = {new Organization(
            ORGANIZATION_NAME_3, ORGANIZATION_URL_3,
            Arrays.asList(POSITION_IN_ORG_3))};
    private static final Organization[] EXPERIENCE_4 = {new Organization(
            ORGANIZATION_NAME_4, ORGANIZATION_URL_4,
            Arrays.asList(POSITION_IN_ORG_4))};

    private static final Organization[] EDUCATION_1 = {new Organization(
            ORGANIZATION_NAME_1, ORGANIZATION_URL_1,
            Arrays.asList(POSITION_IN_EDU_1))};
    private static final Organization[] EDUCATION_2 = {new Organization(
            ORGANIZATION_NAME_2, ORGANIZATION_URL_2,
            Arrays.asList(POSITION_IN_EDU_2))};
    private static final Organization[] EDUCATION_3 = {new Organization(
            ORGANIZATION_NAME_3, ORGANIZATION_URL_3,
            Arrays.asList(POSITION_IN_EDU_3))};
    private static final Organization[] EDUCATION_4 = {new Organization(
            ORGANIZATION_NAME_4, ORGANIZATION_URL_4,
            Arrays.asList(POSITION_IN_EDU_4))};


    private static final Resume RESUME_1;
    private static final Resume RESUME_2;
    private static final Resume RESUME_3;
    private static final Resume RESUME_4;

    static {
        RESUME_1 = new Resume(UUID_1, FULL_NAME_1);
        RESUME_2 = new Resume(UUID_2, FULL_NAME_2);
        RESUME_3 = new Resume(UUID_3, FULL_NAME_3);
        RESUME_4 = new Resume(UUID_4, FULL_NAME_4);
        
        RESUME_1.addContact(ContactType.PHONE, PHONE_1);
        RESUME_2.addContact(ContactType.PHONE, PHONE_2);
        RESUME_3.addContact(ContactType.PHONE, PHONE_3);
        RESUME_4.addContact(ContactType.PHONE, PHONE_4);

        RESUME_1.addSection(SectionType.PERSONAL, new TextSection(PERSONAL_1));
        RESUME_2.addSection(SectionType.PERSONAL, new TextSection(PERSONAL_2));
        RESUME_3.addSection(SectionType.PERSONAL, new TextSection(PERSONAL_3));
        RESUME_4.addSection(SectionType.PERSONAL, new TextSection(PERSONAL_4));

        RESUME_1.addSection(SectionType.OBJECTIVE, new TextSection(OBJECTIVE_1));
        RESUME_2.addSection(SectionType.OBJECTIVE, new TextSection(OBJECTIVE_2));
        RESUME_3.addSection(SectionType.OBJECTIVE, new TextSection(OBJECTIVE_3));
        RESUME_4.addSection(SectionType.OBJECTIVE, new TextSection(OBJECTIVE_4));

        RESUME_1.addSection(SectionType.ACHIEVEMENT, new ListSection(Arrays.asList(ACHIEVEMENT_1)));
        RESUME_2.addSection(SectionType.ACHIEVEMENT, new ListSection(Arrays.asList(ACHIEVEMENT_2)));
        RESUME_3.addSection(SectionType.ACHIEVEMENT, new ListSection(Arrays.asList(ACHIEVEMENT_3)));
        RESUME_4.addSection(SectionType.ACHIEVEMENT, new ListSection(Arrays.asList(ACHIEVEMENT_4)));

        RESUME_1.addSection(SectionType.QUALIFICATIONS, new ListSection(Arrays.asList(QUALIFICATIONS_1)));
        RESUME_2.addSection(SectionType.QUALIFICATIONS, new ListSection(Arrays.asList(QUALIFICATIONS_2)));
        RESUME_3.addSection(SectionType.QUALIFICATIONS, new ListSection(Arrays.asList(QUALIFICATIONS_3)));
        RESUME_4.addSection(SectionType.QUALIFICATIONS, new ListSection(Arrays.asList(QUALIFICATIONS_4)));

        RESUME_1.addSection(SectionType.EXPERIENCE, new OrganizationSection(Arrays.asList(EXPERIENCE_1)));
        RESUME_2.addSection(SectionType.EXPERIENCE, new OrganizationSection(Arrays.asList(EXPERIENCE_2)));
        RESUME_3.addSection(SectionType.EXPERIENCE, new OrganizationSection(Arrays.asList(EXPERIENCE_3)));
        RESUME_4.addSection(SectionType.EXPERIENCE, new OrganizationSection(Arrays.asList(EXPERIENCE_4)));

        RESUME_1.addSection(SectionType.EDUCATION, new OrganizationSection(Arrays.asList(EDUCATION_1)));
        RESUME_2.addSection(SectionType.EDUCATION, new OrganizationSection(Arrays.asList(EDUCATION_2)));
        RESUME_3.addSection(SectionType.EDUCATION, new OrganizationSection(Arrays.asList(EDUCATION_3)));
        RESUME_4.addSection(SectionType.EDUCATION, new OrganizationSection(Arrays.asList(EDUCATION_4)));
        
    }

    protected AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() throws Exception {
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);
    }

    @Test
    public void size() throws Exception {
        assertSize(3);
    }

    @Test
    public void clear() throws Exception {
        storage.clear();
        assertSize(0);
    }

    @Test
    public void update() throws Exception {
        Resume newResume = new Resume(UUID_1, "New Name");
        storage.update(newResume);
        assertTrue(newResume == storage.get(UUID_1));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() throws Exception {
        storage.get("dummy");
    }

    @Test
    public void getAllSorted() throws Exception {
        List<Resume> list = storage.getAllSorted();
        assertEquals(3, list.size());
        assertEquals(list, Arrays.asList(RESUME_1, RESUME_2, RESUME_3));
    }

    @Test
    public void save() throws Exception {
        storage.save(RESUME_4);
        assertSize(4);
        assertGet(RESUME_4);
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() throws Exception {
        storage.save(RESUME_1);
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() throws Exception {
        storage.delete(UUID_1);
        assertSize(2);
        storage.get(UUID_1);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() throws Exception {
        storage.delete("dummy");
    }

    @Test
    public void get() throws Exception {
        assertGet(RESUME_1);
        assertGet(RESUME_2);
        assertGet(RESUME_3);
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() throws Exception {
        storage.get("dummy");
    }

    private void assertGet(Resume r) {
        assertEquals(r, storage.get(r.getUuid()));
    }

    private void assertSize(int size) {
        assertEquals(size, storage.size());
    }
}