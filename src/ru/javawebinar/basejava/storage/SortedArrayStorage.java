package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

import static java.util.Comparator.comparing;

public class SortedArrayStorage extends AbstractArrayStorage {

    /**
     * Shifts elements with indexes from given index
     * to the right by one element. Saves given Resume at index.
     *
     * @param index Index of the cell in the array intended to store given Resume.
     *
     * @param r Resume instance to store.
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
     *
     * @param index Index of the gap in the array we want to cover.
     */
    @Override
    protected void fillGapAtIndex(int index) {
        System.arraycopy(storage, index + 1, storage, index, size - index);
    }

    /**
     * Retrieve index of the Resume with given UUID. Binary search algorithm
     * is used.
     *
     * @return Index of the Resume with given UUID in the storage.
     * If doesn't exist returns (-(insertion point) - 1) The insertion point
     * is defined as the point at which the key would be inserted into the array:
     * the index of the first element in the range greater than the key,
     * or size if all elements in the storage are less than the specified key.
     */
    @Override
    protected Integer getKeyByUuid(String uuid) {
        Resume key = new Resume(uuid);
        return Arrays.binarySearch(storage, 0, size, key, comparing(Resume::getUuid));
    }
}
