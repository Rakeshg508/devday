package com.tomtom.itcu.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class TrafficHistory {

    @Id
    private String signalId;
    private Integer defaultTime;
    private Integer currentSignalTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatationTime;

    public String getSignalId() {
        return signalId;
    }

    public void setSignalId(String signalId) {
        this.signalId = signalId;
    }

    public Integer getDefaultTime() {
        return defaultTime;
    }

    public void setDefaultTime(Integer defaultTime) {
        this.defaultTime = defaultTime;
    }

    public Integer getCurrentSignalTime() {
        return currentSignalTime;
    }

    public void setCurrentSignalTime(Integer currentSignalTime) {
        this.currentSignalTime = currentSignalTime;
    }

    public Date getUpdatationTime() {
        return updatationTime;
    }

    public void setUpdatationTime(Date updatationTime) {
        this.updatationTime = updatationTime;
    }

}
