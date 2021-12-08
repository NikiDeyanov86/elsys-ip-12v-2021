package org.elsys.ip.springweb;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.SortedMap;
import java.util.TreeMap;

@Component
public class ResponseManager {

    public LinkedHashMap<String, String> returnSpecificResponse(Timer timer, String header, boolean isGet) {
        LinkedHashMap<String, String> resp = new LinkedHashMap<>();
        resp.put("id", timer.getID());
        resp.put("name", timer.getName());

       if (header.equals("seconds")) {
            String[] split = timer.getTime().split(":");
            resp.put("totalSeconds", String.valueOf(Integer.parseInt(split[0]) * 3600 + Integer.parseInt(split[1])*60 + Integer.parseInt(split[2])));
        } else if (header.equals("hours-minutes-seconds")) {
            String[] split = timer.getTime().split(":");
            resp.put("hours", split[0]);
            resp.put("minutes", split[1]);
            resp.put("seconds", split[2]);
        } else
            resp.put("time", timer.getTime());

       if (isGet) {
           if (timer.isDone())
            resp.put("done", "yes");
           else resp.put("done", "no");
       }
       return resp;
    }
}
