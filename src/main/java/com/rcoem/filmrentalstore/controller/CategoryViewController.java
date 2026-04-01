package com.rcoem.filmrentalstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/pratham")
public class CategoryViewController {

    @GetMapping("/categories")
    public String categoriesPage() {
        return "categories";
    }

    @GetMapping("/categories/films")
    public String categoryFilmsPage(@RequestParam("categoryId") Byte categoryId,
                                    @RequestParam("name") String name, Model model) {
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("categoryName", name);
        return "category-films";
    }
}