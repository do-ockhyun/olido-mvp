package com.misolab.olido.web;

import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class AppController {

	@Data
	@AllArgsConstructor
	static class User {
		String userId;
		String name;
	}

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
	public User postLogin(String userId) throws Exception {
		log.info("postLogin {}", userId);	

		User user = users.stream()
		.filter(u -> u.userId.equalsIgnoreCase(userId))
		.findFirst()
		.orElseThrow(() -> new Exception(userId + " is Not Found"));

		return user;
	}

}