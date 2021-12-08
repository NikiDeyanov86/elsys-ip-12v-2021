package org.elsys.ip.springweb;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;


public class Timer {
    private String id = UUID.randomUUID().toString();
    private String time;
    private  String name;

    private boolean done;

    public void setTime(String time) {
        this.time = time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isDone() {
        return done;
    }

    public String getID() {
        return id;
    }

    public String getTime() {
        return time;
    }



    public String getName() {
        return name;
    }



}
