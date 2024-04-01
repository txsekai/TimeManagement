<template>
  <el-dialog
    title="设置番茄钟"
    :visible.sync="dialogVisible"
    width="30%"
    center
    append-to-body
    :before-close="handleClose"
  >
    <el-row>
      <el-col :span="6" style="text-align: center">
        <span>番茄时长</span>
        <div>
<!--          <el-button @click="durationMinus"-->
<!--                     class="el-icon-remove btn-none-border btn-hover-background-none"-->
<!--                     :disabled="duration==POMODORO_TIME.FIVE"-->
<!--          ></el-button>-->
          <el-button @click="durationMinus"
                     class="el-icon-remove btn-none-border btn-hover-background-none"
                     :disabled="duration==0"
          ></el-button>
          <span>{{ duration }}</span>
          <el-button @click="durationAdd"
                     class="el-icon-circle-plus btn-none-border btn-hover-background-none"
                     :disabled="duration==POMODORO_TIME.HUNDREDTWENTY"
          ></el-button>
        </div>
        <span>分钟</span>
      </el-col>

      <el-col :span="6" style="text-align: center">
        <span>短休息</span>
        <div>
          <el-button @click="shortRestMinus"
                     class="el-icon-remove btn-none-border btn-hover-background-none"
                     :disabled="shortRest==0"
          ></el-button>
          <span>{{ shortRest }}</span>
          <el-button @click="shortRestAdd"
                     class="el-icon-circle-plus btn-none-border btn-hover-background-none"
                     :disabled="shortRest==POMODORO_TIME.TEN"
          ></el-button>
        </div>
        <span>分钟</span>
      </el-col>

      <el-col :span="6" style="text-align: center">
        <span>番茄个数</span>
        <div>
          <el-button @click="pomodoroNumMinus"
                     class="el-icon-remove btn-none-border btn-hover-background-none"
                     :disabled="pomodoroNum==POMODORO_TIME.ONE"
          ></el-button>
          <span>{{ pomodoroNum }}</span>
          <el-button @click="pomodoroNumAdd"
                     class="el-icon-circle-plus btn-none-border btn-hover-background-none"
                     :disabled="pomodoroNum==POMODORO_TIME.TEN"
          ></el-button>
        </div>
      </el-col>

      <el-col :span="6" style="text-align: center">
        <span>长休息</span>
        <div>
          <el-button @click="longRestMinus"
                     class="el-icon-remove btn-none-border btn-hover-background-none"
                     :disabled="longRest==0"
          ></el-button>
          <span>{{ longRest }}</span>
          <el-button @click="longRestAdd"
                     class="el-icon-circle-plus btn-none-border btn-hover-background-none"
                     :disabled="longRest==POMODORO_TIME.TWENTY"
          ></el-button>
        </div>
        <span>分钟</span>
      </el-col>
    </el-row>

    <div slot="footer" class="dialog-footer">
      <el-switch
        v-model="autoRest"
        active-text="自动休息"
      ></el-switch>
    </div>
  </el-dialog>
</template>

<script>
import {POMODORO_TIME} from "../constants/pomodoroConstants";

export default {
  name: 'PomodoroSetting',
  computed: {
    POMODORO_TIME() {
      return POMODORO_TIME
    }
  },
  props: {
    value: {
      type: Boolean,
      default: false
    },
  },

  data() {
    return {
      dialogVisible: false,
      duration: POMODORO_TIME.TWENTYFIVE,
      shortRest: POMODORO_TIME.FIVE,
      pomodoroNum: POMODORO_TIME.FOUR,
      longRest: POMODORO_TIME.TEN,
      autoRest: true,
      pomodoroResult: {duration: null, shortRest: null, pomodoroNum: null, longRest: null, autoRest: null},
    }
  },

  created() {
    this.dialogVisible = this.value;
  },

  watch: {
    value(val) {
      this.dialogVisible = val;

      if(this.dialogVisible) {
        this.initSetting();
      }
    }
  },

  methods: {
    initSetting() {
      this.duration = POMODORO_TIME.TWENTYFIVE;
      this.shortRest = POMODORO_TIME.FIVE;
      this.pomodoroNum = POMODORO_TIME.FOUR;
      this.longRest = POMODORO_TIME.TEN;
      this.autoRest = true;
    },
    durationMinus() {
      // if (this.duration > POMODORO_TIME.FIVE) {
      //   this.duration = this.duration - POMODORO_TIME.FIVE;
      // }
      if (this.duration > 0) {
        this.duration = this.duration - POMODORO_TIME.FIVE;
      }
    },
    durationAdd() {
      // if (this.duration < POMODORO_TIME.HUNDREDTWENTY) {
      //   this.duration = this.duration + POMODORO_TIME.FIVE;
      // }
      if (this.duration < POMODORO_TIME.HUNDREDTWENTY) {
        this.duration = this.duration + 1;
      }
    },
    shortRestMinus() {
      if (this.shortRest > 0) {
        this.shortRest = this.shortRest - POMODORO_TIME.ONE;
      }
    },
    shortRestAdd() {
      if (this.shortRest < POMODORO_TIME.TEN) {
        this.shortRest = this.shortRest + POMODORO_TIME.ONE;
      }
    },
    pomodoroNumMinus() {
      if (this.pomodoroNum > 0) {
        this.pomodoroNum = this.pomodoroNum - POMODORO_TIME.ONE;
      }
    },
    pomodoroNumAdd() {
      if (this.pomodoroNum < POMODORO_TIME.TEN) {
        this.pomodoroNum = this.pomodoroNum + POMODORO_TIME.ONE;
      }
    },
    longRestMinus() {
      if (this.longRest > 0) {
        this.longRest = this.longRest - POMODORO_TIME.ONE;
      }
    },
    longRestAdd() {
      if (this.longRest < POMODORO_TIME.TWENTY) {
        this.longRest = this.longRest + POMODORO_TIME.ONE;
      }
    },
    handleClose() {
      this.dialogVisible = false;

      const {duration, shortRest, pomodoroNum, longRest, autoRest} = this;
      this.pomodoroResult = {duration, shortRest, pomodoroNum, longRest, autoRest};

      this.$emit("input", this.dialogVisible);
      this.$emit("pomodoroSettingConfirm", this.pomodoroResult);
    }
  }
}
</script>

<style scoped lang="scss">
</style>
