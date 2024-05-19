package org.example.cache;

import org.example.expire.RemoveObsolete;
import org.example.node.CacheNode;

import java.util.HashMap;
import java.util.Map;

public class LruCache<KEY, VALUE> {

    private CacheNode<KEY, VALUE> start;
    private CacheNode<KEY, VALUE> end;
    private int capacity;
    private int size;
    private Map<KEY, CacheNode<KEY, VALUE>> m;
    private RemoveObsolete<KEY, VALUE> removeObsolete;

    public LruCache(int capacity, long frequency) {
        this.capacity = capacity;
        size = 0;
        start = null;
        end = null;
        m = new HashMap<>();
        removeObsolete = new RemoveObsolete<KEY, VALUE>(this, frequency);
        removeObsolete.start();
    }

    public int getSize() {
        return size;
    }

    public void close() {
        removeObsolete.setWorking(false);
    }

    public VALUE get(KEY key) {
        if(!m.containsKey(key)) {
            return null;
        }

        if(key.equals(start.getKey())) {
            if(start.getExpirationTime() != null &&
                    System.currentTimeMillis() > start.getExpirationTime()) {
                return null;
            } else {
                return start.getValue();
            }
        }

        CacheNode<KEY, VALUE> curr = m.get(key);
        curr.getPrev().setNext(curr.getNext());
        if(curr.getNext() == null) {
            end = curr.getPrev();
        } else {
            curr.getNext().setPrev(curr.getPrev());
        }

        curr.setPrev(null);
        curr.setNext(start);
        start.setPrev(curr);
        start = curr;

        if(curr.getExpirationTime() != null &&
                System.currentTimeMillis() > curr.getExpirationTime()) {
            return null;
        } else {
            return curr.getValue();
        }
    }

    public void put(KEY key, VALUE value, Long ttl) {
        if(m.containsKey(key)) {
            get(key);
            CacheNode<KEY, VALUE> curr = m.get(key);
            curr.setValue(value);
            curr.setExpirationTime(ttl == null ? null : System.currentTimeMillis() + ttl);
            m.put(key,curr);
            return;
        }

        if(start == null) {
            start = new CacheNode<>();
            end = start;
            start.setKey(key);
            start.setValue(value);
            start.setExpirationTime(ttl == null ? null : System.currentTimeMillis() + ttl);
            m.put(key, start);
            size++;
            return;
        }

        if(size >= capacity) {
            size--;
            m.remove(end.getKey());
            end = end.getPrev();
            if(capacity == 1) {
                size = 1;
                CacheNode<KEY, VALUE> curr = new CacheNode<>();
                curr.setKey(key);
                curr.setValue(value);
                curr.setExpirationTime(ttl == null ? null : System.currentTimeMillis() + ttl);
                start = curr;
                end = curr;
                m.put(key, curr);
                return;
            }
            end.setNext(null);
        }

        size++;
        CacheNode<KEY, VALUE> curr = new CacheNode<>();
        curr.setKey(key);
        curr.setValue(value);
        curr.setExpirationTime(ttl == null ? null : System.currentTimeMillis() + ttl);
        curr.setPrev(null);
        start.setPrev(curr);
        curr.setNext(start);
        m.put(key, curr);
        start = curr;
    }

    public void put(KEY key, VALUE value) {
        put(key, value, null);
    }

    public void removeExpiredNodes() {
        CacheNode<KEY, VALUE> curr = start;

        while (curr != null) {
            if(curr.getExpirationTime() == null ||
                    System.currentTimeMillis() < curr.getExpirationTime()) {
                curr = curr.getNext();
                continue;
            }

            size--;
            m.remove(curr.getKey());
            if(start.equals(curr)){
                start = curr.getNext();
                if(capacity == 1) {
                    end = start;
                    continue;
                }
                start.setPrev(null);
                curr.setNext(null);
                curr = start;
                continue;
            }

            if(end.equals(curr)) {
                end = curr.getPrev();
                end.setNext(null);
                curr.setPrev(null);
                break;
            }

            curr.getPrev().setNext(curr.getNext());
            curr.getNext().setPrev(curr.getPrev());
            curr = curr.getNext();
        }
    }
}
