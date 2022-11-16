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
    static class Exam {
        String id;
        String title;
        String type;
        String answer;
        String choice;
        String result;  
    }

    @Data
    @Builder
    static class Answer {
        String id;
        String answer;
    }

    @Data
    static class Quiz {
        String id;
        String title;
        
        List<Exam> exam;
        List<Answer> answer;

        Quiz(String id, String title){
            this.id = id;
            this.title = title;
        }
    }

    Map<String, List<Quiz>> userQuiz = new HashMap<>();

    private Quiz makeQuiz(String id, String title) {

        List<Exam> exams = new ArrayList<>();
        List<Answer> answers = new ArrayList<>();

        exams.add( Exam.builder().id("0").title("1번").type("MC").choice("[10, 20, 30, 40, 50]").build());
        answers.add( Answer.builder().id("0").answer("10").build() );

        exams.add( Exam.builder().id("1").title("2번").type("SC").build());
        answers.add( Answer.builder().id("1").answer("true").build() );
        
        exams.add( Exam.builder().id("2").title("3번").type("SA").build()); 
        answers.add( Answer.builder().id("2").answer("olido").build() );

        exams.add( Exam.builder().id("3").title("4번").type("MF").build());
        answers.add( Answer.builder().id("3").answer("\\sqrt{\\dfrac{b}{a}}").build() );

        Quiz quiz = new Quiz(id, title);
        quiz.setExam(exams);
        quiz.setAnswer(answers);
        return quiz;
    }


    String userIDs[] = { "user1", "user2", "user3" };

    @PostConstruct
    void onCreate() {

        for (String userID : userIDs) {
            List<Quiz> quizs = new ArrayList<>();
            quizs.add(makeQuiz("0", "중1-1 수학"));
            quizs.add(makeQuiz("1", "중1-2 수학"));
            
            quizs.add(makeQuiz("2", "중2-1 수학"));
            quizs.add(makeQuiz("3", "중2-2 수학"));
            
            userQuiz.put(userID, quizs);
        }
    }

    Map<String, String> quizInfo(Quiz quiz){
        Map<String, String> quizInfo = new HashMap<>();
        quizInfo.put("id", quiz.getId());
        quizInfo.put("title", quiz.getTitle());
        return quizInfo;
    }

    @GetMapping
    public String home(Model model) {

        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("session is null");
        }

        model.addAttribute("name", user.getName());

        List<Quiz> quizs = userQuiz.get(user.getUserId());
        List<Map<String, String>> quizList = quizs.stream().map(q -> quizInfo(q)).collect(Collectors.toList());
        model.addAttribute("quizList", quizList);

        
        // List<ExamResult> examResults = userAnswers.get(user.getUserId());
        // if (examResults != null) {
        //     List<String> result = examResults.stream().map(er -> er.getExam().getTitle()).collect(Collectors.toList());
        //     model.addAttribute("result", result);    
        // }
        return "study/home";
    }

    @GetMapping("/exam/{id}")
    public String quiz(@PathVariable String id, Model model) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("session is null");
        }

        List<Quiz> quizs = userQuiz.get(user.getUserId());
        Quiz quiz = quizs.stream().filter(q -> q.getId().equals(id)).findFirst().get();
        model.addAttribute("id", quiz.getId());
        model.addAttribute("title", quiz.getTitle());

        model.addAttribute("exams", quiz.getExam());
        return "study/exam";
    }

    @ResponseBody
    @PostMapping("/exam/{id}")
    public String postExam(HttpSession httpSession, @PathVariable String id, @RequestBody ArrayList<Map> params) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("session is null");
        }
/* 
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
*/
        return "study/exam";
    }


    @GetMapping("/result/{id}")
    public String result(@PathVariable String id, Model model) {
        model.addAttribute("id", id);
        return "study/result";
    }
}
