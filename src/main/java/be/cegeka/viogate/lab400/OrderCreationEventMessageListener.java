package be.cegeka.viogate.lab400;

import be.cegeka.viogate.order.OrderCreatedForVioNumberEvent;
import org.axonframework.domain.EventMessage;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.springframework.stereotype.Component;

@Component
public class OrderCreationEventMessageListener {

    @EventHandler
    public void handleEventMessage(EventMessage<OrderCreatedForVioNumberEvent> event) {
        System.out.println("Handling EventMessage");
        if (OrderCreatedForVioNumberEvent.class.isAssignableFrom(event.getPayloadType())) {
            OrderCreatedForVioNumberEvent domainEvent = (OrderCreatedForVioNumberEvent) event.getPayload();
        }
    }

}
