package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.strategy.ObjectStreamStrategy;

public class ObjectStreamPathStorageTest extends AbstractStorageTest{

    public ObjectStreamPathStorageTest() {
        super( new StrategyPathStorage(STORAGE_DIR_STRING, new ObjectStreamStrategy()));
    }

}