<template>
  <div>
    <el-row v-for="(tasks, date) in taskList" :key="date" style="margin-bottom: 2px">
      <h3 style="margin: 8px 0">{{ date }}</h3>

      <ul class="task-ul">
        <li v-for="(task, index) in tasks" :key="task.id" class="mb5">
          <div class="task">
            <task-status-item ref="taskStatusItem" :task="task" style="margin: 2px"></task-status-item>

            <div class="task-detail">
              <div class="task-edit" @click="startEditing(task, date, index)" v-show="!task.editing">

                <span style="display: inline-block; padding-top: 2px">{{ task.taskName }}</span>

                <el-row class="mb5">
                  <el-tag class="tag-group"
                          v-for="tag in task.tags"
                          :key="tag.tagId"
                  >{{ tag.tagName }}
                  </el-tag>
                </el-row>
                <el-row v-if="task.taskStartTime" class="mb5">
                  <span>开始：{{ formatDate(task.taskStartTime) }}</span>
                  <span v-if="task.taskCompletedTime"> ~ </span>
                  <span v-if="task.taskCompletedTime">完成：{{
                      formatDate(task.taskCompletedTime)
                    }}</span>
                </el-row>
                <el-row v-if="task.taskRepeatId!==null" v-html="formattedRepeatResult(task.repeat)"
                        class="mb5"></el-row>
                <el-row v-if="task.taskPriority!=='0'">
                  <i v-for="starCount in Number(task.taskPriority)" :key="starCount" class="el-icon-star-on"></i>
                </el-row>
              </div>

              <div class="input-and-settings mb5" v-show="task.editing">
                <el-input
                  :id="`task_input_${date}_${index}`"
                  v-model="task.taskName"
                  ref="taskInputs"
                  @blur="inputBlur(task)"
                  @keyup.enter.native="$event.target.blur"
                  @change="taskNameInputChange(task)"
                ></el-input>
                <el-row v-if="showSettings" class="settings-row">
                  <el-tooltip content="标签" placement="bottom-start">
                    <el-button
                      class="setting-icon"
                      icon="el-icon-discount setting-click"
                      @click="openTagDialog(task)"
                    ></el-button>
                  </el-tooltip>

                  <el-tooltip content="日期, 时间" placement="bottom-start">
                    <el-button
                      class="setting-icon"
                      icon="el-icon-time setting-click"
                      @click="openDateAndTimeDialog(task)"
                    ></el-button>
                  </el-tooltip>

                  <el-dropdown @command="handlePriorityCommand(task, $event)" class="priority-style">
                    <el-tooltip content="优先级" placement="bottom-start">
                      <el-button class="setting-icon" icon="el-icon-star-on"></el-button>
                    </el-tooltip>
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item v-for="option in priorityOptions"
                                        :key="option.value"
                                        :command="option.value"
                                        class="setting-click"
                                        @click.native="handleSettingIconClick"
                      >{{ option.label }}
                        <i v-for="starCount in option.value"
                           :key="starCount"
                           class="el-icon-star-on setting-click"
                        ></i>
                      </el-dropdown-item>
                    </el-dropdown-menu>
                  </el-dropdown>
                </el-row>
              </div>
            </div>
          </div>
        </li>
      </ul>
    </el-row>

    <el-button class="add-list-button" @click="addTask">+ 添加事项</el-button>

    <tag-dialog v-model="tagDialogVisible" :task="currentTask" @input="buttonClickedAfterBlur=false"></tag-dialog>

    <date-and-time-dialog :date-and-time-dialog-visible="dateAndTimeDialogVisible"
                          :task="currentTask"
                          :task-bk="taskBk"
                          @dateConfirm="dateAndTimeDialogVisible=false;buttonClickedAfterBlur=false"
                          @dateCancel="dateAndTimeDialogVisible=false;buttonClickedAfterBlur=false"
                          @dateDelete="dateAndTimeDialogVisible=false"
    ></date-and-time-dialog>
  </div>
</template>

<script>
import DateMixin from "../mixins/formatDate";
import RepeatMixin from "../mixins/formatRepeat";
import FormatList from "../mixins/formatList";
import {
  addTask,
  delTask,
  insertTaskPriority,
  listToDoTask,
  updateTaskName,
  updateTaskPriority
} from "../../../api/taskList/taskList";
import TaskStatusItem from "./taskStatusItem.vue";
import TagDialog from "../dialogs/tagDialog.vue";
import DateAndTimeDialog from "../dialogs/dateAndTimeDialog.vue";
import updateExceptDateTimeForRepeatDialog from "../dialogs/updateExceptDateTimeForRepeatDialog.vue";
import deleteTaskForRepeatDialog from "../dialogs/deleteTaskForRepeatDialog.vue";

export default {
  name: 'TaskListItem',
  components: {DateAndTimeDialog, TagDialog, TaskStatusItem},
  mixins: [DateMixin, RepeatMixin, FormatList],

  props: {
    todoList: {
      type: Object
    },
    queryParams: {
      type: Object
    }
  },

  data() {
    return {
      taskList: {},
      currentTask: {},
      taskInputs: [],

      tagDialogVisible: false,
      dateAndTimeDialogVisible: false,

      priorityOptions: [
        {
          value: 3,
          label: '高'
        }, {
          value: 2,
          label: '中'
        }, {
          value: 1,
          label: '低'
        },
      ],

      taskBk: {},

      buttonClickedAfterBlur: false,
    }
  },

  watch: {
    todoList(val) {
      this.taskList = val;
    },
  },

  computed: {
    showSettings() {
      if (this.currentTask.taskName) {
        return this.currentTask.taskName.length !== 0
      }
    },
  },

  methods: {
    getToDoList() {
      listToDoTask(this.queryParams).then(res => {
        this.taskList = this.formattedTaskList(res.data);
      })
    },
    startEditing(task, date, index) {
      this.$set(task, 'editing', true);
      this.currentTask = task;

      this.$nextTick().then(() => {
        const inputElement = document.getElementById(`task_input_${date}_${index}`);
        if (inputElement) {
          inputElement.focus();
        }
      });
    },
    inputBlur(task) {
      task.taskName = task.taskName.trim();
      if (task.taskName === '') {
        if (task.taskRepeatId == null) {
          this.$confirm("确认要删除该任务吗?", "确认", {
            confirmButtonText: "确认",
            cancelButtonText: "取消",
            type: "warning"
          }).then(() => {
            if (task.taskId !== undefined) {
              delTask(task.taskId).then(() => {
                this.$modal.msgSuccess("删除任务成功");
                this.getToDoList();
              }).catch(() => {
              })
            } else {
              this.getToDoList();
            }
          }).catch(() => {
            this.getToDoList();
          })
        } else {
          // 如果是重复任务, 但已经停止重复了, 当成重复任务直接删除
          this.$openDialog(deleteTaskForRepeatDialog)({
            task: task,
            onDone: () => this.getToDoList(),
          })
          this.getToDoList();
        }
      } else {
        let self = this;
        setTimeout(() => {
          if (task.initialTaskName == undefined) {
            // blur之后马上点击setting icon, 这时候taskId还没有存到数据库, 所以各个setting要insert taskId
            if (!this.buttonClickedAfterBlur) {
              addTask(task).then(res => {
                self.$modal.msgSuccess("新增任务成功");
                self.getToDoList();
              })
            }
          }
          task.editing = false;
        }, 300)
      }
    },
    handleSettingIconClick(event) {
      let point = event.target;
      if (point.classList.contains('setting-click')) {
        this.buttonClickedAfterBlur = true;
      }
    },
    taskNameInputChange(task) {
      task.taskName = task.taskName.trim();
      if (task.taskId !== undefined && task.taskName !== '') {
        /* 判断该任务是否是重复任务
        如果是重复任务, 提示仅更新当前日程还是所有日程
         */
        if (task.taskRepeatId == null) {
          updateTaskName(task).then(res => {
            this.$modal.msgSuccess("任务名称修改成功");
            this.getToDoList();
          })
        } else {
          this.$openDialog(updateExceptDateTimeForRepeatDialog)({
            task: task,
            onDone: () => this.getToDoList(),
          })
          this.getToDoList();
        }
      }
    },
    addTask() {
      let date = new Date()
      date.setHours(date.getHours() + 8)
      date = date.toISOString().slice(0, 10);
      const newTask = {
        taskStatus: '0',
        taskName: '',
        editing: true,
        tags: [],
        taskStartTime: null,
        taskCompletedTime: null,
        taskPriority: '0',
        repeat: {repeatValue: null, endRepeat: null, endRepeatDate: null, customResult: {num: null, frequencyValue: null, selectedItem: null}}
      }

      if(this.taskList[date]) {
        this.taskList[date].push(newTask);
      }else {
        this.taskList[date] = [newTask];
      }

      this.currentTask = newTask;

      this.$nextTick(() => {
        const inputElements = this.$refs.taskInputs
        const lastIndex = inputElements.length - 1
        const lastInputElement = inputElements[lastIndex]
        lastInputElement.$refs.input.focus();
      })
    },
    handleOutsideClick(event) {
      let point = event.target;
      if (!point.classList.contains('circle-button')) {
        while (point.parentElement !== null) {
          point = point.parentElement;
          if (point.classList.contains('circle-button')) {
            // 点击的是circle button的选项
            return;
          }
        }

        //执行到这里
        //说明点的是outSide
        //处理side的的事件
        this.$refs.taskStatusItem.forEach(item => {
          item.hideDropdown();
          this.buttonClickedAfterBlur = false;
        })
      }
    },
    openTagDialog(task) {
      this.tagDialogVisible = true;
      this.currentTask = task;
    },
    openDateAndTimeDialog(task) {
      this.dateAndTimeDialogVisible = true;
      this.currentTask = task;
      this.taskBk = Object.assign({}, task);
    },
    handlePriorityCommand(task, command) {
      task.taskPriority = command;
      if (task.taskRepeatId == null) {
        if (task.taskId == undefined) {
          insertTaskPriority(task).then(res => {
            this.$modal.msgSuccess("已添加任务并更新优先级");
            this.getToDoList();
          })
        } else {
          updateTaskPriority(task).then(res => {
            this.$modal.msgSuccess("任务优先级修改成功");
            this.getToDoList();
          })
        }
      } else {
        this.$openDialog(updateExceptDateTimeForRepeatDialog)({
          task: task,
          onDone: () => this.getToDoList(),
        })
        this.getToDoList();
      }
    },
  },

  mounted() {
    document.addEventListener("click", this.handleOutsideClick);
    document.addEventListener("click", this.handleSettingIconClick);
  },

  beforeDestroy() {
    document.removeEventListener("click", this.handleOutsideClick);
    document.removeEventListener("click", this.handleSettingIconClick);
  },
}
</script>

<style scoped>
.task-ul {
  list-style: none;
  margin: 0;
  padding-left: 10px;
}

.task {
  display: flex;
  gap: 4px;
  align-items: start;
}

.task-edit {
  cursor: pointer;
}

.tag-group {
  margin: 3px;
  height: 20px;
  line-height: 20px;
}

.input-and-settings {
  display: flex;
  flex-direction: column;
}

.settings-row {
  display: flex;
  align-items: center;
  margin-top: 5px;
}

.setting-icon {
  border: none;
  padding: 0 0;
  background: none;
}

.priority-style {
  margin-left: 10px;
  display: flex;
}

.add-list-button {
  border: none;
  color: #000;
}

.add-list-button:hover,
.add-list-button:active,
.add-list-button:focus {
  color: #1890ff;
  background-color: #FFFFFF;
}
</style>
