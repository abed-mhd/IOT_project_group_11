<template>
  <div class="login-container">
    <h2>Login to Your Account</h2>
    <p class="description">
      Please enter your credentials to access the IoT Environmental Monitoring App. 
      If you don't have an account, please contact support.
    </p>
    <form @submit.prevent="login">
      <div class="form-group">
        <label for="username">Username</label>
        <input type="text" id="username" v-model="username" required />
      </div>
      <div class="form-group">
        <label for="password">Password</label>
        <input type="password" id="password" v-model="password" required />
      </div>
      <button type="submit" :disabled="loading" class="login-btn">Login</button>
      <p v-if="error" class="error-message">{{ error }}</p>
    </form>
    <div v-if="loading" class="loading-spinner">Loading...</div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      username: '',
      password: '',
      loading: false,
      error: ''
    };
  },
  methods: {
    login() {
      this.loading = true;
      this.error = '';

      // Make a request to the backend for authentication
      axios.get('http://localhost:8000/api/rooms', {
        auth: {
          username: this.username,
          password: this.password
        }
      })
      .then(() => {
        // Simulate a successful login and set loggedIn in localStorage
        localStorage.setItem('loggedIn', 'true');
        this.$router.push('/dashboard');  // Navigate to the dashboard
      })
      .catch(() => {
        this.error = 'Invalid credentials, please try again.';
      })
      .finally(() => {
        this.loading = false;
      });
    }
  }
};
</script>

<style scoped>
.login-container {
  max-width: 400px;
  margin: 0 auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 8px;
  background-color: #fafafa;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

h2 {
  text-align: center;
  font-size: 1.8rem;
  color: #333;
}

.description {
  text-align: center;
  font-size: 1rem;
  color: #555;
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 20px;
}

label {
  display: block;
  font-size: 1rem;
  color: #333;
  margin-bottom: 5px;
}

input {
  width: 100%;
  padding: 10px;
  font-size: 1rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  margin-bottom: 10px;
}

input:focus {
  border-color: #4caf50;
  outline: none;
}

button.login-btn {
  width: 100%;
  padding: 12px;
  font-size: 1.1rem;
  background-color: #4caf50;
  color: #fff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

button.login-btn:hover {
  background-color: #45a049;
}

button:disabled {
  background-color: #ddd;
  cursor: not-allowed;
}

.error-message {
  color: red;
  font-weight: bold;
  text-align: center;
  margin-top: 10px;
}

.loading-spinner {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
}

@media (max-width: 500px) {
  .login-container {
    padding: 15px;
  }
  h2 {
    font-size: 1.5rem;
  }
  .description {
    font-size: 0.9rem;
  }
}
</style>





  