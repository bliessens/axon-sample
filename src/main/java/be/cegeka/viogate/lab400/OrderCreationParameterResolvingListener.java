package be.cegeka.viogate.lab400;

import be.cegeka.viogate.order.OrderCreatedForVioNumberEvent;
import be.cegeka.viogate.patient.PatientRepository;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.springframework.stereotype.Component;

@Component
public class OrderCreationParameterResolvingListener {

    @EventHandler
    public void handleEventWithAutowiredBean(OrderCreatedForVioNumberEvent event, PatientRepository patientRepository) {
        System.out.println("Handling typed domain event with Spring Bean as 2md method param");

    }

}
