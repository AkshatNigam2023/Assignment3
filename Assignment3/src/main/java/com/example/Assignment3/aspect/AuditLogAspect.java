package com.example.Assignment3.aspect;

import com.example.Assignment3.model.AuditEntry;
import com.example.Assignment3.repository.AuditRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * An aspect is a way to modularize cross-cutting concerns, like logging, in your application. It's a separate module
 * that can be applied to various parts of your code.
 */
@Aspect
@Component
public class AuditLogAspect {

    /**
     * logger is a tool for writing log messages. It's used to record information about what the application is doing.
     */
    private static final Logger logger = LoggerFactory.getLogger(AuditLogAspect.class);

    @Autowired
    private AuditRepository auditRepository;


    /**
     * The auditPointcut method defines a pointcut, which specifies where in the application the aspect should be applied.
     * In this case, it targets methods in controller classes
     */
    @Pointcut("within(com.example.Assignment3.controller..*)")
    public void auditPointcut() {
    }

    /**
     *The @AfterReturning and @AfterThrowing annotations mark methods (afterReturning and afterThrowing) that should be
     *executed after a method in the target classes returns successfully or throws an exception, respectively.
     */
    @AfterReturning(pointcut = "auditPointcut()", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        saveAuditEntry(joinPoint);
    }
    @AfterThrowing(pointcut = "auditPointcut()", throwing = "exception")
    public void afterThrowing(JoinPoint joinPoint, Exception exception) {
        saveAuditEntry(joinPoint);
    }

    @Async
    private void saveAuditEntry(JoinPoint joinPoint) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();

            String methodName = method.getName();
            String parameters = Arrays.toString(joinPoint.getArgs());
            Instant timestamp = Instant.now();

            AuditEntry auditEntry = new AuditEntry();
            auditEntry.setMethodName(methodName);
            auditEntry.setParameters(parameters);
            auditEntry.setTimestamp(LocalDateTime.from(timestamp));

            auditRepository.save(auditEntry);

            logger.info("Audit entry saved - Method: {}, Parameters: {}, Timestamp: {}",
                    methodName, parameters, timestamp);
        } catch (Exception e) {
            logger.error("Exception during audit logging", e);
        }
    }
}

