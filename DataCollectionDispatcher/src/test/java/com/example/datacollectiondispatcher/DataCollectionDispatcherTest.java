package com.example.datacollectiondispatcher;

import at.fhtw.datacollectiondispatcher.config.RabbitMQConfig;
import at.fhtw.datacollectiondispatcher.controller.DataCollectionDispatcher;
import at.fhtw.datacollectiondispatcher.entity.Station;
import at.fhtw.datacollectiondispatcher.repository.StationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

class DataCollectionDispatcherTest {

    @Test
    void testReceiveMessage() {
        // Arrange
        RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);
        StationRepository stationRepository = mock(StationRepository.class);
        DataCollectionDispatcher dispatcher = new DataCollectionDispatcher(rabbitTemplate, stationRepository);

        Station station1 = new Station();
        station1.setDbUrl("localhost:30011");
        Station station2 = new Station();
        station2.setDbUrl("localhost:30012");
        List<Station> stations = Arrays.asList(station1, station2);

        when(stationRepository.findAll()).thenReturn(stations);

        String customerId = "1";

        // Act
        dispatcher.receiveMessage(customerId);

        // Assert
        verify(stationRepository, times(1)).findAll();
        verify(rabbitTemplate, times(1)).convertAndSend(RabbitMQConfig.STATION_DATA_COLLECTOR_QUEUE, Map.of("customerId", customerId, "dbUrl", station1.getDbUrl().toString()));
        verify(rabbitTemplate, times(1)).convertAndSend(RabbitMQConfig.STATION_DATA_COLLECTOR_QUEUE, Map.of("customerId", customerId, "dbUrl", station2.getDbUrl().toString()));
        verify(rabbitTemplate, times(1)).convertAndSend(RabbitMQConfig.DATA_COLLECTION_RECEIVER_QUEUE, "New job started");
    }

    @Test
    void contextLoads() {
    }
}