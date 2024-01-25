<template>
  <el-dialog
    title="请设置日期, 时间"
    :visible.sync="dateAndTimeDialogVisible"
    width="30%"
    center
    :show-close="false"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
  >
    <el-row class="mb20">
      <!--  开始日期和开始时间    -->
      <el-col :span="12">
        <span style="display: inline-block; width: 100%;" class="mb5">开始日期</span>
        <date-item class="mb10"
                   v-model="taskStartTime"
                   align="left"
                   :picker-options="pickerOptionsStartDate"
        ></date-item>

        <el-switch
          class="mb5"
          style="width: 100%"
          active-text="开始时间"
          v-model="startTimeVisible"
        ></el-switch>
        <time-item v-show="startTimeVisible" v-model="taskStartTime"></time-item>
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
                   v-model="taskCompletedTime"
                   align="right"
                   :picker-options="pickerOptionsCompletedDate"></date-item>
        <div v-show="!completedDateVisible" style="min-height: 46px"></div>

        <el-switch
          class="mb5"
          style="width: 100%"
          active-text="完成时间"
          v-model="completedTimeVisible"
        ></el-switch>
        <time-item v-show="completedTimeVisible" v-model="taskCompletedTime"></time-item>
      </el-col>
    </el-row>

    <el-row v-if="completedTimeVisible" class="mb10">
      <el-col :span="12">
        <span>所用时长：{{ timeDiff }} 分钟</span>
      </el-col>

      <el-col :span="12"><span>{{ formattedTimeDiff }}</span></el-col>
    </el-row>

    <div class="spend-time-grid mb20">
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
      <el-button class="mb8" @click="handleOpenRepeatDialog">设置重复周期</el-button>
      <el-row v-if="repeat!==null" v-html="formattedRepeatResult(repeat)"></el-row>
    </el-row>

    <div slot="footer" class="dialog-footer">
      <el-button v-show="showDateDeleteButton"
                 @click="handleDateDelete">
        {{ task.taskRepeatId === null ? '删除日期时间' : '删除日期时间以及重复' }}
      </el-button>
      <el-button @click="handleDateConfirm">确认</el-button>
      <el-button @click="handleDateCancel">取消</el-button>
    </div>

    <repeat-dialog :repeat-dialog-visible="repeatDialogVisible"
                   :title="repeatDialogTitle"
                   :repeat-result="repeat"
                   @repeatConfirm="handleRepeatConfirm"
                   @repeatCancel="repeatDialogVisible=false"
    ></repeat-dialog>
  </el-dialog>
</template>

<script>
import DateItem from "../components/dateItem.vue";
import TimeItem from "../components/timeItem.vue";
import RepeatDialog from "./repeatDialog.vue";
import DateMixin from "../mixins/formatDate";
import RepeatMixin from "../mixins/formatRepeat";
import {
  deleteDateAndTimeAndRepeatForRepeat,
  deleteRepeatForRepeat,
  insertDateAndTime,
  insertDateTimeAndRepeat,
  updateDateAndTime,
  updateDateTimeAndRepeat
} from "../../../api/taskList/dateAndTime";
import updateDateTimeRepeatForRepeatDialog from "./updateDateTimeRepeatForRepeatDialog.vue";
import deleteRepeatForRepeatDialog from "./deleteRepeatForRepeatDialog.vue";

export default {
  name: 'DateAndTimeDialog',
  components: {RepeatDialog, TimeItem, DateItem},
  mixins: [DateMixin, RepeatMixin],

  props: {
    dateAndTimeDialogVisible: {
      type: Boolean,
      default: false
    },
    task: {
      type: Object
    },
    taskBk: {
      type: Object
    }
  },

  data() {
    return {
      completedDateVisible: false,
      startTimeVisible: false,
      completedTimeVisible: false,

      taskStartTime: new Date(),
      pickerOptionsStartDate: {
        shortcuts: [{
          text: '今天',
          onClick(picker) {
            picker.$emit('pick', new Date());
          }
        }]
      },

      taskCompletedTime: new Date(),
      pickerOptionsCompletedDate: {
        disabledDate: time => {
          const startOfDay = new Date(time.getFullYear(), time.getMonth(), time.getDate());
          const copyOfStartTime = new Date(this.taskStartTime.getTime());
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
      repeat: {
        repeatValue: null,
        endRepeat: null,
        endRepeatDate: null,
        customResult: {num: null, frequencyValue: null, selectedItem: null}
      },
    }
  },

  watch: {
    task: {
      handler(newTask) {
        if (newTask.taskStartTime == null) {
          this.startTimeVisible = false;
          this.completedDateVisible = false;
          this.completedTimeVisible = false;

          this.taskStartTime = new Date(new Date().setHours(0, 0, 0, 0));
        } else {
          if (newTask.taskStartTime.getHours() == 0 && newTask.taskStartTime.getMinutes() == 0) {
            this.startTimeVisible = false;
            this.taskStartTime = new Date(newTask.taskStartTime.setHours(0, 0, 0, 0));
          } else {
            this.startTimeVisible = true;
            this.taskStartTime = new Date(newTask.taskStartTime.setSeconds(0, 0));
          }

          if (newTask.taskCompletedTime !== null) {
            if (newTask.taskCompletedTime.getHours() == 23 && newTask.taskCompletedTime.getMinutes() == 59) {
              this.completedDateVisible = true;
              this.completedTimeVisible = false;

              this.taskCompletedTime = new Date(newTask.taskCompletedTime.setHours(23, 59, 59, 999));
            } else {
              this.completedDateVisible = true;
              this.completedTimeVisible = true;
              this.taskCompletedTime = new Date(newTask.taskCompletedTime.setSeconds(0, 0));
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
    taskStartTime(newVal) {
      if (!this.completedDateVisible) {
        if (this.completedTimeVisible) {
          let copy = new Date(this.taskCompletedTime);
          this.$set(this, 'taskCompletedTime', new Date(newVal.getFullYear(), newVal.getMonth(), newVal.getDate(), copy.getHours(), copy.getMinutes()));
        } else {
          this.taskCompletedTime = null;
        }
      }
    },
    startTimeVisible(newVal) {
      if (!newVal) {
        let copyStartTime = new Date(this.taskStartTime);
        this.taskStartTime = new Date(copyStartTime.setHours(0, 0, 0, 0));

        if (this.completedDateVisible) {
          let copyCompletedTime = new Date(this.taskCompletedTime);
          this.taskCompletedTime = new Date(copyCompletedTime.setHours(23, 59, 59, 999));
        } else {
          this.taskCompletedTime = null;
        }
        this.completedTimeVisible = false;
      } else {
        let copy = new Date();
        this.$set(this, 'taskStartTime', new Date(this.taskStartTime.getFullYear(), this.taskStartTime.getMonth(), this.taskStartTime.getDate(), copy.getHours(), copy.getMinutes()));
      }
    },
    completedDateVisible(newVal) {
      if (newVal) {
        if (!this.completedTimeVisible) {
          let copy = new Date(this.taskStartTime);
          this.taskCompletedTime = new Date(copy.setHours(23, 59, 59, 999));
        } else {
          let copy = new Date(this.taskCompletedTime);
          this.$set(this, 'taskCompletedTime', new Date(this.taskStartTime.getFullYear(), this.taskStartTime.getMonth(), this.taskStartTime.getDate(), copy.getHours(), copy.getMinutes()));
        }
      } else {
        if (this.completedTimeVisible) {
          let copy = new Date(this.taskCompletedTime);
          this.$set(this, 'taskCompletedTime', new Date(this.taskStartTime.getFullYear(), this.taskStartTime.getMonth(), this.taskStartTime.getDate(), copy.getHours(), copy.getMinutes()));
        } else {
          this.taskCompletedTime = null;
        }
      }
    },
    completedTimeVisible(newVal) {
      if (newVal) {
        if (this.completedDateVisible) {
          let copy = new Date(this.taskCompletedTime);
          if (this.startTimeVisible) {
            copy.setHours(this.taskStartTime.getHours(), this.taskStartTime.getMinutes(), 0, 0);
          } else {
            const current = new Date();
            copy.setHours(current.getHours(), current.getMinutes(), 0, 0);
          }
          this.$set(this, 'taskCompletedTime', new Date(copy.getTime() + 30 * 60000));
        } else {
          if (this.startTimeVisible) {
            this.$set(this, 'taskCompletedTime', new Date(this.taskStartTime.getTime() + 30 * 60000))
          } else {
            const current = new Date()
            this.$set(this, 'taskCompletedTime', new Date(current.getTime() + 30 * 60000))
          }
        }
        this.startTimeVisible = true;
      } else {
        if (this.completedDateVisible) {
          let copy = new Date(this.taskCompletedTime)
          this.taskCompletedTime = new Date(copy.setHours(23, 59, 59, 999))
        } else {
          this.taskCompletedTime = null
        }
      }
    },
  },

  computed: {
    timeDiff() {
      return this.completedTimeVisible ? (this.taskCompletedTime - this.taskStartTime) / 60000 : '';
    },
    formattedTimeDiff() {
      const temp = this.timeDiff;
      const dayDiff = Math.floor(temp / 1440);
      const hourDiff = Math.floor((temp % 1440) / 60);
      const minDiff = temp % 60;

      if (temp <= 120) {
        return '';
      } else if (temp < 1440) {
        if (minDiff === 0) {
          return `${hourDiff}小时`;
        } else {
          return `${hourDiff}小时${minDiff}分钟`;
        }
      } else {
        if (hourDiff === 0) {
          if (minDiff === 0) {
            return `${dayDiff}天`;
          } else {
            return `${dayDiff}天${minDiff}分钟`;
          }
        } else {
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
      return this.task.taskStartTime !== null;
    },
    repeatDialogTitle() {
      if (this.validateTime()) {
        if (this.completedDateVisible || this.completedTimeVisible) {
          return `开始: ${this.formatDate(this.taskStartTime)} ~ 完成: ${this.formatDate(this.taskCompletedTime)}`;
        }
        return `开始: ${this.formatDate(this.taskStartTime)}`;
      }
      return '';
    },
  },

  methods: {
    validateTime() {
      if (this.timeDiff < 0 || (this.taskCompletedTime !== null && this.taskCompletedTime < this.taskStartTime)) {
        this.$message({
          message: '完成时间需大于开始时间',
          type: "warning"
        })
        return false;
      } else {
        return true;
      }
    },
    handlePlanTime(plan) {
      this.$set(this, 'taskCompletedTime', new Date(this.taskStartTime.getTime() + plan * 60000));
    },
    handleRepeatConfirm(repeat) {
      this.repeatDialogVisible = false;
      this.labelFormatHolder.selectedRepeatList = repeat.customResult.selectedItem;
      this.labelFormatHolder.frequencyValue = repeat.customResult.frequencyValue;
      this.repeat = repeat;
    },
    handleDateConfirm() {
      if (this.validateTime()) {
        this.task.taskStartTime = this.taskStartTime;
        this.task.taskCompletedTime = this.completedTimeVisible || this.completedDateVisible ? this.taskCompletedTime : null;
        this.task.repeat = this.repeat;
        if (this.repeat !== null && Object.keys(this.repeat.customResult).length !== 0) {
          this.task.repeat.num = this.repeat.customResult.num;
          this.task.repeat.frequencyValue = this.repeat.customResult.frequencyValue;
          this.task.repeat.selectedItem = this.repeat.customResult.selectedItem !== null ? this.repeat.customResult.selectedItem.join(',') : null;
        }

        /*
        先判断是否已有taskRepeatId, 如果有的话, 就是更新重复: 判断日期,时间,重复是否有变更, 只要一个有变更弹dialog
        如果没有taskRepeatId就是添加重复: 再判断repeatValue !== null && repeatValue !== 'never', 成立的话就是有设置重复; 反之没有设置重复; 判断是否有变更日期, 时间
         */
        if (this.task.taskRepeatId == null) {
          if(this.repeat !== null) {
            if (this.repeat.repeatValue !== null && this.repeat.repeatValue !== 'never') {
              // taskId == undefined => 输入taskName之后马上点击dateAndTimeDialog
              if(this.task.taskId == undefined) {
                insertDateTimeAndRepeat(this.task).then(() => {
                  this.$modal.msgSuccess("成功添加日期, 时间, 重复设置");
                  this.$parent.getTaskList();
                })
              }else {
                updateDateTimeAndRepeat(this.task).then(() => {
                  this.$modal.msgSuccess("成功更新日期, 时间, 重复设置");
                  this.$parent.getTaskList();
                })
              }
            }else if(this.repeat.repeatValue == null || this.repeat.repeatValue == "never"){
              if(this.task.taskId == undefined) {
                insertDateAndTime(this.task).then(() => {
                  this.$modal.msgSuccess("成功添加日期, 时间");
                  this.$parent.getTaskList();
                })
              }else {
                if (this.task.taskStartTime.getTime() !== this.taskBk.taskStartTime?.getTime() ||
                  this.task.taskCompletedTime?.getTime() !== this.taskBk.taskCompletedTime?.getTime()) {
                  updateDateAndTime(this.task).then(() => {
                    this.$modal.msgSuccess("成功更新日期, 时间");
                    this.$parent.getTaskList();
                  })
                }
              }
            }
          } else {
            if(this.task.taskId == undefined) {
              insertDateAndTime(this.task).then(() => {
                this.$modal.msgSuccess("成功添加日期, 时间");
                this.$parent.getTaskList();
              })
            }else {
              if (this.task.taskStartTime.getTime() !== this.taskBk.taskStartTime?.getTime() ||
                this.task.taskCompletedTime?.getTime() !== this.taskBk.taskCompletedTime?.getTime()) {
                updateDateAndTime(this.task).then(() => {
                  this.$modal.msgSuccess("成功更新日期, 时间");
                  this.$parent.getTaskList();
                })
              }
            }
          }
        } else {
          /*
          如果有taskRepeatId, 需要判断是更新repeat还是删除repeat
           */
          if (this.repeat.repeatValue == 'never') {
            this.$openDialog(deleteRepeatForRepeatDialog)({
              task: this.task,
              onDone: () => this.$parent.getTaskList(),
            })
          } else if (this.task.taskStartTime.getTime() !== this.taskBk.taskStartTime?.getTime() ||
            this.task.taskCompletedTime?.getTime() !== this.taskBk.taskCompletedTime?.getTime() ||
            this.task.repeat !== this.taskBk.repeat) {
            this.$openDialog(updateDateTimeRepeatForRepeatDialog)({
              task: this.task,
              onDone: () => this.$parent.getTaskList()
            })
          }
        }
        this.$parent.getTaskList();
        this.$emit("dateConfirm");
      }
    },
    handleDateCancel() {
      this.$parent.getTaskList();
      this.$emit("dateCancel");
    },
    handleOpenRepeatDialog() {
      if (this.validateTime()) {
        this.repeatDialogVisible = true;
      } else {
        this.repeatDialogVisible = false;
      }
    },
    handleDateDelete() {
      let msg = '';
      if(this.task.taskRepeatId == null) {
        msg = "确认要删除该日程的日期, 时间吗？";
      }else {
        msg = "该日程为重复日程, 确认要删除该重复日程所有的日期, 时间以及重复设置吗？";
      }
      this.$confirm(msg, "确认", {
        confirmButtonText: "确认",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.startTime = null;
        this.completedTime = null;
        this.task.taskStartTime = this.startTime;
        this.task.taskCompletedTime = this.completedTime;

        deleteDateAndTimeAndRepeatForRepeat(this.task).then(res => {
          this.$modal.msgSuccess("已成功删除");
          this.$parent.getTaskList();
        })
      }).catch(() => {
      })
      this.$emit("dateDelete");
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
