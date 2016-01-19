package be.cegeka.viogate.order;

import be.cegeka.viogate.microbio.AddMicroBioSampleCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.domain.AggregateRoot;
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

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:/spring-emf.xml",
        "classpath:/spring-context.xml",
        "classpath:/spring-axon.xml"
})
@ActiveProfiles({"hsqldb"})
//@Transactional(transactionManager = "transactionManager")
//@Rollback(value = false)
public class OrderAggregateIntegrationTest {

    @Autowired
    private CommandGateway commandGateway;
    @Autowired
    private EventSourcingRepository eventSourcingRepository;
    @Autowired
    private PlatformTransactionManager txManager;

    private UnitOfWorkFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new DefaultUnitOfWorkFactory(new SpringTransactionManager(txManager));
    }

    @Test
    public void name() throws Exception {

        final String orderId = "ORD-" + LocalTime.now().toString("HHmmssSSS");

        commandGateway.send(new CreateOrderForVioNumberCommand(orderId, "VIO-345", 187));
        commandGateway.send(new AddMicroBioSampleCommand(orderId, "RCP1", 2010, 2020, 2023));
        commandGateway.send(new AddMicroBioSampleCommand(orderId, "RCP2", 3456, 6790));

        UnitOfWork unitOfWork = factory.createUnitOfWork();
        final AggregateRoot aggregate = eventSourcingRepository.load(orderId);

        assertThat(aggregate.getVersion()).isEqualTo(2);
    }

}