package com.ibm.sterling.bfg.app.service;

import com.ibm.sterling.bfg.app.model.Entity;
import com.ibm.sterling.bfg.app.model.Schedule;
import com.ibm.sterling.bfg.app.repository.ScheduleRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ScheduleService {
    private static final Logger LOG = LogManager.getLogger(ScheduleService.class);
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private EntityService entityService;

    public List<Schedule> findActualSchedulesByEntityId(Integer id) {
        Entity entity = entityService.findById(id).orElse(null);
        return Optional.ofNullable(entity).map(entity1 -> entity.getSchedules()).orElse(null);
    }

    public void deleteById(Long id) {
        LOG.info("Try to delete schedule with id {}", id);
        scheduleRepository.deleteScheduleById(id);
    }
}
