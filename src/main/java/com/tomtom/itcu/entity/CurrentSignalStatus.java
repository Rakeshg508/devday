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
public class CurrentSignalStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String signalId;
    private Integer currentSignalTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updationTime;

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

    public MasterTrafficInfo getMasterTrafficInfo() {
        return masterTrafficInfo;
    }

    public void setMasterTrafficInfo(MasterTrafficInfo masterTrafficInfo) {
        this.masterTrafficInfo = masterTrafficInfo;
    }

}
