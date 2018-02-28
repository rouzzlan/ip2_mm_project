package be.kdg.musicmaker.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
public class HomeController {

    /**
     * Solely used for token and permission testing
     * @return
     */

    @GetMapping(value = "/")
    public String index(){
        return "Hello world - CI test";
    }

    @GetMapping(value = "/private")
    public String privateArea(){
        return "Private area";
    }

}
