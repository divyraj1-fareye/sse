package com.techprimers.springboot;

import java.time.LocalDateTime;

public class MyDataObject {

    String name;

    java.time.LocalDateTime LocalDateTime ;


    public MyDataObject(String name, LocalDateTime localDateTime) {
        this.name = name;
        LocalDateTime = localDateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public java.time.LocalDateTime getLocalDateTime() {
        return LocalDateTime;
    }

    public void setLocalDateTime(java.time.LocalDateTime localDateTime) {
        LocalDateTime = localDateTime;
    }
}
