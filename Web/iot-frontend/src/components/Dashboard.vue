<template>
  <div class="dashboard-container">
    <h2>Dashboard</h2>
    <p>View sensor data in real-time.</p>
    <div class="rooms-container">
      <div v-for="room in rooms" :key="room.id" class="room-card">
        <h3>Room {{ room.id }}: {{ room.name }}</h3>
        <p><strong>Surface:</strong> {{ room.surface }} m²</p>
        <p><strong>Windows:</strong> {{ room.windows }}</p>
        <p><strong>Sensor:</strong> {{ room.sensor }}</p>
        <ul class="sensor-list">
          <li v-for="sensor in room.sensors" :key="sensor.id" class="sensor-item">
            <div class="sensor-data">
              <span class="icon">🌡️</span>
              <p><strong>Temperature:</strong> {{ sensor.temperature }} °C</p>
            </div>
            <div class="sensor-data">
              <span class="icon">💧</span>
              <p><strong>Humidity:</strong> {{ sensor.humidity }} %</p>
            </div>
            <div class="sensor-data">
              <span class="icon">🌬️</span>
              <p><strong>Pressure:</strong> {{ sensor.pressure }} hPa</p>
            </div>
            <div class="sensor-data">
              <span class="icon">💡</span>
              <p><strong>Luminosity:</strong> {{ sensor.luminosity }} lx</p>
            </div>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      rooms: []
    };
  },
  created() {
    this.fetchRoomData();
    this.setupWebSocket();
  },
  methods: {
    fetchRoomData() {
      axios.get('http://localhost:8000/api/rooms')
        .then(response => {
          this.rooms = response.data;
          this.rooms.forEach(room => {
            this.fetchSensorData(room.id);
          });
        })
        .catch(error => {
          console.error('Error fetching room data:', error);
        });
    },
    fetchSensorData(roomId) {
      axios.get(`http://localhost:8000/sensor/data?room=${roomId}`, {
        auth: {
          username: 'admin',
          password: 'password'
        }
      })
        .then(sensorResponse => {
          const room = this.rooms.find(r => r.id === roomId);
          if (room) {
            room.sensors = this.getLatestSensorData(sensorResponse.data.filter(sensor => sensor.room === room.id));
          }
        })
        .catch(error => {
          console.error('Error fetching sensor data:', error);
        });
    },
    getLatestSensorData(sensorData) {
      const latestSensorData = {};
      sensorData.forEach(sensor => {
        if (!latestSensorData[sensor.room] || sensor.id > latestSensorData[sensor.room].id) {
          latestSensorData[sensor.room] = sensor;
        }
      });
      return Object.values(latestSensorData);
    },
    setupWebSocket() {
      const socket = new WebSocket('ws://localhost:8000/ws');
      socket.onmessage = (event) => {
        const newSensorData = JSON.parse(event.data);
        this.updateSensorData(newSensorData);
      };
    },
    updateSensorData(newSensorData) {
      const room = this.rooms.find(r => r.id === newSensorData.room);
      if (room) {
        const sensorIndex = room.sensors.findIndex(sensor => sensor.id === newSensorData.id);
        if (sensorIndex !== -1) {
          // Update existing sensor data
          this.$set(room.sensors, sensorIndex, newSensorData);
        } else {
          // Add new sensor data
          room.sensors.push(newSensorData);
        }
      }
    }
  }
};
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
  background-color: #f0f4f8;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

h2 {
  text-align: center;
  color: #333;
  margin-bottom: 20px;
}

p {
  text-align: center;
  color: #666;
}

.rooms-container {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
}

.room-card {
  background-color: #fff;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  flex: 1 1 300px;
  max-width: 300px;
}

.room-card h3 {
  margin-top: 0;
  color: #4CAF50;
}

.sensor-list {
  list-style-type: none;
  padding: 0;
}

.sensor-item {
  background-color: #f9f9f9;
  margin-bottom: 10px;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.sensor-data {
  display: flex;
  align-items: center;
  margin-bottom: 5px;
}

.sensor-data .icon {
  font-size: 1.5rem;
  margin-right: 10px;
}

.sensor-item p {
  margin: 0;
  color: #333;
}

.sensor-item p strong {
  color: #4CAF50;
}
</style>
  