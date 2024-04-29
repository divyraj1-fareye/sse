package com.techprimers.springboot.serverSentEventOnConnection;

import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class SSEControllerWithConnection {

    private final Map<String, FluxSink<ServerSentEvent<String>>> connections;

    public SSEControllerWithConnection() {
        this.connections = new ConcurrentHashMap<>();
    }

    @GetMapping("/connect/{userId}")
    public Flux<ServerSentEvent<String>> connect(@PathVariable String userId) {
        return Flux.<ServerSentEvent<String>>create(emitter -> {
            connections.put(userId, emitter);
            emitter.onDispose(() -> connections.remove(userId));
       });
    }

    @GetMapping("/disconnect/{userId}")
    public void disconnect(@PathVariable String userId) {
        connections.remove(userId);
    }

    @GetMapping("/emit/{userId}/{data}/{event}")
    public void emitEvent(@PathVariable String userId, @PathVariable String data, @PathVariable String event) {
        FluxSink<ServerSentEvent<String>> sink = connections.get(userId);
        if (sink != null) {
            ServerSentEvent<String> sseEvent = ServerSentEvent.builder(data)
                    .event(event) // Optional event name
                    .build();
            sink.next(sseEvent);
        }
    }

}
