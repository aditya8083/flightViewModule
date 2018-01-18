package com.coviam.util;

public class InteractionData {

    private String interactionId;
    private String interactionName;

    public String getInteractionId() {
        return interactionId;
    }

    public void setInteractionId(String interactionId) {
        this.interactionId = interactionId;
    }

    public String getInteractionName() {
        return interactionName;
    }

    public void setInteractionName(String interactionName) {
        this.interactionName = interactionName;
    }

    public InteractionData(String interactionId, String interactionName) {
        this.interactionId = interactionId;
        this.interactionName = interactionName;
    }

    @Override
    public String toString() {
        return "InteractionData{" + "interactionId='" + interactionId + '\'' + ", interactionName='" + interactionName + '\'' + '}';
    }
}
