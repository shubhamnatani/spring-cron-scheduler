package com.cron;

import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.logging.Logger;

@Service
public class PriceEngine {
    static final Logger LOGGER =
            Logger.getLogger(PriceEngine.class.getName());
    private Double price;

    public Double getProductPrice() {
        return price;
    }
    /*fixedDelay/fixedDelayString attribute to configure a job to run after a fixed delay which means the interval between the end of the previous job and the beginning of the new job is fixed. */
    /*it will wait for the first execution to complete and after fixed delay to start next*/
    // 1. @Scheduled(fixedDelay = 2000)spring.task.scheduling.pool.size

    /*fixedRate/fixedRateString attribute to configure a job to run after a fixed amount of time it will not check either previous job is completed tasks or not
     * so to avoid this either we need to use @Async or spring.task.scheduling.pool.size to allow multiple execution in parallel during the overlapped time interval*/
    // 2. @Scheduled(fixedRate = 3000) || @Scheduled(fixedDelayString = "PT02S")) PT02S

    /* initialDelay attribute to configure first job execution after context is initialized with specific delay*/
    // 3.@Scheduled(initialDelay = 2000, fixedRate = 3000)

    /* cron attribute to specify the time interval in UNIX style cron-like expression */
    // 4. @Scheduled(cron = "${interval-in-cron}")

    /* cron macro: hourly/yearly/monthly/weekly/daily instead of the less readable cron expression 0 0 * * * *   */
//    @Scheduled(cron = "@hourly")
//    @SchedulerLock(name = "TaskScheduler_scheduledTask", lockAtLeastFor = "PT5M", lockAtMostFor = "PT14M")

    @Scheduled(cron = "*/2 * * * * *")
    @SchedulerLock(
            name = "UNIQUE_KEY_FOR_SHEDLOCK_SCHEDULER",
            lockAtLeastFor = "PT5S", // lock for at least a minute, overriding defaults
            lockAtMostFor = "PT10S" // lock for at most 7 minutes
    )
    public void computePrice() throws InterruptedException {
        // To assert that the lock is held (prevents misconfiguration errors)
        LockAssert.assertLocked();
        LOGGER.info("Do other things ...");
        LOGGER.info("computing price at " +
                LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        Thread.sleep(4000);
    }
}
