package com.tomtom.itcu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tomtom.itcu.service.TrafficControllerService;
import com.tomtom.itcu.service.response.TrafficResponse;

@RestController
@RequestMapping("/")
public class TrafficController {

    @Autowired
    public TrafficControllerService trafficControllerService;

    @RequestMapping(value = "/traffic/info", method = RequestMethod.GET)
    @JsonProperty
    public TrafficResponse getTrafficInfo(String signalId) {
        return trafficControllerService.getDefaultTrafficInfo(signalId).get(0);
    }

    @RequestMapping(value = "/signal/info", method = RequestMethod.GET)
    @JsonProperty
    public TrafficResponse getSignalInfo(String signalId) {
        return trafficControllerService.getSignalInfo(signalId);
    }

    @RequestMapping(value = "/signal/change", method = RequestMethod.GET)
    @JsonProperty
    public void setTemporaryTrafficTime(String signalId, int changedSignalTime, int durationInMinute) {
        // input: changed value, duration, signal id
        trafficControllerService.setTemporaryTime(signalId, changedSignalTime, durationInMinute);
    }

    @RequestMapping(value = "/signal/all", method = RequestMethod.GET)
    @JsonProperty
    public List<TrafficResponse> getAllSignal() {
        // return all the signals info
        return trafficControllerService.getAllSignals();
    }

}
