<template>
  <div>
    <div class="timer-container">
      <div v-show="!isShortRest" class="pomodoro-container">
        <div class="stalk-icon"></div>

        <div class="leaf-icon leaf-icon1"></div>
        <div class="leaf-icon leaf-icon2"></div>
        <div class="leaf-icon leaf-icon3"></div>
        <div class="leaf-icon leaf-icon4"></div>

        <div class="timer-text">{{ formatTime }}</div>
      </div>

      <!--   休息的样式     -->
      <pomodoro-rest v-show="isShortRest" :pomodoro-result="pomodoroResult"
                     :is-short-rest="isShortRest"
                     @rest-finish="handleRestFinish"
      ></pomodoro-rest>

      <div style="margin-top: 15px">番茄钟</div>
    </div>

    <pomodoro-setting v-model="pomodoroSettingVisible"
                      @pomodoroSettingConfirm="handlePomodoroSettingConfirm"></pomodoro-setting>
  </div>
</template>

<script>
import PomodoroSetting from "../dialogs/pomodoroSetting.vue";
import {POMODORO_TIME} from "../constants/pomodoroConstants";
import PomodoroRest from "./pomodoroRest.vue";

export default {
  name: 'Pomodoro',
  components: {PomodoroSetting, PomodoroRest},

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
      seconds: 59,
      minutes: POMODORO_TIME.TWENTYFOUR,
      pastTimeInSeconds: 0,
      timerInterval: null,
      pomodoroResult: {duration: null, shortRest: null, pomodoroNum: null, longRest: null, autoRest: null},
      isShortRest: false,
      isLongRest: false,
    }
  },

  watch: {
    isTimerRunning(val) {
      if (this.timerType == 'pomodoro') {
        if (val) {
          // 点开始计时
          if (this.openPomodoroSetting) {
            this.pomodoroSettingVisible = true;
          } else {
            // 继续
            if (this.pomodoroResult.duration !== null) {
              this.tick();
            }
          }
        } else {
          clearInterval(this.timerInterval);
        }
      }
    },
    isCompleted(val) {
      if (this.timerType == 'pomodoro') {
        if (val) {
          if (this.calcPastTimeInMinutes() >= 1) {
            this.$message({
              message: `恭喜你，已完成${this.formatTimeResult()}`,
              type: 'success'
            })

            this.pomodoroResult = {duration: null, shortRest: null, pomodoroNum: null, longRest: null, autoRest: null};
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
        const minutes = this.minutes;
        return `${this.pad(minutes)}:${this.pad(seconds)}`;
      } else {
        return `${this.pad(POMODORO_TIME.TWENTYFIVE)}:${this.pad(0)}`;
      }
    },
  },

  methods: {
    validateCompletedTime() {
      if (this.calcPastTimeInMinutes() < 1) {
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

          this.pomodoroResult = {duration: null, shortRest: null, pomodoroNum: null, longRest: null, autoRest: null};
          clearInterval(this.timerInterval);
        })
      }
    },
    pad(value) {
      return value.toString().padStart(2, '0');
    },
    tick() {
      clearInterval(this.timerInterval);
      if (this.pomodoroResult.duration == null) {
        // 开始计时
        this.minutes = this.pomodoroResult.duration - 1;
      }

      this.timerInterval = setInterval(() => {
        this.seconds--;
        if (this.seconds == 0) {
          this.seconds = 59;
          this.minutes--;
          if (this.minutes == 0) {
            /*
             每个番茄时长到了之后要提示
              如果自动休息, 就直接进入休息时间, 可以选择跳过休息, 在休息中也可以继续休息完成计时
              如果没有自动休息就继续计时, 页面展示休息button, 等待用户暂停并开始休息
             长休息的逻辑--一轮的番茄个数完成
             */
            this.isShortRest = true;
          }
        }
      }, 1000);
    },
    calcPastTimeInSeconds() {
      let totalTimeInSeconds = this.pomodoroResult.duration * 60;
      let remainingTimeInSeconds = this.minutes * 60 + this.seconds;

      let pastTimeInSeconds = totalTimeInSeconds - remainingTimeInSeconds;

      return pastTimeInSeconds;
    },
    calcPastTimeInMinutes() {
      let pastTimeInMinutes = Math.floor(this.calcPastTimeInSeconds() / 60);

      return pastTimeInMinutes;
    },
    formatTimeResult() {
      let minutes = this.calcPastTimeInMinutes();
      let seconds = this.calcPastTimeInSeconds() % 60;

      let formattedTime = `${minutes}分钟${seconds}秒`;

      return formattedTime;
    },
    handlePomodoroSettingConfirm(pomodoroResult) {
      this.pomodoroResult = pomodoroResult;

      this.minutes = this.pomodoroResult.duration - 1;
      this.seconds = 59;
      this.tick();
    },
    handleRestFinish(isShortRest) {
      this.isShortRest = isShortRest;
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

.timer-text {
  font-size: 24px;
  font-weight: bold;
  color: #fff;
}
</style>
