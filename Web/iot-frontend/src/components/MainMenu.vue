<template>
  <div class="main-menu">
    <nav class="menu">
      <router-link to="/dashboard" class="menu-item">
        <span class="icon">📊</span>
        <div class="menu-text">
          <h3>Dashboard</h3>
        </div>
      </router-link>

      <router-link to="/rooms" class="menu-item">
        <span class="icon">🏠</span>
        <div class="menu-text">
          <h3>Rooms</h3>
        </div>
      </router-link>
    </nav>
  </div>
</template>

<script>
export default {
  name: 'MainMenu',
};
</script>

<style scoped>
.main-menu {
  text-align: center;
  padding: 10px;
  margin-top: -20px; /* Move the menu upwards */
}

.menu {
  display: flex;
  justify-content: center; /* Keep the items centered */
  gap: 100px; /* Increased space between menu items */
  background: #f4f4f4;
  padding: 15px 30px; /* Increased padding for bigger menu items */
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.menu-item {
  text-align: center;
  text-decoration: none;
  color: #333;
  background: #fff;
  padding: 12px 20px; /* Increased padding for bigger menu items */
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease, background-color 0.3s ease;
  font-size: 1rem; /* Increased font size */
}

.menu-item:hover {
  transform: translateY(-5px);
  background-color: #e9f7ff;
}

.icon {
  font-size: 1.7rem; /* Increased icon size */
  margin-bottom: 5px;
}

.menu-text h3 {
  font-size: 1.1rem; /* Increased text size */
  margin: 0;
}

@media (max-width: 600px) {
  .menu-item {
    padding: 8px 15px; /* Slightly reduced padding on small screens */
  }
  .icon {
    font-size: 1.4rem; /* Adjust icon size on small screens */
  }
  .menu-text h3 {
    font-size: 1rem; /* Adjust text size on small screens */
  }
}
</style>






  