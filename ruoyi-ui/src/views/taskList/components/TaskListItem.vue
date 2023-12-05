<template>
  <div>
    <el-row v-for="(task, index) in taskList" :key="index">
      <div class="task">
        <input type="checkbox" v-model="task.taskStatus" style="margin: 2px"/>
        <div class="task-detail">
          <template v-if="!task.editing">
            <div class="task-edit" @click="startEditing(task, index)">
              <span>{{ task.taskName }}</span>
              <el-row>
                <el-tag class="tag-group"
                        v-for="tag in task.tags"
                        :key="tag.tagId"
                >{{ tag.tagName }}
                </el-tag>
              </el-row>
              <el-row v-if="task.dateAndTime.startTime">
                <span>开始：{{ formatDate(task.dateAndTime.startTime) }}</span>
                <span v-if="task.dateAndTime.completedTime"> ~ </span>
                <span v-if="task.dateAndTime.completedTime">完成：{{
                    formatDate(task.dateAndTime.completedTime)
                  }}</span>
              </el-row>
              <el-row v-html="formattedRepeatResult(task.repeat)"></el-row>
              <el-row v-if="task.taskPriority!=='0'">
                <i v-for="starCount in task.taskPriority" :key="starCount" class="el-icon-star-on"></i>
              </el-row>
            </div>
          </template>
          <template v-else>
            <div class="input-and-settings">
              <el-input
                :id="'task_input_' + index"
                v-model="task.taskName"
                ref="taskInputs[index]"
                @blur="inputBlur(task, index)"
              ></el-input>
            </div>
          </template>
        </div>
      </div>
    </el-row>
  </div>
</template>

<script>
import DateMixin from "../mixins/formatDate";
import RepeatMixin from "../mixins/formatRepeat";
import {delTask} from "../../../api/taskList/taskList";

export default {
  name: 'TaskListItem',
  mixins: [DateMixin, RepeatMixin],

  props: {
    todoList: {
      type: Array
    }
  },

  data() {
    return {
      taskList: [],
      currentTask: {},
      taskInputs: [],
    }
  },

  watch: {
    todoList(val) {
      for (let row of val) {
        row.dateAndTime = {startTime: new Date(row.taskStartTime), completedTime: new Date(row.taskCompletedTime)};
        if (row.tags.length > 0) {
          row.tags = row.tags.map((tagRow) => {
            return {tagId: tagRow.tagId, tagName: tagRow.tagData.tagName};
          })
        }
        row.repeat.customResult = {
          num: row.repeat.num,
          frequencyValue: row.repeat.frequencyValue,
          selectedItem: row.repeat.selectedItem
        };
        row.repeat = {
          repeatValue: row.repeat.repeatValue,
          endRepeat: row.repeat.endRepeat,
          endRepeatDate: row.repeat.endRepeatDate,
          customResult: row.repeat.customResult
        };
        this.taskInputs.push(row.taskId - 1);
      }
      this.taskList = val;
    },
  },

  methods: {
    startEditing(task, index) {
      task.editing = true;
      this.currentTask = task;

      this.$nextTick(() => {
        // document.getElementById('task_input_' + index).focus();
        this.$refs.taskInputs[index].focus();
      })
    },
    inputBlur(task, index) {
      if (task.taskName === '' || task.taskName === undefined) {
        this.$confirm("确认要删除该任务吗?", "确认", {
          confirmButtonText: "确认",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {

        })
      }
    },
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
  margin-left: 3px;
  margin-bottom: 5px;
  height: 20px;
  line-height: 20px;
}

.input-and-settings {
  display: flex;
  flex-direction: column;
}
</style>
