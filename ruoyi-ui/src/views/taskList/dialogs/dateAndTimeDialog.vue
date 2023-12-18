<template>
  <el-dialog
    title="请设置日期, 时间"
    :visible.sync="dialogVisible"
    width="30%"
    center
    :before-close="handleClose"
  >
    <el-row class="mb20">
      <!--  开始日期和开始时间    -->
      <el-col :span="12">
        <span style="display: inline-block; width: 100%;" class="mb5">开始日期</span>
        <date-item class="mb10"
                   v-model="startTime"
                   align="left"
                   :picker-options="pickerOptionsStartDate"
        ></date-item>

        <el-switch
          class="mb5"
          style="width: 100%"
          active-text="开始时间"
          v-model="startTimeVisible"
        ></el-switch>
        <time-item v-show="startTimeVisible" v-model="startTime"></time-item>
      </el-col>

      <!--  完成日期和完成时间    -->
      <el-col :span="12">
        <el-switch
          class="mb5"
          style="width: 100%"
          active-text="完成日期"
          v-model="completedDateVisible"
        ></el-switch>
        <date-item class="mb10"
                   v-show="completedDateVisible"
                   v-model="completedTime"
                   align="right"
                   :picker-options="pickerOptionsCompletedDate"></date-item>
        <div v-show="!completedDateVisible" style="min-height: 46px"></div>

        <el-switch
          class="mb5"
          style="width: 100%"
          active-text="完成时间"
          v-model="completedTimeVisible"
        ></el-switch>
        <time-item v-show="completedTimeVisible" v-model="completedTime"></time-item>
      </el-col>
    </el-row>

    <el-row v-if="completedTimeVisible" class="mb10">
      <el-col :span="12">
        <span>所用时长：{{ timeDiff }} 分钟</span>
      </el-col>

      <el-col :span="12"><span>{{ formattedTimeDiff }}</span></el-col>
    </el-row>

    <div class="spend-time-grid mb24">
      <el-button
        v-for="t in timeSpentOptions"
        :key="t"
        class="spend-time-padding"
        :disabled="timeSpentBtnDisabled"
        @click="handlePlanTime(t)"
        plain
        :type="timeDiff == t ? 'primary' : ''"
      >
        {{ t }}分钟
      </el-button>
    </div>

    <el-row>
      <el-button class="mb8" @click="handleOpenRepeatDialog">重复</el-button>
      <el-row v-if="task.taskRepeatId!==null" v-html="formattedRepeatResult(repeat)"></el-row>
    </el-row>

    <div slot="footer">
      <el-button v-show="showDateDeleteButton"
                 @click="handleDateDelete"
      >删除日程</el-button>
    </div>

    <repeat-dialog v-model="repeatDialogVisible" :title="repeatDialogTitle" :repeat="repeat"></repeat-dialog>
  </el-dialog>
</template>

<script>
import DateItem from "../components/dateItem.vue";
import TimeItem from "../components/timeItem.vue";
import RepeatDialog from "./repeatDialog.vue";
import DateMixin from "../mixins/formatDate";
import RepeatMixin from "../mixins/formatRepeat";

export default {
  name: 'DateAndTimeDialog',
  components: {RepeatDialog, TimeItem, DateItem},
  mixins: [DateMixin, RepeatMixin],

  props: {
    value: {
      type: Boolean,
      default: false
    },
    task: {
      type: Object
    }
  },

  data() {
    return {
      dialogVisible: false,

      completedDateVisible: false,
      startTimeVisible: false,
      completedTimeVisible: false,

      startTime: new Date(),
      pickerOptionsStartDate: {
        shortcuts: [{
          text: '今天',
          onClick(picker) {
            picker.$emit('pick', new Date());
          }
        }]
      },

      completedTime: new Date(),
      pickerOptionsCompletedDate: {
        disabledDate: time => {
          const startOfDay = new Date(time.getFullYear(), time.getMonth(), time.getDate());
          const copyOfStartTime = new Date(this.startTime.getTime());
          copyOfStartTime.setHours(0, 0, 0, 0);
          return startOfDay.getTime() < copyOfStartTime;
        },
        shortcuts: [{
          text: '今天',
          onClick(picker) {
            picker.$emit('pick', new Date());
          }
        }]
      },

      timeSpentOptions: [10, 15, 20, 30, 40, 60, 90, 120],

      repeatDialogVisible: false,
      repeat: {repeatValue: null, endRepeat: null, endRepeatDate: null, customResult: {}}
    }
  },

  watch: {
    value(val) {
      this.dialogVisible = val;
    },
    task: {
      handler(newTask) {
        if (newTask.dateAndTime.startTime == null) {
          this.startTimeVisible = false;
          this.completedDateVisible = false;
          this.completedTimeVisible = false;

          this.startTime = new Date(new Date().setHours(0, 0, 0, 0));
        } else {
          if (newTask.dateAndTime.startTime.getHours() == 0 && newTask.dateAndTime.startTime.getMinutes() == 0) {
            this.startTimeVisible = false;
            this.startTime = new Date(newTask.dateAndTime.startTime.setHours(0, 0, 0, 0));
          } else {
            this.startTimeVisible = true;
            this.startTime = new Date(newTask.dateAndTime.startTime.setSeconds(0, 0));
          }

          if (newTask.dateAndTime.completedTime !== null) {
            if (newTask.dateAndTime.completedTime.getHours() == 23 && newTask.dateAndTime.completedTime.getMinutes() == 59) {
              this.completedDateVisible = true;
              this.completedTimeVisible = false;

              this.completedTime = new Date(newTask.dateAndTime.completedTime.setHours(23, 59, 59, 999));
            } else {
              this.completedDateVisible = true;
              this.completedTimeVisible = true;
              this.completedTime = new Date(newTask.dateAndTime.completedTime.setSeconds(0, 0));
            }
          } else {
            this.completedDateVisible = false;
            this.completedTimeVisible = false;
          }
        }

        this.repeat = newTask.repeat;
      },
      deep: true
    },
    startTime(newVal) {
      if (!this.completedDateVisible) {
        if (this.completedTimeVisible) {
          let copy = new Date(this.completedTime);
          this.$set(this, 'completedTime', new Date(newVal.getFullYear(), newVal.getMonth(), newVal.getDate(), copy.getHours(), copy.getMinutes()));
        } else {
          this.completedTime = null;
        }
      }
    },
    startTimeVisible(newVal) {
      if (!newVal) {
        let copyStartTime = new Date(this.startTime);
        this.startTime = new Date(copyStartTime.setHours(0, 0, 0, 0));

        if (this.completedDateVisible) {
          let copyCompletedTime = new Date(this.completedTime);
          this.completedTime = new Date(copyCompletedTime.setHours(23, 59, 59, 999));
        } else {
          this.completedTime = null;
        }
        this.completedTimeVisible = false;
      } else {
        let copy = new Date();
        this.$set(this, 'startTime', new Date(this.startTime.getFullYear(), this.startTime.getMonth(), this.startTime.getDate(), copy.getHours(), copy.getMinutes()));
      }
    },
    completedDateVisible(newVal) {
      if(newVal) {
        if(!this.completedTimeVisible) {
          let copy = new Date(this.startTime);
          this.completedTime = new Date(copy.setHours(23,59,59,999));
        }else {
          let copy = new Date(this.completedTime);
          this.$set(this, 'completedTime', new Date(this.startTime.getFullYear(), this.startTime.getMonth(), this.startTime.getDate(), copy.getHours(), copy.getMinutes()));
        }
      }else {
        if(this.completedTimeVisible) {
          let copy = new Date(this.completedTime);
          this.$set(this, 'completedTime', new Date(this.startTime.getFullYear(), this.startTime.getMonth(), this.startTime.getDate(), copy.getHours(), copy.getMinutes()));
        }else {
          this.completedTime = null;
        }
      }
    },
    completedTimeVisible(newVal) {
      if(newVal) {
        if(this.completedDateVisible) {
          let copy = new Date(this.completedTime);
          if(this.startTimeVisible) {
            copy.setHours(this.startTime.getHours(), this.startTime.getMinutes(), 0, 0);
          }else {
            const current = new Date();
            copy.setHours(current.getHours(), current.getMinutes(), 0, 0);
          }
          this.$set(this, 'completedTime', new Date(copy.getTime() + 30 * 60000));
        }else {
          if(this.startTimeVisible) {
            this.$set(this, 'completedTime', new Date(this.startTime.getTime() + 30 * 60000))
          }else {
            const current = new Date()
            this.$set(this, 'completedTime', new Date(current.getTime() + 30 * 60000))
          }
        }
        this.startTimeVisible = true;
      }else {
        if (this.completedDateVisible) {
          let copy = new Date(this.completedTime)
          this.completedTime = new Date(copy.setHours(23, 59, 59, 999))
        } else {
          this.completedTime = null
        }
      }
    },
  },

  computed: {
    timeDiff() {
      return this.completedTimeVisible ? (this.completedTime - this.startTime) / 60000 : '';
    },
    formattedTimeDiff() {
      const temp = this.timeDiff;
      const dayDiff = Math.floor(temp / 1440);
      const hourDiff = Math.floor((temp % 1440) / 60);
      const minDiff = temp % 60;

      if(temp <= 120) {
        return '';
      }else if(temp < 1440) {
        if(minDiff === 0) {
          return `${hourDiff}小时`;
        }else {
          return `${hourDiff}小时${minDiff}分钟`;
        }
      }else {
        if(hourDiff === 0) {
          if(minDiff === 0) {
            return `${dayDiff}天`;
          }else {
            return `${dayDiff}天${minDiff}分钟`;
          }
        }else {
          if (minDiff === 0) {
            return `${dayDiff}天${hourDiff}小时`;
          } else {
            return `${dayDiff}天${hourDiff}小时${minDiff}分钟`;
          }
        }
      }
    },
    timeSpentBtnDisabled() {
      return !this.completedTimeVisible;
    },
    showDateDeleteButton() {
      if(this.task.dateAndTime !== undefined) {
        return this.task.dateAndTime.startTime !== null;
      }
    },
    repeatDialogTitle() {
      if(this.validateTime()) {
        if(this.completedDateVisible || this.completedTimeVisible) {
          return `开始: ${this.formatDate(this.startTime)} ~ 完成: ${this.formatDate(this.completedTime)}`;
        }
        return `开始: ${this.formatDate(this.startTime)}`;
      }
      return '';
    },
  },

  methods: {
    validateTime() {
      if(this.timeDiff < 0 || (this.completedTime !== null && this.completedTime < this.startTime)) {
        this.$message({
          message: '完成时间需大于开始时间',
          type: "warning"
        })
        return false;
      }else {
        return true;
      }
    },
    handlePlanTime(plan) {
      this.$set(this, 'completedTime', new Date(this.startTime.getTime() + plan * 60000));
    },
    handleClose() {
      if(this.validateTime()) {
        this.$confirm("确认为该任务添加日期, 时间吗?", "确认", {
          confirmButtonText: "确认",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {

        }).catch(() => {
          this.dialogVisible = false;
          this.$emit('input', this.dialogVisible);
        })



        this.task.dateAndTime.startTime = this.startTime;
        this.task.dateAndTime.completedTime = this.completedTimeVisible || this.completedDateVisible ? this.completedTime : null;
      }
    },
    handleOpenRepeatDialog() {
      if(this.validateTime()) {
        this.repeatDialogVisible = true;
      }else {
        this.repeatDialogVisible = false;
      }
    },
    handleDateDelete() {

    },
  },
}
</script>

<style scoped>
.spend-time-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  grid-template-rows: repeat(2, 1fr);
  gap: 8px;
}

.spend-time-padding {
  padding: 6px 10px;
}

.spend-time-padding + .spend-time-padding {
  margin-left: 0;
}
</style>
