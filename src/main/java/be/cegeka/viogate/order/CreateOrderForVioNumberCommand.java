package be.cegeka.viogate.order;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

public class CreateOrderForVioNumberCommand {

    @TargetAggregateIdentifier
    private final String orderId;
    private final String vioNumber;
    private final int prescriber;

    public CreateOrderForVioNumberCommand(String orderId, String vioNumber, int prescriber) {
        this.orderId = orderId;
        this.vioNumber = vioNumber;
        this.prescriber = prescriber;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getVioNumber() {
        return vioNumber;
    }

    public int getPrescriber() {
        return prescriber;
    }
}
