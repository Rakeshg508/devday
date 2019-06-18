package com.tomtom.itcu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.tomtom.itcu.entity.MasterTrafficInfo;
import com.tomtom.itcu.model.FlowSegmentData;
import com.tomtom.itcu.repository.TrafficInfoRepository;
import com.tomtom.itcu.service.response.ResponseConstructor;
import com.tomtom.itcu.service.response.TrafficResponse;
import com.tomtom.itcu.tomtom.api.TomtomService;

@Service
public class TrafficControllerService {

    @Autowired
    public TrafficInfoRepository trafficInfoRepository;
    @Autowired
    public ResponseConstructor responseConstructor;

    @Autowired
    @Qualifier("tomtomServiceImpl")
    public TomtomService tomtomServiceImpl;

    public List<TrafficResponse> getDefaultTrafficInfo(String signalId) {
        final MasterTrafficInfo masterTrafficInfo = trafficInfoRepository.findBySignalId(signalId).get(0);
        final String defaultTime = masterTrafficInfo.getDefaultTime();
        final Double lat = masterTrafficInfo.getLat();
        final Double lon = masterTrafficInfo.getLon();

        final FlowSegmentData flowSegmentData = getFlowSegmentData(lat, lon);
        final int calculatedSignalTime = getCalculatedSignalTime(defaultTime, flowSegmentData);

        updateTrafficHistory(masterTrafficInfo, calculatedSignalTime);

        return responseConstructor.getSignalResponse(signalId, calculatedSignalTime, defaultTime);
    }

    private void updateTrafficHistory(MasterTrafficInfo masterTrafficInfo, int calculatedSignalTime) {
        // todo update history table
    }

    private int getCalculatedSignalTime(String idDefaultTime, FlowSegmentData flowSegmentData) {
        final int currentSpeed = flowSegmentData.getCurrentSpeed();
        final int freeFlowSpeed = flowSegmentData.getFreeFlowSpeed();
        final int defaultTime = Integer.parseInt(idDefaultTime);
        int deviateTime = 0;
        if (currentSpeed == freeFlowSpeed) {
            deviateTime = defaultTime;
        } else {
            final int diffPercentage = (freeFlowSpeed - currentSpeed) * 100 / freeFlowSpeed;
            deviateTime = defaultTime + defaultTime * diffPercentage / 100;
        }

        return deviateTime;

    }

    private FlowSegmentData getFlowSegmentData(double lat, double lon) {
        // call tomtom api
        return tomtomServiceImpl.getSpeedInfo(lat, lon);
    }

    public TrafficResponse getSignalInfo(String signalId) {
        final List<MasterTrafficInfo> trafficInfo = trafficInfoRepository.findBySignalId(signalId);
        // responseConstructor.constructResponse(trafficInfo);
        final TrafficResponse trafficResponse = new TrafficResponse();
        trafficResponse.setSignalId("u123");
        trafficResponse.setDefaultTime("30");
        trafficResponse.setCurrentTime("60");
        trafficResponse.setLat(40.757285);
        trafficResponse.setLon(-73.989927);
        return trafficResponse;
    }

    public void setTemporaryTime(String signalId, int changedSignalTime, int durationInMinute) {

    }

}
