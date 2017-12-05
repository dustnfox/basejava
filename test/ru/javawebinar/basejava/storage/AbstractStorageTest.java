package ru.javawebinar.basejava.storage;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import static java.util.Comparator.comparing;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public abstract class AbstractStorageTest {
    final Storage storage;
    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String UUID_4 = "uuid4";
    private static final String UUID_NON_EXISTS = "dummy";
    private static final String NAME_1 = "name1";
    private static final String NAME_2 = "name2";
    private static final String NAME_3 = "name3";
    private static final String NAME_4 = "name4";
    private static final String NAME_NON_EXISTS = "dummy_name";
    private static final Resume RESUME_1 = new Resume(UUID_1, NAME_1);
    private static final Resume RESUME_2 = new Resume(UUID_2, NAME_2);
    private static final Resume RESUME_3 = new Resume(UUID_3, NAME_3);
    private static final Resume RESUME_4 = new Resume(UUID_4, NAME_4);
    private static final Comparator<Resume> RESUME_COMPARATOR_BY_FULLNAME = comparing(Resume::getFullName);

    AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    private boolean isListsSorted(List<Resume> list, Comparator<Resume> comparator) {
        if (list == null) {
            return false;
        } else {
            Iterator<Resume> listIterator = list.iterator();
            Resume prevResume = null;
            Resume currentResume;
            while (listIterator.hasNext()) {
                currentResume = listIterator.next();
                if (prevResume != null && comparator.compare(prevResume, currentResume) > 0)
                    return false;
                else
                    prevResume = currentResume;
            }
            return true;
        }
    }

    @Before
    public void setUp() throws Exception {
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);
    }

    @After
    public void clearDown() throws Exception {
        storage.clear();
    }

    @Test
    public void clear() throws Exception {
        storage.clear();
        Assert.assertEquals(0, storage.size());
    }

    @Test
    public void update() throws Exception {
        Resume rTest = new Resume(UUID_1, NAME_2);
        storage.update(rTest);
        assertTrue(rTest == storage.get(UUID_1));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() throws Exception {
        storage.update(new Resume(UUID_NON_EXISTS, NAME_NON_EXISTS));
    }

    @Test
    public void save() throws Exception {
        storage.save(RESUME_4);
        assertEquals(4, storage.size());
        storage.get(UUID_4);
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() throws ExistStorageException {
        storage.save(RESUME_1);
    }

    @Test
    public void get() throws Exception {
        assertEquals(UUID_1, RESUME_1.getUuid());
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() throws Exception {
        storage.get(UUID_NON_EXISTS);
    }

    @Test
    public void delete() throws Exception {
        storage.delete(UUID_3);
        assertEquals(2, storage.size());
        storage.get(UUID_1);
        storage.get(UUID_2);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() throws Exception {
        storage.delete(UUID_NON_EXISTS);
    }

    @Test
    public void getAll() throws Exception {
        storage.delete(UUID_1);
        storage.save(RESUME_1);
        List<Resume> rTestArray = storage.getAll();
        assertEquals(3, rTestArray.size());
        // Checking if we've got all three resumes
        for(Resume r : rTestArray) {
            storage.get(r.getUuid());
        }
        // Checking the order
        assertTrue(isListsSorted(rTestArray, RESUME_COMPARATOR_BY_FULLNAME));
    }

    @Test
    public void size() throws Exception {
        Assert.assertEquals(3, storage.size());
    }


}