package com.tomtom.itcu.service.response;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.tomtom.itcu.entity.CurrentSignalStatus;

@Component
public class ResponseConstructor {

    public List<TrafficResponse> getSignalResponse(String signalId, int calculatedSignalTime, String defaultTime) {
        final List<TrafficResponse> reponses = new ArrayList<TrafficResponse>();
        final TrafficResponse trafficResponse = new TrafficResponse();
        trafficResponse.setSignalId(signalId);
        trafficResponse.setCurrentTime(calculatedSignalTime);
        trafficResponse.setDefaultTime(Integer.parseInt(defaultTime));
        reponses.add(trafficResponse);
        return reponses;
    }

    public TrafficResponse constructResponse(CurrentSignalStatus currentSignalStatus) {
        final TrafficResponse trafficResponse = new TrafficResponse();
        trafficResponse.setSignalId(currentSignalStatus.getSignalId());
        trafficResponse.setCurrentTime(currentSignalStatus.getCurrentSignalTime());
        trafficResponse.setDefaultTime(currentSignalStatus.getDefaultTime());
        return trafficResponse;

    }

}
