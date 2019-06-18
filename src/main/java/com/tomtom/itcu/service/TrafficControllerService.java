package com.tomtom.itcu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.tomtom.itcu.constant.TrafficConstatnts;
import com.tomtom.itcu.model.FlowSegmentData;
import com.tomtom.itcu.model.MasterTrafficInfo;
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

    public List<TrafficResponse> getDefaultTrafficInfo(String signalId, String defaultTime, double lat, double lon) {
        String idDefaultTime = "-1";
        if (defaultTime.equals(TrafficConstatnts.INSTALLATION_TIME)) {
            final List<MasterTrafficInfo> masterTrafficInfo = trafficInfoRepository.findBySignalId(signalId);
            idDefaultTime = masterTrafficInfo.get(0).getDefaultTime();

        } else {
            idDefaultTime = defaultTime;
        }
        final FlowSegmentData flowSegmentData = getFlowSegmentData(lat, lon);
        final int calculatedSignalTime = getCalculatedSignalTime(idDefaultTime, flowSegmentData);

        return responseConstructor.getSignalResponse(signalId, calculatedSignalTime);
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

    public void getSignalInfo(String signalId) {
        final List<MasterTrafficInfo> trafficInfo = trafficInfoRepository.findBySignalId(signalId);
        // responseConstructor.constructResponse(trafficInfo);

    }

}
