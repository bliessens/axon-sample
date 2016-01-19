package be.cegeka.viogate.microbio;

import java.util.Collection;

public class MicroBiologySample {

    private String recipientType;
    private Collection<Integer> analyses;

    public MicroBiologySample(String recipientType, Collection<Integer> analyses) {
        this.recipientType = recipientType;
        this.analyses = analyses;
    }

    public String getRecipientType() {
        return recipientType;
    }

    public Collection<Integer> getAnalyses() {
        return analyses;
    }
}
