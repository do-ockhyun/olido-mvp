const vm = new Vue({
  el: "#app",
  mounted() {
    this.exams = _exams.map((ex) => {
      if (ex.choice) {
        ex.choice = JSON.parse(ex.choice)
      }
      if (ex.type === "MF") {
        setTimeout(() => {
          ex.result = katex.renderToString(ex.result);
          ex.answer = katex.renderToString(ex.answer);
        }, 100);  
        
      }
      return ex;
    })
  },
  data() {
    return {
      exams: null,
    };
  },
  methods: {
  },
});


