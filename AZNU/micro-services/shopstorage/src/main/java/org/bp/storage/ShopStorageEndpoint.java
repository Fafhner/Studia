package org.bp.storage;

import org.bp.*;
import org.bp.types.StorageInfo;


@org.springframework.stereotype.Service
public class ShopStorageEndpoint implements ItemsPurchasing {

    @Override
    public StorageInfo purchaseItems(PurchaseItemsRequest payload) throws StorageFaultMsg {
        int transactionId;
        try {
            System.out.println("Got " + payload.getItems().size() + " items for person: "
                    + payload.getPerson().getFirstName() + " " + payload.getPerson().getLastName());

            transactionId = ShopStorage.getInstance().purchaseItems(payload.getItems());
        }
        catch (Exception e) {
            org.bp.types.Fault storageFault = new org.bp.types.Fault();
            throw new StorageFaultMsg("Unable to generating id: " + e.getMessage(), storageFault);
        }
        StorageInfo response = new StorageInfo();

        response.setId(transactionId);
        float cost;
        try{
            cost = ShopStorage.getInstance().getCost(payload.getItems());
        }
        catch (Exception e) {
            org.bp.types.Fault storageFault = new org.bp.types.Fault();
            throw new StorageFaultMsg("Unable to get cost: " + e.getMessage(), storageFault);
        }
        response.setCost(cost);
        return response;
    }

    @Override
    public CancelPurchasingResponse cancelPurchasing(CancelPurchasingRequest payload) throws StorageFaultMsg {
        try {
        ShopStorage.getInstance().returnItems(payload.getPurchaseId());
        }
        catch (Exception e) {
            org.bp.types.Fault storageFault = new org.bp.types.Fault();
            throw new StorageFaultMsg("Unable to return products: " + e.getMessage(), storageFault);
        }
        CancelPurchasingResponse response = new CancelPurchasingResponse();
        response.setCanceled(true);
        return response;
    }

    @Override
    public GetItemsResponse getItems(Object payload) {
        GetItemsResponse response = new GetItemsResponse();
        response.getItems().addAll(ShopStorage.getInstance().getItems());
        return response;
    }
}
