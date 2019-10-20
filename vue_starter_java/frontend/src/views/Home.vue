<template>
  <div class="home">
    <nav-bar @clockevent="getTimeStamps()"/>
    <div class="home-body pl-3 pr-3 float-left">
    <h4>You have been clocked <span class="alert p-1" :class="{'alert-success' : timeStamps[0].isIn, 'alert-danger' : !timeStamps[0].isIn}">{{timeStamps[0].isIn | stateText}}</span> since {{ timeStamps[0].stamp | moment }}</h4>
    <h4>Total hours clocked: {{totalClockedTime}}</h4>

    <b-card no-body class="mb-1">
      <b-card-header header-tag="header" class="p-0" role="tab">
        <b-button block href="#" v-b-toggle.accordion-1 variant="light">History</b-button>
      </b-card-header>
      <b-collapse id="accordion-1" accordion="my-accordion" role="tabpanel">
        <b-card-body class="p-0">
          <ul style="list-style-type:none;" class="p-0">
            <li v-for="timeStamp in timeStamps" :key="timeStamp.id" class="history p-1" :class="{'alert-success' : timeStamp.isIn, 'alert-danger' : !timeStamp.isIn}">
              <div class="stamp-cell d-inline-block">{{timeStamp.isIn | stateText}}</div><div class="stamp-cell d-inline-block">{{ timeStamp.stamp | timeText }}</div><div class="d-inline-block float-right">{{ timeStamp.stamp | dateText }}</div>
            </li>
          </ul>
        </b-card-body>
      </b-collapse>
    </b-card>
    
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
      timeStamps: []
    };
  },
  computed: {
    totalClockedTime(){
      if (this.timeStamps[0].id == 'N/A'){
        return 'N/A';
      }
      let outStamp = 0;
      let totalMilS = 0;
      let totalHours = 0;
      let totalMinutes = 0;
      this.timeStamps.forEach(timeStamp => {
        if (!timeStamp.isIn){
          let dateStr = timeStamp.stamp.year + "-" + timeStamp.stamp.monthValue + "-" + timeStamp.stamp.dayOfMonth + " " + timeStamp.stamp.hour + ":" + timeStamp.stamp.minute;
          outStamp = new Date(dateStr);
        } else if (outStamp != 0){
          let dateStr = timeStamp.stamp.year + "-" + timeStamp.stamp.monthValue + "-" + timeStamp.stamp.dayOfMonth + " " + timeStamp.stamp.hour + ":" + timeStamp.stamp.minute;
          let inStamp = new Date(dateStr);
          totalMilS += outStamp - inStamp;
        }
      });
      totalHours = (totalMilS / (60*60*1000)).toFixed(0);
      let decimalMinutes = (totalMilS / (60*60*1000)).toFixed(2) - totalHours;
      totalMinutes = Math.abs((decimalMinutes * 60).toFixed(0));
      return totalHours + " Hours and " + totalMinutes + " Minutes";
    }
  },
  filters: {
    moment: function (date) {
      if (date == 'N/A'){
        return 'N/A';
      }
      let dateStr = date.year + "-" + date.monthValue + "-" + date.dayOfMonth + " " + date.hour + ":" + date.minute;
      return moment(dateStr, 'YYYY-MM-DD hh:mm').calendar().toLowerCase();
    },
    stateText: function (isIn){
      return isIn ? 'in' : 'out';
    },
    dateText: function (date) {
      if (date == 'N/A'){
        return 'N/A';
      }
      let dateStr =  date.monthValue + "/" + date.dayOfMonth + "/" + date.year;
      return dateStr;
    },
    timeText: function (date) {
      if (date == 'N/A'){
        return 'N/A';
      }
      let hour = date.hour > 12 ? date.hour - 12 : date.hour;
      let minute = (date.minute < 10 ? '0' : '') + date.minute;
      let second = (date.second < 10 ? '0' : '') + date.second;
      let am_pm = date.hour >= 12 ? "pm" : "am";

      let dateStr = hour + ":" + minute + ":" + second + " " + am_pm;
      return dateStr;
    }
  },
  methods: {
    getTimeStamps(){
      fetch(this.stampAPI + "/stamps", { 
          method: "GET",
          headers: {
            Authorization: "Bearer " + auth.getToken(),
            "Content-Type": "application/json"
          }})
      .then((response) => {
        return response.json();
      })
      .then((timeStamps) => {
        if (Array.isArray(timeStamps) && timeStamps.length) {
          this.timeStamps = timeStamps;
        } else {
          this.timeStamps = [{
            id: 'N/A',
            stamp: 'N/A',
            username: 'N/A',
            isIn: false
          }];
        }
      })
      .catch((err) => console.error(err));
    }
  }, 
  created() {
    this.getTimeStamps();
  }
};
</script>

<style>

.home-body {
  margin-top: 15px;
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

.stamp-cell{
  width: 30%;
}

.history{

}

@media only screen and (max-width: 600px){
  img {
    object-fit: cover;
    width: 100%;
   
  }
.history{
  width: 100%;
}
}

@media only screen and (min-width: 600px){
  img {
    object-fit: cover;
    width: 100%;
 }
 .history{
  width: 100%;
}


}
</style>

