package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

/**
 * Abstract class for common Storage interface implementation
 */
public abstract class AbstractStorage implements Storage {

    abstract int getIndexOfResume(String uuid);

    abstract void updateResume(int intendedIndex, Resume r);

    abstract void saveResume(int intendedIndex, Resume r);

    abstract Resume getResume(int intendedIndex, String uuid);

    abstract void deleteResume(int intendedIndex, String uuid);

    @Override
    public abstract void clear();
    /**
     * Saves new Resume obj in place of the old one with the same UUID.
     * @throws NotExistStorageException in not found
     *
     * */
    @Override
    public void update(Resume r) {
        int index = getIndexOfResume(r.getUuid());

        if (index < 0) {
            throw new NotExistStorageException(r.getUuid());
        } else {
            updateResume(index, r);
        }
    }
    /**
     * Place new Resume obj in the storage
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

        saveResume(index, r);
    }
    /**
     * Retrieve Resume object from Storage by UUID.
     *
     * @return Resume with given UUID or null if not found.
     *
     * @throws NotExistStorageException if Resume with given UUID
     * isn't found in storage
     */
    @Override
    public Resume get(String uuid) {
        int index = getIndexOfResume(uuid);

        if (index < 0) {
            throw new NotExistStorageException(uuid);
        }

        return getResume(index, uuid);
    }
    /**
     * Delete Resume object by UUID
     *
     * @throws NotExistStorageException if Resume with given UUID
     * isn't found in storage
     */
    @Override
    public void delete(String uuid) {
        int index = getIndexOfResume(uuid);

        if (index < 0) {
            throw new NotExistStorageException(uuid);
        } else {
            deleteResume(index, uuid);
        }
    }

    @Override
    public abstract Resume[] getAll();

    @Override
    public abstract int size();
}
