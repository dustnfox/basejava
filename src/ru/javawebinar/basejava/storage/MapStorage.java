package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the Resume objects storage based on the List interface.
 *
 */
public class MapStorage extends AbstractStorage{
    final private Map<String, Resume> storage;

    public MapStorage() {
        this.storage = new HashMap<>();
    }

    public MapStorage(int initialSize) {
        this.storage = new HashMap<>(initialSize);
    }

    /**
     * Here just returns UUID argument. We'll check it for
     * existence later in isKeyValid method
     *
     * @param uuid UUID to search.
     * @return UUID argumen
     */
    @Override
    Object getKeyByUuid(String uuid) {
        return uuid;
    }

    /**
     * Checks if map has Resume stored with such key.
     * @param key UUID of the Resume to search.
     * @return true if map has such key and false if not.
     */
    @Override
    boolean isKeyValid(Object key) {
        return storage.containsKey((String)key);
    }

    @Override
    void updateResume(Object key, Resume r) {
        storage.replace((String)key, r);
    }

    @Override
    void saveResume(Object key, Resume r) {
        storage.put((String)key, r);
    }

    @Override
    Resume getResumeByUuid(String uuid) {
        return storage.get(uuid);
    }

    @Override
    Resume deleteResumeByUuid(String uuid) {
        return storage.remove(uuid);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public Resume[] getAll() {
        return storage.values().toArray( new Resume[storage.size()] );
    }

    @Override
    public int size() {
        return storage.size();
    }
}
