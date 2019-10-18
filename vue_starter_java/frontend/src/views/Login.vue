<template>
  <div class="login">
    <nav-bar/>
    <div class="login-body text-center">
    <form class="form-signin" style="width: 80%; max-width: 400px;" @submit.prevent="login">
      <h1 class="h3 mb-3 font-weight-normal" style="color: rgb(62, 102, 131)">Please Sign In</h1>
      <div class="alert alert-danger" role="alert" v-if="invalidCredentials">
        Invalid username and password!
      </div>
      <div class="alert alert-success" role="alert" v-if="this.$route.query.registration">
        Thank you for registering, please sign in.
      </div>
      <label for="username" class="sr-only">Username</label>
      <input
        type="text"
        id="username"
        class="form-control"
        placeholder="Username"
        v-model="user.username"
        required
        autofocus
      />
      <label for="password" class="sr-only">Password</label>
      <input
        type="password"
        id="password"
        class="form-control"
        placeholder="Password"
        v-model="user.password"
        required
      />
      <router-link :to="{ name: 'register' }">Need an account?</router-link>
      <button class="btn btn-light btn-lg btn-block" type="submit">Sign in</button>
    </form>
  </div>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar';
import auth from '../auth';

export default {
  name: 'login',
  components: {
    NavBar
  },
  data() {
    return {
      user: {
        username: '',
        password: '',
      },
      invalidCredentials: false,
    };
  },
  methods: {
    login() {
      fetch(`http://localhost:8080/TimeClock/login`, {
        method: 'POST',
        headers: {
          Accept: 'application/json',
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(this.user),
      })
        .then((response) => {
          if (response.ok) {
            return response.text();
          } else {
            this.invalidCredentials = true;
          }
        })
        .then((token) => {
          if (token != undefined) {
            if (token.includes('"')) {
              token = token.replace(/"/g, '');
            }
            auth.saveToken(token);
            this.$router.push('/');
          }
        })
        .catch((err) => console.error(err));
    },
  },
};
</script>


<style>
.login-body{
  margin-top: 30px;
  width: 100%;
  display: flex;
  justify-content: space-around
}

</style>
