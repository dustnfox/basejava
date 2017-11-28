package ru.javawebinar.basejava.storage;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import static org.junit.Assert.*;

public abstract class AbstractArrayStorageTest {
    private Storage storage;
    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";

    AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() throws Exception {
        storage.save(new Resume(UUID_1));
        storage.save(new Resume(UUID_2));
        storage.save(new Resume(UUID_3));
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
        Resume rTest = new Resume(UUID_1);
        storage.update(rTest);
        assertTrue(rTest == storage.get(UUID_1));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() throws Exception {
        storage.update(new Resume("dummy"));
    }

    @Test
    public void save() throws Exception {
        storage.save(new Resume("UUID_4"));
        assertEquals(4, storage.size());
        storage.get("UUID_4");
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() throws ExistStorageException {
        storage.save(new Resume(UUID_1));
    }

    @Test
    public void get() throws Exception {
        Resume rTest = storage.get(UUID_1);
        assertEquals(UUID_1, rTest.getUuid());
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() throws Exception {
        storage.get("dummy");
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
        storage.delete("dummy");
    }

    @Test
    public void getAll() throws Exception {
        Resume[] rTestArray = storage.getAll();
        assertEquals(3, rTestArray.length);
        for(Resume r : rTestArray) {
            storage.get(r.getUuid());
        }
    }

    @Test
    public void size() throws Exception {
        Assert.assertEquals(3, storage.size());
    }

    @Test(expected = StorageException.class)
    public void sizeOverflow() throws Exception {
        for(int i = 0; i < 100001 - 3; i++) {
            storage.save(new Resume(String.valueOf(i)));
        }
    }
}