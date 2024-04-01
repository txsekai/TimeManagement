<template>
  <div class="rest-style">
    <div class="stalk-icon"></div>

    <div class="leaf-icon leaf-icon1"></div>
    <div class="leaf-icon leaf-icon2"></div>
    <div class="leaf-icon leaf-icon3"></div>
    <div class="leaf-icon leaf-icon4"></div>

    <div style="display: flex;flex-direction: column">
      <div class="rest-timer-text">{{ restFormatTime }}</div>

      <!--    判读条件todo-->
      <div style="font-size: 12px;color: #606266">{{ shortRestContent }}</div>

      <el-button class="btn-none-border skip-btn">跳过</el-button>
    </div>
  </div>
</template>

<script>
import {POMODORO_TIME} from "../constants/pomodoroConstants";

export default {
  name: 'PomodoroRest',
  props: {
    pomodoroResult: {
      type: Object
    },
    isShortRest: {
      type: Boolean
    },
    isLongRest: {
      type: Boolean
    }
  },

  data() {
    return {
      restSeconds: 59,
      restMinutes: POMODORO_TIME.FOUR,
      shortRestContent: '短休息',
      longRestContent: '长休息',
    }
  },

  watch: {
    isShortRest(val) {
      if(val) {
        this.tick();
      }
    },
  },

  computed: {
    restFormatTime() {
      if (this.pomodoroResult.duration !== null) {
        const restSeconds = this.restSeconds;
        const restMinutes = this.restMinutes;

        return `${this.pad(restMinutes)}:${this.pad(restSeconds)}`;
      } else {
        return `${this.pad(POMODORO_TIME.FIVE)}:${this.pad(0)}`;
      }
    },
  },

  methods: {
    pad(value) {
      return value.toString().padStart(2, '0');
    },
    tick() {
      clearInterval(this.timerInterval);

      this.restMinutes = this.pomodoroResult.shortRest;

      this.timerInterval = setInterval(() => {
        this.restSeconds--;
        if(this.restSeconds == 0) {
          this.restSeconds = 59;
          this.restMinutes--;
          if(this.restMinutes == 0) {
            this.isShortRest = false;
            this.$emit('rest-finish', this.isShortRest);
          }
        }
      })
    },
  }
}
</script>

<style scoped lang="scss">
.rest-style {
  position: relative;
  width: 150px;
  height: 150px;
  border: 3.5px solid #fa5252;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #fff;;
}

.rest-timer-text {
  font-size: 22px;
  font-weight: bold;
  margin-top: 36px;
  margin-bottom: 5px;
  color: #606266;
}

.skip-btn:hover,
.skip-btn:active,
.skip-btn:focus {
  color: #1890ff;
  background-color: #FFFFFF;
}
</style>
