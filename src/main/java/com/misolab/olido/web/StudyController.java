package com.misolab.olido.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.misolab.olido.dto.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/study")
public class StudyController {

    final HttpSession httpSession;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class Exam {
        String id;
        String title;

        Exam(String id, String title) {
            this.id = id;
            this.title = title;
        }
        String type;
        String answer;
        String choice;
        String result;

        static Exam convert(Map<String, String> map) {
            return Exam.builder()
                        .id(map.get("id"))
                        .answer(map.get("answer"))
                        .build();
        }        
    }
    List<Exam> examList = new ArrayList<>();
    List<Exam> exams = new ArrayList<>();
    List<Exam> answers = new ArrayList<>();

    @Data
    static class ExamResult {
        Exam exam;
        List<Exam> answers;
    }

    Map<String, List<ExamResult>> userAnswers = new HashMap<>();

    @PostConstruct
    void onCreate() {
        examList.add(new Exam("0", "중1-1 수학"));
        examList.add(new Exam("1", "중1-2 수학"));
        examList.add(new Exam("2", "중2-1 수학"));
        examList.add(new Exam("3", "중2-2 수학"));

        exams.add(new Exam("0", "1번", "MC", null, "[10, 20, 30, 40, 50]", null));
        answers.add( Exam.builder().id("0").answer("10").build() );

        exams.add(new Exam("1", "2번", "SC", null, null, null));
        answers.add( Exam.builder().id("1").answer("true").build() );
        
        exams.add(new Exam("2", "3번", "SA", null, null, null));
        answers.add( Exam.builder().id("2").answer("olido").build() );

        exams.add(new Exam("3", "4번", "MF", null, null, null));
        answers.add( Exam.builder().id("3").answer("\\sqrt{\\dfrac{b}{a}}").build() );
        
    }

    @GetMapping
    public String home(Model model) {
        model.addAttribute("examList", examList);

        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("session is null");
        }

        model.addAttribute("name", user.getName());
        
        List<ExamResult> examResults = userAnswers.get(user.getUserId());
        if (examResults != null) {
            List<String> result = examResults.stream().map(er -> er.getExam().getTitle()).collect(Collectors.toList());
            model.addAttribute("result", result);    
        }
        return "study/home";
    }

    @GetMapping("/exam/{id}")
    public String exam(@PathVariable String id, Model model) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("session is null");
        }

        model.addAttribute("id", id);
        model.addAttribute("exams", exams);
        return "study/exam";
    }

    @ResponseBody
    @PostMapping("/exam/{id}")
    public String postExam(HttpSession httpSession, @PathVariable String id, @RequestBody ArrayList<Map> params) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("session is null");
        }

        List<Exam> answer = params.stream().map(Exam::convert).collect(Collectors.toList());
        log.info("answer {}", answer);

        List<ExamResult> examResultList = userAnswers.get(user.getUserId());
        if (examResultList == null) {
            examResultList = new ArrayList<>();
        }
        if (examResultList.size() == 0) {
            ExamResult examResult = new ExamResult();
            Exam exam = examList.stream().filter(e -> e.getId().equals(id)).findFirst().get();
            examResult.setExam(exam);
            examResult.setAnswers(answer);
            
            examResultList.add(examResult);
        }
        userAnswers.put(user.getUserId(), examResultList);
        return "study/exam";
    }


    @GetMapping("/result/{id}")
    public String result(@PathVariable String id, Model model) {
        model.addAttribute("id", id);
        return "study/result";
    }
}
