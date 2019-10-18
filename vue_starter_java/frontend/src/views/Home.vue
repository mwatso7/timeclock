<template>
  <div class="home">
    <nav-bar @clockevent="getLastTimeStamp()"/>
    <div class="home-body pl-5 pr-5">
    <h4>You have been clocked <span class="alert p-1" :class="{'alert-success' : lastTimeStamp.isIn, 'alert-danger' : !lastTimeStamp.isIn}">{{dateText}}</span> since {{ lastTimeStamp.stamp | moment }}</h4>
    <ul>
      <li></li>
    </ul>
  </div>
  </div>
</template>
<script src="../node_modules/moment/moment.js"></script>

<script>
import NavBar from '@/components/NavBar';
import auth from '../auth';

window.moment = require('moment');
export default {
  name: "home",
  components: {
    NavBar
  },
  data() {
    return {
      stampAPI: "http://localhost:8080/TimeClock/api",
      isLoggedIn: false,
      timeStamps: [],
      lastTimeStamp: {
        id: null,
        stamp: null,
        username: null,
        isIn: false
      }
    };
  },
  computed: {
    dateText(){
      return this.lastTimeStamp.isIn ? 'in' : 'out';
    }
  },
  filters: {
    moment: function (date) {
      let dateStr = date.year + "-" + date.monthValue + "-" + date.dayOfMonth + " " + date.hour + ":" + date.minute;
      return moment(dateStr, 'YYYY-MM-DD hh:mm').calendar().toLowerCase();
    }
  },
  methods: {
    getLastTimeStamp(){
      fetch(this.stampAPI + "/laststamp", { 
          method: "GET",
          headers: {
            Authorization: "Bearer " + auth.getToken(),
            "Content-Type": "application/json"
          }})
      .then((response) => {
        return response.json();
      })
      .then((timeStamp) => {
        console.log(timeStamp);
        this.lastTimeStamp = timeStamp;
      })
      .catch((err) => console.error(err));
    }
  }, 
  created() {
    this.getLastTimeStamp();
  }
};
</script>

<style>

.home-body {
  margin-top: 30px;
}

a {
  text-decoration: none;
  color: #00ADEE;
}

h1 {
  color: blue;
  text-align: center;
  
}

h4 {
  color: rgb(62, 102, 131);
}

img {
  align-content: center;
}

@media only screen and (max-width: 600px){
  img {
    object-fit: cover;
    width: 100%;
   
  }
}

@media only screen and (min-width: 600px){
  img {
    object-fit: cover;
    width: 100%;
 }

  .home-body{
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
  }
}
</style>

