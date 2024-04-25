package com.techprimers.springboot.db;


import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
public class DataBaseUpdateListener {


    private final SSEEventEmitter sseEventEmitter;

    Long counter = 0L;

    public DataBaseUpdateListener(SSEEventEmitter sseEventEmitter) {
        this.sseEventEmitter = sseEventEmitter;
    }

    // Method to listen for database updates
    public void listenForDatabaseUpdates() {
        // Logic to listen for database updates
        // When a database update occurs, call the SSEEventEmitter to emit an SSE event
        counter++;
        String updatedData = "Data updated in the database";
        // Emit SSE event with event criteria (e.g., user ID)
        this.sseEventEmitter.emitSSEEvent("update", updatedData+counter);
    }

}
