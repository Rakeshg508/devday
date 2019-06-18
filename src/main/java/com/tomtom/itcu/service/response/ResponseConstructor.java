package com.tomtom.itcu.service.response;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.tomtom.itcu.entity.MasterTrafficInfo;

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

    public void constructResponse(List<MasterTrafficInfo> signalInfo) {
        final List<TrafficResponse> reponses = new ArrayList();
        final TrafficResponse trafficResponse = new TrafficResponse();

        trafficResponse.setSignalId(signalInfo.get(0).getSignalId());
        trafficResponse.setSignalId(signalInfo.get(0).getSignalId());
        trafficResponse.setSignalId(signalInfo.get(0).getSignalId());

        reponses.add(trafficResponse);

    }

}
