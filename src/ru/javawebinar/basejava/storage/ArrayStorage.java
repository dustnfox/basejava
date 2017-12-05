package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

public class ArrayStorage extends AbstractArrayStorage {
    /**
     * Saves given Resume at the end of the storage.
     *
     * @param index Index given for capability reasons and will be ignored.
     *              Resume'll be saved at the end of the array.
     * @param r Resume to save.
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
    protected void fillGapAtIndex(int index) {
        storage[index] = storage[size-1];
    }
    /**
     * Retrieve index of the Resume with given UUID.
     *
     * @param uuid UUID to search
     *
     * @return Index of the Resume with given UUID in the storage.
     * If doesn't exist returns  -1.
     */
    @Override
    protected Integer getKeyByUuid(String uuid) {
        for(int i=0; i<size; i++) {
            if(storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }

        return -1;
    }
}
