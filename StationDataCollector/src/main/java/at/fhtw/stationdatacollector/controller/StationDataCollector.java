package at.fhtw.stationdatacollector.controller;

import at.fhtw.stationdatacollector.config.DataSourceConfig;
import at.fhtw.stationdatacollector.config.RabbitMQConfig;
import at.fhtw.stationdatacollector.entity.ChargeEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class StationDataCollector {

    private final RabbitTemplate rabbitTemplate;
    private final JdbcTemplate jdbcTemplate1;
    private final JdbcTemplate jdbcTemplate2;
    private final JdbcTemplate jdbcTemplate3;

    @Autowired
    private DataSourceConfig dataSourceConfig;

    @Autowired
    public StationDataCollector(RabbitTemplate rabbitTemplate,
                                @Qualifier("Db1") JdbcTemplate jdbcTemplate1,
                                @Qualifier("Db2") JdbcTemplate jdbcTemplate2,
                                @Qualifier("Db3") JdbcTemplate jdbcTemplate3) {
        this.rabbitTemplate = rabbitTemplate;
        this.jdbcTemplate1 = jdbcTemplate1;
        this.jdbcTemplate2 = jdbcTemplate2;
        this.jdbcTemplate3 = jdbcTemplate3;
    }

    @RabbitListener(queues = RabbitMQConfig.STATION_DATA_COLLECTOR_QUEUE)
    public void receiveCustomerData(Map<String, String> message) {
        try {
            int customerId = Integer.parseInt(message.get("customerId"));
            String dbUrl = message.get("dbUrl");

            String sql = "SELECT id, kwh, customer_id FROM charge WHERE customer_id = ?";

            List<ChargeEntity> chargeEntities = new ArrayList<>();
            String stationPort = "";

            if (dbUrl.contains("30011")) {
                chargeEntities = jdbcTemplate1.query(sql, (ResultSet rs, int rowNum) -> new ChargeEntity(
                        rs.getInt("id"),
                        rs.getDouble("kwh"),
                        rs.getInt("customer_id")
                ), customerId);
                stationPort = "30011";
            } else if (dbUrl.contains("30012")) {
                chargeEntities = jdbcTemplate2.query(sql, (ResultSet rs, int rowNum) -> new ChargeEntity(
                        rs.getInt("id"),
                        rs.getDouble("kwh"),
                        rs.getInt("customer_id")
                ), customerId);
                stationPort = "30012";
            } else if (dbUrl.contains("30013")) {
                chargeEntities = jdbcTemplate3.query(sql, (ResultSet rs, int rowNum) -> new ChargeEntity(
                        rs.getInt("id"),
                        rs.getDouble("kwh"),
                        rs.getInt("customer_id")
                ), customerId);
                stationPort = "30013";
            } else {
                log.error("Invalid station port in dbUrl: {}", dbUrl);
                // Handle the error accordingly
            }

            double totalKwh = chargeEntities.stream()
                    .mapToDouble(ChargeEntity::getKwh)
                    .sum();

            String resultMessage = "sum:" + totalKwh + ",customerId:" + customerId + ",stationPort:" + stationPort;
            System.out.println(resultMessage);
            rabbitTemplate.convertAndSend(RabbitMQConfig.SPECIFIC_DATA_COLLECTION_RECEIVER_QUEUE, resultMessage);
        } catch (NumberFormatException e) {
            log.error("Invalid customerId in message: {}", message);
        }

    }

}