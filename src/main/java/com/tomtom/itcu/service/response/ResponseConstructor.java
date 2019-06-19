package com.tomtom.itcu.service.response;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.tomtom.itcu.entity.CurrentSignalStatus;
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

    public TrafficResponse constructResponse(CurrentSignalStatus currentSignalStatus, MasterTrafficInfo masterTrafficInfo) {
        final TrafficResponse trafficResponse = new TrafficResponse();
        trafficResponse.setSignalId(currentSignalStatus.getSignalId());
        trafficResponse.setCurrentTime(currentSignalStatus.getCurrentSignalTime());
        trafficResponse.setDefaultTime(currentSignalStatus.getDefaultTime());
        trafficResponse.setLat(masterTrafficInfo.getLat());
        trafficResponse.setLon(masterTrafficInfo.getLon());
        return trafficResponse;

    }

    public List<TrafficResponse> constructResponse(Iterable<MasterTrafficInfo> allSignals,
        Iterable<CurrentSignalStatus> currentSignalStatus) {
        final List<CurrentSignalStatus> statusList = new ArrayList();
        currentSignalStatus.forEach(status -> statusList.add(status));
        final List<TrafficResponse> respose = new ArrayList();
        for (final MasterTrafficInfo trafficInfo : allSignals) {
            final TrafficResponse tr = new TrafficResponse();
            tr.setDefaultTime(Integer.parseInt(trafficInfo.getDefaultTime()));
            tr.setLat(trafficInfo.getLat());
            tr.setLon(trafficInfo.getLon());
            tr.setSignalId(trafficInfo.getSignalId());
            final Integer signalCurrentTime = getSignalCurrentInfo(trafficInfo.getSignalId(), statusList);
            tr.setCurrentTime(signalCurrentTime);
            respose.add(tr);
        }
        return respose;

    }

    private Integer getSignalCurrentInfo(String signalId, List<CurrentSignalStatus> statusList) {
        for (final CurrentSignalStatus status : statusList) {
            if (status.getSignalId().equalsIgnoreCase(signalId)) {
                return status.getCurrentSignalTime();
            }
        }
        return 0;
    }

}
