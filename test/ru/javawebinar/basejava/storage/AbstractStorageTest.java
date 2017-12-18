package ru.javawebinar.basejava.storage;

import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.*;

import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public abstract class AbstractStorageTest {
    protected Storage storage;

    private static final String UUID = "uuid";
    private static final String FULL_NAME = "Full Name";
    private static final String PHONE = "+7910984";
    private static final String OBJECTIVE = "Objective text";
    private static final String PERSONAL = "Personal text";
    private static final String ACHIEVEMENT = "Achievement";
    private static final String QUALIFICATIONS = "Qualification";
    private static final String ORG_NAME = "Organization name";
    private static final String ORG_URL_PREFIX = "https://www.organization";
    private static final String ORG_URL_SUFFIX = ".com";
    private static final String EDU_NAME = "University";
    private static final String EDU_URL_PREFIX = "https://www.university";
    private static final String EDU_URL_SUFFIX = ".edu";
    private static final String POSITION_TITLE = "Position title";
    private static final String POSITION_DESCRIPTION = "Position description";
    private static final int POSITION_YEAR = 2000;

    private static final String UUID_1 = "uuid1";

    private static final Resume RESUME_1 = constructResume(1, 3);
    private static final Resume RESUME_2 = constructResume(2, 3);
    private static final Resume RESUME_3 = constructResume(3, 3);
    private static final Resume RESUME_4 = constructResume(4, 3);

    private static Resume constructResume(int rNumber, int listSizes) {
        Resume r = new Resume(UUID + rNumber, FULL_NAME + " " + rNumber);

        r.addContact(ContactType.PHONE, String.format("%s%04d", PHONE, rNumber));

        r.addSection(SectionType.PERSONAL, new TextSection(PERSONAL));
        r.addSection(SectionType.OBJECTIVE, new TextSection(OBJECTIVE));

        List<String> achievementList = new ArrayList<>(listSizes);
        for (int i = 1; i <= listSizes; i++) {
            achievementList.add(String.format("%s %d_%d", ACHIEVEMENT, rNumber, i));
        }
        r.addSection(SectionType.ACHIEVEMENT, new ListSection(achievementList));

        List<String> qualificationList = new ArrayList<>(listSizes);
        for (int i = 1; i <= listSizes; i++) {
            qualificationList.add(String.format("%s %d_%d", QUALIFICATIONS, rNumber, i));
        }
        r.addSection(SectionType.QUALIFICATIONS, new ListSection(qualificationList));

        List<Organization> organizationsList = new ArrayList<>(listSizes);
        for (int i = 1; i <= listSizes; i++) {
            organizationsList.add(
                    new Organization(
                            String.format("%s %d_%d", ORG_NAME, rNumber, i),
                            String.format("%s-%d-%d%s", ORG_URL_PREFIX, rNumber, i, ORG_URL_SUFFIX),
                            Arrays.asList(
                                    new Position(
                                            POSITION_YEAR + i, Month.JANUARY,
                                            POSITION_YEAR + 1 + i, Month.JULY,
                                            String.format("%s %d_%d", POSITION_TITLE, rNumber, i),
                                            String.format("%s %d_%d", POSITION_DESCRIPTION, rNumber, i)
                                    )
                            )
                    )
            );
        }
        r.addSection(SectionType.EXPERIENCE, new OrganizationSection(organizationsList));


        List<Organization> educationsList = new ArrayList<>(listSizes);
        for (int i = 1; i <= listSizes; i++) {
            educationsList.add(
                    new Organization(
                            String.format("%s %d_%d", EDU_NAME, rNumber, i),
                            String.format("%s-%d-%d%s", EDU_URL_PREFIX, rNumber, i, EDU_URL_SUFFIX),
                            Arrays.asList(
                                    new Position(
                                            POSITION_YEAR - i, Month.JANUARY,
                                            POSITION_YEAR + 1 - i, Month.JULY,
                                            String.format("%s %d_%d", POSITION_TITLE, rNumber, i),
                                            null
                                    )
                            )
                    )
            );
        }
        r.addSection(SectionType.EDUCATION, new OrganizationSection(educationsList));

        return r;
    }


    public AbstractStorageTest(Storage storage) {
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