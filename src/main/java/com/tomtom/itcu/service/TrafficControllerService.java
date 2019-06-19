package com.tomtom.itcu.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.tomtom.itcu.entity.CurrentSignalStatus;
import com.tomtom.itcu.entity.MasterTrafficInfo;
import com.tomtom.itcu.entity.TemporarryTrafficDetails;
import com.tomtom.itcu.entity.TrafficHistory;
import com.tomtom.itcu.model.FlowSegmentData;
import com.tomtom.itcu.repository.CurrentSignalStatusRepository;
import com.tomtom.itcu.repository.TemporaryTrafficRepository;
import com.tomtom.itcu.repository.TrafficHistoryRepository;
import com.tomtom.itcu.repository.TrafficInfoRepository;
import com.tomtom.itcu.service.response.ResponseConstructor;
import com.tomtom.itcu.service.response.TrafficResponse;
import com.tomtom.itcu.tomtom.api.TomtomService;

@Service
public class TrafficControllerService {

    @Autowired
    public TemporaryTrafficRepository tempTrafficRepository;
    @Autowired
    public TrafficInfoRepository trafficInfoRepository;
    @Autowired
    public ResponseConstructor responseConstructor;
    @Autowired
    public CurrentSignalStatusRepository currentSignalStatusRepository;

    @Autowired
    public TrafficHistoryRepository trafficHistoryRepository;

    @Autowired
    @Qualifier("tomtomServiceImpl")
    public TomtomService tomtomServiceImpl;

    public List<TrafficResponse> getDefaultTrafficInfo(final String signalId) {
        final MasterTrafficInfo masterTrafficInfo = trafficInfoRepository.findBySignalId(signalId).get(0);
        final String defaultTime = masterTrafficInfo.getDefaultTime();
        final Double lat = masterTrafficInfo.getLat();
        final Double lon = masterTrafficInfo.getLon();
        int calculatedSignalTime = 0;
        // check if temporary override time is set.
        final int temporaryOverrideTime = isTemporaryOverride(signalId);

        final FlowSegmentData flowSegmentData = getFlowSegmentData(lat, lon);
        calculatedSignalTime = getCalculatedSignalTime(defaultTime, flowSegmentData) + temporaryOverrideTime;
        updateTrafficHistory(masterTrafficInfo, calculatedSignalTime);
        updateCurrentSignalStatus(masterTrafficInfo, calculatedSignalTime);

        return responseConstructor.getSignalResponse(signalId, calculatedSignalTime, defaultTime);
    }

    private void updateCurrentSignalStatus(MasterTrafficInfo masterTrafficInfo, int calculatedSignalTime) {

        final Optional<CurrentSignalStatus> curSignalStatus =
            currentSignalStatusRepository.findById(masterTrafficInfo.getSignalId());
        if (curSignalStatus.isPresent()) {
            final CurrentSignalStatus currentSignalStatus = curSignalStatus.get();
            currentSignalStatus.setSignalId(masterTrafficInfo.getSignalId());
            currentSignalStatus.setCurrentSignalTime(calculatedSignalTime);
            currentSignalStatus.setUpdationTime(new Date());
            currentSignalStatus.setDefaultTime(Integer.parseInt(masterTrafficInfo.getDefaultTime()));
            currentSignalStatusRepository.save(currentSignalStatus);
        } else {
            final CurrentSignalStatus currentSignalStatus = new CurrentSignalStatus();
            currentSignalStatus.setSignalId(masterTrafficInfo.getSignalId());
            currentSignalStatus.setCurrentSignalTime(calculatedSignalTime);
            currentSignalStatus.setUpdationTime(new Date());
            currentSignalStatus.setDefaultTime(Integer.parseInt(masterTrafficInfo.getDefaultTime()));
            currentSignalStatusRepository.save(currentSignalStatus);
        }
    }

    private int isTemporaryOverride(final String signalId) {
        int tempTime = 0;
        final Optional<TemporarryTrafficDetails> temporaryDetails = tempTrafficRepository.findById(signalId);
        if (temporaryDetails.isPresent()) {
            final Date now = new Date();
            final TemporarryTrafficDetails temporarryTrafficDetails = temporaryDetails.get();
            final int duration = temporarryTrafficDetails.getDuration();
            final Date creationTime = temporarryTrafficDetails.getCreationTime();
            final long timeDifference = now.getTime() - creationTime.getTime();
            final long minutes = TimeUnit.MILLISECONDS.toMinutes(timeDifference);
            if (minutes <= duration) {
                // set temp
                tempTime = temporarryTrafficDetails.getTemporaryTime();
            }

        }
        return tempTime;
    }

    private void updateTrafficHistory(final MasterTrafficInfo masterTrafficInfo, final int calculatedSignalTime) {
        final TrafficHistory trafficHistory = new TrafficHistory();
        trafficHistory.setCurrentSignalTime(calculatedSignalTime);
        trafficHistory.setSignalId(masterTrafficInfo.getSignalId());
        trafficHistory.setDefaultTime(Integer.parseInt(masterTrafficInfo.getDefaultTime()));
        trafficHistory.setUpdatationTime(new Date());
        trafficHistoryRepository.save(trafficHistory);

    }

    private int getCalculatedSignalTime(final String idDefaultTime, final FlowSegmentData flowSegmentData) {
        final int currentSpeed = flowSegmentData.getCurrentSpeed();
        final int freeFlowSpeed = flowSegmentData.getFreeFlowSpeed();
        final int defaultTime = Integer.parseInt(idDefaultTime);
        int deviateTime = 0;
        if (currentSpeed == freeFlowSpeed) {
            deviateTime = defaultTime;
        } else {
            int diffPercentage = (freeFlowSpeed - currentSpeed) * 100 / freeFlowSpeed;
            if (diffPercentage > 50) {
                diffPercentage = 50;
            } else if (diffPercentage < -50) {
                diffPercentage = -50;
            }
            deviateTime = defaultTime + defaultTime * diffPercentage / 100;
        }

        return deviateTime;

    }

    private FlowSegmentData getFlowSegmentData(final double lat, final double lon) {
        // call tomtom api
        return tomtomServiceImpl.getSpeedInfo(lat, lon);
    }

    public TrafficResponse getSignalInfo(final String signalId) {
        final Optional<CurrentSignalStatus> trafficInfo = currentSignalStatusRepository.findById(signalId);
        if (trafficInfo.isPresent()) {
            return responseConstructor.constructResponse(trafficInfo.get());
        } else {
            return new TrafficResponse();
        }

    }

    public void setTemporaryTime(final String signalId, final int changedSignalTime, final int durationInMinute) {

        final Optional<TemporarryTrafficDetails> tempTrafficDetails = tempTrafficRepository.findById(signalId);
        if (tempTrafficDetails.isPresent()) {
            final TemporarryTrafficDetails temporarryTrafficDetails = tempTrafficDetails.get();
            temporarryTrafficDetails.setDuration(durationInMinute);
            temporarryTrafficDetails.setSignalId(signalId);
            temporarryTrafficDetails.setTemporaryTime(changedSignalTime);
            temporarryTrafficDetails.setCreationTime(new Date());
            tempTrafficRepository.save(temporarryTrafficDetails);
        } else {
            final TemporarryTrafficDetails temporarryTrafficDetails = new TemporarryTrafficDetails();
            temporarryTrafficDetails.setDuration(durationInMinute);
            temporarryTrafficDetails.setSignalId(signalId);
            temporarryTrafficDetails.setTemporaryTime(changedSignalTime);
            temporarryTrafficDetails.setCreationTime(new Date());
            tempTrafficRepository.save(temporarryTrafficDetails);
        }

    }

}
