package com.tomtom.itcu.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class TemporarryTrafficDetails {

    @Id
    private String signalId;
    private int duration;
    private int temporaryTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;

    public String getSignalId() {
        return signalId;
    }

    public void setSignalId(String signalId) {
        this.signalId = signalId;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getTemporaryTime() {
        return temporaryTime;
    }

    public void setTemporaryTime(int temporaryTime) {
        this.temporaryTime = temporaryTime;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

}
