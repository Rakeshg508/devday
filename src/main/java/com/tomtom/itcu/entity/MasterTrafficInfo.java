package com.tomtom.itcu.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MasterTrafficInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String signalId;
    private String defaultTime;
    private Double lat;
    private Double lon;

    public MasterTrafficInfo() {
    }

    public MasterTrafficInfo(String defaultTime) {
        this.defaultTime = defaultTime;
    }

    public String getSignalId() {
        return signalId;
    }

    public void setSignalId(String signalId) {
        this.signalId = signalId;
    }

    public String getDefaultTime() {
        return defaultTime;
    }

    public void setDefaultTime(String defaultTime) {
        this.defaultTime = defaultTime;
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

    @Override
    public String toString() {
        return "MasterTrafficInfo{" + "signalId=" + signalId + ", defaultTime='" + defaultTime + '\'' + '}';
    }

}
