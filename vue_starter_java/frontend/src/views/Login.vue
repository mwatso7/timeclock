<template>
  <div class="login">
    <nav-bar/>
    <div class="login-body text-center">
    <form class="form-signin" style="width: 80%; max-width: 350px;" @submit.prevent="login">
      <h3 class="h3 mb-3 font-weight-normal" style="color: rgb(62, 102, 131)">Please Sign In</h3>
      <div class="alert alert-danger" role="alert" v-if="invalidCredentials">
        Invalid username and password!
      </div>
      <div class="alert alert-success" role="alert" v-if="this.$route.query.registration">
        Thank you for registering, please sign in.
      </div>
      <label for="username" class="sr-only">Username</label>
      <div class="input-group">
        <div class="input-group-prepend">
          <span class="input-group-text mb-2" id="inputGroupPrepend"><i class="fas fa-user-alt"></i></span>
        </div>
      <input
        type="text"
        id="username"
        class="form-control form-control-lg mb-2"
        placeholder="Username"
        v-model="user.username"
        required
        autofocus
      />
      </div>
      <label for="password" class="sr-only">Password</label>
      <div class="input-group">
        <div class="input-group-prepend">
          <span class="input-group-text mb-1" id="inputGroupPrepend"><i class="fas fa-key"></i></span>
        </div>
      <input
        type="password"
        id="password"
        class="form-control form-control-lg mb-1"
        placeholder="Password"
        v-model="user.password"
        required
      />
      </div>
      <router-link :to="{ name: 'register' }">Need an account?</router-link>
      <b-button block class="btn btn-lg mt-1" variant="light" type="submit">Sign in</b-button>
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
