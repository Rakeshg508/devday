package com.tomtom.itcu.repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tomtom.itcu.entity.TemporarryTrafficDetails;

public interface TemporaryTrafficRepository extends CrudRepository<TemporarryTrafficDetails, Long> {

    @Override
    Optional<TemporarryTrafficDetails> findById(Long id);

    @Override
    TemporarryTrafficDetails save(TemporarryTrafficDetails tempTrafficDetails);

    @Query("select ttd from com.tomtom.itcu.entity.TemporarryTrafficDetails ttd where ttd.signalId=?1")
    Optional<TemporarryTrafficDetails> findBySignalId(String signalId);

    @Query("select ttd from com.tomtom.itcu.entity.TemporarryTrafficDetails ttd where ttd.signalId=?2 and (?1 between ttd.start_date_time and ttd.end_date_time)")
    Optional<TemporarryTrafficDetails> findTempTrafficDetailInCurrentTime(Date currentTime, String signalId);

}
