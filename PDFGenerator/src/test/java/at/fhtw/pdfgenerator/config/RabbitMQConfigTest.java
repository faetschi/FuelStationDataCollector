package at.fhtw.pdfgenerator.config;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RabbitMQConfigTest {

    private RabbitMQConfig rabbitMQConfig = new RabbitMQConfig();

    @Test
    public void testPdfGeneratorQueue() {
        Queue queue = rabbitMQConfig.pdfGeneratorQueue();
        assertNotNull(queue);
        Mockito.verifyNoInteractions(queue);
    }

    @Test
    public void testJackson2JsonMessageConverter() {
        Jackson2JsonMessageConverter converter = rabbitMQConfig.jackson2JsonMessageConverter();
        assertNotNull(converter);
        Mockito.verifyNoInteractions(converter);
    }
}
