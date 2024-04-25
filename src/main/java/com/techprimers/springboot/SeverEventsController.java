package com.techprimers.springboot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@RestController
@RequestMapping("/server-events")
@CrossOrigin
public class SeverEventsController {


    private final ObjectMapper objectMapper;

    public SeverEventsController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


        @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> getEventss() {

        return Flux.interval(Duration.ofSeconds(1))
                .flatMap(sequence -> createServerSentEvent(sequence)
                        .onErrorResume(e -> Flux.empty()))
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))) // Retry with exponential backoff for up to 3 times
                .doOnCancel(() -> System.out.println("Connection closed"))
                .log(); // Log events for debugging

    }

    private Flux<ServerSentEvent<String>> createServerSentEvent(long sequence) {
        try {
            // Serialize JSON data
            MyDataObject dataObject = createDataObject(sequence);
            String jsonData = objectMapper.writeValueAsString(dataObject);
            return Flux.just(ServerSentEvent.<String>builder()
                    .id(String.valueOf(sequence))
                    .event("message")
                    .data(jsonData)
                    .build());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Flux.error(e);
        }
    }

    private MyDataObject createDataObject(long sequence) {
        // Create your data object here
        // For demonstration purposes, let's assume we have a MyDataObject class
        return new MyDataObject("data: " + sequence, LocalDateTime.now());
    }




    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> getEvents() throws IOException {

        //TODO: Replace this with your file path
        Stream<String> lines =  Stream.of("Hello", "World", "Java");
//                Files.lines(Path.of("/home/divyraj/practise/server-sent-events-example/pom.xml" ));
        AtomicInteger counter = new AtomicInteger(1);
        return Flux.fromStream(lines)
                .filter(line -> !line.isBlank())
                .map(line -> ServerSentEvent.<String> builder()
                        .id(String.valueOf(counter.getAndIncrement()))
                        .data(line)
                        .event("lineEvent")
                        .retry(Duration.ofMillis(1000))
                        .build())
                .delayElements(Duration.ofMillis(3000));
    }
    @GetMapping(path = "/alternative", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getEventsAlternativeOption() throws IOException {

        //TODO: Replace this with your file path
        Stream<String> lines = Files.lines(Path.of("/home/divyraj/practise/server-sent-events-example/pom.xml"));
        return Flux.fromStream(lines)
                .filter(line -> !line.isBlank())
                .delayElements(Duration.ofMillis(300));
    }

    @GetMapping("/api")
    public MyDataObject getMyObject(){

        return new MyDataObject("abc",LocalDateTime.now());

    }


}
