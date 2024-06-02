package at.fhtw.datacollectiondispatcher;

import at.fhtw.datacollectiondispatcher.config.RabbitMQConfig;
import at.fhtw.datacollectiondispatcher.entity.Station;
import at.fhtw.datacollectiondispatcher.repository.StationRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataCollectionDispatcher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private StationRepository stationRepository;

    // listens to data collection jobs queue
    @RabbitListener(queues = RabbitMQConfig.DATA_COLLECTION_JOBS_QUEUE)
    public void receiveMessage(@Payload String customerId) {
        List<Station> stations = stationRepository.findAll();
        startDataGatheringJob(customerId, stations);
    }
    public void startDataGatheringJob(String customerId, List<Station> stations) {
        // send a message for every charging station to the Station Data Collector
        stations.forEach(station -> {
            // Create a map to hold the customerId and stationId
            Map<String, String> message = new HashMap<>();
            message.put("customerId", customerId);
            message.put("stationId", station.getId().toString());

            rabbitTemplate.convertAndSend(RabbitMQConfig.STATION_DATA_COLLECTOR_QUEUE, message);
        });

        // send a message to the Data Collection Receiver, that a new job started
        rabbitTemplate.convertAndSend(RabbitMQConfig.DATA_COLLECTION_RECEIVER_QUEUE, "New job started");

    }

}
