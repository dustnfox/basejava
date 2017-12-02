package ru.javawebinar.basejava.storage;


import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

/**
 *  Abstract class for common Array based storage for Resumes
 */
public abstract class AbstractArrayStorage extends AbstractStorage{
    // Size of the storage
    private static final int STORAGE_LIMIT = 10000;
    // The number of elements actually stored in array
    int size = 0;
    // Storage for Resume objects
    final Resume[] storage = new Resume[STORAGE_LIMIT];

    protected abstract void saveToIndex(int index, Resume r);

    protected abstract void fillGapAtIndex(int index);

    @Override
    protected abstract Object getKeyByUuid(String uuid);
    // Check if the key is valid index for storage
    @Override
    boolean isKeyValid(Object key) {
        int index = (int)key;
        return index >=0 && index < storage.length;
    }

    /**
     * Replaces Resume at given index with new Resume object
     *
     * @param key Index of the old Resume object.
     *
     * @param r New Resume instance
     */
    @Override
    void updateResume(Object key, Resume r) {
        storage[(int)key] = r;
    }

    /**
     * Saves given Resume object in array
     *
     * @param key Intended index of array at which Resume may be saved
     *
     * @param r Resume instance to save
     *
     * @throws StorageException If array's ran out of space.
     */
    @Override
    void saveResume(Object key, Resume r) {
        // Check if we're running out of storage space.
        if (size == STORAGE_LIMIT) {
            throw new StorageException("Storage overflow.", r.getUuid());
        }

        saveToIndex((int)key, r);
        size++;
    }

    /**
     * Retrieve Resume with given UUID from the storage
     *
     * @param uuid UUID to search
     *
     * @return Resume instance with given UUID or null is Resume
     * with such UUID wasn't found.
     */
    @Override
    Resume getResumeByUuid(String uuid) {
        int index = (int) getKeyByUuid(uuid);
        return (index >= 0) ? storage[index] : null;
    }

    /**
     * Deletes Resume with given UUID
     *
     * @param uuid UUID to search
     *
     * @return Deleted Resume instance or null if Resume with
     * such UUID wasn't found.
     */
    @Override
    Resume deleteResumeByUuid(String uuid) {
        Resume resume = null;

        int index = (int)getKeyByUuid(uuid);
        if(index >= 0) {
            resume = storage[index];
            fillGapAtIndex(index);
            storage[size - 1] = null;
            size--;
        }

        return resume;
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
