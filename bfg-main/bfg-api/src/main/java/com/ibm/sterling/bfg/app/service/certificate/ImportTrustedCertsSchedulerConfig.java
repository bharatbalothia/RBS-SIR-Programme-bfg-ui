package com.ibm.sterling.bfg.app.service.certificate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.sterling.bfg.app.service.PropertyService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.util.Date;

@Configuration
@EnableScheduling
public class ImportTrustedCertsSchedulerConfig implements SchedulingConfigurer {

    private static final Logger LOG = LogManager.getLogger(ImportTrustedCertsSchedulerConfig.class);

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private ImportedTrustedCertificateService importedTrustedCertificateService;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        Runnable runnable = () -> {
            LOG.info("Trigger import task executed at {}", new Date());
            try {
                importedTrustedCertificateService.importCertificatesFromB2B();
            } catch (JsonProcessingException e) {
                LOG.error("FAIL on import the trusted certificates from B2BI");
            }
        };
        Trigger trigger = triggerContext -> {
            CronTrigger cronTrigger = new CronTrigger(propertyService.getTrustedCertsImportSchedule());
            return cronTrigger.nextExecutionTime(triggerContext);
        };
        taskRegistrar.addTriggerTask(runnable, trigger);
    }
}
