package com.tomtom.itcu.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.tomtom.itcu.entity.CurrentSignalStatus;

public interface CurrentSignalStatusRepository extends CrudRepository<CurrentSignalStatus, String> {

    @Override
    Optional<CurrentSignalStatus> findById(String id);

    @Override
    CurrentSignalStatus save(CurrentSignalStatus entity);

}
