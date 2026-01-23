package com.bismark.serviceboilerplate.service;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

abstract class AbstractEventHandlerScheduler<T> {
    private static final long INTERVAL_TIME = 1000;
    private static final long MAX_QUEUE_SIZE = 50;
    private static final long CORE_POOL_SIZE = 1;
    private static final long MAX_POOL_SIZE = 10;
    private static final long KEEP_ALIVE_MSEC = 1000;
    @Getter private long maxPoolSize;
    @Getter private long maxQueueSize;
    private ThreadPoolExecutor executor;

    @PostConstruct
    public void init() {
        maxPoolSize = AbstractEventHandlerScheduler.MAX_POOL_SIZE;
        executor = new ThreadPoolExecutor((int) AbstractEventHandlerScheduler.CORE_POOL_SIZE, (int) maxPoolSize, AbstractEventHandlerScheduler.KEEP_ALIVE_MSEC, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>((int) maxQueueSize));
    }

    public AbstractEventHandlerScheduler(Long newMaxQueueSize) {
        if (newMaxQueueSize != null) {
            maxQueueSize = newMaxQueueSize;
        }
    }

    public AbstractEventHandlerScheduler() {
        maxQueueSize = AbstractEventHandlerScheduler.MAX_QUEUE_SIZE;
    }

    public void setMaxPoolSize(long newMaxPoolSize) {
        maxPoolSize = newMaxPoolSize;
        executor.setMaximumPoolSize((int) newMaxPoolSize);
    }

    @Scheduled(fixedRate = AbstractEventHandlerScheduler.INTERVAL_TIME)
    private void recordEventHandler() {
        if (executor.getQueue().size() < AbstractEventHandlerScheduler.MAX_QUEUE_SIZE) {
            getEventIds().forEach(eventId -> {
                Runnable eventHandler = getEventHandler(eventId);
                executor.execute(eventHandler);
            });
        }
    }

    ThreadPoolExecutor getExecutor() {
        return executor;
    }

    /* user defined to get list of event id */
    public abstract List<T> getEventIds();
    /* user defined to handle an event id */
    public abstract Runnable getEventHandler(T eventId);
}
