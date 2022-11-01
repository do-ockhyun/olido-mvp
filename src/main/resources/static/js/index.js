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

editorElement.addEventListener("exported", (evt) => {
  console.log("result", evt.detail);

  if (evt.detail) {
    resultElement.innerHTML = evt.detail.exports["application/x-latex"];
  } else {
    resultElement.innerHTML = "";
  }
});

new Vue({
  el: "#app",
  data: function () {
    return { visible: false };
  },
  methods: {
    convert() {
      editorElement.editor.export_();
    },
  },
});
