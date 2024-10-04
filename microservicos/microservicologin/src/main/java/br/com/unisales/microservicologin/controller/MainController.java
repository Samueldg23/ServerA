package br.com.unisales.microservicologin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String criarConta() {
        return "/HTML/index.html";  
    }
}

