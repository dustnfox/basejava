package ru.javawebinar.basejava.storage;


import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage extends AbstractStorage implements Storage{
    // Size of the storage
    private static final int STORAGE_LIMIT = 10000;
    // The number of elements actually stored in array
    int size = 0;
    // Storage for Resume objects
    final Resume[] storage = new Resume[STORAGE_LIMIT];

    protected abstract int getIndexOfResume(String uuid);

    protected abstract void saveToIndex(int index, Resume r);

    protected abstract void deleteByIndex(int index);

    /**
     * Clear out the elements storage by filling it with nulls
     * and trim the size to zero.
     *
     */
    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    void updateResume(int intendedIndex, Resume r) {
        storage[intendedIndex] = r;
    }

    @Override
    void saveResume(int intendedIndex, Resume r) {
        // Check if we're running out of storage space.
        if (size == STORAGE_LIMIT) {
            throw new StorageException("Storage overflow.", r.getUuid());
        }

        saveToIndex(intendedIndex, r);
        size++;
    }

    @Override
    Resume getResume(int intendedIndex, String uuid) {
        return storage[intendedIndex];
    }

    @Override
    void deleteResume(int intendedIndex, String uuid) {
        deleteByIndex(intendedIndex);
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
