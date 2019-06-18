package com.tomtom.itcu.controller;

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

    @RequestMapping(value = "/traffic", method = RequestMethod.GET)
    @JsonProperty
    public TrafficResponse getTrafficInfo(String defaultTime, String signalId, double lat, double lon) {
        return trafficControllerService.getDefaultTrafficInfo(signalId, defaultTime, lat, lon).get(0);
    }

    @RequestMapping(value = "/signal/info", method = RequestMethod.POST)
    @JsonProperty
    public TrafficResponse getSignalInfo(String signalId) {
        return trafficControllerService.getSignalInfo(signalId);
    }

    @RequestMapping(value = "/change/signal", method = RequestMethod.POST)
    @JsonProperty
    public void setTemporaryTrafficTime() {
        System.out.println("traffic temporary  called");
    }

    @RequestMapping(value = "/change/default", method = RequestMethod.POST)
    @JsonProperty
    public String setDefaultTrafficTime() {
        System.out.println("set first time unit value");
        return "traffic";
    }

}
