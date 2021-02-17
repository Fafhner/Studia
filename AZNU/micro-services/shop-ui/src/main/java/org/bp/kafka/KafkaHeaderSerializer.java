package org.bp.kafka;

import org.apache.camel.component.kafka.serde.DefaultKafkaHeaderSerializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class KafkaHeaderSerializer extends DefaultKafkaHeaderSerializer {
    @Override
    public byte[] serialize(String key, Object value) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        byte[] bytes;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(value);
            out.flush();
            bytes = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return super.serialize(key, value);
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }

        return bytes;
    }
}