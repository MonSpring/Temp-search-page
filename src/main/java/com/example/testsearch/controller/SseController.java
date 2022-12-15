package com.example.testsearch.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Controller
@RequiredArgsConstructor
public class SseController {

    @GetMapping(value = "/sseTest")
    public String sseTest() {
        return "sseTest";
    }

    private static final Map<String, SseEmitter> CLIENTS = new ConcurrentHashMap<>();

    @ResponseBody
    @GetMapping("/api/subscribe")
    public SseEmitter subscribe(String id) {

        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        CLIENTS.put(id, emitter);

        emitter.onError((e) -> CLIENTS.remove(id));
        emitter.onTimeout(() -> CLIENTS.remove(id));
        emitter.onCompletion(() -> CLIENTS.remove(id));
        return emitter;
    }

    @ResponseBody
    @GetMapping("/api/publish")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public void publish(String message) {

        Set<String> deadIds = new HashSet<>();

        String eventFormatted = new JSONObject()
                .put("메세지", message).toString();


        CLIENTS.forEach((id, emitter) -> {
            try {
                emitter.send(SseEmitter.event().data(eventFormatted));
            } catch (Exception e) {
                deadIds.add(id);
                log.warn("disconnected id : {}", id);
            }
        });

        deadIds.forEach(CLIENTS::remove);
    }

}