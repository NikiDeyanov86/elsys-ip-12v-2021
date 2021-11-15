package org.elsys.ip.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class AnotherClass {
    private final Logic logic;

    private final ApplicationContext context;

    public AnotherClass(ApplicationContext context) {
        this.context = context;
        System.out.println("AnotherClass()");
        logic = context.getBean(Logic.class);
    }

    @PostConstruct
    public void onPost() {
        System.out.println("Hello from PostConstruct");
        System.out.println("Logic = " + logic.getString());
        AnotherClass self = context.getBean(AnotherClass.class);
    }
}
