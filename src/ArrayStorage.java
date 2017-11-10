import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    // Increase rate for new storage when we filled up the old one
    static private final double GROWTH_RATE = 1.5;
    // The number of elements actually stored in array
    private int size = 0;
    // Storage for Resume objects
    Resume[] storage = new Resume[10000];

    // Clear out the elements storage by filling it with nulls
    // and trim the size to zero.
    void clear() {
        Arrays.fill(storage, null);
        size = 0;
    }

    // Place new Resume obj at the end of the previously stored
    // objects succession.
    void save(Resume r) {
        if(size == storage.length) {
            storage = Arrays.copyOf(
                    storage,
                    (int)Math.ceil(storage.length*GROWTH_RATE)
            );
        }

        storage[size] = r;
        size++;
    }

    // Getting Resume by uuid
    Resume get(String uuid) {
        for(int i = 0; i < size; i++) {
            if(uuid.equals(storage[i].uuid)) {
                return storage[i];
            }
        }

        return null;
    }

    // Delete Resume object by UUID
    void delete(String uuid) {
        for(int i = 0; i < size; i++) {
            if(uuid.equals(storage[i].uuid)) {
                // Copy the last element in place
                // of the removed one to fill the gap
                storage[i] = storage[size-1];
                storage[size-1] = null;
                size--;
                return;
            }
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        Resume[] result = new Resume[size];
        System.arraycopy(storage,0, result, 0, size);

        return result;
    }

    // Get the number of the Resumes in array.
    int size() {

        return size;
    }
}
