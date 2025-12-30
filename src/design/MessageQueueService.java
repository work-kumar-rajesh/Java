package design;

import java.util.*;
import java.util.concurrent.*;

public class MessageQueueService {

    // Map of topic -> queue of messages
    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> topics = new ConcurrentHashMap<>();

    // Publish a message to a topic
    public void publish(String topic, String message) {
        ConcurrentLinkedQueue<String> queue = topics.computeIfAbsent(topic, k -> new ConcurrentLinkedQueue<>());
        queue.offer(message); // thread-safe
        System.out.println(Thread.currentThread().getName() + " published: " + message + " to topic: " + topic);
    }

    // Consume a message from a topic (returns null if empty)
    public String consume(String topic) {
        ConcurrentLinkedQueue<String> queue = topics.get(topic);
        if (queue == null) return null; // topic doesn't exist
        String message = queue.poll();   // thread-safe removal
        if (message != null) {
            System.out.println(Thread.currentThread().getName() + " consumed: " + message + " from topic: " + topic);
        }
        return message;
    }

    // Optional: blocking consume
    public String consumeBlocking(String topic) throws InterruptedException {
        while (true) {
            String msg = consume(topic);
            if (msg != null) return msg;
            Thread.sleep(50); // wait for messages
        }
    }
}

class Main {
    public static void main(String[] args) {
        MessageQueueService service = new MessageQueueService();

        // Producers
        for (int i = 1; i <= 5; i++) {
            int id = i;
            new Thread(() -> service.publish("topic1", "msg" + id), "Producer-" + id).start();
        }

        // Consumers
        for (int i = 1; i <= 5; i++) {
            int id = i;
            new Thread(() -> {
                String msg = service.consume("topic1");
                System.out.println("Consumer-" + id + " got: " + msg);
            }, "Consumer-" + id).start();
        }
    }
}
