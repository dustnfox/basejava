package ru.javawebinar.basejava.storage;

public class StrategyFileStorageTest extends AbstractStorageTest{

    public StrategyFileStorageTest() {
        super(new StrategyFileStorage(STORAGE_DIR, new ObjectStreamStrategy()));
    }
}