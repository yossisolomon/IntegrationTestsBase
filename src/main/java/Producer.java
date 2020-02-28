import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class Producer {
    private static final Logger LOGGER = Logger.getLogger(Producer.class.getName());

    public static List<String> sendMessages(String topic) {
        LOGGER.info("Sending to topic " + topic);

        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);
        List<String> messages = new ArrayList<>();
        try {
            for (int i = 0; i < 10; i++) {
                String value = "sharone " + i;
                messages.add(value);
                LOGGER.info("Sending message " + value);
                kafkaProducer.send(new ProducerRecord<>(topic, Integer.toString(i), value)).get();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            kafkaProducer.close();
        }

        return messages;
    }

    public static void main(String[] args) {
        sendMessages("sharone");
    }
}