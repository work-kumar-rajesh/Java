package design;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class LRUCache {

    private final int capacity;
    private final Map<Integer, Node> map;
    private final Node head, tail;
    private final ReentrantLock lock;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>();
        this.lock = new ReentrantLock();

        head = new Node(-1, -1);
        tail = new Node(-1, -1);
        head.next = tail;
        tail.prev = head;
    }

    public int get(int key) {
        lock.lock();
        try {
            Node node = map.get(key);
            if (node == null) return -1;

            moveToHead(node);
            return node.value;
        } finally {
            lock.unlock();
        }
    }

    public void put(int key, int value) {
        lock.lock();
        try {
            Node node = map.get(key);

            if (node != null) {
                node.value = value;
                moveToHead(node);
                return;
            }

            Node newNode = new Node(key, value);
            map.put(key, newNode);
            addToHead(newNode);

            if (map.size() > capacity) {
                Node lru = removeTail();
                map.remove(lru.key);
            }
        } finally {
            lock.unlock();
        }
    }

    private void addToHead(Node node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }

    private void moveToHead(Node node) {
        removeNode(node);
        addToHead(node);
    }

    private void removeNode(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private Node removeTail() {
        Node lru = tail.prev;
        removeNode(lru);
        return lru;
    }
}

class Node {
    int key;
    int value;
    Node prev, next;

    Node(int key, int value) {
        this.key = key;
        this.value = value;
    }
}
