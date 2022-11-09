new Vue({
  el: "#app",
  data() {
    return {
      formData: {
        userId: "",
        password: "",
      },
      admin: false
    };
  },
  methods: {
    submitForm(param) {
      const {userId}  = this.formData
      axios.post('/login', null, { params: {userId} })
      .then((response) => {
        console.log(response.data);
        const { name } = response.data;
        alert(`Hi ~ ${name}`);
        location.href = "/study"
      })
      .catch((error) => {
        console.error(error);
        const { message } = error.response.data
        alert(message);
        this.resetForm()
      })
    },
    resetForm() {
      this.formData.userId = ""
      this.formData.password = ""
    },
  },
});
