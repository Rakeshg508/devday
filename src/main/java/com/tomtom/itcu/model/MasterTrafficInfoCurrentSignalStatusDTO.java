package com.tomtom.itcu.model;

public class MasterTrafficInfoCurrentSignalStatusDTO {

    private String signalId;
    private Integer defaultTime;
    private Double lat;
    private Double lon;
    private Integer currentTime;

    public MasterTrafficInfoCurrentSignalStatusDTO() {
        super();
    }

    public MasterTrafficInfoCurrentSignalStatusDTO(String signalId, Integer defaultTime, Double lat, Double lon,
        Integer currentTime) {
        super();
        this.signalId = signalId;
        this.defaultTime = defaultTime;
        this.lat = lat;
        this.lon = lon;
        this.currentTime = currentTime;
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

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Integer getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Integer currentTime) {
        this.currentTime = currentTime;
    }

}
