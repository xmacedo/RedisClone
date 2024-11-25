package com.xmacedo.redis_clone.model;

import lombok.Data;
import java.util.Map;

@Data
public class Store {

    private final Map<String, Object> store;
    private final Map<String, Long> ttlStore;
}
