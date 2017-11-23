package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;
import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {
    /**
     * Returns index of the Resume with given UUID in the storage.
     * If doesn't exist returns (-(insertion point) - 1) The insertion point
     * is defined as the point at which the key would be inserted into the array:
     * the index of the first element in the range greater than the key,
     * or size if all elements in the storage are less than the specified key.
     *
     */
    @Override
    protected int getIndexOfResume(String uuid) {
        Resume key = new Resume();
        key.setUuid(uuid);

        return Arrays.binarySearch(storage, 0, size, key);
    }
    /**
     * Place new Resume obj in the appropriate place of the storage.
     * If storage already has one print error message.
     */
    @Override
    public void save(Resume r) {
        int index = getIndexOfResume(r.getUuid());
        // Check if element already presents.
        if (index >= 0) {
            System.out.printf("ERROR. Can't save. Resume with UUID [%s] already exists.\n",
                    r.getUuid());
            return;
        }
        // Check if we're running out of storage space.
        if (size == STORAGE_LIMIT) {
            System.out.printf("ERROR. Can't save. Storage's ran out of space.");
            return;
        }
        // Get real index of the first element greater than the argument.
        index = -(index + 1);
        System.arraycopy(storage, index, storage, index+1, size-index);
        storage[index] = r;
        size++;
    }
    /**
     * Delete Resume object by UUID
     * Print error message if not found.
     */
    @Override
    public void delete(String uuid) {
        int index = getIndexOfResume(uuid);

        if(index < 0) {
            System.out.printf("ERROR. Can't delete resume with UUID [%s]. " +
                    "No such element.\n", uuid);
        } else {
            System.arraycopy(storage, index+1, storage, index, size - index);
            storage[size] = null;
            size--;
        }
    }
}
