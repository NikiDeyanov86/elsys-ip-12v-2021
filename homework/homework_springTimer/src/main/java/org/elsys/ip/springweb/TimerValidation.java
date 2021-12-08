package org.elsys.ip.springweb;

import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class TimerValidation {

    public boolean validateWrapper(RequestBodyWrapper t) {
        if (t.getTime() != null) {
            String[] split = t.getTime().split(":");
            try {
                for (String a : split) {
                    Integer.parseInt(a);
                }
            }catch (Exception e) {
                return false;
            }
            if (t.getHours() != 0 || t.getMinutes() != 0 || t.getSeconds() != 0) {
                return false;
            }
            return true;
        } else if (t.getTime() == null) {
            if (t.getHours() > 0 || t.getMinutes() > 0 || t.getSeconds() > 0)
                return true;
            else
                return false;
        }

        return false;
    }



}
