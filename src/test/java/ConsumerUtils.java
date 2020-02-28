import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Logger;

public class ConsumerUtils implements ExtensionContext.Store.CloseableResource {
    private static final Logger LOGGER = Logger.getLogger(ConsumerUtils.class.getName());
    private static final String randomId = UUID.randomUUID().toString().substring(0, 5);
    private KafkaConsumer<String, String> kafkaConsumer;

    public ConsumerUtils(String topic) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("group.id", "random_group_" + randomId);

        kafkaConsumer = new KafkaConsumer<>(properties);
        kafkaConsumer.subscribe(Collections.singletonList(topic), new AlwaysSeekToBeginListener<>(kafkaConsumer));
        LOGGER.info("Subscribing to topic " + topic);
    }

    public void kafkaPoll(Collection<String> actualValue) {
        ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(0));
        kafkaConsumer.commitSync();
        for (ConsumerRecord<String, String> record : records) {
            actualValue.add(record.value());
            LOGGER.info(String.format(
                    "Topic - %s, Partition - %d, Value: %s", record.topic(), record.partition(), record.value()));
        }
    }

    @Override
    public void close() {
        kafkaConsumer.close();
    }
}
