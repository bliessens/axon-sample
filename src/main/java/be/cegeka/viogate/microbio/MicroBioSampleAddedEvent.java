package be.cegeka.viogate.microbio;

import java.util.Collection;

public class MicroBioSampleAddedEvent {

    private final String orderId;
    private final String recipientType;
    private final Collection<Integer> analyses;

    public MicroBioSampleAddedEvent(String orderId, String recipientType, Collection<Integer> analyses) {
        this.orderId = orderId;
        this.recipientType = recipientType;
        this.analyses = analyses;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getRecipientType() {
        return recipientType;
    }

    public Collection<Integer> getAnalyses() {
        return analyses;
    }
}
