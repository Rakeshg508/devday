package com.tomtom.itcu.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.tomtom.itcu.entity.TemporarryTrafficDetails;

public interface TemporaryTrafficRepository extends CrudRepository<TemporarryTrafficDetails, String> {

    @Override
    Optional<TemporarryTrafficDetails> findById(String id);

    @Override
    TemporarryTrafficDetails save(TemporarryTrafficDetails tempTrafficDetails);
}
