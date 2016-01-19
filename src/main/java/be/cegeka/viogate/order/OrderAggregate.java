package be.cegeka.viogate.order;

import be.cegeka.viogate.microbio.MicroBioSampleAddedEvent;
import be.cegeka.viogate.microbio.MicroBiologySample;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;

import java.util.Collection;
import java.util.LinkedList;

public class OrderAggregate extends AbstractAnnotatedAggregateRoot {

    @AggregateIdentifier
    private String orderId;
    private String vioNumber;
    private int prescriber;

    private Collection<MicroBiologySample> mbSamples = new LinkedList<>();

    protected OrderAggregate() {
    }

    public OrderAggregate(String orderId, int prescriber, String vioNumber) {
        apply(new OrderCreatedForVioNumberEvent(orderId, prescriber, vioNumber));
    }

    @EventHandler
    public void handle(final OrderCreatedForVioNumberEvent event) {
        this.orderId = event.getOrderId();
        this.vioNumber = event.getVioNumber();
        this.prescriber = event.getPrescriber();
    }

    public void addMicroBioSample(String recipientType, Collection<Integer> analyses) {
        apply(new MicroBioSampleAddedEvent(this.orderId, recipientType, analyses));
    }

    @EventHandler
    public void handle(final MicroBioSampleAddedEvent event) {
        System.out.println("Adding MB Sample with recipient " + event.getRecipientType());
        mbSamples.add(new MicroBiologySample(event.getRecipientType(), event.getAnalyses()));
    }
}
