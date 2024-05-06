<template>
  <div>
    <el-row v-for="msg in msgInfoList" :key="msg.msgId">
      <el-row v-show="msg.isAssistantMsg==1" class="sing-msg assistant-msg mb20">
        <img src="../../../assets/images/plan_picture.webp" class="msg-avatar">
        <div class="show-msg-content assistant-content" style="white-space: pre-wrap;">
          {{ msg.msgContent }}
          <el-divider></el-divider>
          <el-tooltip content="创建任务" placement="bottom-start">
            <el-button class="show-create-task-btn" icon="el-icon-tickets" @click="handleCreateTask(msg)"></el-button>
          </el-tooltip>
        </div>
      </el-row>

      <el-row v-show="msg.isAssistantMsg==0" class="sing-msg user-msg mb20">
        <div class="show-msg-content user-content" style="white-space: pre-wrap;">{{ msg.msgContent }}</div>
        <img :src="avatar" class="msg-avatar">
      </el-row>
    </el-row>
  </div>
</template>

<script>
import {mapGetters} from "vuex";
import {handleAssistantMsgToTask} from "../../../api/taskList/taskList";

export default {
  name: 'SingMsg',

  props: {
    msgInfoList: {
      type: Array
    }
  },

  data() {
    return {

    }
  },

  computed: {
    ...mapGetters([
      'avatar',
    ]),
  },

  methods: {
    handleCreateTask(msg) {
      this.$confirm("确认根据该内容来创建任务吗?", "确认", {
        confirmButtonText: "确认",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        handleAssistantMsgToTask(msg).then(() => {
          this.$modal.msgSuccess("成功返回");
          // this.$bus.$emit('call-handle-query');
        })
      }).catch(() => {})
    },
  }
}
</script>

<style scoped lang="scss">
.sing-msg {
  display: flex;
  gap: 10px;
  text-align: left;
}

.assistant-msg {
  justify-content: flex-start;
}

.msg-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}

.user-msg {
  justify-content: flex-end;
}

.show-msg-content {
  padding: 8px;
  border-radius: 8px;
}

.assistant-content {
  background-color: rgba(0, 0, 0, 0.05);
}

.user-content {
  background-color: #e8f4ff;
  color: #1890ff;
}

.show-create-task-btn {
  border: none;
  padding: 0 10px;
  background: none;
  font-size: 16px;
}
</style>
