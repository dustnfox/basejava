package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.List;

// TODO refactoring
public interface Storage {

    void clear();

    void update(Resume r);

    void save(Resume r);

    Resume get(String uuid);

    void delete(String uuid);

    /**
     * @return list sorted by Resume fullName contains only Resumes in storage (without null)
     */
    List<Resume> getAllSorted();

    int size();
}

