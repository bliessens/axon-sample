package be.cegeka.viogate.order;

import be.cegeka.viogate.microbio.AddMicroBioSampleCommand;
import org.axonframework.commandhandling.CommandDispatchInterceptor;
import org.axonframework.commandhandling.CommandHandlerInterceptor;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.domain.AggregateRoot;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.unitofwork.DefaultUnitOfWorkFactory;
import org.axonframework.unitofwork.SpringTransactionManager;
import org.axonframework.unitofwork.UnitOfWork;
import org.axonframework.unitofwork.UnitOfWorkFactory;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Collection;
import java.util.LinkedList;

import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:/spring-emf.xml",
        "classpath:/spring-context.xml",
        "classpath:/spring-axon.xml"
})
@ActiveProfiles({"hsqldb"})
public class OrderAggregateIntegrationTest {

    @Autowired
    private CommandGateway commandGateway;
    @Autowired
    private EventSourcingRepository eventSourcingRepository;
    @Autowired
    private PlatformTransactionManager txManager;
    @Autowired
    private EventBus eventBus;
    @Autowired
    private SimpleCommandBus commandBus;

    private UnitOfWorkFactory factory;

    private final String orderId = "ORD-" + LocalTime.now().toString("HHmmssSSS");

    @Before
    public void setUp() throws Exception {
        factory = new DefaultUnitOfWorkFactory(new SpringTransactionManager(txManager));
    }

    @Test
    public void transactionalReadFromEventStore() throws Exception {

        commandGateway.send(new CreateOrderForVioNumberCommand(orderId, "VIO-345", 187));
        commandGateway.send(new AddMicroBioSampleCommand(orderId, "RCP1", 2010, 2020, 2023));
        commandGateway.send(new AddMicroBioSampleCommand(orderId, "RCP2", 3456, 6790));

        UnitOfWork unitOfWork = factory.createUnitOfWork();
        final AggregateRoot aggregate = eventSourcingRepository.load(orderId);

        assertThat(aggregate.getVersion()).isEqualTo(2);
    }

    @Test
    public void commandBusAndEventBusUseSameThreadAsDispatching() throws Exception {
        final Collection<String> threadNames = new LinkedList<>();
        commandBus.setDispatchInterceptors(singletonList((CommandDispatchInterceptor) commandMessage -> {
            threadNames.add("Dispatch thread name: " + Thread.currentThread().getName());
            return commandMessage;
        }));
        commandBus.setHandlerInterceptors(singletonList((CommandHandlerInterceptor) (commandMessage, unitOfWork, interceptorChain) -> {
            threadNames.add("Handler thread name: " + Thread.currentThread().getName());
            return interceptorChain.proceed();
        }));
        eventBus.subscribe(event -> threadNames.add("EventListener thread name: " + Thread.currentThread().getName()));

        commandGateway.send(new CreateOrderForVioNumberCommand(orderId, "VIO-456", 23));

        assertThat(threadNames).allMatch(threadName -> threadName.endsWith(Thread.currentThread().getName()));

    }


    @Test
    public void metadata() throws Exception {

        commandBus.setDispatchInterceptors(singletonList((CommandDispatchInterceptor) commandMessage -> {
            commandMessage.andMetaData(singletonMap("dispatch.timestamp", LocalTime.now().toString()));
            return commandMessage.andMetaData(singletonMap("dispatch.user", "Benoit"));
        }));

        commandGateway.send(new CreateOrderForVioNumberCommand(orderId, "VIO-345", 187));

    }
}