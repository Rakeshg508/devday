package com.tomtom.itcu.tomtom.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.tomtom.itcu.constant.TrafficConstatnts;
import com.tomtom.itcu.model.FlowSegmentData;
import com.tomtom.itcu.model.TrafficFlowSegmentResponse;

@Component("tomtomServiceImpl")
public class TomtomServiceImpl implements TomtomService {

    @Value("${tomtom.sevice.url}")
    private String tomtomApiUrl;

    @Value("${tomtom.service.key}")
    private String tomtomApiKey;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public FlowSegmentData getSpeedInfo(double lat, double lon) {
        final String apiUrl =
            tomtomApiUrl + "point=" + lat + "," + lon + "&unit=" + TrafficConstatnts.SPEED_UNIT + "&key=" + tomtomApiKey;
        final TrafficFlowSegmentResponse forObject = restTemplate.getForObject(apiUrl, TrafficFlowSegmentResponse.class);
        return forObject.getFlowSegmentData();
    }

}
