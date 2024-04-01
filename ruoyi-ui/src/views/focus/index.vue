<template>
  <div>
    <div class="whole-focus-plan">
      <el-button class="btn-none-border btn-hover-background-none">选择专注计划</el-button>
      <el-divider direction="vertical"></el-divider>
      <el-tooltip content="自定义专注名称" placement="bottom-start">
        <el-button class="btn-none-border btn-hover-background-none" icon="el-icon-edit"></el-button>
      </el-tooltip>
    </div>

    <el-radio-group v-model="radio" class="all-timer-container">
      <el-radio label="positiveTimer">
        <positive-timing :is-timer-running="isTimerRunning" :timer-type="timerType"
                         :is-completed="isCompleted"
                         @update-state="handleUpdateState"
        ></positive-timing>
      </el-radio>

      <el-radio label="pomodoro">
        <pomodoro :is-timer-running="isTimerRunning" :timer-type="timerType"
                  :is-completed="isCompleted"
                  :open-pomodoro-setting="openPomodoroSetting"
                  @update-state="handleUpdateState"
        ></pomodoro>
      </el-radio>

      <el-radio label="countdown">倒计时</el-radio>
    </el-radio-group>

    <div style="text-align: center">
      <el-button v-show="!isTimerRunning && !showContinueOrCompleteBtn"
                 style="border-radius: 10px;font-size: 18px"
                 @click="startTimer">开始专注
      </el-button>

      <el-button v-show="isTimerRunning"
                 class="btn-none-border timer-btn large-btn"
                 icon="el-icon-video-pause"
                 @click="handlePause">暂停
      </el-button>

      <div v-show="showContinueOrCompleteBtn">
        <el-button class="btn-none-border timer-btn large-btn"
                   icon="el-icon-video-play"
                   @click="handleContinue"
        >继续
        </el-button>

        <el-button class="btn-none-border timer-btn large-btn"
                   icon="el-icon-circle-check"
                   @click="handleCompleted"
        >完成
        </el-button>
      </div>
    </div>
  </div>
</template>

<script>
import PositiveTiming from "./components/positiveTiming.vue";
import Pomodoro from "./components/pomodoro.vue";

export default {
  name: 'Focus',
  components: {Pomodoro, PositiveTiming},

  data() {
    return {
      radio: 'pomodoro',
      isTimerRunning: false,
      timerType: '',
      showContinueOrCompleteBtn: false,
      isCompleted: false,

      openPomodoroSetting: false,

    };
  },

  methods: {
    startTimer() {
      if (this.isTimerRunning) return;

      this.isTimerRunning = true;
      this.timerType = this.radio;
      this.isCompleted = false;

      if(this.timerType == 'pomodoro') {
        this.openPomodoroSetting = true;
      }
    },
    handlePause() {
      this.showContinueOrCompleteBtn = true;
      this.isTimerRunning = false;

      if(this.timerType == 'pomodoro') {
        this.openPomodoroSetting = false;
      }
    },
    handleContinue() {
      this.showContinueOrCompleteBtn = false;
      this.isTimerRunning = true;

      if(this.timerType == 'pomodoro') {
        this.openPomodoroSetting = false;
      }
    },
    handleCompleted() {
      this.isCompleted = true;
      this.isTimerRunning = false;
      this.showContinueOrCompleteBtn = false;

      if(this.timerType == 'pomodoro') {
        this.openPomodoroSetting = false;
      }
    },
    handleUpdateState(state) {
      // 更新父组件的状态属性
      this.isTimerRunning = state.isTimerRunning;
      this.showContinueOrCompleteBtn = state.showContinueOrCompleteBtn;
      this.isCompleted = state.isCompleted;
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

.all-timer-container {
  margin: 50px 0;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 100px;
}

::v-deep .el-radio__input {
  display: none;
}

.large-btn {
  font-size: 20px;
}

::v-deep .el-icon-video-pause,
::v-deep .el-icon-video-play,
::v-deep .el-icon-circle-check {
  font-size: 24px;
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
