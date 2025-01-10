import { createRouter, createWebHistory } from 'vue-router';
import Login from '../components/Login.vue';
import Rooms from '../components/Rooms.vue';
import Dashboard from '../components/Dashboard.vue';
import RoomForm from '../components/RoomForm.vue';

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: Login },
  { 
    path: '/rooms', 
    component: Rooms,
    meta: { requiresAuth: true } // Require login
  },
  { 
    path: '/dashboard', 
    component: Dashboard,
    meta: { requiresAuth: true } // Require login
  },
  { 
    path: '/rooms/create', 
    component: RoomForm,
    meta: { requiresAuth: true } // Require login
  },
  { 
    path: '/rooms/edit/:id', 
    component: RoomForm,
    meta: { requiresAuth: true } // Require login
  }

];

const router = createRouter({
  history: createWebHistory(),
  routes
});

// Add navigation guard to protect routes
router.beforeEach((to, from, next) => {
  const isAuthenticated = localStorage.getItem('loggedIn') === 'true'; // Check if logged in
  if (to.meta.requiresAuth && !isAuthenticated) {
    next('/login'); // Redirect to login if not authenticated
  } else {
    next(); // Allow access if authenticated
  }
});

export default router;



