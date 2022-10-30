package com.misolab.olido.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

    @GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("name", "Login page");
		return "login";
	}

}