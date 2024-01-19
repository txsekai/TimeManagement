<template>
  <div class="app-container">
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

    <div class="card-container grid">
      <el-card>
        <div>To Do</div>
        <div class="SeparationOfRepetition">
          <task-list-item :todo-list="todoList" :query-params="queryParams"></task-list-item>
        </div>
      </el-card>

      <el-card>
      </el-card>

      <el-card>
      </el-card>
    </div>
  </div>
</template>

<script>
import DictSelect from "./components/dictSelect.vue";
import TaskListItem from "./components/taskListItem.vue";
import FormatList from "./mixins/formatList";
import {listToDoTask} from "../../api/taskList/taskList";

export default {
  name: 'List',
  mixins: [FormatList],
  components: {TaskListItem, DictSelect},

  data() {
    return {
      queryParams: {
        taskName: '',
        tagName: '',
        taskPriority: ''
      },
      todoList: [],
    }
  },

  created() {
    this.getToDoList();
  },

  methods: {
    getToDoList() {
      listToDoTask(this.queryParams).then(res => {
        this.todoList = this.formattedToDoList(res.data);
      })
    },
    handleQuery() {
      this.getToDoList();
    },
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery();
    },
  }
}
</script>

<style scoped>
.card-container {
  margin: 0 auto;
  height: 100%;
}

.grid {
  display: grid;
  column-gap: 48px;
  grid-template-columns: repeat(3, 1fr);
}

</style>
