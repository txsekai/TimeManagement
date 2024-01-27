<template>
  <div>
    <div class="whole-focus-plan">
      <el-button class="btn-none-border">选择专注计划</el-button>
      <el-divider direction="vertical"></el-divider>
      <el-tooltip content="自定义专注名称" placement="bottom-start">
        <el-button class="btn-none-border" icon="el-icon-edit"></el-button>
      </el-tooltip>
    </div>

    <div>
      <el-radio-group v-model="radio">
        <el-radio label="positiveTimer">
          <positive-timing :start-time="startTime" :is-timer-running="isTimerRunning"></positive-timing>
        </el-radio>

        <el-radio label="pomodoro">番茄钟</el-radio>

        <el-radio label="countdown">倒计时</el-radio>
      </el-radio-group>
    </div>

    <div>
      <el-button v-show="!isTimerRunning"
                 class="btn-none-border timer-btn"
                 icon="el-icon-video-pause"
                 @click="startTimer">开始专注
      </el-button>
      
      <div v-show="isTimerRunning">
        <el-button class="btn-none-border timer-btn"
                   icon="el-icon-video-play"
                   @click="handleContinue"
        >继续</el-button>

        <el-button class="btn-none-border timer-btn"
                   icon="el-icon-circle-check"
                   @click="handleCompleted"
        >完成</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import PositiveTiming from "./components/positiveTiming.vue";

export default {
  name: 'Focus',
  components: {PositiveTiming},

  data() {
    return {
      radio: 'pomodoro',
      isTimerRunning: false,
      startTime: null,
      elapsedTime: 0,
      tickStyle: {},
    };
  },

  methods: {
    pad(value) {
      return value.toString().padStart(2, '0');
    },
    startTimer() {
      if (this.isTimerRunning) return;

      this.isTimerRunning = true;
      this.startTime = Date.now();
    },
    handleContinue() {

    },
    handleCompleted() {

    },
  },
}
</script>

<style scoped>
.whole-focus-plan {
  border: 1px solid #1890ff;
  width: 198px;
  margin: 20px auto;
  border-radius: 10px;
  overflow: hidden;
}

.timer-btn {
  color: #000;
}

.timer-btn:hover,
.timer-btn:active,
.timer-btn:focus {
  color: #1890ff;
  background-color: #FFFFFF;
}
</style>
