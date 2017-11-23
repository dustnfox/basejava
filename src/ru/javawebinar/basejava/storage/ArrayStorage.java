package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

public class ArrayStorage extends AbstractArrayStorage {
    /**
     * Returns index of the Resume with given UUID in the storage.
     * If doesn't exist returns -1
     *
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
     * Place new Resume obj at the end
     * of the previously stored objects succession.
     *
     */
    @Override
    public void save(Resume r) {
        // Check if element already presents.
        if (getIndexOfResume(r.getUuid()) != -1) {
            System.out.printf("ERROR. Can't save. Resume with UUID [%s] already exists.\n",
                    r.getUuid());
            return;
        }
        // Check if we're running out of storage space.
        if (size == STORAGE_LIMIT) {
            System.out.printf("ERROR. Can't save. Storage's ran out of space.");
            return;
        }

        storage[size] = r;
        size++;
    }
    /**
     * Delete Resume object by UUID
     * Print error message if not found.
     */
    @Override
    public void delete(String uuid) {
        int index = getIndexOfResume(uuid);

        if(index == -1) {
            System.out.printf("ERROR. Can't delete resume with UUID [%s]. " +
                    "No such element.\n", uuid);
            return;
        }

        // Swap found element and the last one
        // to fill the gap
        storage[index] = storage[size-1];
        storage[size-1] = null;
        size--;
    }
}
