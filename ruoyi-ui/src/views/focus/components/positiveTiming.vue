<template>
  <div class="timer-container">
    <div class="timer-dial">
      <div class="timer-tick"
           v-for="tick in 60"
           :key="tick"
           :style="getTickStyle(tick)">
        <div :class="{ 'highlighted': tick <= currentSecond }"
             style="height:8%; width:2px; background-color:#606266"></div>
        <div style="height: 92%;width:100%;"></div>
      </div>`

      <div class="timer-text">{{ formatTime }}</div>
    </div>

    <div style="margin-top: 15px">正计时</div>
  </div>
</template>

<script>
import {POMODORO_TIME} from "../constants/pomodoroConstants";

export default {
  name: 'PositiveTiming',
  props: {
    isTimerRunning: {
      type: Boolean
    },
    timerType: {
      type: String
    },
    isCompleted: {
      type: Boolean
    }
  },

  data() {
    return {
      currentSecond: 0,
      seconds: 0,
      timerInterval: null,
    };
  },

  watch: {
    isTimerRunning(val) {
      if (this.timerType == 'positiveTimer') {
        if (val) {
          this.tick();
        } else {
          clearInterval(this.timerInterval);
        }
      }
    },
    isCompleted(val) {
      if (this.timerType == 'positiveTimer') {
        if (val) {
          if (this.minutes >= POMODORO_TIME.FIVE) {
            this.$message({
              message: `恭喜你，已完成${this.formatTimeResult}`,
              type: 'success'
            })

            this.currentSecond = 0;
            this.seconds = 0;
            clearInterval(this.timerInterval);
          } else {
            this.validateCompletedTime();
          }
        }
      }
    },
  },

  computed: {
    minutes() {
      return Math.floor((this.seconds % 3600) / 60);
    },
    hours() {
      return Math.floor(this.seconds / 3600);
    },
    formatTime() {
      const seconds = this.seconds;
      const minutes = this.minutes;
      const hours = this.hours;

      return `${this.pad(hours)}:${this.pad(minutes % 60)}:${this.pad(seconds % 60)}`;
    },
  },

  methods: {
    validateCompletedTime() {
      if (this.minutes < POMODORO_TIME.FIVE) {
        this.$confirm("至少需要专注5分钟, 否则此次专注将不被记录", '提示', {
          confirmButtonText: '继续',
          cancelButtonText: '放弃',
          type: 'warning'
        }).then(() => {
          this.$emit('update-state', {
            isTimerRunning: true,
            showContinueOrCompleteBtn: false,
            isCompleted: false
          });

          this.tick();
        }).catch(() => {
          this.$emit('update-state', {
            isTimerRunning: false,
            showContinueOrCompleteBtn: false,
            isCompleted: true
          });

          this.currentSecond = 0;
          this.seconds = 0;
          clearInterval(this.timerInterval);
        })
      }
    },
    formatTimeResult() {
      let formattedTime = '';

      if (this.hours > 0) {
        formattedTime += `${this.hours}小时`;
      }

      if (this.minutes > 0) {
        formattedTime += `${this.minutes}分钟`;
      }

      if (this.seconds > 0) {
        formattedTime += `${this.seconds}秒`;
      }

      return formattedTime;
    },
    getTickStyle(tick) {
      const rotation = `rotate(${(tick - 1) * 6}deg)`;
      return {
        transform: rotation
      };
    },
    pad(value) {
      return value.toString().padStart(2, '0');
    },
    tick() {
      clearInterval(this.timerInterval);
      this.timerInterval = setInterval(() => {
        this.currentSecond++;
        this.seconds++;
        if (this.currentSecond == 60) {
          this.currentSecond = 0;
        }
      }, 1000);
    },
  },
}
</script>

<style scoped lang="scss">
.timer-container {
  text-align: center;
}

.timer-dial {
  position: relative;
  width: 150px;
  height: 150px;
  border: 2px solid #000;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.timer-tick {
  position: absolute;
  width: 1px;
  height: 50%;
  top: 0;
  left: 49.5%;
  transform-origin: bottom;
}

.highlighted {
  background-color: #1890ff !important;
}

.timer-text {
  font-size: 24px;
  font-weight: bold;
  margin-top: 10px;
}
</style>
