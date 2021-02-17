package org.bp.models.shop;

import com.google.gson.annotations.SerializedName;

public class Response {
    @SerializedName("basketId")
    int basketId = -1;

    public int getBasketId() {
        return basketId;
    }

    public void setBasketId(int basketId) {
        this.basketId = basketId;
    }
}
