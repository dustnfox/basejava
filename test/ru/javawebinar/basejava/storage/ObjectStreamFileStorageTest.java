package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.strategy.ObjectStreamStrategy;

public class ObjectStreamFileStorageTest extends AbstractStorageTest{

    public ObjectStreamFileStorageTest() {
        super(new StrategyFileStorage(STORAGE_DIR, new ObjectStreamStrategy()));
    }
}