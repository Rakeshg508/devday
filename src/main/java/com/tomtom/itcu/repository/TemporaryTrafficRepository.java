package com.tomtom.itcu.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tomtom.itcu.entity.TemporarryTrafficDetails;

public interface TemporaryTrafficRepository extends CrudRepository<TemporarryTrafficDetails, String> {

    List<TemporarryTrafficDetails> getTemporaryDetails(String signalId);
}
