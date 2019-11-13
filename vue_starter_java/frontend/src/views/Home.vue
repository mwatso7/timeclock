<template>
  <div id="home">
    <nav-bar @clockevent="getTimeStamps()" 
    v-bind:timeStamps="timeStamps" 
    v-bind:total="totalClocked" 
    v-bind:totalToday="totalClockedToday" 
    v-bind:totalWeek="totalClockedWeek"
    v-bind:totalMonth="totalClockedMonth"
    v-bind:lastActivity="this.$options.filters.moment(timeStamps[0].stamp)"/>
    <div class="home-body pl-3 pr-3 float-left">
    <div class="h5 pb-3 mb-3 border-bottom">You have been clocked
      <span class="alert pb-0 pt-0 pr-3 pl-3 ml-1 mr-1 round" :class="{'alert-success' : timeStamps[0].isIn, 'alert-danger' : !timeStamps[0].isIn}">
        {{timeStamps[0].isIn | stateText}}</span>since {{ timeStamps[0].stamp | moment }}
    </div>
    <div class="h5 mb-1">
      <div class="recent d-inline-block">Total: </div><span class="text-info">{{totalClocked}}</span>
    </div>
    <div class="h5 mb-1">
      <div class="recent d-inline-block">Today: </div><span class="text-info">{{totalClockedToday}}</span>
    </div>
    <div class="h5 mb-1">
      <div class="recent d-inline-block">This week: </div><span class="text-info">{{totalClockedWeek}}</span>
    </div>
    <div class="h5 mb-3">
      <div class="recent d-inline-block">This month: </div><span class="text-info">{{totalClockedMonth}}</span>
    </div>

    <b-card no-body class="mb-1 border-0">
      <b-card-header header-tag="header" class="p-0" role="tab">
        <b-button block href="#" v-b-toggle.accordion-1 variant="default" @click="activity = !activity">
          <span v-if="!activity" class="float-left">+</span>
          <span v-if="activity" class="float-left">-</span>Activity</b-button>
      </b-card-header>
      <b-collapse id="accordion-1" accordion="my-accordion" role="tabpanel">
        <b-card-body class="p-0">
          <b-form-group size="lg" class="d-flex justify-content-around m-0 round">
            <b-form-radio-group
              class="d-flex justify-content-around mb-2 mt-2"
              id="btn-radios-2"
              v-model="selected"
              :options="options"
              buttons
              button-variant="outline-secondary"
              size="md"
              name="radio-btn-outline">
            </b-form-radio-group>
          </b-form-group>

          <all-stamps v-if="selected == 'all'" v-bind:timeStamps="timeStamps"/>
          <day-stamps v-if="selected == 'day'" v-bind:timeStamps="timeStamps"/>
          
          <ul v-if="selected == 'week'" style="list-style-type:none;" class="p-0">
            <li class="alert alert-info history round">
              <div class="d-block">Coming soon...</div>
            </li>
          </ul>
          <ul v-if="selected == 'month'" style="list-style-type:none;" class="p-0">
            <li class="alert alert-info history round">
              <div class="d-block">Coming soon...</div>
            </li>
          </ul>
        </b-card-body>
      </b-collapse>
    </b-card>
    
  </div>
  </div>
</template>

<style>

.home-body {
  margin-top: 15px;
}

.h5 {
  color: rgb(62, 102, 131);
}

.stamp-cell{
  width: 30%;
}

ul.round{
  border-radius: 1.5rem;
}

li.round{
  border-radius: 1.5rem;
}

li.right-round{
  border-radius: 0rem 1.5rem 1.5rem 0rem;
}

span.round{
  border-radius: 1.5rem;
}

.history{
  width: 100%;
}

.recent{
  width: 115px;
}
</style>

<script src="../node_modules/moment/moment.js"></script>
<script>
import NavBar from '@/components/NavBar';
import AllStamps from '@/components/AllStamps';
import DayStamps from '@/components/DayStamps';
import auth from '../auth';

window.moment = require('moment');
export default {
  name: "home",
  components: {
    NavBar,
    AllStamps,
    DayStamps
  },
  data() {
    return {
      stampAPI: "http://localhost:8080/TimeClock/api",
      isLoggedIn: false,
      timeStamps: [],
      activity: false,
      selected: 'all',
        options: [
          { text: 'All', value: 'all' },
          { text: 'Day', value: 'day' },
          { text: 'Week', value: 'week' },
          { text: 'Month', value: 'month' }
        ]
    };
  },
  computed: {
    totalClocked(){
      return this.totalClockedTime(this.timeStamps);
    },
    totalClockedMonth(){
      let thisDate = new Date();
      let thisMonth = thisDate.getMonth() + 1;
      let stampsThisMonth = this.timeStamps.filter(aStamp => {
        return aStamp.stamp.monthValue == thisMonth;
        });
    
      return this.totalClockedTime(stampsThisMonth);
    },
    totalClockedWeek(){
      Date.prototype.getMonthWeek = function(){
        var firstDay = new Date(this.getFullYear(), this.getMonth(), 1).getDay();
        return Math.floor((this.getDate() + firstDay)/7);
      }
      let today = new Date();
      let week = today.getMonthWeek();
      let mm = String(today.getMonth() + 1); //January is 0!
      let yyyy = today.getFullYear();
      let filterString= mm + '/' + week + '/' + yyyy; 

      console.log(filterString);
      
      let stampsThisWeek = this.timeStamps.filter(aStamp => {
        console.log(this.$options.filters.weekText(aStamp.stamp));
        return this.$options.filters.weekText(aStamp.stamp) == filterString;
        });
      return this.totalClockedTime(stampsThisWeek);
    },
    totalClockedToday(){
      let today = new Date();
      let dd = String(today.getDate());
      let mm = String(today.getMonth() + 1); //January is 0!
      let yyyy = today.getFullYear();
      let filterString= mm + '/' + dd + '/' + yyyy; 
      
      let stampsToday = this.timeStamps.filter(aStamp => {
        return this.$options.filters.dateText(aStamp.stamp) == filterString;
        });
      return this.totalClockedTime(stampsToday);
    },
    
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
    weekText: function (date) {
      if (date == 'N/A'){
        return 'N/A';
      }

      Date.prototype.getMonthWeek = function(){
        let firstDay = new Date(this.getFullYear(), this.getMonth(), 1).getDay();
        return Math.ceil((this.getDate() + firstDay)/7);
      }

      let compareDate = new Date(date.year, date.monthValue, date.dayOfMonth);

      let dateStr =  date.monthValue + "/" + compareDate.getMonthWeek() + "/" + date.year;
      return dateStr;
    }

  },
  methods: {
    totalClockedTime(filteredStamps){

      if (this.timeStamps[0].id == 'N/A'){
        return 'N/A';
      }

      filteredStamps.push

      let outStamp = 0;
      let totalMilS = 0;
      let totalHours = 0;
      let totalMinutes = 0;

      filteredStamps.forEach(timeStamp => {
        if (!timeStamp.isIn){
          let dateStr = timeStamp.stamp.year + "-" + timeStamp.stamp.monthValue + "-" + timeStamp.stamp.dayOfMonth + " " + timeStamp.stamp.hour + ":" + timeStamp.stamp.minute;
          outStamp = new Date(dateStr);
        } else if (outStamp != 0){
          let dateStr = timeStamp.stamp.year + "-" + timeStamp.stamp.monthValue + "-" + timeStamp.stamp.dayOfMonth + " " + timeStamp.stamp.hour + ":" + timeStamp.stamp.minute;
          let inStamp = new Date(dateStr);
          totalMilS += outStamp - inStamp;
        } else {
          let currentTime = new Date();
          let dateStr = timeStamp.stamp.year + "-" + timeStamp.stamp.monthValue + "-" + timeStamp.stamp.dayOfMonth + " " + timeStamp.stamp.hour + ":" + timeStamp.stamp.minute;
          let inStamp = new Date(dateStr);
          totalMilS += currentTime - inStamp;
        }
      });

      totalHours = Math.floor((totalMilS / (60*60*1000)).toFixed(1));
      let decimalMinutes = (totalMilS / (60*60*1000)).toFixed(2) - totalHours;
      totalMinutes = Math.abs((decimalMinutes * 60).toFixed(0));
      
      let hourText = (totalHours == 1) ? " hour and " : " hours and ";
      let minuteText = (totalMinutes == 1) ? " minute" : " minutes";

      return totalHours + hourText + totalMinutes + minuteText;
    },
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
          this.setActivity();
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


