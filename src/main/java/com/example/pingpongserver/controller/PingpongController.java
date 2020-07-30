package com.example.pingpongserver.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class PingpongController {

    @RequestMapping("/pingpong")
    public String index(@RequestBody String body) {
        return body + body;
    }

}
