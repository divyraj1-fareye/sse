package com.techprimers.springboot.db;


import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CounterIncrementService {

    private Long counter = 0L;

    public Long incrementCounter(){

        return counter++;
    }

}
