package com.misolab.olido.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    final static ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
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
    @NoArgsConstructor
    @AllArgsConstructor
    static class Answer {
        String id;
        String answer;
    }

    @Data
    @NoArgsConstructor
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

    List<Quiz> quizList = new ArrayList<>();

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

    @Data
    static class Result {
        String created;
        String id;
        String title;
        List<Answer> answer;

        Map<String, String> getInfo(){
            Map<String, String> info = new HashMap<>();
            info.put("id", id);
            info.put("title", title);
            return info;
        }

        Result(String id, String title, ArrayList<Map> params) {
            this.id = id;
            this.title = title;
            this.answer = new ArrayList<>();
            
            for (Map<String, String> received : params) {
                String _id = received.get("id");
                String _answer = received.get("answer");
                Answer ans = new Answer(_id, _answer);
                this.answer.add(ans);
            }
        }
    }

    Map<String, List<Result>> userResult = new HashMap<>();

    @PostConstruct
    void onCreate() {
        quizList.add(makeQuiz("0", "중1-1 수학"));
        quizList.add(makeQuiz("1", "중1-2 수학"));
        quizList.add(makeQuiz("2", "중2-1 수학"));
        quizList.add(makeQuiz("3", "중2-2 수학"));
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
        model.addAttribute("quizList", quizList);
        
        List<Result> resultList = userResult.get(user.getUserId());
        if (resultList != null) {
            model.addAttribute("result", resultList);    
        }
        return "study/home";
    }

    @GetMapping("/exam/{id}")
    public String quiz(@PathVariable String id, Model model) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("session is null");
        }

        Quiz quiz = quizList.stream().filter(q -> q.getId().equals(id)).findFirst().get();
        model.addAttribute("id", quiz.getId());
        model.addAttribute("title", quiz.getTitle());

        model.addAttribute("exams", quiz.getExam());
        return "study/exam";
    }

    @ResponseBody
    @PostMapping("/exam/{id}")
    public String postExam(HttpSession httpSession, @PathVariable String id, String title, @RequestBody ArrayList<Map> params) {
        log.info("params {}", params);
        
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("session is null");
        }

        Result result = new Result(id, title, params);
        List<Result> resultList = userResult.get(user.getUserId());
        if (resultList == null) {
            resultList = new ArrayList<>();
        }
        resultList.removeIf(r -> r.getId().equals(result.getId()));
        resultList.add(result);
        userResult.put(user.getUserId(), resultList);
        return "study";
    }


    @GetMapping("/result/{id}")
    public String result(@PathVariable String id, Model model) throws JsonMappingException, JsonProcessingException {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("session is null");
        }

        model.addAttribute("id", id);

        List<Result> resultList = userResult.get(user.getUserId());
        if (resultList == null) {
            return "study/result";
        }

        Result result = resultList.stream().filter(r -> r.getId().equals(id)).findFirst().get();
        Quiz quiz = quizList.stream().filter(q -> q.getId().equals(id)).findFirst().get();

        ObjectMapper objectMapper = new ObjectMapper();
        Quiz newQquiz = objectMapper.readValue(objectMapper.writeValueAsString(quiz), Quiz.class);
        List<Exam> _exam = newQquiz.getExam();
        List<Answer> _answer = newQquiz.getAnswer();
        List<Answer> _resultAnswer = result.getAnswer();
        for (int i = 0; i < _resultAnswer.size(); i++) {
            _exam.get(i).setResult(_resultAnswer.get(i).getAnswer());
            _exam.get(i).setAnswer(_answer.get(i).getAnswer());
        }
        model.addAttribute("title", newQquiz.getTitle());
        model.addAttribute("exams", newQquiz.getExam());
        return "study/result";
    }
}
