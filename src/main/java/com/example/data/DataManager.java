package com.example.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Map;

/**
 * The {@code DataManager} class encapsulates the collection of
 * {@link DataEntry} objects.  Internally it uses a {@link Map} to
 * guarantee uniqueness of keys and exposes an {@link ObservableList}
 * for easy integration with JavaFX controls like {@link
 * javafx.scene.control.TableView}.  Entries can be added, updated,
 * and removed via convenience methods.
 */
public class DataManager {
    private final Map<String, DataEntry> entryMap;
    private final ObservableList<DataEntry> observableList;

    /**
     * Constructs a new, empty data manager.
     */
    public DataManager() {
        this.entryMap = new HashMap<>();
        this.observableList = FXCollections.observableArrayList();
    }

    /**
     * Returns an observable list of all entries.  This list is
     * synchronized with the underlying map so that updates are
     * automatically reflected in UI components bound to it.
     *
     * @return the observable list of entries
     */
    public ObservableList<DataEntry> getObservableList() {
        return observableList;
    }

    /**
     * Adds a new entry or updates the value of an existing entry.  If
     * the key already exists, the value is replaced; otherwise a new
     * {@link DataEntry} is created and added to both the map and the
     * observable list.
     *
     * @param key   the key for the entry
     * @param value the value to associate with the key
     * @return {@code true} if the entry was added or updated, {@code false} if the key is null
     */
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

    /**
     * Removes an entry from the collection based on its key.
     *
     * @param key the key of the entry to remove
     * @return {@code true} if the entry existed and was removed; otherwise {@code false}
     */
    public boolean deleteEntry(String key) {
        DataEntry removed = entryMap.remove(key);
        if (removed != null) {
            observableList.remove(removed);
            return true;
        }
        return false;
    }
}