package com.tomtom.itcu.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class MasterTrafficInfo {

    @Id
    private String signalId;
    private Integer defaultTime;
    private Double lat;
    private Double lon;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "masterTrafficInfo")
    private CurrentSignalStatus curSignalStatus;

    public MasterTrafficInfo() {
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

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public CurrentSignalStatus getCurSignalStatus() {
        return curSignalStatus;
    }

    public void setCurSignalStatus(CurrentSignalStatus curSignalStatus) {
        this.curSignalStatus = curSignalStatus;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "MasterTrafficInfo{" + "signalId=" + signalId + ", defaultTime='" + defaultTime + '\'' + '}';
    }

}
