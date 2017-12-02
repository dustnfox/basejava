package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

/**
 * Abstract class for common Storage interface implementation
 */
public abstract class AbstractStorage implements Storage {

    abstract Object getKeyByUuid(String uuid);

    abstract boolean isKeyValid(Object key);

    abstract void updateResume(Object key, Resume r);

    abstract void saveResume(Object key, Resume r);

    abstract Resume getResumeByUuid(String uuid);

    abstract Resume deleteResumeByUuid(String uuid);


    @Override
    public abstract void clear();
    /**
     * Saves new Resume obj in place of
     * the old one with the same UUID.
     *
     * @throws NotExistStorageException in not found
     *
     * */
    @Override
    public void update(Resume r) {
        Object key = getKeyByUuid(r.getUuid());

        if (!isKeyValid(key)) {
            throw new NotExistStorageException(r.getUuid());
        } else {
            updateResume(key, r);
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
        Object key = getKeyByUuid(r.getUuid());

        if (isKeyValid(key)) {
            throw new ExistStorageException(r.getUuid());
        }

        saveResume(key, r);
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
        Resume resume = getResumeByUuid(uuid);

        if (resume == null) {
            throw new NotExistStorageException(uuid);
        }

        return resume;
    }
    /**
     * Delete Resume object by UUID
     *
     * @throws NotExistStorageException if Resume with given UUID
     * isn't found in storage
     */
    @Override
    public void delete(String uuid) {
        if (deleteResumeByUuid(uuid) == null)
            throw new NotExistStorageException(uuid);
    }

    @Override
    public abstract Resume[] getAll();

    @Override
    public abstract int size();
}
