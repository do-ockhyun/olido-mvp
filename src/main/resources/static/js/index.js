const editorElement = document.getElementById("editor");
const resultElement = document.getElementById("export-result");

iink.register(editorElement, {
  recognitionParams: {
    type: "MATH",
    server: {
      applicationKey: "515131ab-35fa-411c-bb4d-3917e00faf60",
      hmacKey: "54b2ca8a-6752-469d-87dd-553bb450e9ad",
    },
  },
});

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

editorElement.addEventListener("exported", (evt) => {
  console.log("result", evt.detail);

  // const exports = evt.detail.exports;
  // if (exports && exports["application/x-latex"]) {
  //   const katexValue = cleanLatex(exports["application/x-latex"])
  //   console.log("katexValue", katexValue);
  //   katex.render(katexValue, resultElement);
  // } else {
  //   resultElement.innerHTML = "";
  // }
});

new Vue({
  el: "#app",
  data: function () {
    return { visible: false };
  },
  methods: {
    convert() {
      editorElement.editor.convert();
    },
    clear() {
      editorElement.editor.clear();
    }
  },
});
