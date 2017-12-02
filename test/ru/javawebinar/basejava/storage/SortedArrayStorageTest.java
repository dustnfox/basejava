package ru.javawebinar.basejava.storage;

import static org.junit.Assert.*;

import org.junit.Test;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;


public class SortedArrayStorageTest extends AbstractArrayStorageTest{
    /**
     * SortedArrayStorage test
     */
    public SortedArrayStorageTest() {
        super(new SortedArrayStorage());
    }

    @Test
    public void saveOrder() throws Exception {
        final Resume r5 = new Resume("uuid5");

        storage.save(r5); // Save to the right end.
        storage.save(RESUME_4); // Save in the middle.

        Resume[] getAllArray = storage.getAll();
        Resume[] expectedArray = {
                RESUME_1,
                RESUME_2,
                RESUME_3,
                RESUME_4,
                r5
        };
        assertTrue(Arrays.equals(expectedArray, getAllArray));
    }

    @Test
    public void deleteOrder() throws Exception {
        //Delete from the middle
        storage.delete(UUID_2);
        Resume[] getAllArray = storage.getAll();
        Resume[] expectedArray = new Resume[]{
                RESUME_1,
                RESUME_3
        };
        assertTrue(Arrays.equals(expectedArray, getAllArray));
    }

}