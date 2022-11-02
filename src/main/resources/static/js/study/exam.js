new Vue({
  el: "#app",
  data() {
    return {
      exams: [
        {
          id: 0,
          title: "1번",
          type: 'MC',   // multiple choice
          choice: [1,2,3,4,5],
          answer: null,
        },
        {
          id: 1,
          title: "2번",
          type: 'SA',   //  single answer
          answer: null,
        },
        {
          id: 2,
          title: "3번",
          type: 'MF',   //  math form
          answer: null,
        },
      ],
    };
  },
  methods: {
    clearAnswer(ex) {
      ex.answer = null;
    },
  },
});
