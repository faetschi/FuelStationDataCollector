package at.fhtw.springbootapp;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendStartMessage(String customerId) {
        rabbitTemplate.convertAndSend("", "data-collection-jobs", customerId);
    }
}
