new Vue({
  el: "#app",
  data() {
    return {
      exams: [
        {
          id: 0,
          name: "exam-1",
        },
        {
          id: 1,
          name: "exam-2",
        },
      ],
      results: [
        {
          id: 0,
          name: "result-1",
        },
        {
          id: 1,
          name: "result-2",
        },
      ],
    };
  },
  methods: {
    goExam(id) {
      location.href = "/study/exam/" + id
    }
  },
});
