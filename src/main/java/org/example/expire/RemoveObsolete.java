package org.example.expire;

import org.example.cache.LruCache;

public class RemoveObsolete<KEY, VALUE> extends Thread {

    private LruCache<KEY, VALUE> cache;
    private long frequency;
    private boolean working;

    public RemoveObsolete(LruCache cache, long frequency) {
        this.cache = cache;
        this.frequency = frequency;
        this.working = true;
    }

    public void setWorking(boolean working) {
        this.working = working;
    }

    @Override
    public void run() {
        while (working) {
            try {
                Thread.sleep(frequency);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cache.removeExpiredNodes();
        }
    }

}
