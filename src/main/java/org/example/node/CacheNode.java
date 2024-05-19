package org.example.node;

public class CacheNode<KEY, VALUE> {
    public CacheNode() { }
    private CacheNode<KEY, VALUE> next;
    private CacheNode<KEY, VALUE> prev;
    private Long expirationTime;
    private KEY key;
    private VALUE value;

    public CacheNode<KEY, VALUE> getNext() {
        return next;
    }

    public void setNext(CacheNode<KEY, VALUE> next) {
        this.next = next;
    }

    public CacheNode<KEY, VALUE> getPrev() {
        return prev;
    }

    public void setPrev(CacheNode<KEY, VALUE> prev) {
        this.prev = prev;
    }

    public KEY getKey() {
        return key;
    }

    public void setKey(KEY key) {
        this.key = key;
    }

    public VALUE getValue() {
        return value;
    }

    public void setValue(VALUE value) {
        this.value = value;
    }

    public Long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Long expirationTime) {
        this.expirationTime = expirationTime;
    }
}
