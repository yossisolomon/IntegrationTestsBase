import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static com.google.common.truth.Truth.assertThat;
import static org.awaitility.Awaitility.with;

public class TestMessage implements ExtensionContext.Store.CloseableResource {
    private static final Logger LOGGER = Logger.getLogger(TestMessage.class.getName());
    private static final String randomId = UUID.randomUUID().toString();
    private KafkaConsumer<String, String> kafkaConsumer;
    private String topic;

    public TestMessage() {
        topic = "sharone_" + randomId;
        setupAndSubscribeConsumer();
    }

    private void setupAndSubscribeConsumer() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("group.id", "random_group_" + randomId);

        this.kafkaConsumer = new KafkaConsumer<>(properties);
        kafkaConsumer.subscribe(Collections.singletonList(topic), new AlwaysSeekToBeginListener<>(kafkaConsumer));
        LOGGER.info("Subscribing to topic " + topic);
    }

    void kafkaPoll(List<String> actualValue) {
        ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(0));
        kafkaConsumer.commitSync();
        for (ConsumerRecord<String, String> record : records) {
            actualValue.add(record.value());
            LOGGER.info(String.format("Topic - %s, Partition - %d, Value: %s", record.topic(), record.partition(), record.value()));
        }
    }

    @Test
    void testSendingMessages() {
        LOGGER.info("Sending to topic " + topic);
        List<String> expectedValue = Producer.sendMessages(topic);
        List<String> actualValue = new ArrayList<>();

        with()
                .pollDelay(200, TimeUnit.MILLISECONDS)
                .pollInterval(100, TimeUnit.MILLISECONDS)
                .atMost(Duration.ofMillis(10000))
                .untilAsserted(() -> {
                    kafkaPoll(actualValue);
                    assertThat(actualValue).isEqualTo(expectedValue);
                });

    }

    @Override
    public void close() {
        kafkaConsumer.close();
    }
}
