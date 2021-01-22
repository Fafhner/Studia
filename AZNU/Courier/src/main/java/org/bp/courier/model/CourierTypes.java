package org.bp.courier.model;

import java.util.ArrayList;
import java.util.List;

public class CourierTypes {
    private static CourierTypes instance = new CourierTypes();
    private List<String> types;

    private CourierTypes() {
        types = new ArrayList<>();
        types.add("courier");
        types.add("locker");
    }

    public static CourierTypes getInstance(){
        if (instance == null)
            instance = new CourierTypes();

        return instance;
    }
    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }
}
