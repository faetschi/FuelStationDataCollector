package at.fhtw.stationdatacollector;

import at.fhtw.stationdatacollector.config.RabbitMQConfig;
import at.fhtw.stationdatacollector.entity.ChargeEntity;
import at.fhtw.stationdatacollector.services.StationDataCollector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class StationDataCollectorTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private JdbcTemplate jdbcTemplate1;

    @Mock
    private JdbcTemplate jdbcTemplate2;

    @Mock
    private JdbcTemplate jdbcTemplate3;

    @InjectMocks
    private StationDataCollector stationDataCollector;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void contextLoads() {
    }

    // TODO: not working yet
    @Test
    public void testReceiveCustomerData() {
        // Prepare the test data
        Map<String, String> message = new HashMap<>();
        message.put("customerId", "1");
        message.put("dbUrl", "localhost:30011");

        ChargeEntity chargeEntity1 = new ChargeEntity(1, 5.0, 1);
        ChargeEntity chargeEntity2 = new ChargeEntity(2, 10.0, 1);
        List<ChargeEntity> chargeEntities = List.of(chargeEntity1, chargeEntity2);

        // Mock the database query result
        when(jdbcTemplate1.query(anyString(), any(RowMapper.class), anyInt()))
                .thenReturn(chargeEntities);

        // Call the method
        stationDataCollector.receiveCustomerData(message);

        // Verify the database query was called
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<RowMapper> rowMapperCaptor = ArgumentCaptor.forClass(RowMapper.class);
        ArgumentCaptor<Integer> customerIdCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(jdbcTemplate1, times(1)).query(sqlCaptor.capture(), rowMapperCaptor.capture(), customerIdCaptor.capture());

        // Assert the captured arguments
        assertEquals("SELECT id, kwh, customer_id FROM charge WHERE customer_id = ?", sqlCaptor.getValue());
        assertNotNull(rowMapperCaptor.getValue());
        assertEquals(1, customerIdCaptor.getValue());

        // Verify the RabbitMQ message was sent
        String expectedMessage = "sum:15.0,customerId:1,stationPort:30011";
        verify(rabbitTemplate, times(1)).convertAndSend(RabbitMQConfig.SPECIFIC_DATA_COLLECTION_RECEIVER_QUEUE, expectedMessage);
    }

}
