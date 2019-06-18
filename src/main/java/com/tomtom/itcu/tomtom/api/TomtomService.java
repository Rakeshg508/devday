package com.tomtom.itcu.tomtom.api;

import com.tomtom.itcu.model.FlowSegmentData;

public interface TomtomService {

    public FlowSegmentData getSpeedInfo(double lat, double lon);
}
