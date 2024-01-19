<template>
  <base-dialog
    title="提示"
    :visible.sync="visible"
    width="20%"
    append-to-body
  >
    <el-row>
      <i class="el-icon-warning warning-icon-style"></i>
      <span>该日程为重复日程, 请问仅更新当前日程还是更新所有未来日程?</span>
    </el-row>
      <template v-slot:footerButton>
        <el-button @click="handleCurrentTask">仅更新当前日程</el-button>
        <el-button @click="handleAllTasks">更新所有未来日程</el-button>
      </template>
  </base-dialog>
</template>

<script>
import {updateCurrentTaskForRepeat, updateTemplateForRepeat
} from "../../../api/taskList/tag";

export default {
  name: 'updateExceptDateTimeForRepeatDialog',
  props: {
    task: {
      type: Object
    },
    tagId: {
      type: String,
    },
    hasSelectTag: {
      type: String,
      default: '' // 默认为''即没有触发selectTag或deselectTag; 如果是selectTag, 值为'1'; 如果是deselectTag, 值为'0'
    }
  },

  data() {
    return {
      visible:false,
    }
  },

  methods: {
    handleCurrentTask() {
      this.$lockSubmit();
      updateCurrentTaskForRepeat(this.task, this.tagId, this.hasSelectTag).then(res => {
        this.$closeDialog(true);
        this.$modal.msgSuccess("已更新当前日程");
      }).finally(() => this.$unlockSubmit())
    },
    handleAllTasks() {
      this.$lockSubmit();
      updateTemplateForRepeat(this.task, this.tagId, this.hasSelectTag).then(res => {
        this.$closeDialog(true);
        this.$modal.msgSuccess("已更新未来所有日程");
      }).finally(() => this.$unlockSubmit())
    },
  },
}
</script>

<style scoped>
.warning-icon-style {
  color: #e6a23c;
  font-size: 18px !important;
  padding-right: 5px;
}
</style>
