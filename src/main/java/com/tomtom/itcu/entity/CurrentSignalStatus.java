package com.tomtom.itcu.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class CurrentSignalStatus {

    @Id
    private String signalId;
    private Integer currentSignalTime;
    private Integer defaultTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updationTime;

    public String getSignalId() {
        return signalId;
    }

    public void setSignalId(String signalId) {
        this.signalId = signalId;
    }

    public Integer getCurrentSignalTime() {
        return currentSignalTime;
    }

    public void setCurrentSignalTime(Integer currentSignalTime) {
        this.currentSignalTime = currentSignalTime;
    }

    public Date getUpdationTime() {
        return updationTime;
    }

    public void setUpdationTime(Date updationTime) {
        this.updationTime = updationTime;
    }

    public Integer getDefaultTime() {
        return defaultTime;
    }

    public void setDefaultTime(Integer defaultTime) {
        this.defaultTime = defaultTime;
    }

}
