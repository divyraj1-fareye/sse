package com.techprimers.springboot.db;

import org.apache.tomcat.util.json.JSONFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.servlet.http.HttpSession;


@RestController
public class SSEController {



    @Autowired
    CounterIncrementService counterIncrementService;

    @Autowired
    SSEService sseService;


    @GetMapping(value = "/events", produces = "text/event-stream")
    public Flux<ServerSentEvent<String>> getEvents(HttpSession session) {
        String username = (String) session.getAttribute("username");
        System.out.println("userName-----"+username);
        return sseService.getEventFlux();
    }

    @GetMapping(value="/update/db")
    public String updateDB(){
      return   sseService.listenForDatabaseUpdates();
    }


    @PostMapping("login")
    public String login(@RequestParam("username") String username, HttpSession session) {
        // Validate the username and authenticate the user
        // Example: You might use Spring Security for authentication

        // If authentication is successful, set the username in the session
        session.setAttribute("username", username);

        // Redirect to a protected page or dashboard
        return "logged in ";
    }

   @GetMapping("/increment_counter")
    public Long incrementCounter(){
        return counterIncrementService.incrementCounter();
   }

}
