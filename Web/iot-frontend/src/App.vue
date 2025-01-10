<template>
  <div id="app">
    <!-- Show the menu only when the user is logged in and not on the login page -->
    <MainMenu v-if="isLoggedIn && !isLoginPage" />
    <router-view />
  </div>
</template>

<script>
import MainMenu from './components/MainMenu.vue';

export default {
  components: {
    MainMenu,
  },
  data() {
    return {
      isLoggedIn: false, // Default state for login
      isLoginPage: false, // Track if the current route is login page
    };
  },
  mounted() {
    // Check if the user is logged in when the app is mounted
    this.isLoggedIn = localStorage.getItem('loggedIn') === 'true';
    this.checkIfLoginPage();
  },
  watch: {
    // Watch route changes to update login page check
    '$route'() {
      this.checkIfLoginPage();
    }
  },
  methods: {
    // Check if the current route is the login page
    checkIfLoginPage() {
      this.isLoginPage = this.$route.path === '/login';
    }
  },
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>






