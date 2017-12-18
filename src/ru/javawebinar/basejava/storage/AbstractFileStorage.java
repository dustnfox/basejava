package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.basejava.util.FileUtil.getFilesInDirectory;

/**
 * gkislin
 * 22.07.2016
 */
public abstract class AbstractFileStorage extends AbstractStorage<File> {
    private File directory;

    protected AbstractFileStorage(File directory) {
        Objects.requireNonNull(directory, "directory must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writable");
        }
        this.directory = directory;
    }

    @Override
    public void clear() {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File f : getFilesInDirectory(directory)) {
                if (!f.delete()) {
                    throw new StorageException("IO error", f.getName());
                }
            }
        }
    }

    @Override
    public int size() {
        int count = 0;
        String[] listOfFiles = directory.list();
        if (listOfFiles != null)
            count = listOfFiles.length;
        return count;
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected void doUpdate(Resume r, File file) {
        try {
            doWrite(r, file);
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

    @Override
    protected void doSave(Resume r, File file) {
        try {
            if (!file.createNewFile()) {
                throw new StorageException("IO error", file.getName());
            }
            doWrite(r, file);
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    protected abstract void doWrite(Resume r, File file) throws IOException;

    protected abstract Resume doRead(File file) throws IOException;

    @Override
    protected Resume doGet(File file) {
        try {
            return doRead(file);
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    protected void doDelete(File file) {
        boolean isSuccessful = file.delete();
        if (!isSuccessful) {
            throw new StorageException("can't delete file", file.getName());
        }
    }

    @Override
    protected List<Resume> doCopyAll() {
        ArrayList<Resume> copy = new ArrayList<>(size());
        File[] files = directory.listFiles();

        if (files != null) {
            for (File f : files) {
                copy.add(doGet(f));
            }
        }

        return copy;
    }
}
