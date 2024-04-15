package com.example.datacollectiondispatcher;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQReceiver {

    @RabbitListener(queues = "data-collection-jobs") // queue name
    public void receiveMessage(@Payload String customerId) {
        // - Starts the data gathering job
        startDataGatheringJob(customerId);

        // - Has knowledge about the available stations
            // get the list of available stations (you can retrieve it from the stations database)

        // - Sends a message for every charging station to the Station Data Collector
        sendMessageToStationDataCollector(customerId, "stationId1"); // Replace "stationId1" with actual station ID
        sendMessageToStationDataCollector(customerId, "stationId2");
        sendMessageToStationDataCollector(customerId, "stationId3");
    }

    private void startDataGatheringJob(String customerId) {
        // Logic to start the data gathering job
    }

    // - Sends a message to the Data Collection Receiver, that a new job started
    private void sendMessageToStationDataCollector(String customerId, String stationId) {
        // Logic to send a message to Station Data Collector
    }
}
