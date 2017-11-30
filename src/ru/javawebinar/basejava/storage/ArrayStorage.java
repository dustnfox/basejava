package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

public class ArrayStorage extends AbstractArrayStorage {
    /**
     * @return Index of the Resume with given UUID in the storage.
     * If doesn't exist returns  -1.
     */
    @Override
    protected int getIndexOfResume(String uuid) {
        for(int i=0; i<size; i++) {
            if(storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }

        return -1;
    }
    /**
     * Saves given Resume at the end of the storage.
     */
    @Override
    protected void saveToIndex(int index, Resume r) {
        storage[size] = r;
    }
    /**
     * Substitutes the element at index with the last
     * element stored in the storage.
     */
    @Override
    protected void deleteByIndex(int index) {
        storage[index] = storage[size-1];
    }
}
