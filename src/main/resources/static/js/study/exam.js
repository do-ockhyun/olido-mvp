new Vue({
  el: "#app",
  data() {
    return {
      exams: [{
        id: 0,
        title: "1번",
        type: 'MC',
        c: ['1','2','3','4','5'],
      },{
        id: 1,
        title: "2번",
        type: 'SA',
      }]
    };
  },
  methods: {},
});
