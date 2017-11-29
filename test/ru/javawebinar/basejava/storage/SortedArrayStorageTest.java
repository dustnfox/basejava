package ru.javawebinar.basejava.storage;

import static org.junit.Assert.*;

import org.junit.Test;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;


public class SortedArrayStorageTest extends AbstractArrayStorageTest{
    public SortedArrayStorageTest() {
        super(new SortedArrayStorage());
    }

    @Test
    public void saveOrder() throws Exception {
        final Resume r0 = new Resume("uuid0");
        final Resume r5 = new Resume("uuid5");

        storage.save(r0); // Save to the left end.
        storage.save(r5); // Save to the right end.
        storage.save(RESUME_4); // Save in the middle.

        Resume[] getAllArray = storage.getAll();
        Resume[] expectedArray = {
                r0,
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
        storage.save(RESUME_4);

        // Delete from the left end.
        storage.delete(UUID_1);
        Resume[] getAllArray = storage.getAll();
        Resume[] expectedArray = {
                RESUME_2,
                RESUME_3,
                RESUME_4
        };
        assertTrue(Arrays.equals(expectedArray, getAllArray));

        //Delete from the middle
        storage.delete(UUID_3);
        getAllArray = storage.getAll();
        expectedArray = new Resume[]{
                RESUME_2,
                RESUME_4
        };
        assertTrue(Arrays.equals(expectedArray, getAllArray));

        //Delete from the right end
        storage.delete(UUID_4);
        getAllArray = storage.getAll();
        expectedArray = new Resume[]{
                RESUME_2
        };
        assertTrue(Arrays.equals(expectedArray, getAllArray));
    }

}