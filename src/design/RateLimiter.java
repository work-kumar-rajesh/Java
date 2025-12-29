package design;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class RateLimiter {

    private final ConcurrentHashMap<String, Deque<Long>> userMapping = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ReentrantLock> userLocks = new ConcurrentHashMap<>();

    private final int maxRequests;
    private final int timeLimit; // in seconds

    public RateLimiter(int maxRequests, int windowSeconds) {
        this.maxRequests = maxRequests;
        this.timeLimit = windowSeconds;
    }

    public boolean allowRequest(String userId) {
        long now = System.currentTimeMillis();

        // Get per-user lock
        ReentrantLock lock = userLocks.computeIfAbsent(userId, k -> new ReentrantLock());
        lock.lock();
        try {
            // Get or create queue for this user
            Deque<Long> userRequests = userMapping.computeIfAbsent(userId, k -> new ArrayDeque<>());

            // Remove expired requests
            while (!userRequests.isEmpty()) {
                long front = userRequests.peek();
                if ((now - front) / 1000 >= timeLimit) {
                    userRequests.poll();
                } else {
                    break;
                }
            }

            if (userRequests.size() >= maxRequests) {
                return false;
            }

            userRequests.offer(now);
            return true;

        } finally {
            lock.unlock();
        }
    }
}
