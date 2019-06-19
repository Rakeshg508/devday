package com.tomtom.itcu.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class TrafficHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String signalId;
    private Integer defaultTime;
    private Integer currentSignalTime;
    private String source;
    private Double lat;
    private Double lon;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatationTime;

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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

}
