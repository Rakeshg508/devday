package com.tomtom.itcu.repository;

import org.springframework.data.repository.CrudRepository;

import com.tomtom.itcu.entity.TrafficHistory;

public interface TrafficHistoryRepository extends CrudRepository<TrafficHistory, String> {

}
