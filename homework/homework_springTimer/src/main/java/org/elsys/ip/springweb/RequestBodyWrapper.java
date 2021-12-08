package org.elsys.ip.springweb;
import org.springframework.stereotype.Component;

@Component
public class RequestBodyWrapper {
    private String name;
    private String time;
    private int hours;
    private int minutes;
    private int seconds;

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }


}
