package at.fhtw.stationdatacollector;

import at.fhtw.stationdatacollector.config.RabbitMQConfig;
import at.fhtw.stationdatacollector.entity.ChargeEntity;
import at.fhtw.stationdatacollector.services.StationDataCollector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StationDataCollectorTests {

    @Mock
    RabbitTemplate rabbitTemplate;

    @Mock(name = "Db1")
    JdbcTemplate jdbcTemplate1;

    @Mock(name = "Db2")
    JdbcTemplate jdbcTemplate2;

    @Mock(name = "Db3")
    JdbcTemplate jdbcTemplate3;

    @InjectMocks
    StationDataCollector stationDataCollector;

    @BeforeEach
    void setUp() {
        reset(rabbitTemplate, jdbcTemplate1, jdbcTemplate2, jdbcTemplate3);
    }

    @Test
    void testReceiveCustomerData_Db1() {
        // Mock data
        Map<String, String> message = new HashMap<>();
        message.put("customerId", "1");
        message.put("dbUrl", "localhost:30011/stationdb");

        List<ChargeEntity> chargeEntities = Arrays.asList(
                new ChargeEntity(1, 10.5, 1), // id, kwh, customerId
                new ChargeEntity(2, 15.3, 1)
        );

        // Mock jdbcTemplate1 behavior
        when(jdbcTemplate1.query(anyString(), any(RowMapper.class), anyInt()))
                .thenReturn(chargeEntities);

        // Call method
        stationDataCollector.receiveCustomerData(message);

        // Verify rabbitTemplate behavior
        verify(rabbitTemplate).convertAndSend(
                eq(RabbitMQConfig.SPECIFIC_DATA_COLLECTION_RECEIVER_QUEUE),
                eq((Object) "sum:25.8,customerId:1,stationPort:30011")
        );
    }

    @Test
    void testReceiveCustomerData_Db2() {
        // Mock data for Db2
        Map<String, String> message = new HashMap<>();
        message.put("customerId", "2");
        message.put("dbUrl", "localhost:30012/stationdb");

        List<ChargeEntity> chargeEntities = Arrays.asList(
                new ChargeEntity(3, 20.5, 2),
                new ChargeEntity(4, 30.3, 2)
        );

        // Mock jdbcTemplate2 behavior for customerId 2
        when(jdbcTemplate2.query(anyString(), any(RowMapper.class), eq(2)))
                .thenReturn(chargeEntities);

        // Call method under test
        stationDataCollector.receiveCustomerData(message);

        // Verify rabbitTemplate behavior for Db2
        verify(rabbitTemplate).convertAndSend(
                eq(RabbitMQConfig.SPECIFIC_DATA_COLLECTION_RECEIVER_QUEUE),
                eq((Object) "sum:50.8,customerId:2,stationPort:30012")
        );
    }

    @Test
    void testReceiveCustomerData_InvalidCustomerId() {
        // Mock data with invalid customer ID
        Map<String, String> message = new HashMap<>();
        message.put("customerId", "invalid");
        message.put("dbUrl", "localhost:30012/stationdb");


        // Call method
        stationDataCollector.receiveCustomerData(message);

        // Verify error logging or no message sent to RabbitMQ
        verify(rabbitTemplate, never()).convertAndSend(anyString(), any(Object.class));
    }

}
