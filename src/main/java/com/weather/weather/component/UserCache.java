package com.weather.weather.component;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Data
public class UserCache {

    public static final int MAX_CACHE_SIZE = 10;
    private final Map<String, Object> hashMap = new LinkedHashMap<>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, Object> eldest) {
            return size() > MAX_CACHE_SIZE;
        }
    };

    public void addToCache(String key, Object value) {
        hashMap.put(key, value);
    }

    public Object getFromCache(String key) {
        return hashMap.get(key);
    }

    public void removeFromCache(String key) {
        hashMap.remove(key);
    }

    public void clearCache() {
        hashMap.clear();
    }

}
