package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    // Size of the storage
    private static final int STORAGE_LIMIT = 10000;
    // The number of elements actually stored in array
    private int size = 0;
    // Storage for Resume objects
    private Resume[] storage = new Resume[STORAGE_LIMIT];

    /**
     * Returns index of the Resume with given UUID in the storage.
     * If doesn't exist returns -1
     *
     */
    private int getIndexOfResume(String uuid) {
        for(int i=0; i<size; i++) {
            if(storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Clear out the elements storage by filling it with nulls
     * and trim the size to zero.
     *
     */
    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    /**
     * Place new Resume obj at the end
     * of the previously stored objects succession.
     *
     */
    public void save(Resume r) {
        // Check if element already presents.
        if (getIndexOfResume(r.getUuid()) != -1) {
            System.out.printf("ERROR. Can't save. Resume with UUID [%s] already exists.\n",
                    r.getUuid());
            return;
        }
        // Check if we're running out of storage space.
        if (size == STORAGE_LIMIT) {
            System.out.printf("ERROR. Can't save. Storage's ran out of space.");
            return;
        }

        storage[size] = r;
        size++;
    }

    /**
     * Saves new Resume obj in place of the old one with the same ID.
     * If can't find such UUID prints error message.
     *
     * */
    public void update(Resume r) {
        int index = getIndexOfResume(r.getUuid());

        if (index == -1) {
            System.out.printf("ERROR. Can't update resume with UUID [%s]. " +
                    "No such element.\n", r.getUuid());
        } else {
            storage[index] = r;
        }

    }

    /**
     * Retrieve Resume object from ArrayStorage by UUID.
     *
     * @return Resume with given UUID or null if not found.
     */
    public Resume get(String uuid) {
        int index = getIndexOfResume(uuid);

        if (index == -1) {
            System.out.printf("ERROR. Can't find resume with UUID [%s]. " +
                    "No such element.\n", uuid);
            return null;
        }

        return storage[index];
    }

    /**
     * Delete Resume object by UUID
     * Print error message if not found.
     */
    public void delete(String uuid) {
        int index = getIndexOfResume(uuid);

        if(index == -1) {
            System.out.printf("ERROR. Can't delete resume with UUID [%s]. " +
                    "No such element.\n", uuid);
            return;
        }

        // Swap found element and the last one
        // to fill the gap
        storage[index] = storage[size-1];
        storage[size-1] = null;
        size--;
    }

    /**
     * Get all resumes from the storage.
     *
     * @return array, contains only Resumes in storage (without null)
     *
     */
    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    /**
     * @return The number of the Resumes in array.
     *
     */
    public int size() {
        return size;
    }

}
