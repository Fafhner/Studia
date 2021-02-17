package org.bp.models.courier;

import com.google.gson.annotations.SerializedName;

public class CancelResponse {
    @SerializedName("courierId")
    int courierId;
    @SerializedName("text")
    String text;

    public int getCourierId() {
        return courierId;
    }

    public void setCourierId(int courierId) {
        this.courierId = courierId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
