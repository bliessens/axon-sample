package be.cegeka.viogate.order;

public class OrderCreatedForVioNumberEvent {

    private final String orderId;
    private final String vioNumber;
    private final int prescriber;

    public OrderCreatedForVioNumberEvent(String orderId, int prescriber, String vioNumber) {
        this.orderId = orderId;
        this.prescriber = prescriber;
        this.vioNumber = vioNumber;
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
