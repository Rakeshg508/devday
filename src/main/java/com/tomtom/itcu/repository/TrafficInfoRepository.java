package com.tomtom.itcu.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tomtom.itcu.entity.MasterTrafficInfo;

public interface TrafficInfoRepository extends CrudRepository<MasterTrafficInfo, String> {

    List<MasterTrafficInfo> findBySignalId(String signalId);

}
