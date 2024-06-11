package at.fhtw.stationdatacollector.config;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;

@Configuration
public class RabbitMQConfig {
    public static final String STATION_DATA_COLLECTOR_QUEUE = "station-data-collector";
    public static final String SPECIFIC_DATA_COLLECTION_RECEIVER_QUEUE = "specific-data-collection-receiver";

    @Bean
    public Queue stationDataCollectorQueue() {
        return new Queue(STATION_DATA_COLLECTOR_QUEUE, false);
    }

    @Bean
    public Queue specificDataCollectionReceiverQueue() {
        return new Queue(SPECIFIC_DATA_COLLECTION_RECEIVER_QUEUE, false);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}