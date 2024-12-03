package com.xmacedo.redis_clone.service;

import com.xmacedo.redis_clone.model.Store;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

//todo do a partition
public class DataStoreService {
    //(Strings: set, get, remove, append, maps:set get keys, values
    private final Store store;
    private final ScheduledExecutorService scheduledExecutorService;
    private final Long DEFAULT_TTL = 60L;

    public DataStoreService(){
        this.store = new Store(new HashMap<>(), new HashMap<>()); // todo investigate how to do this with concurrent, and methods as Syncronyzed
        this.scheduledExecutorService = Executors.newScheduledThreadPool(1);

        scheduledExecutorService.scheduleAtFixedRate(this::removeExpiredKeys,1,1, TimeUnit.SECONDS);
    }

    public void set(String key, String value) {
        store.getStore().put(key, value);
        //added ttl by default
        store.getTtlStore().put(key, System.currentTimeMillis() + DEFAULT_TTL * 1000);
    }

    public String get(String key) {
        //Check if this key has not expired
        if(isExpired(key)){
            remove(key);
            return null;
        }

        Object value = store.getStore().get(key);
        return value instanceof String ? (String) value : null;
    }

    public void remove(String key) {
        store.getStore().remove(key);
        store.getTtlStore().remove(key);
    }

    public void append(String key, String value) {
        if (store.getStore().containsKey(key) && store.getStore().get(key) instanceof String) {
            store.getStore().put(key, store.getStore().get(key) + value);
        } else {
            store.getStore().put(key, value);
        }
        store.getTtlStore().remove(key);
    }
    public void mapSet(String mapKey, String key, String value){
        store.getStore().computeIfAbsent(mapKey, k -> new HashMap<String, String>());
        ((Map<String, String>) store.getStore().get(mapKey)).put(key, value);
    }

    public String mapGet(String mapKey, String key){
        if(store.getStore().containsKey(mapKey) && store.getStore().get(mapKey) instanceof Map){
            return ((Map<String, String>) store.getStore().get(mapKey)).get(key);
        }
        return null;
    }

    public Map<String, String> mapKeys(String mapKey){
        if(store.getStore().containsKey(mapKey) && store.getStore().get(mapKey) instanceof Map){
            return (Map<String, String>) store.getStore().get(mapKey);
        }
        return null;
    }

    public long getRemainingTTL(String key) {
        if (!store.getTtlStore().containsKey(key)) {
            return -1;
        }
        long expirationTime = store.getTtlStore().get(key);
        return Math.max(0, (expirationTime - System.currentTimeMillis()) / 1000);
    }

    //todo check by key, if not expired
    private boolean isExpired(String key){
        if(!store.getTtlStore().containsKey(key)){
            return false;
        }
        return store.getTtlStore().get(key) <= System.currentTimeMillis();
    }

    private void removeExpiredKeys(){
        Long now = System.currentTimeMillis();
        store.getTtlStore().entrySet().removeIf(entry -> {
            //value is ttl
           if(entry.getValue() <= now){
               store.getTtlStore().remove(entry.getKey());
               return true;
           }
           return false;
        });
    }

}
