package com.xmacedo.redis_clone.service;

import com.xmacedo.redis_clone.model.Store;
import java.util.HashMap;
import java.util.Map;

//todo do a partition
public class DataStoreService {
    //(Strings: set, get, remove, append, maps:set get keys, values
    private Store store = new Store(new HashMap<>(), new HashMap<>()); // todo investigate how to do this with concurrent, and methods as Syncronyzed

    //Scheduler?

    public void set(String key, String value) {
        store.getStore().put(key, value);
    }

    public String get(String key) {
        //Check if this key has not expired
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

    //todo check by key, if not expired

}
