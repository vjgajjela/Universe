package com.uid;

import java.net.NetworkInterface;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Enumeration;

/**
 * Distributed Sequence Generator. Inspired by Twitter snowflake
 */
public class UniqueIdUtil {

	private static final int NODE_ID_BITS = 10;
	private static final int SEQUENCE_BITS = 12;
	private static final long MAX_NODE_ID = (int) (Math.pow(2, NODE_ID_BITS) - 1);
	private static final long MAX_SEQUENCE = (int) (Math.pow(2, SEQUENCE_BITS) - 1);
	// Custom Epoch (January 1, 2015 Midnight UTC = 2015-01-01T00:00:00Z)
	private static final long CUSTOM_EPOCH = 1420070400000L;
	private final long nodeId;
	// Limit the algorith to generate 8 digit numbers, it increase is this needs to be changed
	private static final int MAX_SYSTEM_NUM = (int)Math.pow(10, 8);
	private volatile long lastTimestamp = -1L;
	private volatile long sequence = 0L;

	// Create Snowflake with a nodeId and custom epoch
	public UniqueIdUtil() {
		this.nodeId = createNodeId();
	}

	public synchronized long nextId() {
		long currentTimestamp = timestamp();
		if (currentTimestamp < lastTimestamp) {
			throw new IllegalStateException("Invalid System Clock!");
		}
		if (currentTimestamp == lastTimestamp) {
			sequence = (sequence + 1) & MAX_SEQUENCE;
			if (sequence == 0) {
				// Sequence Exhausted, wait till next millisecond.
				currentTimestamp = waitNextMillis(currentTimestamp);
			}
		} else {
			// reset sequence to start with zero for the next millisecond
			sequence = 0;
		}
		lastTimestamp = currentTimestamp;
		long id = currentTimestamp << (NODE_ID_BITS + SEQUENCE_BITS) | (nodeId << SEQUENCE_BITS) | sequence;
		return id % MAX_SYSTEM_NUM;
	}

	// Get current timestamp in milliseconds, adjust for the custom epoch.
	private long timestamp() {
		return Instant.now().toEpochMilli() - CUSTOM_EPOCH;
	}

	// Block and wait till next millisecond
	private long waitNextMillis(long currentTimestamp) {
		while (currentTimestamp == lastTimestamp) {
			currentTimestamp = timestamp();
		}
		return currentTimestamp;
	}

	private long createNodeId() {
		long currentNodeId;
		try {
			StringBuilder sb = new StringBuilder();
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			while (networkInterfaces.hasMoreElements()) {
				NetworkInterface networkInterface = networkInterfaces.nextElement();
				byte[] mac = networkInterface.getHardwareAddress();
				if (mac != null) {
					for (byte macPort : mac) {
						sb.append(String.format("%02X", macPort));
					}
				}
			}
			currentNodeId = sb.toString().hashCode();
		} catch (Exception ex) {
			currentNodeId = (new SecureRandom().nextInt());
		}
		currentNodeId = currentNodeId & MAX_NODE_ID;
		return currentNodeId;
	}
}