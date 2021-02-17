package org.bp.courier.model;

import java.util.ArrayList;
import java.util.List;

public class CourierGetTypesResponse {
    List<String> types;

    public CourierGetTypesResponse() {
        types = new ArrayList<>();
        types.addAll(CourierTypes.getInstance().getTypes());
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }
}
