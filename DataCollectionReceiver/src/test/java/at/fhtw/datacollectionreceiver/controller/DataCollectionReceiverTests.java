package at.fhtw.datacollectionreceiver.controller;

import at.fhtw.datacollectionreceiver.config.RabbitMQConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import static org.mockito.Mockito.*;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class DataCollectionReceiverTests {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private DataCollectionReceiver dataCollectionReceiver;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        dataCollectionReceiver = new DataCollectionReceiver();
        dataCollectionReceiver.rabbitTemplate = rabbitTemplate;
    }

    @Test
    void testHandleMessage_CompleteData() throws Exception {
        // Mock data
        String message1 = "sum:25.8,customerId:1,stationPort:30011";
        String message2 = "sum:30.2,customerId:1,stationPort:30012";
        String message3 = "sum:40.0,customerId:1,stationPort:30013";

        dataCollectionReceiver.handleMessage(message1);
        dataCollectionReceiver.handleMessage(message2);
        dataCollectionReceiver.handleMessage(message3);

        // Expected data map
        Map<Integer, Double> expectedDataMap = new HashMap<>();
        expectedDataMap.put(30011, 25.8);
        expectedDataMap.put(30012, 30.2);
        expectedDataMap.put(30013, 40.0);

        // Expected message map
        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("customerId", 1);
        messageMap.put("data", expectedDataMap);

        // Convert the message map to JSON
        String expectedMessage = objectMapper.writeValueAsString(messageMap);

        // Verify that the message was sent to the PDF Generator queue
        verify(rabbitTemplate).convertAndSend(RabbitMQConfig.PDF_GENERATOR_QUEUE, expectedMessage);
    }

    @Test
    void testHandleMessage_IncompleteData() {
        // Mock data
        String message1 = "sum:25.8,customerId:1,stationPort:30011";
        String message2 = "sum:30.2,customerId:1,stationPort:30012";

        dataCollectionReceiver.handleMessage(message1);
        dataCollectionReceiver.handleMessage(message2);

        // Verify that no message was sent to the PDF Generator queue
        verify(rabbitTemplate, never()).convertAndSend(anyString(), anyString());
    }

    @Test
    void testHandleMessage_InvalidMessage() {
        // Mock data with invalid message
        String invalidMessage = "sum:asdf,customerId:asdf,stationPart:999999";

        dataCollectionReceiver.handleMessage(invalidMessage);

        // Verify that no message was sent to the PDF Generator queue
        verify(rabbitTemplate, never()).convertAndSend(anyString(), anyString());
    }

}
