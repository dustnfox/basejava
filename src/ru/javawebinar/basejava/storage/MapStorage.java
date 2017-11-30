package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dustnfox on 29.11.2017.
 */
public class MapStorage extends AbstractStorage {
    final private Map<String, Resume> storage;

    public MapStorage() {
        this.storage = new HashMap<>();
    }

    public MapStorage(int initialSize) {
        this.storage = new HashMap<>(initialSize);
    }

    @Override
    int getIndexOfResume(String uuid) {

        return storage.containsKey(uuid) ? 1 : -1;
    }

    @Override
    void updateResume(int intendedIndex, Resume r) {
        storage.replace(r.getUuid(), r);
    }

    @Override
    void saveResume(int intendedIndex, Resume r) {
        storage.put(r.getUuid(), r);
    }

    @Override
    Resume getResume(int intendedIndex, String uuid) {
        return storage.get(uuid);
    }

    @Override
    void deleteResume(int intendedIndex, String uuid) {
        storage.remove(uuid);
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
