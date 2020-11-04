package com.example.pingpongserver.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class PingpongController {

    @RequestMapping("/version")
    public String index() {
        return "現在のバージョンは0.1.0です"; // TODO: 設定ファイルから取る
    }

    @RequestMapping("/pingpong")
    public String index(@RequestBody String body) {
        return body;
    }

}
