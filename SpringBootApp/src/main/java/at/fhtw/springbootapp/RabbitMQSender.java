package at.fhtw.springbootapp;

import at.fhtw.springbootapp.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQSender {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMQSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendStartMessage(String customerId) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.DATA_COLLECTION_JOBS_QUEUE, customerId);
    }
}
