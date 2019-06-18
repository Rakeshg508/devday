package com.tomtom.itcu.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FlowSegmentData {

    @JsonProperty
    private int currentSpeed;
    @JsonProperty
    private int freeFlowSpeed;

    public int getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(int currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public int getFreeFlowSpeed() {
        return freeFlowSpeed;
    }

    public void setFreeFlowSpeed(int freeFlowSpeed) {
        this.freeFlowSpeed = freeFlowSpeed;
    }

    @Override
    public String toString() {
        return "FlowSegmentData{" + "currentSpeed=" + currentSpeed + ", freeFlowSpeed='" + freeFlowSpeed + '\'' + '}';
    }

}
