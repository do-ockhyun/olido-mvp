new Vue({
  el: "#app",
  data() {
    return {
      exams: _exams,
      results: _result,
    };
  },
  methods: {
    goExam(id) {
      location.href = "/study/exam/" + id
    }
  },
});
