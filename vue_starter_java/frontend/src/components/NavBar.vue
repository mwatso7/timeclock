  <template>
  <div class="nav-bar">
    <div id="nav" class="navbar navbar-default sticky-top shadow-sm" >
      <span class="d-flex align-items-center"><router-link  class="navbar-brand" style="text-decoration: none;" to="/" v-b-tooltip title="Home" variant="outline-success"><span id="logo" class="h2"><i class="fas fa-user-clock"></i></span></router-link>
      <b-button v-if="isLoggedIn" pill class="btn btn-lg btn-inline" style="width: 80px" :class="{'btn-success' : !lastTimeStamp.isIn, 'btn-danger' : lastTimeStamp.isIn}" @click.prevent="recordStamp()">{{buttonText}}</b-button></span>
      <span class="d-flex align-items-center" style="color: #00ADEE;">
          <a v-if="isLoggedIn" href="#" v-b-modal.report-modal><h3 class="navtxt mr-4 mt-2" v-b-tooltip title="Send report" variant="outline-success"><i class="fas fa-paper-plane"></i></h3></a>
          <router-link class="navtxt mt-2" v-if="!isLoggedIn" to="/login" v-b-tooltip title="Login" variant="outline-success"><h2><i class="fas fa-sign-in-alt"></i></h2></router-link>        
          <router-link class="navtxt mt-2" v-if="isLoggedIn" to="/logout" v-b-tooltip title="Logout" variant="outline-success"><h2><i class="fas fa-sign-out-alt"></i></h2></router-link>
    </span>
    </div>

    <b-modal id="report-modal" ref="modal" title="Send Report" centered>
      <template slot="modal-header">
        <h5>Send Report</h5>
      </template>
      <template slot="default">
        <p>Are you sure you want to send a report of your TimeClock activity to the following email addresses?</p>
       <ul v-if="recipients[0].id != 'N/A'" style="list-style-type:none;" class="p-0">
            <li v-for="(recipient, index) in recipients" :key="recipient.id" class="mb-1">
              <div class="d-flex justify-content-between align-items-center p-2 m-0 alert" :class="{'alert-success' : recipient.enabled, 'alert-dark' : !recipient.enabled}">
                <div @click.prevent="isEnabled(index)" class="d-inline-block text-truncate"><b-form-checkbox v-model="recipient.enabled" switch inline size="lg">{{recipient.email}}</b-form-checkbox></div>
                <button type="button" class="close ml-2" data-dismiss="alert" aria-label="Close" @click="deleteRecipient(recipient.id)"><span aria-hidden="true">&times;</span></button>
              </div>
            </li>
          </ul>
        <form class="form-register" style="width: 100%;" @submit.prevent="addRecipient()">
      <label for="email" class="sr-only">Email</label>
      <div class="input-group">
        <div class="input-group-prepend">
          <span class="input-group-text pl-3 left-round" id="inputGroupPrepend"><i class="fas fa-envelope"></i></span>
        </div>
      <input
        type="email"
        id="email"
        style="border-radius: 0rem 1.5rem 1.5rem 0rem"
        class="right-round form-control form-control-lg"
        placeholder="Email"
        v-model="email"
        required
        autofocus
      />
      <b-button pill class="btn btn-success btn-lg btn-inline ml-2" type="submit">
        +
      </b-button>
      </div>
    </form>
      </template>
      <template slot="modal-footer" slot-scope="{ cancel }">
        <b-button pill size="md" variant="light" @click="cancel()">Cancel</b-button>
        <b-button pill size="md" variant="primary" @click="sendReport()">Send</b-button>
      </template>
    </b-modal> 
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

input.right-round{
  border-radius: 0rem 1.5rem 1.5rem 0rem;
}

span.left-round{
  background-color: white;
  border-radius: 1.5rem 0rem 0rem 1.5rem
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
      email: '',
      recipients: [],
      
      enabled: false,
      lastTimeStamp: {
        id: 'N/A',
        stamp: 'N/A',
        username: 'N/A',
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
    isEnabled(index){
      
      this.recipients.splice(index, 1, {
        id: this.recipients[index].id,
        email: this.recipients[index].email,
        username: this.recipients[index].username,
        enabled: !this.recipients[index].enabled
      });
    },
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
    addRecipient(){
      fetch(this.stampAPI + "/addrecipient", { 
          method: "POST",
          headers: {
            Authorization: "Bearer " + auth.getToken(),
            "Content-Type": "application/json"
          },
          body: JSON.stringify(this.email.trimLeft())
         })
        .then(response => {
          if (response.ok) {
            this.getRecipients();
            this.email = '';
          }
        })
        .catch(err => {
          console.log(err);
        });
    },
    deleteRecipient(id){
      fetch(this.stampAPI + "/deleterecipient", { 
          method: "DELETE",
          headers: {
            Authorization: "Bearer " + auth.getToken(),
            "Content-Type": "application/json"
          },
          body: JSON.stringify(id)
         })
        .then(response => {
          if (response.ok) {
            this.getRecipients();
            this.email = '';
          }
        })
        .catch(err => {
          console.log(err);
        });
    },
    getLastTimeStamp(){
      if (!this.isLoggedIn){
        this.lastTimeStamp = {
          id: 'N/A',
          stamp: 'N/A',
          username: 'N/A',
          isIn: false
        };
        return;
      }
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
        if (timeStamp){
          this.lastTimeStamp = timeStamp;
        } else {
          this.lastTimeStamp = {
            id: 'N/A',
            stamp: 'N/A',
            username: 'N/A',
            isIn: false
          };
        }
      })
      .catch((err) => console.error(err));
    },
    getRecipients(){
      if (!this.isLoggedIn){
            this.recipients = [{
            id: 'N/A',
            email: 'N/A',
            username: 'N/A',
            enabled: false
          }];
          return;
      }
      fetch(this.stampAPI + "/recipients", { 
          method: "GET",
          headers: {
            Authorization: "Bearer " + auth.getToken(),
            "Content-Type": "application/json"
          }})
      .then((response) => {
        return response.json();
      })
      .then((recipients) => {
        if (Array.isArray(recipients) && recipients.length) {
          this.recipients = recipients;
          this.recipients.forEach((recipient) => { 
            recipient.enabled = false;
          });
        } else {
          this.recipients = [{
            id: 'N/A',
            email: 'N/A',
            username: 'N/A',
            enabled: false
          }];
        }
      })
      .catch((err) => console.error(err));
    }
  },
  created(){
    //this.isLoggedIn = auth.getUser ? true : false;
    this.getLastTimeStamp();
    this.getRecipients();
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