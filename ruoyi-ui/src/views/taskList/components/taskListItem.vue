<template>
  <div>
    <el-row v-for="(task, index) in taskList" :key="'taskList_'+index" style="margin-bottom: 10px">
      <div class="task">
        <task-status-item ref="taskStatusItem" :task="task" style="margin: 2px"></task-status-item>

        <div class="task-detail">
          <div class="task-edit" @click="startEditing(task, index)" v-show="!task.editing">

            <span style="display: inline-block; padding-top: 2px">{{ task.taskName }}</span>

            <el-row>
              <el-tag class="tag-group"
                      v-for="tag in task.tags"
                      :key="tag.tagId"
              >{{ tag.tagName }}
              </el-tag>
            </el-row>

            <el-row v-if="task.taskStartTime">
              <span>开始：{{ formatDate(task.taskStartTime) }}</span>
              <span v-if="task.taskCompletedTime"> ~ </span>
              <span v-if="task.taskCompletedTime">完成：{{
                  formatDate(task.taskCompletedTime)
                }}</span>
            </el-row>
            <el-row v-if="task.taskRepeatId!==null" v-html="formattedRepeatResult(task.repeat)"></el-row>
            <el-row v-if="task.taskPriority!=='0'">
              <i v-for="starCount in task.taskPriority" :key="starCount" class="el-icon-star-on"></i>
            </el-row>
          </div>

          <div class="input-and-settings" v-show="task.editing">
            <el-input
              :id="'task_input_' + index"
              v-model="task.taskName"
              ref="taskInputs"
              @blur="inputBlur(task)"
              @change="taskNameInputChange(task)"
            ></el-input>
            <el-row v-if="showSettings" class="settings-row">
              <el-tooltip content="标签" placement="bottom-start">
                <el-button
                  class="setting-icon"
                  icon="el-icon-discount"
                  @click="openTagDialog(task)"
                ></el-button>
              </el-tooltip>

              <el-tooltip content="日期, 时间" placement="bottom-start">
                <el-button
                  class="setting-icon"
                  icon="el-icon-time"
                  @click="openDateAndTimeDialog(task)"
                ></el-button>
              </el-tooltip>
            </el-row>
          </div>
        </div>
      </div>
    </el-row>

    <el-button class="add-list-button" @click="addTask">+ 添加事项</el-button>

    <tag-dialog v-model="tagDialogVisible" :task="currentTask"></tag-dialog>

    <date-and-time-dialog :date-and-time-dialog-visible="dateAndTimeDialogVisible"
                          :task="currentTask"
                          @dateConfirm="dateAndTimeDialogVisible=false"
                          @dateCancel="dateAndTimeDialogVisible=false"></date-and-time-dialog>
  </div>
</template>

<script>
import DateMixin from "../mixins/formatDate";
import RepeatMixin from "../mixins/formatRepeat";
import FormatList from "../mixins/formatList";
import {addTask, delTask, listToDoTask, updateTaskName} from "../../../api/taskList/taskList";
import TaskStatusItem from "./taskStatusItem.vue";
import TagDialog from "../dialogs/tagDialog.vue";
import DateAndTimeDialog from "../dialogs/dateAndTimeDialog.vue";

export default {
  name: 'TaskListItem',
  components: {DateAndTimeDialog, TagDialog, TaskStatusItem},
  mixins: [DateMixin, RepeatMixin, FormatList],

  props: {
    todoList: {
      type: Array
    },
    queryParams: {
      type: Object
    }
  },

  data() {
    return {
      taskList: [],
      currentTask: {},
      taskInputs: [],

      tagDialogVisible: false,
      dateAndTimeDialogVisible: false,
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
    }
  },

  methods: {
    getToDoList() {
      listToDoTask(this.queryParams).then(res => {
        this.taskList = this.formattedToDoList(res.data);
      })
    },
    startEditing(task, index) {
      this.$set(task, 'editing', true);
      this.currentTask = task;

      this.$nextTick().then(() => {
        const inputElement = document.getElementById(`task_input_${index}`);
        if (inputElement) {
          inputElement.focus();
        }
      });
    },
    inputBlur(task) {
      if (task.taskName === '' || task.taskName === undefined) {
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
        setTimeout(() => {
          if (task.initialTaskName == undefined) {
            addTask(task).then(res => {
              this.$modal.msgSuccess("新增任务成功");
            })
          }
          task.editing = false;
          // bug 新增任务然后打卡dateAndTime dialog, 然后关闭, 页面上没有新增的这条任务,要刷新
          this.getToDoList();
        }, 300)
      }
    },
    taskNameInputChange(task) {
      if (task.taskId !== undefined && task.taskName !== '') {
        updateTaskName(task).then(res => {
          this.$modal.msgSuccess("任务名称修改成功");
          this.getToDoList();
        })
      }
    },
    addTask() {
      const newTask = {
        taskStatus: '0',
        taskName: '',
        editing: true,
        tags: [],
        taskStartTime: null,
        taskCompletedTime: null,
        taskPriority: '',
        repeat: {repeatValue: null, endRepeat: null, endRepeatDate: null, customResult: {}}
      }
      this.taskList.push(newTask);
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
    },
  },

  mounted() {
    document.addEventListener("click", this.handleOutsideClick);
  },

  beforeDestroy() {
    document.removeEventListener("click", this.handleOutsideClick);
  },
}
</script>

<style scoped>
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
