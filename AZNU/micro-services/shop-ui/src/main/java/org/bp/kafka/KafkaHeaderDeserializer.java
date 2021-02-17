package org.bp.kafka;

import org.apache.camel.component.kafka.serde.DefaultKafkaHeaderDeserializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class KafkaHeaderDeserializer extends DefaultKafkaHeaderDeserializer {
    @Override
    public Object deserialize(String key, byte[] value) {
        ByteArrayInputStream in = new ByteArrayInputStream(value);
        ObjectInputStream is = null;
        try {
            is = new ObjectInputStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if(is != null) {
                return is.readObject();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return super.deserialize(key, value);
    }
}
