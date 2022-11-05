package com.misolab.olido.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @GetMapping("/exam")
    public String exam(){
        return "admin/exam";
    }

    @GetMapping("/result")
    public String result(){
        return "admin/result";
    }

    @GetMapping("/quiz")
    public String quiz(){
        return "admin/quiz";
    }

}
