package com.xmacedo.redis_clone.controller;

import com.xmacedo.redis_clone.service.DataStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RedisCloneController {

    @Autowired
    private DataStoreService dataStoreService;


    @PostMapping("/set")
    public ResponseEntity<?> set(@RequestParam String key, @RequestParam String value) {
        dataStoreService.set(key, value);

        return ResponseEntity.status(HttpStatus.CREATED).body("ok");
    }

    @GetMapping("/get")
    public ResponseEntity<?> get(@RequestParam String key) {
        return ResponseEntity.ok(dataStoreService.get(key));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> delete(@RequestParam String key) {
        dataStoreService.remove(key);
        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }

    @PostMapping("/append")
    public ResponseEntity<?> append(@RequestParam String key, @RequestParam String value) {
        dataStoreService.append(key, value);

        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }
}
