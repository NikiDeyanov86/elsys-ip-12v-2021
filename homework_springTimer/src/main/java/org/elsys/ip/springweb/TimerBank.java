package org.elsys.ip.springweb;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@Component
public class TimerBank {
    private HashMap<Timer, ZonedDateTime> timers = new HashMap<>();

    public void addTimer(Timer timer, ZonedDateTime date) {
        timers.put(timer, date);
    }

    public Timer takeTimer(String ID) {
        for (Timer t : timers.keySet()) {
            if (t.getID().equals(ID)) return t;
        }
        return null;
    }

    public ZonedDateTime getValue(Timer key) {
        return timers.get(key);
    }

    public void updateValue(Timer key, ZonedDateTime value) {
        timers.replace(key, value);
    }

    public void updateTimer(String id) {
        Timer t = this.takeTimer(id);
        ZonedDateTime startTime = this.getValue(t);

        String[] split = t.getTime().split(":");

        ZonedDateTime futureEndTime = startTime.plusHours(Integer.parseInt(split[0])).plusMinutes(Integer.parseInt(split[1])).plusSeconds(Integer.parseInt(split[2]));

        ZonedDateTime now = ZonedDateTime.now();

        Duration duration = Duration.between(now, futureEndTime);

        if (duration.toSeconds() < 0)
            this.takeTimer(id).setDone(true);
        else
            this.takeTimer(id).setDone(false);

        String hours = "0";
        if (duration.toHours() < 10) hours += String.valueOf(duration.toHours());
        else hours = String.valueOf(duration.toHours());

        String minutes = "0";
        if ((duration.toMinutes()- duration.toHours()*60) < 10 ) minutes += String.valueOf(duration.toMinutes() - duration.toHours()*60);
        else minutes = String.valueOf(duration.toMinutes() - duration.toHours()*60);

        String seconds = "0";
        if ((duration.toSeconds() - duration.toMinutes()*60) < 10 && (duration.toSeconds() - duration.toMinutes()*60) > 0) seconds += String.valueOf(duration.toSeconds() - duration.toMinutes()*60);
        else if ((duration.toSeconds() - duration.toMinutes()*60) <= 0) {
            seconds += "0";
            this.takeTimer(id).setDone(true);
        }
        else seconds = String.valueOf(duration.toSeconds() - duration.toMinutes()*60);


        String timerChange = hours + ":" + minutes + ":" + seconds;
        this.takeTimer(id).setTime(timerChange);

        this.updateValue(this.takeTimer(id), now);
    }

    private boolean isAvlive(String id) {
        Timer t = this.takeTimer(id);
        ZonedDateTime startTime = this.getValue(t);

        String[] split = t.getTime().split(":");

        ZonedDateTime futureEndTime = startTime.plusHours(Integer.parseInt(split[0])).plusMinutes(Integer.parseInt(split[1])).plusSeconds(Integer.parseInt(split[2]));

        ZonedDateTime now = ZonedDateTime.now();

        Duration duration = Duration.between(now, futureEndTime);

        if (duration.toSeconds() < 0)
            return false;
        else
            return true;
    }

    public int activeTimers() {
        int count = 0;
        for (Timer t : timers.keySet()) {
            if (this.isAvlive(t.getID()))
                count += 1;
        }
        return count;
    }



}
