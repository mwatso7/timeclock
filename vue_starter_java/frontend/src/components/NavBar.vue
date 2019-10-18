  <template>
  <div class="nav-bar">
    <div id="nav" class="navbar navbar-default sticky-top shadow-sm" >
      <span class="d-flex align-items-center"><router-link  class="navbar-brand" style="text-decoration: none;" to="/"><span id="logo" class="h2"><i class="fas fa-user-clock"></i></span></router-link>
      <button v-if="isLoggedIn" class="btn btn-md btn-inline" :class="{'btn-success' : !lastTimeStamp.isIn, 'btn-danger' : lastTimeStamp.isIn}" @click.prevent="recordStamp()">{{buttonText}}</button></span>
      <div class="d-flex align-items-center" style="color: #00ADEE;">
          <router-link class="navtxt" v-if="!isLoggedIn" to="/login"><h2><i class="fas fa-sign-in-alt"></i></h2></router-link>        
          <router-link class="navtxt" v-if="isLoggedIn" to="/logout"><h2><i class="fas fa-sign-out-alt"></i></h2></router-link>
    </div>
    </div>
  </div>
</template>

<style scoped>

#logo{
    font-family: 'Pacifico';
    color: rgb(201, 227, 236);
    margin-right: 5px;
}

.navtxt{
    color: rgb(201, 227, 236);
}

#nav{
    background-color: rgb(62, 102, 131);
  
}
</style>


<script>
import auth from '../auth'

export default {
  name: 'nav-bar',
  data(){
    return {
      stampAPI: "http://localhost:8080/TimeClock/api",
      isLoggedIn: auth.getUser(),
      lastTimeStamp: {
        id: null,
        stamp: null,
        username: null,
        isIn: false
      }
    }
  },
  computed:{
    buttonText(){
      return this.lastTimeStamp.isIn ? 'Out' : 'In';
    }
  },
  methods:{
    logoutUser(){
      auth.logout();
    },
    recordStamp(){
      fetch(this.stampAPI + "/addstamp", { 
          method: "POST",
          headers: {
            Authorization: "Bearer " + auth.getToken(),
            "Content-Type": "application/json"
          },
          body: JSON.stringify(this.lastTimeStamp.isIn)
         })
        .then(response => {
          if (response.ok) {
            this.getLastTimeStamp();
            this.$emit('clockevent');
          }
        })
        .catch(err => {
          console.log(err);
        });
    },
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
  created(){
    this.getLastTimeStamp();
  },
  watch: {
    '$route': function(value){
      if (value.path === '/'){
        this.isLoggedIn = true;
        }
      if(value.name === 'logout'){
        this.isLoggedIn = false;
      }
    }
  }
}
</script>