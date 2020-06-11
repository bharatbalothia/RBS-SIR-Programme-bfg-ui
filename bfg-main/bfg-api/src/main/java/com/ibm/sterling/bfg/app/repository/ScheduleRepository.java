package com.ibm.sterling.bfg.app.repository;

import com.ibm.sterling.bfg.app.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Modifying
    @Transactional
    @Query(value="DELETE FROM SCT_SCHEDULE WHERE SCHEDULE_ID = ?1", nativeQuery=true)
    void deleteScheduleById(Long id);

}
