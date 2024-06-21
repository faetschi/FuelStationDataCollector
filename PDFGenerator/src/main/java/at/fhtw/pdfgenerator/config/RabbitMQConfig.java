package at.fhtw.pdfgenerator.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String PDF_GENERATOR_QUEUE = "pdf-generator";

    @Bean
    public Queue pdfGeneratorQueue() {
        return new Queue(PDF_GENERATOR_QUEUE, false);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}