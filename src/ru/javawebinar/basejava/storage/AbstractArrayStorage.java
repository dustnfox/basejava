package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;
import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage implements Storage{
    // Size of the storage
    static final int STORAGE_LIMIT = 10000;
    // The number of elements actually stored in array
    int size = 0;
    // Storage for Resume objects
    final Resume[] storage = new Resume[STORAGE_LIMIT];

    protected abstract int getIndexOfResume(String uuid);

    public abstract void save(Resume r);

    public abstract void delete(String uuid);

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
     * Saves new Resume obj in place of the old one with the same UUID.
     * If can't find such UUID prints error message.
     *
     * */
    public void update(Resume r) {
        int index = getIndexOfResume(r.getUuid());

        if (index < 0) {
            System.out.printf("ERROR. Can't update resume with UUID [%s]. " +
                    "No such element.\n", r.getUuid());
        } else {
            storage[index] = r;
        }
    }
    /**
     * Retrieve Resume object from AbstractArrayStorage by UUID.
     *
     * @return Resume with given UUID or null if not found.
     */
    public Resume get(String uuid) {
        int index = getIndexOfResume(uuid);

        if (index < 0) {
            System.out.printf("ERROR. Can't find resume with UUID [%s]. " +
                    "No such element.\n", uuid);
            return null;
        }

        return storage[index];
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
