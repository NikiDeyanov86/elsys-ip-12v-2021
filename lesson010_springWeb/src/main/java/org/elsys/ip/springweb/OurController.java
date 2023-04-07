package org.elsys.ip.springweb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("base")
public class OurController {

    @GetMapping("{id}")
    public String getString(@PathVariable int id, @RequestParam(defaultValue = "5") int queryParam) {
        return String.valueOf(id + queryParam);
    }

    @PostMapping
    public String create(@RequestBody SlojenObect body) {
        return String.valueOf(body.getId()) + " " + body.getName();
    }

    @PutMapping
    public SlojenObect change(@RequestBody SlojenObect body) {
        return new SlojenObect(body.getId() + 1, body.getName() + " Changed");
    }
}
