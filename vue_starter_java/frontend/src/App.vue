<template>

  <div id="app">
  <div id="nav" class="navbar navbar-default sticky-top shadow-sm" >
      <span class="d-flex align-items-center"><router-link  class="navbar-brand" style="text-decoration: none;" to="/"><span id="logo" class="h2"><i class="fas fa-user-clock"></i></span></router-link>
      <button v-if="isLoggedIn" class="btn btn-md btn-inline" :class="{'btn-success' : isIn, 'btn-danger' : !isIn}" @click.prevent="recordStamp()">{{buttonText}}</button></span>
      <div class="d-flex align-items-center" style="color: #00ADEE;">
          <router-link class="navtxt" v-if="!isLoggedIn" to="/login"><h2><i class="fas fa-sign-in-alt"></i></h2></router-link>        
          <router-link class="navtxt" v-if="isLoggedIn" to="/logout"><h2><i class="fas fa-sign-out-alt"></i></h2></router-link>
  </div>
  </div>
    <router-view/>
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

body > div#app > div#nav{
  background-color: rgb(62, 102, 131);
  
}
</style>


<script>
import auth from './auth'
//import NavBar from "./components/NavBar";

export default {
  components: {
    //NavBar
  },
  data(){
    return {
      isLoggedIn: auth.getUser(),
      isIn: true
    }
  },
  computed:{
    buttonText(){
      return this.isIn ? 'In' : 'Out';
    }
  },
  methods:{
    logoutUser(){
      auth.logout();
    },
    recordStamp(){
      this.isIn = !this.isIn;
    }

  },
  watch: {
    '$route': function(value){
      if (value.path === '/home'){
        this.isLoggedIn = true;
        }
      if(value.name === 'logout'){
        this.isLoggedIn = false;
      }
    }
  }
}
</script>





