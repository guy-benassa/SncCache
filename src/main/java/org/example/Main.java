package org.example;

import org.example.cache.LruCache;

import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        LruCache<Integer,String> cache = new LruCache<>(2, 3000);
        System.out.println(" cache put 1 first");
        cache.put(1, "first");
        System.out.println(" cache put 2 second");
        cache.put(2, "second");
        String value = cache.get(1);
        System.out.println(" assert cache get 1 is first");
        assert "first".equals(value);

        System.out.println(" cache put 2 third");
        cache.put(2,"third");
        System.out.println(" cache put 3 third");
        cache.put(3,"third");

        value = cache.get(2);
        System.out.println(" assert cache get 2 is third");
        assert "third".equals(value);

        value = cache.get(1);
        System.out.println(" since capacity is 2 assert element 1 is not cached");
        assert value == null;

        System.out.println(" cache put 2 forth. Adding ttl");
        cache.put(2, "forth", 500L);
        value = cache.get(2);
        System.out.println(" assert cache get 2 is forth before expires");
        assert "forth".equals(value);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(" waiting one second now element is expired");
        value = cache.get(2);
        assert value == null;
        System.out.println(" but cache is still not clean");
        assert cache.getSize() == 2;

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(" assert cache is now cleaned");
        assert cache.getSize() == 1;


        cache.close();

    }
}