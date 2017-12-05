package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the Resume objects storage based on the List interface.
 *
 */
public class ListStorage extends AbstractStorage {
    final private List<Resume> storage;

    public ListStorage() {
        this.storage = new ArrayList<>();
    }

    public ListStorage(int initialSize) {
        this.storage = new ArrayList<>(initialSize);
    }

    /**
     * Retrieves Resume with given UUID from the storage.
     *
     * @param uuid UUID to search.
     * @return Position of the Resume of -1 if Resume with such UUID
     * was not found
     */
    @Override
    Integer getKeyByUuid(String uuid) {
        for(int i = 0; i < storage.size(); i++)
            if( uuid.equals( storage.get(i).getUuid() ) )
                return i;

        return -1;
    }

    // Check if the key is valid index for storage
    @Override
    boolean isKeyValid(Object index) {
        int ind = (int)index;
        return ind >= 0 && ind < storage.size();
    }

    /**
     * Replaces Resume at given index with new Resume object
     *
     * @param index Index of the old Resume object.
     *
     * @param r New Resume instance
     */
    @Override
    void updateResume(Object index, Resume r) {
        storage.set((Integer)index, r);
    }

    /**
     * Saves given Resume object in array
     *
     * @param index Index given for capability reasons and will be ignored.
     *              Resume'll be saved at the end of the array.
     *
     * @param r Resume instance to save
     *
     */
    @Override
    void saveResume(Object index, Resume r) {
        storage.add(r);
    }

    /**
     * Retrieve Resume with given UUID from the storage
     * @return Resume instance at the given index.
     */
    @Override
    Resume getResumeByKey(Object index) {
        return storage.get((Integer)index);
    }
    /**
     * Deletes Resume with given UUID
     *
     * @param index Index of Resume to remove.
     */
    @Override
    void deleteResumeByKey(Object index) {
        storage.remove((int)index);
    }

    /**
     * Clear out the elements storage
     */
    @Override
    public void clear() {
        storage.clear();
    }

    /**
     * Get all resumes from the storage.
     *
     * @return array, contains only Resumes in storage (without null)
     *
     */
    @Override
    public Resume[] getArray() {
        return storage.toArray(new Resume[storage.size()]);
    }

    /**
     * @return The number of the Resumes in array.
     *
     */
    @Override
    public int size() {
        return storage.size();
    }
}
