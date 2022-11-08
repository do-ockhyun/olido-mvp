const vm = new Vue({
  el: "#app",
  mounted() {
  },

  data() {
    return {
      selected: null,
      exams: [
        {
          id: 0,
          title: "1번",
          type: "MC", // multiple choice
          choice: [1, 2, 3, 4, 5],
          answer: null,
        },
        {
          id: 1,
          title: "2번",
          type: "SC", //  single choice
          answer: null,
        },
        {
          id: 2,
          title: "3번",
          type: "SA", //  single answer
          answer: null,
        },
        {
          id: 3,
          title: "4번",
          type: "MF", //  math form
          answer: null,
          result: null,
        },
      ],
    };
  },
  methods: {
    clearAnswer(ex) {
      ex.answer = null
      if (ex.result) {
        ex.result = null
      }
    },
    showMathForm(ex){
      this.selected = ex;
      openPopup()
    },
    closeMathForm(answer) {
      this.selected.answer = answer;
      this.selected.result = katex.renderToString(answer);
      this.selected = null;
    },
    goHome() {
      const ok = confirm("제출하시겠습니까?");
      if (ok) {
        location.href = "/study";
      }
    },
  },
});

function openPopup() {
  window.open('/mathform.html')
}

function returnPopup(params) {
  console.log('returnPopup', params);
  setTimeout(() => {
    vm.closeMathForm(params);
  }, 100);  
}

