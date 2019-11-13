<template>
    <div id="all-stamps">
        <ul style="list-style-type:none;" id="test" class="p-0 alert alert-dark round">
            <li v-for="timeStamp in allTimeStamps" :key="timeStamp.id" class="history alert m-0" :class="{'alert-success' : timeStamp.isIn, 'alert-danger' : !timeStamp.isIn}">
                <div class="stamp-cell d-inline-block">{{timeStamp.isIn | stateText}}</div><div class="stamp-cell d-inline-block">{{ timeStamp.stamp | timeText }}</div><div class="d-inline-block float-right">{{ timeStamp.stamp | dateText }}</div>
            </li>
        </ul>
    </div>
</template>

<style>
.stamp-cell{
  width: 30%;
}

ul.round{
  border-radius: 1.5rem;
}
.history{
  width: 100%;
}
</style>

<script>

export default {
  name: 'all-stamps',
  props: ['timeStamps'],
  data(){
    return {
      allTimeStamps: []
    }
  },
  computed:{
   
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

  },
  created(){
      this.allTimeStamps = this.timeStamps;
  }
  
}
</script>