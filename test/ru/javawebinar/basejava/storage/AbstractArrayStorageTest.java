package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Test;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractArrayStorageTest extends AbstractStorageTest{

    public AbstractArrayStorageTest(Storage storage) {
        super(storage);
    }

    @Test(expected = StorageException.class)
    public void saveOverflow() throws Exception {
        try {
            for (int i = 4; i <= AbstractArrayStorage.STORAGE_LIMIT; i++) {
                storage.save(new Resume("uuid" + String.valueOf(i), "name" + String.valueOf(i)));
            }
        } catch (StorageException e) {
            Assert.fail();
        }
        storage.save(new Resume("uuid" + String.valueOf(AbstractArrayStorage.STORAGE_LIMIT + 1),
                "name" + String.valueOf(AbstractArrayStorage.STORAGE_LIMIT + 1)));
    }
}