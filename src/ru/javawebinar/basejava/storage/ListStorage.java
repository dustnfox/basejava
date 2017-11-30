package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dustnfox on 29.11.2017.
 */
public class ListStorage extends AbstractStorage {
    final private List<Resume> storage;

    public ListStorage() {
        this.storage = new ArrayList<>();
    }

    public ListStorage(int initialSize) {
        this.storage = new ArrayList<>(initialSize);
    }

    @Override
    int getIndexOfResume(String uuid) {
        return storage.indexOf(new Resume(uuid));
    }

    @Override
    void updateResume(int intendedIndex, Resume r) {
        storage.set(intendedIndex, r);
    }

    @Override
    void saveResume(int intendedIndex, Resume r) {
        storage.add(r);
    }

    @Override
    Resume getResume(int intendedIndex, String uuid) {
        return storage.get(intendedIndex);
    }

    @Override
    void deleteResume(int intendedIndex, String uuid) {
        storage.remove(intendedIndex);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public Resume[] getAll() {
        return storage.toArray( new Resume[storage.size()] );
    }

    @Override
    public int size() {
        return storage.size();
    }
}
