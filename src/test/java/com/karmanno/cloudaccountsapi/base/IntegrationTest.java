package com.karmanno.cloudaccountsapi.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@EmbeddedKafka
@ActiveProfiles("test")
public abstract class IntegrationTest {
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().port(8888));

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    public ObjectMapper objectMapper;

    @ClassRule
    public static EmbeddedKafkaRule embeddedKafkaRule = new EmbeddedKafkaRule(
            1, false, "CREATE_ACCOUNT"
    );

    public KafkaMessageListenerContainer<String, String> container;

    public BlockingQueue<ConsumerRecord<String, String>> records;

    @Before
    public void setup() {
        // set up the Kafka consumer properties
        Map<String, Object> consumerProperties =
                KafkaTestUtils.consumerProps("sender", "false",
                        embeddedKafkaRule.getEmbeddedKafka());

        // create a Kafka consumer factory
        DefaultKafkaConsumerFactory<String, String> consumerFactory =
                new DefaultKafkaConsumerFactory<>(
                        consumerProperties);

        // set the topic that needs to be consumed
        ContainerProperties containerProperties =
                new ContainerProperties("CREATE_ACCOUNT");

        // create a Kafka MessageListenerContainer
        container = new KafkaMessageListenerContainer<>(consumerFactory,
                containerProperties);

        // create a thread safe queue to store the received message
        records = new LinkedBlockingQueue<>();

        // setup a Kafka message listener
        container
                .setupMessageListener(new MessageListener<String, String>() {
                    @Override
                    public void onMessage(
                            ConsumerRecord<String, String> record) {
                        records.add(record);
                    }
                });

        // start the container and underlying message listener
        container.start();

        // wait until the container has the required number of assigned partitions
        ContainerTestUtils.waitForAssignment(container,
                embeddedKafkaRule.getEmbeddedKafka().getPartitionsPerTopic());
    }

    @After
    public void tearDown() {
        container.stop();
    }
}
