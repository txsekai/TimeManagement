<template>
  <base-dialog
    title="提示"
    :visible.sync="visible"
    width="20%"
    append-to-body
  >
    <el-row>
      <i class="el-icon-warning warning-icon-style"></i>
      <span>该日程为重复日程, 更新日期, 时间或者重复设置将更新所有未来日程, 确定更新吗?</span>
    </el-row>
    <template v-slot:footerButton>
      <el-button @click="handleAllTasks">更新所有未来日程</el-button>
    </template>
  </base-dialog>
</template>

<script>
import {updateDateTimeAndRepeatForRepeat} from "../../../api/taskList/dateAndTime";

export default {
  name: 'updateDateTimeRepeatForRepeatDialog',
  props: {
    task: {
      type: Object
    },
  },

  data() {
    return {
      visible:false,
    }
  },

  methods: {
    handleAllTasks() {
      this.$lockSubmit();
      updateDateTimeAndRepeatForRepeat(this.task).then(res => {
        this.$closeDialog(true);
        this.$modal.msgSuccess("已更新未来所有日程");
      }).finally(() => this.$unlockSubmit())
    },
  }
}
</script>

<style scoped>
.warning-icon-style {
  color: #e6a23c;
  font-size: 18px !important;
  padding-right: 5px;
}
</style>
