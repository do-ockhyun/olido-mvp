package com.misolab.olido.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/study")
public class StudyController {
    
    @GetMapping
    public String home() {
        return "study/home";
    }

    @GetMapping("/exam/{id}")
    public String exam(@PathVariable String id, Model model) {
        model.addAttribute("id", id);
        return "study/exam";
    }

    @GetMapping("/result/{id}")
    public String result(@PathVariable String id, Model model) {
        model.addAttribute("id", id);
        return "study/result";
    }
}
