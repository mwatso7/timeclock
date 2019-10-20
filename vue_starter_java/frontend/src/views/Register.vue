<template>
  <div id="register">
    <nav-bar/>
    <div class="register-body text-center">
    <form class="form-register" style="width: 80%; max-width: 350px;" @submit.prevent="register">
      <h3 class="h3 mb-3 font-weight-normal" style="color: rgb(62, 102, 131)">Create Account</h3>
      <div class="alert alert-danger" role="alert" v-if="registrationErrors">
        There were problems registering this user.
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
      <label for="email" class="sr-only">Email</label>
      <div class="input-group">
        <div class="input-group-prepend">
          <span class="input-group-text mb-2" id="inputGroupPrepend"><i class="fas fa-envelope"></i></span>
        </div>
      <input
        type="email"
        id="email"
        class="form-control form-control-lg mb-2"
        placeholder="Email"
        v-model="user.email"
        required
        autofocus
      />
      </div>
      <label for="password" class="sr-only">Password</label>
      <div class="input-group">
        <div class="input-group-prepend">
          <span class="input-group-text mb-2" id="inputGroupPrepend"><i class="fas fa-key"></i></span>
        </div>
      <input
        type="password"
        id="password"
        class="form-control form-control-lg mb-2"
        placeholder="Password"
        v-model="user.password"
        required
      />
      </div>
      <div class="input-group">
        <div class="input-group-prepend">
          <span class="input-group-text mb-1" id="inputGroupPrepend"><i class="fas fa-key"></i></span>
        </div>
      <input
        type="password"
        id="confirmPassword"
        class="form-control form-control-lg mb-1"
        placeholder="Confirm Password"
        v-model="user.confirmPassword"
        required
      />
      </div>
      <router-link :to="{ name: 'login' }">
        Have an account?
      </router-link>
      <button class="btn btn-light btn-lg btn-block mt-1" type="submit">
        Create Account
      </button>
    </form>
  </div>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar';

export default {
  name: 'register',
  components: {
    NavBar
  },
  data() {
    return {
      user: {
        username: '',
        email:'',
        password: '',
        confirmPassword: '',
        role: 'user',
      },
      registrationErrors: false,
    };
  },
  methods: {
    register() {
      fetch(`${process.env.VUE_APP_REMOTE_API}/register`, {
        method: 'POST',
        headers: {
          Accept: 'application/json',
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(this.user),
      })
        .then((response) => {
          if (response.ok) {
            this.$router.push({ path: '/login', query: { registration: 'success' } });
          } else {
            this.registrationErrors = true;
          }
        })

        .then((err) => console.error(err));
    },
  },
};
</script>

<style>
.register-body {
  margin-top: 30px;
  width: 100%;
  display: flex;
  justify-content: space-around;
}



</style>
