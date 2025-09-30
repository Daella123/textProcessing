package com.example.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Map;


public class DataManager {
    private final Map<String, DataEntry> entryMap;
    private final ObservableList<DataEntry> observableList;


    public DataManager() {
        this.entryMap = new HashMap<>();
        this.observableList = FXCollections.observableArrayList();
    }


    public ObservableList<DataEntry> getObservableList() {
        return observableList;
    }


    public boolean addOrUpdateEntry(String key, String value) {
        if (key == null) {
            return false;
        }
        DataEntry existing = entryMap.get(key);
        if (existing != null) {
            existing.setValue(value);
        } else {
            DataEntry entry = new DataEntry(key, value);
            entryMap.put(key, entry);
            observableList.add(entry);
        }
        return true;
    }


    public boolean deleteEntry(String key) {
        DataEntry removed = entryMap.remove(key);
        if (removed != null) {
            observableList.remove(removed);
            return true;
        }
        return false;
    }
}