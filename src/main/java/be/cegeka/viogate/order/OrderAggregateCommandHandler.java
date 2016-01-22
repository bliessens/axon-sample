package be.cegeka.viogate.order;

import be.cegeka.viogate.microbio.AddMicroBioSampleCommand;
import be.cegeka.viogate.patient.PatientRepository;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.unitofwork.UnitOfWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class OrderAggregateCommandHandler {

    @Autowired
    @Qualifier("orderAggregateEventSourcingRepository")
    private EventSourcingRepository<OrderAggregate> repository;

    @CommandHandler(commandName = "be.cegeka.viogate.order.CreateOrderForVioNumberCommand")
    public void handle(final CommandMessage<CreateOrderForVioNumberCommand> generic, final UnitOfWork work, final PatientRepository patientRepository) {
        System.out.println("Handling CreateOrderForVioNumberCommand");
        CreateOrderForVioNumberCommand command = generic.getPayload();
        if (patientRepository.isPatient(command.getVioNumber())) {
            repository.add(new OrderAggregate(command.getOrderId(), command.getPrescriber(), command.getVioNumber()));
        }
    }

    @CommandHandler
    public void handle(final AddMicroBioSampleCommand command, final UnitOfWork work) {
        if (command.getAnalyses().isEmpty()) {
            throw new RuntimeException("MB Sample must have analyses");
        }

        try {
            final OrderAggregate aggregate = repository.load(command.getOrderId());
            aggregate.addMicroBioSample(command.getRecipientType(), command.getAnalyses());

        } catch (AggregateNotFoundException anfe) {
            System.err.println("No such Order with id " + command.getOrderId());
        }
    }

}
