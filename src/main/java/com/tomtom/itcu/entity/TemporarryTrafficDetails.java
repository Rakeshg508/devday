package com.tomtom.itcu.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class TemporarryTrafficDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String signalId;

    private int temporaryTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startDateTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endDateTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;

    private String comments;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "signalId", insertable = false, updatable = false)
    private MasterTrafficInfo masterTrafficInfo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSignalId() {
        return signalId;
    }

    public void setSignalId(String signalId) {
        this.signalId = signalId;
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

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public MasterTrafficInfo getMasterTrafficInfo() {
        return masterTrafficInfo;
    }

    public void setMasterTrafficInfo(MasterTrafficInfo masterTrafficInfo) {
        this.masterTrafficInfo = masterTrafficInfo;
    }

}
