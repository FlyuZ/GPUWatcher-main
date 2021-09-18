package com.example.webfluxtest.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/api/")
public class BaseController {
    @Autowired
    GpuUtils gpuUtils;


    @GetMapping("/sse")
    public Flux<ServerSentEvent<String>> randomNumbers() {
        Flux<ServerSentEvent<String>> sse = Flux.interval(Duration.ofSeconds(5)).map(val ->{
            return  ServerSentEvent.<String>builder()
//                    .id(UUID.randomUUID().toString())
//                    .event("info")
                    .data(gpuUtils.nowgpuinfo)
                    .build();
        });
        return sse;
    }
}