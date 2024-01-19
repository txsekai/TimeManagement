<template>
  <base-dialog
    title="提示"
    :visible.sync="visible"
    width="20%"
    append-to-body
  >
    <el-row>
      <i class="el-icon-warning warning-icon-style"></i>
      <span>该日程为重复任务, 请问仅删除当前任务还是删除所有未来任务?</span>
    </el-row>
    <template v-slot:footerButton>
      <el-button @click="handleCurrentTask">仅删除当前任务</el-button>
      <el-button @click="handleAllTasks">删除未来所有任务</el-button>
    </template>
  </base-dialog>
</template>

<script>
import {deleteTemplateTaskForRepeat, delTask} from "../../../api/taskList/taskList";

export default {
  name: 'deleteTaskForRepeatDialog',
  props: {
    task: {
      type: Object
    },
  },

  data() {
    return {
      visible: false,
    }
  },

  methods: {
    handleCurrentTask() {
      this.$lockSubmit();
      delTask(this.task.taskId).then(res => {
        this.$closeDialog(true);
        this.$modal.msgSuccess("已删除当前任务");
      }).finally(() => this.$unlockSubmit())
    },
    handleAllTasks() {
      this.$lockSubmit();
      deleteTemplateTaskForRepeat(this.task).then(res => {
        this.$closeDialog(true);
        this.$modal.msgSuccess("已删除未来所有任务");
      }).finally(() => this.$unlockSubmit())
    }
  }
}
</script>

<style scoped lang="scss">
.warning-icon-style {
  color: #e6a23c;
  font-size: 18px !important;
  padding-right: 5px;
}
</style>
