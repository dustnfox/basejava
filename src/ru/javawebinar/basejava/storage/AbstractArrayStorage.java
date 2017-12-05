package ru.javawebinar.basejava.storage;


import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

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

    // Check if the key is valid index for storage
    @Override
    boolean isKeyValid(Object index) {
        Integer ind = (Integer)index;
        return ind >=0 && ind < storage.length;
    }

    /**
     * Replaces Resume at given index with new Resume object
     *
     * @param index Index of the old Resume object.
     *
     * @param r New Resume instance
     */
    @Override
    void updateResume(Object index, Resume r) {
        storage[(int)index] = r;
    }

    /**
     * Saves given Resume object in array
     *
     * @param index Intended index of array at which Resume may be saved
     *
     * @param r Resume instance to save
     *
     * @throws StorageException If array's ran out of space.
     */
    @Override
    void saveResume(Object index, Resume r) {
        // Check if we're running out of storage space.
        if (size == STORAGE_LIMIT) {
            throw new StorageException("Storage overflow.", r.getUuid());
        }

        saveToIndex((Integer)index, r);
        size++;
    }

    /**
     * Retrieve Resume with given index (key) from the storage
     * @return Resume instance stored at given index.
     */
    @Override
    Resume getResumeByKey(Object index) {
        return storage[(Integer)index];
    }

    /**
     * Deletes Resume with given index (key)
     */
    @Override
    void deleteResumeByKey(Object index) {
        Integer ind = (Integer)index;
        fillGapAtIndex(ind);
        storage[size - 1] = null;
        size--;
    }

    @Override
    protected Resume[] getArray() {
        return Arrays.copyOfRange(storage, 0, size);
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
     * @return The number of the Resumes in array.
     *
     */
    public int size() {
        return size;
    }

}
