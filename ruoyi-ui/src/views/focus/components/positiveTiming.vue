<template>
  <div class="timer-container">
    <div class="timer-circle">
      <div class="timer-tick" :style="tickStyle"></div>
      <div class="timer-text">{{ formatTime }}</div>
    </div>
    <div>正计时</div>
  </div>
</template>

<script>
export default {
  name: 'PositiveTiming',
  props: {
    startTime: {
      type: Date
    },
    isTimerRunning: {
      type: Boolean
    }
  },

  data() {
    return {
      elapsedTime: 0,
      tickStyle: {},
    };
  },

  watch: {
    isTimerRunning(val) {
      if(val) {
        this.tick();
      }
    }
  },

  computed: {
    formatTime() {
      const seconds = Math.floor(this.elapsedTime / 1000);
      const minutes = Math.floor(seconds / 60);
      const hours = Math.floor(minutes / 60);

      return `${this.pad(hours)}:${this.pad(minutes % 60)}:${this.pad(seconds % 60)}`;
    },
  },
  methods: {
    pad(value) {
      return value.toString().padStart(2, '0');
    },
    tick() {
      const currentTime = Date.now();
      this.elapsedTime = currentTime - this.startTime;

      const rotation = (this.elapsedTime / 1000) * 6; // 每秒旋转 6 度
      this.tickStyle = {
        transform: `rotate(${rotation}deg)`,
      };

      requestAnimationFrame(this.tick);
    },
  },
}
</script>

<style scoped>
.timer-container {
  text-align: center;
}

.timer-circle {
  position: relative;
  width: 150px;
  height: 150px;
  border: 2px solid #000;
  border-radius: 50%;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
}

.timer-tick {
  position: absolute;
  width: 2px;
  height: 70px;
  background-color: #000;
  top: 2px;
  left: calc(50% - 1px);
  transform-origin: bottom;
  transition: transform 1s linear;
}

.timer-text {
  font-size: 24px;
  font-weight: bold;
  margin-top: 10px;
}


</style>
