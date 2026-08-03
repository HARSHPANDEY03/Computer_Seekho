package com.example.aspects;

import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceLoggingAspect {

    private static final Logger log =
            LoggerFactory.getLogger(
                    ServiceLoggingAspect.class
            );

    @Around(
        "execution(public * com.example.services..*(..))"
    )
    public Object logServiceMethod(
            ProceedingJoinPoint joinPoint)
            throws Throwable {

        String className =
                joinPoint.getSignature()
                        .getDeclaringType()
                        .getSimpleName();

        String methodName =
                joinPoint.getSignature()
                        .getName();

        long startTime =
                System.nanoTime();

        log.info(
                "SERVICE START: {}.{}",
                className,
                methodName
        );

        try {

            /*
             * This line runs the real service method.
             */
            Object result =
                    joinPoint.proceed();

            long endTime =
                    System.nanoTime();

            long duration =
                    TimeUnit.NANOSECONDS.toMillis(
                            endTime - startTime
                    );

            log.info(
                    "SERVICE SUCCESS: {}.{} completed in {} ms",
                    className,
                    methodName,
                    duration
            );

            return result;

        } catch (Throwable exception) {

            long endTime =
                    System.nanoTime();

            long duration =
                    TimeUnit.NANOSECONDS.toMillis(
                            endTime - startTime
                    );

            log.error(
                    "SERVICE FAILED: {}.{} after {} ms. Error={}",
                    className,
                    methodName,
                    duration,
                    exception.getMessage()
            );

            /*
             * Do not remove this.
             * We must send the exception forward.
             */
            throw exception;
        }
    }
}