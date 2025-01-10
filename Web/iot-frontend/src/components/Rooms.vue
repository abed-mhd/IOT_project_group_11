<template>
  <div class="rooms-container">
    <h2>Rooms</h2>
    <button class="create-button" @click="createRoom">Create Room</button>
    <div class="room-list">
      <div v-for="room in rooms" :key="room.id" class="room-card">
        <p><strong>Name:</strong> {{ room.name }}</p>
        <p><strong>Surface:</strong> {{ room.surface }} m²</p>
        <p><strong>Number of Windows:</strong> {{ room.windows }}</p>
        <p><strong>Sensor:</strong> {{ room.sensor }}</p>
        <div class="button-group">
          <button class="edit-button" @click="editRoom(room.id)">Edit</button>
          <button class="delete-button" @click="deleteRoom(room.id)">Delete</button>
        </div>
      </div>
    </div>
    <button class="mqtt-button" @click="showMqttPublisher = true">Publish to MQTT</button>
    <div v-if="showMqttPublisher" class="mqtt-publisher">
      <h2>MQTT Publisher</h2>
      <form @submit.prevent="publishMessage">
        <div>
          <label for="room">Room ID:</label>
          <input type="number" id="room" v-model="roomId" required />
        </div>
        <div>
          <label for="message">Message:</label>
          <textarea id="message" v-model="message" required></textarea>
        </div>
        <button type="submit">Publish</button>
        <button type="button" @click="showMqttPublisher = false">Cancel</button>
      </form>
      <p v-if="response">{{ response }}</p>
    </div>
    <router-view @add-room="addRoom" @update-room="updateRoom"></router-view>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      rooms: [],
      showMqttPublisher: false,
      roomId: '',
      message: '',
      response: ''
    };
  },
  created() {
    this.fetchRooms();
  },
  methods: {
    fetchRooms() {
      axios.get('http://localhost:8000/api/rooms')
        .then(response => {
          this.rooms = response.data;
        })
        .catch(error => {
          console.error('Error fetching rooms:', error);
        });
    },
    createRoom() {
      this.$router.push('/rooms/create');
    },
    editRoom(id) {
      this.$router.push(`/rooms/edit/${id}`);
    },
    deleteRoom(id) {
      axios.delete(`http://localhost:8000/api/rooms/${id}`)
        .then(() => {
          this.rooms = this.rooms.filter(room => room.id !== id);
        })
        .catch(error => {
          console.error('Error deleting room:', error);
        });
    },
    addRoom(room) {
      this.rooms.push(room);
    },
    updateRoom(updatedRoom) {
      const index = this.rooms.findIndex(room => room.id === updatedRoom.id);
      if (index !== -1) {
        this.rooms.splice(index, 1, updatedRoom);
      }
    },
    publishMessage() {
      const topic = `rooms/message`;
      const payload = {
        room: this.roomId,
        message: this.message
      };

      axios.post('http://localhost:8000/mqtt/publish', payload, {
        params: { topic }
      })
      .then(response => {
        this.response = response.data;
      })
      .catch(error => {
        console.error('Error publishing message:', error);
        this.response = 'Error publishing message';
      });
    }
  }
};
</script>

<style scoped>
.rooms-container {
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

.create-button, .mqtt-button {
  background-color: #4CAF50;
  color: white;
  padding: 10px 20px;
  border: none;
  cursor: pointer;
  margin-bottom: 20px;
  display: block;
  margin-left: auto;
  margin-right: auto;
}

.create-button:hover, .mqtt-button:hover {
  background-color: #45a049;
}

.room-list {
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

.room-card p {
  margin: 10px 0;
  color: #333;
}

.button-group {
  display: flex;
  justify-content: space-between;
}

.edit-button, .delete-button {
  background-color: #008CBA;
  color: white;
  padding: 5px 10px;
  border: none;
  cursor: pointer;
  border-radius: 4px;
}

.delete-button {
  background-color: #f44336;
}

.edit-button:hover {
  background-color: #007bb5;
}

.delete-button:hover {
  background-color: #da190b;
}

.mqtt-publisher {
  background-color: #fff;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  max-width: 400px;
  margin: 20px auto;
}

form {
  display: flex;
  flex-direction: column;
}

form div {
  margin-bottom: 10px;
}

form label {
  margin-bottom: 5px;
}

form input, form textarea {
  width: 100%;
  padding: 8px;
  box-sizing: border-box;
}

form button {
  padding: 10px;
  background-color: #007bff;
  color: white;
  border: none;
  cursor: pointer;
  margin-top: 10px;
}

form button:hover {
  background-color: #0056b3;
}
</style>
  