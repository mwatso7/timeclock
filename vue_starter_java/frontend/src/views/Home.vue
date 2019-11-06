<template>
  <div class="home">
    <nav-bar @clockevent="getTimeStamps()"/>
    <div class="home-body pl-3 pr-3 float-left">
    <div class="h5 pb-3 mb-3 border-bottom">You have been clocked<span class="alert pb-0 pt-0 pr-3 pl-3 ml-1 mr-1 round" :class="{'alert-success' : timeStamps[0].isIn, 'alert-danger' : !timeStamps[0].isIn}">{{timeStamps[0].isIn | stateText}}</span>since {{ timeStamps[0].stamp | moment }}
    </div>
    <div class="h5 mb-1">
      Total: <span class="text-info d-float-right">{{totalClocked}}</span>
    </div>
    <div class="h5 mb-1">
      Today: <span class="text-info">{{totalClockedToday}}</span>
    </div>
    <div class="h5 mb-1">
      This week: <span class="text-info">{{totalClockedWeek}}</span>
    </div>
    <div class="h5 mb-3">
      This month: <span class="text-info">{{totalClockedMonth}}</span>
    </div>

    <b-card no-body class="mb-1 border-0">
      <b-card-header header-tag="header" class="p-0" role="tab">
        <b-button block href="#" v-b-toggle.accordion-1 variant="default" @click="activity = !activity"><span v-if="!activity" class="float-left">+</span><span v-if="activity" class="float-left">-</span>Activity</b-button>
      </b-card-header>
      <b-collapse id="accordion-1" accordion="my-accordion" role="tabpanel">
        <b-card-body class="p-0">
          <b-form-group size="lg" class="d-flex justify-content-around m-0">
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
          <ul v-if="selected == 'all'" style="list-style-type:none;" class="p-0">
            <li v-for="timeStamp in timeStamps" :key="timeStamp.id" class="history p-1" :class="{'alert-success' : timeStamp.isIn, 'alert-danger' : !timeStamp.isIn}">
              <div class="stamp-cell d-inline-block">{{timeStamp.isIn | stateText}}</div><div class="stamp-cell d-inline-block">{{ timeStamp.stamp | timeText }}</div><div class="d-inline-block float-right">{{ timeStamp.stamp | dateText }}</div>
            </li>
          </ul>
          <ul v-if="selected == 'day'" style="list-style-type:none;" class="p-0">
            <li v-for="day in dailyActivity" :key="day.day" class="history p-0 mb-3 d-flex align-items-center alert-secondary">
              <div class="d-inline-block text-center" style="width:30%">{{ day.day }}</div>
              <div class="d-inline-block" style="width:70%">
                <ul style="list-style-type:none;" class="p-0">
                  <li v-for="timeStamp in day.stamps" :key="timeStamp.id" class="history p-1" :class="{'alert-success' : timeStamp.isIn, 'alert-danger' : !timeStamp.isIn}">
                    <div class="d-inline-block">{{timeStamp.isIn | stateText}}</div><div class="d-inline-block float-right">{{ timeStamp.stamp | timeText }}</div>
                  </li>
                </ul>
                </div>
            </li>
          </ul>
          <ul v-if="selected == 'week'" style="list-style-type:none;" class="p-0">
            <li class="alert-info history p-1">
              <div class="d-block">Coming soon...</div>
            </li>
          </ul>
          <ul v-if="selected == 'month'" style="list-style-type:none;" class="p-0">
            <li class="alert-info history p-1">
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

span.round{
  border-radius: 1.5rem;
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
    dailyActivity(){ 

      let stampedDays = [];
      
      this.timeStamps.forEach(stamp => {
        stampedDays.push(this.$options.filters.dateText(stamp.stamp));
      });

      const unique = (value, index, self) => {
        return self.indexOf(value) === index;
      }

      let uniqueStamps = stampedDays.filter(unique);

      let dayArray = [];
      uniqueStamps.forEach(stamp => {
        let dayObj = {
          day: stamp,
          stamps: this.timeStamps.filter(day => {
            return (stamp == this.$options.filters.dateText(day.stamp))
          })
        };
        dayArray.push(dayObj);
      });
      return dayArray;
    },
    weeklyActivity(){ 

      let stampedDays = [];
      
      this.timeStamps.forEach(stamp => {
        stampedDays.push(this.$options.filters.dateText(stamp.stamp));
      });

      const unique = (value, index, self) => {
        return self.indexOf(value) === index;
      }

      let uniqueStamps = stampedDays.filter(unique);

      let dayArray = [];
      uniqueStamps.forEach(stamp => {
        let dayObj = {
          day: stamp,
          stamps: this.timeStamps.filter(day => {
            return (stamp == this.$options.filters.dateText(day.stamp))
          })
        };
        dayArray.push(dayObj);
      });
      return dayArray;
    },
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
    totalClockedDay(day){

    },
    totalClockedTime(filteredStamps){

      if (this.timeStamps[0].id == 'N/A'){
        return 'N/A';
      }

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


