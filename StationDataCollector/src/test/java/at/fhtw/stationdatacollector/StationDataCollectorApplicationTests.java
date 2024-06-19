package at.fhtw.stationdatacollector;

import at.fhtw.stationdatacollector.config.RabbitMQConfig;
import at.fhtw.stationdatacollector.entity.ChargeEntity;
import at.fhtw.stationdatacollector.services.StationDataCollector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.slf4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class StationDataCollectorApplicationTests {

    @Test
    void contextLoads() {
    }

}
