package com.tomtom.itcu.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    // signal unit gets the updated traffic congestion info
    @RequestMapping(value = "/traffic/info", method = RequestMethod.GET)
    @JsonProperty
    public TrafficResponse getUpdatedTrafficInfo(String signalId) {
        return trafficControllerService.getCalculatedTrafficInfo(signalId).get(0);
    }

    // api to get just what is current signal time set status
    @RequestMapping(value = "/signal/info", method = RequestMethod.GET)
    @JsonProperty
    public TrafficResponse getCurrentSignalStatus(String signalId) {
        return trafficControllerService.getSignalInfo(signalId);
    }

    @RequestMapping(value = "/signal/change", method = RequestMethod.GET)
    @JsonProperty
    public void setTemporaryTrafficTime(String signalId, int changedSignalTime, int durationInMinute) {
        // input: changed value, duration, signal id
        trafficControllerService.setTemporaryTime(signalId, changedSignalTime, durationInMinute);
    }

    // setting temporary signal time for particular period may help in red corridor
    @RequestMapping(value = "/signal/change/temptime", method = RequestMethod.GET)
    @JsonProperty
    public void setTemporarySignalTime(String signalId, int changedSignalTime, String startDateTime, String endDateTime,
        String comments) throws ParseException {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        final Date sDateTime = sdf.parse(startDateTime);
        final Date eDateTime = sdf.parse(endDateTime);
        // input: changed value, duration, signal id
        trafficControllerService.setSignalTempTime(signalId, changedSignalTime, sDateTime, eDateTime, comments);
    }

    // api to get all the signal info
    @RequestMapping(value = "/signal/all", method = RequestMethod.GET)
    @JsonProperty
    public List<TrafficResponse> getAllSignal() {
        // return all the signals info
        return trafficControllerService.getAllSignals();
    }

}
