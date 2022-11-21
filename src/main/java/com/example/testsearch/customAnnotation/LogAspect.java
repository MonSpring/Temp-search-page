package com.example.testsearch.customAnnotation;

import com.example.testsearch.config.SlackConfig;
import com.example.testsearch.mailing.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;


@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
public class LogAspect {

    private final StopWatchRepository stopWatchRepository;

    private final EmailService emailService;

    private final SlackConfig slackConfig;

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
            String text = stopWatchTable.getMethod() + " 서비스 메소드에서 " + stopWatchTable.getMills() / 1000 + "초 이상 걸리는 Slow Query가 발생했습니다";
            SlackConfig.send(text);
        }

        return proceed;
    }
}
