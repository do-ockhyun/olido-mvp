package com.misolab.olido.web;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.misolab.olido.dto.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/study")
public class StudyController {

    final HttpSession httpSession;

    @Data
    @AllArgsConstructor
    static class Exam {
        Integer id;
        String name;
    }

    ArrayList<Exam> exams = new ArrayList<>();
    ArrayList<Exam> result = new ArrayList<>();

    @PostConstruct
    void onCreate() {
        exams.add(new Exam(0, "중1-1 수학"));
        exams.add(new Exam(1, "중1-2 수학"));
        exams.add(new Exam(2, "중2-1 수학"));
        exams.add(new Exam(3, "중2-2 수학"));
    }

    @GetMapping
    public String home(Model model) throws Exception {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            throw new Exception("session is null");
        }

        model.addAttribute("name", user.getName());
        model.addAttribute("exams", exams);
        model.addAttribute("result", result);
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
