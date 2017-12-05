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

    abstract Resume getResumeByKey(Object key);

    abstract void deleteResumeByKey(Object key);

    private Object getKey(String uuid, boolean shouldExist) {
        Object key = getKeyByUuid(uuid);

        if (isKeyValid(key) ^ shouldExist) {
            if(shouldExist)
                throw new NotExistStorageException(uuid);
            else
                throw new ExistStorageException(uuid);
        } else {
            return key;
        }
    }

    /**
     * Saves new Resume obj in place of
     * the old one with the same UUID.
     *
     * @throws NotExistStorageException in not found
     *
     * */
    @Override
    public void update(Resume r) {
        Object key = getKey(r.getUuid(), true);
        updateResume(key, r);
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
        Object key = getKey(r.getUuid(), false);
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
        Object key = getKey(uuid, true);
        return getResumeByKey(key);
    }
    /**
     * Delete Resume object by UUID
     *
     * @throws NotExistStorageException if Resume with given UUID
     * isn't found in storage
     */
    @Override
    public void delete(String uuid) {
        Object key = getKey(uuid, true);
        deleteResumeByKey(key);
    }
}
