package com.tomtom.itcu.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.tomtom.itcu.constant.TrafficConstatnts;
import com.tomtom.itcu.entity.CurrentSignalStatus;
import com.tomtom.itcu.entity.MasterTrafficInfo;
import com.tomtom.itcu.entity.TemporarryTrafficDetails;
import com.tomtom.itcu.entity.TrafficHistory;
import com.tomtom.itcu.model.FlowSegmentData;
import com.tomtom.itcu.model.MasterTrafficInfoCurrentSignalStatusDTO;
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

    public List<TrafficResponse> getCalculatedTrafficInfo(final String signalId) {
        final MasterTrafficInfo masterTrafficInfo = trafficInfoRepository.findBySignalId(signalId).get(0);
        final int defaultTime = masterTrafficInfo.getDefaultTime();
        final Double lat = masterTrafficInfo.getLat();
        final Double lon = masterTrafficInfo.getLon();
        int calculatedSignalTime = 0;

        final Optional<TemporarryTrafficDetails> tempTrafficTime = getTempTrafficTime(signalId);
        if (tempTrafficTime.isPresent()) {
            if (redCorrodor(tempTrafficTime)) {
                calculatedSignalTime = TrafficConstatnts.RED_CORRIDOR_TEMP_TIME;
            } else {
                // check if temporary override time is set.
                final int temporaryOverrideTime = tempTrafficTime.get().getTemporaryTime();
                final FlowSegmentData flowSegmentData = getFlowSegmentData(lat, lon);
                calculatedSignalTime = getCalculatedSignalTime(defaultTime, flowSegmentData) + temporaryOverrideTime;
            }
        } else {
            final FlowSegmentData flowSegmentData = getFlowSegmentData(lat, lon);
            calculatedSignalTime = getCalculatedSignalTime(defaultTime, flowSegmentData);
        }
        updateCurrentSignalStatus(masterTrafficInfo, calculatedSignalTime);
        return responseConstructor.getSignalResponse(signalId, calculatedSignalTime, defaultTime);
    }

    private boolean redCorrodor(Optional<TemporarryTrafficDetails> tempTrafficTime) {
        return tempTrafficTime.get().getTemporaryTime() == TrafficConstatnts.RED_CORRIDOR_TEMP_TIME;
    }

    private Optional<TemporarryTrafficDetails> getTempTrafficTime(String signalId) {
        final Date currentTime = new Date();
        final Optional<TemporarryTrafficDetails> tempTrafficDetails =
            tempTrafficRepository.findTempTrafficDetailInCurrentTime(currentTime, signalId);
        return tempTrafficDetails;
    }

    private void updateCurrentSignalStatus(MasterTrafficInfo masterTrafficInfo, int calculatedSignalTime) {

        final Optional<CurrentSignalStatus> curSignalStatus =
            currentSignalStatusRepository.findBySignalId(masterTrafficInfo.getSignalId());

        CurrentSignalStatus currentSignalStatus = null;
        if (curSignalStatus.isPresent()) {
            currentSignalStatus = curSignalStatus.get();

        } else {
            currentSignalStatus = new CurrentSignalStatus();
        }
        currentSignalStatus.setSignalId(masterTrafficInfo.getSignalId());
        currentSignalStatus.setCurrentSignalTime(calculatedSignalTime);
        currentSignalStatus.setUpdationTime(new Date());
        currentSignalStatusRepository.save(currentSignalStatus);
    }

    private int isTemporaryOverride(final String signalId) {
        int tempTime = 0;
        final Optional<TemporarryTrafficDetails> temporaryDetails = tempTrafficRepository.findBySignalId(signalId);
        if (temporaryDetails.isPresent()) {
            final Date now = new Date();
            final TemporarryTrafficDetails temporarryTrafficDetails = temporaryDetails.get();
            final int duration = 0;// temporarryTrafficDetails.getDuration();
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

    private void updateTrafficHistory(final MasterTrafficInfo masterTrafficInfo, final int calculatedSignalTime,
        String source) {
        final TrafficHistory trafficHistory = new TrafficHistory();
        trafficHistory.setCurrentSignalTime(calculatedSignalTime);
        trafficHistory.setSignalId(masterTrafficInfo.getSignalId());
        trafficHistory.setDefaultTime(masterTrafficInfo.getDefaultTime());
        trafficHistory.setUpdatationTime(new Date());
        trafficHistory.setSource(source);
        trafficHistory.setLat(masterTrafficInfo.getLat());
        trafficHistory.setLon(masterTrafficInfo.getLon());
        trafficHistoryRepository.save(trafficHistory);

    }

    private int getCalculatedSignalTime(final int defaultTime, final FlowSegmentData flowSegmentData) {
        final int currentSpeed = flowSegmentData.getCurrentSpeed();
        final int freeFlowSpeed = flowSegmentData.getFreeFlowSpeed();
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
        final List<MasterTrafficInfoCurrentSignalStatusDTO> ctsi =
            currentSignalStatusRepository.fetchCurrentTrafficSignalInfo();
        return responseConstructor.constructResponse(ctsi).get(0);

    }

    public void setTemporaryTime(final String signalId, final int changedSignalTime, final int durationInMinute) {

        updateTemporaryTrafficTime(signalId, changedSignalTime, durationInMinute);
        final MasterTrafficInfo masterInfo = trafficInfoRepository.findBySignalId(signalId).get(0);
        updateCurrentSignalStatus(masterInfo, changedSignalTime);
        updateTrafficHistory(masterInfo, changedSignalTime, TrafficConstatnts.SOURCE_TRAFFIC_TEMPOERARY_CHANGE);

    }

    private void updateTemporaryTrafficTime(String signalId, int changedSignalTime, int durationInMinute) {
        final Optional<TemporarryTrafficDetails> tempTrafficDetails = tempTrafficRepository.findBySignalId(signalId);
        TemporarryTrafficDetails temporarryTrafficDetails = null;
        if (tempTrafficDetails.isPresent()) {
            temporarryTrafficDetails = tempTrafficDetails.get();

        } else {
            temporarryTrafficDetails = new TemporarryTrafficDetails();
        }
        temporarryTrafficDetails.setSignalId(signalId);
        temporarryTrafficDetails.setTemporaryTime(changedSignalTime);
        temporarryTrafficDetails.setCreationTime(new Date());
        tempTrafficRepository.save(temporarryTrafficDetails);

    }

    public List<TrafficResponse> getAllSignals() {
        final List<MasterTrafficInfoCurrentSignalStatusDTO> ctsi =
            currentSignalStatusRepository.fetchCurrentTrafficSignalInfo();
        return responseConstructor.constructResponse(ctsi);
    }

    public void setSignalTempTime(String signalId, int temporaryTime, Date startDateTime, Date endDateTime,
        String comments) {
        updateTemporaryTrafficTimePeriod(signalId, temporaryTime, startDateTime, endDateTime, comments);
        final MasterTrafficInfo masterInfo = trafficInfoRepository.findBySignalId(signalId).get(0);
        updateCurrentSignalStatus(masterInfo, temporaryTime);
        updateTrafficHistory(masterInfo, temporaryTime, comments);

    }

    private void updateTemporaryTrafficTimePeriod(String signalId, int temporaryTime, Date startDateTime, Date endDateTime,
        String comments) {

        final Optional<TemporarryTrafficDetails> tempTrafficDetails = tempTrafficRepository.findBySignalId(signalId);
        TemporarryTrafficDetails temporarryTrafficDetails = null;
        if (tempTrafficDetails.isPresent()) {
            temporarryTrafficDetails = tempTrafficDetails.get();

        } else {
            temporarryTrafficDetails = new TemporarryTrafficDetails();
        }
        temporarryTrafficDetails.setSignalId(signalId);
        temporarryTrafficDetails.setTemporaryTime(temporaryTime);
        temporarryTrafficDetails.setCreationTime(new Date());
        temporarryTrafficDetails.setComments(comments);
        temporarryTrafficDetails.setStartDateTime(startDateTime);
        temporarryTrafficDetails.setEndDateTime(endDateTime);
        tempTrafficRepository.save(temporarryTrafficDetails);
    }

}
