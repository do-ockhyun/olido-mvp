new Vue({
  el: "#app",
  data() {
    return {
      admin: false
    };
  },
  methods: {
    submitForm(param) {
      alert("submit-" + param);
    },
    resetForm(param) {
      alert("reset-" + param);
    },
  },
});
