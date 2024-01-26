<template>
  <div>
  <el-row v-for="(tasks, date) in taskList" :key="date" style="margin-bottom: 2px">
    <h3 style="margin: 8px 0">{{ date }}</h3>

    <ul class="task-ul">
      <li v-for="task in tasks" :key="task.id" class="mb5">
        <div class="task">
          <task-status-item ref="taskStatusItem" :task="task" style="margin: 2px"></task-status-item>

          <div>
            <span style="display: inline-block; padding-top: 2px">{{ task.taskName }}</span>

            <el-row class="mb5">
              <el-tag class="tag-group"
                      v-for="tag in task.tags"
                      :key="tag.tagId"
              >{{ tag.tagName }}</el-tag>
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
        </div>
      </li>
    </ul>
  </el-row>
  </div>
</template>

<script>
import DateMixin from "../mixins/formatDate";
import RepeatMixin from "../mixins/formatRepeat";
import FormatList from "../mixins/formatList";
import {listDoneTask} from "../../../api/taskList/taskList";
import TaskStatusItem from "./taskStatusItem.vue";

export default {
  name: 'TaskListDoneItem',
  mixins: [DateMixin, RepeatMixin, FormatList],
  components: {TaskStatusItem},

  props: {
    type: {
      type: String,
    },
    queryParams: {
      type: Object
    }
  },

  data() {
    return {
      taskType: '',
      taskList: {},
    }
  },

  created() {
    this.taskType = this.type;
    this.getTaskList();
  },

  watch: {
    type(val) {
      this.taskType = val;
    },
  },

  methods: {
    getTaskList() {
      if(this.taskType == '2') {
        listDoneTask(this.queryParams).then(res => {
          this.taskList = this.formattedTaskList(res.data);
        })
      }
    },
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

.tag-group {
  margin: 3px;
  height: 20px;
  line-height: 20px;
}
</style>
