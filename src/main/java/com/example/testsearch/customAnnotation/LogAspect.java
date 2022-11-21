package com.example.testsearch.customAnnotation;

import com.example.testsearch.mailing.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.transaction.Transactional;

@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
public class LogAspect {

    private final StopWatchRepository stopWatchRepository;

    private final EmailService emailService;

    @Around("@annotation(LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Object proceed = joinPoint.proceed();

        stopWatch.stop();
        log.info("total mills : " + stopWatch.getTotalTimeMillis() + " ms");
        log.info("total nanos : " + stopWatch.getTotalTimeNanos() + " ns");
        log.info("method name : " + joinPoint.getSignature().getName());

        StopWatchTable stopWatchTable = StopWatchTable.builder()
                .method(joinPoint.getSignature().getName())
                .mills(stopWatch.getTotalTimeMillis())
                .nanos(stopWatch.getTotalTimeNanos())
                .build();

        stopWatchRepository.save(stopWatchTable);

        // SlowQuery Checking & Send To BackEnd Developers
        if(stopWatchTable.getMills() > 10000) {
            emailService.sendMailDevelopersToSlowQuery(stopWatchTable);
        }

        return proceed;
    }
}
