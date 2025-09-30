package com.example.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;


public class DataEntry {
    private final StringProperty key;
    private final StringProperty value;


    public DataEntry(String key, String value) {
        this.key = new SimpleStringProperty(key);
        this.value = new SimpleStringProperty(value);
    }


    public String getKey() {
        return key.get();
    }

    public void setKey(String key) {
        this.key.set(key);
    }


    public StringProperty keyProperty() {
        return    key;
}

    public String getValue() {
        return value.get();
    }

    public void setValue(String value) {
        this.value.set(value);
    }

    public StringProperty valueProperty() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataEntry dataEntry = (DataEntry) o;
        return Objects.equals(getKey(), dataEntry.getKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey());
    }

    @Override
    public String toString() {
        return "DataEntry{" + "key='" + getKey() + '\'' + ", value='" + getValue() + '\'' + '}';
    }
}