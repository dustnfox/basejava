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
        return 0;
    }

    @Override
    void updateResume(int intendedIndex, Resume r) {

    }

    @Override
    void saveResume(int intendedIndex, Resume r) {

    }

    @Override
    Resume getResume(int intendedIndex, String uuid) {
        return null;
    }

    @Override
    void deleteResume(int intendedIndex, String uuid) {

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
