package com.jinelei.scalefish.config;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.net.NetworkInterface;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Enumeration;

public class SnowflakeIdGenerator implements IdentifierGenerator {

    private static final Logger log = LoggerFactory.getLogger(SnowflakeIdGenerator.class);

    private static final long EPOCH = Instant.parse("2025-01-01T00:00:00Z").toEpochMilli();

    private static final long WORKER_ID_BITS = 6L;
    private static final long SEQUENCE_BITS = 5L;

    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);

    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

    private static final long workerId = initWorkerId();

    private long lastTimestamp = -1L;
    private long sequence = 0L;

    private static long initWorkerId() {
        long id;
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (interfaces != null) {
                StringBuilder sb = new StringBuilder();
                while (interfaces.hasMoreElements()) {
                    NetworkInterface ni = interfaces.nextElement();
                    byte[] mac = ni.getHardwareAddress();
                    if (mac != null) {
                        for (byte b : mac) {
                            sb.append(String.format("%02x", b));
                        }
                    }
                }
                id = sb.toString().hashCode() & MAX_WORKER_ID;
            } else {
                id = new SecureRandom().nextInt((int) MAX_WORKER_ID + 1);
            }
        } catch (Exception e) {
            id = new SecureRandom().nextInt((int) MAX_WORKER_ID + 1);
        }
        log.info("SnowflakeIdGenerator initialized: workerId={}", id);
        return id;
    }

    @Override
    public synchronized Serializable generate(SharedSessionContractImplementor session, Object object) {
        long timestamp = System.currentTimeMillis();

        if (timestamp < lastTimestamp) {
            log.warn("Clock moved backwards! Waiting for next millisecond. lastTimestamp={}, timestamp={}", lastTimestamp, timestamp);
            timestamp = waitForNextMillis(lastTimestamp);
        }

        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) {
                timestamp = waitForNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        long id = ((timestamp - EPOCH) << TIMESTAMP_SHIFT)
            | (workerId << WORKER_ID_SHIFT)
            | sequence;
        log.trace("Generated snowflake id: {}", id);
        return id;
    }

    private long waitForNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }
}
