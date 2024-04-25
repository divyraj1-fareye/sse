package com.techprimers.springboot.db;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;


@Component
public class SSEEventEmitter {


    private Flux<ServerSentEvent<String>> eventFlux =null;

    private FluxSink<ServerSentEvent<String>> eventSink =null;

    public SSEEventEmitter() {
        this.eventFlux = Flux.<ServerSentEvent<String>>create(sink -> this.eventSink = sink, FluxSink.OverflowStrategy.IGNORE).share(); // Share the flux among subscribers
        this.eventFlux.subscribe();
    }

    public void emitSSEEvent(String event, String data) {
        // Emit SSE event to clients with the provided data
        // You can customize the event type, data, and criteria as needed
        ServerSentEvent<String> sseEvent = ServerSentEvent.builder(data)
                .event(event)// Use criteria as event ID for uniqueness
                .build();
        eventSink.next(sseEvent);
    }

    public Flux<ServerSentEvent<String>> getEventFlux() {
        return eventFlux;
    }


}
