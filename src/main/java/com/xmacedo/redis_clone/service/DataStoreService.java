package com.xmacedo.redis_clone.service;

import com.xmacedo.redis_clone.model.Store;
import org.springframework.stereotype.Service;
import java.util.HashMap;

@Service
public class DataStoreService {
    //(Strings: set, get, remove, append, maps:set get keys, values
    private Store store = new Store(new HashMap<>());

    public void set(String key, String value) {
        store.getStore().put(key, value);
    }

    public String get(String key) {
        Object value = store.getStore().get(key);
        return value instanceof String ? (String) value : null;
    }

    public void remove(String key) {
        store.getStore().remove(key);
    }

    public void append(String key, String value) {
        if (store.getStore().containsKey(key) && store.getStore().get(key) instanceof String) {
            store.getStore().put(key, store.getStore().get(key) + value);
        } else {
            store.getStore().put(key, value);
        }
    }

}
