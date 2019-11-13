<template>
    <div id="day-stamps">
        <ul style="list-style-type:none;" class="p-0">
            <div v-if="dailyActivity" class="alert alert-info mb-2 round text-center">{{ totalClockedSelectedDays }}</div>
                <li v-for="(day, index) in dayTimeStamps" :key="day.day" class="history p-0 pl-2 mb-3 d-flex align-items-center alert alert-secondary round">
                    <div @click.prevent="isEnabled(index)" class="d-inline-block text-center" style="width:30%" ><b-form-checkbox v-model="day.enabled" switch size="lg" class="mb-1"></b-form-checkbox>{{ day.day }}</div>
                    <div class="d-inline-block" style="width:70%">
                    <ul style="list-style-type:none;" id="test2" class="p-0">
                        <li v-for="timeStamp in day.stamps" :key="timeStamp.id" class="history alert m-0" :class="{'alert-success' : timeStamp.isIn, 'alert-danger' : !timeStamp.isIn}">
                            <div class="d-inline-block">{{timeStamp.isIn | stateText}}</div><div class="d-inline-block float-right">{{ timeStamp.stamp | timeText }}</div>
                        </li>
                    </ul>
                </div>
            </li>
        </ul>
    </div>
</template>

<style>

ul.round{
  border-radius: 1.5rem;
}

ul#test li:first-child{
  border-radius: 1.5rem 1.5rem 0rem 0rem;
}

ul#test li:last-child{
  border-radius: 0rem 0rem 1.5rem 1.5rem;
}

ul#test li:only-child{
  border-radius: 1.5rem 1.5rem 1.5rem 1.5rem;
}

ul#test2 li:first-child{
  border-radius: 0rem 1.5rem 0rem 0rem;
}

ul#test2 li:last-child{
  border-radius: 0rem 0rem 1.5rem 0rem;
}

ul#test2 li:only-child{
  border-radius: 0rem 1.5rem 1.5rem 0rem;
}

.history{
  width: 100%;
}
</style>

<script>

export default {
  name: 'day-stamps',
  props: ['timeStamps'],
  data(){
    return {
      dayTimeStamps: []
    }
  },
  computed:{
    dailyActivity(){ 
      let enabled = false;
      this.dayTimeStamps.forEach(day => {
        if (day.enabled === true){
          enabled = true;
        }
      });
      return enabled;
    },
    totalClockedSelectedDays(){
      let selectedStamps = [];
      
      this.dayTimeStamps.forEach(day => {
        if (day.enabled){ 
          selectedStamps = selectedStamps.concat(day.stamps);
        }
      });
      console.log(selectedStamps);

      return this.totalClockedTime(selectedStamps);
    }
  },
  filters: {
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
      let am_pm = date.hour >= 12 ? "pm" : "am";

      let dateStr = hour + ":" + minute + " " + am_pm;
      return dateStr;
    }
  },
  methods:{
    isEnabled(index){
      
      this.dayTimeStamps.splice(index, 1, {
        day: this.dayTimeStamps[index].day,
        stamps: this.dayTimeStamps[index].stamps,
        enabled: !this.dayTimeStamps[index].enabled
      });
      console.log(this.dayTimeStamps[index].day + ' : ' + this.dayTimeStamps[index].enabled);
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
    setActivity(){ 

      let stampedDays = [];
      
      this.timeStamps.forEach(stamp => {
        stampedDays.push(this.$options.filters.dateText(stamp.stamp));
      });

      const unique = (value, index, self) => {
        return self.indexOf(value) === index;
      }

      let uniqueStamps = stampedDays.filter(unique);

      this.dayTimeStamps = [];
      uniqueStamps.forEach(stamp => {
        let dayObj = {
          day: stamp,
          stamps: this.timeStamps.filter(day => {
            return (stamp == this.$options.filters.dateText(day.stamp))
          }),
          enabled: false
        };
        console.log(dayObj);
        this.dayTimeStamps.push(dayObj);
      });
    },
  },
  created(){
      this.setActivity();
  }
  
}
</script>