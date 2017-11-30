package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;
import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {
    /**
     * @return Index of the Resume with given UUID in the storage.
     * If doesn't exist returns (-(insertion point) - 1) The insertion point
     * is defined as the point at which the key would be inserted into the array:
     * the index of the first element in the range greater than the key,
     * or size if all elements in the storage are less than the specified key.
     */
    @Override
    protected int getIndexOfResume(String uuid) {
        Resume key = new Resume(uuid);

        return Arrays.binarySearch(storage, 0, size, key);
    }
    /**
     * Shifts elements with indexes from given
     * to the right by one element. Saves given Resume at index.
     */
    @Override
    protected void saveToIndex(int index, Resume r) {
        // Get real index of the first element greater than the argument.
        index = -(index + 1);
        System.arraycopy(storage, index, storage, index+1, size-index);
        storage[index] = r;
    }
    /**
     * Shifts elements with indexes greater than given
     * to the left by one element.
     */
    @Override
    protected void deleteByIndex(int index) {
        System.arraycopy(storage, index + 1, storage, index, size - index);
    }
}
