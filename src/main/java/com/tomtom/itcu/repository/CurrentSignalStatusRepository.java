package com.tomtom.itcu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tomtom.itcu.entity.CurrentSignalStatus;
import com.tomtom.itcu.model.MasterTrafficInfoCurrentSignalStatusDTO;

public interface CurrentSignalStatusRepository extends CrudRepository<CurrentSignalStatus, Long> {

    @Override
    Optional<CurrentSignalStatus> findById(Long id);

    @Override
    CurrentSignalStatus save(CurrentSignalStatus entity);

    @Query("select new com.tomtom.itcu.model.MasterTrafficInfoCurrentSignalStatusDTO(mti.signalId,mti.defaultTime, "
        + "mti.lat,mti.lon,css.currentSignalTime) from MasterTrafficInfo mti left join CurrentSignalStatus css on mti.signalId =  css.signalId")
    List<MasterTrafficInfoCurrentSignalStatusDTO> fetchCurrentTrafficSignalInfo();

    @Query("select css from CurrentSignalStatus css where css.signalId=?1")
    Optional<CurrentSignalStatus> findBySignalId(String signalId);

    @Query("select new com.tomtom.itcu.model.MasterTrafficInfoCurrentSignalStatusDTO(mti.signalId,mti.defaultTime, mti.lat,mti.lon,\r\n"
        + "css.currentSignalTime)" + " from MasterTrafficInfo mti inner join CurrentSignalStatus css where mti.signalId=?1")
    List<MasterTrafficInfoCurrentSignalStatusDTO> fetchCurrentSignalInfoBySignalId(String signalId);

}
