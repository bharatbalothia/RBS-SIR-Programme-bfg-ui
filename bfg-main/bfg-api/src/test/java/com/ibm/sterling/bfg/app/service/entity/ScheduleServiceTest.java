package com.ibm.sterling.bfg.app.service.entity;

import com.ibm.sterling.bfg.app.model.entity.Entity;
import com.ibm.sterling.bfg.app.model.entity.Schedule;
import com.ibm.sterling.bfg.app.repository.entity.ScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class ScheduleServiceTest {

    @InjectMocks
    private ScheduleService service;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private EntityService entityService = new EntityServiceImpl();

    private Schedule schedule;
    private Entity entity;
    private List<Schedule> scheduleList;

    @BeforeEach
    void setUp() {

        entityService = new EntityServiceImpl();
        schedule = new Schedule();
        schedule.setScheduleId(1L);
        entity = new Entity();
        entity.setEntityId(1000);
        scheduleList = Collections.singletonList(schedule);
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void findActualSchedulesByEntityIdShouldReturnListOfSchedules() {
        entity.setSchedules(scheduleList);
        when(entityService.findById(anyInt())).thenReturn(Optional.of(entity));
        assertEquals(scheduleList, service.findActualSchedulesByEntityId(entity.getEntityId()));
    }

    @Test
    void findActualSchedulesByEntityIdShouldReturnNull() {
        when(entityService.findById(anyInt())).thenReturn(Optional.empty());
        assertNull(service.findActualSchedulesByEntityId(entity.getEntityId()));
    }

    @Test
    void deleteById() {
        doNothing().when(scheduleRepository).deleteScheduleById(anyLong());
        service.deleteById(schedule.getScheduleId());
        verify(scheduleRepository, times(1)).deleteScheduleById(schedule.getScheduleId());
    }
}