package org.bp.storage;

import org.bp.types.StorageItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ShopStorage {
    private static final ShopStorage instance;
    protected HashMap<Integer, StorageItem> items;
    protected HashMap<Integer, List<StorageItem>> transactions;
    protected int transactionCount = 0;


    private ShopStorage(){ }

    static{
        try{
            instance = new ShopStorage();
            instance.fillStorage();
        }catch(Exception e){
            throw new RuntimeException("Exception occured in creating singleton instance of ShopStorage. Exception :" + e.getMessage());
        }
    }

    private void fillStorage(){
        items = new HashMap<>();
        transactions = new HashMap<>();

        String[] names = new String[]{
                "Wool hat", "Jacket",
                "Shoes", "Gloves",
                "Scarf", "Sweater",
                "Hockey stick", "Ice skates"
        };
        float[] prices = new float[]{
                20.00f, 150.00f,
                250.99f, 34.99f,
                30.00f, 120.49f,
                180.99f, 340.00f
        };

        int id=0;
        Random random = new Random();
        for (String name: names) {
            StorageItem item = new StorageItem();
            item.setItemName(name);
            item.setItemId(id);
            item.setCount(random.nextInt(19)+1);
            item.setPrice(prices[id]);
            this.items.put(id, item);
            id += 1;
        }
    }

    public static ShopStorage getInstance(){
        return instance;
    }

    public List<StorageItem> getItems() {

        List<StorageItem> items = new ArrayList<StorageItem>();
        this.items.forEach((i, item) -> items.add(item));
        return items;
    }

    public int commitTransaction(List<StorageItem> transaction) throws Exception {
        for(StorageItem item: transaction) {
            StorageItem storageItem = this.items.get(item.getItemId());
            int count = storageItem.getCount() - item.getCount();
            storageItem.setCount(count);
        }

        transactionCount += 1;
        try {
            transactions.put(transactionCount, transaction);
        }
        catch (Exception e) {
            throw new Exception("Unable to insert: " + e.getMessage());
        }


        return transactionCount;
    }

    public synchronized int purchaseItems(List<StorageItem> items) throws Exception {
        List<StorageItem> transaction = new ArrayList<>();
        int tid;
        for(StorageItem item: items) {
            if(this.items.containsKey(item.getItemId())) {
                StorageItem storageItem = this.items.get(item.getItemId());
                int count = storageItem.getCount() - item.getCount();

                if(count < 0) {
                    throw new Exception("Not enough items in storage for  " +
                            storageItem.getItemName());
                }

                transaction.add(item);
            }
            else {
                throw new Exception("Item with an id: " + item.getItemId() + " does not exist.");
            }
        }
        tid = this.commitTransaction(transaction);
        return tid;
    }
    public Float getCost(List<StorageItem> items) throws Exception {
        float cost = 0.0f;

        for(StorageItem item: items){
            if(this.items.containsKey(item.getItemId())) {
                cost += this.items.get(item.getItemId()).getPrice()*item.getCount();
            }
            else {
                throw new Exception("Item with an id: " + item.getItemId() + " does not exist.");
            }
        }

        return cost;
    }

    public void returnItems(int transactionId) throws Exception {
        if(!transactions.containsKey(transactionId)) {
            throw  new Exception("Transaction with an id: " + transactionId + " does not exist or was already used.");
        }
        List<StorageItem> transaction = transactions.get(transactionId);
        for(StorageItem item: transaction) {
            if(this.items.containsKey(item.getItemId())) {
                StorageItem storageItem = this.items.get(item.getItemId());
                int new_count = storageItem.getCount() + item.getCount();
                storageItem.setCount(new_count);
            }
            else {
                throw new Exception("Item with an id: " + item.getItemId() + " does not exist.");
            }
        }
        transactions.remove(transactionId);
    }
}
