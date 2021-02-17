package org.bp.models;

public class StorageItemWrapper {
     StorageItem item;
     int size;


    public StorageItem getItem() {
        return item;
    }

    public void setItem(StorageItem item) {
        this.item = item;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "StorageItemData{" +
                "item=" + item +
                ", size=" + size +
                '}';
    }
}
