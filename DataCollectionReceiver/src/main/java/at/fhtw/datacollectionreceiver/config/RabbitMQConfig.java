package at.fhtw.datacollectionreceiver.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String DATA_COLLECTION_RECEIVER_QUEUE = "data-collection-receiver";
    public static final String SPECIFIC_DATA_COLLECTION_RECEIVER_QUEUE = "specific-data-collection-receiver";
    public static final String PDF_GENERATOR_QUEUE = "pdf-generator";

    @Bean
    public Queue dataCollectionReceiverQueue() {
        return new Queue(DATA_COLLECTION_RECEIVER_QUEUE, false);
    }

    @Bean
    public Queue specificDataCollectionReceiverQueue() {
        return new Queue(SPECIFIC_DATA_COLLECTION_RECEIVER_QUEUE, false);
    }

    @Bean
    public Queue pdfGeneratorQueue() {
        return new Queue(PDF_GENERATOR_QUEUE, false);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}