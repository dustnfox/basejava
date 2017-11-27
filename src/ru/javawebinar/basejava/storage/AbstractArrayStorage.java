package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage implements Storage{
    // Size of the storage
    private static final int STORAGE_LIMIT = 10000;
    // The number of elements actually stored in array
    int size = 0;
    // Storage for Resume objects
    final Resume[] storage = new Resume[STORAGE_LIMIT];

    protected abstract int getIndexOfResume(String uuid);

    protected abstract void saveToIndex(Resume r, int index);

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
    /**
     * Saves new Resume obj in place of the old one with the same UUID.
     * @throws NotExistStorageException in not found
     *
     * */
    public void update(Resume r) {
        int index = getIndexOfResume(r.getUuid());

        if (index < 0) {
            throw new NotExistStorageException(r.getUuid());
        } else {
            storage[index] = r;
        }
    }

    /**
     * Place new Resume obj at the end
     * of the previously stored objects succession.
     *
     * @throws ExistStorageException if Resume with the same UUID is already
     * in storage.
     *
     * @throws StorageException if has no enough space in the storage
     */
    @Override
    public void save(Resume r) {
        int index = getIndexOfResume(r.getUuid());
        // Check if element already presents.
        if (index >= 0) {
            throw new ExistStorageException(r.getUuid());
        }
        // Check if we're running out of storage space.
        if (size == STORAGE_LIMIT) {
            throw new StorageException("Storage overflow.", r.getUuid());
        }

        saveToIndex(r, index);
        size++;
    }
    /**
     * Retrieve Resume object from AbstractArrayStorage by UUID.
     *
     * @return Resume with given UUID or null if not found.
     *
     * @throws NotExistStorageException is Resume with given UUID
     * not found in storage
     */
    public Resume get(String uuid) {
        int index = getIndexOfResume(uuid);

        if (index < 0) {
            throw new NotExistStorageException(uuid);
        }

        return storage[index];
    }
    /**
     * Delete Resume object by UUID
     *
     * @throws NotExistStorageException if can't find Resume
     * with given UUID.
     */
    @Override
    public void delete(String uuid) {
        int index = getIndexOfResume(uuid);

        if (index < 0) {
            throw new NotExistStorageException(uuid);
        } else {
            deleteByIndex(index);
            storage[size-1] = null;
            size--;
        }
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
