import jdk.nashorn.internal.objects.NativeUint8Array;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class Consumer {
    private static final Logger LOGGER = Logger.getLogger(Consumer.class.getName());

    public static String getKafkaAddress(){
        String KAFKA_ADDRESS = System.getenv("KAFKA_ADDRESS");
        String LOCALHOST = "localhost";
        if (KAFKA_ADDRESS == null){
            KAFKA_ADDRESS = LOCALHOST + ":9092";
        }

        LOGGER.info(String.format("kafka address is: %s", KAFKA_ADDRESS));
        return KAFKA_ADDRESS;
    }

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", getKafkaAddress());
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("group.id", "test-group");

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
        List<String> topics = new ArrayList<>();
        topics.add("sharone");
        kafkaConsumer.subscribe(topics);
        try {
            while (true) {
                ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(10));
                for (ConsumerRecord<String, String> record : records) {
                    LOGGER.info(String.format("Topic - %s, Partition - %d, Value: %s", record.topic(), record.partition(), record.value()));
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            kafkaConsumer.close();
        }
    }
}