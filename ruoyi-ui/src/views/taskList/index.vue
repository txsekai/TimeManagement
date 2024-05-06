<template>
  <div class="app-container">
    <div class="wrapper-contain">
      <msg-card class="item"></msg-card>

      <div class="item">
        <el-form
          :model="queryParams"
          ref="queryForm"
          size="small"
          :inline="true"
        >
          <el-form-item label="任务名称" prop="taskName">
            <el-input
              v-model="queryParams.taskName"
              placeholder="请输入任务名称"
              clearable
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            ></el-input>
          </el-form-item>
          <el-form-item label="标签名称" prop="tagName">
            <el-input
              v-model="queryParams.tagName"
              placeholder="请输入标签名称"
              clearable
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            ></el-input>
          </el-form-item>
          <el-form-item label="任务优先级" prop="taskPriority">
            <dict-select type="task_priority" v-model="queryParams.taskPriority"
                         placeholder="请选择"></dict-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <div class="card-container card-grid">
          <el-card class="task-card">
            <h3 style="margin: 10px 0">待办</h3>
            <task-list-item ref="taskTodoListItem" :type="todo" :query-params="queryParams"></task-list-item>
          </el-card>

          <el-card class="task-card">
            <h3 style="margin: 10px 0">正在处理</h3>
            <task-list-item ref="taskDoingListItem" :type="doing" :query-params="queryParams"></task-list-item>
          </el-card>

          <el-card class="task-card">
            <h3 style="margin: 10px 0">已完成</h3>
            <task-list-done-item ref="taskDoneListItem" :type="done" :query-params="queryParams"></task-list-done-item>
          </el-card>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import DictSelect from "./components/dictSelect.vue";
import TaskListItem from "./components/taskListItem.vue";
import FormatList from "./mixins/formatList";
import bus from "./eventBus/bus";
import TaskListDoneItem from "./components/taskListDoneItem.vue";
import MsgCard from "./components/msgCard.vue";

export default {
  name: 'List',
  mixins: [FormatList],
  components: {MsgCard, TaskListDoneItem, TaskListItem, DictSelect},

  data() {
    return {
      queryParams: {
        taskName: '',
        tagName: '',
        taskPriority: ''
      },
      todo: "0",
      doing: "1",
      done: "2",
    }
  },

  mounted() {
    bus.$on('call-handle-query', this.handleQuery);
  },

  methods: {
    handleQuery() {
      this.$refs.taskTodoListItem.getTaskList();
      this.$refs.taskDoingListItem.getTaskList();
      this.$refs.taskDoneListItem.getTaskList();
    },
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery();
    },
  }
}
</script>

<style scoped>
.wrapper-contain {
  display: grid;
  column-gap: 30px;
  grid-template-columns: 1fr 2fr;
}

.item:first-child {
  height: calc(100vh - 200px);
  grid-column: 1;
  max-width: 100%;
  word-break: break-word;
}

.item:last-child {
  height: calc(100vh - 140px);
}

.card-container {
  margin: 0 auto;
}

.card-grid {
  display: grid;
  column-gap: 20px;
  grid-template-columns: repeat(3, 1fr);
}

.task-card {
  height: calc(100vh - 170px);
  overflow-y: auto;
}

</style>
