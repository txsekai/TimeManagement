<template>
  <div>
    <el-card class="msg-card">
      <h3 style="margin: 10px 0">任务计划助手</h3>

      <sing-msg class="msg-content" :msg-info-list="msgInfoList"></sing-msg>
    </el-card>

    <div class="mgs-footer">
      <div class="textarea-container">
        <el-input
          type="textarea"
          autosize
          maxlength="700"
          v-model="enterMsgContent"
          placeholder="请输入内容  (Shift + Enter 换行)"
          @keydown.native="handleKeyDown"
        >
        </el-input>
        <i class="send-icon el-icon-position" @click="handleSendMsg"></i>
      </div>
    </div>
  </div>
</template>

<script>
import {addUserMsg, assistantProcess, getAssistantCatch, msgInfo} from "../../../api/taskList/taskList";
import SingMsg from "./singMsg.vue";

export default {
  name: 'MsgCard',
  components: {SingMsg},

  data() {
    return {
      msgInfoList: [],
      enterMsgContent: "",
      timerInterval: null,
    }
  },

  created() {
    this.getMsgInfo();
  },

  methods: {
    getMsgInfo() {
      msgInfo().then(res => {
        this.msgInfoList = res.data;
      })
    },
    insertUserSendMsg(msg) {
      addUserMsg(msg).then(() => {
        // 请求assistant的处理
        // this.handleAssistantProcess(msg);

        clearInterval(this.timerInterval);

        // check AI助手有没有返回
        this.timerInterval = setInterval(() => {
          this.checkAssistantCatch();
        }, 20000);
      }).catch(() => {
      }).finally(() => {
        this.getMsgInfo();
      })
    },
    handleAssistantProcess(msg) {
      assistantProcess(msg).then(() => {
      }).catch(() => {
      }).finally(() => {
      })
    },
    checkAssistantCatch() {
      getAssistantCatch().then((res) => {
        if (res.data) {
          this.getMsgInfo();

          clearInterval(this.timerInterval);
        }
      }).catch(() => {
      }).finally(() => {

      })
    },
    handleSendMsg() {
      this.enterMsgContent = this.enterMsgContent.trim();

      if (this.enterMsgContent.length !== 0) {

        let msg = {
          msgContent: this.enterMsgContent,
        }

        // 把用户发的消息插入数据库
        this.insertUserSendMsg(msg);

        this.enterMsgContent = "";
      } else {
        this.$message({
          message: '请输入内容',
          type: 'warning'
        });
      }
    },
    handleKeyDown(event) {
      if (event.shiftKey && event.key === 'Enter') {
        event.preventDefault();
        this.enterMsgContent += '\n';
      } else if (event.key === 'Enter') {
        event.preventDefault();
        this.handleSendMsg();
      }
    },
    scrollToBottom() {
     this.$nextTick(() => {
       let element = this.$el.querySelector('.msg-card');
       element.scrollTop = element.scrollHeight;
     })
    }
  },

  watch: {
    msgInfoList() {
      this.scrollToBottom();
    }
  }
}
</script>

<style scoped lang="scss">
.msg-card {
  display: flex;
  height: 100%;
  flex-direction: column;
  text-align: center;
  position: relative;
  margin-bottom: 20px;
  overflow-y: auto;
}

.mgs-footer {
  margin-left: 10px;
  width: calc(100% - 20px);
}

.textarea-container {
  position: relative;
}

.send-icon {
  font-size: 22px;
  cursor: pointer;
  position: absolute;
  top: 50%;
  right: 10px;
  transform: translateY(-50%) rotate(45deg);
  color: #606266;
}

::v-deep textarea {
  resize: none;
  padding-right: 36px;
  min-height: 60px !important;
  max-height: 60px;
}
</style>
