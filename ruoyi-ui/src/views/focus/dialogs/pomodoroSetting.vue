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
          <el-button @click="durationMinus"
                     class="el-icon-remove btn-none-border btn-hover-background-none"
                     :disabled="duration==5"
          ></el-button>
          <span>{{ duration }}</span>
          <el-button @click="durationAdd"
                     class="el-icon-circle-plus btn-none-border btn-hover-background-none"
                     :disabled="duration==120"
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
                     :disabled="shortRest==10"
          ></el-button>
        </div>
        <span>分钟</span>
      </el-col>

      <el-col :span="6" style="text-align: center">
        <span>番茄个数</span>
        <div>
          <el-button @click="pomodoroNumMinus"
                     class="el-icon-remove btn-none-border btn-hover-background-none"
                     :disabled="pomodoroNum==0"
          ></el-button>
          <span>{{ pomodoroNum }}</span>
          <el-button @click="pomodoroNumAdd"
                     class="el-icon-circle-plus btn-none-border btn-hover-background-none"
                     :disabled="pomodoroNum==10"
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
                     :disabled="longRest==20"
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
export default {
  name: 'PomodoroSetting',
  props: {
    value: {
      type: Boolean,
      default: false
    },
  },

  data() {
    return {
      dialogVisible: false,
      duration: 25,
      shortRest: 5,
      pomodoroNum: 4,
      longRest: 10,
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
    }
  },

  methods: {
    durationMinus() {
      if (this.duration > 5) {
        this.duration = this.duration - 5;
      }
    },
    durationAdd() {
      if (this.duration < 120) {
        this.duration = this.duration + 5;
      }
    },
    shortRestMinus() {
      if (this.shortRest > 0) {
        this.shortRest = this.shortRest - 1;
      }
    },
    shortRestAdd() {
      if (this.shortRest < 10) {
        this.shortRest = this.shortRest + 1;
      }
    },
    pomodoroNumMinus() {
      if (this.pomodoroNum > 0) {
        this.pomodoroNum = this.pomodoroNum - 1;
      }
    },
    pomodoroNumAdd() {
      if (this.pomodoroNum < 10) {
        this.pomodoroNum = this.pomodoroNum + 1;
      }
    },
    longRestMinus() {
      if (this.longRest > 0) {
        this.longRest = this.longRest - 1;
      }
    },
    longRestAdd() {
      if (this.longRest < 20) {
        this.longRest = this.longRest + 1;
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
