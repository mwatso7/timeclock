<template>
  <div class="logout">
    <nav-bar/>
    <div class="logout-body">
    <div class="alert alert-success logout-alert" role="alert">
        You have been logged out successfully.
      </div>
  </div>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar';
import auth from '../auth'

export default {
  name: "logout",
  components:{
    NavBar
  },
  data(){
    return {
      user: auth.getUser()
    }
  },
  created(){
    auth.logout();
    let status = false;
    this.$emit('changeLogin', status);
  }
}
</script>

<style>

.logout-body{
  width:100%;
  display:flex;
  justify-content: space-around;
}
.logout-alert{
  margin-top: 30px;
  width: 80%; 
  max-width: 400px;
}

</style>
