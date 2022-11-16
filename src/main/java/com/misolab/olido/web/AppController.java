package com.misolab.olido.web;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.misolab.olido.dto.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class AppController {

	ArrayList<User> users = new ArrayList<>();

	@PostConstruct
	public void onCreate() {
		users.add(new User("user1", "도세현"));
		users.add(new User("user2", "도옥현"));
		users.add(new User("user3", "올리도"));
	}

	@GetMapping("/login")
	public String login(Model model, String type) {
		model.addAttribute("name", type);
		return "login";
	}

	@ResponseBody
	@PostMapping("/login")
	public User postLogin(String userId, HttpSession httpSession) throws Exception {
		log.info("postLogin {}", userId);	

		User user = users.stream()
		.filter(u -> u.getUserId().equalsIgnoreCase(userId))
		.findFirst()
		.orElseThrow(() -> new Exception(userId + " is Not Found"));

		httpSession.setAttribute("user", user);

		return user;
	}

}