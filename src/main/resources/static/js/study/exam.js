const vm = new Vue({
  el: "#app",
  mounted() {
    this.exams = _exams.map((ex) => {
      if (ex.choice) {
        ex.choice = JSON.parse(ex.choice)
      }
      return ex;
    })
  },

  data() {
    return {
      selected: null,
      exams: null,
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
        const data = this.exams.map(ex => ({
          id: `${ex.id}`,
          answer: `${ex.answer}`
        }))
        console.log('data', data)
        axios.post(`/study/exam/${_id}?title=${_title}`, data )
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

