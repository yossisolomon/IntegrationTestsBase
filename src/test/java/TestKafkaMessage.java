import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static com.google.common.truth.Truth.assertThat;
import static org.awaitility.Awaitility.with;

public class TestKafkaMessage {
    private static final Logger LOGGER = Logger.getLogger(TestKafkaMessage.class.getName());
    private static final String randomId = UUID.randomUUID().toString().substring(0, 5);
    private String topic = randomId + "_sharone";
    private ConsumerUtils consumerUtils;

    public TestKafkaMessage() {
    }

    @DisplayName("Produce message and consume")
    @Test
    void testSendingMessages() {
        consumerUtils = new ConsumerUtils(topic);
        Collection<String> expectedValue = Producer.sendMessages(topic);
        Collection<String> actualValue = new ArrayList<>();

        with()
                .pollDelay(200, TimeUnit.MILLISECONDS)
                .pollInterval(100, TimeUnit.MILLISECONDS)
                .atMost(Duration.ofMillis(10000))
                .untilAsserted(() -> {
                    consumerUtils.kafkaPoll(actualValue);
                    assertThat(actualValue).isEqualTo(expectedValue);
                });
    }
}
