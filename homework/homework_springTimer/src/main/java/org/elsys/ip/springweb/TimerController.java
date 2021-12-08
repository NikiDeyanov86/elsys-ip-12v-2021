package org.elsys.ip.springweb;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("")
public class TimerController {
    TimerBank timers = new TimerBank();
    ResponseManager responseManager = new ResponseManager();
    TimerValidation timerValidation = new TimerValidation();


    @PostMapping("/timer")
    public ResponseEntity answer(@RequestBody RequestBodyWrapper body, @RequestHeader(value = "TIME-FORMAT", required = false, defaultValue = "time") String header) {
        // TO:DO - CHECK FOR INPUT
        Timer t = new Timer();

        if (!timerValidation.validateWrapper(body)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        ZonedDateTime now = ZonedDateTime.now();

        t.setName(body.getName());
        if (body.getTime() == null) {
            String fixTime = "";
            if (body.getHours() < 10)
                fixTime += "0" + body.getHours() + ":";
            else
                fixTime += body.getHours() + ":";

            if (body.getMinutes() < 10)
                fixTime += "0" + body.getMinutes() + ":";
            else
                fixTime += body.getMinutes() + ":";

            if (body.getSeconds() < 10)
                fixTime += "0" + body.getSeconds();
            else
                fixTime += body.getSeconds();

            t.setTime(fixTime);
        } else {
            t.setTime(body.getTime());
        }
        t.setDone(false);
        timers.addTimer(t, now);
        String count = String.valueOf(timers.activeTimers());

        return ResponseEntity.status(HttpStatus.CREATED).header("ACTIVE-TIMERS", count).body(responseManager.returnSpecificResponse(t, header, false));

    }

    @GetMapping(path = "/timer/{id}")
    public ResponseEntity getTimer(@PathVariable String id, @RequestHeader(value = "TIME-FORMAT", required = false, defaultValue = "time") String header, @RequestParam(required = false, value = "long") boolean polling) {

        if (timers.takeTimer(id) == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);



        if (polling) {
            //DeferredResult<ResponseEntity> output = new DeferredResult<>();
            int sleepTime = 0;
            Timer t = timers.takeTimer(id);
            String[] split = t.getTime().split(":");
            if (Integer.parseInt(split[0]) > 0 || Integer.parseInt(split[1]) > 0 || Integer.parseInt(split[2]) >= 10) {
                sleepTime = 10 * 1000;
            } else if (Integer.parseInt(split[2]) < 10) {
                sleepTime = Integer.parseInt(split[2]) * 1000;
            }

            try {
                Thread.sleep(sleepTime);

            } catch (Exception e) {
                e.printStackTrace();
            }
            timers.updateTimer(id);

        }

        timers.updateTimer(id);


        return ResponseEntity.ok().header("ACTIVE-TIMERS", String.valueOf(timers.activeTimers())).body(responseManager.returnSpecificResponse(timers.takeTimer(id), header, true));
    }


}
