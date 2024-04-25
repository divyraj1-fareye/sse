package com.techprimers.springboot.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SSEService {

    private  SSEEventEmitter sseEventEmitter;

    private  DataBaseUpdateListener databaseUpdateListener;

    @Autowired
    public SSEService() {
        this.sseEventEmitter = new SSEEventEmitter(); // Manually create SSEEventEmitter
        this.databaseUpdateListener = new DataBaseUpdateListener(sseEventEmitter); // Pass the manually created instance to DatabaseUpdateListener
    }

    public  Flux<ServerSentEvent<String>> getEventFlux() {
        return sseEventEmitter.getEventFlux();
    }

    public String listenForDatabaseUpdates() {
        databaseUpdateListener.listenForDatabaseUpdates();
        return "database updated succesfully";
    }
}
