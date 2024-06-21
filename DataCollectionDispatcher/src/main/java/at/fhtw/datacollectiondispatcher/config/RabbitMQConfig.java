package at.fhtw.datacollectiondispatcher.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String DATA_COLLECTION_JOBS_QUEUE = "data-collection-jobs";
    public static final String STATION_DATA_COLLECTOR_QUEUE = "station-data-collector";
    public static final String DATA_COLLECTION_RECEIVER_QUEUE = "data-collection-receiver";

    @Bean
    public Queue dataCollectionJobsQueue() {
        return new Queue(DATA_COLLECTION_JOBS_QUEUE, false);
    }

    @Bean
    public Queue stationDataCollectorQueue() {
        return new Queue(STATION_DATA_COLLECTOR_QUEUE, false);
    }

    @Bean
    public Queue dataCollectionReceiverQueue() {
        return new Queue(DATA_COLLECTION_RECEIVER_QUEUE, false);
    }
}