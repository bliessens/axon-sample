package be.cegeka.viogate.microbio;

import org.apache.commons.lang3.ArrayUtils;
import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class AddMicroBioSampleCommand {

    @TargetAggregateIdentifier
    private final String orderId;
    private final String recipientType;
    private final Collection<Integer> analyses;


    public AddMicroBioSampleCommand(String orderId, String recipientType, Integer... analyses) {
        this.orderId = orderId;
        this.recipientType = recipientType;
        this.analyses = ArrayUtils.isEmpty(analyses) ? Collections.emptyList() : Arrays.asList(analyses);
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
