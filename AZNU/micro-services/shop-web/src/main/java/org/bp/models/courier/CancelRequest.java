package org.bp.models.courier;

import com.google.gson.annotations.SerializedName;

public class CancelRequest {
    @SerializedName("courierId")
    int courierId;

    public int getCourierId() {
        return courierId;
    }

    public void setCourierId(int courierId) {
        this.courierId = courierId;
    }
}
