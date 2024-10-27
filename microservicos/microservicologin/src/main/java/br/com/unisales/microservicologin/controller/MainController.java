package br.com.unisales.microservicologin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String index() {
        return "html/index.html";  
    }

    @GetMapping("/cadastro")
    public String cadastro() {
        return "html/cadastro.html";  
    }

    @GetMapping("/listagem")
    public String listagem() {
        return "html/listagem.html";  
    }
}
