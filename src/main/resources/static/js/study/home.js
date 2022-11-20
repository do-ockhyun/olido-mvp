new Vue({
  el: "#app",
  data() {
    return {
      quiz: _quiz,
      results: _result,
    };
  },
  methods: {
    goExam(id) {
      location.href = "/study/exam/" + id
    },
    goResult(id) {
      location.href = "/study/result/" + id
    }
  },
});
