<template>
  <div>
    <div class="timer-container">
      <div class="pomodoro-container">
        <div class="stalk-icon"></div>

        <div class="leaf-icon leaf-icon1"></div>
        <div class="leaf-icon leaf-icon2"></div>
        <div class="leaf-icon leaf-icon3"></div>
        <div class="leaf-icon leaf-icon4"></div>

        <div class="timer-text">{{ formatTime }}</div>
      </div>

      <div style="margin-top: 15px">番茄钟</div>
    </div>

    <pomodoro-setting v-model="pomodoroSettingVisible"
                      @pomodoroSettingConfirm="handlePomodoroSettingConfirm"></pomodoro-setting>
  </div>
</template>

<script>
import PomodoroSetting from "../dialogs/pomodoroSetting.vue";

export default {
  name: 'Pomodoro',
  components: {PomodoroSetting},

  props: {
    isTimerRunning: {
      type: Boolean
    },
    timerType: {
      type: String
    },
    isCompleted: {
      type: Boolean
    },
    openPomodoroSetting: {
      type: Boolean
    }
  },

  data() {
    return {
      pomodoroSettingVisible: false,
      seconds: 60,
      minutes: 24,
      timerInterval: null,
      pomodoroResult: {duration: null, shortRest: null, pomodoroNum: null, longRest: null, autoRest: null},
    }
  },

  watch: {
    isTimerRunning(val) {
      if (this.timerType == 'pomodoro') {
        if (val) {
          if (this.openPomodoroSetting) {
            this.pomodoroSettingVisible = true;
          }
          this.tick();
        } else {
          clearInterval(this.timerInterval);
        }
      }
    },
    isCompleted(val) {
      if (this.timerType == 'pomodoro') {
        if (val) {
          if (this.minutes >= 5) {
            this.$message({
              message: `恭喜你，已完成${this.formatTimeResult}`,
              type: 'success'
            })

            this.pomodoroResult.duration = null;

            clearInterval(this.timerInterval);
          } else {
            this.validateCompletedTime();
          }
        }
      }
    },
  },

  computed: {
    formatTime() {
      if (this.pomodoroResult.duration !== null) {
        const seconds = this.seconds;
        const minutes = this.pomodoroResult.duration - 1;
        return `${this.pad(minutes)}:${this.pad(seconds)}`;
      } else {
        return `${this.pad(25)}:${this.pad(0)}`;
      }
    }
  },

  methods: {
    validateCompletedTime() {
      if (this.minutes < 5) {
        this.$confirm("至少需要专注5分钟, 否则此次专注将不被记录", '提示', {
          confirmButtonText: '继续',
          cancelButtonText: '完成',
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

          this.pomodoroResult.duration = null;
          clearInterval(this.timerInterval);
        })
      }
    },
    pad(value) {
      return value.toString().padStart(2, '0');
    },
    tick() {
      clearInterval(this.timerInterval);
      this.minutes = this.pomodoroResult.duration - 1;

      this.timerInterval = setInterval(() => {
        this.seconds--;
        if (this.seconds == 0) {
          this.seconds = 60;
          this.minutes--;
          if (this.minutes == 0) {
            this.$message('恭喜你已完成');
          }
        }
      }, 1000);
    },
    formatTimeResult() {
      let totalTimeInSeconds = this.pomodoroResult.duration * 60;
      let remainingTimeInSeconds = this.minutes * 60 + this.seconds;

      let pastTimeInSeconds = totalTimeInSeconds - remainingTimeInSeconds;

      let minutes = Math.floor(pastTimeInSeconds / 60);
      let seconds = pastTimeInSeconds % 60;

      let formattedTime = `${minutes}分钟 + ${this.pad(seconds)}秒`;

      return formattedTime;
    },
    handlePomodoroSettingConfirm(pomodoroResult) {
      this.pomodoroResult = pomodoroResult;
    },
  }
}
</script>

<style scoped lang="scss">
.timer-container {
  text-align: center;
}

.pomodoro-container {
  position: relative;
  width: 150px;
  height: 150px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(to bottom, #ffcccc, #f03e3e);
}

.stalk-icon {
  position: absolute;
  width: 5%;
  height: 10%;
  top: -10px;
  background-color: #2f9e44;
  border-radius: 4px;
}

.leaf-icon {
  position: absolute;
  opacity: 0.8;
  background: linear-gradient(to bottom, #37b24d, #2f9e44);
  z-index: 1;
}

.leaf-icon1 {
  width: 20%;
  height: 35%;
  top: 0;
  left: 31%;
  border-radius: 100% 4px;
}

.leaf-icon2 {
  width: 20%;
  height: 35%;
  top: 0;
  left: 50%;
  border-radius: 4px 100%;
}

.leaf-icon3 {
  width: 26%;
  height: 25%;
  top: 2px;
  left: 58%;
  border-radius: 4px 100%;
}

.leaf-icon4 {
  width: 26%;
  height: 25%;
  top: 2px;
  left: 18%;
  border-radius: 100% 4px;
}

.timer-text {
  font-size: 24px;
  font-weight: bold;
  color: #fff;
}
</style>
