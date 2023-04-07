package org.elsys.ip.spring;

import javax.annotation.PostConstruct;

public class Logic {
    public Logic() {
        System.out.println("Logic()");
    }

    public String getString() {
        return "LOGIC";
    }

    @PostConstruct
    public void onPost() {
        System.out.println("Logic.onPost()");
    }
}
