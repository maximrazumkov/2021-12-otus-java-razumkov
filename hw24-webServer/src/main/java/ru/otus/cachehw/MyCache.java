package ru.otus.cachehw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;


public class MyCache<K, V> implements HwCache<K, V> {

    private static final Logger log = LoggerFactory.getLogger(MyCache.class);

    private final Map<K, V> map = new WeakHashMap<>();
    private final List<HwListener<K, V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        map.put(key, value);
        notice(key, value, "put");
    }

    @Override
    public void remove(K key) {
        V value = map.remove(key);
        notice(key, value, "remove");
    }

    @Override
    public V get(K key) {
        V value = map.get(key);
        notice(key, value, "get");
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listeners);
    }

    private void notice(K key, V value, String action) {
        listeners.forEach(listener -> {
            try {
                listener.notify(key, value, action);
            } catch (Exception e) {
                log.info("Не удалось выполнить действие: key:{}, value:{}, action: {}", key, value, action);
            }
        });
    }
}
