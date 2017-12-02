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
    Object getKeyByUuid(String uuid) {
        for(int i = 0; i < storage.size(); i++)
            if( uuid.equals( storage.get(i).getUuid() ) )
                return i;

        return -1;
    }

    // Check if the key is valid index for storage
    @Override
    boolean isKeyValid(Object key) {
        int index = (int)key;
        return index >= 0 && index < storage.size();
    }

    /**
     * Replaces Resume at given index with new Resume object
     *
     * @param key Index of the old Resume object.
     *
     * @param r New Resume instance
     */
    @Override
    void updateResume(Object key, Resume r) {
        storage.set((int)key, r);
    }

    /**
     * Saves given Resume object in array
     *
     * @param key Index given for capability reasons and will be ignored.
     *              Resume'll be saved at the end of the array.
     *
     * @param r Resume instance to save
     *
     */
    @Override
    void saveResume(Object key, Resume r) {
        storage.add(r);
    }

    /**
     * Retrieve Resume with given UUID from the storage
     *
     * @param uuid UUID to search
     *
     * @return Resume instance with given UUID or null is Resume
     * with such UUID wasn't found.
     */
    @Override
    Resume getResumeByUuid(String uuid) {
        int index = (int)getKeyByUuid(uuid);

        return index < 0 ? null : storage.get(index);
    }
    /**
     * Deletes Resume with given UUID
     *
     * @param uuid UUID to search
     *
     * @return Deleted Resume instance or null if Resume with
     * such UUID wasn't found.
     */
    @Override
    Resume deleteResumeByUuid(String uuid) {
        Resume resume = null;

        int index = (int)getKeyByUuid(uuid);
        if(index >= 0) {
            resume = storage.get(index);
            storage.remove(index);
        }

        return resume;
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
    public Resume[] getAll() {
        return storage.toArray( new Resume[storage.size()] );
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
