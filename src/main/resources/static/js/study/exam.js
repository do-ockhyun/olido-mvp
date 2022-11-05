function cleanLatex(latexExport) {
  if (latexExport.includes("\\\\")) {
    const steps = "\\begin{align*}" + latexExport + "\\end{align*}";
    return steps
      .replace("\\begin{aligned}", "")
      .replace("\\end{aligned}", "")
      .replace(new RegExp("(align.{1})", "g"), "aligned");
  }
  return latexExport.replace(new RegExp("(align.{1})", "g"), "aligned");
}

new Vue({
  el: "#app",
  mounted() {    
    console.log('refs', this.$refs);

    this.editor = iink.register(this.$refs.editor, {
      recognitionParams: {
        type: "MATH",
        server: {
          applicationKey: "515131ab-35fa-411c-bb4d-3917e00faf60",
          hmacKey: "54b2ca8a-6752-469d-87dd-553bb450e9ad",
        },
      },
    });

    
    this.$refs.editor.addEventListener('exported', (evt) => {
      const exports = evt.detail.exports;
      if (exports && exports['application/x-latex']) {
        this.selected.ex.answer = cleanLatex(exports['application/x-latex']);
        katex.render(this.selected.ex.answer,  this.selected.result);
      }
    });

  },
  destroyed() {
    this.editor.close();
  },

  data() {
    return {
      selected: {
        title: "수식입력",
        visible: true,
        ex: null
      },
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
          type: 'SC',   //  single choice
          answer: null,
        },
        {
          id: 2,
          title: "3번",
          type: 'SA',   //  single answer
          answer: null,
        },
        {
          id: 3,
          title: "4번",
          type: 'MF',   //  math form
          answer: null,
          result: null
        },
      ],
    };
  },
  methods: {
    convert() {
      this.editor.convert();
      this.closeMahtForm();
    },
    clear() {
      this.editor.clear();
    },

    showMathForm(ex){
      this.selected.ex = ex
      this.selected.visible = true
      this.selected.result = this.$refs.result[0]
    },
    closeMahtForm(){
      this.selected.ex = null
      this.selected.visible = false
    },
    clearAnswer(ex) {
      ex.answer = null;
      if (ex.type === "MF") {
        this.selected.result.innerHTML = "수식입력";
      }
    },
  },
});
