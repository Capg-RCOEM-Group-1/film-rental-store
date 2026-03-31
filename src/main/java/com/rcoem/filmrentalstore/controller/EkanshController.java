package com.rcoem.filmrentalstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ekansh")
public class EkanshController {

    @GetMapping("/customers")
    public String customersPage() {
        return "customers";
    }

    @GetMapping("/rentals")
    public String rentalsPage() {
        return "customerrentals";
    }
}
